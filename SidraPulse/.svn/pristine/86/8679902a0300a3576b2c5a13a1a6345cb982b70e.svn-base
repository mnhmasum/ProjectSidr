package com.atomix.sidrapulse;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atomix.adapter.ForumsAdapter;
import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.notification.ScreenReceiver;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.utils.Utilities;

public class ForumHashTagDetails extends Activity implements
		OnItemClickListener, OnClickListener {

	private Button btnBack;
	private TextView txtViewHashtagName;
	private Button btnStartDiscussion;
	private ProgressDialog progressDialog;
	private int forumsListStatus;
	private ListView lstViewForums;
	private ForumsAdapter forumAdapter;
	public static ArrayList<String> messagesList = new ArrayList<String>();
	private String tagName = "";
	private boolean isFirstTime = true;
	private ScreenReceiver mReceiver = null;
	
	private RelativeLayout relativeBtnRow;
	private TextView txtViewLatestPost;
	private TextView txtViewTopConversation;
	private TextView txtViewNoData;
	
	private int postType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forum_hash_tag_details);
		ConstantValues.isHashTagActivityRunning = true;
		// intiScreenReceiver();
		initViews();
		setListener();
		loadData();
	}

	private void loadData() {
		if (getIntent().getData() != null) {
			Log.e("in if", "-----");
			Uri uri = getIntent().getData();
			String tag = uri.toString().split("/")[3];
			tagName = tag.split("#")[1];
			Log.i("Total Tag", "-----" + uri.toString());
			callForumsListApi();
			
		} else if (getIntent().getExtras() != null) {
			Log.e("I am in else if with tag name", "----" + tagName);
			tagName = getIntent().getExtras().getString("HASH_TAG");
			callForumsListApi();
			
		} else {
			Log.e("in else else else", "-----" + tagName);
		}

		txtViewHashtagName.setText("#" + tagName);
		txtViewHashtagName.setVisibility(View.VISIBLE);
		Log.e("tag touched", "-----" + tagName);
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		btnStartDiscussion.setOnClickListener(this);
		lstViewForums.setOnItemClickListener(this);
		
		txtViewLatestPost.setOnClickListener(this);
		txtViewTopConversation.setOnClickListener(this);
	}

	private void initViews() {
		txtViewNoData  = (TextView) findViewById(R.id.txt_view_no_data);
		tagName = "";
		postType = 4;
		btnBack = (Button) findViewById(R.id.btn_back);
		txtViewHashtagName = (TextView) findViewById(R.id.txt_view_hash_tag_name);
		btnStartDiscussion = (Button) findViewById(R.id.btn_start_discussion);
		lstViewForums = (ListView) findViewById(R.id.lst_view_forums);
		
		relativeBtnRow = (RelativeLayout) findViewById(R.id.relative_layout_btn);
		txtViewLatestPost = (TextView) findViewById(R.id.txt_view_lastest);
		txtViewTopConversation = (TextView) findViewById(R.id.txt_view_top);
		txtViewTopConversation.setTextColor(getResources().getColor(R.color.light_gray));

	}

	private void intiScreenReceiver() {
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mReceiver = new ScreenReceiver();
		registerReceiver(mReceiver, filter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(ForumHashTagDetails.this,
				ForumsDetailsActivity.class);
		intent.putExtra("click_position", position);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;

		case R.id.btn_start_discussion:
			Intent intent = new Intent(ForumHashTagDetails.this,
					ForumNewPostActivity.class);
			intent.putExtra("HASH_TAG", "#" + tagName);
			startActivity(intent);
			break;
			
		case R.id.txt_view_lastest:
			postType = 4;
			txtViewLatestPost.setTextColor(getResources().getColor(R.color.blue));
			txtViewTopConversation.setTextColor(getResources().getColor(R.color.light_gray));
			callForumsListApi();
			break;
			
		case R.id.txt_view_top:
			postType = 3;
			txtViewTopConversation.setTextColor(getResources().getColor(R.color.blue));
			txtViewLatestPost.setTextColor(getResources().getColor(R.color.light_gray));
			callForumsListApi();
			break;

		default:
			break;
		}

	}

	private void callForumsListApi() {
		if (InternetConnectivity.isConnectedToInternet(ForumHashTagDetails.this)) {
			new ForumsListApiTask(tagName).execute();
		} else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(
					ForumHashTagDetails.this);
		}
	}

	private class ForumsListApiTask extends AsyncTask<Void, Void, Void> {
		String tagName;

		private ForumsListApiTask(String tagName) {
			this.tagName = tagName;
		}

		@Override
		protected void onPreExecute() {
			progressDialog = SidraCustomProgressDialog.creator(ForumHashTagDetails.this);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				forumsListStatus = CommunicationLayer.getAPIShowForumList(ConstantValues.FUNC_ID_API_SHOW_FORUM_LIST, Integer.toString(SidraPulseApp.getInstance() .getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(postType), tagName, "","");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (forumsListStatus == 1) {
				if (SidraPulseApp.getInstance().getForumsInfoList() != null) {
					forumAdapter = new ForumsAdapter(ForumHashTagDetails.this,ForumHashTagDetails.this, SidraPulseApp.getInstance().getForumsInfoList());
					lstViewForums.setVisibility(View.VISIBLE);
					txtViewNoData.setVisibility(View.GONE);
					lstViewForums.setAdapter(forumAdapter);
					isFirstTime = false;
				} else {
					lstViewForums.setAdapter(null);
					lstViewForums.setVisibility(View.GONE);
					txtViewNoData.setVisibility(View.VISIBLE);
				}

			} else if(forumsListStatus == 5) {
				SidraPulseApp.getInstance().accessTokenChange(ForumHashTagDetails.this);
				
			}  else {
				lstViewForums.setAdapter(null);
				lstViewForums.setVisibility(View.GONE);
				txtViewNoData.setVisibility(View.VISIBLE);
			}

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isFirstTime) {
			forumAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
