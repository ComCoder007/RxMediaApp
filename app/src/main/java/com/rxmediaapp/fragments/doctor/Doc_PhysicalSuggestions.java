package com.rxmediaapp.fragments.doctor;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.customfonts.CustomEditText;
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
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Doc_PhysicalSuggestions extends Fragment {

  ImageView backbtn_img;
  TextView title_txt;
  LinearLayout add_physical_lay;
  public static TextView nodatavailable_txt;
  public static RecyclerView patientlist_history_recyler;
  public static HashMapRecycleviewadapter adapter;
  public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
  public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();

  EditText ass_search_edtxt;
  int pagecount=1,totalpages=0;
  String recordsperpage="50";

  ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.doc_patientlist_history_one, null, false);
    StoredObjects.page_type = "phy_suggestion";

    SideMenu.updatemenu(StoredObjects.page_type);
    try {
      StoredObjects.listcount= 7;
      SideMenu.adapter.notifyDataSetChanged();
    }catch (Exception e){

    }
    initilization(v);
    pagecount=1;
    serviceCalling();
    return v;
  }


  private void initilization(View v) {

    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);
    add_physical_lay = v.findViewById(R.id.add_physical_lay);
    patientlist_history_recyler = v.findViewById(R.id.patientlist_history_recyler);
    ass_search_edtxt = v.findViewById(R.id.ass_search_edtxt);
    nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);

    title_txt.setText("Physical Suggestion");


    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        fragmentcalling(new Doc_Dashboard());
      }
    });

    add_physical_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Physicalexminationpopup(getActivity());
      }
    });
    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    patientlist_history_recyler.setLayoutManager(linearLayoutManager);

    ass_search_edtxt.addTextChangedListener(new TextWatcher() {


      public void afterTextChanged(Editable s) {
      }

      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      public void onTextChanged(CharSequence s, int start, int before, int count) {
        int textlength = ass_search_edtxt.getText().length();

        filter_list.clear();

        for (int i = 0; i < data_list.size(); i++) {
          if (textlength <= data_list.get(i).get("suggestion").length()) {

            if ((data_list.get(i).get("suggestion").toLowerCase().trim().contains(  ass_search_edtxt.getText().toString().toLowerCase().trim()))) {

              filter_list.add(data_list.get(i));
            }
          }
        }

        adapter = new HashMapRecycleviewadapter(getActivity(), filter_list, "patientlist_history", patientlist_history_recyler, R.layout.doc_patientlist_history_listitem);
        patientlist_history_recyler.setAdapter(adapter);
        if(filter_list.size()==0){
          nodatavailable_txt.setVisibility(View.VISIBLE);
          patientlist_history_recyler.setVisibility(View.GONE);

        }else{
          nodatavailable_txt.setVisibility(View.GONE);
          patientlist_history_recyler.setVisibility(View.VISIBLE);

        }



      }
    });

    patientlist_history_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

/*
        StoredObjects.hashmaplist(2);
        patientlist_history_recyler.setAdapter(new HashMapRecycleviewadapter(getActivity(),StoredObjects.dummy_list,"patientlist_history",patientlist_history_recyler,R.layout.doc_patientlist_history_listitem));
*/

  }

  private void serviceCalling() {

    if (InterNetChecker.isNetworkAvailable(getActivity())) {
      getPhsyicalSuggestions(getActivity(),pagecount,recordsperpage);
    } else {
      StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
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

  private void Physicalexminationpopup(final Activity activity) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.add_physical_examination_popup);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomButton search_button = (CustomButton) dialog.findViewById(R.id.search_button);
    final CustomEditText physcial_examination_edtx = (CustomEditText) dialog.findViewById(R.id.physcial_examination_edtx);

    final ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);


    cancel_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        dialog.dismiss();
      }
    });

    canclimg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        dialog.dismiss();
      }
    });


    search_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String physcial_examination_str = physcial_examination_edtx.getText().toString().trim();
        if (StoredObjects.inputValidation(physcial_examination_edtx, "Please enter suggestion", getActivity())){
          if (InterNetChecker.isNetworkAvailable(getActivity())) {
            dialog.dismiss();
            addPhsyicalSuggestions(getActivity(), physcial_examination_str);
          } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
          }
        }



      }
    });

    dialog.show();
  }


  private void getPhsyicalSuggestions1(final Activity activity, final int pagecount, String recordsperpage) {
    if(pagecount==1){
      //CustomProgressbar.Progressbarshow(activity);
    }

    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getPhsyicalSuggestions(RetrofitInstance.physical_suggestions, StoredObjects.UserId, StoredObjects.UserRoleId,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
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

                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "patientlist_history", patientlist_history_recyler, R.layout.doc_patientlist_history_listitem);
                patientlist_history_recyler.setAdapter(adapter);

              } else {

                dummy_list = JsonParsing.GetJsonData(results);
                data_list.addAll(dummy_list);
                adapter.notifyDataSetChanged();
                patientlist_history_recyler.invalidate();
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

  private void getPhsyicalSuggestions(final Activity activity, final int pagecount, String recordsperpage) {
    if(pagecount==1){
      CustomProgressbar.Progressbarshow(activity);
    }

    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getPhsyicalSuggestions(RetrofitInstance.physical_suggestions, StoredObjects.UserId, StoredObjects.UserRoleId,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
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

                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "patientlist_history", patientlist_history_recyler, R.layout.doc_patientlist_history_listitem);
                patientlist_history_recyler.setAdapter(adapter);

              } else {

                dummy_list = JsonParsing.GetJsonData(results);
                data_list.addAll(dummy_list);
                adapter.notifyDataSetChanged();
                patientlist_history_recyler.invalidate();
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

  private void addPhsyicalSuggestions(final Activity activity, String physcial_examination_str) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.addPhsyicalSuggestions(RetrofitInstance.add_physical_suggestion,StoredObjects.UserRoleId, StoredObjects.UserId,physcial_examination_str).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("response", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Added Successfully", activity);

              if (InterNetChecker.isNetworkAvailable(getActivity())) {
                getPhsyicalSuggestions1(getActivity(),pagecount,recordsperpage);
              } else {
                StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
              }

            }
          } catch (IOException | JSONException e) {
            e.printStackTrace();
          }
        }


        //CustomProgressbar.Progressbarcancel(activity);
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        CustomProgressbar.Progressbarcancel(activity);

      }
    });

  }

  public static void updatelay(ArrayList<HashMap<String, String>> data_list) {

    if(data_list.size()==0){
      nodatavailable_txt.setVisibility(View.VISIBLE);
      patientlist_history_recyler.setVisibility(View.GONE);
    }else{
      nodatavailable_txt.setVisibility(View.GONE);
      patientlist_history_recyler.setVisibility(View.VISIBLE);
    }
  }

}


