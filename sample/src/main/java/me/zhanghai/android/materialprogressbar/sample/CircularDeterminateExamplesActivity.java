package me.zhanghai.android.materialprogressbar.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class CircularDeterminateExamplesActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.circular_determinate_activity);
        setupDeterminateCircleProgressBars();
    }

    private void setupDeterminateCircleProgressBars() {
        final MaterialProgressBar cdDefault = (MaterialProgressBar) findViewById(R.id.cd_default);
        final MaterialProgressBar cdTinted = (MaterialProgressBar) findViewById(R.id.cd_tinted);
        final MaterialProgressBar cdSecondary = (MaterialProgressBar) findViewById(R.id.cd_secondary);
        final MaterialProgressBar cdSecondaryTinted = (MaterialProgressBar) findViewById(R.id.cd_secondary_tinted);

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
                                    cdDefault.setProgress(progress);
                                    cdTinted.setProgress(progress);
                                    cdSecondary.setProgress(progress);
                                    cdSecondaryTinted.setProgress(progress);

                                    int secondary = Math.min((int) Math.round(progress*1.333), 100);
                                    cdSecondary.setSecondaryProgress(secondary);
                                    cdSecondaryTinted.setSecondaryProgress(secondary);
                                } else if (progress <= 125) {
                                    // rest at end position
                                    cdDefault.setProgress(100);
                                    cdTinted.setProgress(100);
                                    cdSecondary.setProgress(100);
                                    cdSecondaryTinted.setProgress(100);

                                    cdSecondary.setSecondaryProgress(100);
                                    cdSecondaryTinted.setSecondaryProgress(100);
                                } else if (progress <= 150) {
                                    // rest at start position
                                    cdDefault.setProgress(0);
                                    cdTinted.setProgress(0);
                                    cdSecondary.setProgress(0);
                                    cdSecondaryTinted.setProgress(0);

                                    cdSecondary.setSecondaryProgress(0);
                                    cdSecondaryTinted.setSecondaryProgress(0);
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

}
