package com.atomix.synctask;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.atomix.interfacecallback.OnUploadComplete;

public class VideoCommentsAsyncTask extends AsyncTask<String, Void, Void> {

	private Activity activity;
	private ProgressDialog progressDialog;
	private OnUploadComplete callback;
	private int responseStatus;
	private String data;
	int fieldNo;

	public VideoCommentsAsyncTask(Activity x, OnUploadComplete callback2, int fieldNo) {
		this.activity = x;
		this.callback = (OnUploadComplete) callback2;
		this.fieldNo = fieldNo;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//progressDialog = ProgressDialog.show(activity, "", "Please wait...", true, false);
	}

	@Override
	protected Void doInBackground(String... params) {
		String func_id = params[0];
		String path = params[1];
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httppost = new HttpGet(path);
			
			//MultipartEntity reqEntry = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			//reqEntry.addPart("func_id", new StringBody(func_id));
			
			//httppost.setEntity(reqEntry);

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();

			InputStream is = resEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			is.close();
			String thisdata = "";
			thisdata = sb.toString().trim();

			//JSONObject jDataObj = new JSONObject(thisdata);
			responseStatus = 1;
			//responseStatus = jDataObj.getInt("status")
			/*JSONObject jDataPhoto = jDataObj.getJSONObject("data");
			jDataPhoto.getString("comment_id");
			Log.i("Comments_id","" + jDataPhoto.getString("comment_id"));
			Log.i("Status","" + jDataPhoto.getBoolean("status"));*/
			data = thisdata;
			
			Log.i ("Youtube Response Jsoan","***#"+ thisdata);
			Log.i ("Video Status","***"+ responseStatus);

		} catch (Exception ex) {
			Log.i ("CommentsPostStatus_try","***#" + ex.getMessage());
			return null;
		}
			

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		/*if(progressDialog.isShowing()) {
			progressDialog.dismiss();
		}*/
		
		callback.onUploadComplete(responseStatus, data);
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
