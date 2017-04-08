package me.zhanghai.android.materialprogressbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;

import me.zhanghai.android.materialprogressbar.internal.Colors;
import me.zhanghai.android.materialprogressbar.internal.ThemeUtils;

/**
 * Package private as this does only work together with MaterialProgressBar and is not suitable
 * for another ProgressBar
 */
class DeterminateCircularProgressDrawable extends LayerDrawable
        implements IntrinsicPaddingDrawable, MaterialProgressDrawable, ShowBackgroundDrawable, TintableDrawable {

    @SuppressWarnings("unused")
    private static final String TAG = DeterminateCircularProgressDrawable.class.getSimpleName();

    private float mBackgroundAlpha;

    private SingleCircularProgressBackgroundDrawable mBackgroundDrawable;
    private SingleCircularProgressDrawable mProgressDrawable;
    private SingleCircularProgressDrawable mSecondaryProgressDrawable;

    private boolean mHasSecondaryProgressTint;
    private ColorStateList mSecondaryProgressTint;
    private boolean mHasSecondaryProgressTintColor;
    private int mSecondaryProgressTintColor;

    DeterminateCircularProgressDrawable(Context context, int determinateCircularStyle) {
        super(new Drawable[] {
                new SingleCircularProgressBackgroundDrawable(),
                new SingleCircularProgressDrawable(determinateCircularStyle),
                new SingleCircularProgressDrawable(determinateCircularStyle),
        });

        mBackgroundAlpha = ThemeUtils.getFloatFromAttrRes(android.R.attr.disabledAlpha, context);

        setId(0, android.R.id.background);
        mBackgroundDrawable = (SingleCircularProgressBackgroundDrawable) getDrawable(0);
        setId(1, android.R.id.secondaryProgress);
        mSecondaryProgressDrawable = (SingleCircularProgressDrawable) getDrawable(1);
        setId(2, android.R.id.progress);
        mProgressDrawable = (SingleCircularProgressDrawable) getDrawable(2);

        int controlActivatedColor = ThemeUtils.getColorFromAttrRes(R.attr.colorControlActivated,
                context);
        setTint(controlActivatedColor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getShowBackground() {
        return mBackgroundDrawable.getShowBackground();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShowBackground(boolean show) {
        if (mBackgroundDrawable.getShowBackground() != show) {
            mBackgroundDrawable.setShowBackground(show);
            updateSecondaryProgressTint();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getUseIntrinsicPadding() {
        return mProgressDrawable.getUseIntrinsicPadding();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUseIntrinsicPadding(boolean useIntrinsicPadding) {
        mBackgroundDrawable.setUseIntrinsicPadding(useIntrinsicPadding);
        mSecondaryProgressDrawable.setUseIntrinsicPadding(useIntrinsicPadding);
        mProgressDrawable.setUseIntrinsicPadding(useIntrinsicPadding);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressLint("NewApi")
    public void setTint(@ColorInt int tintColor) {
        // Modulate alpha of tintColor against mBackgroundAlpha.
        int backgroundTintColor = ColorUtils.setAlphaComponent(tintColor, Math.round(
                Color.alpha(tintColor) * mBackgroundAlpha));
        mBackgroundDrawable.setTint(backgroundTintColor);
        setSecondaryProgressTintColor(backgroundTintColor);
        mProgressDrawable.setTint(tintColor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressLint("NewApi")
    public void setTintList(@Nullable ColorStateList tint) {
        ColorStateList backgroundTint;
        if (tint != null) {
            if (!tint.isOpaque()) {
                Log.w(TAG, "setTintList() called with a non-opaque ColorStateList, its original alpha will be discarded");
            }
            backgroundTint = tint.withAlpha(Math.round(0xFF * mBackgroundAlpha));
        } else {
            backgroundTint = null;
        }
        mBackgroundDrawable.setTintList(backgroundTint);
        setSecondaryProgressTintList(backgroundTint);
        mProgressDrawable.setTintList(tint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressLint("NewApi")
    public void setTintMode(@NonNull PorterDuff.Mode tintMode) {
        mBackgroundDrawable.setTintMode(tintMode);
        mProgressDrawable.setTintMode(tintMode);
        mSecondaryProgressDrawable.setTintMode(tintMode);
    }

    @SuppressWarnings("WeakerAccess")
    void setSecondaryProgressTintColor(int tintColor) {
        mHasSecondaryProgressTintColor = true;
        mSecondaryProgressTintColor = tintColor;
        mHasSecondaryProgressTint = false;
        updateSecondaryProgressTint();
    }

    void setSecondaryProgressTintList(ColorStateList tint) {
        mHasSecondaryProgressTintColor = false;
        mHasSecondaryProgressTint = true;
        mSecondaryProgressTint = tint;
        updateSecondaryProgressTint();
    }

    void setSecondaryProgressTintMode(@NonNull PorterDuff.Mode tintMode) {
        mSecondaryProgressDrawable.setTintMode(tintMode);
    }

    @SuppressLint("NewApi")
    private void updateSecondaryProgressTint() {
        if (mHasSecondaryProgressTintColor) {
            int tintColor = mSecondaryProgressTintColor;
            if (!getShowBackground()) {
                // Alpha of tintColor may not be mBackgroundAlpha because we modulated it in
                // setTint().
                float backgroundAlpha = (float) Color.alpha(tintColor) / 0xFF;
                tintColor = ColorUtils.setAlphaComponent(tintColor, Math.round(
                        0xFF * Colors.compositeAlpha(backgroundAlpha, backgroundAlpha)));
            }
            mSecondaryProgressDrawable.setTint(tintColor);
        } else if (mHasSecondaryProgressTint) {
            ColorStateList tint = mSecondaryProgressTint;
            if (!getShowBackground()) {
                // Composite alpha so that the secondary progress looks as before.
                tint = tint.withAlpha(Math.round(0xFF * Colors.compositeAlpha(mBackgroundAlpha,
                        mBackgroundAlpha)));
            }
            mSecondaryProgressDrawable.setTintList(tint);
        }
    }
}
