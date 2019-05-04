package com.hjsoft.emptransport.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.adapter.DBAdapter;
import com.hjsoft.emptransport.adapter.ExpandableListAdapter;
import com.hjsoft.emptransport.model.DutyData;
import com.hjsoft.emptransport.model.EmpPojo;
import com.hjsoft.emptransport.model.TripEmpPojo;
import com.hjsoft.emptransport.webservices.API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hjsoft on 22/6/17.
 */
public class DutySummaryFragment extends Fragment {

    View v;
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v=inflater.inflate(R.layout.activity_duty_summary,container,false);



        return  v;
    }
}
