package com.rxmediaapp.fragments.patient;

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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.customfonts.CustomEditText;
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


public class Block_Doctor extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    public static RecyclerView blockdoctor_recycle;
    EditText search_doc_edtxt;
    Button search_doc_btn;
    public static TextView nodatavailable_txt;

    public static HashMapRecycleviewadapter adapter;
    ArrayList<HashMap<String, String>> availabledoctors = new ArrayList<>();
    ArrayList<String> availabledoctorslist = new ArrayList<>();
    ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
    int pagecount=1,totalpages=0;
    String recordsperpage="50";

    String speacialisation_id="" ,search_text="";

    ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();
    ArrayList<String> specialisation_names_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> specialization_list = new ArrayList<>();
    String first_time="yes";

    EditText h_spclztn_edtx;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.block_doctor,null,false );
        StoredObjects.page_type="block_doctor";

        SideMenu.updatemenu(StoredObjects.page_type);
        first_time="yes";
        try {
            StoredObjects.listcount= 5;
            SideMenu.adapter.notifyDataSetChanged();
        }catch (Exception e){

        }
        initilization(v);

        if (InterNetChecker.isNetworkAvailable(getActivity())) {

            getDrSpecializationService(getActivity());
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }


        return v;
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
                    pagecount=1;
                    serviceCalling();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                CustomProgressbar.Progressbarcancel(activity);


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
                search_doc_edtxt.setText("");
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
        blockdoctor_recycle = v.findViewById( R.id. blockdoctor_recycle);
        search_doc_edtxt= v.findViewById( R.id. search_doc_edtxt);
        search_doc_btn = v.findViewById( R.id. search_doc_btn);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        title_txt.setText( "Block Doctors" );
        h_spclztn_edtx=v.findViewById(R.id.h_spclztn_edtx);

        backbtn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentcallinglay( new P_Dashboard() );
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
        search_doc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InterNetChecker.isNetworkAvailable(getActivity())) {
                    getAvailableDoctors(getActivity());
                } else {
                    StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                }


            }
        });



        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        blockdoctor_recycle.setLayoutManager(linearLayoutManager);

        blockdoctor_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                    pagecount = pagecount + 1;
                    if (pagecount <= totalpages) {
                        serviceCalling();
                    }

                }
            }
        });

        search_doc_edtxt.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = search_doc_edtxt.getText().length();

                filter_list.clear();

                for (int i = 0; i < data_list.size(); i++) {
                    if (textlength <= data_list.get(i).get("name").length()||textlength <= data_list.get(i).get("phone").length()
                    ||textlength <= data_list.get(i).get("address").length()) {

                        if ((data_list.get(i).get("name").toLowerCase().trim().contains(  search_doc_edtxt.getText().toString().toLowerCase().trim()))
                                ||(data_list.get(i).get("phone").toLowerCase().trim().contains(  search_doc_edtxt.getText().toString().toLowerCase().trim()))
                         ||(data_list.get(i).get("address").toLowerCase().trim().contains(  search_doc_edtxt.getText().toString().toLowerCase().trim()))) {

                            filter_list.add(data_list.get(i));
                        }
                    }
                }


                adapter = new HashMapRecycleviewadapter(getActivity(), filter_list, "blockdoctor", blockdoctor_recycle, R.layout.block_doctor_listitems);
                blockdoctor_recycle.setAdapter(adapter);
                if(filter_list.size()==0){
                    nodatavailable_txt.setVisibility(View.VISIBLE);
                    blockdoctor_recycle.setVisibility(View.GONE);

                }else{
                    nodatavailable_txt.setVisibility(View.GONE);
                    blockdoctor_recycle.setVisibility(View.VISIBLE);

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

    private void getAvailableDoctors(final Activity activity) {
        CustomProgressbar.Progressbarshow(activity);
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.AvailableDoctorList(RetrofitInstance.available_doctors, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
                            availabledoctors = JsonParsing.GetJsonData(results);
                            availabledoctorslist.clear();
                            for (int k = 0; k < availabledoctors.size(); k++) {
                                availabledoctorslist.add(availabledoctors.get(k).get("name") + " (" + availabledoctors.get(k).get("specialization") + ")");
                            }

                        } else {
                            availabledoctors.clear();
                            availabledoctorslist.clear();

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                    if (availabledoctors.size() > 0) {
                        blockdoctorpopup(getActivity());
                    } else {
                        StoredObjects.ToastMethod("No Doctors Available", activity);
                    }
                }

                CustomProgressbar.Progressbarcancel(activity);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomProgressbar.Progressbarcancel(activity);

            }
        });

    }
    private void getAssistantService(final Activity activity, final int pagecount, String recordsperpage) {
        if(pagecount==1){

            if( blocked.equalsIgnoreCase("yes")||first_time.equalsIgnoreCase("Yes")){
                blocked="no";
            }else{
                CustomProgressbar.Progressbarshow(activity);
            }


        }

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.BlockDoctorList(RetrofitInstance.block_doctorlist, StoredObjects.UserId, StoredObjects.UserRoleId,pagecount+"",recordsperpage,speacialisation_id).enqueue(new Callback<ResponseBody>() {
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

                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "blockdoctor", blockdoctor_recycle, R.layout.block_doctor_listitems);
                                blockdoctor_recycle.setAdapter(adapter);

                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                blockdoctor_recycle.invalidate();
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

            }
        });


    }

    public static void updatelay(ArrayList<HashMap<String, String>> data_list) {

        if(data_list.size()==0){
            nodatavailable_txt.setVisibility(View.VISIBLE);
            blockdoctor_recycle.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            blockdoctor_recycle.setVisibility(View.VISIBLE);
        }
    }

    String doctor_id="";
    private void blockdoctorpopup(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.blockdoctor_popup);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);

        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
        CustomButton Block_button = (CustomButton) dialog.findViewById(R.id.Block_button);
        final CustomEditText select_doctor_edtx = (CustomEditText) dialog.findViewById(R.id.select_doctor_edtx);
        ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);

        select_doctor_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DoctorsListPopup (select_doctor_edtx,getActivity());
            }
        });


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


        Block_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String doc_str=select_doctor_edtx.getText().toString().trim();

                if (StoredObjects.inputValidation(select_doctor_edtx, getString(R.string.selectdoctor_validation), getActivity())) {

                    dialog.dismiss();
                    BlockDoctorService(getActivity(),doctor_id);

                }

            }
        });

        dialog.show();
    }

    String blocked="no";

    private void BlockDoctorService(final FragmentActivity activity, String doc_str) {
        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.BlockDoctor(RetrofitInstance.block_doctor,StoredObjects.UserId,StoredObjects.UserRoleId,doc_str).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseRecieved = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseRecieved);
                        StoredObjects.LogMethod("response", "response::" + responseRecieved);
                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("200")) {
                            StoredObjects.ToastMethod("Selected Doctor Blocked", activity);
                            // CustomProgressbar.Progressbarcancel(activity);
                            pagecount = 1;
                            blocked = "yes";
                            serviceCalling();

                        } else {
                            String error = jsonObject.getString("error");
                            StoredObjects.ToastMethod(error, activity);
                            CustomProgressbar.Progressbarcancel(activity);
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


    private void DoctorsListPopup(final CustomEditText prfilenme,Activity activity){
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
        listPopupWindow.setAdapter(new ArrayAdapter<>(activity,R.layout.drpdwn_lay,availabledoctorslist));
        listPopupWindow.setAnchorView(prfilenme);
        listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prfilenme.setText(availabledoctorslist.get(position));
                doctor_id=availabledoctors.get(position).get("doctor_id");
                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
    }

    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).commit ();

    }




}


