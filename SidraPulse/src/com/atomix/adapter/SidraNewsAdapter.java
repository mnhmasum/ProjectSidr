package com.atomix.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atomix.customview.SidraTextView;
import com.atomix.datamodel.SidraInNewsAPIInfo;
import com.atomix.sidrapulse.R;

public class SidraNewsAdapter extends BaseAdapter {

	private ArrayList<SidraInNewsAPIInfo> arrayList;
	private LayoutInflater mInflater;
	private Context context;

	public SidraNewsAdapter(Context context, ArrayList<SidraInNewsAPIInfo> arrayList) {
		this.context = context;
		this.arrayList = arrayList;
	}

	@Override
	public int getCount() {
		if(arrayList != null) {
			return arrayList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
		
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
			convertView = mInflater.inflate(R.layout.sidra_news_row, null);

			holder = new ViewHolder();

			holder.txtViewTitle = (SidraTextView) convertView.findViewById(R.id.txt_view_title);
			holder.txtViewDate = (SidraTextView) convertView.findViewById(R.id.txt_view_date);
			holder.txtViewSource = (SidraTextView) convertView.findViewById(R.id.txt_view_source);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtViewTitle.setText(arrayList.get(position).getHeadline().trim().toString());
		holder.txtViewDate.setText(arrayList.get(position).getReleaseDate().trim().toString());
		holder.txtViewSource.setText(arrayList.get(position).getSourcePublication().trim().toString());

		return convertView;
	}

	static class ViewHolder {
		SidraTextView txtViewTitle;
		SidraTextView txtViewDate;
		SidraTextView txtViewSource;
	}

}
