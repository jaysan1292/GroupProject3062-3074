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

    /** The authentication header that is sent with every request to the web service. */
    public static final String USER_AUTH = "user_auth";

    /**
     * Development version only: The web service's URL. Because in development, the URL
     * changes often, it's helpful to store this too. In a production environment, every
     * usage of this value would instead be replaced with the permanent URL of the actual
     * web service.
     */
    public static final String SERVICE_URL = "service_url";

    /** Contains the ID of the current checkpoint. */
    public static final String CURRENT_CHECKPOINT_ID = "checkpoint_id";

    private UserMetaManager() {}

    public static Map<String, ?> getUserMeta() {
        return getSharedPreferences().getAll();
    }

    public static String getMetaValue(String key) {
        Object value = getUserMeta().get(key);
        if (value == null) {
            return null;
        } else {
            return String.valueOf(value);
        }
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
        return getMetaValue(USER_AUTH) != null;
    }
}
