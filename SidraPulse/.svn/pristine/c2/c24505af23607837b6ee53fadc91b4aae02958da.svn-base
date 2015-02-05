package com.atomix.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.atomix.sidrapulse.R;

public class SidraEditText extends EditText {

	public SidraEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}
	
	public SidraEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
		
	}
	
	public SidraEditText(Context context) {
		super(context);
		init(null);
	}
	
	private void init(AttributeSet attrs) {
		if (attrs!=null) {
			 TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SidraTextView);
			 String fontName = a.getString(R.styleable.SidraTextView_fontName);
			 Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName);
			 
			 if (fontName!=null) {
				 setTypeface(myTypeface);
			 }
			 
			 a.recycle();
		}
	}
}

/*
 * Uses
 *  xmlns:customtextview="http://schemas.android.com/apk/res-auto"
 *  
 *     <com.atomix.customtextview.SidraTextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="14sp"
        android:padding="12dp"
        android:textColor="#000"
        fontName="ProximaNova-Black.otf"
        android:text="Custom Android Font fsdfsdafadf sdfasf a" />
 */
