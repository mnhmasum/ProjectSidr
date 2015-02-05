package com.atomix.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtil {
	
	Context mContext;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor spEditor;
	
	private final String USER_ID = "user_id";
	private final String USER_PASSWORD = "user_password";
	private final String DEVICE_TOKEN = "device_token";
	private final String REGISTRATION_KEY = "registration_key";
	private final String PUSH_STATUS = "push_status";
	
	public PreferenceUtil(Context mContext) {
		super();
		this.mContext = mContext;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
	}
	
	public String getUserID() {
		return sharedPreferences.getString(USER_ID, "");  
	}
	
	public String getUserPassword() {
		return sharedPreferences.getString(USER_PASSWORD, "");  
	}
	
	public String getDeviceToken() {
		return sharedPreferences.getString(DEVICE_TOKEN, "");
	}
	
	public String getRegistrationKey() {
		return sharedPreferences.getString(REGISTRATION_KEY, "");
	}
	
	public int getPushStatus() {
		return sharedPreferences.getInt(PUSH_STATUS, 0);
	}
	
	public void setUserID(String userID) {
		spEditor = sharedPreferences.edit();
		spEditor.putString(USER_ID, userID);
		spEditor.commit();
	}
	
	public void setUserPasword(String userPassword) {
		spEditor = sharedPreferences.edit();
		spEditor.putString(USER_PASSWORD, userPassword);
		spEditor.commit();
	}
	
	public void setDeviceToken(String deviceToken) {
		spEditor = sharedPreferences.edit();
		spEditor.putString(DEVICE_TOKEN, deviceToken);
		spEditor.commit();
	}
	
	public void setRegistrationKey(String registrationKey) {
		spEditor = sharedPreferences.edit();
		spEditor.putString(REGISTRATION_KEY, registrationKey);
		spEditor.commit();
	}
	
	public void setPushStatus(int pushStatus) {
		spEditor = sharedPreferences.edit();
		spEditor.putInt(PUSH_STATUS, pushStatus);
		spEditor.commit();
	}
}