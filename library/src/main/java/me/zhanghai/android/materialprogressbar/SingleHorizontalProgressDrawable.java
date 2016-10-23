/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

class SingleHorizontalProgressDrawable extends BaseSingleHorizontalProgressDrawable {

    private static final int LEVEL_MAX = 10000;

    public SingleHorizontalProgressDrawable(Context context) {
        super(context);
    }

    @Override
    protected boolean onLevelChange(int level) {
        invalidateSelf();
        return true;
    }

    @Override
    protected void onDrawRect(Canvas canvas, Paint paint) {

        int level = getLevel();
        if (level == 0) {
            return;
        }

        int saveCount = canvas.save();
        canvas.scale((float) level / LEVEL_MAX, 1, RECT_BOUND.left, 0);

        super.onDrawRect(canvas, paint);

        canvas.restoreToCount(saveCount);
    }
}
