package me.zhanghai.android.materialprogressbar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

class SingleCircularProgressDrawable extends BaseSingleCircularProgressDrawable
        implements ShowBackgroundDrawable {

    /**
     * Value from {@link Drawable#getLevel()}
     */
    private static final int LEVEL_MAX = 10000;

    private final float mStartAngleMin;
    private final float mStartAngleMax;
    private final float mEndAngleMin;
    private final float mEndAngleMax;

    private boolean mShowBackground;

    SingleCircularProgressDrawable(int determinateCircularStyle) {
        super();

        switch (determinateCircularStyle) {
            case MaterialProgressBar.DETERMINATE_CIRCULAR_STYLE_FIXEDSTARTTOP:
                mStartAngleMin = 0;
                mStartAngleMax = 0;
                mEndAngleMin = 0;
                mEndAngleMax = 360;
                break;
            case MaterialProgressBar.DETERMINATE_CIRCULAR_STYLE_MOVINSTART:
                // leading and trailing angles start at 15° or 5 minutes
                // https://storage.googleapis.com/material-design/publish/material_v_10/assets/0B14F_FSUCc01N2kzc1hlaFR5WlU/061101_Circular_Sheet_xhdpi_005.webm
                mStartAngleMin = 15;
                mStartAngleMax = 360;
                mEndAngleMin = 15;
                mEndAngleMax = 720;
                break;
            default:
                throw new AssertionError("Invalid value for determinateCircularStyle");
        }

    }

    @Override
    protected boolean onLevelChange(int level) {
        invalidateSelf();
        return true;
    }

    @Override
    public boolean getShowBackground() {
        return mShowBackground;
    }

    @Override
    public void setShowBackground(boolean show) {
        if (mShowBackground != show) {
            mShowBackground = show;
            invalidateSelf();
        }
    }

    @Override
    protected void onDrawRing(Canvas canvas, Paint paint) {

        int level = getLevel();
        if (level == 0) {
            return;
        }

        float ratio = (float) level / LEVEL_MAX;
        float startAngle = mStartAngleMin + ratio * (mStartAngleMax - mStartAngleMin);
        float endAngle = mEndAngleMin + ratio * (mEndAngleMax - mEndAngleMin);
        float sweepAngle = endAngle - startAngle;

        drawRing(canvas, paint, startAngle, sweepAngle);
        if (mShowBackground) {
            // Draw twice to emulate the background for secondary progress.
            super.drawRing(canvas, paint, startAngle, sweepAngle);
        }
    }
}
