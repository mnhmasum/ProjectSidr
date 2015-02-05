//package com.atomix.adapter;
//
//import java.util.ArrayList;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.atomix.customview.SidraTextView;
//import com.atomix.sidrapulse.R;
//
//public class CustomSpinnerAdapter extends ArrayAdapter<String> {
//	private LayoutInflater inflater;
//	private Context context;
//	ArrayList<String> itemList;
//	int textViewResourceId;
//
//	public CustomSpinnerAdapter(Context context, int textViewResourceId,
//			ArrayList<String> itemList) {
//		super(context, textViewResourceId, itemList);
//		inflater = LayoutInflater.from(context);
//		this.context = context;
//		this.itemList = itemList;
//		this.textViewResourceId = textViewResourceId;
//	}
//
//	@Override
//	public View getDropDownView(int position, View convertView, ViewGroup parent) {
//		return getCustomView(position, convertView, parent);
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		return getCustomView(position, convertView, parent);
//	}
//
//	public View getCustomView(int position, View convertView, ViewGroup parent) {
//
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View row = inflater.inflate(textViewResourceId, parent, false);
//		SidraTextView txtViewItem = (SidraTextView) row.findViewById(R.id.txt_view_item);
//		txtViewItem.setText(itemList.get(position));
//
//		return row;
//	}
//
//}
