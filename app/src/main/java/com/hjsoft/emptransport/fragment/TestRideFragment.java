package com.hjsoft.emptransport.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
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
import com.hjsoft.emptransport.BuildConfig;
import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.activity.DutySummaryActivity;
import com.hjsoft.emptransport.activity.HomeActivity;
import com.hjsoft.emptransport.activity.ResultActivity;
import com.hjsoft.emptransport.adapter.DBAdapter;
import com.hjsoft.emptransport.model.Distance;
import com.hjsoft.emptransport.model.DistancePojo;
import com.hjsoft.emptransport.model.DutyData;
import com.hjsoft.emptransport.model.EmpData;
import com.hjsoft.emptransport.model.EmpPojo;
import com.hjsoft.emptransport.model.Leg;
import com.hjsoft.emptransport.model.Pojo;
import com.hjsoft.emptransport.model.Route;
import com.hjsoft.emptransport.model.SnapDistance;
import com.hjsoft.emptransport.model.SnappedPoint;
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
 * Created by hjsoft on 22/6/17.
 */
public class TestRideFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    View v;
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
    Handler h,hDist,hS;
    Runnable r,rDist,rS;
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
    float distance=0,finalDistance=0;
    TextView tvMyDuties,tvStaffDetails;
    boolean staffDetails=false;
    ImageButton ibDots;
    List<EmpPojo> empPojoList;
    EmpPojo empPojo;
    TripEmpPojo tripEmpPojo;
    List<TripEmpPojo> tripEmpPojoList;
    boolean flag=false;
    String companyId="CMP0001";
    String stTotalTime,stRouteDetails;
    TextView tvSaveDuty;
    String snapToRoadWaypoints="";
    ArrayList<com.hjsoft.emptransport.model.Location> a1=new ArrayList<>();
    ArrayList<com.hjsoft.emptransport.model.Location> a2=new ArrayList<>();
    ArrayList<com.hjsoft.emptransport.model.Location> a3=new ArrayList<>();
    String crntPickupLat,crntPickupLng;
    int iteration=1;
    boolean flagForFinishClicked=false;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SharedPref";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.activity_maps,container,false);

        REST_CLIENT= RestClient.get();
        dbAdapter=new DBAdapter(getActivity());
        dbAdapter=dbAdapter.open();

        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        dutyList=(ArrayList<DutyData>) getActivity().getIntent().getSerializableExtra("list");
        empPojoList=(ArrayList<EmpPojo>)getActivity().getIntent().getSerializableExtra("empList");
        tripEmpPojoList=(ArrayList<TripEmpPojo>)getActivity().getIntent().getSerializableExtra("tripEmpList");

        // System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!"+empPojoList.size()+tripEmpPojoList.size());

        pref = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        for(int i=0;i<empPojoList.size();i++)
        {
            empPojo=empPojoList.get(i);
            // System.out.println("111111111"+empPojo.getLocationname());
            tripEmpPojoList=empPojo.getTripEmpPojo();

            for(int j=0;j<tripEmpPojoList.size();j++)
            {
                tripEmpPojo=tripEmpPojoList.get(j);

                // System.out.println("222222"+tripEmpPojo.getEmployeename());
            }


            // System.out.println();
        }
        //empList=(ArrayList<EmpData>)getIntent().getSerializableExtra("empList");

        d=getActivity().getIntent().getExtras();

        position=d.getInt("position");
        dutyData=dutyList.get(position);

        btFinishDuty=(TextView)v.findViewById(R.id.am_tv_finish_duty);
        tvCurrentLoc=(TextView)v.findViewById(R.id.am_tv_place);
        tvMyDuties=(TextView)v.findViewById(R.id.am_tv_my_duties);
        tvStaffDetails=(TextView)v.findViewById(R.id.am_tv_staff_details);
        ibDots=(ImageButton)v.findViewById(R.id.am_ib_dots);
        tvSaveDuty=(TextView)v.findViewById(R.id.am_tv_save_duty);

        stTripId=dutyData.getTripId();

        if((dbAdapter.getInfo(stTripId,"starting_time").equals("")))
        {
            // System.out.println("++++++++++++++++++++++******+++++++++++++++++++++++++++++++++");
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
            if(getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                establishConnection();
            }
            else
            {
                if(shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    Toast.makeText(getActivity(),"Location Permission is required for this app to run!",Toast.LENGTH_LONG).show();
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

                Intent i=new Intent(getActivity(),HomeActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        ibDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                staffDetails=true;

                Intent i=new Intent(getActivity(),DutySummaryActivity.class);
                i.putExtra("list", dutyList);
                i.putExtra("position", position);
                i.putExtra("value","yes");
                startActivity(i);

            }
        });


        btFinishDuty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                flagForFinishClicked=true;
                getWaypointsTillNow(dbAdapter.getWaypointsForSnapToRoad(stTripId));

                // API : AIzaSyB7RFlSBoNyRCkuGMZo5i28yGZmu4GCHDI

                /*for(int i=0;i<empPojoList.size();i++)
                {
                    empPojo=empPojoList.get(i);
                    // System.out.println("111111111"+empPojo.getLocationname());
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


                // System.out.println("flag is "+flag);


                if(flag) {


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                    LayoutInflater inflater = getActivity().getLayoutInflater();
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

                            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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

                                finalDistance = distance;

                                // System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
                                // System.out.println(finalDistance + ":" + startingTime + ":" + endingTime + ":" + position + ":" + emp1OTP + ":" + emp2OTP + ":" + emp3OTP + ":" + emp4OTP);

                                startingTime = dbAdapter.getInfo(stTripId, "starting_time");
                                stRouteDetails = dbAdapter.getRouteDetails(stTripId);

                                /////////////////////////////////////////////

                                //SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
                                //timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

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
                                    stTotalTime = hFormatted + ":" + mFormatted + ":" + sFormatted;

                                    // System.out.println("Total time travelled is "+finalTimeTravelled);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                JsonObject v = new JsonObject();
                                v.addProperty("tripid", dutyData.getTripId());
                                v.addProperty("driverid", dutyData.getDriverId());
                                v.addProperty("tripdate", dutyData.getTripDate());
                                v.addProperty("gpstokms", finalDistance);
                                v.addProperty("gpdtohrs", stTotalTime);
                                v.addProperty("gpsroutedetails", stRouteDetails);
                                v.addProperty("driverdutystatus", "Y");
                                v.addProperty("longitude", "");
                                v.addProperty("latitude", "");
                                v.addProperty("companyid", companyId);

                                Call<Pojo> call1 = REST_CLIENT.sendingUpdates(v);
                                call1.enqueue(new Callback<Pojo>() {
                                    @Override
                                    public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                                        if (response.isSuccessful()) {

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
                                            Intent j = new Intent(getActivity(), ResultActivity.class);
                                            j.putExtras(b);
                                            j.putExtra("list", dutyList);
                                            j.putExtra("empList", (ArrayList) empPojoList);
                                            j.putExtra("position", position);
                                            j.putExtra("tripEmpList", (ArrayList) tripEmpPojoList);

                                            //j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            progressDialog.dismiss();
                                            startActivity(j);
                                            getActivity().finish();


                                            //empDetails(0,0);

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Pojo> call, Throwable t) {

                                        progressDialog.dismiss();
                                        tvSaveDuty.setVisibility(View.VISIBLE);
                                        pickupLat = "No";
                                        pickupLong = "No";
                                        dropLat = "No";
                                        dropLong = "No";

                                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                                    }
                                });


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "No Internet Connection..Try again!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(),"Sorry, Cannot close the duty !\nAtleast one employee should be onBoard.",Toast.LENGTH_LONG).show();
                }*/

            }
        });


        tvSaveDuty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println(stTripId+":"+dutyData.getDriverId()+":"+dutyData.getTripDate()+":"+dutyData.getRouteName()+":"+finalDistance+":"+totalTime+":"+stRouteDetails+":"+pickupLat+":"+pickupLong+":"+dropLat+":"+dropLong);

                dbAdapter.insertJourneyData(stTripId,dutyData.getDriverId(),dutyData.getTripDate(),dutyData.getRouteName(),String.valueOf(finalDistance),totalTime,stRouteDetails,pickupLat,pickupLong,dropLat,dropLong);

                stopLocationUpdates();
                mGoogleApiClient.disconnect();
                getActivity().finish();

            }
        });



        return  v;
    }


    public void establishConnection(){

        buildGoogleApiClient();
        buildLocationSettingsRequest();
        gettingLocationUpdates();
        storeCoordinatesForDistance();
        entered=true;
    }

    @Override
    public void onStart() {

        super.onStart();

        if(Build.VERSION.SDK_INT>=23)
        {
            if(!entered)
            {

            }
            else
            {
                if(staffDetails) {

                }
                else
                {
                    //mGoogleApiClient.connect();
                }
            }
        }
        else
        {
            if(staffDetails) {

            }
            else
            {
                //mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onStop() {

        if(mGoogleApiClient!=null) {

            if(staffDetails)
            {

            }
            else {
                // mGoogleApiClient.disconnect();
            }
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mGoogleApiClient!=null) {

            if (mGoogleApiClient.isConnected()) {

                if(staffDetails)
                {

                }
                else {
                    // stopLocationUpdates();
                }

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(entered)
        {
            if(mGoogleApiClient.isConnecting()||mGoogleApiClient.isConnected())
            {

            }
            else {

                mGoogleApiClient.connect();
            }
        }

        if(mGoogleApiClient!=null) {

            if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {

                startLocationUpdates();

                if(staffDetails)
                {
                    staffDetails=false;
                }
                else {
                    //startLocationUpdates();
                }
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {

        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
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
                            status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
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
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
            mRequestingLocationUpdates=true;
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    protected void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                establishConnection();

            } else {
                Toast.makeText(getActivity(), "Permission not granted", Toast.LENGTH_LONG).show();
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

            // System.out.println("location changed getting called "+ location.getLatitude()+location.getLongitude());

            if (location != null && location.hasAccuracy()) {
                // System.out.println("location changed getting called "+ location.getLatitude()+"::"+location.getLongitude()+"::"+location.getAccuracy());


                if (location.getAccuracy() <= inAccurate) {

                    System.out.println("*********** loc changed ***************"+location.getLatitude()+"::::"+location.getLongitude());


                    if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {


                        current_lat = location.getLatitude();
                        current_long = location.getLongitude();

                        curntloc = new LatLng(current_lat, current_long);

                        if(first)
                        {
                            // System.out.println("first ******************");
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

                h.postDelayed(r,30000);
                //currentTime = java.text.DateFormat.getTimeInstance().format(new Date());

                // System.out.println("1  @@@@########################################################################");

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

                    // System.out.println("moving car");
                    startPosition = car.getPosition();
                    finalPosition = new LatLng(current_lat, current_long);

                    double toRotation = bearingBetweenLocations(startPosition, finalPosition);
                    rotateMarker(car, (float) toRotation);

                    accelerateDecelerate();

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curntloc, 16));
                    mMap.getUiSettings().setMapToolbarEnabled(false);


                    // System.out.println(dutyData.getTripId()+":"+dutyData.getDriverId()+":"+dutyData.getTripDate());
                    // System.out.println(current_lat+"::"+current_long);

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

                                    // System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Hurray!!!!!!!@@@!");
                                }
                                else {
                                    // System.out.println("Hhhhhhhhhhhhhhhhhh");
                                }
                            }
                            else {
                                // System.out.println("Wwwwwwwwwwwwwww");
                            }
                        }

                        @Override
                        public void onFailure(Call<Pojo> call, Throwable t) {

                            Toast.makeText(getActivity(),"Searching Network ...",Toast.LENGTH_SHORT).show();

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

                // System.out.println("2  ########################################################################");
                hDist.postDelayed(rDist,300000);//5 min ~ 300 sec

                Date date = new Date();
                timeUpdated = dateFormat.format(date);

                currentTime = getCurrentTime();

                if (lastLocDist != null && current_lat != 0.0 && current_long != 0.0 && lastLocDist.latitude != 0.0 && lastLocDist.longitude != 0.0) {

                    // System.out.println(lastLocDist.latitude + ":" + lastLocDist.longitude);
                    // System.out.println(current_lat + ":" + current_long);

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

    public void storeCoordinatesForSnapToRoad()
    {
        hS=new Handler();
        rS=new Runnable() {
            @Override
            public void run() {

                hS.postDelayed(rS,60000);

                //rideCurrentTime = java.text.DateFormat.getTimeInstance().format(new Date());

                Date date = new Date();
                timeUpdated=dateFormat.format(date);

                if(current_lat!=0.0 && current_long!=0.0)
                {
                    dbAdapter.insertLatLngEntry(stTripId,current_lat,current_long,timeUpdated);
                }

                if(dbAdapter.getWaypointsCount(stTripId)>=100)
                {
                    //System.out.println("Waypoints for snap to road are.."+dbAdapter.getWaypointsForSnapToRoad(stDSNo));


                    getWaypointsTillNow(dbAdapter.getWaypointsForSnapToRoad(stTripId));
                    //dbAdapter.deleteLatLng(stDSNo);
                    //System.out.println("Waypoint count after delete.."+dbAdapter.getWaypointsCount(stDSNo));



                }

            }
        };

        hS.post(rS);
    }

    public static String getCurrentTime() {
        //date output format
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //stopLocationUpdates();
        // mGoogleApiClient.disconnect();

        if(h!=null)
        {
            h.removeCallbacks(r);
        }

        if(hDist!=null)
        {
            hDist.removeCallbacks(rDist);
        }
    }


    public void getWaypointsTillNow(String path)
    {
        String urlString="https://roads.googleapis.com/v1/snapToRoads?path="+path+"&interpolate=true&key="+ BuildConfig.SNAP_TO_ROAD;

        //System.out.println("urlSTribg issss "+urlString);
        dbAdapter.insertLocUrl(stTripId,"SnapToRoadAPI:"+urlString);

        Call<SnapDistance>call=REST_CLIENT.getSnapToRoadDetails(urlString);
        call.enqueue(new Callback<SnapDistance>() {
            @Override
            public void onResponse(Call<SnapDistance> call, Response<SnapDistance> response) {

                SnapDistance sd;
                List<SnappedPoint> aL;
                ArrayList<SnappedPoint> aS=new ArrayList<>();
                SnappedPoint s;
                ArrayList<com.hjsoft.emptransport.model.Location> aLoc=new ArrayList<>();
                ArrayList<com.hjsoft.emptransport.model.Location> aNewLoc=new ArrayList<>();
                com.hjsoft.emptransport.model.Location loc;

                if(response.isSuccessful())
                {
                    sd=response.body();
                    aL=sd.getSnappedPoints();
                    snapToRoadWaypoints="";

                    //System.out.println("Location arraylist size"+aL.size());

                    for(int i=0;i< aL.size();i++)
                    {
                        s=aL.get(i);

                        aLoc.add(s.getLocation());

                        //System.out.println("Is s.originalIndex present..."+s.getOriginalIndex());

                        if(s.getOriginalIndex()!=null)
                        {
                            //System.out.println("Is s.originalIndex present inside..."+s.getOriginalIndex());
                            aNewLoc.add(s.getLocation());
                        }

                    }

                    //System.out.println("AL SIZES.."+aL.size()+"::"+aLoc.size()+"::"+aNewLoc.size());

                    /*int k;

                    if(aLoc.size()<=10)
                    {
                        k=1;
                    }
                    else if(aLoc.size()>10&&aLoc.size()<20)
                    {
                        k=2;
                    }
                    else {

                        k=aLoc.size()/10;

                    }



                    String stWaypoints="";

                    for(int i=0;(k*i)<aLoc.size();i++)
                    {
                        loc=aLoc.get(k*i);

                        if(i==0)
                        {
                            stWaypoints=loc.getLatitude()+","+loc.getLongitude();
                        }
                        else {
                            stWaypoints=stWaypoints+"|"+loc.getLatitude()+","+loc.getLongitude();
                        }
                    }*/

                    snapToRoadWaypoints=stWaypoints;
                    //dbAdapter.deleteLatLng(stDSNo);

                   /* if(flagForFinishClicked)
                    {
                        crntPickupLat=pref.getString("from_lat",crntPickupLat);
                        crntPickupLng=pref.getString("from_lng",crntPickupLng);

                        getFinalDistance(crntPickupLat,crntPickupLng,stWaypoints);
                    }
                    else {

                        crntPickupLat=pref.getString("from_lat",crntPickupLat);
                        crntPickupLng=pref.getString("from_lng",crntPickupLng);

                        getDistanceTillNow(crntPickupLat, crntPickupLng, stWaypoints);
                    }*/

                    getDistanceInIntervals(aNewLoc);
                }
                else {
                    Toast.makeText(getActivity(),"Please try again!"+response.message(),Toast.LENGTH_SHORT).show();
                    /*if(progressDialog!=null) {
                        progressDialog.dismiss();
                    }

                    if(alertDialog!=null) {
                        alertDialog.dismiss();
                    }*/
                }
            }

            @Override
            public void onFailure(Call<SnapDistance> call, Throwable t) {

                /*if(progressDialog!=null) {
                    progressDialog.dismiss();
                }

                if(alertDialog!=null) {
                    alertDialog.dismiss();
                }*/

                Toast.makeText(getActivity(), "Please check Internet connection!", Toast.LENGTH_SHORT).show();


            }
        });
    }

    public void getDistanceInIntervals(ArrayList<com.hjsoft.emptransport.model.Location> alist)
    {
        int n=alist.size()/3;

        a1.clear();
        a2.clear();
        a3.clear();

        //System.out.println("aList.size***"+alist.size());
        //System.out.println("n isss"+n);

        for(int i=0;i<alist.size();i++)
        {
            if(i<n)
            {
                a1.add(alist.get(i));
            }
            else if(i>=n&&i<=2*n)
            {
                if(i==n)
                {
                    a1.add(alist.get(i));
                }
                a2.add(alist.get(i));

                if(i==2*n)
                {
                    a3.add(alist.get(i));
                }
            }
            else {

                a3.add(alist.get(i));
            }

            /*if(i<n)
            {
                a1.add(alist.get(i));
            }
            else if(i>=n&&i<=2*n)
            {
                a2.add(alist.get(i));
            }
            else {
                a3.add(alist.get(i));
            }*/
        }

        /*System.out.println("a1.size***"+a1.size());
        System.out.println("a2.size***"+a2.size());
        System.out.println("a3.size***"+a3.size());*/

        calculateIterativeDistance(a1);



    }

    public void calculateIterativeDistance(ArrayList<com.hjsoft.emptransport.model.Location> a4)
    {
        if(a4.size()!=0) {

            String olat = "", olng = "", dlat = "", dlng = "", waypoints = "";
            int c=0;
            int last = a4.size();
            olat = String.valueOf(a4.get(0).getLatitude());
            olng = String.valueOf(a4.get(0).getLongitude());

            dlat = String.valueOf(a4.get(last - 1).getLatitude());
            dlng = String.valueOf(a4.get(last - 1).getLongitude());

            if(a4.size()<=19)
            {
                c=1;
            }
            else {
                c=2;
            }

            //System.out.println("c value is"+c);

            for (int i = 0; (c*i) < a4.size(); i++) {

                if (i == 0) {
                    waypoints = String.valueOf(a4.get(i).getLatitude()) + "," + String.valueOf(a4.get(i).getLongitude());
                } else {

                    waypoints = waypoints + "|" + String.valueOf(a4.get(c*i).getLatitude()) + "," + String.valueOf(a4.get(c*i).getLongitude());

                }
            }

            /*for (int i = 0; (2*i) < a4.size(); i++) {

                if (i == 0) {
                    waypoints = String.valueOf(a4.get(i).getLatitude()) + "," + String.valueOf(a4.get(i).getLongitude());
                } else {

                    waypoints = waypoints + "|" + String.valueOf(a4.get(2*i).getLatitude()) + "," + String.valueOf(a4.get(2*i).getLongitude());

                }
            }*/


            String urlString = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "origin=" + olat + "," + olng + "&destination=" + dlat + "," + dlng + "&waypoints=" + waypoints + "&key="+BuildConfig.DIRECTIONS;

            //System.out.println("Distance urlString iss " + urlString);

            //dbAdapter.insertLocUrl(stDSNo,"DirectionsMatrixAPI:"+urlString);

            Call<DistancePojo> call = REST_CLIENT.getOSDistanceDetails(urlString);
            call.enqueue(new Callback<DistancePojo>() {
                @Override
                public void onResponse(Call<DistancePojo> call, Response<DistancePojo> response) {

                    DistancePojo data;
                    Route rData;
                    Leg lData;
                    float dist = 0;

                    if (response.isSuccessful()) {
                        data = response.body();

                        // System.out.println(response.message() + "::" + response.code() + "::" + response.errorBody());

                        // System.out.println("status is " + data.getStatus());
                        List<Route> rDataList = data.getRoutes();
                        // System.out.println("Route size "+rDataList.size());

                        if (rDataList != null) {

                            for (int i = 0; i < rDataList.size(); i++) {
                                rData = rDataList.get(i);

                                List<Leg> lDataList = rData.getLegs();

                                for (int j = 0; j < lDataList.size(); j++) {
                                    lData = lDataList.get(j);

                                    Distance d = lData.getDistance();

                                    dist = dist + d.getValue();

                                    // System.out.println("dist and value is " + d.getValue() + ":::" + distance);
                                }

                            }

                            //System.out.println("dist isssssss "+dist);

                            float d = pref.getFloat("distance", 0);
                            //float d=dbAdapter.getDist(stDSNo);

                            d = d + dist;


                            //System.out.println("d value issss " + d);
                            editor.putFloat("distance", d);
                            editor.commit();

                            //dbAdapter.updateDist(stDSNo,d);

                            crntPickupLat = String.valueOf(current_lat);
                            crntPickupLng = String.valueOf(current_long);

                            editor.putString("from_lat", crntPickupLat);
                            editor.putString("from_lng", crntPickupLng);
                            editor.commit();

                            iteration++;

                            ///////////////////
                            if (iteration == 2) {
                                calculateIterativeDistance(a2);
                            }

                            if (iteration == 3) {

                                calculateIterativeDistance(a3);

                            }

                            if (iteration == 4) {
                                //System.out.println("D VALUE FINALLY ISS::" + d);
                                iteration = 1;

                                //System.out.println("***All Details***");
                                //System.out.println(stDSNo);
                                //System.out.println(dbAdapter.getIntervalWaypoints(stDSNo));
                                //System.out.println(dbAdapter.getLocUrl(stDSNo));
                                //System.out.println(pref.getFloat("distance", 0));
                                //System.out.println("************************");


                                //************************

                                JsonObject v=new JsonObject();
                                v.addProperty("tripId",stTripId);
                                v.addProperty("tripdetails",dbAdapter.getIntervalWaypoints(stTripId));
                                v.addProperty("distancepoints",dbAdapter.getLocUrl(stTripId));
                                v.addProperty("Totalkms",pref.getFloat("distance", 0));

                                Call<Pojo> call1=REST_CLIENT.sendDistanceForInterval(v);
                                call1.enqueue(new Callback<Pojo>() {
                                    @Override
                                    public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                                        if(response.isSuccessful())
                                        {
                                            dbAdapter.deleteLatLng(stTripId);
                                            dbAdapter.deleteLocUrl(stTripId);


                                            if (flagForFinishClicked) {
                                                finishClicked();
                                            }
                                            else {
                                                a1.clear();
                                                a2.clear();
                                                a3.clear();
                                            }
                                        }
                                        else {

                                            //System.out.println("in failure."+response.message());
                                        }



                                    }

                                    @Override
                                    public void onFailure(Call<Pojo> call, Throwable t) {

                                        Toast.makeText(getActivity(), "Issue in Internet connection!", Toast.LENGTH_SHORT).show();


                                    }
                                });




                                //**************************

                                /*dbAdapter.deleteLatLng(stTripId);

                                if (flagForFinishClicked) {
                                    finishClicked();
                                }
                                else {
                                    a1.clear();
                                    a2.clear();
                                    a3.clear();
                                }*/
                            }


                        } else {

                            //  Toast.makeText(MapsActivity.this,data.getStatus(),Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // System.out.println(response.message() + "::" + response.code() + "::" + response.isSuccessful());
                    }
                }

                @Override
                public void onFailure(Call<DistancePojo> call, Throwable t) {

                    //btStoreData.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please check Internet connection!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {

            iteration++;

            if (iteration == 2) {
                calculateIterativeDistance(a2);
            }

            if (iteration == 3) {
                calculateIterativeDistance(a3);
            }

            if (iteration == 4) {

                iteration = 1;

                /*System.out.println("***All Details***");
                System.out.println(stDSNo);
                System.out.println(dbAdapter.getIntervalWaypoints(stDSNo));
                System.out.println(dbAdapter.getLocUrl(stDSNo));
                System.out.println(pref.getFloat("distance", 0));
                System.out.println("************************");*/


                //************************

                JsonObject v=new JsonObject();
                v.addProperty("tripId",stTripId);
                v.addProperty("tripdetails",dbAdapter.getIntervalWaypoints(stTripId));
                v.addProperty("distancepoints",dbAdapter.getLocUrl(stTripId));
                v.addProperty("Totalkms",pref.getFloat("distance", 0));

                Call<Pojo> call1=REST_CLIENT.sendDistanceForInterval(v);
                call1.enqueue(new Callback<Pojo>() {
                    @Override
                    public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                        if(response.isSuccessful())
                        {
                            dbAdapter.deleteLatLng(stTripId);
                            dbAdapter.deleteLocUrl(stTripId);


                            if (flagForFinishClicked) {
                                finishClicked();
                            }
                            else {
                                a1.clear();
                                a2.clear();
                                a3.clear();
                            }
                        }
                        else {

                            //System.out.println("in failure."+response.message());
                        }



                    }

                    @Override
                    public void onFailure(Call<Pojo> call, Throwable t) {

                        Toast.makeText(getActivity(), "Issue in Internet connection!", Toast.LENGTH_SHORT).show();


                    }
                });


                //**************************

                /*dbAdapter.deleteLatLng(stTripId);

                if (flagForFinishClicked) {
                    finishClicked();
                }
                else {
                    a1.clear();
                    a2.clear();
                    a3.clear();
                }*/

            }
        }
    }

    public void finishClicked()
    {

        for(int i=0;i<empPojoList.size();i++)
        {
            empPojo=empPojoList.get(i);
            // System.out.println("111111111"+empPojo.getLocationname());
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


        // System.out.println("flag is "+flag);


        if(flag) {


            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
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

                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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

                                /*String urlString = "https://maps.googleapis.com/maps/api/directions/json?" +
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

                                            // System.out.println(response.message() + "::" + response.code() + "::" + response.errorBody());

                                            // System.out.println("status for directions calculation is " + data.getStatus());
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

                                                        // System.out.println("dist and value is " + d.getValue() + ":::" + distance);
                                                    }

                                                }



                                                distance = distance / 1000;*/
                        finalDistance = distance;

                        // System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
                        // System.out.println(finalDistance + ":" + startingTime + ":" + endingTime + ":" + position + ":" + emp1OTP + ":" + emp2OTP + ":" + emp3OTP + ":" + emp4OTP);

                        startingTime=dbAdapter.getInfo(stTripId,"starting_time");
                        stRouteDetails=dbAdapter.getRouteDetails(stTripId);

                        /////////////////////////////////////////////

                        //SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
                        //timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                        try {
                            Date date1 = timeFormat.parse(startingTime);
                            Date date2 = timeFormat.parse(endingTime);
                            //Date date3=timeFormat.parse(idle_time);

                            diff = (date2.getTime() - date1.getTime());
                            int Hours = (int) (diff/(1000 * 60 * 60));
                            int Mins = (int) (diff/(1000*60)) % 60;
                            long Secs = (int) (diff / 1000) % 60;

                            DecimalFormat formatter = new DecimalFormat("00");
                            String hFormatted = formatter.format(Hours);
                            String mFormatted = formatter.format(Mins);
                            String sFormatted = formatter.format(Secs);
                            stTotalTime=hFormatted+":"+mFormatted+":"+sFormatted;

                            // System.out.println("Total time travelled is "+finalTimeTravelled);

                        }catch (ParseException e) {e.printStackTrace();}

                        JsonObject v = new JsonObject();
                        v.addProperty("tripid", dutyData.getTripId());
                        v.addProperty("driverid", dutyData.getDriverId());
                        v.addProperty("tripdate", dutyData.getTripDate());
                        v.addProperty("gpstokms", finalDistance);
                        v.addProperty("gpdtohrs", stTotalTime);
                        v.addProperty("gpsroutedetails", stRouteDetails);
                        v.addProperty("driverdutystatus", "Y");
                        v.addProperty("longitude", "");
                        v.addProperty("latitude", "");
                        v.addProperty("companyid",companyId);

                        Call<Pojo> call1 = REST_CLIENT.sendingUpdates(v);
                        call1.enqueue(new Callback<Pojo>() {
                            @Override
                            public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                                if (response.isSuccessful()) {

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
                                    Intent j = new Intent(getActivity(), ResultActivity.class);
                                    j.putExtras(b);
                                    j.putExtra("list", dutyList);
                                    j.putExtra("empList",(ArrayList) empPojoList);
                                    j.putExtra("position", position);
                                    j.putExtra("tripEmpList",(ArrayList)tripEmpPojoList);

                                    //j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    progressDialog.dismiss();
                                    startActivity(j);
                                    getActivity().finish();


                                    //empDetails(0,0);

                                }
                            }

                            @Override
                            public void onFailure(Call<Pojo> call, Throwable t) {

                                progressDialog.dismiss();
                                tvSaveDuty.setVisibility(View.VISIBLE);
                                pickupLat="No";
                                pickupLong="No";
                                dropLat="No";
                                dropLong="No";

                                Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();

                            }
                        });







                        //////////////////////////////////////////////////

                        // code here
                        ////////////////////////////////
                                           /* } else {

                                                finalDistance=0;

                                                // System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
                                                // System.out.println(finalDistance + ":" + startingTime + ":" + endingTime + ":" + position + ":" + emp1OTP + ":" + emp2OTP + ":" + emp3OTP + ":" + emp4OTP);

                                                startingTime=dbAdapter.getInfo(stTripId,"starting_time");
                                                stRouteDetails=dbAdapter.getRouteDetails(stTripId);

                                                /////////////////////////////////////////////

                                                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
                                                timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                                                try {
                                                    Date date1 = timeFormat.parse(startingTime);
                                                    Date date2 = timeFormat.parse(endingTime);
                                                    //Date date3=timeFormat.parse(idle_time);

                                                    diff = (date2.getTime() - date1.getTime());
                                                    int Hours = (int) (diff/(1000 * 60 * 60));
                                                    int Mins = (int) (diff/(1000*60)) % 60;
                                                    long Secs = (int) (diff / 1000) % 60;

                                                    DecimalFormat formatter = new DecimalFormat("00");
                                                    String hFormatted = formatter.format(Hours);
                                                    String mFormatted = formatter.format(Mins);
                                                    String sFormatted = formatter.format(Secs);
                                                    stTotalTime=hFormatted+":"+mFormatted+":"+sFormatted;

                                                    // System.out.println("Total time travelled is "+finalTimeTravelled);

                                                }catch (ParseException e) {e.printStackTrace();}

                                                JsonObject v = new JsonObject();
                                                v.addProperty("tripid", dutyData.getTripId());
                                                v.addProperty("driverid", dutyData.getDriverId());
                                                v.addProperty("tripdate", dutyData.getTripDate());
                                                v.addProperty("gpstokms", finalDistance);
                                                v.addProperty("gpdtohrs", stTotalTime);
                                                v.addProperty("gpsroutedetails", stRouteDetails);
                                                v.addProperty("driverdutystatus", "Y");
                                                v.addProperty("longitude", "");
                                                v.addProperty("latitude", "");
                                                v.addProperty("companyid",companyId);

                                                Call<Pojo> call1 = REST_CLIENT.sendingUpdates(v);
                                                call1.enqueue(new Callback<Pojo>() {
                                                    @Override
                                                    public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                                                        if (response.isSuccessful()) {

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
                                                            Intent j = new Intent(getActivity(), ResultActivity.class);
                                                            j.putExtras(b);
                                                            j.putExtra("list", dutyList);
                                                            j.putExtra("empList",(ArrayList) empPojoList);
                                                            j.putExtra("position", position);
                                                            j.putExtra("tripEmpList",(ArrayList)tripEmpPojoList);

                                                            //j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            progressDialog.dismiss();
                                                            startActivity(j);
                                                            getActivity().finish();


                                                            //empDetails(0,0);

                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Pojo> call, Throwable t) {

                                                        progressDialog.dismiss();
                                                        pickupLat="No";
                                                        pickupLong="No";
                                                        dropLat="No";
                                                        dropLong="No";
                                                        tvSaveDuty.setVisibility(View.VISIBLE);

                                                        Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();

                                                    }
                                                });



                                            }

                                            //////////////////////
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<DistancePojo> call, Throwable t) {

                                        tvSaveDuty.setVisibility(View.VISIBLE);

                                        Toast.makeText(getActivity(),"No Internet Connection! Please Retry!",Toast.LENGTH_LONG).show();

                                    }
                                });*/


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "No Internet Connection..Try again!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(),"Sorry, Cannot close the duty !\nAtleast one employee should be onBoard.",Toast.LENGTH_LONG).show();
        }
        

    }
}
