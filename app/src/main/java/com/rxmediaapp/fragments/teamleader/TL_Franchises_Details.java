package com.rxmediaapp.fragments.teamleader;

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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
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

public class TL_Franchises_Details extends Fragment {

    ImageView backbtn_img;
    TextView title_txt,tlf_address_txt,tlf_number_txt,tlf_name_txt;

    RecyclerView franchise_recyler,franchise_one_recyler;
    public static TextView nodatavailable_txt;

    public static ArrayList<HashMap<String, String>> gridtabs_list = new ArrayList<>();

    String h_count="0",p_count="0",d_count="0",sf_count="0";

    String[] tabslist = {"Hospitals","Doctors","Patients","Sub Franchises"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.tl_franchises_details_two,null,false );
        StoredObjects.page_type="franchises_details_two";

        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        tlf_address_txt = v.findViewById( R.id. tlf_address_txt);
        tlf_number_txt = v.findViewById( R.id. tlf_number_txt);
        tlf_name_txt = v.findViewById( R.id. tlf_name_txt);

        franchise_recyler = v.findViewById( R.id. tl_franchise_recyler);
        franchise_one_recyler= v.findViewById( R.id. tl_franchise_one_recyler);
        nodatavailable_txt = v.findViewById(R.id.nodatavailable_txt);
        title_txt.setText("Franchises List");
        try {
            tlf_name_txt.setText( StoredObjects.franshise_list.get(0).get("name"));
            tlf_number_txt.setText( StoredObjects.franshise_list.get(0).get("phone"));
            tlf_address_txt.setText( StoredObjects.franshise_list.get(0).get("email"));
        }catch (Exception e){

        }

        backbtn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        });

        if(gridtabs_list.size()>0){
            franchise_recyler.setAdapter(new HashMapRecycleviewadapter(getActivity(),gridtabs_list,"tl_franchise",franchise_one_recyler,R.layout.tl_franchises_details_listitem));

        }else{
            H_serviceCalling();
        }

        //StoredObjects.hashmaplist(4);

        StoredObjects.getrray(tabslist);


        //customRecyclerview.Assigndatatorecyleviewhashmap( franchise_recyler, StoredObjects.dummy_list,"franchise", StoredObjects.Gridview, 2, StoredObjects.ver_orientation, R.layout.franchises_details_listitem);
        final GridLayoutManager linearLayoutManager=new GridLayoutManager(getActivity(),2);
        franchise_recyler.setLayoutManager(linearLayoutManager);

        final LinearLayoutManager linearLayoutManagerone=new LinearLayoutManager(getActivity());
        franchise_one_recyler.setLayoutManager(linearLayoutManagerone);

        franchise_one_recyler.setAdapter(new HashMapRecycleviewadapter(getActivity(),StoredObjects.menuitems_list,"tl_franchise_one",franchise_one_recyler,R.layout.tl_franchises_detailsone_listitem));


    }

    public void fragmentcallinglay(Fragment fragment) {
        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();
    }

    private void H_serviceCalling() {
        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            getHospitalService(getActivity(),1,"10");
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }

    private void getHospitalService(final FragmentActivity activity, final int pagecount, String recordsperpage) {
        CustomProgressbar.Progressbarshow(activity);
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.franchiseeHospitals(RetrofitInstance.hospitals, StoredObjects.F_UserId, StoredObjects.F_RoleUserId, "" + pagecount, recordsperpage).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseReceived = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseReceived);
                        StoredObjects.LogMethod("response", "response::" + responseReceived);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("200")) {
                            String total_pages = jsonObject.getString("total_records");
                            h_count = total_pages;

                        } else {
                            h_count = "0";
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                        getDoctorsService(getActivity(), 1, "10");
                    } else {
                        StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomProgressbar.Progressbarcancel(activity);

            }
        });

    }
    private void getDoctorsService(final Activity activity, final int pagecount, String recordsperpage) {

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.franchiseeDoctors(RetrofitInstance.doctors,  StoredObjects.F_UserId, StoredObjects.F_RoleUserId, "" +pagecount,recordsperpage).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseReceived = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseReceived);
                        StoredObjects.LogMethod("response", "response::" + responseReceived);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("200")) {
                            d_count = jsonObject.getString("total_records");

                        } else {
                            d_count = "0";

                        }

                        if (InterNetChecker.isNetworkAvailable(getActivity())) {
                            getPatientService(getActivity(), 1, "10");
                        } else {
                            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomProgressbar.Progressbarcancel(activity);

            }
        });

    }


    private void getPatientService(final Activity activity, final int pagecount, String recordsperpage) {

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.franchiseeDoctors(RetrofitInstance.patients,  StoredObjects.F_UserId, StoredObjects.F_RoleUserId, "" +pagecount,recordsperpage).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseReceived = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseReceived);
                        StoredObjects.LogMethod("response", "response::" + responseReceived);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("200")) {
                            p_count = jsonObject.getString("total_records");

                        } else {
                            p_count = "0";

                        }

                        if (InterNetChecker.isNetworkAvailable(getActivity())) {
                            getSubFranchiseeService(getActivity(), 1, "10");
                        } else {
                            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomProgressbar.Progressbarcancel(activity);

            }
        });

    }

    private void getSubFranchiseeService(final Activity activity, final int pagecount, String recordsperpage) {

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.franchiseeDoctors(RetrofitInstance.sub_franchisee,  StoredObjects.F_UserId, StoredObjects.F_RoleUserId, "" +pagecount,recordsperpage).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseReceived = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseReceived);
                        StoredObjects.LogMethod("response", "response::" + responseReceived);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("200")) {
                            sf_count = jsonObject.getString("total_records");

                        } else {
                            sf_count = "0";

                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    CustomProgressbar.Progressbarcancel(activity);
                    gridtabs_list.clear();
                    for (int k = 0; k < tabslist.length; k++) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("name", tabslist[k]);
                        if (k == 0) {
                            hashMap.put("tabcount", h_count);
                        } else if (k == 1) {
                            hashMap.put("tabcount", d_count);
                        } else if (k == 2) {
                            hashMap.put("tabcount", p_count);
                        } else {
                            hashMap.put("tabcount", sf_count);
                        }

                        gridtabs_list.add(hashMap);
                    }
                    franchise_recyler.setAdapter(new HashMapRecycleviewadapter(getActivity(), gridtabs_list, "tl_franchise", franchise_one_recyler, R.layout.tl_franchises_details_listitem));

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomProgressbar.Progressbarcancel(activity);

            }
        });

    }

}

