package com.samsunghack.apps.android.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.GooglePlusUtil;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusOneButton;

public class GPlusUtils implements ConnectionCallbacks, OnConnectionFailedListener  {
	private final static String TAG="GPlusUtils";
	PlusClient mPlusClient = null;
	PlusOneButton mPlusOneButton;
	Uri mAppUri = null;
	
    // The request code must be 0 or higher.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    
	public PlusClient setupGooglePlusButton(Context context, PlusOneButton plusOneButton,Uri appUri) {
		if((plusOneButton!=null) && (context!=null)) {
			int plusAppStatus = GooglePlusUtil.checkGooglePlusApp(context);
			mPlusOneButton = plusOneButton;
			mAppUri =  appUri;
			switch (plusAppStatus) {
				case GooglePlusUtil.SUCCESS: 
					Log.d(TAG,"Google+ Status: SUCCESS");
					mPlusClient = new PlusClient.Builder(context, this,this)
										.clearScopes()
										.build();									
					break;
					
				case GooglePlusUtil.APP_MISSING:
					Log.d(TAG,"Google+ Status: APP_MISSING");
					plusOneButton.setVisibility(View.GONE);
					break;
					
				case GooglePlusUtil.APP_UPDATE_REQUIRED:
					Log.d(TAG,"Google+ Status: APP_UPDATE_REQUIRED");
					plusOneButton.setVisibility(View.GONE);
					break;
				
				case GooglePlusUtil.APP_DISABLED:
					Log.d(TAG,"Google+ Status: APP_DISABLED");
					plusOneButton.setVisibility(View.GONE);
					break;
				default: 
					plusOneButton.setVisibility(View.GONE);
					break;
			}
		}
		return mPlusClient;
	}
	
	public void connectPlusClient() {
        if(mPlusClient!=null) mPlusClient.connect();
	}
	
	public void disconnectPlusClient() {
        if(mPlusClient!=null) mPlusClient.disconnect();
	}
	
	public void initPlusOneButton() {
		if(mPlusOneButton!=null && (mPlusClient!=null)) {
			mPlusOneButton.initialize(mPlusClient, mAppUri.toString(), PLUS_ONE_REQUEST_CODE);
		}
	}
	public PlusClient getPlusClient() {
		return mPlusClient;
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.d(TAG,"Google Plus connection failed");
		
	}

	@Override
	public void onDisconnected() {
		Log.d(TAG,"Google Plus disconnected");
	}

	@Override
	public void onConnected(Bundle arg0) {
		Log.d(TAG,"Google Plus connection succeeded");
	}
}