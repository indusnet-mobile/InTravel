package com.intravel;

import android.app.Application;

import com.intravel.DB.DBHelper;
import com.intravel.Facebook.Facebook;
import com.intravel.Facebook.SessionStore;

public class ApplicationConstant extends Application {

	public Facebook mFaceBook;

	@Override
	public void onCreate() {
		super.onCreate();
		restoreFacebookSession();
	}

	private void restoreFacebookSession() {
		mFaceBook = new Facebook(Util.facebook_appID);
		SessionStore.restore(mFaceBook, this);
	}

	@Override
	public void onTerminate() {
		DBHelper.getInstance(this).close();
		super.onTerminate();
	}

}
