package com.atomix.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.sidrapulse.R;

public class MainMenuAdapter extends BaseAdapter {
	private int pageNo=0;
	private int[] sectionArray;
	private String[] menuTextArray;
	private LayoutInflater mInflater;
	private Context context;

	public MainMenuAdapter(Context context, int[] menuImages, String[] menuText, int pageNo) {
		this.context = context;
		this.sectionArray = menuImages;
		this.menuTextArray = menuText;
		this.pageNo = pageNo;
	}

	@Override
	public int getCount() {
		if(sectionArray != null) {
			return sectionArray.length;
		}
		return 0;

	}

	@Override
	public Object getItem(int position) {
		if (sectionArray != null && position >= 0 && position < getCount()) {
			return sectionArray[position];
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (sectionArray != null && position >= 0 && position < getCount()) {
			return sectionArray[position];
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.main_menu_row, null);

			holder = new ViewHolder();

			holder.imageView = (ImageView) convertView.findViewById(R.id.img_view);
			holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txt_view_title);
			holder.txtViewNotification = (TextView) convertView.findViewById(R.id.txt_view_notification);
			holder.layoutNotification = (RelativeLayout) convertView.findViewById(R.id.linear_notification);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.imageView.setBackgroundResource(sectionArray[position]);
		holder.txtViewTitle.setText(menuTextArray[position]);
		
		setNotification(position, pageNo, holder);
		
		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		TextView txtViewNotification;
		TextView txtViewTitle;
		RelativeLayout layoutNotification;
	}
	
	private void setNotification(int position, int pageNo, ViewHolder holder) {
		
		switch (pageNo) {
		case 1:
			
			if(position == 0) {
				if(SidraPulseApp.getInstance().getNotificationInfo().getAnnouncement() > 0) {
					holder.layoutNotification.setVisibility(View.VISIBLE);
					
					holder.txtViewNotification.setText("" + setLimitedBubble(SidraPulseApp.getInstance().getNotificationInfo().getAnnouncement()));
					
				} else {
					holder.layoutNotification.setVisibility(View.GONE);
				}
				
			} else if (position == 3) {
				if(SidraPulseApp.getInstance().getNotificationInfo().getClassified() > 0) {
					holder.layoutNotification.setVisibility(View.VISIBLE);
					holder.txtViewNotification.setText("" + setLimitedBubble(SidraPulseApp.getInstance().getNotificationInfo().getClassified()));
					
				} else {
					holder.layoutNotification.setVisibility(View.GONE);
				}
				
			} else if (position == 4) {
				if(SidraPulseApp.getInstance().getNotificationInfo().getOfferAndPromotion() > 0) {
					holder.layoutNotification.setVisibility(View.VISIBLE);
					holder.txtViewNotification.setText("" + setLimitedBubble(SidraPulseApp.getInstance().getNotificationInfo().getOfferAndPromotion()));
					
				} else {
					holder.layoutNotification.setVisibility(View.GONE);
				}
				
			} else if (position == 5) {
				if(SidraPulseApp.getInstance().getNotificationInfo().getEvent() > 0) {
					holder.layoutNotification.setVisibility(View.VISIBLE);
					holder.txtViewNotification.setText("" + setLimitedBubble(SidraPulseApp.getInstance().getNotificationInfo().getEvent()));
				}
				else {
					holder.layoutNotification.setVisibility(View.GONE);
				}
				
			} else if (position == 7) {
				if(SidraPulseApp.getInstance().getNotificationInfo().getGallery() > 0){
					holder.layoutNotification.setVisibility(View.VISIBLE);
					holder.txtViewNotification.setText("" + setLimitedBubble(SidraPulseApp.getInstance().getNotificationInfo().getGallery()));
					
				} else {
					holder.layoutNotification.setVisibility(View.GONE);
				}
				
			} else {
				holder.layoutNotification.setVisibility(View.GONE);
			}
			
			break;
			
		case 2:
			
			if(position == 1) {
				if(SidraPulseApp.getInstance().getNotificationInfo().getGallery() > 0) {
					holder.layoutNotification.setVisibility(View.VISIBLE);
					holder.txtViewNotification.setText("" + setLimitedBubble(SidraPulseApp.getInstance().getNotificationInfo().getGallery()));
					
				} else {
					holder.layoutNotification.setVisibility(View.GONE);
				}
				
			}else {
				holder.layoutNotification.setVisibility(View.GONE);
			}
			
			break;
			
		default:
			break;
		}
		
	}

	private int setLimitedBubble(int messageAmount) {
		if (messageAmount > 99) {
			return 99;
		}
		
		return messageAmount;
	}

}
