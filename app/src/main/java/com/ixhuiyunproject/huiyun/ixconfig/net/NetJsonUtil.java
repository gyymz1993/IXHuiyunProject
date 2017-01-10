package com.ixhuiyunproject.huiyun.ixconfig.net;

import android.os.SystemClock;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.cmd.Data;
import com.ixhuiyunproject.huiyun.ixconfig.net.cmd.JsonPack;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.BaseJsonModule;
import com.ixhuiyunproject.huiyun.ixconfig.utils.BytesStringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ThreadManager;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 传输json的工具
 * 
 * @author lzy_torah
 * 
 */
public class NetJsonUtil {
	private static NetJsonUtil instance = new NetJsonUtil();
	/** 本地连接tcp接口 */
	private static final int LocalJsonPort = 48901;
	private SparseArray<BaseJsonModule<?>> handleDataArray = new SparseArray<BaseJsonModule<?>>();
	Map<Integer, BaseJsonModule<?>> map=new HashMap<Integer, BaseJsonModule<?>>();
	/** 本地tcp的链接 */
	private Socket localSocket;
	/** 远程tcp连接 */
	private Socket serverSocket;
	private SendCmdThread sendCmdThread;
	Gson gson = new Gson();
	/**
	 * 当前的连接状态
	 */
	ConnStateBean currentConnStateBean;

