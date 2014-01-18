package com.intravel.Adapter;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intravel.R;
import com.intravel.model.HotelDetails;

public class HotelBaseAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HotelDetails> arrasearchDetail;
	private ArrayList<HotelDetails> arrayHotelListDetail=null;
	private LayoutInflater minfInflater = null;
	private Boolean isCallFromFav = false;

	public HotelBaseAdapter(Context mContexts,ArrayList<HotelDetails> arrayHotelListDetail, Boolean isCallFromFav) {
		this.context = mContexts;
		this.arrayHotelListDetail = arrayHotelListDetail;
		minfInflater = (LayoutInflater) mContexts.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.isCallFromFav = isCallFromFav;
		
		this.arrasearchDetail = new ArrayList<HotelDetails>();
		this.arrasearchDetail.addAll(arrayHotelListDetail);
	}

	@Override
	public int getCount() {
		return arrayHotelListDetail.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		Holder_hotel mHolder;
		final HotelDetails mDetails = arrayHotelListDetail.get(position);
		if (convertView == null) {
			mHolder = new Holder_hotel();
			convertView = minfInflater.inflate(R.layout.list_itemdetails,
					parent, false);
			mHolder.hoteLname = (TextView) convertView
					.findViewById(R.id.text_hotel_name);
			mHolder.SateName = (TextView) convertView
					.findViewById(R.id.text_hotel_location);
			mHolder.phnoumber = (TextView) convertView
					.findViewById(R.id.text_phone_number);
			mHolder.emial = (TextView) convertView
					.findViewById(R.id.txt_mail_id);
			mHolder.imageFab = (ImageView) convertView
					.findViewById(R.id.img_love);
			mHolder.rel_sharing = (RelativeLayout) convertView
					.findViewById(R.id.rel_sharing);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder_hotel) convertView.getTag();
		}
		
		
		if (isCallFromFav) {
			mHolder.rel_sharing.setVisibility(View.INVISIBLE);
		}else {
			mHolder.rel_sharing.setVisibility(View.VISIBLE);
		}
		
		if (mDetails != null) {
			final String Address = mDetails.getAddress();
			mHolder.hoteLname.setText(mDetails.getHotelName());
			mHolder.SateName.setText(mDetails.getState());
			mHolder.phnoumber.setText(mDetails.getPhone());
			mHolder.emial.setText(mDetails.getEmailId());
			if (mDetails.getFavStatus() == 0) {
				mHolder.imageFab.setImageResource(R.drawable.heart_deselect);

				mHolder.imageFab.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (mAgentItemClickListner != null && Address != null) {
							mAgentItemClickListner.UpdateIsFavouriteOne(
									Address, 1, position);
						}
					}
				});
			} else {
				mHolder.imageFab.setImageResource(R.drawable.heart_select);

				mHolder.imageFab.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (mAgentItemClickListner != null && Address != null) {
							mAgentItemClickListner.UpdateIsFavouriteOne(
									Address, 0, position);
						}
					}
				});

			}

		}

		mHolder.rel_sharing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mAgentItemClickListner != null) {
					mAgentItemClickListner.showSharingDialog(mDetails);
				}

			}
		});

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAgentItemClickListner != null) {
					mAgentItemClickListner.showDetailsDialog(mDetails,position);
				}
			}
		});

		return convertView;
	}

	private class Holder_hotel {
		TextView hoteLname;
		TextView SateName;
		TextView phnoumber;
		TextView emial;
		ImageView imageFab;
		RelativeLayout rel_sharing;
	}

	public interface HotelItemClickListner {
		public Void UpdateIsFavouriteOne(String phoneNumber, int selectornot,
				int selectionPosition);

		public Void showDetailsDialog(HotelDetails mDetails, int position);

		public Void showSharingDialog(HotelDetails mDetails);
	}

	public HotelItemClickListner mAgentItemClickListner;

	public HotelItemClickListner getmAgentItemClickListner() {
		return mAgentItemClickListner;
	}

	public void setmAgentItemClickListner(
			HotelItemClickListner mAgentItemClickListner) {
		this.mAgentItemClickListner = mAgentItemClickListner;
	}
	
	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		arrayHotelListDetail.clear();
		if (charText.length() == 0) {
			arrayHotelListDetail.addAll(arrasearchDetail);
		} else {
			for (HotelDetails mHotelDetails : arrasearchDetail) {
				if (mHotelDetails.getHotelName().toLowerCase(Locale.getDefault()).contains(charText)) {
					arrayHotelListDetail.add(mHotelDetails);
				}
			}
		}
		notifyDataSetChanged();
	}

}
