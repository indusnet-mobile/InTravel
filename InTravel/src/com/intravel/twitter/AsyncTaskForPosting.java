package com.intravel.twitter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncTaskForPosting extends AsyncTask<Void, Void, Void>{

	private ProgressDialog postDialog;;
	private String msg;
	private Context context;
	private  twitter4j.Twitter twitter;
	private int flag = 0;
	public AsyncTaskForPosting (Context context, String string, twitter4j.Twitter twitter) {
		this.context = context;
		this.msg = string;
		this.twitter = twitter;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		postDialog = ProgressDialog.show(context, "",
				"Posting Comments...", true); 
	}
	@Override
	protected Void doInBackground(Void... params) {
		try {
			twitter.updateStatus(msg);
			flag = 1;
		} catch (Exception e) {
			flag = 0;
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		postDialog.dismiss();
		if (flag == 1) {
			Toast.makeText(context,
					"Tweeted Successfully", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, "Whoops! You already said that.",
					Toast.LENGTH_SHORT).show();
		}
	}
}
