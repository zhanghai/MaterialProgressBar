/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.zhanghai.android.materialprogressbar.ProgressIndeterminateDrawable;

public class AboutActivity extends AppCompatActivity {

    @InjectView(R.id.about_icon)
    ProgressBar iconProgress;
    @InjectView(R.id.about_version)
    TextView versionText;
    @InjectView(R.id.about_github)
    TextView githubText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.about_activity);
        ButterKnife.inject(this);

        iconProgress.setIndeterminateDrawable(new ProgressIndeterminateDrawable(this));
        String version = getString(R.string.about_version,
                AppUtils.getPackageInfo(this).versionName);
        versionText.setText(version);
        githubText.setMovementMethod(LinkMovementMethod.getInstance());
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
