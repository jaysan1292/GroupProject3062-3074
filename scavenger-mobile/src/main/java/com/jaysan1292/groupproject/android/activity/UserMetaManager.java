package com.jaysan1292.groupproject.android.activity;

import android.content.SharedPreferences;
import com.jaysan1292.groupproject.android.MobileAppCommon;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 07/12/12
 * Time: 8:23 PM
 */
public class UserMetaManager {
    private static final String PREF_NAME = "user_meta";
    public static final String USER_AUTH = "user_auth";
    public static final String SERVICE_URI = "service_uri";

    private UserMetaManager() {}

    public static Map<String, ?> getUserMeta() {
        return getSharedPreferences().getAll();
    }

    public static String getMetaValue(String key) {
        return String.valueOf(getUserMeta().get(key));
    }

    public static void removeMetaValue(String key) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        {
            editor.remove(key);
        }
        editor.commit();
    }

    public static void setMetaValue(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        {
            editor.putBoolean(key, value);
        }
        editor.commit();
    }

    public static void setMetaValue(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        {
            editor.putString(key, value);
        }
        editor.commit();
    }

    public static void setMetaValue(String key, float value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        {
            editor.putFloat(key, value);
        }
        editor.commit();
    }

    public static void setMetaValue(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        {
            editor.putInt(key, value);
        }
        editor.commit();
    }

    public static void setMetaValue(String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        {
            editor.putLong(key, value);
        }
        editor.commit();
    }

    public static void setMetaValue(String key, Set<String> value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        {
            editor.putStringSet(key, value);
        }
        editor.commit();
    }

    private static SharedPreferences getSharedPreferences() {
        return MobileAppCommon.getContext().getSharedPreferences(PREF_NAME, 0);
    }

    public static boolean checkLoginStatus() {
        return !getMetaValue(USER_AUTH).equals("null");
    }
}
