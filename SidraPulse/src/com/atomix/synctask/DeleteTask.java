package com.atomix.synctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.interfacecallback.UnReadRequest;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;

public class DeleteTask extends AsyncTask<Void, Void, Void> {
	private int deleteStatus;
	private ProgressDialog dlog;
	private Activity mActivity;
	private UnReadRequest listener;
	private String elementId;
	private String type;

	public DeleteTask(Activity context, UnReadRequest listener, String type, String elementId) {
		this.mActivity = context;
		this.listener = listener;
		this.type = type;
		this.elementId = elementId;
	}

	@Override
	protected void onPreExecute() {
		 dlog = SidraCustomProgressDialog.creator(mActivity);
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		try {
			Log.i("PARAM_FOR_DELETE","F_id " + ConstantValues.FUNC_ID_DELETE_API + " USER_ID " + Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()) + " access " + SidraPulseApp.getInstance().getUserInfo().getAccessToken() + " type: " + type + " e_id: " + elementId);
			deleteStatus = CommunicationLayer.getDeleteAPI(ConstantValues.FUNC_ID_DELETE_API, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), type, elementId);
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
		 
		 Log.i("DELETED_STATUS","---" + deleteStatus);
		 
		 listener.onTaskCompleted(deleteStatus);

	}
}
