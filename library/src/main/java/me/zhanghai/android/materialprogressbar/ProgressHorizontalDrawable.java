/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import me.zhanghai.android.materialprogressbar.internal.ThemeUtils;

public class ProgressHorizontalDrawable extends LayerDrawable {

    public ProgressHorizontalDrawable(Context context) {
        super(new Drawable[]{
                new SingleProgressHorizontalDrawable(context),
                new SingleProgressHorizontalDrawable(context),
                new SingleProgressHorizontalDrawable(context)
        });

        setId(0, android.R.id.background);

        setId(1, android.R.id.secondaryProgress);
        SingleProgressHorizontalDrawable secondaryProgressDrawable =
                (SingleProgressHorizontalDrawable) getDrawable(1);
        float disabledAlpha = ThemeUtils.getThemeAttrFloat(context, android.R.attr.disabledAlpha);
        int secondaryAlpha = Math.round(disabledAlpha * 0xFF);
        secondaryProgressDrawable.setAlpha(secondaryAlpha);
        secondaryProgressDrawable.setShowTrack(false);

        setId(2, android.R.id.progress);
        SingleProgressHorizontalDrawable progressDrawable =
                (SingleProgressHorizontalDrawable) getDrawable(2);
        progressDrawable.setShowTrack(false);
    }
}
