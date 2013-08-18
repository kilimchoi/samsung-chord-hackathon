package com.samsunghack.apps.apis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.samsunghack.apps.android.noq.ApiKeys;
import com.samsunghack.apps.android.utils.AndroidJSONParser;

public class GooglePlacesIfc {
	
	private static final String TAG="GooglePlacesIfc";
	static GooglePlacesData GooglePlacesData = null;
	static String name;
	static String vicinity;
	static String type;
	static String lattitude;
	static String longitude;
	static String icon;
	static String reference;

	public static GooglePlacesData getPlaces(final Context context, String lat,String lon, String searchTerm, String types) {
		String result = null;
		GooglePlacesData googlePlacesData = new GooglePlacesData();
		
		Log.d(TAG,"GooglePlacesData : + getPlaces");
		// https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=5000&types=food&name=harbour&sensor=true&key=AIzaSyDu4MlzzxTMfun8qXYcCkSvWNZ310gOxnQ
		Log.d(TAG, "Lat = " + lat);
		Log.d(TAG, "Lon = " + lon);
		Log.d(TAG, "Search Data = " + searchTerm);
		StringBuilder URI = new StringBuilder();
		URI.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lon + "&radius=5000&types=food")
			.append("&name=" + searchTerm)
			.append("&sensor=true")
			.append("&key=" + ApiKeys.GOOGLE_ANDROID_API_KEY);
		
		Log.d(TAG,"API Request: " + URI.toString());
		result = AndroidJSONParser.makeAPIRequest(URI.toString());
		
		if (result != null) {
			Log.d(TAG,"GooglePlacesData : getPlaces Data = " + result);
			try {
				JSONObject theData = new JSONObject(result);
				JSONArray array = theData.getJSONArray("results");
				
				int length  = array.length();
				if ((array != null) && (length > 0)) {
					for (int i = 0; i < length; i++) {
						JSONObject object = array.getJSONObject(i);
						if(object.has("name")) {
							name = object.getString("name");
						}
						
						if(object.has("vicinity")) {
							vicinity = object.getString("vicinity");
						}
						
						if(object.has("icon")) {
							icon = object.getString("icon");
						}
						
						if(object.has("reference")) {
							reference = object.getString("reference");
						}
						
						if(object.has("icon")) {
							icon = object.getString("icon");
						}
				
						googlePlacesData.addGooglePlacesData(name, vicinity, type, lattitude,
								longitude, icon, reference);
					}
				}
			} catch(JSONException e) {
				
			}
		}
		Log.d(TAG,"GooglePlacesData : - getPlaces");
		return googlePlacesData;
	}

}
