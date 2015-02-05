package com.atomix.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atomix.customview.SidraTextView;
import com.atomix.sidrapulse.R;

public class StayInformedAdapter extends BaseAdapter {

	private ArrayList<String> arrayList;
	private LayoutInflater mInflater;
	private Context context;

	public StayInformedAdapter(Context context, ArrayList<String> arrayList) {
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
			convertView = mInflater.inflate(R.layout.stay_informed_row, null);

			holder = new ViewHolder();

			holder.txtViewTitle = (SidraTextView) convertView.findViewById(R.id.txt_view_title);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtViewTitle.setText(arrayList.get(position).toString());

		return convertView;
	}

	static class ViewHolder {
		SidraTextView txtViewTitle;
	}

}
