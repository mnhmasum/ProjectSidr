package com.atomix.sidrapulse;

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

import com.atomix.adapter.PolicyDeptItemAdapter;
import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;

public class PolicyDepartmentActivity extends Activity implements OnItemClickListener, OnClickListener {
	
	private Button btnBack;
	private Button btnMenu;
	private TextView txtViewTitle;
	private ListView lstViewPolicyDept;
	private ProgressDialog progressDialog;
	private int policyDeptStatus;
	private int position;
	private TextView txtViewEmptyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policy_department);
		
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
		txtViewTitle = (TextView) findViewById(R.id.txt_view_title);
		lstViewPolicyDept = (ListView) findViewById(R.id.lst_view_policies_dept);
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
		lstViewPolicyDept.setOnItemClickListener(this);
	}

	private void loadData() {
		if(getIntent().getExtras() != null) {
			position = getIntent().getExtras().getInt("position");
			txtViewTitle.setText(SidraPulseApp.getInstance().getPoliciesDeptInfoList().get(position).getCatName());
			if (InternetConnectivity.isConnectedToInternet(PolicyDepartmentActivity.this)) {
				new PolicyDepartmentApiTask().execute();
			} else {
				SidraPulseApp.getInstance().openDialogForInternetChecking(PolicyDepartmentActivity.this);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
	
		case R.id.btn_menu:
			startActivity(new Intent(PolicyDepartmentActivity.this, MainMenuActivity.class));
			finish();
			break;


		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(PolicyDepartmentActivity.this, PolicyDetailsActivity.class);
		intent.putExtra("position", arg2);
		startActivity(intent);
	}
	
	private class PolicyDepartmentApiTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			progressDialog = SidraCustomProgressDialog.creator(PolicyDepartmentActivity.this);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				policyDeptStatus = CommunicationLayer.getShowPolicyDataByDept(ConstantValues.FUNC_ID_SHOW_POLICY_DATA_BY_DEPT, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(SidraPulseApp.getInstance().getPoliciesDeptInfoList().get(position).getId()));
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
			
			if(policyDeptStatus == 1) {
				if(SidraPulseApp.getInstance().getPoliceisInfoList() != null) {
					lstViewPolicyDept.setVisibility(View.VISIBLE);
					txtViewEmptyList.setVisibility(View.INVISIBLE);
					lstViewPolicyDept.setAdapter(new PolicyDeptItemAdapter(getApplicationContext(), SidraPulseApp.getInstance().getPoliceisInfoList()));
				} else {
					lstViewPolicyDept.setAdapter(null);
					lstViewPolicyDept.setVisibility(View.INVISIBLE);
					txtViewEmptyList.setVisibility(View.VISIBLE);
				}
			} else if(policyDeptStatus == 5) {
				SidraPulseApp.getInstance().accessTokenChange(PolicyDepartmentActivity.this);
				
			} else {
				lstViewPolicyDept.setVisibility(View.INVISIBLE);
				txtViewEmptyList.setVisibility(View.VISIBLE);
			}
		}
	}

}
