package com.hjsoft.emptransport.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.SessionManager;
import com.hjsoft.emptransport.adapter.DBAdapter;
import com.hjsoft.emptransport.adapter.DrawerItemCustomAdapter;
import com.hjsoft.emptransport.fragment.HomeFragment;
import com.hjsoft.emptransport.fragment.RideFragment;
import com.hjsoft.emptransport.model.Distance;
import com.hjsoft.emptransport.model.DistancePojo;
import com.hjsoft.emptransport.model.DutyData;
import com.hjsoft.emptransport.model.EmpData;
import com.hjsoft.emptransport.model.EmpPojo;
import com.hjsoft.emptransport.model.Leg;
import com.hjsoft.emptransport.model.NavigationData;
import com.hjsoft.emptransport.model.Pojo;
import com.hjsoft.emptransport.model.Route;
import com.hjsoft.emptransport.model.TripEmpPojo;
import com.hjsoft.emptransport.webservices.API;
import com.hjsoft.emptransport.webservices.RestClient;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 26/5/17.
 */
public class RideActivity extends AppCompatActivity {

    SupportMapFragment mapFragment;
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected boolean mRequestingLocationUpdates;
    final static int REQUEST_LOCATION = 199;
    protected Location mLastLocation;
    double latitude,longitude,current_lat,current_long;
    Geocoder geocoder;
    List<Address> addresses;
    LatLng lastLoc,curntloc;
    String complete_address;
    float[] results=new float[3];
    long res=0;
    boolean entered=false;
    Marker mPickup,mDrop;
    LayoutInflater inflater;
    TextView btFinishDuty;
    Marker car;
    TextView tvCurrentLoc;
    boolean isMarkerRotating=false;
    LatLng startPosition,finalPosition,currentPosition,lastLocDist;
    double cpLat,cpLng;
    SimpleDateFormat dateFormat;
    String timeUpdated;
    Handler h,hDist;
    Runnable r,rDist;
    float inAccurate = 10;
    String currentTime;
    API REST_CLIENT;
    ArrayList<DutyData> dutyList;
    ArrayList<EmpData> empList;
    int position;
    Bundle d;
    EmpData empData;
    DutyData dutyData;
    TextView tvFinish;
    String stTripId;
    DBAdapter dbAdapter;
    boolean first=true;
    boolean emp1OTP=false,emp2OTP=false,emp3OTP=false,emp4OTP=false,emp5OTP=false,emp6OTP=false,emp7OTP=false;
    String startingTime,endingTime;
    long diff;
    String totalTime,routeDetails;
    String pickupLat,pickupLong,dropLat,dropLong,stWaypoints;
    float distance=0,finalDistance;
    TextView tvMyDuties,tvStaffDetails;
    boolean staffDetails=false;
    ImageButton ibDots;
    List<EmpPojo> empPojoList;
    EmpPojo empPojo;
    TripEmpPojo tripEmpPojo;
    List<TripEmpPojo> tripEmpPojoList;
    boolean flag=false;
    String companyId="CMP0001";

    // ----------------------------------

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    DrawerItemCustomAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        NavigationData[] drawerItem = new NavigationData[3];

        drawerItem[0] = new NavigationData(R.drawable.arrow, "Upcoming Duties");
        drawerItem[1] = new NavigationData(R.drawable.arrow, "Past Duties");
        drawerItem[2] = new NavigationData(R.drawable.arrow, "Logout");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setupDrawerToggle();

