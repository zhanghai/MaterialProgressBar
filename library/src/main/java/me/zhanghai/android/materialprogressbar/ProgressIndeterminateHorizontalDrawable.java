package me.zhanghai.android.materialprogressbar;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;

public class ProgressIndeterminateHorizontalDrawable extends Drawable implements Animatable {

    private static final String LOG_TAG = ProgressIndeterminateHorizontalDrawable.class
            .getSimpleName();

    private static final float INTRINSIC_HEIGHT_DP = 3.2f;
    private static final RectF RECT_BOUND = new RectF(-180, -1, 180, 1);
    private static final RectF RECT_PROGRESS = new RectF(-144, -1, 144, 1);
    private static final RectTransformX RECT_1_TRANSFORM_X = new RectTransformX(-522.6f, 0.1f);
    private static final RectTransformX RECT_2_TRANSFORM_X = new RectTransformX(-197.6f, 0.1f);

    private int mIntrinsicHeight;
    private int mAlpha = 0xFF;
    private ColorFilter mColorFilter;
    private ColorStateList mTint;
    private PorterDuff.Mode mTintMode = PorterDuff.Mode.SRC_IN;
    private PorterDuffColorFilter mTintFilter;
    private int mLayoutDirection;
    private boolean mAutoMirrored = true;

    private Paint mPaint;
    private RectTransformX mRect1TransformX = new RectTransformX(RECT_1_TRANSFORM_X);
    private RectTransformX mRect2TransformX = new RectTransformX(RECT_2_TRANSFORM_X);
    private Animator[] mAnimators;

    private ProgressIndeterminateHorizontalDrawable(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        mIntrinsicHeight = Math.round(INTRINSIC_HEIGHT_DP * density);
        mAnimators = new Animator[] {
                Animators.createIndeterminateHorizontalRect1(mRect1TransformX),
                Animators.createIndeterminateHorizontalRect2(mRect2TransformX)
        };
    }

    public static Drawable create(Context context) {
        Drawable drawable = new ProgressIndeterminateHorizontalDrawable(context);
        drawable = DrawableCompat.wrap(drawable);
        int colorControlActivated = getThemeAttrColor(context, R.attr.colorControlActivated);
        DrawableCompat.setTint(drawable, colorControlActivated);
        return drawable;
    }

    private static int getThemeAttrColor(Context context, int attr) {
        TypedArray a = context.obtainStyledAttributes(null, new int[] {attr});
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntrinsicHeight() {
        return mIntrinsicHeight;
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

    @Override
    public void setTintList(ColorStateList tint) {
        mTint = tint;
        mTintFilter = makeTintFilter(tint, mTintMode);
        invalidateSelf();
    }

    @Override
    public void setTintMode(PorterDuff.Mode tintMode) {
        mTintMode = tintMode;
        mTintFilter = makeTintFilter(mTint, tintMode);
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

    /**
     * Returns the resolved layout direction for this Drawable.
     *
     * @return One of {@link android.view.View#LAYOUT_DIRECTION_LTR},
     *   {@link android.view.View#LAYOUT_DIRECTION_RTL}
     */
    public int getLayoutDirection() {
        return mLayoutDirection;
    }

    /**
     * Set the layout direction for this drawable. Should be a resolved direction as the
     * Drawable as no capacity to do the resolution on his own.
     *
     * @param layoutDirection One of {@link android.view.View#LAYOUT_DIRECTION_LTR},
     *   {@link android.view.View#LAYOUT_DIRECTION_RTL}
     */
    public void setLayoutDirection(int layoutDirection) {
        if (getLayoutDirection() != layoutDirection) {
            mLayoutDirection = layoutDirection;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAutoMirrored() {
        return mAutoMirrored;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAutoMirrored(boolean autoMirrored) {
        if (mAutoMirrored != autoMirrored) {
            mAutoMirrored = autoMirrored;
            invalidateSelf();
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    public RectTransformX getRect1Target() {
        return mRect1TransformX;
    }

    public RectTransformX getRect2Target() {
        return mRect2TransformX;
    }

    @Override
    public void draw(Canvas canvas) {

        Rect bounds = getBounds();
        if (bounds.width() == 0 || bounds.height() == 0) {
            // too small to draw
            return;
        }

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.BLACK);
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

        canvas.scale(bounds.width() / RECT_BOUND.width(), bounds.height() / RECT_BOUND.height());
        canvas.translate(RECT_BOUND.width() / 2, RECT_BOUND.height() / 2);

        drawProgressRect(canvas, mRect2TransformX);
        drawProgressRect(canvas, mRect1TransformX);

        canvas.restoreToCount(saveCount);

        if (isStarted()) {
            invalidateSelf();
        }
    }

    private boolean needMirroring() {
        return isAutoMirrored() && getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    private void drawProgressRect(Canvas canvas, RectTransformX transformX) {

        if (transformX.scaleX == 0) {
            Log.w(LOG_TAG, "Skipping rect with scaleX = 0");
            return;
        }

        int saveCount = canvas.save();
        canvas.translate(transformX.translateX, 0);
        canvas.scale(transformX.scaleX, 1);

        canvas.drawRect(RECT_PROGRESS, mPaint);

        canvas.restoreToCount(saveCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {

        if (isStarted()) {
            return;
        }

        for (Animator animator : mAnimators) {
            animator.start();
        }
        invalidateSelf();
    }

    private boolean isStarted() {
        for (Animator animator : mAnimators) {
            if (animator.isStarted()) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        for (Animator animator : mAnimators) {
            animator.end();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRunning() {
        for (Animator animator : mAnimators) {
            if (animator.isRunning()) {
                return true;
            }
        }
        return false;
    }

    private static class RectTransformX {

        public float translateX;
        public float scaleX;

        public RectTransformX(float translateX, float scaleX) {
            this.translateX = translateX;
            this.scaleX = scaleX;
        }

        public RectTransformX(RectTransformX that) {
            translateX = that.translateX;
            scaleX = that.scaleX;
        }

        public void setTranslateX(float translateX) {
            this.translateX = translateX;
        }

        public void setScaleX(float scaleX) {
            this.scaleX = scaleX;
        }
    }
}
