package com.ixhuiyunproject.huiyun.ixconfig.activity.about;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnSucceedListener;
import com.ixhuiyunproject.huiyun.ixconfig.server.HttpConnectUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

import java.util.HashMap;
import java.util.Map;


 /**
 *  Function: 关于我们
 *  @author YangShao 2015年5月12日 下午3:46:52
 *  @version 1.0
 */
public class AboutHuiyunActivity extends SwipeBackActivity {
	private Context mContext;
	FeedbackAgent agent;

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // 保证 onPageEnd 在onPause
													// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	// private TextView userBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about_huiyun);
		mContext = this;
	}

	public void about(View v) {
		dialog = new FeedBackDialog();
		dialog.show(getFragmentManager(), "tag");
		// agent = new FeedbackAgent(mContext);
		// agent.sync();
		// agent.startFeedbackActivity();
	}

	public void back(View v) {
		this.finish();
	}

	/**
	 * 意见反馈
	 */
	private DialogFragment dialog;
	@SuppressLint("ValidFragment")
	private class FeedBackDialog extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final EditText addEare;
			final EditText addName;
			Button condirmBtn;
			final View view = View.inflate(mContext, R.layout.dialog_about,
					null);
			addEare = (EditText) view.findViewById(R.id.about_recommended);
			addName = (EditText) view.findViewById(R.id.add_userphone);
			condirmBtn = (Button) view.findViewById(R.id.about_confirmU);
			Dialog builder = new Dialog(mContext, R.style.Theme_dialog);

			condirmBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (addEare.getText().toString().equals("")
							|| addName.getText().toString().equals("")) {
						Toast.makeText(mContext, "建议或联系方式不能为空！", Toast.LENGTH_LONG).show();
						dialog.dismiss();
						return;
					} else {
						/*************** post **********************/
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("context", addEare.getText().toString());
						map.put("phone", addName.getText().toString());
						map.put("method", "suggest");
						HttpConnectUtil httpDeviceDao = new HttpConnectUtil();
						httpDeviceDao.suggest(map,
								new OnSucceedListener<String>() {
									@Override
									public void onSucceed(String obj) {
										ToastUtils.showToastReal("感谢您的建议");
										dialog.dismiss();
									}
								});
					}
				}
			});

			builder.setContentView(view);
			Window dialogWindow = builder.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER);

			DisplayMetrics metric = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metric);
			int width = metric.widthPixels; // 屏幕宽度（像素）
			// int height = metric.heightPixels; // 屏幕高度（像素）

			lp.width = width - 150;
			dialogWindow.setAttributes(lp);
			builder.show();
			return builder;
		}
	}

	@Override
	public OnItemClickListener popmenuItemClickListener(
			OnItemClickListener listener) {
		return null;
	}

	@Override
	public String[] setPopMenuName() {
		return null;
	}
}

//class PostSuggest extends AsyncTask<String, String, String> {
//	private List<NameValuePair> params;
//	public PostSuggest(List<NameValuePair> params) {
//		this.params = params;
//	}
//
//	@Override
//	protected String doInBackground(String... arg0) {
//
//		JSONParser jsonParser = new JSONParser();
//		JSONObject json = jsonParser.makeHttpRequest("http://"
//				+ FinalValue.IP_SERVER + ":8080/Ix_Project/UserServlet",
//				"POST", params);
//		System.out.println("服务器返回的suggest数据：" + json);
//		return null;
//	}
//
//}
