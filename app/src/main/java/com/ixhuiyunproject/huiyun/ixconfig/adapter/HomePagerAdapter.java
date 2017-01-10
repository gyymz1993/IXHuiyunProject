package com.ixhuiyunproject.huiyun.ixconfig.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.utils.UIUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * 供主页的ViewPager使用的适配器
 * @author lzn
 *
 */
public class HomePagerAdapter extends PagerAdapter {

	public static class ImageRes {
		public static final int TYPE_IMAGE_RESID = 1; // 通过本地资源加载的图片
		public static final int TYPE_IMAGE_URL   = 2; // 通过URL加载的图片
		
		private int type; // 图片类型
		private int imageResId; // 图片资源ID,当type为TYPE_IMAGE_RESID时有效
		private String imageUrl; // 图片URL,当type为TYPE_IMAGE_URL时有效
		private String url; // 图片被点击后进入的URL,可以为空
		
		/**
		 * 通过本地资源id加载的图片
		 * @param imageResId
		 * @param url
		 */
		public ImageRes(int imageResId, String url){
			type = TYPE_IMAGE_RESID;
			this.imageResId = imageResId;
			this.url = url;
		}
		
		/**
		 * 通过URL加载的图片
		 * @param imageUrl
		 * @param url
		 */
		public ImageRes(String imageUrl, String url){
			type = TYPE_IMAGE_URL;
			this.imageUrl = imageUrl;
			this.url = url;
		}

		public int getType() {
			return type;
		}

		public int getImageResId() {
			return imageResId;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public String getUrl() {
			return url;
		}
	}
	
	// 图片资源列表
	private List<ImageRes> listImageRes;
	// URL图片加载设置
	private DisplayImageOptions options;
	// 视图容器
	private SparseArray<View> viewContainer;
	// 布尔值,判断当前的容器是否需要清空并重新加载
	private boolean reload;
	
	public HomePagerAdapter(){
		listImageRes = new ArrayList<ImageRes>();
		viewContainer = new SparseArray<View>();
		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.home_adpic1)
			.showImageOnFail(R.drawable.home_adpic2)
			.resetViewBeforeLoading(true)
			.cacheOnDisk(true)
			.imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.ARGB_8888)
			.considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(300))
			.build();
		reload = false;
	}
	
	/**
	 * 重新加载图片资源列表
	 * @param listImageRes
	 */
	public void loadImageResList(List<ImageRes> listImageRes){
		this.listImageRes = listImageRes;
		reload = true;
	}
	
	@Override
	public int getCount() {
		return listImageRes.size();
	}

	@Override
	public boolean isViewFromObject(View v, Object obj) {
		return v == obj;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		// 需要重新加载
		if(reload){
			viewContainer.clear();
			container.removeAllViews();
			reload = false;
		}
		
		// 若该视图已存在,直接从容器中获取
		if(viewContainer.get(position) != null){
			return viewContainer.get(position);
		}
		
		// 若该视图不存在,重新生成一个并加载到容器中
		View view = View.inflate(UIUtils.getContext(), R.layout.image_home_ad, null);
		ImageView ivAdvert = (ImageView) view.findViewById(R.id.iv_advert);
		
		// 根据类型进行不同操作
		ImageRes ir = listImageRes.get(position);
		switch(ir.getType()){
		case ImageRes.TYPE_IMAGE_RESID:
			ivAdvert.setBackgroundResource(ir.getImageResId());
			break;
		case ImageRes.TYPE_IMAGE_URL:
			ImageLoader.getInstance().displayImage(
					ir.getImageUrl(),ivAdvert,options);
			break;
		}
		
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("被点击的是" + position);
			}
		});
		
		viewContainer.append(position, view);
		container.addView(view);
		
		return view;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// do nothing
	}
}
