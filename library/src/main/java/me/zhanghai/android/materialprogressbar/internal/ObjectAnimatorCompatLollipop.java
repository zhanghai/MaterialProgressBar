package me.zhanghai.android.materialprogressbar.internal;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Property;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class ObjectAnimatorCompatLollipop {

    private ObjectAnimatorCompatLollipop() {}

    @NonNull
    public static ObjectAnimator ofFloat(Object target, String xPropertyName, String yPropertyName,
                                         Path path) {
        return ObjectAnimator.ofFloat(target, xPropertyName, yPropertyName, path);
    }

    @NonNull
    public static <T> ObjectAnimator ofFloat(T target, Property<T, Float> xProperty,
                                             Property<T, Float> yProperty, @NonNull Path path) {
        return ObjectAnimator.ofFloat(target, xProperty, yProperty, path);
    }
}
