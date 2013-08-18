package com.samsunghack.apps.android.noq;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.immersion.uhl.Launcher;
import com.samsunghack.apps.android.utils.ImageDownloader;
import com.samsunghack.apps.apis.GooglePlacesData;
import com.samsunghack.apps.apis.GooglePlacesData.GooglePlaces;
import com.samsunghack.apps.apis.GooglePlacesIfc;

public class NearbyFragment extends Fragment {
	private static final String TAG="NearbyFragment";
	private ViewGroup mRootView;
	private Cursor mCursor;
	private ListView mRestaurantsListV;
	private TextView mNoDataV;
	 private Launcher m_launcher;        // to use built-in effects
	
	GooglePlacesData mGooglePlacesData;
	ArrayList<GooglePlaces> mGooglePlacesList;
	private GooglePlacesAdapter mGooglePlacesAdapter;
	private EditText mSearchField;
	private Button mSearchButton;
	private String mSearchData;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_nearby,
				null);

		mRestaurantsListV = (ListView) mRootView.findViewById(R.id.list_items);
		
//		mListView.setTextFilterEnabled(true);
//		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//		mListView.setCacheColorHint(Color.TRANSPARENT);
//		mListView.setOnItemClickListener(clickListener);

		mNoDataV = (TextView) mRootView.findViewById(R.id.empty_log_warning);
		mGooglePlacesAdapter = new GooglePlacesAdapter(getActivity(), null);
		mRestaurantsListV.setAdapter(mGooglePlacesAdapter);
		
		mRestaurantsListV.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				// Launch Reservations activity
				GooglePlaces restaurantPlace = mGooglePlacesList.get(position);
				Intent intent = new Intent(getActivity(),ReservationFormActivity.class);
				intent.putExtra(AppConstants.RESTAURANT_NAME, restaurantPlace.getName());
				intent.putExtra(AppConstants.RESTAURANT_ADDRESS, restaurantPlace.getVicinity());
				startActivity(intent);
			}
		});
		
		// initialize UHL vibration object
        try { m_launcher = new Launcher(getActivity()); }
        catch (RuntimeException re) {}

		mSearchField = (EditText) mRootView.findViewById(R.id.search_field);
		mSearchButton = (Button) mRootView.findViewById(R.id.search_button);
		mSearchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSearchData = mSearchField.getText().toString();
				String[] params = new String[3];;
				params[0] = "37.37677240"; // Lat
				params[1] = "-121.92164020"; // Long
				params[2] = mSearchData;
				m_launcher.play(Launcher.LONG_BUZZ_66);
				new FindNearbyRestaurantsTask().execute(params);
			}
		});
		

		return mRootView;
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		String[] params = new String[3];
		params[0] = "37.37677240";
		params[1] = "-121.92164020";
		params[2] = "pizza";
		
		// Find Nearby Restaurants
		new FindNearbyRestaurantsTask().execute(params);
	}

	private OnItemClickListener clickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,
				long arg3) {
			mCursor.moveToPosition(index);
			Intent intent = new Intent(getActivity(),
					ReservationFormActivity.class);
			// File file = new
			// File(mCursor.getString(mCursor.getColumnIndex(PATH)));
			// intent.putExtra(AppConstants.FILE_NAME,file.getName());
			startActivity(intent);
		}
	};

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	/** Called to create menus */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		// MenuInflater Inflater = getActivity().getMenuInflater();
		// Inflater.inflate(R.menu.favorite_menu, menu);
	}

	/** Called when the menu item is selected */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			default:
				break;
		}

		return false;
	}

	class GooglePlacesAdapter extends ArrayAdapter<GooglePlaces> {
		private final LayoutInflater mLayoutInflater;
		private final ImageDownloader imageDownloader = new ImageDownloader();

		GooglePlacesAdapter(Context context,ArrayList<GooglePlaces> GooglePlacesList) {
			super(context, 0);
			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(R.layout.list_item_nearby_restaurants, parent, false);

				holder = new ViewHolder();

				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.vicinity = (TextView) convertView.findViewById(R.id.vicinity);
				holder.imageView = (ImageView) convertView.findViewById(R.id.image);
				

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final GooglePlaces googlePlaces = getItem(position);
			imageDownloader.download(googlePlaces.getIcon(), (ImageView) holder.imageView);
			holder.name.setText(googlePlaces.getName());
			holder.vicinity.setText(googlePlaces.getVicinity());
			return convertView;
		}
	}

	class ViewHolder {
		ImageView imageView;
		TextView name;
		TextView vicinity;
		
	}
	
	private class FindNearbyRestaurantsTask extends AsyncTask<String, String, GooglePlacesData> {

		protected GooglePlacesData doInBackground(String... params) {
			// Turn the indefinite activity indicator on
			// FIXME - Progress Bar is causing some issues
//			if(mProgressBar!=null) {
//				mProgressBar.setVisibility(View.VISIBLE);
//			}
			return GooglePlacesIfc.getPlaces(getActivity(),params[0],params[1],params[2],"food");
		}

		protected void onProgressUpdate(String... progress) {

		}

		protected void onPostExecute(GooglePlacesData googlePlacesData) {
			if(googlePlacesData!=null) {
				displayGooglePlaces(googlePlacesData);
			}
		}

		protected void onPreExecute() {

		}
	}
	
	private void displayGooglePlaces(GooglePlacesData googlePlacesData) {
		mGooglePlacesData = googlePlacesData;
		if (mGooglePlacesData != null) {
			mGooglePlacesList = mGooglePlacesData.getFeatuersData();
			if (mGooglePlacesList != null && mGooglePlacesList.isEmpty() == false) {
				if (mGooglePlacesAdapter == null) {
					mGooglePlacesAdapter = new GooglePlacesAdapter(getActivity(),mGooglePlacesList);
				}
				mGooglePlacesAdapter.clear();
				Iterator<GooglePlaces> it = mGooglePlacesList.iterator();
				
				while(it.hasNext()){
					GooglePlaces GooglePlaces = it.next();
					mGooglePlacesAdapter.add(GooglePlaces);
				}
				if (mGooglePlacesAdapter.isEmpty()) {
					// Toast.makeText(this,R.string.fatal_error, Toast.LENGTH_SHORT).show();
				} else {
					mRestaurantsListV.setAdapter(mGooglePlacesAdapter);
				}
			}
			else{
				Toast.makeText(getActivity(), "No GooglePlaces available.", Toast.LENGTH_SHORT).show();
			}
		} else {
			Log.e(TAG,"displayGooglePlaces: mGooglePlacesData = null");
		}
	}

}
