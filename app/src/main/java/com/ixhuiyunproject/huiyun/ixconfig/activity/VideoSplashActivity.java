package com.ixhuiyunproject.huiyun.ixconfig.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.activity.userlogin.LoginActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.Version;
import com.ixhuiyunproject.huiyun.ixconfig.dialog.UpdateDialog;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.Sp_Key;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.UpdateUtil;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.UpdateVideo;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.FileUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.SpUtils;
import com.ixhuiyunproject.huiyun.ixconfig.view.CircleProgressBar;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * 进行一些初始化的操作
 * 
 * @author lzy
 * 
 */
public class VideoSplashActivity extends Activity {
	public static VideoSplashActivity activity;
	public final static int MSG_WHAT_JUMP = 1;
	public final static int MSG_PLAY = 4;// 下载广告文件
	public final int MSG_WHAT_TIME = 3;// 进度条
	public int seconds = 15;
	public Context context;
	TextView tv_jumpHome;
	static ImageView circlePointImg;
	private TextView tv_time;
	private VideoView video_ad;
	private View iv_ads;
	private CircleProgressBar myProgress;
	private Button btn_jihuo;
	public static Handler handle;

	/*
	 * 视频广告逻辑： 显示视频：查找sp，得到视频文件名，显示。
	 * 
	 * 下载说明字符串，跟sp文件对比，如果不同则需要更新 更新视频文件，下载成功后，删除旧的，将文件版本存入sp文件
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad2_splash);
		// 提前找主机
		NetJsonUtil.getInstance().findMasteByUDP();
		activity = this;
		context = this;
		initView();
		intContrl();
		preparetoJump();
		downApk();
		downVideo();
	}

	public void intContrl() {
		handle = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_WHAT_JUMP:
					if (seconds <= 0) {
						Intent intent = new Intent(context, LoginActivity.class);
						startActivity(intent);
						VideoSplashActivity.this.finish();
					} else {
						seconds--;
						tv_time.setText(seconds + "'s");
						myProgress.setProgress(100 - seconds * 20 / 3,
								circlePointImg);
						preparetoJump();
					}
					break;
				case MSG_WHAT_TIME:
					break;
				case MSG_PLAY:
					playVideo();
					break;
				default:
					break;
				}
			};
		};
	}

	private void initView() {
		View tv_jumpHome = findViewById(R.id.tv_jumpHome);
		tv_jumpHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 跳转下一页
				handle.removeMessages(MSG_WHAT_JUMP);
				seconds = 0;
				handle.sendEmptyMessage(MSG_WHAT_JUMP);
			}
		});
		tv_time = (TextView) findViewById(R.id.tv_time);
		video_ad = (VideoView) findViewById(R.id.video_ad);
		iv_ads = findViewById(R.id.iv_ads);

		// 进度条
		myProgress = (CircleProgressBar) findViewById(R.id.myProgress);
		circlePointImg = (ImageView) findViewById(R.id.circle_point_img);
		btn_jihuo = (Button) findViewById(R.id.btn_jihuo);
		btn_jihuo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myProgress.setVisibility(View.VISIBLE);
				circlePointImg.setVisibility(View.VISIBLE);
				btn_jihuo.setVisibility(View.INVISIBLE);
				// 跳转下一页
				handle.removeMessages(MSG_WHAT_JUMP);
				handle.sendEmptyMessage(MSG_WHAT_JUMP);
			}
		});

	}

	/**
	 * Function:下载视频文件
	 * 
	 * @author YangShao 2015年4月30日 上午10:22:46
	 */
	private void downVideo() {
		/*
		 * 先获得地址，然后判断是否需要更新
		 * 
		 */
		UpdateVideo.setOnResultListener(new OnResultListener<String>() {
			@Override
			public void onResult(boolean isSecceed, String obj) {
				if (isSecceed == true && null != obj) {
					// 版本判断
				//	System.out.println("==========" + obj + "-----"
					//		+ UpdateVideo.videoFile.getAbsolutePath());
					String str= SpUtils.getString(Sp_Key.VIDEO_FILENAME);
					if (!obj.equals(str)) {
						//先删除原来的
						String dir = FileUtils.getDir("huiyun");
						File videoFile = new File(dir);
						deleteAllFiles(videoFile);
						//在下载
						UpdateVideo.downLoadVideo(context);
						// 停止运行当前的Handler
						handle.removeMessages(MSG_WHAT_JUMP);
					} else {
						if (UpdateVideo.videoFile != null
								&& UpdateVideo.videoFile.exists()) {
							playVideo();
						} else
							UpdateVideo.downLoadVideo(context);
					}
				}
				UpdateVideo.setOnResultListener(null);
			}
		});
		UpdateVideo.getUrl();
	}

	
	
	 /**
	 *  Function:删除目录下得文件
	 * 
	 *  @author YangShao 2015年5月23日 下午2:28:27
	 *  @param root
	 */
	private void deleteAllFiles(File root) {  
        File files[] = root.listFiles();  
        if (files != null)  
            for (File f : files) {  
                if (f.isDirectory()) { // 判断是否为文件夹  
                    deleteAllFiles(f);  
                    try {  
                        f.delete();  
                    } catch (Exception e) {  
                    }  
                } else {  
                    if (f.exists()) { // 判断是否存在  
                        deleteAllFiles(f);  
                        try {  
                            f.delete();  
                        } catch (Exception e) {  
                        }  
                    }  
                }  
            }  
    }  
	public void playVideo() {
		iv_ads.setVisibility(View.GONE);
		video_ad.setVisibility(View.VISIBLE);
		video_ad.setVideoPath(UpdateVideo.videoFile.getAbsolutePath());
		video_ad.start();
	}

	/**
	 * Function:软件更新
	 * 
	 * @author YangShao 2015年4月30日 上午9:49:12
	 */
	private void downApk() {
		UpdateUtil.setOnResultListener(new OnResultListener<Version>() {
			@Override
			public void onResult(boolean isSecceed, Version obj) {
				if (isSecceed == true && null != obj) {
					int serviceCode = obj.getVersion_id();
					// 版本判断
					if (serviceCode > UpdateUtil.getVersionCode(context)) {
						// 弹出提示框警告
						UpdateDialog dlg = new UpdateDialog(context, "软件更新",
								obj.getDesc());
						// 停止运行当前的Handler
						handle.removeMessages(MSG_WHAT_JUMP);
						dlg.setConfirmListener(new OnResultListener<Object>() {
							@Override
							public void onResult(boolean isSecceed, Object obj) {
								if (isSecceed) {
									if (UpdateUtil.isExis()) {
										UpdateUtil.apkfile.delete();
									}
									UpdateUtil.downLoadNewApk(context);
								} else {
									Intent intent = new Intent(context,
											LoginActivity.class);
									startActivity(intent);
									VideoSplashActivity.this.finish();
								}
							}
						});
						dlg.show(getFragmentManager(), "");
					}
				}
				UpdateUtil.setOnResultListener(null);
			}
		});
		UpdateUtil.getVersion();
	}

	/**
	 * 准备跳转主页面
	 */
	private void preparetoJump() {
		// 跳转下一页
		Message message = Message.obtain();
		message.what = MSG_WHAT_JUMP;
		handle.sendMessageDelayed(message, 1000);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
			return true;
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);
		downVideo();
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
		MobclickAgent.onPause(this);
		video_ad.pause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "跳过");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 跳转下一页
		handle.removeMessages(MSG_WHAT_JUMP);
		seconds = 0;
		handle.sendEmptyMessage(MSG_WHAT_JUMP);
		return true;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		VideoSplashActivity.activity.finish();
	}
}
