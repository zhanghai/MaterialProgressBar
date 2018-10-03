/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar.sample;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindViews({
            R.id.determinate_circular_large_progress,
            R.id.determinate_circular_progress,
            R.id.determinate_circular_small_progress
    })
    ProgressBar[] mDeterminateCircularProgressBars;

    private ValueAnimator mDeterminateCircularProgressAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        mDeterminateCircularProgressAnimator =
                Animators.makeDeterminateCircularPrimaryProgressAnimator(
                        mDeterminateCircularProgressBars);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        mDeterminateCircularProgressAnimator.start();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mDeterminateCircularProgressAnimator.end();
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
