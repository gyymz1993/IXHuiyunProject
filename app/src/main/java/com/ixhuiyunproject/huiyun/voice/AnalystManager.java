package com.ixhuiyunproject.huiyun.voice;

import com.ixhuiyunproject.huiyun.voice.analyst.LampOffAnalyst1;
import com.ixhuiyunproject.huiyun.voice.analyst.LampOffAnalyst2;
import com.ixhuiyunproject.huiyun.voice.analyst.LampOnAnalyst1;
import com.ixhuiyunproject.huiyun.voice.analyst.LampOnAnalyst2;
import com.ixhuiyunproject.huiyun.voice.analyst.RedContrlAnalyst1;
import com.ixhuiyunproject.huiyun.voice.analyst.RedContrlAnalyst2;
import com.ixhuiyunproject.huiyun.voice.analyst.RedContrlAnalyst3;
import com.ixhuiyunproject.huiyun.voice.analyst.SceneOnAnalyst;
import com.ixhuiyunproject.huiyun.voice.analyst.SceneOnAnalyst2;
import com.ixhuiyunproject.huiyun.voice.analyst.SceneOnAnalyst3;

/**
 * 初始化各种Pattern
 * @author torah
 *
 */
public class AnalystManager {
	LampOffAnalyst1 lampOff1 = new LampOffAnalyst1();
	LampOffAnalyst2 lampOff2 = new LampOffAnalyst2();
	LampOnAnalyst1 lampOn1 = new LampOnAnalyst1();
	LampOnAnalyst2 lampOn2 = new LampOnAnalyst2();
	SceneOnAnalyst sceneon = new SceneOnAnalyst();
	SceneOnAnalyst2 sceneOn2 = new SceneOnAnalyst2();
	SceneOnAnalyst3 sceneOn3 = new SceneOnAnalyst3();
	RedContrlAnalyst1 redone = new RedContrlAnalyst1();
	RedContrlAnalyst2 redotwo = new RedContrlAnalyst2();
	RedContrlAnalyst3 redothree = new RedContrlAnalyst3();
	/**
	 * 初始化Pattern解析者
	 * @param sentenceController 
	 */
	public void initAnalyst(SentenceController sentenceController) {
		sentenceController.addAModuleAnalyst(lampOff1);
		sentenceController.addAModuleAnalyst(lampOff2);
		sentenceController.addAModuleAnalyst(lampOn1);
		sentenceController.addAModuleAnalyst(lampOn2);
		sentenceController.addAModuleAnalyst(sceneon);
		sentenceController.addAModuleAnalyst(sceneOn2);
		sentenceController.addAModuleAnalyst(sceneOn3);
		sentenceController.addAModuleAnalyst(redone);
		sentenceController.addAModuleAnalyst(redotwo);
		sentenceController.addAModuleAnalyst(redothree);
	}

}
