package com.example.androidhive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by Revanth K on 14/03/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EventDB";
    private static final String TABLE_NAME = "eventlist";

    ArrayList<HashMap<String, String>> mylistData1 =new ArrayList<HashMap<String, String>>();

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // Category table create query
        String CREATE_ITEM_TABLE;
        CREATE_ITEM_TABLE = "CREATE TABLE eventlist(eventname varchar(100),eventdate varchar(100),college varchar(100),category varchar(100),events varchar(255),website varchar(100),state varchar(100),district varchar(100),contactname varchar(100),number varchar(100));";
        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
   public int insert_data(String eventname,String date,String college,String category,String events,String website,String state,String district,String contact,String number)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int j=checkStatus(eventname);
        int status=0;
        if(j==0)
        {
        ContentValues values = new ContentValues();
        values.put("eventname", eventname);
        values.put("eventdate",date);
        values.put("college",college);
        values.put("category",category);
        values.put("events",events);
        values.put("website",website);
        values.put("state",state);
        values.put("district",district);
            values.put("contactname",contact);
            values.put("number",number);
        db.insert(TABLE_NAME,null,values);
        db.close();
        }
        else
        {
            status=1;
        }

        return status;
    }

    public int checkStatus(String eventname) {

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT eventname FROM eventlist where eventname='"+eventname+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        int i=cursor.getCount();
        return  i;
    }

    public ArrayList<String> select_data()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> l=new ArrayList<String>();
        String selectQuery = "SELECT eventname FROM eventlist";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                l.add(cursor.getString(0));
                // get  the  data into array,or class variable
            } while (cursor.moveToNext());
        }
        db.close();
        return l;
    }
    public ArrayList<HashMap<String, String>> getValues(String eventname){

        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String,String> map = new HashMap<String, String>();
        String selectQuery="";
        selectQuery = "SELECT * FROM eventlist where eventname='"+eventname+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        int columnSize = cursor.getColumnCount();//number of columns eventname,eventdate,college
        int resultSize = cursor.getCount();//

        cursor.moveToFirst();
        String str=""+columnSize+","+resultSize;
        int i = 0;
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                map.put("eventname", cursor.getString(i));
                map.put("date", cursor.getString(i + 1));
                map.put("college", cursor.getString(i + 2));
                map.put("category", cursor.getString(i + 3));
                map.put("events", cursor.getString(i + 4));
                map.put("website", cursor.getString(i + 5));
                map.put("state", cursor.getString(i + 6));
                map.put("district", cursor.getString(i + 7));
                map.put("contact", cursor.getString(i + 8));
                map.put("number", cursor.getString(i + 9));

                mylistData1.add(map);
                i =0;
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        return mylistData1;
    }
}