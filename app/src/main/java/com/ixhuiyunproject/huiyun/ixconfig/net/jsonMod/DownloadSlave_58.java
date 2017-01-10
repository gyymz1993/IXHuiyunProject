package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;

import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.Slave;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DownloadSlave_58 extends BaseJsonModule<List<Slave>>{
	
	private static final int DOWNLOAD_SLAVE = 58;
	
	List<Slave> allSlave=new ArrayList<Slave>();
	
	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& DOWNLOAD_SLAVE == baseJsonObj.getCode()) {
			downLoadSlave(baseJsonObj);
		}
	
	}
	 /**
	  *  Function:
	  *  @author Yangshao 
	  *  2015年1月10日 下午3:07:03
	  *  @param baseJsonObj
	  */
	private void downLoadSlave(BaseJsonObj baseJsonObj) {
		allSlave.clear();
		if (baseJsonObj.result != 1) {
			notifyListeners(false, null);
			return;
		}
		MyJsonObj2 myjsonObj = null;
		if (baseJsonObj instanceof MyJsonObj2) {
			myjsonObj = (MyJsonObj2) baseJsonObj;
		} else
			return;
		List<Map<String, String>> keymaps= myjsonObj.data.list;
		for(Map<String,String> map:keymaps){
			Slave slave=new Slave();
			slave.setAddr(Integer.valueOf(map.get("address")));
			slave.setType(Integer.valueOf(map.get("type")));
			allSlave.add(slave);
		}
		if(allSlave.size()!=0){
			notifyListeners(true, allSlave);
		}else{
			notifyListeners(false, null);
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Slave> getData() {
		return allSlave;
	}

}
