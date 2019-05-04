package com.hjsoft.emptransport.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.activity.TripEmpActivity;
import com.hjsoft.emptransport.adapter.AllRidesRecyclerAdapter;
import com.hjsoft.emptransport.model.AllTripsData;
import com.hjsoft.emptransport.model.AllTripsPojo;
import com.hjsoft.emptransport.webservices.API;
import com.hjsoft.emptransport.webservices.RestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 22/6/17.
 */
public class AllRidesFragment extends Fragment {

    AllRidesRecyclerAdapter mAdapter;
    RecyclerView rView;
    API REST_CLIENT;
    String stCompanyId = "CMP0001";
    ArrayList<AllTripsData> dataList=new ArrayList<>();
    AllTripsData data;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sp";
    String stDriverId;
    TextView tvFrom, tvTo;
    LinearLayout llFrom,llTo;
    int day, mnth, yr;
    String stDate, stFrom, stTo;
    View v;
    TextView tvOk,tvNoDuties;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_all_rides, container, false);

        tvFrom = (TextView) v.findViewById(R.id.aar_tv_from);
        tvTo = (TextView) v.findViewById(R.id.aar_tv_to);

        tvOk=(TextView)v.findViewById(R.id.aar_tv_ok);
        rView=(RecyclerView)v.findViewById(R.id.aar_rv_list);
        llFrom=(LinearLayout)v.findViewById(R.id.aar_ll_from);
        llTo=(LinearLayout)v.findViewById(R.id.aar_ll_to);
        tvNoDuties=(TextView)v.findViewById(R.id.aar_tv_no_duties);

        REST_CLIENT = RestClient.get();
        pref = getActivity().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        stDriverId = pref.getString("driverId", null);

        Date d=new Date();

        SimpleDateFormat newformat = new SimpleDateFormat("MM/dd/yyy");

        String reformattedStr = newformat.format(d);
        //holder.tvTripDate.setText(reformattedStr);
        tvFrom.setText(reformattedStr);
        tvTo.setText(reformattedStr);
        stFrom=reformattedStr;
        stTo=reformattedStr;
        getAllRides();


        llFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.alert_date, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);

                Button ok = (Button) dialogView.findViewById(R.id.ad_bt_ok);
                ImageView cancel = (ImageView) dialogView.findViewById(R.id.ad_bt_cancel);
                DatePicker dp = (DatePicker) dialogView.findViewById(R.id.datePicker);

                long now = System.currentTimeMillis() - 1000;

                //dp.setMinDate(now);
                //dp.setMaxDate(now + (1000 * 60 * 60 * 24 * 2));

                day = dp.getDayOfMonth();
                mnth = dp.getMonth() + 1;
                yr = dp.getYear();

                //System.out.println("date is " + day + ":" + mnth + ":" + yr);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                dp.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        day = dayOfMonth;
                        mnth = month + 1;
                        yr = year;

                        //System.out.println("date is " + day + ":::" + mnth + ":::" + yr);
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // stDate = day + "/" + mnth + "/" + yr;
                        stDate=mnth+"/"+day+"/"+yr;
                        tvFrom.setText(stDate);
                        stFrom = stDate;
                        alertDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });
            }
        });

        llTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.alert_date, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);

                Button ok = (Button) dialogView.findViewById(R.id.ad_bt_ok);
                ImageView cancel = (ImageView) dialogView.findViewById(R.id.ad_bt_cancel);
                DatePicker dp = (DatePicker) dialogView.findViewById(R.id.datePicker);

                long now = System.currentTimeMillis() - 1000;

                //dp.setMinDate(now);
                //dp.setMaxDate(now + (1000 * 60 * 60 * 24 * 2));

                day = dp.getDayOfMonth();
                mnth = dp.getMonth() + 1;
                yr = dp.getYear();

                //System.out.println("date is " + day + ":" + mnth + ":" + yr);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                dp.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        day = dayOfMonth;
                        mnth = month + 1;
                        yr = year;

                        //System.out.println("date is " + day + ":::" + mnth + ":::" + yr);
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // stDate = day + "/" + mnth + "/" + yr;
                        stDate=mnth+"/"+day+"/"+yr;
                        tvTo.setText(stDate);
                        stTo = stDate;
                        alertDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });


            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getAllRides();
            }
        });




        return v;

    }


//    @Override
//    public void onMethodCallback(final int position, final ArrayList<AllTripsData> data) {
//
//
//        Intent i=new Intent(AllRidesActivity.this,TripEmpActivity.class);
//        i.putExtra("position", position);
//        i.putExtra("list",dataList);
//        startActivity(i);
//}


    public void getAllRides()
    {
        tvNoDuties.setVisibility(View.GONE);
        dataList.clear();


        // System.out.println("Error i s*****************"+stCompanyId+"::"+stDriverId+":"+stFrom+":"+stTo);

        Call<List<AllTripsPojo>> call = REST_CLIENT.getAllTrips(stCompanyId, stDriverId, stFrom, stTo);
        call.enqueue(new Callback<List<AllTripsPojo>>() {
            @Override
            public void onResponse(Call<List<AllTripsPojo>> call, Response<List<AllTripsPojo>> response) {

                List<AllTripsPojo> list;
                AllTripsPojo l;

                if (response.isSuccessful()) {
                    list = response.body();

                    for (int i = 0; i < list.size(); i++) {
                        l = list.get(i);
                        dataList.add(new AllTripsData(l.getTripid(), l.getTripdate(), l.getRouteName(), l.getRoosterno(), l.getTripstatus(), l.getModifieddate(), l.getDrivermobile(),
                                l.getDriverdutystatus(), l.getGpsroutedetails(), l.getGpstotkms(), l.getGpdtothrs(), l.getVehicleRegNo(), l.getVehicleRegNo()));

                    }
                }
                else {
                    // System.out.println("error is "+response.message()+":"+response.isSuccessful()+":"+response.code());
                }

                if (dataList.size() != 0) {

                    mAdapter = new AllRidesRecyclerAdapter(getActivity(), dataList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    rView.setLayoutManager(mLayoutManager);
                    rView.setItemAnimator(new DefaultItemAnimator());
                    rView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                else {
                    //Toast.makeText(getActivity(),"No data",Toast.LENGTH_SHORT).show();
                    tvNoDuties.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(Call<List<AllTripsPojo>> call, Throwable t) {

                Toast.makeText(getActivity(),"Retry..No Internet Connection",Toast.LENGTH_SHORT).show();

            }
        });
    }






}
