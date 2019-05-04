package com.hjsoft.emptransport.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.adapter.DBAdapter;
import com.hjsoft.emptransport.adapter.ExpandableListAdapter;
import com.hjsoft.emptransport.adapter.RecyclerAdapter;
import com.hjsoft.emptransport.model.DutyData;
import com.hjsoft.emptransport.model.EmpPojo;
import com.hjsoft.emptransport.model.TripEmpPojo;
import com.hjsoft.emptransport.webservices.API;
import com.hjsoft.emptransport.webservices.RestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 9/6/17.
 */
public class DutySummaryActivity extends AppCompatActivity implements RecyclerAdapter.AdapterCallback {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    API REST_CLIENT;
    String stTripId;
    //    ArrayList<EmpPojo> empDataList=new ArrayList<EmpPojo>();
//    EmpData empData;
    LayoutInflater inflater;
    List<EmpPojo> empPojoList;
    EmpPojo empPojo;
    List<TripEmpPojo> tripEmpPojoList;
    TripEmpPojo tripEmpPojo;
    DBAdapter dbAdapter;
    TextView tvAccept,tvStart;
    TextView tvStartingPoint,tvEndPoint,tvDate;
    Bundle b;
    ArrayList<DutyData> dutyList;
    DutyData dutyData;
    int position;
    ProgressDialog progressDialog;
    String value;
    TextView tvBack;
    ImageView ivRefresh;
    String companyId="CMP0001";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_summary);

        REST_CLIENT= RestClient.get();

        inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbAdapter=new DBAdapter(getApplicationContext());
        dbAdapter=dbAdapter.open();
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        tvAccept=(TextView)findViewById(R.id.ads_tv_accept);
        tvStart=(TextView)findViewById(R.id.ads_tv_start);
        tvStartingPoint=(TextView)findViewById(R.id.ads_tv_spoint);
        tvEndPoint=(TextView)findViewById(R.id.ads_tv_epoint);
        tvDate=(TextView)findViewById(R.id.ads_tv_date);
        tvBack=(TextView)findViewById(R.id.ads_tv_back);
        ivRefresh=(ImageView)findViewById(R.id.ads_iv_refresh);
        ivRefresh.setVisibility(View.GONE);

        dutyList=(ArrayList<DutyData>) getIntent().getSerializableExtra("list");
        b=getIntent().getExtras();
        position=b.getInt("position");
        value=b.getString("value");
        dutyData=dutyList.get(position);
        stTripId=dutyData.getTripId();

        if(value.equals("no")) {

            if (dutyData.getDriverDutyStatus().equals("A")) {
                tvAccept.setVisibility(View.GONE);
                tvStart.setVisibility(View.VISIBLE);

                if (dbAdapter.findTripInfo(stTripId)) {
                    tvStart.setText("Continue Duty !");
                } else {

                }
            }
        }
        else {

            tvAccept.setVisibility(View.GONE);
            tvStart.setVisibility(View.GONE);

        }

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

        progressDialog = new ProgressDialog(DutySummaryActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        prepareListData();

//        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
//        expListView.setAdapter(listAdapter);

        // setting list adapter


        // Listview Group click listener
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
//                    System.out.println("not insertinggggggggggggggggggg");
//                }


                return false;
            }
        });

        // Listview Group expanded listener
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
                // TODO Auto-generated method stub
              //  Toast.makeText(
                      //  getApplicationContext(),
                      //  listDataHeader.get(groupPosition)
                            //    + " : "
                            //    + listDataChild.get(
                            //    listDataHeader.get(groupPosition)).get(
                             //   childPosition), Toast.LENGTH_SHORT)
                      //  .show();

                if(value.equals("no"))
                {

                    // System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                }
                else {

                    empPojo = empPojoList.get(groupPosition);
                    tripEmpPojoList = empPojo.getTripEmpPojo();
                    tripEmpPojo = tripEmpPojoList.get(childPosition);

                    if(dbAdapter.getInfo(stTripId,tripEmpPojo.getEmployeename() + "@" + tripEmpPojo.getMobileno()+"#"+tripEmpPojo.getEmptimein()).equals("true"))
                    {

                    }
                    else {


                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DutySummaryActivity.this);

                        final View dialogView = inflater.inflate(R.layout.alert_validate_otp, null);
                        dialogBuilder.setView(dialogView);

                        final AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.show();

                        TextView tvOk = (TextView) dialogView.findViewById(R.id.avo_tv_ok);
                        final EditText etOk = (EditText) dialogView.findViewById(R.id.avo_et_otp);
                        TextView tvEmpName = (TextView) dialogView.findViewById(R.id.avo_tv_empName);


                        tvEmpName.setText(tripEmpPojo.getEmployeename());

                        tvOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (etOk.getText().toString().trim().equals("")) {
                                    Toast.makeText(DutySummaryActivity.this, "Please enter OTP!", Toast.LENGTH_SHORT).show();
                                } else {

                                    String otp = etOk.getText().toString().trim();

                                    if (otp.equals(tripEmpPojo.getOtp())) {

                                        String arrivedTime=dbAdapter.getInfo(stTripId,groupPosition+"*"+empPojo.getLocationname());
                                       // System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                                        // System.out.println("arrived time is "+arrivedTime);
                                        dbAdapter.insertInfo(tripEmpPojo.getEmployeename() + "@" + tripEmpPojo.getMobileno()+"#"+tripEmpPojo.getEmptimein(), "true", stTripId,arrivedTime);
//                                tvValOTP1.setText("On Board");
//                                tvValOTP1.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
//                                tvValOTP1.setTextColor(Color.parseColor("#1976d2"));
//                                tvValOTP1.setEnabled(false);
//                                emp1OTP=true;

                                        Toast.makeText(DutySummaryActivity.this, "Valid OTP!", Toast.LENGTH_SHORT).show();
                                        //listAdapter.notifyDataSetChanged();
                                        expListView.invalidateViews();
                                        listAdapter.setNewItems(listDataHeader, listDataChild);
                                    } else {

                                        Toast.makeText(DutySummaryActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }
                }



                return false;
            }
        });

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(DutySummaryActivity.this, RideActivity.class);
                i.putExtra("list", dutyList);
                i.putExtra("position", position);
                i.putExtra("empList",(ArrayList)empPojoList);
                i.putExtra("tripEmpList",(ArrayList)tripEmpPojoList);
                startActivity(i);
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ivRefresh.setVisibility(View.GONE);
                prepareListData();

            }
        });
    }

    /*
     * Preparing the list data
     */
    public void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        Call<List<EmpPojo>> call=REST_CLIENT.getEmpData(stTripId,companyId);
        call.enqueue(new Callback<List<EmpPojo>>() {
            @Override
            public void onResponse(Call<List<EmpPojo>> call, Response<List<EmpPojo>> response) {

                if(response.isSuccessful())
                {
                    progressDialog.dismiss();
                    empPojoList=response.body();

                    for(int i=0;i<empPojoList.size();i++)
                    {
                        empPojo=empPojoList.get(i);
                        listDataHeader.add(empPojo.getLocationname());

                        tripEmpPojoList=empPojo.getTripEmpPojo();

                        List<String> ll = new ArrayList<String>();

                        for(int j=0;j<tripEmpPojoList.size();j++)
                        {
                            tripEmpPojo=tripEmpPojoList.get(j);

                            ll.add(tripEmpPojo.getEmployeename()+"@"+tripEmpPojo.getMobileno()+"#"+tripEmpPojo.getEmptimein());

                        }
                        //ll.add("The Shawshank Redemption");
                        listDataChild.put(listDataHeader.get(i), ll);
                    }

                    listAdapter = new ExpandableListAdapter(DutySummaryActivity.this, listDataHeader, listDataChild,stTripId);
                    expListView.setAdapter(listAdapter);
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<List<EmpPojo>> call, Throwable t) {

                progressDialog.dismiss();
                ivRefresh.setVisibility(View.VISIBLE);

                Toast.makeText(DutySummaryActivity.this,"Please check Internet Connection",Toast.LENGTH_SHORT).show();

            }
        });





        /*
        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);*/
    }

    @Override
    public void onMethodCallback(final int position, final ArrayList<DutyData> data) {


        Intent i = new Intent(DutySummaryActivity.this, RideActivity.class);
        i.putExtra("list", data);
        i.putExtra("position", position);
        i.putExtra("empList",(ArrayList)empPojoList);
        i.putExtra("tripEmpList",(ArrayList)tripEmpPojoList);
        startActivity(i);
        finish();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    public static String getCurrentTime() {
        //date output format
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
}