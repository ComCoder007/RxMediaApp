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

import androidx.annotation.Nullable;

import com.rxmediaapp.R;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.Sidemenu.SideMenu;
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


public class DoctorSignup extends Activity {

  ImageView d_sgnup_back_img;
  CustomEditText d_sgnup_nme_edtx, d_sgnup_email_edtx, d_sgnup_rgstno_edtx, d_sgnup_spclztn_edtx, d_sgnup_statbard_edtx,
      d_sgnup_yrrgstn_edtx, d_sgnup_mbile_edtx, d_sgnup_adres_edtx, d_sgnup_frmday_edtx, d_sgnup_today_edtx, d_sgnup_custm_edtx,
      d_sgnup_avabletme_edtx, d_sgnup_tmetwo_edtx, d_sgnup_pswd_edtx, d_sgnup_cnfmpswd_edtx;
  CustomButton d_sgnup_btn;
  String speacialisation_id="",fromtimeslot_id="",totimeslot_id="",fromday_id="",today_id="";


  ArrayList<String> daynames_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> days_list = new ArrayList<>();


  ArrayList<String> specialisation_names_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> specialization_list = new ArrayList<>();

  ArrayList<String> to_daynames_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> to_days_list = new ArrayList<>();
  ArrayList<String> to_timeSlot_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> to_times_list = new ArrayList<>();

