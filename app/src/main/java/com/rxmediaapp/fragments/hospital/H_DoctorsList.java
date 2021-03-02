package com.rxmediaapp.fragments.hospital;

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
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customfonts.CustomEditText;
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

public class H_DoctorsList extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    public static RecyclerView  h_doclist;
    EditText h_searchdoc_edtxt,h_spclztn_edtx;
    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();

    public static ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();

    public static HashMapRecycleviewadapter adapter;
    LinearLayout adddoctor_lay,new_actionbar_lay;
    public static TextView nodatavailable_txt;
    String speacialisation_id="" ,search_text="";

    ArrayList<String> specialisation_names_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> specialization_list = new ArrayList<>();

    Button h_submit_btn,h_cancel_btn;

    int pagecount=1,totalpages=0;
    String recordsperpage="10";





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.h_doctorslist,null,false );
        StoredObjects.page_type="h_doctors";
        SideMenu.updatemenu(StoredObjects.page_type);
        first_time="yes";
        try {
            StoredObjects.listcount= 1;
            SideMenu.adapter.notifyDataSetChanged();
        }catch (Exception e){

        }
        initilization(v);
        pagecount=1;

        if (InterNetChecker.isNetworkAvailable(getActivity())) {

            getDrSpecializationService(getActivity());
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }

        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        h_doclist = v.findViewById( R.id.h_doclist);
        adddoctor_lay=v.findViewById(R.id.adddoctor_lay);
        h_searchdoc_edtxt=v.findViewById(R.id.h_searchdoc_edtxt);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        h_spclztn_edtx = v.findViewById(R.id.h_spclztn_edtx);

        new_actionbar_lay=v.findViewById(R.id.new_actionbar_lay);
        new_actionbar_lay.setVisibility(View.VISIBLE);

        h_submit_btn=v.findViewById(R.id.h_submit_btn);
        h_cancel_btn=v.findViewById(R.id.h_cancel_btn);

        title_txt.setText( "Doctors" );


        backbtn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentcallinglay1(new H_Dashboard());
            }
        });

        adddoctor_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcallinglay(new H_AddDoctor());

            }
        });

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
        h_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_text=h_searchdoc_edtxt.getText().toString().trim();

                if(search_text.length()>0||speacialisation_id.length()>0){

                    pagecount=1;
                    serviceCalling();
                }else{
                    StoredObjects.ToastMethod("Please select Filter options",getActivity());
                }

            }
        });
        h_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                search_text="";
                speacialisation_id="";
                h_searchdoc_edtxt.setText("");
                h_spclztn_edtx.setText("");
                pagecount=1;
                serviceCalling();

            }
        });

       /* h_searchdoc_edtxt.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = h_searchdoc_edtxt.getText().length();

                filter_list.clear();

                for (int i = 0; i < data_list.size(); i++) {
                    if (textlength <= data_list.get(i).get("name").length()) {

                        if ((data_list.get(i).get("name").toLowerCase().trim().contains(  h_searchdoc_edtxt.getText().toString().toLowerCase().trim()))) {

                            filter_list.add(data_list.get(i));
                        }
                    }
                }

                adapter=new HashMapRecycleviewadapter(getActivity(),filter_list,"h_docsinnerlist",h_doclist,R.layout.h_docsub_listitem);
                h_doclist.setAdapter(adapter);
                if(filter_list.size()==0){
                    nodatavailable_txt.setVisibility(View.VISIBLE);
                    h_doclist.setVisibility(View.GONE);

                }else{
                    nodatavailable_txt.setVisibility(View.GONE);
                    h_doclist.setVisibility(View.VISIBLE);

                }



            }
        });*/

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        h_doclist.setLayoutManager(linearLayoutManager);

        h_doclist.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            getAssistantService(getActivity(),pagecount,recordsperpage);

        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }

    private void getAssistantService(final Activity activity, final int pagecount, String recordsperpage) {
        if(pagecount==1){
            if(first_time.equalsIgnoreCase("Yes")){

            }else{
                CustomProgressbar.Progressbarshow(activity);
            }

        }

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.doctorslist(RetrofitInstance.hospital_doctors_list, StoredObjects.UserId, StoredObjects.UserRoleId,speacialisation_id,pagecount+"",recordsperpage,search_text).enqueue(new Callback<ResponseBody>() {
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

                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "h_docsinnerlist", h_doclist, R.layout.h_docsub_listitem);
                                h_doclist.setAdapter(adapter);

                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                h_doclist.invalidate();
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
                if(first_time.equalsIgnoreCase("Yes")){

                    first_time="No";
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(pagecount==1){
                    CustomProgressbar.Progressbarcancel(activity);
                }
                if(first_time.equalsIgnoreCase("Yes")){

                    first_time="No";
                }

            }
        });

    }


    private void SpeclizatnListPopup(final CustomEditText prfilenme, final Activity activity) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
        listPopupWindow.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, specialisation_names_list));
        listPopupWindow.setAnchorView(prfilenme);
        listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    speacialisation_id="";
                }else{
                    speacialisation_id = specialization_list.get(position-1).get("specialization_id");
                }

                prfilenme.setText(specialisation_names_list.get(position));
                h_searchdoc_edtxt.setText("");
                pagecount=1;
                if (InterNetChecker.isNetworkAvailable(activity)) {
                    getAssistantService(activity,pagecount,recordsperpage);

                } else {
                    StoredObjects.ToastMethod(getString(R.string.nointernet),activity);
                }

                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
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

    private void updatelay(ArrayList<HashMap<String, String>> data_list) {

        if(data_list.size()==0){
            nodatavailable_txt.setVisibility(View.VISIBLE);
            h_doclist.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            h_doclist.setVisibility(View.VISIBLE);
        }
    }

    public void fragmentcallinglay(Fragment fragment) {
        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

    }

    public void fragmentcallinglay1(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace(R.id.frame_container, fragment).commit();

    }


}



