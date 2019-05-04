package com.hjsoft.emptransport.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.model.AllTripsData;
import com.hjsoft.emptransport.model.DutyData;
import com.hjsoft.emptransport.webservices.API;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by hjsoft on 22/6/17.
 */
public class AllRidesRecyclerAdapter extends RecyclerView.Adapter<AllRidesRecyclerAdapter.MyViewHolder> {

    ArrayList<AllTripsData> customArrayList;
    Context context;
    AllTripsData data;
    LayoutInflater inflater;
    API REST_CLIENT;
    Dialog dialog;
    int pos;
    DBAdapter dbAdapter;
    String companyId="CMP0001";
    private RidesAdapterCallback mRidesAdapterCallback;

    public AllRidesRecyclerAdapter(Context context, ArrayList<AllTripsData> customArrayList)
    {
        this.context=context;
        this.customArrayList=customArrayList;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog=new Dialog(context);
        try {
            this.mRidesAdapterCallback = ((RidesAdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement RidesAdapterCallback.");
        }

        dbAdapter=new DBAdapter(context);
        dbAdapter=dbAdapter.open();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_rides_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        data=customArrayList.get(position);

        holder.lLayout.setTag(position);

        try {
            SimpleDateFormat newformat = new SimpleDateFormat("d MMM yyyy");
            String datestring = data.getTripDate().split("T")[0];
            SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd");
            String reformattedStr = newformat.format(oldformat.parse(datestring));
            //holder.tvTripDate.setText(reformattedStr);

            //String a[]=reformattedStr.split(" ");
            holder.tvDate.setText(reformattedStr);

        }catch(ParseException e){e.printStackTrace();}

        String r[]=data.getRouteName().split("-");

        holder.tvRouteName.setText(r[0]+" - "+r[1]);
        holder.tvKms.setText(String.valueOf(data.getGpsToKms()));
        holder.tvHrs.setText(String.valueOf(data.getGpdToHrs()));

        if(data.getDriverDutyStatus().equals("Y"))
        {
            holder.tvStatus.setText("Completed");
        }
        else  if(data.getDriverDutyStatus().equals("A"))
        {
            holder.tvStatus.setText("Ongoing");
            holder.tvKms.setText("-");
            holder.tvHrs.setText("-");
        }
        else {
            holder.tvStatus.setText("Not yet started");
        }

        holder.lLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos;

                try {
                    pos= (int) holder.lLayout.getTag();
                    //Toast.makeText(context,pos+":::",Toast.LENGTH_SHORT).show();
                    mRidesAdapterCallback.onMethodCallback(pos,customArrayList);
                }
                catch (ClassCastException e)
                {
                    e.printStackTrace();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return customArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate,tvStatus,tvRouteName,tvKms,tvHrs;
        LinearLayout lLayout;

        public MyViewHolder(final View itemView) {
            super(itemView);

            tvDate=(TextView)itemView.findViewById(R.id.arr_tv_date);
            tvStatus=(TextView)itemView.findViewById(R.id.arr_tv_status);
            tvRouteName=(TextView)itemView.findViewById(R.id.arr_tv_route_name);
            tvKms=(TextView)itemView.findViewById(R.id.arr_tv_kms);
            tvHrs=(TextView)itemView.findViewById(R.id.arr_tv_hrs);
            lLayout=(LinearLayout)itemView.findViewById(R.id.arr_ll);
        }
    }


    public static interface RidesAdapterCallback {
        void onMethodCallback(int position, ArrayList<AllTripsData> data);
    }



}
