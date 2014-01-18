package com.intravel.Activity;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intravel.R;
import com.intravel.Adapter.HotelBaseAdapter;
import com.intravel.Adapter.HotelBaseAdapter.HotelItemClickListner;
import com.intravel.DBHelper.HotelDBHelper;
import com.intravel.model.HotelDetails;

public class HotelListDetailsListActivity extends SharingActivity implements
		OnClickListener, HotelItemClickListner {

	private TextView text_hotel;
	private RelativeLayout back_bnt;
	private EditText edt_sear;

	private String StateName;;
	private ListView mLIstView;
	private ArrayList<HotelDetails> arrayHotelListDetail;
	private HotelDBHelper mHotelDBHelper;
	private HotelBaseAdapter mHotelBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hotel_and_agent_list_detailsactivity);

		getBundleValue();
		initialize();
		createHotelDBHelperObj();
		gethotelsDate(0);

	}

	private void gethotelsDate(int selectPosition) {
		arrayHotelListDetail = getHotelDetailsList(StateName);
		loadUI(selectPosition);
	}

	private void getBundleValue() {
		Bundle bun = getIntent().getExtras();
		StateName = bun.getString("STATE");
	}

	private void initialize() {
		text_hotel = (TextView) findViewById(R.id.text_hotel);
		edt_sear = (EditText) findViewById(R.id.edt_sear);
		back_bnt = (RelativeLayout) findViewById(R.id.rel_image_back);
		mLIstView = (ListView) findViewById(R.id.lin_hotel_details_list);
		back_bnt.setOnClickListener(this);
	}

	private void loadUI(int selectPosition) {
		text_hotel.setText(getResources().getString(R.string.hotel_details));
		if (arrayHotelListDetail != null && arrayHotelListDetail.size() > 0) {
			mHotelBaseAdapter = new HotelBaseAdapter(this,
					arrayHotelListDetail, false);
			mHotelBaseAdapter.setmAgentItemClickListner(this);
			mLIstView.setAdapter(mHotelBaseAdapter);
			mLIstView.setSelection(selectPosition);
		}

		/* Filter the Agent List w.r.t AgencyName */
		edt_sear.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				String text = edt_sear.getText().toString()
						.toLowerCase(Locale.getDefault());
				mHotelBaseAdapter.filter(text);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
		});

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

	@Override
	public Void UpdateIsFavouriteOne(String address, int selectornot,
			int selectPosition) {
		if (address != null) {
			if (UpdateData(address, selectornot)) {
				gethotelsDate(selectPosition);
			}
		}
		return null;
	}

	@Override
	public Void showDetailsDialog(HotelDetails mDetails, int position) {
		if (mDetails != null) {
			ShowInitDialog(this, mDetails, position);
		}
		return null;
	}

	@Override
	public Void showSharingDialog(HotelDetails mDetails) {
		if (mDetails != null) {
			String Ht_Name = mDetails.getHotelName();
			String Add_ = mDetails.getAddress();
			String Ph_ = mDetails.getPhone();
			String Emailid_ = mDetails.getEmailId();
			String sharing_msg = "Hotel Name: " + Ht_Name + "\n" + "Address: "
					+ Add_ + "\n" + "Phone Number: " + Ph_ + "\n" + "EmailId: "
					+ Emailid_;

			dialogshow(sharing_msg);
		}
		return null;
	}

	private ArrayList<HotelDetails> getHotelDetailsList(String stateName) {

		ArrayList<HotelDetails> arraDetails = null;
		if (mHotelDBHelper != null) {
			arraDetails = mHotelDBHelper.getHotelDetails(stateName);
		} else {
			createHotelDBHelperObj();
			arraDetails = mHotelDBHelper.getHotelDetails(stateName);
		}
		return arraDetails;
	}

	private void createHotelDBHelperObj() {
		mHotelDBHelper = HotelDBHelper
				.getInstance(HotelListDetailsListActivity.this);
	}

	private Boolean UpdateData(String ph, int value) {
		Boolean updated = false;

		if (mHotelDBHelper != null) {
			updated = mHotelDBHelper.UpdateData(ph, value);
		} else {
			createHotelDBHelperObj();
			updated = mHotelDBHelper.UpdateData(ph, value);
		}
		return updated;

	}

	public void ShowInitDialog(Context mContext, HotelDetails mDetails,
			final int position) {

		final Context context = mContext;

		final Dialog mDialog;
		mDialog = new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		mDialog.getWindow().getAttributes().windowAnimations = R.style.SlideDialogAnimation;
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		mDialog.setCancelable(true);
		mDialog.setContentView(R.layout.hotel_dialog);

		final TextView hotel_name = (TextView) mDialog
				.findViewById(R.id.ap_dlg_hotel_text_name);
		final TextView hotel_address = (TextView) mDialog
				.findViewById(R.id.ap_dlg_hotel_text_address);
		final TextView hotel_type = (TextView) mDialog
				.findViewById(R.id.ap_dlg_hotel_text_type);
		final TextView hotel_phone = (TextView) mDialog
				.findViewById(R.id.ap_dlg_hotel_text_phone);
		final TextView hotel_fax = (TextView) mDialog
				.findViewById(R.id.ap_dlg_hotel_text_fax);
		final TextView hotel_email = (TextView) mDialog
				.findViewById(R.id.ap_dlg_hotel_text_emailid);
		final TextView hotel_website = (TextView) mDialog
				.findViewById(R.id.ap_dlg_hotel_text_website);
		final TextView hotel_rooms = (TextView) mDialog
				.findViewById(R.id.ap_dlg_hotel_text_rooms);

		RelativeLayout rel_feb = (RelativeLayout) mDialog
				.findViewById(R.id.rel_feb);
		RelativeLayout rel_sharing = (RelativeLayout) mDialog
				.findViewById(R.id.rel_sharing);

		final ImageView img_lo = (ImageView) mDialog
				.findViewById(R.id.img_love);
		final HotelDetails details = mDetails;
		
		String name = mDetails.getHotelName();
		String address = mDetails.getAddress();
		String type = mDetails.getType();
		String ph = mDetails.getPhone();
		String fax = mDetails.getFax();
		String email = mDetails.getEmailId();
		final String webside = mDetails.getWebside();
		String room = mDetails.getRoom();
		int isFav=mDetails.getFavStatus();
		
		final String latitute = mDetails.getLatitude();
		final String longitute = mDetails.getLongitude();

		
		changeFevimage(img_lo,isFav);
		
		hotel_type.setText(type);
		hotel_rooms.setText(room);

		if (name.equalsIgnoreCase("NA") || name.equalsIgnoreCase("")) {
			hotel_name.setText("NA");
		} else {
			hotel_name.setText(name);
		}

		if (fax != null) {
			if (fax.equalsIgnoreCase("NA") || fax.equalsIgnoreCase("")) {
				hotel_fax.setText("NA");
			} else {
				hotel_fax.setText(fax);
			}
		} else {
			hotel_fax.setText("NA");
		}

		if (address.equalsIgnoreCase("NA") || address.equalsIgnoreCase("")) {
			hotel_address.setText("NA");
		} else {
			hotel_address.setText(address);
			hotel_address.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (latitute != null && longitute != null) {
						// mDialog.cancel();
						Intent goMapActivity = new Intent(context,
								ShowMapActivity.class);
						goMapActivity.putExtra("hotelListDet", "hotelListDet");
						goMapActivity.putExtra("lat", latitute);
						goMapActivity.putExtra("long", longitute);
						context.startActivity(goMapActivity);

					}

				}
			});
		}

		if (webside.equalsIgnoreCase("NA") || webside.equalsIgnoreCase("")) {
			hotel_website.setText("NA");
		} else {
			hotel_website.setText(webside);
			hotel_website.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						mDialog.cancel();
						Intent browserIntent = new Intent(Intent.ACTION_VIEW,
								Uri.parse("http://" + webside));
						context.startActivity(browserIntent);

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
		}

		if (ph.equalsIgnoreCase("NA") || ph.equalsIgnoreCase("")) {
			hotel_phone.setText("NA");
		} else {
			hotel_phone.setText(ph);
			hotel_phone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						mDialog.cancel();
						String number = "tel:"
								+ hotel_phone.getText().toString();
						Intent callIntent = new Intent(Intent.ACTION_CALL, Uri
								.parse(number));
						context.startActivity(callIntent);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
		}

		if (email.equalsIgnoreCase("NA") || email.equalsIgnoreCase("")) {
			hotel_email.setText("NA");
		} else {
			hotel_email.setText(email);
			hotel_email.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						mDialog.cancel();
						String to_mailId = hotel_email.getText().toString();
						Intent email = new Intent(Intent.ACTION_SEND);
						email.putExtra(Intent.EXTRA_EMAIL,
								new String[] { to_mailId });
						email.putExtra(Intent.EXTRA_SUBJECT, "");
						email.putExtra(Intent.EXTRA_TEXT, "");
						email.setType("message/rfc822");

						context.startActivity(Intent.createChooser(email,
								"Choose an Email client :"));
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
		}

		rel_feb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int selection = details.getFavStatus();
				if (selection == 1) {
					selection = 0;
				} else {
					selection = 1;
				}
				String address = details.getAddress();
				if (address != null) {
					if (UpdateData(address, selection)) {
						gethotelsDate(position);
						details.FavStatus = selection;
						changeFevimage(img_lo, selection);
					}
				}

			}
		});
		rel_sharing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (details != null) {
					String Ht_Name = details.getHotelName();
					String Add_ = details.getAddress();
					String Ph_ = details.getPhone();
					String Emailid_ = details.getEmailId();
					String sharing_msg = "Hotel Name: " + Ht_Name + "\n" + "Address: "
							+ Add_ + "\n" + "Phone Number: " + Ph_ + "\n" + "EmailId: "
							+ Emailid_;

					dialogshow(sharing_msg);
				}

			}
		});

		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		int height = metrics.heightPixels;
		int wwidth = metrics.widthPixels;
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(mDialog.getWindow().getAttributes());
		lp.width = (wwidth * 18) / 20;
		// lp.height = (height) / 2;
		mDialog.getWindow().setAttributes(lp);

		mDialog.show();

	}
	
	private void changeFevimage(ImageView img_lo, int isFav) {
		if (isFav == 0) {
			img_lo.setImageResource(R.drawable.heart_deselect);
		} else {
			img_lo.setImageResource(R.drawable.heart_select);
		}
		
	}

}