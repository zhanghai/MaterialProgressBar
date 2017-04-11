/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar.internal;

public class ColorUtils {

    private ColorUtils() {}

    public static float compositeAlpha(float alpha1, float alpha2) {
        // See https://en.wikipedia.org/wiki/Alpha_compositing
        return alpha1 + alpha2 * (1 - alpha1);
    }
}
