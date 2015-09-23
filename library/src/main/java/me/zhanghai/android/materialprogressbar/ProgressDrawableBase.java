/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;

import me.zhanghai.android.materialprogressbar.internal.ThemeUtils;

abstract class ProgressDrawableBase extends Drawable implements IntrinsicPaddingDrawable {

    protected boolean mUseIntrinsicPadding = true;
    protected int mAlpha = 0xFF;
    protected ColorFilter mColorFilter;
    protected ColorStateList mTintList;
    protected PorterDuff.Mode mTintMode = PorterDuff.Mode.SRC_IN;
    protected PorterDuffColorFilter mTintFilter;

    private Paint mPaint;

    public ProgressDrawableBase(Context context) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAlpha(int alpha) {
        if (mAlpha != alpha) {
            mAlpha = alpha;
            invalidateSelf();
        }
    }

    /**
     * {@inheritDoc}
     */
    // Rewrite for compatibility.
    @Override
    public void setTint(int tint) {
        setTintList(ColorStateList.valueOf(tint));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ColorFilter getColorFilter() {
        return mColorFilter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mColorFilter = colorFilter;
        invalidateSelf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTintList(ColorStateList tintList) {
        mTintList = tintList;
        mTintFilter = makeTintFilter(mTintList, mTintMode);
        invalidateSelf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTintMode(@NonNull PorterDuff.Mode tintMode) {
        mTintMode = tintMode;
        mTintFilter = makeTintFilter(mTintList, mTintMode);
        invalidateSelf();
    }

    private PorterDuffColorFilter makeTintFilter(ColorStateList tint, PorterDuff.Mode tintMode) {

        if (tint == null || tintMode == null) {
            return null;
        }

        int color = tint.getColorForState(getState(), Color.TRANSPARENT);
        // They made PorterDuffColorFilter.setColor() and setMode() @hide.
        return new PorterDuffColorFilter(color, tintMode);
    }

    private boolean needMirroring() {
        return DrawableCompat.isAutoMirrored(this)
                && DrawableCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Canvas canvas) {

        Rect bounds = getBounds();
        if (bounds.width() == 0 || bounds.height() == 0) {
            return;
        }

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.BLACK);
            onPreparePaint(mPaint);
        }
        mPaint.setAlpha(mAlpha);
        ColorFilter colorFilter = mColorFilter != null ? mColorFilter : mTintFilter;
        mPaint.setColorFilter(colorFilter);

        int saveCount = canvas.save();

        canvas.translate(bounds.left, bounds.top);
        if (needMirroring()) {
            canvas.translate(bounds.width(), 0);
            canvas.scale(-1, 1);
        }

        onDraw(canvas, bounds.width(), bounds.height(), mPaint);

        canvas.restoreToCount(saveCount);
    }

    abstract protected void onPreparePaint(Paint paint);

    abstract protected void onDraw(Canvas canvas, int width, int height, Paint paint);
}
