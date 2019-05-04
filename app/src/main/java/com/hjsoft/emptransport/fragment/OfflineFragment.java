package com.hjsoft.emptransport.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.activity.HomeActivity;
import com.hjsoft.emptransport.adapter.DBAdapter;
import com.hjsoft.emptransport.model.Distance;
import com.hjsoft.emptransport.model.DistancePojo;
import com.hjsoft.emptransport.model.DutyData;
import com.hjsoft.emptransport.model.EmpData;
import com.hjsoft.emptransport.model.EmpPojo;
import com.hjsoft.emptransport.model.JourneyDetails;
import com.hjsoft.emptransport.model.Leg;
import com.hjsoft.emptransport.model.Pojo;
import com.hjsoft.emptransport.model.Route;
import com.hjsoft.emptransport.model.TripEmpPojo;
import com.hjsoft.emptransport.webservices.API;
import com.hjsoft.emptransport.webservices.RestClient;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 1/7/17.
 */
public class OfflineFragment extends Fragment {

    View view;
    TextView tvTripId,tvTripDate,tvTotalHrs,tvTotalKms,tvNextDuty,tvStartingTime,tvEndingTime,tvRoute;
    ArrayList<DutyData> dutyDataList;
    ArrayList<EmpData> empDataList;
    DutyData dutyData;
    EmpData empData;
    int position;
    Bundle b;
    API REST_CLIENT;
    DBAdapter dbAdapter;
    String stRouteDetails,stTripId,stTotalTime;
    long diff;
    List<EmpPojo> empPojoList;
    EmpPojo empPojo;
    TripEmpPojo tripEmpPojo;
    List<TripEmpPojo> tripEmpPojoList;
    boolean flag=false;
    ProgressDialog progressDialog;
    int i,j;
    String companyId="CMP0001";
    TextView tvRetry,tvSaveData;
    ArrayList<JourneyDetails> jDataList=new ArrayList<>();
    JourneyDetails jData;
    String stDistance;
    String pickupLat,pickupLng,dropLat,dropLng,stWaypoints;
    float distance=0,finalDistance;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.activity_offline_data,container,false);

        REST_CLIENT= RestClient.get();
        dbAdapter=new DBAdapter(getActivity());
        dbAdapter=dbAdapter.open();


        tvTripId=(TextView)view.findViewById(R.id.aod_tv_tripId);
        tvTripDate=(TextView)view.findViewById(R.id.aod_tv_tripDate);
        tvTotalKms=(TextView)view.findViewById(R.id.aod_tv_total_kms);
        tvTotalHrs=(TextView)view.findViewById(R.id.aod_tv_total_hrs);
        tvNextDuty=(TextView)view.findViewById(R.id.aod_tv_next_duty);
        tvStartingTime=(TextView)view.findViewById(R.id.aod_tv_startingTime);
        tvEndingTime=(TextView)view.findViewById(R.id.aod_tv_endingTime);
        tvRoute=(TextView)view.findViewById(R.id.aod_tv_route);
        tvRetry=(TextView)view.findViewById(R.id.aod_tv_retry);
        tvSaveData=(TextView)view.findViewById(R.id.aod_tv_save_data);


        dutyDataList=(ArrayList<DutyData>) getActivity().getIntent().getSerializableExtra("list");
        empPojoList=(ArrayList<EmpPojo>) getActivity().getIntent().getSerializableExtra("empList");
        tripEmpPojoList=(ArrayList<TripEmpPojo>)getActivity().getIntent().getSerializableExtra("tripEmpList");

        b=getActivity().getIntent().getExtras();
        position=b.getInt("position");

        dutyData=dutyDataList.get(position);
        //empData=empDataList.get(position);
        stTripId=dutyData.getTripId();

        tvTripId.setText(dutyData.getTripId());
        // tvTripDate.setText(dutyData.getTripDate());
        String s=dutyData.getTripDate();
        try {
            SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy");
            String datestring = s.split("T")[0];
            SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd");
            String reformattedStr = newformat.format(oldformat.parse(datestring));
            tvTripDate.setText(reformattedStr);

        }catch(ParseException e){e.printStackTrace();}

        jDataList=dbAdapter.getJourneyData(stTripId);
        jData=jDataList.get(0);

        tvTotalKms.setText(jData.getDistance());
        tvTotalHrs.setText(jData.getTime());
        tvRoute.setText(dutyData.getRouteName());
        pickupLat=jData.getPickupLat();
        pickupLng=jData.getPickupLng();
        dropLat=jData.getDropLat();
        dropLng=jData.getDropLng();



        stDistance=jData.getDistance();
        stTotalTime=jData.getTime();

        System.out.println("_________________________**********_____________________________________________________");
        System.out.println(pickupLat+">"+pickupLng+">"+dropLat+">"+dropLng+">"+stDistance+">"+stTotalTime);

        stRouteDetails=dbAdapter.getRouteDetails(stTripId);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        if(pickupLat.equals("No")||pickupLng.equals("No"))
        {
            retryData();
        }
        else {


            stWaypoints = dbAdapter.getWaypoints(stTripId);
            String urlString = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "origin=" + pickupLat + "," + pickupLng + "&destination=" + dropLat + "," + dropLng + "&waypoints=" + stWaypoints + "&key=AIzaSyA5f0AhduhPgOWHWC9tGgzE_teAC_it5d4";

            // System.out.println(urlString);

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
                            }

                            distance = distance / 1000;
                            stDistance = String.valueOf(distance);
                            System.out.println("distance sis "+stDistance);
                            retryData();

                    }

                }

                @Override
                public void onFailure(Call<DistancePojo> call, Throwable t) {

                }
            });


        }



        // empDetails(0,0);


        // System.out.println(dutyData.getTripId()+"::::"+dutyData.getTripDate()+"::::"+dutyData.getDriverId());
        // System.out.println(stDistance+":"+stTotalTime+":"+stRouteDetails);

        //sendEmpDetails();



