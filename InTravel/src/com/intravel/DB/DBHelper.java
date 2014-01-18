package com.intravel.DB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {


	private static String DB_NAME = "Travel.sqlite";
	private static DBHelper mInstance;
	private Context context;

	private static SQLiteDatabase sqliteDb;
	private static final int DATABASE_VERSION = 1;
	private static String DB_PATH_PREFIX = "/data/data/";
	private static String DB_PATH_SUFFIX = "/databases/";
	private static final String TAG = "DBHelper";

	/***
	 * Contructor
	 * 
	 * @param context
	 *            : app context
	 * @param name
	 *            : database name
	 * @param factory
	 *            : cursor Factory
	 * @param version
	 *            : DB version
	 */
	private DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context = context;
		Log.i(TAG, "Create or Open database : " + name);
	}

	/***
	 * Initialize method
	 * 
	 * @param context
	 *            : application context
	 * @param databaseName
	 *            : database name
	 */
	private static void initialize(Context context, String databaseName) {
		if (mInstance == null) {
			/**
			 * Try to check if there is an Original copy of DB in asset
			 * Directory
			 */
			if (!checkDatabase(context, databaseName)) {
				// if not exists, I try to copy from asset dir
				try {
					copyDataBase(context, databaseName);
				} catch (IOException e) {
					Log.e(TAG,
							"Database "
									+ databaseName
									+ " does not exists and there is no Original Version in Asset dir");
				}
			}

			Log.i(TAG, "Try to create instance of database (" + databaseName
					+ ")");
			mInstance = new DBHelper(context, databaseName, null,
					DATABASE_VERSION);
			sqliteDb = mInstance.getWritableDatabase();
			Log.i(TAG, "instance of database (" + databaseName + ") created !");
		}
	}

	/***
	 * Static method for getting singleton instance
	 * 
	 * @param context
	 *            : application context
	 * @param databaseName
	 *            : database name
	 * @return : singleton instance
	 */
	public static final DBHelper getInstance(
			Context context) {
		initialize(context, DB_NAME);
		return mInstance;
	}

	/***
	 * Method to get database instance
	 * 
	 * @return database instance
	 */
	public SQLiteDatabase getDatabase() {
		return sqliteDb;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "onCreate : nothing to do");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "onCreate : nothing to do");

	}

	/***
	 * Method for Copy the database from asset directory to application's data
	 * directory
	 * 
	 * @param databaseName
	 *            : database name
	 * @throws IOException
	 *             : exception if file does not exists
	 */
	private void copyDataBase(String databaseName) throws IOException {
		copyDataBase(context, databaseName);
	}

	/***
	 * Static method for copy the database from asset directory to application's
	 * data directory
	 * 
	 * @param aContext
	 *            : application context
	 * @param databaseName
	 *            : database name
	 * @throws IOException
	 *             : exception if file does not exists
	 */
	private static void copyDataBase(Context aContext, String databaseName)
			throws IOException {

		// Open your local db as the input stream
		InputStream myInput = aContext.getAssets().open(databaseName);

		// Path to the just created empty db
		String outFileName = getDatabasePath(aContext, databaseName);

		Log.i(TAG,
				"Check if create dir : " + DB_PATH_PREFIX
						+ aContext.getPackageName() + DB_PATH_SUFFIX);

		// if the path doesn't exist first, create it
		File f = new File(DB_PATH_PREFIX + aContext.getPackageName()
				+ DB_PATH_SUFFIX);
		if (!f.exists())
			f.mkdir();

		Log.i(TAG, "Trying to copy local DB to : " + outFileName);

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

		Log.i(TAG, "DB (" + databaseName + ") copied!");
	}

	/***
	 * Method to check if database exists in application's data directory
	 * 
	 * @param databaseName
	 *            : database name
	 * @return : boolean (true if exists)
	 */
	public boolean checkDatabase(String databaseName) {
		return checkDatabase(context, databaseName);
	}

	/***
	 * Static Method to check if database exists in application's data directory
	 * 
	 * @param aContext
	 *            : application context
	 * @param databaseName
	 *            : database name
	 * @return : boolean (true if exists)
	 */
	public static boolean checkDatabase(Context aContext, String databaseName) {
		SQLiteDatabase checkDB = null;

		try {
			String myPath = getDatabasePath(aContext, databaseName);

			Log.i(TAG, "Trying to conntect to : " + myPath);
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
			Log.i(TAG, "Database " + databaseName + " found!");
			checkDB.close();
		} catch (SQLiteException e) {
			Log.i(TAG, "Database " + databaseName + " does not exists!");

		}

		return checkDB != null ? true : false;
	}

	/***
	 * Method that returns database path in the application's data directory
	 * 
	 * @param databaseName
	 *            : database name
	 * @return : complete path
	 */
	private String getDatabasePath(String databaseName) {
		return getDatabasePath(context, databaseName);
	}

	/***
	 * Static Method that returns database path in the application's data
	 * directory
	 * 
	 * @param aContext
	 *            : application context
	 * @param databaseName
	 *            : database name
	 * @return : complete path
	 */
	private static String getDatabasePath(Context aContext, String databaseName) {
		return DB_PATH_PREFIX + aContext.getPackageName() + DB_PATH_SUFFIX
				+ databaseName;
	}
}
