package com.rxmediaapp.fragments.hospital;

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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.CameraUtils;
import com.rxmediaapp.storedobjects.StoredObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class H_EditAssistant extends Fragment {

  ImageView backbtn_img, h_assistant_edit_img;
  TextView title_txt,h_doc_name_txt;
  CustomButton h_assistant_sbmt_btn;
  CustomEditText h_assistant_name_edtx, h_assistant_number_edtx,h_assistant_email_edtx, h_assistant_avltime_edtx, h_assistant_totime_edtx, h_assistant_asgndct_edtx;

  public static int position;
  String image_type = "";
  public static ArrayList<HashMap<String, String>> editarraylist = new ArrayList<>();


  ArrayList<String> doctors_names_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> doctors_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> doctors_list_ = new ArrayList<>();



/*  ArrayList<String> From_timeSlot_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> from_times_list = new ArrayList<>();

  ArrayList<String> to_timeSlot_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> to_times_list = new ArrayList<>();*/
 // String fromtimeslot_id="",totimeslot_id="";
  String hospital_id="",assistant_id="";
  String doctor_id="",doctor_names="";

  String first_time="yes";
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.h_edit_assistant, null, false);
    StoredObjects.page_type = "h_edit_assistant";
    SideMenu.updatemenu(StoredObjects.page_type);

    initilization(v);
    assignData();
    return v;
  }

  /* "assistant_id":27,"user_id":187,"hospital_id":19,"from_time":"02:00:00","to_time":"06:00:00",
           "created_at":"2020-06-12 18:03:18","name":"ad5","phone":"1212121214","email":"","assigned_doctors_count":1,
           "assigned_doctors":[{"name":"Doctor 1","specialization":"ENT (Ear, Nose & Throat)"}]}]}
*/
  /*private void timeListPopup(final CustomEditText prfilenme, final Activity activity) {
    final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
    listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, From_timeSlot_list));
    listPopupWindowone.setAnchorView(prfilenme);
    listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
    listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(From_timeSlot_list.get(position));
        String time_selected = From_timeSlot_list.get(position);
        fromtimeslot_id=from_times_list.get(position).get("time_slot");
        getToTimeService(activity, time_selected);
        listPopupWindowone.dismiss();

      }
    });

    listPopupWindowone.show();
  }

  private void toTimeListPopup(final CustomEditText prfilenme,Activity activity) {
    final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
    listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, to_timeSlot_list));
    listPopupWindowone.setAnchorView(prfilenme);
    listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
    listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(to_timeSlot_list.get(position));
        totimeslot_id=to_times_list.get(position).get("time_slot");
        listPopupWindowone.dismiss();

      }
    });

    listPopupWindowone.show();
  }

*/



  private void assignData() {
    try {
      h_assistant_name_edtx.setText(editarraylist.get(0).get("name"));
      h_assistant_number_edtx.setText(editarraylist.get(0).get("phone"));
      h_assistant_email_edtx.setText(editarraylist.get(0).get("email"));
      hospital_id=editarraylist.get(0).get("hospital_id");
      assistant_id=editarraylist.get(0).get("assistant_id");

      h_assistant_number_edtx.setEnabled(false);

      doctors_names_list.clear();
      doctors_list_=JsonParsing.GetJsonData(editarraylist.get(0).get("assigned_doctors"));
       String  doctornames="";
       doctor_names="";

      for(int k=0;k<doctors_list_.size();k++) {

        doctornames=doctornames+doctors_list_.get(k).get("name")+",";
      }

      if(doctornames.equalsIgnoreCase("")){

      }else{
        doctor_names=doctornames.substring(0,doctornames.length()-1);
      }

      h_doc_name_txt.setText(doctor_names);
     /* fromtimeslot_id=editarraylist.get(0).get("from_time");
      totimeslot_id=editarraylist.get(0).get("to_time");
      h_assistant_avltime_edtx.setText(StoredObjects.time12hrsformat(fromtimeslot_id));
      h_assistant_totime_edtx.setText(StoredObjects.time12hrsformat(totimeslot_id));
*/

     /* if (InterNetChecker.isNetworkAvailable(getActivity())) {
        getTimeSlots(getActivity());
      } else {
        StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
      }*/

      if (InterNetChecker.isNetworkAvailable(getActivity())) {
        getDrNamesService(getActivity());
      } else {
        StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
      }
    } catch ( JSONException e) {
      e.printStackTrace();
    }
  }

