/*
 * Copyright 2019 Dionysios Karatzas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rams.jenkinsapp;

import static com.rams.jenkinsapp.inapp.update.Constants.FLEXIBLE_UPDATE;
import static com.rams.jenkinsapp.inapp.update.Constants.IMMEDIATE_UPDATE;
import static com.rams.jenkinsapp.inapp.update.Constants.UpdateMode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import com.rams.jenkinsapp.inapp.update.InAppUpdateManager;
import com.rams.jenkinsapp.inapp.update.InAppUpdateStatus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InAppUpdateActivity extends AppCompatActivity implements InAppUpdateManager.InAppUpdateHandler {
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private static final String TAG = "MainActivity";
    private InAppUpdateManager inAppUpdateManager;

    @BindView(R.id.toggle_button_group)
    protected MaterialButtonToggleGroup toggleGroup;
    @BindView(R.id.bt_update)
    protected Button updateButton;
    @BindView(R.id.progressBar)
    protected ProgressBar progressBar;
    @BindView(R.id.tv_available_version)
    protected TextView tvVersionCode;
    @BindView(R.id.tv_update_available)
    protected TextView tvUpdateAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        inAppUpdateManager = InAppUpdateManager.Builder(this)
                .resumeUpdates(true)
                .handler(this);

        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (checkedId == R.id.tb_immediate && isChecked) {
                inAppUpdateManager.mode(UpdateMode.IMMEDIATE);
            } else {
                inAppUpdateManager
                        .mode(UpdateMode.FLEXIBLE)
                        .useCustomNotification(true)
                        .snackBarMessage("An update has just been downloaded.")
                        .snackBarAction("RESTART")
                        .handler(this);
            }
        });
        updateButton.setOnClickListener(view ->
                inAppUpdateManager.checkForAppUpdate()
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        View rootView = this.getWindow().getDecorView().findViewById(android.R.id.content);
        if (requestCode == IMMEDIATE_UPDATE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.e(TAG, "force update cancel requestC:" + requestCode);
                inAppUpdateManager.checkForAppUpdate();
            } else if (resultCode == Activity.RESULT_OK) {
                Log.e(TAG, "Force Update else" + requestCode);
                Snackbar.make(rootView, "FU Update success!", Snackbar.LENGTH_LONG).show();
                super.onActivityResult(requestCode, resultCode, data);
            } else {
                Log.e(TAG, "Force Update else" + requestCode);
                Snackbar.make(rootView, "FU Update Failed!", Snackbar.LENGTH_LONG).show();
                inAppUpdateManager.checkForAppUpdate();
                super.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == FLEXIBLE_UPDATE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.e(TAG, "request code softupgrade cancel" + requestCode);
            } else if (resultCode == Activity.RESULT_OK) {
                Log.e(TAG, "request code softupgrade" + requestCode);
                Snackbar.make(rootView, "SU Update success!", Snackbar.LENGTH_LONG).show();
                super.onActivityResult(requestCode, resultCode, data);
            } else {
                Log.e(TAG, "request code softupgrade" + requestCode);
                Snackbar.make(rootView, "SU Update Failed!", Snackbar.LENGTH_LONG).show();
                inAppUpdateManager.checkForAppUpdate();
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // InAppUpdateHandler implementation

    @Override
    public void onInAppUpdateError(int code, Throwable error) {
        /*
         * Called when some error occurred. See Constants class for more details
         */
        Log.e(TAG, "code: " + code, error);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onInAppUpdateStatus(InAppUpdateStatus status) {

        /*
         * Called when the update status change occurred.
         */

        progressBar.setVisibility(status.isDownloading() ? View.VISIBLE : View.GONE);

        Log.e(TAG,"Available version code is  : "+status.availableVersionCode());
        Log.e(TAG,"is Update available  : "+status.isUpdateAvailable());

        tvVersionCode.setText(String.format("Available version code: %d", status.availableVersionCode()));
        tvUpdateAvailable.setText(String.format("Is Update available : %s", String.valueOf(status.isUpdateAvailable())));
//        tvUpdateAvailable.setText(String.format("Update available: %s", String.valueOf(status.isUpdateAvailable())));

        if (status.isDownloaded()) {
            updateButton.setText("Complete Update");
            updateButton.setOnClickListener(view -> inAppUpdateManager.completeUpdate());
        }
    }

}
