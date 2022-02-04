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

import static com.rams.jenkinsapp.inapp.update.Constants.UpdateMode;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rams.jenkinsapp.inapp.update.InAppUpdateManager;
import com.rams.jenkinsapp.inapp.update.InAppUpdateStatus;

import java.lang.annotation.Retention;


public class FlexibleDefaultSnackbar extends AppCompatActivity implements InAppUpdateManager.InAppUpdateHandler {
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private static final String TAG = "Sample";

    @Retention(SOURCE)
    @IntDef({NAVIGATION_MODE_STANDARD, NAVIGATION_MODE_LIST, NAVIGATION_MODE_TABS})
    public @interface NavigationMode {}
    public static final int NAVIGATION_MODE_STANDARD = 0;
    public static final int NAVIGATION_MODE_LIST = 1;
    public static final int NAVIGATION_MODE_TABS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InAppUpdateManager inAppUpdateManager = InAppUpdateManager.Builder(this, REQ_CODE_VERSION_UPDATE)
                .resumeUpdates(true) // Resume the update, if the update was stalled. Default is true
                .mode(UpdateMode.FLEXIBLE)
                .useCustomNotification(false) //default is false
                .snackBarMessage("An update has just been downloaded.")
                .snackBarAction("RESTART")
                .handler(this);

        inAppUpdateManager.checkForAppUpdate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE_VERSION_UPDATE) {
            if (resultCode != RESULT_OK) {
                // If the update is cancelled or fails,
                // you can request to start the update again.
                Log.d(TAG, "Update flow failed! Result code: " + resultCode);
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
        Log.d(TAG, "code: " + code, error);
    }

    @Override
    public void onInAppUpdateStatus(InAppUpdateStatus status) {
        /*
         * Called when the update status change occurred. See Constants class for more details
         */
    }

}
'wrapper-sbook-betboo-br',
'wrapper-sbet-uk',
'wrapper-sb-br',
'wrapper-sb-za',
'wrapper-pbull',
'wrapper-gb',
'wrapper-gd',
'wrapper-bwin-be',
'wrapper-bwincom',
'wrapper-bwinfr',
'wrapper-bwin-it',
'wrapper-bwines',
'wrapper-premium',
'wrapper-sbcom',
'wrapper-sbet-ro',
'wrapper-vistabet-gr',
'wrapper-sbet-gr',
'wrapper-bwin-dk',
'wrapper-bwin-gr',
'wrapper-bwin-ru',
'wrapper-partypoker-com',
'wrapper-bwin-se',
'wrapper-playmgm-nj',
'wrapper-borgata-nj',
'wrapper-bwin-co',
'wrapper-mgm-wv',
'wrapper-mgm-in',
'wrapper-mgm-co',
'wrapper-mgm-pa',
'wrapper-sportingbet-de',
'wrapper-bwin-de',
'wrapper-ladbrokes-de',
'wrapper-gamebookers-de',
'wrapper-bpremium-de',
'cas-wrapper-bpremium-de',
'cas-wrapper-gamebookers-de',
'cas-wrapper-ladbrokes-de',
'cas-wrapper-bwin-de',
'cas-wrapper-sbet-de',
'cas-wrapper-partyslotsde',
'cas-wrapper-slotclubde',
'cas-wrapper-mgm-pa',
'cas-wrapper-betboo-br',
'cas-wrapper-sbet-uk',
'cas-wrapper-sbet-cee',
'cas-wrapper-sbet-ro',
'cas-wrapper-sbet-gr',
'cas-wrapper-vbet-gr',
'cas-wrapper-pbull',
'cas-wrapper-bwin-es',
'cas-wrapper-bwin-it',
'cas-wrapper-bwin-gr',
'cas-wrapper-bwin-dk',
'cas-wrapper-bwin-be',
'cas-wrapper-bwin-se',
'cas-wrapper-sb-br',
'cas-wrapper-party',
'cas-wrapper-party-premium',
'cas-wrapper-partycasino-se',
'cas-wrapper-bwin-com',
'cas-wrapper-bwin-live',
'cas-wrapper-premium',
'cas-wrapper-premium-live',
'cas-wrapper-partycasino-es',
'cas-wrapper-partycasino-nj',
'cas-wrapper-borgatacasino-nj',
'cas-wrapper-gd',
'cas-wrapper-foxy-com',
'cas-wrapper-playmgm-nj',
'cas-wrapper-galaspins-com',
'cas-wrapper-galacasino-com',
'cas-wrapper-bwin-co',
'cas-wrapper-ladbrokes-casinogames',
'cas-wrapper-ladbrokes_livecasino',
'bingo-wrapper-gd',
'bingo-wrapper-bwin-it',
'bingo-wrapper-think-com',
'bingo-wrapper-cheeky-com',
'bingo-wrapper-foxy-com',
'bingo-wrapper-galabingo-com',
'cas-wrapper-coral-vegas',
'wrapper-horseracing-mgm-com"