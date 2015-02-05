package com.atomix.adapter;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atomix.customview.SidraTextView;
import com.atomix.datamodel.ClassifiedInfo;
import com.atomix.interfacecallback.UnReadRequest;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.sidrapulse.R;
import com.atomix.synctask.DeleteTask;
import com.atomix.utils.Utilities;

public class ClassifiedAdapter extends BaseAdapter {

	private ArrayList<ClassifiedInfo> classifiedInfoList;
	private LayoutInflater mInflater;
	private Context context;
	private Activity activity;
	private int type;
	private Dialog dialogConfirmation;
	private Button dialogBtnYes;
	private Button dialogBtnNo;
	private TextView dialogMsg;

	public ClassifiedAdapter(Activity activity, Context context, ArrayList<ClassifiedInfo> classifiedInfoList, int type) {
		this.activity = activity;
		this.context = context;
		this.classifiedInfoList = classifiedInfoList;
		this.type = type;
	}

	@Override
	public int getCount() {
		if(classifiedInfoList != null) {
			return classifiedInfoList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return classifiedInfoList.get(position);
		
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
			convertView = mInflater.inflate(R.layout.classified_row, null);

			holder = new ViewHolder();

			holder.txtViewTitle = (SidraTextView) convertView.findViewById(R.id.txt_view_title);
			holder.txtViewDate = (SidraTextView) convertView.findViewById(R.id.txt_view_date);
			holder.txtViewPostOwner = (SidraTextView) convertView.findViewById(R.id.txt_view_post_owner);
			holder.imgViewDelete = (ImageView) convertView.findViewById(R.id.img_view_delete);
			holder.txtViewDescription = (SidraTextView) convertView.findViewById(R.id.txt_view_description);
			holder.imgViewLabel = (ImageView) convertView.findViewById(R.id.img_view_label);
			holder.relativeBg = (RelativeLayout) convertView.findViewById(R.id.relative_bg);
			holder.imgViewDelete.setTag(position);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtViewTitle.setText(classifiedInfoList.get(position).getTitle());
		holder.txtViewDate.setText(classifiedInfoList.get(position).getCreatedAt());
		
		if(120 < classifiedInfoList.get(position).getDescription().length()) {
			String text=classifiedInfoList.get(position).getDescription().substring(0, 120) + " ";
			holder.txtViewDescription.setText(Html.fromHtml(text+"<font color='black'><b>Read More</b></font>"));
		}
		else {
			holder.txtViewDescription.setText(classifiedInfoList.get(position).getDescription());
		}
		
		if(classifiedInfoList.get(position).getCreatedBy() == SidraPulseApp.getInstance().getUserInfo().getUserID()) {
			holder.relativeBg.setBackgroundResource(R.drawable.class_text_holdar);
			holder.txtViewPostOwner.setText("You");
			holder.imgViewDelete.setVisibility(View.VISIBLE);
			holder.txtViewPostOwner.setTextColor(context.getResources().getColor(R.color.green));
			
		} else {
			holder.relativeBg.setBackgroundResource(R.drawable.class_text_holdar);
			holder.txtViewPostOwner.setText(classifiedInfoList.get(position).getOwnerInfo().getName());
			holder.txtViewPostOwner.setTextColor(context.getResources().getColor(R.color.cyan));
			holder.imgViewDelete.setVisibility(View.INVISIBLE);
		}
		
		if (type == 0) {
			holder.imgViewLabel.setVisibility(View.VISIBLE);
			if (1 == classifiedInfoList.get(position).getIsDraft()) {
				holder.imgViewLabel.setBackgroundResource(R.drawable.cla_draft_btn);
			} 
			else {
				if(1 == classifiedInfoList.get(position).getStatus()){
					holder.imgViewLabel.setBackgroundResource(R.drawable.cla_active_btn);
			}
				else {
					holder.imgViewLabel.setBackgroundResource(R.drawable.cla_inactive_btn);
				}
			}
		} else {
			holder.imgViewLabel.setVisibility(View.GONE);
		}
			
		
		
		holder.imgViewDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = ((Integer) v.getTag());

				dialogConfirmation = new Dialog(activity);
				dialogConfirmation.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialogConfirmation.setContentView(R.layout.confirm_delete_dialog);
				dialogConfirmation.setCanceledOnTouchOutside(true);
				dialogConfirmation.setCancelable(true);

				dialogConfirmation.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				dialogConfirmation.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				
				dialogMsg = (TextView) dialogConfirmation.findViewById(R.id.txt_view_dialog_message);
				dialogMsg.setText("Are you sure you want to delete your classified?");

				dialogBtnYes = (Button) dialogConfirmation.findViewById(R.id.btn_yes);
				dialogBtnNo = (Button) dialogConfirmation.findViewById(R.id.btn_no);

				dialogBtnYes.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (InternetConnectivity.isConnectedToInternet(activity)) {
							dialogConfirmation.dismiss();
							new DeleteTask(activity, new UnReadRequest() {
								@Override
								public void onTaskCompleted(int status) {
									if(status == 1){
										SidraPulseApp.getInstance().getClassifiedInfoList().remove(index);
										ClassifiedAdapter.this.notifyDataSetChanged();
									}
									else{
										Utilities.showToast(context, ConstantValues.failureMessage);
									}
									
								}
							}, "3", Integer.toString(SidraPulseApp.getInstance().getClassifiedInfoList().get(index).getId())).execute();
				 	
						} else {
							SidraPulseApp.getInstance().openDialogForInternetChecking(activity);
						}
					}
				});

				dialogBtnNo.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dialogConfirmation.dismiss();
					}
				});

				dialogConfirmation.show();

			}
				
		});		
		
		return convertView;
	}

	static class ViewHolder {
		SidraTextView txtViewTitle;
		SidraTextView txtViewPostOwner;
		SidraTextView txtViewDate;
		SidraTextView txtViewDescription;
		ImageView imgViewLabel;
		ImageView imgViewDelete;
		RelativeLayout relativeBg;
	}

}
