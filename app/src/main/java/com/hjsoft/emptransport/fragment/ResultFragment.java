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
import com.hjsoft.emptransport.model.DutyData;
import com.hjsoft.emptransport.model.EmpData;
import com.hjsoft.emptransport.model.EmpPojo;
import com.hjsoft.emptransport.model.Pojo;
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
 * Created by hjsoft on 22/6/17.
 */
public class ResultFragment extends Fragment {

    View view;
    TextView tvTripId,tvTripDate,tvTotalHrs,tvTotalKms,tvNextDuty,tvStartingTime,tvEndingTime,tvRoute;
    ArrayList<DutyData> dutyDataList;
    ArrayList<EmpData> empDataList;
    DutyData dutyData;
    EmpData empData;
    int position;
    Bundle b;
    String stStartingTime,stEndingTime;
    float stDistance;
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.activity_result,container,false);

        REST_CLIENT= RestClient.get();
        dbAdapter=new DBAdapter(getActivity());
        dbAdapter=dbAdapter.open();


        tvTripId=(TextView)view.findViewById(R.id.ar_tv_tripId);
        tvTripDate=(TextView)view.findViewById(R.id.ar_tv_tripDate);
        tvTotalKms=(TextView)view.findViewById(R.id.ar_tv_total_kms);
        tvTotalHrs=(TextView)view.findViewById(R.id.ar_tv_total_hrs);
        tvNextDuty=(TextView)view.findViewById(R.id.ar_tv_next_duty);
        tvStartingTime=(TextView)view.findViewById(R.id.ar_tv_startingTime);
        tvEndingTime=(TextView)view.findViewById(R.id.ar_tv_endingTime);
        tvRoute=(TextView)view.findViewById(R.id.ar_tv_route);
        tvRetry=(TextView)view.findViewById(R.id.ar_tv_retry);
        tvSaveData=(TextView)view.findViewById(R.id.ar_tv_save_data);

        dutyDataList=(ArrayList<DutyData>) getActivity().getIntent().getSerializableExtra("list");
        empPojoList=(ArrayList<EmpPojo>) getActivity().getIntent().getSerializableExtra("empList");
        tripEmpPojoList=(ArrayList<TripEmpPojo>)getActivity().getIntent().getSerializableExtra("tripEmpList");

        b=getActivity().getIntent().getExtras();
        position=b.getInt("position");
        stStartingTime=b.getString("starting_time");
        stEndingTime=b.getString("ending_time");
        stDistance=b.getFloat("Result");

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));


        try {
            Date date1 = timeFormat.parse(stStartingTime);
            Date date2 = timeFormat.parse(stEndingTime);
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
        tvTotalKms.setText(String.valueOf(stDistance));
        tvTotalHrs.setText(stTotalTime);
        tvStartingTime.setText(stStartingTime);
        tvEndingTime.setText(stEndingTime);
        tvRoute.setText(dutyData.getRouteName());

        stRouteDetails=dbAdapter.getRouteDetails(stTripId);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

       empDetails(0,0);


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

                    /*

                    String stEmpData="";

                    if(emp1OTP)
                    {
                        empData=empDataList.get(0);
                        stEmpData=empData.getEmpId();
                    }

                    if(emp2OTP)
                    {
                        empData=empDataList.get(1);
                        if(stEmpData.equals("")) {
                            stEmpData = empData.getEmpId();
                        }
                        else {
                            stEmpData=stEmpData+","+empData.getEmpId();
                        }
                    }

                    if(emp3OTP)
                    {
                        empData=empDataList.get(2);
                        if(stEmpData.equals("")) {
                            stEmpData = empData.getEmpId();
                        }
                        else {
                            stEmpData=stEmpData+","+empData.getEmpId();
                        }
                    }

                    if(emp4OTP)
                    {
                        empData=empDataList.get(3);
                        if(stEmpData.equals("")) {
                            stEmpData =empData.getEmpId();
                        }
                        else {
                            stEmpData=stEmpData+","+empData.getEmpId();
                        }
                    }

                    if(emp5OTP)
                    {
                        empData=empDataList.get(4);
                        if(stEmpData.equals("")) {
                            stEmpData =empData.getEmpId();
                        }
                        else {
                            stEmpData=stEmpData+","+empData.getEmpId();
                        }
                    }

                    if(emp6OTP)
                    {
                        empData=empDataList.get(5);
                        if(stEmpData.equals("")) {
                            stEmpData =empData.getEmpId();
                        }
                        else {
                            stEmpData=stEmpData+","+empData.getEmpId();
                        }
                    }

                    if(emp7OTP)
                    {
                        empData=empDataList.get(6);
                        if(stEmpData.equals("")) {
                            stEmpData =empData.getEmpId();
                        }
                        else {
                            stEmpData=stEmpData+","+empData.getEmpId();
                        }
                    }


                    System.out.println("*******************************************************");
                    System.out.println("Employee Validation string is "+stEmpData);


                    JsonObject j=new JsonObject();
                    j.addProperty("tripid",dutyData.getTripId());
                    j.addProperty("EmpIds",stEmpData);

                    Call<Pojo> call1=REST_CLIENT.sendEmpValidation(j);
                    call1.enqueue(new Callback<Pojo>() {
                        @Override
                        public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                            progressDialog.dismiss();

                            if(response.isSuccessful())
                            {
                                dbAdapter.deleteInfo(stTripId);
                                dbAdapter.deleteLatLngDetails(stTripId);
                                Toast.makeText(ResultActivity.this,"Duty completed successfully!",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Pojo> call, Throwable t) {

                            progressDialog.dismiss();

                            Toast.makeText(ResultActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();

                        }
                    });
                    */

        //finish();
        //  Toast.makeText(ResultActivity.this,"Duty successfully completed!",Toast.LENGTH_SHORT).show();

           /*    }
            }

            @Override
            public void onFailure(Call<Pojo> call, Throwable t) {

                progressDialog.dismiss();
                tvRetry.setVisibility(View.VISIBLE);
                tvSaveData.setVisibility(View.VISIBLE);

                Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();

            }
        });*/

        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // retryData();
                empDetails(i,j);
            }
        });

        tvSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbAdapter.insertJourneyData(stTripId,dutyData.getDriverId(),dutyData.getTripDate(),dutyData.getRouteName(),String.valueOf(stDistance),stTotalTime,stRouteDetails,"No","No","No","No");

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

                            tvRetry.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Please check Internet connection !",Toast.LENGTH_LONG).show();



                        }
                    });



                }
                else {

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




    public  void sendEmpDetails()
    {
        for(i=0;i<empPojoList.size();i++)
        {
            empPojo=empPojoList.get(i);
            // System.out.println("111111111"+empPojo.getLocationname());
            tripEmpPojoList=empPojo.getTripEmpPojo();



            for(j=0;j<tripEmpPojoList.size();j++)
            {
                tripEmpPojo=tripEmpPojoList.get(j);

                String stValidation=tripEmpPojo.getEmployeename()+"@"+tripEmpPojo.getMobileno()+"#"+tripEmpPojo.getEmptimein();

                // System.out.println("DBadpter respons eis "+dbAdapter.getInfo(stTripId,stValidation));

                if((dbAdapter.getInfo(stTripId,stValidation)).equals(""))
                {


                }
                else {

                    // System.out.println("::::::::::::::::@@@::::::::::::::::::::::::::::::::::::");
                    // System.out.println(stTripId+":"+tripEmpPojo.getEmployeeid()+":"+dbAdapter.getInfoTime(stTripId,stValidation));

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

                                progressDialog.dismiss();

//                                dbAdapter.deleteInfo(stTripId);
//                                dbAdapter.deleteLatLngDetails(stTripId);

                                // System.out.println("i and j is "+i+"::::"+j+":"+empPojoList.size()+":"+tripEmpPojoList.size());

                                if((i==(empPojoList.size()-1))&&(j==(tripEmpPojoList.size()-1))) {
                                    // System.out.println("end of all records");
                                    dbAdapter.deleteInfo(stTripId);
                                    dbAdapter.deleteLatLngDetails(stTripId);
                                    Toast.makeText(getActivity(), "Duty completed successfully !", Toast.LENGTH_LONG).show();
                                }
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
        }
    }
}
