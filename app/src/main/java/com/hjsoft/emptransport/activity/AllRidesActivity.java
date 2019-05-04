package com.hjsoft.emptransport.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.SessionManager;
import com.hjsoft.emptransport.adapter.AllRidesRecyclerAdapter;
import com.hjsoft.emptransport.adapter.DrawerItemCustomAdapter;
import com.hjsoft.emptransport.fragment.AllRidesFragment;
import com.hjsoft.emptransport.fragment.HomeFragment;
import com.hjsoft.emptransport.model.AllTripsData;
import com.hjsoft.emptransport.model.AllTripsPojo;
import com.hjsoft.emptransport.model.NavigationData;
import com.hjsoft.emptransport.webservices.API;
import com.hjsoft.emptransport.webservices.RestClient;

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
public class AllRidesActivity extends AppCompatActivity implements AllRidesRecyclerAdapter.RidesAdapterCallback {


    AllRidesRecyclerAdapter mAdapter;
    RecyclerView rView;
    API REST_CLIENT;
    String stCompanyId="CMP0001";
    ArrayList<AllTripsData> dataList;
    AllTripsData data;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sp";
    String stDriverId;
    TextView tvFrom,tvTo;
    ImageView ivFrom,ivTo;
    int day,mnth,yr;
    String stDate,stFrom,stTo;

    //------------------------

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

        Fragment fragment=new AllRidesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, fragment,"all_duties").commit();
        setTitle("Past Duties");

        adapter.setSelectedItem(1);
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
                Intent i=new Intent(AllRidesActivity.this,AfterLoginActivity.class);
                startActivity(i);
                finish();
                break;
            case 1:
                Intent j=new Intent(AllRidesActivity.this,AllRidesActivity.class);
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

        finish();
    }


    /*
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_rides);

        tvFrom=(TextView)findViewById(R.id.aar_tv_from);
        tvTo=(TextView)findViewById(R.id.aar_tv_to);
        ivFrom=(ImageView)findViewById(R.id.aar_iv_from);
        ivTo=(ImageView)findViewById(R.id.aar_iv_to);

        REST_CLIENT= RestClient.get();
        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        stDriverId=pref.getString("driverId",null);

        ivFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AllRidesActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.alert_date, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);

                Button ok=(Button)dialogView.findViewById(R.id.ad_bt_ok);
                ImageView cancel=(ImageView)dialogView.findViewById(R.id.ad_bt_cancel);
                DatePicker dp=(DatePicker)dialogView.findViewById(R.id.datePicker);

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

                        stDate=day+"/"+mnth+"/"+yr;
                        tvFrom.setText(stDate);
                        stFrom=stDate;
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

        ivTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AllRidesActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.alert_date, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);

                Button ok=(Button)dialogView.findViewById(R.id.ad_bt_ok);
                ImageView cancel=(ImageView)dialogView.findViewById(R.id.ad_bt_cancel);
                DatePicker dp=(DatePicker)dialogView.findViewById(R.id.datePicker);

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

                        stDate=day+"/"+mnth+"/"+yr;
                        tvTo.setText(stDate);
                        stTo=stDate;
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

        Call<List<AllTripsPojo>> call=REST_CLIENT.getAllTrips(stCompanyId,stDriverId,stFrom,stTo);
        call.enqueue(new Callback<List<AllTripsPojo>>() {
            @Override
            public void onResponse(Call<List<AllTripsPojo>> call, Response<List<AllTripsPojo>> response) {

                List<AllTripsPojo> list;
                AllTripsPojo l;

                if(response.isSuccessful()) {
                    list = response.body();

                    for (int i = 0; i < list.size(); i++)
                    {
                        l=list.get(0);
                        dataList.add(new AllTripsData(l.getTripid(),l.getTripdate(),l.getRouteName(),l.getRoosterno(),l.getTripstatus(),l.getModifieddate(),l.getDrivermobile(),
                                l.getDriverdutystatus(),l.getGpsroutedetails(),l.getGpstotkms(),l.getGpdtothrs(),l.getVehicleRegNo(),l.getVehicleRegNo()));

                    }

                    if(dataList.size()!=0)
                    {

                        mAdapter = new AllRidesRecyclerAdapter(AllRidesActivity.this,dataList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rView.setLayoutManager(mLayoutManager);
                        rView.setItemAnimator(new DefaultItemAnimator());
                        rView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }


                }


            }

            @Override
            public void onFailure(Call<List<AllTripsPojo>> call, Throwable t) {

            }
        });


    }

*/
    @Override
    public void onMethodCallback(final int position, final ArrayList<AllTripsData> data) {


        Intent i=new Intent(AllRidesActivity.this,TripEmpActivity.class);
        i.putExtra("position", position);
        i.putExtra("list",data);
        startActivity(i);

    }

}
