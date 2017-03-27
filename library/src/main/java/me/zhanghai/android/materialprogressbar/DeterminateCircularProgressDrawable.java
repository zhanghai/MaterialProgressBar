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

class DeterminateCircularProgressDrawable extends LayerDrawable
        implements IntrinsicPaddingDrawable, MaterialProgressDrawable, TintableDrawable {

    @SuppressWarnings("unused")
    private static final String TAG = DeterminateCircularProgressDrawable.class.getSimpleName();

    private SingleCircularProgressDrawable mProgressDrawable;

    public DeterminateCircularProgressDrawable(Context context) {
        super(new Drawable[] {
                new SingleCircularProgressDrawable()
        });

        setId(0, android.R.id.progress);
        mProgressDrawable = (SingleCircularProgressDrawable) getDrawable(0);

        int controlActivatedColor = ThemeUtils.getColorFromAttrRes(R.attr.colorControlActivated,
                context);
        setTint(controlActivatedColor);
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
        mProgressDrawable.setUseIntrinsicPadding(useIntrinsicPadding);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressLint("NewApi")
    public void setTint(@ColorInt int tintColor) {
        mProgressDrawable.setTint(tintColor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressLint("NewApi")
    public void setTintList(@Nullable ColorStateList tint) {
        mProgressDrawable.setTintList(tint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressLint("NewApi")
    public void setTintMode(@NonNull PorterDuff.Mode tintMode) {
        mProgressDrawable.setTintMode(tintMode);
    }
}
