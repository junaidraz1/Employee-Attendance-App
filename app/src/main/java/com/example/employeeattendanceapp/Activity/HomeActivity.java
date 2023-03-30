package com.example.employeeattendanceapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.employeeattendanceapp.Fragment.HomeFragment;
import com.example.employeeattendanceapp.R;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */


public class HomeActivity extends AppCompatActivity {

    String TAG = "HomeActivity";
    RelativeLayout notificationsLayout, profileLayout, homeLayout;
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        notificationsLayout = findViewById(R.id.rl_notifications);
        homeLayout = findViewById(R.id.rl_home);
        profileLayout = findViewById(R.id.rl_profile);


        /**
         * Check for app update
         */
        try {
            Log.d("updateCheck", "inside app update try ");
            AppUpdater appUpdater = new AppUpdater(this)
                    .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                    .setDisplay(Display.DIALOG)
                    .setTitleOnUpdateAvailable("Update available")
                    .setContentOnUpdateAvailable("Please update your app to the latest available version!")
                    .setButtonUpdate("Update")
                    .setButtonUpdateClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("updateCheck", "inside app update try click ");
                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            Log.e(TAG, "onClick: Package name: " + appPackageName);
                            try {
                                Log.d("updateCheck", "inside app update click try ");
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (ActivityNotFoundException anfe) {
                                Log.d("updateCheck", "inside app update click catch ");
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    })
                    .setButtonDismiss(null)
                    .setButtonDoNotShowAgain(null)
                    .setCancelable(true);
            appUpdater.start();

        } catch (Exception e) {
            Log.e(TAG, "AppUpdater Exception: " + e.toString());
        }

        /**
         * show home fragment
         */
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer_, fragment);
        ft.commit();

        /**
         * home icon layout click listener
         */
        homeLayout.setOnClickListener(view12 -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        /**
         * notifications layout click listener
         */
        notificationsLayout.setOnClickListener(view13 -> {
            Toast.makeText(this, "Coming Soon !!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(HomeActivity.this, NotificationsActivity.class);
//            startActivity(intent);
        });

        /**
         * profile layout click listener
         */
        profileLayout.setOnClickListener(view14 ->
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class))
        );
    }

    /**
     * on Resume method
     */
    @Override
    protected void onResume() {
        super.onResume();
        try {
            SplashActivity.getRemoteConfigs(HomeActivity.this);

        } catch (Exception e) {
            Log.e("Firebase Remote Exp", e.toString());
        }
        try {
            checkNewAppVersionState();
        } catch (Exception e) {
            Log.e("EXP 2", e.toString());
        }
    }


    /**
     * onDestroy method
     */
    @Override
    protected void onDestroy() {
        try {
            unregisterInstallStateUpdListener();
        } catch (Exception e) {
            Log.e("EXP 3", e.toString());
        }
        super.onDestroy();
    }


    /**
     * checkForAppUpdate method
     */
    private void checkForAppUpdate() {
        //Log.e("checkForAppUpdate", "Here");
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(HomeActivity.this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        installStateUpdatedListener = installState -> {
            // Show module progress, log state, or install the update.
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                //Log.e("APPUpdate", "Downloaded");
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                popupSnackbarForCompleteUpdateAndUnregister();

            } else if (installState.installStatus() == InstallStatus.INSTALLED) {
                //Log.e("APPUpdate", "Installed");
                if (appUpdateManager != null) {
                    appUpdateManager.unregisterListener(installStateUpdatedListener);
                }

            } else if (installState.installStatus() == InstallStatus.PENDING) {
                //Log.e("APPUpdate", "Pending");

            } else if (installState.installStatus() == InstallStatus.DOWNLOADING) {
                // Log.e("APPUpdate", "Downloading");

            } else if (installState.installStatus() == InstallStatus.INSTALLING) {
                // Log.e("APPUpdate", "Installing");

            } else if (installState.installStatus() == InstallStatus.FAILED) {
                // Log.e("APPUpdate", "Failed");

            } else {
                // Log.e("appUpdateManager", "InstallStateUpdatedListener: state: " + installState.installStatus());
            }
        };

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            //  Log.e("V1", "" + appUpdateInfo.updateAvailability());
            //   Log.e("V2", "" + UpdateAvailability.UPDATE_AVAILABLE);
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    appUpdateManager.registerListener(installStateUpdatedListener);
                    // Start an update.
                    //  Log.e("UpdateType", "Immediate");
                    startAppUpdateImmediate(appUpdateInfo);

                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    appUpdateManager.registerListener(installStateUpdatedListener);
                    // Log.e("UpdateType", "Flexible");
                    // Start an update.
                    startAppUpdateFlexible(appUpdateInfo);

                } else {
                    // Log.e("UpdateType", "Nothing");
                }

            } else {
                // Log.e("UpdateType", "Not Available");
            }
        });

        appUpdateInfoTask.addOnFailureListener(e -> {
            //Log.e("appUpdateInfoTask Fail", e.toString())
        });
    }


    /**
     * onActivityResult method
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQ_CODE_VERSION_UPDATE) {
            if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                // Log.e("Update flow failed!", " Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
                unregisterInstallStateUpdListener();
            }
        }
    }


    /**
     * startAppUpdateImmediate method
     *
     * @param appUpdateInfo
     */
    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    /**
     * startAppUpdateFlexible method
     *
     * @param appUpdateInfo
     */
    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
        }
    }

    /**
     * Displays the snackbar notification and call to action.
     * Needed only for Flexible app update
     */
    private void popupSnackbarForCompleteUpdateAndUnregister() {
        //Log.e("APPUpdate", "Snackbar");

        try {
            appUpdateManager.completeUpdate();
            //unregisterInstallStateUpdListener();

        } catch (Exception e) {
            Log.e("complete exception", e.toString());
        }
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    private void checkNewAppVersionState() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            //FLEXIBLE:
                            // If the update is downloaded but not installed,
                            // notify the user to complete the update.
                            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                                // Log.e("AppUpdate", "Was Downloaded");
                                popupSnackbarForCompleteUpdateAndUnregister();
                            }

                            //IMMEDIATE:
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                //Log.e("AppUpdate", "Here");
                                startAppUpdateImmediate(appUpdateInfo);
                            } else {
                                // Log.e("AppUpdate", "Here 1");
                            }
                        });

    }

    /**
     * Needed only for FLEXIBLE update
     */
    private void unregisterInstallStateUpdListener() {
        // Log.e("Unregister", "1");
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}