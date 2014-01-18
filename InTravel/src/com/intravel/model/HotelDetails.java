package com.intravel.model;

public class HotelDetails {

	public String HotelName;
	public String Address;
	public String State;
	public String Phone;
	public String Fax;
	public String EmailId;
	public String Webside;
	public String Type;
	public String Room;
	public String contactPersone;
	public String city;
	public String region;
	public int FavStatus;
	public String latitude;
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String longitude;

	public String getContactPersone() {
		return contactPersone;
	}

	public void setContactPersone(String contactPersone) {
		this.contactPersone = contactPersone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getHotelName() {
		return HotelName;
	}

	public void setHotelName(String hotelName) {
		HotelName = hotelName;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getFax() {
		return Fax;
	}

	public void setFax(String fax) {
		Fax = fax;
	}

	public String getEmailId() {
		return EmailId;
	}

	public void setEmailId(String emailId) {
		EmailId = emailId;
	}

	public String getWebside() {
		return Webside;
	}

	public void setWebside(String webside) {
		Webside = webside;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getRoom() {
		return Room;
	}

	public void setRoom(String room) {
		Room = room;
	}

	public int getFavStatus() {
		return FavStatus;
	}

	public void setFavStatus(int favStatus) {
		FavStatus = favStatus;
	}

}
