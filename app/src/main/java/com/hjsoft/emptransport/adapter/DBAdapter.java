package com.hjsoft.emptransport.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.hjsoft.emptransport.DatabaseHandler;
import com.hjsoft.emptransport.model.JourneyDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hjsoft on 29/5/17.
 */
public class DBAdapter {


    static final String DATABASE_NAME = "user.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    
    public static final String TABLE_STORE_TIMES="create table if not exists "+"INFO"+
            "( " +"DESC text,DATA text,TRIPID text,AT text); ";

    public static final String TABLE_LATLNG = "create table if not exists "+"LATLNG_DETAILS"+
            "( " +"TRIPID  text,LATITUDE  double,LONGITUDE double,PLACE text,CUM_DISTANCE integer,TIME_UPDATED text); ";

    public static final String TABLE_TRIPID="create table if not exists "+"TRIP_ID"+
            "( " +"TRIPID  text);";

    public static final String TABLE_JOURNEY_DETAILS="create table if not exists "+"JOURNEY_DETAILS" +
                       "( " +"TRIPID  text,DRIVER_ID text,TRIP_DATE text,ROUTE_NAME text,DISTANCE text,JTIME text,ROUTE_DETAILS text,PICKUP_LAT text,PICKUP_LNG text,DROP_LAT text,DROP_LNG text);";

    public static final String DB_CREATE_TEMP_LATLNG = "create table if not exists "+"RIDE_TEMP_LATLNG"+
            "( " +"REQUEST_ID text,LATITUDE  double,LONGITUDE double,TIME_UPDATED text); ";

    public static final String TABLE_STORE_URL="create table if not exists "+"LOC_URL"+
            "( " +"DSNO  text,URL text); ";

    // Variable to hold the database instance
    public SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DatabaseHandler dbHelper;

