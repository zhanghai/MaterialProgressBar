package me.zhanghai.android.materialprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class SingleCircularProgressDrawable extends BaseSingleCircularProgressDrawable {

    @SuppressWarnings("unused")
    private static final String TAG = SingleCircularProgressDrawable.class.getSimpleName();

    // Offset that must be added to angles in order to start at 12 o'clock
    private static final float CANVAS_IMPLEMENTATION_ANGLE_OFFSET = -90;

    // leading and trailing angles start at 15° or 5 minutes
    // https://storage.googleapis.com/material-design/publish/material_v_10/assets/0B14F_FSUCc01N2kzc1hlaFR5WlU/061101_Circular_Sheet_xhdpi_005.webm
    private static final float LEADING_ANGLE_MIN = 15;
    private static final float TRAILING_ANGLE_MIN = 15;
    private static final float LEADING_ANGLE_MAX = 720;
    private static final float TRAILING_ANGLE_MAX = 360;

    /**
     * Value from {@link Drawable#getLevel()}
     */
    protected static final int LEVEL_MAX = 10000;

    public SingleCircularProgressDrawable() {
        super();
    }

    @Override
    protected boolean onLevelChange(int level) {
        invalidateSelf();
        return true;
    }

    /**
     * Custom wrapper around final {@link Drawable#getLevel()} such that we can override it
     */
    protected int getLevelImpl() {
        return getLevel();
    }

    @Override
    protected void onDrawRing(Canvas canvas, Paint paint) {

        int level = getLevelImpl();
        if (level == 0) {
            return;
        }

        int saveCount = canvas.save();

        float ratio = (float) level / LEVEL_MAX;
        float leadingAngle = LEADING_ANGLE_MIN + ratio*(LEADING_ANGLE_MAX - LEADING_ANGLE_MIN);
        float trailingAngle = TRAILING_ANGLE_MIN + ratio*(TRAILING_ANGLE_MAX - TRAILING_ANGLE_MIN);
        float sweepAngle = leadingAngle - trailingAngle;

        canvas.drawArc(RECT_PROGRESS, trailingAngle + CANVAS_IMPLEMENTATION_ANGLE_OFFSET, sweepAngle, false, paint);

        canvas.restoreToCount(saveCount);
    }
}
