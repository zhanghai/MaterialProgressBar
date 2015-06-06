package me.zhanghai.android.materialprogressbar.internal;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Build;
import android.util.Property;

public class ObjectAnimatorCompat {

    private ObjectAnimatorCompat() {}

    public static ObjectAnimator ofArgb(Object target, String propertyName, int... values) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ObjectAnimatorCompatLollipop.ofArgb(target, propertyName, values);
        }
        return ObjectAnimatorCompatBase.ofArgb(target, propertyName, values);
    }

    public static <T> ObjectAnimator ofArgb(T target, Property<T, Integer> property,
                                            int... values) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ObjectAnimatorCompatLollipop.ofArgb(target, property, values);
        }
        return ObjectAnimatorCompatBase.ofArgb(target, property, values);
    }

    public static ObjectAnimator ofFloat(Object target, String xPropertyName, String yPropertyName,
                                         Path path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ObjectAnimatorCompatLollipop.ofFloat(target, xPropertyName, yPropertyName, path);
        }
        return ObjectAnimatorCompatBase.ofFloat(target, xPropertyName, yPropertyName, path);
    }

    public static <T> ObjectAnimator ofFloat(T target, Property<T, Float> xProperty,
                                             Property<T, Float> yProperty, Path path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ObjectAnimatorCompatLollipop.ofFloat(target, xProperty, yProperty, path);
        }
        return ObjectAnimatorCompatBase.ofFloat(target, xProperty, yProperty, path);
    }

    public static ObjectAnimator ofInt(Object target, String xPropertyName, String yPropertyName,
                                       Path path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ObjectAnimatorCompatLollipop.ofInt(target, xPropertyName, yPropertyName, path);
        }
        return ObjectAnimatorCompatBase.ofInt(target, xPropertyName, yPropertyName, path);
    }

    public static <T> ObjectAnimator ofInt(T target, Property<T, Integer> xProperty,
                                           Property<T, Integer> yProperty, Path path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ObjectAnimatorCompatLollipop.ofInt(target, xProperty, yProperty, path);
        }
        return ObjectAnimatorCompatBase.ofInt(target, xProperty, yProperty, path);
    }
}
