package com.atomix.jsonparse;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.atomix.datamodel.AnnouncementsInfo;
import com.atomix.datamodel.ArticleInfo;
import com.atomix.datamodel.AuthorInfo;
import com.atomix.datamodel.ClassifiedCategoryInfo;
import com.atomix.datamodel.ClassifiedInfo;
import com.atomix.datamodel.ClassifiedsOwnerInfo;
import com.atomix.datamodel.ClassifiedsPhotoInfo;
import com.atomix.datamodel.EventsInfo;
import com.atomix.datamodel.ForumCommentator;
import com.atomix.datamodel.ForumComments;
import com.atomix.datamodel.ForumHotTopics;
import com.atomix.datamodel.ForumPhotoInfo;
import com.atomix.datamodel.ForumsInfo;
import com.atomix.datamodel.ForumsTagInfo;
import com.atomix.datamodel.GalleryDetailsInfo;
import com.atomix.datamodel.GalleryInfo;
import com.atomix.datamodel.GalleryYearInfo;
import com.atomix.datamodel.HumanResourceInfo;
import com.atomix.datamodel.NotificationInfo;
import com.atomix.datamodel.OfferPromotionCategories;
import com.atomix.datamodel.OfferPromotionInfo;
import com.atomix.datamodel.OfferPromotionPhoto;
import com.atomix.datamodel.PoliceisInfo;
import com.atomix.datamodel.PoliciesDeptInfo;
import com.atomix.datamodel.SidraInNewsAPIInfo;
import com.atomix.datamodel.SidraPressReleaseInfo;
import com.atomix.datamodel.StaffDirectoryDepartmentsInfo;
import com.atomix.datamodel.StaffListInfo;
import com.atomix.datamodel.UserInfo;
import com.atomix.sidrainfo.ConstantValues;
import com.atomix.sidrainfo.SidraPulseApp;
import com.atomix.utils.Utilities;

