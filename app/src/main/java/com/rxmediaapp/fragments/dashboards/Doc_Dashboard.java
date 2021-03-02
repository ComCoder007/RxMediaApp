package com.rxmediaapp.fragments.dashboards;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Doc_Dashboard extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    LinearLayout new_actionbar_lay;
    public static RecyclerView franchise_recyler;
    public static TextView nodatavailable_txt;
    public static HashMapRecycleviewadapter adapter;
    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.doc_dashboard,null,false );
        StoredObjects.page_type="home";

        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        serviceCalling();
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        new_actionbar_lay=v.findViewById(R.id.new_actionbar_lay);
        new_actionbar_lay.setVisibility(View.GONE);
        franchise_recyler = v.findViewById( R.id. ass_franchise_recyler);

        nodatavailable_txt = v.findViewById(R.id.nodatavailable_txt);

        title_txt.setText("Dash Board");

        final GridLayoutManager linearLayoutManager=new GridLayoutManager(getActivity(),2);
        franchise_recyler.setLayoutManager(linearLayoutManager);
       // StoredObjects.hashmaplist(7);


    }

    private void serviceCalling() {
        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            DashBoardService(getActivity());
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }

    }


    private void DashBoardService(final Activity activity) {

        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.Dashboard(RetrofitInstance.dashboard, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseReceived = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseReceived);
                        StoredObjects.LogMethod("response", "response::" + responseReceived);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("200")) {
                            String results = jsonObject.getString("results");
                            data_list = JsonParsing.GetJsonData(results);

                            adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "doc_dashboard", franchise_recyler, R.layout.dashboard_listitem);
                            franchise_recyler.setAdapter(adapter);

                        } else {
                            // CustomProgressbar.Progressbarcancel(activity);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
                CustomProgressbar.Progressbarcancel(activity);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                CustomProgressbar.Progressbarcancel(activity);


            }
        });

    }



    public void fragmentcallinglay(Fragment fragment) {
        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();
    }

}

