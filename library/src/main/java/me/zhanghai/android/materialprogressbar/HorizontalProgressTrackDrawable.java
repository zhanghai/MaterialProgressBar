/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.content.Context;
import android.graphics.Canvas;

class HorizontalProgressTrackDrawable extends BaseSingleHorizontalProgressDrawable
        implements ShowTrackDrawable {

    private boolean mShowTrack = true;

    public HorizontalProgressTrackDrawable(Context context) {
        super(context);
    }

    @Override
    public boolean getShowTrack() {
        return mShowTrack;
    }

    @Override
    public void setShowTrack(boolean showTrack) {
        if (mShowTrack != showTrack) {
            mShowTrack = showTrack;
            invalidateSelf();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mShowTrack) {
            super.draw(canvas);
        }
    }
}
