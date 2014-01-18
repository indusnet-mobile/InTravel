package com.intravel.Activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intravel.R;
import com.intravel.Util;
import com.intravel.Adapter.AgentBaseAdapter;
import com.intravel.Adapter.HotelBaseAdapter;
import com.intravel.DBHelper.AgentDBHelper;
import com.intravel.DBHelper.HotelDBHelper;
import com.intravel.model.HotelDetails;

public class FavouriteListActivity extends Activity implements OnClickListener {
	private RelativeLayout rel_image_back;
	private Button hotel_click, travelaget_click;
	private ListView list_hotel_, list_agent_;
	private TextView text_hotel;

	private ArrayList<HotelDetails> arrayhotelDetails;
	private ArrayList<HotelDetails> arrayAgentDetailslist;

	private HotelDBHelper mHotelDBHelper;
	private AgentDBHelper mAgentDBHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.favourite_activity);

		initialize();
		createHotelDBHelperObj();
		createAgentDBHelperObj();
		gethotelsData();
	}

	private void gethotelsData() {
		arrayAgentDetailslist = getHotelDetailsforAgentList();
		arrayhotelDetails = getHotelDetailsforHotelList();
		loadUI();
	}

	private void initialize() {

		text_hotel = (TextView) findViewById(R.id.text_hotel);
//		text_hotel.setText("Favourite List");
		rel_image_back = (RelativeLayout) findViewById(R.id.rel_image_back);
		list_hotel_ = (ListView) findViewById(R.id.hotellist);
		list_agent_ = (ListView) findViewById(R.id.agentlist);

		hotel_click = (Button) findViewById(R.id.tab_btn_hotel);
		travelaget_click = (Button) findViewById(R.id.tab_btn_agent);

		hotel_click.setOnClickListener(this);
		travelaget_click.setOnClickListener(this);

		rel_image_back.setOnClickListener(this);
	}

	private void loadUI() {
		Boolean isCallFromFav = true;
		if (arrayAgentDetailslist != null && arrayAgentDetailslist.size() > 0) {
			AgentBaseAdapter mAgentBaseAdapter = new AgentBaseAdapter(this,arrayAgentDetailslist,isCallFromFav);
			list_agent_.setAdapter(mAgentBaseAdapter);
		}
		
		if (arrayhotelDetails != null && arrayhotelDetails.size() > 0) {
			HotelBaseAdapter mHotelBaseAdapter = new HotelBaseAdapter(this,arrayhotelDetails, isCallFromFav);
			list_hotel_.setAdapter(mHotelBaseAdapter);
		} else {
			Util.showToast(this, getResources().getString(R.string.fav_hotel_empty_msg));
		}

		list_hotel_.setVisibility(View.VISIBLE);
		list_agent_.setVisibility(View.GONE);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rel_image_back:
			finish();
			overridePendingTransition(0, 0);
			break;

		case R.id.tab_btn_hotel:
			
			list_hotel_.setVisibility(View.VISIBLE);
			list_agent_.setVisibility(View.GONE);
			hotel_click.setBackgroundResource(R.drawable.hotel_select);
			travelaget_click.setBackgroundResource(R.drawable.agent_deselect);
			
			if (arrayhotelDetails != null && arrayhotelDetails.size() > 0) {
			} else {
				Util.showToast(this, getResources().getString(R.string.fav_hotel_empty_msg));
			}

			break;

		case R.id.tab_btn_agent:
			
			list_hotel_.setVisibility(View.GONE);
			list_agent_.setVisibility(View.VISIBLE);
			hotel_click.setBackgroundResource(R.drawable.hotel_deselect);
			travelaget_click.setBackgroundResource(R.drawable.agent_select);
			
			if (arrayAgentDetailslist != null
					&& arrayAgentDetailslist.size() > 0) {
			} else {
				Util.showToast(this, getResources().getString(R.string.fav_agent_empty_msg));
			}
			break;
		default:
			break;
		}

	}

	public ArrayList<HotelDetails> getHotelDetailsforAgentList() {

		ArrayList<HotelDetails> arraDetails = null;
		if (mAgentDBHelper != null) {
			arraDetails = mAgentDBHelper.getAgetntDetailsLiat_Fav();
		} else {
			createAgentDBHelperObj();
			arraDetails = mAgentDBHelper.getAgetntDetailsLiat_Fav();
		}
		return arraDetails;

	}

	private void createAgentDBHelperObj() {
		mAgentDBHelper = AgentDBHelper.getInstance(FavouriteListActivity.this);
	}

	public ArrayList<HotelDetails> getHotelDetailsforHotelList() {
		ArrayList<HotelDetails> arraDetails = null;
		if (mHotelDBHelper != null) {
			arraDetails = mHotelDBHelper.getHotelDetailsLiat_Fav();
		} else {
			createHotelDBHelperObj();
			arraDetails = mHotelDBHelper.getHotelDetailsLiat_Fav();
		}
		return arraDetails;
	}

	private void createHotelDBHelperObj() {
		mHotelDBHelper = HotelDBHelper.getInstance(FavouriteListActivity.this);
	}
}
