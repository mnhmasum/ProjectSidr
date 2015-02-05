package com.atomix.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.atomix.datamodel.GalleryDetailsInfo;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.sidrapulse.R;
import com.atomix.sidrapulse.ViewPagerActivity;

public class ViewPagerAdapter extends PagerAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private boolean isClickable;
	private boolean isImagePathLocal;
	private int typeIs;
	
	public ViewPagerAdapter(Activity act, ArrayList<HashMap<String, String>> list, boolean isImagePathLocal, boolean isClickable, int typeIs) {
		this.list = list;
		activity = act;
		this.isClickable = isClickable;
		this.isImagePathLocal = isImagePathLocal;
		this.typeIs = typeIs;
	}

	public int getCount() {
		if (list == null)
			return 0;
		else
			return list.size();
	}

	public Object instantiateItem(ViewGroup collection, final int position) {
		
	  /////////
	  TextView textView = new TextView(activity);
      textView.setTextColor(Color.WHITE);
      textView.setTextSize(30);
      textView.setTypeface(Typeface.DEFAULT_BOLD);
      textView.setText(String.valueOf(position));
	
      RelativeLayout.LayoutParams relativeMainImage = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      relativeMainImage.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
      
	  ImageView imgView = new ImageView(activity);
	  //imgView.setBackgroundColor(Color.parseColor("#999999"));
	  imgView.setBackgroundResource(R.drawable.no_image);
	  //imgView.setScaleType(ScaleType.FIT_XY);
	  
	  //imgView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	  imgView.setLayoutParams(relativeMainImage);
	  //imgView.setScaleType(ScaleType.FIT_XY);
	  //new AQuery(activity).id(imgView).image(ConstantValues.FILE_BASE_URL+list.get(position).get("url"), true, true, 300, R.drawable.image);
	  
	  ImageView imageViewPlay = new ImageView(activity);
      imageViewPlay.setImageResource(R.drawable.play_btn);
	  
	  RelativeLayout.LayoutParams relativeImagePlay = new RelativeLayout.LayoutParams(100, 100);
	  relativeImagePlay.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
	  imageViewPlay.setLayoutParams(relativeImagePlay);
      
      RelativeLayout relativeMainLayout = new RelativeLayout(activity);
      //layout.setOrientation(RelativeLayout.V);
      LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
      relativeMainLayout.setBackgroundColor(Color.WHITE);
      relativeMainLayout.setLayoutParams(layoutParams);
      
		/*AQuery aq = new AQuery(activity);
		BitmapAjaxCallback bmCallBack = new BitmapAjaxCallback();
		
		if(isImagePathLocal) {
			if (list.get(position).get("url").startsWith("upload")) {
				bmCallBack.url(ConstantValues.FILE_BASE_URL+ list.get(position).get("url")).targetWidth(500).rotate(true);
				
			}  else if(list.get(position).get("url").startsWith("public")) {
				bmCallBack.url(ConstantValues.FILE_BASE_URL + list.get(position).get("url")).targetWidth(500).rotate(true);
				
			}else {
				bmCallBack.url(list.get(position).get("url")).targetWidth(300).rotate(true);
				
			}
			
		} else {
			bmCallBack.url(ConstantValues.FILE_BASE_URL + list.get(position).get("url")).targetWidth(500).rotate(true);
			Log.i("POSTED_URL","" + ConstantValues.FILE_BASE_URL + list.get(position).get("url"));
		}
		
		bmCallBack.memCache(true);
		bmCallBack.fileCache(true);
		aq.id(imgView).image(bmCallBack);*/
		
		
		if (InternetConnectivity.isConnectedToInternet(activity)) {
			AQuery aquery = new AQuery(activity);
			final Bitmap placeholder = aquery.getCachedImage(R.drawable.no_image);
			
			String url = "";
			
			if(isImagePathLocal) {
				if (list.get(position).get("url").startsWith("upload")) {
					url = ConstantValues.FILE_BASE_URL+ list.get(position).get("url");
					//bmCallBack.url(ConstantValues.FILE_BASE_URL+ list.get(position).get("url")).targetWidth(500).rotate(true);
					
				} else if(list.get(position).get("url").startsWith("public")) {
					url = ConstantValues.FILE_BASE_URL + list.get(position).get("url");
					//bmCallBack.url(ConstantValues.FILE_BASE_URL + list.get(position).get("url")).targetWidth(500).rotate(true);
					
				} else {
					url = list.get(position).get("url");
					//bmCallBack.url(list.get(position).get("url")).targetWidth(300).rotate(true);
					
				}
				
			} else {
				url = ConstantValues.FILE_BASE_URL + list.get(position).get("url");
				//bmCallBack.url(ConstantValues.FILE_BASE_URL + list.get(position).get("url")).targetWidth(500).rotate(true);
				Log.i("POSTED_URL","" + ConstantValues.FILE_BASE_URL + list.get(position).get("url"));
			}
			
			
			aquery.id(imgView).image(url, true, true, 500, 500, new BitmapAjaxCallback(){
			//aquery.id(imgView).image(url,  true, true, 0, 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE, new BitmapAjaxCallback(){
			//imageUrl, true, true, 0, 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE
			    @Override
			    public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status){
			        
			        if (status.getCode() == 200) {
			        	 
						if (bm != null) {
							iv.setImageBitmap(bm);
							iv.setBackground(null);
							
						} else {
							iv.setImageBitmap(placeholder);
						}
	        	
			        } else {
			        	iv.setImageBitmap(placeholder);
			        	
			        }
			        
			        //loadingViewAnim.stop();
			        
			    }
			    
			}.rotate(true));
			
		} else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(activity);
		}
		
		//imgView.setPadding(10, 10, 10, 10);
		
		if(isClickable) {
			imgView.setEnabled(true);
			
		} else {
			imgView.setEnabled(false);
			
		}
		
		imgView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<GalleryDetailsInfo> arrayList = new ArrayList<GalleryDetailsInfo>();
				
				for(int i = 0; i < list.size(); i++) {
					GalleryDetailsInfo info = new GalleryDetailsInfo();
					info.setMediaType(1);
					Log.e("path of full view", "---"+list.get(i).get("url").toString());
					info.setImageOrVideoUrl(list.get(i).get("url").toString());
					arrayList.add(info);
				}
				
				SidraPulseApp.getInstance().setGalleryDetailsInfoList(arrayList);
				
				Intent intent = new Intent(activity, ViewPagerActivity.class);
				intent.putExtra("click_position", position);
				intent.putExtra("type_is", typeIs);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				activity.getApplicationContext().startActivity(intent);
			}
		});
		
		 relativeMainLayout.addView(imgView);
		 
		 if (this.typeIs == 4) {
				if (list.get(position).get("media_type").equals("2")) {
					imgView.setEnabled(true);
					relativeMainLayout.addView(imageViewPlay);
					
					imgView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(Intent.ACTION_VIEW);
					        intent.setDataAndType(Uri.parse(ConstantValues.FILE_BASE_URL + SidraPulseApp.getInstance().getGalleryDetailsInfoList().get(position).getImageOrVideoUrl().toString()), "video/*");
					        activity.startActivity(intent);
						}
					});
					
			        
				} else {
					imgView.setEnabled(false);
				}
		 }
		
		
		//**return imgView;
		
		//layout.addView(textView);
		
		collection.addView(relativeMainLayout);
		   
		return relativeMainLayout;
		
		/////// new Start
		
		  /*TextView textView = new TextView(activity);
	      textView.setTextColor(Color.WHITE);
	      textView.setTextSize(30);
	      textView.setTypeface(Typeface.DEFAULT_BOLD);
	      textView.setText(String.valueOf(position));
	      
	      ImageView imageView = new ImageView(activity);
	      imageView.setImageResource(R.drawable.play_btn);
	      LayoutParams imageParams = new LayoutParams( LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	      imageView.setLayoutParams(imageParams);
	      
	      LinearLayout layout = new LinearLayout(activity);
	      layout.setOrientation(LinearLayout.VERTICAL);
	      LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	      layout.setBackgroundColor(Color.RED);
	      layout.setLayoutParams(layoutParams);
	      layout.addView(textView);
	      layout.addView(imageView);
	      
	      final int page = position;
	      layout.setOnClickListener(new OnClickListener(){

		    @Override
		    public void onClick(View v) {
		     Toast.makeText(activity, 
		      "Page " + page + " clicked", 
		      Toast.LENGTH_LONG).show();
		    }});
	      
	      collection.addView(layout);
	      return layout;*/
		
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}
}
