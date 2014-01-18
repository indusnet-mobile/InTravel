package com.intravel.twitter;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intravel.R;
import com.intravel.twitter.Twitter.DialogListenerTwitter;

public class TWDialog extends Dialog {
	public static final String TAG = "twitter";
	private int flag = 0;
	static final int TW_BLUE = 0xFFC0DEED;
	private final float[] DIMENSIONS_LANDSCAPE = new float[2];
	private final float[] DIMENSIONS_PORTRAIT = new float[2];
	static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.FILL_PARENT);
	static final int MARGIN = 4;
	static final int PADDING = 2;
	private int mIcon;
	private String mUrl;
	private DialogListenerTwitter mListener;
	private ProgressDialog mSpinner;
	private WebView mWebView;
	private LinearLayout mContent;
	private TextView mTitle;
	private Handler mHandler;
	private Context context;
	private CommonsHttpOAuthConsumer mConsumer;
	private CommonsHttpOAuthProvider mProvider;

	public TWDialog(Context context, CommonsHttpOAuthProvider provider,
			CommonsHttpOAuthConsumer consumer, DialogListenerTwitter listener,
			int icon) {
		super(context);
		mProvider = provider;
		mConsumer = consumer;
		mListener = listener;
		mIcon = icon;
		mHandler = new Handler();
		this.context = context;
		getWindowSize(context);
	}

	private void getWindowSize(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		DIMENSIONS_LANDSCAPE[0] = metrics.widthPixels - 50;
		DIMENSIONS_LANDSCAPE[1] = metrics.heightPixels - 50;

		DIMENSIONS_PORTRAIT[0] = metrics.widthPixels - 50;
		DIMENSIONS_PORTRAIT[1] = metrics.heightPixels - 50;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSpinner = new ProgressDialog(getContext());
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage("Loading...");
		mSpinner.setCancelable(true);
		// mSpinner.show();
		mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.VERTICAL);
		setUpTitle();
		setUpWebView();

		Display display = getWindow().getWindowManager().getDefaultDisplay();
		final float scale = getContext().getResources().getDisplayMetrics().density;
		float[] dimensions = display.getWidth() < display.getHeight() ? DIMENSIONS_PORTRAIT
				: DIMENSIONS_LANDSCAPE;
		addContentView(mContent, new FrameLayout.LayoutParams(
				(int) (dimensions[0] * scale + 0.5f), (int) (dimensions[1]
						* scale + 0.5f)));

		retrieveRequestToken();
	}

	private void setUpTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Drawable icon = getContext().getResources().getDrawable(mIcon);
		mTitle = new TextView(getContext());
		mTitle.setText("Twitter");
		mTitle.setTextColor(Color.WHITE);
		mTitle.setTypeface(Typeface.DEFAULT_BOLD);
		mTitle.setBackgroundColor(TW_BLUE);
		mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
		mTitle.setCompoundDrawablePadding(MARGIN + PADDING);
		mTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		mContent.addView(mTitle);
	}

	private void retrieveRequestToken() {
		// mSpinner.show();
		new Thread() {
			@Override
			public void run() {
				try {
					mUrl = mProvider.retrieveRequestToken(mConsumer,
							Twitter.CALLBACK_URI);
					mWebView.loadUrl(mUrl);
				} catch (OAuthMessageSignerException e) {
					TWDialog.this.dismiss();
					mListener.onError(new DialogError(e.getMessage(), -1,
							Twitter.OAUTH_REQUEST_TOKEN));
				} catch (OAuthNotAuthorizedException e) {
					TWDialog.this.dismiss();
					mListener.onError(new DialogError(e.getMessage(), -1,
							Twitter.OAUTH_REQUEST_TOKEN));
				} catch (OAuthExpectationFailedException e) {
					TWDialog.this.dismiss();
					mListener.onError(new DialogError(e.getMessage(), -1,
							Twitter.OAUTH_REQUEST_TOKEN));
				} catch (OAuthCommunicationException e) {
					TWDialog.this.dismiss();
					mListener.onError(new DialogError(e.getMessage(), -1,
							Twitter.OAUTH_REQUEST_TOKEN));
				}
			}
		}.start();
	}

	private void retrieveAccessToken(final String url) {
		// mSpinner.show();
		new Thread() {
			@Override
			public void run() {
				Uri uri = Uri.parse(url);
				String verifier = uri
						.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
				final Bundle values = new Bundle();
				try {
					mProvider.retrieveAccessToken(mConsumer, verifier);
					values.putString(Twitter.ACCESS_TOKEN, mConsumer.getToken());
					values.putString(Twitter.SECRET_TOKEN,
							mConsumer.getTokenSecret());
					mListener.onComplete(values);
				} catch (OAuthMessageSignerException e) {
					TWDialog.this.dismiss();
					mListener.onError(new DialogError(e.getMessage(), -1,
							verifier));
				} catch (OAuthNotAuthorizedException e) {
					TWDialog.this.dismiss();
					mListener.onTwitterError(new TwitterError(e.getMessage()));
				} catch (OAuthExpectationFailedException e) {
					TWDialog.this.dismiss();
					mListener.onTwitterError(new TwitterError(e.getMessage()));
				} catch (OAuthCommunicationException e) {
					TWDialog.this.dismiss();
					mListener.onError(new DialogError(e.getMessage(), -1,
							verifier));
				}
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// mSpinner.dismiss();
						TWDialog.this.dismiss();
					}
				});
			}
		}.start();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setUpWebView() {
		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.clearCache(true);
		mWebView.clearFormData();
		mWebView.clearHistory();
		mWebView.setWebViewClient(new TWDialog.TwWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setLayoutParams(FILL);
		mContent.addView(mWebView);
	}

	private class TwWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "Redirect URL: " + url);
			if (url.startsWith(Twitter.CALLBACK_URI)) {
				retrieveAccessToken(url);
				return true;
			} else if (url.startsWith(Twitter.CANCEL_URI)) {
				mListener.onCancel();
				TWDialog.this.dismiss();
				return true;
			}
			// launch non-dialog URLs in a full browser
			getContext().startActivity(
					new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			mSpinner.dismiss();
			flag = 1;
			mListener.onError(new DialogError(description, errorCode,
					failingUrl));
			TWDialog.this.dismiss();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(TAG, "WebView loading URL: " + url);
			super.onPageStarted(view, url, favicon);
			if (mSpinner.isShowing()) {
				mSpinner.dismiss();
				flag = 1;
			}
			if (url.startsWith(Twitter.CALLBACK_URI)) {
				retrieveAccessToken(url);
			} else
				mSpinner.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			String title = mWebView.getTitle();
			if (title != null && title.length() > 0) {
				mTitle.setText(title);
			}
			mSpinner.dismiss();
			flag = 1;
		}

	}

	@Override
	public void onBackPressed() {
		if (flag == 1) {
			super.onBackPressed();
		}

	}
}
