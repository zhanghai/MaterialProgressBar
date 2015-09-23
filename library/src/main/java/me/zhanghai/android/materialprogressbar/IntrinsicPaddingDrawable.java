/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

public interface IntrinsicPaddingDrawable {

    /**
     * Get whether this {@code Drawable} is using an intrinsic padding. The default is true.
     *
     * @return whether this {@code Drawable} is using an intrinsic padding.
     */
    boolean getUseIntrinsicPadding();

    /**
     * Set whether this {@code Drawable} should use an intrinsic padding. The default is true.
     */
    void setUseIntrinsicPadding(boolean useIntrinsicPadding);
}
