package com.ixhuiyunproject.huiyun.ixconfig.fragment.control;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.ColorState;
import com.ixhuiyunproject.huiyun.ixconfig.bean.CurtainDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.dialog.ColorPickerDialog;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.VibrateHelp;
import com.ixhuiyunproject.huiyun.ixconfig.view.ContrlView;
import com.ixhuiyunproject.huiyun.ixconfig.view.WindowsView;
import com.umeng.analytics.MobclickAgent;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewContrlDeviceFragment extends Fragment {

	public View view;
	public ListViewAdapter adpater;
	private List<OutDevice> devices;
	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.new_contrl_main,
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
		listView = (ListView) v.findViewById(R.id.lv_contrl);
		adpater = new ListViewAdapter();
		listView.setAdapter(adpater);
	}

	class ListViewAdapter extends BaseAdapter {

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
		public int getItemViewType(int position) {
			int p = devices.get(position).getType();
			if (p == 14) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			final ViewHolder viewHolder;
			OutDevice device = devices.get(position);
			if (convertView == null) {
				if (getItemViewType(position) == 0) {
					convertView = View.inflate(getActivity(),
							R.layout.new_window_list_item, null);
					holder = new ViewHolder();
					/** 得到各个控件的对象 */
					holder.iv_logo = (ImageView) convertView
							.findViewById(R.id.switch_logo);
					holder.tx_name = (TextView) convertView
							.findViewById(R.id.switch_name);
					holder.btn_switch_on = (Button) convertView
							.findViewById(R.id.switch_on);
					holder.btn_switch_off = (Button) convertView
							.findViewById(R.id.switch_off);
					holder.windView = (WindowsView) convertView
							.findViewById(R.id.wv_contrl);
				} else {
					convertView = View.inflate(getActivity(),
							R.layout.new_contrl_list_item, null);
					holder = new ViewHolder();
					/** 得到各个控件的对象 */
					holder.iv_logo = (ImageView) convertView
							.findViewById(R.id.switch_logo);
					holder.tx_name = (TextView) convertView
							.findViewById(R.id.switch_name);
					holder.btn_switch = (Button) convertView
							.findViewById(R.id.switch_press);
					holder.tx_state = (TextView) convertView
							.findViewById(R.id.switch_state);
					holder.contrlView = (ContrlView) convertView
							.findViewById(R.id.cv_contrl);
				}
				convertView.setTag(holder);// 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
			}
			holder.device = device;// 将数据存入holder中
			/**
			 * 更具设备取得类型取得图片
			 */
			if (holder.device.getType() == OutDevice.TYPE_OUT) {
				holder.iv_logo.setImageResource(R.drawable.contrl_light);
			} else if (holder.device.getType() == OutDevice.TYPE_COLOR_LAMP) {
				holder.iv_logo.setImageResource(R.drawable.contrl_light_belt);
			} else if (holder.device.getType() == OutDevice.TYPE_WINDOW) {
				holder.iv_logo.setImageResource(R.drawable.contrl_window);
			} else if (holder.device.getType() == OutDevice.TYPE_GATEWAY) {
				if (holder.device.getDetails().equals("灯")) {
					holder.iv_logo.setImageResource(R.drawable.contrl_light);
				} else if (holder.device.getDetails().equals("日光灯")) {
					holder.iv_logo.setImageResource(R.drawable.contrl_dai);
				} else if (holder.device.getDetails().equals("灯带")) {
					holder.iv_logo
							.setImageResource(R.drawable.contrl_light_belt);
				} else if (holder.device.getDetails().equals("筒灯")) {
					holder.iv_logo
							.setImageResource(R.drawable.contrl_tube_light);
				} else if (holder.device.getDetails().equals("台灯")) {
					holder.iv_logo.setImageResource(R.drawable.contrl_lamp);
				} else if (holder.device.getDetails().equals("吊灯")) {
					holder.iv_logo.setImageResource(R.drawable.contrl_pendant);
				} else if (holder.device.getDetails().equals("落地灯")) {
					holder.iv_logo
							.setImageResource(R.drawable.contrl_down_light);
				} else if (holder.device.getDetails().equals("调光膜")) {
					holder.iv_logo.setImageResource(R.drawable.contrl_tv);
				} else if (holder.device.getDetails().equals("风机")) {
					holder.iv_logo.setImageResource(R.drawable.contrl_fan);
				} else {
					holder.iv_logo.setImageResource(R.drawable.contrl_light);
				}
			}

			holder.tx_name.setText(device.getName());
			viewHolder = holder;
			if (device.getType() == OutDevice.TYPE_WINDOW
					&& holder.btn_switch_off != null) {
				holder.btn_switch_off.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//System.out.println("btn_switch_off");
						CurtainDevice cd = (CurtainDevice) viewHolder.device;
						cd.setCurrentNumber(1);
						contrlDevice(viewHolder.device);
					}
				});
			}

			if (device.getType() == OutDevice.TYPE_WINDOW
					&& holder.btn_switch_on != null) {
				holder.btn_switch_on.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//System.out.println("btn_switch_on");
						CurtainDevice cd = (CurtainDevice) viewHolder.device;
						cd.setCurrentNumber(0);
						contrlDevice(viewHolder.device);
					}
				});
			}

			if (device.getType() != OutDevice.TYPE_WINDOW
					&& holder.btn_switch != null) {
				holder.btn_switch.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						contrlDevice(viewHolder.device);
					}
				});
				if (holder.device.getState() == 0) {
					holder.contrlView.setState(true);
				} else {
					holder.contrlView.setState(false);
				}
			}
			/** 设置TextView显示的内容，即我们存放在动态数组中的数据 */
			return convertView;
		}

		class ViewHolder {
			ImageView iv_logo;
			TextView tx_name;
			TextView tx_state;
			OutDevice device;
			Button btn_switch;
			Button btn_switch_on;
			Button btn_switch_off;
			ContrlView contrlView;
			WindowsView windView;

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

			private ListViewAdapter getOuterType() {
				return ListViewAdapter.this;
			}
		}

	}

	/**
	 */
	public void contrlDevice(final OutDevice device) {
		final MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
		final Map<String, String> map = new HashMap<String, String>();
		jsonobj.code = 27;
		jsonobj.obj = FinalValue.OBJ_MASTER;
		map.put("token", StaticValue.user.getToken());
		// 窗帘
		if (device.getType() == OutDevice.TYPE_WINDOW) {
			CurtainDevice cd = (CurtainDevice) device;
			if (cd.getCurrentNumber() == 1) {
				map.put("state", 1 + "");
				map.put("phoneCode",
						((CurtainDevice) device).getPhoneCodeSecond() + "");
			} else {
				map.put("state", 1 + "");
				map.put("phoneCode", device.getPhoneCode() + "");
			}
			// 振动
			VibrateHelp.vSimple(getActivity(), 60);
			jsonobj.setData(map);
			NetJsonUtil.getInstance().addCmdForSend(jsonobj);
			adpater.notifyDataSetChanged();
		}
		if (device.getType() == OutDevice.TYPE_COLOR_LAMP) { // 调色调
			int argb;
			if (device.getState() == -1) {
				argb = Color.BLACK;
			} else {
				argb = ColorState.getARGBfromBRG(device.getState());
			}
			ColorPickerDialog colorPickerDialog = new ColorPickerDialog(
					getActivity(), argb, "调色灯", new ColorPickerDialog.OnColorChangedListener() {
						@Override
						public void colorChanged(int color) {
							System.out.println("获取RGB"
									+ ColorState.getBRGfromARGB(color));
							// 振动
							VibrateHelp.vSimple(getActivity(), 60);
							map.put("state", ColorState.getBRGfromARGB(color)
									+ "");
							map.put("phoneCode", device.getPhoneCode() + "");
							jsonobj.setData(map);
							NetJsonUtil.getInstance().addCmdForSend(jsonobj);
							adpater.notifyDataSetChanged();
						}
					});
			colorPickerDialog.show();
		} else {
			int state = device.getState();
			//System.out.println("当前" + state);
			if (state == 1) {
				map.put("state", 0 + "");
			} else {
				map.put("state", 1 + "");
			}
			map.put("phoneCode", device.getPhoneCode() + "");
			// 振动
			VibrateHelp.vSimple(getActivity(), 60);
			jsonobj.setData(map);
			NetJsonUtil.getInstance().addCmdForSend(jsonobj);
			adpater.notifyDataSetChanged();
		}

	}

	public NewContrlDeviceFragment() {
		super();
	}

	public List<OutDevice> getDevices() {
		return devices;
	}

	public void setDevices(List<OutDevice> devices) {
		this.devices = copyOf(devices);
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

	/**
	 * 重新复制一份
	 * 
	 * @param origin
	 * @return
	 */
	private List<OutDevice> copyOf(List<OutDevice> origin) {
		List<OutDevice> copy = new ArrayList<OutDevice>();
		for (OutDevice dev : origin) {
			if (dev.getType() == OutDevice.TYPE_WINDOW) {
				boolean exist = false;
				for (OutDevice d : copy) {
					if (d.getAddress() == dev.getAddress()) {
						if (dev.getNumber() == 1) {
							CurtainDevice cd = (CurtainDevice) d;
							cd.setNumberSecond(dev.getNumber());
							cd.setPhoneCodeSecond(dev.getPhoneCode());
							cd.setStateSecond(dev.getState());
						} else {
							d.setNumber(dev.getNumber());
							d.setPhoneCode(dev.getPhoneCode());
							d.setState(dev.getState());
						}
						exist = true;
						break;
					}
				}
				if (!exist) {
					OutDevice d = new CurtainDevice();
					d.setAddress(dev.getAddress());
					d.setName(dev.getName());
					d.setDetails(dev.getDetails());
					d.setType(dev.getType());

					if (d.getNumber() == 1) {
						CurtainDevice cd = (CurtainDevice) d;
						cd.setNumberSecond(dev.getNumber());
						cd.setPhoneCodeSecond(dev.getPhoneCode());
						cd.setStateSecond(dev.getState());
					} else {
						d.setNumber(dev.getNumber());
						d.setPhoneCode(dev.getPhoneCode());
						d.setState(dev.getState());
					}
					copy.add(d);
				}
			} else {
				copy.add(dev);
			}
		}
		return copy;
	}
}