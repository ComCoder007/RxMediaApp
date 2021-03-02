package com.rxmediaapp.fragments.teamleader;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.rxmediaapp.fragments.dashboards.TL_Dashboard;
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

public class Marketing_Exicutive extends Fragment {

  ImageView backbtn_img;
  TextView title_txt;
  LinearLayout add_marketing_ex_lay;
  EditText me_search_edtxt;
  public static RecyclerView marketing_exicutive_recyler;
  public static HashMapRecycleviewadapter adapter;
  public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();

  public static TextView nodatavailable_txt;

  public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
  int pagecount=1,totalpages=0;
  String recordsperpage="50";

  ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.marketing_exicutive, null, false);
    StoredObjects.page_type = "marketing";
    data_list.clear();
    SideMenu.updatemenu(StoredObjects.page_type);
    try {
      StoredObjects.listcount= 1;
      SideMenu.adapter.notifyDataSetChanged();
    }catch (Exception e){

    }
    initilization(v);
    pagecount=1;
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
    me_search_edtxt = v.findViewById(R.id.me_search_edtxt);
    add_marketing_ex_lay = v.findViewById(R.id.add_marketing_ex_lay);
    marketing_exicutive_recyler = v.findViewById(R.id.marketing_exicutive_recyler);

    nodatavailable_txt = v.findViewById(R.id.nodatavailable_txt);

    title_txt.setText("Marketing Executive");


    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        fragmentcallinglay1(new TL_Dashboard());
      }
    });

    me_search_edtxt.addTextChangedListener(new TextWatcher() {


      public void afterTextChanged(Editable s) {
      }

      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      public void onTextChanged(CharSequence s, int start, int before, int count) {
        int textlength = me_search_edtxt.getText().length();

        filter_list.clear();

        for (int i = 0; i < data_list.size(); i++) {
          if (textlength <= data_list.get(i).get("name").length()) {

            if ((data_list.get(i).get("name").toLowerCase().trim().contains(  me_search_edtxt.getText().toString().toLowerCase().trim()))) {

              filter_list.add(data_list.get(i));
            }
          }
        }


        adapter = new HashMapRecycleviewadapter(getActivity(), filter_list, "marketing_exicutive", marketing_exicutive_recyler, R.layout.marketing_exicutive_listitem);
        marketing_exicutive_recyler.setAdapter(adapter);
        if(filter_list.size()==0){
          nodatavailable_txt.setVisibility(View.VISIBLE);
          marketing_exicutive_recyler.setVisibility(View.GONE);

        }else{
          nodatavailable_txt.setVisibility(View.GONE);
          marketing_exicutive_recyler.setVisibility(View.VISIBLE);

        }



      }
    });

    add_marketing_ex_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        fragmentcallinglay(new Add_Marketing_Exicutive());
      }
    });


    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    marketing_exicutive_recyler.setLayoutManager(linearLayoutManager);

    marketing_exicutive_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    // marketing_exicutive_recyler.setAdapter(new HashMapRecycleviewadapter(getActivity(), StoredObjects.dummy_list, "marketing_exicutive", marketing_exicutive_recyler, R.layout.marketing_exicutive_listitem));


  }

  private void serviceCalling() {

    if (InterNetChecker.isNetworkAvailable(getActivity())) {
      marketingExecutivesService(getActivity(),pagecount,recordsperpage);
    } else {
      StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
    }
  }

  private void marketingExecutivesService(final Activity activity, final int pagecount, String recordsperpage) {

    if(pagecount==1){
      CustomProgressbar.Progressbarshow(activity);
    }
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.marketingExecutives(RetrofitInstance.marekting_executives, StoredObjects.UserId, StoredObjects.UserRoleId,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
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

                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "marketing_exicutive", marketing_exicutive_recyler, R.layout.marketing_exicutive_listitem);
                marketing_exicutive_recyler.setAdapter(adapter);

              } else {

                dummy_list = JsonParsing.GetJsonData(results);
                data_list.addAll(dummy_list);
                adapter.notifyDataSetChanged();
                marketing_exicutive_recyler.invalidate();
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

    if (data_list.size() == 0) {
      nodatavailable_txt.setVisibility(View.VISIBLE);
      marketing_exicutive_recyler.setVisibility(View.GONE);
    } else {
      nodatavailable_txt.setVisibility(View.GONE);
      marketing_exicutive_recyler.setVisibility(View.VISIBLE);
    }
  }


  public void fragmentcallinglay(Fragment fragment) {

    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace(R.id.frame_container, fragment).addToBackStack("").commit();

  }

}

