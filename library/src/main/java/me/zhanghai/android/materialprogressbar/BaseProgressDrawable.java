/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import me.zhanghai.android.materialprogressbar.internal.ThemeUtils;

abstract class BaseProgressDrawable extends BaseDrawable implements IntrinsicPaddingDrawable {

    protected boolean mUseIntrinsicPadding = true;

    private Paint mPaint;

    @SuppressLint("NewApi")
    public BaseProgressDrawable(Context context) {
        int colorControlActivated = ThemeUtils.getColorFromAttrRes(R.attr.colorControlActivated,
                context);
        // setTint() has been overridden for compatibility; DrawableCompat won't work because
        // wrapped Drawable won't be Animatable.
        setTint(colorControlActivated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getUseIntrinsicPadding() {
        return mUseIntrinsicPadding;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUseIntrinsicPadding(boolean useIntrinsicPadding) {
        if (mUseIntrinsicPadding != useIntrinsicPadding) {
            mUseIntrinsicPadding = useIntrinsicPadding;
            invalidateSelf();
        }
    }

    @Override
    protected final void onDraw(Canvas canvas, int width, int height) {

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.BLACK);
            onPreparePaint(mPaint);
        }
        mPaint.setAlpha(mAlpha);
        mPaint.setColorFilter(getColorFilterForDrawing());

        onDraw(canvas, width, height, mPaint);
    }

    protected abstract void onPreparePaint(Paint paint);

    protected abstract void onDraw(Canvas canvas, int width, int height, Paint paint);
}
