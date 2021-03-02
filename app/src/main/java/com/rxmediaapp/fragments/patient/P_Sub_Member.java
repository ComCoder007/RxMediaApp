package com.rxmediaapp.fragments.patient;

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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.fragments.dashboards.Doc_Dashboard;
import com.rxmediaapp.fragments.dashboards.P_Dashboard;
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

public class P_Sub_Member extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    LinearLayout add_member_lay,new_actionbar_lay;

    
    public static RecyclerView submember_recyler;
    public static HashMapRecycleviewadapter adapter;
    public static TextView nodatavailable_txt;

    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();

    int pagecount = 1, totalpages = 0;
    String recordsperpage = "50";

    EditText ps_search_patient_edtxt;
    ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.p_sub_member,null,false );
        StoredObjects.page_type="submembers";

        SideMenu.updatemenu(StoredObjects.page_type);
        try {
            StoredObjects.listcount= 1;
            SideMenu.adapter.notifyDataSetChanged();
        }catch (Exception e){

        }
        initilization(v);
        pagecount=1;
        servicecalling();
        return v;
    }

    private void servicecalling() {

        if (InterNetChecker.isNetworkAvailable(getContext())) {
            getAssistantService(getActivity(),pagecount,recordsperpage);
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
        }
    }
    public void fragmentcallinglay1(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace(R.id.frame_container, fragment).commit();

    }
    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        add_member_lay = v.findViewById( R.id. add_member_lay);
        submember_recyler = v.findViewById( R.id.submember_recyler);
        new_actionbar_lay=v.findViewById(R.id.new_actionbar_lay);
        new_actionbar_lay.setVisibility(View.VISIBLE);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        ps_search_patient_edtxt=v.findViewById(R.id.ps_search_patient_edtxt);

        title_txt.setText( "Sub Members" );

        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentcallinglay1(new P_Dashboard());

            }
        } );

        ps_search_patient_edtxt.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = ps_search_patient_edtxt.getText().length();

                filter_list.clear();

                for (int i = 0; i < data_list.size(); i++) {
                    if (textlength <= data_list.get(i).get("name").length()
                            ||textlength <= data_list.get(i).get("aadhar_number").length()
                            ||textlength <= data_list.get(i).get("email").length()
                            ||textlength <= data_list.get(i).get("phone").length()) {

                        if ((data_list.get(i).get("name").toLowerCase().trim().contains(  ps_search_patient_edtxt.getText().toString().toLowerCase().trim()))
                                ||(data_list.get(i).get("aadhar_number").toLowerCase().trim().contains(  ps_search_patient_edtxt.getText().toString().toLowerCase().trim()))
                                ||(data_list.get(i).get("phone").toLowerCase().trim().contains(  ps_search_patient_edtxt.getText().toString().toLowerCase().trim()))) {

                            filter_list.add(data_list.get(i));
                        }
                    }
                }


                adapter = new HashMapRecycleviewadapter(getActivity(), filter_list, "p_submember", submember_recyler, R.layout.p_submember_listitem);
                submember_recyler.setAdapter(adapter);

                if(filter_list.size()==0){
                    nodatavailable_txt.setVisibility(View.VISIBLE);
                    submember_recyler.setVisibility(View.GONE);

                }else{
                    nodatavailable_txt.setVisibility(View.GONE);
                    submember_recyler.setVisibility(View.VISIBLE);

                }



            }
        });


        add_member_lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcallinglay( new P_Add_Member() );
            }
        } );

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        submember_recyler.setLayoutManager(linearLayoutManager);

        submember_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                    pagecount = pagecount + 1;
                    if (pagecount <= totalpages) {
                        servicecalling();
                    }

                }
            }
        });

       // submember_recyler.setAdapter(new HashMapRecycleviewadapter(getActivity(),StoredObjects.dummy_list,"p_submember",submember_recyler,R.layout.p_submember_listitem));


    }


    private void getAssistantService(final Activity activity,final int pagecount,String recordsperpage) {
        CustomProgressbar.Progressbarshow(activity);
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.getmembers(RetrofitInstance.members, StoredObjects.UserId, StoredObjects.UserRoleId,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
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

                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "p_submember", submember_recyler, R.layout.p_submember_listitem);
                                submember_recyler.setAdapter(adapter);

                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                submember_recyler.invalidate();
                            }
                            updatelay(data_list);


                        } else {
                            if (pagecount == 1) {
                                updatelay(data_list);
                            }

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    if (pagecount == 1) {
                        CustomProgressbar.Progressbarcancel(activity);
                    }

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
    public static void updatelay(ArrayList<HashMap<String, String>> data_list) {

        if(data_list.size()==0){
            nodatavailable_txt.setVisibility(View.VISIBLE);
            submember_recyler.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            submember_recyler.setVisibility(View.VISIBLE);
        }
    }

    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

    }




}

