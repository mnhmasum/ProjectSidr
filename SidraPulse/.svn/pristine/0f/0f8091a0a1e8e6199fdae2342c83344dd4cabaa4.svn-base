package com.atomix.sidrapulse;

import com.atomix.customview.SidraTextView;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class PolicyDetailsActivity extends Activity implements OnClickListener, OnCheckedChangeListener {

	private Button btnBack;
	private Button btnMenu;
	private TextView txtViewPolicyNumber;
	private SidraTextView txtViewPolicyTitle;
	private RadioButton radioBtnOverView;
	private RadioButton radioBtnPoliciStatement;
	private RadioButton radioBtnDefinition;
	private RadioButton radioBtnReference;
	private SidraTextView txtViewContent;
	private ScrollView scrollViewBody;
	private int position = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policy_details);
		
		initViews();
		setListener();
		loadData();
	}

	private void initViews() {
		btnBack = (Button) findViewById(R.id.btn_back);
		btnMenu = (Button) findViewById(R.id.btn_menu);
		txtViewPolicyNumber = (TextView) findViewById(R.id.txt_view_policy_number);
		
		radioBtnOverView = (RadioButton) findViewById(R.id.radio_btn_overview);
		radioBtnPoliciStatement = (RadioButton) findViewById(R.id.radio_btn_policy_statement);
		radioBtnDefinition = (RadioButton) findViewById(R.id.radio_btn_definition);
		radioBtnReference = (RadioButton) findViewById(R.id.radio_btn_reference);

		txtViewContent = (SidraTextView) findViewById(R.id.txt_view_content);
		txtViewPolicyTitle = (SidraTextView) findViewById(R.id.txt_view_policy_title);
		scrollViewBody = (ScrollView) findViewById(R.id.scrollView1);
		
		
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
		((RadioGroup) findViewById(R.id.radio_group)).setOnCheckedChangeListener(this);

	}

	private void loadData() {
		if(getIntent().getExtras() != null) {
			position = getIntent().getExtras().getInt("position");
			txtViewPolicyNumber.setText("Policy Number : "+SidraPulseApp.getInstance().getPoliceisInfoList().get(position).getPolicyNo());
			txtViewContent.setText(SidraPulseApp.getInstance().getPoliceisInfoList().get(position).getOverview());
			txtViewPolicyTitle.setText(SidraPulseApp.getInstance().getPoliceisInfoList().get(position).getTitle());
			Linkify.addLinks(txtViewPolicyTitle, Linkify.ALL);
			Linkify.addLinks(txtViewContent, Linkify.ALL);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
		
		case R.id.btn_menu:
			startActivity(new Intent(PolicyDetailsActivity.this, MainMenuActivity.class));
			finish();
			break;
			
		default:
			break;
		}
	}

	private void loadContent(int i) {
		switch (i) {
		case 0:
			txtViewContent.setText(SidraPulseApp.getInstance().getPoliceisInfoList().get(position).getOverview());
			break;
		case 1:
			txtViewContent.setText(SidraPulseApp.getInstance().getPoliceisInfoList().get(position).getPolicyStatement());
			break;
			
		case 2:
			txtViewContent.setText(SidraPulseApp.getInstance().getPoliceisInfoList().get(position).getDefinitions());
			break;
			
		case 3:
			txtViewContent.setText(SidraPulseApp.getInstance().getPoliceisInfoList().get(position).getReference());
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switchTab();
	}

	private void switchTab() {
			int selectedTab = ((RadioGroup) findViewById(R.id.radio_group)).getCheckedRadioButtonId();
		
		switch (selectedTab) {
			case R.id.radio_btn_overview:
				radioBtnOverView.setButtonDrawable(R.drawable.overview_b);
				radioBtnPoliciStatement.setButtonDrawable(R.drawable.policy_w);
				radioBtnDefinition.setButtonDrawable(R.drawable.definitions_w);
				radioBtnReference.setButtonDrawable(R.drawable.reference_w);
				scrollViewBody.scrollTo(0, 0);
				loadContent(0);
				break;
				
			case R.id.radio_btn_policy_statement:
				radioBtnOverView.setButtonDrawable(R.drawable.overview_w);
				radioBtnPoliciStatement.setButtonDrawable(R.drawable.policy_b);
				radioBtnDefinition.setButtonDrawable(R.drawable.definitions_w);
				radioBtnReference.setButtonDrawable(R.drawable.reference_w);
				scrollViewBody.scrollTo(0, 0);
				loadContent(1);
				break;
				
			case R.id.radio_btn_definition:
				radioBtnOverView.setButtonDrawable(R.drawable.overview_w);
				radioBtnPoliciStatement.setButtonDrawable(R.drawable.policy_w);
				radioBtnDefinition.setButtonDrawable(R.drawable.definitions_b);
				radioBtnReference.setButtonDrawable(R.drawable.reference_w);
				scrollViewBody.scrollTo(0, 0);
				loadContent(2);
				break;
				
			case R.id.radio_btn_reference:
				radioBtnOverView.setButtonDrawable(R.drawable.overview_w);
				radioBtnPoliciStatement.setButtonDrawable(R.drawable.policy_w);
				radioBtnDefinition.setButtonDrawable(R.drawable.definitions_w);
				radioBtnReference.setButtonDrawable(R.drawable.reference_b);
				scrollViewBody.scrollTo(0, 0);
				/*scrollViewBody.post(new Runnable() {

				    public void run() {
				    	scrollViewBody.scrollTo(0, );
				    }
				}); */
				loadContent(3);
				break;
	
			default:
				break;
		}
	}

}
