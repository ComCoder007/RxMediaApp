package com.rxmediaapp.fragments.patient;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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

public class P_Prescriptionslist extends Fragment {

  ImageView backbtn_img;
  TextView title_txt;

  static RecyclerView prescription_recyler, doc_prescription_recyler;
  public static TextView nodatavailable_txt;
  LinearLayout add_prescr_lay;
  public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
  public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
  int pagecount = 1, totalpages = 0;
  String recordsperpage = "10";
  CustomNormalButton p_prescription_submit_btn;
  CustomEditText p_prescription_from_date_edtx,p_patient_edtx, p_type_edtx,p_prescription_to_date_edtx,p_presecription_searchText;
  public static HashMapRecycleviewadapter adapter;

  DatePickerDialog datePickerDialog;
  int year;
  int month;
  int dayOfMonth;
  Calendar calendar;
  String patient_id="",from_date="",to_date="",type="",search_text="";

  public static ArrayList<HashMap<String, String>> patientslist = new ArrayList<>();
  public   ArrayList<HashMap<String, String>> dummypatientslist = new ArrayList<>();
  public static ArrayList<String> patname_list = new ArrayList<>();
  Button p_patient_cancel_btn;

  String[] typelist = {"Doctor Prescriptions", "Own Prescriptions"};

