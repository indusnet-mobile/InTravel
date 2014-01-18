package com.intravel.Activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

import com.intravel.ApplicationConstant;
import com.intravel.R;
import com.intravel.Util;
import com.intravel.Facebook.DialogError;
import com.intravel.Facebook.Facebook;
import com.intravel.Facebook.Facebook.DialogListener;
import com.intravel.Facebook.FacebookError;
import com.intravel.Facebook.SessionStore;
import com.intravel.twitter.AsyncTwitterRunner;
import com.intravel.twitter.Keys;
import com.intravel.twitter.Twitter;
import com.intravel.twitter.Twitter.DialogListenerTwitter;
import com.intravel.twitter.TwitterError;

public class SharingActivity extends Activity {

	private Facebook facebook;
	private SharedPreferences sharedPreferences;
	private ApplicationConstant appConstant;
	public static Twitter mTwitter;
	public static AsyncTwitterRunner mTWT_AsyncRunner;
	Handler handlerTwitter;
	private OAuthConsumer mConsumer = null;
	private String title;
	private Dialog mdiDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		facebook = ((ApplicationConstant) getApplication()).mFaceBook;
		appConstant = ((ApplicationConstant) getApplication());
		mTwitter = new Twitter(R.drawable.twt_icon, this);
		mTWT_AsyncRunner = new AsyncTwitterRunner(mTwitter);
	}

	public void dialogshow(final String mesg) {

		try {
			title = mesg;
			mdiDialog = new Dialog(SharingActivity.this);
			mdiDialog.getWindow().getAttributes().windowAnimations = R.style.SlideDialogAnimation;
			mdiDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			mdiDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mdiDialog.setContentView(R.layout.sharing_dialog);

			((RelativeLayout) mdiDialog.findViewById(R.id.rel_facebook_sharing))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mdiDialog.cancel();
							if (Util.hasConnection(SharingActivity.this)) {
								sharingFB(SharingActivity.this, mesg);
							} else {
								Util.showToast(SharingActivity.this,
										"No internet connection");
							}
						}
					});

			((RelativeLayout) mdiDialog.findViewById(R.id.rel_twitter_sharing))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mdiDialog.cancel();
							if (Util.hasConnection(SharingActivity.this)) {
								sharingTW(SharingActivity.this, mesg);
							} else {
								Util.showToast(SharingActivity.this,
										"No internet connection");
							}
						}
					});

			((RelativeLayout) mdiDialog.findViewById(R.id.rel_email_sharing))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							mdiDialog.cancel();
							if (Util.hasConnection(SharingActivity.this)) {
								sendEmail(title);
							} else {
								Util.showToast(SharingActivity.this,
										"No internet connection");
							}
						}
					});

			mdiDialog.show();
			mdiDialog.setCancelable(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sharingFB(Context mContext, String title) {

		if (facebook != null && facebook.isSessionValid()) {
			PostToWell(SharingActivity.this, title);
		} else {
			facebook.authorize(SharingActivity.this, Util.permission,
					new LoginDialogListener());
		}

	}

	private final class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			SessionStore.save(facebook, SharingActivity.this);
			PostToWell(SharingActivity.this, title);
		}

		public void onFacebookError(FacebookError error) {
			showError(error.getMessage());
		}

		public void onError(DialogError error) {
			showError(error.getMessage());
		}

		public void onCancel() {

		}
	}

	private void PostToWell(Context mContext, String mesg) {
		new postToFriendWall(mContext, mesg).execute();
	}

	private class postToFriendWall extends AsyncTask<Void, Void, Void> {

		private Context mContext = null;
		private String message;
		private String friendId;
		private ProgressDialog mProgressDialog = null;
		private String postId = "";

		public postToFriendWall(Context mContext, String message) {
			this.mContext = mContext;
			this.message = message;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(mContext, "", "Loading...");
			mProgressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Bundle param = new Bundle();
				param.putString("message", message);
				// Uses the Facebook Graph API
				postId = facebook.request("me/feed", param, "POST");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}

			if (postId != null) {
				Util.showToast(SharingActivity.this, "Posted successfully");
			} else {
				Util.showToast(SharingActivity.this, "Posting error");
			}

		}
	}

	/* twitter sharing */
	private void sharingTW(Context mContext, String title) {
		mConsumer = new CommonsHttpOAuthConsumer(
				com.intravel.twitter.Keys.TWITTER_CONSUMER_KEY,
				com.intravel.twitter.Keys.TWITTER_CONSUMER_SECRET);
		if (!mTwitter.isSessionValid()) {
			try {
				mTwitter.authorize(mContext, handlerTwitter,
						Keys.TWITTER_CONSUMER_KEY,
						Keys.TWITTER_CONSUMER_SECRET,
						new DialogListenertwitter());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				new tweetAsync(mContext, title).execute();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private final class DialogListenertwitter implements DialogListenerTwitter {

		@Override
		public void onComplete(Bundle values) {
			handler.sendEmptyMessageDelayed(0, 0);
		}

		@Override
		public void onTwitterError(TwitterError e) {

		}

		@Override
		public void onCancel() {

		}

		@Override
		public void onError(com.intravel.twitter.DialogError e) {

		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			new tweetAsync(SharingActivity.this, title).execute();
		}
	};

	private class tweetAsync extends AsyncTask<Void, Void, Void> {

		private Context mContext = null;
		private String message;
		private boolean response;
		private ProgressDialog mProgressDialog = null;

		public tweetAsync(Context mContext, String message) {
			this.mContext = mContext;
			this.message = message;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(mContext, "", "Loading...");
			mProgressDialog.setCancelable(false);
		}

		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground(Void... params) {

			mConsumer.setTokenWithSecret(mTwitter.getAccessToken(),
					mTwitter.getSecretToken());
			AccessToken mAccessToken = new AccessToken(mConsumer.getToken(),
					mConsumer.getTokenSecret());

			twitter4j.Twitter _twitter = new TwitterFactory().getInstance();
			if (mTwitter.getAccessToken() != null) {
				_twitter.setOAuthConsumer(Keys.TWITTER_CONSUMER_KEY,
						Keys.TWITTER_CONSUMER_SECRET);
				_twitter.setOAuthAccessToken(mAccessToken);
			}
			try {
				if (message.length() > 100) {
					SpannableString span = new SpannableString(message);
					String Title = String.valueOf(span.subSequence(0, 70));

					_twitter.updateStatus(Title + "\n"
							+ new Date().toLocaleString());
				} else {
					_twitter.updateStatus(message + "\n"
							+ new Date().toLocaleString());
				}
				response = true;

			} catch (TwitterException e) {
				response = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
			if (response) {
				Util.showToast(mContext, "Twitted");
			} else {
				Util.showToast(mContext, "Twitt error");
			}
		}
	}

	// Facebook Error Dialog
	public void showError(String Error) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Error).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

	/* Email Sharing */
	private void sendEmail(String msg) {
		if (msg!=null && !msg.equals("")) {
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "!nTravel");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
			SharingActivity.this.startActivity(Intent.createChooser(emailIntent,"Send mail..."));
		}
	}

}
