package com.example.employeeattendanceapp.Manager;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Copyright : Muhammad Junaid Raza
 * @Developer : Muhammad Junaid Raza
 */

public class PrefsManager {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public String LOGIN_KEY = "KEY_LOGIN";
    public String FINGERPRINT_KEY = "KEY_FINGERPRINT";
    public String TOKEN_KEY = "TOKEN_KEY";
    public String USER_ID_KEY = "USER_ID_KEY";
    public String CHECK_IN_KEY = "CHECK_IN_KEY";
    public static final String PREF_USERNAME = "UserName";
    public static final String PREF_PASSWORD = "Password";

    public PrefsManager(Context context) {
        this.context = context;
    }

    public void setLogin(boolean login) {
        editor = context.getSharedPreferences(LOGIN_KEY, MODE_PRIVATE).edit();
        editor.putBoolean(LOGIN_KEY, login);
        editor.commit();
    }

    public boolean getLogin() {
        sharedPreferences = context.getSharedPreferences(LOGIN_KEY, MODE_PRIVATE);
        return sharedPreferences.getBoolean(LOGIN_KEY, false);
    }

    public void clearLogin() {
        editor = context.getSharedPreferences(LOGIN_KEY, MODE_PRIVATE).edit();
        editor.clear().apply();
    }

    public void setFingerprint(boolean fingerprint) {
        editor = context.getSharedPreferences(FINGERPRINT_KEY, MODE_PRIVATE).edit();
        editor.putBoolean(FINGERPRINT_KEY, fingerprint);
        editor.commit();
    }

    public boolean getFingerprint() {
        sharedPreferences = context.getSharedPreferences(FINGERPRINT_KEY, MODE_PRIVATE);
        return sharedPreferences.getBoolean(FINGERPRINT_KEY, false);
    }

    public void setAtdStatus(String atdStatus) {
        editor = context.getSharedPreferences(CHECK_IN_KEY, MODE_PRIVATE).edit();
        editor.putString(CHECK_IN_KEY, atdStatus);
        editor.commit();
    }

    public String getAtdStatus() {
        sharedPreferences = context.getSharedPreferences(CHECK_IN_KEY, MODE_PRIVATE);
        return sharedPreferences.getString(CHECK_IN_KEY, "");
    }

    public void clearAtdStatus() {
        editor = context.getSharedPreferences(CHECK_IN_KEY, MODE_PRIVATE).edit();
        editor.clear().apply();
    }

    public void setToken(String token) {
        editor = context.getSharedPreferences(TOKEN_KEY, MODE_PRIVATE).edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public String getToken() {
        sharedPreferences = context.getSharedPreferences(TOKEN_KEY, MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_KEY, "");
    }

    public void clearToken() {
        editor = context.getSharedPreferences(TOKEN_KEY, MODE_PRIVATE).edit();
        editor.clear().apply();
    }

    public void setUserName(String userName) {
        editor = context.getSharedPreferences(PREF_USERNAME, MODE_PRIVATE).edit();
        editor.putString(PREF_USERNAME, userName);
        editor.apply();
    }

    public String getUserName() {
        sharedPreferences = context.getSharedPreferences(PREF_USERNAME, MODE_PRIVATE);
        return sharedPreferences.getString(PREF_USERNAME, "");
    }

    public void setPassword(String password) {
        editor = context.getSharedPreferences(PREF_PASSWORD, MODE_PRIVATE).edit();
        editor.putString(PREF_PASSWORD, password);
        editor.apply();
    }

    public String getPassword() {
        sharedPreferences = context.getSharedPreferences(PREF_PASSWORD, MODE_PRIVATE);
        return sharedPreferences.getString(PREF_PASSWORD, "");
    }

    public void setUserId(String uId) {
        editor = context.getSharedPreferences(USER_ID_KEY, MODE_PRIVATE).edit();
        editor.putString(USER_ID_KEY, uId);
        editor.commit();
    }

    public String getUserId() {
        sharedPreferences = context.getSharedPreferences(USER_ID_KEY, MODE_PRIVATE);
        return sharedPreferences.getString(USER_ID_KEY, "");
    }


}
