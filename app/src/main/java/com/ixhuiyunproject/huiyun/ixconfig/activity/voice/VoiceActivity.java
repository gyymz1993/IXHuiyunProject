package com.ixhuiyunproject.huiyun.ixconfig.activity.voice;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.TitleInitUtil;
import com.ixhuiyunproject.huiyun.ixconfig.bean.JsonVoiceKeyWord;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.SceneItem;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentContainer;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.DeviceManager;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.RadioButtonDialogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.SpUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.voice.SentenceController;
import com.ixhuiyunproject.huiyun.voice.VoiceCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 语音识别
 * 
 * @author torah
 * 
 */
public class VoiceActivity extends SwipeBackActivity {
	SentenceController sentenceController = new SentenceController();
	Context context;
	private SpeechRecognizer mIat;
	VoiceCore core = VoiceCore.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice);
		context = this;
		core.initSpeechRecognizer(context);
		mIat = VoiceCore.mIat;
		initView();
	}

	/**
	 * 初始化界面
	 * 
	 */
	private void initView() {
		TitleInitUtil.initTitle(this, "语音功能");
		View iv_right = findViewById(R.id.iv_right);
		iv_right.setVisibility(View.INVISIBLE);

		Button btn_userWord = (Button) findViewById(R.id.btn_userWord);
		btn_userWord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				JsonVoiceKeyWord json = new JsonVoiceKeyWord();
				JsonVoiceKeyWord.KeyWord keyWord = new JsonVoiceKeyWord.KeyWord();
				keyWord.name = "开关词汇";
				addUserWords(keyWord.words);
				json.userword.add(keyWord);
				String contents = new Gson().toJson(json);
				LogUtils.i(contents);
				// 指定引擎类型
				mIat.setParameter(SpeechConstant.ENGINE_TYPE,
						SpeechConstant.TYPE_CLOUD);
				mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
				int ret = mIat.updateLexicon("userword", contents,
						lexiconListener);
				if (ret != ErrorCode.SUCCESS)
					LogUtils.e("上传热词失败,错误码：" + ret);
			}
		});

		Button btn_Language = (Button) findViewById(R.id.btn_language);
		btn_Language.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RadioButtonDialogUtils buttonDialogUtils = new RadioButtonDialogUtils(
						context, "请选择语言") {
					@Override
					protected ArrayList<HashMap<String, String>> getListDate() {
						ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
						HashMap<String, String> hashMap1 = new HashMap<String, String>();
						hashMap1.put("test", "普通话");
						HashMap<String, String> hashMap2 = new HashMap<String, String>();
						hashMap2.put("test", "粤语");
						HashMap<String, String> hashMap3 = new HashMap<String, String>();
						hashMap3.put("test", "河南话");
						HashMap<String, String> hashMap4 = new HashMap<String, String>();
						hashMap4.put("test", "英语");
						list.add(hashMap1);
						list.add(hashMap2);
						list.add(hashMap3);
						list.add(hashMap4);
						return list;
					}

					@Override
					protected int getDialogWidth() {
						return 700;
					}
				};

				buttonDialogUtils.onCreateDialog();
				buttonDialogUtils.setCallBack(new RadioButtonDialogUtils.SelectCallBack() {
					@Override
					public void isConfirm(String edString) {
						if (StringUtils.isEmpty(edString)) {
							Toast.makeText(VoiceActivity.this, "不能为空！",
									Toast.LENGTH_SHORT).show();
							return;
						} else {
							if (edString.equals("普通话")) {
								SpUtils.saveString(SpUtils.key_language,
										"mandarin");
							}
							if (edString.equals("粤语")) {
								SpUtils.saveString(SpUtils.key_language,
										"cantonese");

							}
							if (edString.equals("河南话")) {
								SpUtils.saveString(SpUtils.key_language,
										"henanese");
							}
							if (edString.equals("英语")) {
								SpUtils.saveString(SpUtils.key_language,
										"en_us");
							}
							core.setLanguage(context);
						}
					}
				});
			}
		});
	}

	/**
	 * 添加核心常用词汇
	 * 
	 * @param words
	 */
	private void addUserWords(List<String> words) {
		words.add("开灯");
		words.add("关灯");
		words.add("场景");
		words.add("模式");
		words.add("开");
		words.add("关");
		words.add("设备");
		words.add("灯");
		List<OutDevice> outDeviceList = DeviceManager.outDeviceList;
		for (OutDevice d : outDeviceList) {
			words.add(d.getName());
		}
		List<SceneItem> sceneList = FragmentContainer.SCENE_LIST;
		for (SceneItem scene : sceneList) {
			words.add(scene.getScene_name());
		}
	}

	/**
	 * 上传联系人/词表监听器。
	 */
	private LexiconListener lexiconListener = new LexiconListener() {

		@Override
		public void onLexiconUpdated(String lexiconId, SpeechError error) {
			if (error != null) {
				LogUtils.e(error.toString());
			} else {
				ToastUtils.showToastReal("上传用户词汇成功");
			}
		}
	};
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
