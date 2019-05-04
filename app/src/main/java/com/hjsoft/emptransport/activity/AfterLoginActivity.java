package com.hjsoft.emptransport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.SessionManager;
import com.hjsoft.emptransport.adapter.DBAdapter;
import com.hjsoft.emptransport.adapter.DrawerItemCustomAdapter;
import com.hjsoft.emptransport.adapter.RecyclerAdapter;
import com.hjsoft.emptransport.fragment.HomeFragment;
import com.hjsoft.emptransport.model.DutyData;
import com.hjsoft.emptransport.model.EmpPojo;
import com.hjsoft.emptransport.model.NavigationData;
import com.hjsoft.emptransport.model.TripEmpPojo;
import com.hjsoft.emptransport.webservices.API;
import com.hjsoft.emptransport.webservices.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 27/6/17.
 */
public class AfterLoginActivity extends AppCompatActivity implements RecyclerAdapter.AdapterCallback{

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    DrawerItemCustomAdapter adapter;

    DBAdapter dbAdapter;
    API REST_CLIENT;
    String stCompanyId="CMP0001";
    List<EmpPojo> empPojoList;
    EmpPojo empPojo;
    List<TripEmpPojo> tripEmpPojoList;
    TripEmpPojo tripEmpPojo;

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

        Fragment fragment=new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, fragment,"all_duties").commit();
        setTitle("Upcoming Duties");

        adapter.setSelectedItem(0);
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
                Intent i=new Intent(AfterLoginActivity.this,AfterLoginActivity.class);
                startActivity(i);
                finish();
                break;
            case 1:
                Intent j=new Intent(AfterLoginActivity.this,AllRidesActivity.class);
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

        dbAdapter=new DBAdapter(getApplicationContext());
        dbAdapter=dbAdapter.open();
        REST_CLIENT= RestClient.get();

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

        if(dbAdapter.findJourneyData(stTripId))
        {

            Call<List<EmpPojo>> call = REST_CLIENT.getEmpData(stTripId, stCompanyId);
            call.enqueue(new Callback<List<EmpPojo>>() {
                @Override
                public void onResponse(Call<List<EmpPojo>> call, Response<List<EmpPojo>> response) {

                    if (response.isSuccessful()) {
                        //progressDialog.dismiss();
                        empPojoList = response.body();

                        for (int i = 0; i < empPojoList.size(); i++) {
                            empPojo = empPojoList.get(i);


                            tripEmpPojoList = empPojo.getTripEmpPojo();

                            List<String> ll = new ArrayList<String>();

                            for (int j = 0; j < tripEmpPojoList.size(); j++) {
                                tripEmpPojo = tripEmpPojoList.get(j);

                                ll.add(tripEmpPojo.getEmployeename() + "@" + tripEmpPojo.getMobileno());

                            }
                            //ll.add("The Shawshank Redemption");

                        }

                        Intent i=new Intent(AfterLoginActivity.this,OfflineDataActivity.class);
                        i.putExtra("list", data);
                        i.putExtra("position", position);
                        i.putExtra("empList", (ArrayList) empPojoList);
                        i.putExtra("tripEmpList", (ArrayList) tripEmpPojoList);
                        startActivity(i);
                        finish();


                    } else {

                    }
                }

                @Override
                public void onFailure(Call<List<EmpPojo>> call, Throwable t) {

                    // progressDialog.dismiss();

                    Toast.makeText(AfterLoginActivity.this, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                }
            });



        }
        else
        {

            Call<List<EmpPojo>> call = REST_CLIENT.getEmpData(stTripId, stCompanyId);
            call.enqueue(new Callback<List<EmpPojo>>() {
                @Override
                public void onResponse(Call<List<EmpPojo>> call, Response<List<EmpPojo>> response) {

                    if (response.isSuccessful()) {
                        //progressDialog.dismiss();
                        empPojoList = response.body();

                        for (int i = 0; i < empPojoList.size(); i++) {
                            empPojo = empPojoList.get(i);


                            tripEmpPojoList = empPojo.getTripEmpPojo();

                            List<String> ll = new ArrayList<String>();

                            for (int j = 0; j < tripEmpPojoList.size(); j++) {
                                tripEmpPojo = tripEmpPojoList.get(j);

                                ll.add(tripEmpPojo.getEmployeename() + "@" + tripEmpPojo.getMobileno());

                            }
                            //ll.add("The Shawshank Redemption");

                        }

                        Intent i = new Intent(AfterLoginActivity.this, RideActivity.class);
                        i.putExtra("list", data);
                        i.putExtra("position", position);
                        i.putExtra("empList", (ArrayList) empPojoList);
                        i.putExtra("tripEmpList", (ArrayList) tripEmpPojoList);
                        startActivity(i);
                        finish();


                    } else {

                    }
                }

                @Override
                public void onFailure(Call<List<EmpPojo>> call, Throwable t) {

                    // progressDialog.dismiss();

                    Toast.makeText(AfterLoginActivity.this, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
