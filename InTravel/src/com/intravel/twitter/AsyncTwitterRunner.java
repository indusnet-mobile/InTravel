

package com.intravel.twitter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.LinkedList;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;




public class AsyncTwitterRunner {

	Twitter tw;
	Context mContext;
	OAuthConsumer mConsumer;
	PostingListenerTwitter mListener;
	
	
	
    public AsyncTwitterRunner(Twitter tw) {
    	this.tw = tw;
    }

  
    public void logout(final Context context, final RequestListener listener) {
        new Thread() {
            @Override public void run() {
                try {
                    String response = tw.logout(context);
                    if (response.length() == 0 || response.equals("false")){
                        listener.onTwitterError(new TwitterError(
                                "auth.expireSession failed"));
                        return;
                    }
                    listener.onComplete(response);
                } catch (FileNotFoundException e) {
                    listener.onFileNotFoundException(e);
                } catch (MalformedURLException e) {
                    listener.onMalformedURLException(e);
                } catch (IOException e) {
                    listener.onIOException(e);
                }
            }
        }.start();
    }
    
    
   
    
    
    public void postStatus(Context context,OAuthConsumer consumer,String status,PostingListenerTwitter listener){
    	mContext=context;
    	mConsumer=consumer;
    	mListener=listener;
    	new PostTask().execute(status);
    }
    
    

    

	//----------------------------
	// This task posts a message to your message queue on the service.
	private class PostTask extends AsyncTask<String, Void, JSONObject> {
 
		ProgressDialog postDialog;
 
		@Override
		protected void onPreExecute() {
			postDialog = ProgressDialog.show(mContext, "",
					"Posting Comments...", true); // not cancel-able
		}
 
		@Override
		protected JSONObject doInBackground(String... params) {
	        //twitter implementation
	        HttpParams parameters = new BasicHttpParams();
			HttpProtocolParams.setVersion(parameters, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(parameters, HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(parameters, false);
			HttpConnectionParams.setTcpNoDelay(parameters, true);
			HttpConnectionParams.setSocketBufferSize(parameters, 8192);
			
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			ClientConnectionManager tsccm = new ThreadSafeClientConnManager(parameters, schReg);
			HttpClient mClient = new DefaultHttpClient(tsccm, parameters);
			
			JSONObject jso = null;
			try {
				HttpPost post = new HttpPost("http://twitter.com/statuses/update.json");
				LinkedList<BasicNameValuePair> out = new LinkedList<BasicNameValuePair>();
				out.add(new BasicNameValuePair("status", params[0]));
				post.setEntity(new UrlEncodedFormEntity(out, HTTP.UTF_8));
				post.setParams(getParams());
				// sign the request to authenticate
				mConsumer.sign(post);
				String response = mClient.execute(post, new BasicResponseHandler());
				jso = new JSONObject(response);
				//mListener.onComplete();
			} catch (UnsupportedEncodingException e) {
				//mListener.onError();
			} catch (OAuthMessageSignerException e) {
				//mListener.onError();
			} catch (OAuthExpectationFailedException e) {
				//mListener.onError();
			} catch (OAuthCommunicationException e) {
				//mListener.onError();
			} catch (ClientProtocolException e) {
				//mListener.onError();
			} catch (IOException e) {
				//mListener.onError();
			} catch (JSONException e) {
				//mListener.onError();
			} finally {
				;
			}
			return jso;
		}
 
		// This is in the UI thread, so we can mess with the UI
		protected void onPostExecute(JSONObject jso) {
			postDialog.dismiss();
			if(jso != null) { // authorization succeeded, the json object contains the user information
				mListener.onComplete();
			} else {
				mListener.onError();
			}
		}
	}
    
    
	// These parameters are needed to talk to the messaging service
	public HttpParams getParams() {
		// Tweak further as needed for your app
		HttpParams params = new BasicHttpParams();
		// set this to false, or else you'll get an Expectation Failed: error
		HttpProtocolParams.setUseExpectContinue(params, false);
		return params;
	}
	
	
//mine added......
	
	public static interface PostingListenerTwitter {
		public void onComplete();
		public void onError();
	}
	
	
	
    public static interface RequestListener {
        public void onComplete(String response);
        public void onIOException(IOException e);
        public void onFileNotFoundException(FileNotFoundException e);
        public void onMalformedURLException(MalformedURLException e);
        public void onTwitterError(TwitterError e);
        
    }
    
}