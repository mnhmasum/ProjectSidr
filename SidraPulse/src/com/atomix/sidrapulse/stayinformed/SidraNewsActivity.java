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
import com.atomix.adapter.SidraNewsAdapter;
import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.sidrapulse.MainMenuActivity;
import com.atomix.sidrapulse.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class SidraNewsActivity extends Activity implements OnItemClickListener, OnClickListener {

	private Button btnBack;
	private Button btnMenu;
	private PullToRefreshListView lstViewSidraNews;
	private ProgressDialog progressDialog;
	private int sidraNewsStatus;
	private SidraNewsAdapter sidraNewsAdapter;
	private int pageNo = 1;
	private TextView txtViewEmptyList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sidra_news);
		
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
		lstViewSidraNews = (PullToRefreshListView) findViewById(R.id.lst_view_sidra_news);
		lstViewSidraNews.setMode(Mode.BOTH);
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
		lstViewSidraNews.setOnItemClickListener(this);
		
		lstViewSidraNews.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String first_element_id = Integer.toString(SidraPulseApp.getInstance().getSidraInNewsAPI().get(0).getId());
				new SidraNewsApiTask(first_element_id, "0").execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if (!InternetConnectivity.isConnectedToInternet(SidraNewsActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(SidraNewsActivity.this);
					lstViewSidraNews.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getSidraInNewsAPI() == null) {
					lstViewSidraNews.onRefreshComplete();
					return;
				}
				
				/*if (SidraPulseApp.getInstance().getSidraInNewsAPI().size() >= pageNo*10) {
					pageNo++;
					new SidraNewsApiTask().execute();
					
				} else {
					new SidraNewsApiTask().execute();
				}*/
				
				String last_element_id = Integer.toString(SidraPulseApp.getInstance().getSidraInNewsAPI().get(SidraPulseApp.getInstance().getSidraInNewsAPI().size()-1).getId());
				new SidraNewsApiTask(last_element_id, "1").execute();
				
			}

		});
	}

	private void loadData() {
		ConstantValues.PullDownActive = true;
		if (InternetConnectivity.isConnectedToInternet(SidraNewsActivity.this)) { 
			new SidraNewsApiTask("","").execute();
		} else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(SidraNewsActivity.this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
			
		case R.id.btn_menu:
			startActivity(new Intent(SidraNewsActivity.this, MainMenuActivity.class));
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent intent = new Intent(SidraNewsActivity.this, SidraNewsDetailsActivity.class);
		intent.putExtra("position", position - 1);
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
			progressDialog = SidraCustomProgressDialog.creator(SidraNewsActivity.this);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				
				if (direction.equals("0")) {
					sidraNewsStatus = CommunicationLayer.getSidraInNewsAPI(ConstantValues.FUNC_ID_SIDRA_IN_NEWS_API, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), this.last_element_id, this.direction);
				} else if (ConstantValues.PullDownActive) {
					sidraNewsStatus = CommunicationLayer.getSidraInNewsAPI(ConstantValues.FUNC_ID_SIDRA_IN_NEWS_API, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), this.last_element_id, this.direction);
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
				sidraNewsAdapter.notifyDataSetChanged();
				lstViewSidraNews.onRefreshComplete();
				return;
			}
			
			if(sidraNewsStatus == 1) {
				if(SidraPulseApp.getInstance().getSidraInNewsAPI() != null) {
					lstViewSidraNews.setVisibility(View.VISIBLE);
					txtViewEmptyList.setVisibility(View.INVISIBLE);
					sidraNewsAdapter = new SidraNewsAdapter(getApplicationContext(), SidraPulseApp.getInstance().getSidraInNewsAPI());
					lstViewSidraNews.setAdapter(sidraNewsAdapter);
					
				} else {
					lstViewSidraNews.setAdapter(null);
					lstViewSidraNews.setVisibility(View.INVISIBLE);
					txtViewEmptyList.setVisibility(View.VISIBLE);
					
				}
				
			} else {
				lstViewSidraNews.setVisibility(View.INVISIBLE);
				txtViewEmptyList.setVisibility(View.VISIBLE);
			}
			
			lstViewSidraNews.onRefreshComplete();
		}
	}

}
