package com.rxmediaapp.fragments.hospital;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import retrofit2.http.Query;

public class H_PatientList_Main extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    LinearLayout dates_lay;
    public static RecyclerView h_patlist;
    public static HashMapRecycleviewadapter adapter;
    public static CustomEditText h_ptsearch_edt,docas_frmdate_edtx,docas_todate_edtx,h_patients_list_dr_select,checkup_status_edtx;
    CustomNormalButton h_today_btn,h_hstry_btn;
    Button h_submit_btn,d_cancel_btn;
    public static TextView nodatavailable_txt;

    String[] checkuplist = {"In Que", "Completed"};
    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();

    int pagecount=1,totalpages=0;
    String recordsperpage="10";

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    String from_date="",to_date="",search_text="",tab_name="today",type="",doctor_id="";

    ArrayList<String> doctor_names_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> doctor_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.h_patientlist_main,null,false );
        StoredObjects.page_type="h_patientmain";
        SideMenu.updatemenu(StoredObjects.page_type);
        try {
            StoredObjects.listcount= 3;
            SideMenu.adapter.notifyDataSetChanged();
        }catch (Exception e){

        }
        initilization(v);
       // tab_name="today";
       // from_date=StoredObjects.CurrentDate;
       // to_date=StoredObjects.CurrentDate;
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
        h_patlist = v.findViewById( R.id.h_patlist);
        h_ptsearch_edt = v.findViewById( R.id.h_ptsearch_edt);
        docas_frmdate_edtx=v.findViewById(R.id.docas_frmdate_edtx);
        docas_todate_edtx=v.findViewById(R.id.docas_todate_edtx);
        h_today_btn=v.findViewById(R.id.h_today_btn);
        h_hstry_btn=v.findViewById(R.id.h_hstry_btn);
        dates_lay =v.findViewById(R.id.dates_lay);
        h_submit_btn =v.findViewById(R.id.h_submit_btn);
        d_cancel_btn=v.findViewById(R.id.d_cancel_btn);
        checkup_status_edtx =v.findViewById(R.id.checkup_status_edtx);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        h_patients_list_dr_select=v.findViewById(R.id.h_patients_list_dr_select);



        dates_lay.setVisibility(View.VISIBLE);

        title_txt.setText( "Appointment List" );

        h_patients_list_dr_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpeclizatnListPopup(h_patients_list_dr_select,getActivity());
            }
        });

        h_hstry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab_name="history";
                dates_lay.setVisibility(View.VISIBLE);
                if(tab_name.equalsIgnoreCase("today")){
                    docas_frmdate_edtx.setText(StoredObjects.CurrentDate);
                    docas_todate_edtx.setText(StoredObjects.CurrentDate);
                    String fromdate=docas_frmdate_edtx.getText().toString().trim();
                    String todate=docas_todate_edtx.getText().toString().trim();
                    from_date=fromdate;
                    to_date=todate;

                }else{
                    from_date="";
                    to_date="";
                }
                type="";
                search_text="";
                pagecount=1;
                doctor_id="";

                h_patients_list_dr_select.setText("");
                docas_frmdate_edtx.setText("");
                docas_todate_edtx.setText("");
                h_ptsearch_edt.setText("");
                checkup_status_edtx.setText("");

                serviceCalling();
            }
        });
        h_today_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab_name="today";
                dates_lay.setVisibility(View.GONE);
                if(tab_name.equalsIgnoreCase("today")){
                    docas_frmdate_edtx.setText(StoredObjects.CurrentDate);
                    docas_todate_edtx.setText(StoredObjects.CurrentDate);
                    String fromdate=docas_frmdate_edtx.getText().toString().trim();
                    String todate=docas_todate_edtx.getText().toString().trim();
                    from_date=fromdate;
                    to_date=todate;

                }else{
                    from_date="";
                    to_date="";
                }
                type="";
                search_text="";
                pagecount=1;
                doctor_id="";

                h_patients_list_dr_select.setText("");
                docas_frmdate_edtx.setText("");
                docas_todate_edtx.setText("");
                h_ptsearch_edt.setText("");
                checkup_status_edtx.setText("");

                serviceCalling();
            }
        });


        backbtn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentcallinglay(new H_Dashboard());
            }
        });

        checkup_status_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckupStatusPopup (checkup_status_edtx,getActivity());
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

        h_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*if(tab_name.equalsIgnoreCase("today")){
                   docas_frmdate_edtx.setText(StoredObjects.CurrentDate);
                   docas_todate_edtx.setText(StoredObjects.CurrentDate);
               }*/
                String fromdate=docas_frmdate_edtx.getText().toString().trim();
                String todate=docas_todate_edtx.getText().toString().trim();
                type=checkup_status_edtx.getText().toString().trim();
                search_text=h_ptsearch_edt.getText().toString().trim();
                from_date=fromdate;
                to_date=todate;

                if(fromdate.length()>0||todate.length()>0||search_text.length()>0||type.length()>0||doctor_id.length()>0){

                    pagecount=1;
                    serviceCalling();
                }else{
                    StoredObjects.ToastMethod("Please select Filter options",getActivity());
                }

            }
        });

        d_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if(tab_name.equalsIgnoreCase("today")){
                    docas_frmdate_edtx.setText(StoredObjects.CurrentDate);
                    docas_todate_edtx.setText(StoredObjects.CurrentDate);
                    String fromdate=docas_frmdate_edtx.getText().toString().trim();
                    String todate=docas_todate_edtx.getText().toString().trim();
                    from_date=fromdate;
                    to_date=todate;

                }else{
                    from_date="";
                    to_date="";
                }*/

                from_date="";
                to_date="";
                type="";
                search_text="";
                pagecount=1;
                doctor_id="";

                h_patients_list_dr_select.setText("");

                docas_frmdate_edtx.setText("");
                docas_todate_edtx.setText("");
                h_ptsearch_edt.setText("");
                checkup_status_edtx.setText("");

                serviceCalling();
            }
        });

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        h_patlist.setLayoutManager(linearLayoutManager);


        h_patlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                        PatientlistmainService(getActivity(), pagecount, recordsperpage);
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


    private void serviceCalling() {

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            PatientlistmainService(getActivity(),pagecount,recordsperpage);

        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }

    private void PatientlistmainService(final Activity activity, final int pagecount, String recordsperpage) {
        if(pagecount==1){
            if(first_time.equalsIgnoreCase("yes")){

            }else{
                CustomProgressbar.Progressbarshow(activity);
            }
        }

        //http://o3sa.co.in/demos/Rx/app/index.php?method=hospital-patient-list&logged_in_user_id=19&doctor_id=35&logged_in_role_id=6&from_date=&to_date=&page=&records_per_page=&search_text=&status=
        StoredObjects.LogMethod("val::","val::"+search_text+":::"+type+":::"+doctor_id);
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.Patientlistmain(RetrofitInstance.patient_list_main, StoredObjects.UserId, StoredObjects.UserRoleId,from_date,to_date,pagecount+"",recordsperpage,search_text,type,doctor_id).enqueue(new Callback<ResponseBody>() {
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

                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "h_patientinnerlist", h_patlist, R.layout.h_patsub_listitem);
                                h_patlist.setAdapter(adapter);

                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                h_patlist.invalidate();
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

                    if (pagecount == 1) {
                        CustomProgressbar.Progressbarcancel(activity);
                    }
                    if (first_time.equalsIgnoreCase("yes")) {
                        first_time = "No";
                    }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(pagecount==1){
                    CustomProgressbar.Progressbarcancel(activity);
                }
                if(first_time.equalsIgnoreCase("yes")){
                    first_time="No";
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


    private void updatelay(ArrayList<HashMap<String, String>> data_list) {

        if(data_list.size()==0){
            nodatavailable_txt.setVisibility(View.VISIBLE);
            h_patlist.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            h_patlist.setVisibility(View.VISIBLE);
        }
    }


    public void fragmentcallinglay(Fragment fragment) {
        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).commit ();

    }

    private void CheckupStatusPopup(final CustomEditText prfilenme,Activity activity){
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
        listPopupWindow.setAdapter(new ArrayAdapter<>(activity,R.layout.drpdwn_lay,checkuplist));
        listPopupWindow.setAnchorView(prfilenme);
        listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prfilenme.setText(checkuplist[position]);
                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
    }

}




