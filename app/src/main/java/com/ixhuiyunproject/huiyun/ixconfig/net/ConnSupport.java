package com.ixhuiyunproject.huiyun.ixconfig.net;

import android.os.SystemClock;

import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.cmd.Data;
import com.ixhuiyunproject.huiyun.ixconfig.net.cmd.JsonPack;
import com.ixhuiyunproject.huiyun.ixconfig.net.container.JsonCommandBuffer;
import com.ixhuiyunproject.huiyun.ixconfig.net.container.PackContainer;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.SystemUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ThreadManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * 局域网连接的支持类
 * 
 * @author Torah
 * 
 */
public class ConnSupport {
	private static final int PORT_FIND_MANSTER = 48899;// 寻找主机的端口
	private static final int PORT_TCP_CONN = 48900;// 寻找主机的端口
	//private static final String UDP_MASTER_RETURN_SUFFIX = "TES";
	private static final String UDP_MASTER_RETURN_SUFFIX = "MAS";
	private String addr_broadcast;
	private DatagramSocket socket;

	public ConnSupport() {
		String myIp = SystemUtils.getWifiIp();
		String front = myIp.substring(0, myIp.lastIndexOf(".") + 1);
		addr_broadcast = front + "255";
		try {
			socket = new DatagramSocket(null);
			socket.setReuseAddress(true);
			socket.bind(new InetSocketAddress(PORT_FIND_MANSTER));
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接收主机返回信号
	 * 
	 * @param listener
	 *            成功后回调
	 */
	public void udpReceiveForMaster(final OnResultListener<InetAddress> listener) {
		new Thread() {
			public void run() {
				try {
					byte[] receive = new byte[64];
					DatagramPacket packet = new DatagramPacket(receive,
							receive.length);
					while (StaticValue.isRunning() && (!StaticValue.isRemote())) {
						socket.receive(packet);
						if (JudgeIsMaster(packet)) {
							listener.onResult(true, packet.getAddress());
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					listener.onResult(false, null);
				} finally {
					if (socket != null)
						socket.close();
				}
			};
		}.start();
	}

	/**
	 * 判断是否是主机
	 * 
	 */
	public boolean JudgeIsMaster(DatagramPacket packet) {
		try {
			String data = new String(packet.getData(), packet.getOffset(),
					packet.getLength());
			LogUtils.i("收到udp:" + data);
			if (data.contains(UDP_MASTER_RETURN_SUFFIX)) {
				return true;
			} else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * udp发送信号寻找主机
	 */
	public void udpSendToFindMaster() {
		final String code = "HF-A11ASSISTHREAD";
		ThreadManager.getShortPool().execute(new Thread() {
			public void run() {
				try {//
					InetAddress addr = InetAddress.getByName(addr_broadcast);

					DatagramPacket packet = new DatagramPacket(code.getBytes(),
							code.getBytes().length, addr, PORT_FIND_MANSTER);
					for (int i = 0; i < 3; i++) {
						SystemClock.sleep(600);
						socket.send(packet);
					}
				} catch (IOException e) {
					e.printStackTrace();
					try {
						socket = new DatagramSocket(null);
						socket.setReuseAddress(true);
						socket.bind(new InetSocketAddress(PORT_FIND_MANSTER));
						NetJsonUtil.getInstance().findMasteByUDP();
					} catch (SocketException e1) {
						e1.printStackTrace();
					}
					
				}
			};
		});
	}

	/**
	 * 创建TCP连接
	 * 
	 * @param masterIp
	 *            主机ip
	 * @param listener
	 *            成功回调
	 */
	public static void createTCPConn(final InetAddress masterIp,
			final OnResultListener<Socket> listener) {
		createTCPConn(masterIp, PORT_TCP_CONN, listener);
	}

	/**
	 * 创建TCP连接
	 * 
	 * @param masterIp
	 * @param port
	 *            指定的端口
	 * @param listener
	 */
	public static void createTCPConn(final InetAddress masterIp,
			final int port, final OnResultListener<Socket> listener) {
		new Thread() {
			@Override
			public void run() {
				SocketAddress socketAddress;
				Socket socket = new Socket();
				try {
					socketAddress = new InetSocketAddress(masterIp, port);
					socket.connect(socketAddress, 5000);
					FinalValue.MasterIp=masterIp.getHostAddress();
					System.out.println("createTCPConn"+socket);
					listener.onResult(true, socket);// 连接成功，回调
				} catch (Exception e) {
					// e.printStackTrace();
					LogUtils.e("connSupport:建立tcp失败"
							+ masterIp.getHostAddress() + " prot " + port);
					listener.onResult(false, null);// 连接失败，回调
				}
			}
		}.start();// 开启线程建立TCP连接
	}

	/**
	 * tcp连接后接收信息,限长32byte
	 * 
	 * @param socket
	 * @param listener
	 */
	public static void tcpReceive(final Socket socket,
			final OnResultListener<byte[]> listener) {
		tcpReceive(socket, 32, listener);
	}

	/**
	 * 建立接收线程
	 * 
	 * @param socket
	 * @param cmdlength
	 * @param listener
	 */
	public static void tcpReceive(final Socket socket, final int cmdlength,
			final OnResultListener<byte[]> listener) {
		new Thread() {
			@Override
			public void run() {
				try {
					int lenRead;
					byte[] read, pack, cmd;
					read = new byte[cmdlength];
					JsonCommandBuffer buffer = new JsonCommandBuffer(
							Data.BUFFER_CAPACITY);
					PackContainer container = new PackContainer();

					while (!socket.isClosed() && StaticValue.isRunning()) {
						// 接收数据
						lenRead = socket.getInputStream().read(read);
						if (lenRead <= 0)
							break;

						// 将接受到的数据添进缓存
						buffer.importByte(read, lenRead);

						// 从缓存中提取命令，放到容器中
						while ((pack = buffer.exportPack()) != null) {
							container.add(pack);
						}

						// 从容器中逐个提取指令并处理，最后清空容器
						for (byte[] b : container.getContainer()) {
							// 解包
							if (!JsonPack.Check_CustomCode(b,
									Data.CUSTOM_CODE_RF))
								continue;
							cmd = JsonPack.Data_Unpack(b);
							if (cmd == null)
								continue;
							// 对解包后的命令进行处理
							if (listener != null) {
								listener.onResult(true, cmd); // 成功回调
							}
						}
						container.clear();

						// 检查缓存的状态，处理异常
						switch (buffer.check()) {
						case JsonCommandBuffer.RESULT_ERROR_DATA:
							buffer.clean();
							break;
						}
					}

					listener.onResult(false, null);// 断开回调
					LogUtils.e("tcp接收线程断开或建立失败");

				} catch (IOException e) {
					listener.onResult(false, null);// 异常回调
					LogUtils.e("tcp接收线程异常");
				}
			}
		}.start();
	}

	/**
	 * 通过tcp连接发送协议
	 * 
	 * @param socket
	 * @param cmd
	 */
	public static void tcpSend(Socket socket, byte[] cmd,
			OnResultListener<byte[]> listener) {
		try {
			socket.getOutputStream().write(cmd);
		} catch (IOException e) {
			e.printStackTrace();
			LogUtils.e("tcp错误");
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (listener != null) {
				listener.onResult(false, cmd);
			}
		}
	}
}
