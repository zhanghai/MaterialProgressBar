package me.zhanghai.android.materialprogressbar.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.util.Arrays;
import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class DeterminateCircularSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.determinate_circular_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDeterminateCircleProgressBars();
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

    private void setupDeterminateCircleProgressBars() {
        final MaterialProgressBar dcDefault = (MaterialProgressBar) findViewById(R.id.dc_default);
        final MaterialProgressBar dcSecondary = (MaterialProgressBar) findViewById(R.id.dc_secondary);
        final MaterialProgressBar dcSecondaryBackground = (MaterialProgressBar) findViewById(R.id.dc_secondary_background);

        final MaterialProgressBar dcTinted = (MaterialProgressBar) findViewById(R.id.dc_tinted);
        final MaterialProgressBar dcTintedSecondary = (MaterialProgressBar) findViewById(R.id.dc_tinted_secondary);
        final MaterialProgressBar dcTintedSecondaryBackground = (MaterialProgressBar) findViewById(R.id.dc_tinted_secondary_background);

        final MaterialProgressBar dcmDefault = (MaterialProgressBar) findViewById(R.id.dc_moving_default);
        final MaterialProgressBar dcmSecondary = (MaterialProgressBar) findViewById(R.id.dc_moving_secondary);
        final MaterialProgressBar dcmSecondaryBackground = (MaterialProgressBar) findViewById(R.id.dc_moving_secondary_background);

        final MaterialProgressBar dcmTinted = (MaterialProgressBar) findViewById(R.id.dc_moving_tinted);
        final MaterialProgressBar dcmTintedSecondary = (MaterialProgressBar) findViewById(R.id.dc_moving_tinted_secondary);
        final MaterialProgressBar dcmTintedSecondaryBackground = (MaterialProgressBar) findViewById(R.id.dc_moving_tinted_secondary_background);

        final List<MaterialProgressBar> all = Arrays.asList(
                dcDefault,
                dcSecondary,
                dcSecondaryBackground,
                dcTinted,
                dcTintedSecondary,
                dcTintedSecondaryBackground,

                dcmDefault,
                dcmSecondary,
                dcmSecondaryBackground,
                dcmTinted,
                dcmTintedSecondary,
                dcmTintedSecondaryBackground
        );

        final List<MaterialProgressBar> withSecondary = Arrays.asList(
                dcSecondary,
                dcSecondaryBackground,
                dcTintedSecondary,
                dcTintedSecondaryBackground,

                dcmSecondary,
                dcmSecondaryBackground,
                dcmTintedSecondary,
                dcmTintedSecondaryBackground
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
