package com.atomix.sidrapulse;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.atomix.adapter.GalleryDetailsAdapter;
import com.atomix.interfacecallback.OnRequestComplete;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.synctask.GalleryDetailsAsyncTask;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

public class GalleryDetailsActivity extends Activity implements OnItemClickListener, OnClickListener {

	private Button btnBack;
	private Button btnMenu;
	private TextView txtViewTitle;
	private TextView txtViewDate;
	private TextView txtViewContentNumber;
	private PullToRefreshGridView gridView;
	private GalleryDetailsAdapter galleryDetailsAdapter;
	private int pageNo = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery_details);
		
		initViews();
		setListener();
		loadData();
	}

	private void initViews() {
		btnBack = (Button) findViewById(R.id.btn_back);
		btnMenu = (Button) findViewById(R.id.btn_menu);
		txtViewTitle = (TextView) findViewById(R.id.txt_view_title);
		txtViewDate = (TextView) findViewById(R.id.txt_view_date);
		txtViewContentNumber = (TextView) findViewById(R.id.txt_view_content_number);
		gridView = (PullToRefreshGridView) findViewById(R.id.grid_view);
		gridView.setMode(Mode.BOTH);
	}

	private void setListener() {
		btnBack.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
		gridView.setOnItemClickListener(this);
		// Set a listener to be invoked when the list should be refreshed.
		gridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				
				if (!InternetConnectivity.isConnectedToInternet(GalleryDetailsActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(GalleryDetailsActivity.this);
					gridView.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getGalleryDetailsInfoList() == null) {
					gridView.onRefreshComplete();
					return;
					
				}
				
				String first_element_id = Integer.toString(SidraPulseApp.getInstance().getGalleryDetailsInfoList().get(0).getId());
				
				GalleryDetailsAsyncTask galleryDetailsRequest = new GalleryDetailsAsyncTask(GalleryDetailsActivity.this, new OnRequestComplete() {
					
					@Override
					public void onRequestComplete(int responseStatus) {
						if(responseStatus == 1) {
							galleryDetailsAdapter.notifyDataSetChanged();
							gridView.onRefreshComplete();
						} else if(responseStatus == 5) {
							SidraPulseApp.getInstance().accessTokenChange(GalleryDetailsActivity.this);
							
						} else {
							gridView.onRefreshComplete();
						}
					}
					
				});
				galleryDetailsRequest.execute(ConstantValues.FUNC_ID_GALLERY_IMAGES, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(SidraPulseApp.getInstance().getGalleryInfoList().get(getIntent().getExtras().getInt("click_position")).getId()), first_element_id, "0");
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				
				if (!InternetConnectivity.isConnectedToInternet(GalleryDetailsActivity.this)) {
					SidraPulseApp.getInstance().openDialogForInternetChecking(GalleryDetailsActivity.this);
					gridView.onRefreshComplete();
					return;
				} 
				
				if (SidraPulseApp.getInstance().getGalleryDetailsInfoList() == null) {
					gridView.onRefreshComplete();
					return;
					
				}
				
				String last_element_id = Integer.toString(SidraPulseApp.getInstance().getGalleryDetailsInfoList().get(SidraPulseApp.getInstance().getGalleryDetailsInfoList().size() - 1).getId());
				GalleryDetailsAsyncTask galleryDetailsRequest = new GalleryDetailsAsyncTask(GalleryDetailsActivity.this, new OnRequestComplete() {
					
					@Override
					public void onRequestComplete(int responseStatus) {
						if(responseStatus == 1) {
							galleryDetailsAdapter.notifyDataSetChanged();
							gridView.onRefreshComplete();
							
						} else if(responseStatus == 5) {
							SidraPulseApp.getInstance().accessTokenChange(GalleryDetailsActivity.this);
							
						} else {
							gridView.onRefreshComplete();
						}
					}
					
				});
				galleryDetailsRequest.execute(ConstantValues.FUNC_ID_GALLERY_IMAGES, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(SidraPulseApp.getInstance().getGalleryInfoList().get(getIntent().getExtras().getInt("click_position")).getId()), last_element_id, "1");
				
				/*if (SidraPulseApp.getInstance().getGalleryDetailsInfoList().size() >= pageNo*10) {
					pageNo++;
					//new GetDataTask().execute();
					GalleryDetailsAsyncTask galleryDetailsRequest = new GalleryDetailsAsyncTask(GalleryDetailsActivity.this, new OnRequestComplete() {
						
						@Override
						public void onRequestComplete(int responseStatus) {
							if(responseStatus == 1) {
								galleryDetailsAdapter.notifyDataSetChanged();
							} else if(responseStatus == 5) {
								SidraPulseApp.getInstance().accessTokenChange(GalleryDetailsActivity.this);
								
							}
						}
						
					});
					
					galleryDetailsRequest.execute(ConstantValues.FUNC_ID_GALLERY_IMAGES, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(SidraPulseApp.getInstance().getGalleryInfoList().get(getIntent().getExtras().getInt("click_position")).getId()), "", "");
					
				} else {
					Log.i("GALLERY_DATA","NO");
					gridView.onRefreshComplete();
				}*/
				
			}

		});
		
	}
	
	private void loadData() {
		ConstantValues.PullDownActive = true;
		if(getIntent().getExtras() != null) {
			int position = getIntent().getExtras().getInt("click_position");
			txtViewTitle.setText(SidraPulseApp.getInstance().getGalleryInfoList().get(position).getAlbumTitle());
			txtViewDate.setText("Created on: "+SidraPulseApp.getInstance().getGalleryInfoList().get(position).getDate());
			txtViewContentNumber.setText(SidraPulseApp.getInstance().getGalleryInfoList().get(position).getPhotoNumber() +" Photos, "+SidraPulseApp.getInstance().getGalleryInfoList().get(position).getVideoNumber() +" videos");
			//new GalleryDetailsApiTask().execute();
			if (InternetConnectivity.isConnectedToInternet(GalleryDetailsActivity.this)) {
				GalleryDetailsAsyncTask galleryDetailsRequest = new GalleryDetailsAsyncTask(GalleryDetailsActivity.this, new OnRequestComplete() {
					
					@Override
					public void onRequestComplete(int responseStatus) {
						if(responseStatus == 1) {
							// load data to adapter 
							galleryDetailsAdapter = new GalleryDetailsAdapter(GalleryDetailsActivity.this, SidraPulseApp.getInstance().getGalleryDetailsInfoList());
							if(SidraPulseApp.getInstance().getGalleryDetailsInfoList() != null) {
								gridView.setAdapter(galleryDetailsAdapter);
							} else {
								gridView.setAdapter(null);
							}
						} else if(responseStatus == 5) {
							SidraPulseApp.getInstance().accessTokenChange(GalleryDetailsActivity.this);
						}
					}
					
				});
				galleryDetailsRequest.execute(ConstantValues.FUNC_ID_GALLERY_IMAGES, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), Integer.toString(SidraPulseApp.getInstance().getGalleryInfoList().get(getIntent().getExtras().getInt("click_position")).getId()), "", "");
				
			} else {
				SidraPulseApp.getInstance().openDialogForInternetChecking(GalleryDetailsActivity.this);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
			
		case R.id.btn_menu:
			startActivity(new Intent(GalleryDetailsActivity.this, MainMenuActivity.class));
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
		Log.i("MEDIA_POSITION","***"+ position);
		
		if(SidraPulseApp.getInstance().getGalleryDetailsInfoList().get(position).getMediaType() == 2) {
	        Intent intent = new Intent(Intent.ACTION_VIEW);
	        intent.setDataAndType(Uri.parse(ConstantValues.FILE_BASE_URL+SidraPulseApp.getInstance().getGalleryDetailsInfoList().get(position).getImageOrVideoUrl().toString()), "video/*");
	        startActivity(intent);
		} else {
			Intent intent = new Intent(GalleryDetailsActivity.this, ViewPagerActivity.class);
			intent.putExtra("click_position", position);
			intent.putExtra("type_is", 4);
			startActivity(intent);
		}
		
	}
	
	
	/*private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			
			GalleryDetailsInfo gi = new GalleryDetailsInfo();
			gi.setImageOrVideoUrl("");
			gi.setMediaType(1);
			
			SidraPulseApp.getInstance().getGalleryDetailsInfoList().add(gi);
			// Call onRefreshComplete when the list has been refreshed.
			galleryDetailsAdapter.notifyDataSetChanged();
			gridView.onRefreshComplete();
			
			super.onPostExecute(result);
		}
	}*/

}
