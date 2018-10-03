/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar.sample;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class DeterminateCircularSampleActivity extends AppCompatActivity {

    @BindViews({
            R.id.normal_progress,
            R.id.tinted_normal_progress,
            R.id.dynamic_progress,
            R.id.tinted_dynamic_progress
    })
    ProgressBar[] mPrimaryProgressBars;
    @BindViews({
            R.id.normal_secondary_progress,
            R.id.normal_background_progress,
            R.id.tinted_normal_secondary_progress,
            R.id.tinted_normal_background_progress,
            R.id.dynamic_secondary_progress,
            R.id.dynamic_background_progress,
            R.id.tinted_dynamic_secondary_progress,
            R.id.tinted_dynamic_background_progress
    })
    ProgressBar[] mPrimaryAndSecondaryProgressBars;

    private ValueAnimator mPrimaryProgressAnimator;
    private ValueAnimator mPrimaryAndSecondaryProgressAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.determinate_circular_sample_activity);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPrimaryProgressAnimator =
                Animators.makeDeterminateCircularPrimaryProgressAnimator(mPrimaryProgressBars);
        mPrimaryAndSecondaryProgressAnimator =
                Animators.makeDeterminateCircularPrimaryAndSecondaryProgressAnimator(
                        mPrimaryAndSecondaryProgressBars);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        mPrimaryProgressAnimator.start();
        mPrimaryAndSecondaryProgressAnimator.start();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mPrimaryProgressAnimator.end();
        mPrimaryAndSecondaryProgressAnimator.end();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AppUtils.navigateUp(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
