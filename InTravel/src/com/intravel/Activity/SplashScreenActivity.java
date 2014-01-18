package com.intravel.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;

import com.google.analytics.tracking.android.EasyTracker;
import com.intravel.R;
import com.intravel.Util;

public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);

		Activitychange();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (Util.getIsInstallFirstTIme(this)) {
			EasyTracker.getInstance(this).activityStart(this);
		}

	}

	@Override
	public void onStop() {
		super.onStop();

		if (Util.getIsInstallFirstTIme(this)) {
			EasyTracker.getInstance(this).activityStop(this);
			Util.saveFirstTime(this, false);
		}

	}

	private void Activitychange() {

		new CountDownTimer(3000, 30) {
			@Override
			public void onTick(long l) {

			}

			@Override
			public void onFinish() {
				Intent mainscreenactivity = new Intent();
				mainscreenactivity.setClass(SplashScreenActivity.this,
						InTravelCatagoryActivity.class);
				mainscreenactivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(mainscreenactivity);
				finish();

			};
		}.start();

	}

}
