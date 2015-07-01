package com.example.androidhive;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;
    ArrayList<Actors> actorsList;

    ActorAdapter adapter;

    // url to get all products list
    private static String url_all_products = "http://www.seshathiri1.byethost7.com/get_all_products.php";

    // JSON Node names private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_DESCRIPTION = "description";


    // products JSONArray
    JSONArray products = null;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        // new LoadAllProducts().execute();

        actorsList = new ArrayList<Actors>();
        //Button sign = (Button) rootView.findViewById(R.id.signup);

        new JSONAsyncTask().execute("http://www.seshathiri1.byethost7.com/get_all_products.php");
        ListView lv = (ListView) rootView.findViewById(android.R.id.list);

        adapter = new ActorAdapter(getActivity(), R.layout.list_item, actorsList);

        lv.setAdapter(adapter);

       /* sign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);

            }
        });*/
        // on seleting single product
        // launching Edit Product Screen
       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(getActivity(),
                        EventsActivity.class);
                // sending pid to next activity
                in.putExtra(TAG_PID, pid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });


        return rootView;

    }
    // Response from Edit Product Activity
       // Response from Edit Product Activity


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
           // dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        /**
         * getting All products from url
         */
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);


                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray(TAG_PRODUCTS);

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        Actors actor = new Actors();

                        actor.setName(object.getString("name"));
                        actor.setDescription(object.getString("pid"));
                        actor.setCollege(object.getString("description"));
                        //actor.setImage(object.getString("image"));
                        String img = object.getString("image_link");
                        img.replaceAll("'\'", "");
                        actor.setImage(img);
                        actorsList.add(actor);
                    }
                    return true;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            adapter.notifyDataSetChanged();
            if (result == false)
                Toast.makeText(getActivity(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();


        }
    }

}
