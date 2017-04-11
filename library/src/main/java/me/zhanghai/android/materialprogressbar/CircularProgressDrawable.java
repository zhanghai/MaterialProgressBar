package me.zhanghai.android.materialprogressbar;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * A new {@code Drawable} for determinate circular {@code ProgressBar}.
 *
 * Package private as this does only work together with MaterialProgressBar and is not suitable
 * for another ProgressBar
 */
class CircularProgressDrawable extends BaseProgressLayerDrawable<SingleCircularProgressDrawable,
        CircularProgressBackgroundDrawable> {

    /**
     * Create a new {@code CircularProgressDrawable}.
     *
     * @param context the {@code Context} for retrieving style information.
     */
    CircularProgressDrawable(int style, Context context) {
        super(new Drawable[] {
                new CircularProgressBackgroundDrawable(),
                new SingleCircularProgressDrawable(style),
                new SingleCircularProgressDrawable(style),
        }, context);
    }
}
