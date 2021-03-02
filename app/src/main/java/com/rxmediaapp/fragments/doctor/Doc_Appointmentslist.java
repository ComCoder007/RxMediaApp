package com.rxmediaapp.fragments.doctor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.customfonts.CustomNormalButton;
import com.rxmediaapp.fragments.dashboards.Doc_Dashboard;
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

public class Doc_Appointmentslist extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    CustomNormalButton d_today_btn, d_hstry_btn,h_test_submit_btn,h_patient_cancel_btn;
    static RecyclerView d_que_recyler, d_todaycheck_recyler;
    public static HashMapRecycleviewadapter adapter, adapterone;
    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();

    public static TextView nodatavailable_txt;
    EditText d_patent_from_date_edtx, d_patent_to_date_edtx,doc_patient_searchText;
    public static LinearLayout quepat_lay, p_todaylist_lay, doc_patient_days_lay;
    int pagecount = 1, totalpages = 0;
    String recordsperpage = "10";


    String from_date="",to_date="",search_text="";
    public  static String tab_name="today";
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    public static TextView history_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.doc_patient_list, null, false);
        StoredObjects.page_type = "doc_patnt_lst";
        SideMenu.updatemenu(StoredObjects.page_type);
        try {
            StoredObjects.listcount= 2;
            SideMenu.adapter.notifyDataSetChanged();
        }catch (Exception e){

        }
        initilization(v);
        pagecount = 1;
        /*from_date=StoredObjects.CurrentDate;
        to_date=StoredObjects.CurrentDate;
*/
        serviceCalling();
        return v;
    }
    public void fragmentcallinglay1(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace(R.id.frame_container, fragment).commit();

    }
    private void initilization(View v) {

        backbtn_img = v.findViewById(R.id.backbtn_img);
        title_txt = v.findViewById(R.id.title_txt);

        d_today_btn = v.findViewById(R.id.d_today_btn);
        d_hstry_btn = v.findViewById(R.id.d_hstry_btn);
        d_que_recyler = v.findViewById(R.id.d_que_recyler);
        d_todaycheck_recyler = v.findViewById(R.id.d_todaycheck_recyler);
        doc_patient_days_lay = v.findViewById(R.id.doc_patient_days_lay);
        d_patent_from_date_edtx = v.findViewById(R.id.d_patent_from_date_edtx);
        d_patent_to_date_edtx = v.findViewById(R.id.d_patent_to_date_edtx);
        quepat_lay = v.findViewById(R.id.quepat_lay);
        p_todaylist_lay = v.findViewById(R.id.p_todaylist_lay);
        doc_patient_searchText = v.findViewById(R.id.doc_patient_searchText);
        nodatavailable_txt = v.findViewById(R.id.nodatavailable_txt);
        history_btn=v.findViewById(R.id.history_btn);
        // d_patent_from_date_edtx.setVisibility(View.GONE);
        title_txt.setText("Appointment List");



        h_test_submit_btn = v.findViewById(R.id.h_patient_submit_btn);
        h_patient_cancel_btn=v.findViewById(R.id.h_patient_cancel_btn);

        doc_patient_days_lay.setVisibility(View.VISIBLE);

        backbtn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SideMenu.hospitals_list.size()==0||SideMenu.hospitals_list.size()==1){

                    fragmentcallinglay(new Doc_Dashboard());
                }else{

                    fragmentcallinglay(new Doc_Patientlistmain());

                }

            }
        });

        d_hstry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tab_name="history";
                from_date="";
                to_date=StoredObjects.PreviousDate;
                search_text="";
                doc_patient_searchText.setText("");
                d_patent_from_date_edtx.setText("");
                d_patent_to_date_edtx.setText("");
                pagecount=1;
                serviceCalling();
                StoredObjects.LogMethod("val","to_date::"+StoredObjects.PreviousDate+StoredObjects.LastDate);

            }
        });
        d_today_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d_patent_from_date_edtx.setText(StoredObjects.CurrentDate);
                d_patent_to_date_edtx.setText(StoredObjects.CurrentDate);
                String fromdate=d_patent_from_date_edtx.getText().toString().trim();
                String todate=d_patent_to_date_edtx.getText().toString().trim();
                from_date=fromdate;
                to_date=todate;
                search_text="";
                doc_patient_searchText.setText("");
                pagecount=1;
                tab_name="today";
                serviceCalling();
            }
        });


        h_test_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(tab_name.equalsIgnoreCase("today")){
                    d_patent_from_date_edtx.setText(StoredObjects.CurrentDate);
                    d_patent_to_date_edtx.setText(StoredObjects.CurrentDate);
                }*/
                String fromdate=d_patent_from_date_edtx.getText().toString().trim();
                String todate=d_patent_to_date_edtx.getText().toString().trim();
                search_text=doc_patient_searchText.getText().toString().trim();
                from_date=fromdate;
                to_date=todate;

                if(fromdate.length()>0||todate.length()>0||search_text.length()>0){

                    pagecount=1;
                    serviceCalling();
                }else{
                    StoredObjects.ToastMethod("Please select Filter options",getActivity());
                }




            }
        });

        h_patient_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(tab_name.equalsIgnoreCase("today")){
                    d_patent_from_date_edtx.setText(StoredObjects.CurrentDate);
                    d_patent_to_date_edtx.setText(StoredObjects.CurrentDate);
                    String fromdate=d_patent_from_date_edtx.getText().toString().trim();
                    String todate=d_patent_to_date_edtx.getText().toString().trim();
                    from_date=fromdate;
                    to_date=todate;

                }else{
                    from_date="";
                    to_date="";
                }*/

                from_date="";
                to_date="";
                search_text="";
                pagecount=1;
                doc_patient_searchText.setText("");
                d_patent_from_date_edtx.setText("");
                d_patent_to_date_edtx.setText("");


                serviceCalling();
            }
        });

        p_todaylist_lay.setVisibility(View.VISIBLE);
        quepat_lay.setVisibility(View.GONE);
        history_btn.setVisibility(View.GONE);
        d_patent_from_date_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d_patent_to_date_edtx.setText("");
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                d_patent_to_date_edtx.setText("");
                                d_patent_from_date_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });


        d_patent_to_date_edtx.setOnClickListener(new View.OnClickListener() {
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
                                String from_date=d_patent_from_date_edtx.getText().toString().trim();
                                if(from_date.length()==0){
                                    StoredObjects.ToastMethod( getString(R.string.selectfrm_date),getActivity());
                                }else{
                                    if(StoredObjects.daysDifference(from_date,to_date)==true){
                                        d_patent_to_date_edtx.setText(to_date);
                                    }else{
                                        d_patent_to_date_edtx.setText("");
                                        StoredObjects.ToastMethod(getString(R.string.to_date),getActivity());
                                    }

                                }

                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        d_que_recyler.setLayoutManager(linearLayoutManager);

        final LinearLayoutManager linearLayoutManagerone = new LinearLayoutManager(getActivity());
        d_todaycheck_recyler.setLayoutManager(linearLayoutManagerone);


        d_todaycheck_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapter.getItemCount() - 1) {
                    if(tab_name.equalsIgnoreCase("today")){

                    }else{

                        pagecount = pagecount + 1;
                        if (pagecount <= totalpages) {
                            serviceCalling();

                        }
                    }


                }
            }
        });


        // StoredObjects.hashmaplist(3);

    }

    private void historyPatientList(final Activity activity) {
        if (pagecount == 1) {
            CustomProgressbar.Progressbarshow(activity);
        }

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.DocpatientlistHistory(RetrofitInstance.doctor_patient_list, StoredObjects.Doc_Hospital_Id, from_date, to_date,search_text, StoredObjects.UserId, StoredObjects.UserRoleId,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
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
                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "d_patent_chcked", d_todaycheck_recyler, R.layout.h_patents_lstitem);
                                d_todaycheck_recyler.setAdapter(adapter);

                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                d_todaycheck_recyler.invalidate();
                            }
                            updatelay();

                        } else {
                            if (pagecount == 1) {
                                data_list.clear();
                                dummy_list.clear();
                                updatelay();
                            }

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
                if (pagecount == 1) {
                    CustomProgressbar.Progressbarcancel(activity);
                }

            }
        });

    }

    private void serviceCalling() {

      /*  if(tab_name.equalsIgnoreCase("today")){
            doc_patient_days_lay.setVisibility(View.GONE);
            quepat_lay.setVisibility(View.VISIBLE);
            history_btn.setVisibility(View.VISIBLE);

            if (InterNetChecker.isNetworkAvailable(getActivity())) {
                PatientlistService(getActivity());
            } else {
                StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
            }
        }else{

            doc_patient_days_lay.setVisibility(View.VISIBLE);
            quepat_lay.setVisibility(View.GONE);
            history_btn.setVisibility(View.GONE);

            if (InterNetChecker.isNetworkAvailable(getActivity())) {
                historyPatientList(getActivity());
            } else {
                StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
            }


        }*/
        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            historyPatientList(getActivity());
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }

    }


  /*  private void PatientlistService(final Activity activity) {
        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.Docpatientlist(RetrofitInstance.doctor_patient_list, StoredObjects.Doc_Hospital_Id, StoredObjects.UserId, StoredObjects.UserRoleId,from_date,to_date,search_text).enqueue(new Callback<ResponseBody>() {
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
                            t_dummy_list = JsonParsing.GetJsonData(results);


                            c_dummy_list.clear();
                            q_dummy_list.clear();
                            for (int k = 0; k < t_dummy_list.size(); k++) {
                                if (t_dummy_list.get(k).get("status").equalsIgnoreCase("In Que")) {
                                    q_dummy_list.add(t_dummy_list.get(k));
                                } else {
                                    c_dummy_list.add(t_dummy_list.get(k));
                                }
                            }

                            if (c_dummy_list.size() == 0 && q_dummy_list.size() > 0) {
                                p_todaylist_lay.setVisibility(View.GONE);
                                quepat_lay.setVisibility(View.VISIBLE);
                            } else if (c_dummy_list.size() > 0 && q_dummy_list.size() == 0) {
                                p_todaylist_lay.setVisibility(View.VISIBLE);
                                quepat_lay.setVisibility(View.GONE);
                            } else {
                                p_todaylist_lay.setVisibility(View.VISIBLE);
                                quepat_lay.setVisibility(View.VISIBLE);
                            }

                            adapter = new HashMapRecycleviewadapter(getActivity(), q_dummy_list, "d_patnt_que", d_que_recyler, R.layout.h_patents_lstitem);
                            d_que_recyler.setAdapter(adapter);

                            adapterone = new HashMapRecycleviewadapter(getActivity(), c_dummy_list, "d_patent_chcked", d_todaycheck_recyler, R.layout.h_patents_lstitem);
                            d_todaycheck_recyler.setAdapter(adapterone);


                        } else {
                            c_dummy_list.clear();
                            q_dummy_list.clear();
                            t_dummy_list.clear();
                            updatelay();


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

*/

    private void updatelay() {
        if(data_list.size()==0){
            nodatavailable_txt.setVisibility(View.VISIBLE);
            d_todaycheck_recyler.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            d_todaycheck_recyler.setVisibility(View.VISIBLE);
        }




    }


    public void fragmentcallinglay(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace(R.id.frame_container, fragment).addToBackStack("").commit();
    }

}

