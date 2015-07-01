package com.example.androidhive;

import static com.example.androidhive.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.example.androidhive.CommonUtilities.EXTRA_MESSAGE;
import static com.example.androidhive.CommonUtilities.SENDER_ID;
import static com.example.androidhive.CommonUtilities.SERVER_URL;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

public class RegisterActivity extends Activity {
	// alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	
	// Internet detector
	ConnectionDetector cd;
	
	// UI elements
	EditText txtName;
	EditText txtEmail;
    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;
    public static String name;
    public static String email;
    public static String event;
    public static String date;
    public static String college;


    Button btnRegister;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(RegisterActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// Check if GCM configuration is set
		if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
				|| SENDER_ID.length() == 0) {
			// GCM sernder id / server url is missing
			alert.showAlertDialog(RegisterActivity.this, "Configuration Error!",
					"Please set your Server URL and GCM Sender ID", false);
			// stop executing code by return
			 return;
		}
		
		txtName = (EditText) findViewById(R.id.txtName);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		btnRegister = (Button) findViewById(R.id.btnRegister);
        Intent i = getIntent();

        // getting product id (pid) from intent
         event = i.getStringExtra("event");
         date = i.getStringExtra("date");
         college=i.getStringExtra("college");
		/*
		 * Click event on Register button
		 * */
		btnRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// Read EditText dat
		    name = txtName.getText().toString();
			email = txtEmail.getText().toString();
				
				// Check if user filled the form
				//if(name.trim().length() > 0 && email.trim().length() > 0){
					// Launch Main Activity
					//Intent i = new Intent(getApplicationContext(), SecondGcmActivity.class);
					
					// Registering user on our server					
					// Sending registraiton details to MainActivity
				/*	i.putExtra("name", name);
				i.putExtra("email", email);
                i.putExtra("event",event);
                i.putExtra("date",date);
                i.putExtra("college",college);
					startActivity(i);
					finish();*/
                // Make sure the device has the proper dependencies.
                GCMRegistrar.checkDevice(getApplicationContext());


                // Make sure the manifest was properly set - comment out this line
                // while developing the app, then uncomment it when it's ready.
                GCMRegistrar.checkManifest(getApplicationContext());
                registerReceiver(mHandleMessageReceiver, new IntentFilter(
                        DISPLAY_MESSAGE_ACTION));
                // Get GCM registration id
                final String regId = GCMRegistrar.getRegistrationId(getApplicationContext());
                // Check if regid already presents
                if (regId.equals("")) {
                    // Registration is not present, register now with GCM
                    GCMRegistrar.register(getApplicationContext(), SENDER_ID);
                }
                else {
                    // Device is already registered on GCM
                    // if (GCMRegistrar.isRegisteredOnServer(this)) {
                    // Skips registration.
                    //   Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
                    //} else {
                    // Try to register again, but not in the UI thread.
                    // It's also necessary to cancel the thread onDestroy(),
                    // hence the use of AsyncTask instead of a raw thread.
                    final Context context = getApplicationContext();
                    mRegisterTask = new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            // Register on our server
                            // On server creates a new user
                            ServerUtilities.register(context,name,email,regId,event,date,college);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            mRegisterTask = null;
                        }

                    };
                    mRegisterTask.execute(null, null, null);
                }


            }/*else{
					// user doen't filled that data
					// ask him to fill the form
					alert.showAlertDialog(RegisterActivity.this, "Registration Error!", "Please enter your details", false);
				}*/

		});
	}
    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */

            // Showing received message

            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

            // Releasing wake lock
            WakeLocker.release();
        }
    };
    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            //Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }
}
