package me.zhanghai.android.materialprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.widget.ProgressBar;

public abstract class BaseSingleCircularProgressDrawable extends BaseProgressDrawable {

    /**
     * A dummy value smaller or equal to any size we need to use this because
     * {@link ProgressBar#setProgressDrawable(Drawable)} sets maxHeight to at
     * least this value. 12px = 12dp on low-end 1x MDPI device, and 12dp is smaller
     * than the Small.NoPadding style, so this should be a good lower bound.
     */
    static final private int INTRINSIC_SIZE_IN_PX = 12;

    protected static final RectF RECT_BOUND = new RectF(-21, -21, 21, 21);
    protected static final RectF RECT_PADDED_BOUND = new RectF(-24, -24, 24, 24);
    protected static final RectF RECT_PROGRESS = new RectF(-19, -19, 19, 19);

    @Override
    public int getIntrinsicHeight() {
        return INTRINSIC_SIZE_IN_PX;
    }

    @Override
    public int getIntrinsicWidth() {
        return INTRINSIC_SIZE_IN_PX;
    }

    @Override
    protected void onPreparePaint(Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
    }

    @Override
    protected void onDraw(Canvas canvas, int width, int height, Paint paint) {

        if (mUseIntrinsicPadding) {
            canvas.scale(width / RECT_PADDED_BOUND.width(), height / RECT_PADDED_BOUND.height());
            canvas.translate(RECT_PADDED_BOUND.width() / 2, RECT_PADDED_BOUND.height() / 2);
        } else {
            canvas.scale(width / RECT_BOUND.width(), height / RECT_BOUND.height());
            canvas.translate(RECT_BOUND.width() / 2, RECT_BOUND.height() / 2);
        }

        onDrawRing(canvas, paint);
    }

    abstract protected void onDrawRing(Canvas canvas, Paint paint);
}
