/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar.sample;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindViews({ R.id.determinate_circular_progress_1, R.id.determinate_circular_progress_2,
            R.id.determinate_circular_progress_3 })
    ProgressBar[] mCircularDeterminateProgresses;

    private ValueAnimator mCircularDeterminateProgressAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        mCircularDeterminateProgressAnimator = ValueAnimator.ofInt(0, 150);
        mCircularDeterminateProgressAnimator.setDuration(6000);
        mCircularDeterminateProgressAnimator.setInterpolator(new LinearInterpolator());
        mCircularDeterminateProgressAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mCircularDeterminateProgressAnimator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            for (ProgressBar progressBar : mCircularDeterminateProgresses) {
                                progressBar.setProgress((int) animator.getAnimatedValue());
                            }
                        }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mCircularDeterminateProgressAnimator.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mCircularDeterminateProgressAnimator.end();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.action_determinate_circular_sample:
                startActivity(new Intent(this, DeterminateCircularSampleActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
