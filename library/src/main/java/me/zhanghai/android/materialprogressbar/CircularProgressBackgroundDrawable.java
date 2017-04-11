/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.graphics.Canvas;
import android.graphics.Paint;

class CircularProgressBackgroundDrawable extends BaseSingleCircularProgressDrawable
        implements ShowBackgroundDrawable {

    private boolean mShow = true;

    @Override
    public boolean getShowBackground() {
        return mShow;
    }

    @Override
    public void setShowBackground(boolean show) {
        if (mShow != show) {
            mShow = show;
            invalidateSelf();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mShow) {
            super.draw(canvas);
        }
    }

    @Override
    protected void onDrawRing(Canvas canvas, Paint paint) {
        drawRing(canvas, paint, 0, 360);
    }
}
