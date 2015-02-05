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
import com.atomix.interfacecallback.OnImageUploadComplete;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;

public class UploadImageAsyncTask extends AsyncTask<String, Void, Void> {

	private Activity activity;
	private ProgressDialog progressDialog;
	private OnImageUploadComplete callback;
	private int responseStatus;
	boolean isClassified = false;
	private String uploadedPhotoName = ""; 

	public UploadImageAsyncTask(Activity x, OnImageUploadComplete callback2, boolean isClassified) {
		this.activity = x;
		this.callback = (OnImageUploadComplete) callback2;
		this.isClassified = isClassified;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = SidraCustomProgressDialog.creator(activity);
	}

	@Override
	protected Void doInBackground(String... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(SidraPulseApp.getInstance().getBaseUrl());
	
				MultipartEntity reqEntry = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				if (isClassified) {
					reqEntry.addPart("func_id", new StringBody(ConstantValues.FUNC_ID_CLASSIFIED_PHOTO_UPLOAD));
				} else {
					reqEntry.addPart("func_id", new StringBody(ConstantValues.FUNC_ID_THREAD_PHOTO_UPLOAD));
				}
				reqEntry.addPart("user_id", new StringBody(Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID())));
				reqEntry.addPart("access_token", new StringBody(SidraPulseApp.getInstance().getUserInfo().getAccessToken()));
				
				FileBody bin = new FileBody(new File(params[0]), "image/jpeg");
				reqEntry.addPart("photo", bin);
				
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
				
				JSONObject jDataObj = new JSONObject(thisdata);
				responseStatus = jDataObj.getInt("status");
	
				JSONObject jDataPhoto = jDataObj.getJSONObject("data");
				uploadedPhotoName = jDataPhoto.getString("photo_name");
				
	//			if(isClassified) {
	//				int index = ClassifiedsCreateNewPostActivity.photoAddedIndex;
	//				ClassifiedsCreateNewPostActivity.mapPhotoPaths.put(index, params[0]);
	//				ClassifiedsCreateNewPostActivity.mapPhotoName.put(index, jDataPhoto.getString("photo_name"));
	//			} else {
	//				//ForumNewPostActivity.serverImagePathList += uploadedPhotoName + ",";
	//				ForumNewPostActivity.takePath.add(uploadedPhotoName);
	//			}
	
			} catch (Exception ex) {
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
		
		Log.e("uploaded photo name", uploadedPhotoName);
		callback.OnImageUploadComplete(uploadedPhotoName);
		
//		if(responseStatus == 1)
//		{
//	//	callback.onRequestComplete(responseStatus);
//		callback.OnImageUploadComplete(uploadedPhotoName);
//		}
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

