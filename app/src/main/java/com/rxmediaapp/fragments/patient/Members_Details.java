package com.rxmediaapp.fragments.patient;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

public class Members_Details extends Fragment {

    ImageView backbtn_img,p_member_img;
    TextView title_txt,p_mem_relation_txt,p_mem_mobile_txt,p_mem_age_txt,p_mem_gender_txt,p_mem_bgroup_txt;
    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> o_data_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> d_data_list = new ArrayList<>();
    static RecyclerView doc_prescription_recyler,prescription_recyler;
    public static TextView nodatavailable_txt,d_nodatavailable_txt;

    String user_id="",patient_id="";

    public static Button prescription_button,doc_prescription_button;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.p_members_details,null,false );
        StoredObjects.page_type="members_details";

        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        assignData();
        return v;
    }

    private void assignData() {
        try {
            p_mem_relation_txt.setText(data_list.get(0).get("relation"));
            p_mem_mobile_txt.setText(data_list.get(0).get("phone"));
            p_mem_age_txt.setText(data_list.get(0).get("age"));
            p_mem_gender_txt.setText(data_list.get(0).get("gender"));
            p_mem_bgroup_txt.setText(data_list.get(0).get("blood_group"));
            user_id=data_list.get(0).get("user_id");
            patient_id=data_list.get(0).get("user_id");


            try {
                Glide.with(getActivity())
                        .load(Uri.parse(RetrofitInstance.IMAGE_URL + data_list.get(0).get("image")))
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.no_image)
                                .fitCenter()
                                .centerCrop())
                        .into(p_member_img);
            } catch (Exception e) {
                e.printStackTrace();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (InterNetChecker.isNetworkAvailable(getContext())) {

            prescriptionListService(getActivity(),1);
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
        }
    }
    private void prescriptionListService(final Activity activity , final int pagecount) {

        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.PrescriptionList(RetrofitInstance.prescriptions_list, "", "", "Own Prescriptions", "", StoredObjects.UserId, StoredObjects.UserRoleId,"1","100",patient_id ).enqueue(new Callback<ResponseBody>() {
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
                            o_data_list = JsonParsing.GetJsonData(results);
                            prescription_recyler.setAdapter(new HashMapRecycleviewadapter(getActivity(), o_data_list, "subuser_prescr", prescription_recyler, R.layout.members_details_lstitem));

                        } else {
                            if (pagecount == 1) {
                                o_data_list.clear();
                            }

                        }
                        updatelay(o_data_list, "Own Prescriptions");
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                    if (InterNetChecker.isNetworkAvailable(getContext())) {

                        doc_prescriptionListService(getActivity(), 1);
                    } else {
                        StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (pagecount == 1) {
                    CustomProgressbar.Progressbarcancel(activity);
                }

            }
        });

    }
    private void doc_prescriptionListService(final Activity activity , final int pagecount) {


        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.PrescriptionList(RetrofitInstance.prescriptions_list, "", "", "Doctor Prescriptions", "", StoredObjects.UserId, StoredObjects.UserRoleId,"1","100",patient_id ).enqueue(new Callback<ResponseBody>() {
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
                            d_data_list = JsonParsing.GetJsonData(results);
                            doc_prescription_recyler.setAdapter(new HashMapRecycleviewadapter(getActivity(), d_data_list, "subuserdoc_prescr", doc_prescription_recyler, R.layout.doc_prescription_lstitem));


                        } else {
                            if (pagecount == 1) {
                                d_data_list.clear();
                            }

                        }
                        updatelay(d_data_list, "Doctor Prescriptions");
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
                CustomProgressbar.Progressbarcancel(activity);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (pagecount == 1) {
                    CustomProgressbar.Progressbarcancel(activity);
                }

            }
        });

    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        prescription_recyler = v.findViewById( R.id. prescription_recyler);
        doc_prescription_recyler = v.findViewById(R.id.doc_prescription_recyler);
        p_member_img = v.findViewById( R.id. p_member_img);
        p_mem_relation_txt= v.findViewById( R.id. p_mem_relation_txt);
        p_mem_mobile_txt = v.findViewById( R.id. p_mem_mobile_txt);
        p_mem_age_txt = v.findViewById(R.id.p_mem_age_txt);
        p_mem_gender_txt = v.findViewById( R.id. p_mem_gender_txt);
        p_mem_bgroup_txt = v.findViewById(R.id.p_mem_bgroup_txt);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        d_nodatavailable_txt=v.findViewById(R.id.d_nodatavailable_txt);
        prescription_button=v.findViewById(R.id.prescription_button);
        doc_prescription_button=v.findViewById(R.id.doc_prescription_button);



        title_txt.setText( "Members Details" );


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
        prescription_recyler.setLayoutManager(linearLayoutManager);


        final LinearLayoutManager linearLayoutManagerone=new LinearLayoutManager(getActivity());
        doc_prescription_recyler.setLayoutManager(linearLayoutManagerone);

    }

    public static void updatelay(ArrayList<HashMap<String, String>> data_list,String type) {

        if(type.equalsIgnoreCase("Own Prescriptions")){
            if(data_list.size()==0){


                nodatavailable_txt.setVisibility(View.VISIBLE);
                prescription_recyler.setVisibility(View.GONE);
            }else{
                nodatavailable_txt.setVisibility(View.GONE);
                prescription_recyler.setVisibility(View.VISIBLE);
            }
        }else{
            if(data_list.size()==0){
                d_nodatavailable_txt.setVisibility(View.VISIBLE);
                doc_prescription_recyler.setVisibility(View.GONE);
            }else{
                d_nodatavailable_txt.setVisibility(View.GONE);
                doc_prescription_recyler.setVisibility(View.VISIBLE);
            }
        }

    }

    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

    }




}

