package com.ixhuiyunproject.huiyun.ixconfig.net;


import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.ContrlLoginResult_6;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.ContrlSceneResult_56;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.Contrl_InfraredResult_12;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.ControlClosedAndOpenResult_28;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.DeleteRemoteResult_20;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.DleTimerTaskResult_74;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.DownDevicesResult_22;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.DownWebCameResult_64;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.DownloadAllEare_38;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.DownloadAllSceneResult_52;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.DownloadOutDevices_24;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.DownloadRemote_16;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.DownloadSlave_58;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.DownloadTimerTask_72;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.NotificationStudyResult_8;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.ReadStateResult_30;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.RegisterAdminResult_2;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.RegisterUserResult_4;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.SeachDeviceResult_44;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.SetPanKeyResult_76;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.SettingGateway_34;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.SettingRelationResult_26;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.Start_InfraredResult_10;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.TimerTaskResult_68;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.UploadAreaResult_36;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.UploadDevicesResult_40;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.UploadRemoteResult_18;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.UploadSceneResult_54;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.UploadWebCameResult_62;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.WifiConnectResult_78;

/**
 * 管理所有的mod
 * 
 * @author lzy
 * 
 */
public class JSONModuleManager {
	private static JSONModuleManager instance;

	/**
	 * 返回唯一的模块管理
	 * 
	 * @return
	 */
	public static JSONModuleManager getInstance() {
		if (instance == null) {
			synchronized (JSONModuleManager.class) {
				if (instance == null) {
					instance = new JSONModuleManager();
					instance.initMods();
				}
			}
		}
		return instance;
	}

	private JSONModuleManager() {
	}

	/**
	 * 初始化各个处理模块的mod
	 */
	public void initMods() {
		NetJsonUtil instance2 = NetJsonUtil.getInstance();
		instance2.setHandleDataMod(2, admin_2);
		instance2.setHandleDataMod(4, user_4);
		instance2.setHandleDataMod(6, login_6);
		instance2.setHandleDataMod(8, result_8);
		instance2.setHandleDataMod(10, result_10);
		instance2.setHandleDataMod(12, result_12);
		instance2.setHandleDataMod(16, result_16);
		instance2.setHandleDataMod(18, result_18);
		instance2.setHandleDataMod(20, result_20);
		instance2.setHandleDataMod(22, result_22);
		instance2.setHandleDataMod(24, result_24);
		instance2.setHandleDataMod(26, result_26);
		instance2.setHandleDataMod(28, result_28);
		instance2.setHandleDataMod(30, result_30);
		instance2.setHandleDataMod(34, result_34);
		instance2.setHandleDataMod(36, result_36);
		instance2.setHandleDataMod(38, result_38);
		instance2.setHandleDataMod(40, result_40);
		instance2.setHandleDataMod(44, result_44);

		instance2.setHandleDataMod(52, result_52);
		instance2.setHandleDataMod(54, result_54);
		instance2.setHandleDataMod(56, result_56);
		instance2.setHandleDataMod(58, result_58);

		instance2.setHandleDataMod(62, result_62);
		instance2.setHandleDataMod(64, result_64);
		instance2.setHandleDataMod(68, result_68);
		instance2.setHandleDataMod(72, result_72);
		instance2.setHandleDataMod(74, result_74);
		instance2.setHandleDataMod(76, result_76);
		instance2.setHandleDataMod(78, result_78);
	}

	public RegisterAdminResult_2 admin_2 = new RegisterAdminResult_2();
	public RegisterUserResult_4 user_4 = new RegisterUserResult_4();
	public ContrlLoginResult_6 login_6 = new ContrlLoginResult_6();
	public NotificationStudyResult_8 result_8 = new NotificationStudyResult_8();
	public Start_InfraredResult_10 result_10 = new Start_InfraredResult_10();
	public Contrl_InfraredResult_12 result_12 = new Contrl_InfraredResult_12();
	public DownloadRemote_16 result_16 = new DownloadRemote_16();
	public UploadRemoteResult_18 result_18 = new UploadRemoteResult_18();
	public DeleteRemoteResult_20 result_20 = new DeleteRemoteResult_20();
	public DownDevicesResult_22 result_22 = new DownDevicesResult_22();
	public DownloadOutDevices_24 result_24 = new DownloadOutDevices_24();
	public SettingRelationResult_26 result_26 = new SettingRelationResult_26();
	public ControlClosedAndOpenResult_28 result_28 = new ControlClosedAndOpenResult_28();
	public ReadStateResult_30 result_30 = new ReadStateResult_30();
	public SettingGateway_34 result_34 = new SettingGateway_34();
	public UploadAreaResult_36 result_36 = new UploadAreaResult_36();
	public DownloadAllEare_38 result_38 = new DownloadAllEare_38();
	public UploadDevicesResult_40 result_40 = new UploadDevicesResult_40();
	public SeachDeviceResult_44 result_44 = new SeachDeviceResult_44();
	public DownloadAllSceneResult_52 result_52 = new DownloadAllSceneResult_52();
	public UploadSceneResult_54 result_54 = new UploadSceneResult_54();
	public ContrlSceneResult_56 result_56 = new ContrlSceneResult_56();
	public DownloadSlave_58 result_58 = new DownloadSlave_58();
	public UploadWebCameResult_62 result_62 = new UploadWebCameResult_62();
	public DownWebCameResult_64 result_64 = new DownWebCameResult_64();
	public TimerTaskResult_68 result_68 = new TimerTaskResult_68();
	public DownloadTimerTask_72 result_72 = new DownloadTimerTask_72();
	public DleTimerTaskResult_74 result_74 = new DleTimerTaskResult_74();
	public SetPanKeyResult_76 result_76 = new SetPanKeyResult_76();
	public WifiConnectResult_78 result_78 = new WifiConnectResult_78();
}
