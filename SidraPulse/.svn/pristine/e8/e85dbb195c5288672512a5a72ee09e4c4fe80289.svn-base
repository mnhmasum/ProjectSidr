package com.atomix.sharedpreference;

import java.util.ArrayList;

import com.atomix.datamodel.DraftClassifiedInfo;
import com.atomix.sidrainfo.SidraPulseApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseUtil {

	private static final String TAG = "DataBaseUtil";
	private static final String DATABASE_NAME = "db_sidra_pulse";
	private static final String DATABASE_TABLE = "tbl_post_draft";
	private static final int DATABASE_VERSION = 2;

	// Table Columns
	public static final String KEY_ROWID = "id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DATE = "date";
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_IMAGE_URL_1 = "url_1";
	public static final String KEY_IMAGE_URL_2 = "url_2";
	public static final String KEY_IMAGE_URL_3 = "url_3";

	public String[] columns = {KEY_ROWID, KEY_TITLE, KEY_DATE, KEY_CATEGORY, KEY_DESCRIPTION, KEY_IMAGE_URL_1, KEY_IMAGE_URL_2, KEY_IMAGE_URL_3};

	private static final String CREATE_TABLE_INFO = "create table "
			+ DATABASE_TABLE + " ("+ KEY_ROWID + " integer primary key autoincrement, " 
			+ KEY_TITLE + " text, " 
			+ KEY_DATE + " text, "
			+ KEY_CATEGORY + " text, "
			+ KEY_DESCRIPTION + " text, "
			+ KEY_IMAGE_URL_1 + " text, "
			+ KEY_IMAGE_URL_2 + " text, "
			+ KEY_IMAGE_URL_3 + " text);";

	private final Context context;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqlDb;

	public DataBaseUtil(Context ctx) {
		this.context = ctx;
	}

	public DataBaseUtil open() throws SQLException {
		dbHelper = new DatabaseHelper(context);
		sqlDb = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		sqlDb.close();
	}

	public long insertPostForDraft(String title, String date, String category, String description, String url_1, String url_2, String url_3) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_TITLE, title);
		cv.put(KEY_DATE, date);
		cv.put(KEY_CATEGORY, category);
		cv.put(KEY_DESCRIPTION, description);
		cv.put(KEY_IMAGE_URL_1, url_1);
		cv.put(KEY_IMAGE_URL_2, url_2);
		cv.put(KEY_IMAGE_URL_3, url_3);

		return sqlDb.insert(DATABASE_TABLE, null, cv);
	}

	public void fetchAllDraft(String categoryId) {
		Cursor cursor = sqlDb.query(DATABASE_TABLE, columns, KEY_CATEGORY+ "= ?", new String[] { String.valueOf(categoryId) }, null, null, null);
		cursor.moveToFirst();
		ArrayList<DraftClassifiedInfo> arrayList = new ArrayList<DraftClassifiedInfo>();
		while(!cursor.isAfterLast()) {
			DraftClassifiedInfo info = new DraftClassifiedInfo();
			info.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
			info.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
			info.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
			info.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
			info.setUrl_1(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URL_1)));
			info.setUrl_2(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URL_2)));
			info.setUrl_3(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URL_3)));
			arrayList.add(info);
			cursor.moveToNext();
		}
		SidraPulseApp.getInstance().setDraftClassifiedInfoList(arrayList);
		cursor.close();
	}
	
	public ArrayList<String> fetchSingleDraft(String draft_id) {
		ArrayList<String> wordList = new ArrayList<String>();
		Cursor cursor = sqlDb.query(DATABASE_TABLE, columns, KEY_ROWID+ "= ?", new String[] { String.valueOf(draft_id) }, null, null, null);
		wordList.add(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
		wordList.add(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
		wordList.add(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
		wordList.add(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
		wordList.add(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URL_1)));
		wordList.add(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URL_2)));
		wordList.add(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URL_3)));
		
		cursor.close();
		return wordList;
	}

	private class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE_INFO);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading Database from version " + oldVersion + " to " + newVersion + " version.");
		}
	}
}
