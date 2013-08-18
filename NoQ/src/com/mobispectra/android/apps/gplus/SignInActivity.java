package com.mobispectra.android.apps.gplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;
import com.mobispectra.android.apps.gplus.PlusClientFragment.OnSignedInListener;
import com.samsunghack.apps.android.noq.AppPrefs;
import com.samsunghack.apps.android.noq.NavDrawerMainActivity;
import com.samsunghack.apps.android.noq.R;

public class SignInActivity extends FragmentActivity
        implements View.OnClickListener, OnSignedInListener {

    public static final int REQUEST_CODE_PLUS_CLIENT_FRAGMENT = 0;

    private TextView mSignInStatus;
    private PlusClientFragment mSignInFragment;
    private SignInButton gPlusSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gplus_signin);

        mSignInFragment =
                PlusClientFragment.getPlusClientFragment(this, MomentUtil.VISIBLE_ACTIVITIES);

        gPlusSignInButton =  (SignInButton) findViewById(R.id.sign_in_button);
        gPlusSignInButton.setSize(SignInButton.SIZE_WIDE);
        gPlusSignInButton.setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.revoke_access_button).setOnClickListener(this);
        mSignInStatus = (TextView) findViewById(R.id.sign_in_status);
        
		
		// Initialize Shared Preferences
		AppPrefs.init(getApplicationContext());
		
        if(AppPrefs.getUserAccountName(this)!=null) {
        	// Launch the Dashboard
			Intent reservationsIntent = new Intent(SignInActivity.this, NavDrawerMainActivity.class);
			startActivity(reservationsIntent);
			finish();
        }
        // Setup the Action bar
        // setupActionBar(getString(R.string.gplus_signin));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.sign_out_button:
                resetAccountState();
                mSignInFragment.signOut();
                break;
            case R.id.sign_in_button:
                mSignInFragment.signIn(REQUEST_CODE_PLUS_CLIENT_FRAGMENT);
                break;
            case R.id.revoke_access_button:
                resetAccountState();
                mSignInFragment.revokeAccessAndDisconnect();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        mSignInFragment.handleOnActivityResult(requestCode, responseCode, intent);
    }

    @Override
    public void onSignedIn(PlusClient plusClient) {
        mSignInStatus.setText(getString(R.string.signed_in_status));

        // We can now obtain the signed-in user's profile information.
        Person currentPerson = plusClient.getCurrentPerson();
        if (currentPerson != null) {
            String greeting = getString(R.string.greeting_status, currentPerson.getDisplayName());
            mSignInStatus.setText(greeting);
            // Add the user name in the Preferences
            AppPrefs.setUserAccountName(SignInActivity.this, currentPerson.getDisplayName());
            Intent reservationsIntent = new Intent(SignInActivity.this, NavDrawerMainActivity.class);
            startActivity(reservationsIntent);
            finish();
        } else {
            resetAccountState();
        }
    }

    private void resetAccountState() {
        mSignInStatus.setText(getString(R.string.signed_out_status));
    }
}
