package com.atomix.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atomix.datamodel.ForumHotTopics;
import com.atomix.sidrapulse.R;

public class ForumHotTopicsAdapter extends BaseAdapter {

	private ArrayList<ForumHotTopics> hotTopicsList;
	private LayoutInflater mInflater;
	private Context context;
	private Activity activity;
	

	public ForumHotTopicsAdapter(Activity activity, Context context, ArrayList<ForumHotTopics> hotTopicsList) {
		this.activity = activity;
		this.context = context;
		this.hotTopicsList = hotTopicsList;
	}

	@Override
	public int getCount() {
		if(hotTopicsList != null) {
			return hotTopicsList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return hotTopicsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.forum_hot_topics_row, null);

			holder = new ViewHolder();

			holder.txtViewTagName = (TextView) convertView.findViewById(R.id.txt_view_tag);
			holder.txtViewTotalCoversation = (TextView) convertView.findViewById(R.id.txt_view_total_hot_topics);
			holder.txtViewNo = (TextView) convertView.findViewById(R.id.txt_view_no);
			
			holder.relativeBg = (RelativeLayout) convertView.findViewById(R.id.relative_bg);
			holder.relativeBg.setTag(position);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtViewTagName.setText("#"+hotTopicsList.get(position).getTagName());
		SpannableString content = new SpannableString(holder.txtViewTagName.getText().toString());
		//content.setSpan(new UnderlineSpan(), 0,holder.txtViewTagName.getText().toString().length(), 0);
		holder.txtViewTagName.setTextColor(context.getResources().getColor(R.color.hash_tag_color));
		holder.txtViewTagName.setText(content);
	
		holder.txtViewTotalCoversation.setText(Integer.toString(hotTopicsList.get(position).getTotal()));
		int positionNo = position+1;
		if(positionNo > 9){
			holder.txtViewNo.setText(position+1);
		}
		else if(positionNo > 0 && positionNo <= 9){
			holder.txtViewNo.setText("0"+positionNo);
		}
		
		return convertView;
	}

	static class ViewHolder {
		TextView txtViewTagName;
		TextView txtViewTotalCoversation;
		TextView txtViewNo;
		RelativeLayout relativeBg;
	}
	
}
