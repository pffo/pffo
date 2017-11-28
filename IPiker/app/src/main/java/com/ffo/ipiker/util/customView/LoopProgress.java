package com.ffo.ipiker.util.customView;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ffo.ipiker.R;

public class LoopProgress extends View {

	// 短线的颜色，我们也可以开放一个setColor接口设置颜色
	private int color = R.color.yellow_FFD700;

	// 绘制短线的画笔
	private Paint paint;

	// 短线之间间隔
	private float space;

	// 短线的宽度
	private float lineWidth;

	// 绘制短线的起点
	private int startX;

	public LoopProgress(Context context) {
		this(context, null);
	}

	public LoopProgress(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		space = 80;
		lineWidth = 160;

		paint = new Paint();
		paint.setColor(Color.rgb(255,255,0));
		paint.setAntiAlias(true);
	}

	public LoopProgress(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 整个控件的高度和宽度当执行到onDraw时已经layout了宽高早已经测量完成
		int w = getMeasuredWidth();
		int h = getMeasuredHeight();

		// 每次重绘短线移动的距离
		int delta = 10;

		// 画笔的宽度
		paint.setStrokeWidth(h*3);

		// startX从0开始每次递增delta,当第一根短线（最右边一根）的右端到达空间末尾时从0重新开始绘制，形成循环
		if (startX >= w + (lineWidth + space) - (w % (lineWidth + space))) {
			startX = 0;
		} else {
			startX += delta;
		}

		float start = startX;

		// 绘制短线
		while (start < w) {
			canvas.drawLine(start, 5, start + lineWidth, 5, paint);
			start += (lineWidth + space);
		}

		start = startX - space - lineWidth;

		RectF rectFf = new RectF(start,5,lineWidth+start,5+h);

		// 从左边进入的短线
		while (start >= -lineWidth) {
			canvas.drawLine(start, 5, start + lineWidth, 5, paint);
			start -= (lineWidth + space);
		}

		// 延迟一定的时间重绘，否则太快了，如果要流畅的话1s至少得16帧即62ms一帧，这里设置为60还挺流畅的
		postInvalidateDelayed(60);
	}
}
