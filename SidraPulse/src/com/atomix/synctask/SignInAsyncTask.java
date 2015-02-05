package com.atomix.synctask;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.interfacecallback.OnRequestComplete;
import com.atomix.jsonparse.CommunicationLayer;

public class SignInAsyncTask extends AsyncTask<String, Void, Void> {

	private Activity activity;
	private ProgressDialog progressDialog;
	private OnRequestComplete callback;
	private int responseStatus;

	public SignInAsyncTask(Activity x, OnRequestComplete callback2) {
		this.activity = x;
		this.callback = (OnRequestComplete) callback2;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = SidraCustomProgressDialog.creator(activity);
	}

	@Override
	protected Void doInBackground(String... params) {
		String func_id = params[0];
		String user_id = params[1];
		String password = params[2];
		String device_type = params[3];
		String access_token = params[4];
		String others = params[5];	/*Others is not implemented*/
		try {
			//responseStatus = CommunicationLayer.getGalleryData(func_id, user_id, access_token, others);
			responseStatus = CommunicationLayer.getSignInData(func_id, user_id, password, device_type, access_token, others);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		
		callback.onRequestComplete(responseStatus);
	}

}

/*ServerComAsyncTask request = new ServerComAsyncTask(this,new OnRequestComplete() {
	@Override
	public void onRequestComplete(String result) {
		System.out.println("Result Finally :: "+ result);
	}
 });
 
request.execute("URL");
*/
