package com.atomix.synctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.interfacecallback.UnReadRequest;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.SidraPulseApp;

public class FavoriateOrNotAsyncTask extends AsyncTask<Void, Void, Void> {
	private ProgressDialog progressDialog;
	private Activity activity;
	private UnReadRequest listener;
	private String id;
	private String bookmarkStatus;
	private int favoriateOrNotStatus;
	private String apiNo;
	
	public FavoriateOrNotAsyncTask(Activity activity, String id, String bookmarkStatus, String apiNo, UnReadRequest listener) {
		this.activity = activity;
		this.listener = listener;
		this.id = id;
		this.bookmarkStatus = bookmarkStatus;
		this.apiNo = apiNo;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = SidraCustomProgressDialog.creator(activity);
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		try {
			if("16".equals(apiNo)) {
				favoriateOrNotStatus= CommunicationLayer.getBookmarkOfferAndPromotion(apiNo, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), id, bookmarkStatus);
			}
			else if ("8".equals(apiNo)) {
				favoriateOrNotStatus= CommunicationLayer.getEventMakeFavourite(apiNo, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), bookmarkStatus, id);
			} else if("34".equals(apiNo)) {
				favoriateOrNotStatus= CommunicationLayer.getStaffBookmarkData(apiNo, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), bookmarkStatus, id);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			listener.onTaskCompleted(favoriateOrNotStatus);

		}
	}