public class CommunicationLayer {
	public static int getSignInData(String func_id, String user_id, String password, String device_type, String device_token, String registration_key) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(6);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("username", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("password", password));
		postData.add((NameValuePair) new BasicNameValuePair("device_type", device_type));
		postData.add((NameValuePair) new BasicNameValuePair("device_token", device_token));
		postData.add((NameValuePair) new BasicNameValuePair("registration_key", registration_key));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		Log.i("server response ", "__________"+serverResponse);
		return parseSignInData(serverResponse);
	}

	private static int parseSignInData(String jData) throws JSONException {
		int logindata;
		JSONObject jDataObj = new JSONObject(jData);
		logindata = jDataObj.getInt("status");
		
		UserInfo info = new UserInfo();
		if(logindata == 1) {
			JSONObject dataObj = jDataObj.getJSONObject("data");
			info.setUserID(dataObj.getInt("user_id"));
			info.setPushStatus(dataObj.getInt("push_on"));
			info.setAccessToken(dataObj.getString("access_token").trim());
			Log.e("------User Id", dataObj.getString("user_id").trim());
		} else {
			
		}
		
		SidraPulseApp.getInstance().setUserInfo(info);
		return logindata;
	}
	
	//API #2
	public static int getPushStatusData(String func_id, String user_id, String access_token, String push_on) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("push_on", push_on));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parsePushStatusData(serverResponse);
	}

	private static int parsePushStatusData(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getInt("status");
	}
	
	//API #3
	public static int getNotificationData(String func_id, String user_id, String access_token) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(2);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseNotificationData(serverResponse);
	}

	private static int parseNotificationData(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		NotificationInfo info = new NotificationInfo();
		Log.i("Notification Response","" + jData);
		if(jDataObj.getInt("status") == 1) {
			JSONObject dataObj = jDataObj.getJSONObject("data");
			info.setAnnouncement(dataObj.getInt("announcement"));
			info.setClassified(dataObj.getInt("classified"));
			info.setEvent(dataObj.getInt("event"));
			info.setGallery(dataObj.getInt("gallery"));
			info.setOfferAndPromotion(dataObj.getInt("offerAndPromotion"));
			info.setForum(dataObj.getInt("forum"));
			
		} else {
			
		}
		
		SidraPulseApp.getInstance().setNotificationInfo(info);
		return jDataObj.getInt("status");
	}
	
	//API #4
	public static int getAnnouncementData(String func_id, String user_id, String access_token, String last_element_id, String direction) throws Exception {
		Log.i("AnnounceMent_PARAM","f_id: " + func_id + " accToken: " + access_token + " Page: " + " page_no: " + last_element_id);
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("last_element_id", last_element_id));
		postData.add((NameValuePair) new BasicNameValuePair("direction", direction));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		Log.i("Announcement Response", "***" + serverResponse);
		return parseAnnouncementData(serverResponse, last_element_id, direction);
	}

	private static int parseAnnouncementData(String jData, String element_id, String direction) throws JSONException, ParseException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<AnnouncementsInfo> arrayList = new ArrayList<AnnouncementsInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					AnnouncementsInfo info = new AnnouncementsInfo();
					JSONObject values = dataArray.getJSONObject(i);
					info.setType(values.getString("cat_name").trim());
					info.setId(values.getInt("id"));
					info.setTitle(values.getString("title").trim());
					info.setDescription(values.getString("description").trim());
					info.setDayAndDate(Utilities.dateConvertion(values.getString("created_at").trim()));
					info.setDate(Utilities.eventFullDateConvertion(values.getString("created_at").trim()));
					info.setTime(Utilities.timeConversion(values.getString("created_at").trim()));
					info.setReadStatus(values.getInt("is_read"));
					arrayList.add(info);
				}
			}
			
			if (!"".equals(element_id) && direction.equals("1")) {
				SidraPulseApp.getInstance().getAnnouncementsInfoList().addAll(arrayList);
				
			} else if (!"".equals(element_id) && direction.equals("0")) {
				SidraPulseApp.getInstance().getAnnouncementsInfoList().addAll(0, arrayList);
				
			}else {
				SidraPulseApp.getInstance().setAnnouncementsInfoList(arrayList);
				
			}
			
			if (dataArray.length() == 10) {
				ConstantValues.PullDownActive = true;
			} else {
				ConstantValues.PullDownActive = false;
			}
			
		} else {
			
		}
		
		return jDataObj.getInt("status");
	}
	
	public static int getUnReadData(String func_id, String user_id, String access_token, String type, String element_id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(5);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("type", type));
		postData.add((NameValuePair) new BasicNameValuePair("element_id", element_id));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseUnReadData(serverResponse);
	}

	private static int parseUnReadData(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getInt("status");
	}
	
	public static int getBubbleUnReadData(String func_id, String user_id, String access_token, String type) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(4);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("type", type));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseBubbleUnReadData(serverResponse);
	}

	private static int parseBubbleUnReadData(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getInt("status");
	}
	
	//API #15
	//API #15
		public static int getOfferAndPromotionData(String func_id, String user_id, String access_token, String type, String cat_id, String last_element_id, String direction) throws Exception {
			List<NameValuePair> postData = new ArrayList<NameValuePair>(6);
			
			postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
			postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
			postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
			postData.add((NameValuePair) new BasicNameValuePair("type", type));
			postData.add((NameValuePair) new BasicNameValuePair("cat_id", cat_id));
			postData.add((NameValuePair) new BasicNameValuePair("last_element_id", last_element_id));
			postData.add((NameValuePair) new BasicNameValuePair("direction", direction));

			String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
			Log.i("server response offer and promotion ", "_______________" + serverResponse);
			return parseOfferAndPromotionData(serverResponse, last_element_id, direction);
		}

		private static int parseOfferAndPromotionData(String jData,String last_element_id, String direction) throws JSONException, ParseException {
			JSONObject jDataObj = new JSONObject(jData);
			ArrayList<OfferPromotionInfo> arrayList = new ArrayList<OfferPromotionInfo>();
			if(jDataObj.getInt("status") == 1) {
				JSONArray dataArray = jDataObj.getJSONArray("data");
				if(dataArray.length() > 0) {
					for(int i = 0; i < dataArray.length(); i++) {
						OfferPromotionInfo offerPromotionInfo = new OfferPromotionInfo();
						JSONObject values = dataArray.getJSONObject(i);
						offerPromotionInfo.setId(values.getInt("id"));
						offerPromotionInfo.setCategoryId(values.getInt("cat_id"));
						offerPromotionInfo.setTitle(values.getString("title").trim());
						if(!values.getString("valid_until").trim().equalsIgnoreCase("null")) {
							offerPromotionInfo.setValidUntil(Utilities.offerPromotionDateConvertion(values.getString("valid_until").trim()));
						} else {
							offerPromotionInfo.setValidUntil("");
						}
						
						offerPromotionInfo.setLongTerm(values.getInt("long_term"));
						offerPromotionInfo.setDescription(values.getString("description").trim());
						offerPromotionInfo.setCompanyThumb(values.getString("company_thumb").trim());
//						offerPromotionInfo.setApprovedBy(values.getInt("approved_by"));
//						offerPromotionInfo.setPublishedBy(values.getInt("published_by"));
						offerPromotionInfo.setIsDelete(values.getInt("is_del"));
						offerPromotionInfo.setIsDraft(values.getInt("is_draft"));
						offerPromotionInfo.setCreatedBy(values.getInt("created_by"));
						offerPromotionInfo.setUpdatedBy(values.getInt("updated_by"));
						offerPromotionInfo.setCreatedAt(Utilities.offerPromotionDateConvertion(values.getString("created_at").trim()).toString());
						offerPromotionInfo.setUpdatedAt(Utilities.offerPromotionDateConvertion(values.getString("updated_at").trim()));
						offerPromotionInfo.setIsBookmarked(values.getInt("is_bookmarked"));
						
						ArrayList<OfferPromotionPhoto> photo_arraylist = new ArrayList<OfferPromotionPhoto>();
						JSONArray values_photoArray = values.getJSONArray("photo");
						if(values_photoArray.length() > 1) {
							for(int j = 0; j < values_photoArray.length(); j++) {
								OfferPromotionPhoto photo = new OfferPromotionPhoto();
								JSONObject photoObject = values_photoArray.getJSONObject(j);
								photo.setId(photoObject.getInt("id"));
								photo.setOffer_and_promotion_id(photoObject.getInt("offer_and_promotion_id"));
								photo.setPhoto(photoObject.getString("photo").trim());
								photo.setIsDelete(photoObject.getInt("is_del"));
								photo_arraylist.add(photo);
							}
							offerPromotionInfo.setPhotoArray(photo_arraylist);
						}
						arrayList.add(offerPromotionInfo);
					}
					
					
					if (!"".equals(last_element_id) && direction.equals("1")) {
						SidraPulseApp.getInstance().getOfferPromotionInfoList().addAll(arrayList);
						
					} else if (!"".equals(last_element_id) && direction.equals("0")) {
						SidraPulseApp.getInstance().getOfferPromotionInfoList().addAll(0,arrayList);
						
					} else {
						SidraPulseApp.getInstance().setOfferPromotionInfoList(arrayList);
					}
					
					if (dataArray.length() == 10) {
						ConstantValues.PullDownActive = true;
					} else {
						ConstantValues.PullDownActive = false;
					}
					
					
				} else {
					SidraPulseApp.getInstance().setOfferPromotionInfoList(null);
				}
				
			} else {
				/*if (Integer.parseInt(last_element_id) > 1) {
				} else {
					SidraPulseApp.getInstance().setOfferPromotionInfoList(null);
				} */
				//SidraPulseApp.getInstance().setOfferPromotionInfoList(null);
			}
			
			return jDataObj.getInt("status");
		}
	
	/*public static int getOfferAndPromotionData(String func_id, String user_id, String access_token, String type, String cat_id, String last_element_id, String direction) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(6);
		
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("type", type));
		postData.add((NameValuePair) new BasicNameValuePair("cat_id", cat_id));
		postData.add((NameValuePair) new BasicNameValuePair("last_element_id", last_element_id));
		postData.add((NameValuePair) new BasicNameValuePair("direction", direction));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		Log.i("server response offer and promotion ", "_______________"+serverResponse);
		return parseOfferAndPromotionData(serverResponse, last_element_id, direction);
	}

	private static int parseOfferAndPromotionData(String jData,String last_element_id, String direction) throws JSONException, ParseException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<OfferPromotionInfo> arrayList = new ArrayList<OfferPromotionInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					OfferPromotionInfo offerPromotionInfo = new OfferPromotionInfo();
					JSONObject values = dataArray.getJSONObject(i);
					offerPromotionInfo.setId(values.getInt("id"));
					offerPromotionInfo.setCategoryId(values.getInt("cat_id"));
					offerPromotionInfo.setTitle(values.getString("title"));
					if(!values.getString("valid_until").equalsIgnoreCase("null")) {
						offerPromotionInfo.setValidUntil(Utilities.offerPromotionDateConvertion(values.getString("valid_until")));
					} else {
						offerPromotionInfo.setValidUntil("");
					}
					
					offerPromotionInfo.setLongTerm(values.getInt("long_term"));
					offerPromotionInfo.setDescription(values.getString("description"));
					offerPromotionInfo.setCompanyThumb(values.getString("company_thumb"));
//					offerPromotionInfo.setApprovedBy(values.getInt("approved_by"));
//					offerPromotionInfo.setPublishedBy(values.getInt("published_by"));
					offerPromotionInfo.setIsDelete(values.getInt("is_del"));
					offerPromotionInfo.setIsDraft(values.getInt("is_draft"));
					offerPromotionInfo.setCreatedBy(values.getInt("created_by"));
					offerPromotionInfo.setUpdatedBy(values.getInt("updated_by"));
					offerPromotionInfo.setCreatedAt(Utilities.offerPromotionDateConvertion(values.getString("created_at")).toString());
					offerPromotionInfo.setUpdatedAt(Utilities.offerPromotionDateConvertion(values.getString("updated_at")));
					offerPromotionInfo.setIsBookmarked(values.getInt("is_bookmarked"));
					
					ArrayList<OfferPromotionPhoto> photo_arraylist = new ArrayList<OfferPromotionPhoto>();
					JSONArray values_photoArray = values.getJSONArray("photo");
					if(values_photoArray.length() > 1) {
						for(int j = 0; j < values_photoArray.length(); j++) {
							OfferPromotionPhoto photo = new OfferPromotionPhoto();
							JSONObject photoObject = values_photoArray.getJSONObject(j);
							photo.setId(photoObject.getInt("id"));
							photo.setOffer_and_promotion_id(photoObject.getInt("offer_and_promotion_id"));
							photo.setPhoto(photoObject.getString("photo"));
							photo.setIsDelete(photoObject.getInt("is_del"));
							photo_arraylist.add(photo);
						}
						offerPromotionInfo.setPhotoArray(photo_arraylist);
					}
					arrayList.add(offerPromotionInfo);
				} // End For Loop
				
				if (!"".equals(last_element_id) && direction.equals("1")) {
					SidraPulseApp.getInstance().getOfferPromotionInfoList().addAll(arrayList);
					
				} else if (!"".equals(last_element_id) && direction.equals("0")) {
					SidraPulseApp.getInstance().getOfferPromotionInfoList().addAll(0,arrayList);
					
				} else {
					SidraPulseApp.getInstance().setOfferPromotionInfoList(arrayList);
				}
				
				if (dataArray.length() == 10) {
					ConstantValues.PullDownActive = true;
				} else {
					ConstantValues.PullDownActive = false;
				}
				
				
			} else {
				SidraPulseApp.getInstance().setOfferPromotionInfoList(null);
			} // End dataArray length checking
			
		} else {
			
			if (Integer.parseInt(last_element_id) > 1) {
			} else {
				SidraPulseApp.getInstance().setOfferPromotionInfoList(null);
			} 
			
		}
		
		return jDataObj.getInt("status");
	}*/
	
	
	public static int getBookmarkOfferAndPromotion(String func_id, String user_id, String access_token, String offer_id, String is_bookmark) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(5);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("offer_id", offer_id));
		postData.add((NameValuePair) new BasicNameValuePair("is_bookmark", is_bookmark));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseBookmarkOfferAndPromotion(serverResponse);
	}

	private static int parseBookmarkOfferAndPromotion(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getInt("status");
	}
	
	
	public static int getOfferAndPromotionCategories(String func_id, String user_id, String access_token) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(4);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return getOfferAndPromotionCategories(serverResponse);
	}

	private static int getOfferAndPromotionCategories (String jData) throws JSONException, ParseException {
		
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<OfferPromotionCategories> arrayList = new ArrayList<OfferPromotionCategories>();
		
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			
			if(dataArray.length() > 0) {
				Log.e("category data", "---------"+dataArray);
				for(int i = 0; i < dataArray.length(); i++) {
					OfferPromotionCategories catagoryInfo = new OfferPromotionCategories();
					JSONObject values = dataArray.getJSONObject(i);
					catagoryInfo.setId(values.getInt("id"));
					catagoryInfo.setCategoryName(values.getString("cat_name").trim());
					catagoryInfo.setCreatedBy(values.getInt("created_by"));
					catagoryInfo.setUpdatedBy(values.getInt("updated_by"));
					catagoryInfo.setIsDelete(values.getInt("is_del"));
					catagoryInfo.setCreatedAt(Utilities.offerPromotionDateConvertion(values.getString("created_at").trim()).toString());
					catagoryInfo.setUpdatedAt(Utilities.offerPromotionDateConvertion(values.getString("updated_at").trim()));
					arrayList.add(catagoryInfo);
					Collections.sort(arrayList, OfferPromotionCategories.CategoryNameComparator);
					
				}
				
			}
		} else {

		}
		
		SidraPulseApp.getInstance().setOfferAndPromotionCategories(arrayList);
		
		return jDataObj.getInt("status");
	}
	
	
	
	
	public static int getGalleryData(String func_id, String user_id, String access_token, String year) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(4);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("year", year));
		
		
		
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseGalleryData(serverResponse);
	}

	private static int parseGalleryData(String jData) throws JSONException, ParseException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<GalleryInfo> arrayList = new ArrayList<GalleryInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					GalleryInfo info = new GalleryInfo();
					JSONObject values = dataArray.getJSONObject(i);
					info.setId(values.getInt("id"));
					info.setAlbumTitle(values.getString("cat_name").trim());
					info.setAlbumImageUrl(values.getString("thumb").trim());
					info.setPhotoNumber(Integer.toString(values.getInt("photos")));
					info.setVideoNumber(Integer.toString(values.getInt("videos")));
					info.setTotalAlbum(values.getInt("total_album"));
					info.setDate(Utilities.eventFullDateConvertion(values.getString("created_at").trim()));
					arrayList.add(info);
				}
			}
		} else {
			
		}
		
		SidraPulseApp.getInstance().setGalleryInfoList(arrayList);
		return jDataObj.getInt("status");
	}
	
	
	public static int getGalleryImageAndVideoData(String func_id, String user_id, String access_token, String gallery_id, String last_element_id, String direction) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(5);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("gallery_id", gallery_id));
		postData.add((NameValuePair) new BasicNameValuePair("last_element_id", last_element_id));
		postData.add((NameValuePair) new BasicNameValuePair("direction", direction));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseGalleryImageAndVideoData(serverResponse, last_element_id, direction);
	}

	private static int parseGalleryImageAndVideoData(String jData, String last_element_id, String direction) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<GalleryDetailsInfo> arrayList = new ArrayList<GalleryDetailsInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					GalleryDetailsInfo info = new GalleryDetailsInfo();
					JSONObject values = dataArray.getJSONObject(i);
					info.setMediaType(values.getInt("media_type"));
					info.setId(values.getInt("id"));
					info.setImageOrVideoUrl(values.getString("media").trim());
					arrayList.add(info);
				}
			}
			
		} else {
			
		}
		
		if (!"".equals(last_element_id) && direction.equals("1")) {
			SidraPulseApp.getInstance().getGalleryDetailsInfoList().addAll(arrayList);
			
		} else if (!"".equals(last_element_id) && direction.equals("0")) {
			SidraPulseApp.getInstance().getGalleryDetailsInfoList().addAll(0, arrayList);
			
		} else {
			SidraPulseApp.getInstance().setGalleryDetailsInfoList(arrayList);
		}
		
		return jDataObj.getInt("status");
	}
	
	public static int getLogoutData(String func_id, String user_id, String access_token) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseLogoutData(serverResponse);
	}

	private static int parseLogoutData(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getInt("status");
	}
	
	//API #7
	public static int getEventsData(String func_id, String user_id, String access_token, String type, String date, String last_element_id, String direction) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(7);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("type", type));
		postData.add((NameValuePair) new BasicNameValuePair("date", date));
		postData.add((NameValuePair) new BasicNameValuePair("last_element_id", last_element_id));
		postData.add((NameValuePair) new BasicNameValuePair("direction", direction));
		
		Log.i("EVENT POST DATA API NO 7","***" + postData);
		
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		Log.i("EVENTS","***" + serverResponse);
		return parseEventsData(serverResponse, last_element_id, direction);
	}

	private static int parseEventsData(String jData, String last_element_id, String direction) throws JSONException, ParseException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<EventsInfo> arrayList = new ArrayList<EventsInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					EventsInfo info = new EventsInfo();
					JSONObject values = dataArray.getJSONObject(i);
					info.setId(values.getInt("id"));
					info.setEventTitle(values.getString("event_title").trim());
					info.setVanue(values.getString("venue").trim());
					info.setEventDescription(values.getString("event_description").trim());
					info.setStartDate(Utilities.eventFullDateConvertion(values.getString("start_dt").trim()));
					info.setStartTime(Utilities.timeConversion(values.getString("start_dt").trim()));
					info.setEventStartDay(Utilities.eventDayConvertion(values.getString("start_dt").trim()).toUpperCase());
					info.setEventStartDate(Utilities.eventDateConvertion(values.getString("start_dt").trim()));
					info.setEventStartFullDate(Utilities.eventReminderDateConvertion(values.getString("start_dt").trim()));
					info.setEventEndFullDate(Utilities.eventReminderDateConvertion(values.getString("end_dt").trim()));
					info.setEndDate(Utilities.eventFullDateConvertion(values.getString("end_dt").trim()));
					info.setEndTime(Utilities.timeConversion(values.getString("end_dt").trim()));
					info.setEventEndDay(Utilities.eventDayConvertion(values.getString("end_dt").trim()).toUpperCase());
					info.setEventEndDate(Utilities.eventDateConvertion(values.getString("end_dt").trim()));
					info.setBookMarked(values.getInt("is_bookmarked"));
					arrayList.add(info);
					
				}
				
				if (!"".equals(last_element_id) && "1".equals(direction)) {
					SidraPulseApp.getInstance().getEventsInfoList().addAll(arrayList);
					
				} else if (!"".equals(last_element_id) && "0".equals(direction)) {
					SidraPulseApp.getInstance().getEventsInfoList().addAll(0, arrayList);
					
				} else {
					SidraPulseApp.getInstance().setEventsInfoList(arrayList);
				}
				
				if (dataArray.length() == 10) {
					ConstantValues.PullDownActive = true;
				} else {
					ConstantValues.PullDownActive = false;
				}
				
			} else {
				//SidraPulseApp.getInstance().setEventsInfoList(null);
			}
			
		} else {
			//SidraPulseApp.getInstance().setEventsInfoList(null);
		}
		
		return jDataObj.getInt("status");
	}
	
	public static int getStaffDirectoryDepartmentsData(String func_id, String user_id, String access_token) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseStaffDirectoryDepartmentsData(serverResponse);
	}

	private static int parseStaffDirectoryDepartmentsData(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<StaffDirectoryDepartmentsInfo> arrayList = new ArrayList<StaffDirectoryDepartmentsInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					StaffDirectoryDepartmentsInfo info = new StaffDirectoryDepartmentsInfo();
					JSONObject values = dataArray.getJSONObject(i);
					info.setId(values.getInt("id"));
					info.setDepartmentName(values.getString("dept_name").trim());
					arrayList.add(info);
				}
			}
		} else {
			
		}
		
		SidraPulseApp.getInstance().setDirectoryDepartmentsInfoList(arrayList);
		return jDataObj.getInt("status");
	}
	
	public static int getStaffDirectoryStaffListData(String func_id, String user_id, String access_token, String dept_id, String last_element_id, String direction) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(5);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("dept_id", dept_id));
		postData.add((NameValuePair) new BasicNameValuePair("last_element_id", last_element_id));
		postData.add((NameValuePair) new BasicNameValuePair("direction", direction));
		
		Log.e("StaffDirectory","post: ***" + postData);
		
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseStaffDirectoryStaffListData(serverResponse, last_element_id, direction);
	}

	private static int parseStaffDirectoryStaffListData(String jData, String last_element_id,String direction) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<StaffListInfo> arrayList = new ArrayList<StaffListInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					StaffListInfo info = new StaffListInfo();
					JSONObject values = dataArray.getJSONObject(i);
					info.setId(values.getInt("id")); 
					info.setName(values.getString("name").trim());
					info.setDesignation(values.getString("designation").trim());
					info.setDepartment(values.getString("dept_name").trim());
					info.setEmail(values.getString("email").trim());
					info.setOffice(values.getInt("office"));
					info.setMobile(Integer.toString(values.getInt("mobile")));
					info.setIsBookmarked(values.getInt("is_bookmarked"));
					arrayList.add(info);
				}
			}
			
			if (!"".equals(last_element_id) && "1".equals(direction)) {
				SidraPulseApp.getInstance().getStaffListInfoList().addAll(arrayList);
				
			} else if (!"".equals(last_element_id) && "0".equals(direction)) {
				SidraPulseApp.getInstance().getStaffListInfoList().addAll(0, arrayList);
				
			} else {
				SidraPulseApp.getInstance().setStaffListInfoList(arrayList);
			}
			
			if (dataArray.length() == 10) {
				ConstantValues.PullDownActive = true;
			} else {
				ConstantValues.PullDownActive = false;
			}
			
		} else {
			
		}
		
		//SidraPulseApp.getInstance().setStaffListInfoList(arrayList);
		return jDataObj.getInt("status");
	}
	
	public static int getEventMakeFavourite(String func_id, String user_id, String access_token, String is_fav, String event_id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(5);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("is_fav", is_fav));
		postData.add((NameValuePair) new BasicNameValuePair("event_id", event_id));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseEventMakeFavourite(serverResponse);
	}

	private static int parseEventMakeFavourite(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getInt("status");
	}
	
	
	public static int getSidraInNewsAPI(String func_id, String user_id, String access_token, String last_element_id, String direction) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(4);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("last_element_id", last_element_id));
		postData.add((NameValuePair) new BasicNameValuePair("direction", direction));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		Log.i("SIDRA_IN_NEWS", "*** " + serverResponse);
		return parseSidraInNewsAPI(serverResponse, last_element_id, direction);
	}

	private static int parseSidraInNewsAPI(String jData, String last_element_id, String direction) throws JSONException, ParseException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<SidraInNewsAPIInfo> arrayList = new ArrayList<SidraInNewsAPIInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					SidraInNewsAPIInfo newsApi = new SidraInNewsAPIInfo();
					JSONObject values = dataArray.getJSONObject(i);
					newsApi.setId(values.getInt("id"));
					newsApi.setHeadline(values.getString("headline").trim());
					newsApi.setReleaseDate(Utilities.dateConvertion(values.getString("release_date").trim())+", "+Utilities.timeConversion(values.getString("release_date").trim()));
					newsApi.setSourcePublication(values.getString("source_publication").trim());
					newsApi.setLink(values.getString("link").trim());
