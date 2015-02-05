package com.atomix.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.atomix.sidrapulse.R;

public class SpinnerAdapter  extends ArrayAdapter<String>
{
    private Activity context;
   // private String [] spinnerData = null;
    LayoutInflater 	mInflater;
    private List<String> spinnerData;

    public SpinnerAdapter (Activity context, int resource, List<String> array)
    {
        super(context, resource, array);
        this.context = context;
        this.spinnerData = array;
    }
    
    

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {   // Ordinary view in Spinner, we use android.R.layout.simple_spinner_item
    	ViewHolder holder;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.spinner_simple_row, null);
			holder = new ViewHolder();
			holder.txtViewParentName = (TextView) convertView.findViewById(R.id.txt_view_category);
			convertView.setTag(holder);
		
			

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtViewParentName.setText(spinnerData.get(position));
		
		Typeface externalFont = Typeface.createFromAsset(context.getAssets(), "fonts/ProximaNova-Semibold.otf");
		holder.txtViewParentName.setTypeface(externalFont);
		holder.txtViewParentName.setTextSize(18);

		return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {   // This view starts when we click the spinner.

		ViewHolder holder;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.spinner_dropdown_row, null);
			holder = new ViewHolder();
			holder.txtViewPaymentsName = (TextView) convertView.findViewById(R.id.txt_view_category_name);
			holder.imgViewLogo = (ImageView) convertView.findViewById(R.id.img_view_logo);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtViewPaymentsName.setText("  "+spinnerData.get(position));
		
		
		Typeface externalFont = Typeface.createFromAsset(context.getAssets(), "fonts/ProximaNova-Semibold.otf");
		holder.txtViewPaymentsName.setTypeface(externalFont);
		holder.txtViewPaymentsName.setTextSize(18);

		return convertView;
	}

	static class ViewHolder {
		TextView txtViewPaymentsName;
		ImageView imgViewLogo;
		TextView txtViewParentName;
	}
	
}

