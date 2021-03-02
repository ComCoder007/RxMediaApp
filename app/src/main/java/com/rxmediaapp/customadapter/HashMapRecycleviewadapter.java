package com.rxmediaapp.customadapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by VINI on 02-11-2018.
 */

public class HashMapRecycleviewadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements Filterable {

    Activity activity;
    ArrayList<HashMap<String, String>> datalist=new ArrayList<>();
    String formtype;
    int list_itemview;

    private ItemFilter mFilter = new ItemFilter();


    public HashMapRecycleviewadapter(Activity activity2, ArrayList<HashMap<String, String>> data_list, String string, RecyclerView recyclerView, int recylerviewlistitem) { //ArrayList<HashMap<String, String>> data_list

        this.datalist = data_list;
        this.activity = activity2;
        formtype = string;
        list_itemview=recylerviewlistitem;


    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(list_itemview, parent, false);
        return new HashmapViewHolder(view,formtype,activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
        if (holder1 instanceof HashmapViewHolder) {
            HashmapViewHolder holder = (HashmapViewHolder) holder1;
            holder.assign_data(datalist,position,formtype);


        }
    }

    @Override
    public int getItemCount() {
        return datalist == null ? 0 : datalist.size();
    }




    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            ArrayList<HashMap<String, String>> list = datalist;
            int count = list.size();
            ArrayList<HashMap<String, String>> nlist=new ArrayList<>();
            String filterableString = null;
            for (int i = 0; i < count; i++) {

                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(datalist.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            datalist = (ArrayList<HashMap<String, String>>) results.values;
            notifyDataSetChanged();


        }

    }

}
