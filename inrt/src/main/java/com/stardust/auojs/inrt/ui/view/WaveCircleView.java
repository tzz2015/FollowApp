package com.stardust.auojs.inrt.ui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import org.autojs.autoxjs.inrt.R;

public class WaveCircleView extends View {

    private Paint circlePaint;
    private Paint wavePaintTop;
    private Paint wavePaintBottom;
    private Path wavePathTop;
    private Path wavePathBottom;

    private float centerX;
    private float centerY;
    private float radius;

    private float maxWaveHeight = 150; // 波浪的最大高度
    private float currentWaveHeight = 0; // 当前波浪高度

    private RectF topRect = new RectF();
    private RectF bottomRect = new RectF();

    private int waveColorTop;
    private int waveColorBottom;

    private int waveColorCenter;
    // 设置裁剪路径为一个圆形
    private Path clipPath = new Path();

    private ValueAnimator waveAnimator;

    public WaveCircleView(Context context) {
        super(context);
        init(null);
    }

    public WaveCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WaveCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.FILL);

        wavePaintTop = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaintTop.setStyle(Paint.Style.FILL);

        wavePaintBottom = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaintBottom.setStyle(Paint.Style.FILL);

        wavePathTop = new Path();
        wavePathBottom = new Path();

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WaveCircleView);
            waveColorTop = a.getColor(R.styleable.WaveCircleView_waveColorTop, Color.BLUE);
            waveColorBottom = a.getColor(R.styleable.WaveCircleView_waveColorBottom, Color.GREEN);
            waveColorCenter = a.getColor(R.styleable.WaveCircleView_waveColorCenter, Color.RED);
            a.recycle();
        }

        // 创建波浪动画
        waveAnimator = ValueAnimator.ofFloat(0, maxWaveHeight);
        waveAnimator.setDuration(2000);
        waveAnimator.setInterpolator(new LinearInterpolator());
        waveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        waveAnimator.setRepeatMode(ValueAnimator.REVERSE);
        waveAnimator.addUpdateListener(animation -> {
            currentWaveHeight = (float) animation.getAnimatedValue();
            invalidate(); // 重绘 View
        });


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(w, h) / 2f;
        maxWaveHeight = radius / 3f;
        topRect.set(0f, 0f, w, h / 2f);
        bottomRect.set(0f, h / 2f, w, h * 1f);
        // 启动波浪动画
        waveAnimator.start();

        clipPath.reset();
        RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        clipPath.addOval(rectF, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.clipPath(clipPath);
        wavePaintTop.setColor(waveColorTop);
        canvas.drawRect(topRect, wavePaintTop);
        wavePaintBottom.setColor(waveColorBottom);
        canvas.drawRect(bottomRect, wavePaintBottom);

        // 绘制上半部分波浪
        wavePaintTop.setColor(waveColorCenter);
        drawWave(canvas, wavePathTop, wavePaintTop);
        // 绘制圆形
//        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // 绘制下半部分波浪
//        wavePaintBottom.setColor(waveColorBottom);
//        drawWave(canvas, wavePathBottom, wavePaintBottom);
    }

    private void drawWave(Canvas canvas, Path wavePath, Paint wavePaint) {
        wavePath.reset();
        wavePath.moveTo(centerX - radius, centerY);

        for (float x = centerX - radius; x < centerX + radius; x += 10) {
            float y = centerY + currentWaveHeight * (float) Math.sin((x - centerX) / radius * Math.PI);
            wavePath.lineTo(x, y);
        }

        wavePath.lineTo(centerX + radius, centerY);
        wavePath.lineTo(centerX - radius, centerY);
        wavePath.close();

        canvas.drawPath(wavePath, wavePaint);
    }
}
