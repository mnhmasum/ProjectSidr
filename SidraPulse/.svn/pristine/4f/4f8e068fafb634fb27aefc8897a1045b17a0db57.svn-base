package com.atomix.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.atomix.datamodel.GalleryInfo;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.sidrapulse.R;

public class GalleryAdapter extends BaseAdapter {

	private ArrayList<GalleryInfo> galleryInfoList;
	private LayoutInflater mInflater;
	private Context context;

	public GalleryAdapter(Context context, ArrayList<GalleryInfo> galleryInfoList) {
		this.context = context;
		this.galleryInfoList = galleryInfoList;
	}

	@Override
	public int getCount() {
		return galleryInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return galleryInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.gallery_row, null);

			holder = new ViewHolder();

			holder.imageView = (ImageView) convertView.findViewById(R.id.img_view_gallery_single);
			holder.txtViewContentNumber = (TextView) convertView.findViewById(R.id.txt_view_content_number);
			holder.txtViewAlbumTitle = (TextView) convertView.findViewById(R.id.txt_view_title);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.txtViewAlbumTitle.setText(galleryInfoList.get(position).getAlbumTitle());
		holder.txtViewContentNumber.setText(galleryInfoList.get(position).getPhotoNumber() +" Photos, "+galleryInfoList.get(position).getVideoNumber() +" videos");
		if (InternetConnectivity.isConnectedToInternet(context)) {
			AQuery aq = new AQuery(context);
			final Bitmap placeholder = aq.getCachedImage(R.drawable.no_image);
			aq.id(holder.imageView).image(ConstantValues.FILE_BASE_URL+galleryInfoList.get(position).getAlbumImageUrl(), true, true, 200, 300, new BitmapAjaxCallback(){

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
			    
			});
		} else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(context);
		}
		
		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		TextView txtViewAlbumTitle;
		TextView txtViewContentNumber;
	}
	
}
