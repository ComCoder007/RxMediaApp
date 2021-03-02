package com.rxmediaapp.fragments.teamleader;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rxmediaapp.R;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;

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

public class Franchisee_Details extends Fragment {

  ImageView backbtn_img;
  TextView title_txt;
  LinearLayout h_allpages_lay, h_onebtn_lay, h_twobtn_lay, h_threebtn_lay, h_fourbtn_lay;
  EditText h_search_edtxt;
  public static RecyclerView f_patient_recyler;
  public static HashMapRecycleviewadapter adapter;
  TextView addleadtitle_txt;
  ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();
  public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();

  public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();

  int pagecount=1,totalpages=0;
  String recordsperpage="10";

  public static TextView nodatavailable_txt;
  public static EditText d_patent_from_date_edtx, d_patent_to_date_edtx;

  DatePickerDialog datePickerDialog;
  int year;
  int month;
  int dayOfMonth;
  Calendar calendar;

  Button h_patient_submit_btn,h_patient_cancel_btn;

  String from_date="",to_date="",search_text="";

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.f_hospital_five, null, false);
    StoredObjects.page_type = "f_hospital_five";

    SideMenu.updatemenu(StoredObjects.page_type);
    pagecount = 1;
    initilization(v);
    serviceCalling();
    return v;
  }

  private void serviceCalling() {
    if (InterNetChecker.isNetworkAvailable(getContext())) {
      LeadsService(getActivity(),pagecount, recordsperpage);


    } else {
      StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
    }
  }

  private void initilization(View v) {

    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);
    h_allpages_lay = v.findViewById(R.id.h_allpages_lay);
    h_onebtn_lay = v.findViewById(R.id.h_onebtn_lay);
    h_twobtn_lay = v.findViewById(R.id.h_twobtn_lay);
    h_threebtn_lay = v.findViewById(R.id.h_threebtn_lay);
    h_fourbtn_lay = v.findViewById(R.id.h_fourbtn_lay);
    addleadtitle_txt = v.findViewById(R.id.addleadtitle_txt);
    f_patient_recyler = v.findViewById(R.id.f_hospital_five_recyler);
    addleadtitle_txt.setText("Add " + StoredObjects.tab_type + " Lead");
    title_txt.setText("Lead");

    nodatavailable_txt = v.findViewById(R.id.nodatavailable_txt);
    h_search_edtxt=v.findViewById(R.id.h_search_edtxt);
    d_patent_from_date_edtx = v.findViewById(R.id.d_patent_from_date_edtx);
    d_patent_to_date_edtx = v.findViewById(R.id.d_patent_to_date_edtx);
    h_patient_submit_btn = v.findViewById(R.id.h_patient_submit_btn);
    h_patient_cancel_btn = v.findViewById(R.id.h_patient_cancel_btn);


    h_patient_submit_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String fromdate=d_patent_from_date_edtx.getText().toString().trim();
        String todate=d_patent_to_date_edtx.getText().toString().trim();
        search_text=h_search_edtxt.getText().toString().trim();
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
        d_patent_from_date_edtx.setText("");
        d_patent_to_date_edtx.setText("");
        h_search_edtxt.setText("");

        search_text="";
        from_date="";
        to_date="";
        pagecount=1;
        serviceCalling();
      }
    });

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



    h_allpages_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (StoredObjects.tab_type.equalsIgnoreCase("Hospital")) {
          fragmentcallinglay(new F_Hospital_Lead());

        } else if (StoredObjects.tab_type.equalsIgnoreCase("Doctor")) {
          fragmentcallinglay(new F_Doctor_Lead());

        } else {
          fragmentcallinglay(new F_Patient_Lead());
        }

      }
    });

    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
          fm.popBackStack();
        }
      }
    });


     final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    f_patient_recyler.setLayoutManager(linearLayoutManager);

    f_patient_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
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


   /* h_search_edtxt.addTextChangedListener(new TextWatcher() {


      public void afterTextChanged(Editable s) {
      }

      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      public void onTextChanged(CharSequence s, int start, int before, int count) {
        int textlength = h_search_edtxt.getText().length();

        filter_list.clear();

        for (int i = 0; i < data_list.size(); i++) {
          if (textlength <= data_list.get(i).get("name").length()) {

            if ((data_list.get(i).get("name").toLowerCase().trim().contains(  h_search_edtxt.getText().toString().toLowerCase().trim()))) {

              filter_list.add(data_list.get(i));
            }
          }
        }



        adapter = new HashMapRecycleviewadapter(getActivity(), filter_list, "f_hospital_five", f_patient_recyler, R.layout.f_hospital_five_listitem);
        f_patient_recyler.setAdapter(adapter);
        if(filter_list.size()==0){
          nodatavailable_txt.setVisibility(View.VISIBLE);
          f_patient_recyler.setVisibility(View.GONE);

        }else{
          nodatavailable_txt.setVisibility(View.GONE);
          f_patient_recyler.setVisibility(View.VISIBLE);

        }



      }
    });
*/

  }




  private void LeadsService(final Activity activity, final int pagecount, String recordsperpage) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    String method="";
    if (StoredObjects.tab_type.equalsIgnoreCase("Hospital")) {
      method=RetrofitInstance.hospital_leads;
    } else if (StoredObjects.tab_type.equalsIgnoreCase("Doctor")) {
      method=RetrofitInstance.doctor_leads;
    } else {
      method=RetrofitInstance.patient_leads;
    }
    api.GetLeads(method, StoredObjects.UserId, StoredObjects.UserRoleId,"" + pagecount,recordsperpage,from_date,to_date,search_text).enqueue(new Callback<ResponseBody>() {
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

                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "f_hospital_five", f_patient_recyler, R.layout.f_hospital_five_listitem);
                f_patient_recyler.setAdapter(adapter);

              } else {

                dummy_list = JsonParsing.GetJsonData(results);
                data_list.addAll(dummy_list);
                adapter.notifyDataSetChanged();
                f_patient_recyler.invalidate();
              }
              updatelay(data_list);

            } else {
              data_list.clear();
              updatelay(data_list);
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

  public static void updatelay(ArrayList<HashMap<String, String>> data_list) {

    if (data_list.size() == 0) {
      nodatavailable_txt.setVisibility(View.VISIBLE);
      f_patient_recyler.setVisibility(View.GONE);
    } else {
      nodatavailable_txt.setVisibility(View.GONE);
      f_patient_recyler.setVisibility(View.VISIBLE);
    }
  }

  public void fragmentcallinglay(Fragment fragment) {

    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace(R.id.frame_container, fragment).addToBackStack("").commit();

  }

}

