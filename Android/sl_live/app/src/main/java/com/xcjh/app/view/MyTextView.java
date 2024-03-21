package com.xcjh.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Administrator
 */
public class MyTextView extends View {
        /**
         * 需要绘制的文字
         */
        private String mText;
        /**
         * 文本的颜色
         */
        private int mTextColor;
        /**
         * 文本的大小
         */
        private int mTextSize;
        /**
         * 绘制时控制文本绘制的范围
         */
        private Rect mBound;
        private Paint mPaint;

        public MyTextView(Context context) {
            this(context, null);
        }
        public MyTextView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }
        public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            //初始化
            mText = "Udf32fA";
            mTextColor = Color.BLACK;
            mTextSize = 100;

            mPaint = new Paint();
            mPaint.setTextSize(mTextSize);
            mPaint.setColor(mTextColor);
            //获得绘制文本的宽和高
            mBound = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), mBound);
        }
        //API21
//    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init();
//    }

        @Override
        protected void onDraw(Canvas canvas) {
            //绘制文字
            canvas.drawText(mText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
        }
    }

