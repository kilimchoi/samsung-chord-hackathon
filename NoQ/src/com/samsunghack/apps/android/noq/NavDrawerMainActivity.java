/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samsunghack.apps.android.noq;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class NavDrawerMainActivity extends Activity {
	private static final String TAG = "NavDrawerMainActivity";
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	// Navigation Menu items
	private static final int NAV_MENU_HOME=0;
	private static final int NAV_MENU_RESERVATIONS=1;
	private static final int NAV_MENU_NEARBY=2;
	private static final int NAV_MENU_PAYMENTS=3;
	private static final int NAV_MENU_SETTINGS=4;
	private static final int NAV_MENU_LENGTH=5;

	private static String[] mAppFeatureTitles = new String[NAV_MENU_LENGTH];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navdrawer_main);

		mTitle = mDrawerTitle = getTitle();
		setupNavigationMenu();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mAppFeatureTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItemNew(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_websearch:
			// create intent to perform web search for this planet
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available,
						Toast.LENGTH_LONG).show();
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItemNew(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new PlanetFragment();
		Log.d(TAG, "+selectItem: positon = " + position);
		Bundle args = new Bundle();
		args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mAppFeatureTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	private void selectItemNew(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new PlanetFragment();
		Log.d(TAG, "+selectItem: positon = " + position);
		Bundle args = new Bundle();
		args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		switch(position) {
		case NAV_MENU_HOME:
			Log.d(TAG,"Home Menu Pressed");
			break;
		case NAV_MENU_RESERVATIONS:
			Log.d(TAG,"Reservations Menu Pressed");
			Intent reservationsIntent = new Intent(NavDrawerMainActivity.this,
					ReservationFormActivity.class);
			startActivity(reservationsIntent);
			break;
			
		case NAV_MENU_NEARBY:
			Intent nearbyIntent = new Intent(NavDrawerMainActivity.this, NearbyActivity.class);
			startActivity(nearbyIntent);
			Log.d(TAG,"Nearby Menu Pressed");
			break;
		
		case NAV_MENU_PAYMENTS:
			Intent paymentsIntent = new Intent(NavDrawerMainActivity.this, PaymentsActivity.class);
			startActivity(paymentsIntent);
			Log.d(TAG,"Nearby Menu Pressed");
			break;
		
		case NAV_MENU_SETTINGS:
			Intent chordUpload = new Intent(NavDrawerMainActivity.this, ChordFileUploaderActivity.class);
			startActivity(chordUpload);
			Log.d(TAG,"Settings Menu Pressed");
			break;
			
		}
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mAppFeatureTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}
	

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	public static class PlanetFragment extends Fragment {
		public static final String ARG_PLANET_NUMBER = "planet_number";

		public PlanetFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_planet,
					container, false);
			int i = getArguments().getInt(ARG_PLANET_NUMBER);
			String planet = mAppFeatureTitles[i];

			int imageId = getResources().getIdentifier(
					planet.toLowerCase(Locale.getDefault()), "drawable",
					getActivity().getPackageName());
			((ImageView) rootView.findViewById(R.id.image))
					.setImageResource(imageId);
			getActivity().setTitle(planet);
			return rootView;
		}
	}

	// Sliding Menu items
	class NavMenuItem {

		int mItemId;
		int mStringResourceId;
		int mIconResourceId;

		public NavMenuItem(int itemId, int stringResourceId, int iconResourceId) {
			this.mItemId = itemId;
			this.mStringResourceId = stringResourceId;
			this.mIconResourceId = iconResourceId;
			if(itemId < NAV_MENU_LENGTH) {
				mAppFeatureTitles[itemId] = getResources().getString(stringResourceId);
			}
		}

		public int getItemId() {
			return mItemId;
		}

		public int getStringResourceId() {
			return mStringResourceId;
		}

		public int getIconResourceId() {
			return mIconResourceId;
		}
	}
	
	private void setupNavigationMenu() {
		ArrayList<NavMenuItem> navMenuItemsList = new ArrayList<NavMenuItem>();
		navMenuItemsList.add(new NavMenuItem(NAV_MENU_HOME, R.string.title_home, R.drawable.ic_launcher));
		navMenuItemsList.add(new NavMenuItem(NAV_MENU_RESERVATIONS, R.string.title_reservations,R.drawable.ic_launcher));
		navMenuItemsList.add(new NavMenuItem(NAV_MENU_NEARBY, R.string.title_nearby_restaurants,R.drawable.ic_launcher));
		navMenuItemsList.add(new NavMenuItem(NAV_MENU_PAYMENTS, R.string.title_payments,R.drawable.ic_launcher));
		navMenuItemsList.add(new NavMenuItem(NAV_MENU_SETTINGS, R.string.title_settings,R.drawable.ic_launcher));
	}
}