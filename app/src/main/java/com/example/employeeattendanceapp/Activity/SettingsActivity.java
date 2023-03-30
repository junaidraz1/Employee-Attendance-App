package com.example.employeeattendanceapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.biometric.BiometricManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.Listeners.DialogClickListener;
import com.example.employeeattendanceapp.Manager.PrefsManager;
import com.example.employeeattendanceapp.R;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class SettingsActivity extends AppCompatActivity {

    String TAG = "SettingActivity";

    RelativeLayout menuLayout, notificationsLayout, profileLayout, homeLayout;
    ;
    ImageView ivBack;
    SwitchCompat fingerprintSwitch;
    TextView titleTv;

    PrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        notificationsLayout = findViewById(R.id.rl_notifications);
        homeLayout = findViewById(R.id.rl_home);
        profileLayout = findViewById(R.id.rl_profile);
        menuLayout = findViewById(R.id.rl_menu);
        ivBack = findViewById(R.id.iv_back);
        fingerprintSwitch = findViewById(R.id.sb_fingerprintLogin);
        titleTv = findViewById(R.id.tv_title);

        /**
         * Setting activity's title in top bar
         */
        titleTv.setText("Settings");

        prefsManager = new PrefsManager(SettingsActivity.this);

        /**
         * Method that contains implementation of click listeners
         */
        clickListeners();

        /**
         * To check if fingerprint was enabled earlier or not
         */
        fingerprintState();

    }

    private void clickListeners() {

        /**
         * Back icon click listener
         */
        ivBack.setOnClickListener(v -> onBackPressed());

        /**
         * Menu layout click listener
         */
        menuLayout.setOnClickListener(v -> DialogHandler.getSharedInstance().popmenu(v, SettingsActivity.this));

        /**
         * home layout click listener
         */
        homeLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


        /**
         * notifications layout click listener
         */
        notificationsLayout.setOnClickListener(view -> {
            Toast.makeText(this, "Coming Soon !!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getApplicationContext(), NotificationsActivity.class);
//            startActivity(intent);
        });

        /**
         * profile layout click listener
         */
        profileLayout.setOnClickListener(view -> {
            Toast.makeText(this, "Coming Soon !!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getApplicationContext(), NotificationsActivity.class);
//            startActivity(intent);
        });

        /**
         * Finger switch button listener
         */
        fingerprintSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                //check its state
                //if it is checked
                if (checked) {

                    //check for biometric support
                    // if it is true
                    if (checkbiometricSupport()) {

                        //set button to check and set fingerprint status to true in pref
                        fingerprintSwitch.setChecked(true);
                        prefsManager.setFingerprint(true);
                    } else {

                        //else set both to false
                        fingerprintSwitch.setChecked(false);
                        prefsManager.setFingerprint(false);
                    }
                } else {

                    //if button is not check then let it stay false and its state in pref manager false too
                    fingerprintSwitch.setChecked(false);
                    prefsManager.setFingerprint(false);
                }
            }
        });
    }


    public void fingerprintState() {

        //if true then button will be checked true and false otherwise
        if (prefsManager.getFingerprint() && checkbiometricSupport()) {
            fingerprintSwitch.setChecked(true);

        } else {
            fingerprintSwitch.setChecked(false);
            prefsManager.setFingerprint(false);
        }
    }

    //method that checks if device supports biometric or not
    public boolean checkbiometricSupport() {
        boolean isChecked = false;
        BiometricManager biometricManager = BiometricManager.from(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:
                if (!prefsManager.getFingerprint()) {
                    DialogHandler.getSharedInstance().dialogGeneric1Btn(SettingsActivity.this, true, "Settings",
                            "Ok", "Biometric Login Enabled Successfully", new DialogClickListener() {
                                @Override
                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                    alertDialog.dismiss();
                                }
                            });
                }
                isChecked = true;
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                DialogHandler.getSharedInstance().dialogGeneric1Btn(SettingsActivity.this, true, "Settings",
                        "Ok", "This device doesn't support biometric", new DialogClickListener() {
                            @Override
                            public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                alertDialog.dismiss();
                            }
                        });
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                DialogHandler.getSharedInstance().dialogGeneric1Btn(SettingsActivity.this, true, "Settings",
                        "Ok", "Biometric sensor unavailable", new DialogClickListener() {
                            @Override
                            public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                alertDialog.dismiss();
                            }
                        });
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                DialogHandler.getSharedInstance().dialogGeneric1Btn(SettingsActivity.this, true, "Settings",
                        "Ok", "No saved fingerprint found, click ok to configure", new DialogClickListener() {
                            @Override
                            public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                alertDialog.dismiss();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    startActivity(new Intent(Settings.ACTION_BIOMETRIC_ENROLL));
                                }
                            }
                        });
                break;
        }
//        }
        return isChecked;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}