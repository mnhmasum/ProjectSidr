package com.atomix.sidrapulse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sharedpreference.PreferenceUtil;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;

public class SplashScreenActivity extends Activity {

	private ImageView imgViewLoading;
	private AnimationDrawable loadingViewAnim = null;
	private int signinStatus;
	private PreferenceUtil preferenceUtil;
	private int pushType = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		initViews();
		loadData();
	}

	private void loadData() {
		
		if(getIntent().getExtras() != null) {
			pushType = getIntent().getExtras().getInt("type");
		}
		imgViewLoading.setImageBitmap(null);
		imgViewLoading.setBackgroundResource(R.anim.loading_frame);
		loadingViewAnim = (AnimationDrawable) imgViewLoading.getBackground();
			
		if (InternetConnectivity.isConnectedToInternet(SplashScreenActivity.this)) {
			if(!preferenceUtil.getUserID().equalsIgnoreCase("") && !preferenceUtil.getUserPassword().equalsIgnoreCase("")) {
				new SingUpApiTask().execute();
			} else {
				loadingViewAnim.start();
				checkIfAnimationFinished(loadingViewAnim);
			}
			
		} else {
	        SidraPulseApp.getInstance().openDialogForInternetChecking(SplashScreenActivity.this);
		}
	}

	private void initViews() {
		imgViewLoading = (ImageView) findViewById(R.id.img_view_loading);
		preferenceUtil = new PreferenceUtil(getApplicationContext());
	}

	private class SingUpApiTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			loadingViewAnim.start();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				signinStatus = CommunicationLayer.getSignInData(ConstantValues.FUNC_ID_SIGN_IN, preferenceUtil.getUserID(), preferenceUtil.getUserPassword(), ConstantValues.DEVICE_TYPE, preferenceUtil.getDeviceToken(), preferenceUtil.getRegistrationKey());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			loadingViewAnim.stop();
			
			if(signinStatus == 1) {
				if (pushType == 1) {
					startActivity(new Intent(SplashScreenActivity.this, AnnouncementsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				} else if (pushType == 2) {
					startActivity(new Intent(SplashScreenActivity.this, ForumsActivity.class).putExtra("is_my_post", true).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				}else if (pushType == 3) {
					Intent eventIntent = new Intent(SplashScreenActivity.this, EventsActivity.class);
					eventIntent.putExtra("is_my_event", true);
					
				 	if (getIntent().getExtras().getBoolean("IS_ID_FOUND")) {
						eventIntent.putExtra("is_id_found", true);
						eventIntent.putExtra("EVENT_ID", getIntent().getExtras().getInt("EVENT_ID"));
					} else{
						eventIntent.putExtra("is_id_found", false);
					}
					eventIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(eventIntent);
					
				} else {
					startActivity(new Intent(SplashScreenActivity.this, MainMenuActivity.class).putExtra("First_Time", true));
				}
				
				finish();
				
			} else if(signinStatus == 5) {
				SidraPulseApp.getInstance().accessTokenChange(SplashScreenActivity.this);
				
			} else {
				startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
				finish();
			}
			
		}
	}
	
	private void checkIfAnimationFinished(AnimationDrawable anim) {
		int timeFrame = 100;
		final AnimationDrawable a = anim;

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
					checkIfAnimationFinished(a);
				} 
				else {
					startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
					finish();
				}
			}
		}, timeFrame);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		finish();
		startActivity(getIntent());
	}

}
