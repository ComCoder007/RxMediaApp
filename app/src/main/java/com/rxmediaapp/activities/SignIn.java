package com.rxmediaapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.database.Database;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends Activity {
  TextView sgn_cratacnt_txt, sgn_forgotpswd_txt;
  CustomButton sgn_sgnin_btn;
  EditText email_edttx, sgn_in_passwd;
  TextView rem_txt;
  CheckBox rem_cb;


  Database database;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    int r_count=0;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.signin);

    database=new Database(SignIn.this);
    database.getAllDevice();
      loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
      loginPrefsEditor = loginPreferences.edit();

      saveLogin = loginPreferences.getBoolean("s_saveLogin", false);


      initialisation();
  }

  public void initialisation() {

    sgn_cratacnt_txt = findViewById(R.id.sgn_cratacnt_txt);
    sgn_forgotpswd_txt = findViewById(R.id.sgn_forgotpswd_txt);
    sgn_sgnin_btn = findViewById(R.id.sgn_sgnin_btn);
    email_edttx = findViewById(R.id.email_edttx);
    sgn_in_passwd = findViewById(R.id.sgn_in_passwd);

      rem_txt = findViewById(R.id.rem_txt);
      rem_cb = findViewById(R.id.rem_cb);

      if (saveLogin == true) {
          r_count=1;
          email_edttx.setText(loginPreferences.getString("username", ""));
          sgn_in_passwd.setText(loginPreferences.getString("password", ""));
          rem_cb.setChecked(true);
      }

      rem_cb.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              r_count++;
              if(r_count==1){
                  rem_cb.setChecked(true);

              }else{
                  rem_cb.setChecked(false);
                  r_count=0;

              }
          }
      });
      rem_txt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              r_count++;
              if(r_count==1){
                  rem_cb.setChecked(true);

              }else{
                  rem_cb.setChecked(false);
                  r_count=0;

              }
          }
      });

    sgn_sgnin_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String username = email_edttx.getText().toString().trim();
        String sgn_in_passwd_str = sgn_in_passwd.getText().toString().trim();
        if (StoredObjects.inputValidation(email_edttx, "Please enter Username", SignIn.this)) {
            if(StoredObjects.inputValidation(sgn_in_passwd,getApplicationContext().getResources().getString(R.string.enter_pass_validation),SignIn.this)){
          /*if (username.equalsIgnoreCase("Patients") || username.equalsIgnoreCase("Doctors") ||
              username.equalsIgnoreCase("Hospitals") || username.equalsIgnoreCase("Assistant")
              || username.equalsIgnoreCase("Team Leader") || username.equalsIgnoreCase("Marketing Executive")
              || username.equalsIgnoreCase("Franchisee") || username.equalsIgnoreCase("Sub Franchisee")) {
            StoredObjects.UserType = username;
            StoredObjects.listcount = 0;
              SignIn.this.finish();
              startActivity(new Intent(SignIn.this,SideMenu.class));
          }*/
              loginService(SignIn.this,username,sgn_in_passwd_str);
          }
        }

      }
    });


    sgn_cratacnt_txt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        startActivity(new Intent(SignIn.this, SignupOptions.class));
      }
    });

    sgn_forgotpswd_txt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        startActivity(new Intent(SignIn.this, ForgotPassword.class));
      }
    });

  }

    private void loginService(final Activity activity, String username, String sgn_in_passwd_str) {
        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.login(RetrofitInstance.login,username,sgn_in_passwd_str,"11444",RetrofitInstance.device_type).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseReceived = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseReceived);
                        String status = jsonObject.getString("status");
                        if(status.equalsIgnoreCase("200"))
                        {

                            rememberpassword();
                            // {"status":200,"message":"success","user_id":35,"user_type":"Doctors","role_id":7,"doctor_id":2,"assistatnt_id":"","patient_id":"","hospital_id":""}
                            StoredObjects.ToastMethod("Logged in successfully",activity);
                            StoredObjects.UserId= jsonObject.getString("user_id");
                            StoredObjects.UserType = jsonObject.getString("user_type");
                            StoredObjects.UserRoleId= jsonObject.getString("role_id");
                            StoredObjects.Logged_DoctorId = jsonObject.getString("doctor_id");
                            StoredObjects.Logged_AssistantId= jsonObject.getString("assistant_id");
                            StoredObjects.Logged_PatientId = jsonObject.getString("patient_id");
                            StoredObjects.Logged_HospitalId= jsonObject.getString("hospital_id");
                            StoredObjects.listcount = 0;
                            database.UpdateUserdata("user_id",StoredObjects.UserId);
                            database.UpdateUserdata("user_type",StoredObjects.UserType);
                            database.UpdateUserdata("role_id",StoredObjects.UserRoleId);
                            database.UpdateUserdata("doctor_id",StoredObjects.Logged_DoctorId);
                            database.UpdateUserdata("assistant_id",StoredObjects.Logged_AssistantId);
                            database.UpdateUserdata("patient_id",StoredObjects.Logged_PatientId);
                            database.UpdateUserdata("hospital_id",StoredObjects.Logged_HospitalId);
                            SignIn.this.finish();
                            startActivity(new Intent(SignIn.this,SideMenu.class));
                        }else{
                            StoredObjects.ToastMethod("Invalid Login details",activity);
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

    private void rememberpassword() {
        String email = email_edttx.getText().toString().trim();
        String passwordval = sgn_in_passwd.getText().toString().trim();

        if (r_count==1) {
            loginPrefsEditor.putBoolean("s_saveLogin", true);
            loginPrefsEditor.putString("username", email);
            loginPrefsEditor.putString("password", passwordval);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

    }
}
