package com.samsunghack.apps.android.noq;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

public class ReservationsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reservation_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(
					ReservationDetailFragment.ARG_ITEM_ID,
					getIntent().getStringExtra(
							ReservationDetailFragment.ARG_ITEM_ID));
			ReservationDetailFragment fragment = new ReservationDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.reservation_detail_container, fragment).commit();
		}
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch(item.getItemId()) {
	        case android.R.id.home:
	        	this.finish();
	        	break;
	        	
	        default:
	            return super.onOptionsItemSelected(item);
        }
		return false;
    }

}
