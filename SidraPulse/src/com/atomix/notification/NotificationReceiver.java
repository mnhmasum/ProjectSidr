package com.atomix.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
         
		Intent service1 = new Intent(context, NotificationService.class);
		Log.e("title from broadcast receiver", "--"+intent.getStringExtra("EVENT_TITLE"));
		service1.putExtra("EVENT_TITLE", intent.getStringExtra("EVENT_TITLE"));
		service1.putExtra("EVENT_ID", intent.getIntExtra("EVENT_ID", 0));
		context.startService(service1);
	}
}