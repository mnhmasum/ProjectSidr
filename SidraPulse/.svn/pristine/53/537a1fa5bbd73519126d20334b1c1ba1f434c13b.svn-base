package com.atomix.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.atomix.sidrapulse.R;
import com.atomix.sidrapulse.SplashScreenActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentServive");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString(), "0");
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server: " + extras.toString(), "0");
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				// This loop represents the service doing some work.
				for (int i = 0; i < 5; i++) {
					Log.i("", "Working... " + (i + 1) + "/5 @ " + SystemClock.elapsedRealtime());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {

					}
				}
			}

			Log.i("", "Completed work @ " + SystemClock.elapsedRealtime());
			// Post notification of received message.
			sendNotification(extras.getString("message"), extras.getString("type"));
			Log.i("", "Received: " + extras.toString());
		}

		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg, String key) {
		if(msg != null) {
			mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
			
			/*if("1".equals(key)) {
				contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, AnnouncementsActivity.class), 0);
			} else if("2".equals(key)) {
				contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ForumsActivity.class), 0);
			} else {
				contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, SplashScreenActivity.class), 0);
			}
			*/
			int pushType = 0;
			PendingIntent contentIntent;
			
			int flag = 0;
			
			if ("1".equals(key)) {
				flag = 1;
				pushType = 1;
			} else if ("2".equals(key)) {
				flag = 2;
				pushType = 2;
			} else {
				flag = 0;
				pushType = 0;
			}
			
			contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, SplashScreenActivity.class).putExtra("type", pushType), PendingIntent.FLAG_UPDATE_CURRENT);
			
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this).setSmallIcon(R.drawable.app_icon)
					.setContentTitle(getResources().getString(R.string.app_name))
					.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
					.setContentText(msg)
					.setAutoCancel(true);

			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		}
	}
}
