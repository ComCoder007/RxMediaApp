package com.rxmediaapp.fragments.doctor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.activities.DoctorSignup;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.fragments.assistant.AssistantProfile;
import com.rxmediaapp.fragments.dashboards.Doc_Dashboard;
import com.rxmediaapp.fragments.hospital.H_PatentList;
import com.rxmediaapp.fragments.teamleader.TL_AddDoctor;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.CameraUtils;
import com.rxmediaapp.storedobjects.StoredObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class Doc_Profile extends Fragment {

  ImageView backbtn_img, dr_prof_img,edit_image;
  TextView title_txt, dr_name_text;
  CustomButton d_prf_sbmt_btn;
  CustomEditText d_prf_email_edtx, d_prf_rgstno_edtx, d_prf_spclztn_edtx, d_prf_mbile_edtx, d_prf_adres_edtx,d_prf_name_edtx,d_prf_year_of_registration_edtx,d_prf_state_bard_edtx;
  static RecyclerView d_prf_recyler;

  String speacialisation_id="";
  public static TextView nodatavailable_txt;
  public static HashMapRecycleviewadapter adapter;
  public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();

  ArrayList<String> specialisation_names_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> specialization_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> hospitals_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> d_hospitals_list = new ArrayList<>();
    String image_type = "",file_name_str="";



    EditText d_qualification_edtx,d_otherqualification_edtx,d_extraqualification_edtx;


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.doc_profile, null, false);
    StoredObjects.page_type = "doc_prfle";

    SideMenu.updatemenu(StoredObjects.page_type);
      try {
          StoredObjects.listcount= 9;
          SideMenu.adapter.notifyDataSetChanged();
      }catch (Exception e){

      }
    initilization(v);

    if (InterNetChecker.isNetworkAvailable(getActivity())) {
      getProfileService(getActivity());
    } else {
      StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
    }

    return v;
  }


  private void initilization(View v) {
    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);
    d_prf_recyler = v.findViewById(R.id.d_prf_recyler);
    dr_name_text = v.findViewById(R.id.dr_name_text);
    d_prf_sbmt_btn = v.findViewById(R.id.d_prf_sbmt_btn);
    d_prf_email_edtx = v.findViewById(R.id.d_prf_email_edtx);
    d_prf_rgstno_edtx = v.findViewById(R.id.d_prf_rgstno_edtx);
    d_prf_spclztn_edtx = v.findViewById(R.id.d_prf_spclztn_edtx);
    d_prf_mbile_edtx = v.findViewById(R.id.d_prf_mbile_edtx);
    d_prf_adres_edtx = v.findViewById(R.id.d_prf_adres_edtx);
    d_prf_name_edtx = v.findViewById(R.id.d_prf_name_edtx);
    dr_prof_img = v.findViewById(R.id.dr_prof_img);
    d_prf_state_bard_edtx = v.findViewById(R.id.d_prf_state_bard_edtx);
    d_prf_year_of_registration_edtx = v.findViewById(R.id.d_prf_year_of_registration_edtx);
    nodatavailable_txt = v.findViewById(R.id.nodatavailable_txt);
      edit_image = v.findViewById(R.id.edit_image);
      d_qualification_edtx = v.findViewById(R.id.d_qualification_edtx);
      d_otherqualification_edtx = v.findViewById(R.id.d_otherqualification_edtx);
      d_extraqualification_edtx = v.findViewById(R.id.d_extraqualification_edtx);




      d_prf_adres_edtx.setImeOptions(EditorInfo.IME_ACTION_DONE);
      d_prf_adres_edtx.setRawInputType(InputType.TYPE_CLASS_TEXT);

    title_txt.setText("Profile");
    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        fragmentcallinglay(new Doc_Dashboard());
      }
    });

    d_prf_spclztn_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

          if(specialisation_names_list.size()>0){
              SpeclizatnListPopup((CustomEditText) d_prf_spclztn_edtx,getActivity());
          }else{
              StoredObjects.ToastMethod("No Data found",getActivity());
          }
      }
    });

      d_prf_mbile_edtx.setEnabled(false);

    d_prf_sbmt_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String name_str = d_prf_name_edtx.getText().toString().trim();
        String email_str = d_prf_email_edtx.getText().toString().trim();
        String mobile_str = d_prf_mbile_edtx.getText().toString().trim();
        String register_no = d_prf_rgstno_edtx.getText().toString().trim();
        String address_Str = d_prf_adres_edtx.getText().toString().trim();
        String d_prf_state_bard_str = d_prf_state_bard_edtx.getText().toString().trim();
        String d_prf_year_of_registration_str = d_prf_year_of_registration_edtx.getText().toString().trim();

          String qualification = d_qualification_edtx.getText().toString().trim();
          String o_qualification = d_otherqualification_edtx.getText().toString().trim();
          String e_qualification = d_extraqualification_edtx.getText().toString().trim();


           String availabletimes="";
           int count=0;
         // "id":"1","from_days":"1","to_days":"2","custom_timings":"test","from_time":"10:30:00""to_time":"14:30:00"}]

          JSONArray Photosarray = new JSONArray();
          JSONObject jsonObject = null;
          for (int i= 0;i<hospitals_list.size();i++) {
              try {
                  if(hospitals_list.get(i).get("to_days").equalsIgnoreCase("0")||
                          hospitals_list.get(i).get("to_time").equalsIgnoreCase("0")){

                      count=count+1;

                  }else{
                      jsonObject = new JSONObject();
                      jsonObject.put("id", hospitals_list.get(i).get("id"));
                      jsonObject.put("from_days",  hospitals_list.get(i).get("from_days_pos"));
                      jsonObject.put("to_days",  hospitals_list.get(i).get("to_days_pos"));
                      jsonObject.put("custom_timings",  hospitals_list.get(i).get("custom_timings"));
                      jsonObject.put("from_time",  hospitals_list.get(i).get("from_time"));
                      jsonObject.put("to_time",  hospitals_list.get(i).get("to_time"));

                      jsonObject.put("from_time1",  hospitals_list.get(i).get("from_time1"));
                      jsonObject.put("to_time1",  hospitals_list.get(i).get("to_time1"));

                      jsonObject.put("from_time2",  hospitals_list.get(i).get("from_time2"));
                      jsonObject.put("to_time2",  hospitals_list.get(i).get("to_time2"));

                      jsonObject.put("from_time3",  hospitals_list.get(i).get("from_time3"));
                      jsonObject.put("to_time3",  hospitals_list.get(i).get("to_time3"));
                      Photosarray.put(jsonObject);
                  }

              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }
          availabletimes =Photosarray.toString();


          if (StoredObjects.inputValidation(d_prf_name_edtx, getString(R.string.enter_dr_name), getActivity())) {
              if (StoredObjects.Emailvalidation(email_str,getString(R.string.enter_valid_email), getActivity())) {

                  if (StoredObjects.inputValidation(d_prf_rgstno_edtx, getString(R.string.reg_validation), getActivity())) {
                      if (StoredObjects.inputValidation(d_prf_spclztn_edtx, getString(R.string.specizlization_validation), getActivity())) {
                          if (StoredObjects.inputValidation(d_qualification_edtx, "Please enter Qualification", getActivity())) {
                              if (StoredObjects.inputValidation(d_prf_year_of_registration_edtx, getString(R.string.year_reg_validation), getActivity())) {
                                  if(StoredObjects.inputValidation(d_prf_state_bard_edtx,getString(R.string.state_board_validation),getActivity())){

                                      if(count==0){

                                          if(availabletimes.equalsIgnoreCase("[]")){
                                              StoredObjects.ToastMethod("Please select Available Timings", getActivity());
                                          }else{
                                              try {
                                                  SideMenu.header_name.setText(name_str);
                                              }catch (Exception e){

                                              }

                                              editDrProfileService(getActivity(),name_str,email_str,mobile_str,register_no,address_Str,d_prf_state_bard_str,d_prf_year_of_registration_str,availabletimes,qualification,e_qualification,o_qualification);

                                          }

                                      }else{
                                          StoredObjects.ToastMethod("Please select Available Timings", getActivity());
                                      }

                                  }

                              }
                          }


                      }

                  }

              }


          }



      }
    });


      dr_prof_img.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (CameraUtils.checkAndRequestPermissions(getActivity()) == true) {
                  Imagepickingpopup(getActivity(), "doctor profile");
              }
          }
      });

      edit_image.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (CameraUtils.checkAndRequestPermissions(getActivity()) == true) {
                  Imagepickingpopup(getActivity(), "doctor profile");
              }
          }
      });


      final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    d_prf_recyler.setLayoutManager(linearLayoutManager);







  }



  private void getProfileService(final Activity activity) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getDrProfile(RetrofitInstance.doctor_profile, StoredObjects.Logged_DoctorId, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
          if(response.body() != null) {
              try {
                  String responseReceived = response.body().string();
                  JSONObject jsonObject = new JSONObject(responseReceived);
                  String status = jsonObject.getString("status");
                  if (status.equalsIgnoreCase("200")) {
                      String results = jsonObject.getString("results");
                      data_list = JsonParsing.GetJsonData(results);
                      d_prf_email_edtx.setText(data_list.get(0).get("email"));
                      d_prf_rgstno_edtx.setText(data_list.get(0).get("doctor_registration_number"));
                      d_prf_mbile_edtx.setText(data_list.get(0).get("phone"));
                      d_prf_adres_edtx.setText(data_list.get(0).get("address"));
                      d_prf_name_edtx.setText(data_list.get(0).get("name"));
                      dr_name_text.setText(data_list.get(0).get("name"));
                      d_qualification_edtx.setText(data_list.get(0).get("qualification"));
                      d_otherqualification_edtx.setText(data_list.get(0).get("other_qualification"));
                      d_extraqualification_edtx.setText(data_list.get(0).get("extra_qualification"));


                      d_prf_year_of_registration_edtx.setText(data_list.get(0).get("year_of_registration"));
                      file_name_str=  data_list.get(0).get("image");
                      speacialisation_id=data_list.get(0).get("specialization_id");

                      d_prf_state_bard_edtx.setText(data_list.get(0).get("state_board"));

                      //hospitals_list = JsonParsing.GetJsonData(data_list.get(0).get("attached_hospital"));

                      d_hospitals_list.clear();
                      d_hospitals_list = JsonParsing.GetJsonData(data_list.get(0).get("attached_hospital"));

                      try {
                          Glide.with(activity)
                                  .load(Uri.parse(RetrofitInstance.BASEURL + data_list.get(0).get("image")))
                                  .apply(new RequestOptions()
                                          .placeholder(R.drawable.no_image)
                                          .fitCenter()
                                          .centerCrop())
                                  .into(dr_prof_img);
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                  }
              } catch (IOException | JSONException e) {
                  e.printStackTrace();
              }

              if (InterNetChecker.isNetworkAvailable(getActivity())) {
                  get_weekdays(getActivity());

              } else {
                  StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.nointernet), getActivity());
              }
          }

        //CustomProgressbar.Progressbarcancel(activity);
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        CustomProgressbar.Progressbarcancel(activity);

      }
    });

  }
    public void get_weekdays(final Activity activity) {

        //CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.getweekdays(RetrofitInstance.week_days).enqueue(new Callback<ResponseBody>() {
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

                            TL_AddDoctor.days_list.clear();
                            TL_AddDoctor.days_list = JsonParsing.GetJsonData(results);
                            TL_AddDoctor.daynames_list.clear();

                            for (int k = 0; k < TL_AddDoctor.days_list.size(); k++) {
                                TL_AddDoctor.daynames_list.add(TL_AddDoctor.days_list.get(k).get("day_name"));

                            }


                        } else {
                            StoredObjects.ToastMethod("No Data found", activity);
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                        getTimeSlots(getActivity());
                    } else {
                        StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.nointernet), getActivity());
                    }
                }


                //CustomProgressbar.Progressbarcancel(activity);




            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomProgressbar.Progressbarcancel(activity);


            }
        });
    }

    private void getTimeSlots(final Activity activity) {

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.getTimeSlots(RetrofitInstance.time_slots).enqueue(new Callback<ResponseBody>() {
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
                            TL_AddDoctor.dummy_times_list.clear();
                            TL_AddDoctor.dummy_times_list = JsonParsing.GetJsonData(results);
                            TL_AddDoctor. dummy_timeSlot_list.clear();
                            for (int k = 0; k < TL_AddDoctor.dummy_times_list.size(); k++) {
                                TL_AddDoctor.dummy_timeSlot_list.add(StoredObjects.time12hrsformat(TL_AddDoctor.dummy_times_list.get(k).get("time_slot")));
                            }

                        } else {
                            StoredObjects.ToastMethod("No Data found", activity);
                        }
                        for(int k=0;k<d_hospitals_list.size();k++){
                            HashMap<String, String> hashMap = new HashMap<>();

                            hashMap.put("hospital_id",  d_hospitals_list.get(k).get("hospital_id"));
                            hashMap.put("custom_timings",  d_hospitals_list.get(k).get("custom_timings"));
                            hashMap.put("clinic_name",  d_hospitals_list.get(k).get("clinic_name"));

                            hashMap.put("id", d_hospitals_list.get(k).get("id"));
                            hashMap.put("is_viewed", "No");

                            hashMap.put("from_time",  d_hospitals_list.get(k).get("from_time"));
                            hashMap.put("to_time",  d_hospitals_list.get(k).get("to_time"));
                            hashMap.put("from_time1",  d_hospitals_list.get(k).get("from_time1"));
                            hashMap.put("to_time1",  d_hospitals_list.get(k).get("to_time1"));
                            hashMap.put("from_time2",  d_hospitals_list.get(k).get("from_time2"));
                            hashMap.put("to_time2",  d_hospitals_list.get(k).get("to_time2"));
                            hashMap.put("from_time3",  d_hospitals_list.get(k).get("from_time3"));
                            hashMap.put("to_time3",  d_hospitals_list.get(k).get("to_time3"));

                            int val1=-1,val2=-1;
                            int f_val1=-1,t_val1=-1;
                            int f_val2=-1,t_val2=-1;
                            int f_val3=-1,t_val3=-1;
                            int f_val4=-1,t_val4=-1;

                            for (int m = 0; m < TL_AddDoctor.dummy_times_list.size(); m++) {

                                if(d_hospitals_list.get(k).get("from_time").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                                    f_val1=m;
                                }
                                if(d_hospitals_list.get(k).get("to_time").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                                    t_val1=m;
                                }
                                if(d_hospitals_list.get(k).get("from_time1").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                                    f_val2=m;
                                }
                                if(d_hospitals_list.get(k).get("to_time1").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                                    t_val2=m;
                                }

                                if(d_hospitals_list.get(k).get("from_time2").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                                    f_val3=m;
                                }

                                if(d_hospitals_list.get(k).get("to_time2").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                                    t_val3=m;
                                }

                                if(d_hospitals_list.get(k).get("from_time3").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                                    f_val4=m;
                                }
                                if(d_hospitals_list.get(k).get("to_time3").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                                    t_val4=m;
                                }


                            }
                            if(f_val1==-1){
                                hashMap.put("from_time_pos", "0");
                            }else{
                                hashMap.put("from_time_pos", f_val1+"");
                            }

                            if(t_val1==-1){
                                hashMap.put("to_time_pos", "0");
                            }else{
                                hashMap.put("to_time_pos", t_val1+"");
                            }


                            if(f_val2==-1){
                                hashMap.put("from_time1_pos", "0");
                            }else{
                                hashMap.put("from_time1_pos", f_val2+"");
                            }

                            if(t_val2==-1){
                                hashMap.put("to_time1_pos", "0");
                            }else{
                                hashMap.put("to_time1_pos", t_val2+"");
                            }

                            if(f_val3==-1){
                                hashMap.put("from_time2_pos", "0");
                            }else{
                                hashMap.put("from_time2_pos", f_val3+"");
                            }

                            if(t_val3==-1){
                                hashMap.put("to_time2_pos", "0");
                            }else{
                                hashMap.put("to_time2_pos", t_val3+"");
                            }

                            if(f_val4==-1){
                                hashMap.put("from_time3_pos", "0");
                            }else{
                                hashMap.put("from_time3_pos", f_val4+"");
                            }

                            if(t_val4==-1){
                                hashMap.put("to_time3_pos", "0");
                            }else{
                                hashMap.put("to_time3_pos", t_val4+"");
                            }


                            for (int i= 0; i < TL_AddDoctor.days_list.size(); i++) {
                                if(TL_AddDoctor.days_list.get(i).get("id").equalsIgnoreCase(d_hospitals_list.get(k).get("from_days"))){
                                    val1=i;
                                }
                                if(TL_AddDoctor.days_list.get(i).get("id").equalsIgnoreCase(d_hospitals_list.get(k).get("to_days"))){
                                    val2=i;
                                }


                            }
                            if(val1==-1){
                                hashMap.put("from_days_pos", "0");
                                hashMap.put("from_days", "");
                            }else{
                                hashMap.put("from_days_pos", TL_AddDoctor.days_list.get(val1).get("id"));
                                hashMap.put("from_days", TL_AddDoctor.days_list.get(val1).get("day_name"));
                            }

                            if(val2==-1){
                                hashMap.put("to_days_pos", "0");
                                hashMap.put("to_days", "");
                            }else{
                                hashMap.put("to_days_pos", TL_AddDoctor.days_list.get(val2).get("id"));
                                hashMap.put("to_days", TL_AddDoctor.days_list.get(val2).get("day_name"));
                            }



                            hospitals_list.add(hashMap);
                        }

                        adapter = new HashMapRecycleviewadapter(getActivity(),hospitals_list, "doc_prfle", d_prf_recyler, R.layout.doc_prfle_lstitem);
                        d_prf_recyler.setAdapter(adapter);


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    if (InterNetChecker.isNetworkAvailable(activity)) {
                        getDrSpecializationService(activity);
                    } else {
                        StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.nointernet), activity);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomProgressbar.Progressbarcancel(activity);

            }
        });
    }

    private void getDrSpecializationService(final Activity activity) {
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
                            for (int k = 0; k < specialization_list.size(); k++) {
                                if(speacialisation_id.equalsIgnoreCase(specialization_list.get(k).get("specialization_id"))){
                                    d_prf_spclztn_edtx.setText(specialization_list.get(k).get("name"));
                                }
                                specialisation_names_list.add(specialization_list.get(k).get("name"));
                            }

                        } else {
                            StoredObjects.ToastMethod("No Data found", activity);
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
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
  private void SpeclizatnListPopup(final CustomEditText prfilenme,Activity activity) {
      final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
    listPopupWindow.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, specialisation_names_list));
    listPopupWindow.setAnchorView(prfilenme);
    listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        speacialisation_id = specialization_list.get(position).get("specialization_id");
        prfilenme.setText(specialisation_names_list.get(position));
        listPopupWindow.dismiss();

      }
    });

    listPopupWindow.show();
  }

  private void editDrProfileService(final Activity activity, String name_str,String email_str, String mobile_str, String registor_no,String address_Str,String state_board
          ,String d_prf_year_of_registration_str,String timings,String qualify,String e_qualify,String o_qualify) {
      CustomProgressbar.Progressbarshow(activity);
      APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
      api.editDrProfile(RetrofitInstance.edit_doctor_profile,name_str,email_str,mobile_str,file_name_str,speacialisation_id,
              state_board,registor_no,d_prf_year_of_registration_str,address_Str,timings,StoredObjects.Logged_DoctorId,
              StoredObjects.UserId,StoredObjects.UserRoleId,qualify,e_qualify,o_qualify).enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              if(response.body() != null) {
                  try {
                      String responseReceived = response.body().string();
                      JSONObject jsonObject = new JSONObject(responseReceived);
                      StoredObjects.LogMethod("response", "response::" + responseReceived);
                      String status = jsonObject.getString("status");
                      if (status.equalsIgnoreCase("200")) {
                          StoredObjects.ToastMethod("Updated successfully", activity);
                      } else {
                          String error = jsonObject.getString("error");
                          StoredObjects.ToastMethod(error, activity);
                      }
                  } catch (IOException | JSONException e) {
                      e.printStackTrace();
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
  private void updatelay(ArrayList<HashMap<String, String>> data_list) {

    if (data_list.size() == 0) {
      nodatavailable_txt.setVisibility(View.VISIBLE);
      d_prf_recyler.setVisibility(View.GONE);
    } else {
      nodatavailable_txt.setVisibility(View.GONE);
      d_prf_recyler.setVisibility(View.VISIBLE);
    }
  }

    private void Imagepickingpopup(final Activity activity, final String type) {

        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.photo_selpopup);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout p_cancel_lay=(LinearLayout) dialog.findViewById(R.id.p_cancel_lay);

    p_cancel_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog.dismiss();
      }
    });LinearLayout takepic_lay = (LinearLayout) dialog.findViewById(R.id.takepic_lay);
        LinearLayout pickglry_lay = (LinearLayout) dialog.findViewById(R.id.pickglry_lay);
        LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);


        cancel_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        takepic_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_type = type;
                captureImage();

                dialog.dismiss();
            }
        });

        pickglry_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_type = type;

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);


                dialog.dismiss();

            }

        });

        dialog.show();
    }

    private Uri filePath;
    File fileOrDirectory;

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(CameraUtils.MEDIA_TYPE_IMAGE);
        if (file != null) {
            CameraUtils.imageStoragePath = file.getAbsolutePath();
            fileOrDirectory = file;
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getActivity(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private Uri picUri;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //user is returning from capturing an image using the camera
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getActivity(), CameraUtils.imageStoragePath);

                try {
                    f_new = createNewFile("CROP_");
                    try {
                        f_new.createNewFile();
                    } catch (IOException ex) {
                        Log.e("io", ex.getMessage());
                    }


                    //Photo_SHowDialog(SignUp.this(),f_new,imageStoragePath,myBitmap);
                    imageupload(getActivity(), CameraUtils.imageStoragePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    StoredObjects.LogMethod("", "imagepathexpection:--" + e);

                }
                // successfully captured the image
                // display it in image view
                // Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getActivity(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
            }
        } else if (requestCode == 2) {

            StoredObjects.LogMethod("resultcode", "result code" + resultCode);
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();


                try {
                    Bitmap myBitmap = null;
                    picUri = data.getData();

                    myBitmap = (BitmapFactory.decodeFile(picturePath));

                    try {


                        f_new = createNewFile("CROP_");
                        try {
                            f_new.createNewFile();
                        } catch (IOException ex) {
                            Log.e("io", ex.getMessage());
                        }
                        StoredObjects.LogMethod("path", "path:::" + picturePath + "--" + myBitmap);
                        CameraUtils.imageStoragePath = picturePath;
                        imageupload(getActivity(), picturePath);
                        //new ImageUploadTaskNew().execute(docFilePath.toString());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        StoredObjects.LogMethod("", "Exception:--" + e1);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    StoredObjects.LogMethod("", "Exception:--" + e);
                }
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getActivity(),
                    "User cancelled image picking", Toast.LENGTH_SHORT)
                    .show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity(),
                    "Sorry! Failed to pick the image", Toast.LENGTH_SHORT)
                    .show();
            }

        }


    }

    private Uri mCropImagedUri;
    File f_new;

    private File createNewFile(String prefix) {
        if (prefix == null || "".equalsIgnoreCase(prefix)) {
            prefix = "IMG_";
        }
        File newDirectory = new File(Environment.getExternalStorageDirectory() + "/mypics/");
        if (!newDirectory.exists()) {
            if (newDirectory.mkdir()) {
                Log.d(getActivity().getClass().getName(), newDirectory.getAbsolutePath() + " directory created");
            }
        }
        File file = new File(newDirectory, (prefix + System.currentTimeMillis() + ".jpg"));
        if (file.exists()) {
            //this wont be executed
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public static String fileName = "";
    private Bitmap myImg = null;
    private File compressedImage;

    public void imageupload(final Context context, final String path) {
        String fileNameSegments[] = path.split("/");
        fileName = fileNameSegments[fileNameSegments.length - 1];

        myImg = Bitmap.createBitmap(CameraUtils.getResizedBitmap(CameraUtils.getUnRotatedImage(path, BitmapFactory.decodeFile(path)), 500));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        myImg.compress(Bitmap.CompressFormat.PNG, 100, stream);

        bitmapToUriConverter(myImg);

    }

    public void bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();

            File file = new File(getActivity().getFilesDir(), "UploadImages"
                + new Random().nextInt() + ".png");

            FileOutputStream out;
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion > Build.VERSION_CODES.M) {
                out = getActivity().openFileOutput(file.getName(),
                    Context.MODE_PRIVATE);
            } else {
                out = getActivity().openFileOutput(file.getName(),
                    Context.MODE_WORLD_READABLE);
            }

            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            new Compressor(getActivity())
                .compressToFileAsFlowable(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) {
                        compressedImage = file;
                        setCompressedImage();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });


        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
    }

    private void setCompressedImage() {

        Log.i("Compressor", "Compressed image save in " + compressedImage.getAbsolutePath());
        String realPath = compressedImage.getAbsolutePath();
        if (InterNetChecker.isNetworkAvailable(getActivity())) {

            File file = new File(realPath);

            try {
               // postFile(realPath, RetrofitInstance.BASEURL + "app/index.php", file.getName());
                new ImageuploadTask().execute(realPath, file.getName());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.nointernet), getActivity());
        }


    }

  public void fragmentcallinglay(Fragment fragment) {

    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
  }




    public void postFile(String encodedImage, String postUrl, String fileName) {


        CustomProgressbar.Progressbarshow(getActivity());
        okhttp3.Response response = null;

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        StrictMode.ThreadPolicy policy = new StrictMode.
            ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("method", RetrofitInstance.upload_file)
            .addFormDataPart("uploaded_file", fileName,
                RequestBody.create(MediaType.parse("application/octet-stream"),
                    new File(encodedImage)))
            .build();
        Request request = new Request.Builder()
            .url(postUrl)
            .method("POST", body)
            .addHeader("Cookie", "PHPSESSID=pp4db1qhog5fku530huapduqm5")
            .build();

        try {
            response = client.newCall(request).execute();
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            if (response.code() == 200) {
                file_name_str = jsonObject.getString("file_name");
                dr_prof_img.setImageBitmap(myImg);
                CustomProgressbar.Progressbarcancel(getActivity());
            } else {
                CustomProgressbar.Progressbarcancel(getActivity());
            }
            StoredObjects.LogMethod("val", "val::" + responseReceived);
        } catch (IOException|JSONException e) {

        }

    }

    public class ImageuploadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomProgressbar.Progressbarshow(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {

            String res = null;
            try {


                StrictMode.ThreadPolicy policy = new StrictMode.
                        ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("method", RetrofitInstance.upload_file)
                        .addFormDataPart("uploaded_file", params[1],
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        new File(params[0])))
                        .build();
                Request request = new Request.Builder()
                        .url(RetrofitInstance.IMAGEUPLOADURL)
                        .method("POST", body)
                        .build();

                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                Log.e("TAG", "Response : " + res);
                return res;

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e("TAG", "Error: " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
            }


            return res;

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            CustomProgressbar.Progressbarcancel(getActivity());

            if (response != null) {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        file_name_str = jsonObject.getString("file_name");
                        // patient_image.setImageBitmap(myImg);
                        try {
                            Glide.with(getActivity())
                                    .load(Uri.parse(RetrofitInstance.IMAGE_URL + file_name_str))
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.no_image)
                                            .fitCenter()
                                            .centerCrop())
                                    .into(dr_prof_img);
                            Glide.with(getActivity())
                                    .load(Uri.parse(RetrofitInstance.IMAGE_URL + file_name_str))
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.no_image)
                                            .fitCenter()
                                            .centerCrop())
                                    .into(SideMenu.header_circular_img);

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        String name_str = d_prf_name_edtx.getText().toString().trim();
                        String email_str = d_prf_email_edtx.getText().toString().trim();
                        String mobile_str = d_prf_mbile_edtx.getText().toString().trim();
                        String register_no = d_prf_rgstno_edtx.getText().toString().trim();
                        String address_Str = d_prf_adres_edtx.getText().toString().trim();
                        String d_prf_state_bard_str = d_prf_state_bard_edtx.getText().toString().trim();
                        String d_prf_year_of_registration_str = d_prf_year_of_registration_edtx.getText().toString().trim();
                        String qualification = d_qualification_edtx.getText().toString().trim();
                        String o_qualification = d_otherqualification_edtx.getText().toString().trim();
                        String e_qualification = d_extraqualification_edtx.getText().toString().trim();

                        String availabletimes="";

                        editDrProfileService(getActivity(),name_str,email_str,mobile_str,register_no,address_Str,d_prf_state_bard_str,d_prf_year_of_registration_str,availabletimes,qualification,e_qualification,o_qualification);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
