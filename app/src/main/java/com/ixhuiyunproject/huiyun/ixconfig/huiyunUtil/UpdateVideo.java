package com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil;

import android.content.Context;

import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.VideoSplashActivity;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.utils.FileUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.SpUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.UIUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 检查更新的工具类
 */
public class UpdateVideo {
	public static OnResultListener<String> onResultListener;
	public static String url;
	// 下载保存路径
	public static File videoFile;

	public static void setOnResultListener(
			OnResultListener<String> monResultListener) {
		onResultListener = monResultListener;
	}

	/**
	 * 获取服务器最新版本的信息
	 */
	public static void getUrl() {
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET, FinalValue.URL_AD_VIDEO,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						System.out.println(current + "/" + total);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.i(responseInfo.result);
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							url = jsonObject.getString("videoUrl");
							String videoName = getVideoName(url);
							String dir = FileUtils.getDir("huiyun");
							dir += getVideoName(url);
							videoFile = new File(dir);
							mSavePath=videoFile.getAbsolutePath();
							
							if (onResultListener != null) {
								onResultListener.onResult(true, videoName);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtils.i("getUrl");
					}
				});
	}

	@SuppressWarnings("rawtypes")
	public static HttpHandler handler = null;
	private static String mSavePath;
	public static void downLoadVideo(final Context context) {

		HttpUtils http = new HttpUtils();
		if (!StringUtils.isEmpty(url)) {
			handler = http.download(url, mSavePath, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
					true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
					new RequestCallBack<File>() {
						@Override
						public void onStart() {
							System.out.println("conn...");
							if(videoFile.exists()){
								VideoSplashActivity.handle
								.sendEmptyMessage(VideoSplashActivity.MSG_PLAY);
								VideoSplashActivity.handle.sendEmptyMessage(VideoSplashActivity.MSG_WHAT_JUMP);
							}
						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							int x = (int) (current * 100 / (int) total);
							System.out.println(x + "%");
							System.out.println("onLoading" + current + "/"
									+ total);
						}

						@Override
						public void onSuccess(ResponseInfo<File> responseInfo) {
							System.out.println("downloaded:"
									+ responseInfo.result.getPath());
							UIUtils.runInMainThread(new Runnable() {
								@Override
								public void run() {
									SpUtils.saveString(Sp_Key.VIDEO_FILENAME, getVideoName(url));
									VideoSplashActivity.handle
											.sendEmptyMessage(VideoSplashActivity.MSG_PLAY);
									VideoSplashActivity.handle.sendEmptyMessage(VideoSplashActivity.MSG_WHAT_JUMP);
								}
							});
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							LogUtils.e("下载视频失败");
						}
					});
		}

	}

	/**
	 * 
	 */
	public static String getVideoName(String url) {
		int a = url.lastIndexOf("/");
		String str = url.substring(a + 1, url.length());
		return str;
	}


}
