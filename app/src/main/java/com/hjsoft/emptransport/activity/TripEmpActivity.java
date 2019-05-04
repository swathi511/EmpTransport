package com.hjsoft.emptransport.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.adapter.AllRidesExpandableListAdapter;
import com.hjsoft.emptransport.adapter.DBAdapter;
import com.hjsoft.emptransport.adapter.ExpandableListAdapter;
import com.hjsoft.emptransport.model.AllTripsData;
import com.hjsoft.emptransport.model.EmpPojo;
import com.hjsoft.emptransport.model.TripEmpPojo;
import com.hjsoft.emptransport.webservices.API;
import com.hjsoft.emptransport.webservices.RestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 22/6/17.
 */
public class TripEmpActivity extends AppCompatActivity {

    AllRidesExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    API REST_CLIENT;
    String stTripId;
    LayoutInflater inflater;
    List<EmpPojo> empPojoList;
    EmpPojo empPojo;
    List<TripEmpPojo> tripEmpPojoList;
    TripEmpPojo tripEmpPojo;
    DBAdapter dbAdapter;
    TextView tvStartingPoint,tvEndPoint,tvDate;
    Bundle b;
    ArrayList<AllTripsData> dutyList;
    AllTripsData dutyData;
    int position;
    ProgressDialog progressDialog;

    TextView tvBack;
    ImageView ivRefresh;
    String companyId="CMP0001";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_emp);

        inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbAdapter=new DBAdapter(getApplicationContext());
        dbAdapter=dbAdapter.open();
        expListView = (ExpandableListView) findViewById(R.id.ate_Exp);
        tvStartingPoint=(TextView)findViewById(R.id.ate_tv_spoint);
        tvEndPoint=(TextView)findViewById(R.id.ate_tv_epoint);
        tvDate=(TextView)findViewById(R.id.ate_tv_date);
        tvBack=(TextView)findViewById(R.id.ate_tv_back);
        ivRefresh=(ImageView)findViewById(R.id.ate_iv_refresh);
        ivRefresh.setVisibility(View.GONE);

        dutyList=(ArrayList<AllTripsData>) getIntent().getSerializableExtra("list");
        b=getIntent().getExtras();
        position=b.getInt("position");

        dutyData=dutyList.get(position);
        stTripId=dutyData.getTripId();

        REST_CLIENT= RestClient.get();

        try {
            SimpleDateFormat newformat = new SimpleDateFormat("d MMM yyyy");
            String datestring = dutyData.getTripDate().split("T")[0];
            SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd");
            String reformattedStr = newformat.format(oldformat.parse(datestring));

            tvDate.setText(reformattedStr);

        }catch(ParseException e){e.printStackTrace();}

        String loc[]=dutyData.getRouteName().split("-");

        tvStartingPoint.setText(loc[0]);
        tvEndPoint.setText(loc[1]);

        // preparing list data

        progressDialog = new ProgressDialog(TripEmpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        prepareListData();


        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

//                 Toast.makeText(getApplicationContext(),
//                 "Group Clicked " + listDataHeader.get(groupPosition),
//                 Toast.LENGTH_SHORT).show();

//                empPojo = empPojoList.get(groupPosition);
//
//                if(dbAdapter.getInfo(stTripId,groupPosition+"*"+empPojo.getLocationname()).equals("")) {
//                    dbAdapter.insertInfo(groupPosition + "*" + empPojo.getLocationname(), getCurrentTime(), stTripId, getCurrentTime());
//                }
//                else {
//
//                    System.out.println("not inserting");
//                }


                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                // Toast.makeText(getApplicationContext(),
                //   listDataHeader.get(groupPosition) + " Expanded",
                //   Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                //  Toast.makeText(getApplicationContext(),
                // listDataHeader.get(groupPosition) + " Collapsed",
                //  Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        final int groupPosition, int childPosition, long id) {



                return  false;


            }
        });


        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ivRefresh.setVisibility(View.GONE);
                prepareListData();

            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }

    public void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        Call<List<EmpPojo>> call = REST_CLIENT.getEmpData(stTripId, companyId);
        call.enqueue(new Callback<List<EmpPojo>>() {
            @Override
            public void onResponse(Call<List<EmpPojo>> call, Response<List<EmpPojo>> response) {

                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    empPojoList = response.body();

                    for (int i = 0; i < empPojoList.size(); i++) {
                        empPojo = empPojoList.get(i);
                        listDataHeader.add(empPojo.getLocationname());

                        tripEmpPojoList = empPojo.getTripEmpPojo();

                        List<String> ll = new ArrayList<String>();

                        for (int j = 0; j < tripEmpPojoList.size(); j++) {
                            tripEmpPojo = tripEmpPojoList.get(j);

                            ll.add(tripEmpPojo.getEmployeename() + "@" + tripEmpPojo.getMobileno() + "#" + tripEmpPojo.getEmptimein());

                        }
                        listDataChild.put(listDataHeader.get(i), ll);
                    }

                    listAdapter = new AllRidesExpandableListAdapter(TripEmpActivity.this, listDataHeader, listDataChild, stTripId);
                    expListView.setAdapter(listAdapter);
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<EmpPojo>> call, Throwable t) {

                progressDialog.dismiss();
                ivRefresh.setVisibility(View.VISIBLE);

                Toast.makeText(TripEmpActivity.this, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
