package com.atomix.sidrapulse.stayinformed;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.atomix.adapter.PressReleaseAdapter;
import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.sidrapulse.MainMenuActivity;
import com.atomix.sidrapulse.PressReleaseDetailsActivity;
import com.atomix.sidrapulse.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class PressReleaseActivity extends Activity implements OnItemClickListener, OnClickListener{

	private Button btnBack;
	private Button btnMenu;
	private PullToRefreshListView lstViewPressRelease;
	private ProgressDialog progressDialog;
	private int pressReleaseStatus;
	private PressReleaseAdapter pressReleaseAdapter;
	private int pageNo = 1;
	private TextView txtViewEmptyList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_press_release);
		
		initViews();
		setListener();
		loadData();
	}
	
	private void initViews() {
		txtViewEmptyList = (TextView) findViewById(R.id.txt_view_empty_list);
		txtViewEmptyList.setText(getResources().getString(R.string.empty_list));
		txtViewEmptyList.setVisibility(View.INVISIBLE);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnMenu = (Button) findViewById(R.id.btn_menu);
		lstViewPressRelease = (PullToRefreshListView) findViewById(R.id.lst_view_press_release);
		lstViewPressRelease.setMode(Mode.BOTH);
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
		lstViewPressRelease.setOnItemClickListener(this);
		
		lstViewPressRelease.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				if (!InternetConnectivity.isConnectedToInternet(PressReleaseActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(PressReleaseActivity.this);
					lstViewPressRelease.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getSidraPressRelease() == null) {
					lstViewPressRelease.onRefreshComplete();
					return;
				}
				
				String first_element_id = Integer.toString(SidraPulseApp.getInstance().getSidraPressRelease().get(0).getId());
				new SidraNewsApiTask(first_element_id, "0").execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if (!InternetConnectivity.isConnectedToInternet(PressReleaseActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(PressReleaseActivity.this);
					lstViewPressRelease.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getSidraPressRelease() == null) {
					lstViewPressRelease.onRefreshComplete();
					return;
				}
				/*
				if (SidraPulseApp.getInstance().getSidraPressRelease().size() >= pageNo*10) {
					pageNo++;
					new SidraNewsApiTask().execute();
					
				} else {
					new SidraNewsApiTask().execute();
				}*/
				
				String last_element_id = Integer.toString(SidraPulseApp.getInstance().getSidraPressRelease().get(SidraPulseApp.getInstance().getSidraPressRelease().size()-1).getId());
				new SidraNewsApiTask(last_element_id, "1").execute();
				
			}

		});
	}

	private void loadData() {
		ConstantValues.PullDownActive = true;
		if (InternetConnectivity.isConnectedToInternet(PressReleaseActivity.this)) { 
			new SidraNewsApiTask("","").execute();
		} else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(PressReleaseActivity.this);
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
			
		case R.id.btn_menu:
			startActivity(new Intent(PressReleaseActivity.this, MainMenuActivity.class));
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent intent = new Intent(PressReleaseActivity.this, PressReleaseDetailsActivity.class);
		intent.putExtra("click_position", position - 1);
		startActivity(intent);
	}
	
	private class SidraNewsApiTask extends AsyncTask<Void, Void, Void> {
		
		public String last_element_id = null;
		public String direction = null;
		
		public SidraNewsApiTask(String last_element_id, String direction) {
			this.last_element_id = last_element_id;
			this.direction = direction;
		}
		
		
		@Override
		protected void onPreExecute() {
			progressDialog = SidraCustomProgressDialog.creator(PressReleaseActivity.this);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				
				if (direction.equals("0")) {
					pressReleaseStatus = CommunicationLayer.getSidraPressRelease(ConstantValues.FUNC_ID_SIDRA_PRESS_RELEASE, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), this.last_element_id, this.direction);
				} else if (ConstantValues.PullDownActive) {
					pressReleaseStatus = CommunicationLayer.getSidraPressRelease(ConstantValues.FUNC_ID_SIDRA_PRESS_RELEASE, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), this.last_element_id, this.direction);
				}
				
				
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
			
			if (this.direction.equals("0") || this.direction.equals("1")) {
				pressReleaseAdapter.notifyDataSetChanged();
				lstViewPressRelease.onRefreshComplete();
				return;
			}
			
			if(pressReleaseStatus == 1) {
				if(SidraPulseApp.getInstance().getSidraPressRelease() != null) {
					pressReleaseAdapter = new PressReleaseAdapter(getApplicationContext(), SidraPulseApp.getInstance().getSidraPressRelease());
					txtViewEmptyList.setVisibility(View.INVISIBLE);
					lstViewPressRelease.setVisibility(View.VISIBLE);
					lstViewPressRelease.setAdapter(pressReleaseAdapter);
				} else {
					lstViewPressRelease.setAdapter(null);
					txtViewEmptyList.setVisibility(View.VISIBLE);
					lstViewPressRelease.setVisibility(View.INVISIBLE);
				}
			} else {
				txtViewEmptyList.setVisibility(View.VISIBLE);
				lstViewPressRelease.setVisibility(View.INVISIBLE);
			}
		}
	}

}
