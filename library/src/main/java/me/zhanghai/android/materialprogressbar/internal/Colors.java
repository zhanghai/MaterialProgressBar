package me.zhanghai.android.materialprogressbar.internal;

public class Colors {
    // See https://en.wikipedia.org/wiki/Alpha_compositing
    public static float compositeAlpha(float alpha1, float alpha2) {
        return alpha1 + alpha2 * (1 - alpha1);
    }
}
