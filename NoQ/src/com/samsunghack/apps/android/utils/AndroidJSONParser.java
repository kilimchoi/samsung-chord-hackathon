package com.samsunghack.apps.android.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class AndroidJSONParser {
	private static final String TAG = "AndroidJSONParser";
	public static String makeAPIRequest (String httpurl) {
		Log.v(TAG ,"AndroidJSONParser :+makeAPIRequest");
		String result = null;
		// Make HTTP Request
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(httpurl);
		
		HttpResponse response;
		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if(entity!=null) {
				InputStream inputstream = entity.getContent();
				
				// Create a string from the input Stream
				BufferedReader bufferedreader = new BufferedReader( new InputStreamReader(inputstream));
				
				StringBuilder stringbuilder = new StringBuilder();
				
				String currentline = null;
				
				try {
					while ((currentline = bufferedreader.readLine()) != null) {
						stringbuilder.append(currentline + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Convert the result into string - JSON Data String
				result = stringbuilder.toString();
				// LOGD(TAG, "AndroidJsonParser: result" + result);

				inputstream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.v(TAG ,"AndroidJSONParser :-makeAPIRequest");
		return result;
	}
}
