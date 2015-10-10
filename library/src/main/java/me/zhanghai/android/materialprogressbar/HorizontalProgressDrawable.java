/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.zhanghai.android.materialprogressbar.internal.ThemeUtils;

/**
 * A backported {@code Drawable} for determinate horizontal {@code ProgressBar}.
 */
public class HorizontalProgressDrawable extends LayerDrawable
        implements IntrinsicPaddingDrawable, ShowTrackDrawable, TintableDrawable {

    private int mSecondaryAlpha;
    private SingleHorizontalProgressDrawable mTrackDrawable;
    private SingleHorizontalProgressDrawable mSecondaryProgressDrawable;
    private SingleHorizontalProgressDrawable mProgressDrawable;

    /**
     * Create a new {@code HorizontalProgressDrawable}.
     *
     * @param context the {@code Context} for retrieving style information.
     */
    public HorizontalProgressDrawable(Context context) {
        super(new Drawable[] {
                new SingleHorizontalProgressDrawable(context),
                new SingleHorizontalProgressDrawable(context),
                new SingleHorizontalProgressDrawable(context)
        });

        setId(0, android.R.id.background);
        mTrackDrawable = (SingleHorizontalProgressDrawable) getDrawable(0);

        setId(1, android.R.id.secondaryProgress);
        mSecondaryProgressDrawable = (SingleHorizontalProgressDrawable) getDrawable(1);
        float disabledAlpha = ThemeUtils.getFloatFromAttrRes(android.R.attr.disabledAlpha, context);
        mSecondaryAlpha = Math.round(disabledAlpha * 0xFF);
        mSecondaryProgressDrawable.setAlpha(mSecondaryAlpha);
        mSecondaryProgressDrawable.setShowTrack(false);

        setId(2, android.R.id.progress);
        mProgressDrawable = (SingleHorizontalProgressDrawable) getDrawable(2);
        mProgressDrawable.setShowTrack(false);
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
            // HACK: Make alpha act as if composited.
            mSecondaryProgressDrawable.setAlpha(showTrack ? mSecondaryAlpha : 2 * mSecondaryAlpha);
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
        mTrackDrawable.setTint(tintColor);
        mSecondaryProgressDrawable.setTint(tintColor);
        mProgressDrawable.setTint(tintColor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressLint("NewApi")
    public void setTintList(@Nullable ColorStateList tint) {
        mTrackDrawable.setTintList(tint);
        mSecondaryProgressDrawable.setTintList(tint);
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
}
