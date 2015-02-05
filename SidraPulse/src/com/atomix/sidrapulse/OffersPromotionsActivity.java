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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.atomix.adapter.OfferPromotionAdapter;
import com.atomix.adapter.SpinnerAdapter;
import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class OffersPromotionsActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemSelectedListener,  OnCheckedChangeListener{
	private String TAG = "OffersPromotionActivity";
	private Button btnBack;
	private Spinner spinnerOfferPromotionCategories;
	private PullToRefreshListView lstViewOfferPromotionCategories;
	private RadioButton radioBtnExpiringSoon;
	private RadioButton radioBtnLatestAdded;
	private RadioButton radioBtnMyFavourites;
	private RadioButton radioBtnLongTermOffers;
	private ProgressDialog progressDialog;
	private int offerAndPromotionStatus;
	private int offerAndPromotionCategoriesStatus;
	private TextView txtViewSubtitle;
	private TextView txtViewEmptyList;
	
	private OfferPromotionAdapter offerPromotionAdapter;
	private int categorySelected = 0;
//	public static int tabIndex = 1;
	private int pageNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offers_promotions);

		initViews();
		setListener();
		loadSpinnerData();
		loadData();
		
	}

	private void loadData() {
		txtViewSubtitle.setText("Offers expiring in 30 Days");
		offerPromotionAdapter = new OfferPromotionAdapter(OffersPromotionsActivity.this, OffersPromotionsActivity.this, SidraPulseApp.getInstance().getOfferPromotionInfoList(), lstViewOfferPromotionCategories, txtViewEmptyList);
		
		/*if (SidraPulseApp.getInstance().getOfferPromotionInfoList() != null) {
				} else {
			
		}*/
	}

	private void loadSpinnerData() {
		new OfferPromotionCategories().execute();
	}

	private void initViews() {
		txtViewEmptyList = (TextView) findViewById(R.id.txt_view_empty_list);
		txtViewEmptyList.setText(getResources().getString(R.string.empty_list));
		txtViewEmptyList.setVisibility(View.INVISIBLE);
		txtViewSubtitle = (TextView) findViewById(R.id.txt_view_subtitle);
		btnBack = (Button) findViewById(R.id.btn_back);
		spinnerOfferPromotionCategories = (Spinner) findViewById(R.id.spinner_offer_promotion_categories);
		lstViewOfferPromotionCategories = (PullToRefreshListView) findViewById(R.id.lst_view_offer_promotion_categories);
		lstViewOfferPromotionCategories.setMode(Mode.BOTH);
		radioBtnExpiringSoon = (RadioButton) findViewById(R.id.radio_btn_expiring_soon);
		radioBtnLatestAdded = (RadioButton) findViewById(R.id.radio_btn_latest_added);
		radioBtnMyFavourites = (RadioButton) findViewById(R.id.radio_btn_my_favourites);
		radioBtnLongTermOffers = (RadioButton) findViewById(R.id.radio_btn_long_term_offers);
	}
	
	private void setListener() {
		btnBack.setOnClickListener(this);
		lstViewOfferPromotionCategories.setOnItemClickListener(this);
		spinnerOfferPromotionCategories.setOnItemSelectedListener(this);
		((RadioGroup) findViewById(R.id.radio_group)).setOnCheckedChangeListener(this);
		
		lstViewOfferPromotionCategories.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				if (!InternetConnectivity.isConnectedToInternet(OffersPromotionsActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(OffersPromotionsActivity.this);
					lstViewOfferPromotionCategories.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getOfferPromotionInfoList() == null) {
					new OfferPromotionApiTask("", "-1").execute();
					/*offerPromotionAdapter.notifyDataSetChanged();
					lstViewOfferPromotionCategories.onRefreshComplete();*/
					return;
				} 
				
				if (ConstantValues.offersTabIndex == 3) {
					
				}
				
				String first_element_id = Integer.toString(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(0).getId());
				new OfferPromotionApiTask(first_element_id, "0").execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if (!InternetConnectivity.isConnectedToInternet(OffersPromotionsActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(OffersPromotionsActivity.this);
					lstViewOfferPromotionCategories.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getOfferPromotionInfoList() == null) {
					new OfferPromotionApiTask("", "-1").execute();
					Log.i("Offer & Promo","OK NULL");
					return;
				} 
				
				String last_element_id = Integer.toString(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(SidraPulseApp.getInstance().getOfferPromotionInfoList().size() - 1).getId());
				new OfferPromotionApiTask(last_element_id, "1").execute();
				
				/*if (SidraPulseApp.getInstance().getOfferPromotionInfoList().size() >= pageNo*10) {
					pageNo++;
					new OfferPromotionApiTask().execute();
					
				} else {
					Log.i("Offer & Promo","Refresh");
					new OfferPromotionApiTask().execute();
					return;
				}*/
			}

		});
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		//lstViewOfferPromotionCategories.onRefreshComplete(); 
		categorySelected = arg2;
		ConstantValues.PullDownActive = true;
		new OfferPromotionApiTask("", "").execute();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent intent = new Intent(OffersPromotionsActivity.this, OffersPromotionsDetailsActivity.class);
		intent.putExtra("click_position", position - 1);
		startActivity(intent);
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
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		pageNo = 1;
		switchTab();
	}
	
	private void switchTab() {
		ConstantValues.PullDownActive = true;
		int selectedTab = ((RadioGroup) findViewById(R.id.radio_group)).getCheckedRadioButtonId();
		
		switch (selectedTab) {
			case R.id.radio_btn_expiring_soon:
				ConstantValues.offersTabIndex = 1;
				txtViewSubtitle.setVisibility(View.VISIBLE);
				txtViewSubtitle.setText("Offers expiring in 30 Days");
				radioBtnExpiringSoon.setButtonDrawable(R.drawable.expiring_soon_g);
				radioBtnLatestAdded.setButtonDrawable(R.drawable.latest_added_w);
				radioBtnLongTermOffers.setButtonDrawable(R.drawable.long_term_w);
				radioBtnMyFavourites.setButtonDrawable(R.drawable.my_favorites_w);
				if (InternetConnectivity.isConnectedToInternet(OffersPromotionsActivity.this)) {
					new OfferPromotionApiTask("", "").execute();
				} else {
					SidraPulseApp.getInstance().openDialogForInternetChecking(OffersPromotionsActivity.this);
				}
				break;
				
			case R.id.radio_btn_latest_added:
				ConstantValues.offersTabIndex = 2;
				txtViewSubtitle.setVisibility(View.GONE);
				radioBtnLatestAdded.setButtonDrawable(R.drawable.latest_added_g);
				radioBtnExpiringSoon.setButtonDrawable(R.drawable.expiring_soon_w);
				radioBtnLongTermOffers.setButtonDrawable(R.drawable.long_term_w);
				radioBtnMyFavourites.setButtonDrawable(R.drawable.my_favorites_w);
				if (InternetConnectivity.isConnectedToInternet(OffersPromotionsActivity.this)) {
					new OfferPromotionApiTask("", "").execute();
				} else {
					SidraPulseApp.getInstance().openDialogForInternetChecking(OffersPromotionsActivity.this);
				}
				break;
				
			case R.id.radio_btn_long_term_offers:
				ConstantValues.offersTabIndex = 4;
				txtViewSubtitle.setVisibility(View.GONE);
				radioBtnLongTermOffers.setButtonDrawable(R.drawable.long_term_g);
				radioBtnLatestAdded.setButtonDrawable(R.drawable.latest_added_w);
				radioBtnExpiringSoon.setButtonDrawable(R.drawable.expiring_soon_w);
				radioBtnMyFavourites.setButtonDrawable(R.drawable.my_favorites_w);
				if (InternetConnectivity.isConnectedToInternet(OffersPromotionsActivity.this)) {
					new OfferPromotionApiTask("", "").execute();
				} else {
					SidraPulseApp.getInstance().openDialogForInternetChecking(OffersPromotionsActivity.this);
				}
				break;
				
			case R.id.radio_btn_my_favourites:
				ConstantValues.offersTabIndex = 3;
				txtViewSubtitle.setVisibility(View.VISIBLE);
				txtViewSubtitle.setText("Bookmarked Offers");
				radioBtnMyFavourites.setButtonDrawable(R.drawable.my_favorites_g);
				radioBtnLongTermOffers.setButtonDrawable(R.drawable.long_term_w);
				radioBtnLatestAdded.setButtonDrawable(R.drawable.latest_added_w);
				radioBtnExpiringSoon.setButtonDrawable(R.drawable.expiring_soon_w);
				if (InternetConnectivity.isConnectedToInternet(OffersPromotionsActivity.this)) {
					new OfferPromotionApiTask("", "").execute();
				} else {
					SidraPulseApp.getInstance().openDialogForInternetChecking(OffersPromotionsActivity.this);
				}
				break;
	
			default:
				break;
		}
	}

	private class OfferPromotionApiTask extends AsyncTask<Void, Void, Void> {
		private String last_element_id  =null;
		private String direction = null;
		
		public OfferPromotionApiTask(String last_element_id, String direction) {
			this.last_element_id = last_element_id;
			this.direction = direction;
		}
		
		@Override
		protected void onPreExecute() {
			progressDialog = SidraCustomProgressDialog.creator(OffersPromotionsActivity.this);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				
				if (this.direction.equals("-1")) {
					return null;
				}
				
				if (this.direction.equals("0")) {
					if(categorySelected == 0) {
						offerAndPromotionStatus = CommunicationLayer.getOfferAndPromotionData(ConstantValues.FUNC_ID_OFFER_AND_PROMOTION_API, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(ConstantValues.offersTabIndex), "0", this.last_element_id, this.direction);
					} else {
						offerAndPromotionStatus = CommunicationLayer.getOfferAndPromotionData(ConstantValues.FUNC_ID_OFFER_AND_PROMOTION_API, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(ConstantValues.offersTabIndex), Integer.toString(SidraPulseApp.getInstance().getOfferAndPromotionCategories().get(categorySelected-1).getId()), this.last_element_id, this.direction);
					}
				} else if (ConstantValues.PullDownActive) {
					if(categorySelected == 0) {
						offerAndPromotionStatus = CommunicationLayer.getOfferAndPromotionData(ConstantValues.FUNC_ID_OFFER_AND_PROMOTION_API, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(ConstantValues.offersTabIndex), "0", this.last_element_id, this.direction);
					} else {
						offerAndPromotionStatus = CommunicationLayer.getOfferAndPromotionData(ConstantValues.FUNC_ID_OFFER_AND_PROMOTION_API, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(ConstantValues.offersTabIndex), Integer.toString(SidraPulseApp.getInstance().getOfferAndPromotionCategories().get(categorySelected-1).getId()), this.last_element_id, this.direction);
					}
				}
				
				
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
			
			if (ConstantValues.offersTabIndex == 3) {
				offerPromotionAdapter.notifyDataSetChanged();
				lstViewOfferPromotionCategories.onRefreshComplete();
			}
			
			
			
			if(offerAndPromotionStatus == 5) {
				SidraPulseApp.getInstance().accessTokenChange(OffersPromotionsActivity.this);
				return;
			}
			
			if (direction.equals("0") || direction.equals("1")) {
				offerPromotionAdapter.notifyDataSetChanged();
				lstViewOfferPromotionCategories.onRefreshComplete();
				return;
			} 
			
			/*Log.i(TAG,"Status: " + offerAndPromotionStatus + " PAGE NO: " + pageNo);
			if (offerAndPromotionStatus == 0 && pageNo > 1) {
				lstViewOfferPromotionCategories.onRefreshComplete();
				return;
			}
			
			if (offerAndPromotionStatus == 1 && pageNo > 1) {
				lstViewOfferPromotionCategories.onRefreshComplete();
				offerPromotionAdapter.notifyDataSetChanged();
				return;
			}*/
			
			if (offerAndPromotionStatus == 1) {
				if (SidraPulseApp.getInstance().getOfferPromotionInfoList() != null) {
					txtViewEmptyList.setVisibility(View.INVISIBLE);
					lstViewOfferPromotionCategories.setVisibility(View.VISIBLE);
					offerPromotionAdapter = new OfferPromotionAdapter(OffersPromotionsActivity.this, OffersPromotionsActivity.this, SidraPulseApp.getInstance().getOfferPromotionInfoList(), lstViewOfferPromotionCategories, txtViewEmptyList);
					lstViewOfferPromotionCategories.setAdapter(offerPromotionAdapter);
					lstViewOfferPromotionCategories.onRefreshComplete();
				} else {
					lstViewOfferPromotionCategories.setAdapter(null);
					lstViewOfferPromotionCategories.onRefreshComplete();
					lstViewOfferPromotionCategories.setVisibility(View.INVISIBLE);
					txtViewEmptyList.setVisibility(View.VISIBLE);
				}

			} else {
				Log.i(TAG,"Status Last: " + offerAndPromotionStatus + " PAGE NO: " + pageNo);
				lstViewOfferPromotionCategories.setAdapter(null);
				lstViewOfferPromotionCategories.onRefreshComplete();
				offerPromotionAdapter.notifyDataSetChanged();
				lstViewOfferPromotionCategories.setVisibility(View.INVISIBLE);
				txtViewEmptyList.setVisibility(View.VISIBLE);
			}
			
			
			lstViewOfferPromotionCategories.onRefreshComplete();
			
		}
		
	}

	private class OfferPromotionCategories extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			progressDialog = SidraCustomProgressDialog.creator(OffersPromotionsActivity.this);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				offerAndPromotionCategoriesStatus = CommunicationLayer.getOfferAndPromotionCategories(ConstantValues.FUNC_ID_OFFER_AND_PROMOTION_CATAGORIES, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();

				if (offerAndPromotionCategoriesStatus == 1) {
					if (SidraPulseApp.getInstance().getOfferAndPromotionCategories() != null) {
						ArrayList<String> list = new ArrayList<String>();
						list.add("  Show All");
						for (int i = 0; i < SidraPulseApp.getInstance().getOfferAndPromotionCategories().size(); i++) {
							list.add("  " + SidraPulseApp.getInstance().getOfferAndPromotionCategories().get(i).getCategoryName());
						}
					     
					    spinnerOfferPromotionCategories.setAdapter(new SpinnerAdapter(OffersPromotionsActivity.this, R.layout.spinner_simple_row, list));
						
					} else {
						spinnerOfferPromotionCategories.setAdapter(null);
					}
				}
			}
		}

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(offerPromotionAdapter != null ) {
			offerPromotionAdapter.notifyDataSetChanged();
		}
		if(SidraPulseApp.getInstance().getOfferPromotionInfoList() != null) {
			if(SidraPulseApp.getInstance().getOfferPromotionInfoList().size() == 0){
				lstViewOfferPromotionCategories.setVisibility(View.GONE);
				txtViewEmptyList.setVisibility(View.VISIBLE);
			 }
		
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		ConstantValues.offersTabIndex = 1;
		SidraPulseApp.getInstance().setOfferPromotionInfoList(null);
		super.onBackPressed();
	}

}
