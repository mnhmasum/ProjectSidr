package com.atomix.sidrapulse.stayinformed;

import java.lang.reflect.InvocationTargetException;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.sidrapulse.MainMenuActivity;
import com.atomix.sidrapulse.R;

@SuppressLint("SetJavaScriptEnabled")
public class SocialChannelsActivity extends Activity implements OnCheckedChangeListener, OnClickListener {

	private Button btnBack;
	private Button btnMenu;
	private RadioButton radioButtonTwitter;
	private RadioButton radioButtonYoutube;
	private RadioButton radioButtonFacebook;
	private WebView webView;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_channels);
		
		initViews();
		setListener();
		loadData();
	}

	private void initViews() {
		btnBack = (Button) findViewById(R.id.btn_back);
		btnMenu = (Button) findViewById(R.id.btn_menu);
		radioButtonTwitter = (RadioButton) findViewById(R.id.radio_btn_twitter);
		radioButtonYoutube = (RadioButton) findViewById(R.id.radio_btn_youtube);
		radioButtonFacebook = (RadioButton) findViewById(R.id.radio_btn_facebook);
		webView = (WebView) findViewById(R.id.web_view);
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
		((RadioGroup) findViewById(R.id.radio_group)).setOnCheckedChangeListener(this);
	}

	private void loadData() {
		webView.setWebChromeClient(new WebChromeClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.loadUrl("https://mobile.twitter.com/sidra");
		
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
				if (progressDialog == null) {
					progressDialog = SidraCustomProgressDialog.creator2(SocialChannelsActivity.this);
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
			startActivity(new Intent(SocialChannelsActivity.this, MainMenuActivity.class));
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switchTab();
	}
	
	private void switchTab() {
		int selectedTab = ((RadioGroup) findViewById(R.id.radio_group)).getCheckedRadioButtonId();
		
		switch (selectedTab) {
			case R.id.radio_btn_twitter:
				radioButtonTwitter.setButtonDrawable(R.drawable.twitter_act);
				radioButtonYoutube.setButtonDrawable(R.drawable.youtube);
				radioButtonFacebook.setButtonDrawable(R.drawable.facebook);
				loadWebview(0);
				break;
				
			case R.id.radio_btn_youtube:
				radioButtonTwitter.setButtonDrawable(R.drawable.twitter);
				radioButtonYoutube.setButtonDrawable(R.drawable.youtube_act);
				radioButtonFacebook.setButtonDrawable(R.drawable.facebook);
				loadWebview(1);
				break;
				
			case R.id.radio_btn_facebook:
				radioButtonTwitter.setButtonDrawable(R.drawable.twitter);
				radioButtonYoutube.setButtonDrawable(R.drawable.youtube);
				radioButtonFacebook.setButtonDrawable(R.drawable.facebook_act);
				loadWebview(2);
				break;
	
			default:
				break;
		}
	}

	private void loadWebview(int i) {
		switch (i) {
		case 0:
			webView.loadUrl("https://mobile.twitter.com/sidra");
			break;
			
		case 1:
			webView.loadUrl("http://www.youtube.com/channel/UCBf9kAG-cyTT03V6BmG8osg?app=mobile");
			break;
			
		case 2:
			webView.loadUrl("https://m.facebook.com/SidraMedicalAndResearchCenter");
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
