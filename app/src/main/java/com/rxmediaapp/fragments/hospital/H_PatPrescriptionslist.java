package com.rxmediaapp.fragments.hospital;

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

public class H_PatPrescriptionslist extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    TextView nodatavailable_txt;
    EditText d_clininc_edtx,d_patent_srch_edtx,docas_frmdate_edtx,docas_todate_edtx;

    Button d_sbmt_btn,d_cancel_btn,p_prescription_button,p_otherprescription_button;


    RecyclerView p_prescription_recyler,p_doc_prescription_recyler;
    ArrayList<HashMap<String, String>> y_prescr_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> o_prescr_list = new ArrayList<>();
    HashMapRecycleviewadapter adapter;
    HashMapRecycleviewadapter adapterone;

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    String search_text="",from_date="",to_date="",doctor_id="";

    ArrayList<String> hosp_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.doc_patprescrslist,null,false );
        StoredObjects.page_type="doc_prescriptions";
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);



        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        p_prescription_recyler = v.findViewById( R.id. p_prescription_recyler);
        p_doc_prescription_recyler = v.findViewById(R.id.p_doc_prescription_recyler);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        docas_frmdate_edtx=v.findViewById(R.id.docas_frmdate_edtx);
        docas_todate_edtx=v.findViewById(R.id.docas_todate_edtx);
        d_patent_srch_edtx=v.findViewById(R.id.d_patent_srch_edtx);

        d_patent_srch_edtx=v.findViewById(R.id.d_patent_srch_edtx);
        d_clininc_edtx=v.findViewById(R.id.d_clininc_edtx);
        d_sbmt_btn=v.findViewById(R.id.d_sbmt_btn);
        d_cancel_btn=v.findViewById(R.id.d_cancel_btn);

        p_prescription_button=v.findViewById(R.id.p_prescription_button);
        p_otherprescription_button=v.findViewById(R.id.p_otherprescription_button);
        hosp_list.clear();
        for(int k=0;k<SideMenu.hospitals_list.size();k++){
            hosp_list.add(SideMenu.hospitals_list.get(k).get("clinic_name"));
        }

        d_clininc_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BloodgroupPopup(d_clininc_edtx,getActivity());
            }
        });



        d_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d_patent_srch_edtx.setText("");
                docas_frmdate_edtx.setText("");
                docas_todate_edtx.setText("");
                d_clininc_edtx.setText("");


                doctor_id="";
                search_text="";
                from_date="";
                to_date="";
                serviceCalling();


            }
        });


        d_sbmt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromdate=docas_frmdate_edtx.getText().toString().trim();
                String todate=docas_todate_edtx.getText().toString().trim();
                search_text=d_patent_srch_edtx.getText().toString().trim();
                from_date=fromdate;
                to_date=todate;
                if(fromdate.length()>0||todate.length()>0||search_text.length()>0||doctor_id.length()>0){
                    serviceCalling();
                }else{
                    StoredObjects.ToastMethod("Please select Filter options",getActivity());
                }


            }
        });

        title_txt.setText( "Prescriptions" );


        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        });



        docas_frmdate_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docas_todate_edtx.setText("");
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                docas_todate_edtx.setText("");
                                docas_frmdate_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });


        docas_todate_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                String to_date=StoredObjects.GetSelectedDate(day,month,year);
                                String from_date=docas_frmdate_edtx.getText().toString().trim();
                                if(from_date.length()==0){
                                    StoredObjects.ToastMethod( getString(R.string.selectfrm_date),getActivity());
                                }else{
                                    if(StoredObjects.daysDifference(from_date,to_date)==true){
                                        docas_todate_edtx.setText(to_date);
                                    }else{
                                        docas_todate_edtx.setText("");
                                        StoredObjects.ToastMethod(getString(R.string.to_date),getActivity());
                                    }

                                }


                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });


        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        p_prescription_recyler.setLayoutManager(linearLayoutManager);

        final LinearLayoutManager linearLayoutManagerone=new LinearLayoutManager(getActivity());
        p_doc_prescription_recyler.setLayoutManager(linearLayoutManagerone);
        //p_doc_prescription_recyler.setAdapter(new HashMapRecycleviewadapter(getActivity(),StoredObjects.dummy_list,"p_doc_prescription",p_doc_prescription_recyler,R.layout.yourhospital_prescription_lstitem));


        if (InterNetChecker.isNetworkAvailable(getActivity())) {

            hospitalDoctorsListService(getActivity(),1,"100");
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }


    }

    String first_time="yes";
    private void hospitalDoctorsListService(final Activity activity, final int page, String records) {
        CustomProgressbar.Progressbarshow(activity);
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.hospitalDrList(RetrofitInstance.hospital_doctors_list,StoredObjects.UserId,StoredObjects.UserRoleId,""+page,records).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {

                        String responseRecieved = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseRecieved);
                        StoredObjects.LogMethod("response", "response::" + responseRecieved);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("200")) {
                            String results = jsonObject.getString("results");
                            doctor_list = JsonParsing.GetJsonData(results);
                            doctor_names_list.clear();
                            for (int k = 0; k < doctor_list.size(); k++) {
                                doctor_names_list.add(doctor_list.get(k).get("name"));
                            }

                        } else {
                            doctor_list.clear();
                            doctor_names_list.clear();
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    serviceCalling();
                }

                // CustomProgressbar.Progressbarcancel(activity);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                CustomProgressbar.Progressbarcancel(activity);


            }
        });
    }

    ArrayList<String> doctor_names_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> doctor_list = new ArrayList<>();

    private void BloodgroupPopup(final EditText prfilenme, Activity activity) {
        final ListPopupWindow dropdownpopup = new ListPopupWindow(activity);
        dropdownpopup.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, doctor_names_list));
        dropdownpopup.setAnchorView(prfilenme);
        dropdownpopup.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        dropdownpopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doctor_id = doctor_list.get(position).get("user_id");
                prfilenme.setText(doctor_names_list.get(position));
                dropdownpopup.dismiss();

            }
        });

        dropdownpopup.show();
    }
    private void serviceCalling() {

        if (InterNetChecker.isNetworkAvailable(getActivity())) {

            DocPrescriptionService(getActivity());
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }

/*

    private void PatientPrescriptionService(final Activity activity, final int pagecount, String recordsperpage) {


        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.PatientPrescription(RetrofitInstance.patient_prescription, StoredObjects.UserId, StoredObjects.UserRoleId,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
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
                            o_prescr_list = JsonParsing.GetJsonData(results);

                            adapterone = new HashMapRecycleviewadapter(getActivity(), o_prescr_list, "p_doc_prescription", p_doc_prescription_recyler, R.layout.yourhospital_prescription_lstitem);
                            p_doc_prescription_recyler.setAdapter(adapterone);


                        } else {


                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                }
                if (pagecount == 1) {
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

*/


    private void DocPrescriptionService(final Activity activity) {
        if(first_time.equalsIgnoreCase("yes")){

        }else{
            CustomProgressbar.Progressbarshow(activity);
        }



        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);



        api.TotalPrescriptions(RetrofitInstance.total_prescriptions,doctor_id,from_date,to_date,search_text,StoredObjects.UserId, StoredObjects.UserRoleId,StoredObjects.Pat_DocID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseReceived = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseReceived);
                        StoredObjects.LogMethod("response", "response::" + StoredObjects.Pat_DocID+":::"+responseReceived);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("200")) {
                            String results = jsonObject.getString("results");
                            y_prescr_list = JsonParsing.GetJsonData(results);

                            adapter = new HashMapRecycleviewadapter(getActivity(), y_prescr_list, "p_prescription", p_prescription_recyler, R.layout.doctorprescription_lstitem);
                            p_prescription_recyler.setAdapter(adapter);

                        } else {
                            y_prescr_list.clear();

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    updatelay(y_prescr_list);
                    CustomProgressbar.Progressbarcancel(activity);
                    if(first_time.equalsIgnoreCase("yes")){
                        first_time="No";
                    }

                   /* if (InterNetChecker.isNetworkAvailable(getActivity())) {

                        DocPrescriptionService(getActivity());
                    } else {
                        StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                    }*/

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


