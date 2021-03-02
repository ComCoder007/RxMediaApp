package com.rxmediaapp.fragments.hospital;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.customfonts.CustomNormalButton;
import com.rxmediaapp.fragments.dashboards.H_Dashboard;
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

public class H_TotalPrescriptions extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    public static RecyclerView h_prescriptionslist;
    public static TextView nodatavailable_txt;
    public static HashMapRecycleviewadapter adapter;
    LinearLayout pre_filter_lay;

    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
     CustomEditText h_presc_list_dr_select,h_prescription_from_date_edtx, h_prescription_to_date_edtx, h_presecription_searchText;
    CustomNormalButton h_prescription_submit_btn,d_cancel_btn;

    ArrayList<String> doctor_names_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> doctor_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();
    int pagecount=1,totalpages=0;
    String recordsperpage="10";

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    LinearLayout p_prescription_days_lay;


    String doctor_id="",from_date="",to_date="",type="",search_text="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.h_prescriptions_list,null,false );
        StoredObjects.page_type="total_prescr";

        SideMenu.updatemenu(StoredObjects.page_type);
        try {
            StoredObjects.listcount= 5;
            SideMenu.adapter.notifyDataSetChanged();
        }catch (Exception e){

        }
        initilization(v);
        first_time="yes";
        pagecount=1;
        if (InterNetChecker.isNetworkAvailable(getActivity())) {

            hospitalDoctorsListService(getActivity(),1,"100");
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        h_prescriptionslist = v.findViewById( R.id.h_prescriptionslist);
        pre_filter_lay=v.findViewById(R.id.pre_filter_lay);
     //   searchwith_edtxt =v.findViewById(R.id.searchwith_edtxt);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        h_prescription_from_date_edtx = v.findViewById(R.id.h_prescription_from_date_edtx);
        h_prescription_to_date_edtx = v.findViewById(R.id.h_prescription_to_date_edtx);
        h_presecription_searchText = v.findViewById(R.id.searchwith_edtxt);
        h_presc_list_dr_select = v.findViewById(R.id.h_presc_list_dr_select);
        h_prescription_submit_btn = v.findViewById(R.id.h_prescription_submit_btn);
        d_cancel_btn=v.findViewById(R.id.d_cancel_btn);
        title_txt.setText( "Prescriptions List" );
        p_prescription_days_lay=v.findViewById(R.id.p_prescription_days_lay);


        if( StoredObjects.redirect_type.equalsIgnoreCase("doctor")){
            doctor_id=StoredObjects.H_DOC_ID;
            h_presc_list_dr_select.setVisibility(View.GONE);
        }else{
            doctor_id="";
            h_presc_list_dr_select.setVisibility(View.VISIBLE);
        }


        h_prescription_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( StoredObjects.redirect_type.equalsIgnoreCase("doctor")){
                    doctor_id=StoredObjects.H_DOC_ID;
                }

                String fromdate=h_prescription_from_date_edtx.getText().toString().trim();
                String todate=h_prescription_to_date_edtx.getText().toString().trim();
                search_text=h_presecription_searchText.getText().toString().trim();
                from_date=fromdate;
                to_date=todate;
                if( StoredObjects.redirect_type.equalsIgnoreCase("doctor")){
                    if(fromdate.length()>0||todate.length()>0||search_text.length()>0){

                        pagecount=1;
                        serviceCalling();
                    }else{
                        StoredObjects.ToastMethod("Please select Filter options",getActivity());
                    }
                }else{
                    if(fromdate.length()>0||todate.length()>0||search_text.length()>0||doctor_id.length()>0){

                        pagecount=1;
                        serviceCalling();
                    }else{
                        StoredObjects.ToastMethod("Please select Filter options",getActivity());
                    }
                }


            }
        });

        d_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( StoredObjects.redirect_type.equalsIgnoreCase("doctor")){
                    doctor_id=StoredObjects.H_DOC_ID;
                }else{
                    doctor_id="";

                }

                from_date="";
                to_date="";
                type="";
                search_text="";
                pagecount=1;

                h_prescription_from_date_edtx.setText("");
                h_prescription_to_date_edtx.setText("");
                h_presecription_searchText.setText("");
                h_presc_list_dr_select.setText("");

                serviceCalling();
            }
        });
        h_presc_list_dr_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if(doctor_names_list.size()>0){
                    SpeclizatnListPopup((CustomEditText) h_presc_list_dr_select,getActivity());
                }else{
                    StoredObjects.ToastMethod("No Data found",getActivity());
                }
            }
        });


        h_prescription_from_date_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                h_prescription_to_date_edtx.setText("");
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                h_prescription_to_date_edtx.setText("");
                                h_prescription_from_date_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });


        h_prescription_to_date_edtx.setOnClickListener(new View.OnClickListener() {
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
                                String from_date=h_prescription_from_date_edtx.getText().toString().trim();
                                if(from_date.length()==0){
                                    StoredObjects.ToastMethod( getString(R.string.selectfrm_date),getActivity());
                                }else{
                                    if(StoredObjects.daysDifference(from_date,to_date)==true){
                                        h_prescription_to_date_edtx.setText(to_date);
                                    }else{
                                        h_prescription_to_date_edtx.setText("");
                                        StoredObjects.ToastMethod(getString(R.string.to_date),getActivity());
                                    }
                                }
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });
       /* h_presecription_searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                int textlength = h_presecription_searchText.getText().length();

                filter_list.clear();

                for (int i = 0; i < data_list.size(); i++) {
                    if (textlength <= data_list.get(i).get("patient_name").length()) {

                        if ((data_list.get(i).get("patient_name").toLowerCase().trim().contains(  h_presecription_searchText.getText().toString().toLowerCase().trim()))) {

                            filter_list.add(data_list.get(i));
                        }
                    }
                }

                adapter=new HashMapRecycleviewadapter(getActivity(),data_list,"h_presclist",h_prescriptionslist,R.layout.h_docmain_listitem);
                h_prescriptionslist.setAdapter(adapter);
                if(filter_list.size()==0){
                    nodatavailable_txt.setVisibility(View.VISIBLE);
                    h_prescriptionslist.setVisibility(View.GONE);

                }else{
                    nodatavailable_txt.setVisibility(View.GONE);
                    h_prescriptionslist.setVisibility(View.VISIBLE);

                }



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        backbtn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentcalling(new H_Dashboard());
            }
        });

        pre_filter_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcallinglay(new H_Prescription_Filter());
            }
        });




        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        h_prescriptionslist.setLayoutManager(linearLayoutManager);


        h_prescriptionslist.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


    }

    private void SpeclizatnListPopup(final CustomEditText prfilenme,Activity activity) {
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

            TotalPrescriptionsService(getActivity(),pagecount,recordsperpage);
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
                    if (InterNetChecker.isNetworkAvailable(getActivity())) {

                        TotalPrescriptionsService(getActivity(), pagecount, recordsperpage);
                    } else {
                        StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                    }
                }

               // CustomProgressbar.Progressbarcancel(activity);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                CustomProgressbar.Progressbarcancel(activity);


            }
        });
    }

    private void TotalPrescriptionsService(final Activity activity, final int pagecount, String recordsperpage) {
        if(pagecount==1){
            if(first_time.equalsIgnoreCase("yes")){

            }else{
                CustomProgressbar.Progressbarshow(activity);
            }

        }
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.TotalPrescriptions(RetrofitInstance.total_prescriptions,doctor_id,from_date,to_date,search_text,StoredObjects.UserId, StoredObjects.UserRoleId,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
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

                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "h_presinnerlist", h_prescriptionslist, R.layout.h_pressub_listitem);
                                h_prescriptionslist.setAdapter(adapter);

                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                h_prescriptionslist.invalidate();
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
                if(first_time.equalsIgnoreCase("yes")){
                    first_time="No";
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(pagecount==1){
                    CustomProgressbar.Progressbarcancel(activity);
                    if(first_time.equalsIgnoreCase("yes")){
                        first_time="No";
                    }
                }

            }
        });

    }



    private void updatelay(ArrayList<HashMap<String, String>> data_list) {

        if(data_list.size()==0){
            nodatavailable_txt.setVisibility(View.VISIBLE);
            h_prescriptionslist.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            h_prescriptionslist.setVisibility(View.VISIBLE);
        }
    }


    public void fragmentcallinglay(Fragment fragment) {
        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();
    }

    public void fragmentcalling(Fragment fragment) {
        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).commit ();

    }

}




