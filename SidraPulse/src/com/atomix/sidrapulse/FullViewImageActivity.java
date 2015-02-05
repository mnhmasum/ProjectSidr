package com.atomix.sidrapulse;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;

public class FullViewImageActivity extends Activity implements OnClickListener {
	private ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullview_image);
		intView();
		setListener();
		loadData();
	}

	private void intView() {
		imageView =(ImageView) findViewById(R.id.img_viewer);
		
	}

	private void loadData() {
		if(getIntent().getExtras() != null) {
			//
			int forumListPosition = getIntent().getExtras().getInt("ForumPosition");
			int position = getIntent().getExtras().getInt("commentsPosition");
			try {
		        new AQuery(FullViewImageActivity.this).id(imageView).image(ConstantValues.FILE_BASE_URL+SidraPulseApp.getInstance().getForumsInfoList().get(forumListPosition).getForumComments().get(position).getCommentsPhoto(), true, true, 0, R.drawable.no_image);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setListener() {
		((Button) findViewById(R.id.btn_back)).setOnClickListener(this);
		((Button) findViewById(R.id.btn_menu)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
			
		case R.id.btn_menu:
			//startActivity(new Intent(FullViewImageActivity.this, MainMenuActivity.class));
			//finish();
			break;

		default:
			break;
		}
	}

}
