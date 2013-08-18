package com.samsunghack.apps.android.noq;

import com.samsung.chord.ChordManager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ChordFileUploaderActivity extends FragmentActivity {
    
	private Button mstartButton;
    private Button mJoin_leave_btn;
    private Button mPublic_channel_send_btn;
    private ListView mPublicChannelListView;
    //FIXME - private ChordFileUploaderService mChordFileUploaderActivity;
    private int mInterfaceType;
    
    private boolean bStartedChord = false;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chord_fileuploader);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mstartButton = (Button) findViewById(R.id.button1);
		mstartButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startChord();
			}
		});
	}
	
	public void startChord() {
        // FIXME int nError = mChordFileUploaderActivity.start(mInterfaceType);
//        if (ChordManager.ERROR_NONE == nError) {
//            mstartButton.setText(R.string.stop_chord);
//            mJoin_leave_btn.setEnabled(true);
//            mJoin_leave_btn.setText(R.string.join_channel);
//            mPublicChannelListView.setEnabled(true);
//            bStartedChord = true;
//            mPublic_channel_send_btn.setEnabled(true);
//
//        }
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
