package com.rams.jenkinsapp.inapp.update

import android.R
import android.content.IntentSender.SendIntentException
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.rams.jenkinsapp.inapp.update.Constants.FLEXIBLE_UPDATE
import com.rams.jenkinsapp.inapp.update.Constants.IMMEDIATE_UPDATE


class InAppUpdateManager : LifecycleObserver {
    /**
     * Callback methods where update events are reported.
     */
    interface InAppUpdateHandler {
        fun onInAppUpdateError(code: Int, error: Throwable?)
        fun onInAppUpdateStatus(status: InAppUpdateStatus?)
    }

    private var activity: AppCompatActivity
    private var appUpdateManager: AppUpdateManager? = null
    private var requestCode = 6212
    private var snackBarMessage = "An update has just been downloaded."
    private var snackBarAction = "RESTART"
    private var mode = Constants.UpdateMode.FLEXIBLE
    private var resumeUpdates = true
    private var useCustomNotification = false
    private var handler: InAppUpdateHandler? = null
    private var snackbar: Snackbar? = null
    private val inAppUpdateStatus = InAppUpdateStatus()
    private val installStateUpdatedListener: InstallStateUpdatedListener? =
        InstallStateUpdatedListener { installState ->
            inAppUpdateStatus.setInstallState(installState)
            reportStatus()

            // Show module progress, log state, or install the update.
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                Log.e("TAG", "------- In  line 52 " + installState.installStatus())
                popupSnackbarForUserConfirmation()
            }
        }

    private constructor(activity: AppCompatActivity) {
        this.activity = activity
        setupSnackbar()
        init()
    }

    private constructor(activity: AppCompatActivity, requestCode: Int) {
        this.activity = activity
        this.requestCode = requestCode
        init()
    }

    private fun init() {
        setupSnackbar()
        appUpdateManager = AppUpdateManagerFactory.create(activity)
        activity.lifecycle.addObserver(this)
        if (mode == Constants.UpdateMode.FLEXIBLE) appUpdateManager!!.registerListener(
            installStateUpdatedListener!!
        )
        Log.e("TAG", "-------init called")
        checkForUpdate(false)
    }

    fun mode(mode: Constants.UpdateMode): InAppUpdateManager {
        this.mode = mode
        return this
    }

    fun resumeUpdates(resumeUpdates: Boolean): InAppUpdateManager {
        this.resumeUpdates = resumeUpdates
        return this
    }

    fun handler(handler: InAppUpdateHandler?): InAppUpdateManager {
        this.handler = handler
        return this
    }

    fun useCustomNotification(useCustomNotification: Boolean): InAppUpdateManager {
        this.useCustomNotification = useCustomNotification
        return this
    }

    fun snackBarMessage(snackBarMessage: String): InAppUpdateManager {
        this.snackBarMessage = snackBarMessage
        setupSnackbar()
        return this
    }

    fun snackBarAction(snackBarAction: String): InAppUpdateManager {
        this.snackBarAction = snackBarAction
        setupSnackbar()
        return this
    }

    fun snackBarActionColor(color: Int): InAppUpdateManager {
        snackbar!!.setActionTextColor(color)
        return this
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (resumeUpdates) checkNewAppVersionState()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        unregisterListener()
    }

    fun checkForAppUpdate() {
        checkForUpdate(true)
    }

    fun completeUpdate() {
        appUpdateManager!!.completeUpdate()
    }

    private fun checkForUpdate(startUpdate: Boolean) {
        Log.e(LOG_TAG, "checkForUpdate :$startUpdate")
        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
        Log.e(LOG_TAG, "updateinfo :$appUpdateInfoTask")
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            inAppUpdateStatus.setAppUpdateInfo(appUpdateInfo)
            Log.e(LOG_TAG, "onsuccess :" + appUpdateInfo.updateAvailability())
            Log.e(LOG_TAG, "MODE :$mode")
            if (startUpdate) {
                Log.e(LOG_TAG, "inside if :$startUpdate")
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    // Request the update.
                    if (mode == Constants.UpdateMode.FLEXIBLE &&
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                    ) {
                        // Start an update.
                        startAppUpdateFlexible(appUpdateInfo)
                    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        // Start an update.
                        startAppUpdateImmediate(appUpdateInfo)
                    }
                    Log.e(
                        LOG_TAG,
                        "checkForAppUpdate(): Update available. Version Code: " + appUpdateInfo.availableVersionCode()
                    )
                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_NOT_AVAILABLE) {
                    Log.e(
                        LOG_TAG,
                        "checkForAppUpdate(): No Update available. Code: " + appUpdateInfo.updateAvailability()
                    )
                }
            }
            reportStatus()
        }
    }

    private fun startAppUpdateImmediate(appUpdateInfo: AppUpdateInfo) {
        try {
            Log.e("FU Started","Force Update")
            appUpdateManager!!.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.IMMEDIATE,
                activity,
                IMMEDIATE_UPDATE
            )
        } catch (e: SendIntentException) {
            Log.e(LOG_TAG, "error in startAppUpdateImmediate", e)
            reportUpdateError(Constants.UPDATE_ERROR_START_APP_UPDATE_IMMEDIATE, e)
        }
    }

    private fun startAppUpdateFlexible(appUpdateInfo: AppUpdateInfo) {
        try {
            Log.e("SU Started","Soft Update")
            appUpdateManager!!.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.FLEXIBLE,
                activity,
                FLEXIBLE_UPDATE
            )
        } catch (e: SendIntentException) {
            Log.e(LOG_TAG, "error in startAppUpdateFlexible", e)
            reportUpdateError(Constants.UPDATE_ERROR_START_APP_UPDATE_FLEXIBLE, e)
        }
    }

    private fun popupSnackbarForUserConfirmation() {
        if (!useCustomNotification) {
            if (snackbar != null && snackbar!!.isShownOrQueued) snackbar!!.dismiss()
            snackbar!!.show()
        }
    }

    private fun checkNewAppVersionState() {
        Log.e("TAG","------- inside checkNewAppVersionState")
        appUpdateManager?.getAppUpdateInfo()?.addOnSuccessListener { appUpdateInfo ->
            inAppUpdateStatus.setAppUpdateInfo(appUpdateInfo)

            //FLEXIBLE:
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForUserConfirmation()
                Log.e("TAG","------- appUpdate info "+appUpdateInfo.installStatus())
                reportStatus()
                Log.e(
                    LOG_TAG,
                    "checkNewAppVersionState(): resuming flexible update. Code: " + appUpdateInfo.updateAvailability()
                )
            }

            //IMMEDIATE:
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startAppUpdateImmediate(appUpdateInfo)

                Log.e(
                    LOG_TAG,
                    "checkNewAppVersionState(): resuming immediate update. Code: " + appUpdateInfo.updateAvailability()
                )
            }
        }
    }

    private fun setupSnackbar() {
        val rootView = activity.window.decorView.findViewById<View>(R.id.content)
        snackbar = Snackbar.make(
            rootView,
            snackBarMessage,
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar!!.setAction(snackBarAction, { appUpdateManager!!.completeUpdate() })
    }

    private fun unregisterListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null) appUpdateManager!!.unregisterListener(
            installStateUpdatedListener
        )
    }

    private fun reportUpdateError(errorCode: Int, error: Throwable) {
        if (handler != null) {
            handler!!.onInAppUpdateError(errorCode, error)
        }
    }

    private fun reportStatus() {
        if (handler != null) {
            handler!!.onInAppUpdateStatus(inAppUpdateStatus)
        }
    }

    companion object {
        private const val LOG_TAG = "InAppUpdateManager"

        //region Constructor
        private var instance: InAppUpdateManager? = null

        @JvmStatic
        fun Builder(activity: AppCompatActivity): InAppUpdateManager? {
            if (instance == null) {
                instance = InAppUpdateManager(activity)
            }
            return instance
        }

        @JvmStatic
        fun Builder(activity: AppCompatActivity, requestCode: Int): InAppUpdateManager? {
            if (instance == null) {
                instance = InAppUpdateManager(activity, requestCode)
            }
            return instance
        }
    }
}