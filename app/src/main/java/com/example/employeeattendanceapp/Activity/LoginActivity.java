package com.example.employeeattendanceapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import androidx.biometric.BiometricPrompt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.employeeattendanceapp.BuildConfig;
import com.example.employeeattendanceapp.DB.DBManager;
import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.Manager.ConnectionManager;
import com.example.employeeattendanceapp.Manager.PrefsManager;
import com.example.employeeattendanceapp.Model.Response.LoginResponse;
import com.example.employeeattendanceapp.R;
import com.example.employeeattendanceapp.Retrofit.apiUtils;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.gson.Gson;
import com.jaredrummler.android.device.DeviceName;
import com.xwray.passwordview.PasswordView;

import java.util.concurrent.Executor;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class LoginActivity extends AppCompatActivity {

    String TAG = "LoginActivity";

    EditText usernameET;
    PasswordView passwordPV;
    Button loginBtn;
    LinearLayout fingerprintLayout;
    RelativeLayout loaderLayout, passwordLayout;
    ImageView imageLoader;

    String token = "", pass = "", Username = "", PassLogin = "", model = "", manufacturer = "",
            osVersion = "", appVersion = "";

    PrefsManager prefsManager;
    DBManager dbManager;
    LoginResponse loginResponseModel;

    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.et_userName);
        passwordPV = findViewById(R.id.et_password);
        loginBtn = findViewById(R.id.btn_login);
        imageLoader = findViewById(R.id.imageLoader);
        loaderLayout = findViewById(R.id.rl_loader);
        fingerprintLayout = findViewById(R.id.ll_biometric);
        passwordLayout = findViewById(R.id.rl_password);

        fingerprintLayout.setVisibility(View.GONE);

        prefsManager = new PrefsManager(LoginActivity.this);
        loginResponseModel = new LoginResponse();
        dbManager = new DBManager(this);
        gson = new Gson();

        dbManager.open();


        /**
         * if fingerprint status is enabled in prefs manager then make imageview visible
         */
        if (prefsManager.getFingerprint()) {
            fingerprintLayout.setVisibility(View.VISIBLE);
        } else {
            fingerprintLayout.setVisibility(View.GONE);
        }

        /**
         * Implementation of click listeners
         */
        clickListeners();

        /**
         * Method to get Device Info
         */
        getDeviceInfo();

        /**
         * Condition to handle when to generate new token or get old token
         */
        if (prefsManager.getToken().isEmpty()) {
            generateSessionToken();
        } else {
            prefsManager.getToken();
            Log.e(TAG, "onCreate: Old Token: " + prefsManager.getToken());
        }

    }

    /**
     * Method that contains implementation of click listeners
     */
    @SuppressLint("ClickableViewAccessibility")
    private void clickListeners() {
        //when fingerprint imageview is clicked call fingerprint authorization method
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            fingerprintLayout.setOnClickListener(view -> authorizeFingerprint());
        }

        //when user clicks on login button
        loginBtn.setOnClickListener(v -> {
            String username = usernameET.getText().toString();
            String password = passwordPV.getText().toString();

            if (username.isEmpty() && password.isEmpty()) {
                DialogHandler.getSharedInstance().dialogGeneric1Btn(LoginActivity.this, true, "Login",
                        "Ok", "Please enter username and password to login",
                        (status, message, alertDialog) -> alertDialog.dismiss());

            } else if (username.isEmpty()) {
                DialogHandler.getSharedInstance().dialogGeneric1Btn(LoginActivity.this, true, "Login",
                        "Ok", "Please enter username to login",
                        (status, message, alertDialog) -> alertDialog.dismiss());

            } else if (password.isEmpty()) {
                DialogHandler.getSharedInstance().dialogGeneric1Btn(LoginActivity.this, true, "Login",
                        "Ok", "Please enter password to login",
                        (status, message, alertDialog) -> alertDialog.dismiss());

            } else {
                sendLoginRequest(username, password);
            }
        });
    }

    /**
     * Method to log in user, contains API call
     */
    private void sendLoginRequest(String username, String password) {
        animateLoaderIcon();
        if (ConnectionManager.getSharedInstance().isNetworkAvailable(this)) {
            if (!username.isEmpty() && !password.isEmpty() && prefsManager.getToken() != null) {
                RequestBody requestBody = new FormBody.Builder()
                        .add("UserName", username)
                        .add("Password", password)
                        .add("IsDependent", "false")
                        .add("DeviceToken", prefsManager.getToken())
                        .add("Manufacturer", manufacturer)
                        .add("Model", model)
                        .add("AppVersion", appVersion)
                        .add("DeviceVersion", model)
                        .build();

                Call<LoginResponse> call = apiUtils.getAPIService(LoginActivity.this).loginEmployee(requestBody);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            loaderLayout.setVisibility(View.GONE);

                            loginResponseModel = response.body();
                            if (loginResponseModel.status != null && loginResponseModel.status == 1) {

                                prefsManager.setUserId(loginResponseModel.empId);
                                prefsManager.setLogin(true);
                                prefsManager.setUserName(username);
                                prefsManager.setPassword(password);
                                dbManager.insertLoginProfile(loginResponseModel);

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                            } else {
                                loaderLayout.setVisibility(View.GONE);
                                if (!loginResponseModel.errorMessage.isEmpty()) {
                                    DialogHandler.getSharedInstance().dialogGeneric1Btn(LoginActivity.this, true,
                                            "Login", "Ok", loginResponseModel.errorMessage,
                                            (status, message, alertDialog) -> alertDialog.dismiss());
                                } else {
                                    DialogHandler.getSharedInstance().dialogGeneric1Btn(LoginActivity.this, true,
                                            "Login", "Ok", "Please try again later",
                                            (status, message, alertDialog) -> alertDialog.dismiss());
                                }
                            }
                        } else {
                            loaderLayout.setVisibility(View.GONE);
                            DialogHandler.getSharedInstance().dialogGeneric1Btn(LoginActivity.this, true,
                                    "Login", "Ok", "Login Failed",
                                    (status, message, alertDialog) -> alertDialog.dismiss());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                        loaderLayout.setVisibility(View.GONE);
                        DialogHandler.getSharedInstance().dialogGeneric1Btn(LoginActivity.this, true,
                                "Login", "Ok", "Please try again later",
                                (status, message, alertDialog) -> alertDialog.dismiss());

                    }
                });
            }

        } else {
            loaderLayout.setVisibility(View.GONE);
            DialogHandler.getSharedInstance().dialogGeneric1Btn(this, true, "Login Failed"
                    , "Ok", "Please check your internet connection and try again", (status, message, alertDialog) -> alertDialog.dismiss());
        }

    }

    /**
     * Method to generate session token
     */
    private void generateSessionToken() {
        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                return;
            }
            token = task.getResult().getToken();
            prefsManager.setToken(token);
            Log.e(TAG, "New Token: " + token);
        });
    }


    /**
     * Method to get device info
     */
    private void getDeviceInfo() {
        try {
            model = android.os.Build.MODEL;
            manufacturer = android.os.Build.MANUFACTURER;
            osVersion = Build.VERSION.RELEASE + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
            appVersion = BuildConfig.VERSION_NAME;

            DeviceName.with(this).request((info, error) -> {
                if (info.model.equalsIgnoreCase(info.marketName)) {
                    model = info.marketName;
                } else {
                    model = info.marketName + " " + info.model;
                }
                Log.e(TAG, "Model: " + model);
            });

        } catch (Exception e) {
            model = android.os.Build.MODEL;
            Log.e(TAG, "Device Model EXP: " + e);
        }
    }


    /**
     * Method to authorize user fingerprint on fingerprint login
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void authorizeFingerprint() {

        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);

        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.d(TAG, "Biometric Authentication Error: " + errorCode + ":" + errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Username = prefsManager.getUserName();
                PassLogin = prefsManager.getPassword();

                Log.d(TAG, "onAuthenticationSucceeded: USERNAME: " + Username);
                Log.d(TAG, "onAuthenticationSucceeded: PASS: " + pass);

                sendLoginRequest(Username, PassLogin);

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d(TAG, "Biometric Authentication Failed ");
            }
        });

        // creating a variable for our promptInfo BIOMETRIC DIALOG
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Fingerprint Login")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();
        fingerprintLayout.setOnClickListener(v -> biometricPrompt.authenticate(promptInfo));
    }


    /**
     * Method to animate logo as a loader image
     */
    private void animateLoaderIcon() {
        loaderLayout.setVisibility(View.VISIBLE);
        Glide.with(LoginActivity.this)
                .load(R.drawable.ic_ihclogo)
                .into(imageLoader);
        imageLoader.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this,
                R.anim.blink_animation));
    }


    /**
     * Method to handle back press
     */
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}