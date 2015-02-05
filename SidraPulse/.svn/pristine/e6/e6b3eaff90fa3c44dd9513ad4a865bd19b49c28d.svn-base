package com.atomix.sidrainfo;

import java.util.ArrayList;

import com.atomix.datamodel.AnnouncementsInfo;
import com.atomix.datamodel.ArticleInfo;
import com.atomix.datamodel.ClassifiedCategoryInfo;
import com.atomix.datamodel.ClassifiedInfo;
import com.atomix.datamodel.DraftClassifiedInfo;
import com.atomix.datamodel.EventsInfo;
import com.atomix.datamodel.ForumHotTopics;
import com.atomix.datamodel.ForumsInfo;
import com.atomix.datamodel.ForumsTagInfo;
import com.atomix.datamodel.GalleryDetailsInfo;
import com.atomix.datamodel.GalleryInfo;
import com.atomix.datamodel.GalleryYearInfo;
import com.atomix.datamodel.HumanResourceCategoryInfo;
import com.atomix.datamodel.HumanResourceInfo;
import com.atomix.datamodel.NotificationInfo;
import com.atomix.datamodel.OfferPromotionCategories;
import com.atomix.datamodel.OfferPromotionInfo;
import com.atomix.datamodel.PoliceisInfo;
import com.atomix.datamodel.PoliciesDeptInfo;
import com.atomix.datamodel.SidraInNewsAPIInfo;
import com.atomix.datamodel.SidraPressReleaseInfo;
import com.atomix.datamodel.StaffDirectoryDepartmentsInfo;
import com.atomix.datamodel.StaffListInfo;
import com.atomix.datamodel.UserInfo;
import com.atomix.sidrapulse.R;
import com.atomix.sidrapulse.SignInActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

public class SidraPulseApp {

	private String baseUrl = "http://114.134.91.91/sidra_pull/api/json";
	public static SidraPulseApp instance;
	private ArrayList<AnnouncementsInfo> announcementsInfoList;
	private ArrayList<GalleryInfo> galleryInfoList;
	private ArrayList<GalleryDetailsInfo> galleryDetailsInfoList;
	private UserInfo userInfo;
	private NotificationInfo notificationInfo;
	private ArrayList<OfferPromotionCategories> offerAndPromotionCategories;
	private ArrayList<EventsInfo> eventsInfoList;
	private ArrayList<StaffDirectoryDepartmentsInfo> directoryDepartmentsInfoList;
	private ArrayList<StaffListInfo> staffListInfoList;
	private ArrayList<SidraInNewsAPIInfo> sidraInNewsAPI;
	private ArrayList<SidraPressReleaseInfo> sidraPressRelease;
	private ArrayList<OfferPromotionInfo> offerPromotionInfoList;
	private ArrayList<ForumsTagInfo> forumsTagInfoList;
	private ArrayList<ForumsInfo> forumsInfoList;
	private ArrayList<ClassifiedCategoryInfo> classifiedCategoryInfoList;
	private ArrayList<ClassifiedInfo> classifiedInfoList;
	private ArrayList<PoliciesDeptInfo> policiesDeptInfoList;
	private ArrayList<PoliceisInfo> policeisInfoList;
	private ArrayList<HumanResourceCategoryInfo> humanResourceCategoryInfoList;
	private ArrayList<HumanResourceInfo> humanResourceInfoList;
	private ArrayList<DraftClassifiedInfo> draftClassifiedInfoList;
	private ArrayList<GalleryYearInfo> galleryYearInfoList;
	private ArrayList<ForumHotTopics> hotTopicsList;
	private ArrayList<ArticleInfo> articleInfoList;

	public static SidraPulseApp getInstance() {
		if (instance == null) {
			instance = new SidraPulseApp();
		}
		
		return instance;
	}

	public static void destroyInstance() {
		instance = null;
	}

