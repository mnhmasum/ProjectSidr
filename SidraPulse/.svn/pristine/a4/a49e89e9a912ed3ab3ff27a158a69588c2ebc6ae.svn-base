package com.atomix.synctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.interfacecallback.UnReadRequest;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;


public class ClassifiedCreatePost extends AsyncTask<Void, Void, Void> {
	private int postStatus;
	private ProgressDialog dlog;
	private Activity mActivity;
	private UnReadRequest listener;
	private String categoryId;
	private String title;
	private String description;
	private String photo;
	private String isDraft;
	private int classifiedId;
	

	public ClassifiedCreatePost(Activity context, UnReadRequest listener, String categoryId, String title, String description, String photo, String isDraft, int classifiedId) {
		this.mActivity = context;
		this.listener = listener;
		this.categoryId = categoryId;
		this.title = title;
		this.description = description;
		this.photo = photo;
		this.isDraft = isDraft;
		this.classifiedId = classifiedId;
		
	}

	@Override
	protected void onPreExecute() {
		 dlog = SidraCustomProgressDialog.creator(mActivity);
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		try {
			if(classifiedId == 0) { 
				postStatus = CommunicationLayer.getClassifiedEntry(ConstantValues.FUNC_ID_CLASSIFIED_ENTRY, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), categoryId, title, description, photo, isDraft);
			}
			else {
				postStatus = CommunicationLayer.getClassifiedUpdate(ConstantValues.FUNC_ID_CLASSIFIED_UPDATE, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), categoryId, Integer.toString(classifiedId), title, description, photo, isDraft);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		 if (dlog.isShowing()) {
			 dlog.dismiss();
		 }
		 
		 listener.onTaskCompleted(postStatus);

	}
}
