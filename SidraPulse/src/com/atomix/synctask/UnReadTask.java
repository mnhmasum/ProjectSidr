package com.atomix.synctask;

import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.interfacecallback.UnReadRequest;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class UnReadTask extends AsyncTask<Void, Void, Void> {
	private int unReadStatus;
	private ProgressDialog dlog;
	private Activity mActivity;
	private UnReadRequest listener;
	private int type;
	private int position;
	private boolean isBubble;

	public UnReadTask(Activity context, UnReadRequest listener, int type, int position, boolean isBubble) {
		this.mActivity = context;
		this.listener = listener;
		this.type = type;
		this.position = position;
		this.isBubble = isBubble;
	}

	@Override
	protected void onPreExecute() {
		 dlog = SidraCustomProgressDialog.creator(mActivity);
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		try {
			if(isBubble) {
				unReadStatus = CommunicationLayer.getBubbleUnReadData(ConstantValues.FUNC_ID_BUBBLE_READ, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(type));
			} else {
				if(type == Integer.parseInt(ConstantValues.FUNC_ID_ANNOUNCEMENT)) {
					unReadStatus = CommunicationLayer.getUnReadData(ConstantValues.FUNC_ID_UNREAD, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "1", Integer.toString(SidraPulseApp.getInstance().getAnnouncementsInfoList().get(position).getId()));
				} else if(type == Integer.parseInt(ConstantValues.FUNC_ID_EVENTS)) {
					unReadStatus = CommunicationLayer.getUnReadData(ConstantValues.FUNC_ID_UNREAD, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "3", Integer.toString(SidraPulseApp.getInstance().getEventsInfoList().get(position).getId()));
				} else {
					
				}
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
		 
		 listener.onTaskCompleted(unReadStatus);

	}
}
