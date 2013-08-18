package com.samsunghack.apps.android.noq;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class ReservationFormActivity extends FragmentActivity {
	
    // where we display the selected date and time
    private TextView mDateDisplay;
    private TextView mTimeDisplay;
    private TextView mRestaurantNameV;
    private TextView mRestaurantAddressV;
    private String mRestaurantName;
    private String mRestaurantAddress;

    // date and time
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    
    static final int TIME_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID = 1;
    private Button mMakeReservationButton;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reservation_form);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Extract the Extra Data passed to this activity
		Intent intent = getIntent();
		Bundle indexBundle = intent.getExtras();
		if (indexBundle != null) {
			mRestaurantName = indexBundle.getString(AppConstants.RESTAURANT_NAME);
			mRestaurantAddress = indexBundle.getString(AppConstants.RESTAURANT_ADDRESS);
		}
		
		mRestaurantNameV = (TextView) findViewById(R.id.restaurant_name);
		mRestaurantNameV.setText(mRestaurantName);
		mRestaurantAddressV = (TextView) findViewById(R.id.restaurant_address);
		mRestaurantAddressV.setText(mRestaurantAddress);
		
		mDateDisplay = (TextView) findViewById(R.id.pickDate);
		mTimeDisplay = (TextView) findViewById(R.id.pickTime);
		
		Button pickDate = (Button) findViewById(R.id.pickDate);
		pickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		Button pickTime = (Button) findViewById(R.id.pickTime);
		pickTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        updateDisplay();
        mMakeReservationButton = (Button) findViewById(R.id.submitForm);
		mMakeReservationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Launch confirm Activity
				Intent reservationConfirmIntent = new Intent(ReservationFormActivity.this,
						ReservationConfirmActivity.class);
				
				startActivity(reservationConfirmIntent);
			}
		});
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, mHour, mMinute, false);
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                            mDateSetListener,
                            mYear, mMonth, mDay);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case TIME_DIALOG_ID:
                ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
                break;
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }    

    private void updateDisplay() {
        mDateDisplay.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mMonth + 1).append("-")
                    .append(mDay).append("-")
                    .append(mYear).append(" "));
        
        mTimeDisplay.setText(
                new StringBuilder()
                	.append(pad(mHour)).append(":")
                    .append(pad(mMinute)));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;
                    updateDisplay();
                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
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
