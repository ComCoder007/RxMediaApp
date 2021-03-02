package com.rxmediaapp.fragments.hospital;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.fragments.patient.P_Prescription_Details;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class H_OtherDoc_Prescriptions extends Fragment {

    ImageView backbtn_img;
    TextView title_txt,otherdoc_txt;
    TextView nodatavailable_txt;
   public static   ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> data_list_one = new ArrayList<>();
     ArrayList<HashMap<String, String>> data_list_two = new ArrayList<>();
     ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
     RecyclerView prescrption_details_recyler,diagnose_recyler,physical_examination_rv;
     HashMapRecycleviewadapter adapter,adapterone,adaptertwo;
    ArrayList<HashMap<String, String>> data_list_three = new ArrayList<>();
    LinearLayout medicationmain_lay,diagnose_lay;
    TextView patadvise_txt,prescription_txt,docadvice_txt;
    TextView docname_by,problem_txt,datetime_txt;

    TextView descr_txt,enroll_txt,mobile_txt,age_txt,gender_txt;

    CardView phy_cardview;
    LinearLayout assementmain_lay;


    ImageView patimg;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.h_otherdoc_prescrdetails,null,false );
        StoredObjects.page_type="prescription_details";
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        serviceCalling();
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        prescrption_details_recyler= v.findViewById( R.id. prescrption_details_recyler);
        diagnose_recyler = v.findViewById( R.id. diagnose_recyler);
        otherdoc_txt=v.findViewById(R.id.otherdoc_txt);
        physical_examination_rv=v.findViewById(R.id.physical_examination_rv);
        patadvise_txt=v.findViewById(R.id.patadvise_txt);
        prescription_txt=v.findViewById(R.id.prescription_txt);
        docadvice_txt=v.findViewById(R.id.docadvice_txt);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        medicationmain_lay=v.findViewById(R.id.medicationmain_lay);
        diagnose_lay=v.findViewById(R.id.diagnose_lay);


        docname_by=v.findViewById(R.id.docname_by);
        problem_txt=v.findViewById(R.id.problem_txt);
        datetime_txt=v.findViewById(R.id.datetime_txt);

        descr_txt=v.findViewById(R.id.descr_txt);
        enroll_txt=v.findViewById(R.id.enroll_txt);
        mobile_txt=v.findViewById(R.id.mobile_txt);
        age_txt=v.findViewById(R.id.age_txt);
        gender_txt=v.findViewById(R.id.gender_txt);
        problem_txt=v.findViewById(R.id.problem_txt);
        patimg=v.findViewById(R.id.patimg);

        phy_cardview = v.findViewById( R.id. phy_cardview);
        assementmain_lay = v.findViewById( R.id. assementmain_lay);

        title_txt.setText( "Prescription Details" );



        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        });



        otherdoc_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcallinglay(new P_Prescription_Details());
            }
        });


        final GridLayoutManager linearLayoutManagertwo=new GridLayoutManager(getActivity(),2);
        physical_examination_rv.setLayoutManager(linearLayoutManagertwo);

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        prescrption_details_recyler.setLayoutManager(linearLayoutManager);

        final LinearLayoutManager linearLayoutManagerone=new LinearLayoutManager(getActivity());
        diagnose_recyler.setLayoutManager(linearLayoutManagerone);


    }

    private void serviceCalling() {

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            PrescriptiondetailsService(getActivity());
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }

    private void PrescriptiondetailsService(final Activity activity) {

            CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.prescriptiondetails(RetrofitInstance.doctor_view_prescription,StoredObjects.UserId,StoredObjects.UserRoleId,StoredObjects.Prescription_Id).enqueue(new Callback<ResponseBody>() {
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
                            data_list_one = JsonParsing.GetJsonData(data_list.get(0).get("physical_examinations"));
                            data_list_two = JsonParsing.GetJsonData(data_list.get(0).get("tests_suggested"));
                            data_list_three = JsonParsing.GetJsonData(data_list.get(0).get("medications"));


                            if (data_list.get(0).get("medications_count").equalsIgnoreCase("0")) {
                                medicationmain_lay.setVisibility(View.GONE);

                            } else {
                                medicationmain_lay.setVisibility(View.VISIBLE);
                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list_three, "prescrption_details", prescrption_details_recyler, R.layout.prescription_details_listitem);
                                prescrption_details_recyler.setAdapter(adapter);

                            }

                            if (data_list.get(0).get("tests_suggested_count").equalsIgnoreCase("0")) {
                                diagnose_lay.setVisibility(View.GONE);

                            } else {
                                diagnose_lay.setVisibility(View.VISIBLE);
                                adapterone = new HashMapRecycleviewadapter(getActivity(), data_list_two, "diagnose_recyler", diagnose_recyler, R.layout.diagnose_suggestion_listitem);
                                diagnose_recyler.setAdapter(adapterone);
                            }

                            if(data_list.get(0).get("physical_examinations_count").equalsIgnoreCase("0")){
                                phy_cardview.setVisibility(View.GONE);
                            }else{
                                phy_cardview.setVisibility(View.VISIBLE);
                                adaptertwo = new HashMapRecycleviewadapter(getActivity(), data_list_one, "physical_examination", physical_examination_rv, R.layout.physical_examination_listitem);
                                physical_examination_rv.setAdapter(adaptertwo);

                            }


                            if(data_list.get(0).get("patient_advice").equalsIgnoreCase("")
                            &&data_list.get(0).get("assessment_plan").equalsIgnoreCase("")
                            &&data_list.get(0).get("doctors_note").equalsIgnoreCase("")){
                                assementmain_lay.setVisibility(View.GONE);
                            }else{
                                assementmain_lay.setVisibility(View.VISIBLE);
                            }
                            if (data_list.get(0).get("patient_advice").equalsIgnoreCase("")) {
                                patadvise_txt.setVisibility(View.GONE);
                            } else {
                                patadvise_txt.setVisibility(View.VISIBLE);
                            }
                            if (data_list.get(0).get("assessment_plan").equalsIgnoreCase("")) {
                                prescription_txt.setVisibility(View.GONE);
                            } else {
                                prescription_txt.setVisibility(View.VISIBLE);
                            }

                            if (data_list.get(0).get("doctors_note").equalsIgnoreCase("")) {
                                docadvice_txt.setVisibility(View.GONE);
                            } else {
                                docadvice_txt.setVisibility(View.VISIBLE);
                            }
                            patadvise_txt.setText(data_list.get(0).get("patient_advice"));
                            prescription_txt.setText(data_list.get(0).get("assessment_plan"));
                            docadvice_txt.setText(data_list.get(0).get("doctors_note"));

                            try {

                                docname_by.setText("Prescription by : " + data_list.get(0).get("doctor_name"));


                                patadvise_txt.setText(data_list.get(0).get("patient_advice"));
                                prescription_txt.setText(data_list.get(0).get("assessment_plan"));
                                docadvice_txt.setText(data_list.get(0).get("doctors_note"));

                                descr_txt.setText(data_list.get(0).get("name"));
                                enroll_txt.setText(data_list.get(0).get("enroll_id"));
                                mobile_txt.setText(data_list.get(0).get("phone"));
                                age_txt.setText(data_list.get(0).get("age"));
                                gender_txt.setText(data_list.get(0).get("gender"));
                                problem_txt.setText(data_list.get(0).get("problem"));

                                try {
                                    Glide.with(getActivity())
                                            .load(Uri.parse(RetrofitInstance.BASEURL + data_list.get(0).get("image")))
                                            .apply(new RequestOptions()
                                                    .placeholder(R.drawable.no_image)
                                                    .fitCenter()
                                                    .centerCrop())
                                            .into(patimg);
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }

                                try {
                                    datetime_txt.setText(StoredObjects.convertfullDateTimeformat(data_list.get(0).get("appointment_date_time")));

                                } catch (Exception e) {
                                    datetime_txt.setText(data_list.get(0).get("appointment_date_time"));

                                }
                            } catch (Exception e) {
                                //{"prescription_id":29,"patient_name":"Sudha rani","enroll_id":"RX00000029",
                                // "appointment_date_time":"2020-06-15 00:00:00","problem":"Fever","status":"Completed","doctor_name":"Kiran","doctor_id":0,"hospital_id":0}
                            }


                        } else {

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


