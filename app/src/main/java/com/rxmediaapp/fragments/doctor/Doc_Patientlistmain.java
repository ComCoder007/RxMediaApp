package com.rxmediaapp.fragments.doctor;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rxmediaapp.R;
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
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Doc_Patientlistmain extends Fragment {

  ImageView backbtn_img;
  TextView title_txt;
  RecyclerView d_patmainlist;
  public static HashMapRecycleviewadapter adapter;
  public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
  public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
  public static TextView nodatavailable_txt;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.doc_patientlist_main, null, false);
    StoredObjects.page_type = "patients_main";
    SideMenu.updatemenu(StoredObjects.page_type);
    initilization(v);

    serviceCalling();
    return v;
  }

  private void initilization(View v) {

    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);
    d_patmainlist = v.findViewById(R.id.d_patmainlist);
    nodatavailable_txt = v.findViewById(R.id.nodatavailable_txt);

    if (StoredObjects.tab_type.equalsIgnoreCase("patients")) {
      title_txt.setText("Patients List");
    } else if (StoredObjects.tab_type.equalsIgnoreCase("prescr")) {
      title_txt.setText("Prescriptions List");
    } else if (StoredObjects.tab_type.equalsIgnoreCase("testsugsted")) {
      title_txt.setText("Test Suggested");
    }


    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        fragmentcallinglay(new Doc_Dashboard());
      }
    });


    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    d_patmainlist.setLayoutManager(linearLayoutManager);



  }

  private void serviceCalling() {

    if (InterNetChecker.isNetworkAvailable(getActivity())) {
      PatientlistmainService(getActivity());
    } else {
      StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
    }
  }

  private void PatientlistmainService(final Activity activity) {
      CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.Docpatientlistmain(RetrofitInstance.doctor_clinics, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
              adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "d_doctorslist", d_patmainlist, R.layout.doc_patlistitem);
              d_patmainlist.setAdapter(adapter);


            }
          } catch (IOException | JSONException e) {
            e.printStackTrace();
          }
        }
          CustomProgressbar.Progressbarcancel(activity);
        updatelay(data_list);
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
          CustomProgressbar.Progressbarcancel(activity);

      }
    });

  }

  private void updatelay(ArrayList<HashMap<String, String>> data_list) {

    if (data_list.size() == 0) {
      nodatavailable_txt.setVisibility(View.VISIBLE);
      d_patmainlist.setVisibility(View.GONE);
    } else {
      nodatavailable_txt.setVisibility(View.GONE);
      d_patmainlist.setVisibility(View.VISIBLE);
    }
  }


  public void fragmentcallinglay(Fragment fragment) {
    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace(R.id.frame_container, fragment).commit();

  }


}




