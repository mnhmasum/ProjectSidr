package com.atomix.sidrapulse;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.atomix.adapter.OfferPromotionAdapter;
import com.atomix.adapter.ViewPagerAdapter;
import com.atomix.customview.SidraTextView;
import com.atomix.interfacecallback.UnReadRequest;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.synctask.FavoriateOrNotAsyncTask;
import com.atomix.utils.Utilities;

public class OffersPromotionsDetailsActivity extends Activity implements OnClickListener {

	private Button btnBack;
	private Button btnMenu;
	private SidraTextView txtViewHeadLine;
	private SidraTextView txtViewDate;
	private ImageView imgViewFavorite;
	private SidraTextView txtViewDescription;
	private SidraTextView txtViewTitle;
	private SidraTextView txtViewDateField;
	private int position;
	private ViewPager viewPager;
	private LinearLayout linearIndicatorLayout;
	private boolean isPathLocal = false;
	private String id;
	private int isBookMarked;
	private int isBookMarkedFirstTime;
	private RelativeLayout relativeImageHolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offers_promotions_details);
		
		initViews();
		setListener();
		loadData();
	}
	
	private void loadData() {
		if(getIntent().getExtras() != null) {
			position = getIntent().getExtras().getInt("click_position");
			id = Integer.toString(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getId());
			isBookMarked = SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getIsBookmarked();
			isBookMarkedFirstTime = isBookMarked;
			//txtViewHeadLine.setText("This is Static Headline");
			txtViewTitle.setText(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getTitle());
			
			txtViewDescription.setText(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getDescription());
			
			Log.e("Long term offer is","" + SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getLongTerm());
			if(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getIsBookmarked() == 1) {
				imgViewFavorite.setBackgroundResource(R.drawable.star_icon);
			} else {
				imgViewFavorite.setBackgroundResource(R.drawable.star_gray_icon);
			}
			
			if(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getLongTerm() != 1) {
				txtViewDate.setVisibility(View.VISIBLE);
				txtViewDate.setText(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getValidUntil());
			} else {
				txtViewDateField.setText("Always available");
				txtViewDate.setVisibility(View.INVISIBLE);
			}
			
			Linkify.addLinks(txtViewTitle, Linkify.ALL);
			Linkify.addLinks(txtViewDescription, Linkify.ALL);
			
			
			
		}
		
		ArrayList<HashMap<String, String>> mediaList = new ArrayList<HashMap<String, String>>();
		if(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getPhotoArray() != null) {
		    for(int j = 0; j < SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getPhotoArray().size(); j++) {
		        ImageView image = new ImageView(OffersPromotionsDetailsActivity.this);
		        image.setBackgroundResource(R.drawable.circle);
		        image.setTag(j);
		        linearIndicatorLayout.addView(image);
		    }		
		    linearIndicatorLayout.findViewWithTag(0).setBackgroundResource(R.drawable.circle_selected);
			
			for(int count = 0; count < SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getPhotoArray().size(); count++) {
				HashMap<String, String> map1 = new HashMap<String, String>();
				map1.put("url", SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getPhotoArray().get(count).getPhoto());
				mediaList.add(map1);
			}
		}else {
			HashMap<String, String> map1 = new HashMap<String, String>();
			map1.put("url", SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getCompanyThumb());
			mediaList.add(map1);
		}
			
			viewPager.setAdapter(new ViewPagerAdapter(OffersPromotionsDetailsActivity.this, mediaList, isPathLocal, true, 3));
		
	}

	private void initViews() {
		relativeImageHolder = (RelativeLayout) findViewById(R.id.relative_image_holder);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnMenu = (Button) findViewById(R.id.btn_menu);
		imgViewFavorite = (ImageView) findViewById(R.id.img_view_favorite_or_not);
		//txtViewHeadLine = (SidraTextView) findViewById(R.id.txt_view_title);
		txtViewDate = (SidraTextView) findViewById(R.id.txt_view_date);
		txtViewDescription = (SidraTextView) findViewById(R.id.txt_view_description);
		txtViewTitle = (SidraTextView) findViewById(R.id.txt_view_headline);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		linearIndicatorLayout = (LinearLayout) findViewById(R.id.linear_indicator);
		txtViewDateField = (SidraTextView) findViewById(R.id.textView2);
		
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
		imgViewFavorite.setOnClickListener(this);
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
	         
	         @Override
	         public void onPageSelected(int index) {
	        	 	Log.d("Page", "**** onPageSelected = " + index);
	        	 	
	        	 	for(int count = 0; count < SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getPhotoArray().size(); count++) {
	        	 	if (index == count) {
	        	 		linearIndicatorLayout.findViewWithTag(count).setBackgroundResource(R.drawable.circle_selected);
	        	 		
	        	 	} else{
	        	 	linearIndicatorLayout.findViewWithTag(count).setBackgroundResource(R.drawable.circle);
	        	 	} 
	        	 }
	        	 	
	         }

	         @Override
	         public void onPageScrolled(int arg0, float arg1, int arg2) {
	        	 
	         }
	         
	         @Override
	         public void onPageScrollStateChanged(int arg0) {
	        	 
	         }
	         
	      });
		
	}
	
	
	@Override
	public void onBackPressed() {
	if(isBookMarked != isBookMarkedFirstTime) {
		changeBookMarkStatus();
	}
		super.onBackPressed();
	}

	private void changeBookMarkStatus() {
		if(isBookMarked == 1) {
			SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).setIsBookmarked(1);
		} else {
			SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).setIsBookmarked(0);
			if(ConstantValues.offersTabIndex == 3) {
				SidraPulseApp.getInstance().getOfferPromotionInfoList().remove(position);
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
			if(isBookMarked != isBookMarkedFirstTime) {
				changeBookMarkStatus();
			}
			startActivity(new Intent(OffersPromotionsDetailsActivity.this, MainMenuActivity.class));
			finish();
			break;
			
		case R.id.img_view_favorite_or_not:
			if (InternetConnectivity.isConnectedToInternet(OffersPromotionsDetailsActivity.this)) {
				if(isBookMarked == 0) {
					//SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).setIsBookmarked(1);
					new FavoriateOrNotAsyncTask(OffersPromotionsDetailsActivity.this, id, "1", ConstantValues.FUNC_ID_BOOKMARK_OFFERS_AND_PROMOTION, new UnReadRequest()  {
						@Override
						public void onTaskCompleted(int status) {
							if(status == 1){
								isBookMarked = 1;
								Utilities.showToast(OffersPromotionsDetailsActivity.this, getResources().getString(R.string.toast_add_bookmark_for_offers));
								imgViewFavorite.setBackgroundResource(R.drawable.star_icon);
							}else{
								Utilities.showToast(OffersPromotionsDetailsActivity.this, ConstantValues.failureMessage);
							}
						}
					}).execute();
				} else {
					//SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).setIsBookmarked(0);
					new FavoriateOrNotAsyncTask(OffersPromotionsDetailsActivity.this, id, "0", ConstantValues.FUNC_ID_BOOKMARK_OFFERS_AND_PROMOTION ,new UnReadRequest() {
						 
						@Override
						public void onTaskCompleted(int status) {
							if(status == 1){
								isBookMarked  = 0;
								Utilities.showToast(OffersPromotionsDetailsActivity.this, getResources().getString(R.string.toast_remove_bookmark_for_offers));
								imgViewFavorite.setBackgroundResource(R.drawable.star_gray_icon);
								
							} else if(status == 5) {
								SidraPulseApp.getInstance().accessTokenChange(OffersPromotionsDetailsActivity.this);
								
							}else{
								Utilities.showToast(OffersPromotionsDetailsActivity.this, ConstantValues.failureMessage);
							}
						}
					}).execute();
				}
			} else {
				SidraPulseApp.getInstance().openDialogForInternetChecking(OffersPromotionsDetailsActivity.this);
			}
			
			break;

		default:
			break;
		}
	}

}
