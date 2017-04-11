/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
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

    public static final int DETERMINATE_CIRCULAR_PROGRESS_STYLE_NORMAL = 0;
    public static final int DETERMINATE_CIRCULAR_PROGRESS_STYLE_DYNAMIC = 1;

    // This field remains false inside super class constructor.
    @SuppressWarnings("FieldCanBeLocal")
    private boolean mSuperInitialized = true;
    private int mProgressStyle;
    private TintInfo mProgressTintInfo = new TintInfo();

    public MaterialProgressBar(Context context) {
        super(context);

        init(null, 0, 0);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs, 0, 0);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialProgressBar(Context context, AttributeSet attrs, int defStyleAttr,
                               int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs, defStyleAttr, defStyleRes);
    }

    @SuppressWarnings("RestrictedApi")
    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        Context context = getContext();
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
                R.styleable.MaterialProgressBar, defStyleAttr, defStyleRes);
        mProgressStyle = a.getInt(R.styleable.MaterialProgressBar_mpb_progressStyle,
                PROGRESS_STYLE_CIRCULAR);
        boolean setBothDrawables = a.getBoolean(
                R.styleable.MaterialProgressBar_mpb_setBothDrawables, false);
        boolean useIntrinsicPadding = a.getBoolean(
                R.styleable.MaterialProgressBar_mpb_useIntrinsicPadding, true);
        boolean showProgressBackground = a.getBoolean(
                R.styleable.MaterialProgressBar_mpb_showProgressBackground,
                mProgressStyle == PROGRESS_STYLE_HORIZONTAL);
        int determinateCircularProgressStyle = a.getInt(
                R.styleable.MaterialProgressBar_mpb_determinateCircularProgressStyle,
                DETERMINATE_CIRCULAR_PROGRESS_STYLE_NORMAL);
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_progressTint)) {
            mProgressTintInfo.mProgressTint = a.getColorStateList(
                    R.styleable.MaterialProgressBar_mpb_progressTint);
            mProgressTintInfo.mHasProgressTint = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_progressTintMode)) {
            mProgressTintInfo.mProgressTintMode = DrawableCompat.parseTintMode(a.getInt(
                    R.styleable.MaterialProgressBar_mpb_progressTintMode, -1), null);
            mProgressTintInfo.mHasProgressTintMode = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_secondaryProgressTint)) {
            mProgressTintInfo.mSecondaryProgressTint = a.getColorStateList(
                    R.styleable.MaterialProgressBar_mpb_secondaryProgressTint);
            mProgressTintInfo.mHasSecondaryProgressTint = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_secondaryProgressTintMode)) {
            mProgressTintInfo.mSecondaryProgressTintMode = DrawableCompat.parseTintMode(a.getInt(
                    R.styleable.MaterialProgressBar_mpb_secondaryProgressTintMode, -1), null);
            mProgressTintInfo.mHasSecondaryProgressTintMode = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_progressBackgroundTint)) {
            mProgressTintInfo.mProgressBackgroundTint = a.getColorStateList(
                    R.styleable.MaterialProgressBar_mpb_progressBackgroundTint);
            mProgressTintInfo.mHasProgressBackgroundTint = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_progressBackgroundTintMode)) {
            mProgressTintInfo.mProgressBackgroundTintMode = DrawableCompat.parseTintMode(a.getInt(
                    R.styleable.MaterialProgressBar_mpb_progressBackgroundTintMode, -1), null);
            mProgressTintInfo.mHasProgressBackgroundTintMode = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_indeterminateTint)) {
            mProgressTintInfo.mIndeterminateTint = a.getColorStateList(
                    R.styleable.MaterialProgressBar_mpb_indeterminateTint);
            mProgressTintInfo.mHasIndeterminateTint = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_indeterminateTintMode)) {
            mProgressTintInfo.mIndeterminateTintMode = DrawableCompat.parseTintMode(a.getInt(
                    R.styleable.MaterialProgressBar_mpb_indeterminateTintMode, -1), null);
            mProgressTintInfo.mHasIndeterminateTintMode = true;
        }
        a.recycle();

        switch (mProgressStyle) {
            case PROGRESS_STYLE_CIRCULAR:
                if (isIndeterminate() || setBothDrawables) {
                    if (!isInEditMode()) {
                        setIndeterminateDrawable(new IndeterminateCircularProgressDrawable(
                                context));
                    }
                }
                if (!isIndeterminate() || setBothDrawables) {
                    setProgressDrawable(new CircularProgressDrawable(
                            determinateCircularProgressStyle, context));
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
        setShowProgressBackground(showProgressBackground);
    }

    @Override
    public synchronized void setIndeterminate(boolean indeterminate) {
        super.setIndeterminate(indeterminate);

        if (mSuperInitialized && !(getCurrentDrawable() instanceof MaterialProgressDrawable)) {
            Log.w(TAG,
                    "Current drawable is not a MaterialProgressDrawable, you may want to set app:mpb_setBothDrawables");
        }
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
     * Get whether the current drawable is showing a background. The default is {@code true}.
     *
     * @return Whether the current drawable is showing a background, or {@code false} if the
     * drawable does not implement {@link ShowBackgroundDrawable}.
     */
    public boolean getShowProgressBackground() {
        Drawable drawable = getCurrentDrawable();
        if (drawable instanceof ShowBackgroundDrawable) {
            return ((ShowBackgroundDrawable) drawable).getShowBackground();
        } else {
            return false;
        }
    }

    /**
     * Set whether the current drawable should show a background. The default is {@code true}.
     *
     * @param show Whether background should be shown. When {@code false}, does nothing if the
     *             progress drawable does not implement {@link ShowBackgroundDrawable}, otherwise a
     *             {@link IllegalStateException} is thrown.
     * @throws IllegalStateException If {@code show} is {@code true} but the current drawable
     * does not implement {@link ShowBackgroundDrawable}.
     */
    public void setShowProgressBackground(boolean show) {
        Drawable drawable = getCurrentDrawable();
        if (drawable instanceof ShowBackgroundDrawable) {
            ((ShowBackgroundDrawable) drawable).setShowBackground(show);
        }
        Drawable indeterminateDrawable = getIndeterminateDrawable();
        if (indeterminateDrawable instanceof ShowBackgroundDrawable) {
            ((ShowBackgroundDrawable) indeterminateDrawable).setShowBackground(show);
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
        return mProgressTintInfo.mProgressTint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProgressTintList(@Nullable ColorStateList tint) {
        mProgressTintInfo.mProgressTint = tint;
        mProgressTintInfo.mHasProgressTint = true;

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
        return mProgressTintInfo.mSecondaryProgressTint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSecondaryProgressTintList(@Nullable ColorStateList tint) {
        mProgressTintInfo.mSecondaryProgressTint = tint;
        mProgressTintInfo.mHasSecondaryProgressTint = true;

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
        return mProgressTintInfo.mProgressBackgroundTint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProgressBackgroundTintList(@Nullable ColorStateList tint) {
        mProgressTintInfo.mProgressBackgroundTint = tint;
        mProgressTintInfo.mHasProgressBackgroundTint = true;

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
        return mProgressTintInfo.mIndeterminateTint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIndeterminateTintList(@Nullable ColorStateList tint) {
        mProgressTintInfo.mIndeterminateTint = tint;
        mProgressTintInfo.mHasIndeterminateTint = true;

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
        if (mProgressTintInfo.mHasProgressTint || mProgressTintInfo.mHasProgressTintMode) {
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.progress, true);
            if (target != null) {
                applyTintForDrawable(target, mProgressTintInfo.mProgressTint,
                        mProgressTintInfo.mHasProgressTint, mProgressTintInfo.mProgressTintMode,
                        mProgressTintInfo.mHasProgressTintMode);
            }
        }
    }

    private void applySecondaryProgressTint() {
        if (getProgressDrawable() == null) {
            return;
        }
        if (mProgressTintInfo.mHasSecondaryProgressTint
                || mProgressTintInfo.mHasSecondaryProgressTintMode) {
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.secondaryProgress,
                    false);
            if (target != null) {
                applyTintForDrawable(target, mProgressTintInfo.mSecondaryProgressTint,
                        mProgressTintInfo.mHasSecondaryProgressTint,
                        mProgressTintInfo.mSecondaryProgressTintMode,
                        mProgressTintInfo.mHasSecondaryProgressTintMode);
            }
        }
    }

    private void applyProgressBackgroundTint() {
        if (getProgressDrawable() == null) {
            return;
        }
        if (mProgressTintInfo.mHasProgressBackgroundTint
                || mProgressTintInfo.mHasProgressBackgroundTintMode) {
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.background, false);
            if (target != null) {
                applyTintForDrawable(target, mProgressTintInfo.mProgressBackgroundTint,
                        mProgressTintInfo.mHasProgressBackgroundTint,
                        mProgressTintInfo.mProgressBackgroundTintMode,
                        mProgressTintInfo.mHasProgressBackgroundTintMode);
            }
        }
    }

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
        if (mProgressTintInfo.mHasIndeterminateTint
                || mProgressTintInfo.mHasIndeterminateTintMode) {
            indeterminateDrawable.mutate();
            applyTintForDrawable(indeterminateDrawable, mProgressTintInfo.mIndeterminateTint,
                    mProgressTintInfo.mHasIndeterminateTint,
                    mProgressTintInfo.mIndeterminateTintMode,
                    mProgressTintInfo.mHasIndeterminateTintMode);
        }
    }

    // Progress drawables in this library has already rewritten tint related methods for
    // compatibility.
    @SuppressLint("NewApi")
    private void applyTintForDrawable(Drawable drawable, ColorStateList tint,
                                      boolean hasTint, PorterDuff.Mode tintMode,
                                      boolean hasTintMode) {

        if (hasTint || hasTintMode) {

            if (hasTint) {
                if (drawable instanceof TintableDrawable) {
                    //noinspection RedundantCast
                    ((TintableDrawable) drawable).setTintList(tint);
                } else {
                    Log.w(TAG,
                            "Drawable did not implement TintableDrawable, it won't be tinted below Lollipop");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable.setTintList(tint);
                    }
                }
            }

            if (hasTintMode) {
                if (drawable instanceof TintableDrawable) {
                    //noinspection RedundantCast
                    ((TintableDrawable) drawable).setTintMode(tintMode);
                } else {
                    Log.w(TAG,
                            "Drawable did not implement TintableDrawable, it won't be tinted below Lollipop");
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

        public ColorStateList mProgressTint;
        public PorterDuff.Mode mProgressTintMode;
        public boolean mHasProgressTint;
        public boolean mHasProgressTintMode;

        public ColorStateList mSecondaryProgressTint;
        public PorterDuff.Mode mSecondaryProgressTintMode;
        public boolean mHasSecondaryProgressTint;
        public boolean mHasSecondaryProgressTintMode;

        public ColorStateList mProgressBackgroundTint;
        public PorterDuff.Mode mProgressBackgroundTintMode;
        public boolean mHasProgressBackgroundTint;
        public boolean mHasProgressBackgroundTintMode;

        public ColorStateList mIndeterminateTint;
        public PorterDuff.Mode mIndeterminateTintMode;
        public boolean mHasIndeterminateTint;
        public boolean mHasIndeterminateTintMode;
    }
}
