package com.rxmediaapp.fragments.patient;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.fragments.dashboards.P_Dashboard;
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

public class P_Test_Sugestions extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    CustomEditText docas_frmdate_edtx,docas_todate_edtx,patient_frmdate_edtx,patient_todate_edtx,p_type_edtx,p_patient_edtx;
    static RecyclerView p_test_recyler;
    public static HashMapRecycleviewadapter adapter;
    public static TextView nodatavailable_txt;

    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    int pagecount=1,totalpages=0;
    String recordsperpage="10";
    Button d_sbmt_btn,d_cancel_btn;


    String search_text="",from_date="",to_date="";
    String first_time="yes";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.patient_test_sugestions,null,false );
        StoredObjects.page_type="patient_test_sugestions";

        SideMenu.updatemenu(StoredObjects.page_type);
        first_time="yes";
        try {
            StoredObjects.listcount= 4;
            SideMenu.adapter.notifyDataSetChanged();
        }catch (Exception e){

        }
        initilization(v);
        pagecount=1;
        if (InterNetChecker.isNetworkAvailable(getContext())) {
            getAssistantService(getActivity(),1,"50");
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
        }
        serviceCalling();
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById(R.id.backbtn_img);
        title_txt = v.findViewById(R.id.title_txt);
        p_test_recyler = v.findViewById(R.id.p_test_recyler);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        patient_frmdate_edtx=v.findViewById(R.id.patient_frmdate_edtx);
        patient_todate_edtx=v.findViewById(R.id.patient_todate_edtx);
        docas_frmdate_edtx=v.findViewById(R.id.docas_frmdate_edtx);
        docas_todate_edtx=v.findViewById(R.id.docas_todate_edtx);
        p_type_edtx=v.findViewById(R.id.p_type_edtx);
        d_sbmt_btn=v.findViewById(R.id.d_sbmt_btn);
        d_cancel_btn=v.findViewById(R.id.d_cancel_btn);
        p_patient_edtx =v.findViewById(R.id.p_patient_edtx);

        title_txt.setText("Test Suggestion List");

        p_patient_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(patname_list.size()>0){
                    PatientListPopUp((CustomEditText) p_patient_edtx,getActivity());
                }else{
                    StoredObjects.ToastMethod("No Data found",getActivity());
                }
            }
        });

     /*   p_type_edtx.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = p_type_edtx.getText().length();

                filter_list.clear();

                for (int i = 0; i < data_list.size(); i++) {
                    if ((textlength <= data_list.get(i).get("referred_lab").length())
                    ||(textlength <= data_list.get(i).get("ref_doctor_name").length())
                    ||(textlength <= data_list.get(i).get("test_name").length())){

                        if ((data_list.get(i).get("referred_lab").toLowerCase().trim().contains(  p_type_edtx.getText().toString().toLowerCase().trim()))
                        ||(data_list.get(i).get("ref_doctor_name").toLowerCase().trim().contains(  p_type_edtx.getText().toString().toLowerCase().trim()))
                        ||(data_list.get(i).get("test_name").toLowerCase().trim().contains(  p_type_edtx.getText().toString().toLowerCase().trim()))) {

                            filter_list.add(data_list.get(i));
                        }
                    }
                }

                adapter = new HashMapRecycleviewadapter(getActivity(),filter_list,"p_tst_sugstn",p_test_recyler,R.layout.p_test_sugtn_lstitem);
                p_test_recyler.setAdapter(adapter);
                if(filter_list.size()==0){
                    nodatavailable_txt.setVisibility(View.VISIBLE);
                    p_test_recyler.setVisibility(View.GONE);

                }else{
                    nodatavailable_txt.setVisibility(View.GONE);
                    p_test_recyler.setVisibility(View.VISIBLE);

                }



            }
        });
*/
        d_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p_type_edtx.setText("");
                patient_frmdate_edtx.setText("");
                patient_todate_edtx.setText("");
                patient_id="";
                search_text="";
                from_date="";
                to_date="";

                pagecount=1;

                if (InterNetChecker.isNetworkAvailable(getActivity())) {
                    P_TestSugestionsService(getActivity(),pagecount,recordsperpage,from_date,to_date,search_text);
                } else {
                    StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                }
            }
        });

        backbtn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentcallinglay( new P_Dashboard() );
            }
        });

        d_sbmt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String fromdate=patient_frmdate_edtx.getText().toString().trim();
                String todate=patient_todate_edtx.getText().toString().trim();
                search_text=p_type_edtx.getText().toString().trim();
                from_date=fromdate;
                to_date=todate;

                if(fromdate.length()>0||todate.length()>0||search_text.length()>0||patient_id.length()>0){

                    pagecount=1;
                    serviceCalling();
                }else{
                    StoredObjects.ToastMethod("Please select Filter options",getActivity());
                }
            }
        });


           patient_frmdate_edtx.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   patient_todate_edtx.setText("");
                   calendar = Calendar.getInstance();
                   year = calendar.get(Calendar.YEAR);
                   month = calendar.get(Calendar.MONTH);
                   dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                   datePickerDialog = new DatePickerDialog(getActivity(),
                           new DatePickerDialog.OnDateSetListener() {
                               @Override
                               public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                   patient_todate_edtx.setText("");
                                   patient_frmdate_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
                               }
                           }, year, month, dayOfMonth);
                   datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                   datePickerDialog.show();
               }
           });


        patient_todate_edtx.setOnClickListener(new View.OnClickListener() {
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
                                String from_date=patient_frmdate_edtx.getText().toString().trim();
                                if(from_date.length()==0){
                                    StoredObjects.ToastMethod( getString(R.string.selectfrm_date),getActivity());
                                }else{
                                    if(StoredObjects.daysDifference(from_date,to_date)==true){
                                        patient_todate_edtx.setText(to_date);
                                    }else{
                                        patient_todate_edtx.setText("");
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
        p_test_recyler.setLayoutManager(linearLayoutManager);

        p_test_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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



    public static ArrayList<HashMap<String, String>> patientslist = new ArrayList<>();
    public   ArrayList<HashMap<String, String>> dummypatientslist = new ArrayList<>();
    public static ArrayList<String> patname_list = new ArrayList<>();


    private void getAssistantService(final Activity activity,final int pagecount,String recordsperpage) {
        CustomProgressbar.Progressbarshow(activity);
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.getmembers(RetrofitInstance.members, StoredObjects.UserId, StoredObjects.UserRoleId,"1",recordsperpage).enqueue(new Callback<ResponseBody>() {
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
                            dummypatientslist.clear();
                            patientslist.clear();
                            patname_list.clear();
                            dummypatientslist = JsonParsing.GetJsonData(results);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("name", SideMenu.data_list.get(0).get("name") + " (Self)");
                            hashMap.put("user_id", StoredObjects.UserId);
                            hashMap.put("patient_id", StoredObjects.Logged_PatientId);
                            patientslist.add(hashMap);

                            for (int k = 0; k < dummypatientslist.size(); k++) {
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("name", dummypatientslist.get(k).get("name")+ " ("+dummypatientslist.get(k).get("relation")+")");
                                hashMap1.put("user_id", dummypatientslist.get(k).get("user_id"));
                                hashMap1.put("patient_id", dummypatientslist.get(k).get("patient_id"));
                                patientslist.add(hashMap1);

                            }
                            for (int k = 0; k < patientslist.size(); k++) {
                                patname_list.add(patientslist.get(k).get("name"));

                            }

                        } else {
                            dummypatientslist.clear();
                            patientslist.clear();
                            patname_list.clear();

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                   // CustomProgressbar.Progressbarcancel(activity);


                    serviceCalling();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                CustomProgressbar.Progressbarcancel(activity);


            }
        });

    }

    String patient_id="";
    private void PatientListPopUp(final EditText f_pat_status_edtx,Activity activity) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
        listPopupWindow.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, patname_list));
        listPopupWindow.setAnchorView(f_pat_status_edtx);
        listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                f_pat_status_edtx.setText(patname_list.get(position));

                patient_id=patientslist.get(position).get("user_id");
                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
    }

    private void serviceCalling() {

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            P_TestSugestionsService(getActivity(),pagecount,recordsperpage,from_date,to_date,search_text);

        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }


    private void P_TestSugestionsService(final Activity activity, final int pagecount, String recordsperpage,String fromdate,String todate,String search_text) {
        if(pagecount==1){
            CustomProgressbar.Progressbarshow(activity);
        }

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.TestSugestions(RetrofitInstance.test_suggestions, StoredObjects.UserId, StoredObjects.UserRoleId,fromdate,todate,pagecount+"",recordsperpage,search_text,"Doctor Suggestions").enqueue(new Callback<ResponseBody>() {
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

                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "p_tst_sugstn", p_test_recyler, R.layout.p_test_sugtn_lstitem);
                                p_test_recyler.setAdapter(adapter);

                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                p_test_recyler.invalidate();
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

                if(first_time.equalsIgnoreCase("Yes")){

                    first_time="no";
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

    public static void updatelay(ArrayList<HashMap<String, String>> data_list) {

        if(data_list.size()==0){
            nodatavailable_txt.setVisibility(View.VISIBLE);
            p_test_recyler.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            p_test_recyler.setVisibility(View.VISIBLE);
        }
    }

    public void fragmentcallinglay(Fragment fragment) {
        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ().replace (R.id.frame_container , fragment).commit ();

    }

}
