package me.zhanghai.android.materialprogressbar.internal;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Property;

public class ObjectAnimatorCompat {

    private ObjectAnimatorCompat() {}

    @NonNull
    public static ObjectAnimator ofFloat(Object target, String xPropertyName, String yPropertyName,
                                         Path path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ObjectAnimatorCompatLollipop.ofFloat(target, xPropertyName, yPropertyName, path);
        }
        return ObjectAnimatorCompatBase.ofFloat(target, xPropertyName, yPropertyName, path);
    }

    @NonNull
    public static <T> ObjectAnimator ofFloat(T target, Property<T, Float> xProperty,
                                             Property<T, Float> yProperty, @NonNull Path path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ObjectAnimatorCompatLollipop.ofFloat(target, xProperty, yProperty, path);
        }
        return ObjectAnimatorCompatBase.ofFloat(target, xProperty, yProperty, path);
    }
}
