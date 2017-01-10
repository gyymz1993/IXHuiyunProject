package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;

import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.EntityBase;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来处理命令，存储处理后的数据,不可在ModuleManager外面初始化此类的子类
 * 
 * @author torahs
 *
 * @param <T>
 */
public abstract class BaseJsonModule<T> {

	/**
	 * 返回成功 失败
	 */
	public int RE_RESULT_SUCCESS = 1;
	public int RE_RESULT_FAIL = 2;

	protected OnResultListener<T> listener;

	public abstract void handleCMDdata(BaseJsonObj baseJsonObj);

	/**
	 * 得到一段时间内的所有结果
	 * 
	 * @return
	 */
	public abstract <K> K getData();

	/**
	 * 每次有命令处理时都会通知
	 * 
	 * @param listener
	 */
	public void setOnCmdReseivedListener(OnResultListener<T> listener) {
		this.listener = listener;
	}

	/**
	 * 复制一个完全不相干的集合
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <R extends EntityBase> List<R> cloneList(List<R> list) {
		ArrayList<R> arrayList = new ArrayList<R>();
		for (R r : list) {
			R d1 = null;
			try {
				d1 = (R) r.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			arrayList.add(d1);
		}
		return arrayList;
	}

	/**
	 * 通知监听者
	 */
	protected void notifyListeners(boolean isSucceed,T obj) {
		if(listener!=null){
			listener.onResult(isSucceed, obj);
		}
	}
}
