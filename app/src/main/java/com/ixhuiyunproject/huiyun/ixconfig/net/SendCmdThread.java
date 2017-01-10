package com.ixhuiyunproject.huiyun.ixconfig.net;

import android.os.SystemClock;

import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;

import java.util.ArrayList;
import java.util.List;


/**
 * 维护一个队列，接受命令，每隔一段时间调用sendCMD
 * 
 * @author lzy_torah
 *
 */
public abstract class SendCmdThread extends Thread {
	/**发送
	 * @param cmd
	 */
	protected abstract void sendCMD(byte[] cmd);
	/**
	 * 要设置组合的队列
	 */
	public List<byte[]> msgList = new ArrayList<byte[]>();
	private final long intervalTime = 300;

	@Override
	public void run() {

		while (StaticValue.isRunning()) {
			if (msgList.size() != 0) {
				synchronized (msgList) {
					if (msgList.size() != 0) {
						byte[] cmd = null;
						cmd = msgList.remove(0);
						this.sendCMD(cmd);
					}
				}
			}
			SystemClock.sleep(intervalTime);
		}
	}

	/**
	 * 添加一个要发送的处理的命令
	 * 
	 * @param list
	 */
	public void addCmdForSend(byte[] bytes) {
		synchronized (msgList) {
			msgList.add(bytes);
		}
	}
}
