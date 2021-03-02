package com.rxmediaapp.fragments.doctor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rxmediaapp.R;
import com.rxmediaapp.customfonts.CustomNormalButton;
import com.rxmediaapp.fragments.dashboards.Doc_Dashboard;
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

public class Doc_Patient_List extends Fragment {

  ImageView backbtn_img;
  TextView title_txt;
  LinearLayout f_add_patient_lay;
  EditText f_search_patient_edtxt;
  TextView nodatavailable_txt;
  RecyclerView f_patient_recyler;
   HashMapRecycleviewadapter adapter;
   ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
  int pagecount=1,totalpages=0;
  String recordsperpage="100";

  ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.f_patientlist_one, null, false);
    StoredObjects.page_type = "tl_patients";

    SideMenu.updatemenu(StoredObjects.page_type);
    try {
      StoredObjects.listcount= 4;
      SideMenu.adapter.notifyDataSetChanged();
    }catch (Exception e){

    }
    initilization(v);
    pagecount=1;
    serviceCalling();
    return v;
  }

  private void serviceCalling() {
    if (InterNetChecker.isNetworkAvailable(getContext())) {
      getPatientsService(getActivity(),pagecount,recordsperpage);
    } else {
      StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
    }
  }

  private void initilization(View v) {

    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);
    f_add_patient_lay = v.findViewById(R.id.f_add_patient_lay);
    f_search_patient_edtxt = v.findViewById(R.id.f_search_patient_edtxt);
    nodatavailable_txt = v.findViewById(R.id.nodatavailable_txt);
    f_patient_recyler = v.findViewById(R.id.f_patient_one_recyler);
    title_txt.setText("Patient List");
    f_add_patient_lay.setVisibility(View.GONE);

    f_search_patient_edtxt.setHint("Name, Phone");

    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(SideMenu.hospitals_list.size()==0||SideMenu.hospitals_list.size()==1){

          fragmentcallinglay(new Doc_Dashboard());
        }else{

          fragmentcallinglay(new Doc_Patientlistmain());

        }
      }
    });



    // customRecyclerview.Assigndatatorecyleviewhashmap( marketing_exicutive_recyler, StoredObjects.dummy_list,"marketing_exicutive", StoredObjects.Listview, 0, StoredObjects.ver_orientation, R.layout.marketing_exicutive_listitem);

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

    f_search_patient_edtxt.addTextChangedListener(new TextWatcher() {


      public void afterTextChanged(Editable s) {
      }

      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      public void onTextChanged(CharSequence s, int start, int before, int count) {
        int textlength = f_search_patient_edtxt.getText().length();

        filter_list.clear();

        for (int i = 0; i < data_list.size(); i++) {
          if (textlength <= data_list.get(i).get("name").length()
                  ||textlength <= data_list.get(i).get("phone").length()) {

            if ((data_list.get(i).get("name").toLowerCase().trim().contains(  f_search_patient_edtxt.getText().toString().toLowerCase().trim()))
                    ||(data_list.get(i).get("phone").toLowerCase().trim().contains(  f_search_patient_edtxt.getText().toString().toLowerCase().trim()))) {

              filter_list.add(data_list.get(i));
            }
          }
        }


        adapter = new HashMapRecycleviewadapter(getActivity(), filter_list, "d_patient_one", f_patient_recyler, R.layout.d_patient_listitem);
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

    //  f_patient_recyler.setAdapter(new HashMapRecycleviewadapter(getActivity(),StoredObjects.dummy_list,"f_patient_one",f_patient_recyler,R.layout.f_patientlist_one_listitem));

  }


  private void getPatientsService(final Activity activity, final int pagecount, String recordsperpage) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getDoctorPatients(RetrofitInstance.doctors_patients, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
              adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "d_patient_one", f_patient_recyler, R.layout.d_patient_listitem);
              f_patient_recyler.setAdapter(adapter);

              updatelay(data_list);
            } else {
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

  public  void updatelay(ArrayList<HashMap<String, String>> data_list) {

    if (data_list.size() == 0) {
      nodatavailable_txt.setVisibility(View.VISIBLE);
      f_patient_recyler.setVisibility(View.GONE);
      f_search_patient_edtxt.setVisibility(View.GONE);
    } else {
      nodatavailable_txt.setVisibility(View.GONE);
      f_patient_recyler.setVisibility(View.VISIBLE);
      f_search_patient_edtxt.setVisibility(View.VISIBLE);
    }
  }
  public void fragmentcallinglay(Fragment fragment) {

    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace(R.id.frame_container, fragment).addToBackStack("").commit();

  }

  public void fragmentcalling(Fragment fragment) {

    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace(R.id.frame_container, fragment).commit();

  }


}

