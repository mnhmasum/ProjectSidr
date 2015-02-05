package com.atomix.sidrapulse;

import com.atomix.customview.SidraTextView;
import com.atomix.sidrainfo.SidraPulseApp;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

public class AnnouncementDetailsActivity extends Activity implements OnClickListener {

	private Button btnBack;
	private Button btnMenu;
	private SidraTextView txtViewTitle;
	private SidraTextView txtViewType;
	private SidraTextView txtViewDateTime;
	private SidraTextView txtViewDescription;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_announcement_details);
		
		initViews();
		setListener();
		loadData();
	}

	private void loadData() {
		if(getIntent().getExtras() != null) {
			int position = getIntent().getExtras().getInt("click_position");
			txtViewTitle.setText(SidraPulseApp.getInstance().getAnnouncementsInfoList().get(position).getTitle().toString().trim());
			Linkify.addLinks(txtViewTitle, Linkify.ALL);
			
			txtViewType.setText("Type: "+SidraPulseApp.getInstance().getAnnouncementsInfoList().get(position).getType().toString().trim());
			if("OAM".equals(SidraPulseApp.getInstance().getAnnouncementsInfoList().get(position).getType())) {
				txtViewType.setTextColor(Color.parseColor("#2367B2"));
			} else if("CEMC".equals(SidraPulseApp.getInstance().getAnnouncementsInfoList().get(position).getType())) {
				txtViewType.setTextColor(Color.parseColor("#0C8943"));
			} else if("FLASH".equals(SidraPulseApp.getInstance().getAnnouncementsInfoList().get(position).getType())) {
				txtViewType.setTextColor(Color.parseColor("#C82828"));
			}
			
			txtViewDescription.setText(SidraPulseApp.getInstance().getAnnouncementsInfoList().get(position).getDescription().toString().trim());
			Linkify.addLinks(txtViewDescription, Linkify.ALL);
			
			String dateAndTime = SidraPulseApp.getInstance().getAnnouncementsInfoList().get(position).getDayAndDate().toString().trim() +", "+SidraPulseApp.getInstance().getAnnouncementsInfoList().get(position).getTime().toString().trim();
			txtViewDateTime.setText(dateAndTime);
			
			
		}
	}

	private void initViews() {
		btnBack = (Button) findViewById(R.id.btn_back);
		btnMenu = (Button) findViewById(R.id.btn_menu);
		txtViewTitle = (SidraTextView) findViewById(R.id.txt_view_title);
		txtViewType = (SidraTextView) findViewById(R.id.txt_view_type);
		txtViewDateTime = (SidraTextView) findViewById(R.id.txt_view_date_time);
		txtViewDescription = (SidraTextView) findViewById(R.id.txt_view_description);
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
			
		case R.id.btn_menu:
			startActivity(new Intent(AnnouncementDetailsActivity.this, MainMenuActivity.class));
			finish();
			break;

		default:
			break;
		}
	}

}
