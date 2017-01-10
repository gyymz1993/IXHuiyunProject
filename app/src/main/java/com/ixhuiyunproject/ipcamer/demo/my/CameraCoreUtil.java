package com.ixhuiyunproject.ipcamer.demo.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.ipcamer.demo.BridgeService;
import com.ixhuiyunproject.ipcamer.demo.ContentCommon;
import com.ixhuiyunproject.ipcamer.demo.SystemValue;
import com.ixhuiyunproject.vstc2.nativecaller.NativeCaller;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


public class CameraCoreUtil implements BridgeService.PlayInterface {
	private static CameraCoreUtil instance = new CameraCoreUtil();

	public static CameraCoreUtil getInstance() {
		return instance;
	}

	private List<OnResultListener<Bitmap>> listnerList = new ArrayList<OnResultListener<Bitmap>>();
	/** 图片的byte数组是否已经处理 */
	private boolean bDisplayFinished = true;

	/**添加一个监控图片监听者
	 * @param listener
	 */
	public void addVideoPiclistener(OnResultListener<Bitmap> listener) {
		if (listener == null)
			return;
		if (listnerList.contains(listener))//防止重复注册
			return;
		listnerList.add(listener);
	}

	/**删除一个监控图片监听者
	 * @param listener
	 */
	public void removeVideoPiclistener(OnResultListener<Bitmap> listener) {
		if (listener == null)
			return;
		listnerList.remove(listener);
	}

	/**
	 * 开始视频连接
	 */
	public static void startPPPP() {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(200);
					int result = NativeCaller.StartPPPP(SystemValue.deviceId,
							SystemValue.deviceName, SystemValue.devicePass);
					LogUtils.i("连接摄像机result:" + result);
				} catch (Exception e) {
					e.printStackTrace();
					LogUtils.e("开启摄像头失败");
				}
			}
		}.start();
	}

	/**
	 * 通知监听者
	 */
	public void notifyVideoPiclisteners(Bitmap bitmap) {
		LogUtils.i("通知观察者");
		int length = listnerList.size();
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				OnResultListener<Bitmap> listener = listnerList.get(i);
				listener.onResult(true, bitmap);
			}
			bDisplayFinished = true;// 设为图片已经处理
		}
	}

	/**
	 * 停止接收摄像数据时调用
	 */
	public static void recycleCameraThings(Context context) {
		NativeCaller.StopPPPPLivestream(SystemValue.deviceId);
		try {
			NativeCaller.Free(); // 释放资源
		} catch (Exception e) {
			e.printStackTrace();
		}
		Intent intent = new Intent();
		intent.setClass(context, BridgeService.class);
		context.stopService(intent); // 停止服务
	}

	// ************************PlayInterface接口实现方法****start
	@Override
	public void callBackCameraParamNotify(String did, int resolution,
			int brightness, int contrast, int hue, int saturation, int flip) {
		// 分辨率相关？
	}

	@Override
	public void callBaceVideoData(String did, byte[] videobuf, int h264Data,
			int len, int width, int height) {
		if (!bDisplayFinished) {
			Log.d("info", "上一帧未来得及处理");
			return;
		}
		if (!did.equals(SystemValue.deviceId)) {// 判断是哪个摄像头传过来的数据
			return;
		}
		bDisplayFinished = false;// 设为图片未处理
		// log:传进来的数据一般是width*height=1280*720
		byte[] rgb = new byte[width * height * 2];
		NativeCaller.YUV4202RGB565(videobuf, rgb, width, height);
		ByteBuffer buffer = ByteBuffer.wrap(rgb);
		rgb = null;
		/* ByteBuffer buffer = ByteBuffer.wrap(videodata); */
		// 转成特定大小的bitmap
		Bitmap mBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		mBmp.copyPixelsFromBuffer(buffer);
		// 通知观察者
		notifyVideoPiclisteners(mBmp);
	}

	/**
	 * 设备状态变化通知
	 */
	@Override
	public void callBackMessageNotify(String did, int msgType, int param) {

		LogUtils.i("MessageNotify did: " + did + " msgType: " + msgType
				+ " param: " + param);
		if (msgType == ContentCommon.PPPP_MSG_TYPE_STREAM) {
			LogUtils.e("未处理：PPPP_MSG_TYPE_STREAM");
			// nStreamCodecType = param;
			// Message msgMessage = new Message();
			// msgStreamCodecHandler.sendMessage(msgMessage);
			return;
		}
		if (msgType != ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS) {
			return;
		}
		ToastUtils.showToastReal(did + "设备掉线");
	}

	@Override
	public void callBackAudioData(byte[] pcm, int len) {
		LogUtils.i("语音数据，长度：" + len + " 暂未处理");

	}

	@Override
	public void callBackH264Data(byte[] h264, int type, int size) {
		// 注：被不断回调
	}
	// ************************PlayInterface接口实现方法****end
}
