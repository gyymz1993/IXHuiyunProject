package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;

import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.RedRay;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.Map;

/**
 * 红外控制结果
 * 
 * @author lzy_torah
 * 
 */
public class Contrl_InfraredResult_12 extends BaseJsonModule<RedRay> {

	private static final int CODE_Contrl = 12;
	private RedRay rayCode;

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (baseJsonObj.result != 1) {
			if (baseJsonObj.result == 2) {
				ToastUtils.showToastReal("该按键未学习");
			}
			notifyListeners(false, null);
			return;
		}
		if (baseJsonObj instanceof MyJsonObj1) {
			MyJsonObj1 myJsonObj = (MyJsonObj1) baseJsonObj;
			try {
				if (FinalValue.OBJ_PHONE.equals(myJsonObj.obj)
						&& CODE_Contrl == myJsonObj.code) {
					Map<String, String> map = myJsonObj.getData();
					rayCode = new RedRay();
					rayCode.setBtn_code(Integer.valueOf(map.get("btn_code")));
					rayCode.setPageType(Integer.valueOf(map.get("pageType")));
					rayCode.setR_name(map.get("r_name"));
					notifyListeners(true, rayCode);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("参数错误");
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public RedRay getData() {
		if (rayCode != null)
			return rayCode;
		return null;
	}

}