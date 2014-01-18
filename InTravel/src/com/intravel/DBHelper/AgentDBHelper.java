package com.intravel.DBHelper;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.intravel.DB.DBHelper;
import com.intravel.model.HotelDetails;

public class AgentDBHelper {

	private String nameofEgency = "AgencyName";
	private String Address = "Address";
	private String State = "State";
	private String Phone = "Phone";
	private String Fax = "Fax";
	private String EmailId = "EmailId";
	private String City = "City";
	private String ContactPerson = "ContactPerson";
	private String Type = "Type";
	private String FabStatus = "FavouriteStatus";
	private String Region = "Region";
	private String latitude = "Latitude";
	private String longitude = "Longitude";

	private String TableNameAgentDetails = "AgentDetails";
	private DBHelper dbHelper;
	private static AgentDBHelper mInstance;
	Context context;

	public static synchronized AgentDBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new AgentDBHelper(context);
		}
		return mInstance;
	}

	public AgentDBHelper(Context context) {
		this.context = context;
		dbHelper = DBHelper.getInstance(context);
	}

	public ArrayList<HotelDetails> getAgentDetails(String stateName) {
		HotelDetails mHotelDetails = null;
		Cursor mCursor = null;
		ArrayList<HotelDetails> arraDetails = null;

		try {
			 String sql = "select * from AgentDetails where STATE='" +stateName+ "'";
//			String sql = "select * from AgentDetails where STATE='" + stateName+ "' limit 4";
			mCursor = dbHelper.getDatabase().rawQuery(sql, null);

			if (mCursor.getCount() > 0) {

				arraDetails = new ArrayList<HotelDetails>();

				while (mCursor.moveToNext()) {

					mHotelDetails = new HotelDetails();
					mHotelDetails.setHotelName(mCursor.getString(mCursor
							.getColumnIndex(nameofEgency)));
					mHotelDetails.setAddress(mCursor.getString(mCursor
							.getColumnIndex(Address)));
					mHotelDetails.setState(mCursor.getString(mCursor
							.getColumnIndex(State)));
					mHotelDetails.setPhone(mCursor.getString(mCursor
							.getColumnIndex(Phone)));
					mHotelDetails.setFax(mCursor.getString(mCursor
							.getColumnIndex(Fax)));
					mHotelDetails.setEmailId(mCursor.getString(mCursor
							.getColumnIndex(EmailId)));
					mHotelDetails.setRegion(mCursor.getString(mCursor
							.getColumnIndex(Region)));
					mHotelDetails.setType(mCursor.getString(mCursor
							.getColumnIndex(Type)));
					mHotelDetails.setContactPersone(mCursor.getString(mCursor
							.getColumnIndex(ContactPerson)));
					mHotelDetails.setCity(mCursor.getString(mCursor
							.getColumnIndex(City)));
					mHotelDetails.setFavStatus(mCursor.getInt(mCursor
							.getColumnIndex(FabStatus)));

					mHotelDetails.setLatitude(mCursor.getString(mCursor
							.getColumnIndex(latitude)));
					mHotelDetails.setLongitude(mCursor.getString(mCursor
							.getColumnIndex(longitude)));

					arraDetails.add(mHotelDetails);
					mHotelDetails = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (mCursor != null && !(mCursor.isClosed())) {
				mCursor.close();
			}
		}

		if (mCursor != null && !(mCursor.isClosed())) {
			mCursor.close();
		}

		return arraDetails;

	}

	public ArrayList<HotelDetails> getAgetntDetailsLiat_Fav() {
		HotelDetails mHotelDetails = null;
		Cursor mCursor = null;
		ArrayList<HotelDetails> arraDetails = null;
		try {
			String sql = "select * from AgentDetails where FavouriteStatus = 1";
			mCursor = dbHelper.getDatabase().rawQuery(sql, null);

			if (mCursor.getCount() > 0) {

				arraDetails = new ArrayList<HotelDetails>();

				while (mCursor.moveToNext()) {

					mHotelDetails = new HotelDetails();
					mHotelDetails.setHotelName(mCursor.getString(mCursor
							.getColumnIndex(nameofEgency)));
					mHotelDetails.setAddress(mCursor.getString(mCursor
							.getColumnIndex(Address)));
					mHotelDetails.setState(mCursor.getString(mCursor
							.getColumnIndex(State)));
					mHotelDetails.setPhone(mCursor.getString(mCursor
							.getColumnIndex(Phone)));
					mHotelDetails.setFax(mCursor.getString(mCursor
							.getColumnIndex(Fax)));
					mHotelDetails.setEmailId(mCursor.getString(mCursor
							.getColumnIndex(EmailId)));
					mHotelDetails.setRegion(mCursor.getString(mCursor
							.getColumnIndex(Region)));
					mHotelDetails.setType(mCursor.getString(mCursor
							.getColumnIndex(Type)));
					mHotelDetails.setContactPersone(mCursor.getString(mCursor
							.getColumnIndex(ContactPerson)));
					mHotelDetails.setCity(mCursor.getString(mCursor
							.getColumnIndex(City)));
					mHotelDetails.setFavStatus(mCursor.getInt(mCursor
							.getColumnIndex(FabStatus)));

					mHotelDetails.setLatitude(mCursor.getString(mCursor
							.getColumnIndex(latitude)));
					mHotelDetails.setLongitude(mCursor.getString(mCursor
							.getColumnIndex(longitude)));

					arraDetails.add(mHotelDetails);
					mHotelDetails = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (mCursor != null && !(mCursor.isClosed())) {
				mCursor.close();
			}
		}

		if (mCursor != null && !(mCursor.isClosed())) {
			mCursor.close();
		}

		return arraDetails;

	}

	public boolean UpdateData(String address, int i) {

		ContentValues contentValue = new ContentValues();
		contentValue.put(FabStatus, i);
		long Success = -1;
		try {
			Success = dbHelper.getDatabase().update(TableNameAgentDetails,
					contentValue, Address + "=?", new String[] { address });
		} catch (Exception e) {
			Success = -1;
		}

		if (Success > -1)
			return true;
		else
			return false;
	}

	public Boolean isCountMoreThenOne(String stateName) {

		Cursor mCursor = null;
		try {
			String sql = "select * from AgentDetails where STATE ='"
					+ stateName + "'";
			mCursor = dbHelper.getDatabase().rawQuery(sql, null);

			if (mCursor.getCount() > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (mCursor != null && !(mCursor.isClosed())) {
				mCursor.close();
			}
			return false;
		}

		if (mCursor != null && !(mCursor.isClosed())) {
			mCursor.close();
		}

		return false;

	}

	/* Get State Count with respect to StateNAme */
	public int getCount(String stateName) {
		int count = 0;
		Cursor mCursor = null;
		try {
			String sql = "select * from AgentDetails where STATE ='"+ stateName + "'";
			mCursor = dbHelper.getDatabase().rawQuery(sql, null);

			if (mCursor.getCount() > 0) {
				return mCursor.getCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (mCursor != null && !(mCursor.isClosed())) {
				mCursor.close();
			}
			return count;
		}

		if (mCursor != null && !(mCursor.isClosed())) {
			mCursor.close();
		}

		return count;

	}

}
