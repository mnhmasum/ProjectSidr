package com.atomix.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.atomix.datamodel.OfferPromotionInfo;
import com.atomix.interfacecallback.UnReadRequest;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.sidrapulse.OffersPromotionsActivity;
import com.atomix.sidrapulse.R;
import com.atomix.synctask.FavoriateOrNotAsyncTask;
import com.atomix.utils.Utilities;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


public class OfferPromotionAdapter extends BaseAdapter {

	private ArrayList<OfferPromotionInfo> offerPromotionInfoList;
	private LayoutInflater mInflater;
	private Context context;
	private PullToRefreshListView lstView;
	private Activity activity;
	private AnimationDrawable loadingViewAnim = null;
	private AQuery  aq;
	private TextView txtViewemptyMessage;
	
	public OfferPromotionAdapter(Activity activity, Context context, ArrayList<OfferPromotionInfo> offerPromotionInfoList, PullToRefreshListView lstView, TextView txtViewemptyMessage) {
		this.activity = activity;
		this.context = context;
		this.offerPromotionInfoList = offerPromotionInfoList;
		this.lstView = lstView;
		this.aq = new AQuery(activity);  
		this.txtViewemptyMessage = txtViewemptyMessage;
	}

	@Override
	public int getCount() {
		if(offerPromotionInfoList != null) {
			return offerPromotionInfoList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return offerPromotionInfoList.get(position);
		
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.offer_promotion_row, null);

			holder = new ViewHolder();

			holder.imgViewOffer = (ImageView) convertView.findViewById(R.id.img_view_offer);
			holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txt_view_title);
			holder.txtViewUnlitLongTerm = (TextView) convertView.findViewById(R.id.txt_view_unlit_or_longterm);
			holder.txtViewDate = (TextView) convertView.findViewById(R.id.txt_view_date);
			holder.imgViewFavoriate = (ImageView) convertView.findViewById(R.id.img_view_favorite_or_not);
			convertView.findViewById(R.id.img_view_favorite_or_not).setTag(position);
			//findViewById(R.id.BtnToClick).setTag(position)
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		//holder.imgViewOffer.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image));
		holder.imgViewOffer.setBackgroundResource(R.drawable.no_image);
		//loadingViewAnim = (AnimationDrawable) holder.imgViewOffer.getBackground();
		//loadingViewAnim.start();
		
		if (SidraPulseApp.getInstance().getOfferPromotionInfoList() != null) {
			
		//	Log.e("size and position is", ""+SidraPulseApp.getInstance().getOfferPromotionInfoList().size()+"::::"+position);
			try {
				holder.txtViewTitle.setText(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getTitle());
				
				if(offerPromotionInfoList.get(position).getLongTerm() != 1) {
					holder.txtViewUnlitLongTerm.setText("Available Until");
					holder.txtViewDate.setText(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getValidUntil());
				} else {
					holder.txtViewUnlitLongTerm.setText("Long Term Offer");
					holder.txtViewDate.setText("");
				}
				
				if(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getIsBookmarked() == 0) {
					holder.imgViewFavoriate.setBackgroundResource(R.drawable.offers_star_gray) ;
				} else if(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(position).getIsBookmarked() == 1) {
					holder.imgViewFavoriate.setBackgroundResource(R.drawable.offers_star_icon) ;
				}
			} catch(IndexOutOfBoundsException e) {
				e.getMessage();
			}
			
		}
		
		final Bitmap placeholder = aq.getCachedImage(R.drawable.no_image);
		
		if (InternetConnectivity.isConnectedToInternet(context)) {
			
			Log.i("OFFERS_IMAGES", "***" + ConstantValues.FILE_BASE_URL+offerPromotionInfoList.get(position).getCompanyThumb());
			aq.id(holder.imgViewOffer).image(ConstantValues.FILE_BASE_URL+offerPromotionInfoList.get(position).getCompanyThumb(), true, true, 200, 300, new BitmapAjaxCallback(){

			    @Override
			    public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status){
			        Log.i("Status", "***" + status.getCode() + bm);
			        
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
			    
			});
			
		} else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(context);
		}
				
		
		holder.imgViewFavoriate.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				final int index =((Integer) v.getTag());
				Log.i("Child imgViewFavoriate","" + index+" SIZE IS: "+ SidraPulseApp.getInstance().getOfferPromotionInfoList().size());
				if (InternetConnectivity.isConnectedToInternet(activity)) {
					String id = Integer.toString(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(index).getId());
					if(SidraPulseApp.getInstance().getOfferPromotionInfoList().get(index).getIsBookmarked() == 0) {
						SidraPulseApp.getInstance().getOfferPromotionInfoList().get(index).setIsBookmarked(1);
						new FavoriateOrNotAsyncTask(activity, id, "1", ConstantValues.FUNC_ID_BOOKMARK_OFFERS_AND_PROMOTION, new UnReadRequest() {
							@Override
							public void onTaskCompleted(int status) {
								if(status ==1){
									Utilities.showToast(context, activity.getResources().getString(R.string.toast_add_bookmark_for_offers));
									holder.imgViewFavoriate.setBackgroundResource(R.drawable.offers_star_icon);
									//OfferPromotionAdapter.this.notifyDataSetChanged();
								} else{
									Utilities.showToast(context, ConstantValues.failureMessage);
								}
							}
						}).execute();
					} else {
						SidraPulseApp.getInstance().getOfferPromotionInfoList().get(index).setIsBookmarked(0);
						new FavoriateOrNotAsyncTask(activity, id, "0",ConstantValues.FUNC_ID_BOOKMARK_OFFERS_AND_PROMOTION, new UnReadRequest() {
							@Override
							public void onTaskCompleted(int status) {
								if (status == 1) {
									Utilities.showToast(context, activity.getResources().getString(R.string.toast_remove_bookmark_for_offers));
									holder.imgViewFavoriate.setBackgroundResource(R.drawable.offers_star_gray);
									
									if(ConstantValues.offersTabIndex == 3) {
										SidraPulseApp.getInstance().getOfferPromotionInfoList().remove(index);
										 if(SidraPulseApp.getInstance().getOfferPromotionInfoList().size() == 0){
											 lstView.setVisibility(View.GONE);
											 txtViewemptyMessage.setVisibility(View.VISIBLE);
										 }
									}
									OfferPromotionAdapter.this.notifyDataSetChanged();
								} else{
									Utilities.showToast(context, ConstantValues.failureMessage);
								}
							}
						}).execute();
					}
				} else {
					SidraPulseApp.getInstance().openDialogForInternetChecking(context);
				}
				//lstView.getPositionForView(v);
				//int index = holder.imgViewFavoriategetPositionForView(v);
			}
		});

		return convertView;
	}

	static class ViewHolder {
		ImageView imgViewOffer;
		TextView txtViewTitle;
		TextView txtViewUnlitLongTerm;
		TextView txtViewDate;
		ImageView imgViewFavoriate;
	}
	
}
