package com.rxmediaapp.fragments.teamleader;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.fragments.dashboards.Franchisee_Dashboard;
import com.rxmediaapp.fragments.dashboards.Marketing_Dashboard;
import com.rxmediaapp.fragments.dashboards.SubFranchisee_Dashboard;
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

public class TL_Doctors extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    LinearLayout add_doctor_lay;
    public static  EditText tl_doc_search_edtxt,h_spclztn_edtx;
    public static TextView nodatavailable_txt;
    public static RecyclerView tl_doctors_recyler;
    ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();

    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();

    int pagecount=1,totalpages=0;
    String recordsperpage="50";
    public static HashMapRecycleviewadapter adapter;

    String speacialisation_id="" ,search_text="";

    ArrayList<String> specialisation_names_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> specialization_list = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.tl_doctor_one,null,false );
        StoredObjects.page_type="tl_doctors";

        first_time="yes";
        SideMenu.updatemenu(StoredObjects.page_type);
        try {
            if (StoredObjects.UserType.equalsIgnoreCase("Team Leader")) {
                StoredObjects.listcount= 4;
                SideMenu.adapter.notifyDataSetChanged();
            }else if(StoredObjects.UserType.equalsIgnoreCase("Marketing Executive")){
                StoredObjects.listcount= 3;
                SideMenu.adapter.notifyDataSetChanged();
            }else if(StoredObjects.UserType.equalsIgnoreCase("Franchisee")){
                StoredObjects.listcount= 3;
                SideMenu.adapter.notifyDataSetChanged();
            }else if(StoredObjects.UserType.equalsIgnoreCase("Sub Franchisee")){
                StoredObjects.listcount= 2;
                SideMenu.adapter.notifyDataSetChanged();
            }else{

            }
        }catch (Exception e){

        }


        initilization(v);
        pagecount = 1;


        if (InterNetChecker.isNetworkAvailable(getActivity())) {

            getDrSpecializationService(getActivity());
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
        return v;
    }

    private void SpeclizatnListPopup(final CustomEditText prfilenme, final Activity activity) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
        listPopupWindow.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, specialisation_names_list));
        listPopupWindow.setAnchorView(prfilenme);
        listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tl_doc_search_edtxt.setText("");
                if(position==0){
                    speacialisation_id="";
                }else{
                    speacialisation_id = specialization_list.get(position-1).get("specialization_id");
                }

                prfilenme.setText(specialisation_names_list.get(position));

                pagecount=1;
                serviceCalling();

                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);

        add_doctor_lay = v.findViewById( R.id. tl_add_doctor_lay);
        tl_doctors_recyler = v.findViewById( R.id.tl_doctors_recyler);
        tl_doc_search_edtxt = v.findViewById( R.id.tl_doc_search_edtxt);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);

        h_spclztn_edtx=v.findViewById(R.id.h_spclztn_edtx);
        title_txt.setText( "Doctors" );

        h_spclztn_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(specialisation_names_list.size()>0){
                    SpeclizatnListPopup((CustomEditText) h_spclztn_edtx,getActivity());
                }else{
                    StoredObjects.ToastMethod("No Data found",getActivity());
                }

            }
        });


        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        } );

        add_doctor_lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               fragmentcallinglay( new TL_AddDoctor() );
            }
        } );


        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        tl_doctors_recyler.setLayoutManager(linearLayoutManager);


        tl_doc_search_edtxt.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = tl_doc_search_edtxt.getText().length();

                filter_list.clear();

                for (int i = 0; i < data_list.size(); i++) {
                    if (textlength <= data_list.get(i).get("name").length()
                            ||textlength <= data_list.get(i).get("doctor_registration_number").length()
                            ||textlength <= data_list.get(i).get("state_board").length()
                            ||textlength <= data_list.get(i).get("address").length()
                            ||textlength <= data_list.get(i).get("phone").length()) {

                        if ((data_list.get(i).get("name").toLowerCase().trim().contains(  tl_doc_search_edtxt.getText().toString().toLowerCase().trim()))
                                ||(data_list.get(i).get("doctor_registration_number").toLowerCase().trim().contains(  tl_doc_search_edtxt.getText().toString().toLowerCase().trim()))
                                ||(data_list.get(i).get("state_board").toLowerCase().trim().contains(  tl_doc_search_edtxt.getText().toString().toLowerCase().trim()))

                                ||(data_list.get(i).get("address").toLowerCase().trim().contains(  tl_doc_search_edtxt.getText().toString().toLowerCase().trim()))
                                ||(data_list.get(i).get("phone").toLowerCase().trim().contains(  tl_doc_search_edtxt.getText().toString().toLowerCase().trim()))) {

                            filter_list.add(data_list.get(i));
                        }
                    }
                }



                adapter = new HashMapRecycleviewadapter(getActivity(), filter_list, "tl_doctors", tl_doctors_recyler, R.layout.tl_doctor_one_listitem);
                tl_doctors_recyler.setAdapter(adapter);

                if(filter_list.size()==0){
                    nodatavailable_txt.setVisibility(View.VISIBLE);
                    tl_doctors_recyler.setVisibility(View.GONE);

                }else{
                    nodatavailable_txt.setVisibility(View.GONE);
                    tl_doctors_recyler.setVisibility(View.VISIBLE);

                }



            }
        });

        tl_doctors_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
      //  tl_doctors_recyler.setAdapter(new HashMapRecycleviewadapter(getActivity(),StoredObjects.dummy_list,"tl_doctors",tl_doctors_recyler,R.layout.tl_doctor_one_listitem));


    }

    private void getDrSpecializationService(final Activity activity) {
        CustomProgressbar.Progressbarshow(activity);
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.getDrSpecialization(RetrofitInstance.dr_specializtion).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {

                        String responseRecieved = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseRecieved);
                        StoredObjects.LogMethod("response", "response::" + responseRecieved);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("200")) {
                            String results = jsonObject.getString("results");
                            specialization_list = JsonParsing.GetJsonData(results);
                            specialisation_names_list.clear();
                            specialisation_names_list.add("Specialization");
                            for (int k = 0; k < specialization_list.size(); k++) {
                                specialisation_names_list.add(specialization_list.get(k).get("name"));
                            }

                        } else {
                            StoredObjects.ToastMethod("No Data found", activity);
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

    String first_time="yes";
    private void serviceCalling() {
        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            getDoctorsService(getActivity(), pagecount, recordsperpage);
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }
    private void getDoctorsService(final Activity activity, final int pagecount, String recordsperpage) {
        if (pagecount == 1) {
            if(first_time.equalsIgnoreCase("Yes")){

            }else{
                CustomProgressbar.Progressbarshow(activity);
            }
        }
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.franchiseeDoctors(RetrofitInstance.doctors,  StoredObjects.UserId, StoredObjects.UserRoleId, "" + pagecount,recordsperpage,speacialisation_id).enqueue(new Callback<ResponseBody>() {
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

                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "tl_doctors", tl_doctors_recyler, R.layout.tl_doctor_one_listitem);
                                tl_doctors_recyler.setAdapter(adapter);

                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                tl_doctors_recyler.invalidate();
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
                if (pagecount == 1) {
                    CustomProgressbar.Progressbarcancel(activity);
                }
                if(first_time.equalsIgnoreCase("Yes")){

                    first_time="No";
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (pagecount == 1) {
                    CustomProgressbar.Progressbarcancel(activity);
                }

            }
        });

    }


    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

    }
    private void updatelay(ArrayList<HashMap<String, String>> data_list) {

        if (data_list.size() == 0) {
           // tl_doc_search_edtxt.setVisibility(View.GONE);
            nodatavailable_txt.setVisibility(View.VISIBLE);
            tl_doctors_recyler.setVisibility(View.GONE);
        } else {
            //tl_doc_search_edtxt.setVisibility(View.VISIBLE);
            nodatavailable_txt.setVisibility(View.GONE);
            tl_doctors_recyler.setVisibility(View.VISIBLE);
        }
    }

    public void fragmentcalling(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).commit ();

    }

}

