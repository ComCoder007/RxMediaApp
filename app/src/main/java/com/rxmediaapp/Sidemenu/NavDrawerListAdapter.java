package com.rxmediaapp.Sidemenu;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;


import com.rxmediaapp.R;
import com.rxmediaapp.storedobjects.StoredObjects;

import java.util.ArrayList;

/**
 * Created by android-4 on 5/31/2018.
 */

public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    public static TextView txtTitle,ntfc_count_txt;
    public static LinearLayout navigation_lay;
    View selview_bg;

    public static ImageView icon;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
            context.getSystemService( Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate( R.layout.sidemenu_drawablelist, null,false);
        }

        txtTitle=(TextView) convertView.findViewById(R.id.title);
        //ntfc_count_txt = (TextView) convertView.findViewById(R.id.ntfc_count_txt);
        ImageView image_icon= (ImageView) convertView.findViewById(R.id.image_icon);
        navigation_lay = (LinearLayout) convertView.findViewById(R.id.navigation_lay);
        selview_bg=convertView.findViewById(R.id.selview_bg);


        txtTitle.setText(navDrawerItems.get(position).getTitle());
        image_icon.setImageResource(navDrawerItems.get(position).getimage());



        if(StoredObjects.listcount == position){
            selview_bg.setVisibility(View.VISIBLE);
        }else{
            selview_bg.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }

}

