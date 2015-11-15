package com.zhuanghongji.lockpattern.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhuanghongji on 2015/11/15.
 */
public class PreferenceUtil {
    public static String getGesturePassword(Context context) {
        SharedPreferences sp = context.getSharedPreferences("gesturePassword", Context.MODE_PRIVATE);
        String password = sp.getString("password", "");
        return password;
    }

    public static void setGesturePassword(Context context, String gesturePassword) {
        SharedPreferences sp = context.getSharedPreferences("gesturePassword", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("password", gesturePassword);
        editor.commit();
    }
}
