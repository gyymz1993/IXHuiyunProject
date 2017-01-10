package com.ixhuiyunproject.huiyun.ixconfig.fragment.control;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.bean.ColorState;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.dialog.ColorPickerDialog;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.VibrateHelp;
import com.ixhuiyunproject.huiyun.ixconfig.view.LineGridView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class ContrlDeviceFragment extends Fragment {

	public View view;
	public GridViewAdpater adpater;
	private List<OutDevice> devices;
	private LineGridView gridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.contrl_gridview,
				container, false);
		initView(contextView);
		return contextView;
	}

	/**
	 * 
	 * Function: 初始化
	 * 
	 * @author 2015年1月19日 上午9:30:58
	 * @param v
	 */
	public void initView(View v) {
		gridView = (LineGridView) v.findViewById(R.id.brainheroall);
		adpater = new GridViewAdpater();
		gridView.setAdapter(adpater);
		/**
		 * 管理Adapter
		 */
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View item,
					int position, long arg3) {
				final MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
				final Map<String, String> map = new HashMap<String, String>();
				jsonobj.code = 27;
				jsonobj.obj = FinalValue.OBJ_MASTER;
				map.put("token", StaticValue.user.getToken());
				final GridViewAdpater.ViewHolder holder = (GridViewAdpater.ViewHolder) item.getTag();
				/**
				 * 单独调色灯
				 * /*Color.rgb(ColorState.getRed(holder.device.getState()),
				 * ColorState.getGreen(holder.device.getState()),
				 * ColorState.getBlue(holder.device.getState()))
				 */
				if (holder.device.getType() == OutDevice.TYPE_COLOR_LAMP) {
					int argb;
					if (holder.device.getState() == -1) {
						argb = Color.BLACK;
					} else {
						argb = ColorState.getARGBfromBRG(holder.device
								.getState());
					}
					ColorPickerDialog colorPickerDialog = new ColorPickerDialog(
							getActivity(), argb, "调色灯",
							new ColorPickerDialog.OnColorChangedListener() {
								@Override
								public void colorChanged(int color) {
									System.out.println("获取RGB"
											+ ColorState.getBRGfromARGB(color));
									// 振动
									VibrateHelp.vSimple(getActivity(), 60);
									map.put("state",
											ColorState.getBRGfromARGB(color)
													+ "");
									map.put("phoneCode",
											holder.device.getPhoneCode() + "");
									jsonobj.setData(map);
									NetJsonUtil.getInstance().addCmdForSend(
											jsonobj);
									adpater.notifyDataSetChanged();
								}
							});
					colorPickerDialog.show();
				} else {
					// 振动
					VibrateHelp.vSimple(getActivity(), 60);
					int state = holder.device.getState();
					if (state == 1) {
						map.put("state", 0 + "");
					} else {
						map.put("state", 1 + "");
					}
					map.put("phoneCode", holder.device.getPhoneCode() + "");
					jsonobj.setData(map);
					NetJsonUtil.getInstance().addCmdForSend(jsonobj);
					adpater.notifyDataSetChanged();
				}
			}
		});
	}


	class GridViewAdpater extends BaseAdapter {

		@Override
		public int getCount() {
			try {
				return devices.size();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return devices.get(position);
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
						R.layout.contrl_gridviewitem, null);
				holder = new ViewHolder();
				/** 得到各个控件的对象 */
				holder.bg = (ImageView) convertView
						.findViewById(R.id.img_contrl);
				holder.switc = (ImageView) convertView
						.findViewById(R.id.contrl_switch);
				holder.tx = (TextView) convertView
						.findViewById(R.id.contrl_name);
				convertView.setTag(holder);// 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
			}
			OutDevice device = devices.get(position);
			holder.device = device;// 将数据存入holder中
			/**
			 * 更具设备取得类型取得图片
			 */
			if (holder.device.getType() == OutDevice.TYPE_OUT) {
				holder.bg.setImageResource(R.drawable.contrl_light);
			}
			if (holder.device.getType() == OutDevice.TYPE_COLOR_LAMP) {
				holder.bg.setImageResource(R.drawable.contrl_color_light);
			}
			if (holder.device.getType() == OutDevice.TYPE_GATEWAY) {
				if (holder.device.getDetails().equals("灯")) {
					holder.bg.setImageResource(R.drawable.contrl_light);
				} else if (holder.device.getDetails().equals("日光灯")) {
					holder.bg.setImageResource(R.drawable.contrl_light);
				} else if (holder.device.getDetails().equals("灯带")) {
					holder.bg.setImageResource(R.drawable.contrl_light_belt);
				} else if (holder.device.getDetails().equals("筒灯")) {
					holder.bg.setImageResource(R.drawable.contrl_tube_light);
				} else if (holder.device.getDetails().equals("台灯")) {
					holder.bg.setImageResource(R.drawable.contrl_lamp);
				} else if (holder.device.getDetails().equals("吊灯")) {
					holder.bg.setImageResource(R.drawable.contrl_pendant);
				} else if (holder.device.getDetails().equals("落地灯")) {
					holder.bg.setImageResource(R.drawable.contrl_down_light);
				} else if (holder.device.getDetails().equals("调光膜")) {
					holder.bg.setImageResource(R.drawable.contrl_tv);
				} else if (holder.device.getDetails().equals("风机")) {
					holder.bg.setImageResource(R.drawable.contrl_fan);
				} else {
					holder.bg.setImageResource(R.drawable.contrl_light);
				}
			}
			if (holder.device.getState() == 0) {
				holder.switc.setImageResource(R.drawable.contrl_off);
			} else {
				holder.switc.setImageResource(R.drawable.contrl_on);
			}

			holder.tx.setText(device.getName());
			/** 设置TextView显示的内容，即我们存放在动态数组中的数据 */
			return convertView;
		}

		class ViewHolder {
			ImageView bg;
			ImageView switc;
			TextView tx;
			OutDevice device;

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + getOuterType().hashCode();
				result = prime * result
						+ ((device == null) ? 0 : device.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				ViewHolder other = (ViewHolder) obj;
				if (!getOuterType().equals(other.getOuterType()))
					return false;
				if (device == null) {
					if (other.device != null)
						return false;
				} else if (!device.equals(other.device))
					return false;
				return true;
			}

			private GridViewAdpater getOuterType() {
				return GridViewAdpater.this;
			}

		}

	}

	public ContrlDeviceFragment() {
		super();
	}

	public List<OutDevice> getDevices() {
		return devices;
	}

	public void setDevices(List<OutDevice> devices) {
		this.devices = devices;
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(FinalValue.mPageName);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(FinalValue.mPageName);
	}
}