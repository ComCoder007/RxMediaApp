package com.rxmediaapp.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PatientSignup extends Activity {

  ImageView p_sgnup_back_img;
  CustomEditText p_sgnup_nme_edtx, p_sgnup_email_edtx, p_sgnup_mbile_edtx, p_sgnup_adhar_edtx, p_sgnup_dob_edtx,
      p_sgnup_gender_edtx, p_sgnup_bldgrup_edtx, p_sgnup_pswd_edtx, p_sgnup_cnfmpswd_edtx;
  CustomButton p_sgnup_btn;

  private ListPopupWindow listPopupWindow;
  String[] genderlist = {"Male", "Female"};

  DatePickerDialog datePickerDialog;
  int year;
  int month;
  int dayOfMonth;
  Calendar calendar;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.patient_signup);
    listPopupWindow = new ListPopupWindow(PatientSignup.this);
    initialisation();
  }

  public void initialisation() {

    p_sgnup_back_img = findViewById(R.id.p_sgnup_back_img);
    p_sgnup_btn = findViewById(R.id.p_sgnup_btn);
    p_sgnup_nme_edtx = findViewById(R.id.p_sgnup_nme_edtx);
    p_sgnup_email_edtx = findViewById(R.id.p_sgnup_email_edtx);
    p_sgnup_mbile_edtx = findViewById(R.id.p_sgnup_mbile_edtx);
    p_sgnup_adhar_edtx = findViewById(R.id.p_sgnup_adhar_edtx);
    p_sgnup_dob_edtx = findViewById(R.id.p_sgnup_dob_edtx);
    p_sgnup_gender_edtx = findViewById(R.id.p_sgnup_gender_edtx);
    p_sgnup_bldgrup_edtx = findViewById(R.id.p_sgnup_bldgrup_edtx);
    p_sgnup_pswd_edtx = findViewById(R.id.p_sgnup_pswd_edtx);
    p_sgnup_cnfmpswd_edtx = findViewById(R.id.p_sgnup_cnfmpswd_edtx);


    p_sgnup_bldgrup_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        BloodgroupPopup(p_sgnup_bldgrup_edtx,PatientSignup.this);
      }
    });
    p_sgnup_back_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        PatientSignup.this.finish();
      }
    });

    p_sgnup_dob_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(PatientSignup.this,
                new DatePickerDialog.OnDateSetListener() {
                  @Override
                  public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                    p_sgnup_dob_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
                  }
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
      }
    });

    p_sgnup_gender_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        GenderListPopup(p_sgnup_gender_edtx);
      }
    });

    p_sgnup_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String p_sgnup_nme_str = p_sgnup_nme_edtx.getText().toString().trim();
        String p_sgnup_email_str = p_sgnup_email_edtx.getText().toString().trim();
        String p_sgnup_dob_str = p_sgnup_dob_edtx.getText().toString().trim();
        String p_sgnup_gender_str = p_sgnup_gender_edtx.getText().toString().trim();
        String p_sgnup_mbile_str = p_sgnup_mbile_edtx.getText().toString().trim();
        String p_sgnup_bldgrup_str = p_sgnup_bldgrup_edtx.getText().toString().trim();
        String p_sgnup_adhar_str = p_sgnup_adhar_edtx.getText().toString().trim();
        String p_sgnup_pswd_str = p_sgnup_pswd_edtx.getText().toString().trim();
        String p_sgnup_cnfmpswd_str = p_sgnup_cnfmpswd_edtx.getText().toString().trim();


        if (StoredObjects.inputValidation(p_sgnup_nme_edtx, getApplicationContext().getResources().getString(R.string.enter_dr_name), PatientSignup.this)) {

            if (StoredObjects.Emailvalidation(p_sgnup_email_str,getApplicationContext().getResources().getString(R.string.enter_valid_email),PatientSignup.this)) {
              if (StoredObjects.isValidMobile(p_sgnup_mbile_str)) {
                if (StoredObjects.inputValidation(p_sgnup_dob_edtx, getApplicationContext().getResources().getString(R.string.dob_validation), PatientSignup.this)) {
                  if (StoredObjects.inputValidation(p_sgnup_gender_edtx, getApplicationContext().getResources().getString(R.string.gender_validate), PatientSignup.this)) {
                    if (StoredObjects.inputValidation(p_sgnup_bldgrup_edtx, getApplicationContext().getResources().getString(R.string.blood_validation), PatientSignup.this)) {

                      if (StoredObjects.inputValidation(p_sgnup_pswd_edtx, getApplicationContext().getResources().getString(R.string.enter_pass_validation), PatientSignup.this)) {
                        if (StoredObjects.inputValidation(p_sgnup_cnfmpswd_edtx, getApplicationContext().getResources().getString(R.string.enter_confirm_pass_validation), PatientSignup.this)) {
                          if (p_sgnup_pswd_str.equals(p_sgnup_cnfmpswd_str)) {
                            patientSignUpService(PatientSignup.this, p_sgnup_nme_str, p_sgnup_email_str, p_sgnup_dob_str, p_sgnup_gender_str, p_sgnup_mbile_str, p_sgnup_bldgrup_str, p_sgnup_adhar_str, p_sgnup_pswd_str);
                            //startActivity(new Intent(DoctorSignup.this, SideMenu.class));
                          } else {
                            StoredObjects.ToastMethod(getApplicationContext().getResources().getString(R.string.confirm_pass_validation), PatientSignup.this);
                          }
                        }
                      }
                    }
                  }
                }

              } else {
                StoredObjects.ToastMethod(getApplicationContext().getResources().getString(R.string.enter_valid_mobile), PatientSignup.this);
              }

            }


        }
      }
    });

    if (InterNetChecker.isNetworkAvailable(PatientSignup.this)) {
      getBloodGroup(PatientSignup.this);

    } else {
      StoredObjects.ToastMethod(getApplicationContext().getResources().getString(R.string.nointernet), PatientSignup.this);
    }


  }

  private void BloodgroupPopup(final CustomEditText prfilenme,Activity activity) {
    final ListPopupWindow dropdownpopup = new ListPopupWindow(activity);
    dropdownpopup.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, bloodgroupnames));
    dropdownpopup.setAnchorView(prfilenme);
    dropdownpopup.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    dropdownpopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(bloodgroupnames.get(position));
        blood_id=bloodgroup_list.get(position).get("id");
        dropdownpopup.dismiss();

      }
    });

    dropdownpopup.show();
  }
  String blood_id="";
  ArrayList<String> bloodgroupnames = new ArrayList<>();
  ArrayList<HashMap<String, String>> bloodgroup_list = new ArrayList<>();
  private void getBloodGroup(final Activity activity) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getBloodgroup(RetrofitInstance.blood_groups).enqueue(new Callback<ResponseBody>() {
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
              bloodgroup_list = JsonParsing.GetJsonData(results);
              bloodgroupnames.clear();
              for (int k = 0; k < bloodgroup_list.size(); k++) {

                bloodgroupnames.add(bloodgroup_list.get(k).get("name"));
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
  private void GenderListPopup(final CustomEditText prfilenme) {
    listPopupWindow.setAdapter(new ArrayAdapter<>(PatientSignup.this, R.layout.drpdwn_lay, genderlist));
    listPopupWindow.setAnchorView(prfilenme);
    listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(genderlist[position]);
        listPopupWindow.dismiss();

      }
    });

    listPopupWindow.show();
  }

  private void patientSignUpService(final Activity activity, String p_sgnup_nme_str, String p_sgnup_email_str, String p_sgnup_dob_str, String p_sgnup_gender_str, String p_sgnup_mbile_str, String p_sgnup_bldgrup_str, String p_sgnup_adhar_str, String p_sgnup_pswd_str) {
    CustomProgressbar.Progressbarshow(activity);

    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.patientSignUp(RetrofitInstance.patient_sign_up, p_sgnup_nme_str, p_sgnup_email_str, p_sgnup_dob_str, p_sgnup_gender_str, p_sgnup_mbile_str, blood_id, p_sgnup_adhar_str, p_sgnup_pswd_str).enqueue(new Callback<ResponseBody>() {
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
              PatientSignup.this.finish();
              startActivity(new Intent(PatientSignup.this, SignIn.class));
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
