package com.rxmediaapp.fragments.hospital;

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
import com.rxmediaapp.fragments.dashboards.H_Dashboard;
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


public class H_Assistant extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    LinearLayout add_assistant_lay;
    public static TextView nodatavailable_txt;
    public static RecyclerView h_assistant_recyler;
    public static HashMapRecycleviewadapter adapter;

    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();

    EditText h_ass_search_edtxt;
    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
    int pagecount=1,totalpages=0;
    String recordsperpage="50";

    ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.h_assistant,null,false );
        StoredObjects.page_type="h_assistant";

        SideMenu.updatemenu(StoredObjects.page_type);
        try {
            StoredObjects.listcount= 2;
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
            getAssistantService(getActivity(),pagecount,recordsperpage);
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
        }
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        add_assistant_lay= v.findViewById( R.id. h_add_assistant_lay);
        h_assistant_recyler = v.findViewById( R.id. h_assistant_recyler);
        h_ass_search_edtxt = v.findViewById( R.id. h_ass_search_edtxt);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);

        title_txt.setText( "Assistant" );


        backbtn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentcalling(new H_Dashboard());
            }
        });
        add_assistant_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcallinglay(new H_AddAssistant());
            }
        });

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        h_assistant_recyler.setLayoutManager(linearLayoutManager);

        h_ass_search_edtxt.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = h_ass_search_edtxt.getText().length();

                filter_list.clear();

                for (int i = 0; i < data_list.size(); i++) {
                    if (textlength <= data_list.get(i).get("name").length()||textlength <= data_list.get(i).get("phone").length()) {

                        if ((data_list.get(i).get("name").toLowerCase().trim().contains(  h_ass_search_edtxt.getText().toString().toLowerCase().trim()))
                        ||(data_list.get(i).get("phone").toLowerCase().trim().contains(  h_ass_search_edtxt.getText().toString().toLowerCase().trim()))) {

                            filter_list.add(data_list.get(i));
                        }
                    }
                }

                adapter = new HashMapRecycleviewadapter(getActivity(), filter_list, "h_assistant", h_assistant_recyler, R.layout.h_assistant_listitem);
                h_assistant_recyler.setAdapter(adapter);
                if(filter_list.size()==0){
                    nodatavailable_txt.setVisibility(View.VISIBLE);
                    h_assistant_recyler.setVisibility(View.GONE);

                }else{
                    nodatavailable_txt.setVisibility(View.GONE);
                    h_assistant_recyler.setVisibility(View.VISIBLE);

                }



            }
        });


    }

    private void getAssistantService(final Activity activity,final int pagecount, String recordsperpage) {
        if(pagecount==1){
            CustomProgressbar.Progressbarshow(activity);
        }
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.gethospitalAssistants(RetrofitInstance.hospital_assistants, StoredObjects.UserId, StoredObjects.UserRoleId,""+pagecount,recordsperpage).enqueue(new Callback<ResponseBody>() {
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

                            data_list = JsonParsing.GetJsonData(results);
                            if (pagecount == 1) {
                                data_list.clear();
                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);

                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "h_assistant", h_assistant_recyler, R.layout.h_assistant_listitem);
                                h_assistant_recyler.setAdapter(adapter);

                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                h_assistant_recyler.invalidate();
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

    public static void updatelay(ArrayList<HashMap<String, String>> data_list) {

        if(data_list.size()==0){
            nodatavailable_txt.setVisibility(View.VISIBLE);
            h_assistant_recyler.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            h_assistant_recyler.setVisibility(View.VISIBLE);
        }
    }


    public void fragmentcallinglay(Fragment fragment) {
        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

    }


    public void fragmentcalling(Fragment fragment) {
        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).commit ();

    }


}


