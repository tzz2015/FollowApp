package com.stardust.auojs.inrt.ui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.linsh.utilseverywhere.StringUtils;
import com.mind.lib.util.DensityUtil;

import org.autojs.autoxjs.inrt.R;

public class WaveCircleView extends View {

    private Paint circlePaint;
    private Paint wavePaintTop;
    private Paint wavePaintBottom;
    private Path wavePathTop = new Path();

    private float centerX;
    private float centerY;
    private float radius;

    private float maxWaveHeight = 100; // 波浪的最大高度
    private float currentWaveHeight = 0; // 当前波浪高度

    private RectF topRect = new RectF();
    private RectF bottomRect = new RectF();

    private int waveColorTop;
    private int waveColorBottom;

    private int waveColorCenter;
    // 设置裁剪路径为一个圆形
    private final Path clipPath = new Path();
    private final Path topTextPath = new Path();
    private final Path bottomTextPath = new Path();


    private TextPaint textPaint;

    private ValueAnimator waveAnimator;


    private String topText = "Top Text";
    private String bottomText = "Bottom Text";
    private float textPathLength;

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
        initPaint();
        initAttrs(attrs);
        initAnimator();
    }

    private void initAnimator() {
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

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            try {
                TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WaveCircleView);
                waveColorTop = a.getColor(R.styleable.WaveCircleView_waveColorTop, Color.BLUE);
                waveColorBottom = a.getColor(R.styleable.WaveCircleView_waveColorBottom, Color.GREEN);
                waveColorCenter = a.getColor(R.styleable.WaveCircleView_waveColorCenter, Color.RED);
                topText = a.getString(R.styleable.WaveCircleView_waveTextTop);
                bottomText = a.getString(R.styleable.WaveCircleView_waveTextBottom);
                a.recycle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initPaint() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.FILL);
        wavePaintTop = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaintTop.setStyle(Paint.Style.FILL);
        wavePaintBottom = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaintBottom.setStyle(Paint.Style.FILL);
        // 文本画笔
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(DensityUtil.INSTANCE.sp2px(18f, getContext()));
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(w, h) / 2f;
        topRect.set(0f, 0f, w, h / 2f);
        bottomRect.set(0f, h / 2f, w, h * 1f);
        // 启动波浪动画
        waveAnimator.start();
        // 裁剪圆形
        clipPath.reset();
        RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        clipPath.addOval(rectF, Path.Direction.CW);

        // 计算文字环绕圆
        topTextPath.reset();
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        float textRadius = radius - textHeight;
        RectF topTextRect = new RectF(centerX - textRadius, centerY - textRadius, centerX + textRadius, centerY + textRadius);
        topTextPath.addArc(topTextRect, 180, 180);
        PathMeasure pathMeasure = new PathMeasure(topTextPath, false);
        textPathLength = pathMeasure.getLength();
        bottomTextPath.reset();
        bottomTextPath.addArc(topTextRect, 0, 180);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 裁剪圆型
        canvas.clipPath(clipPath);
        wavePaintTop.setColor(waveColorTop);
        canvas.drawRect(topRect, wavePaintTop);
        wavePaintBottom.setColor(waveColorBottom);
        canvas.drawRect(bottomRect, wavePaintBottom);
        // 绘制上波浪
        wavePaintTop.setColor(waveColorCenter);
        drawWave(canvas, wavePathTop, wavePaintTop);

        // 绘制文字
        if(StringUtils.isNotAllEmpty(topText,bottomText)){
            float topWidth = textPaint.measureText(topText);
            float topTextHOffset = (textPathLength - topWidth) / 2;
            textPaint.setColor(waveColorBottom);
            canvas.drawTextOnPath(topText, topTextPath, topTextHOffset, 0, textPaint);
            float bottomWidth = textPaint.measureText(bottomText);
            float bottomTextHOffset = (textPathLength - bottomWidth) / 2;
            textPaint.setColor(waveColorTop);
            canvas.drawTextOnPath(bottomText, bottomTextPath, bottomTextHOffset, 0, textPaint);
        }
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (waveAnimator != null) {
            waveAnimator.cancel();
        }
    }
}
