package com.hjsoft.emptransport.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjsoft.emptransport.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hjsoft on 22/6/17.
 */
public class AllRidesExpandableListAdapter extends BaseExpandableListAdapter {


    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    DBAdapter dbAdapter;
    String stTripId;

    public AllRidesExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData,String stTripId) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.stTripId=stTripId;
        dbAdapter=new DBAdapter(context);
        dbAdapter=dbAdapter.open();

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        ImageView ivMark=(ImageView)convertView.findViewById(R.id.mark);
        final LinearLayout lLayout=(LinearLayout)convertView.findViewById(R.id.lLayout);
        ImageView ivCall=(ImageView)convertView.findViewById(R.id.ll_call);
        TextView tvRepDate=(TextView)convertView.findViewById(R.id.lblListItem1);

        lLayout.setTag(childText);

        ivMark.setVisibility(View.GONE);
        String stTripId=dbAdapter.getTrip();

        // System.out.println("****************************************"+stTripId+childText);


        String a[]=childText.split("@");

        txtListChild.setText(a[0]);

        String b[]=a[1].split("#");

        // System.out.println("Checking here");

        if(b.length>1) {

//            if (b[1].isEmpty()) {
//
//                tvRepDate.setText(" ");
//            } else {

            if(b[1].equals("0"))
            {
                tvRepDate.setText(" - ");
            }
            else {

                DecimalFormat formatter = new DecimalFormat("00.00");
                String aFormatted = formatter.format(Double.parseDouble(b[1]));
                tvRepDate.setText(aFormatted);
                // System.out.println();
            }

        }
        else {


            tvRepDate.setText(" - ");
        }

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tag=(String)lLayout.getTag();

                // System.out.println("tag is "+tag);

                String p[]=tag.split("@");
                String q[]=p[1].split("#");
                String driverMobile=q[0];

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+driverMobile));//GUEST NUMBER HERE...
                _context.startActivity(intent);

            }
        });

        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);
        final TextView tvArrived=(TextView)convertView.findViewById(R.id.lg_tv_arrived);

        tvArrived.setVisibility(View.GONE);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setNewItems(List<String> listDataHeader,HashMap<String, List<String>> listChildData) {
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        notifyDataSetChanged();
    }




}
