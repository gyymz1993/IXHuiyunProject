package com.ixhuiyunproject.huiyun.ixconfig.fragment.scene;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.HomeActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.TitleManager;
import com.ixhuiyunproject.huiyun.ixconfig.activity.scene.SceneSettingActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj3;
import com.ixhuiyunproject.huiyun.ixconfig.bean.SceneItem;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentContainer;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.RandomOfImageBg;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.UIUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.VibrateHelp;
import com.ixhuiyunproject.huiyun.ixconfig.view.LineGridView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneFragment extends Fragment {
	private List<SceneItem> lstImageItem;
	private LineGridView gridView;
	private GridViewAdpater adapter;
	private TitleManager fragManager;
	public static SceneFragment sceneFragment;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.scene_gridview, container, false);
		sceneFragment=this;
		initSceneGridView(view);
		initActionBar();
		return view;
	}
	
	/**
	 * 
	 *  Function:刷新界面
	 *  @author Howard  DateTime 
	 *  2015年1月30日 下午3:30:24
	 *  @param items
	 */
	public static void sendMessage(List<SceneItem> items){
		Message message=Message.obtain();
		message.what=1;
		message.obj=items;
		if(sceneFragment!=null){
			sceneFragment.handler.sendMessage(message);
		}
	}
	
	Handler handler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			if(msg.what==1&&adapter!=null){
				setLstImageItem(((List<SceneItem>) msg.obj));
				adapter.notifyDataSetChanged();
			}
		};
	};

	/**
	 * 初始化标题
	 * 
	 */
	private void initActionBar() {
		fragManager = HomeActivity.homeActivity.getTitleManager();
		fragManager.setRightPopMenu(new String[] { "添加" },
				new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long id) {
						fragManager.popMenuDismiss();
						switch (position) {
						case 0:
							// System.out.println("传入区域" + current);
							Intent intent = new Intent();
							intent.setClass(getActivity(),
									SceneSettingActivity.class);
							startActivity(intent);
							break;
						case 1:
							break;
						}
					}
				});
		fragManager.setTitlePopMenu(false, null, null);
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
		
		MobclickAgent.onPageStart(FinalValue.mPageName);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(FinalValue.mPageName);
	}

	
	/**
	*  Function:初始化GridView
	*  @author Howard  DateTime 
	*  2015年1月23日 下午7:02:26
	*  @param view
	*/
	private void initSceneGridView(View view) {
		if (FragmentContainer.SCENE_LIST.size() == 0) {
			//ToastUtils.showToastReal("请添加场景或刷新界面");
			System.out.println("请添加场景或刷新界面");
		}
		gridView = (LineGridView) view.findViewById(R.id.scene_gridview);
		lstImageItem = FragmentContainer.SCENE_LIST;
		adapter = new GridViewAdpater();
		gridView.setAdapter(adapter);
		/**
		 * 场景控制
		 */
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				try {
					// 振动
					VibrateHelp.vSimple(getActivity(), 60);
					GridViewAdpater.ViewHolder holder = (GridViewAdpater.ViewHolder) view.getTag();
					MyJsonObj1 jsonObj = JsonUtil.getAJsonObj1ForMaster();
					Map<String, String> maps = new HashMap<String, String>();
					System.out.println(holder.sItem.getScene_name());
					maps.put("scene_name", holder.sItem.getScene_name());
					maps.put("token", StaticValue.user.getToken());
					jsonObj.code = 55;
					jsonObj.setData(maps);
					NetJsonUtil.getInstance().addCmdForSend(jsonObj);
				} catch (Exception e) {
					ToastUtils.showToastReal("错误\r\n请重新登录");
					e.printStackTrace();
				}
			}
		});

		/*
		 * 长按事件
		 */
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					final int position, long arg3) {
				// System.out.println("长按"+lstImageItem.get(position).get("ItemName"));
				final GridViewAdpater.ViewHolder holder = (GridViewAdpater.ViewHolder) view.getTag();
				// holder.delete.setVisibility(View.VISIBLE);
				// 创建退出对话框
				AlertDialog isExit = new AlertDialog.Builder(getActivity())
						.create();
				// 设置对话框标题
				isExit.setTitle("善意提示!");
				// 设置对话框消息
				isExit.setMessage("确定要删除吗?");
				// 添加选择按钮并注册监听
				isExit.setButton("删除", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						System.out.println("删除" + holder.sItem.getScene_name());
						MyJsonObj3 jsonObj = JsonUtil.getAJsonObj3ForMaster();
						jsonObj.code = 53;
						MyJsonObj3.Data3 data = new MyJsonObj3.Data3();
						Map<String, String> info = new HashMap<String, String>();
						info.put("scene_name", holder.sItem.getScene_name());
						info.put("token", StaticValue.user.getToken());
						info.put("image_bg", holder.sItem.getImage_bg() + "");
						data.list = null;
						data.info = info;
						jsonObj.setData(data);
						NetJsonUtil.getInstance().addCmdForSend(jsonObj);
						JSONModuleManager.getInstance().result_54
								.setOnCmdReseivedListener(new OnResultListener<Object>() {
									@Override
									public void onResult(boolean isSecceed,
											Object obj) {
										if (isSecceed) {
											System.out.println("-----------");
											UIUtils.runInMainThread(new Runnable() {
												@Override
												public void run() {
													lstImageItem
															.remove(position);
													adapter.notifyDataSetChanged();
												}
											});
											// gridView.setAdapter(adapter);
										}
									}
								});

					}
				});
				isExit.setButton2("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						System.out.println("取消");
					}
				});
				// 显示对话框
				isExit.show();
				return true;
			}
		});
	}

	public class GridViewAdpater extends BaseAdapter {

		@Override
		public int getCount() {
			return lstImageItem.size();
		}

		@Override
		public Object getItem(int position) {
			return lstImageItem.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(getActivity(),
						R.layout.scene_gridviewitem, null);
				holder = new ViewHolder();
				/** 得到各个控件的对象 */
				holder.bg = (ImageView) convertView
						.findViewById(R.id.scene_img_bg);
				holder.tx = (TextView) convertView
						.findViewById(R.id.scene_tv_name);
				holder.delete = (Button) convertView
						.findViewById(R.id.scene_delete);
				convertView.setTag(holder);// 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
			}
			SceneItem item = lstImageItem.get(position);
			holder.sItem = item;

			/*holder.bg.setImageResource(Integer.valueOf(
					lstImageItem.get(position).get("Itembg").toString()));*/
			holder.bg.setImageResource(RandomOfImageBg.getSceneImage(item
					.getImage_bg()));
			holder.tx.setText(item.getScene_name());
			// OutDevice device = lstImageItem.get(position);
			// holder.device = device;// 将数据存入holder中
			// holder.tx.setText(device.getName());
			/** 设置TextView显示的内容，即我们存放在动态数组中的数据 */
			return convertView;
		}

		class ViewHolder {
			ImageView bg;
			ImageView icon;
			TextView tx;
			Button delete;
			SceneItem sItem;
		}

	}

	/**
	 * 避免下次进入报错
	 */
	public void onDetach() {
		super.onDetach();
		// lstImageItem.clear();
	}

	/**
	 * @return lstImageItem
	 */
	public List<SceneItem> getLstImageItem() {
		return lstImageItem;
	}

	/**
	 * @param lstImageItem 要设置的 lstImageItem
	 */
	public void setLstImageItem(List<SceneItem> lstImageItem) {
		this.lstImageItem = lstImageItem;
	}

	

}
