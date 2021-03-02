package com.rxmediaapp.fragments.assistant;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
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
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionsList extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
   TextView nodatavailable_txt;
     RecyclerView p_prescription_recyler;
    ArrayList<HashMap<String, String>> prescr_list = new ArrayList<>();
    HashMapRecycleviewadapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.prescriptionslist,null,false );
        StoredObjects.page_type="prescriptionslist";
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);


        serviceCalling();
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        p_prescription_recyler = v.findViewById( R.id. p_prescription_recyler);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);

        title_txt.setText( "Prescriptions List" );


        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        });



        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
       p_prescription_recyler.setLayoutManager(linearLayoutManager);




    }


    private void serviceCalling() {

        if (InterNetChecker.isNetworkAvailable(getActivity())) {

            PrescriptionService(getActivity());
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }


    private void PrescriptionService(final Activity activity) {

        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);

        api.Prscriptionslist(RetrofitInstance.assistant_prescription, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
                            prescr_list = JsonParsing.GetJsonData(results);

                            adapter = new HashMapRecycleviewadapter(getActivity(), prescr_list, "prescriptionslist", p_prescription_recyler, R.layout.prescriptions_lstitem);
                            p_prescription_recyler.setAdapter(adapter);

                        } else {
                            prescr_list.clear();

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    updatelay(prescr_list);
                    CustomProgressbar.Progressbarcancel(activity);


                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomProgressbar.Progressbarcancel(activity);

            }
        });

    }


    private void updatelay(ArrayList<HashMap<String, String>> data_list) {

        if(data_list.size()==0){
            nodatavailable_txt.setVisibility(View.VISIBLE);
            p_prescription_recyler.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            p_prescription_recyler.setVisibility(View.VISIBLE);
        }
    }

    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

    }




}

