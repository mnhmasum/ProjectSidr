package com.atomix.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atomix.customview.SidraTextView;
import com.atomix.datamodel.StaffListInfo;
import com.atomix.interfacecallback.UnReadRequest;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.sidrapulse.R;
import com.atomix.synctask.FavoriateOrNotAsyncTask;
import com.atomix.utils.Utilities;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;


public class StaffDirectoryItemExpandableAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	private ArrayList<StaffListInfo> parentItem;
	private ArrayList<String> dateItem = new ArrayList<String>();
	private ArrayList<StaffListInfo> childItem;
	private Activity activity;
	private boolean isSavedContact;
	private TextView txtViewSavedNoContact;
	private PullToRefreshExpandableListView expandableListView;

	public StaffDirectoryItemExpandableAdapter(Context context, Activity activity, ArrayList<StaffListInfo> parentItem, ArrayList<String> dateItem, ArrayList<StaffListInfo> childItem, boolean isSavedContact, TextView txtViewSavedNoContact, PullToRefreshExpandableListView expandableListView) {
		this.parentItem = parentItem;
		this.dateItem = dateItem;
		this.childItem = childItem;
		mContext = context;
		this.activity = activity;
		this.isSavedContact = isSavedContact;
		inflater = LayoutInflater.from(mContext);
		this.txtViewSavedNoContact = txtViewSavedNoContact;
		this.expandableListView = expandableListView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.staff_directory_row_child, parent, false);
		}
		final SidraTextView txtViewEmail = (SidraTextView) convertView.findViewById(R.id.txt_view_email);
		
		if(childItem.get(groupPosition).getEmail().toString()!= null && !childItem.get(groupPosition).getEmail().toString().equalsIgnoreCase("")){
			SpannableString content = new SpannableString(childItem.get(groupPosition).getEmail().toString());
			content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
			txtViewEmail.setText(content);
			
			txtViewEmail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent_email = new Intent(Intent.ACTION_SEND);
					intent_email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent_email.setType("plain/text");
					intent_email.putExtra(Intent.EXTRA_EMAIL, new String[] {txtViewEmail.getText().toString()});
					//intent_email.putExtra(Intent.EXTRA_SUBJECT, SidraPulseApp.getInstance().getClassifiedInfoList().get(position).getTitle());
					try {
						activity.startActivity(Intent.createChooser(intent_email, ""));
					} catch (android.content.ActivityNotFoundException ex) {
					    Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
					}
				}
			});
		} else {
			txtViewEmail.setText("unavailable");
		}
		
		final SidraTextView txtViewOffice = (SidraTextView) convertView.findViewById(R.id.txt_view_office);
		if(childItem.get(groupPosition).getOffice() != 0) {
			SpannableString content1 = new SpannableString(""+childItem.get(groupPosition).getOffice());
			content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
			txtViewOffice.setText(content1);
			
			txtViewOffice.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent_phone = new Intent(Intent.ACTION_DIAL);
					intent_phone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
					intent_phone.setData(Uri.parse("tel:"+ txtViewOffice.getText().toString()));
					mContext.startActivity(intent_phone);
				}
			});
		} else {
			txtViewOffice.setText("unavailable");
		}
		
		final SidraTextView txtViewMobile = (SidraTextView) convertView.findViewById(R.id.txt_view_mobile);
		if(childItem.get(groupPosition).getMobile() != null && !childItem.get(groupPosition).getMobile().equalsIgnoreCase("")) {
			SpannableString content2 = new SpannableString(""+childItem.get(groupPosition).getMobile());
				content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
				txtViewMobile.setText(content2);
				
				txtViewMobile.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent_phone = new Intent(Intent.ACTION_DIAL);
						intent_phone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
						intent_phone.setData(Uri.parse("tel:"+ txtViewMobile.getText().toString()));
						mContext.startActivity(intent_phone);
					}
				});
		} else {
			txtViewMobile.setText("unavailable");
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		if(parentItem.size() > 0) {
			return parentItem.size();
		} else {
			return 0;
		}
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.staff_directory_row_group, parent, false);
		}

		SidraTextView textViewName = (SidraTextView) convertView.findViewById(R.id.txt_view_name);
		textViewName.setText(parentItem.get(groupPosition).getName().toString());
		
		SidraTextView textViewDesignation = (SidraTextView) convertView.findViewById(R.id.txt_view_designation);
		textViewDesignation.setText(parentItem.get(groupPosition).getDesignation().toString());
		
		SidraTextView textViewDepartment = (SidraTextView) convertView.findViewById(R.id.txt_view_department);
		textViewDepartment.setText(parentItem.get(groupPosition).getDepartment().toString());
		
		final ImageView imgViewFavoriate = (ImageView) convertView.findViewById(R.id.img_view_favoriate);
		imgViewFavoriate.setTag(groupPosition);
		
		if(parentItem.get(groupPosition).getIsBookmarked() == 1) {
			imgViewFavoriate.setBackgroundResource(R.drawable.star_icon);
		} else {
			imgViewFavoriate.setBackgroundResource(R.drawable.star_gray_icon);
		}
		
		imgViewFavoriate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = (Integer) v.getTag();
				Log.e("Position is", "::::"+index);
				if (InternetConnectivity.isConnectedToInternet(activity)) {
					String id = Integer.toString(parentItem.get(index).getId());
					Log.i("id is ", "____________"+id);
					if(parentItem.get(index).getIsBookmarked() == 0) {
						new FavoriateOrNotAsyncTask(activity, id, "1", ConstantValues.FUNC_ID_STAFF_BOOKMARE, new UnReadRequest() {
							@Override
							public void onTaskCompleted(int status) {
								if(status == 1) {
									parentItem.get(index).setIsBookmarked(1);
									imgViewFavoriate.setBackgroundResource(R.drawable.star_icon);
							        Utilities.showToast(mContext, parentItem.get(index).getName().toString()+" "+activity.getResources().getString(R.string.toast_saved_contacts));
							        StaffDirectoryItemExpandableAdapter.this.notifyDataSetChanged();
								} else {
									Utilities.showToast(mContext, ConstantValues.failureMessage);
								}
								
							}
						}).execute();
					} else {
						new FavoriateOrNotAsyncTask(activity, id, "0", ConstantValues.FUNC_ID_STAFF_BOOKMARE, new UnReadRequest() {
							@Override
							public void onTaskCompleted(int status) {
								if(status == 1){
									parentItem.get(index).setIsBookmarked(0);
									imgViewFavoriate.setBackgroundResource(R.drawable.star_gray_icon);
									Utilities.showToast(mContext, parentItem.get(index).getName().toString()+" "+activity.getResources().getString(R.string.toast_removed_contacts));
									if(isSavedContact) {
										parentItem.remove(index);
										if(parentItem.size() == 0){
											expandableListView.setVisibility(View.GONE);
											txtViewSavedNoContact.setVisibility(View.VISIBLE);
										}
									}
									StaffDirectoryItemExpandableAdapter.this.notifyDataSetChanged();
								} else {
									Utilities.showToast(mContext, ConstantValues.failureMessage);
								}
							}
						}).execute();
					}
				} else {
					SidraPulseApp.getInstance().openDialogForInternetChecking(mContext);
				}
		
			}
		});

		if (dateItem != null) {
			//TextView textViewDate = (TextView) convertView .findViewById(R.id.groupTextDate);
			//textViewDate.setText(dateItem.get(groupPosition).toString());
		}

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
