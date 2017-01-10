package com.ixhuiyunproject.huiyun.ixconfig.server;

import com.google.gson.Gson;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnSucceedListener;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 与服务器交互的类
 * 
 * @author torahs
 *
 */
public class HttpConnectUtil {

	private String USER_URL = "http://" + FinalValue.IP_SERVER + ":8080/Ix_Project" + "/UserServlet";

	public void suggest(Map<String, Object> map,
			final OnSucceedListener<String> listener) {// 登陆
		RequestCallBack<String> callBack = new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				LogUtils.i(arg0.result);
				Gson gson = new Gson();
				JsonResult jsonResult = gson.fromJson(arg0.result,
						JsonResult.class);
				if (jsonResult.getResult_code() == 1) {
					if (listener != null)
						listener.onSucceed("提交成功");
				} else if (jsonResult.getResult_code() == 2) {
					ToastUtils.showToastReal("提交失败");
					if (listener != null)
						listener.onSucceed(null);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.i("网络错误");
				ToastUtils.showToastReal("网络错误");
				if (listener != null)
					listener.onSucceed(null);
			}
		};
		realHttpConnect(map, USER_URL, callBack);// 调用真正的网络方法
	}

	/**
	 * 调用xutils的post方法
	 * 
	 * @param map
	 *            不能是final类型(会调用entrySet()方法)
	 * @param url
	 * @param callBack
	 */
	private void realHttpConnect(Map<String, Object> map, String url,
			RequestCallBack<String> callBack) {

		HttpUtils http = new HttpUtils();
		HttpMethod method = HttpMethod.POST;
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (Entry<String, Object> entry : map.entrySet()) {
			NameValuePair pars = new BasicNameValuePair(entry.getKey(), entry
					.getValue().toString());
			list.add(pars);
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter(list);
		http.send(method, url, params, callBack);
	}
}
