package com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil;

import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * 下载文件的工具类
 * @author torah
 *
 */
public class DownloadUtils {
	/**下载文件
	 * @param url
	 * @param filePath
	 * @param listener
	 */
	public static void downLoad(String url, String filePath,
			final OnResultListener<Object> listener) {
		HttpUtils http = new HttpUtils();
		http.download(url, filePath, true, true, new RequestCallBack<File>() {

			public void onStart() {
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				if (listener != null) {
					listener.onResult(false, null);
				}
			}

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				if (listener != null) {
					listener.onResult(true, responseInfo.result.getPath());
				}
			}
		});

	}
}