/*  private void getToTimeService(final Activity activity, String time_selected) {
    if(first_time.equalsIgnoreCase("Yes")){

    }else{
      CustomProgressbar.Progressbarshow(activity);
    }

    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getToTime(RetrofitInstance.to_time, time_selected).enqueue(new Callback<ResponseBody>() {
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
              to_times_list = JsonParsing.GetJsonData(results);
              to_timeSlot_list.clear();
              for (int k = 0; k < to_times_list.size(); k++) {
                to_timeSlot_list.add(StoredObjects.time12hrsformat(to_times_list.get(k).get("time_slot")));
              }

            } else {
              StoredObjects.ToastMethod("No Data found", activity);
            }

            first_time="no";


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
  }*/
  private void initilization(View v) {

    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);
    title_txt.setText("Edit Assistant");

    h_assistant_name_edtx = v.findViewById(R.id.h_assistant_name_edtx);
    h_assistant_number_edtx = v.findViewById(R.id.h_assistant_number_edtx);
    h_assistant_avltime_edtx = v.findViewById(R.id.h_assistant_avltime_edtx);
    h_assistant_totime_edtx = v.findViewById(R.id.h_assistant_totime_edtx);
    h_assistant_asgndct_edtx = v.findViewById(R.id.h_assistant_asgndct_edtx);
    h_assistant_edit_img = v.findViewById(R.id.h_assistant_edit_img);
        h_assistant_email_edtx= v.findViewById(R.id.h_assistant_email_edtx);
    h_assistant_sbmt_btn = v.findViewById(R.id.h_assistant_sbmt_btn);
    h_doc_name_txt=v.findViewById(R.id.h_doc_name_txt);

   /* h_assistant_totime_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        toTimeListPopup(h_assistant_totime_edtx,getActivity());
      }
    });
    h_assistant_avltime_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        h_assistant_totime_edtx.setText("");
        totimeslot_id="";
        timeListPopup(h_assistant_avltime_edtx,getActivity());
      }
    });*/

    h_assistant_asgndct_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(doctors_list.size()>0){

          assigndoctorpopup (getActivity());

        }else{
          StoredObjects.ToastMethod("No Doctors added",getActivity());
        }

      }
    });

    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
          fm.popBackStack();
        }
      }
    });


    h_assistant_sbmt_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String docas_nme_str = h_assistant_name_edtx.getText().toString().trim();
        String docas_mbile_str = h_assistant_number_edtx.getText().toString().trim();
        String email = h_assistant_email_edtx.getText().toString().trim();

        if (StoredObjects.inputValidation(h_assistant_name_edtx, getString(R.string.enter_dr_name), getActivity())) {
                   if(StoredObjects.Emailvalidation(email, getString(R.string.enter_valid_email), getActivity())){
                    if(doctor_id.equalsIgnoreCase("")){
                      StoredObjects.ToastMethod(getString(R.string.assign_doc_validate),getActivity());
                    }else{
                      editAssistantService(getActivity(), docas_nme_str, docas_mbile_str, email);

                    }


              }


        }

      }
    });
  }
  RecyclerView ass_doctor_recycle;
  public static HashMapRecycleviewadapter adapter;
  private void assigndoctorpopup(final Activity activity) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.assign_doctor_popup);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomButton submit_button = (CustomButton) dialog.findViewById(R.id.submit_button);
    ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);
    ass_doctor_recycle = (RecyclerView) dialog.findViewById(R.id.ass_doctor_recycle);

    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
    ass_doctor_recycle.setLayoutManager(linearLayoutManager);


    adapter = new HashMapRecycleviewadapter(getActivity(), doctors_list, "assign_doctor", ass_doctor_recycle, R.layout.assign_doc_listitem);
    ass_doctor_recycle.setAdapter(adapter);


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


    submit_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String doctorid="";
        String   doctornames="";

        doctor_id="";
        doctor_names="";
        for(int k=0;k<doctors_list.size();k++){
          if(doctors_list.get(k).get("is_viewed").equalsIgnoreCase("Yes")){
            doctorid=doctorid+doctors_list.get(k).get("user_id")+",";
            doctornames=doctornames+doctors_list.get(k).get("name")+",";
          }
        }
        if(doctorid.equalsIgnoreCase("")){
          h_doc_name_txt.setVisibility(View.GONE);
          StoredObjects.ToastMethod("Please select Doctors",getActivity());
        }else{
          doctor_id=doctorid.substring(0,doctorid.length()-1);
          doctor_names=doctornames.substring(0,doctornames.length()-1);
          h_doc_name_txt.setVisibility(View.VISIBLE);
          h_doc_name_txt.setText(doctor_names);
          dialog.dismiss();

        }


      }
    });

    dialog.show();
  }
 /* private void getTimeSlots(final Activity activity) {
    CustomProgressbar.Progressbarshow(activity);

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
              from_times_list = JsonParsing.GetJsonData(results);
              From_timeSlot_list.clear();
              for (int k = 0; k < from_times_list.size(); k++) {
                From_timeSlot_list.add(StoredObjects.time12hrsformat(from_times_list.get(k).get("time_slot")));
              }

            } else {
              StoredObjects.ToastMethod("No Data found", activity);
            }


          } catch (IOException | JSONException e) {
            e.printStackTrace();
          }

          if (InterNetChecker.isNetworkAvailable(getActivity())) {
            getDrNamesService(getActivity());
          } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
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
*/
  private void getDrNamesService(final Activity activity) {

    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getDrnames(RetrofitInstance.hospital_doctors_list,StoredObjects.UserId,StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
              dummy_list = JsonParsing.GetJsonData(results);

              doctors_list.clear();
              doctor_id = "";
              for (int k = 0; k < dummy_list.size(); k++) {

                int val = -1;
                for (int i = 0; i < doctors_list_.size(); i++) {
                  if (dummy_list.get(k).get("name").equalsIgnoreCase(doctors_list_.get(i).get("name"))) {
                    val = k;
                  }

                }
                HashMap<String, String> dumpData_update = new HashMap<String, String>();
                if (val == -1) {

                  dumpData_update.put("is_viewed", "No");
                  dumpData_update.put("user_id", dummy_list.get(k).get("user_id"));
                  dumpData_update.put("doctor_id", dummy_list.get(k).get("doctor_id"));
                  dumpData_update.put("name", dummy_list.get(k).get("name"));
                } else {

                  dumpData_update.put("is_viewed", "yes");
                  dumpData_update.put("user_id", dummy_list.get(k).get("user_id"));
                  dumpData_update.put("doctor_id", dummy_list.get(k).get("doctor_id"));
                  dumpData_update.put("name", dummy_list.get(k).get("name"));
                }
                doctors_list.add(dumpData_update);

              }

              doctor_id = "";
              String doctorids = "";
              for (int k = 0; k < doctors_list.size(); k++) {
                doctorids = doctorids + doctors_list.get(k).get("user_id") + ",";
              }

              if (doctorids.length() > 0) {
                doctor_id = doctorids.substring(0, doctorids.length() - 1);
              }


            } else {
              doctors_list.clear();

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
  private void editAssistantService(final Activity activity, String name_str, String mobile_str, String email) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.EdithospitalAssistant(RetrofitInstance.hospital_edit_assistant, StoredObjects.UserId, StoredObjects.UserRoleId, name_str, mobile_str, doctor_id, assistant_id,email).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {

            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("response", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Edited successfully", activity);
            } else {
              String error = jsonObject.getString("error");
              StoredObjects.ToastMethod(error, activity);
            }
          } catch (IOException | JSONException e) {
            e.printStackTrace();
          }

        }
        CustomProgressbar.Progressbarcancel(activity);

        fragmentcallinglay(new H_Assistant());
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        CustomProgressbar.Progressbarcancel(activity);

      }
    });

  }



  public void fragmentcallinglay(Fragment fragment) {

    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
  }


}
