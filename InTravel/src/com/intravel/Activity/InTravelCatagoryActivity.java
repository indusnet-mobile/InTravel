package com.intravel.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.intravel.R;

public class InTravelCatagoryActivity extends Activity implements
		OnClickListener {

	private RelativeLayout rel_hotel;
	private RelativeLayout rel_travel_agent;
	private ImageView img_favourite_icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.intravel_catagoryactivity);

		initialize();

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rel_travel_agent:
			Intent intent = new Intent(InTravelCatagoryActivity.this,
					HotelAndAgentListActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("CATEGORY", 0);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
			
		case R.id.rel_hotel:
			Intent minIntent = new Intent(InTravelCatagoryActivity.this,
					HotelAndAgentListActivity.class);
			Bundle bun = new Bundle();
			bun.putInt("CATEGORY", 1);
			minIntent.putExtras(bun);
			startActivity(minIntent);
			break;
			
		case R.id.img_favourite_icon:
			startActivity(new Intent(InTravelCatagoryActivity.this,FavouriteListActivity.class));
			break;
			
		default:
			break;
		}
	}

	private void initialize() {
		rel_hotel = (RelativeLayout) findViewById(R.id.rel_hotel);
		rel_travel_agent = (RelativeLayout) findViewById(R.id.rel_travel_agent);

		img_favourite_icon = (ImageView) findViewById(R.id.img_favourite_icon);
		img_favourite_icon.setOnClickListener(this);
		rel_hotel.setOnClickListener(this);
		rel_travel_agent.setOnClickListener(this);
	}

}