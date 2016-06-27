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
    private TintInfo mProgressTint = new TintInfo();

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
        if (a.hasValue(R.styleable.MaterialProgressBar_android_tint)) {
            mProgressTint.mTintList = a.getColorStateList(
                    R.styleable.MaterialProgressBar_android_tint);
            mProgressTint.mHasTintList = true;
        }
        if (a.hasValue(R.styleable.MaterialProgressBar_mpb_tintMode)) {
            mProgressTint.mTintMode = DrawableCompat.parseTintMode(a.getInt(
                    R.styleable.MaterialProgressBar_mpb_tintMode, -1), null);
            mProgressTint.mHasTintMode = true;
        }
        a.recycle();

        switch (mProgressStyle) {
            case PROGRESS_STYLE_CIRCULAR:
                if (!isIndeterminate() || setBothDrawables) {
                    throw new UnsupportedOperationException(
                            "Determinate circular drawable is not yet supported");
                } else {
                    setIndeterminateDrawable(new IndeterminateProgressDrawable(context));
                }
                break;
            case PROGRESS_STYLE_HORIZONTAL:
                if (isIndeterminate() || setBothDrawables) {
                    setIndeterminateDrawable(new IndeterminateHorizontalProgressDrawable(context));
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

    /**
     * Get the style of current progress drawable.
     *
     * @return The style of current progress drawable.
     */
    public int getProgressStyle() {
        return mProgressStyle;
    }

    /**
     * Get the progress drawable of this ProgressBar.
     *
     * @return The progress drawable.
     */
    public Drawable getDrawable() {
        return isIndeterminate() ? getIndeterminateDrawable() : getProgressDrawable();
    }

    /**
     * Get whether the progress drawable is using an intrinsic padding. The default is {@code true}.
     *
     * @return Whether the progress drawable is using an intrinsic padding.
     * @throws IllegalStateException If the progress drawable does not implement
     * {@link IntrinsicPaddingDrawable}.
     */
    public boolean getUseIntrinsicPadding() {
        Drawable drawable = getDrawable();
        if (drawable instanceof IntrinsicPaddingDrawable) {
            return ((IntrinsicPaddingDrawable) drawable).getUseIntrinsicPadding();
        } else {
            throw new IllegalStateException("Drawable does not implement IntrinsicPaddingDrawable");
        }
    }

    /**
     * Set whether the progress drawable should use an intrinsic padding. The default is {@code true}.
     *
     * @param useIntrinsicPadding Whether the progress drawable should use its intrinsic padding.
     * @throws IllegalStateException If the progress drawable does not implement
     * {@link IntrinsicPaddingDrawable}.
     */
    public void setUseIntrinsicPadding(boolean useIntrinsicPadding) {
        Drawable drawable = getDrawable();
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
     * Get whether the progress drawable is showing a track. The default is {@code true}.
     *
     * @return Whether the progress drawable is showing a track, or {@code false} if the drawable
     * does not implement {@link ShowTrackDrawable}.
     */
    public boolean getShowTrack() {
        Drawable drawable = getDrawable();
        if (drawable instanceof ShowTrackDrawable) {
            return ((ShowTrackDrawable) drawable).getShowTrack();
        } else {
            return false;
        }
    }

    /**
     * Set whether the progress drawable should show a track. The default is {@code true}.
     *
     * @param showTrack Whether track should be shown. When {@code false}, does nothing if the
     *                  progress drawable does not implement {@link ShowTrackDrawable}. When
     *                  {@code true}, throws {@link IllegalStateException}.
     * @throws IllegalStateException If {@code showTrack} is {@code true} but the progress drawable
     * does not implement {@link ShowTrackDrawable}.
     */
    public void setShowTrack(boolean showTrack) {
        Drawable drawable = getDrawable();
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

        // mProgressTint can be null during super class initialization.
        if (mProgressTint != null) {
            applyDeterminateProgressTint();
        }
    }

    @Override
    public void setIndeterminateDrawable(Drawable d) {
        super.setIndeterminateDrawable(d);

        // mProgressTint can be null during super class initialization.
        if (mProgressTint != null) {
            applyIndeterminateProgressTint();
        }
    }

    /**
     * Return the tint applied to the progress drawable, if specified.
     *
     * @return The tint applied to the progress drawable.
     * @see #setProgressTintList(ColorStateList)
     */
    @Nullable
    @Override
    public ColorStateList getProgressTintList() {
        return mProgressTint.mTintList;
    }

    /**
     * Applies a tint to the progress drawable. Does not modify the current tint mode, which is
     * {@link PorterDuff.Mode#SRC_IN} by default.
     * <p>
     * Subsequent calls to {@link #setBackground(Drawable)} will automatically mutate the drawable
     * and apply the specified tint and tint mode using
     * {@link Drawable#setTintList(ColorStateList)}.
     *
     * @param tint The tint to apply, may be {@code null} to clear tint.
     *
     * @see #getProgressTintList()
     * @see Drawable#setTintList(ColorStateList)
     */
    @Override
    public void setProgressTintList(@Nullable ColorStateList tint) {
        mProgressTint.mTintList = tint;
        mProgressTint.mHasTintList = true;

        applyProgressTint();
    }

    /**
     * Return the blending mode used to apply the tint to the progress drawable, if specified.
     *
     * @return The blending mode used to apply the tint to the progress drawable.
     * @see #setBackgroundTintMode(PorterDuff.Mode)
     */
    @Nullable
    @Override
    public PorterDuff.Mode getProgressTintMode() {
        return mProgressTint.mTintMode;
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setProgressTintList(ColorStateList)}} to the progress drawable. The default mode is
     * {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode The blending mode used to apply the tint, may be {@code null} to clear tint
     * @see #getProgressTintMode()
     * @see Drawable#setTintMode(PorterDuff.Mode)
     */
    @Override
    public void setProgressTintMode(@Nullable PorterDuff.Mode tintMode) {
        mProgressTint.mTintMode = tintMode;
        mProgressTint.mHasTintMode = true;

        applyProgressTint();
    }

    private void applyProgressTint() {
        applyDeterminateProgressTint();
        applyIndeterminateProgressTint();
    }

    private void applyDeterminateProgressTint() {
        if (mProgressTint.mHasTintList || mProgressTint.mHasTintMode) {
            Drawable drawable = getProgressDrawable();
            if (drawable != null) {
                applyTintForDrawable(drawable, mProgressTint);
            }
        }
    }

    private void applyIndeterminateProgressTint() {
        if (mProgressTint.mHasTintList || mProgressTint.mHasTintMode) {
            Drawable drawable = getIndeterminateDrawable();
            if (drawable != null) {
                applyTintForDrawable(drawable, mProgressTint);
            }
        }
    }

    // Progress drawables in this library has already rewritten tint related methods for
    // compatibility.
    @SuppressLint("NewApi")
    private void applyTintForDrawable(Drawable drawable, TintInfo tint) {

        if (tint.mHasTintList || tint.mHasTintMode) {

            if (tint.mHasTintList) {
                if (drawable instanceof TintableDrawable) {
                    ((TintableDrawable) drawable).setTintList(tint.mTintList);
                } else {
                    Log.w(TAG, "drawable did not implement TintableDrawable, it won't be tinted below Lollipop");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable.setTintList(tint.mTintList);
                    }
                }
            }

            if (tint.mHasTintMode) {
                if (drawable instanceof TintableDrawable) {
                    ((TintableDrawable) drawable).setTintMode(tint.mTintMode);
                } else {
                    Log.w(TAG, "drawable did not implement TintableDrawable, it won't be tinted below Lollipop");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable.setTintMode(tint.mTintMode);
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
        boolean mHasTintList;
        ColorStateList mTintList;
        boolean mHasTintMode;
        PorterDuff.Mode mTintMode;
    }
}
