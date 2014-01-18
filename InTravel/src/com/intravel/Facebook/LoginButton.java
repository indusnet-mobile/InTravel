package com.intravel.Facebook;
//
//package com.facebook.android;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import org.json.JSONException;
//import org.json.JSONObject;
//import com.android.test.AppData;
//import com.android.test.FbtestActivity;
//import com.facebook.android.Facebook.DialogListener;
//import com.facebook.android.SessionEvents.AuthListener;
//import com.facebook.android.SessionEvents.LogoutListener;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageButton;
//
//public class LoginButton extends ImageButton {
//    
//    private Facebook mFb;
//    private Handler mHandler;
//    private SessionListener mSessionListener = new SessionListener();
//    private String[] mPermissions;
//    private Activity mActivity;
//    
//    AsyncFacebookRunner mAsyncRunner;
//    
//    public LoginButton(Context context) {
//        super(context);
//    }
//    
//    public LoginButton(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//    
//    public LoginButton(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//    
//    public void init(final Activity activity, final Facebook fb,AsyncFacebookRunner mARunner) {
//    	init(activity, fb, new String[] {},mARunner);
//    }
//    
//    public void init(final Activity activity, final Facebook fb,
//                     final String[] permissions,AsyncFacebookRunner mARunner) {
//        mActivity = activity;
//        mFb = fb;
//        mPermissions = permissions;
//        mHandler = new Handler();
//        mAsyncRunner=mARunner;
//        
//        setBackgroundColor(Color.TRANSPARENT);
//        setAdjustViewBounds(true);
////        setImageResource(fb.isSessionValid() ?
////        		R.drawable.tapbutton: 
////        			R.drawable.tapbutton);
//        drawableStateChanged();
//        
//        SessionEvents.addAuthListener(mSessionListener);
//        SessionEvents.addLogoutListener(mSessionListener);
//        setOnClickListener(new ButtonOnClickListener());
//    }
//    
//    private final class ButtonOnClickListener implements OnClickListener {
//        
//        public void onClick(View arg0) {
//            if (mFb.isSessionValid()) {
//            	mFb.dialog(mActivity, "feed",
//                         new SampleDialogListener());
//            } else {
//                mFb.authorize(mActivity, mPermissions,
//                              new LoginDialogListener());
//            }
//        }
//    }
//
//    private final class LoginDialogListener implements DialogListener {
//        public void onComplete(Bundle values) {
//            SessionEvents.onLoginSuccess();
//            
//            mFb.dialog(mActivity, "feed",
//                    new SampleDialogListener());
//        }
//
//        public void onFacebookError(FacebookError error) {
//            SessionEvents.onLoginError(error.getMessage());
//        }
//        
//        public void onError(DialogError error) {
//            SessionEvents.onLoginError(error.getMessage());
//        }
//
//        public void onCancel() {
//            SessionEvents.onLoginError("Action Canceled");
//        }
//    }
//    
//    private class LogoutRequestListener extends BaseRequestListener {
//        public void onComplete(String response, final Object state) {
//            // callback should be run in the original thread, 
//            // not the background thread
//            mHandler.post(new Runnable() {
//                public void run() {
//                    SessionEvents.onLogoutFinish();
//                }
//            });
//        }
//    }
//    
//    private class SessionListener implements AuthListener, LogoutListener {
//        
//        public void onAuthSucceed() {
////            setImageResource(R.drawable.tapbutton);
//            SessionStore.save(mFb, getContext());
//        }
//
//        public void onAuthFail(String error) {
//        }
//        
//        public void onLogoutBegin() {           
//        }
//        
//        public void onLogoutFinish() {
//            SessionStore.clear(getContext());
//        }
//    }
//    public class SampleDialogListener extends BaseDialogListener {
//
//        public void onComplete(Bundle values) {
//        	
//        	String accessToken = (AppData.mFacebook).getAccessToken();
//            final String postId = values.getString("post_id");
//            if (postId != null) {
//                Log.d("Facebook-Example", "Dialog Success! post_id=" + postId);
//                mAsyncRunner.logout(getContext(), new LogoutRequestListener());
//            } else {
//                Log.d("Facebook-Example", "No wall post made");
//            }
//        }
//    }
//    
////    public class WallPostRequestListener extends BaseRequestListener {
////
////        public void onComplete(final String response, final Object state) {
////            Log.d("Facebook-Example", "Got response: " + response);
////            @SuppressWarnings("unused")
////			String message = "<empty>";
////            try {
////                JSONObject json = Util.parseJson(response);
////                message = json.getString("message");
////            } catch (JSONException e) {
////                Log.w("Facebook-Example", "JSON Error in response");
////            } catch (FacebookError e) {
////                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
////            }
////            
////            SessionEvents.onLoginSuccess();
////
////        }
////    }
//	@SuppressWarnings("unused")
//	private String getFeed() throws JSONException {
//
//		try {
//			getMeFeed(mFb.getAccessToken());
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return "";
//	}
//	private void getMeFeed(String accessToken) {
//
//
//		String response = "";
//		try {
//			Bundle bundle = new Bundle();
//			bundle.putString(Facebook.TOKEN, accessToken);
//			response = mFb.request("me/feed", bundle, "GET");
//			Log.d("UPDATE RESPONSE", "" + response);
//		} catch (MalformedURLException e) {
//			Log.e("MALFORMED URL", "" + e.getMessage());
//		} catch (IOException e) {
//			Log.e("IOEX", "" + e.getMessage());
//		}
//
//		 Bundle params = new Bundle();
//		 params.putString("fields", "name, picture,presence");
//		 try {
//			response=   mFb.request("me/friends",params);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//			Log.d("UPDATE RESPONSE", "" + response);  
//		    
//	}
//
//	public void init(FbtestActivity activity, Facebook mFacebook,
//			AsyncFacebookRunner mAsyncRunner2) {
//		init(activity, mFacebook, new String[] {},mAsyncRunner2);
//	
//		
//	}
//
//
//  
//}
