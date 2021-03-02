package com.rxmediaapp.fragments.hospital;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class H_Patient_Details extends Fragment {

    ImageView backbtn_img,patient_img;
    TextView title_txt,patientname_txt,enrole_txt,mobilenumbr_txt,age_txt,sex_txt,h_problem_txt;
    public static TextView nodatavailable_txt;
    public static  RecyclerView p_prescription_recyler,p_doc_prescription_recyler;
    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
    public static HashMapRecycleviewadapter adapter,adapterone;
    int pagecount=1,totalpages=0;
    String recordsperpage="10";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.h_patient_details,null,false );
        StoredObjects.page_type="patient_details";
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        pagecount=1;
        serviceCalling();
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        p_prescription_recyler = v.findViewById( R.id. p_prescription_recyler);
        p_doc_prescription_recyler = v.findViewById(R.id.p_doc_prescription_recyler);
        patient_img = v.findViewById( R.id. patient_img);
        patientname_txt= v.findViewById( R.id. patientname_txt);
        enrole_txt = v.findViewById( R.id. enrole_txt);
        mobilenumbr_txt = v.findViewById(R.id.mobilenumbr_txt);
        age_txt = v.findViewById( R.id. age_txt);
        sex_txt = v.findViewById(R.id.sex_txt);
        h_problem_txt = v.findViewById(R.id.h_problem_txt);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);

        title_txt.setText( "Patient Details" );


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

        p_prescription_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                    pagecount=pagecount+1;
                    if(pagecount<=totalpages){
                        serviceCalling();
                    }

                }
            }
        });



        final LinearLayoutManager linearLayoutManagerone=new LinearLayoutManager(getActivity());
        p_doc_prescription_recyler.setLayoutManager(linearLayoutManagerone);

        p_doc_prescription_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapterone.getItemCount() - 1) {

                    pagecount=pagecount+1;
                    if(pagecount<=totalpages){
                        serviceCalling();
                    }

                }
            }
        });

    }


    private void serviceCalling() {

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            PersonalPrescriptionsService(getActivity(),pagecount,recordsperpage);
            DoctorsPrescriptionsService(getActivity(),pagecount,recordsperpage);
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }

    private void PersonalPrescriptionsService(final Activity activity, final int pagecount, String recordsperpage) {
        if(pagecount==1){
            CustomProgressbar.Progressbarshow(activity);
        }

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.PersonalPrescriptions(RetrofitInstance.personal_prescriptions, StoredObjects.UserId, StoredObjects.UserRoleId,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
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
                            String total_pages = jsonObject.getString("total_pages");
                            totalpages = Integer.parseInt(total_pages);

                            if (pagecount == 1) {
                                data_list.clear();
                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);

                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "p_prescription", p_prescription_recyler, R.layout.doctorprescription_lstitem);
                                p_prescription_recyler.setAdapter(adapter);

                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                p_prescription_recyler.invalidate();
                            }
                            updatelay(data_list);


                        } else {
                            if (pagecount == 1) {
                                data_list.clear();
                                dummy_list.clear();
                                updatelay(data_list);
                            }

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
                if(pagecount==1){
                    CustomProgressbar.Progressbarcancel(activity);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(pagecount==1){
                    CustomProgressbar.Progressbarcancel(activity);
                }

            }
        });

    }



    private void DoctorsPrescriptionsService(final Activity activity, final int pagecount, String recordsperpage) {
        if(pagecount==1){
            CustomProgressbar.Progressbarshow(activity);
        }

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.DoctorsPrescriptions(RetrofitInstance.doctors_prescriptions, StoredObjects.UserId, StoredObjects.UserRoleId,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
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
                            String total_pages = jsonObject.getString("total_pages");
                            totalpages = Integer.parseInt(total_pages);

                            if (pagecount == 1) {
                                data_list.clear();
                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);

                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "p_doc_prescription", p_doc_prescription_recyler, R.layout.yourhospital_prescription_lstitem);
                                p_doc_prescription_recyler.setAdapter(adapter);

                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                p_doc_prescription_recyler.invalidate();
                            }
                            updatelay(data_list);


                        } else {
                            if (pagecount == 1) {
                                data_list.clear();
                                dummy_list.clear();
                                updatelay(data_list);
                            }

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
                if(pagecount==1){
                    CustomProgressbar.Progressbarcancel(activity);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(pagecount==1){
                    CustomProgressbar.Progressbarcancel(activity);
                }

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

