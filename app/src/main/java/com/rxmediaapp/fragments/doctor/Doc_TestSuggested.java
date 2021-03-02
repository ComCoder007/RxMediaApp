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


public class Doc_TestSuggested extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;

    EditText docas_frmdate_edtx,docas_todate_edtx,enrole_search_edtxt;
    Button h_submit_btn,d_cancel_btn;
    public static RecyclerView doc_testsuggested_recyler;
    public static HashMapRecycleviewadapter adapter;

    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
    public static TextView nodatavailable_txt;

    String search_text="",from_date="",to_date="";

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    int pagecount=1,totalpages=0;
    String recordsperpage="10";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.doc_test_suggested,null,false );
        StoredObjects.page_type="test_suggested";
        SideMenu.updatemenu(StoredObjects.page_type);
        try {
            StoredObjects.listcount= 6;
            SideMenu.adapter.notifyDataSetChanged();
        }catch (Exception e){

        }
        initilization(v);
        pagecount=1;
        serviceCalling();
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        doc_testsuggested_recyler = v.findViewById( R.id.doc_testsuggested_recyler);
       // test_date_edtx = v.findViewById( R.id. test_date_edtx);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        docas_frmdate_edtx=v.findViewById(R.id.docas_frmdate_edtx);
        docas_todate_edtx=v.findViewById(R.id.docas_todate_edtx);
        enrole_search_edtxt=v.findViewById(R.id.enrole_search_edtxt);

        h_submit_btn=v.findViewById(R.id.d_sbmt_btn);
        d_cancel_btn=v.findViewById(R.id.d_cancel_btn);

        title_txt.setText( "Test Suggested" );

        d_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enrole_search_edtxt.setText("");
                docas_frmdate_edtx.setText("");
                docas_todate_edtx.setText("");

                search_text="";
                from_date="";
                to_date="";

                pagecount=1;
                serviceCalling();


            }
        });

        h_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromdate=docas_frmdate_edtx.getText().toString().trim();
                String todate=docas_todate_edtx.getText().toString().trim();
                search_text=enrole_search_edtxt.getText().toString().trim();
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

        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SideMenu.hospitals_list.size()==0||SideMenu.hospitals_list.size()==1){

                    fragmentcallinglay(new Doc_Dashboard());
                }else{

                    fragmentcallinglay(new Doc_Patientlistmain());

                }
            }
        } );



        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        doc_testsuggested_recyler.setLayoutManager(linearLayoutManager);


        doc_testsuggested_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

      //  doc_testsuggested_recyler.setAdapter(new HashMapRecycleviewadapter(getActivity(),StoredObjects.dummy_list,"doc_testsuggested",doc_testsuggested_recyler,R.layout.doc_testsuggested_lstitem));
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



    }


    private void serviceCalling() {

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            TestSuggestedService(getActivity(),pagecount,recordsperpage);
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }

    private void TestSuggestedService(final Activity activity, final int pagecount, String recordsperpage) {
        if(pagecount==1){
            CustomProgressbar.Progressbarshow(activity);
        }

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.DoctorTestSugested(RetrofitInstance.doctor_test_suggested, StoredObjects.UserId, StoredObjects.UserRoleId, StoredObjects.Doc_Hospital_Id,from_date,to_date,pagecount+"",recordsperpage,search_text).enqueue(new Callback<ResponseBody>() {
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

                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "doc_testsuggested", doc_testsuggested_recyler, R.layout.doc_testsuggested_lstitem);
                                doc_testsuggested_recyler.setAdapter(adapter);

                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                doc_testsuggested_recyler.invalidate();
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
            doc_testsuggested_recyler.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            doc_testsuggested_recyler.setVisibility(View.VISIBLE);
        }
    }

    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

    }




}


