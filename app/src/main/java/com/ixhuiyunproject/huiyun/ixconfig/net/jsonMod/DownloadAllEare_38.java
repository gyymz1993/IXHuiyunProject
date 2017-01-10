package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 所有区域下载
 * @date 2015年1月10日 下午5:43:06
 * @version V1.0
 */
public class DownloadAllEare_38 extends BaseJsonModule<List<String>> {

	private final int DOWN_EAEW = 38;
	List<String> eares=new ArrayList<String>();
	List<Map<String, String>> keymaps;
	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& DOWN_EAEW == baseJsonObj.getCode()) {
			downEareResult(baseJsonObj);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getData() {
		return eares;
	}


	/**
	 * Function:
	 * 
	 * @author Yangshao 2015年1月10日 上午9:53:18
	 */
	private void downEareResult(BaseJsonObj jsonObj) {
		eares.clear();
		if (jsonObj.result != 1) {
			notifyListeners(false, null);
			return;
		}
		MyJsonObj2 myjsonObj = null;
		if (jsonObj instanceof MyJsonObj2) {
			myjsonObj = (MyJsonObj2) jsonObj;
		} else
			return;
		keymaps= myjsonObj.data.list;
		for(Map<String,String> map:keymaps){
			eares.add(map.get("area"));
		}
		if(eares.size()!=0){
			notifyListeners(true, eares);
		}else{
			notifyListeners(false, null);
		}
	}

}
