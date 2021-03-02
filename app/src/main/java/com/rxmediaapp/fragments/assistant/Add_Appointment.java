package com.rxmediaapp.fragments.assistant;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.fragments.dashboards.Asst_Dashboard;
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


public class Add_Appointment extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    static RecyclerView addapointment_recycle;
    public static HashMapRecycleviewadapter adapter;
    LinearLayout new_actionbar_lay;
    public static TextView nodatavailable_txt;

    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.add_appointment,null,false );
        StoredObjects.page_type="add_apmnt";

        SideMenu.updatemenu(StoredObjects.page_type);
        try {
            StoredObjects.listcount= 1;
            SideMenu.adapter.notifyDataSetChanged();
        }catch (Exception e){

        }
        initilization(v);

        serviceCalling();
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        addapointment_recycle = v.findViewById( R.id. addapointment_recycle);
        new_actionbar_lay=v.findViewById(R.id.new_actionbar_lay);
        new_actionbar_lay.setVisibility(View.VISIBLE);
        title_txt.setText( "Add Appointment" );
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);

        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcallinglay( new Asst_Dashboard() );
            }
        } );


        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        addapointment_recycle.setLayoutManager(linearLayoutManager);


    }
    private void serviceCalling() {

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            AddAppointmentService(getActivity());
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }

    private void AddAppointmentService(final Activity activity) {

            CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.AddAppointment(RetrofitInstance.add_appointment, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
                            adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "addapointment", addapointment_recycle, R.layout.add_appointment_listitems);
                            addapointment_recycle.setAdapter(adapter);


                            updatelay(data_list);
                        } else {
                            updatelay(data_list);
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
    public static void updatelay(ArrayList<HashMap<String, String>> data_list) {

        if(data_list.size()==0){
            nodatavailable_txt.setVisibility(View.VISIBLE);
            addapointment_recycle.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            addapointment_recycle.setVisibility(View.VISIBLE);
        }
    }

    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ().replace (R.id.frame_container , fragment).commit ();

    }




}


