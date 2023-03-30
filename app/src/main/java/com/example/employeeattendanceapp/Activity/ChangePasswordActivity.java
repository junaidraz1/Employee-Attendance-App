package com.example.employeeattendanceapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.employeeattendanceapp.Helper.DialogHandler;
import com.example.employeeattendanceapp.Listeners.DialogClickListener;
import com.example.employeeattendanceapp.R;
import com.xwray.passwordview.PasswordView;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class ChangePasswordActivity extends AppCompatActivity {

    String TAG = "ChangePasswordActivity", oldPassword = "", newPassword = "", confirmNewPassword = "";
    ImageView ivBack;
    RelativeLayout menuLayout, notificationsLayout, profileLayout;
    TextView tvHeader;
    PasswordView pvOldPassword, pvNewPassword, pvConfirmNewPassword;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ivBack = findViewById(R.id.iv_back);
        notificationsLayout = findViewById(R.id.rl_notifications);
        profileLayout = findViewById(R.id.rl_profile);
        menuLayout = findViewById(R.id.rl_menu);
        tvHeader = findViewById(R.id.tv_title);
        pvOldPassword = findViewById(R.id.pv_oldPassword);
        pvNewPassword = findViewById(R.id.pv_newPassword);
        pvConfirmNewPassword = findViewById(R.id.pv_confirmPassword);
        submitBtn = findViewById(R.id.btn_submit);

        /**
         * Set activity name to top bar
         */
        tvHeader.setText("Change Password");

        /**
         * Method that contains implementation of click listeners
         */
        clickListeners();

    }

    private void clickListeners() {

        /**
         * Back icon click listener
         */
        ivBack.setOnClickListener(v -> onBackPressed());

        /**
         * Menu layout click listener
         */
        menuLayout.setOnClickListener(v -> DialogHandler.getSharedInstance().popmenu(v, ChangePasswordActivity.this));

        /**
         * Submit button click listener
         */
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword = pvOldPassword.getText().toString();
                newPassword = pvNewPassword.getText().toString();
                confirmNewPassword = pvConfirmNewPassword.getText().toString();

                if (oldPassword.isEmpty()) {
                    DialogHandler.getSharedInstance().dialogGeneric1Btn(ChangePasswordActivity.this, true,
                            "Change Password", "OK", "Please enter your old password", new DialogClickListener() {
                                @Override
                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                    alertDialog.dismiss();
                                }
                            });
                } else if (newPassword.isEmpty()) {
                    DialogHandler.getSharedInstance().dialogGeneric1Btn(ChangePasswordActivity.this, true,
                            "Change Password", "OK", "Please enter your new password", new DialogClickListener() {
                                @Override
                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                    alertDialog.dismiss();
                                }
                            });

                } else if (confirmNewPassword.isEmpty()) {
                    DialogHandler.getSharedInstance().dialogGeneric1Btn(ChangePasswordActivity.this, true,
                            "Change Password", "OK", "Please confirm your new password", new DialogClickListener() {
                                @Override
                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                    alertDialog.dismiss();
                                }
                            });

                } else if (!newPassword.equals(confirmNewPassword)) {
                    DialogHandler.getSharedInstance().dialogGeneric1Btn(ChangePasswordActivity.this, true,
                            "Change Password", "OK", "Password doesn't match with new password", new DialogClickListener() {
                                @Override
                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                    alertDialog.dismiss();
                                }
                            });

                } else {
                    DialogHandler.getSharedInstance().genericDialogSuccess(ChangePasswordActivity.this, true,
                            "Change Password", "OK", "Password changed successfully", new DialogClickListener() {
                                @Override
                                public void onSuccessResponse(String status, String message, AlertDialog alertDialog) {
                                    alertDialog.dismiss();
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}