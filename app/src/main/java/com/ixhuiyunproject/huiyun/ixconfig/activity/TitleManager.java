package com.ixhuiyunproject.huiyun.ixconfig.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentFactory;
import com.ixhuiyunproject.huiyun.ixconfig.utils.UIUtils;
import com.ixhuiyunproject.huiyun.ixconfig.view.PopMenu;
import com.ixhuiyunproject.huiyun.ixconfig.view.TitlePopMenu;


public class TitleManager {
	private HomeActivity activity;
	private ImageView iv_left;
	private ImageView iv_right;
	private TextView tv_title;

	/** 弹出菜单 */
	protected PopMenu popMenu;

	/** 标题菜单 */
	protected TitlePopMenu titlePopMenu;
	/** 提示标题可点击 */
	private ImageView iv_title_click;

	public TitleManager(HomeActivity homeactivity) {
		this.activity = homeactivity;
		popMenu = new PopMenu(activity);
		titlePopMenu = new TitlePopMenu(activity);
		iv_left = (ImageView) homeactivity.findViewById(R.id.iv_left);
		iv_right = (ImageView) homeactivity.findViewById(R.id.iv_right);
		tv_title = (TextView) homeactivity.findViewById(R.id.tv_title);
		iv_title_click = (ImageView) homeactivity
				.findViewById(R.id.iv_title_click);
	}

	/**
	 * 切换设备状态
	 * 
	 * @param type
	 */
	public void switchType(int type) {
		iv_title_click.setVisibility(View.INVISIBLE);
		switch (type) {
		case FragmentFactory.HOMEPAGE:// 主页
			switchToType_home();
			break;
		case FragmentFactory.SCENE:// 场景
			switchToType_SCENE();
			break;
		case FragmentFactory.REDRAY:// 红外
			switchToType_RedRay();
			break;
		case FragmentFactory.CONTRL:// 开关
			switchToType_device_control();
			break;
		case FragmentFactory.SETTING_MAIN:// 设置
			switchToType_setting_main();
			break;
		default:
			break;
		}
	}

	private void switchToType_SCENE() {
		tv_title.setText("场景控制");
		setLeftBtnMenu();
		setTitlePopMenu(false, null, null);
		if (StaticValue.isRemote()) {
			System.out.println("当前" + StaticValue.isRemote());
			iv_right.setVisibility(View.GONE);
		} else {
			iv_right.setVisibility(View.VISIBLE);
			iv_right.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					popMenu.showAsDropDown(v);
				}
			});
		}
	}

	/**
	 * 设置页面的标题
	 */
	private void switchToType_setting_main() {
		tv_title.setText("设置");
		setLeftBtnMenu();
		if (StaticValue.isRemote()) {
			iv_right.setVisibility(View.GONE);
		} else {
			iv_right.setVisibility(View.GONE);
			setTitlePopMenu(false, null, null);
		}
	}

	/**
	 * 跳转到设备控制页面
	 */
	private void switchToType_device_control() {
		tv_title.setText("控制页面");
		setLeftBtnMenu();
		setTitlePopMenu(false, null, null);
		if (StaticValue.isRemote()) {
			System.out.println("当前" + StaticValue.isRemote());
			iv_right.setVisibility(View.GONE);
		} else {
			iv_right.setVisibility(View.VISIBLE);
			iv_right.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					popMenu.showAsDropDown(v);
				}
			});
		}

	}

	/**
	 * 切换到红外的显示状态
	 */
	private void switchToType_RedRay() {
		setLeftBtnMenu();
		iv_right.setVisibility(View.VISIBLE);
		iv_right.setImageResource(R.drawable.menu);
		if (StaticValue.isRemote()) {
			System.out.println("当前" + StaticValue.isRemote());
			iv_right.setVisibility(View.GONE);
			iv_title_click.setVisibility(View.VISIBLE);
		} else {
			iv_title_click.setVisibility(View.VISIBLE);
			iv_right.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					popMenu.showAsDropDown(v);
				}
			});
		}
	}

	/**
	 * 切换标题为主页面状态
	 */
	private void switchToType_home() {
		tv_title.setText("主页");
		setLeftBtnMenu();
		iv_right.setVisibility(View.GONE);
		setTitlePopMenu(false, null, null);
	}

	/**
	 * 将左键设置成menu键
	 */
	private void setLeftBtnMenu() {
		iv_left.setImageResource(R.drawable.tv_setting);
		iv_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.menu.toggle();// 自动动画切换menu显示状态
			}
		});
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(final String title) {
		UIUtils.runInMainThread(new Runnable() {

			@Override
			public void run() {
				tv_title.setText(title);
			}
		});
	}

	/**
	 * 设置右边菜单的的文字 设置弹出菜单点击事件
	 * 
	 * @param textList
	 */
	public void setRightPopMenu(String[] textList,
			OnItemClickListener onItemClickListener) {
		popMenu.setItems(textList);
		popMenu.setOnItemClickListener(onItemClickListener);
	}

	/**
	 * 设置右边菜单的的文字 设置弹出菜单点击事件
	 * 
	 * @param enable
	 *            是否开启点击事件
	 * @param textList
	 * @param onItemClickListener
	 */
	public void setTitlePopMenu(boolean enable, String[] textList,
			OnItemClickListener onItemClickListener) {
		if (enable) {
			tv_title.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					titlePopMenu.showAsDropDown(v);
				}
			});
			try {
				titlePopMenu.setItems(textList);
				titlePopMenu.setOnItemClickListener(onItemClickListener);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			tv_title.setOnClickListener(null);
		}
	}

	/**
	 * 弹出的右菜单
	 */
	public void popMenuDismiss() {
		popMenu.dismiss();
	}

	/**
	 * 弹出的标题菜单
	 */
	public void titlepopMenuDismiss() {
		titlePopMenu.dismiss();
	}
}
