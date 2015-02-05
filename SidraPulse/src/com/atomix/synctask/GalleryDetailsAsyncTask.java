package com.atomix.synctask;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.interfacecallback.OnRequestComplete;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.sidrapulse.GalleryDetailsActivity;

public class GalleryDetailsAsyncTask extends AsyncTask<String, Void, Void> {

	private Activity activity;
	private ProgressDialog progressDialog;
	private OnRequestComplete callback;
	private int responseStatus;

	public GalleryDetailsAsyncTask(Activity x, OnRequestComplete callback2) {
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
		
		//String func_id, String user_id, String access_token, String gallery_id, String page_no
		String func_id = params[0];
		String user_id = params[1];
		String access_token = params[2];
		String gallery_id = params[3];
		String last_element_id = params[4];
		String direction = params[5];
		
		try {
			
			if (direction.equals("0")) {
				responseStatus = CommunicationLayer.getGalleryImageAndVideoData(func_id, user_id, access_token, gallery_id, last_element_id, direction);
			} else if (ConstantValues.PullDownActive) {
				responseStatus = CommunicationLayer.getGalleryImageAndVideoData(func_id, user_id, access_token, gallery_id, last_element_id, direction);
			}
			
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
