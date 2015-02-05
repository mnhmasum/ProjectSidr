package com.atomix.sidrapulse;

import com.atomix.adapter.PolicyDeptAdapter;
import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

public class PoliciesActivity extends Activity implements OnItemClickListener, OnClickListener {
	
	private Button btnBack;
	private ListView lstViewPolicy;
	private ProgressDialog progressDialog;
	private int deptStatus;
	private TextView txtViewEmptyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policies);
		
		initViews();
		setListener();
		loadData();
	}
	
	private void initViews() {
		txtViewEmptyList = (TextView) findViewById(R.id.txt_view_empty_list);
		txtViewEmptyList.setText(getResources().getString(R.string.empty_list));
		txtViewEmptyList.setVisibility(View.INVISIBLE);
		btnBack = (Button) findViewById(R.id.btn_back);
		lstViewPolicy = (ListView) findViewById(R.id.lst_view_policies);
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		lstViewPolicy.setOnItemClickListener(this);
	}

	private void loadData() {
		if (InternetConnectivity.isConnectedToInternet(PoliciesActivity.this)) {
			new PolicyDepartmentApiTask().execute();
		} else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(PoliciesActivity.this);
		}
		
	}

	private class PolicyDepartmentApiTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			progressDialog = SidraCustomProgressDialog.creator(PoliciesActivity.this);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				deptStatus = CommunicationLayer.getShowAllPolicyDept(ConstantValues.FUNC_ID_SHOW_ALL_POLICY_DEPT, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken());
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
			
			if(deptStatus == 1) {
				if(SidraPulseApp.getInstance().getPoliciesDeptInfoList() != null) {
					lstViewPolicy.setVisibility(View.VISIBLE);
					txtViewEmptyList.setVisibility(View.INVISIBLE);
					lstViewPolicy.setAdapter(new PolicyDeptAdapter(getApplicationContext(), SidraPulseApp.getInstance().getPoliciesDeptInfoList()));
				} else {
					lstViewPolicy.setVisibility(View.INVISIBLE);
					txtViewEmptyList.setVisibility(View.VISIBLE);
					lstViewPolicy.setAdapter(null);
				}
			} else if(deptStatus == 5) {
				SidraPulseApp.getInstance().accessTokenChange(PoliciesActivity.this);
				
			} else {
				lstViewPolicy.setVisibility(View.INVISIBLE);
				txtViewEmptyList.setVisibility(View.VISIBLE);
			}
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
		Intent intent = new Intent(PoliciesActivity.this, PolicyDepartmentActivity.class);
		intent.putExtra("position", arg2);
		startActivity(intent);
	}

}
