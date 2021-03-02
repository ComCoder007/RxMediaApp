package com.rxmediaapp.activities;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.customfonts.CustomEditText;
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
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HosptalSignup extends Activity {

  ImageView h_sgnup_back_img;
  CustomEditText h_sgnup_hsptlnme_edtx, h_sgnup_email_edtx, h_sgnup_hsptlrgstno_edtx, h_sgnup_beds_edtx, h_sgnup_mbile_edtx,
      h_sgnup_adres_edtx, h_sgnup_avabletme_edtx, h_sgnup_tmetwo_edtx, h_sgnup_pswd_edtx, h_sgnup_cnfmpswd_edtx;
  CustomButton h_sgnup_btn;

  String fromtimeslot_id="",totimeslot_id="";
  ArrayList<String> From_timeSlot_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> from_times_list = new ArrayList<>();

  ArrayList<String> to_timeSlot_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> to_times_list = new ArrayList<>();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.hosptal_signup);


    initialisation();

    serviceCalling(HosptalSignup.this);
  }

  private void serviceCalling(Activity activity) {
    if (InterNetChecker.isNetworkAvailable(activity)) {
      getTimeSlots(activity);
    } else {
      StoredObjects.ToastMethod(getApplicationContext().getResources().getString(R.string.nointernet), activity);
    }
  }

  public void initialisation() {

    h_sgnup_back_img = findViewById(R.id.h_sgnup_back_img);
    h_sgnup_adres_edtx = findViewById(R.id.h_sgnup_adres_edtx);
    h_sgnup_avabletme_edtx = findViewById(R.id.h_sgnup_avabletme_edtx);
    h_sgnup_tmetwo_edtx = findViewById(R.id.h_sgnup_tmetwo_edtx);
    h_sgnup_hsptlnme_edtx = findViewById(R.id.h_sgnup_hsptlnme_edtx);
    h_sgnup_email_edtx = findViewById(R.id.h_sgnup_email_edtx);
    h_sgnup_hsptlrgstno_edtx = findViewById(R.id.h_sgnup_hsptlrgstno_edtx);
    h_sgnup_beds_edtx = findViewById(R.id.h_sgnup_beds_edtx);
    h_sgnup_mbile_edtx = findViewById(R.id.h_sgnup_mbile_edtx);
    h_sgnup_pswd_edtx = findViewById(R.id.h_sgnup_pswd_edtx);
    h_sgnup_cnfmpswd_edtx = findViewById(R.id.h_sgnup_cnfmpswd_edtx);

    h_sgnup_btn = findViewById(R.id.h_sgnup_btn);

    h_sgnup_back_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        finish();
      }
    });



    h_sgnup_avabletme_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        h_sgnup_tmetwo_edtx.setText("");
        totimeslot_id="";
        timeListPopup(h_sgnup_avabletme_edtx,HosptalSignup.this);
      }
    });

    h_sgnup_tmetwo_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        toTimeListPopup(h_sgnup_tmetwo_edtx,HosptalSignup.this);
      }
    });

    h_sgnup_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String h_sgnup_hsptlnme_str = h_sgnup_hsptlnme_edtx.getText().toString().trim();
        String h_sgnup_email_str = h_sgnup_email_edtx.getText().toString().trim();
        String h_sgnup_hsptlrgstno_str = h_sgnup_hsptlrgstno_edtx.getText().toString().trim();
        String h_sgnup_mbile_str = h_sgnup_mbile_edtx.getText().toString().trim();
        String h_sgnup_beds_str = h_sgnup_beds_edtx.getText().toString().trim();
        String h_sgnup_adres_str = h_sgnup_adres_edtx.getText().toString().trim();
        String h_sgnup_avabletme_str = h_sgnup_avabletme_edtx.getText().toString().trim();
        String  h_sgnup_tmetwo_str =  h_sgnup_tmetwo_edtx.getText().toString().trim();
        String h_sgnup_pswd_str = h_sgnup_pswd_edtx.getText().toString().trim();
        String h_sgnup_cnfmpswd_str = h_sgnup_cnfmpswd_edtx.getText().toString().trim();

        if (StoredObjects.inputValidation(h_sgnup_hsptlnme_edtx, getApplicationContext().getResources().getString(R.string.enter_dr_name), HosptalSignup.this)) {

            if (StoredObjects.Emailvalidation(h_sgnup_email_str,getApplicationContext().getResources().getString(R.string.enter_valid_email), HosptalSignup.this)) {
              if (StoredObjects.inputValidation(h_sgnup_hsptlrgstno_edtx, getApplicationContext().getResources().getString(R.string.reg_validation), HosptalSignup.this)) {
                if (StoredObjects.inputValidation(h_sgnup_beds_edtx,getApplicationContext().getResources().getString(R.string.no_beds_validate), HosptalSignup.this)) {
                  if (StoredObjects.isValidMobile(h_sgnup_mbile_str)) {

                    if (StoredObjects.inputValidation(h_sgnup_avabletme_edtx, getApplicationContext().getResources().getString(R.string.from_time_validate), HosptalSignup.this) && StoredObjects.inputValidation(h_sgnup_tmetwo_edtx, getString(R.string.to_time_validate), HosptalSignup.this)) {

                        if (StoredObjects.inputValidation(h_sgnup_pswd_edtx, getApplicationContext().getResources().getString(R.string.enter_pass_validation), HosptalSignup.this)) {
                          if (StoredObjects.inputValidation(h_sgnup_cnfmpswd_edtx, getApplicationContext().getResources().getString(R.string.enter_confirm_pass_validation), HosptalSignup.this)) {
                            if (h_sgnup_pswd_str.equals(h_sgnup_cnfmpswd_str)) {
                              hospialSignUpService(HosptalSignup.this,h_sgnup_hsptlnme_str,h_sgnup_email_str,h_sgnup_hsptlrgstno_str,h_sgnup_beds_str,h_sgnup_mbile_str,h_sgnup_adres_str,h_sgnup_avabletme_str,h_sgnup_tmetwo_str,h_sgnup_pswd_str,h_sgnup_cnfmpswd_str);
                              //startActivity(new Intent(DoctorSignup.this, SideMenu.class));
                            } else {
                              StoredObjects.ToastMethod(getApplicationContext().getResources().getString(R.string.confirm_pass_validation), HosptalSignup.this);
                            }
                          }
                        }


                    }

                  } else {
                    StoredObjects.ToastMethod(getApplicationContext().getResources().getString(R.string.enter_valid_mobile), HosptalSignup.this);
                  }
                }
              }
            }
          }

      }
    });

  }



  private void timeListPopup(final CustomEditText prfilenme,Activity activity) {
    final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
    listPopupWindowone.setAdapter(new ArrayAdapter<>(HosptalSignup.this, R.layout.drpdwn_lay, From_timeSlot_list));
    listPopupWindowone.setAnchorView(prfilenme);
    listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
    listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(From_timeSlot_list.get(position));
        fromtimeslot_id=from_times_list.get(position).get("time_slot");
        getToTimeService(HosptalSignup.this, From_timeSlot_list.get(position));
        listPopupWindowone.dismiss();

      }
    });
    listPopupWindowone.show();
  }

  private void toTimeListPopup(final CustomEditText prfilenme,Activity activity) {
    final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
    listPopupWindowone.setAdapter(new ArrayAdapter<>(HosptalSignup.this, R.layout.drpdwn_lay, to_timeSlot_list));
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

  private void getTimeSlots(final Activity activity) {
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
        }

        CustomProgressbar.Progressbarcancel(activity);
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        CustomProgressbar.Progressbarcancel(activity);

      }
    });
  }

  private void getToTimeService(final Activity activity, String time_selected) {
    CustomProgressbar.Progressbarshow(activity);
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

  private void hospialSignUpService(final Activity activity, String h_sgnup_hsptlnme_str, String h_sgnup_email_str, String h_sgnup_hsptlrgstno_str, String h_sgnup_beds_str, String h_sgnup_mbile_str, String h_sgnup_adres_str, String h_sgnup_avabletme_str, String h_sgnup_tmetwo_str, String h_sgnup_pswd_str, String h_sgnup_cnfmpswd_str) {
    CustomProgressbar.Progressbarshow(activity);

    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.hospitalSignup(RetrofitInstance.hospital_sign_up,h_sgnup_hsptlnme_str,h_sgnup_email_str,h_sgnup_hsptlrgstno_str,h_sgnup_beds_str,h_sgnup_mbile_str,h_sgnup_adres_str,h_sgnup_avabletme_str,h_sgnup_tmetwo_str,h_sgnup_pswd_str).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseRecieved = response.body().string();
            JSONObject jsonObject = new JSONObject(responseRecieved);
            StoredObjects.LogMethod("response", "response::" + responseRecieved);
            String status = jsonObject.getString("status");

            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Signed Up successfully!", activity);
              HosptalSignup.this.finish();
              startActivity(new Intent(HosptalSignup.this, SignIn.class));
            }else{

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

}
