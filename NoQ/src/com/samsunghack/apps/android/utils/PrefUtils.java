package com.samsunghack.apps.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class PrefUtils {
	private static final String TAG="Prefs";

	private static SharedPreferences mPrefs = null;
	private static Context mAppContext = null;

	public static SharedPreferences init(Context appContext,String sharedPrefName) {
		if((appContext!=null) && (sharedPrefName!=null)) {
			mAppContext = appContext;
			mPrefs = mAppContext.getSharedPreferences(sharedPrefName,Context.MODE_PRIVATE);
			return mPrefs;
		}
		else {
			return null;
		}
	}

	public static SharedPreferences get(Context context, String sharedPrefName) {
		if(mPrefs == null) {
			mPrefs = init(mAppContext,sharedPrefName);
		}
		return mPrefs;
	}

	public static String getString(String key, String defValue) {
		String result = null;
		if (mPrefs != null) {
			result = mPrefs.getString(key, defValue);
		}
		return result;
	}

	public static void putString(String key, String value) {
		if (key != null && value != null && mPrefs != null) {
			Editor editor = mPrefs.edit();
			editor.putString(key, value);
			editor.commit();
			return;
		}
	}

	public static void putBoolean(String key, boolean value) {
		if (mPrefs != null) {
			Editor editor = mPrefs.edit();
			editor.putBoolean(key, value);
			editor.commit();
			return;
		}
	}

	public static boolean getBoolean(String key, boolean defValue) {
		boolean hasCredentials = false;
		if (mPrefs != null) {
			hasCredentials = mPrefs.getBoolean(key, defValue);
		}
		return hasCredentials;
	}

	public static void clearString(String key) {
		if (mPrefs != null) {
			Editor editor = mPrefs.edit();
			editor.putString(key, "");
			editor.commit();
			return;
		}
	}
}