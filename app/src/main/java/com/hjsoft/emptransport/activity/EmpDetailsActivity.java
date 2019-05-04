package com.hjsoft.emptransport.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.adapter.DBAdapter;
import com.hjsoft.emptransport.model.EmpData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by hjsoft on 5/6/17.
 */
public class EmpDetailsActivity extends AppCompatActivity {


    /*
    TextView tvEmp1,tvEmp1No,tvEmp1Loc,tvEmp1Time,tvEmp2,tvEmp2No,tvEmp2Loc,tvEmp2Time;
    TextView  tvEmp3,tvEmp3No,tvEmp3Loc,tvEmp3Time,tvEmp4,tvEmp4No,tvEmp4Loc,tvEmp4Time;
    TextView  tvEmp5,tvEmp5No,tvEmp5Loc,tvEmp5Time,tvEmp6,tvEmp6No,tvEmp6Loc,tvEmp6Time;
    TextView  tvEmp7,tvEmp7No,tvEmp7Loc,tvEmp7Time,tvEmp8,tvEmp8No,tvEmp8Loc,tvEmp8Time;
    LinearLayout ll1,ll2,ll3,ll4,ll5,ll6,ll7;
    TextView tvValOTP1,tvValOTP2,tvValOTP3,tvValOTP4,tvValOTP5,tvValOTP6,tvValOTP7;
    ArrayList<EmpData> empList;
    EmpData empData;
    DBAdapter dbAdapter;
    boolean emp1OTP=false,emp2OTP=false,emp3OTP=false,emp4OTP=false,emp5OTP=false,emp6OTP=false,emp7OTP=false;
    String stTripId;
    LayoutInflater inflater;
    Bundle b;
    TextView tvBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_emp_details);
        tvValOTP1=(TextView)findViewById(R.id.aed_tv_votp_1);
        tvValOTP2=(TextView)findViewById(R.id.aed_tv_votp_2);
        tvValOTP3=(TextView)findViewById(R.id.aed_tv_votp_3);
        tvValOTP4=(TextView)findViewById(R.id.aed_tv_votp_4);
        tvValOTP5=(TextView)findViewById(R.id.aed_tv_votp_5);
        tvValOTP6=(TextView)findViewById(R.id.aed_tv_votp_6);
        tvValOTP7=(TextView)findViewById(R.id.aed_tv_votp_7);
        tvEmp1=(TextView)findViewById(R.id.aed_tv_emp1);
        tvEmp2=(TextView)findViewById(R.id.aed_tv_emp2);
        tvEmp3=(TextView)findViewById(R.id.aed_tv_emp3);
        tvEmp4=(TextView)findViewById(R.id.aed_tv_emp4);
        tvEmp5=(TextView)findViewById(R.id.aed_tv_emp5);
        tvEmp6=(TextView)findViewById(R.id.aed_tv_emp6);
        tvEmp7=(TextView)findViewById(R.id.aed_tv_emp7);
        tvEmp1No=(TextView)findViewById(R.id.aed_tv_emp1_no);
        tvEmp2No=(TextView)findViewById(R.id.aed_tv_emp2_no);
        tvEmp3No=(TextView)findViewById(R.id.aed_tv_emp3_no);
        tvEmp4No=(TextView)findViewById(R.id.aed_tv_emp4_no);
        tvEmp5No=(TextView)findViewById(R.id.aed_tv_emp5_no);
        tvEmp6No=(TextView)findViewById(R.id.aed_tv_emp6_no);
        tvEmp7No=(TextView)findViewById(R.id.aed_tv_emp7_no);
        tvEmp1Loc=(TextView)findViewById(R.id.aed_tv_emp1_loc);
        tvEmp2Loc=(TextView)findViewById(R.id.aed_tv_emp2_loc);
        tvEmp3Loc=(TextView)findViewById(R.id.aed_tv_emp3_loc);
        tvEmp4Loc=(TextView)findViewById(R.id.aed_tv_emp4_loc);
        tvEmp5Loc=(TextView)findViewById(R.id.aed_tv_emp5_loc);
        tvEmp6Loc=(TextView)findViewById(R.id.aed_tv_emp6_loc);
        tvEmp7Loc=(TextView)findViewById(R.id.aed_tv_emp7_loc);
        tvEmp1Time=(TextView)findViewById(R.id.aed_tv_emp1_time);
        tvEmp2Time=(TextView)findViewById(R.id.aed_tv_emp2_time);
        tvEmp3Time=(TextView)findViewById(R.id.aed_tv_emp3_time);
        tvEmp4Time=(TextView)findViewById(R.id.aed_tv_emp4_time);
        tvEmp5Time=(TextView)findViewById(R.id.aed_tv_emp5_time);
        tvEmp6Time=(TextView)findViewById(R.id.aed_tv_emp6_time);
        tvEmp7Time=(TextView)findViewById(R.id.aed_tv_emp7_time);
        ll1=(LinearLayout)findViewById(R.id.aed_ll1);
        ll2=(LinearLayout)findViewById(R.id.aed_ll2);
        ll3=(LinearLayout)findViewById(R.id.aed_ll3);
        ll4=(LinearLayout)findViewById(R.id.aed_ll4);
        ll5=(LinearLayout)findViewById(R.id.aed_ll5);
        ll6=(LinearLayout)findViewById(R.id.aed_ll6);
        ll7=(LinearLayout)findViewById(R.id.aed_ll7);
        tvBack=(TextView)findViewById(R.id.aed_tv_back);

        ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.GONE);
        ll3.setVisibility(View.GONE);
        ll4.setVisibility(View.GONE);
        ll5.setVisibility(View.GONE);
        ll6.setVisibility(View.GONE);
        ll7.setVisibility(View.GONE);

        empList=(ArrayList<EmpData>)getIntent().getSerializableExtra("empList");

        b=getIntent().getExtras();
        stTripId=b.getString("tripId");

        inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbAdapter=new DBAdapter(getApplicationContext());
        dbAdapter=dbAdapter.open();

        if((dbAdapter.getInfo(stTripId,"emp1OTP")).equals(""))
        {

        }
        else
        {
            tvValOTP1.setText("On Board");
            tvValOTP1.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
            tvValOTP1.setTextColor(Color.parseColor("#1976d2"));
            tvValOTP1.setEnabled(false);
            emp1OTP=true;
        }

        if((dbAdapter.getInfo(stTripId,"emp2OTP")).equals(""))
        {

        }
        else
        {
            tvValOTP2.setText("On Board");
            tvValOTP2.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
            tvValOTP2.setTextColor(Color.parseColor("#1976d2"));
            tvValOTP2.setEnabled(false);
            emp2OTP=true;
        }

        if((dbAdapter.getInfo(stTripId,"emp3OTP")).equals(""))
        {

        }
        else
        {
            tvValOTP3.setText("On Board");
            tvValOTP3.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
            tvValOTP3.setTextColor(Color.parseColor("#1976d2"));
            tvValOTP3.setEnabled(false);
            emp3OTP=true;
        }

        if((dbAdapter.getInfo(stTripId,"emp4OTP")).equals(""))
        {

        }
        else
        {
            tvValOTP4.setText("On Board");
            tvValOTP4.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
            tvValOTP4.setTextColor(Color.parseColor("#1976d2"));
            tvValOTP4.setEnabled(false);
            emp4OTP=true;
        }

        if((dbAdapter.getInfo(stTripId,"emp5OTP")).equals(""))
        {

        }
        else
        {
            tvValOTP4.setText("On Board");
            tvValOTP4.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
            tvValOTP4.setTextColor(Color.parseColor("#1976d2"));
            tvValOTP4.setEnabled(false);
            emp5OTP=true;
        }

        if((dbAdapter.getInfo(stTripId,"emp6OTP")).equals(""))
        {

        }
        else
        {
            tvValOTP4.setText("On Board");
            tvValOTP4.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
            tvValOTP4.setTextColor(Color.parseColor("#1976d2"));
            tvValOTP4.setEnabled(false);
            emp6OTP=true;
        }

        if((dbAdapter.getInfo(stTripId,"emp7OTP")).equals(""))
        {

        }
        else
        {
            tvValOTP4.setText("On Board");
            tvValOTP4.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
            tvValOTP4.setTextColor(Color.parseColor("#1976d2"));
            tvValOTP4.setEnabled(false);
            emp7OTP=true;
        }

        for(int i=0;i<empList.size();i++)
        {
            empData=empList.get(i);
            switch (i){

                case 0:ll1.setVisibility(View.VISIBLE);
                    tvEmp1.setText(empData.getEmpName());
                    tvEmp1No.setText(empData.getMobileNo());
                    tvEmp1Loc.setText(empData.getEmpLoc());
                    tvEmp1Time.setText(empData.getEmpTimeIn());
                    tvValOTP1.setVisibility(View.VISIBLE);
                    break;
                case 1:ll2.setVisibility(View.VISIBLE);
                    tvEmp2.setText(empData.getEmpName());
                    tvEmp2No.setText(empData.getMobileNo());
                    tvEmp2Loc.setText(empData.getEmpLoc());
                    tvEmp2Time.setText(empData.getEmpTimeIn());
                    tvValOTP2.setVisibility(View.VISIBLE);
                    break;
                case 2:ll3.setVisibility(View.VISIBLE);
                    tvEmp3.setText(empData.getEmpName());
                    tvEmp3No.setText(empData.getMobileNo());
                    tvEmp3Loc.setText(empData.getEmpLoc());
                    tvEmp3Time.setText(empData.getEmpTimeIn());
                    tvValOTP3.setVisibility(View.VISIBLE);
                    break;
                case 3:ll4.setVisibility(View.VISIBLE);
                    tvEmp4.setText(empData.getEmpName());
                    tvEmp4No.setText(empData.getMobileNo());
                    tvEmp4Loc.setText(empData.getEmpLoc());
                    tvEmp4Time.setText(empData.getEmpTimeIn());
                    tvValOTP4.setVisibility(View.VISIBLE);
                    break;
                case 4:ll5.setVisibility(View.VISIBLE);
                    tvEmp5.setText(empData.getEmpName());
                    tvEmp5No.setText(empData.getMobileNo());
                    tvEmp5Loc.setText(empData.getEmpLoc());
                    tvEmp5Time.setText(empData.getEmpTimeIn());
                    tvValOTP5.setVisibility(View.VISIBLE);
                    break;
                case 5:ll6.setVisibility(View.VISIBLE);
                    tvEmp6.setText(empData.getEmpName());
                    tvEmp6No.setText(empData.getMobileNo());
                    tvEmp6Loc.setText(empData.getEmpLoc());
                    tvEmp6Time.setText(empData.getEmpTimeIn());
                    tvValOTP6.setVisibility(View.VISIBLE);
                    break;
                case 6:ll7.setVisibility(View.VISIBLE);
                    tvEmp7.setText(empData.getEmpName());
                    tvEmp7No.setText(empData.getMobileNo());
                    tvEmp7Loc.setText(empData.getEmpLoc());
                    tvEmp7Time.setText(empData.getEmpTimeIn());
                    tvValOTP7.setVisibility(View.VISIBLE);
                    break;
            }
        }

        tvValOTP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmpDetailsActivity.this);

                final View dialogView = inflater.inflate(R.layout.alert_validate_otp, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                TextView tvOk=(TextView)dialogView.findViewById(R.id.avo_tv_ok);
                final EditText etOk=(EditText)dialogView.findViewById(R.id.avo_et_otp);
                TextView tvEmpName=(TextView)dialogView.findViewById(R.id.avo_tv_empName);

                final EmpData ed;
                ed=empList.get(0);

                tvEmpName.setText(ed.getEmpName());

                tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(etOk.getText().toString().trim().equals(""))
                        {
                            Toast.makeText(EmpDetailsActivity.this,"Please enter OTP!",Toast.LENGTH_SHORT).show();
                        }
                        else {

                            String otp=etOk.getText().toString().trim();

                            if(otp.equals(ed.getOtp()))
                            {
                                dbAdapter.insertInfo("emp1OTP","true",stTripId,getCurrentTime());
                                tvValOTP1.setText("On Board");
                                tvValOTP1.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
                                tvValOTP1.setTextColor(Color.parseColor("#1976d2"));
                                tvValOTP1.setEnabled(false);
                                emp1OTP=true;
                            }
                            else {

                                Toast.makeText(EmpDetailsActivity.this,"Invalid OTP",Toast.LENGTH_SHORT).show();
                            }
                            alertDialog.dismiss();
                        }
                    }
                });

            }
        });

        tvValOTP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmpDetailsActivity.this);

                final View dialogView = inflater.inflate(R.layout.alert_validate_otp, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                TextView tvOk=(TextView)dialogView.findViewById(R.id.avo_tv_ok);
                final EditText etOk=(EditText)dialogView.findViewById(R.id.avo_et_otp);
                TextView tvEmpName=(TextView)dialogView.findViewById(R.id.avo_tv_empName);

                final EmpData ed;
                ed=empList.get(1);

                tvEmpName.setText(ed.getEmpName());

                tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(etOk.getText().toString().trim().equals(""))
                        {
                            Toast.makeText(EmpDetailsActivity.this,"Please enter OTP!",Toast.LENGTH_SHORT).show();
                        }
                        else {

                            String otp=etOk.getText().toString().trim();

                            if(otp.equals(ed.getOtp()))
                            {
                                dbAdapter.insertInfo("emp2OTP","true",stTripId,getCurrentTime());
                                tvValOTP2.setText("On Board");
                                tvValOTP2.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
                                tvValOTP2.setTextColor(Color.parseColor("#1976d2"));
                                tvValOTP2.setEnabled(false);
                                emp2OTP=true;
                            }
                            else {

                                Toast.makeText(EmpDetailsActivity.this,"Invalid OTP",Toast.LENGTH_SHORT).show();
                            }
                            alertDialog.dismiss();

                        }
                    }
                });

            }
        });

        tvValOTP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmpDetailsActivity.this);

                final View dialogView = inflater.inflate(R.layout.alert_validate_otp, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                TextView tvOk=(TextView)dialogView.findViewById(R.id.avo_tv_ok);
                final EditText etOk=(EditText)dialogView.findViewById(R.id.avo_et_otp);
                TextView tvEmpName=(TextView)dialogView.findViewById(R.id.avo_tv_empName);

                final EmpData ed;
                ed=empList.get(2);

                tvEmpName.setText(ed.getEmpName());

                tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(etOk.getText().toString().trim().equals(""))
                        {
                            Toast.makeText(EmpDetailsActivity.this,"Please enter OTP!",Toast.LENGTH_SHORT).show();
                        }
                        else {

                            String otp=etOk.getText().toString().trim();

                            if(otp.equals(ed.getOtp()))
                            {
                                dbAdapter.insertInfo("emp3OTP","true",stTripId,getCurrentTime());
                                tvValOTP3.setText("On Board");
                                tvValOTP3.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
                                tvValOTP3.setTextColor(Color.parseColor("#1976d2"));
                                tvValOTP3.setEnabled(false);
                                emp3OTP=true;
                            }
                            else {

                                Toast.makeText(EmpDetailsActivity.this,"Invalid OTP",Toast.LENGTH_SHORT).show();
                            }
                            alertDialog.dismiss();

                        }
                    }
                });
            }
        });

        tvValOTP4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmpDetailsActivity.this);

                final View dialogView = inflater.inflate(R.layout.alert_validate_otp, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                TextView tvOk=(TextView)dialogView.findViewById(R.id.avo_tv_ok);
                final EditText etOk=(EditText)dialogView.findViewById(R.id.avo_et_otp);
                TextView tvEmpName=(TextView)dialogView.findViewById(R.id.avo_tv_empName);

                final EmpData ed;
                ed=empList.get(3);

                tvEmpName.setText(ed.getEmpName());

                tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(etOk.getText().toString().trim().equals(""))
                        {
                            Toast.makeText(EmpDetailsActivity.this,"Please enter OTP!",Toast.LENGTH_SHORT).show();
                        }
                        else {

                            String otp=etOk.getText().toString().trim();

                            if(otp.equals(ed.getOtp()))
                            {
                                dbAdapter.insertInfo("emp4OTP","true",stTripId,getCurrentTime());
                                tvValOTP4.setText("On Board");
                                tvValOTP4.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
                                tvValOTP4.setTextColor(Color.parseColor("#1976d2"));
                                tvValOTP4.setEnabled(false);
                                emp4OTP=true;
                            }
                            else {

                                Toast.makeText(EmpDetailsActivity.this,"Invalid OTP",Toast.LENGTH_SHORT).show();
                            }
                            alertDialog.dismiss();


                        }
                    }
                });


            }
        });

        tvValOTP5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmpDetailsActivity.this);

                final View dialogView = inflater.inflate(R.layout.alert_validate_otp, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                TextView tvOk=(TextView)dialogView.findViewById(R.id.avo_tv_ok);
                final EditText etOk=(EditText)dialogView.findViewById(R.id.avo_et_otp);
                TextView tvEmpName=(TextView)dialogView.findViewById(R.id.avo_tv_empName);

                final EmpData ed;
                ed=empList.get(4);

                tvEmpName.setText(ed.getEmpName());

                tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(etOk.getText().toString().trim().equals(""))
                        {
                            Toast.makeText(EmpDetailsActivity.this,"Please enter OTP!",Toast.LENGTH_SHORT).show();
                        }
                        else {

                            String otp=etOk.getText().toString().trim();

                            if(otp.equals(ed.getOtp()))
                            {
                                dbAdapter.insertInfo("emp5OTP","true",stTripId,getCurrentTime());
                                tvValOTP4.setText("On Board");
                                tvValOTP4.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
                                tvValOTP4.setTextColor(Color.parseColor("#1976d2"));
                                tvValOTP4.setEnabled(false);
                                emp4OTP=true;
                            }
                            else {

                                Toast.makeText(EmpDetailsActivity.this,"Invalid OTP",Toast.LENGTH_SHORT).show();
                            }
                            alertDialog.dismiss();


                        }
                    }
                });


            }
        });

        tvValOTP6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmpDetailsActivity.this);

                final View dialogView = inflater.inflate(R.layout.alert_validate_otp, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                TextView tvOk=(TextView)dialogView.findViewById(R.id.avo_tv_ok);
                final EditText etOk=(EditText)dialogView.findViewById(R.id.avo_et_otp);
                TextView tvEmpName=(TextView)dialogView.findViewById(R.id.avo_tv_empName);

                final EmpData ed;
                ed=empList.get(5);

                tvEmpName.setText(ed.getEmpName());

                tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(etOk.getText().toString().trim().equals(""))
                        {
                            Toast.makeText(EmpDetailsActivity.this,"Please enter OTP!",Toast.LENGTH_SHORT).show();
                        }
                        else {

                            String otp=etOk.getText().toString().trim();

                            if(otp.equals(ed.getOtp()))
                            {
                                dbAdapter.insertInfo("emp6OTP","true",stTripId,getCurrentTime());
                                tvValOTP4.setText("On Board");
                                tvValOTP4.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
                                tvValOTP4.setTextColor(Color.parseColor("#1976d2"));
                                tvValOTP4.setEnabled(false);
                                emp4OTP=true;
                            }
                            else {

                                Toast.makeText(EmpDetailsActivity.this,"Invalid OTP",Toast.LENGTH_SHORT).show();
                            }
                            alertDialog.dismiss();

                        }
                    }
                });
            }
        });

        tvValOTP7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EmpDetailsActivity.this);

                final View dialogView = inflater.inflate(R.layout.alert_validate_otp, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                TextView tvOk=(TextView)dialogView.findViewById(R.id.avo_tv_ok);
                final EditText etOk=(EditText)dialogView.findViewById(R.id.avo_et_otp);
                TextView tvEmpName=(TextView)dialogView.findViewById(R.id.avo_tv_empName);

                final EmpData ed;
                ed=empList.get(6);

                tvEmpName.setText(ed.getEmpName());

                tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(etOk.getText().toString().trim().equals(""))
                        {
                            Toast.makeText(EmpDetailsActivity.this,"Please enter OTP!",Toast.LENGTH_SHORT).show();
                        }
                        else {

                            String otp=etOk.getText().toString().trim();

                            if(otp.equals(ed.getOtp()))
                            {
                                dbAdapter.insertInfo("emp7OTP","true",stTripId,getCurrentTime());
                                tvValOTP4.setText("On Board");
                                tvValOTP4.setBackground(getResources().getDrawable(R.drawable.rect_stroke_blue_bg));
                                tvValOTP4.setTextColor(Color.parseColor("#1976d2"));
                                tvValOTP4.setEnabled(false);
                                emp4OTP=true;
                            }
                            else {

                                Toast.makeText(EmpDetailsActivity.this,"Invalid OTP",Toast.LENGTH_SHORT).show();
                            }
                            alertDialog.dismiss();

                        }
                    }
                });
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
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

    */
}