    public  DBAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DatabaseHandler(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public  DBAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public void insertJourneyData(String tripId,String driverId,String tripDate,String routeName,String distance,String time,String routeDetails,String pickupLat,String pickupLng,String dropLat,String dropLng)
    {
        db=dbHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("TRIPID",tripId);
        newValues.put("DRIVER_ID",driverId);
        newValues.put("TRIP_DATE",tripDate);
        newValues.put("ROUTE_NAME",routeName);
        newValues.put("DISTANCE",distance);
        newValues.put("JTIME",time);
        newValues.put("ROUTE_DETAILS",routeDetails);
        newValues.put("PICKUP_LAT",pickupLat);
        newValues.put("PICKUP_LNG",pickupLng);
        newValues.put("DROP_LAT",dropLat);
        newValues.put("DROP_LNG",dropLng);
        // Insert the row into your table
        long value=db.insert("JOURNEY_DETAILS", null, newValues);
    }

    public ArrayList<JourneyDetails> getJourneyData(String tripId)
    {

        db=dbHelper.getReadableDatabase();

        Cursor c=db.rawQuery("SELECT * FROM JOURNEY_DETAILS WHERE TRIPID="+" '"+tripId+"' ",null);
        ArrayList<JourneyDetails> dataList=new ArrayList<JourneyDetails>();
        JourneyDetails data;

        if(c.getCount()>0)
        {
            for(int i=0;i<c.getCount();i++)
            {
                c.moveToNext();
                data=new JourneyDetails(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getString(10));
                dataList.add(data);
            }
        }
        c.close();
        // close();
        return dataList;
    }

    public boolean findJourneyData(String tripId)
    {
        boolean flag=false;

        db=dbHelper.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM JOURNEY_DETAILS WHERE TRIPID ="+" '"+tripId+"' ",null);
        String status="";

        if(c.getCount()>0) {

            flag=true;
        }
        c.close();
        // close();
        return flag;

    }

    public void deleteJourenyInfo(String tripId)
    {
        db=dbHelper.getReadableDatabase();
        // System.out.println("delete from INFO WHERE TRIPID ="+" '"+tripId+"'" );
        db.execSQL("delete from JOURNEY_DETAILS WHERE TRIPID ="+" '"+tripId+"'" );
        //System.out.println("deletion done.....duty_details");

    }

    public void insertInfo(String desc,String data,String tripId,String at)
    {
        db=dbHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("DESC",desc);
        newValues.put("DATA",data);
        newValues.put("TRIPID",tripId);
        newValues.put("AT",at);
        // Insert the row into your table
        long value=db.insert("INFO", null, newValues);

        // System.out.println("insertion result is "+value);
    }

    public void updateInfo(String desc,String data,String tripId)
    {
        db=dbHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put("DATA",data);

        // Assign values for each row.
        // Insert the row into your table

        db.update("INFO",newValues,"DESC ="+" '"+desc+"' "+" AND TRIPID ="+" '"+tripId+"'", null);
    }

    public void deleteInfoForTripId(String tripId)
    {
        db=dbHelper.getReadableDatabase();
        // System.out.println("delete from INFO WHERE TRIPID ="+" '"+tripId+"'" );
        db.execSQL("delete from INFO WHERE TRIPID ="+" '"+tripId+"'" );
        //System.out.println("deletion done.....duty_details");

    }

    public String getInfo(String tripId,String desc)
    {
        String data="";

        Cursor c=db.rawQuery("SELECT * FROM INFO WHERE DESC ="+" '"+desc+"' "+" AND TRIPID ="+" '"+tripId+"'  ",null);

        // System.out.println("SELECT * FROM INFO WHERE DESC ="+" '"+desc+"' "+" AND TRIPID ="+" '"+tripId+"'  ");

        c.moveToNext();

        if(c.getCount()>0) {

            data = c.getString(1);
        }

        return data;
    }


    public String getInfoTime(String tripId,String desc)
    {
        String data="";

        Cursor c=db.rawQuery("SELECT * FROM INFO WHERE DESC ="+" '"+desc+"' "+" AND TRIPID ="+" '"+tripId+"'  ",null);

        // System.out.println("SELECT * FROM INFO WHERE DESC ="+" '"+desc+"' "+" AND TRIPID ="+" '"+tripId+"'  ");

        c.moveToNext();

        if(c.getCount()>0) {

            data = c.getString(3);
        }

        return data;
    }


    public long insertEntry(String tripId,double latitude,double longitude,String place,long cum_distance,String time_updated)
    {
        db=dbHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("TRIPID",tripId);
        newValues.put("LATITUDE",latitude);
        newValues.put("LONGITUDE",longitude);
        newValues.put("PLACE",place);
        newValues.put("CUM_DISTANCE",cum_distance);
        newValues.put("TIME_UPDATED",time_updated);
        // Insert the row into your table
        long value=db.insert("LATLNG_DETAILS", null, newValues);
        //  close();
        return  value;
    }

    public String getRouteDetails(String tripId) {

        String st = "";
        db = dbHelper.getReadableDatabase();

        String sql="SELECT * FROM LATLNG_DETAILS "+"WHERE TRIPID= '"+tripId+"' ";
        Cursor cursor=db.rawQuery(sql,null);
        JSONObject jobj ;
        JSONArray arr = new JSONArray();

        String data="";
        while (cursor.moveToNext())
        {
            if(cursor.isFirst())
            {
                data=data+cursor.getString(1)+","+cursor.getString(2);
            }
            else {
                data=data+"*"+cursor.getString(1)+","+cursor.getString(2);
            }
        }

        while(cursor.moveToNext()) {
            try {
                jobj = new JSONObject();
                jobj.put("TripId",cursor.getString(0));
                jobj.put("Latitude", cursor.getString(1));
                jobj.put("Longitude", cursor.getString(2));
                jobj.put("Place",cursor.getString(3));
                jobj.put("Cumulative_Distance (meters)",cursor.getString(4));
                jobj.put("Time_Updated", cursor.getString(5));
                //System.out.println("lat "+cursor.getString(0)+" long "+cursor.getString(1)+" place "+cursor.getString(2)+" distance "+cursor.getString(3)+" time"+cursor.getString(4));
                arr.put(jobj);
            }
            catch (JSONException e){e.printStackTrace();}
        }
        try{
            jobj = new JSONObject();
            jobj.put("data", arr);
            st=jobj.toString();
        }catch(JSONException e){e.printStackTrace();}

        return data;
    }

    public String getWaypoints(String tripId)
    {
        String waypoints="";

        String st = "";
        db = dbHelper.getReadableDatabase();
        float[] dist=new float[3];
        long distance=0;

        String sql="SELECT * FROM LATLNG_DETAILS WHERE TRIPID ="+" '"+tripId+"' ";

        Cursor c=db.rawQuery(sql,null);

        // System.out.println("c count before "+c.getCount());

        int n;

        if(c.getCount()<5)
        {
            n=1;
        }
        else {
            n=c.getCount()/5;

            // System.out.println("n is "+n);
        }

        for(int i=0;(n*i)<c.getCount();i++)
        {
            c.moveToPosition(n*i);

            if(i==0)
            {
                waypoints=c.getString(1)+","+c.getString(2);
            }
            else {
                waypoints=waypoints+"|"+c.getString(1)+","+c.getString(2);
            }
        }

        String sql1="SELECT * FROM LATLNG_DETAILS";

        Cursor c1=db.rawQuery(sql1,null);

        // System.out.println("c count after is "+c1.getCount());

        for(int j=0;j<c1.getCount();j++)
        {
            c1.moveToNext();
            // System.out.println(c1.getString(0)+":"+c1.getString(1)+":"+c1.getString(2));

        }

        return waypoints;
    }

    public boolean findTripInfo(String tripId)
    {
        boolean flag=false;

        db=dbHelper.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM LATLNG_DETAILS WHERE TRIPID ="+" '"+tripId+"' ",null);
        String status="";

        if(c.getCount()>0) {

            flag=true;
        }
        c.close();
        // close();
        return flag;

    }

    public void deleteLatLngDetails(String tripId)
    {
        db=dbHelper.getReadableDatabase();
        // System.out.println("delete from LATLNG_DETAILS WHERE TRIPID ="+" '"+tripId+"'" );
        db.execSQL("delete from LATLNG_DETAILS WHERE TRIPID ="+" '"+tripId+"'" );
    }

    public void deleteInfo(String tripId)
    {
        db=dbHelper.getReadableDatabase();
        // System.out.println("delete from INFO WHERE TRIPID ="+" '"+tripId+"'" );
        db.execSQL("delete from INFO WHERE TRIPID ="+" '"+tripId+"'" );
    }

    public void insertTrip(String tripId)
    {
        db=dbHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        // Assign values for each row.

        newValues.put("TRIPID",tripId);
        // Insert the row into your table
        long value=db.insert("TRIP_ID", null, newValues);

        // System.out.println("insertion result is "+value);
    }

    public void updateTrip(String tripId)
    {
        db=dbHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put("TRIPID",tripId);

        // Assign values for each row.
        // Insert the row into your table

        db.update("TRIP_ID",newValues,null, null);
    }

    public String getTrip()
    {
        String data="";

        Cursor c=db.rawQuery("SELECT * FROM TRIP_ID",null);

        // System.out.println("SELECT * FROM TRIP_ID ");

        c.moveToNext();

        if(c.getCount()>0) {

            data = c.getString(0);
        }

        return data;
    }



    //waypoints data

    public int getWaypointsCount(String rId)
    {

        db = dbHelper.getReadableDatabase();
        float[] dist=new float[3];
        long distance=0;

        String sql="SELECT * FROM RIDE_TEMP_LATLNG WHERE REQUEST_ID ="+" '"+rId+"' ";

        Cursor c=db.rawQuery(sql,null);

        return c.getCount();
    }

    public String getWaypointsForSnapToRoad(String rId)
    {
        String waypoints="";

        String st = "";
        db = dbHelper.getReadableDatabase();
        float[] dist=new float[3];
        long distance=0;

        String sql="SELECT * FROM RIDE_TEMP_LATLNG WHERE REQUEST_ID ="+" '"+rId+"' ";

        Cursor c=db.rawQuery(sql,null);

        for(int i=0;i<c.getCount();i++)
        {
            if(c.getCount()<=100) {

                c.moveToPosition(i);

                if (i == 0) {
                    waypoints = c.getString(1) + "," + c.getString(2);
                } else {
                    waypoints = waypoints + "|" + c.getString(1) + "," + c.getString(2);
                }
            }
        }

        return waypoints;
    }

    public String getIntervalWaypoints(String rId)
    {
        String waypoints="";

        String st = "";
        db = dbHelper.getReadableDatabase();
        float[] dist=new float[3];
        long distance=0;

        String sql="SELECT * FROM RIDE_TEMP_LATLNG WHERE REQUEST_ID ="+" '"+rId+"' ";

        Cursor c=db.rawQuery(sql,null);

        for(int i=0;i<c.getCount();i++)
        {
            if(c.getCount()<=100) {

                c.moveToPosition(i);

                if (i == 0) {
                    waypoints = c.getString(1) + "," + c.getString(2)+"#"+c.getString(3);
                } else {
                    waypoints = waypoints + "*" + c.getString(1) + "," + c.getString(2)+"#"+c.getString(3);
                }
            }
        }

        return waypoints;
    }

    public long insertLatLngEntry(String rId,double latitude,double longitude,String timeUpdated)
    {
        db=dbHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("REQUEST_ID",rId);
        newValues.put("LATITUDE",latitude);
        newValues.put("LONGITUDE",longitude);
        newValues.put("TIME_UPDATED",timeUpdated);

        // Insert the row into your table
        long value=db.insert("RIDE_TEMP_LATLNG", null, newValues);
        //  close();
        return  value;
    }

    public void deleteLatLng(String rId)
    {
        db=dbHelper.getReadableDatabase();
        db.execSQL("delete from RIDE_TEMP_LATLNG WHERE REQUEST_ID ="+" '"+rId+"' ");
        // System.out.println("delete from LATLNG_DETAILS WHERE DSNO ="+" '"+dsno+"' ");
        //System.out.println("deletion done.....latlng_data");
    }

    public long insertLocUrl(String dsno,String url)
    {
        db=dbHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("DSNO",dsno);
        newValues.put("URL",url);

        // Insert the row into your table
        long value=db.insert("LOC_URL", null, newValues);
        //  close();
        return  value;
    }

    public String getLocUrl(String dsno)
    {
        String url="";

        db = dbHelper.getReadableDatabase();

        String sql="SELECT * FROM LOC_URL WHERE DSNO ="+" '"+dsno+"' ";

        Cursor c=db.rawQuery(sql,null);

        for(int i=0;i<c.getCount();i++)
        {
            c.moveToNext();

            if(i==0)
            {
                url=c.getString(1);
            }
            else {

                url=url+"*"+c.getString(1);
            }
        }

        return url;
    }

    public void deleteLocUrl(String dsno)
    {
        db=dbHelper.getReadableDatabase();
        db.execSQL("delete from LOC_URL WHERE DSNO ="+" '"+dsno+"' ");
        // System.out.println("delete from LATLNG_DETAILS WHERE DSNO ="+" '"+dsno+"' ");
        //System.out.println("deletion done.....latlng_data");
    }



}
