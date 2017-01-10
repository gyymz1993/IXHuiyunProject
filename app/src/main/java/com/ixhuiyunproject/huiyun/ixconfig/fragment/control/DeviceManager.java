package com.ixhuiyunproject.huiyun.ixconfig.fragment.control;

import com.ixhuiyunproject.huiyun.ixconfig.bean.InDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.Slave;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;



public class DeviceManager {
	public static List<OutDevice> outDeviceList = new ArrayList<OutDevice>();
	public static List<OutDevice> gatewayList = new ArrayList<OutDevice>();
	public static List<InDevice> inDeviceList = new ArrayList<InDevice>();

	public static List<Slave> slaveList = new ArrayList<Slave>();

	/**
	 * Function: 得到区域的设备
	 * 
	 * @author Yangshao 2015年1月21日 上午9:15:17
	 * @param area
	 */
	public static List<OutDevice> getDeviceForArea(String area) {
		List<OutDevice> devices = new ArrayList<OutDevice>();
		if (outDeviceList.size() != 0) {
			for (OutDevice d : outDeviceList) {
				if (!StringUtils.isEmpty(d.getArea())) {
					if (area.equals(d.getArea())) {
						devices.add(d);
					}
				}
			}
		}
//		for (int i = 0; i < 2; i++) {
//			OutDevice device = new OutDevice();
//			device.setType(14);
//			device.setArea("公共");
//			device.setDetails("窗帘");
//			devices.add(device);
//		}
		return devices;
	}

	/**
	 * 获得所有设备的类别
	 */
	public static List<String> getAllDetails(List<OutDevice> allDevices) {
		boolean exists;
		List<String> allDetails = new ArrayList<String>();
		// 遍历所有设备
		for (OutDevice dev : allDevices) {
			exists = false;
			// 遍历所有已存在的类别
			for (String details : allDetails) {
				if (details.equals(dev.getDetails())) {
					exists = true;
					continue;
				}
			}
			// 遍历后发现不存在，于是添加进去
			if (!exists) {
				allDetails.add(dev.getDetails());
			}
		}
		return allDetails;
	}

	/**
	 * 根据类别得到设备
	 * 
	 * @param details
	 * @return
	 */
	public static List<OutDevice> getDeviceForDetails(String details,
			List<OutDevice> allDevices) {
		List<OutDevice> devices = new ArrayList<OutDevice>();
		if (allDevices.size() != 0) {
			for (OutDevice d : allDevices) {
				if (details.equals(d.getDetails())) {
					devices.add(d);
				}
			}
		}
		return devices;
	}
}
