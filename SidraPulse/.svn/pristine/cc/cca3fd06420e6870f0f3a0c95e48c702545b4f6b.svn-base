package com.atomix.sidrapulse;

import java.lang.reflect.InvocationTargetException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("SetJavaScriptEnabled")
public class NewsLetterActivity extends Activity implements OnClickListener {

	private Button btnBack;
	private WebView webView;
	private ProgressDialog progressDialog;
	private LinearLayout linear_loader;
	private AnimationDrawable loadingViewAnim = null;
	private ImageView imgViewLoading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_letter);
		
		initViews();
		setListener();
		loadData();
	}

	private void initViews() {
		imgViewLoading = (ImageView) findViewById(R.id.img_view_loading);
		linear_loader = (LinearLayout) findViewById(R.id.linear_loader);
		
		imgViewLoading.setImageBitmap(null);
		imgViewLoading.setBackgroundResource(R.anim.loading_frame);
		loadingViewAnim = (AnimationDrawable) imgViewLoading.getBackground();
		
		btnBack = (Button) findViewById(R.id.btn_back);
		webView = (WebView) findViewById(R.id.web_view);
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
	}

	private void loadData() {
		webView.setWebChromeClient(new WebChromeClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.loadUrl("http://0380e28.netsolhost.com/jananewsletter/");
		
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(NewsLetterActivity.this);
					progressDialog.setMessage("Loading...");
					//progressDialog.show();
	            }
				
				loadingViewAnim.start();
				linear_loader.setVisibility(View.VISIBLE);
				
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				try {
					loadingViewAnim.stop();
					linear_loader.setVisibility(View.GONE);
		            /*if (progressDialog.isShowing()) {
		            	progressDialog.dismiss();
		            	progressDialog = null;
		            }*/
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

		default:
			break;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		try {
	        Class.forName("android.webkit.WebView").getMethod("onPause", (Class[]) null).invoke(webView, (Object[]) null);
	    } catch(ClassNotFoundException cnfe) {
	    	
	    } catch(NoSuchMethodException nsme) {
	    	
	    } catch(InvocationTargetException ite) {
	    	
	    } catch (IllegalAccessException iae) {
	    	
	    }
	}
}