        Fragment fragment=new RideFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, fragment,"all_duties").commit();
        setTitle("Duty In Progress");

        adapter.setSelectedItem(-1);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;
        adapter.setSelectedItem(position);

        switch (position) {
            case 0:
                Intent i=new Intent(RideActivity.this,AfterLoginActivity.class);
                startActivity(i);
                finish();
                break;
            case 1:
                Intent j=new Intent(RideActivity.this,AllRidesActivity.class);
                startActivity(j);
                finish();
                break;
            case 2:
                SessionManager s=new SessionManager(getApplicationContext());
                s.logoutUser();
                Intent l=new Intent(this,MainActivity.class);
                l.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                l.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(l);
                finish();

                break;

            default:
                break;
        }

        if (fragment != null) {

            openFragment(fragment,position);

        } else {
            // Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private void openFragment(Fragment fragment,int position){

        Fragment containerFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (containerFragment.getClass().getName().equalsIgnoreCase(fragment.getClass().getName())) {
            mDrawerLayout.closeDrawer(mDrawerList);
            return;
        }

        else{
            /*
           FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            */
            mDrawerLayout.closeDrawer(mDrawerList);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
        //  getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" +mTitle + "</font>")));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        /*
        int titleId = getResources().getIdentifier("toolbar", "id", "android");
        TextView abTitle = (TextView) findViewById(titleId);
        abTitle.setTextColor(Color.parseColor("#000000"));*/
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {

    }

   /* @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        REST_CLIENT= RestClient.get();
        dbAdapter=new DBAdapter(getApplicationContext());
        dbAdapter=dbAdapter.open();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        geocoder = new Geocoder(this, Locale.getDefault());
        inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        dutyList=(ArrayList<DutyData>) getIntent().getSerializableExtra("list");
        empPojoList=(ArrayList<EmpPojo>)getIntent().getSerializableExtra("empList");
        tripEmpPojoList=(ArrayList<TripEmpPojo>)getIntent().getSerializableExtra("tripEmpList");

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!"+empPojoList.size()+tripEmpPojoList.size());



        for(int i=0;i<empPojoList.size();i++)
        {
            empPojo=empPojoList.get(i);
            System.out.println("111111111"+empPojo.getLocationname());
            tripEmpPojoList=empPojo.getTripEmpPojo();

            for(int j=0;j<tripEmpPojoList.size();j++)
            {
                tripEmpPojo=tripEmpPojoList.get(j);

                System.out.println("222222"+tripEmpPojo.getEmployeename());
            }


            System.out.println();
        }
        //empList=(ArrayList<EmpData>)getIntent().getSerializableExtra("empList");

        d=getIntent().getExtras();

        position=d.getInt("position");
        dutyData=dutyList.get(position);

        btFinishDuty=(TextView) findViewById(R.id.am_tv_finish_duty);
        tvCurrentLoc=(TextView)findViewById(R.id.am_tv_place);
        tvMyDuties=(TextView)findViewById(R.id.am_tv_my_duties);
        tvStaffDetails=(TextView)findViewById(R.id.am_tv_staff_details);
        ibDots=(ImageButton)findViewById(R.id.am_ib_dots);

        stTripId=dutyData.getTripId();

        if((dbAdapter.getInfo(stTripId,"starting_time").equals("")))
        {
            System.out.println("++++++++++++++++++++++******+++++++++++++++++++++++++++++++++");
            startingTime = getCurrentTime();
            dbAdapter.insertInfo("starting_time", startingTime,stTripId,startingTime);
        }
        else {

        }

        if(Build.VERSION.SDK_INT<23)
        {
            //System.out.println("Sdk_int is"+Build.VERSION.SDK_INT);
            //System.out.println("the enetred values is "+entered);
            establishConnection();
        }
        else
        {
            if(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                establishConnection();
            }
            else
            {
                if(shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    Toast.makeText(RideActivity.this,"Location Permission is required for this app to run!",Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            }
        }

//        tvStaffDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                staffDetails=true;
//
//                Bundle b=getIntent().getExtras();
//                b.putString("tripId",stTripId);
//
//                Intent i=new Intent(RideActivity.this, EmpDetailsActivity.class);
//                i.putExtra("empList",empList);
//                i.putExtras(b);
//
//                startActivity(i);
//
//            }
//        });


        tvMyDuties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(RideActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        ibDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                staffDetails=true;

                Intent i=new Intent(RideActivity.this,DutySummaryActivity.class);
                i.putExtra("list", dutyList);
                i.putExtra("position", position);
                i.putExtra("value","yes");
                startActivity(i);

            }
        });


        btFinishDuty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // API : AIzaSyB7RFlSBoNyRCkuGMZo5i28yGZmu4GCHDI

                for(int i=0;i<empPojoList.size();i++)
                {
                    empPojo=empPojoList.get(i);
                    System.out.println("111111111"+empPojo.getLocationname());
                    tripEmpPojoList=empPojo.getTripEmpPojo();

                    for(int j=0;j<tripEmpPojoList.size();j++)
                    {
                        tripEmpPojo=tripEmpPojoList.get(j);

                        if((dbAdapter.getInfo(stTripId,tripEmpPojo.getEmployeename()+"@"+tripEmpPojo.getMobileno()+"#"+tripEmpPojo.getEmptimein())).equals(""))
                        {

                        }
                        else {

                            flag=true;
                        }
                    }



                }


                System.out.println("flag is "+flag);


                if(flag) {


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RideActivity.this);

                    LayoutInflater inflater = getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.alert_finish_duty, null);
                    dialogBuilder.setView(dialogView);

                    TextView ok = (TextView) dialogView.findViewById(R.id.afd_tv_ok);
                    TextView cancel = (TextView) dialogView.findViewById(R.id.afd_tv_cancel);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            alertDialog.dismiss();

                            final ProgressDialog progressDialog = new ProgressDialog(RideActivity.this);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Please wait ...");
                            progressDialog.show();

                            endingTime = getCurrentTime();

                            startingTime=dbAdapter.getInfo(stTripId,"starting_time");

                            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
                            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                            try {
                                Date date1 = timeFormat.parse(startingTime);
                                Date date2 = timeFormat.parse(endingTime);
                                //Date date3=timeFormat.parse(idle_time);

                                diff = (date2.getTime() - date1.getTime());
                                int Hours = (int) (diff / (1000 * 60 * 60));
                                int Mins = (int) (diff / (1000 * 60)) % 60;
                                long Secs = (int) (diff / 1000) % 60;

                                DecimalFormat formatter = new DecimalFormat("00");
                                String hFormatted = formatter.format(Hours);
                                String mFormatted = formatter.format(Mins);
                                String sFormatted = formatter.format(Secs);
                                totalTime = hFormatted + ":" + mFormatted + ":" + sFormatted;

                                // System.out.println("Total time travelled is "+finalTimeTravelled);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            routeDetails = dbAdapter.getRouteDetails(stTripId);

                            if (current_lat != 0.0 && current_long != 0.0) {

                                stWaypoints = dbAdapter.getWaypoints(stTripId);
                                pickupLat = dbAdapter.getInfo(stTripId, "pickup_lat");
                                pickupLong = dbAdapter.getInfo(stTripId, "pickup_long");
                                dropLat = String.valueOf(current_lat);
                                dropLong = String.valueOf(current_long);

                                String urlString = "https://maps.googleapis.com/maps/api/directions/json?" +
                                        "origin=" + pickupLat + "," + pickupLong + "&destination=" + dropLat + "," + dropLong + "&waypoints=" + stWaypoints + "&key=AIzaSyA5f0AhduhPgOWHWC9tGgzE_teAC_it5d4";

                                System.out.println(urlString);

                                Call<DistancePojo> call1 = REST_CLIENT.getDistanceDetails(urlString);
                                call1.enqueue(new Callback<DistancePojo>() {
                                    @Override
                                    public void onResponse(Call<DistancePojo> call, Response<DistancePojo> response) {


                                        DistancePojo data;
                                        Route rData;
                                        Leg lData;

                                        if (response.isSuccessful()) {
                                            data = response.body();

                                            System.out.println(response.message() + "::" + response.code() + "::" + response.errorBody());

                                            System.out.println("status for directions calculation is " + data.getStatus());
                                            List<Route> rDataList = data.getRoutes();
                                            // System.out.println("Route size "+rDataList.size());

                                            if (rDataList != null) {

                                                for (int i = 0; i < rDataList.size(); i++) {
                                                    rData = rDataList.get(i);

                                                    List<Leg> lDataList = rData.getLegs();

                                                    for (int j = 0; j < lDataList.size(); j++) {
                                                        lData = lDataList.get(j);

                                                        Distance d = lData.getDistance();

                                                        distance = distance + d.getValue();

                                                        System.out.println("dist and value is " + d.getValue() + ":::" + distance);
                                                    }

                                                }

                                                distance = distance / 1000;
                                                finalDistance = distance;

                                                System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
                                                System.out.println(finalDistance + ":" + startingTime + ":" + endingTime + ":" + position + ":" + emp1OTP + ":" + emp2OTP + ":" + emp3OTP + ":" + emp4OTP);

                                                startingTime=dbAdapter.getInfo(stTripId,"starting_time");

                                                h.removeCallbacks(r);
                                                hDist.removeCallbacks(rDist);

                                                stopLocationUpdates();
                                                mGoogleApiClient.disconnect();

                                                Bundle b = new Bundle();
                                                b.putFloat("Result", finalDistance);
                                                b.putString("starting_time", startingTime);
                                                b.putString("ending_time", endingTime);
                                                b.putInt("position", position);

                                                // Intent j=new Intent(getActivity(),ResultActivity.class);
                                                Intent j = new Intent(RideActivity.this, ResultActivity.class);
                                                j.putExtras(b);
                                                j.putExtra("list", dutyList);
                                                j.putExtra("empList",(ArrayList) empPojoList);
                                                j.putExtra("position", position);
                                                j.putExtra("tripEmpList",(ArrayList)tripEmpPojoList);

                                                //j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                progressDialog.dismiss();
                                                startActivity(j);
                                                finish();
                                            } else {

                                            }

                                            //////////////////////
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<DistancePojo> call, Throwable t) {

                                    }
                                });


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RideActivity.this, "No Internet Connection..Try again!", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            alertDialog.dismiss();
                        }
                    });

                }

                else {
                    Toast.makeText(RideActivity.this,"Sorry, Cannot close the duty !\nAtleast one employee should be onBoard.",Toast.LENGTH_LONG).show();
                }

            }
        });

        //establishConnection();
    }

    public void establishConnection(){

        buildGoogleApiClient();
        buildLocationSettingsRequest();
        gettingLocationUpdates();
        storeCoordinatesForDistance();
        entered=true;
    }

    @Override
    protected void onStart() {

        super.onStart();

        //System.out.println("Google API client in start is "+mGoogleApiClient);
        // System.out.println("the enetred values in onStart is "+entered);

        if(Build.VERSION.SDK_INT>=23)
        {
            if(!entered)
            {
                // System.out.println("value of entered in 'if' "+entered);
            }
            else
            {
                if(staffDetails) {

                }
                else
                {
                    mGoogleApiClient.connect();
                }
            }
        }
        else
        {
            if(staffDetails) {

            }
            else
            {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    protected void onStop() {
        // System.out.println("Google API client in stop is "+mGoogleApiClient);

        if(mGoogleApiClient!=null) {

            if(staffDetails)
            {

            }
            else {
                mGoogleApiClient.disconnect();
            }
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // System.out.println("Google API client in pause is "+mGoogleApiClient);

        if(mGoogleApiClient!=null) {

            if (mGoogleApiClient.isConnected()) {

                if(staffDetails)
                {

                }
                else {
                    stopLocationUpdates();
                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // System.out.println("Google API client in resume is "+mGoogleApiClient);
        //System.out.println("the enetred values in onResume is "+entered);

        if(entered)
        {
            //System.out.println("connecting the googleclient in resume here..."+mGoogleApiClient.isConnected()+" "+mGoogleApiClient.isConnecting()+" "+entered);
            if(mGoogleApiClient.isConnecting()||mGoogleApiClient.isConnected())
            {
                // System.out.println("Google Client connectng in 'if' "+mGoogleApiClient.isConnecting());
            }
            else {
                //System.out.println("Google Client connectng in 'else' "+mGoogleApiClient.isConnecting());
                mGoogleApiClient.connect();
            }
        }

        if(mGoogleApiClient!=null) {

            // System.out.println("connections "+mGoogleApiClient.isConnected()+" "+mGoogleApiClient.isConnecting());

            if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {

                if(staffDetails)
                {
                    staffDetails=false;
                }
                else {
                    startLocationUpdates();
                }
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {

        if (mGoogleApiClient == null) {
            //System.out.println("in buildGoogleApiClient after 'if' ");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
    }

    protected void buildLocationSettingsRequest() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        //Location Settings Satisfied
                        startLocationUpdates();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            status.startResolutionForResult(RideActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to resolve it
                        break;
                }
            }
        });
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);//45 sec
        mLocationRequest.setFastestInterval(2000);//5 sec
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {

        //System.out.println("Location Updates Started..");
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,RideActivity.this);
            mRequestingLocationUpdates=true;
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    protected void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, RideActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                establishConnection();

            } else {
                Toast.makeText(RideActivity.this, "Permission not granted", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mapFragment.getMapAsync(this);
        if (mLastLocation == null) {
            try {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        if(mRequestingLocationUpdates)
        {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {


        if(mLastLocation==null)
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            lastLoc = new LatLng(latitude, longitude);
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String add1=addresses.get(0).getAddressLine(1);
                String add2=addresses.get(0).getAddressLine(2);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                complete_address=address+" "+add1+" "+add2;
                //tvCloc.setText(complete_address);
            }
            catch(IOException e)
            {e.printStackTrace();
                complete_address="No response from server";
                //tvCloc.setText(complete_address);
            }
            mMap.addMarker(new MarkerOptions().position(lastLoc)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.cab_image)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, 15));
            mMap.getUiSettings().setMapToolbarEnabled(false);
        }


        if(mLastLocation!=null) {

            System.out.println("location changed getting called "+ location.getLatitude()+location.getLongitude());

            if (location != null && location.hasAccuracy()) {
                System.out.println("location changed getting called "+ location.getLatitude()+"::"+location.getLongitude()+"::"+location.getAccuracy());


                if (location.getAccuracy() <= inAccurate) {

                    System.out.println("*********** loc changed ***************"+location.getLatitude()+"::::"+location.getLongitude());


                    if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {


                        current_lat = location.getLatitude();
                        current_long = location.getLongitude();

                        curntloc = new LatLng(current_lat, current_long);

                        if(first)
                        {
                            System.out.println("first ******************");
                            // editor.putString("pickup_lat",String.valueOf(current_lat));
                            //editor.putString("pickup_long",String.valueOf(current_long));
                            // editor.commit();
                            dbAdapter.insertInfo("pickup_lat",String.valueOf(current_lat),stTripId,getCurrentTime());
                            dbAdapter.insertInfo("pickup_long",String.valueOf(current_long),stTripId,getCurrentTime());
                            //currentTime=getCurrentTime();
                            Date date = new Date();
                            timeUpdated = dateFormat.format(date);
                            dbAdapter.insertEntry(stTripId,current_lat, current_long, complete_address,0, timeUpdated);
                            first=false;
                        }


                        // need to be implemented

                        /*
                        if(first)
                        {
                            // editor.putString("pickup_lat",String.valueOf(current_lat));
                            //editor.putString("pickup_long",String.valueOf(current_long));
                            // editor.commit();
                            dbAdapter.insertTimes("pickup_lat",String.valueOf(current_lat),stDSNo);
                            dbAdapter.insertTimes("pickup_long",String.valueOf(current_long),stDSNo);
                            first=false;
                        }
                        */


    // -------------------------------------------------


    /*
                    }

                }
            }
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        lastLoc = new LatLng(latitude, longitude);

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        if(car!=null) {
            car.setPosition(lastLoc);

        }
        else {

            car = mMap.addMarker(new MarkerOptions().position(lastLoc)
                    .title("Current Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.cab_image)));
        }
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(lastLoc)
                .zoom(16)
                //.bearing(30).tilt(45)
                .build()));
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        try{
            addresses = geocoder.getFromLocation(latitude,longitude, 1);
            int l=addresses.get(0).getMaxAddressLineIndex();
            String add="",add1="",add2="";

            for(int k=0;k<l;k++)
            {
                add=add+addresses.get(0).getAddressLine(k);
                add=add+" ";

                if(k==1)
                {
                    add1=addresses.get(0).getAddressLine(k);
                }
                if(k==2)
                {
                    add2=addresses.get(0).getAddressLine(k);
                }
            }
            tvCurrentLoc.setText(add);
        }
        catch (Exception e){e.printStackTrace();
            tvCurrentLoc.setText("Unable to get the location details");}

    }

    private void accelerateDecelerate()
    {
        final Handler handler = new Handler();

        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 5000;
        final boolean hideMarker = false;

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;

                cpLat=startPosition.latitude * (1 - t) + finalPosition.latitude * t;
                cpLng= startPosition.longitude * (1 - t) + finalPosition.longitude * t;

                currentPosition = new LatLng(cpLat,cpLng);

                car.setPosition(currentPosition);
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,16));

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        car.setVisible(false);
                    } else {
                        car.setVisible(true);
                    }
                }
            }
        });
    }

    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    private void rotateMarker(final Marker marker, final float toRotation) {
        if(!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 500;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    public void gettingLocationUpdates() {

        h = new Handler();
        r = new Runnable() {
            @Override
            public void run() {

                h.postDelayed(r,15000);
                //currentTime = java.text.DateFormat.getTimeInstance().format(new Date());

                System.out.println("1  @@@@########################################################################");

                try {
                    addresses = geocoder.getFromLocation(current_lat, current_long, 1);
                    if (addresses.size() != 0) {
                        int l = addresses.get(0).getMaxAddressLineIndex();
                        String add = "", add1 = "", add2 = "";

                        for (int k = 0; k < l; k++) {
                            add = add + addresses.get(0).getAddressLine(k);
                            add = add + " ";

                            if (k == 1) {
                                add1 = addresses.get(0).getAddressLine(k);
                            }
                            if (k == 2) {
                                add2 = addresses.get(0).getAddressLine(k);
                            }
                        }
                        String address = addresses.get(0).getAddressLine(0);
                        String add_1 = addresses.get(0).getAddressLine(1);//current place name eg:Nagendra nagar,Hyderabad
                        String add_2 = addresses.get(0).getAddressLine(2);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        //complete_address=address+" "+add1+" "+add2;
                        tvCurrentLoc.setText(add1 + " " + add2);
                        complete_address = add;
                    } else {
                        tvCurrentLoc.setText("-");
                        complete_address = "-";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    complete_address = "Unable to get the location details";
                    tvCurrentLoc.setText(complete_address);
                }

                if (lastLocDist != null && current_lat != 0.0 && current_long != 0.0 && lastLocDist.latitude != 0.0 && lastLocDist.longitude != 0.0)
                {

                    curntloc = new LatLng(current_lat, current_long);

                    System.out.println("moving car");
                    startPosition = car.getPosition();
                    finalPosition = new LatLng(current_lat, current_long);

                    double toRotation = bearingBetweenLocations(startPosition, finalPosition);
                    rotateMarker(car, (float) toRotation);

                    accelerateDecelerate();

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curntloc, 16));
                    mMap.getUiSettings().setMapToolbarEnabled(false);


                    System.out.println(dutyData.getTripId()+":"+dutyData.getDriverId()+":"+dutyData.getTripDate());
                    System.out.println(current_lat+"::"+current_long);

                    JsonObject v=new JsonObject();
                    v.addProperty("tripid",dutyData.getTripId());
                    v.addProperty("driverid",dutyData.getDriverId());
                    v.addProperty("tripdate",dutyData.getTripDate());
                    v.addProperty("gpstokms","");
                    v.addProperty("gpdtohrs","");
                    v.addProperty("gpsroutedetails","");
                    v.addProperty("driverdutystatus","A");
                    v.addProperty("longitude",current_long);
                    v.addProperty("latitude",current_lat);
                    v.addProperty("companyid",companyId);

                    Call<Pojo> call=REST_CLIENT.sendingUpdates(v);
                    call.enqueue(new Callback<Pojo>() {
                        @Override
                        public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                            if(response.isSuccessful())
                            {
                                Pojo res=response.body();

                                if(res.getMessage().equals("updated"))
                                {

                                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Hurray!!!!!!!@@@!");
                                }
                                else {
                                    System.out.println("Hhhhhhhhhhhhhhhhhh");
                                }
                            }
                            else {
                                System.out.println("Wwwwwwwwwwwwwww");
                            }
                        }

                        @Override
                        public void onFailure(Call<Pojo> call, Throwable t) {

                            Toast.makeText(RideActivity.this,"Searching Network ...",Toast.LENGTH_SHORT).show();

                        }
                    });
                }

                lastLocDist = new LatLng(current_lat,current_long);

            }
        };

        h.post(r);
    }

    public void storeCoordinatesForDistance()
    {

        hDist=new Handler();
        rDist=new Runnable() {
            @Override
            public void run() {

                System.out.println("2  ########################################################################");
                hDist.postDelayed(rDist,300000);//5 min ~ 300 sec

                Date date = new Date();
                timeUpdated = dateFormat.format(date);

                currentTime = getCurrentTime();

                if (lastLocDist != null && current_lat != 0.0 && current_long != 0.0 && lastLocDist.latitude != 0.0 && lastLocDist.longitude != 0.0) {

                    System.out.println(lastLocDist.latitude + ":" + lastLocDist.longitude);
                    System.out.println(current_lat + ":" + current_long);

                    dbAdapter.insertEntry(stTripId,current_lat,current_long,complete_address,0,timeUpdated);


                    // startPosition = marker.getPosition();
                    //  finalPosition = new LatLng(current_lat, current_long);

                    //  double toRotation = bearingBetweenLocations(startPosition, finalPosition);
                    //  rotateMarker(marker, (float) toRotation);

                    //  accelerateDecelerate();
                } else {

                    //dbAdapter.insertEntry(0.0, 0.0, complete_address, resDist, timeUpdated);

                }

                // lastLocDist = new LatLng(current_lat, current_long);


            }
        };
        hDist.post(rDist);
    }

    public static String getCurrentTime() {
        //date output format
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    */
}

