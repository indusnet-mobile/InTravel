package com.intravel.twitter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharePrefForTwitter {
	private SharedPreferences preferences;
	
	public SharePrefForTwitter(Context context) {
		this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public void setAccessToken(String accessToken) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("ACCESS_TOKEN", accessToken);
		editor.commit();
	}
	
	public void setSecretToken(String mSecretToken) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("SECRET_TOKEN", mSecretToken);
		editor.commit();
	}
	
	public String getAccessToken() {
		String strSavedMem1 = preferences.getString("ACCESS_TOKEN", null);
		return strSavedMem1;
	}
	
	public String getSecretToken() {
		String strSavedMem1 = preferences.getString("SECRET_TOKEN", null);
		return strSavedMem1;
	}

}
