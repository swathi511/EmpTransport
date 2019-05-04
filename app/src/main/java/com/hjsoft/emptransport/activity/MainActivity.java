package com.hjsoft.emptransport.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.SessionManager;
import com.hjsoft.emptransport.model.Pojo;
import com.hjsoft.emptransport.webservices.API;
import com.hjsoft.emptransport.webservices.RestClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 25/5/17.
 */
public class MainActivity extends AppCompatActivity {

    EditText etUsername,etPassword;
    Button btLogin;
    String stUname,stPwd;
    API REST_CLIENT;
    SessionManager session;
    HashMap<String, String> user;
    String uname,pwd;
    ProgressDialog progressDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sp";
    String version="12",companyId="CMP0001";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session=new SessionManager(MainActivity.this);

        etUsername=(EditText)findViewById(R.id.am_et_uname);
        etPassword=(EditText)findViewById(R.id.am_et_pwd);
        btLogin=(Button)findViewById(R.id.am_bt_login);

        //Santosh Kumar 1234567890

        REST_CLIENT= RestClient.get();

        user = session.getUserDetails();

        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        uname=user.get(SessionManager.KEY_NAME);
        pwd=user.get(SessionManager.KEY_PWD);

        if(session.isLoggedIn())
        {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ...");
            progressDialog.show();


            JsonObject v=new JsonObject();
            v.addProperty("login",uname);
            v.addProperty("pwd",pwd);
            v.addProperty("version",version);
            v.addProperty("companycode",companyId);

            Call<Pojo> call=REST_CLIENT.validate(v);
            call.enqueue(new Callback<Pojo>() {
                @Override
                public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                    progressDialog.dismiss();

                    Pojo content;


                    if(response.isSuccessful())
                    {

                        content=response.body();

                        session.createLoginSession(uname,pwd);
                        System.out.println("driver id isssssssssss "+content.getMessage());
                        editor.putString("driverId",content.getMessage());
                        editor.commit();


                        // Toast.makeText(MainActivity.this,"Valid details!",Toast.LENGTH_LONG).show();
                        Intent i=new Intent(MainActivity.this,AfterLoginActivity.class);
                        startActivity(i);
                        finish();

                    }
                    else {

                        String msg=response.message();

                        if(msg.equals("Old Version")) {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);

                            LayoutInflater inflater = getLayoutInflater();
                            final View dialogView = inflater.inflate(R.layout.alert_version, null);
                            dialogBuilder.setView(dialogView);

                            Button ok = (Button) dialogView.findViewById(R.id.av_bt_ok);

                            final AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    alertDialog.dismiss();
                                    finish();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<Pojo> call, Throwable t) {

                    progressDialog.dismiss();

                    Toast.makeText(MainActivity.this,"Check Internet Connection!",Toast.LENGTH_LONG).show();

                    finish();

                }
            });

        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //balreddys 123456789012

                stUname=etUsername.getText().toString().trim();
                stPwd=etPassword.getText().toString().trim();

                if(stUname.equals(""))
                {
                    Toast.makeText(MainActivity.this,"Enter Username!",Toast.LENGTH_SHORT).show();

                }
                else if(stPwd.equals(""))
                {
                    Toast.makeText(MainActivity.this,"Enter Password",Toast.LENGTH_LONG).show();
                }
                else {

                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Please wait ...");
                    progressDialog.show();

                    JsonObject v=new JsonObject();
                    v.addProperty("login",stUname);
                    v.addProperty("pwd",stPwd);
                    v.addProperty("version",version);
                    v.addProperty("companycode",companyId);

                    Call<Pojo> call=REST_CLIENT.validate(v);
                    call.enqueue(new Callback<Pojo>() {
                        @Override
                        public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                            progressDialog.dismiss();
                            Pojo content;

                            if(response.isSuccessful())
                            {
                                content=response.body();
                                session.createLoginSession(stUname,stPwd);
                                editor.putString("driverId",content.getMessage());
                                editor.commit();

                               // Toast.makeText(MainActivity.this,"Valid details!",Toast.LENGTH_LONG).show();
                                Intent i=new Intent(MainActivity.this,AfterLoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else {

                                String msg=response.message();

                                if(msg.equals("Old Version"))
                                {
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);

                                    LayoutInflater inflater = getLayoutInflater();
                                    final View dialogView = inflater.inflate(R.layout.alert_version, null);
                                    dialogBuilder.setView(dialogView);

                                    Button ok = (Button) dialogView.findViewById(R.id.av_bt_ok);

                                    final AlertDialog alertDialog = dialogBuilder.create();
                                    alertDialog.show();
                                    alertDialog.setCanceledOnTouchOutside(false);
                                    alertDialog.setCancelable(false);

                                    ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            alertDialog.dismiss();
                                            finish();
                                        }
                                    });
                                }
                                else {

                                    Toast.makeText(MainActivity.this,"Invalid details!",Toast.LENGTH_SHORT).show();
                                    etUsername.setText("");
                                    etPassword.setText("");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Pojo> call, Throwable t) {

                            progressDialog.dismiss();

                            Toast.makeText(MainActivity.this,"Check Internet Connection!",Toast.LENGTH_LONG).show();

                            finish();

                        }
                    });
                }


            }
        });


    }
}
