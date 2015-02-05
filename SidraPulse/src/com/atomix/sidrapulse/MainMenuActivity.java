package com.atomix.sidrapulse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.atomix.adapter.MainMenuAdapter;
import com.atomix.customview.DialogController;
import com.atomix.customview.SidraCustomProgressDialog;
import com.atomix.interfacecallback.OnRequestComplete;
import com.atomix.interfacecallback.OnUploadComplete;
import com.atomix.interfacecallback.UnReadRequest;
import com.atomix.internetconnection.InternetConnectivity;
import com.atomix.jsonparse.CommunicationLayer;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.synctask.ImageUploadAsyncTask;
import com.atomix.synctask.MainMenuAsyncTask;
import com.atomix.synctask.UnReadTask;
import com.atomix.utils.Utilities;

public class MainMenuActivity extends Activity implements OnClickListener {
	private int imageAdded;
	private Button btnBack;
	private Button btnSettings;
	private Button btnSearch;
	private Button btnShareSidraForum;
	private Button dialogBtnSend;
	private EditText edTxtSearchDirectory;
	private RelativeLayout relativeMainMenu;
	private ViewPager viewPager;
	private GridView menuGridView1, menuGridView2;
	private ImageView indicator1, indicator2;
	private Vector<View> pages;
	private MainMenuAdapter mainMenuAdapter1;
	private MainMenuAdapter mainMenuAdapter2;
	private Dialog dialogReply;
	private LinearLayout linearImageAdd;
	private EditText editTxtDialogReply;
	private final int CHOOSE_SINGLE = 100;
	private Dialog dialogPostingNotice;
	public ArrayList<String> takePath;
	public static String serverImagePathList = "";
	
