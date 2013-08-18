package com.samsunghack.apps.android.noq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ReservationConfirmActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reservation_confirm);
		
		// Linkify the links in the TextView - same thing can be done by using android:autoLink attribute under TextView.
		TextView textView = (TextView) findViewById(R.id.reservation_confirmation);
		Linkify.addLinks(textView,Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES );
		
//        Button button = (Button) findViewById(R.id.restaurant_menu);
//        button.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// Launch confirm Activity
//				Intent reservationConfirmIntent = new Intent(ReservationConfirmActivity.this,
//						RestaurantMenuActivity.class);
//				
//				startActivity(reservationConfirmIntent);
//				
//				finish();
//			}
//		});
        
	}
}
