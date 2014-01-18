package com.intravel.twitter;

import java.io.IOException;
import java.net.MalformedURLException;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.CookieSyncManager;





public class Twitter {
	public static final String TAG = "twitter";
	public static final String CALLBACK_URI = "bloa-app://twitt";
	public static final String CANCEL_URI = "twitter://cancel";
	public static final String ACCESS_TOKEN = "oauth_token";
	public static final String SECRET_TOKEN = "oauth_token_secret";

	public static final String REQUEST = "request";
	public static final String AUTHORIZE = "authorize";

	protected static String REQUEST_ENDPOINT = "https://api.twitter.com/1";
	
	protected static String OAUTH_REQUEST_TOKEN = "https://api.twitter.com/oauth/request_token";
	protected static String OAUTH_ACCESS_TOKEN = "https://api.twitter.com/oauth/access_token";
	protected static String OAUTH_AUTHORIZE = "https://api.twitter.com/oauth/authorize";
	
//	private String mAccessToken = null;
//	private String mSecretToken = null;
	
	private int mIcon;
	private CommonsHttpOAuthConsumer mHttpOauthConsumer;
	private CommonsHttpOAuthProvider mHttpOauthProvider;
	private Context context;
	public Twitter(int icon, Context context) {
		mIcon = icon;
		this.context = context;
	}
	
	public void authorize(Context ctx,
			Handler handler,
			String consumerKey,
			String consumerSecret,
			final DialogListenerTwitter listener) {
		mHttpOauthConsumer = new CommonsHttpOAuthConsumer(
				consumerKey, consumerSecret);
		mHttpOauthProvider = new CommonsHttpOAuthProvider(
				OAUTH_REQUEST_TOKEN, OAUTH_ACCESS_TOKEN, OAUTH_AUTHORIZE);
		CookieSyncManager.createInstance(ctx);
		dialog(ctx, handler, new DialogListenerTwitter() {

			@Override
			public void onComplete(Bundle values) {
				CookieSyncManager.getInstance().sync();
				setAccessToken(values.getString(ACCESS_TOKEN));
				setSecretToken(values.getString(SECRET_TOKEN));
				if (isSessionValid()) {
					Log.d(TAG, "token "+getAccessToken()+" "+getSecretToken());
					listener.onComplete(values);
				} else {
					onTwitterError(new TwitterError("failed to receive oauth token"));
				}
			}

			@Override
			public void onTwitterError(TwitterError e) {
				Log.d(TAG, "Login failed: "+e);
				listener.onTwitterError(e);
			}

			@Override
			public void onError(DialogError e) {
				Log.d(TAG, "Login failed: "+e);
				listener.onError(e);
			}

			@Override
			public void onCancel() {
				Log.d(TAG, "Login cancelled");
				listener.onCancel();
			}
			
		});
	}
	
	
	
	
	public String logout(Context context) throws MalformedURLException, IOException {
		return "true";
	}
	
	

	
	public void dialog(final Context ctx,
			Handler handler,
			final DialogListenerTwitter listener) {
		if (ctx.checkCallingOrSelfPermission(Manifest.permission.INTERNET) !=
			PackageManager.PERMISSION_GRANTED) {
			Util.showAlert(ctx, "Error", "Application requires permission to access the Internet");
			return;
		}
		new TWDialog(ctx, mHttpOauthProvider, mHttpOauthConsumer,
				listener, mIcon).show();
	}
	
	public boolean isSessionValid() {
		return getAccessToken() != null && getSecretToken() != null;
	}
	
	public String getAccessToken() {
		SharePrefForTwitter forTwitter = new SharePrefForTwitter(context);
		return forTwitter.getAccessToken();
	}

	public void setAccessToken(String accessToken) {
		SharePrefForTwitter forTwitter = new SharePrefForTwitter(context);
		forTwitter.setAccessToken(accessToken);
//		mAccessToken = accessToken;
	}

	public String getSecretToken() {
		SharePrefForTwitter forTwitter = new SharePrefForTwitter(context);
		return forTwitter.getSecretToken();
	}

	public void setSecretToken(String secretToken) {
		SharePrefForTwitter forTwitter = new SharePrefForTwitter(context);
		forTwitter.setSecretToken(secretToken);
//		mSecretToken = secretToken;
	}
	
	public static interface DialogListenerTwitter {
		public void onComplete(Bundle values);
		public void onTwitterError(TwitterError e);
		public void onError(DialogError e);
		public void onCancel();
	}

}
