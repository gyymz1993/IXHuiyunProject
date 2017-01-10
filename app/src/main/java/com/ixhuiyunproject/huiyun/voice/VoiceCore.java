package com.ixhuiyunproject.huiyun.voice;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.JsonParser;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.SpUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.label.service.ToastService;

/**
 * 语音的核心类
 * 
 * @author torah
 * 
 */
public class VoiceCore {
	/**
	 * 解析语句的控件
	 */
	SentenceController sentenceController;
	/**
	 * 语音识别者
	 */
	public static SpeechRecognizer mIat;

	private VoiceCore() {
		sentenceController = new SentenceController();
	}

	private static VoiceCore instance = new VoiceCore();

	public static VoiceCore getInstance() {
		return instance;
	}

	/**
	 * 初始化语音
	 * 
	 * @param context
	 */
	public void initSpeechRecognizer(Context context) {
		/*
		 * 转写中文： iatRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");
		 * iatRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		 * iatRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin");
		 * 
		 * 转写英文： iatRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");
		 * iatRecognizer.setParameter(SpeechConstant.LANGUAGE, "en_us");
		 * 
		 * 转写粤语： iatRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");
		 * iatRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		 * iatRecognizer.setParameter(SpeechConstant.ACCENT, "cantonese");
		 */
		setLanguage(context);
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
		// 设置标点符号
		mIat.setParameter(SpeechConstant.ASR_PTT, "0");
	}

	/**
	 * Function: 设置语言
	 * 
	 * @author YangShao 2015年4月22日 上午11:03:20
	 * @param context
	 */
	public void setLanguage(Context context) {
		mIat = SpeechRecognizer.createRecognizer(context, null);
		// 2.设置听写参数，详见《科大讯飞MSC API手册(Android)》 SpeechConstant类
		mIat.setParameter(SpeechConstant.DOMAIN, "iat");
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		// 设置语言
		String lag = SpUtils.getString(SpUtils.key_language);
		//Toast.makeText(UIUtils.getContext(), "当前语言" + lag, Toast.LENGTH_SHORT)
		//.show();
		if (!StringUtils.isEmpty(lag)) {
			if (lag.equals("en_us")) {
				mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
			} else {
				mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
				// 设置语言区域
				mIat.setParameter(SpeechConstant.ACCENT, lag);
			}
		} else {
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		}
	}

	/**
	 * 开始语音识别
	 */
	public void begainSpeechRecognizer() {
		// 3.开始听写
		mIat.startListening(mRecoListener);
	}

	private RecognizerListener mRecoListener = new RecognizerListener() {
		// 听写结果回调接口 (返回Json格式结果，用户可参见附录)；
		// 一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
		// 关于解析Json的代码可参见MscDemo中JsonParser类；
		// isLast等于true时会话结束。
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			LogUtils.e("所说语句为：" + text);
			System.out.println("所说语句为：" + text);
			if (ToastService.tv != null) {
				ToastService.tv.setVisibility(View.VISIBLE);
				ToastService.tv.setText(text);
			}
			if (StringUtils.isEmpty(text)) {
				ToastService.tv.setVisibility(View.GONE);
			}
			sentenceController.judgeSentence(text);// 判断语句含义
		}

		// 会话发生错误回调接口
		public void onError(SpeechError error) {
			error.getPlainDescription(true);// 获取错误码描述
		}

		// 开始录音
		public void onBeginOfSpeech() {
		}

		// 音量值0~30
		public void onVolumeChanged(int volume) {
		}

		// 结束录音
		public void onEndOfSpeech() {
			if (ToastService.img != null) {
				ToastService.img.setImageResource(R.drawable.vioce_icon);
				// ToastService.img.setBackgroundResource(0);
				ToastService.tv.setVisibility(View.GONE);
			}
		}

		// 扩展用接口
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}

	};
}
