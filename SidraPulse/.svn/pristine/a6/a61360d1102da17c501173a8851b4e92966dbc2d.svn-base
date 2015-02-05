package com.atomix.sidrapulse;

import java.util.ArrayList;

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

import com.atomix.adapter.HumanResourceCategoryAdapter;
import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.datamodel.ArticleInfo;
import com.atomix.datamodel.HumanResourceInfo;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class HumanResourcesActivity extends Activity implements OnItemClickListener, OnClickListener {

	private Button btnBack;
	private PullToRefreshListView lstViewHumanResource;
	private ProgressDialog progressDialog;
	private int humanResoueceStatus;
	private TextView txtViewEmptyList;
	private HumanResourceCategoryAdapter humanResourceCatAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_human_resources);
		
		initViews();
		setListener();
		loadData();
	}

	private void initViews() {
		txtViewEmptyList = (TextView) findViewById(R.id.txt_view_empty_list);
		txtViewEmptyList.setText(getResources().getString(R.string.empty_list));
		txtViewEmptyList.setVisibility(View.INVISIBLE);
		btnBack = (Button) findViewById(R.id.btn_back);
		lstViewHumanResource = (PullToRefreshListView) findViewById(R.id.lst_view_human_resource);
		lstViewHumanResource.setMode(Mode.BOTH);
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		lstViewHumanResource.setOnItemClickListener(this);
		lstViewHumanResource.setOnRefreshListener(new OnRefreshListener2 <ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				//Code for pull to up refresh
				if (!InternetConnectivity.isConnectedToInternet(HumanResourcesActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(HumanResourcesActivity.this);
					//adapter.notifyDataSetChanged();
					lstViewHumanResource.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getArticleInfoList() == null) {
					lstViewHumanResource.onRefreshComplete();
					return;
				}
				
				new GetDataTask("0").execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				
				if (!InternetConnectivity.isConnectedToInternet(HumanResourcesActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(HumanResourcesActivity.this);
					lstViewHumanResource.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getArticleInfoList() == null) {
					lstViewHumanResource.onRefreshComplete();
					return;
				}
				
				new GetDataTask("1").execute();
				
			}

		});
	}

	private void loadData() {
		
		View header = (View) getLayoutInflater().inflate(R.layout.staff_directory_row, null);
		TextView txtView = (TextView) header.findViewById(R.id.txt_view_title);
		txtView.setText("FAQs");
		
		//lstViewHumanResource.addHeaderView(header);
		
		if (InternetConnectivity.isConnectedToInternet(HumanResourcesActivity.this)) {
			humanResourceCatAdapter =  new HumanResourceCategoryAdapter(HumanResourcesActivity.this, SidraPulseApp.getInstance().getArticleInfoList());
			new HumanResourceApiTask().execute();
		} else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(HumanResourcesActivity.this);
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		Log.i("FAQS", "***" + arg2);
		if(arg2 == 1) {
			Intent intent = new Intent(HumanResourcesActivity.this, HumanResourceDetailsActivity.class);
			intent.putExtra("position", arg2);
			startActivity(intent);
		} else {
			Intent intent = new Intent(HumanResourcesActivity.this, ArticleDetailsActivity.class);
			intent.putExtra("position", arg2 - 1);
			startActivity(intent);
		}
	}
	
	private class HumanResourceApiTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			progressDialog = SidraCustomProgressDialog.creator(HumanResourcesActivity.this);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				humanResoueceStatus = CommunicationLayer.getHumanResourcesCategory(ConstantValues.FUNC_ID_HUMAN_RESOURCES_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "2", true, "", "");
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
			
			if(humanResoueceStatus == 1) {
				if(SidraPulseApp.getInstance().getArticleInfoList() != null) {
					txtViewEmptyList.setVisibility(View.INVISIBLE);
					lstViewHumanResource.setVisibility(View.VISIBLE);
					ArticleInfo articleInfoHeader = new  ArticleInfo();
					articleInfoHeader.setQuestion("FAQs");
					SidraPulseApp.getInstance().getArticleInfoList().add(0, articleInfoHeader);
					humanResourceCatAdapter =  new HumanResourceCategoryAdapter(HumanResourcesActivity.this, SidraPulseApp.getInstance().getArticleInfoList());
					lstViewHumanResource.setAdapter(humanResourceCatAdapter);
				} else {
					txtViewEmptyList.setVisibility(View.VISIBLE);
					lstViewHumanResource.setVisibility(View.INVISIBLE);
				}
			} else {
				txtViewEmptyList.setVisibility(View.VISIBLE);
				lstViewHumanResource.setVisibility(View.INVISIBLE);
			}
		}
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
					String element_id = Integer.toString(SidraPulseApp.getInstance().getArticleInfoList().get(1).getId());
					humanResoueceStatus = CommunicationLayer.getHumanResourcesCategory(ConstantValues.FUNC_ID_HUMAN_RESOURCES_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "2", true, element_id, this.direction);
				} else if (ConstantValues.PullDownActive) {
					String element_id = Integer.toString(SidraPulseApp.getInstance().getArticleInfoList().get(SidraPulseApp.getInstance().getArticleInfoList().size() - 1).getId());
					humanResoueceStatus = CommunicationLayer.getHumanResourcesCategory(ConstantValues.FUNC_ID_HUMAN_RESOURCES_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "2", true, element_id, this.direction);
				}
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			
			if(humanResoueceStatus == 1) {
				if(SidraPulseApp.getInstance().getArticleInfoList() != null) {
					humanResourceCatAdapter.notifyDataSetChanged();
				} else {
					
				}
			} else if (humanResoueceStatus == 5) {
				SidraPulseApp.getInstance().accessTokenChange(HumanResourcesActivity.this);
			}
			
			lstViewHumanResource.onRefreshComplete();
			super.onPostExecute(result);
			
		}
	}

}
