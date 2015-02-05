package com.atomix.synctask;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.interfacecallback.OnRequestComplete;
import com.atomix.jsonparse.CommunicationLayer;

public class MainMenuAsyncTask extends AsyncTask<String, Void, Void> {

	private Activity activity;
	private ProgressDialog progressDialog;
	private OnRequestComplete callback;
	private int responseStatus;

	public MainMenuAsyncTask(Activity x, OnRequestComplete callback2) {
		this.activity = x;
		this.callback = (OnRequestComplete) callback2;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//progressDialog = ProgressDialog.show(activity, "", "Loading...", true, false);
		progressDialog = SidraCustomProgressDialog.creator(activity);
		//progressDialog.show();
	}

	@Override
	protected Void doInBackground(String... params) {
		String func_id = params[0];
		String user_id = params[1];
		String access_token = params[2];

		try {
			responseStatus = CommunicationLayer.getNotificationData(func_id, user_id, access_token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		try {
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		} catch (Exception e) {
			
		}
		
		callback.onRequestComplete(responseStatus);
	}

}
