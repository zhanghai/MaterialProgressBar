/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Keep;

import me.zhanghai.android.materialprogressbar.internal.ThemeUtils;

/**
 * A backported {@code Drawable} for indeterminate horizontal {@code ProgressBar}.
 */
public class IndeterminateHorizontalProgressDrawable extends BaseIndeterminateProgressDrawable
        implements MaterialProgressDrawable, ShowBackgroundDrawable {

    private static final int PROGRESS_INTRINSIC_HEIGHT_DP = 4;
    private static final int PADDED_INTRINSIC_HEIGHT_DP = 16;
    private static final RectF RECT_BOUND = new RectF(-180, -1, 180, 1);
    private static final RectF RECT_PADDED_BOUND = new RectF(-180, -4, 180, 4);
    private static final RectF RECT_PROGRESS = new RectF(-144, -1, 144, 1);
    private static final RectTransform RECT_1_TRANSFORM_X = new RectTransform(-522.6f, 0.1f, 0f, 1f);
    private static final RectTransform RECT_2_TRANSFORM_X = new RectTransform(-197.6f, 0.1f, 0f, 1f);

    private int mProgressIntrinsicHeight;
    private int mPaddedIntrinsicHeight;
    private boolean mShowBackground = true;
    private float mBackgroundAlpha;

    private RectTransform mRect1Transform = new RectTransform(RECT_1_TRANSFORM_X);
    private RectTransform mRect2Transform = new RectTransform(RECT_2_TRANSFORM_X);

    /**
     * Create a new {@code IndeterminateHorizontalProgressDrawable}.
     *
     * @param context the {@code Context} for retrieving style information.
     */
    public IndeterminateHorizontalProgressDrawable(Context context) {
        super(context);

        float density = context.getResources().getDisplayMetrics().density;
        mProgressIntrinsicHeight = Math.round(PROGRESS_INTRINSIC_HEIGHT_DP * density);
        mPaddedIntrinsicHeight = Math.round(PADDED_INTRINSIC_HEIGHT_DP * density);

        mBackgroundAlpha = ThemeUtils.getFloatFromAttrRes(android.R.attr.disabledAlpha, 0, context);

        mAnimators = new Animator[]{
                Animators.createIndeterminateHorizontalRect1(mRect1Transform),
                Animators.createIndeterminateHorizontalRect2(mRect2Transform)
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getShowBackground() {
        return mShowBackground;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShowBackground(boolean show) {
        if (mShowBackground != show) {
            mShowBackground = show;
            invalidateSelf();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntrinsicHeight() {
        return mUseIntrinsicPadding ? mPaddedIntrinsicHeight : mProgressIntrinsicHeight;
    }

    @Override
    protected void onPreparePaint(Paint paint) {
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas, int width, int height, Paint paint) {

        if (mUseIntrinsicPadding) {
            canvas.scale(width / RECT_PADDED_BOUND.width(), height / RECT_PADDED_BOUND.height());
            canvas.translate(RECT_PADDED_BOUND.width() / 2, RECT_PADDED_BOUND.height() / 2);
        } else {
            canvas.scale(width / RECT_BOUND.width(), height / RECT_BOUND.height());
            canvas.translate(RECT_BOUND.width() / 2, RECT_BOUND.height() / 2);
        }

        if (mShowBackground) {
            paint.setAlpha(Math.round(mAlpha * mBackgroundAlpha));
            drawBackgroundRect(canvas, paint);
            paint.setAlpha(mAlpha);
        }
        drawProgressRect(canvas, mRect2Transform, paint);
        drawProgressRect(canvas, mRect1Transform, paint);
    }

    private static void drawBackgroundRect(Canvas canvas, Paint paint) {
        canvas.drawRect(RECT_BOUND, paint);
    }

    private static void drawProgressRect(Canvas canvas, RectTransform transform, Paint paint) {

        int saveCount = canvas.save();
        canvas.translate(transform.mTranslateX, transform.mTranslateY);
        canvas.scale(transform.mScaleX, transform.mScaleY);

        canvas.drawRect(RECT_PROGRESS, paint);

        canvas.restoreToCount(saveCount);
    }

    private static class RectTransform {

        public float mTranslateX;
        public float mTranslateY;

        public float mScaleX;
        public float mScaleY;

        public RectTransform(float translateX, float scaleX, float translateY, float scaleY) {
            mTranslateX = translateX;
            mScaleX = scaleX;
            mTranslateY = translateY;
            mScaleY = scaleY;
        }

        public RectTransform(RectTransform that) {
            mTranslateX = that.mTranslateX;
            mScaleX = that.mScaleX;
            mTranslateY = that.mTranslateY;
            mScaleY = that.mScaleY;
        }

        @Keep
        @SuppressWarnings("unused")
        public void setTranslateX(float translateX) {
            mTranslateX = translateX;
        }

        @Keep
        @SuppressWarnings("unused")
        public void setScaleX(float scaleX) {
            mScaleX = scaleX;
        }

        @Keep
        @SuppressWarnings("unused")
        public void setTranslateY(float translateY) {
            mTranslateY = translateY;
        }

        @Keep
        @SuppressWarnings("unused")
        public void setScaleY(float scaleY) {
            mScaleY = scaleY;
        }
    }
}
