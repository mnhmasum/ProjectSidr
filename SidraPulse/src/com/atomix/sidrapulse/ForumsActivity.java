package com.atomix.sidrapulse;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atomix.adapter.ForumHotTopicsAdapter;
import com.atomix.adapter.ForumsAdapter;
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

public class ForumsActivity extends Activity implements OnClickListener, OnItemClickListener {

	private Button btnBack;
	private Button btnAllPost;
	private Button btnTopics;
	
	private LinearLayout linearBtnRow;
	private TextView txtViewLatestPost;
	private TextView txtViewTopConversation;
	
	private Button btnMyPost;
	private Button btnStartDiscussion;
	private ProgressDialog progressDialog;
	private int forumsListStatus;
	private PullToRefreshListView lstViewForums;
	private ForumsAdapter forumAdapter = null;
	private ForumHotTopicsAdapter topicsAdapter = null;
	private boolean isTopic = false;
	public static ArrayList<String> messagesList=new ArrayList<String>();
	private String tagName;
	private TextView txtViewSubtitle;
	private TextView txtViewNotification;
	private TextView txtViewNoData;
	private LinearLayout linearNotification;
	private int pageNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forums);
		
		initViews();
		setListener();
		loadData();
	}

	private void initViews() {
		tagName = "";
		isTopic = false;
		txtViewSubtitle = (TextView)findViewById(R.id.txt_view_subtitle);
		btnBack = (Button) findViewById(R.id.btn_back);
		
		btnAllPost = (Button) findViewById(R.id.btn_all_post);
		btnTopics = (Button) findViewById(R.id.btn_topics);
		btnMyPost = (Button) findViewById(R.id.btn_my_post);
		linearBtnRow = (LinearLayout) findViewById(R.id.relative_layout_btn);
		txtViewLatestPost = (TextView) findViewById(R.id.txt_view_lastest);
		txtViewTopConversation = (TextView) findViewById(R.id.txt_view_top);
		txtViewNoData  = (TextView) findViewById(R.id.txt_view_no_data);
		
		linearNotification = (LinearLayout) findViewById(R.id.linear_notification);
		txtViewNotification = (TextView) findViewById(R.id.txt_view_notification);
		
		if(SidraPulseApp.getInstance().getNotificationInfo().getForum()> 0){
			linearNotification.setVisibility(View.VISIBLE);
			if((SidraPulseApp.getInstance().getNotificationInfo().getForum()) >= 99) {
				txtViewNotification.setText("99");
			} else {
				txtViewNotification.setText(Integer.toString(SidraPulseApp.getInstance().getNotificationInfo().getForum()));
			}
		} else{
			linearNotification.setVisibility(View.GONE);
		}
		
		SpannableString content = new SpannableString(txtViewLatestPost.getText().toString());
		content.setSpan(new UnderlineSpan(), 0,txtViewLatestPost.getText().toString().length(), 0);
		txtViewLatestPost.setTextColor(getResources().getColor(R.color.light_gray));
		txtViewLatestPost.setText(content);
		
		SpannableString content2 = new SpannableString(txtViewTopConversation.getText().toString());
		content2.setSpan(new UnderlineSpan(), 0,txtViewTopConversation.getText().toString().length(), 0);
		txtViewTopConversation.setTextColor(getResources().getColor(R.color.light_gray));
		txtViewTopConversation.setText(content2);
		
		btnStartDiscussion = (Button) findViewById(R.id.btn_start_discussion);
		lstViewForums = (PullToRefreshListView) findViewById(R.id.lst_view_forums);
		lstViewForums.setMode(Mode.BOTH);
		
		txtViewLatestPost.setTextColor(getResources().getColor(R.color.blue));
		txtViewTopConversation.setTextColor(getResources().getColor(R.color.light_gray));
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		btnStartDiscussion.setOnClickListener(this);
		lstViewForums.setOnItemClickListener(this);
		btnAllPost.setOnClickListener(this);
		btnTopics.setOnClickListener(this);
		btnMyPost.setOnClickListener(this);
		txtViewLatestPost.setOnClickListener(this);
		txtViewTopConversation.setOnClickListener(this);
		
		lstViewForums.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				
				if (!InternetConnectivity.isConnectedToInternet(ForumsActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(ForumsActivity.this);
					lstViewForums.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getForumsInfoList() == null) {
					lstViewForums.onRefreshComplete();
					return;
				}
				
				String last_element_id = Integer.toString(SidraPulseApp.getInstance().getForumsInfoList().get(0).getId());
				new ForumsListUpdateTask(last_element_id, "0").execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if (!InternetConnectivity.isConnectedToInternet(ForumsActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(ForumsActivity.this);
					lstViewForums.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getForumsInfoList() == null) {
					lstViewForums.onRefreshComplete();
					return;
				}
				
				/*if (SidraPulseApp.getInstance().getForumsInfoList().size() >= pageNo*10) {
					pageNo++;
					new ForumsListUpdateTask().execute();
				} else {
					new ForumsListUpdateTask().execute();
				}*/
				
				String last_element_id = Integer.toString(SidraPulseApp.getInstance().getForumsInfoList().get(SidraPulseApp.getInstance().getForumsInfoList().size() - 1).getId());
				new ForumsListUpdateTask(last_element_id, "1").execute();
				
			}

		});
	}

	private void loadData() {
		ConstantValues.PullDownActive = true;
		
		if(getIntent().getExtras() != null) {
			if(getIntent().getExtras().getBoolean("is_my_post")) {
				forumAdapter = new ForumsAdapter(ForumsActivity.this, ForumsActivity.this, SidraPulseApp.getInstance().getForumsInfoList());
				btnMyPost.performClick();
			} else {
				
			}
		} else {
			forumAdapter = new ForumsAdapter(ForumsActivity.this, ForumsActivity.this, SidraPulseApp.getInstance().getForumsInfoList());
			ConstantValues.FORUM_TAB_SELECTED_INDEX = 4;
			 if (InternetConnectivity.isConnectedToInternet(ForumsActivity.this)) {
		    	 new ForumsListApiTask(tagName).execute();
			 } else {
				SidraPulseApp.getInstance().openDialogForInternetChecking(ForumsActivity.this);
			 }
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
			
		case R.id.btn_start_discussion:
			 startActivity(new Intent(ForumsActivity.this, ForumNewPostActivity.class));
			break;
			
		case R.id.btn_all_post:
			ConstantValues.PullDownActive = true;
			ConstantValues.FORUM_TAB_SELECTED_INDEX = 4;
			txtViewLatestPost.setTextColor(getResources().getColor(R.color.blue));
			txtViewTopConversation.setTextColor(getResources().getColor(R.color.light_gray));
			
			ConstantValues.isShowingHotTopic = false;
			isTopic = false;
			
			txtViewSubtitle.setText("Listing All Posts");
			linearBtnRow.setVisibility(View.VISIBLE);
	
			btnAllPost.setBackgroundResource(R.drawable.all_posts_b);
			btnTopics.setBackgroundResource(R.drawable.topics_w);
			btnMyPost.setBackgroundResource(R.drawable.my_posts_w);
			callForumsListApi();
			break;
			
		case R.id.btn_topics:
			isTopic = true;
			ConstantValues.PullDownActive = true;
			ConstantValues.FORUM_TAB_SELECTED_INDEX = 2;
			ConstantValues.isShowingHotTopic = true;
			
			txtViewSubtitle.setText("Common Conversation Topics");
			linearBtnRow.setVisibility(View.GONE);
			
			btnAllPost.setBackgroundResource(R.drawable.all_posts_w);
			btnTopics.setBackgroundResource(R.drawable.topics_b);
			btnMyPost.setBackgroundResource(R.drawable.my_posts_w);
			callForumsListApi();
			break;
			
		case R.id.btn_my_post:
			ConstantValues.PullDownActive = true;
			ConstantValues.FORUM_TAB_SELECTED_INDEX = 0;
			ConstantValues.isShowingHotTopic = false;
			isTopic = false;
			
			txtViewSubtitle.setText("Listing My Posts");
			linearBtnRow.setVisibility(View.GONE);
			
			btnAllPost.setBackgroundResource(R.drawable.all_posts_w);
			btnTopics.setBackgroundResource(R.drawable.topics_w);
			btnMyPost.setBackgroundResource(R.drawable.my_posts_b);
			if(SidraPulseApp.getInstance().getNotificationInfo().getForum()> 0){
					if (InternetConnectivity.isConnectedToInternet(ForumsActivity.this)) {
						new UnReadTask(ForumsActivity.this, new UnReadRequest() {
							@Override
							public void onTaskCompleted(int status) {
								if(status == 1){
									SidraPulseApp.getInstance().getNotificationInfo().setForum(0);
									linearNotification.setVisibility(View.GONE);
									callForumsListApi();
								}else{
									Utilities.showToast(ForumsActivity.this, ConstantValues.failureMessage);
								}
							}
						}, 6, 0, true).execute();
					} else {
						SidraPulseApp.getInstance().openDialogForInternetChecking(ForumsActivity.this);
					}
			}else{
				callForumsListApi();
			}
			
			break;
			
		case R.id.txt_view_lastest:
			ConstantValues.FORUM_TAB_SELECTED_INDEX = 4;
			txtViewLatestPost.setTextColor(getResources().getColor(R.color.blue));
			txtViewTopConversation.setTextColor(getResources().getColor(R.color.light_gray));
			callForumsListApi();
			break;
			
		case R.id.txt_view_top:
			ConstantValues.FORUM_TAB_SELECTED_INDEX = 3;
			txtViewTopConversation.setTextColor(getResources().getColor(R.color.blue));
			txtViewLatestPost.setTextColor(getResources().getColor(R.color.light_gray));
			callForumsListApi();
			break;

		default:
			break;
		}
	}
	
	
	private void callForumsListApi() {
		if (InternetConnectivity.isConnectedToInternet(ForumsActivity.this)) {
			new ForumsListApiTask("").execute();
		}else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(ForumsActivity.this);
		}
	}


	private class ForumsListApiTask extends AsyncTask<Void, Void, Void> {
		String tagName;
		 
		private ForumsListApiTask (String tagName) {
			 this.tagName = tagName;
		 }
		 
		@Override
		protected void onPreExecute() {
			txtViewNoData.setVisibility(View.GONE);
			progressDialog = SidraCustomProgressDialog.creator(ForumsActivity.this);
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				if(!isTopic) {
					forumsListStatus = CommunicationLayer.getAPIShowForumList(ConstantValues.FUNC_ID_API_SHOW_FORUM_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(ConstantValues.FORUM_TAB_SELECTED_INDEX), "", "", "");
				} else {
					forumsListStatus = CommunicationLayer.getAPIShowForumTopics(ConstantValues.FUNC_ID_API_SHOW_FORUM_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "2", "", "", "");
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
			
			if (forumsListStatus == 1) {
				if(!isTopic) {
					if(SidraPulseApp.getInstance().getForumsInfoList() != null) {
						if (SidraPulseApp.getInstance().getForumsInfoList().size() !=0) {
							txtViewNoData.setVisibility(View.GONE);
							forumAdapter = new ForumsAdapter(ForumsActivity.this, ForumsActivity.this, SidraPulseApp.getInstance().getForumsInfoList());
							lstViewForums.setAdapter(forumAdapter);
							
						} else {
							lstViewForums.setAdapter(null);
							txtViewNoData.setVisibility(View.VISIBLE);
							txtViewNoData.setText(ConstantValues.NO_DATA_FOUND_MESSAGE);
						}
						
					} else {
						lstViewForums.setAdapter(null);
						txtViewNoData.setVisibility(View.VISIBLE);
						txtViewNoData.setText(ConstantValues.NO_DATA_FOUND_MESSAGE);
						
					}
					
				} else {
					if(SidraPulseApp.getInstance().getHotTopicsList() != null) {
						if (SidraPulseApp.getInstance().getHotTopicsList().size() !=0) {
							txtViewNoData.setVisibility(View.GONE);
							topicsAdapter = new ForumHotTopicsAdapter(ForumsActivity.this, ForumsActivity.this, SidraPulseApp.getInstance().getHotTopicsList());
							lstViewForums.setAdapter(topicsAdapter);
							
						} else {
							lstViewForums.setAdapter(null);
							txtViewNoData.setVisibility(View.VISIBLE);
							txtViewNoData.setText(ConstantValues.NO_DATA_FOUND_MESSAGE);
						}
						
					} else {
						lstViewForums.setAdapter(null);
						txtViewNoData.setVisibility(View.VISIBLE);
						txtViewNoData.setText(ConstantValues.NO_DATA_FOUND_MESSAGE);
					}
				}
				
			} else if(forumsListStatus == 5) {
				txtViewNoData.setVisibility(View.GONE);
				SidraPulseApp.getInstance().accessTokenChange(ForumsActivity.this);
			} else {
				lstViewForums.setAdapter(null);
				txtViewNoData.setVisibility(View.VISIBLE);
				txtViewNoData.setText(ConstantValues.NO_DATA_FOUND_MESSAGE);
			}
			
		}
	}
	
	ProgressDialog progress;
	
	private class ForumsListUpdateTask extends AsyncTask<Void, Void, Void> {
		
		private String last_element_id = null;
		private String direction = null;
		
		public ForumsListUpdateTask(String last_element_id, String direction) {
			this.last_element_id = last_element_id;
			this.direction = direction;
			
		}
		
		@Override
		protected void onPreExecute() {
			progress = SidraCustomProgressDialog.creator(ForumsActivity.this);
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				
				if (this.direction.equals("0")) {
					if(!isTopic) {
						forumsListStatus = CommunicationLayer.getAPIShowForumList(ConstantValues.FUNC_ID_API_SHOW_FORUM_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(ConstantValues.FORUM_TAB_SELECTED_INDEX), "", this.last_element_id, this.direction);
					} else {
						forumsListStatus = CommunicationLayer.getAPIShowForumTopics(ConstantValues.FUNC_ID_API_SHOW_FORUM_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "2", "", this.last_element_id, this.direction);
					}
					
				} else if (ConstantValues.PullDownActive) {
					if(!isTopic) {
						forumsListStatus = CommunicationLayer.getAPIShowForumList(ConstantValues.FUNC_ID_API_SHOW_FORUM_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(ConstantValues.FORUM_TAB_SELECTED_INDEX), "", this.last_element_id, this.direction);
					} else {
						forumsListStatus = CommunicationLayer.getAPIShowForumTopics(ConstantValues.FUNC_ID_API_SHOW_FORUM_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "2", "", this.last_element_id, this.direction);
					}
					
				}
				
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(progress.isShowing()) {
				progress.dismiss();
			}
			
			Log.i("Update List Response", "S : 	" + forumsListStatus);
			
			if (forumsListStatus == 1) {
				if(!isTopic) {
					if(SidraPulseApp.getInstance().getForumsInfoList() != null) {
						forumAdapter.notifyDataSetChanged();
						lstViewForums.onRefreshComplete();
					} 
					
				} else {
					if(SidraPulseApp.getInstance().getHotTopicsList() != null) {
						topicsAdapter.notifyDataSetChanged();
					} 
				}
				
			} else if(forumsListStatus == 5) {
				SidraPulseApp.getInstance().accessTokenChange(ForumsActivity.this);
				
			}  else {
				//lstViewForums.setAdapter(null);
				lstViewForums.onRefreshComplete();
			}
			
		}
	}
 
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(!isTopic) {
			ConstantValues.isForumDetailClicked = true;
			//ConstantValues.PullDownActive = true;
			Intent intent = new Intent(ForumsActivity.this, ForumsDetailsActivity.class);
			intent.putExtra("click_position", position - 1);
			startActivity(intent);
		} else {
			isTopic = true;
			ConstantValues.PullDownActive = true;
			ConstantValues.FORUM_TAB_SELECTED_INDEX = 4;
			tagName = SidraPulseApp.getInstance().getHotTopicsList().get(position-1).getTagName();
			
			btnAllPost.setBackgroundResource(R.drawable.all_posts_w);
			btnTopics.setBackgroundResource(R.drawable.topics_b);
			btnMyPost.setBackgroundResource(R.drawable.my_posts_w);

			Intent intent = new Intent(ForumsActivity.this, ForumHashTagDetails.class);
			intent.putExtra("HASH_TAG", tagName);
			startActivity(intent);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
	}

	@Override
	protected void onResume() {
		if(forumAdapter!=null){
			forumAdapter.notifyDataSetChanged();
			//callForumsListApi();
		}
		super.onResume();
		
		if (ConstantValues.isHashTagActivityRunning) {
			ConstantValues.isHashTagActivityRunning = false;
			Intent intent = getIntent();
			finish();
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(getIntent());
			
		} else {
			
		}
		
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		//startActivity(getIntent());
	}
	
}