  String first_time="yes";
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.p_prescriptionslist, null, false);
    StoredObjects.page_type = "p_preslist";
    SideMenu.updatemenu(StoredObjects.page_type);
    pagecount=1;
    first_time="yes";
    try {
      StoredObjects.listcount= 2;
      SideMenu.adapter.notifyDataSetChanged();
    }catch (Exception e){

    }
    initilization(v);

    if (InterNetChecker.isNetworkAvailable(getContext())) {
      getAssistantService(getActivity(),1,"20");
    } else {
      StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
    }

    return v;
  }


  private void getAssistantService(final Activity activity,final int pagecount,String recordsperpage) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getmembers(RetrofitInstance.members, StoredObjects.UserId, StoredObjects.UserRoleId,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
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

          //CustomProgressbar.Progressbarcancel(activity);

          serviceCalling();

        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {

          CustomProgressbar.Progressbarcancel(activity);


      }
    });

  }
  private void serviceCalling() {
    if (InterNetChecker.isNetworkAvailable(getContext())) {

      prescriptionListService(getActivity(),pagecount, recordsperpage);
    } else {
      StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
    }
  }

  private void prescriptionListService(final Activity activity ,final int pagecount, String recordsperpage) {


    if(pagecount==1){
      if(first_time.equalsIgnoreCase("Yes")){

      }else{
        CustomProgressbar.Progressbarshow(activity);
      }

    }
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.PrescriptionList(RetrofitInstance.prescriptions_list, from_date, to_date, type, search_text, StoredObjects.UserId, StoredObjects.UserRoleId,""+pagecount,recordsperpage,patient_id ).enqueue(new Callback<ResponseBody>() {
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

              if (P_Prescriptionslist.this.pagecount == 1) {
                data_list.clear();
                dummy_list = JsonParsing.GetJsonData(results);
                data_list.addAll(dummy_list);
                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "patprescr_doc_list", doc_prescription_recyler, R.layout.doc_prescription_lstitem);
                doc_prescription_recyler.setAdapter(adapter);

              } else {

                dummy_list = JsonParsing.GetJsonData(results);
                data_list.addAll(dummy_list);
                adapter.notifyDataSetChanged();
                doc_prescription_recyler.invalidate();
              }
              updatelay(data_list);


            } else {
              if (pagecount == 1) {
                data_list.clear();

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
        if(first_time.equalsIgnoreCase("Yes")){
          first_time="no";
        }

      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        if(first_time.equalsIgnoreCase("Yes")){
          first_time="no";
        }
        if (pagecount == 1) {
          CustomProgressbar.Progressbarcancel(activity);
        }

      }
    });

  }


  private void TypePopUp(final EditText f_pat_status_edtx,Activity activity) {
    final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
    listPopupWindow.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, typelist));
    listPopupWindow.setAnchorView(f_pat_status_edtx);
    listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        f_pat_status_edtx.setText(typelist[position]);
        listPopupWindow.dismiss();

      }
    });

    listPopupWindow.show();
  }


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

  private void initilization(View v) {

    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);
    prescription_recyler = v.findViewById(R.id.prescription_recyler);
    doc_prescription_recyler = v.findViewById(R.id.doc_prescription_recyler);
    nodatavailable_txt = v.findViewById(R.id.nodatavailable_txt);
    add_prescr_lay = v.findViewById(R.id.add_prescr_lay);
    p_prescription_from_date_edtx = v.findViewById(R.id.p_prescription_from_date_edtx);
    p_prescription_to_date_edtx = v.findViewById(R.id.p_prescription_to_date_edtx);
     p_presecription_searchText = v.findViewById(R.id.p_presecription_searchText);
    p_prescription_submit_btn = v.findViewById(R.id.p_prescription_submit_btn);
    p_patient_cancel_btn= v.findViewById(R.id.p_patient_cancel_btn);

    p_patient_edtx= v.findViewById(R.id.p_patient_edtx);
    p_type_edtx= v.findViewById(R.id.p_type_edtx);
    title_txt.setText("Prescriptions List");

    //patient_id=StoredObjects.UserId;

    p_prescription_submit_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        String fromdate=p_prescription_from_date_edtx.getText().toString().trim();
        String todate=p_prescription_to_date_edtx.getText().toString().trim();
        search_text=p_presecription_searchText.getText().toString().trim();
        from_date=fromdate;
        to_date=todate;
        type=p_type_edtx.getText().toString().trim();

        if(fromdate.length()>0||todate.length()>0||search_text.length()>0||type.length()>0||patient_id.length()>0){

          pagecount=1;
          serviceCalling();
        }else{
          StoredObjects.ToastMethod("Please select Filter options",getActivity());
        }

      }
    });

    p_patient_cancel_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        patient_id=StoredObjects.UserId;
        from_date="";
        to_date="";
        type="";
        search_text="";
        pagecount=1;
        p_prescription_from_date_edtx.setText("");
        p_prescription_to_date_edtx.setText("");
        p_presecription_searchText.setText("");
        p_patient_edtx.setText("");
        p_type_edtx.setText("");

        serviceCalling();
      }
    });
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

    p_type_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        TypePopUp(p_type_edtx,getActivity());
      }
    });

    p_prescription_from_date_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        p_prescription_to_date_edtx.setText("");
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                  @Override
                  public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                    p_prescription_to_date_edtx.setText("");
                    p_prescription_from_date_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
                  }
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();
      }
    });


    p_prescription_to_date_edtx.setOnClickListener(new View.OnClickListener() {
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
                    String from_date=p_prescription_from_date_edtx.getText().toString().trim();
                    if(from_date.length()==0){
                      StoredObjects.ToastMethod( getString(R.string.selectfrm_date),getActivity());
                    }else{
                      if(StoredObjects.daysDifference(from_date,to_date)==true){
                        p_prescription_to_date_edtx.setText(to_date);
                      }else{
                        p_prescription_to_date_edtx.setText("");
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
      public void onClick(View view) {
        fragmentcalling(new P_Dashboard());
      }
    });

    add_prescr_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        P_Add_Prescription.update_data.clear();
        HashMap<String,String> hashMap=new HashMap<>();

        hashMap.put("medication","");
        hashMap.put("selected_brands","");
        hashMap.put("selected_molecules","");
        hashMap.put("tests_suggested","");
        P_Add_Prescription.update_data.add(hashMap);
        fragmentcallinglay(new P_Add_Prescription());
      }
    });

    final LinearLayoutManager linearLayoutManagerone = new LinearLayoutManager(getActivity());
    doc_prescription_recyler.setLayoutManager(linearLayoutManagerone);


  }


  public static void updatelay(ArrayList<HashMap<String, String>> data_list) {

    if (data_list.size() == 0) {
      nodatavailable_txt.setVisibility(View.VISIBLE);
      doc_prescription_recyler.setVisibility(View.GONE);
    } else {
      nodatavailable_txt.setVisibility(View.GONE);
      doc_prescription_recyler.setVisibility(View.VISIBLE);
    }
  }

  public void fragmentcallinglay(Fragment fragment) {

    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace(R.id.frame_container, fragment).addToBackStack("").commit();

  }

  public void fragmentcalling(Fragment fragment) {

    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

  }


}


