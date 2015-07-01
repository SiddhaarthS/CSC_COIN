package com.example.androidhive;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

public class EventsActivity extends Activity {

    TextView txtName;
    TextView txtPrice;
    TextView txtDesc;
    TextView contact,category,state,website,district,events;
    TextView contactname,number;
  //  Button btnSave;
    Button btnSaveEvent;
    Button btnCall;
    ImageButton imageButton;
    private ShareActionProvider provider;
    private static final String DB_NAME = "EventDB";
    Button but;
    String pid;

    // Progress Dialog
    private ProgressDialog pDialog;
    static{
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    private static final String url_product_detials = "http://www.seshathiri1.byethost7.com/get_product_details.php";

    // url to update product
    private static final String url_update_product = "http://www.seshathiri1.byethost7.com/update_product.php";

    // url to delete product
    private static final String url_delete_product = "http://www.seshathiri1.byethost7.com/delete_product.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_CATEGORY= "category";
    private static final String TAG_WEBSITE= "website";
    private static final String TAG_STATE= "state";
    private static final String TAG_DISTRICT= "district";
    private static final String TAG_EVENTS= "events";
    private static final String TAG_CONTACT= "contactname";
    private static final String TAG_NUMBER= "phoneno";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_events);



        // save button
      //  btnSave = (Button) findViewById(R.id.btnSave);
        btnSaveEvent = (Button) findViewById(R.id.btnSaveEvent);
        btnCall=(Button)findViewById(R.id.btnCall);
        contact = (TextView) findViewById(R.id.contact);
        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        pid = i.getStringExtra(TAG_PID);

        // Getting complete product details in background thread
        new GetProductDetails().execute();

        // save button click event
       /* btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                i.putExtra("event",txtName.getText().toString());
                i.putExtra("date",txtPrice.getText().toString());
                i.putExtra("college",txtDesc.getText().toString());
                startActivity(i);
            }
        });*/

        btnSaveEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting product in background thread
              //  Intent i1 = new Intent(getApplicationContext(), SaveEvents.class);
              //  i1.putExtra("event",txtName.getText().toString());
              //  i1.putExtra("date",txtPrice.getText().toString());
              //  i1.putExtra("college",txtDesc.getText().toString());
              //  startActivity(i1);
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                int status=db.insert_data(txtName.getText().toString(),txtPrice.getText().toString(),txtDesc.getText().toString(),category.getText().toString(),events.getText().toString(),website.getText().toString(),state.getText().toString(),district.getText().toString(),contactname.getText().toString(),number.getText().toString());
                if(status==1)
                {
                    Toast.makeText(getApplicationContext(), "Event already saved", Toast.LENGTH_LONG).show();
                }
                else if(status==0)
                {
                    Toast.makeText(getApplicationContext(), "Event Saved Successfully", Toast.LENGTH_LONG).show();
                }
                ArrayList<String> l=db.select_data();
                for(int i=0;i<l.size();i++)
                    Toast.makeText(getApplicationContext(), "EventName: "+l.get(i)+"", Toast.LENGTH_LONG).show();
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+contact.getText().toString()));
                startActivity(callIntent);
            }
        });

      /*  imageButton = (ImageButton) findViewById(R.id.imageButton1);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey guys Found an awesome Event Check the details\n"+txtName.getText().toString()+"\n"+txtPrice.getText().toString()+"\n"+txtDesc.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check this!!\n");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_share was selected
            case R.id.action_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey guys Found an awesome Event Check the details\n"+txtName.getText().toString()+"\n"+txtPrice.getText().toString()+"\n"+txtDesc.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check this!!\n");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            default:
                break;
        }

        return true;
    }


    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EventsActivity.this);
            pDialog.setMessage("Loading details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("pid", pid));

                        // getting product det ails by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_product_detials, "GET", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array

                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);

                            // product with this pid found
                            // Edit Text
                            txtName = (TextView) findViewById(R.id.inputName);
                            txtPrice = (TextView) findViewById(R.id.inputPrice);
                            txtDesc = (TextView) findViewById(R.id.inputDesc);
                            ImageView imageView = (ImageView)findViewById(R.id.imageView);
                            category = (TextView) findViewById(R.id.category);
                            website=(TextView)findViewById(R.id.website);
                            state=(TextView)findViewById(R.id.state);
                            district=(TextView)findViewById(R.id.district);
                            events=(TextView)findViewById(R.id.events);
                            contactname = (TextView) findViewById(R.id.contactname);
                            number= (TextView) findViewById(R.id.contact);
                            // display product data in EditText
                           // Toast.makeText(getApplicationContext(), "name"+product.getString(TAG_CONTACT), Toast.LENGTH_LONG).show();
                            txtName.setText(product.getString(TAG_NAME));
                            txtPrice.setText(product.getString(TAG_PRICE));
                            txtDesc.setText(product.getString(TAG_DESCRIPTION));
                            category.setText(product.getString(TAG_CATEGORY));
                            website.setText(product.getString(TAG_WEBSITE));
                            state.setText(product.getString(TAG_STATE));
                            district.setText(product.getString(TAG_DISTRICT));
                            events.setText(product.getString(TAG_EVENTS));
                            contactname.setText(product.getString(TAG_CONTACT));
                            number.setText(product.getString(TAG_NUMBER));
                            String Img="image";
                            byte[] decodedString = Base64.decode(product.getString(Img), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            //byte[] decodedString = Base64.decode(product.getString(Img), Base64.URL_SAFE);
                            //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            // txtDesc.setText(product.getString(Img));
                            imageView.setImageBitmap(decodedByte);
                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }



        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();

        }
    }

    /**
     * Background Async Task to  Save product Details
     * */
    class SaveProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EventsActivity.this);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String name = txtName.getText().toString();
            String price = txtPrice.getText().toString();
            String description = txtDesc.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, pid));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_PRICE, price));
            params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_product,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }

    /*****************************************************************
     * Background Async Task to Delete Product
     * */
    class DeleteProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EventsActivity.this);
            pDialog.setMessage("Deleting Product...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("pid", pid));

                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_product, "POST", params);

                // check your log for json response
                Log.d("Delete Product", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // product successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about product deletion
                    setResult(100, i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();



        }

    }
}