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
public class HorizontalProgressDrawable extends LayerDrawable
        implements IntrinsicPaddingDrawable, ShowTrackDrawable, TintableDrawable {

    private static final String TAG = HorizontalProgressDrawable.class.getSimpleName();

    private float mDisabledAlpha;

    private HorizontalProgressTrackDrawable mTrackDrawable;
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
                new HorizontalProgressTrackDrawable(context),
                new SingleHorizontalProgressDrawable(context),
                new SingleHorizontalProgressDrawable(context)
        });

        mDisabledAlpha = ThemeUtils.getFloatFromAttrRes(android.R.attr.disabledAlpha, context);

        setId(0, android.R.id.background);
        mTrackDrawable = (HorizontalProgressTrackDrawable) getDrawable(0);
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
    public boolean getShowTrack() {
        return mTrackDrawable.getShowTrack();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShowTrack(boolean showTrack) {
        if (mTrackDrawable.getShowTrack() != showTrack) {
            mTrackDrawable.setShowTrack(showTrack);
            updateSecondaryProgressTint();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getUseIntrinsicPadding() {
        return mTrackDrawable.getUseIntrinsicPadding();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUseIntrinsicPadding(boolean useIntrinsicPadding) {
        mTrackDrawable.setUseIntrinsicPadding(useIntrinsicPadding);
        mSecondaryProgressDrawable.setUseIntrinsicPadding(useIntrinsicPadding);
        mProgressDrawable.setUseIntrinsicPadding(useIntrinsicPadding);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressLint("NewApi")
    public void setTint(@ColorInt int tintColor) {
        // Modulate alpha of tintColor against mDisabledAlpha.
        int backgroundTintColor = ColorUtils.setAlphaComponent(tintColor,
                Math.round(Color.alpha(tintColor) * mDisabledAlpha));
        mTrackDrawable.setTint(backgroundTintColor);
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
            backgroundTint = tint.withAlpha(Math.round(0xFF * mDisabledAlpha));
        } else {
            backgroundTint = null;
        }
        mTrackDrawable.setTintList(backgroundTint);
        setSecondaryProgressTintList(backgroundTint);
        mProgressDrawable.setTintList(tint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressLint("NewApi")
    public void setTintMode(@NonNull PorterDuff.Mode tintMode) {
        mTrackDrawable.setTintMode(tintMode);
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

    private void updateSecondaryProgressTint() {
        if (mHasSecondaryProgressTintColor) {
            int tintColor = mSecondaryProgressTintColor;
            if (!getShowTrack()) {
                // Alpha of tintColor may not be mDisabledAlpha because we modulated it in
                // setTint().
                float backgroundAlpha = (float) Color.alpha(tintColor) / 0xFF;
                tintColor = ColorUtils.setAlphaComponent(tintColor,
                        Math.round(0xFF * compositeAlpha(backgroundAlpha, backgroundAlpha)));
            }
            mSecondaryProgressDrawable.setTint(tintColor);
        } else if (mHasSecondaryProgressTint) {
            ColorStateList tint = mSecondaryProgressTint;
            if (!getShowTrack()) {
                // Composite alpha so that the secondary progress looks as before.
                tint = tint.withAlpha(Math.round(0xFF * compositeAlpha(mDisabledAlpha,
                        mDisabledAlpha)));
            }
            mSecondaryProgressDrawable.setTintList(tint);
        }
    }

    // See https://en.wikipedia.org/wiki/Alpha_compositing
    private float compositeAlpha(float alpha1, float alpha2) {
        return alpha1 + alpha2 * (1 - alpha1);
    }
}
