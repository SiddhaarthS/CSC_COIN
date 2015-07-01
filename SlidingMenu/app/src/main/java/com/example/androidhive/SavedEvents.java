package com.example.androidhive;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Revanth K on 14/03/2015.
 */
public class SavedEvents extends Activity {

    private static final String DB_NAME = "EventDB";

    TextView txtName;
    TextView txtPrice;
    TextView txtDesc;
    TextView contact,category,state,website,district,events;
    TextView contactname,number;
    Button btnCall;
    ImageButton imageButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_events);
        Bundle extras=getIntent().getExtras();
        String event=extras.getString("event");
        String date=extras.getString("date");
        String college=extras.getString("college");
        String tcategory=extras.getString("category");
        String tevents=extras.getString("events");
        String twebsite=extras.getString("website");
        String tstate=extras.getString("state");
        String tdistrict=extras.getString("district");
        String tcontact=extras.getString("contact");
        String tnumber=extras.getString("number");

        txtName = (TextView) findViewById(R.id.inputName);
        txtPrice = (TextView) findViewById(R.id.inputPrice);
        txtDesc = (TextView) findViewById(R.id.inputDesc);
        category = (TextView) findViewById(R.id.category);
        website=(TextView)findViewById(R.id.website);
        state=(TextView)findViewById(R.id.state);
        district=(TextView)findViewById(R.id.district);
        events=(TextView)findViewById(R.id.events);
        contactname=(TextView)findViewById(R.id.contactname);
        number=(TextView)findViewById(R.id.contact);

        txtName.setText(event);
        txtPrice.setText(date);
        txtDesc.setText(college);
        category.setText(tcategory);
        events.setText(tevents);
        website.setText(twebsite);
        district.setText(tdistrict);
        state.setText(tstate);
       // Toast.makeText(getApplicationContext(),"Event Name:"+event+"", Toast.LENGTH_LONG).show();
    }


}

