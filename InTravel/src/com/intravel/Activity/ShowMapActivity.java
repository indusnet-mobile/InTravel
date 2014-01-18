package com.intravel.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.intravel.R;

public class ShowMapActivity extends android.support.v4.app.FragmentActivity {

	private boolean isSessionChanged = false;
	static LatLng HAMBURG = null;
	// static final LatLng KIEL = new LatLng(53.551, 9.993);
	private GoogleMap map;
	// private Prefs pref = null;
	private String lat, vName, longitute = "";
	private float latFloat, longFloat = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.map);

		} catch (Exception e) {
			e.printStackTrace();
		}

		GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());

		try {

			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// }
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		try {
			// if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			// map =
			// ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
			// }
			// else{
			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// }
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		//
		// pref = new Prefs(ApplicationConstant.getContext());
		Fragment fragment = getSupportFragmentManager().findFragmentById(
				R.id.map);

		SupportMapFragment mapFragment = (SupportMapFragment) fragment;
		map = mapFragment.getMap();
		Intent getLoginData = getIntent();
		if (getLoginData != null) {
			if (getLoginData.getExtras().getString("hotelListDet") != null) {
				lat = getLoginData.getExtras().getString("lat");
				latFloat = Float.valueOf(lat);
				longitute = getLoginData.getExtras().getString("long");
				longFloat = Float.valueOf(longitute);

			} else if (getLoginData.getExtras().getString("provider") != null) {
				lat = getLoginData.getExtras().getString("lat");
				String frstNmae[] = null;
				if (lat.contains(" ")) {
					lat = lat.replace(" ", "#");
					frstNmae = lat.split("#");
					lat = frstNmae[0];
					latFloat = Float.valueOf(lat);
				} else {
					latFloat = Float.valueOf(lat);
				}
				String lstNmae[] = null;
				longitute = getLoginData.getExtras().getString("long");
				if (longitute.contains(" ")) {
					longitute = longitute.replace(" ", "#");

					lstNmae = lat.split("#");
					longitute = lstNmae[0];
					longFloat = Float.valueOf(longitute);
				} else {
					longFloat = Float.valueOf(longitute);
				}

				// vName = getLoginData.getExtras().getString("providerName");

			}

		}
		HAMBURG = new LatLng(latFloat, longFloat);
		@SuppressWarnings("unused")
		Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
				.title(vName));
		// Marker kiel = map.addMarker(new MarkerOptions()
		// .position(KIEL)
		// .title("Kiel")
		// .snippet("Kiel is cool")
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.ic_launcher)));
		//
		// // Move the camera instantly to hamburg with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 10));
		// // Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		((RelativeLayout) findViewById(R.id.rel_image_back))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						finish();

					}
				});

		vName = null;
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (isSessionChanged) {
			isSessionChanged = false;

		} else {

		}

	}

	private void unbindDrawables(View view) {
		try {
			if (view.getBackground() != null) {
				view.getBackground().setCallback(null);
			}
			if (view instanceof ViewGroup) {
				for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
					unbindDrawables(((ViewGroup) view).getChildAt(i));

					// if (((ViewGroup) view).getChildAt(i) instanceof MapView)
					// {
					//
					// ((ViewGroup) view).destroyDrawingCache();
					// }

				}
				((ViewGroup) view).removeAllViews();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void freeMemory() {
		System.runFinalization();
		Runtime.getRuntime().gc();
		System.gc();
	}

	@Override
	protected void onDestroy() {
		nullify();
	}

	private void nullify() {

		super.onDestroy();

		if (lat != null)
			lat = null;
		if (vName != null)
			vName = null;
		if (longitute != null)
			longitute = null;

		latFloat = 0;
		longFloat = 0;
		//
		try {
			unbindDrawables((RelativeLayout) findViewById(R.id.parentLayout));
			freeMemory();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onPause() {

		super.onPause();

	}

	@Override
	protected void onStop() {

		super.onStop();

	}

}
