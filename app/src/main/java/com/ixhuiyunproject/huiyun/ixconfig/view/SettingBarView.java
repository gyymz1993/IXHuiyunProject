package com.ixhuiyunproject.huiyun.ixconfig.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;


/**   
* @Title: SettingBarView.java 
* @Package com.demo.custlistview 
* @Description: TODO(用一句话描述该文件做什么) 
* @author Yangshao  
* @date 2015年1月23日 上午11:36:06 
* @version V1.0   
*/
public class SettingBarView extends RelativeLayout {  
    /** 
     * 按钮模式 -1表示没有button ，
     * 0表示有两个button， 
     * 1表示只有leftButton，
     * 2表示只有rightButton 
     */  
    private int buttonMode;  
    private Button rightButton; 
    private ImageView leftButton;
    private TextView tvTitle;  
  
    /**
     * 左边的图标
     */
    private float leftMargin; 
	private ColorStateList leftTextColor;
	private float leftTextSize;
    private Drawable leftBackground;  
	private String leftText;
    private float leftWidth;
    private float leftHeight;  
  
    /**
     * 右边的图标
     */
    private float rightMargin;
    private ColorStateList rightTextColor;  
    private float rightTextSize;  
    private Drawable rightBackground;  
    private String rightText;  
    private float rightWidth;  
    private float rightHeight;  
  
    /**
     * 标题
     */
    private float titleTextSize;  
    private float titleMargin;
    private int titleTextColor;  
    private String title;  
  
    private LayoutParams leftParams, rightParams, titleParams;  
	private Drawable background;
  
    private SettingBarViewLeftClickListener leftListener;  
    private SettingBarViewRightClickListener rightListener;  
  
    public interface SettingBarViewLeftClickListener {  
        public void leftClick();  
    }  
  
    public interface SettingBarViewRightClickListener {  
        public void rightClick();  
    }  
  
    public void setOnSettingBarViewLeftClickListener(SettingBarViewLeftClickListener listener) {  
        this.leftListener = listener;  
    }  
  
    public void setOnSettingBarViewRightClickListener(SettingBarViewRightClickListener listener) {  
        this.rightListener = listener;  
    }  
  
    public SettingBarView(Context context) {  
        this(context, null);
        LogUtils.e("不能调用此方法，可能会报错");
    }  
  
    /**
     * 初始化
     * @param context
     * @param attrs
     */
    public SettingBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * 得到自定义属性
         */
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.SettingBarView);

        buttonMode = ta.getInteger(R.styleable.SettingBarView_buttonMode, -1);
        background = ta.getDrawable(R.styleable.SettingBarView_backgrounds);

        switch (buttonMode) {
        case 0:// 0表示有两个button
            initLeftButton(context, ta);
            initRightButton(context, ta);
            break;
        case 1:// 1表示只有leftButton
            initLeftButton(context, ta);
            break;
        case 2:// 2表示只有rightButton
            initRightButton(context, ta);
            break;
        default:// 表示没有button
            break;
        }

        initTitle(context, ta);
        ta.recycle();

       // setBackgroundDrawable(background);// 设置SettingBarView背景  
       
    }

    private void initTitle(Context context, TypedArray ta) {
    	 titleTextSize = ta.getDimension(R.styleable.SettingBarView_titleTextSize, 0);  
         titleTextColor = ta.getColor(R.styleable.SettingBarView_tTextColor, 0);
         title = ta.getString(R.styleable.SettingBarView_titles);  
         titleMargin = ta.getDimension(R.styleable.SettingBarView_titleMargin, 0);  
    	 tvTitle = new TextView(context);  
         tvTitle.setTextSize(titleTextSize);  
         tvTitle.setTextColor(titleTextColor);  
         tvTitle.setText(title);  
         tvTitle.setGravity(Gravity.CENTER);  
         titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT,  
                 LayoutParams.MATCH_PARENT);  
         titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
         if(0!=titleMargin){
        	 titleParams.setMargins((int) titleMargin, 0, 0, 0);
         }
         addView(tvTitle, titleParams);  
    }
  
    private void initLeftButton(Context context, TypedArray ta) {  
        leftTextColor = ta.getColorStateList(R.styleable.SettingBarView_leftTextColor);  
        leftTextSize = ta.getDimension(R.styleable.SettingBarView_leftTextSize, 0);  
        leftBackground = ta.getDrawable(R.styleable.SettingBarView_leftBackground);  
        leftText = ta.getString(R.styleable.SettingBarView_leftText);  
        leftWidth = ta.getDimension(R.styleable.SettingBarView_leftWidth, 0);  
        leftHeight = ta.getDimension(R.styleable.SettingBarView_leftHeight, 0);  
        leftMargin = ta.getDimension(R.styleable.SettingBarView_leftMargin, 0);  
        leftButton = new ImageView(context);  
        leftButton.setImageDrawable(leftBackground);  
  
        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT,  
                LayoutParams.WRAP_CONTENT);  
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);  
        leftParams.addRule(RelativeLayout.CENTER_VERTICAL);  
        if (0 != leftWidth) {  
            leftParams.width = (int) leftWidth;  
        }  
        if (0 != leftHeight) {  
            leftParams.height = (int) leftHeight;  
        }  
        if (0 != leftMargin) {  
            leftParams.setMargins((int) leftMargin, 0, 0, 0);  
        }  
  
        addView(leftButton, leftParams);  
  
        leftButton.setOnClickListener(new OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                if (leftListener != null)  
                    leftListener.leftClick();  
            }  
        });  
    }  
  
	private void initRightButton(Context context, TypedArray ta) {
        rightTextColor = ta  
                .getColorStateList(R.styleable.SettingBarView_rightTextColor);  
        rightTextSize = ta.getDimension(R.styleable.SettingBarView_rightTextSize, 0);  
        rightBackground = ta.getDrawable(R.styleable.SettingBarView_rightBackground);  
        rightText = ta.getString(R.styleable.SettingBarView_rightText);  
        rightWidth = ta.getDimension(R.styleable.SettingBarView_rightWidth, 0);  
        rightHeight = ta.getDimension(R.styleable.SettingBarView_rightHeight, 0);  
        rightMargin = ta.getDimension(R.styleable.SettingBarView_rightMargin, 0);  
  
        rightButton = new Button(context);  
  
        if (rightTextColor != null)  
            rightButton.setTextColor(rightTextColor);  
        rightButton.setBackgroundDrawable(rightBackground);  
        rightButton.setText(rightText);  
        if (0 != rightTextSize)  
            rightButton.setTextSize(rightTextSize);  
  
        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT,  
                LayoutParams.WRAP_CONTENT);  
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);  
        rightParams.addRule(RelativeLayout.CENTER_VERTICAL);  
        if (0 != rightWidth) {  
            rightParams.width = (int) rightWidth;  
        }  
        if (0 != rightHeight) {  
            rightParams.height = (int) rightHeight;  
        }  
        if (0 != rightMargin) {  
            rightParams.setMargins(0, 0, (int) rightMargin, 0);  
        }  
  
        addView(rightButton, rightParams);  
  
        rightButton.setOnClickListener(new OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                if (rightListener != null)  
                    rightListener.rightClick();  
            }  
        });  
    }  
  
    // 设置标题  
    public void setTitle(String title) {  
        tvTitle.setText(title);  
    }  
  
    public void setTitle(int resId) {  
        tvTitle.setText(resId);  
    }  
}  