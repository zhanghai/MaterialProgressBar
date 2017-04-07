package me.zhanghai.android.materialprogressbar;

import android.graphics.Canvas;

/**
 * Implement background by using a full (i.e. 100 %) SingleCircularProgressDrawable (see level)
 */
public class SingleCircularProgressBackgroundDrawable extends SingleCircularProgressDrawable
        implements ShowBackgroundDrawable {

    private boolean mShow = true;

    public SingleCircularProgressBackgroundDrawable() {
        super(MaterialProgressBar.DETERMINATE_CIRCULAR_STYLE_FIXEDSTARTTOP);
    }

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
    protected int getLevelImpl() {
        return LEVEL_MAX;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mShow) {
            super.draw(canvas);
        }
    }
}
