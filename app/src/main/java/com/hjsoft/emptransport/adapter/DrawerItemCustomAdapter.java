package com.hjsoft.emptransport.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjsoft.emptransport.R;
import com.hjsoft.emptransport.model.NavigationData;

/**
 * Created by hjsoft on 28/2/17.
 */
public class DrawerItemCustomAdapter extends ArrayAdapter<NavigationData> {

    Context mContext;
    int layoutResourceId;
    NavigationData data[] = null;
    private int mSelectedItem;

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, NavigationData[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);

        NavigationData folder = data[position];
        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);

        if(position==mSelectedItem)
        {
            textViewName.setTextColor(Color.parseColor("#39a799"));
            textViewName.setTypeface(Typeface.DEFAULT_BOLD);
            textViewName.setTextSize(14);
        }
        else {
            textViewName.setTextColor(Color.parseColor("#000000"));
            textViewName.setTextSize(14);
        }

        return listItem;
    }

    public int getSelectedItem() {
        return mSelectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        mSelectedItem = selectedItem;
    }
}