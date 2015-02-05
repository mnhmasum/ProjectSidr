package com.atomix.sidrapulse.staffdirectory;

import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.atomix.adapter.StaffDirectoryItemExpandableAdapter;
import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.datamodel.StaffListInfo;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.sidrapulse.ForumsActivity;
import com.atomix.sidrapulse.MainMenuActivity;
import com.atomix.sidrapulse.R;
import com.atomix.sidrapulse.StaffDirectorySearchActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

public class StaffDirectoryItemActivity extends ExpandableListActivity implements OnClickListener, OnCheckedChangeListener, OnGroupExpandListener {

	private Button btnBack;
	private Button btnMenu;
	private RadioButton radioBtnBrowse;
	private RadioButton radioBtnSavedContacts;
	private PullToRefreshExpandableListView exLstViewStaffDirectoryItem;
	private Button btnSearch;
	private Button edTxtSearchItem;
	private TextView txtViewTitle;
	private ProgressDialog progressDialog;
	private int staffListStatus;
	private int position;
	private TextView txtViewEmptySavedContact;
	private int lastExpandedPosition = -1;
	private TextView txtViewSubTititle;
	private ArrayList<StaffListInfo> parentItems;
	private ArrayList<StaffListInfo> childItems;

	private boolean isBookMarked;
	private StaffDirectoryItemExpandableAdapter staffDirAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stay_informed_item);
		initViews();
		setListener();
		loadData();
	}

	private void initViews() {
		txtViewSubTititle = (TextView) findViewById(R.id.txt_view_subtitle);
		txtViewSubTititle.setText("Contacts you bookmarked");
		txtViewSubTititle.setVisibility(View.GONE);
		txtViewEmptySavedContact = (TextView) findViewById(R.id.txt_view_empty_saved_contact);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnMenu = (Button) findViewById(R.id.btn_menu);
		radioBtnBrowse = (RadioButton) findViewById(R.id.radio_btn_browse);
		radioBtnSavedContacts = (RadioButton) findViewById(R.id.radio_btn_saved_contacts);
		exLstViewStaffDirectoryItem = (PullToRefreshExpandableListView) findViewById(R.id.ex_lst_view_staff_directory_item);
		getExpandableListView().setGroupIndicator(null);
		btnSearch = (Button) findViewById(R.id.btn_search);
		edTxtSearchItem = (Button) findViewById(R.id.ed_txt_search_all_departments);
		txtViewTitle = (TextView) findViewById(R.id.txt_view_title);
		txtViewEmptySavedContact.setText(R.string.saved_dept_contact_is_empty);
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		edTxtSearchItem.setOnClickListener(this);
		((RadioGroup) findViewById(R.id.radio_group)).setOnCheckedChangeListener(this);
		getExpandableListView().setOnGroupExpandListener(this);
		
		exLstViewStaffDirectoryItem.setOnRefreshListener(new OnRefreshListener2 <ExpandableListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
				//Code for pull to up refresh
				if (!InternetConnectivity.isConnectedToInternet(StaffDirectoryItemActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(StaffDirectoryItemActivity.this);
					//adapter.notifyDataSetChanged();
					exLstViewStaffDirectoryItem.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getStaffListInfoList() == null) {
					exLstViewStaffDirectoryItem.onRefreshComplete();
					return;
				}
			
				new GetDataTask("0").execute();
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
				
				if (!InternetConnectivity.isConnectedToInternet(StaffDirectoryItemActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(StaffDirectoryItemActivity.this);
					//adapter.notifyDataSetChanged();
					exLstViewStaffDirectoryItem.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getStaffListInfoList() == null) {
					exLstViewStaffDirectoryItem.onRefreshComplete();
					return;
				}
				
				new GetDataTask("1").execute();
			}

		});
		
	}

	private void loadData() {
		staffDirAdapter = new StaffDirectoryItemExpandableAdapter(getApplicationContext(), StaffDirectoryItemActivity.this, parentItems, null, childItems, false, txtViewEmptySavedContact, exLstViewStaffDirectoryItem);
		
		if (getIntent().getExtras() != null) {
			position = getIntent().getExtras().getInt("position");
			txtViewTitle.setText(SidraPulseApp.getInstance().getDirectoryDepartmentsInfoList().get(position).getDepartmentName());
		}
		if (InternetConnectivity.isConnectedToInternet(StaffDirectoryItemActivity.this)) {
			new StaffDirectoryStaffListApiTask().execute();
		} else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(StaffDirectoryItemActivity.this);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;

		case R.id.btn_search:
			// if(edTxtSearchItem.getText().toString().trim().length() > 2) {
			Intent intent = new Intent(StaffDirectoryItemActivity.this, StaffDirectorySearchActivity.class);
			intent.putExtra("keyword", edTxtSearchItem.getText().toString().trim());
			intent.putExtra("is_all", false);
			intent.putExtra("position", position);
			intent.putExtra("isMainMenu", false);
			startActivity(intent);
			// }
			break;

		case R.id.ed_txt_search_all_departments:
			// if(edTxtSearchItem.getText().toString().trim().length() > 2) {
			Intent intent1 = new Intent(StaffDirectoryItemActivity.this,StaffDirectorySearchActivity.class);
			intent1.putExtra("keyword", edTxtSearchItem.getText().toString().trim());
			intent1.putExtra("is_all", false);
			intent1.putExtra("position", position);
			intent1.putExtra("isMainMenu", false);
			startActivity(intent1);
			// }
			break;

		case R.id.btn_menu:
			startActivity(new Intent(StaffDirectoryItemActivity.this, MainMenuActivity.class));
			finish();
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
		case R.id.radio_btn_browse:
			isBookMarked = false;
			txtViewTitle.setText(SidraPulseApp.getInstance().getDirectoryDepartmentsInfoList().get(position).getDepartmentName());
			txtViewSubTititle.setVisibility(View.GONE);
			txtViewEmptySavedContact.setText(R.string.saved_dept_contact_is_empty);
			txtViewEmptySavedContact.setVisibility(View.INVISIBLE);
			radioBtnBrowse.setButtonDrawable(R.drawable.browse_btn_on);
			radioBtnSavedContacts.setButtonDrawable(R.drawable.saved_btn_off);
			new StaffDirectoryStaffListApiTask().execute();
			break;

		case R.id.radio_btn_saved_contacts:
			isBookMarked = true;
			txtViewTitle.setText(R.string.title_activity_staff_directory);
			txtViewSubTititle.setVisibility(View.VISIBLE);
			txtViewEmptySavedContact.setText(R.string.saved_contact_is_empty);
			txtViewEmptySavedContact.setVisibility(View.INVISIBLE);
			radioBtnBrowse.setButtonDrawable(R.drawable.browse_btn_off);
			radioBtnSavedContacts.setButtonDrawable(R.drawable.saved_btn_on);
			new StaffDirectoryStaffListApiTask().execute();
			break;

		default:
			break;
		}
	}

	private class StaffDirectoryStaffListApiTask extends
			AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			progressDialog = SidraCustomProgressDialog.creator(StaffDirectoryItemActivity.this);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				if(!isBookMarked) {
					staffListStatus = CommunicationLayer.getStaffDirectoryStaffListData(ConstantValues.FUNC_ID_STAFF_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(SidraPulseApp.getInstance().getDirectoryDepartmentsInfoList().get(position).getId()), "", "");
				} else {
					staffListStatus = CommunicationLayer.getStaffDirectoryStaffListData(ConstantValues.FUNC_ID_STAFF_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "0", "", "");
				}
				//staffListStatus = CommunicationLayer.getStaffDirectoryStaffListData(ConstantValues.FUNC_ID_STAFF_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "0", "1");
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

			if (staffListStatus == 1) {
				if (SidraPulseApp.getInstance().getStaffListInfoList() != null) {
					parentItems = new ArrayList<StaffListInfo>();
					childItems = new ArrayList<StaffListInfo>();
					int bookMarkedSize = 0;
					for (int i = 0; i < SidraPulseApp.getInstance().getStaffListInfoList().size(); i++) {
						if (isBookMarked) {
							if (SidraPulseApp.getInstance().getStaffListInfoList().get(i).getIsBookmarked() == 1) {
								bookMarkedSize++;
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
						} else {
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
					}
					
					if (bookMarkedSize == 0 && isBookMarked) {
						exLstViewStaffDirectoryItem.setVisibility(View.INVISIBLE);
						txtViewEmptySavedContact.setVisibility(View.VISIBLE);
					} 
					
					exLstViewStaffDirectoryItem.setVisibility(View.VISIBLE);
					//setListAdapter(new StaffDirectoryItemExpandableAdapter(getApplicationContext(), StaffDirectoryItemActivity.this, parentItems, null, childItems, isBookMarked));
					staffDirAdapter = new StaffDirectoryItemExpandableAdapter(getApplicationContext(), StaffDirectoryItemActivity.this, parentItems, null, childItems, isBookMarked, txtViewEmptySavedContact, exLstViewStaffDirectoryItem);
					setListAdapter(staffDirAdapter);
					//exLstViewStaffDirectoryItem.setAdapter(new StaffDirectoryItemExpandableAdapter(getApplicationContext(), StaffDirectoryItemActivity.this, parentItems, null, childItems));
					//exLstViewStaffDirectoryItem.setIndicatorBounds(0, 0);
				} else {
					exLstViewStaffDirectoryItem.setVisibility(View.INVISIBLE);
					txtViewEmptySavedContact.setVisibility(View.VISIBLE);
				}
			} else {
				exLstViewStaffDirectoryItem.setVisibility(View.INVISIBLE);
				txtViewEmptySavedContact.setVisibility(View.VISIBLE);
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
				if(!isBookMarked) {
					if (this.direction.equals("0")) {
						String element_id = Integer.toString(SidraPulseApp.getInstance().getStaffListInfoList().get(0).getId());
						staffListStatus = CommunicationLayer.getStaffDirectoryStaffListData(ConstantValues.FUNC_ID_STAFF_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(SidraPulseApp.getInstance().getDirectoryDepartmentsInfoList().get(position).getId()), element_id, this.direction);
					} else if (ConstantValues.PullDownActive) {
						String element_id = Integer.toString(SidraPulseApp.getInstance().getStaffListInfoList().get(SidraPulseApp.getInstance().getStaffListInfoList().size()-1).getId());
						staffListStatus = CommunicationLayer.getStaffDirectoryStaffListData(ConstantValues.FUNC_ID_STAFF_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(SidraPulseApp.getInstance().getDirectoryDepartmentsInfoList().get(position).getId()), element_id, this.direction);
					}
					
				} else {
					
					if (this.direction.equals("0")) {
						String element_id = Integer.toString(SidraPulseApp.getInstance().getStaffListInfoList().get(0).getId());
						staffListStatus = CommunicationLayer.getStaffDirectoryStaffListData(ConstantValues.FUNC_ID_STAFF_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "0", element_id, this.direction);
					} else if (ConstantValues.PullDownActive) {
						String element_id = Integer.toString(SidraPulseApp.getInstance().getStaffListInfoList().get(SidraPulseApp.getInstance().getStaffListInfoList().size()-1).getId());
						staffListStatus = CommunicationLayer.getStaffDirectoryStaffListData(ConstantValues.FUNC_ID_STAFF_LIST, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), "0", element_id, this.direction);
					}
					
				} 
				
			} catch (Exception e) {
				
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			
			if(SidraPulseApp.getInstance().getStaffListInfoList() == null) {
				exLstViewStaffDirectoryItem.onRefreshComplete();
				return;
			}
			
			if (staffListStatus == 1) {
				if (SidraPulseApp.getInstance().getStaffListInfoList() != null) {
					parentItems = new ArrayList<StaffListInfo>();
					childItems = new ArrayList<StaffListInfo>();
					int bookMarkedSize = 0;
					for (int i = 0; i < SidraPulseApp.getInstance().getStaffListInfoList().size(); i++) {
						if (isBookMarked) {
							if (SidraPulseApp.getInstance().getStaffListInfoList().get(i).getIsBookmarked() == 1) {
								bookMarkedSize++;
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
							
						} else {
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
					}
					
					if (bookMarkedSize == 0 && isBookMarked) {
					}
					
					//exLstViewStaffDirectoryItem.setAdapter(new StaffDirectoryItemExpandableAdapter(getApplicationContext(), StaffDirectoryItemActivity.this, parentItems, null, childItems));
					//exLstViewStaffDirectoryItem.setIndicatorBounds(0, 0);
				} else {
					
				}
				
				staffDirAdapter = new StaffDirectoryItemExpandableAdapter(getApplicationContext(), StaffDirectoryItemActivity.this, parentItems, null, childItems, isBookMarked, txtViewEmptySavedContact, exLstViewStaffDirectoryItem);
				setListAdapter(staffDirAdapter);
				
				if ("0".equals(this.direction)) {
					
				} else {
					setSelectedGroup(SidraPulseApp.getInstance().getStaffListInfoList().size());
				}
				
			} else if (staffListStatus == 5) {
				SidraPulseApp.getInstance().accessTokenChange(StaffDirectoryItemActivity.this);
			}
			
			//adapter.notifyDataSetChanged();
			//staffDirAdapter.notifyDataSetChanged();
			exLstViewStaffDirectoryItem.onRefreshComplete();
			super.onPostExecute(result);
			
		}
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
			getExpandableListView().collapseGroup(lastExpandedPosition);
		}
		lastExpandedPosition = groupPosition;
	}

}
