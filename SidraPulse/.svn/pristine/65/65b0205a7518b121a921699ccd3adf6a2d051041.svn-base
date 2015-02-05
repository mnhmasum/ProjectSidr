package com.atomix.sidrapulse;

import java.util.ArrayList;

import android.app.ExpandableListActivity;
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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atomix.adapter.StaffDirectoryAdapter;
import com.atomix.adapter.StaffDirectoryItemExpandableAdapter;
import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.datamodel.StaffListInfo;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.sidrapulse.staffdirectory.StaffDirectoryItemActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

public class StaffDirectoryActivity extends ExpandableListActivity implements OnItemClickListener, OnClickListener, OnCheckedChangeListener, OnGroupExpandListener {

	private Button btnBack;
	private TextView txtViewEmptySavedContact;
	private RadioButton radioBtnBrowse;
	private RadioButton radioBtnSavedContacts;
	private ListView lstViewStaffDirectory;
	private PullToRefreshExpandableListView exLstViewStaffDirectory;
	private Button btnSearch;
	private Button edTxtSearchItem;
	private ProgressDialog progressDialog;
	private int departmentsStatus;
	private RelativeLayout relativeMain;
	private int lastExpandedPosition = -1;
	private TextView txtViewSubTititle;
	
	private ArrayList<StaffListInfo> parentItems;
	private ArrayList<StaffListInfo> childItems;
	private boolean isExpandable;
	private StaffDirectoryItemExpandableAdapter staffDirItemAdapter;
	private int pageNo = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_staff_directory);
		
		initViews();
		setListener();
		loadData();
	}

	private void initViews() {
		txtViewSubTititle = (TextView) findViewById(R.id.txt_view_subtitle);
		txtViewSubTititle.setText("Contacts you bookmarked");
		txtViewSubTititle.setVisibility(View.GONE);
		relativeMain = (RelativeLayout) findViewById(R.id.relative_main);
		btnBack = (Button) findViewById(R.id.btn_back);
		radioBtnBrowse = (RadioButton) findViewById(R.id.radio_btn_browse);
		radioBtnSavedContacts = (RadioButton) findViewById(R.id.radio_btn_saved_contacts);
		lstViewStaffDirectory = (ListView) findViewById(R.id.lst_view_staff_directory);
		//lstViewStaffDirectory.setMode(Mode.PULL_FROM_END);
		txtViewEmptySavedContact = (TextView) findViewById(R.id.txt_view_empty_saved_contact);
		txtViewEmptySavedContact.setVisibility(View.INVISIBLE);
		btnSearch = (Button) findViewById(R.id.btn_search);
		edTxtSearchItem = (Button) findViewById(R.id.ed_txt_search_all_departments);
		exLstViewStaffDirectory = (PullToRefreshExpandableListView) findViewById(R.id.ex_lst_view_staff_directory);
		exLstViewStaffDirectory.setShowIndicator(false);
		getExpandableListView().setGroupIndicator(null);
		exLstViewStaffDirectory.setVisibility(View.GONE);
		//exLstViewStaffDirectory.getShowIndicator()
	}

	private void setListener() {
		relativeMain.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		lstViewStaffDirectory.setOnItemClickListener(this);
		getExpandableListView().setOnGroupExpandListener(this);
		edTxtSearchItem.setOnClickListener(this);
		((RadioGroup) findViewById(R.id.radio_group)).setOnCheckedChangeListener(this);
		
		exLstViewStaffDirectory.setOnRefreshListener(new OnRefreshListener2 <ExpandableListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
				//Code for pull to up refresh
				if (!InternetConnectivity.isConnectedToInternet(StaffDirectoryActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(StaffDirectoryActivity.this);
					//adapter.notifyDataSetChanged();
					exLstViewStaffDirectory.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getStaffListInfoList() == null) {
					exLstViewStaffDirectory.onRefreshComplete();
					return;
				}
			
				new GetDataTask("0").execute();
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
				
				if (!InternetConnectivity.isConnectedToInternet(StaffDirectoryActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(StaffDirectoryActivity.this);
					//adapter.notifyDataSetChanged();
					exLstViewStaffDirectory.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getStaffListInfoList() == null) {
					exLstViewStaffDirectory.onRefreshComplete();
					return;
				}
				
				new GetDataTask("1").execute();
				
			}

		});
		
	}

	private void loadData() {
		if (InternetConnectivity.isConnectedToInternet(StaffDirectoryActivity.this)) {
			//staffDirItemAdapter  = new StaffDirectoryItemExpandableAdapter(getApplicationContext(), StaffDirectoryActivity.this, parentItems, null, childItems);
			new StaffDirectoryDepartmentsApiTask().execute();
		} else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(StaffDirectoryActivity.this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
			
		case R.id.btn_search:
			//if(edTxtSearchItem.getText().toString().trim().length() > 0) {
				Intent intent = new Intent(StaffDirectoryActivity.this, StaffDirectorySearchActivity.class);
				intent.putExtra("keyword", edTxtSearchItem.getText().toString().trim());
				intent.putExtra("is_all", true);
				intent.putExtra("position", 0);
				intent.putExtra("isMainPage", false);
				startActivity(intent);
			//}
			break;
			
		case R.id.ed_txt_search_all_departments:
			//if(edTxtSearchItem.getText().toString().trim().length() > 0) {
				Intent intent1 = new Intent(StaffDirectoryActivity.this, StaffDirectorySearchActivity.class);
				intent1.putExtra("keyword", edTxtSearchItem.getText().toString().trim());
				intent1.putExtra("is_all", true);
				intent1.putExtra("position", 0);
				intent1.putExtra("isMainPage", false);
				startActivity(intent1);
			//}
			break;
			
		case R.id.relative_main:
			SidraPulseApp.getInstance().hideKeyboard(getApplicationContext(), v);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent intent = new Intent(StaffDirectoryActivity.this, StaffDirectoryItemActivity.class);
		intent.putExtra("position", position);
		startActivity(intent);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switchTab();
	}
	
	private void switchTab() {
		int selectedTab = ((RadioGroup) findViewById(R.id.radio_group)).getCheckedRadioButtonId();
		
		switch (selectedTab) {
			case R.id.radio_btn_browse:
				isExpandable = false;
				txtViewSubTititle.setVisibility(View.GONE);
				txtViewEmptySavedContact.setVisibility(View.INVISIBLE);
				lstViewStaffDirectory.setVisibility(View.GONE);
				exLstViewStaffDirectory.setVisibility(View.GONE);
				radioBtnBrowse.setButtonDrawable(R.drawable.browse_btn_on);
				radioBtnSavedContacts.setButtonDrawable(R.drawable.saved_btn_off);
				new StaffDirectoryDepartmentsApiTask().execute();
				break;
				
			case R.id.radio_btn_saved_contacts:
				isExpandable = true;
				txtViewSubTititle.setVisibility(View.VISIBLE);
				txtViewEmptySavedContact.setVisibility(View.INVISIBLE);
				lstViewStaffDirectory.setVisibility(View.GONE);
				exLstViewStaffDirectory.setVisibility(View.GONE);
				radioBtnBrowse.setButtonDrawable(R.drawable.browse_btn_off);
				radioBtnSavedContacts.setButtonDrawable(R.drawable.saved_btn_on);
				new StaffDirectoryDepartmentsApiTask().execute();
				break;
	
			default:
				break;
		}
	}
	
	private class StaffDirectoryDepartmentsApiTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			progressDialog = SidraCustomProgressDialog.creator(StaffDirectoryActivity.this);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				if(isExpandable) {
					departmentsStatus = CommunicationLayer.getStaffDirectoryStaffListData(ConstantValues.FUNC_ID_STAFF_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "0", "", "");
				} else {
					departmentsStatus = CommunicationLayer.getStaffDirectoryDepartmentsData(ConstantValues.FUNC_ID_STAFF_DIRECTORY_DEPARTMENTS, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken());
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
			
			Log.e("status is", "----"+ departmentsStatus);
			if(departmentsStatus == 1) {
				
				if(isExpandable) {
					if(SidraPulseApp.getInstance().getStaffListInfoList() != null) {
						parentItems = new ArrayList<StaffListInfo>();
						childItems = new ArrayList<StaffListInfo>();
						
						for(int i = 0; i < SidraPulseApp.getInstance().getStaffListInfoList().size(); i++) {
							StaffListInfo info = new StaffListInfo();
							info.setName(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getName());
							info.setDesignation(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getDesignation());
							info.setDepartment(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getDepartment());
							info.setIsBookmarked(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getIsBookmarked());
							info.setId(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getId());
							parentItems.add(info);
							
							StaffListInfo info1 = new StaffListInfo();
							info1.setEmail(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getEmail());
							info1.setOffice(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getOffice());
							info1.setMobile(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getMobile());
							childItems.add(info1);
						}
						
						exLstViewStaffDirectory.setVisibility(View.VISIBLE);
						staffDirItemAdapter  = new StaffDirectoryItemExpandableAdapter(getApplicationContext(), StaffDirectoryActivity.this, parentItems, null, childItems, true, txtViewEmptySavedContact, exLstViewStaffDirectory);
						setListAdapter(staffDirItemAdapter);
						//exLstViewStaffDirectory.setAdapter(adapter);
						//exLstViewStaffDirectory.setIndicatorBounds(0, 0);
						Log.e("i am in if", "----");
					} else {
						Log.e("i am in else", "----");
						txtViewEmptySavedContact.setVisibility(View.VISIBLE);
					}
				} else {
					lstViewStaffDirectory.setVisibility(View.VISIBLE);
					if(SidraPulseApp.getInstance().getDirectoryDepartmentsInfoList() != null) {
						lstViewStaffDirectory.setAdapter(new StaffDirectoryAdapter(getApplicationContext(), SidraPulseApp.getInstance().getDirectoryDepartmentsInfoList()));
					} else {
						lstViewStaffDirectory.setAdapter(null);
					}
				}
				
			} else if(departmentsStatus == 5) {
				SidraPulseApp.getInstance().accessTokenChange(StaffDirectoryActivity.this);
				
			} else {
				if(isExpandable) {
					txtViewEmptySavedContact.setVisibility(View.VISIBLE);
				} else {
					lstViewStaffDirectory.setAdapter(null);
				}
			}
		}
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
			getExpandableListView().collapseGroup(lastExpandedPosition);
		}
		lastExpandedPosition = groupPosition;
	}
	
