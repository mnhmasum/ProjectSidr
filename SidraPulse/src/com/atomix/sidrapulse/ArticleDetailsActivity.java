package com.atomix.sidrapulse;

import com.atomix.sidrainfo.SidraPulseApp;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class ArticleDetailsActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_details);
		
		setListener();
		loadData();
	}

	private void loadData() {
		if(getIntent().getExtras() != null) {
			int position = getIntent().getExtras().getInt("position");
			((TextView) findViewById(R.id.txt_view_article_title)).setText(SidraPulseApp.getInstance().getArticleInfoList().get(position).getQuestion());
			((TextView) findViewById(R.id.txt_view_title_article)).setText(SidraPulseApp.getInstance().getArticleInfoList().get(position).getQuestion());
			((TextView) findViewById(R.id.txt_view_date_time)).setText(SidraPulseApp.getInstance().getArticleInfoList().get(position).getUpdatedAt());
			((TextView) findViewById(R.id.txt_view_answer)).setText(SidraPulseApp.getInstance().getArticleInfoList().get(position).getAnswer());
			
			Linkify.addLinks((TextView) findViewById(R.id.txt_view_article_title), Linkify.ALL);
			Linkify.addLinks((TextView) findViewById(R.id.txt_view_title_article), Linkify.ALL);
			Linkify.addLinks((TextView) findViewById(R.id.txt_view_answer), Linkify.ALL);
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
			startActivity(new Intent(ArticleDetailsActivity.this, MainMenuActivity.class));
			finish();
			break;

		default:
			break;
		}
	}

}
