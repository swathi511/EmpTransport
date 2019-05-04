package com.hjsoft.emptransport.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.SessionManager;
import com.hjsoft.emptransport.adapter.DBAdapter;
import com.hjsoft.emptransport.adapter.ExpandableListAdapter;
import com.hjsoft.emptransport.adapter.RecyclerAdapter;
import com.hjsoft.emptransport.model.DutyData;
import com.hjsoft.emptransport.model.DutyPojo;
import com.hjsoft.emptransport.model.EmpData;
import com.hjsoft.emptransport.model.EmpPojo;
import com.hjsoft.emptransport.model.TripEmpPojo;
import com.hjsoft.emptransport.webservices.API;
import com.hjsoft.emptransport.webservices.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 25/5/17.
 */
public class HomeActivity extends AppCompatActivity implements RecyclerAdapter.AdapterCallback {

    API REST_CLIENT;
    HashMap<String, String> user;
    String uname,pwd;
    SessionManager session;
    ProgressDialog progressDialog;
    ArrayList<DutyData> list;
    RecyclerAdapter mAdapter;
    RecyclerView rView;
    TextView tvNoDuties;
    ImageButton ibRefresh;
    ArrayList<EmpData> empData=new ArrayList<>();
    ImageView ivLogout;
    DBAdapter dbAdapter;
    TextView tvTitle;
    String stMobileNo;
    List<EmpPojo> empPojoList;
    EmpPojo empPojo;
    List<TripEmpPojo> tripEmpPojoList;
    TripEmpPojo tripEmpPojo;
    LayoutInflater inflater;
    String stCompanyId="CMP0001";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_duties);
        REST_CLIENT= RestClient.get();

        dbAdapter=new DBAdapter(getApplicationContext());
        dbAdapter=dbAdapter.open();
        dbAdapter.insertTrip("1");

        rView=(RecyclerView)findViewById(R.id.aad_rv_list);
        tvNoDuties=(TextView)findViewById(R.id.aad_tv_no_duties);
        ibRefresh=(ImageButton) findViewById(R.id.aad_ib_refresh);
        ivLogout=(ImageView)findViewById(R.id.aad_iv_logout);
        tvTitle=(TextView)findViewById(R.id.aad_tv_title);

        session=new SessionManager(getApplicationContext());
        user = session.getUserDetails();


        inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        uname=user.get(SessionManager.KEY_NAME);
        pwd=user.get(SessionManager.KEY_PWD);

        list=new ArrayList<DutyData>();

        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Duties ...");
        progressDialog.show();

        tvTitle.setText(uname+" ("+"90xxxxxxxx"+") ");

        getTripData();

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this);

                final View dialogView = inflater.inflate(R.layout.alert_logout, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                Button yes=(Button)dialogView.findViewById(R.id.al_bt_ok);
                ImageView ivClose=(ImageView)dialogView.findViewById(R.id.al_iv_close);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        session.logoutUser();
                        alertDialog.dismiss();
                        finish();
                    }
                });

                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });
            }
        });

        ibRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ibRefresh.setVisibility(View.GONE);
                progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading Duties ...");
                progressDialog.show();

                getTripData();

            }
        });



    }


    public void getTripData()
    {
        Call<List<DutyPojo>> call=REST_CLIENT.getDutyData(uname,pwd,stCompanyId);
        call.enqueue(new Callback<List<DutyPojo>>() {
            @Override
            public void onResponse(Call<List<DutyPojo>> call, Response<List<DutyPojo>> response) {

                DutyPojo data;
                List<DutyPojo> dataList;

                if(response.isSuccessful())
                {
                    dataList=response.body();

                    for(int i=0;i<dataList.size();i++) {
                        data = dataList.get(i);

                        list.add(new DutyData(data.getCompanyid(), data.getCustomerid(), data.getTripid(), data.getTripdate(),
                                data.getVehcatgid(), data.getVehicleRegId(), data.getOthervehicle(), data.getDriverid(), data.getOtherdriver(),
                                data.getRouteID(),data.getRouteName(), data.getRoosterno(), data.getTripstatus(), data.getPreparedby(), data.getModifieddate(),
                                data.getDrivermobile(), data.getDriverdutystatus(), data.getGpsroutedetails(), data.getVehicleRegNo(),
                                data.getVehcategory(), data.getGpstotkms(), data.getGpdtothrs(), data.getLongitude(), data.getLatitude()));

                        stMobileNo=data.getDrivermobile();


                    }

                    tvTitle.setText(uname+" ("+stMobileNo+") ");
                }
                else
                {
                    progressDialog.dismiss();
                }
                // System.out.println("list size issssssss"+list.size());

                if(list.size()!=0) {

                    progressDialog.dismiss();

                    mAdapter = new RecyclerAdapter(HomeActivity.this, list);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    rView.setLayoutManager(mLayoutManager);
                    rView.setItemAnimator(new DefaultItemAnimator());
                    rView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }else
                {
                    ibRefresh.setVisibility(View.GONE);
                    tvNoDuties.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<DutyPojo>> call, Throwable t) {

                ibRefresh.setVisibility(View.VISIBLE);

                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this,"Please check Internet connection!",Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onMethodCallback(final int position, final ArrayList<DutyData> data) {

       /* final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        DutyData d;

        d=data.get(position);

        Call<List<EmpPojo>> call=REST_CLIENT.getEmpData(d.getTripId());
        call.enqueue(new Callback<List<EmpPojo>>() {
            @Override
            public void onResponse(Call<List<EmpPojo>> call, Response<List<EmpPojo>> response) {

                List<EmpPojo> eList;
                EmpPojo e;

                if (response.isSuccessful())
                {


                    eList=response.body();

                    for(int i=0;i<eList.size();i++)
                    {
                        e=eList.get(i);

                       // empData.add(new EmpData(e.getEmployeeid(),e.getEmployeename(),e.getMobileno(),e.getEmailid(),e.getReportingplace(),e.getEmptimein(),e.getEmplocation(),e.getOtp()));

                    }

                    progressDialog.dismiss();

                    Intent i = new Intent(HomeActivity.this, RideActivity.class);
                    i.putExtra("position", position);
                    i.putExtra("list", data);
                    i.putExtra("empList",empData);
                    startActivity(i);
                    finish();

                }
                else {

                    empData.add(new EmpData("","","","","","","",""));

                    progressDialog.dismiss();

                    Intent i = new Intent(HomeActivity.this, RideActivity.class);
                    i.putExtra("position", position);
                    i.putExtra("list", data);
                    i.putExtra("empList",empData);
                    startActivity(i);
                    finish();

                }
            }

            @Override
            public void onFailure(Call<List<EmpPojo>> call, Throwable t) {

                progressDialog.dismiss();

                Toast.makeText(HomeActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();

            }
        });
        */

        DutyData d;

        d=data.get(position);
        String stTripId=d.getTripId();
        dbAdapter.updateTrip(stTripId);

        /*


        Intent i = new Intent(HomeActivity.this, DutySummaryActivity.class);
        i.putExtra("list", data);
        i.putExtra("position", position);
        i.putExtra("value","no");

        startActivity(i);
        */

//        List<EmpPojo> empPojoList=new ArrayList<>();
//        List<TripEmpPojo> tripEmpPojoList=new ArrayList<>();



        Call<List<EmpPojo>> call=REST_CLIENT.getEmpData(stTripId,stCompanyId);
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


                        tripEmpPojoList=empPojo.getTripEmpPojo();

                        List<String> ll = new ArrayList<String>();

                        for(int j=0;j<tripEmpPojoList.size();j++)
                        {
                            tripEmpPojo=tripEmpPojoList.get(j);

                            ll.add(tripEmpPojo.getEmployeename()+"@"+tripEmpPojo.getMobileno());

                        }
                        //ll.add("The Shawshank Redemption");

                    }

                    Intent i = new Intent(HomeActivity.this, RideActivity.class);
                    i.putExtra("list", data);
                    i.putExtra("position", position);
                    i.putExtra("empList",(ArrayList)empPojoList);
                    i.putExtra("tripEmpList",(ArrayList)tripEmpPojoList);
                    startActivity(i);
                    finish();


                }
                else {

                }
            }

            @Override
            public void onFailure(Call<List<EmpPojo>> call, Throwable t) {

                progressDialog.dismiss();

                Toast.makeText(HomeActivity.this,"Please check Internet Connection",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
