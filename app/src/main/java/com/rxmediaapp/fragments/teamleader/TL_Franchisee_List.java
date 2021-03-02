package com.rxmediaapp.fragments.teamleader;

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
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.fragments.dashboards.Franchisee_Dashboard;
import com.rxmediaapp.fragments.dashboards.Marketing_Dashboard;
import com.rxmediaapp.fragments.dashboards.TL_Dashboard;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;
import com.rxmediaapp.Sidemenu.SideMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TL_Franchisee_List extends Fragment {

  ImageView backbtn_img;
  TextView title_txt,fran_addtab_txt;
  LinearLayout add_franchisee_lay, new_actionbar_lay;
  public static  EditText tl_search_edtxt;

  public static TextView nodatavailable_txt;
  public static RecyclerView tl_franchises_recyler;
  public static HashMapRecycleviewadapter adapter;

  public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
  public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
  int pagecount=1,totalpages=0;
  String recordsperpage="10";

  ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.tl_franchisee_list_one, null, false);
    pagecount=1;
    data_list.clear();
    StoredObjects.page_type = "tl_franchises";
    SideMenu.updatemenu(StoredObjects.page_type);
    try {
      if (StoredObjects.UserType.equalsIgnoreCase("Team Leader")) {
        StoredObjects.listcount= 2;
        SideMenu.adapter.notifyDataSetChanged();
      }else if(StoredObjects.UserType.equalsIgnoreCase("Marketing Executive")){
        StoredObjects.listcount= 1;
        SideMenu.adapter.notifyDataSetChanged();
      }else if(StoredObjects.UserType.equalsIgnoreCase("Franchisee")){
        StoredObjects.listcount= 1;
        SideMenu.adapter.notifyDataSetChanged();
      }else{

      }
    }catch (Exception e){

    }
    initilization(v);
    serviceCalling();
    return v;
  }

  private void initilization(View v) {

    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);
    tl_search_edtxt = v.findViewById(R.id.tlf_search_edtxt);
    add_franchisee_lay = v.findViewById(R.id.tlf_add_franchisee_lay);
    tl_franchises_recyler = v.findViewById(R.id.tl_franchises_one_recyler);
    new_actionbar_lay = v.findViewById(R.id.new_actionbar_lay);

    nodatavailable_txt = v.findViewById(R.id.nodatavailable_txt);
    fran_addtab_txt=v.findViewById(R.id.fran_addtab_txt);

    title_txt.setText("Franchises");

   /* if (StoredObjects.UserType.equalsIgnoreCase("Team Leader")) {
      new_actionbar_lay.setVisibility(View.VISIBLE);
    } else {
      new_actionbar_lay.setVisibility(View.GONE);
    }*/
    add_franchisee_lay.setVisibility(View.VISIBLE);


    tl_search_edtxt.addTextChangedListener(new TextWatcher() {


      public void afterTextChanged(Editable s) {
      }

      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      public void onTextChanged(CharSequence s, int start, int before, int count) {
        int textlength = tl_search_edtxt.getText().length();

        filter_list.clear();

        for (int i = 0; i < data_list.size(); i++) {
          if (textlength <= data_list.get(i).get("name").length()) {

            if ((data_list.get(i).get("name").toLowerCase().trim().contains(  tl_search_edtxt.getText().toString().toLowerCase().trim()))) {

              filter_list.add(data_list.get(i));
            }
          }
        }

        adapter = new HashMapRecycleviewadapter(getActivity(), filter_list, "tl_franchises_one", tl_franchises_recyler, R.layout.tl_franchisee_list_one_listitem);
        tl_franchises_recyler.setAdapter(adapter);
        if(filter_list.size()==0){
          nodatavailable_txt.setVisibility(View.VISIBLE);
          tl_franchises_recyler.setVisibility(View.GONE);

        }else{
          nodatavailable_txt.setVisibility(View.GONE);
          tl_franchises_recyler.setVisibility(View.VISIBLE);

        }



      }
    });
  if (StoredObjects.UserType.equalsIgnoreCase("Franchisee")) {
    fran_addtab_txt.setText("Add Sub Franchisee");
    } else {
    fran_addtab_txt.setText("Add Franchisee");
    }


    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (StoredObjects.UserType.equalsIgnoreCase("Team Leader")) {
          fragmentcallinglay(new TL_Dashboard());
        }else if(StoredObjects.UserType.equalsIgnoreCase("Franchisee")){
          fragmentcallinglay(new Franchisee_Dashboard());
        }else{
          fragmentcallinglay(new Marketing_Dashboard());
        }

      }
    });


    add_franchisee_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        fragmentcalling(new AddFranchisee());
      }
    });

    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    tl_franchises_recyler.setLayoutManager(linearLayoutManager);

    tl_franchises_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

  private void serviceCalling() {

    if (StoredObjects.UserType.equalsIgnoreCase("Franchisee")) {
      if (InterNetChecker.isNetworkAvailable(getContext())) {
        getFranchiseeListService(getActivity(),RetrofitInstance.sub_franchisee);
      } else {
        StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
      }
    } else {
      if (InterNetChecker.isNetworkAvailable(getContext())) {
        getFranchiseeListService(getActivity(),RetrofitInstance.franchisee_list);
      } else {
        StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
      }
    }

  }

  private void getFranchiseeListService(final FragmentActivity activity,String method) {
    if(pagecount==1){
      CustomProgressbar.Progressbarshow(activity);
    }
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getFranchiseeList(method, StoredObjects.UserId, StoredObjects.UserRoleId,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
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
                updatelay(data_list);
                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "tl_franchises_one", tl_franchises_recyler, R.layout.tl_franchisee_list_one_listitem);
                tl_franchises_recyler.setAdapter(adapter);
                updatelay(data_list);
              } else {

                dummy_list = JsonParsing.GetJsonData(results);
                data_list.addAll(dummy_list);
                adapter.notifyDataSetChanged();
                tl_franchises_recyler.invalidate();
              }

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
      tl_franchises_recyler.setVisibility(View.GONE);
      tl_search_edtxt.setVisibility(View.GONE);
    } else {
      nodatavailable_txt.setVisibility(View.GONE);
      tl_franchises_recyler.setVisibility(View.VISIBLE);
      tl_search_edtxt.setVisibility(View.GONE);
    }
  }

  public void fragmentcallinglay(Fragment fragment) {

    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace(R.id.frame_container, fragment).commit();

  }


  public void fragmentcalling(Fragment fragment) {

    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace(R.id.frame_container, fragment).addToBackStack("").commit();

  }

}

