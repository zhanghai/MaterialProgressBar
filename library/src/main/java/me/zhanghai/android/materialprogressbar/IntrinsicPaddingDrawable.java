/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

public interface IntrinsicPaddingDrawable {

    /**
     * Get whether this drawable is using an intrinsic padding. The default is true.
     *
     * @return Whether this drawable is using an intrinsic padding.
     */
    boolean getUseIntrinsicPadding();

    /**
     * Set whether this drawable should use an intrinsic padding. The default is true.
     *
     * @param useIntrinsicPadding Whether this drawable should use its intrinsic padding.
     */
    void setUseIntrinsicPadding(boolean useIntrinsicPadding);
}