//					newsApi.setApprovedBy(values.getInt("approved_by"));
//					newsApi.setPublishedBy(values.getInt("published_by"));
					newsApi.setIsDelete(values.getInt("is_del"));
					newsApi.setIsDraft(values.getInt("is_draft"));
					newsApi.setCreatedBy(values.getInt("created_by"));
					newsApi.setUpdatedBy(values.getInt("updated_by"));
					newsApi.setCreatedAt(Utilities.dateConvertion(values.getString("created_at").trim()).toString()+", "+Utilities.timeConversion(values.getString("created_at").trim()));
					newsApi.setUpdatedAt(Utilities.dateConvertion(values.getString("updated_at").trim())+", "+Utilities.timeConversion(values.getString("updated_at").trim()));
					newsApi.setStatus(values.getInt("status"));
					arrayList.add(newsApi);
				}
			}
			
			if (!"".equals(last_element_id) && direction.equals("1")) {
				SidraPulseApp.getInstance().getSidraInNewsAPI().addAll(arrayList);
				
			}else if (!"".equals(last_element_id) && direction.equals("0")){
				SidraPulseApp.getInstance().getSidraInNewsAPI().addAll(0,arrayList);
				
			} else {
				SidraPulseApp.getInstance().setSidraInNewsAPI(arrayList);
				
			}
			
			if (dataArray.length() == 10) {
				ConstantValues.PullDownActive = true;
			} else {
				ConstantValues.PullDownActive = false;
			}
			
		} else {
			
		}
		
		return jDataObj.getInt("status");
	}
	
	//API #12
	public static int getSidraPressRelease(String func_id, String user_id, String access_token, String last_element_id , String direction) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(4);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("last_element_id", last_element_id));
		postData.add((NameValuePair) new BasicNameValuePair("direction", direction));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseSidraPressRelease(serverResponse, last_element_id, direction);
	}

	private static int parseSidraPressRelease(String jData, String last_element_id, String direction) throws JSONException, ParseException {
		JSONObject jDataObj = new JSONObject(jData);
		
		ArrayList<SidraPressReleaseInfo> arrayList = new ArrayList<SidraPressReleaseInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					SidraPressReleaseInfo pressRelease = new SidraPressReleaseInfo();
					JSONObject values = dataArray.getJSONObject(i);
					pressRelease.setId(values.getInt("id"));
					pressRelease.setTitle(values.getString("title").trim());
					pressRelease.setReleaseDate(Utilities.dateConvertion(values.getString("release_date").trim()));
					pressRelease.setReleaseTime(Utilities.timeConversion(values.getString("release_date").trim()));
					pressRelease.setContent(values.getString("content").trim());
//					pressRelease.setApprovedBy(values.getInt("approved_by"));
//					pressRelease.setPublishedBy(values.getInt("published_by"));
					pressRelease.setIsDelete(values.getInt("is_del"));
					pressRelease.setIsDraft(values.getInt("is_draft"));
					pressRelease.setCreatedBy(values.getInt("created_by"));
					pressRelease.setUpdatedBy(values.getInt("updated_by"));
					pressRelease.setCreatedAt(Utilities.dateConvertion(values.getString("created_at").trim())+", "+Utilities.timeConversion(values.getString("created_at").trim()));
					pressRelease.setUpdatedAt(Utilities.dateConvertion(values.getString("updated_at").trim())+", "+Utilities.timeConversion(values.getString("updated_at").trim()));
					pressRelease.setStatus(values.getInt("status"));	
					arrayList.add(pressRelease);
				}//end for loop
			}
			
			if (!"".equals(last_element_id) && direction.equals("1")) {
				SidraPulseApp.getInstance().getSidraPressRelease().addAll(arrayList);
			
			} else if (!"".equals(last_element_id) && direction.equals("0")) {
				SidraPulseApp.getInstance().getSidraPressRelease().addAll(0,arrayList);
			} else {
				SidraPulseApp.getInstance().setSidraPressRelease(arrayList);
			}
			
			if (dataArray.length() == 10) {
				ConstantValues.PullDownActive = true;
			} else {
				ConstantValues.PullDownActive = false;
			}
			
		} else {
			
		}
		
		return jDataObj.getInt("status");
	}
	
	public static int getHumanResourcesCategory(String func_id, String user_id, String access_token, String type, boolean isArticle, String last_element_id, String direction) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("type", type));
		postData.add((NameValuePair) new BasicNameValuePair("last_element_id", last_element_id));
		postData.add((NameValuePair) new BasicNameValuePair("direction", direction));
		Log.i("FAQ POST", "*****" + postData);
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		Log.e("Server response is: faq", "----------------" + serverResponse);
		return parseHumanResourcesCategory(serverResponse, isArticle, last_element_id, direction);
	}

	private static int parseHumanResourcesCategory(String jData, boolean isArticle, String last_element_id, String direction) throws JSONException, ParseException {
		JSONObject jDataObj = new JSONObject(jData);
		if(isArticle) {
			ArrayList<ArticleInfo> arrayList = new ArrayList<ArticleInfo>();
			if(jDataObj.getInt("status") == 1) {
				JSONArray dataArray = jDataObj.getJSONArray("data");
				if(dataArray.length() > 0) {
					for(int i = 0; i < dataArray.length(); i++) {
						ArticleInfo info = new ArticleInfo();
						JSONObject values = dataArray.getJSONObject(i);
						info.setId(values.getInt("id"));
						info.setCatId(values.getInt("cat_id"));
						info.setQuestion(values.getString("question").trim());
						info.setAnswer(values.getString("answer").trim());
						info.setUpdatedAt(Utilities.dateConvertion(values.getString("updated_at").trim())+", "+Utilities.timeConversion(values.getString("updated_at").trim()));
						arrayList.add(info);
					}
				}
				
				if (!"".equals(last_element_id) && direction.equals("1")) {
					SidraPulseApp.getInstance().getArticleInfoList().addAll(arrayList);
				
				} else if (!"".equals(last_element_id) && direction.equals("0")) {
					SidraPulseApp.getInstance().getArticleInfoList().addAll(1, arrayList);
					
				} else {
					SidraPulseApp.getInstance().setArticleInfoList(arrayList);
				}
				
				if (dataArray.length() == 10) {
					ConstantValues.PullDownActive = true;
				} else {
					ConstantValues.PullDownActive = false;
				}
				
			} else {
				
			}
			
			
		} else {
			ArrayList<HumanResourceInfo> arrayList = new ArrayList<HumanResourceInfo>();
			if(jDataObj.getInt("status") == 1) {
				JSONArray dataArray = jDataObj.getJSONArray("data");
				if(dataArray.length() > 0) {
					for(int i = 0; i < dataArray.length(); i++) {
						HumanResourceInfo info = new HumanResourceInfo();
						JSONObject values = dataArray.getJSONObject(i);
						info.setId(values.getInt("id"));
						info.setQuestion(values.getString("question").trim());
						info.setAnswer(values.getString("answer").trim());
						arrayList.add(info);
					}
				}
				
				if (!"".equals(last_element_id) && direction.equals("1")) {
					SidraPulseApp.getInstance().getHumanResourceInfoList().addAll(arrayList);
				
				} else if (!"".equals(last_element_id) && direction.equals("0")) {
					SidraPulseApp.getInstance().getHumanResourceInfoList().addAll(0, arrayList);
					
				} else {
					SidraPulseApp.getInstance().setHumanResourceInfoList(arrayList);;
				}
				
				if (dataArray.length() == 10) {
					ConstantValues.PullDownActive = true;
				} else {
					ConstantValues.PullDownActive = false;
				}
				
			} else {
				
			}
			
			//SidraPulseApp.getInstance().setHumanResourceInfoList(arrayList);
		}
		
		return jDataObj.getInt("status");
	}
	
	
	public static int getHumanResourceList(String func_id, String user_id, String access_token, String cat_id, String page_no) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(5);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("cat_id", cat_id));
		postData.add((NameValuePair) new BasicNameValuePair("page_no", page_no));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseHumanResourceList(serverResponse);
	}

	private static int parseHumanResourceList(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<HumanResourceInfo> arrayList = new ArrayList<HumanResourceInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					HumanResourceInfo info = new HumanResourceInfo();
					JSONObject values = dataArray.getJSONObject(i);
					info.setId(values.getInt("id"));
					info.setQuestion(values.getString("question").trim());
					info.setAnswer(values.getString("answer").trim());
					arrayList.add(info);
				}
			}
		} else {
			
		}
		
		SidraPulseApp.getInstance().setHumanResourceInfoList(arrayList);
		return jDataObj.getInt("status");
	}
	
	// API #17 Show Classifieds 
	public static int getShowClassified(String func_id, String user_id, String access_token, String type, String cat_id, String last_element_id, String direction) throws Exception {
		Log.e("is classed", "");
		List<NameValuePair> postData = new ArrayList<NameValuePair>(6);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("type", type));
		postData.add((NameValuePair) new BasicNameValuePair("cat_id", cat_id));
		postData.add((NameValuePair) new BasicNameValuePair("last_element_id", last_element_id));
		postData.add((NameValuePair) new BasicNameValuePair("direction", direction));
		
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseShowClassified(serverResponse, last_element_id, direction);
	}

	private static int parseShowClassified(String jData, String last_element_id, String direction) throws JSONException, ParseException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<ClassifiedInfo> arrayList = new ArrayList<ClassifiedInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					ClassifiedInfo info = new ClassifiedInfo();
					JSONObject values = dataArray.getJSONObject(i);
					info.setId(values.getInt("id"));
					info.setTitle(values.getString("title").trim());
					info.setDescription(values.getString("description").trim());
					info.setCatId(values.getInt("cat_id"));
					info.setCreatedBy(values.getInt("created_by"));
					info.setUpdatedBy(values.getInt("updated_by"));
					info.setIsDel(values.getInt("is_del"));
					info.setStatus(values.getInt("status"));
					info.setIsDraft(values.getInt("is_draft"));
					info.setUpdatedAt(Utilities.eventFullDateConvertion(values.getString("updated_at").trim()));
					info.setCreatedAt(Utilities.eventFullDateConvertion(values.getString("created_at").trim()));
					
					JSONArray ownerDataArray = values.getJSONArray("owner_info");
					
					ClassifiedsOwnerInfo ownerInfo = new ClassifiedsOwnerInfo();
					
					if (ownerDataArray.length() > 0) {
						JSONObject ownerValues = ownerDataArray.getJSONObject(0);
						ownerInfo.setName(ownerValues.getString("name").trim());
						ownerInfo.setEmail(ownerValues.getString("email").trim());
						if(!"".equalsIgnoreCase(ownerValues.getString(("mobile").trim())))
								{
								ownerInfo.setMobile(ownerValues.getString(("mobile").trim()));
								}
						
					}
					
					info.setOwnerInfo(ownerInfo);
					
					JSONArray photoDataArray = values.getJSONArray("photo");
					ArrayList<ClassifiedsPhotoInfo> photoArrayList = new ArrayList<ClassifiedsPhotoInfo>();
					if(photoDataArray.length() > 0){
						for(int j = 0; j < photoDataArray.length(); j++) {
							ClassifiedsPhotoInfo photoInfo = new ClassifiedsPhotoInfo();
							JSONObject photoValues = photoDataArray.getJSONObject(j);
							photoInfo.setId(photoValues.getInt("id"));
							photoInfo.setPhoto(photoValues.getString("photo").trim());
							photoInfo.setClassifiedId(photoValues.getInt("classified_id"));
							photoInfo.setIsDel(photoValues.getInt("is_del"));
							photoInfo.setUpdatedAt(photoValues.getString("updated_at").trim());
							photoInfo.setCreatedAt(Utilities.eventFullDateConvertion(photoValues.getString("created_at").trim()));
							
							photoArrayList.add(photoInfo);
						}
						info.setPhotoInfo(photoArrayList);
					}
					
					arrayList.add(info);
				}
				
				
				if (!"".equals(last_element_id) && direction.equals("1")) {
					SidraPulseApp.getInstance().getClassifiedInfoList().addAll(arrayList);
					
				} else if (!"".equals(last_element_id) && direction.equals("0")) {
					SidraPulseApp.getInstance().getClassifiedInfoList().addAll(0,arrayList);
					
				} else {
					SidraPulseApp.getInstance().setClassifiedInfoList(arrayList);
				}
				
				if (dataArray.length() == 10) {
					ConstantValues.PullDownActive = true;
				} else {
					ConstantValues.PullDownActive = false;
				}
				
			}
			
		} else {
			
		}
		
		/*if (Integer.parseInt(last_element_id) > 1) {
			SidraPulseApp.getInstance().getClassifiedInfoList().addAll(arrayList);
		} else {
			SidraPulseApp.getInstance().setClassifiedInfoList(arrayList);
		}
		*/
		
		
		return jDataObj.getInt("status");
	}
	
	// API #18 Classifieds Categories
	public static int getClassifiedCategories(String func_id, String user_id, String access_token) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseClassifiedCategories(serverResponse);
	}

	private static int parseClassifiedCategories(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<ClassifiedCategoryInfo> arrayList = new ArrayList<ClassifiedCategoryInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					ClassifiedCategoryInfo pressRelease = new ClassifiedCategoryInfo();
					JSONObject values = dataArray.getJSONObject(i);
					pressRelease.setId(values.getInt("id"));
					pressRelease.setCategoryName(values.getString("cat_name").trim());	
					arrayList.add(pressRelease);
				}
			}
		} else {
			
		}
		
		SidraPulseApp.getInstance().setClassifiedCategoryInfoList(arrayList);
		return jDataObj.getInt("status");
	}
	
	// API# 19 Classifieds Photo Upload
	public static String getClassifiedPhotoUpload(String func_id, String user_id, String access_token, String photo) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(4);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("photo", photo));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseClassifiedPhotoUpload(serverResponse);
	}

	private static String parseClassifiedPhotoUpload(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		
		JSONObject photoData = jDataObj.getJSONObject("data");
		String  photoName = photoData.getString("photo_name").trim();
		//return jDataObj.getInt("status");
		return photoName;
	}
	
	// API #20 Classified Entry
	public static int getClassifiedEntry(String func_id, String user_id, String access_token, String cat_id, String title, String description, String photo, String is_draft) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(8);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("cat_id", cat_id));
		postData.add((NameValuePair) new BasicNameValuePair("title", title));
		postData.add((NameValuePair) new BasicNameValuePair("description", description));
		postData.add((NameValuePair) new BasicNameValuePair("photo", photo));
		postData.add((NameValuePair) new BasicNameValuePair("is_draft", is_draft));

		Log.e("classified entry is", "--"+ postData);
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseClassifiedEntry(serverResponse);
	}

	private static int parseClassifiedEntry(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getInt("status");
	}
	
	// API #21
	public static int getShowTagList(String func_id, String user_id, String access_token) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseShowTagList(serverResponse);
	}

	private static int parseShowTagList(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<ForumsTagInfo> arrayList = new ArrayList<ForumsTagInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					ForumsTagInfo pressRelease = new ForumsTagInfo();
					JSONObject values = dataArray.getJSONObject(i);
					pressRelease.setId(values.getInt("id"));
					pressRelease.setTagName(values.getString("tag_name").trim());	
					arrayList.add(pressRelease);
				}
			}
		} else {
			
		}
		
		SidraPulseApp.getInstance().setForumsTagInfoList(arrayList);
		return jDataObj.getInt("status");
	}
	
    // API #22
	public static int getAPIShowForumList(String func_id, String user_id, String access_token, String type, String tag_id, String last_element_id, String direction) throws Exception {
		Log.i("FORUM_LIST_PARAM","f_id: " + func_id + " accToken: " + access_token + " type: " + type + " tag: " + tag_id + " element_id: " + last_element_id + " Direction" + direction);
		List<NameValuePair> postData = new ArrayList<NameValuePair>(6);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("type", type));
		postData.add((NameValuePair) new BasicNameValuePair("tag_id", tag_id));
		postData.add((NameValuePair) new BasicNameValuePair("last_element_id", last_element_id));
		postData.add((NameValuePair) new BasicNameValuePair("direction", direction));
		
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		Log.i("FORUM RESPONSE","DATA " + serverResponse);
		
		return parseAPIShowForumList(serverResponse, last_element_id, direction);
	}

	private static int parseAPIShowForumList(String jData, String last_element_id, String direction) throws JSONException, ParseException {
		Calendar calendarDay = Calendar.getInstance(); 
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<ForumsInfo> arrayList = new ArrayList<ForumsInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					ForumsInfo info = new ForumsInfo();
					JSONObject values = dataArray.getJSONObject(i);
					info.setId(values.getInt("id"));
					info.setText(values.getString("text").trim());
					info.setCreatedBy(values.getInt("created_by"));
					info.setUpdatedBy(values.getInt("updated_by"));
					info.setUpdatedAt(values.getString("updated_at").trim());
					
					if (Utilities.getNowDay(values.getString("created_at").trim()).equalsIgnoreCase(Integer.toString(calendarDay.get(Calendar.DATE)))) {
						info.setCreatedAt("Today"+", "+Utilities.timeConversion(values.getString("created_at").trim()));
					} else {
						info.setCreatedAt(Utilities.dateConvertion(values.getString("created_at").trim())+" "+Utilities.timeConversion(values.getString("created_at").trim()));
					}
					
					info.setIsDel(values.getInt("is_del"));
					info.setStatus(values.getInt("status"));
					
					JSONObject authorArrayData = values.getJSONObject("author_info");
					AuthorInfo authorInfo = new AuthorInfo();
					authorInfo.setName(authorArrayData.getString("name").trim());
					authorInfo.setEmail(authorArrayData.getString("email").trim());
					authorInfo.setMobile(authorArrayData.getString("mobile").trim());
					info.setAuthorInfo(authorInfo);
					
					JSONArray photoArrayData = values.getJSONArray("photo");
					ArrayList<ForumPhotoInfo> photoArray = new ArrayList<ForumPhotoInfo>();
					if(photoArrayData.length() > 0) {
						for(int j = 0; j <photoArrayData.length(); j++) {
							ForumPhotoInfo photoInfo = new ForumPhotoInfo();
							JSONObject photoValue = photoArrayData.getJSONObject(j);
							photoInfo.setId(photoValue.getInt("id"));
							photoInfo.setForumId(photoValue.getInt("forum_id"));
							photoInfo.setPhoto(photoValue.getString("photo").trim());
							photoInfo.setStatus(photoValue.getInt("status"));
							photoInfo.setUpdatedAt(Utilities.offerPromotionDateConvertion(photoValue.getString("updated_at").trim()));
							
							if (Utilities.getNowDay(photoValue.getString("created_at").trim()).equalsIgnoreCase(Integer.toString(calendarDay.get(Calendar.DATE)))) {
								photoInfo.setCreatedAt("Today"+", "+Utilities.timeConversion(values.getString("created_at").trim()));
							} else {
								//photoValue.getString("created_at")
								photoInfo.setCreatedAt(Utilities.dateConvertion(photoValue.getString("created_at").trim())+", "+Utilities.timeConversion(values.getString("created_at")));
							}
							
							
							photoArray.add(photoInfo);
						}
					}
					
					info.setForumPhotos(photoArray);
					
					JSONArray commmentsArrayData = values.getJSONArray("comments");
					ArrayList<ForumComments> commmentsArray = new ArrayList<ForumComments>();
					if(commmentsArrayData.length()>0)
					{
						for(int k = 0; k <commmentsArrayData.length(); k++) {
							ForumComments commentsInfo = new ForumComments();
							JSONObject commentsValue = commmentsArrayData.getJSONObject(k);
							commentsInfo.setId(commentsValue.getInt("id"));
							commentsInfo.setUserId(commentsValue.getInt("user_id"));
							commentsInfo.setForumId(commentsValue.getInt("forum_id"));
							commentsInfo.setCommentText(commentsValue.getString("comment_text").trim());
							commentsInfo.setVideoUrl(commentsValue.getString("video").trim());
							commentsInfo.setCommentsPhoto(commentsValue.getString("image").trim());
							commentsInfo.setIsDel(commentsValue.getInt("is_del"));
							commentsInfo.setUpdatedAt(Utilities.offerPromotionDateConvertion(commentsValue.getString("updated_at").trim()));
							
							if (Utilities.getNowDay(commentsValue.getString("created_at").trim()).equalsIgnoreCase(Integer.toString(calendarDay.get(Calendar.DATE)))) {
								commentsInfo.setCreatedAt(("Today")+", "+Utilities.timeConversion(values.getString("created_at").trim()));
							} else {
								commentsInfo.setCreatedAt(Utilities.dateConvertion(commentsValue.getString("created_at").trim()) + ", " + Utilities.timeConversion(commentsValue.getString("created_at").trim()));
								
							}
							
							JSONObject commmentatorArrayData = commentsValue.getJSONObject("commentator");
							ForumCommentator commmentator = new ForumCommentator();
							
							if( commmentatorArrayData!=null){
								commmentator.setName(commmentatorArrayData.getString("name").trim());
								commmentator.setEmail(commmentatorArrayData.getString("email").trim());
								commmentator.setMobile(commmentatorArrayData.getString("mobile").trim());
							}
							else{
								Log.e("Commentator is null", "--------------------------");
							}
							
							commentsInfo.setCommentator(commmentator);
							commmentsArray.add(commentsInfo);
						}
						
					}
					
					info.setForumComments(commmentsArray);
					info.setDate(Utilities.eventDateConvertion(values.getString("created_at").trim()).toString());
					info.setTotalComments(values.getInt("total_comments"));
					arrayList.add(info);
					
				}
				
				if (!"".equals(last_element_id) && direction.equals("1")) {
					SidraPulseApp.getInstance().getForumsInfoList().addAll(arrayList);
					
				} else if (!"".equals(last_element_id) && direction.equals("0")) {
					SidraPulseApp.getInstance().getForumsInfoList().addAll(0, arrayList);
					
				} else {
					SidraPulseApp.getInstance().setForumsInfoList(arrayList);
					
				}
				
				if (dataArray.length() == 10) {
					ConstantValues.PullDownActive = true;
				} else {
					ConstantValues.PullDownActive = false;
				}
				
			}
			
		} else {
			
		}
		
		return jDataObj.getInt("status");
	}
	
	//API #23
	public static int getThreadPhotoUpload(String func_id, String user_id, String access_token, String photo) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(4);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("photo", photo));
		
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseThreadPhotoUpload(serverResponse);
	}

	private static int parseThreadPhotoUpload(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getInt("status");
	}
	
	//API #24
	public static int getThreadEntryAPI(String func_id, String user_id, String access_token, String photo, String text, String tags) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(6);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("photo", photo));
		postData.add((NameValuePair) new BasicNameValuePair("text", text));
		postData.add((NameValuePair) new BasicNameValuePair("tags", tags));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseThreadEntryAPI(serverResponse);
	}

	private static int parseThreadEntryAPI(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getInt("status");
	}
	
	public static int getShowAllPolicyDept(String func_id, String user_id, String access_token) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		Log.e("Server Response for policy dept", ""+serverResponse);
		return parseShowAllPolicyDept(serverResponse);
	}

	private static int parseShowAllPolicyDept(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<PoliciesDeptInfo> arrayList = new ArrayList<PoliciesDeptInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					PoliciesDeptInfo info = new PoliciesDeptInfo();
					JSONObject values = dataArray.getJSONObject(i);
					info.setId(values.getInt("id"));
					info.setCatName(values.getString("cat_name").trim());	
					arrayList.add(info);
				}
			}
		} else {
			
		}
		
		SidraPulseApp.getInstance().setPoliciesDeptInfoList(arrayList);
		return jDataObj.getInt("status");
	}
	
	public static int getShowPolicyDataByDept(String func_id, String user_id, String access_token, String dept_id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(4);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("dept_id", dept_id));
		
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		Log.e("Server Response for policy data by dept", ""+serverResponse);
		return parseShowPolicyDataByDept(serverResponse);
	}

	private static int parseShowPolicyDataByDept(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<PoliceisInfo> arrayList = new ArrayList<PoliceisInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					PoliceisInfo info = new PoliceisInfo();
					JSONObject values = dataArray.getJSONObject(i);
					info.setId(values.getInt("id"));
					info.setTitle(values.getString("title").trim());
					info.setPolicyNo(values.getInt("policy_no"));
					info.setOverview(values.getString("overview").trim());
					info.setPolicyStatement(values.getString("policy_statement").trim());
					info.setDefinitions(values.getString("definitions").trim());
					info.setReference(values.getString("reference").trim());
					arrayList.add(info);
				}
			}
		} else {
			
		}
		
		SidraPulseApp.getInstance().setPoliceisInfoList(arrayList);
		return jDataObj.getInt("status");
	}
	
	public static int getDeleteAPI(String func_id, String user_id, String access_token, String type, String element_id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(5);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("type", type));
		postData.add((NameValuePair) new BasicNameValuePair("element_id", element_id));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseDeleteAPI(serverResponse);
	}

	private static int parseDeleteAPI(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getInt("status");
	}
	
	public static int getForumComment(String func_id, String user_id, String access_token, String forum_id, String comment_text) throws Exception {
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>(5);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("forum_id", forum_id));
		postData.add((NameValuePair) new BasicNameValuePair("comment_text", comment_text));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseForumComment(serverResponse);
	}

	private static int parseForumComment(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getInt("status");
	}
	
	public static int getStaffSearchData(String func_id, String user_id, String access_token, String dept_id, String keyword) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(5);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("dept_id", dept_id));
		postData.add((NameValuePair) new BasicNameValuePair("keyword", keyword));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseStaffSearch(serverResponse);
	}

	private static int parseStaffSearch(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<StaffListInfo> arrayList = new ArrayList<StaffListInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					StaffListInfo info = new StaffListInfo();
					JSONObject values = dataArray.getJSONObject(i);
					info.setId(values.getInt("id")); 
					info.setName(values.getString("name").trim());
					info.setDesignation(values.getString("designation").trim());
					info.setDepartment(values.getString("dept_name").trim());
					info.setEmail(values.getString("email").trim());
					info.setOffice(values.getInt("office"));
					info.setMobile(values.getString("mobile").trim());
					info.setIsBookmarked(values.getInt("is_bookmarked"));
					arrayList.add(info);
				}
			}
		} else {
			
		}
		
		SidraPulseApp.getInstance().setStaffListInfoList(arrayList);
		return jDataObj.getInt("status");
	}
	
	public static int getStaffBookmarkData(String func_id, String user_id, String access_token,  String is_bookmark, String staff_id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(5);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("is_bookmark", is_bookmark));
		postData.add((NameValuePair) new BasicNameValuePair("staff_id", staff_id));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseStaffBookmard(serverResponse);
	}

	private static int parseStaffBookmard(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getInt("status");
	}
	
	
	// API #35 Classified Entry
	public static int getClassifiedUpdate(String func_id, String user_id, String access_token, String cat_id, String classified_id, String title, String description, String photo, String is_draft) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(9);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("cat_id", cat_id));
		postData.add((NameValuePair) new BasicNameValuePair("classified_id", classified_id ));
		postData.add((NameValuePair) new BasicNameValuePair("title", title));
		postData.add((NameValuePair) new BasicNameValuePair("description", description));
		postData.add((NameValuePair) new BasicNameValuePair("photo", photo));
		postData.add((NameValuePair) new BasicNameValuePair("is_draft", is_draft));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseClassifiedUpfate(serverResponse);
	}

	private static int parseClassifiedUpfate(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getInt("status");
	}
	

	//API #36
	public static int getGalleryYearData(String func_id, String user_id, String access_token) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(2);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		return parseGalleryYearData(serverResponse);
	}
	
	private static int parseGalleryYearData(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<GalleryYearInfo> arrayList = new ArrayList<GalleryYearInfo>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					GalleryYearInfo info = new GalleryYearInfo();
					JSONObject values = dataArray.getJSONObject(i);
					info.setTotalAlbum(values.getInt("total_album")); 
					info.setYear(values.getString("year").trim());
					arrayList.add(info);
				}
				
				SidraPulseApp.getInstance().setGalleryYearInfoList(arrayList);
			}
		} else {
			SidraPulseApp.getInstance().setGalleryYearInfoList(null);
		}
		
		return jDataObj.getInt("status");
	}
	
	
	public static int getAPIShowForumTopics(String func_id, String user_id, String access_token, String type, String tag_id, String last_element_id, String direction) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(6);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("user_id", user_id));
		postData.add((NameValuePair) new BasicNameValuePair("access_token", access_token));
		postData.add((NameValuePair) new BasicNameValuePair("type", type));
		postData.add((NameValuePair) new BasicNameValuePair("tag_id", tag_id));
		postData.add((NameValuePair) new BasicNameValuePair("page_no", last_element_id));
	
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SidraPulseApp.getInstance().getBaseUrl(), postData, true);
		Log.e("Server Response", "---" + serverResponse);
		return parseAPIShowForumTopics(serverResponse, last_element_id, direction);
	}

	private static int parseAPIShowForumTopics(String jData, String last_element_id, String direction) throws JSONException, ParseException {
		JSONObject jDataObj = new JSONObject(jData);
		ArrayList<ForumHotTopics> arrayList = new ArrayList<ForumHotTopics>();
		if(jDataObj.getInt("status") == 1) {
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0; i < dataArray.length(); i++) {
					ForumHotTopics hotTopics = new ForumHotTopics();
					JSONObject values = dataArray.getJSONObject(i);
					hotTopics.setTagId(values.getInt("tag_id"));
					hotTopics.setTagName(values.getString("tag_name").trim());
					hotTopics.setTotal(values.getInt("total_posts"));
					arrayList.add(hotTopics);
				}
			}
		} else {
			
		}
		SidraPulseApp.getInstance().setHotTopicsList(arrayList);
		return jDataObj.getInt("status");
	}
	
	
}
