package com.atomix.sidrapulse.stayinformed;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.sidrapulse.MainMenuActivity;
import com.atomix.sidrapulse.R;

@SuppressLint("SetJavaScriptEnabled")
public class SidraNewsDetailsActivity extends Activity implements OnClickListener {

	private Button btnBack;
	private Button btnMenu;
	private WebView webView;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sidra_news_details);
		
		initViews();
		setListener();
		loadData();
	}

	private void initViews() {
		btnBack = (Button) findViewById(R.id.btn_back);
		btnMenu = (Button) findViewById(R.id.btn_menu);
		webView = (WebView) findViewById(R.id.web_view);
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
	}

	private void loadData() {
		webView.setWebChromeClient(new WebChromeClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		if(getIntent().getExtras() != null) {
			webView.loadUrl(SidraPulseApp.getInstance().getSidraInNewsAPI().get(getIntent().getExtras().getInt("position")).getLink().toString());
		}
		
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
				if (progressDialog == null) {
					progressDialog = SidraCustomProgressDialog.creator2(SidraNewsDetailsActivity.this);
	            }
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				try {
		            if (progressDialog.isShowing()) {
		            	progressDialog.dismiss();
		            	progressDialog = null;
		            }
				}
				catch(Exception exception) 
				{
					exception.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
			
		case R.id.btn_menu:
			startActivity(new Intent(SidraNewsDetailsActivity.this, MainMenuActivity.class));
			finish();
			break;

		default:
			break;
		}
	}

}
