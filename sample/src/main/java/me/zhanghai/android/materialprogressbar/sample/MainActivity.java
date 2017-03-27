/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        setupDeterminateCircleProgressBars();
    }

    private void setupDeterminateCircleProgressBars() {
        final MaterialProgressBar determinateCircular1 = (MaterialProgressBar) findViewById(R.id.determinate_circular1);
        final MaterialProgressBar determinateCircular2 = (MaterialProgressBar) findViewById(R.id.determinate_circular2);
        final MaterialProgressBar determinateCircular3 = (MaterialProgressBar) findViewById(R.id.determinate_circular3);

        new Thread() {
            @Override
            public void run() {
                long count = 0;
                while (true) {
                    try {
                        count++;

                        final int progress = (int) count%151; // progress = 0, ..., 150

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (0 <= progress && progress <= 100) {
                                    // progress
                                    determinateCircular1.setProgress(progress);
                                    determinateCircular2.setProgress(progress);
                                    determinateCircular3.setProgress(progress);
                                } else if (progress <= 125) {
                                    // rest at end position
                                    determinateCircular1.setProgress(100);
                                    determinateCircular2.setProgress(100);
                                    determinateCircular3.setProgress(100);
                                } else if (progress <= 150) {
                                    // rest at start position
                                    determinateCircular1.setProgress(0);
                                    determinateCircular2.setProgress(0);
                                    determinateCircular3.setProgress(0);
                                } else {
                                    Log.wtf(TAG, "Broken value for `progress`");
                                }
                            }
                        });

                        sleep(50);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }.start();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
