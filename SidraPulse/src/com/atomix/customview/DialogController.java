package com.atomix.customview;

import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.atomix.sidrapulse.R;

public class DialogController {
	private Dialog dialogReply;
	private Dialog dialog;
	private Activity activity;
	
	public DialogController(Activity activity) {
		this.activity = activity;
	}
	
	public Dialog ForumsReplyDialog(){
		dialogReply = new Dialog(this.activity);
		dialogReply.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogReply.setContentView(R.layout.dialog_forum_reply_post);
		dialogReply.setCanceledOnTouchOutside(true);
		dialogReply.setCancelable(true);
		dialogReply.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialogReply.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		return dialogReply;
	}
	
	public Dialog ExtendsForumsReplyDialog(Activity activity, Boolean isMainMenu){
		dialogReply = new Dialog(activity);
		dialogReply.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogReply.setContentView(R.layout.dialog_forum_reply_post);
		dialogReply.setCanceledOnTouchOutside(true);
		dialogReply.setCancelable(true);
		dialogReply.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialogReply.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		return dialogReply;
	}
	
	public Dialog PostNoticeDialog(){
		dialog = new Dialog(this.activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.posting_notice_dialog);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		return dialog;
	}
	
	public Dialog DeleteConfirmationDialog(){
		dialog = new Dialog(this.activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.confirm_delete_dialog);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		return dialog;
	}
	
}