	private Animation anim;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		initViews();
		initPostNoticeUI();
		setListener();
		loadData();
		
	}
	
	private void loadData() {
		takePath = new ArrayList<String>();
		if(getIntent().getExtras() != null) {
			if(getIntent().getBooleanExtra("First_Time", false)) {
				btnBack.setVisibility(View.INVISIBLE);
			}
			else {
				btnBack.setVisibility(View.VISIBLE);
			}
		}
		
		if (InternetConnectivity.isConnectedToInternet(MainMenuActivity.this)) {
			if(SidraPulseApp.getInstance().getUserInfo().getAccessToken() != null) {
				MainMenuAsyncTask menuSetRequest = new MainMenuAsyncTask(MainMenuActivity.this, new OnRequestComplete() {
					
					@Override
					public void onRequestComplete(int signinStatus) {
						Log.e("Sign in status is :",":::::::::"+signinStatus);
						if(signinStatus == 1){
							loadSlideMenuItem();
						}else{
							
						}
						
					}
					
				});
				//execute callback task
				menuSetRequest.execute(ConstantValues.FUNC_ID_MAIN_PAGE_NOTIFICATION, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken());
			} 
		} else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(MainMenuActivity.this);
			
		}
	}
	
	private void initViews() {
		anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(500); // the blinking time can be adjust with this parameter
		anim.setStartOffset(20);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		
		btnBack = (Button) findViewById(R.id.btn_back);
		btnSettings = (Button) findViewById(R.id.btn_setting);
		btnSearch = (Button) findViewById(R.id.btn_search);
		btnShareSidraForum = (Button) findViewById(R.id.btn_share_sidra_forum);
		edTxtSearchDirectory = (EditText) findViewById(R.id.ed_txt_search_directory);
		relativeMainMenu = (RelativeLayout) findViewById(R.id.relative_main_menu);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		
		menuGridView1 = new GridView(this);
		menuGridView1.setVerticalScrollBarEnabled(false);
		menuGridView1.setSelector(R.color.color_transparent);
		menuGridView2 = new GridView(this);
		menuGridView2.setVerticalScrollBarEnabled(false);
		menuGridView2.setSelector(R.color.color_transparent);
		
		indicator1 = (ImageView)findViewById(R.id.img_view_page_indicator1);
		indicator2 = (ImageView)findViewById(R.id.img_view_page_indicator2);
		
		pages = new Vector<View>();
        pages.add(menuGridView1);
        pages.add(menuGridView2);   
     
	}
	
	private void setListener() {
		btnBack.setOnClickListener(this);
		btnSettings.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		btnShareSidraForum.setOnClickListener(this);
		relativeMainMenu.setOnClickListener(this);
		
		edTxtSearchDirectory.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

	           if(actionId == 3) {
					if(edTxtSearchDirectory.getText().toString().trim().length() > 0) {
						SidraPulseApp.getInstance().hideKeyboard(MainMenuActivity.this, v);
						Intent intent = new Intent(MainMenuActivity.this, StaffDirectorySearchActivity.class);
						intent.putExtra("keyword", edTxtSearchDirectory.getText().toString().trim());
						intent.putExtra("is_all", true);
						intent.putExtra("position", 0);
						intent.putExtra("isMainPage", true);
						startActivity(intent);
					}
	               //return true;
	           }
	           
	           return false;
			}
	    });
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
	         
	         @Override
	         public void onPageSelected(int position) {
	        	 	Log.d("Page", "**** onPageSelected = " + position);
	        	 	if (position==0) {
	        	 		indicator1.setImageResource(R.drawable.circle_selected);
	        	 		indicator2.setImageResource(R.drawable.circle);
	        	 	} else {
	        	 		indicator2.setImageResource(R.drawable.circle_selected);
	        	 		indicator1.setImageResource(R.drawable.circle);
	        	 	}
	         }

	         @Override
	         public void onPageScrolled(int arg0, float arg1, int arg2) {
	        	 
	         }
	         
	         @Override
	         public void onPageScrollStateChanged(int arg0) {
	        	 
	         }
	         
	      });

		menuGridView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {

				switch (position) {
				case 0:
					if(SidraPulseApp.getInstance().getNotificationInfo().getAnnouncement() > 0) {
						if (InternetConnectivity.isConnectedToInternet(MainMenuActivity.this)) {
							new UnReadTask(MainMenuActivity.this, new UnReadRequest() {
								@Override
								public void onTaskCompleted(int status) {
									if(status == 1) {
										SidraPulseApp.getInstance().getNotificationInfo().setAnnouncement(0);
										mainMenuAdapter1.notifyDataSetChanged();
										startActivity(new Intent(MainMenuActivity.this, AnnouncementsActivity.class));
									} else if(status == 5) {
										SidraPulseApp.getInstance().accessTokenChange(MainMenuActivity.this);
									} else {
										Utilities.showToast(MainMenuActivity.this, ConstantValues.failureMessage);
									}
								}
							}, 1, 0, true).execute();
						} else {
							SidraPulseApp.getInstance().openDialogForInternetChecking(MainMenuActivity.this);
						}
					} else {
						startActivity(new Intent(MainMenuActivity.this, AnnouncementsActivity.class));
					}
					break;
				case 1:
					startActivity(new Intent(MainMenuActivity.this, StaffDirectoryActivity.class));
					break;
					
				case 2:
					startActivity(new Intent(MainMenuActivity.this, ForumsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					
					break;
					
				case 3:
					if(SidraPulseApp.getInstance().getNotificationInfo().getClassified() > 0) {
						if (InternetConnectivity.isConnectedToInternet(MainMenuActivity.this)) {
							new UnReadTask(MainMenuActivity.this, new UnReadRequest() {
								@Override
								public void onTaskCompleted(int status) {
									if(status == 1){
										SidraPulseApp.getInstance().getNotificationInfo().setClassified(0);
										mainMenuAdapter1.notifyDataSetChanged();
										startActivity(new Intent(MainMenuActivity.this, ClassifiedsActivity.class));
									} else if(status == 5) {
										SidraPulseApp.getInstance().accessTokenChange(MainMenuActivity.this);
									} else {
										Utilities.showToast(MainMenuActivity.this, ConstantValues.failureMessage);
									}
								}
							}, 2, 0, true).execute();
						} else {
							SidraPulseApp.getInstance().openDialogForInternetChecking(MainMenuActivity.this);
						}
					} else {
						startActivity(new Intent(MainMenuActivity.this, ClassifiedsActivity.class));
					}

					break;
					
				case 4:
					if(SidraPulseApp.getInstance().getNotificationInfo().getOfferAndPromotion() > 0) {
						if (InternetConnectivity.isConnectedToInternet(MainMenuActivity.this)) {
							new UnReadTask(MainMenuActivity.this, new UnReadRequest() {
								@Override
								public void onTaskCompleted(int status) {
									
									if(status == 1) {
										SidraPulseApp.getInstance().getNotificationInfo().setOfferAndPromotion(0);
										mainMenuAdapter1.notifyDataSetChanged();
										startActivity(new Intent(MainMenuActivity.this, OffersPromotionsActivity.class));
									} else if(status == 5) {
										SidraPulseApp.getInstance().accessTokenChange(MainMenuActivity.this);
									} else {
										Utilities.showToast(MainMenuActivity.this, ConstantValues.failureMessage);
									}
								}
							}, 5, 0, true).execute();
						} else {
							SidraPulseApp.getInstance().openDialogForInternetChecking(MainMenuActivity.this);
						}
					} else {
						startActivity(new Intent(MainMenuActivity.this, OffersPromotionsActivity.class));
					}
					break;
					
				case 5:
					if(SidraPulseApp.getInstance().getNotificationInfo().getEvent() > 0) {
						if (InternetConnectivity.isConnectedToInternet(MainMenuActivity.this)) {
							new UnReadTask(MainMenuActivity.this, new UnReadRequest() {
								@Override
								public void onTaskCompleted(int status) {
									if(status == 1) {
										SidraPulseApp.getInstance().getNotificationInfo().setEvent(0);
										mainMenuAdapter1.notifyDataSetChanged();
										startActivity(new Intent(MainMenuActivity.this, EventsActivity.class));
									} else if(status == 5) {
										SidraPulseApp.getInstance().accessTokenChange(MainMenuActivity.this);
									} else {
										Utilities.showToast(MainMenuActivity.this, ConstantValues.failureMessage);
									}
								}
							}, 3, 0, true).execute();
						} else {
							SidraPulseApp.getInstance().openDialogForInternetChecking(MainMenuActivity.this);
						}
					} else {
						startActivity(new Intent(MainMenuActivity.this, EventsActivity.class));
					}
					break;
					
				default:
					break;
				}
			}
		});
		
		menuGridView2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {

				switch (position) {
				case 0:
					startActivity(new Intent(MainMenuActivity.this, NewsLetterActivity.class));
					break;

				case 1:
					if(SidraPulseApp.getInstance().getNotificationInfo().getGallery() > 0) {
						if (InternetConnectivity.isConnectedToInternet(MainMenuActivity.this)) {
							new UnReadTask(MainMenuActivity.this, new UnReadRequest() {
								@Override
								public void onTaskCompleted(int status) {
									if(status == 1) {
										SidraPulseApp.getInstance().getNotificationInfo().setGallery(0);
										mainMenuAdapter2.notifyDataSetChanged();
										startActivity(new Intent(MainMenuActivity.this, GalleryActivity.class));
									} else if(status == 5) {
										SidraPulseApp.getInstance().accessTokenChange(MainMenuActivity.this);
									} else {
										Utilities.showToast(MainMenuActivity.this, ConstantValues.failureMessage);
									}
								}
							}, 4, 0, true).execute();
						} else {
							SidraPulseApp.getInstance().openDialogForInternetChecking(MainMenuActivity.this);
						}
					} else {
						startActivity(new Intent(MainMenuActivity.this, GalleryActivity.class));
					}
					break;
					
				case 2:
					startActivity(new Intent(MainMenuActivity.this, HumanResourcesActivity.class));
					break;
					
				case 3:
					startActivity(new Intent(MainMenuActivity.this, PoliciesActivity.class));
					break;
					
				case 4:
					startActivity(new Intent(MainMenuActivity.this, StayInformedActivity.class));
					break;
					
				case 5:
					break;
				default:
					break;
				}
			}
		});
		
	}
	
	private void loadSlideMenuItem() {
		CustomPagerAdapter adapter = new CustomPagerAdapter(this, pages);
        viewPager.setAdapter(adapter);
        
        menuGridView1.setNumColumns(2);
		menuGridView1.setPadding(0, 0, 0, 0);
        menuGridView1.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        menuGridView1.setHorizontalSpacing(10);
        menuGridView1.setVerticalSpacing(10);

        mainMenuAdapter1 = new MainMenuAdapter(MainMenuActivity.this, ConstantValues.MAIN_MENU_PAGE_ONE_IMAGES, ConstantValues.MAIN_MENU_PAGE_ONE_TEXT, 1);
        menuGridView1.setAdapter(mainMenuAdapter1);
        
        menuGridView2.setNumColumns(2);
        menuGridView2.setPadding(0, 0, 0, 0);
		menuGridView2.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        menuGridView2.setHorizontalSpacing(10);
        menuGridView2.setVerticalSpacing(10);

        mainMenuAdapter2 = new MainMenuAdapter(MainMenuActivity.this, ConstantValues.MAIN_MENU_PAGE_TWO_IMAGES, ConstantValues.MAIN_MENU_PAGE_TWO_TEXT, 2);
        menuGridView2.setAdapter(mainMenuAdapter2);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.relative_main_menu:
			SidraPulseApp.getInstance().hideKeyboard(this, v);
			break;

		case R.id.btn_back:
			onBackPressed();
			break;
			
		case R.id.btn_setting:
			startActivity(new Intent(MainMenuActivity.this, SettingsActivity.class));
			break;
			
		case R.id.btn_search:
			if(edTxtSearchDirectory.getText().toString().trim().length() > 0) {
				SidraPulseApp.getInstance().hideKeyboard(MainMenuActivity.this, v);
				Intent intent = new Intent(MainMenuActivity.this, StaffDirectorySearchActivity.class);
				intent.putExtra("keyword", edTxtSearchDirectory.getText().toString().trim());
				intent.putExtra("is_all", true);
				intent.putExtra("position", 0);
				intent.putExtra("isMainPage", true);
				startActivity(intent);
			}
			break;
			
		case R.id.btn_share_sidra_forum:
			postDialogInterface();
		/*	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);*/
			break;

		default:
			break;
		}
	}
	
	private void postDialogInterface() {
		
		dialogReply = new DialogController(MainMenuActivity.this).ForumsReplyDialog();
		editTxtDialogReply = (EditText) dialogReply.findViewById(R.id.txt_view_reply);
		editTxtDialogReply.setHint("Type your message");
		linearImageAdd = (LinearLayout) dialogReply.findViewById(R.id.linear_img_add);
		
		final TextView txtViewCountLimit = (TextView) dialogReply.findViewById(R.id.txt_view_character_count);
		Button dialogBtnCamera = (Button) dialogReply.findViewById(R.id.btn_add);
		
		dialogBtnSend = (Button) dialogReply.findViewById(R.id.btn_send);
		dialogBtnSend.setEnabled(false);
    	dialogBtnSend.setAlpha(0.3f);
    	
		Button dialogBtnClose = (Button) dialogReply.findViewById(R.id.btn_close);
		
		RelativeLayout relative_scrollview_holder = (RelativeLayout) dialogReply.findViewById(R.id.relative_scrollview_holder);
		RelativeLayout relativeLayout1 = (RelativeLayout) dialogReply.findViewById(R.id.relativeLayout1);
		
		relative_scrollview_holder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
			
			}
		});
		
		relativeLayout1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
			}
		});
		
		dialogBtnCamera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (imageAdded < 1) {
					selectImage();
				} else {
					Toast.makeText(MainMenuActivity.this, "Already you have selected a photo", Toast.LENGTH_LONG).show();
				}
			}
		});

		dialogBtnSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				if (!editTxtDialogReply.getText().toString().trim().equalsIgnoreCase("") || imageAdded !=0) {
					//dialogPostingNotice.show();
					savePost();
				} else {
					Utilities.showToast(MainMenuActivity.this, "Please fill up description field");
				}
				
			}

		});
		
		dialogBtnClose.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//onClickBtnCommentsSend();
				imageAdded = 0;
                takePath.clear();
				dialogReply.dismiss();
				//SidraPulseApp.getInstance().hideKeyboard(getApplicationContext(), v);
			}

		});
		
		dialogReply.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            	 getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                imageAdded = 0;
                takePath.clear();
            }
        });
		
		editTxtDialogReply.addTextChangedListener(new TextWatcher() {
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		    	
		    }

		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		    	
		    }

		    @Override
		    public void afterTextChanged(Editable s) {
		    	if (editTxtDialogReply.getText().toString().trim().length() > 250) {
		    		dialogBtnSend.setEnabled(false);
		    		dialogBtnSend.setAlpha(0.3f);
		        	int lengthChar = 250 - editTxtDialogReply.getText().toString().trim().length();
		        	txtViewCountLimit.setText("" + lengthChar );
		        	txtViewCountLimit.setTextColor(android.graphics.Color.RED);
		        	return;
		        	
		    	} if(editTxtDialogReply.getText().toString().trim().length() > 0 && editTxtDialogReply.getText().toString().trim().length() <= 250) {
		    		dialogBtnSend.setEnabled(true);
		    		dialogBtnSend.setAlpha(1.0f);
		        	int lengthChar = 250 - editTxtDialogReply.getText().toString().trim().length();
		        	txtViewCountLimit.setText("" + lengthChar );
		        	txtViewCountLimit.setTextColor(0xFF9B9CA0);
		        	
		        	if(editTxtDialogReply.getText().toString().trim().length() >= 200 && editTxtDialogReply.getText().toString().trim().length() <= 250) {
		        		txtViewCountLimit.setTextColor(android.graphics.Color.RED);
		        		//txtViewCountLimit.startAnimation(anim);
		    		}
		        	
		        	if(editTxtDialogReply.getText().toString().trim().length() > 245 && editTxtDialogReply.getText().toString().trim().length() <= 250) {
		        		txtViewCountLimit.setTextColor(android.graphics.Color.RED);
		        		txtViewCountLimit.startAnimation(anim);
		    		} else {
		    			txtViewCountLimit.clearAnimation();
		    		}
		        } else {
		        	dialogBtnSend.setEnabled(false);
		        	dialogBtnSend.setAlpha(0.3f);
		        	int lengthChar = 250 - editTxtDialogReply.getText().toString().trim().length();
		        	txtViewCountLimit.setText("" + lengthChar );
		        	txtViewCountLimit.setTextColor(0xFF9B9CA0);
		        	//Utilities.showToast(ForumNewPostActivity.this, "Reached to maximum number of character");
		        }
		        
		     /*   if (editTxtDialogReply.getText().toString().trim().length() == 0) {
		        	btnSend.setEnabled(false);
		        	btnSend.setAlpha(0.3f);
		        	//Utilities.showToast(ForumsDetailsActivity.this, "Reached to maximum number of character");
		        }*/
		    }
		});

		dialogReply.show();
	}
	
	private void selectImage() {
		Intent i = new Intent("luminous.ACTION_PICK");
		startActivityForResult(i, CHOOSE_SINGLE);
	}
	
	private void initPostNoticeUI() {
		dialogPostingNotice = new DialogController(MainMenuActivity.this).PostNoticeDialog();
		final Button btnCancel = (Button) dialogPostingNotice.findViewById(R.id.btnCancel);
		final Button btnClose = (Button) dialogPostingNotice.findViewById(R.id.btn_close);
		
		btnClose.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialogPostingNotice.dismiss();
			}
		});

		final Button btnOk = (Button) dialogPostingNotice.findViewById(R.id.btnOk);

		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialogPostingNotice.dismiss();
			}
		});

		btnOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialogPostingNotice.dismiss();
				//savePost();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode != RESULT_OK)
			return;
		
		switch (requestCode) {

		case CHOOSE_SINGLE:
			 String single_path = data.getStringExtra("single_path");
			  try {
					Bitmap bm;
					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

					bm = BitmapFactory.decodeFile(single_path, btmapOptions);
					bm = Bitmap.createScaledBitmap(bm, 50, 50, true);
					
					LayoutInflater inflater = LayoutInflater.from(MainMenuActivity.this);
					final View item = inflater.inflate(R.layout.select_forum_image_row, null);
					ImageView imageView = (ImageView) item.findViewById(R.id.img_view_select);
					Button btnCancel = (Button) item.findViewById(R.id.btn_cancel);
					imageView.setImageBitmap(bm);
					btnCancel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Log.i("CLickImage_pos_camera","" + v.getTag());
							((LinearLayout)item.getParent()).removeView(item);
							imageAdded = 0;
							int removeIndex = (Integer) v.getTag();
		            		takePath.remove((Integer) v.getTag());
		            		takePath.clear();
		            		if (editTxtDialogReply.getText().toString().trim().equalsIgnoreCase("")) {
								dialogBtnSend.setEnabled(false);
								dialogBtnSend.setAlpha(0.3f);
							}
						}
					});
						
					imageAdded = 1;
					btnCancel.setTag(imageAdded);
					linearImageAdd.addView(item);
					
					if (imageAdded > 0 && editTxtDialogReply.getText().toString().trim().length() <= 250) {
						dialogBtnSend.setEnabled(true);
						dialogBtnSend.setAlpha(1.0f);
					}
					
					if (InternetConnectivity.isConnectedToInternet(MainMenuActivity.this)) {
						ImageUploadAsyncTask imageUploadFromCamera = new ImageUploadAsyncTask(MainMenuActivity.this, new OnUploadComplete() {
							
							@Override
							public void onUploadComplete(int responseStatus, String data) {
								if(responseStatus == 1) {
									Log.i("UPLOAD STATUS CAMERA","OK " + data);
									takePath.add(data);
								}
							}
							
						}, 1);
						imageUploadFromCamera.execute(ConstantValues.FUNC_ID_THREAD_PHOTO_UPLOAD, single_path);
					
					} else {
						SidraPulseApp.getInstance().openDialogForInternetChecking(MainMenuActivity.this);
					}
					
					String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "Phoenix" + File.separator + "default";
					Log.i("Camera_path","*" + path);
					//f.delete();
					OutputStream fOut = null;
					File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
					
					try {
						fOut = new FileOutputStream(file);
						bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
						fOut.flush();
						fOut.close();
						
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			break;

		}
	}
	
	
	public class CustomPagerAdapter extends PagerAdapter {

		 private Vector<View> pages;

		 public CustomPagerAdapter(Context context, Vector<View> pages) {
			 this.pages = pages;
		 }
		 
		 @Override
		 public Object instantiateItem(ViewGroup container, int position) {
			View page = pages.get(position);
		 	container.addView(page);
		 	return page;
		 }
		 
		 @Override
		 public int getCount() {
			 return pages.size();
		 }

		 @Override
		 public boolean isViewFromObject(View view, Object object) {
			 return view.equals(object);
		 }
		 
		 @Override
		 public void destroyItem(ViewGroup container, int position, Object object) {
			 container.removeView((View) object);
		 }

	}
	
	private ProgressDialog dlogProg;
	public int response = 0;

	public class postForumThreadAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			dlogProg = SidraCustomProgressDialog.creator(MainMenuActivity.this);
		}

		@Override
		protected Void doInBackground(String... params) {
			String allImagePath = params[0];
			String tagName = params[1];
			try {
				if (InternetConnectivity.isConnectedToInternet(MainMenuActivity.this)) {
				response = CommunicationLayer.getThreadEntryAPI(ConstantValues.FUNC_ID_THREAD_ENTRY_API, Integer.toString(SidraPulseApp.getInstance().getUserInfo().getUserID()), SidraPulseApp.getInstance().getUserInfo().getAccessToken(), allImagePath, editTxtDialogReply.getText().toString().trim(), tagName);
				}else {
					SidraPulseApp.getInstance().openDialogForInternetChecking(MainMenuActivity.this);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (dlogProg.isShowing()) {
				dlogProg.dismiss();
			}
			
			if (response==1) {
				Utilities.showToast(MainMenuActivity.this, "Posted Successfully to the Sidra Forum");
				linearImageAdd.removeAllViews();
				editTxtDialogReply.setText("");
				dialogReply.dismiss();
				imageAdded = 0;
				takePath.clear();
				//startActivity(new Intent(MainMenuActivity.this, ForumsActivity.class));
				
			} else if(response == 5) {
				SidraPulseApp.getInstance().accessTokenChange(MainMenuActivity.this);
				
			} else {
				Utilities.showToast(MainMenuActivity.this, "Thread Not Created Successfully to the Sidra Forum");
			}
			
			Log.i("CREATE_THREAD_RESPONSE","" + response);
		
		}
	}
	
	private void savePost() {
		for(int j = 0; j < takePath.size(); j++) {
			takePath.get(j);
			serverImagePathList += takePath.get(j) + ",";
		}
		
		StringBuffer buffer = new StringBuffer();
		Pattern MY_PATTERN = Pattern.compile("#(\\w+)");
		Matcher mat = MY_PATTERN.matcher(editTxtDialogReply.getText().toString().trim());
		
		while (mat.find()) {
			buffer.append(mat.group(1) +",");
		}
		
		if (buffer.length() > 0) {
			buffer.setLength(buffer.length() - 1);
		}
		
		if (InternetConnectivity.isConnectedToInternet(MainMenuActivity.this)) {
			if (serverImagePathList.length() > 1) {
				new postForumThreadAsyncTask().execute(serverImagePathList.substring(0, serverImagePathList.length() - 1), buffer.toString());
			} else {
				new postForumThreadAsyncTask().execute("", buffer.toString());
			}
		} else {
			SidraPulseApp.getInstance().openDialogForInternetChecking(MainMenuActivity.this);
		}
		serverImagePathList = "";
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
}
