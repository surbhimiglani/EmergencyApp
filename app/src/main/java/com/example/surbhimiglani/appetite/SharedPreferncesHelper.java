package com.example.surbhimiglani.appetite;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Surbhi Miglani on 11-10-2017.
 */

public class SharedPreferncesHelper {

    private static String PREF_NAME2 = "prefs2";

    SharedPreferences.Editor editor2;

    Boolean isRun = false;

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME2, Context.MODE_PRIVATE);
    }

    public static String getPlace(Context context) {
        String  save = getPrefs(context).getString("save4", "");
        return save;
    }

    public static void putPlace(Context context , String save) {
        SharedPreferences.Editor editor4 = getPrefs(context).edit();
        editor4.putString("save4", save);
        editor4.apply();
    }

    public static int getInt(Context context) {
        int  save = getPrefs(context).getInt("save5", 0);
        return save;
    }

    public static void putInt(Context context , int save) {
        SharedPreferences.Editor editor4 = getPrefs(context).edit();
        editor4.putInt("save5", save);
        editor4.apply();
    }

    public static String getSelect(Context context) {
        String  save = getPrefs(context).getString("save4", "");
        return save;
    }

    public static void putSelect(Context context , String save) {
        SharedPreferences.Editor editor4 = getPrefs(context).edit();
        editor4.putString("save4", save);
        editor4.apply();
    }


}
