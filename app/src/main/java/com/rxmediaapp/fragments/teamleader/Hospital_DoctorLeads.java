package com.rxmediaapp.fragments.teamleader;

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
import com.rxmediaapp.fragments.dashboards.Franchisee_Dashboard;
import com.rxmediaapp.fragments.dashboards.Marketing_Dashboard;
import com.rxmediaapp.fragments.dashboards.SubFranchisee_Dashboard;
import com.rxmediaapp.fragments.dashboards.TL_Dashboard;
import com.rxmediaapp.storedobjects.StoredObjects;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;

import java.util.ArrayList;
import java.util.HashMap;

public class Hospital_DoctorLeads extends Fragment {
  public static TextView nodatavailable_txt;
  ImageView backbtn_img;
  TextView title_txt;
  public static RecyclerView h_doclist;
  public static HashMapRecycleviewadapter adapter;

  String[] tabslist = {"Hospital", "Doctor", "Patient"};

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.f_hospital_four, null, false);
    StoredObjects.page_type = "hospital_leads";

    SideMenu.updatemenu(StoredObjects.page_type);
    try {
      if (StoredObjects.UserType.equalsIgnoreCase("Team Leader")) {
        StoredObjects.listcount= 6;
        SideMenu.adapter.notifyDataSetChanged();

      } else if(StoredObjects.UserType.equalsIgnoreCase("Marketing Executive")){
        StoredObjects.listcount= 5;
        SideMenu.adapter.notifyDataSetChanged();
      }else if(StoredObjects.UserType.equalsIgnoreCase("Franchisee")){
        StoredObjects.listcount= 5;
        SideMenu.adapter.notifyDataSetChanged();
      }else if(StoredObjects.UserType.equalsIgnoreCase("Sub Franchisee")){
        StoredObjects.listcount= 4;
        SideMenu.adapter.notifyDataSetChanged();
      }else{

      }

    }catch (Exception e){

    }
    initilization(v);
    return v;
  }

  private void initilization(View v) {

    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);
    h_doclist = v.findViewById(R.id.f_hospital_four_recycler);
    nodatavailable_txt = v.findViewById(R.id.nodatavailable_txt);
    title_txt.setText("Lead");


    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

       /* if (StoredObjects.UserType.equalsIgnoreCase("Team Leader")) {

          fragmentcalling(new Marketing_Exicutive());
        } else if (StoredObjects.UserType.equalsIgnoreCase("Marketing Executive")) {

          fragmentcalling(new TL_Franchisee_List());
        } else if (StoredObjects.UserType.equalsIgnoreCase("Franchisee")) {

          fragmentcalling(new TL_Franchisee_List());
        } else if (StoredObjects.UserType.equalsIgnoreCase("Sub Franchisee")) {

          fragmentcalling(new TL_Hospitals());
        }*/

        if (StoredObjects.UserType.equalsIgnoreCase("Team Leader")) {
          fragmentcalling(new TL_Dashboard());
        }else if(StoredObjects.UserType.equalsIgnoreCase("Franchisee")){
          fragmentcalling(new Franchisee_Dashboard());
        }else if(StoredObjects.UserType.equalsIgnoreCase("Sub Franchisee")){
          fragmentcalling(new SubFranchisee_Dashboard());
        }else{
          fragmentcalling(new Marketing_Dashboard());
        }
      }
    });

    StoredObjects.getrray(tabslist);
    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    h_doclist.setLayoutManager(linearLayoutManager);

    //   updatelay(data_list); add else block
    adapter = new HashMapRecycleviewadapter(getActivity(), StoredObjects.menuitems_list, "f_hospital_four", h_doclist, R.layout.f_hospital_four_listitem);
    h_doclist.setAdapter(adapter);

  }



  public void fragmentcalling(Fragment fragment) {
    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace(R.id.frame_container, fragment).commit();

  }


}



