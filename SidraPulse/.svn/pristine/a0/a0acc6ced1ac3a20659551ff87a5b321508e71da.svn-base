package com.atomix.sidrapulse;

import java.util.ArrayList;

import com.atomix.adapter.StayInformedAdapter;
import com.atomix.sidrapulse.stayinformed.PressReleaseActivity;
import com.atomix.sidrapulse.stayinformed.SidraNewsActivity;
import com.atomix.sidrapulse.stayinformed.SocialChannelsActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;

public class StayInformedActivity extends Activity implements OnItemClickListener, OnClickListener {

	private Button btnBack;
	private ListView lstViewStayInformed;
	private ArrayList<String> stayInformedArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stay_informed);
		
		initViews();
		setListener();
		loadData();
	}

	private void initViews() {
		btnBack = (Button) findViewById(R.id.btn_back);
		lstViewStayInformed = (ListView) findViewById(R.id.lst_view_stay_informed);
		stayInformedArray = new ArrayList<String>();
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		lstViewStayInformed.setOnItemClickListener(this);
	}

	private void loadData() {
		stayInformedArray.add("Sidra In the News");
		stayInformedArray.add("Press Releases");
		stayInformedArray.add("Social Media Channels");
		
		if(stayInformedArray != null) {
			lstViewStayInformed.setAdapter(new StayInformedAdapter(getApplicationContext(), stayInformedArray));
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if(position == 0) {
			startActivity(new Intent(StayInformedActivity.this, SidraNewsActivity.class));
		} else if(position == 1) {
			startActivity(new Intent(StayInformedActivity.this, PressReleaseActivity.class));
		} else if(position == 2) {
			startActivity(new Intent(StayInformedActivity.this, SocialChannelsActivity.class));
		} else {
			
		}
	}

}