/*

        JsonObject v = new JsonObject();
        v.addProperty("tripid", dutyData.getTripId());
        v.addProperty("driverid", dutyData.getDriverId());
        v.addProperty("tripdate", dutyData.getTripDate());
        v.addProperty("gpstokms", stDistance);
        v.addProperty("gpdtohrs", stTotalTime);
        v.addProperty("gpsroutedetails", stRouteDetails);
        v.addProperty("driverdutystatus", "Y");
        v.addProperty("longitude", "");
        v.addProperty("latitude", "");
        v.addProperty("companyid",companyId);

        Call<Pojo> call = REST_CLIENT.sendingUpdates(v);
        call.enqueue(new Callback<Pojo>() {
            @Override
            public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                if (response.isSuccessful()) {



                    // sendEmpDetails();

                    empDetails(0,0);

                }
            }

            @Override
            public void onFailure(Call<Pojo> call, Throwable t) {

                progressDialog.dismiss();
                tvRetry.setVisibility(View.VISIBLE);
                //tvSaveData.setVisibility(View.VISIBLE);

                Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();

            }
        });

*/
        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                retryData();
            }
        });

        tvSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbAdapter.insertJourneyData(stTripId,dutyData.getDriverId(),dutyData.getTripDate(),dutyData.getRouteName(),String.valueOf(stDistance),stTotalTime,stRouteDetails,pickupLat,pickupLng,dropLat,dropLng);

                getActivity().finish();
            }
        });



        tvNextDuty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent j=new Intent(getActivity(),HomeActivity.class);
                startActivity(j);
                getActivity().finish();
            }
        });


        return  view;
    }


    public void retryData()
    {
        JsonObject v = new JsonObject();
        v.addProperty("tripid", dutyData.getTripId());
        v.addProperty("driverid", dutyData.getDriverId());
        v.addProperty("tripdate", dutyData.getTripDate());
        v.addProperty("gpstokms", Float.parseFloat(stDistance));
        v.addProperty("gpdtohrs", stTotalTime);
        v.addProperty("gpsroutedetails", stRouteDetails);
        v.addProperty("driverdutystatus", "Y");
        v.addProperty("longitude", "");
        v.addProperty("latitude", "");
        v.addProperty("companyid",companyId);

        Call<Pojo> call = REST_CLIENT.sendingUpdates(v);
        call.enqueue(new Callback<Pojo>() {
            @Override
            public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                if (response.isSuccessful()) {

                    if(dbAdapter.findJourneyData(stTripId))
                    {
                        dbAdapter.deleteJourenyInfo(stTripId);
                    }

                    empDetails(0,0);

                }
            }

            @Override
            public void onFailure(Call<Pojo> call, Throwable t) {

                progressDialog.dismiss();

                Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void empDetails(int a,int b)
    {
        i=a;
        j=b;

        if(i<empPojoList.size())
        {
            empPojo=empPojoList.get(i);
            tripEmpPojoList=empPojo.getTripEmpPojo();

            if(j<tripEmpPojoList.size())
            {
                tripEmpPojo=tripEmpPojoList.get(j);

                String stValidation=tripEmpPojo.getEmployeename()+"@"+tripEmpPojo.getMobileno()+"#"+tripEmpPojo.getEmptimein();

                // System.out.println("DBadpterrrrrr respons eis "+dbAdapter.getInfo(stTripId,stValidation));

                if((dbAdapter.getInfo(stTripId,stValidation)).equals(""))
                {

                     System.out.println("timestamp for absentees is "+dbAdapter.getInfo(stTripId,i+"*"+empPojo.getLocationname()));
                    JsonObject v=new JsonObject();
                    v.addProperty("tripid",stTripId);
                    v.addProperty("EmpIds",tripEmpPojo.getEmployeeid());
                    v.addProperty("usedstatus","not used");
                    v.addProperty("timein",dbAdapter.getInfo(stTripId,i+"*"+empPojo.getLocationname()));
                    v.addProperty("companyid",companyId);

                    Call<Pojo> call1=REST_CLIENT.sendEmpValidation(v);
                    call1.enqueue(new Callback<Pojo>() {
                        @Override
                        public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                            if(response.isSuccessful())
                            {
                                if((i==(empPojoList.size()-1))&&(j==(tripEmpPojoList.size()-1))) {
                                    progressDialog.dismiss();
                                   System.out.println("end of all records");

                                    dbAdapter.deleteJourenyInfo(stTripId);
                                    dbAdapter.deleteInfo(stTripId);
                                    dbAdapter.deleteLatLngDetails(stTripId);
                                    Toast.makeText(getActivity(), "Duty completed successfully !", Toast.LENGTH_LONG).show();
                                }

                                j=j+1;
                                empDetails(i,j);
                            }

                        }

                        @Override
                        public void onFailure(Call<Pojo> call, Throwable t) {


                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Please check Internet connection !",Toast.LENGTH_LONG).show();



                        }
                    });



                }
                else {

                     System.out.println(":::::::::::::::@@@:::::::::::::::::::::::::::::::::::::");
                     System.out.println(stTripId+":"+tripEmpPojo.getEmployeeid()+":"+dbAdapter.getInfoTime(stTripId,stValidation));

                    // System.out.println("actual timestamp is "+dbAdapter.getInfoTime(stTripId,stValidation));
                   System.out.println("timestamp for presentees is "+dbAdapter.getInfo(stTripId,i+"*"+empPojo.getLocationname()));

                    JsonObject v=new JsonObject();
                    v.addProperty("tripid",stTripId);
                    v.addProperty("EmpIds",tripEmpPojo.getEmployeeid());
                    v.addProperty("usedstatus","used");
                    v.addProperty("timein",dbAdapter.getInfoTime(stTripId,stValidation));
                    v.addProperty("companyid",companyId);

                    Call<Pojo> call1=REST_CLIENT.sendEmpValidation(v);
                    call1.enqueue(new Callback<Pojo>() {

                        @Override
                        public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                            if(response.isSuccessful()) {



//                                dbAdapter.deleteInfo(stTripId);
//                                dbAdapter.deleteLatLngDetails(stTripId);

                                // System.out.println("i and j is "+i+"::::"+j+":"+empPojoList.size()+":"+tripEmpPojoList.size());

                                if((i==(empPojoList.size()-1))&&(j==(tripEmpPojoList.size()-1))) {
                                    progressDialog.dismiss();
                                   System.out.println("end of all records");
                                    dbAdapter.deleteJourenyInfo(stTripId);
                                    dbAdapter.deleteInfo(stTripId);
                                    dbAdapter.deleteLatLngDetails(stTripId);
                                    Toast.makeText(getActivity(), "Duty completed successfully !", Toast.LENGTH_LONG).show();
                                }

                                j=j+1;
                                empDetails(i,j);
                            }
                            else {
                                // System.out.println("error is "+response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<Pojo> call, Throwable t) {

                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Please check Internet connection !",Toast.LENGTH_LONG).show();

                        }
                    });


                }
            }
            else {

                i=i+1;

                empDetails(i,0);

            }
        }
    }
}

