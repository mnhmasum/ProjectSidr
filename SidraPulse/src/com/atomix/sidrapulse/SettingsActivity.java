package com.atomix.sidrapulse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sharedpreference.PreferenceUtil;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;

public class SettingsActivity extends Activity implements OnClickListener {

	private TextView edTxtCurrentlySignedAs;
	private Button btnLogout;
	private Button btnBack;
	private ProgressDialog progressDialog;
	private int pushStatus;
	private int logoutStatus;
	private PreferenceUtil preferenceUtil;
	private Button btnPushNotification;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		initViews();
		setListener();
		loadData();
	}

	private void loadData() {
		preferenceUtil = new PreferenceUtil(getApplicationContext());
		
		if(preferenceUtil.getPushStatus() == 1) {
			btnPushNotification.setBackgroundResource(R.drawable.on_btn);
		} else {
			btnPushNotification.setBackgroundResource(R.drawable.off_btn);
		}
		
		edTxtCurrentlySignedAs.setText(preferenceUtil.getUserID());
	}

	private void initViews() {
		edTxtCurrentlySignedAs = (TextView) findViewById(R.id.ed_txt_currently_signed_as);
		btnPushNotification = (Button) findViewById(R.id.btn_on_off);
		
		btnLogout = (Button) findViewById(R.id.btn_logout);
		btnBack = (Button) findViewById(R.id.btn_back);
	}

	private void setListener() {
		btnLogout.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnPushNotification.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
			
		case R.id.btn_logout:
			if (InternetConnectivity.isConnectedToInternet(SettingsActivity.this)) {
				new LogoutApiTask().execute();
			} else {
				SidraPulseApp.getInstance().openDialogForInternetChecking(SettingsActivity.this);
			}
			break;
			
		case R.id.btn_on_off:
			if(preferenceUtil.getPushStatus() == 1) {
				if (InternetConnectivity.isConnectedToInternet(SettingsActivity.this)) {
					new PushApiTask("0").execute();
				} else {
					SidraPulseApp.getInstance().openDialogForInternetChecking(SettingsActivity.this);
				}
			} else {
				if (InternetConnectivity.isConnectedToInternet(SettingsActivity.this)) {
					new PushApiTask("1").execute();
				} else {
					SidraPulseApp.getInstance().openDialogForInternetChecking(SettingsActivity.this);
				}
			}
			break;
			
		default:
			break;
		}
	}

	
	private class PushApiTask extends AsyncTask<Void, Void, Void> {
		String push;
		public PushApiTask(String push) {
			this.push = push;
		}
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(SettingsActivity.this, "", "Sending...", true, false);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
					pushStatus = CommunicationLayer.getPushStatusData(ConstantValues.FUNC_ID_PUSH_SETTING, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), push);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			if(pushStatus == 1) {
				if("1".equals(push)) {
					preferenceUtil.setPushStatus(Integer.parseInt(push));
					btnPushNotification.setBackgroundResource(R.drawable.on_btn);
				} else {
					preferenceUtil.setPushStatus(Integer.parseInt(push));
					btnPushNotification.setBackgroundResource(R.drawable.off_btn);
				}
			} else if(pushStatus == 5) {
				SidraPulseApp.getInstance().accessTokenChange(SettingsActivity.this);
				
			} else {
				
			}
			
		}
	}
	
	private class LogoutApiTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			progressDialog = SidraCustomProgressDialog.creator(SettingsActivity.this);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				logoutStatus = CommunicationLayer.getLogoutData(ConstantValues.FUNC_ID_LOGOUT, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			if(logoutStatus == 1) {
				preferenceUtil.setUserID("");
				preferenceUtil.setUserPasword("");
				Intent intent = new Intent(SettingsActivity.this, SignInActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
			} else if(logoutStatus == 5) {
				SidraPulseApp.getInstance().accessTokenChange(SettingsActivity.this);
				
			}
		}
	}

}