//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		finish();
//		startActivity(getIntent());
//	}
	
	private class GetDataTask extends AsyncTask<String, Void, String[]> {
		
		private String direction = null;
		
		public GetDataTask(String direction) {
			this.direction = direction; 
		}
		
		@Override
		protected String[] doInBackground(String... params) {
			
			try {
				if(isExpandable) {
					if (this.direction.equals("0")) {
						String element_id = Integer.toString(SidraPulseApp.getInstance().getStaffListInfoList().get(0).getId());
						departmentsStatus = CommunicationLayer.getStaffDirectoryStaffListData(ConstantValues.FUNC_ID_STAFF_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "0", element_id, "0");
					} else if (ConstantValues.PullDownActive) {
						String element_id = Integer.toString(SidraPulseApp.getInstance().getStaffListInfoList().get(SidraPulseApp.getInstance().getStaffListInfoList().size()-1).getId());
						departmentsStatus = CommunicationLayer.getStaffDirectoryStaffListData(ConstantValues.FUNC_ID_STAFF_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "0", element_id, "1");
					}
					
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			
			if(SidraPulseApp.getInstance().getStaffListInfoList() == null) {
				exLstViewStaffDirectory.onRefreshComplete();
				return;
			}
			if (departmentsStatus == 1) {
				if(isExpandable) {
					
					if(SidraPulseApp.getInstance().getStaffListInfoList() != null) {
						parentItems = new ArrayList<StaffListInfo>();
						childItems = new ArrayList<StaffListInfo>();
						
						for(int i = 0; i < SidraPulseApp.getInstance().getStaffListInfoList().size(); i++) {
							StaffListInfo info = new StaffListInfo();
							info.setName(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getName());
							info.setDesignation(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getDesignation());
							info.setDepartment(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getDepartment());
							info.setIsBookmarked(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getIsBookmarked());
							info.setId(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getId());
							parentItems.add(info);
							
							StaffListInfo info1 = new StaffListInfo();
							info1.setEmail(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getEmail());
							info1.setOffice(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getOffice());
							info1.setMobile(SidraPulseApp.getInstance().getStaffListInfoList().get(i).getMobile());
							childItems.add(info1);
						}
						
						
						staffDirItemAdapter  = new StaffDirectoryItemExpandableAdapter(getApplicationContext(), StaffDirectoryActivity.this, parentItems, null, childItems, true, txtViewEmptySavedContact, exLstViewStaffDirectory);
						setListAdapter(staffDirItemAdapter);
						
						if("0".equals(direction)) {
							
						} else {
							setSelectedGroup(SidraPulseApp.getInstance().getStaffListInfoList().size());
						}
						
						Log.e("i am in if", "----");
						
					} else {
						Log.e("i am in else", "----");
					}
				}
				
				
			} else if (departmentsStatus == 5) {
				SidraPulseApp.getInstance().accessTokenChange(StaffDirectoryActivity.this);
			}
			
			
			//adapter.notifyDataSetChanged();
			exLstViewStaffDirectory.onRefreshComplete();
			
			super.onPostExecute(result);
			
		}
	}
	
	
}
