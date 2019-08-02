package com.example.poc1.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.poc1.R;
import com.example.poc1.models.User;
import com.google.gson.Gson;

public class SharedPreferencesUtilities {

    private static SharedPreferencesUtilities sharedPreferencesUtilities;
    private static SharedPreferences sharedPreferences;

    private static String userField = "User";
    private static String isLoginField = "isLogin";

    private SharedPreferencesUtilities() {

    }

    public static SharedPreferencesUtilities getInstance(Context context) {
        if (sharedPreferencesUtilities == null) {
            sharedPreferencesUtilities = new SharedPreferencesUtilities();
            sharedPreferences = context.getApplicationContext().getSharedPreferences(context.getString(R.string.sharedPreferencesForUser), Context.MODE_PRIVATE);
        }
        return sharedPreferencesUtilities;
    }

    public void saveUserData(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userJSON = new Gson().toJson(user, User.class);
        editor.putString(userField, userJSON);
        editor.putBoolean(isLoginField, true);
        editor.apply();
    }

    public boolean isUserLogin() {
        return sharedPreferences.getBoolean(isLoginField, false);
    }

    public User loggedInUser() {
        String userJSON = sharedPreferences.getString(userField, null);
        User user = null;
        if (userJSON != null) {
            user = new Gson().fromJson(userJSON, User.class);
        }
        return user;
    }

    public void logoutUser() {
        sharedPreferences.edit().clear().apply();
    }
}
