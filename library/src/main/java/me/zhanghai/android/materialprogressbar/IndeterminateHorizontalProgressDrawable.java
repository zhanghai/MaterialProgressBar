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
public class IndeterminateHorizontalProgressDrawable extends IndeterminateProgressDrawableBase
        implements ShowTrackDrawable {

    private static final float PROGRESS_INTRINSIC_HEIGHT_DP = 3.2f;
    private static final float PADDED_INTRINSIC_HEIGHT_DP = 16;
    private static final RectF RECT_BOUND = new RectF(-180, -1, 180, 1);
    private static final RectF RECT_PADDED_BOUND = new RectF(-180, -5, 180, 5);
    private static final RectF RECT_PROGRESS = new RectF(-144, -1, 144, 1);
    private static final RectTransformX RECT_1_TRANSFORM_X = new RectTransformX(-522.6f, 0.1f);
    private static final RectTransformX RECT_2_TRANSFORM_X = new RectTransformX(-197.6f, 0.1f);

    private int mProgressIntrinsicHeight;
    private int mPaddedIntrinsicHeight;
    private boolean mShowTrack = true;
    private float mTrackAlpha;

    private RectTransformX mRect1TransformX = new RectTransformX(RECT_1_TRANSFORM_X);
    private RectTransformX mRect2TransformX = new RectTransformX(RECT_2_TRANSFORM_X);

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

        mTrackAlpha = ThemeUtils.getFloatFromAttrRes(android.R.attr.disabledAlpha, context);

        mAnimators = new Animator[] {
                Animators.createIndeterminateHorizontalRect1(mRect1TransformX),
                Animators.createIndeterminateHorizontalRect2(mRect2TransformX)
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getShowTrack() {
        return mShowTrack;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShowTrack(boolean showTrack) {
        if (mShowTrack != showTrack) {
            mShowTrack = showTrack;
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

        if (mShowTrack) {
            paint.setAlpha(Math.round(mAlpha * mTrackAlpha));
            drawTrackRect(canvas, paint);
            paint.setAlpha(mAlpha);
        }
        drawProgressRect(canvas, mRect2TransformX, paint);
        drawProgressRect(canvas, mRect1TransformX, paint);
    }

    private static void drawTrackRect(Canvas canvas, Paint paint) {
        canvas.drawRect(RECT_BOUND, paint);
    }

    private static void drawProgressRect(Canvas canvas, RectTransformX transformX, Paint paint) {

        int saveCount = canvas.save();
        canvas.translate(transformX.mTranslateX, 0);
        canvas.scale(transformX.mScaleX, 1);

        canvas.drawRect(RECT_PROGRESS, paint);

        canvas.restoreToCount(saveCount);
    }

    private static class RectTransformX {

        public float mTranslateX;
        public float mScaleX;

        public RectTransformX(float translateX, float scaleX) {
            mTranslateX = translateX;
            mScaleX = scaleX;
        }

        public RectTransformX(RectTransformX that) {
            mTranslateX = that.mTranslateX;
            mScaleX = that.mScaleX;
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
    }
}
