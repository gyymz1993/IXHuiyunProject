package com.ixhuiyunproject.huiyun.ixconfig;

public class FinalValue {
	// 是否主机处理 obj:目标
	public static final String OBJ_MASTER = "master";
	public static final String OBJ_PHONE = "phone";
	public static final String OBJ_SERVER = "server";
	public static final String mPageName = "FragmentSimple";
	
	
	public static String MasterIp="";
	// 云服务器"120.24.239.90",本地：192.168.1.135;
	public static final String IP_SERVER = "120.24.239.90";
	/** 主页广告栏图片更新网址 */
	public static final String WEBSITE_ADVERT = 
//			"http://" + IP_SERVER + 
			"http://120.24.239.90:8080/huiyun-webapp/advertising/getAdByCondition";
	/** 广告视频下载地 */
	//public static final String URL_AD_VIDEO = "http://" + IP_SERVER
	//		+ ":8080/huiyun-webapp/advertising/video.txt";
	//public static final String URL_AD_VIDEO = "http://192.168.1.249:8080/JavaServers/video.txt";
	//public static final String URL_AD_VIDEO = "http://" + IP_SERVER
	//		+ ":8080/Ix_Project/video.txt";
	public static final String URL_AD_VIDEO = "http://120.24.239.90:8080/huiyun-webapp/advertising/video";
	//http://120.24.239.90:8080/Ix_Project/01.mp4
	/** 最新的版本信息 */
	//public static final String URL_UPDAT_INFO = "http://" + IP_SERVER
		//	+ ":8080/huiyun-webapp/app/appupdate";
	public static final String URL_UPDAT_INFO = "http://192.168.1.249:8080/JavaServers/update.txt";
}
