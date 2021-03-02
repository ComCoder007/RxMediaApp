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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Franchisee_SubFranchises extends Fragment {

    ImageView backbtn_img;
    TextView title_txt,f_patient_name_txt,f_number_txt,f_address_txt;
    EditText f_searcsub_edtxt;
    public static TextView nodatavailable_txt;
    public static  RecyclerView f_patient_three_recyler;

    int pagecount = 1, totalpages = 0;
    String recordsperpage = "10";
    public static HashMapRecycleviewadapter adapter;
    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();

    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.f_patientlist_three,null,false );
        StoredObjects.page_type="f_patientlist_three";

        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        pagecount=1;
        serviceCalling();
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);

        f_patient_name_txt = v.findViewById( R.id. f_patient_name_txt);
        f_number_txt= v.findViewById( R.id. f_number_txt);
        f_address_txt = v.findViewById( R.id. f_address_txt);
        f_searcsub_edtxt= v.findViewById( R.id. f_searcsub_edtxt);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        f_patient_three_recyler = v.findViewById( R.id.f_patient_three_recyler);
        title_txt.setText( "Sub Franchises List" );



        f_searcsub_edtxt.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = f_searcsub_edtxt.getText().length();

                filter_list.clear();

                for (int i = 0; i < data_list.size(); i++) {
                    if (textlength <= data_list.get(i).get("name").length()
                            ||textlength <= data_list.get(i).get("aadhar_number").length()
                            ||textlength <= data_list.get(i).get("email").length()
                            ||textlength <= data_list.get(i).get("phone").length()) {

                        if ((data_list.get(i).get("name").toLowerCase().trim().contains(  f_searcsub_edtxt.getText().toString().toLowerCase().trim()))
                                ||(data_list.get(i).get("aadhar_number").toLowerCase().trim().contains(  f_searcsub_edtxt.getText().toString().toLowerCase().trim()))
                                ||(data_list.get(i).get("email").toLowerCase().trim().contains(  f_searcsub_edtxt.getText().toString().toLowerCase().trim()))
                                ||(data_list.get(i).get("phone").toLowerCase().trim().contains(  f_searcsub_edtxt.getText().toString().toLowerCase().trim()))) {

                            filter_list.add(data_list.get(i));
                        }
                    }
                }

                adapter = new HashMapRecycleviewadapter(getActivity(),filter_list,"f_patient_three",f_patient_three_recyler,R.layout.f_patientlist_three_listitem);
                f_patient_three_recyler.setAdapter(adapter);

                if(filter_list.size()==0){
                    nodatavailable_txt.setVisibility(View.VISIBLE);
                    f_patient_three_recyler.setVisibility(View.GONE);

                }else{
                    nodatavailable_txt.setVisibility(View.GONE);
                    f_patient_three_recyler.setVisibility(View.VISIBLE);

                }



            }
        });



        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        } );



        try {
            f_patient_name_txt.setText( StoredObjects.franshise_list.get(0).get("name"));
            f_number_txt.setText( StoredObjects.franshise_list.get(0).get("phone"));
            f_address_txt.setText( StoredObjects.franshise_list.get(0).get("email"));
        }catch (Exception e){

        }

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        f_patient_three_recyler.setLayoutManager(linearLayoutManager);
        //  updatelay(data_list); add else block

        f_patient_three_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


    }


    private void updatelay(ArrayList<HashMap<String, String>> data_list) {

        if(data_list.size()==0){
            nodatavailable_txt.setVisibility(View.VISIBLE);
            f_patient_three_recyler.setVisibility(View.GONE);
            f_searcsub_edtxt.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            f_patient_three_recyler.setVisibility(View.VISIBLE);
            f_searcsub_edtxt.setVisibility(View.VISIBLE);
        }
    }
    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

    }

    private void serviceCalling() {
        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            getPatientsService(getActivity(),pagecount,recordsperpage);
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }

    private void getPatientsService(final Activity activity, final int pagecount, String recordsperpage) {
        if (pagecount == 1) {
            CustomProgressbar.Progressbarshow(activity);
        }
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.franchiseePatients(RetrofitInstance.sub_franchisee,  StoredObjects.F_UserId, StoredObjects.F_RoleUserId, "" + pagecount, recordsperpage).enqueue(new Callback<ResponseBody>() {
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
                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "f_patient_three", f_patient_three_recyler, R.layout.f_patientlist_three_listitem);
                                f_patient_three_recyler.setAdapter(adapter);

                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                f_patient_three_recyler.invalidate();
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
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (pagecount == 1) {
                    CustomProgressbar.Progressbarcancel(activity);
                }

            }
        });

    }


}


