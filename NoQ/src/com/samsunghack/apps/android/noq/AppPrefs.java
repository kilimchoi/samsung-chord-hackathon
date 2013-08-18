package com.samsunghack.apps.android.noq;

import com.samsunghack.apps.android.utils.PrefUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class AppPrefs {
	private static SharedPreferences mPrefs = null;

	private static final String APP_PREF_FILE_NAME = "NOQ_PREFS";

	private static final String OAUTH_TOKEN = "oauth_token";
	private static final String HAS_CREDENTIALS = "has_credentials";
	private static final String USER_NAME = "user_name";
	private static final String RESTAURANT_CONSOLE = "is_restaurant_console";

	public static SharedPreferences init(Context context) {
		mPrefs = PrefUtils.init(context, APP_PREF_FILE_NAME);
		return mPrefs;
	}

	public static SharedPreferences get(Context context) {
		if (mPrefs != null) {
			return mPrefs;
		} else {
			mPrefs = init(context);
		}
		return mPrefs;
	}

	public static String getAuthToken() {
		return PrefUtils.getString(OAUTH_TOKEN, "");
	}

	public static void setAuthToken(String authToken) {
		PrefUtils.putString(OAUTH_TOKEN, authToken);
	}

	public static void setHasCredentials(boolean isSet) {
		PrefUtils.putBoolean(HAS_CREDENTIALS, isSet);
	}

	public static boolean hasCredentials() {
		return PrefUtils.getBoolean(HAS_CREDENTIALS, false);
	}
	
	public static void setRestaurantConsole(boolean isSet) {
		PrefUtils.putBoolean(RESTAURANT_CONSOLE, isSet);
	}

	public static boolean getRestaurantConsole() {
		return PrefUtils.getBoolean(RESTAURANT_CONSOLE, false);
	}

	public static void clearCredentials() {
		PrefUtils.putBoolean(HAS_CREDENTIALS, false);
		PrefUtils.putString(OAUTH_TOKEN, "");
	}

	public static void setUserAccountName(Context context, String accountName) {
		if (mPrefs == null) {
			mPrefs = init(context);
		}
		if (mPrefs != null) {
			Editor editor = mPrefs.edit();
			editor.putString(USER_NAME, accountName);
			editor.commit();
			return;
		}
	}

	public static String getUserAccountName(Context context) {
		String accountName = null;
		if (mPrefs == null) {
			mPrefs = init(context);
		}
		if (mPrefs != null) {
			accountName = mPrefs.getString(USER_NAME, null);
		}
		return accountName;
	}

	public static void setAuthorization(Context context, String accountName,
			boolean isGranted) {
		if (mPrefs == null) {
			mPrefs = init(context);
		}
		if (mPrefs != null) {
			Editor editor = mPrefs.edit();
			editor.putBoolean(accountName, isGranted);
			editor.commit();
			return;
		}
	}

	public static boolean getAuthorization(Context context, String accountName) {
		boolean result = false;
		if (mPrefs == null) {
			mPrefs = init(context);
		}
		if (mPrefs != null) {
			result = mPrefs.getBoolean(accountName, false);
		}
		return result;
	}
}