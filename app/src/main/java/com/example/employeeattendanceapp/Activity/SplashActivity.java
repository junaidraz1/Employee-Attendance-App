package com.example.employeeattendanceapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.employeeattendanceapp.Manager.PrefsManager;
import com.example.employeeattendanceapp.R;
import com.example.employeeattendanceapp.Utils.Constants;
import com.example.employeeattendanceapp.Utils.RemoteConfigs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    String TAG = "SplashActivity";

    ImageView imageView;
    Handler handler;
    Animation animationBounce, animationMove_L_to_C, animationMove_C_to_R, animationShake;

    PrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.iv_logo);

        handler = new Handler();
        prefsManager = new PrefsManager(SplashActivity.this);

        //to animate splash screen
        splashAnimation();

        getRemoteConfigs(this);

    }

    //method that contains implementation of animated splash screen
    public void splashAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                animationBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_animation);
                animationShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                animationMove_L_to_C = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_animation_left_to_center);
                animationMove_C_to_R = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_animation_center_to_right);
                imageView.startAnimation(animationMove_L_to_C);
                imageView.setVisibility(View.VISIBLE);

                // Your fade animation
                AlphaAnimation fadeOutAnimation = new AlphaAnimation(1, 0);
                fadeOutAnimation.setDuration(300);

                //Create animation set and add both animations to run simultaneously.
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(animationMove_C_to_R);

                //Now set animation  listener on your rotate animation
                animationMove_L_to_C.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imageView.startAnimation(animationShake);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                animationShake.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imageView.startAnimation(animationMove_C_to_R);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                animationMove_C_to_R.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //to check if user was previously logged in or not
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        getloginPref();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        }, 1);

    }

    private void getloginPref() {
        if (prefsManager.getLogin()) {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    /**
     * Get Remote Configs
     *
     * @param activity
     */
    public static void getRemoteConfigs(Activity activity) {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();

        remoteConfig.setConfigSettingsAsync(configSettings);

        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        remoteConfig.fetch(0); // <- add the zero

        // [START fetch_config_with_callback]
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(activity, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
//                        if (task.isSuccessful()) {
//                            //Log.e("Firebase", "Config params updated: "+ task.getResult().toString());
//                            // get values
//                            RemoteConfigs.baseurlLive = remoteConfig.getString(urlString);
//                            Log.e("SplashActivity", "URL Fetched: " + RemoteConfigs.baseurlLive);
//
//                        } else {
                        RemoteConfigs.baseurlLive = Constants.baseUrl;
                        Log.e("SplashActivity", "Firebase Config params Failed");
                        //Toast.makeText(SplashActivity.this, "Fetch failed", Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
        // [END fetch_config_with_callback]

    }

}