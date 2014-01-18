package com.intravel.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intravel.R;
import com.intravel.Util;
import com.intravel.DBHelper.AgentDBHelper;
import com.intravel.DBHelper.HotelDBHelper;

public class HotelAndAgentListActivity extends Activity implements
		OnClickListener {

	private RelativeLayout rel_image_back;
	private TextView text_hotel;
	private int category;
	private ListView listView;
	private HotelDBHelper mHotelDBHelper;
	private AgentDBHelper mAgentDBHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hotel_and_state_listactivity);

		getBundleValue();
		initialize();
		createHotelDBHelperObj();
		createAgentDBHelperObj();
		loadUi();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rel_image_back:
			finish();
			overridePendingTransition(0, 0);
			break;

		default:
			break;
		}

	}

	private void getBundleValue() {
		Bundle mBundle = getIntent().getExtras();
		category = mBundle.getInt("CATEGORY");
	}

	private void initialize() {
		rel_image_back = (RelativeLayout) findViewById(R.id.rel_image_back);
		text_hotel = (TextView) findViewById(R.id.text_hotel);
		listView = (ListView) findViewById(R.id.lin_hotel_list);
		rel_image_back.setOnClickListener(this);
	}

	private void loadUi() {

		if (category == 0) {
			text_hotel.setText(getResources().getString(R.string.select_agent));
			hotelViewBaseAdapter mhotelViewBaseAdapter = new hotelViewBaseAdapter(
					HotelAndAgentListActivity.this, Util.agentListforDetails);
			listView.setAdapter(mhotelViewBaseAdapter);
		} else {
			text_hotel.setText(getResources().getString(R.string.select_hotel));
			hotelViewBaseAdapter mhotelViewBaseAdapter = new hotelViewBaseAdapter(
					HotelAndAgentListActivity.this, Util.hotelListforDetails);
			listView.setAdapter(mhotelViewBaseAdapter);
		}
	}

	private class hotelViewBaseAdapter extends BaseAdapter {

		private Context context;
		private String[] StateArray;
		private LayoutInflater minfInflater = null;

		public hotelViewBaseAdapter(Context mContexts, String[] hotelStateArray) {
			this.context = mContexts;
			this.StateArray = hotelStateArray;
			minfInflater = (LayoutInflater) mContexts
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public int getCount() {
			return StateArray.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			Holder mHolder;

			if (convertView == null) {
				mHolder = new Holder();
				convertView = minfInflater.inflate(R.layout.hotel_and_agent_statelistitem,
						parent, false);
				mHolder.stateName = (TextView) convertView
						.findViewById(R.id.txt_hotel_name);
				convertView.setTag(mHolder);
			} else {
				mHolder = (Holder) convertView.getTag();
			}

			Boolean iscount = false;
			int count = 0;
			final String stateName = StateArray[position];
			if (category == 0) {
				count = getAgentCountWRTStateName(stateName);
			} else {
				count = getHotelCountWRTStateName(stateName);
			}
			if (count >= 1) {
				iscount = true;
				mHolder.stateName.setText(stateName + " (" + count + ")");
			} else {
				iscount = false;
				mHolder.stateName.setText(stateName);
			}
			final Boolean isCountGreater = iscount;
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (category == 0) {
						if (isCountGreater) {
							Intent imIntent = new Intent(context,
									AgentListDetailsListActivity.class);
							Bundle bun = new Bundle();
							bun.putString("STATE", stateName);
							imIntent.putExtras(bun);
							startActivity(imIntent);
							overridePendingTransition(0, 0);
						}
					} else {
						if (isCountGreater) {
							Intent imIntent = new Intent(context,
									HotelListDetailsListActivity.class);
							Bundle bun = new Bundle();
							bun.putString("STATE", stateName);
							imIntent.putExtras(bun);
							startActivity(imIntent);
							overridePendingTransition(0, 0);
						}

					}
				}
			});

			return convertView;
		}
	}

	private class Holder {
		TextView stateName;
	}

//	private Boolean isHotelCountMoreThenOne(String stateName) {
//		Boolean isCountMoreThenOne = false;
//		if (mHotelDBHelper != null) {
//			isCountMoreThenOne = mHotelDBHelper.isCountMoreThenOne(stateName);
//		} else {
//			createHotelDBHelperObj();
//			isCountMoreThenOne = mHotelDBHelper.isCountMoreThenOne(stateName);
//		}
//		return isCountMoreThenOne;
//	}

	private void createHotelDBHelperObj() {
		mHotelDBHelper = HotelDBHelper
				.getInstance(HotelAndAgentListActivity.this);
	}

//	private Boolean isAgentCountMoreThenOne(String stateName) {
//		Boolean isCountMoreThenOne = false;
//		if (mAgentDBHelper != null) {
//			isCountMoreThenOne = mAgentDBHelper.isCountMoreThenOne(stateName);
//		} else {
//			createHotelDBHelperObj();
//			isCountMoreThenOne = mAgentDBHelper.isCountMoreThenOne(stateName);
//		}
//		return isCountMoreThenOne;
//	}

	private void createAgentDBHelperObj() {
		mAgentDBHelper = AgentDBHelper
				.getInstance(HotelAndAgentListActivity.this);
	}

	private int getAgentCountWRTStateName(String stateName) {
		int count = 0;
		if (mAgentDBHelper != null) {
			count = mAgentDBHelper.getCount(stateName);
		} else {
			createHotelDBHelperObj();
			count = mAgentDBHelper.getCount(stateName);
		}
		return count;
	}

	private int getHotelCountWRTStateName(String stateName) {
		int count = 0;
		if (mHotelDBHelper != null) {
			count = mHotelDBHelper.getCount(stateName);
		} else {
			createHotelDBHelperObj();
			count = mHotelDBHelper.getCount(stateName);
		}
		return count;
	}

}