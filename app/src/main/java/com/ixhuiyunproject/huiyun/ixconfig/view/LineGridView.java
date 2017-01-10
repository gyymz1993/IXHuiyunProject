package com.ixhuiyunproject.huiyun.ixconfig.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;


public class LineGridView extends GridView {
	public LineGridView(Context context) {
		super(context);
	}

	public LineGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LineGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		try {
			View localView1 = getChildAt(0);
			int column = getWidth() / localView1.getWidth();
			int childCount = getChildCount();
			Paint localPaint;
			localPaint = new Paint();
			localPaint.setStyle(Paint.Style.STROKE);

			localPaint.setColor(getContext().getResources().getColor(
					R.color.grid_line));
			for (int i = 0; i < childCount; i++) {
				View cellView = getChildAt(i);
				if ((i + 1) % column == 0) {
					canvas.drawLine(cellView.getLeft(), cellView.getBottom(),
							cellView.getRight(), cellView.getBottom(),
							localPaint);
				} else if ((i + 1) > (childCount - (childCount % column))) {
					canvas.drawLine(cellView.getRight(), cellView.getTop(),
							cellView.getRight(), cellView.getBottom(),
							localPaint);
				} else {
					canvas.drawLine(cellView.getRight(), cellView.getTop(),
							cellView.getRight(), cellView.getBottom(),
							localPaint);
					canvas.drawLine(cellView.getLeft(), cellView.getBottom(),
							cellView.getRight(), cellView.getBottom(),
							localPaint);
				}
			}
			if (childCount % column != 0) {
				for (int j = 0; j < (column - childCount % column); j++) {
					View lastView = getChildAt(childCount - 1);
					canvas.drawLine(lastView.getRight() + lastView.getWidth()
							* j, lastView.getTop(), lastView.getRight()
							+ lastView.getWidth() * j, lastView.getBottom(),
							localPaint);
				}
			}
		} catch (Exception e) {
//			e.printStackTrace();
			LogUtils.w("gridView划线有异常，可不处理");
		}
	}
}