package com.atomix.multipleimagepicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.atomix.sidrapulse.ClassifiedsCreateNewPostActivity;
import com.atomix.sidrapulse.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class CustomGalleryActivity extends Activity {

	GridView gridGallery;
	Handler handler;
	GalleryAdapter adapter;

	ImageView imgNoMedia;
	Button btnGalleryOk;
	Button btnGalleryCancel;
	Button btnCaptureImage;
	Button btnBack;
	private Uri mcaptureUri = null;
	private final int PICK_FROM_CAMERA = 1;

	String action;
	private ImageLoader imageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gallery);

		action = getIntent().getAction();
		if (action == null) {
			finish();
		}
		
		initImageLoader();
		init();
	}

	private void initImageLoader() {
		try {
			String CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.temp_tmp";
			new File(CACHE_DIR).mkdirs();

			File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(), CACHE_DIR);

			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).bitmapConfig(Bitmap.Config.RGB_565).build();
			ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(getBaseContext())
					.defaultDisplayImageOptions(defaultOptions)
					.discCache(new UnlimitedDiscCache(cacheDir))
					.memoryCache(new WeakMemoryCache());
			
			ImageLoaderConfiguration config = builder.build();
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);

		} catch (Exception e) {

		}
	}

	private void init() {

		handler = new Handler();
		gridGallery = (GridView) findViewById(R.id.gridGallery);
		gridGallery.setFastScrollEnabled(true);
		adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
		PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, true, true);
		gridGallery.setOnScrollListener(listener);

		findViewById(R.id.llBottomContainer).setVisibility(View.VISIBLE);
		gridGallery.setOnItemClickListener(mItemMulClickListener);
		adapter.setMultiplePick(true);

		gridGallery.setAdapter(adapter);
		imgNoMedia = (ImageView) findViewById(R.id.imgNoMedia);
		
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBackClickListener);
		
		btnCaptureImage = (Button) findViewById(R.id.btn_capture_img);
		btnCaptureImage.setOnClickListener(mCaptureImageClickListener);

		btnGalleryOk = (Button) findViewById(R.id.btnGalleryOk);
		btnGalleryOk.setOnClickListener(mOkClickListener);
		
		btnGalleryCancel = (Button) findViewById(R.id.btnGalleryCancel);
		btnGalleryCancel.setOnClickListener(mCancelClickListener);
		
	
		if (action.equalsIgnoreCase("luminous.ACTION_MULTIPLE_PICK")) {
			findViewById(R.id.llBottomContainer).setVisibility(View.VISIBLE);
			gridGallery.setOnItemClickListener(mItemMulClickListener);
			adapter.setMultiplePick(true);
		} else if (action.equalsIgnoreCase("luminous.ACTION_PICK")) {

			findViewById(R.id.llBottomContainer).setVisibility(View.GONE);
			gridGallery.setOnItemClickListener(mItemSingleClickListener);
			adapter.setMultiplePick(false);
		}

		

		new Thread() {

			@Override
			public void run() {
				Looper.prepare();
				handler.post(new Runnable() {

					@Override
					public void run() {
						adapter.addAll(getGalleryPhotos());
						checkImageStatus();
					}
				});
				Looper.loop();
			};

		}.start();

	}

	private void checkImageStatus() {
		if (adapter.isEmpty()) {
			imgNoMedia.setVisibility(View.VISIBLE);
		} else {
			imgNoMedia.setVisibility(View.GONE);
		}
	}

	View.OnClickListener mOkClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			ArrayList<CustomGallery> selected = adapter.getSelected();

			String[] allPath = new String[selected.size()];
			for (int i = 0; i < allPath.length; i++) {
				allPath[i] = selected.get(i).sdcardPath;
			}

			Intent data = new Intent().putExtra("all_path", allPath);
			setResult(RESULT_OK, data);
			finish();

		}
	};
	
	View.OnClickListener mBackClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			finish();

		}
	};
	
	View.OnClickListener mCancelClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			finish();

		}
	};
	
	View.OnClickListener mCaptureImageClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			 Uri mcaptureUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());

//			 mcaptureUri = Uri.fromFile(new File(Environment
//					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/Sidra_Classsifieds_"
//					+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mcaptureUri);
			
			try {
				intent.putExtra("return-data", true);
				startActivityForResult(intent, PICK_FROM_CAMERA);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}

		}
	};

	
	AdapterView.OnItemClickListener mItemMulClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
			//adapter.changeSelection(v, position);
			 if (adapter.getSelected().size() >= ClassifiedsCreateNewPostActivity.photoAttachRemainning) {
				 adapter.selectionUnselect(v, position);
	              
	            } else {
	                adapter.changeSelection(v, position);
	               
	            }

		}
	};
	


	AdapterView.OnItemClickListener mItemSingleClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
			CustomGallery item = adapter.getItem(position);
			Intent data = new Intent().putExtra("single_path", item.sdcardPath);
			setResult(RESULT_OK, data);
			finish();
		}
	};

	private ArrayList<CustomGallery> getGalleryPhotos() {
		ArrayList<CustomGallery> galleryList = new ArrayList<CustomGallery>();

		try {
			final String[] columns = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID };
			final String orderBy = MediaStore.Images.Media._ID;

			Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

			if (imagecursor != null && imagecursor.getCount() > 0) {

				while (imagecursor.moveToNext()) {
					CustomGallery item = new CustomGallery();

					int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);

					item.sdcardPath = imagecursor.getString(dataColumnIndex);

					galleryList.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// show newest photo at beginning of the list
		Collections.reverse(galleryList);
		return galleryList;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == PICK_FROM_CAMERA) {
				
//				  Uri selectedImage = data.getData();
//	                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//	                   Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//	                   cursor.moveToFirst();
//
//	                   int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//	                  //file path of captured image
//	                  String  filePath = cursor.getString(columnIndex); 
//	                   //file path of captured image
//	                   File f = new File(filePath);
//	                  String filename= f.getName();
//
//	                   Toast.makeText(CustomGalleryActivity.this, "Your Path:"+filePath, 2000).show();
//	                   Toast.makeText(CustomGalleryActivity.this, "Your Filename:"+filename, 2000).show();
//	                   cursor.close();

	                 //Convert file path into bitmap image using below line.
	                  // yourSelectedImage = BitmapFactory.decodeFile(filePath);

	                   
				initImageLoader();
				init();

				
			
			}
		}
	}

}
