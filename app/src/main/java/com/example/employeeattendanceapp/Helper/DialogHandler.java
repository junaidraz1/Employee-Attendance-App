package com.example.employeeattendanceapp.Helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.employeeattendanceapp.Activity.ChangePasswordActivity;
import com.example.employeeattendanceapp.Activity.SettingsActivity;
import com.example.employeeattendanceapp.Manager.LogoutManager;
import com.example.employeeattendanceapp.R;
import com.example.employeeattendanceapp.Listeners.DialogClickListener;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class DialogHandler {

    String TAG = "DialogHandler";

    public static DialogHandler sharedInstance;

    AlertDialog alertDialog;

    /**
     * Method to create instance of this class if no instance is available otherwise use existing
     *
     * @return
     */
    public static DialogHandler getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new DialogHandler();
        }
        return sharedInstance;
    }

    public void credentialsDialog(Context context, boolean isConcenlable, String btnTxt, DialogClickListener dialogClickListener) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        LayoutInflater factory = LayoutInflater.from(context);
        final View deleteDialogView = factory.inflate(R.layout.dialog_credentials, null);

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

        alertDialog = dialog.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final ImageView img = (ImageView) deleteDialogView.findViewById(R.id.img_close);

        alertDialog.setView(deleteDialogView);
        alertDialog.setCancelable(isConcenlable);


        final Button btn_search = (Button) deleteDialogView.findViewById(R.id.btn_generic);
        btn_search.setText(btnTxt);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogClickListener.onSuccessResponse(null, null, alertDialog);
            }
        });

        try {
            alertDialog.show();

        } catch (Exception e) {
            Log.e("ActivityManager EXP", e.toString());
        }
    }

    public void dialogGeneric1Btn(Context context, boolean isCancelable, String title, String btnTxt, String bodyTxt, DialogClickListener dialogClickListener) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        LayoutInflater factory = LayoutInflater.from(context);
        final View deleteDialogView = factory.inflate(R.layout.dialog_generic_1btn, null);

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

        alertDialog = dialog.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final ImageView img = deleteDialogView.findViewById(R.id.img_close);

        alertDialog.setView(deleteDialogView);
        alertDialog.setCancelable(isCancelable);


        final Button button = deleteDialogView.findViewById(R.id.btn_generic);
        final TextView bodyTv = deleteDialogView.findViewById(R.id.tv_body);
        final TextView titleTv = deleteDialogView.findViewById(R.id.txt_heading);
        button.setText(btnTxt);
        bodyTv.setText(bodyTxt);
        titleTv.setText(title);

        img.setOnClickListener(view -> alertDialog.cancel());

        button.setOnClickListener(view -> dialogClickListener.onSuccessResponse(null, null, alertDialog));

        try {
            alertDialog.show();

        } catch (Exception e) {
            Log.e("DialogHandler", "dialogGeneric1Btn EXP: " + e);
        }
    }

    public void genericDiaglog2btn(Context context, boolean isCancelable, String heading, String body, String btnTxt, String btn2text, DialogClickListener dailogClickListner) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        LayoutInflater factory = LayoutInflater.from(context);
        final View deleteDialogView = factory.inflate(R.layout.dialog_generic_2btn, null);
        alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final ImageView img = (ImageView) deleteDialogView.findViewById(R.id.img_close);
        final TextView txt_messagebody = (TextView) deleteDialogView.findViewById(R.id.tv_body);
        final TextView txt_heading = (TextView) deleteDialogView.findViewById(R.id.txt_heading);

        txt_heading.setText(heading);
        txt_messagebody.setText(body);
        alertDialog.setView(deleteDialogView);
        alertDialog.setCancelable(isCancelable);

        final Button firstBtn = (Button) deleteDialogView.findViewById(R.id.btn_generic);
        final Button secondBtn = (Button) deleteDialogView.findViewById(R.id.btn_generic2);
        firstBtn.setText(btnTxt);
        secondBtn.setText(btn2text);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailogClickListner.onSuccessResponse("1", null, alertDialog);
            }
        });

        secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailogClickListner.onSuccessResponse("0", null, alertDialog);
            }
        });

        try {
            alertDialog.show();

        } catch (Exception e) {
            Log.e("ActivityManager EXP", e.toString());
            alertDialog.dismiss();
        }
    }


    @SuppressLint("InflateParams")
    public void popmenu(View v, Activity activity) {

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.menu_list_item_, null);

        RelativeLayout changePassword = view.findViewById(R.id.changePasswordLayout);
        RelativeLayout settings = view.findViewById(R.id.rl_settings);
        RelativeLayout logout = view.findViewById(R.id.logoutLayout);
        TextView version = view.findViewById(R.id.version);

        //settings.setVisibility(View.GONE);
        final PopupWindow mypopupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);

        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            String vers = pInfo.versionName;
            int verCode = pInfo.versionCode;
            version.setText("V " + vers);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ChangePasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                mypopupWindow.dismiss();
            }
        });

        settings.setOnClickListener(v1 -> {
            Intent intent = new Intent(activity, SettingsActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            mypopupWindow.dismiss();
        });

        logout.setOnClickListener(v2 -> {
            logoutDialog(activity,
                    true,
                    activity.getString(R.string.logout_txt), "Do you want to logout ?",
                    "Yes",
                    "No");
            mypopupWindow.dismiss();
        });

        int[] loc_int = new int[2];
        if (v == null) return;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            //Happens when the view doesn't exist on screen anymore.
            return;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top;

        mypopupWindow.showAtLocation(v, Gravity.TOP | Gravity.START, location.left, location.bottom);

    }

    public void logoutDialog(Activity activity, boolean isCancelable, String title, String bodyTxt, String btn1Txt, String btn2Txt) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity, R.style.CustomAlertDialog);

        LayoutInflater factory = LayoutInflater.from(activity);
        final View deleteDialogView = factory.inflate(R.layout.dialog_generic_2btn, null);

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

        alertDialog = dialog.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final ImageView img = deleteDialogView.findViewById(R.id.img_close);

        alertDialog.setView(deleteDialogView);
        alertDialog.setCancelable(isCancelable);


        final Button button = deleteDialogView.findViewById(R.id.btn_generic);
        final Button button2 = deleteDialogView.findViewById(R.id.btn_generic2);
        final TextView bodyTv = deleteDialogView.findViewById(R.id.tv_body);
        final TextView titleTv = deleteDialogView.findViewById(R.id.txt_heading);
        button.setText(btn1Txt);
        button2.setText(btn2Txt);
        bodyTv.setText(bodyTxt);
        titleTv.setText(title);

        img.setOnClickListener(view -> alertDialog.cancel());

        button.setOnClickListener(view -> {
            LogoutManager.logout = "true";
            LogoutManager.userLogout(activity, title);
            alertDialog.dismiss();
        });

        button2.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        try {
            alertDialog.show();

        } catch (Exception e) {
            Log.e("DialogHandler", "dialogGeneric1Btn EXP: " + e);
        }
    }

    public void genericDialogSuccess(Context context, boolean isCancelable, String heading, String body, String btnTxt, DialogClickListener dialogClickListener) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        LayoutInflater factory = LayoutInflater.from(context);
        final View deleteDialogView = factory.inflate(R.layout.dialog_success, null);

        alertDialog = dialog.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final ImageView img = (ImageView) deleteDialogView.findViewById(R.id.img_close);

        final TextView txt_messagebody = (TextView) deleteDialogView.findViewById(R.id.tv_body);

        final TextView txt_heading = (TextView) deleteDialogView.findViewById(R.id.txt_heading);

        txt_heading.setText(heading);
        txt_messagebody.setText(body);
        alertDialog.setView(deleteDialogView);
        alertDialog.setCancelable(isCancelable);


        final Button btn_search = (Button) deleteDialogView.findViewById(R.id.btn_generic);
        btn_search.setText(btnTxt);

        img.setOnClickListener(view -> alertDialog.dismiss());

        btn_search.setOnClickListener(view -> dialogClickListener.onSuccessResponse(null, null, alertDialog));

        try {
            alertDialog.show();

        } catch (Exception e) {
            Log.e("ActivityManager EXP", e.toString());
        }
    }


}
