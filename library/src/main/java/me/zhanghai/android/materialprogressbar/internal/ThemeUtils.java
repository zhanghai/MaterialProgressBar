/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar.internal;

import android.content.Context;
import android.content.res.TypedArray;

public class ThemeUtils {

    private ThemeUtils() {}

    public static float getThemeAttrFloat(Context context, int attr) {
        TypedArray a = context.obtainStyledAttributes(null, new int[] {attr});
        try {
            return a.getFloat(0, 0);
        } finally {
            a.recycle();
        }
    }
}
