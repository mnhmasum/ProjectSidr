package com.atomix.sidrapulse;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.atomix.adapter.AnnouncementsAdapter;
import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.interfacecallback.UnReadRequest;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.synctask.UnReadTask;
import com.atomix.utils.Utilities;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class AnnouncementsActivity extends Activity implements OnClickListener, OnItemClickListener {

	private Button btnBack;
	private ProgressDialog progressDialog;
	private int announcementStatus;
	private int pageNo = 1;
	private PullToRefreshListView lstViewAnnouncements;
	private AnnouncementsAdapter adapter;
	private TextView txtViewEmptyList; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_announcements);
		
		initViews();
		setListener();
		loadData();
	}

	private void initViews() {
		txtViewEmptyList = (TextView) findViewById(R.id.txt_view_empty_list);
		txtViewEmptyList.setText(getResources().getString(R.string.empty_list));
		txtViewEmptyList.setVisibility(View.INVISIBLE);
		btnBack = (Button) findViewById(R.id.btn_back);
		lstViewAnnouncements = (PullToRefreshListView) findViewById(R.id.lst_view_announcements);
		lstViewAnnouncements.setMode(Mode.BOTH);
	
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		lstViewAnnouncements.setOnItemClickListener(this);
		
		lstViewAnnouncements.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				//Code for pull to up refresh
				if (!InternetConnectivity.isConnectedToInternet(AnnouncementsActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(AnnouncementsActivity.this);
					adapter.notifyDataSetChanged();
					lstViewAnnouncements.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getAnnouncementsInfoList() == null) {
					lstViewAnnouncements.onRefreshComplete();
					return;
				}
			
				new GetDataTask("0").execute();
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				
				if (!InternetConnectivity.isConnectedToInternet(AnnouncementsActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(AnnouncementsActivity.this);
					adapter.notifyDataSetChanged();
					lstViewAnnouncements.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getAnnouncementsInfoList() == null) {
					lstViewAnnouncements.onRefreshComplete();
					return;
				}
				
				new GetDataTask("1").execute();
			}

		});
	}
	
	private void loadData() {
		adapter = new AnnouncementsAdapter(AnnouncementsActivity.this, SidraPulseApp.getInstance().getAnnouncementsInfoList());
		if (InternetConnectivity.isConnectedToInternet(AnnouncementsActivity.this)) {
			new AnnouncementsApiTask().execute();
		} else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(AnnouncementsActivity.this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
		if (InternetConnectivity.isConnectedToInternet(AnnouncementsActivity.this)) {
			if(SidraPulseApp.getInstance().getAnnouncementsInfoList().get(position-1).getReadStatus() == 0) {
				new UnReadTask(AnnouncementsActivity.this, new UnReadRequest() {
					@Override
					public void onTaskCompleted(int status) {
						if(status == 1){
							SidraPulseApp.getInstance().getAnnouncementsInfoList().get(position - 1).setReadStatus(1);
							adapter.notifyDataSetChanged();
							Intent intent = new Intent(AnnouncementsActivity.this, AnnouncementDetailsActivity.class);
							intent.putExtra("click_position", position - 1);
							startActivity(intent);
							
						} else if(announcementStatus == 5) {
							SidraPulseApp.getInstance().accessTokenChange(AnnouncementsActivity.this);
							
						} else{
							Utilities.showToast(AnnouncementsActivity.this, ConstantValues.failureMessage);
						}
					}
				}, Integer.parseInt(ConstantValues.FUNC_ID_ANNOUNCEMENT), position - 1, false).execute();
			} else {
				Intent intent = new Intent(AnnouncementsActivity.this, AnnouncementDetailsActivity.class);
				intent.putExtra("click_position", position - 1);
				startActivity(intent);
			}
			
		} else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(AnnouncementsActivity.this);
			return;
		}
		
	}
	
	private class AnnouncementsApiTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			progressDialog = SidraCustomProgressDialog.creator(AnnouncementsActivity.this);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				announcementStatus = CommunicationLayer.getAnnouncementData(ConstantValues.FUNC_ID_ANNOUNCEMENT, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "", "");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null; 
		}

		@Override
		protected void onPostExecute(Void result) {
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			if(announcementStatus == 1) {
				if(SidraPulseApp.getInstance().getAnnouncementsInfoList() != null) {
					txtViewEmptyList.setVisibility(View.INVISIBLE);
					lstViewAnnouncements.setVisibility(View.VISIBLE);
					adapter = new AnnouncementsAdapter(getApplicationContext(), SidraPulseApp.getInstance().getAnnouncementsInfoList());
					lstViewAnnouncements.setAdapter(adapter);
				} else {
					lstViewAnnouncements.setVisibility(View.INVISIBLE);
					txtViewEmptyList.setVisibility(View.VISIBLE);
				}
			} else if(announcementStatus == 5) {
				SidraPulseApp.getInstance().accessTokenChange(AnnouncementsActivity.this);
			} else {
				lstViewAnnouncements.setVisibility(View.INVISIBLE);
				txtViewEmptyList.setVisibility(View.VISIBLE);
			}
			
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		//finish();
		//startActivity(getIntent());
	}
	
	private class GetDataTask extends AsyncTask<String, Void, String[]> {
		
		private String direction = null;
		
		public GetDataTask(String direction) {
			this.direction = direction; 
		}
		
		@Override
		protected String[] doInBackground(String... params) {
			
			try {
				
				if (this.direction.equals("0")) {
					String element_id = Integer.toString(SidraPulseApp.getInstance().getAnnouncementsInfoList().get(SidraPulseApp.getInstance().getAnnouncementsInfoList().size() - SidraPulseApp.getInstance().getAnnouncementsInfoList().size()).getId()); 
					announcementStatus = CommunicationLayer.getAnnouncementData(ConstantValues.FUNC_ID_ANNOUNCEMENT, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), element_id, this.direction);
					
				} else if (ConstantValues.PullDownActive) {
					String element_id = Integer.toString(SidraPulseApp.getInstance().getAnnouncementsInfoList().get(SidraPulseApp.getInstance().getAnnouncementsInfoList().size() - 1).getId()); 
					announcementStatus = CommunicationLayer.getAnnouncementData(ConstantValues.FUNC_ID_ANNOUNCEMENT, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), element_id, this.direction);
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			
			if(announcementStatus == 1) {
				if(SidraPulseApp.getInstance().getAnnouncementsInfoList() != null) {
					//lstViewAnnouncements.setAdapter(adapter);
				}
			} else if (announcementStatus == 5) {
				SidraPulseApp.getInstance().accessTokenChange(AnnouncementsActivity.this);
			}
			// Call onRefreshComplete when the list has been refreshed.
			Log.i("STATUS _","" + announcementStatus);
			adapter.notifyDataSetChanged();
			lstViewAnnouncements.onRefreshComplete();
			
			super.onPostExecute(result);
			
		}
	}

}
