package com.hjsoft.emptransport.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.model.DutyData;
import com.hjsoft.emptransport.model.Pojo;
import com.hjsoft.emptransport.webservices.API;
import com.hjsoft.emptransport.webservices.RestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 26/5/17.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    ArrayList<DutyData> customArrayList;
    Context context;
    DutyData data;
    LayoutInflater inflater;
    boolean accept=false;
    API REST_CLIENT;
    Dialog dialog;
    private AdapterCallback mAdapterCallback;
    int pos;
    ArrayList<DutyData> mResultList;
    boolean status=false;
    DBAdapter dbAdapter;
    String companyId="CMP0001";


    public RecyclerAdapter(Context context, ArrayList<DutyData> customArrayList)
    {
        this.context=context;
        this.customArrayList=customArrayList;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog=new Dialog(context);
        try {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }

        dbAdapter=new DBAdapter(context);
        dbAdapter=dbAdapter.open();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        data=customArrayList.get(position);

        //holder.tvTripId.setText(data.getTripId());
        // holder.tvTripDate.setText(data.getTripDate());

        holder.lLayout.setTag(position);

        final Animation animation = new AlphaAnimation(1,0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);

        //holder.tvAccept.startAnimation(animation);
        holder.tvStart.startAnimation(animation);


        String s=data.getTripDate();
        try {
            SimpleDateFormat newformat = new SimpleDateFormat("d MMM");
            String datestring = s.split("T")[0];
            SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd");
            String reformattedStr = newformat.format(oldformat.parse(datestring));
            //holder.tvTripDate.setText(reformattedStr);

            String a[]=reformattedStr.split(" ");
            holder.tvDate.setText(a[0]);
            holder.tvMnth.setText(a[1]);

        }catch(ParseException e){e.printStackTrace();}

        holder.tvRouteName.setText(data.getRouteName());

        if(dbAdapter.findTripInfo(data.getTripId()))
        {
            holder.tvStart.setText("Cont.");
        }


        if(data.getDriverDutyStatus().equals("A"))
        {
            holder.tvAccept.setVisibility(View.GONE);
            holder.tvStart.setVisibility(View.VISIBLE);
        }

        holder.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos;

                try {
                    pos= (int) holder.lLayout.getTag();
                   // Toast.makeText(context,pos+":::",Toast.LENGTH_SHORT).show();
                    mAdapterCallback.onMethodCallback(pos,customArrayList);
                }
                catch (ClassCastException e)
                {
                    e.printStackTrace();
                }
            }
        });

        holder.tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final int p=(int)holder.lLayout.getTag();

                final ProgressDialog  progressDialog = new ProgressDialog(context);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait ...");
                progressDialog.show();

                final DutyData d;
                d=customArrayList.get(p);

                JsonObject v=new JsonObject();
                v.addProperty("tripid",d.getTripId());
                v.addProperty("driverid",d.getDriverId());
                v.addProperty("tripdate",d.getTripDate());
                v.addProperty("companyid",companyId);

                REST_CLIENT= RestClient.get();

                Call<Pojo> call=REST_CLIENT.acceptDuty(v);
                call.enqueue(new Callback<Pojo>() {
                    @Override
                    public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                        if(response.isSuccessful())
                        {
                            Pojo res=response.body();

                            progressDialog.dismiss();

                            //System.out.println("******** updated *******"+p+d.getTripId());

                            if(res.getMessage().equals("updated"))
                            {
                                holder.tvAccept.setVisibility(View.GONE);
                                holder.tvStart.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Pojo> call, Throwable t) {

                        progressDialog.dismiss();

                        Toast.makeText(context,"Internet Connectivity error! Please try again.",Toast.LENGTH_LONG).show();

                    }
                });


            }
        });

        /*
        holder.tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int p=(int)holder.rlLayout.getTag();

                final ProgressDialog  progressDialog = new ProgressDialog(context);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait ...");
                progressDialog.show();

                final DutyData d;
                d=customArrayList.get(p);

                JsonObject v=new JsonObject();
                v.addProperty("tripid",d.getTripId());
                v.addProperty("driverid",d.getDriverId());
                v.addProperty("tripdate",d.getTripDate());

                REST_CLIENT= RestClient.get();

                Call<Pojo> call=REST_CLIENT.acceptDuty(v);
                call.enqueue(new Callback<Pojo>() {
                    @Override
                    public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                        if(response.isSuccessful())
                        {
                            Pojo res=response.body();

                            progressDialog.dismiss();

                            System.out.println("******** updated *******"+p+d.getTripId());

                            if(res.getMessage().equals("updated"))
                            {
                               holder.tvAccept.setVisibility(View.GONE);
                                holder.tvStart.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Pojo> call, Throwable t) {

                        progressDialog.dismiss();

                        Toast.makeText(context,"Internet Connectivity error! Please try again.",Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

        holder.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos=(int)holder.rlLayout.getTag();

                try {
                    pos= (int) holder.rlLayout.getTag();
                    Toast.makeText(context,pos+":::",Toast.LENGTH_SHORT).show();
                    mAdapterCallback.onMethodCallback(pos,customArrayList);
                }
                catch (ClassCastException e)
                {
                    e.printStackTrace();
                }



            }
        });*/

    }

    @Override
    public int getItemCount() {
        return customArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //        TextView tvTripId,tvTripDate,tvVehCat,tvVehRegNo,tvRoutName;
//        TextView tvAccept,tvStart;
        LinearLayout lLayout;
        TextView tvDate,tvMnth,tvRouteName;
        ImageView ivTick;
        TextView tvAccept,tvStart;

        public MyViewHolder(final View itemView) {
            super(itemView);

//            tvTripId=(TextView)itemView.findViewById(R.id.rw_tv_trip);
//            tvTripDate=(TextView)itemView.findViewById(R.id.rw_tv_tdate);
//            tvVehCat=(TextView)itemView.findViewById(R.id.rw_tv_vcat);
//            tvVehRegNo=(TextView)itemView.findViewById(R.id.rw_tv_vreg);
//            tvRoutName=(TextView)itemView.findViewById(R.id.rw_tv_rname);
//            tvAccept=(TextView)itemView.findViewById(R.id.rw_tv_accept);
//            tvStart=(TextView)itemView.findViewById(R.id.rw_tv_start);
            // rlLayout=(RelativeLayout)itemView.findViewById(R.id.rw_rlayout);

            tvDate=(TextView)itemView.findViewById(R.id.rn_tv_date);
            tvMnth=(TextView)itemView.findViewById(R.id.rn_tv_month);
            tvRouteName=(TextView)itemView.findViewById(R.id.rn_tv_route_name);
            lLayout=(LinearLayout) itemView.findViewById(R.id.rn_ll);
            tvAccept=(TextView)itemView.findViewById(R.id.rn_tv_accept);
            tvStart=(TextView)itemView.findViewById(R.id.rn_tv_start);


        }
    }

    public static interface AdapterCallback {
        void onMethodCallback(int position, ArrayList<DutyData> data);
    }
}
