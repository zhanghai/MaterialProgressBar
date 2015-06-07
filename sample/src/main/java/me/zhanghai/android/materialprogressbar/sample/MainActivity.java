/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.zhanghai.android.materialprogressbar.ProgressHorizontalDrawable;
import me.zhanghai.android.materialprogressbar.ProgressIndeterminateDrawable;
import me.zhanghai.android.materialprogressbar.ProgressIndeterminateHorizontalDrawable;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.horizontal_progress_library)
    ProgressBar libraryHorizontalProgress;
    @InjectView(R.id.indeterminate_horizontal_progress_library)
    ProgressBar libraryIndeterminateHorizontalProgress;
    @InjectView(R.id.indeterminate_horizontal_progress_library_toolbar)
    ProgressBar libraryIndeterminateHorizontalProgressToolbar;
    @InjectView(R.id.indeterminate_progress_large_library)
    ProgressBar libraryIndeterminateProgressLarge;
    @InjectView(R.id.indeterminate_progress_library)
    ProgressBar libraryIndeterminateProgress;
    @InjectView(R.id.indeterminate_progress_small_library)
    ProgressBar libraryIndeterminateProgressSmall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        ButterKnife.inject(this);

        libraryHorizontalProgress.setProgressDrawable(new ProgressHorizontalDrawable(this));
        libraryIndeterminateHorizontalProgress.setIndeterminateDrawable(
                new ProgressIndeterminateHorizontalDrawable(this));
        ProgressIndeterminateHorizontalDrawable toolbarDrawable =
                new ProgressIndeterminateHorizontalDrawable(this);
        toolbarDrawable.setShowTrack(false);
        toolbarDrawable.setUseIntrinsicPadding(false);
        libraryIndeterminateHorizontalProgressToolbar.setIndeterminateDrawable(
                toolbarDrawable);
        libraryIndeterminateProgressLarge.setIndeterminateDrawable(new ProgressIndeterminateDrawable(this));
        libraryIndeterminateProgress.setIndeterminateDrawable(new ProgressIndeterminateDrawable(this));
        libraryIndeterminateProgressSmall.setIndeterminateDrawable(new ProgressIndeterminateDrawable(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
