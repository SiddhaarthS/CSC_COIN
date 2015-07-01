package com.example.androidhive;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NotificationsFragment extends Fragment {
	
	public NotificationsFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        Button btnSave;
        btnSave=(Button)rootView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                Intent i = new Intent(getActivity(), RegisterActivity.class);
                i.putExtra("event"," ");
                i.putExtra("date"," ");
                i.putExtra("college"," ");
                startActivity(i);
            }
        });
        return rootView;
    }
}
