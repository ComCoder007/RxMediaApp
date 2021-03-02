package com.rxmediaapp.fragments.assistant;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.customfonts.CustomNormalButton;
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
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ass_AppointmentList extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    static RecyclerView ass_patient_recycler;
    public static HashMapRecycleviewadapter adapter;
    public static TextView nodatavailable_txt;

    ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
    int pagecount=1,totalpages=0;
    String recordsperpage="10";

    ArrayList<HashMap<String, String>> doctors_list = new ArrayList<>();
    ArrayList<String> doc_name_list = new ArrayList<>();
    String first_time="Yes";

    String[] checkup_status = {"Checkup Status","In Que","Completed"};
    CustomNormalButton h_test_submit_btn,h_patient_cancel_btn;

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    String search_text="",from_date="",to_date="";;
    public static CustomEditText h_test_list_dr_select, h_test_from_date_edtx, h_test_to_date_edtx,h_test_list_status_select,h_patent_srch_edtx;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.ass_patientlist,null,false );
        StoredObjects.page_type="ass_appointmentlist";

        SideMenu.updatemenu(StoredObjects.page_type);
        try {
            StoredObjects.listcount= 2;
            SideMenu.adapter.notifyDataSetChanged();
        }catch (Exception e){

        }
        initilization(v);
        pagecount=1;
        first_time="yes";
        serviceCalling();
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        ass_patient_recycler = v.findViewById( R.id.ass_patient_recycler);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        title_txt.setText( "Appointment List" );

        h_test_from_date_edtx = v.findViewById(R.id.h_patient_from_date_edtx);
        h_test_to_date_edtx = v.findViewById(R.id.h_patient_to_date_edtx);
        h_test_list_dr_select = v.findViewById(R.id.h_patients_list_dr_select);
        h_test_submit_btn = v.findViewById(R.id.h_patient_submit_btn);
        h_test_list_status_select = v.findViewById(R.id.h_test_list_status_select);
        h_patent_srch_edtx=v.findViewById(R.id.h_patent_srch_edtx);

        h_patient_cancel_btn=v.findViewById(R.id.h_patient_cancel_btn);

        h_patient_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                h_test_from_date_edtx.setText("");
                h_test_to_date_edtx.setText("");
                h_patent_srch_edtx.setText("");
                h_test_list_status_select.setText("");
                h_test_list_dr_select.setText("");

                search_text="";
                from_date="";
                to_date="";
                status="";
                doctor_id="";
                pagecount=1;

                if (InterNetChecker.isNetworkAvailable(getActivity())) {
                    PatientlistService(getActivity(),pagecount,recordsperpage);
                } else {
                    StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                }
            }
        });


        h_test_from_date_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                h_test_to_date_edtx.setText("");
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                h_test_to_date_edtx.setText("");
                                h_test_from_date_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });



        h_test_to_date_edtx.setOnClickListener(new View.OnClickListener() {
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
                                String from_date=h_test_from_date_edtx.getText().toString().trim();
                                if(from_date.length()==0){
                                    StoredObjects.ToastMethod( getString(R.string.selectfrm_date),getActivity());
                                }else{
                                    if(StoredObjects.daysDifference(from_date,to_date)==true){
                                        h_test_to_date_edtx.setText(to_date);
                                    }else{
                                        h_test_to_date_edtx.setText("");
                                        StoredObjects.ToastMethod(getString(R.string.to_date),getActivity());
                                    }
                                }
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });



        backbtn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentcallinglay( new Asst_Dashboard() );
            }
        });


        h_test_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fromdate=h_test_from_date_edtx.getText().toString().trim();
                String todate=h_test_to_date_edtx.getText().toString().trim();
                search_text=h_patent_srch_edtx.getText().toString().trim();

                if(fromdate.length()>0||todate.length()>0||search_text.length()>0
                        ||doctor_id.length()>0){
                    if(fromdate.equalsIgnoreCase("")){

                    }else{
                        from_date=fromdate;
                    }
                    if(todate.equalsIgnoreCase("")){

                    }else{
                        to_date=todate;
                    }
                    pagecount=1;
                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                        PatientlistService(getActivity(),pagecount,recordsperpage);
                    } else {
                        StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                    }
                }else{
                    StoredObjects.ToastMethod("Please select Filter options",getActivity());
                }

            }
        });


        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        ass_patient_recycler.setLayoutManager(linearLayoutManager);


        ass_patient_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                    pagecount=pagecount+1;
                    if(pagecount<=totalpages){


                        if (InterNetChecker.isNetworkAvailable(getActivity())) {
                            PatientlistService(getActivity(),pagecount,recordsperpage);
                        } else {
                            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                        }

                    }

                }
            }
        });
        h_test_list_status_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkStatusPopup(h_test_list_status_select,getActivity());
            }
        });

        h_test_list_dr_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(doctors_list.size()>0){
                    DoctorsPopup((CustomEditText) h_test_list_dr_select,getActivity());
                }else{
                    StoredObjects.ToastMethod("No Doctor found",getActivity());
                }
            }
        });

    }

    String status="";
    private void checkStatusPopup(final CustomEditText h_test_list_status_select, Activity activity) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
        listPopupWindow.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, checkup_status));
        listPopupWindow.setAnchorView(h_test_list_status_select);
        listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                h_test_list_status_select.setText(checkup_status[position]);
                if(position==0){
                    status="";
                }else{
                    status=checkup_status[position];
                }

                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
    }
    String doctor_id="";

    private void DoctorsPopup(final CustomEditText h_test_list_status_select,Activity activity) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
        listPopupWindow.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, doc_name_list));
        listPopupWindow.setAnchorView(h_test_list_status_select);
        listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                h_test_list_status_select.setText(doc_name_list.get(position));

                doctor_id=doctors_list.get(position).get("doctor_id");
                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
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
                            doctors_list = JsonParsing.GetJsonData(results);

                            doc_name_list.clear();
                            for (int k = 0; k < doctors_list.size(); k++) {
                                doc_name_list.add(doctors_list.get(k).get("name"));
                            }

                        } else {
                            doctors_list.clear();
                            doc_name_list.clear();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
                // CustomProgressbar.Progressbarcancel(activity);
                if (InterNetChecker.isNetworkAvailable(getActivity())) {
                    PatientlistService(getActivity(),pagecount,recordsperpage);
                } else {
                    StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                CustomProgressbar.Progressbarcancel(activity);


            }
        });

    }
    private void PatientlistService(final Activity activity, final int pagecount, String recordsperpage) {
        if(pagecount==1){
            if(first_time.equalsIgnoreCase("Yes")){

            }else{
                CustomProgressbar.Progressbarshow(activity);
            }

        }

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.Assistantpatientlist(RetrofitInstance.assistant_patient_list, StoredObjects.UserId, StoredObjects.UserRoleId,from_date,to_date,search_text,doctor_id,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    StoredObjects.LogMethod("responseID", "response::" + StoredObjects.UserId);
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

                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "ass_inner_patientlist", ass_patient_recycler, R.layout.ass_patientsub_listitem);
                                ass_patient_recycler.setAdapter(adapter);

                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                ass_patient_recycler.invalidate();
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
                    if(first_time.equalsIgnoreCase("Yes")){

                        first_time="No";
                    }
                    CustomProgressbar.Progressbarcancel(activity);

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(pagecount==1){
                    if(first_time.equalsIgnoreCase("Yes")){

                        first_time="No";
                    }else{
                        CustomProgressbar.Progressbarshow(activity);
                    }
                }

            }
        });

    }

    public static void updatelay(ArrayList<HashMap<String, String>> data_list) {

        if(data_list.size()==0){
            nodatavailable_txt.setVisibility(View.VISIBLE);
            ass_patient_recycler.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            ass_patient_recycler.setVisibility(View.VISIBLE);
        }
    }

    public void fragmentcallinglay(Fragment fragment) {
        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).commit ();

    }




}




