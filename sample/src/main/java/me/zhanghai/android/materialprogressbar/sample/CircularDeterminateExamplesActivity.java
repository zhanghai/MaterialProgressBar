package me.zhanghai.android.materialprogressbar.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

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
        final MaterialProgressBar cdSecondary = (MaterialProgressBar) findViewById(R.id.cd_secondary);
        final MaterialProgressBar cdSecondaryBackground = (MaterialProgressBar) findViewById(R.id.cd_secondary_background);

        final MaterialProgressBar cdTinted = (MaterialProgressBar) findViewById(R.id.cd_tinted);
        final MaterialProgressBar cdTintedSecondary = (MaterialProgressBar) findViewById(R.id.cd_tinted_secondary);
        final MaterialProgressBar cdTintedSecondaryBackground = (MaterialProgressBar) findViewById(R.id.cd_tinted_secondary_background);

        final List<MaterialProgressBar> all = Arrays.asList(
                cdDefault,
                cdSecondary,
                cdSecondaryBackground,
                cdTinted,
                cdTintedSecondary,
                cdTintedSecondaryBackground
        );

        final List<MaterialProgressBar> withSecondary = Arrays.asList(
                cdSecondary,
                cdSecondaryBackground,
                cdTintedSecondary,
                cdTintedSecondaryBackground
        );

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
                                    for (MaterialProgressBar bar : all) {
                                        bar.setProgress(progress);
                                    }

                                    int secondary = Math.min((int) Math.round(progress*1.333), 100);
                                    for (MaterialProgressBar bar : withSecondary) {
                                        bar.setSecondaryProgress(secondary);
                                    }
                                } else if (progress <= 125) {
                                    // rest at end position
                                    for (MaterialProgressBar bar : all) {
                                        bar.setProgress(100);
                                    }

                                    for (MaterialProgressBar bar : withSecondary) {
                                        bar.setSecondaryProgress(100);
                                    }
                                } else if (progress <= 150) {
                                    // rest at start position
                                    for (MaterialProgressBar bar : all) {
                                        bar.setProgress(0);
                                    }

                                    for (MaterialProgressBar bar : withSecondary) {
                                        bar.setSecondaryProgress(0);
                                    }
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
