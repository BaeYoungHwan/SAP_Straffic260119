package com.mbc.sap.dto;

public class UserDto {
	private String user_id;
	private String user_pw;
	private String station_id;
	private String station_name;
	private String line_name;
	private String user_name;
	private String user_email;
	private int auth;
	
	public UserDto() {
		
	}

	public UserDto(String user_id, String user_pw, String station_id, String station_name, String line_name,
			String user_name, String user_email, int auth) {
		super();
		this.user_id = user_id;
		this.user_pw = user_pw;
		this.station_id = station_id;
		this.station_name = station_name;
		this.line_name = line_name;
		this.user_name = user_name;
		this.user_email = user_email;
		this.auth = auth;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_pw() {
		return user_pw;
	}

	public void setUser_pw(String user_pw) {
		this.user_pw = user_pw;
	}

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	public String getStation_name() {
		return station_name;
	}

	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

	public String getLine_name() {
		return line_name;
	}

	public void setLine_name(String line_name) {
		this.line_name = line_name;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public int getAuth() {
		return auth;
	}

	public void setAuth(int auth) {
		this.auth = auth;
	}

	@Override
	public String toString() {
		return "UserDto [user_id=" + user_id + ", user_pw=" + user_pw + ", station_id=" + station_id + ", station_name="
				+ station_name + ", line_name=" + line_name + ", user_name=" + user_name + ", user_email=" + user_email
				+ ", auth=" + auth + "]";
	}
	
	
}
