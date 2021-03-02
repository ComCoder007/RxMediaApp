package com.rxmediaapp.fragments.patient;

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
import com.rxmediaapp.fragments.hospital.H_OtherDoc_Prescriptions;
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

public class Patient_Added_Prescriptions extends Fragment {

    ImageView backbtn_img;
    TextView title_txt,own_prescription_txt,p_appointment_txt,p_problem_txt,p_comment_edtxt;

     RecyclerView prescrption_details_recyler,diagnose_recyler;


    ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> data_list_one = new ArrayList<>();
    ArrayList<HashMap<String, String>> data_list_two = new ArrayList<>();
    ArrayList<HashMap<String, String>> data_list_three = new ArrayList<>();

  TextView nodatavailable_txt;
  HashMapRecycleviewadapter adapter,adapterone,adaptertwo;
    LinearLayout medicationmain_lay,diagnose_lay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.patient_added_prescriptions,null,false );
        StoredObjects.page_type="patient_added_prescriptions";

        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);

        serviceCalling();
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        p_appointment_txt = v.findViewById( R.id. p_appointment_txt);
        p_problem_txt= v.findViewById( R.id. p_problem_txt);

        p_comment_edtxt= v.findViewById( R.id. p_comment_edtxt);
        prescrption_details_recyler= v.findViewById( R.id. prescrption_details_recyler);
        diagnose_recyler = v.findViewById( R.id. diagnose_recyler);
        own_prescription_txt = v.findViewById( R.id. p_own_prescription_txt);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        title_txt.setText( "Prescriptions" );
        own_prescription_txt.setVisibility(View.GONE);
        medicationmain_lay=v.findViewById(R.id.medicationmain_lay);
        diagnose_lay=v.findViewById(R.id.diagnose_lay);


        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        });

        own_prescription_txt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  fragmentcallinglay( new Add_Appointment() );
            }
        } );

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        prescrption_details_recyler.setLayoutManager(linearLayoutManager);

        final LinearLayoutManager linearLayoutManagerone=new LinearLayoutManager(getActivity());
        diagnose_recyler.setLayoutManager(linearLayoutManagerone);


    }
    private void serviceCalling() {

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            PrescriptiondetailsService(getActivity());
            // DiagnosesugessionService(getActivity(),pagecount,recordsperpage);
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }

    private void PrescriptiondetailsService(final Activity activity) {

        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.patientaddedprescriptions(RetrofitInstance.doctor_view_prescription,StoredObjects.UserId,StoredObjects.UserRoleId,StoredObjects.Prescription_Id).enqueue(new Callback<ResponseBody>() {
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


                            try {
                                p_appointment_txt.setText(StoredObjects.convertfullDateTimeformat(data_list.get(0).get("appointment_date_time")));

                            } catch (Exception e) {
                                p_appointment_txt.setText(data_list.get(0).get("appointment_date_time"));

                            }
                            p_problem_txt.setText(data_list.get(0).get("problem"));
                            p_comment_edtxt.setText("");


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


