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
import com.intravel.Adapter.AgentBaseAdapter;
import com.intravel.Adapter.AgentBaseAdapter.AgentItemClickListner;
import com.intravel.DBHelper.AgentDBHelper;
import com.intravel.model.HotelDetails;

public class AgentListDetailsListActivity extends SharingActivity implements
		OnClickListener, AgentItemClickListner {
	private TextView text_hotel;
	private RelativeLayout back_bnt;

	private String StateName;;
	private ListView mLIstView;
	private AgentDBHelper mAgentDBHelper;
	private AgentBaseAdapter mAgentBaseAdapter;
	private ArrayList<HotelDetails> arrayAgentDetailslist;
	private EditText edt_sear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hotel_and_agent_list_detailsactivity);

		getBundleValue();
		initialize();
		getAgentData(0);

	}

	private void getAgentData(int position) {
		arrayAgentDetailslist = getAgentDetailsLIst(StateName);
		loadUI(position);
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

	private void loadUI(int position) {

		text_hotel.setText(getResources().getString(R.string.agent_details));
		if (arrayAgentDetailslist != null && arrayAgentDetailslist.size() > 0) {
			mAgentBaseAdapter = new AgentBaseAdapter(this,
					arrayAgentDetailslist, false);
			mAgentBaseAdapter.setmAgentItemClickListner(this);
			mLIstView.setAdapter(mAgentBaseAdapter);
			mLIstView.setSelection(position);
		}

		/* Filter the Agent List w.r.t AgencyName */
		edt_sear.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				String text = edt_sear.getText().toString()
						.toLowerCase(Locale.getDefault());
				mAgentBaseAdapter.filter(text);
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
			int position) {
		if (address != null) {
			if (UpdateData(address, selectornot)) {
				getAgentData(position);
			}
		}
		return null;
	}

	@Override
	public Void showDetailsDialog(HotelDetails mDetails,int position) {
		if (mDetails != null) {
			ShowInitDialog(this, mDetails,position);
		}
		return null;
	}

	@Override
	public Void showSharingDialog(HotelDetails mDetails) {
		if (mDetails != null) {
			String agency_anme = mDetails.getHotelName();
			String contact_psName = mDetails.getContactPersone();
			String Add_ = mDetails.getAddress();
			String Ph_ = mDetails.getPhone();
			String Emailid_ = mDetails.getEmailId();
			String sharing_msg = "Agency Name: " + agency_anme + "\n"
					+ "Address: " + Add_ + "\n" + "Contact PersonName: "
					+ contact_psName + "\n" + "Phone Number: " + Ph_ + "\n"
					+ "EmailId: " + Emailid_;

			dialogshow(sharing_msg);
		}
		return null;
	}

	public ArrayList<HotelDetails> getAgentDetailsLIst(String stateName) {

		ArrayList<HotelDetails> arraDetails = null;
		if (mAgentDBHelper != null) {
			arraDetails = mAgentDBHelper.getAgentDetails(stateName);
		} else {
			createAgentDBHelperObj();
			arraDetails = mAgentDBHelper.getAgentDetails(stateName);
		}
		return arraDetails;

	}

	public boolean UpdateData(String hotel_phone, int value) {
		Boolean updated = false;
		if (mAgentDBHelper != null) {
			updated = mAgentDBHelper.UpdateData(hotel_phone, value);
		} else {
			createAgentDBHelperObj();
			updated = mAgentDBHelper.UpdateData(hotel_phone, value);
		}
		return updated;
	}

	private void createAgentDBHelperObj() {
		mAgentDBHelper = AgentDBHelper
				.getInstance(AgentListDetailsListActivity.this);
	}

	public void ShowInitDialog(Context mContext, HotelDetails mDetails, final int position) {

		final Context context = mContext;

		final Dialog mDialog;
		mDialog = new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		mDialog.getWindow().getAttributes().windowAnimations = R.style.SlideDialogAnimation;
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		mDialog.setCancelable(true);
		mDialog.setContentView(R.layout.agent_dialog);

		final TextView cotactParsone = (TextView) mDialog
				.findViewById(R.id.ap_dlg_agent_text_name);
		final TextView agent_type = (TextView) mDialog
				.findViewById(R.id.ap_dlg_agent_text_type);
		final TextView agent_agency = (TextView) mDialog
				.findViewById(R.id.ap_dlg_agent_text_agencyname);
		final TextView agent_address = (TextView) mDialog
				.findViewById(R.id.ap_dlg_agent_text_address);
		final TextView agent_phone = (TextView) mDialog
				.findViewById(R.id.ap_dlg_agent_text_call);
		final TextView agent_email = (TextView) mDialog
				.findViewById(R.id.ap_dlg_agent_text_email);
		RelativeLayout rel_feb = (RelativeLayout) mDialog
				.findViewById(R.id.rel_feb);
		
		final ImageView img_lo = (ImageView) mDialog.findViewById(R.id.img_love);
		RelativeLayout rel_sharing = (RelativeLayout) mDialog.findViewById(R.id.rel_sharing);

		
		final HotelDetails details = mDetails;
		
		String name = mDetails.getContactPersone();
		String type = mDetails.getType();
		String address = mDetails.getAddress();
		String ph = mDetails.getPhone();
		String agencyName = mDetails.getHotelName();
		String email = mDetails.getEmailId();
		final String latitute = mDetails.getLatitude();
		final String longitute = mDetails.getLongitude();
		int isFav=mDetails.getFavStatus();

		changeFevimage(img_lo,isFav);
		
		agent_type.setText(type);
		cotactParsone.setText(name);
		agent_agency.setText(agencyName);

		if (address.equalsIgnoreCase("NA") || address.equalsIgnoreCase("")) {
			agent_address.setText("NA");
		} else {
			agent_address.setText(address);
			agent_address.setOnClickListener(new OnClickListener() {

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

		if (ph.equalsIgnoreCase("NA") || ph.equalsIgnoreCase("")) {
			agent_phone.setText("NA");
		} else {
			agent_phone.setText(ph);
			agent_phone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						mDialog.cancel();
						String number = "tel:"
								+ agent_phone.getText().toString();
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
			agent_email.setText("NA");
		} else {
			agent_email.setText(email);
			agent_email.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						mDialog.cancel();
						String to_mailId = agent_email.getText().toString();
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
				if (selection==1) {
					selection=0;
				}else {
					selection=1;
				}
				String address = details.getAddress();
				if (address != null) {
					if (UpdateData(address, selection)) {
						getAgentData(position);
						details.FavStatus=selection;
						changeFevimage(img_lo,selection);
					}
				}

			}
		});
		rel_sharing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (details != null) {
					String agency_anme = details.getHotelName();
					String contact_psName = details.getContactPersone();
					String Add_ = details.getAddress();
					String Ph_ = details.getPhone();
					String Emailid_ = details.getEmailId();
					String sharing_msg = "Agency Name: " + agency_anme + "\n"
							+ "Address: " + Add_ + "\n"
							+ "Contact PersonName: " + contact_psName + "\n"
							+ "Phone Number: " + Ph_ + "\n" + "EmailId: "
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
		lp.height = (height) / 2;
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
