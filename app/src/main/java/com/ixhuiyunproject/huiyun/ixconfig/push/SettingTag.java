package com.ixhuiyunproject.huiyun.ixconfig.push;

import android.os.Handler;
import android.text.TextUtils;

import com.ixhuiyunproject.huiyun.ixconfig.BaseApplication;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class SettingTag {
	// private static final String TAG = "JPush";
	String logs;
	public void setTag(String tag) {

		// 检查 tag 的有效性
		if (TextUtils.isEmpty(tag)) {
			return;
		}

		// ","隔开的多个 转换成 Set
		String[] sArray = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
				return;
			}
			tagSet.add(sTagItme);
		}

		// 调用JPush API设置Tag
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				// Log.i(TAG, logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				// Log.i(TAG, logs);
				if (ExampleUtil.isConnected(BaseApplication.getApplication())) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_ALIAS, alias),
							1000 * 60);
				} else {
					// Log.i(TAG, "No network");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				// Log.e(TAG, logs);
			}

			// ExampleUtil.showToast(logs, BaseApplication.getApplication());
		}

	};

	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				// Log.i(TAG, logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				// Log.i(TAG, logs);
				if (ExampleUtil.isConnected(BaseApplication.getApplication())) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_TAGS, tags),
							1000 * 60);
				} else {
					// Log.i(TAG, "No network");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				// Log.e(TAG, logs);
			}

			// ExampleUtil.showToast(logs, BaseApplication.getApplication());
		}

	};

	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;

	private final Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				// Log.d(TAG, "Set alias in handler.");
				JPushInterface.setAliasAndTags(
						BaseApplication.getApplication(), (String) msg.obj,
						null, mAliasCallback);
				break;

			case MSG_SET_TAGS:
				// Log.d(TAG, "Set tags in handler.");
				JPushInterface.setAliasAndTags(
						BaseApplication.getApplication(), null,
						(Set<String>) msg.obj, mTagsCallback);
				break;

			default:
				// Log.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};

}