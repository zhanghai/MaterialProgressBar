/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

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
import android.util.Log;

import me.zhanghai.android.materialprogressbar.internal.ColorUtils;
import me.zhanghai.android.materialprogressbar.internal.ThemeUtils;

class BaseProgressLayerDrawable<
        ProgressDrawableType extends IntrinsicPaddingDrawable & TintableDrawable,
        BackgroundDrawableType extends IntrinsicPaddingDrawable & ShowBackgroundDrawable
                & TintableDrawable>
        extends LayerDrawable implements IntrinsicPaddingDrawable, MaterialProgressDrawable,
        ShowBackgroundDrawable, TintableDrawable {

    private float mBackgroundAlpha;

    private ProgressDrawableType mProgressDrawable;
    private ProgressDrawableType mSecondaryProgressDrawable;
    private BackgroundDrawableType mBackgroundDrawable;

    private boolean mHasSecondaryProgressTint;
    private ColorStateList mSecondaryProgressTint;
    private boolean mHasSecondaryProgressTintColor;
    private int mSecondaryProgressTintColor;

    public BaseProgressLayerDrawable(Drawable[] layers, Context context) {
        super(layers);

        mBackgroundAlpha = ThemeUtils.getFloatFromAttrRes(android.R.attr.disabledAlpha, 1, context);

        setId(0, android.R.id.background);
        //noinspection unchecked
        mBackgroundDrawable = (BackgroundDrawableType) getDrawable(0);
        setId(1, android.R.id.secondaryProgress);
        //noinspection unchecked
        mSecondaryProgressDrawable = (ProgressDrawableType) getDrawable(1);
        setId(2, android.R.id.progress);
        //noinspection unchecked
        mProgressDrawable = (ProgressDrawableType) getDrawable(2);

        int controlActivatedColor = ThemeUtils.getColorFromAttrRes(R.attr.colorControlActivated,
                Color.BLACK, context);
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
        return mBackgroundDrawable.getUseIntrinsicPadding();
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
        int backgroundTintColor = android.support.v4.graphics.ColorUtils.setAlphaComponent(
                tintColor, Math.round(Color.alpha(tintColor) * mBackgroundAlpha));
        mBackgroundDrawable.setTint(backgroundTintColor);
        setSecondaryProgressTint(backgroundTintColor);
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
                Log.w(getClass().getSimpleName(),
                        "setTintList() called with a non-opaque ColorStateList, its original alpha will be discarded");
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
        mSecondaryProgressDrawable.setTintMode(tintMode);
        mProgressDrawable.setTintMode(tintMode);
    }

    private void setSecondaryProgressTint(int tintColor) {
        mHasSecondaryProgressTintColor = true;
        mSecondaryProgressTintColor = tintColor;
        mHasSecondaryProgressTint = false;
        updateSecondaryProgressTint();
    }

    private void setSecondaryProgressTintList(ColorStateList tint) {
        mHasSecondaryProgressTintColor = false;
        mHasSecondaryProgressTint = true;
        mSecondaryProgressTint = tint;
        updateSecondaryProgressTint();
    }

    @SuppressLint("NewApi")
    private void updateSecondaryProgressTint() {
        if (mHasSecondaryProgressTintColor) {
            int tintColor = mSecondaryProgressTintColor;
            if (!getShowBackground()) {
                // Alpha of tintColor may not be mBackgroundAlpha because we modulated it in
                // setTint().
                float backgroundAlpha = (float) Color.alpha(tintColor) / 0xFF;
                tintColor = android.support.v4.graphics.ColorUtils.setAlphaComponent(tintColor,
                        Math.round(0xFF * ColorUtils.compositeAlpha(backgroundAlpha,
                                backgroundAlpha)));
            }
            mSecondaryProgressDrawable.setTint(tintColor);
        } else if (mHasSecondaryProgressTint) {
            ColorStateList tint = mSecondaryProgressTint;
            if (!getShowBackground()) {
                // Composite alpha so that the secondary progress looks as before.
                tint = tint.withAlpha(Math.round(0xFF * ColorUtils.compositeAlpha(mBackgroundAlpha,
                        mBackgroundAlpha)));
            }
            mSecondaryProgressDrawable.setTintList(tint);
        }
    }
}
