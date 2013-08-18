package com.samsunghack.apps.android.noq;

import java.math.BigDecimal;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.util.Log;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class PaymentsActivity extends ListActivity {
	public final static String EXTRA_MESSAGE = "aldi_first.testing.MESSAGE";
	// set to PaymentActivity.ENVIRONMENT_LIVE to move real money.
    // set to PaymentActivity.ENVIRONMENT_SANDBOX to use your test credentials from https://developer.paypal.com
    // set to PaymentActivity.ENVIRONMENT_NO_NETWORK to kick the tires without communicating to PayPal's servers.
    private static final String CONFIG_ENVIRONMENT = PaymentActivity.ENVIRONMENT_NO_NETWORK;
    
    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "seller zhupani";
    // when testing in sandbox, this is likely the -facilitator email address. 
    private static final String CONFIG_RECEIVER_EMAIL = "aldizhupani@gmail.com";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payments);
		String[] tickerSymbols = new String[] { "Beef&Broccoli: $9", "Noodle Soup: $10", "Fried Rice: $8"};
		setListAdapter(new ArrayAdapter<String>(this,
		                        android.R.layout.simple_list_item_1,
		                        tickerSymbols));
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//paypal functionality
		Intent intent = new Intent(this, PayPalService.class);
        
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, CONFIG_ENVIRONMENT);
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
        intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, CONFIG_RECEIVER_EMAIL);
        
        startService(intent);
		
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

    public void onBuyPressed(View pressed) {
        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal("1.75"), "USD", "hipster jeans");
        
        Intent intent = new Intent(this, PaymentActivity.class);
        
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, CONFIG_ENVIRONMENT);
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
        intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, CONFIG_RECEIVER_EMAIL);
        
        // It's important to repeat the clientId here so that the SDK has it if Android restarts your 
        // app midway through the payment UI flow.
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "seller zhupani");
        intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "AldiZhupani");
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            JSONObject json_confirmation = confirm.toJSONObject();
            try{
            	JSONObject inner_element = json_confirmation.getJSONObject("payment");
            	Log.i("description", inner_element.get("short_description").toString());
            	Log.i("payment amount", inner_element.get("amount").toString());
            	Log.i("payment key", inner_element.get("currency_code").toString());
            }catch(JSONException e){}
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
            Log.i("paymentExample", "An invalid payment was submitted. Please see the docs.");
        }
    }
    
	@Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

}
