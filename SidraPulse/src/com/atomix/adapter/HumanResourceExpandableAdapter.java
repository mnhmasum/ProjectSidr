package com.atomix.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atomix.customview.SidraTextView;
import com.atomix.datamodel.HumanResourceInfo;
import com.atomix.sidrapulse.R;

public class HumanResourceExpandableAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	private ArrayList<HumanResourceInfo> parentItem;
	private ArrayList<String> dateItem = new ArrayList<String>();
	private ArrayList<HumanResourceInfo> childItem;

	public HumanResourceExpandableAdapter(Context context, Activity activity, ArrayList<HumanResourceInfo> parentItem, ArrayList<String> dateItem, ArrayList<HumanResourceInfo> childItem) {
		this.parentItem = parentItem;
		this.dateItem = dateItem;
		this.childItem = childItem;
		mContext = context;
		inflater = LayoutInflater.from(mContext);
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
			convertView = inflater.inflate(R.layout.human_resource_row_child, parent, false);
		}
		TextView txtViewAnswer = (TextView) convertView.findViewById(R.id.txt_view_answer);
		txtViewAnswer.setText(childItem.get(groupPosition).getAnswer().toString());
		Linkify.addLinks(txtViewAnswer, Linkify.ALL);
		
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
		Log.i("Parents Size","****" + parentItem.size());
		return parentItem.size();
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
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.human_resource_row_group, parent, false);
		}

		SidraTextView textViewQuestion = (SidraTextView) convertView.findViewById(R.id.txt_view_question);
		
		int questionNo = groupPosition+1;
		if( questionNo < 10) {
			textViewQuestion.setText("0"+questionNo+". "+parentItem.get(groupPosition).getQuestion().toString());
		} else {	
			textViewQuestion.setText(questionNo+". "+parentItem.get(groupPosition).getQuestion().toString());
		}
		
		
		ImageView imgViewArrow = (ImageView) convertView.findViewById(R.id.img_view_arrow);
		
		if(isExpanded) {
			imgViewArrow.setBackgroundResource(R.drawable.arrow_down);
		} else {
			imgViewArrow.setBackgroundResource(R.drawable.arrow_right);
		}

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
		return true;
	}
	
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
	}

}