  ArrayList<String> From_timeSlot_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> from_times_list = new ArrayList<>();




  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.doctor_signup);


    initialisation();

    servicecalling();
  }


  public void initialisation() {

    d_sgnup_back_img = findViewById(R.id.d_sgnup_back_img);
    d_sgnup_spclztn_edtx = findViewById(R.id.d_sgnup_spclztn_edtx);
    d_sgnup_frmday_edtx = findViewById(R.id.d_sgnup_frmday_edtx);
    d_sgnup_today_edtx = findViewById(R.id.d_sgnup_today_edtx);
    d_sgnup_avabletme_edtx = findViewById(R.id.d_sgnup_avabletme_edtx);
    d_sgnup_tmetwo_edtx = findViewById(R.id.d_sgnup_tmetwo_edtx);
    d_sgnup_nme_edtx = findViewById(R.id.d_sgnup_nme_edtx);
    d_sgnup_email_edtx = findViewById(R.id.d_sgnup_email_edtx);
    d_sgnup_btn = findViewById(R.id.d_sgnup_btn);
    d_sgnup_rgstno_edtx = findViewById(R.id.d_sgnup_rgstno_edtx);
    d_sgnup_statbard_edtx = findViewById(R.id.d_sgnup_statbard_edtx);
    d_sgnup_yrrgstn_edtx = findViewById(R.id.d_sgnup_yrrgstn_edtx);
    d_sgnup_mbile_edtx = findViewById(R.id.d_sgnup_mbile_edtx);
    d_sgnup_adres_edtx = findViewById(R.id.d_sgnup_adres_edtx);
    d_sgnup_custm_edtx = findViewById(R.id.d_sgnup_custm_edtx);
    d_sgnup_pswd_edtx = findViewById(R.id.d_sgnup_pswd_edtx);
    d_sgnup_cnfmpswd_edtx = findViewById(R.id.d_sgnup_cnfmpswd_edtx);


    d_sgnup_back_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        DoctorSignup.this.finish();
      }
    });

    d_sgnup_frmday_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        d_sgnup_today_edtx.setText("");
        FromdaysPopup(d_sgnup_frmday_edtx,DoctorSignup.this);
      }
    });

    d_sgnup_today_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        ToDayListPopup(d_sgnup_today_edtx,DoctorSignup.this);
      }
    });

    d_sgnup_avabletme_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        d_sgnup_tmetwo_edtx.setText("");
        totimeslot_id="";
        timeListPopup(d_sgnup_avabletme_edtx,DoctorSignup.this);
      }
    });

    d_sgnup_tmetwo_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        toTimeListPopup(d_sgnup_tmetwo_edtx,DoctorSignup.this);
      }
    });

    d_sgnup_spclztn_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SpeclizatnListPopup(d_sgnup_spclztn_edtx,DoctorSignup.this);
      }
    });

    d_sgnup_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String d_signup_dr_name_str = d_sgnup_nme_edtx.getText().toString().trim();
        String d_signup_dr_email_str = d_sgnup_email_edtx.getText().toString().trim();
        String d_signup_dr_regNo_str = d_sgnup_rgstno_edtx.getText().toString().trim();
        String d_signup_dr_spclztn_str = d_sgnup_spclztn_edtx.getText().toString().trim();
        String d_signup_dr_statboard_str = d_sgnup_statbard_edtx.getText().toString().trim();
        String d_signup_dr_yrrgstn_str = d_sgnup_yrrgstn_edtx.getText().toString().trim();
        String d_signup_dr_mobile_str = d_sgnup_mbile_edtx.getText().toString().trim();
        String d_signup_dr_adres_str = d_sgnup_adres_edtx.getText().toString().trim();
        String d_signup_dr_frmday_str = d_sgnup_frmday_edtx.getText().toString().trim();
        String d_signup_dr_today_str = d_sgnup_today_edtx.getText().toString().trim();
        String d_signup_dr_custom_str = d_sgnup_custm_edtx.getText().toString().trim();
        String d_signup_dr_avabletme_str = d_sgnup_avabletme_edtx.getText().toString().trim();
        String d_signup_dr_tmetwo_str = d_sgnup_tmetwo_edtx.getText().toString().trim();
        String d_signup_dr_passwd_str = d_sgnup_pswd_edtx.getText().toString().trim();
        String d_signup_dr_cnPasswd_str = d_sgnup_cnfmpswd_edtx.getText().toString().trim();

        if (StoredObjects.inputValidation(d_sgnup_nme_edtx, getApplicationContext().getResources().getString(R.string.enter_dr_name), DoctorSignup.this)) {

          if (StoredObjects.Emailvalidation(d_signup_dr_email_str, getApplicationContext().getResources().getString(R.string.enter_valid_email), DoctorSignup.this)) {
            if (StoredObjects.inputValidation(d_sgnup_rgstno_edtx, getApplicationContext().getResources().getString(R.string.reg_validation), DoctorSignup.this)) {
              if (StoredObjects.inputValidation(d_sgnup_spclztn_edtx,getApplicationContext().getResources().getString(R.string.specizlization_validation), DoctorSignup.this)) {
                if (StoredObjects.inputValidation(d_sgnup_statbard_edtx,getApplicationContext().getResources().getString(R.string.state_board_validation), DoctorSignup.this)) {
                  if (StoredObjects.inputValidation(d_sgnup_yrrgstn_edtx, getApplicationContext().getResources().getString(R.string.year_reg_validation), DoctorSignup.this)) {
                    if (StoredObjects.isValidMobile(d_signup_dr_mobile_str)) {
                      if (StoredObjects.inputValidation(d_sgnup_frmday_edtx, getApplicationContext().getResources().getString(R.string.from_day_validate), DoctorSignup.this)) {
                        if (StoredObjects.inputValidation(d_sgnup_today_edtx,getApplicationContext().getResources().getString(R.string.to_day_validate), DoctorSignup.this)) {
                          if (StoredObjects.inputValidation(d_sgnup_avabletme_edtx, getApplicationContext().getResources().getString(R.string.from_time_validate), DoctorSignup.this)) {
                            if (StoredObjects.inputValidation(d_sgnup_tmetwo_edtx, getApplicationContext().getResources().getString(R.string.to_time_validate), DoctorSignup.this)) {

                              if (StoredObjects.inputValidation(d_sgnup_pswd_edtx, getApplicationContext().getResources().getString(R.string.enter_pass_validation), DoctorSignup.this)) {
                                if (StoredObjects.inputValidation(d_sgnup_cnfmpswd_edtx, getApplicationContext().getResources().getString(R.string.enter_confirm_pass_validation), DoctorSignup.this)) {
                                  if (d_signup_dr_passwd_str.equals(d_signup_dr_cnPasswd_str)) {
                                    doctorSignUpService(DoctorSignup.this, d_signup_dr_name_str, d_signup_dr_email_str, speacialisation_id, d_signup_dr_statboard_str, d_signup_dr_regNo_str, d_signup_dr_yrrgstn_str, d_signup_dr_mobile_str, d_signup_dr_adres_str, d_signup_dr_frmday_str, d_signup_dr_today_str, d_signup_dr_custom_str, fromtimeslot_id, totimeslot_id, d_signup_dr_passwd_str, d_signup_dr_cnPasswd_str);
                                    //startActivity(new Intent(DoctorSignup.this, SideMenu.class));
                                  } else {
                                    StoredObjects.ToastMethod(getApplicationContext().getResources().getString(R.string.confirm_pass_validation), DoctorSignup.this);
                                  }
                                }
                              }

                            }
                          }
                        }

                      }
                    } else {
                      StoredObjects.ToastMethod(getApplicationContext().getResources().getString(R.string.enter_valid_mobile), DoctorSignup.this);
                    }
                  }
                }
              }


            }
          }
        }



      }
    });
  }


  private void SpeclizatnListPopup(final CustomEditText prfilenme,Activity activity) {
    final ListPopupWindow dropdownpopup = new ListPopupWindow(activity);
    dropdownpopup.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, specialisation_names_list));
    dropdownpopup.setAnchorView(prfilenme);
    dropdownpopup.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    dropdownpopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        speacialisation_id = specialization_list.get(position).get("specialization_id");
        prfilenme.setText(specialisation_names_list.get(position));
        dropdownpopup.dismiss();

      }
    });

    dropdownpopup.show();
  }

  private void FromdaysPopup(final CustomEditText prfilenme, final Activity activity) {
    final ListPopupWindow dropdownpopup = new ListPopupWindow(activity);
    dropdownpopup.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, daynames_list));
    dropdownpopup.setAnchorView(prfilenme);
    dropdownpopup.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    dropdownpopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(daynames_list.get(position));
        fromday_id = days_list.get(position).get("id");
        getToDaysService(activity, fromday_id);
        dropdownpopup.dismiss();

      }
    });

    dropdownpopup.show();
  }

  private void ToDayListPopup(final CustomEditText prfilenme,Activity activity) {
    final ListPopupWindow dropdownpopup = new ListPopupWindow(activity);
    dropdownpopup.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, to_daynames_list));
    dropdownpopup.setAnchorView(prfilenme);
    dropdownpopup.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    dropdownpopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        today_id = days_list.get(position).get("id");
        prfilenme.setText(to_daynames_list.get(position));
        dropdownpopup.dismiss();

      }
    });
    dropdownpopup.show();
  }


  private void timeListPopup(final CustomEditText prfilenme, final Activity activity) {
    final ListPopupWindow dropdownpopup = new ListPopupWindow(activity);
    dropdownpopup.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, From_timeSlot_list));
    dropdownpopup.setAnchorView(prfilenme);
    dropdownpopup.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
    dropdownpopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(From_timeSlot_list.get(position));
       String time_selected = From_timeSlot_list.get(position);
        fromtimeslot_id=from_times_list.get(position).get("time_slot");
        getToTimeService(activity, time_selected);
        dropdownpopup.dismiss();

      }
    });

    dropdownpopup.show();
  }

  private void toTimeListPopup(final CustomEditText prfilenme,Activity activity) {
    final ListPopupWindow dropdownpopup = new ListPopupWindow(activity);
    dropdownpopup.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, to_timeSlot_list));
    dropdownpopup.setAnchorView(prfilenme);
    dropdownpopup.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
    dropdownpopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(to_timeSlot_list.get(position));
        totimeslot_id=to_times_list.get(position).get("time_slot");
        dropdownpopup.dismiss();

      }
    });

    dropdownpopup.show();
  }


  private void servicecalling() {
    if (InterNetChecker.isNetworkAvailable(DoctorSignup.this)) {
      get_weekdays(DoctorSignup.this);

    } else {
      StoredObjects.ToastMethod(getApplicationContext().getResources().getString(R.string.nointernet), DoctorSignup.this);
    }
  }


  public void get_weekdays(final Activity activity) {

    CustomProgressbar.Progressbarshow(activity);

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
              days_list = JsonParsing.GetJsonData(results);
              daynames_list.clear();

              for (int k = 0; k < days_list.size(); k++) {
                daynames_list.add(days_list.get(k).get("day_name"));

              }

            } else {
              StoredObjects.ToastMethod("No Data found", activity);
            }


          } catch (IOException | JSONException e) {
            e.printStackTrace();
          }

          //CustomProgressbar.Progressbarcancel(activity);


          if (InterNetChecker.isNetworkAvailable(DoctorSignup.this)) {
            getTimeSlots(DoctorSignup.this);
          } else {
            StoredObjects.ToastMethod(getApplicationContext().getResources().getString(R.string.nointernet), DoctorSignup.this);
          }
        }


      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        CustomProgressbar.Progressbarcancel(activity);


      }
    });
  }

  private void getToDaysService(final Activity activity, String days_id) {

    CustomProgressbar.Progressbarshow(activity);

    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getToDays(RetrofitInstance.to_days, days_id).enqueue(new Callback<ResponseBody>() {
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
              to_days_list = JsonParsing.GetJsonData(results);
              to_daynames_list.clear();
              for (int k = 0; k < to_days_list.size(); k++) {
                to_daynames_list.add(to_days_list.get(k).get("day_name"));

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
          if (InterNetChecker.isNetworkAvailable(DoctorSignup.this)) {
            getDrSpecializationService(DoctorSignup.this);
          } else {
            StoredObjects.ToastMethod(getApplicationContext().getResources().getString(R.string.nointernet), DoctorSignup.this);
          }
        }

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
        CustomProgressbar.Progressbarcancel(activity);
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

  private void doctorSignUpService(final Activity activity, String d_signup_dr_name_str, String d_signup_dr_email_str, String speacialisation_id, String d_signup_dr_statboard_str, String d_signup_dr_regNo_str, String d_signup_dr_yrrgstn_str, String d_signup_dr_mobile_str, String d_signup_dr_adres_str, String d_signup_dr_frmday_str, String d_signup_dr_today_str, String d_signup_dr_custom_str, String d_signup_dr_avabletme_str, String d_signup_dr_tmetwo_str, String d_signup_dr_passwd_str, String d_signup_dr_cnPasswd_str) {
    CustomProgressbar.Progressbarshow(activity);

    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.drSignUp(RetrofitInstance.doctor_sign_up, d_signup_dr_name_str, d_signup_dr_email_str, speacialisation_id, d_signup_dr_statboard_str,
            d_signup_dr_regNo_str, d_signup_dr_yrrgstn_str, d_signup_dr_mobile_str, d_signup_dr_adres_str, fromday_id, today_id, d_signup_dr_custom_str, d_signup_dr_avabletme_str, d_signup_dr_tmetwo_str, d_signup_dr_passwd_str).enqueue(new Callback<ResponseBody>() {
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
              DoctorSignup.this.finish();
              startActivity(new Intent(DoctorSignup.this, SignIn.class));
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
