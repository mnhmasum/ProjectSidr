package com.atomix.sidrapulse;

import android.os.Bundle;
import android.text.Html;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

public class TermsOfUseActivity extends Activity implements OnClickListener {

	private Button btnBack;
	private TextView txtViewtermsContents;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms_of_use);
		
		initViews();
		setListener();
	}

	private void initViews() {
		btnBack = (Button) findViewById(R.id.btn_back);
		txtViewtermsContents = (TextView) findViewById(R.id.txt_view_content);
		txtViewtermsContents.setText(Html.fromHtml("<h2>Sidra Terms of Use</h2><p align='left'>Avoid losing floppy disks with important school/work projects on them. Use the web to keep your content so you can access it from anywhere in the world. It's also a quick way to write reminders or notes to yourself. With simple html skills, the ones you have gained so far it is easy. For instance, let's say you had a HUGE school or work project to complete. Off hand, the easiest way to transfer the documents from your house could be a 3.5 floppy disk. However, there is an alternative. Place your documents, photos, essays, or videos onto your web server and access them from anywhere in the world.Avoid losing floppy disks with important school/work projects on them. Use the web to keep your content so you can access it from anywhere in the world. It's also a quick way to write reminders or notes to yourself. With simple html skills, (the ones you have gained so far) it is easy.For instance, lets say you had a HUGE school or work project to complete. Off hand, the easiest way to transfer the documents from your house could be a 3.5 floppy disk. However, there is an alternative. Place your documents, photos, essays, or videos onto your web server and access them from anywhere in the world.</p>"));
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
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

}
