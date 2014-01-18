package com.intravel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

public class Util {

//	public static final String facebook_appID = "388104984645995";
	public static final String facebook_appID = "427426467367574";
	public static String[] permission = { "user_photos", "publish_stream",
			"email" };

	public static String hotelListforDetails[] = { "Andhra Pradesh",
			"Arunachal Pradesh", "Assam", "Bihar","Chandigarh", "Chhattisgarh", "Delhi",
			"Goa", "Gujarat", "Haryana", "Himachal Pradesh",
			"Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala",
			"Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram",
			"Nagaland", "Orissa",  "Pondicherry","Punjab", "Rajasthan", "Sikkim",
			"Tamil Nadu", "Tripura", "Uttar Pradesh", "Uttarakhand",
			"West Bengal" };

	public static String agentListforDetails[] = { "Andhra Pradesh","Andman & Nicobar",
			"Arunachal Pradesh", "Assam", "Bihar","Chandigarh", "Chhattisgarh", "Delhi",
			"Goa", "Gujarat", "Haryana", "Himachal Pradesh",
			"Jammu & Kashmir", "Jharkhand", "Karnataka", "Kerala",
			"Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram",
			"Nagaland", "Orissa","Puducherry", "Punjab", "Rajasthan", "Sikkim",
			"Tamil Nadu", "Tripura", "Uttar Pradesh", "Uttarakhand","Uttaranchal",
			"West Bengal" };
	

	public static String getStateNameForHotel(int position) {
		return hotelListforDetails[position];
	}

	public static String getStateNameForAgents(int position) {
		return agentListforDetails[position];
	}

	/* Custom Toast */
	public static void showToast(Context mContext, String msg) {
		Toast mToast = Toast.makeText(mContext, msg, 200);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

	/* Checks if the device has Internet connection. */
	public static boolean hasConnection(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}

		return false;
	}
	
	/* Share Preferance */
	
	public static SharedPreferences getPrefs(Context context) {
		return context.getSharedPreferences("InTravel",Activity.MODE_PRIVATE);
	}
	
	public static Boolean getIsInstallFirstTIme(Context context) {
		SharedPreferences mPrefs = Util.getPrefs(context);
		Boolean isFirtTIme = mPrefs.getBoolean("isFirtTIme", true);
		return isFirtTIme;
	}

	public static void saveFirstTime(Context context, Boolean isFirtTIme) {
		SharedPreferences.Editor prefsEditor = Util.getPrefs(context).edit();
		prefsEditor.putBoolean("isFirtTIme", isFirtTIme);
		prefsEditor.commit();
	}
	
}
