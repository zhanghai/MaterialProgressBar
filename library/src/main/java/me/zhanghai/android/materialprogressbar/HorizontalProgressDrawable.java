/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
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
import android.support.v4.graphics.ColorUtils;
import android.util.Log;

import me.zhanghai.android.materialprogressbar.internal.ThemeUtils;

/**
 * A backported {@code Drawable} for determinate horizontal {@code ProgressBar}.
 */
public class HorizontalProgressDrawable extends LayerDrawable implements IntrinsicPaddingDrawable,
        MaterialProgressDrawable, ShowBackgroundDrawable, TintableDrawable {

    private static final String TAG = HorizontalProgressDrawable.class.getSimpleName();

    private float mBackgroundAlpha;

    private HorizontalProgressBackgroundDrawable mBackgroundDrawable;
    private SingleHorizontalProgressDrawable mSecondaryProgressDrawable;
    private SingleHorizontalProgressDrawable mProgressDrawable;

    private boolean mHasSecondaryProgressTint;
    private ColorStateList mSecondaryProgressTint;
    private boolean mHasSecondaryProgressTintColor;
    private int mSecondaryProgressTintColor;

    /**
     * Create a new {@code HorizontalProgressDrawable}.
     *
     * @param context the {@code Context} for retrieving style information.
     */
    public HorizontalProgressDrawable(Context context) {
        super(new Drawable[] {
                new HorizontalProgressBackgroundDrawable(context),
                new SingleHorizontalProgressDrawable(context),
                new SingleHorizontalProgressDrawable(context)
        });

        mBackgroundAlpha = ThemeUtils.getFloatFromAttrRes(android.R.attr.disabledAlpha, context);

        setId(0, android.R.id.background);
        mBackgroundDrawable = (HorizontalProgressBackgroundDrawable) getDrawable(0);
        setId(1, android.R.id.secondaryProgress);
        mSecondaryProgressDrawable = (SingleHorizontalProgressDrawable) getDrawable(1);
        setId(2, android.R.id.progress);
        mProgressDrawable = (SingleHorizontalProgressDrawable) getDrawable(2);

        int controlActivatedColor = ThemeUtils.getColorFromAttrRes(R.attr.colorControlActivated,
                context);
        setTint(controlActivatedColor);
    }

    /**
     * {@inheritDoc}
     */
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
        int backgroundTintColor = ColorUtils.setAlphaComponent(tintColor, Math.round(
                Color.alpha(tintColor) * mBackgroundAlpha));
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
                tintColor = ColorUtils.setAlphaComponent(tintColor, Math.round(
                        0xFF * compositeAlpha(backgroundAlpha, backgroundAlpha)));
            }
            mSecondaryProgressDrawable.setTint(tintColor);
        } else if (mHasSecondaryProgressTint) {
            ColorStateList tint = mSecondaryProgressTint;
            if (!getShowBackground()) {
                // Composite alpha so that the secondary progress looks as before.
                tint = tint.withAlpha(Math.round(0xFF * compositeAlpha(mBackgroundAlpha,
                        mBackgroundAlpha)));
            }
            mSecondaryProgressDrawable.setTintList(tint);
        }
    }

    // See https://en.wikipedia.org/wiki/Alpha_compositing
    private float compositeAlpha(float alpha1, float alpha2) {
        return alpha1 + alpha2 * (1 - alpha1);
    }
}
