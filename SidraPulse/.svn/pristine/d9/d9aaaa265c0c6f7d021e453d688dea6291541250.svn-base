package com.atomix.synctask;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.interfacecallback.OnUploadComplete;
import com.atomix.sidrainfo.SidraPulseApp;

public class ImageUploadAsyncTask extends AsyncTask<String, Void, Void> {

	private Activity activity;
	private ProgressDialog progressDialog;
	private OnUploadComplete callback;
	private int responseStatus;
	private String data;
	int fieldNo;

	public ImageUploadAsyncTask(Activity x, OnUploadComplete callback2, int fieldNo) {
		this.activity = x;
		this.callback = (OnUploadComplete) callback2;
		this.fieldNo = fieldNo;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = SidraCustomProgressDialog.creator(activity);
	}

	@Override
	protected Void doInBackground(String... params) {
		String func_id = params[0];
		String path = params[1];
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(SidraPulseApp.getInstance().getBaseUrl());

			MultipartEntity reqEntry = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntry.addPart("func_id", new StringBody(func_id));
			reqEntry.addPart("user_id", new StringBody(Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID())));
			reqEntry.addPart("access_token", new StringBody(SidraPulseApp.getInstance().getUserInfo().getAccessToken()));
			
			if (fieldNo == 1) {
				if (path.equalsIgnoreCase("") || path.length() < 2) {
					reqEntry.addPart("photo", new StringBody(""));
				} else {
					FileBody bin = new FileBody(new File(path), "image/jpeg");
					reqEntry.addPart("photo", bin);
				}
				
			
			} else if (fieldNo == 2) {
				if (path.equalsIgnoreCase("") || path.length() < 2) {
					reqEntry.addPart("image", new StringBody(""));
				} else {
					FileBody bin = new FileBody(new File(path), "image/jpeg");
					reqEntry.addPart("image", bin);
				}
				
				reqEntry.addPart("forum_id", new StringBody(params[2]));
				reqEntry.addPart("comment_text", new StringBody(params[3]));
				reqEntry.addPart("video", new StringBody(params[4]));

			}
			
			httppost.setEntity(reqEntry);

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
			
			if (fieldNo == 1) {
				
				JSONObject jDataObj = new JSONObject(thisdata);
				responseStatus = jDataObj.getInt("status");
				JSONObject jDataPhoto = jDataObj.getJSONObject("data");
				String uploadedPhotoName = jDataPhoto.getString("photo_name");
				data = uploadedPhotoName;
			
			} else if (fieldNo == 2) {

				JSONObject jDataObj = new JSONObject(thisdata);
				responseStatus = jDataObj.getInt("status");
				JSONObject jDataPhoto = jDataObj.getJSONObject("data");
				jDataPhoto.getString("comment_id");
				Log.i("Comments_id","" + jDataPhoto.getString("comment_id"));
				Log.i("Status","" + jDataPhoto.getBoolean("status"));
				data = thisdata;
			}
			
			Log.i ("CommentsPostStatus","***#"+ thisdata);
			Log.i ("UploadStatus","***"+ responseStatus);

		} catch (Exception ex) {
			Log.i ("CommentsPostStatus_try","***#" + ex.getMessage());
			return null;
		}
			

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		
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
