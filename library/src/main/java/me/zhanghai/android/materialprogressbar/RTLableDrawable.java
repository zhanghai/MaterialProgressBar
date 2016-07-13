/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

/**
 * A {@code Drawable} that supports right-to-left layout.
 */
public interface RTLableDrawable {

    /**
     * Get whether this drawable is right-to-left. The default is {@code false}.
     *
     * @return Whether this drawable is right-to-left.
     */
    boolean isRTL();

    /**
     * Set whether this drawable should be right-to-left. The default is {@code false}.
     *
     * @param rtl Whether this drawable should be right-to-left.
     */
    void setRTL(boolean rtl);
}