	public void hideKeyboard(Context context, View v) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	public void openInternetSettingsActivity(final Context context) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("Internet Problem");
		alert.setMessage("No internet connection. Please connect to a network first.");
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			}
		});

		alert.show();
	}
	
	public void openErrorDialog(String err_msg, Context context) {
		err_msg = Html.fromHtml(err_msg).toString();
		final AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setMessage(err_msg);
		alert.setCancelable(true);

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
			}
		});
		alert.show();
	}
	
	public void openDialogForInternetChecking(final Context context) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.internet_check);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);

		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		ImageView imageView = (ImageView) dialog.findViewById(R.id.img_view_internet_check);
		
		AlphaAnimation  blinkanimation = new AlphaAnimation(1.0f, 0.0f); // Change alpha from fully visible to invisible
		blinkanimation.setDuration(800); // duration - half a second
		blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
		blinkanimation.setRepeatCount(1); // Repeat animation infinitely
		blinkanimation.setRepeatMode(Animation.REVERSE);
		imageView.startAnimation(blinkanimation);
		
		blinkanimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				dialog.dismiss();
				//accessTokenChange(context);
			}
		});
		
		dialog.show();
	}

	public ArrayList<AnnouncementsInfo> getAnnouncementsInfoList() {
		return announcementsInfoList;
	
	}

	public void setAnnouncementsInfoList(ArrayList<AnnouncementsInfo> announcementsInfoList) {
		this.announcementsInfoList = announcementsInfoList;
	}

	public ArrayList<GalleryInfo> getGalleryInfoList() {
		return galleryInfoList;
	}

	public void setGalleryInfoList(ArrayList<GalleryInfo> galleryInfoList) {
		this.galleryInfoList = galleryInfoList;
	}
	
	public ArrayList<GalleryDetailsInfo> getGalleryDetailsInfoList() {
		return galleryDetailsInfoList;
	}

	public void setGalleryDetailsInfoList(ArrayList<GalleryDetailsInfo> galleryDetailsInfoList) {
		this.galleryDetailsInfoList = galleryDetailsInfoList;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public ArrayList<OfferPromotionInfo> getOfferPromotionInfoList() {
		return offerPromotionInfoList;
	}

	public void setOfferPromotionInfoList(ArrayList<OfferPromotionInfo> offerPromotionInfoList) {
		this.offerPromotionInfoList = offerPromotionInfoList;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public NotificationInfo getNotificationInfo() {
		return notificationInfo;
	}

	public void setNotificationInfo(NotificationInfo notificationInfo) {
		this.notificationInfo = notificationInfo;
	}

	public ArrayList<OfferPromotionCategories> getOfferAndPromotionCategories() {
		return offerAndPromotionCategories;
	}

	public void setOfferAndPromotionCategories(ArrayList<OfferPromotionCategories> offerAndPromotionCategories) {
		this.offerAndPromotionCategories = offerAndPromotionCategories;
	}

	public ArrayList<EventsInfo> getEventsInfoList() {
		return eventsInfoList;
	}

	public void setEventsInfoList(ArrayList<EventsInfo> eventsInfoList) {
		this.eventsInfoList = eventsInfoList;
	}

	public ArrayList<StaffDirectoryDepartmentsInfo> getDirectoryDepartmentsInfoList() {
		return directoryDepartmentsInfoList;
	}

	public void setDirectoryDepartmentsInfoList(
			ArrayList<StaffDirectoryDepartmentsInfo> directoryDepartmentsInfoList) {
		this.directoryDepartmentsInfoList = directoryDepartmentsInfoList;
	}

	public ArrayList<StaffListInfo> getStaffListInfoList() {
		return staffListInfoList;
	}

	public void setStaffListInfoList(ArrayList<StaffListInfo> staffListInfoList) {
		this.staffListInfoList = staffListInfoList;
	}

	public ArrayList<SidraInNewsAPIInfo> getSidraInNewsAPI() {
		return sidraInNewsAPI;
	}

	public void setSidraInNewsAPI(ArrayList<SidraInNewsAPIInfo> sidraInNewsAPI) {
		this.sidraInNewsAPI = sidraInNewsAPI;
	}

	public ArrayList<SidraPressReleaseInfo> getSidraPressRelease() {
		return sidraPressRelease;
	}

	public void setSidraPressRelease(ArrayList<SidraPressReleaseInfo> sidraPressRelease) {
		this.sidraPressRelease = sidraPressRelease;
	}

	public ArrayList<ForumsTagInfo> getForumsTagInfoList() {
		return forumsTagInfoList;
	}

	public void setForumsTagInfoList(ArrayList<ForumsTagInfo> forumsTagInfoList) {
		this.forumsTagInfoList = forumsTagInfoList;
	}

	public ArrayList<ForumsInfo> getForumsInfoList() {
		return forumsInfoList;
	}

	public void setForumsInfoList(ArrayList<ForumsInfo> forumsInfoList) {
		this.forumsInfoList = forumsInfoList;
	}

	public ArrayList<ClassifiedCategoryInfo> getClassifiedCategoryInfoList() {
		return classifiedCategoryInfoList;
	}

	public void setClassifiedCategoryInfoList(
			ArrayList<ClassifiedCategoryInfo> classifiedCategoryInfoList) {
		this.classifiedCategoryInfoList = classifiedCategoryInfoList;
	}

	public ArrayList<ClassifiedInfo> getClassifiedInfoList() {
		return classifiedInfoList;
	}

	public void setClassifiedInfoList(ArrayList<ClassifiedInfo> classifiedInfoList) {
		this.classifiedInfoList = classifiedInfoList;
	}

	public ArrayList<PoliciesDeptInfo> getPoliciesDeptInfoList() {
		return policiesDeptInfoList;
	}

	public void setPoliciesDeptInfoList(ArrayList<PoliciesDeptInfo> policiesDeptInfoList) {
		this.policiesDeptInfoList = policiesDeptInfoList;
	}

	public ArrayList<PoliceisInfo> getPoliceisInfoList() {
		return policeisInfoList;
	}

	public void setPoliceisInfoList(ArrayList<PoliceisInfo> policeisInfoList) {
		this.policeisInfoList = policeisInfoList;
	}

	public ArrayList<HumanResourceCategoryInfo> getHumanResourceCategoryInfoList() {
		return humanResourceCategoryInfoList;
	}

	public void setHumanResourceCategoryInfoList(
			ArrayList<HumanResourceCategoryInfo> humanResourceCategoryInfoLis) {
		this.humanResourceCategoryInfoList = humanResourceCategoryInfoLis;
	}

	public ArrayList<HumanResourceInfo> getHumanResourceInfoList() {
		return humanResourceInfoList;
	}

	public void setHumanResourceInfoList(ArrayList<HumanResourceInfo> humanResourceInfoList) {
		this.humanResourceInfoList = humanResourceInfoList;
	}

	public ArrayList<DraftClassifiedInfo> getDraftClassifiedInfoList() {
		return draftClassifiedInfoList;
	}

	public void setDraftClassifiedInfoList(ArrayList<DraftClassifiedInfo> draftClassifiedInfoList) {
		this.draftClassifiedInfoList = draftClassifiedInfoList;
	}

	public ArrayList<GalleryYearInfo> getGalleryYearInfoList() {
		return galleryYearInfoList;
	}

	public void setGalleryYearInfoList(ArrayList<GalleryYearInfo> galleryYearInfoList) {
		this.galleryYearInfoList = galleryYearInfoList;
	}

	public ArrayList<ForumHotTopics> getHotTopicsList() {
		return hotTopicsList;
	}

	public void setHotTopicsList(ArrayList<ForumHotTopics> hotTopicsList) {
		this.hotTopicsList = hotTopicsList;
	}
	
	public void accessTokenChange(Context context) {
		destroyInstance();
		Intent intent = new Intent(context, SignInActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		context.startActivity(intent);
	}

	public ArrayList<ArticleInfo> getArticleInfoList() {
		return articleInfoList;
	}

	public void setArticleInfoList(ArrayList<ArticleInfo> articleInfoList) {
		this.articleInfoList = articleInfoList;
	}

}
