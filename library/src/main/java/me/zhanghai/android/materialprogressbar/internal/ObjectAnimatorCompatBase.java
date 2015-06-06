package me.zhanghai.android.materialprogressbar.internal;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.util.Property;

class ObjectAnimatorCompatBase {

    private static final int NUM_POINTS = 500;

    private ObjectAnimatorCompatBase() {}

    @NonNull
    public static ObjectAnimator ofFloat(Object target, String xPropertyName, String yPropertyName,
                                         @NonNull Path path) {

        float[] xValues = new float[NUM_POINTS];
        float[] yValues = new float[NUM_POINTS];
        calculateXYValues(path, xValues, yValues);

        PropertyValuesHolder xPvh = PropertyValuesHolder.ofFloat(xPropertyName, xValues);
        PropertyValuesHolder yPvh = PropertyValuesHolder.ofFloat(yPropertyName, yValues);

        return ObjectAnimator.ofPropertyValuesHolder(target, xPvh, yPvh);
    }

    @NonNull
    public static <T> ObjectAnimator ofFloat(T target, Property<T, Float> xProperty,
                                             Property<T, Float> yProperty, @NonNull Path path) {

        float[] xValues = new float[NUM_POINTS];
        float[] yValues = new float[NUM_POINTS];
        calculateXYValues(path, xValues, yValues);

        PropertyValuesHolder xPvh = PropertyValuesHolder.ofFloat(xProperty, xValues);
        PropertyValuesHolder yPvh = PropertyValuesHolder.ofFloat(yProperty, yValues);

        return ObjectAnimator.ofPropertyValuesHolder(target, xPvh, yPvh);
    }

    private static void calculateXYValues(@NonNull Path path, @Size(NUM_POINTS) float[] xValues,
                                          @Size(NUM_POINTS) float[] yValues) {

        PathMeasure pathMeasure = new PathMeasure(path, true /* forceClosed */);
        float pathLength = pathMeasure.getLength();

        float[] position = new float[2];
        for (int i = 0; i < NUM_POINTS; ++i) {
            float distance = (i * pathLength) / (NUM_POINTS - 1);
            pathMeasure.getPosTan(distance, position, null /* tangent */);
            xValues[i] = position[0];
            yValues[i] = position[1];
        }
    }
}
