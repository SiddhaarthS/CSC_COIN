package com.example.androidhive;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SavedEventsFragment extends Fragment {

	public SavedEventsFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        final ListView listview = (ListView)rootView.findViewById(R.id.listview);
       /* String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };*/
        final DatabaseHandler db = new DatabaseHandler(getActivity());
       /* final ArrayList<String> list = new ArrayList<String>();
          for (int i = 0; i < l.size(); i++) {
          list.add(l.get(i));
        }
       */
        ArrayList<String> l=new ArrayList<String>();
        l=db.select_data();

        final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, l);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView)view).getText().toString();

                Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();
                //ArrayList=db.getValues(item);
                ArrayList<HashMap<String, String>> mylistData2 = db.getValues(item);
                String eventname=mylistData2.get(0).get("eventname");
                String date=mylistData2.get(0).get("date");
                String college=mylistData2.get(0).get("college");
                String category=mylistData2.get(0).get("category");
                String events=mylistData2.get(0).get("events");
                String website=mylistData2.get(0).get("website");
                String state=mylistData2.get(0).get("state");
                String district=mylistData2.get(0).get("district");
                String contact=mylistData2.get(0).get("contact");
                String number=mylistData2.get(0).get("number");
                Intent i = new Intent(getActivity(), SavedEvents.class);
                i.putExtra("event",eventname);
                i.putExtra("date",date);
                i.putExtra("college",college);
                i.putExtra("category",category);
                i.putExtra("events",events);
                i.putExtra("website",website);
                i.putExtra("state",state);
                i.putExtra("district",district);
                i.putExtra("contact",contact);
                i.putExtra("number",number);
                startActivity(i);
            }
        });
        return rootView;
    }
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
          /*  for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }*/
        }
/*
        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
*/
    }

}