	private NetJsonUtil() {
		sendCmdThread = new SendCmdThread() {
			@Override
			protected void sendCMD(byte[] cmd) {// 打包后发送
				if (judgeSocketIsAvaliable()) {// 连接正常，发送
					if (StaticValue.isRemote()) {
						ConnSupport.tcpSend(serverSocket, cmd,
								new OnResultListener<byte[]>() {

									@Override
									public void onResult(boolean isSecceed,
											byte[] obj) {
										if (!isSecceed) {
											addCmdForSend(obj);// 重新添加到待发送
										}
									}
								});
					} else {
						ConnSupport.tcpSend(localSocket, cmd,
								new OnResultListener<byte[]>() {

									@Override
									public void onResult(boolean isSecceed,
											byte[] obj) {
										if (!isSecceed) {
											addCmdForSend(obj);// 重新添加到待发送
										}
									}
								});
					}
				} else {// 重连
					try {
						createTCPConn(currentConnStateBean.getConnectIp(),
								currentConnStateBean.getPort(),
								currentConnStateBean.isLocal);
						LogUtils.e(currentConnStateBean.getConnectIp() + "----"
								+ currentConnStateBean.getPort() + "----"
								+ currentConnStateBean.isLocal);
						ToastUtils.showToastReal("网络错误，正在重连");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};// 初始化发送命令线程
		sendCmdThread.start();
	}

	public static NetJsonUtil getInstance() {
		return instance;
	}

	/**
	 * 创建jsonTcp链接，远程和局域网通用
	 * 
	 * @param connectIp
	 * @param port
	 * @param isLocal
	 *            true本地，false远程
	 */
	private void createTCPConn(final InetAddress connectIp, final int port,
			final boolean isLocal) {
		currentConnStateBean = new ConnStateBean(connectIp, port, isLocal);
		// 建立jsontcp链接
		ConnSupport.createTCPConn(connectIp, port,
				new OnResultListener<Socket>() {

					@Override
					public void onResult(boolean isSecceed, final Socket socket) {
						if (isSecceed) {// tcp链接成功
							if (isLocal) {
								NetJsonUtil.this.localSocket = socket;
							} else {
								NetJsonUtil.this.serverSocket = socket;
							}
							LogUtils.i("Json_tcp连接成功");
							notifyNetProgressListener(2);// 通知监听者，tcp连接成功
							// 建立监听者,每条命令的最大长度为10k
							createTcpReceiver(isLocal, socket);
						}
					}

				});
	}

	/**
	 * 在保证socket可用的前提下，建立接收线程
	 * 
	 * @param isLocal
	 * @param socket
	 */
	private void createTcpReceiver(final boolean isLocal, final Socket socket) {
		ConnSupport.tcpReceive(socket, 1024 * 10,
				new OnResultListener<byte[]>() {
					@Override
					public void onResult(boolean isSecceed, byte[] obj) {
						if (isSecceed && obj != null) {
							try {
								LogUtils.i(isLocal ? "本地连接收到数据" : "远程连接收到数据");
								BaseJsonObj jsonObj = JsonUtil
										.analyzeBytes(obj);
								if (jsonObj != null) {// 分发到模块去处理
									BaseJsonModule<?> analyst = handleDataArray
											.get(jsonObj.code);
									if (analyst != null)
										analyst.handleCMDdata(jsonObj);// 分发给相应的模块处理
									else
										LogUtils.i("功能码为" + jsonObj.code
												+ "没有mod处理");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {// 可能tcp断开，可能是退出软件，给用户提示
							if (StaticValue.isRunning()) {
							}
						}
					}
				});
	}

	/**
	 * 设置本地tcp连接
	 * 
	 * @param masterIp
	 */
	private void createLocalTCPConn(InetAddress masterIp) {
		if (isLocalTcpAvaliable()) {// 已经建立了tcp，不再重新建立
			notifyNetProgressListener(2);
			return;
		}
		createTCPConn(masterIp, LocalJsonPort, true);
	}

	/**
	 * 创建远程连接
	 * 
	 * @param connectIp
	 * @param port
	 */
	public void createServerTcpConn(InetAddress connectIp, int port,
			OnResultListener<Integer> netProgressListener) {
		this.netProgressListener = netProgressListener;
		if (isServerTcpAvaliable()) {// 已经建立了tcp，不再重新建立
			notifyNetProgressListener(2);
			return;
		}
		createTCPConn(connectIp, port, false);
		notifyNetProgressListener(1);
	}

	/**
	 * 设置Module,用来处理返回信息
	 * 
	 * @param handleDataEr
	 */
	public void setHandleDataMod(int cmdNumber, BaseJsonModule<?> handleDataer) {
		handleDataArray.put(cmdNumber, handleDataer);
	}

	/**
	 * 添加一个要通过tcp发送的jsonobj
	 * 
	 * @param list
	 */
	public void addCmdForSend(BaseJsonObj myjsonObj) {
		if (myjsonObj != null) {
			String jsonstr = gson.toJson(myjsonObj);
			LogUtils.i("要发送的命令" + jsonstr);
			try {
				sendCmdThread.addCmdForSend(JsonPack.Data_Pack(
						Data.CUSTOM_CODE_RF, jsonstr.getBytes("utf-8")));
				System.out.println(BytesStringUtils.toStringShow(JsonPack.Data_Pack(
						Data.CUSTOM_CODE_RF, jsonstr.getBytes("utf-8")))+"打包");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			LogUtils.i("发送json失败，生成命令时错误");
		}
	}

	/**
	 * 初始化socket，建立连接
	 * 
	 * @return socket是否可用
	 */
	private boolean judgeSocketIsAvaliable() {
		if (StaticValue.isRemote()) {
			if (serverSocket == null || serverSocket.isClosed()) {
				return false;
			}
		} else {
			if (localSocket == null || localSocket.isClosed()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 切换模式时，关闭另外一个的连接
	 */
	public void closeOtherState(boolean isRemote) {
		try {
			if (isRemote) {
				// 远程模式中,关闭近程socket
				if (localSocket != null && !localSocket.isClosed()) {
					localSocket.close();
				}
			} else {
				// 近程模式中，关闭远程socket
				if (serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭所有的连接
	 */
	public void closeAllSocket() {
		try {
			// 关闭近程socket
			if (localSocket != null && !localSocket.isClosed()) {
				localSocket.close();
			}
			// 关闭远程socket
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	ConnSupport connSupp = new ConnSupport();
	/** 主机ip */
	InetAddress masterIp = null;// 找到主机后初始化

	/**
	 * 寻找主机，当前只能寻找返回信息中含有“MASTER,AXTION”的主机，且寻找到一台后就关闭寻找
	 */
	public void findMasteByUDP() {
		if (isHasFindMaster()) {// 如果多个主机
			notifyFindMaster();
			return;
		}
		// 接收线程
		connSupp.udpReceiveForMaster(new OnResultListener<InetAddress>() {
			@Override
			public void onResult(boolean result, InetAddress obj) {
				if (isHasFindMaster()) {// TODO 多个主机
					notifyFindMaster();
					return;
				}
				if (result) {
					masterIp = obj;
					LogUtils.i("找到主机 " + obj.getHostAddress()+"ip"+masterIp);
					notifyFindMaster();
				}
			}
		});
		// 发送
		ThreadManager.getLongPool().execute(new Runnable() {
			@Override
			public void run() {
				while ((!isHasFindMaster()) && StaticValue.isRunning()
						&& (!StaticValue.isRemote())) {// 没有找到主机就一直发送
					LogUtils.i("发送了3条udp，找主机");
					connSupp.udpSendToFindMaster();
					SystemClock.sleep(5000);
				}
			}
		});
	}

	/** 建立连接的进度监听者 */
	private OnResultListener<Integer> netProgressListener;
	private OnResultListener<Integer> onFindMasterListener;

	/**
	 * 创建json_tcp本地连接
	 */
	public void createLocalTCPConnection(
			OnResultListener<Integer> netProgressListener) {
		this.netProgressListener = netProgressListener;
		// 建立json的链接
		if (isHasFindMaster()) {
			createLocalTCPConn(masterIp);
			notifyNetProgressListener(1);
		} else {
			onFindMasterListener = new OnResultListener<Integer>() {

				@Override
				public void onResult(boolean isSecceed, Integer obj) {
					if (isSecceed) {
						createLocalTCPConnection(NetJsonUtil.this.netProgressListener);
						onFindMasterListener = null;
					}
				}
			};
			findMasteByUDP();
			notifyNetProgressListener(0);
		}
	}

	/**
	 * 判断是否找到了主机
	 */
	private boolean isHasFindMaster() {
		return masterIp != null;
	}

	/**
	 * 判断本地连接是否可用
	 * 
	 * @return
	 */
	private boolean isLocalTcpAvaliable() {
		return (localSocket != null && (!localSocket.isClosed()));
	}

	/**
	 * 判断远程连接是否可用
	 * 
	 * @return
	 */
	private boolean isServerTcpAvaliable() {
		return (serverSocket != null && (!serverSocket.isClosed()));
	}

	/**
	 * 通知监听者，联网的进度
	 * 
	 * @param progress
	 *            0:未找到主机。 1：找到主机，未建立成功tcp连接 2：建立tcp成功
	 */
	private void notifyNetProgressListener(int progress) {
		if (netProgressListener != null) {
			if (progress == 2) {
				netProgressListener.onResult(true, 2);
			} else {
				netProgressListener.onResult(false, progress);
			}
		}
	}

	/**
	 * 通知监听者，找到主机了
	 */
	private void notifyFindMaster() {
		if (onFindMasterListener != null) {
			onFindMasterListener.onResult(true, null);
		}
	}

	/**
	 * 储存登录状态
	 * 
	 * @author torah
	 * 
	 */
	public static class ConnStateBean {
		private InetAddress connectIp;
		private int port;
		private boolean isLocal;

		public ConnStateBean(InetAddress connectIp, int port, boolean isLocal) {
			super();
			this.connectIp = connectIp;
			this.port = port;
			this.isLocal = isLocal;
		}

		public InetAddress getConnectIp() {
			return connectIp;
		}

		public int getPort() {
			return port;
		}

		public boolean isLocal() {
			return isLocal;
		}
	}
}
