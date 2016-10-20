/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

import me.zhanghai.android.materialprogressbar.internal.DrawableCompat;

/**
 * A {@link ProgressBar} subclass that handles tasks related to backported progress drawable.
 */
public class MaterialProgressBar extends ProgressBar {

    private static final String TAG = MaterialProgressBar.class.getSimpleName();

    public static final int PROGRESS_STYLE_CIRCULAR = 0;
    public static final int PROGRESS_STYLE_HORIZONTAL = 1;

    private int mProgressStyle;
    private TintInfo mProgressTintInfo = new TintInfo();

    public MaterialProgressBar(Context context) {
        super(context);

        init(context, null, 0, 0);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0, 0);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialProgressBar(Context context, AttributeSet attrs, int defStyleAttr,
                               int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialProgressBar,
                defStyleAttr, defStyleRes);
        mProgressStyle = a.getInt(R.styleable.MaterialProgressBar_mpb_progressStyle,
                PROGRESS_STYLE_CIRCULAR);
        boolean setBothDrawables = a.getBoolean(
                R.styleable.MaterialProgressBar_mpb_setBothDrawables, false);
        boolean useIntrinsicPadding = a.getBoolean(
                R.styleable.MaterialProgressBar_mpb_useIntrinsicPadding, true);
        boolean showTrack = a.getBoolean(R.styleable.MaterialProgressBar_mpb_showTrack,
                mProgressStyle == PROGRESS_STYLE_HORIZONTAL);
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_progressTint)) {
            mProgressTintInfo.mProgressTintList = a.getColorStateList(
                    R.styleable.MaterialProgressBar_mpb_progressTint);
            mProgressTintInfo.mHasProgressTintList = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_progressTintMode)) {
            mProgressTintInfo.mProgressTintMode = DrawableCompat.parseTintMode(a.getInt(
                    R.styleable.MaterialProgressBar_mpb_progressTintMode, -1), null);
            mProgressTintInfo.mHasProgressTintMode = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_secondaryProgressTint)) {
            mProgressTintInfo.mSecondaryProgressTintList = a.getColorStateList(
                    R.styleable.MaterialProgressBar_mpb_secondaryProgressTint);
            mProgressTintInfo.mHasSecondaryProgressTintList = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_secondaryProgressTintMode)) {
            mProgressTintInfo.mSecondaryProgressTintMode = DrawableCompat.parseTintMode(a.getInt(
                    R.styleable.MaterialProgressBar_mpb_secondaryProgressTintMode, -1), null);
            mProgressTintInfo.mHasSecondaryProgressTintMode = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_progressBackgroundTint)) {
            mProgressTintInfo.mProgressBackgroundTintList = a.getColorStateList(
                    R.styleable.MaterialProgressBar_mpb_progressBackgroundTint);
            mProgressTintInfo.mHasProgressBackgroundTintList = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_progressBackgroundTintMode)) {
            mProgressTintInfo.mProgressBackgroundTintMode = DrawableCompat.parseTintMode(a.getInt(
                    R.styleable.MaterialProgressBar_mpb_progressBackgroundTintMode, -1), null);
            mProgressTintInfo.mHasProgressBackgroundTintMode = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_indeterminateTint)) {
            mProgressTintInfo.mIndeterminateTintList = a.getColorStateList(
                    R.styleable.MaterialProgressBar_mpb_indeterminateTint);
            mProgressTintInfo.mHasIndeterminateTintList = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_indeterminateTintMode)) {
            mProgressTintInfo.mIndeterminateTintMode = DrawableCompat.parseTintMode(a.getInt(
                    R.styleable.MaterialProgressBar_mpb_indeterminateTintMode, -1), null);
            mProgressTintInfo.mHasIndeterminateTintMode = true;
        }
        a.recycle();

        switch (mProgressStyle) {
            case PROGRESS_STYLE_CIRCULAR:
                if (!isIndeterminate() || setBothDrawables) {
                    throw new UnsupportedOperationException(
                            "Determinate circular drawable is not yet supported");
                } else {
                    if (!isInEditMode()) {
                        setIndeterminateDrawable(new IndeterminateProgressDrawable(context));
                    }
                }
                break;
            case PROGRESS_STYLE_HORIZONTAL:
                if (isIndeterminate() || setBothDrawables) {
                    if (!isInEditMode()) {
                        setIndeterminateDrawable(new IndeterminateHorizontalProgressDrawable(
                                context));
                    }
                }
                if (!isIndeterminate() || setBothDrawables) {
                    setProgressDrawable(new HorizontalProgressDrawable(context));
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown progress style: " + mProgressStyle);
        }
        setUseIntrinsicPadding(useIntrinsicPadding);
        setShowTrack(showTrack);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // isHardwareAccelerated() only works when attached to a window.
        fixCanvasScalingWhenHardwareAccelerated();
    }

    private void fixCanvasScalingWhenHardwareAccelerated() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // Canvas scaling when hardware accelerated results in artifacts on older API levels, so
            // we need to use software rendering
            if (isHardwareAccelerated() && getLayerType() != LAYER_TYPE_SOFTWARE) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
        }
    }

    /**
     * Get the progress style of this ProgressBar.
     *
     * @return The progress style.
     */
    public int getProgressStyle() {
        return mProgressStyle;
    }

    /**
     * Get the current drawable of this ProgressBar.
     *
     * @return The current drawable.
     */
    public Drawable getCurrentDrawable() {
        return isIndeterminate() ? getIndeterminateDrawable() : getProgressDrawable();
    }

    /**
     * Get whether the current drawable is using an intrinsic padding. The default is {@code true}.
     *
     * @return Whether the current drawable is using an intrinsic padding.
     * @throws IllegalStateException If the current drawable does not implement
     * {@link IntrinsicPaddingDrawable}.
     */
    public boolean getUseIntrinsicPadding() {
        Drawable drawable = getCurrentDrawable();
        if (drawable instanceof IntrinsicPaddingDrawable) {
            return ((IntrinsicPaddingDrawable) drawable).getUseIntrinsicPadding();
        } else {
            throw new IllegalStateException("Drawable does not implement IntrinsicPaddingDrawable");
        }
    }

    /**
     * Set whether the current drawable should use an intrinsic padding. The default is
     * {@code true}.
     *
     * @param useIntrinsicPadding Whether the current drawable should use its intrinsic padding.
     * @throws IllegalStateException If the current drawable does not implement
     * {@link IntrinsicPaddingDrawable}.
     */
    public void setUseIntrinsicPadding(boolean useIntrinsicPadding) {
        Drawable drawable = getCurrentDrawable();
        if (drawable instanceof IntrinsicPaddingDrawable) {
            ((IntrinsicPaddingDrawable) drawable).setUseIntrinsicPadding(useIntrinsicPadding);
        }
        Drawable indeterminateDrawable = getIndeterminateDrawable();
        if (indeterminateDrawable instanceof IntrinsicPaddingDrawable) {
            ((IntrinsicPaddingDrawable) indeterminateDrawable)
                    .setUseIntrinsicPadding(useIntrinsicPadding);
        }
    }

    /**
     * Get whether the current drawable is showing a track. The default is {@code true}.
     *
     * @return Whether the current drawable is showing a track, or {@code false} if the drawable
     * does not implement {@link ShowTrackDrawable}.
     */
    public boolean getShowTrack() {
        Drawable drawable = getCurrentDrawable();
        if (drawable instanceof ShowTrackDrawable) {
            return ((ShowTrackDrawable) drawable).getShowTrack();
        } else {
            return false;
        }
    }

    /**
     * Set whether the current drawable should show a track. The default is {@code true}.
     *
     * @param showTrack Whether track should be shown. When {@code false}, does nothing if the
     *                  progress drawable does not implement {@link ShowTrackDrawable}. When
     *                  {@code true}, throws {@link IllegalStateException}.
     * @throws IllegalStateException If {@code showTrack} is {@code true} but the current drawable
     * does not implement {@link ShowTrackDrawable}.
     */
    public void setShowTrack(boolean showTrack) {
        Drawable drawable = getCurrentDrawable();
        if (drawable instanceof ShowTrackDrawable) {
            ((ShowTrackDrawable) drawable).setShowTrack(showTrack);
        }
        Drawable indeterminateDrawable = getIndeterminateDrawable();
        if (indeterminateDrawable instanceof ShowTrackDrawable) {
            ((ShowTrackDrawable) indeterminateDrawable).setShowTrack(showTrack);
        }
    }

    @Override
    public void setProgressDrawable(Drawable d) {
        super.setProgressDrawable(d);

        // mProgressTintInfo can be null during super class initialization.
        if (mProgressTintInfo != null) {
            applyProgressTints();
        }
    }

    @Override
    public void setIndeterminateDrawable(Drawable d) {
        super.setIndeterminateDrawable(d);

        // mProgressTintInfo can be null during super class initialization.
        if (mProgressTintInfo != null) {
            applyIndeterminateTint();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ColorStateList getProgressTintList() {
        return mProgressTintInfo.mProgressTintList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProgressTintList(@Nullable ColorStateList tint) {
        mProgressTintInfo.mProgressTintList = tint;
        mProgressTintInfo.mHasProgressTintList = true;

        applyPrimaryProgressTint();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public PorterDuff.Mode getProgressTintMode() {
        return mProgressTintInfo.mProgressTintMode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProgressTintMode(@Nullable PorterDuff.Mode tintMode) {
        mProgressTintInfo.mProgressTintMode = tintMode;
        mProgressTintInfo.mHasProgressTintMode = true;

        applyPrimaryProgressTint();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ColorStateList getSecondaryProgressTintList() {
        return mProgressTintInfo.mSecondaryProgressTintList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSecondaryProgressTintList(@Nullable ColorStateList tint) {
        mProgressTintInfo.mSecondaryProgressTintList = tint;
        mProgressTintInfo.mHasSecondaryProgressTintList = true;

        applySecondaryProgressTint();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public PorterDuff.Mode getSecondaryProgressTintMode() {
        return mProgressTintInfo.mSecondaryProgressTintMode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSecondaryProgressTintMode(@Nullable PorterDuff.Mode tintMode) {
        mProgressTintInfo.mSecondaryProgressTintMode = tintMode;
        mProgressTintInfo.mHasSecondaryProgressTintMode = true;

        applySecondaryProgressTint();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ColorStateList getProgressBackgroundTintList() {
        return mProgressTintInfo.mProgressBackgroundTintList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProgressBackgroundTintList(@Nullable ColorStateList tint) {
        mProgressTintInfo.mProgressBackgroundTintList = tint;
        mProgressTintInfo.mHasProgressBackgroundTintList = true;

        applyProgressBackgroundTint();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public PorterDuff.Mode getProgressBackgroundTintMode() {
        return mProgressTintInfo.mProgressBackgroundTintMode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProgressBackgroundTintMode(@Nullable PorterDuff.Mode tintMode) {
        mProgressTintInfo.mProgressBackgroundTintMode = tintMode;
        mProgressTintInfo.mHasProgressBackgroundTintMode = true;

        applyProgressBackgroundTint();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ColorStateList getIndeterminateTintList() {
        return mProgressTintInfo.mIndeterminateTintList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIndeterminateTintList(@Nullable ColorStateList tint) {
        mProgressTintInfo.mIndeterminateTintList = tint;
        mProgressTintInfo.mHasIndeterminateTintList = true;

        applyIndeterminateTint();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public PorterDuff.Mode getIndeterminateTintMode() {
        return mProgressTintInfo.mIndeterminateTintMode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIndeterminateTintMode(@Nullable PorterDuff.Mode tintMode) {
        mProgressTintInfo.mIndeterminateTintMode = tintMode;
        mProgressTintInfo.mHasIndeterminateTintMode = true;

        applyIndeterminateTint();
    }

    private void applyProgressTints() {
        if (getProgressDrawable() == null) {
            return;
        }
        applyPrimaryProgressTint();
        applyProgressBackgroundTint();
        applySecondaryProgressTint();
    }

    private void applyPrimaryProgressTint() {
        if (getProgressDrawable() == null) {
            return;
        }
        if (mProgressTintInfo.mHasProgressTintList || mProgressTintInfo.mHasProgressTintMode) {
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.progress, true);
            if (target != null) {
                applyTintForDrawable(target, mProgressTintInfo.mProgressTintList,
                        mProgressTintInfo.mHasProgressTintList, mProgressTintInfo.mProgressTintMode,
                        mProgressTintInfo.mHasProgressTintMode);
            }
        }
    }

    private void applySecondaryProgressTint() {
        if (getProgressDrawable() == null) {
            return;
        }
        if (mProgressTintInfo.mHasSecondaryProgressTintList
                || mProgressTintInfo.mHasSecondaryProgressTintMode) {
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.secondaryProgress,
                    false);
            if (target != null) {
                applyTintForDrawable(target, mProgressTintInfo.mSecondaryProgressTintList,
                        mProgressTintInfo.mHasSecondaryProgressTintList,
                        mProgressTintInfo.mSecondaryProgressTintMode,
                        mProgressTintInfo.mHasSecondaryProgressTintMode);
            }
        }
    }

    private void applyProgressBackgroundTint() {
        if (getProgressDrawable() == null) {
            return;
        }
        if (mProgressTintInfo.mHasProgressBackgroundTintList
                || mProgressTintInfo.mHasProgressBackgroundTintMode) {
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.background, false);
            if (target != null) {
                applyTintForDrawable(target, mProgressTintInfo.mProgressBackgroundTintList,
                        mProgressTintInfo.mHasProgressBackgroundTintList,
                        mProgressTintInfo.mProgressBackgroundTintMode,
                        mProgressTintInfo.mHasProgressBackgroundTintMode);
            }
        }
    }

    @Nullable
    private Drawable getTintTargetFromProgressDrawable(int layerId, boolean shouldFallback) {
        Drawable progressDrawable = getProgressDrawable();
        if (progressDrawable == null) {
            return null;
        }
        progressDrawable.mutate();
        Drawable layerDrawable = null;
        if (progressDrawable instanceof LayerDrawable) {
            layerDrawable = ((LayerDrawable) progressDrawable).findDrawableByLayerId(layerId);
        }
        if (layerDrawable == null && shouldFallback) {
            layerDrawable = progressDrawable;
        }
        return layerDrawable;
    }

    private void applyIndeterminateTint() {
        Drawable indeterminateDrawable = getIndeterminateDrawable();
        if (indeterminateDrawable == null) {
            return;
        }
        if (mProgressTintInfo.mHasIndeterminateTintList
                || mProgressTintInfo.mHasIndeterminateTintMode) {
            indeterminateDrawable.mutate();
            applyTintForDrawable(indeterminateDrawable, mProgressTintInfo.mIndeterminateTintList,
                    mProgressTintInfo.mHasIndeterminateTintList,
                    mProgressTintInfo.mIndeterminateTintMode,
                    mProgressTintInfo.mHasIndeterminateTintMode);
        }
    }

    // Progress drawables in this library has already rewritten tint related methods for
    // compatibility.
    @SuppressLint("NewApi")
    private void applyTintForDrawable(Drawable drawable, ColorStateList tintList,
                                      boolean hasTintList, PorterDuff.Mode tintMode,
                                      boolean hasTintMode) {

        if (hasTintList || hasTintMode) {

            if (hasTintList) {
                if (drawable instanceof TintableDrawable) {
                    //noinspection RedundantCast
                    ((TintableDrawable) drawable).setTintList(tintList);
                } else {
                    Log.w(TAG, "Drawable did not implement TintableDrawable, it won't be tinted below Lollipop");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable.setTintList(tintList);
                    }
                }
            }

            if (hasTintMode) {
                if (drawable instanceof TintableDrawable) {
                    //noinspection RedundantCast
                    ((TintableDrawable) drawable).setTintMode(tintMode);
                } else {
                    Log.w(TAG, "Drawable did not implement TintableDrawable, it won't be tinted below Lollipop");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable.setTintMode(tintMode);
                    }
                }
            }

            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (drawable.isStateful()) {
                drawable.setState(getDrawableState());
            }
        }
    }

    private static class TintInfo {

        public ColorStateList mProgressTintList;
        public PorterDuff.Mode mProgressTintMode;
        public boolean mHasProgressTintList;
        public boolean mHasProgressTintMode;

        public ColorStateList mSecondaryProgressTintList;
        public PorterDuff.Mode mSecondaryProgressTintMode;
        public boolean mHasSecondaryProgressTintList;
        public boolean mHasSecondaryProgressTintMode;

        public ColorStateList mProgressBackgroundTintList;
        public PorterDuff.Mode mProgressBackgroundTintMode;
        public boolean mHasProgressBackgroundTintList;
        public boolean mHasProgressBackgroundTintMode;

        public ColorStateList mIndeterminateTintList;
        public PorterDuff.Mode mIndeterminateTintMode;
        public boolean mHasIndeterminateTintList;
        public boolean mHasIndeterminateTintMode;
    }
}
