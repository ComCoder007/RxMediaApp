package com.rxmediaapp.fragments.teamleader;

import android.app.Activity;
import android.app.DatePickerDialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.activities.PatientSignup;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.CameraUtils;
import com.rxmediaapp.storedobjects.StoredObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class F_Edit_Lead extends Fragment {

  ImageView backbtn_img;
  TextView title_txt;
  EditText f_appointment_edtx,f_p_address_edtxt,f_p_comment_edtxt,f_p_number_edtxt,f_p_nme_edtx,f_pat_status_edtx;
  Button f_p_save_btn;
  String[] status_list = {"Interested", "Next Appointment","Not Interested","Cancelled"};

  DatePickerDialog datePickerDialog;
  int year;
  int month;
  int dayOfMonth;
  Calendar calendar;
  String status = "";

  public static ArrayList<HashMap<String ,String >> data_list = new ArrayList<>();


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate( R.layout.f_edit_lead,null,false );
    StoredObjects.page_type="f_patient_lead_one";

    SideMenu.updatemenu(StoredObjects.page_type);
    initilization(v);
    assignData();
    return v;
  }

  private void assignData() {
    try {
      f_p_nme_edtx.setText(data_list.get(0).get("name"));
      f_p_number_edtxt.setText(data_list.get(0).get("phone"));
      f_appointment_edtx.setText(StoredObjects.convertDateformat(data_list.get(0).get("appointment_date")));
      f_p_comment_edtxt.setText(data_list.get(0).get("comments"));
      f_p_address_edtxt.setText(data_list.get(0).get("address"));
      status=data_list.get(0).get("status");

      f_pat_status_edtx.setText(status);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initilization(View v) {

    backbtn_img = v.findViewById( R.id. backbtn_img);
    title_txt= v.findViewById( R.id. title_txt);
    f_p_save_btn = v.findViewById( R.id. f_p_save_btn);
    f_appointment_edtx = v.findViewById( R.id. f_pat_appointment_edtx);
    f_p_address_edtxt = v.findViewById( R.id. f_p_address_edtxt);
    f_p_comment_edtxt= v.findViewById( R.id. f_p_comment_edtxt);
    f_p_number_edtxt = v.findViewById( R.id. f_p_number_edtxt);
    f_appointment_edtx = v.findViewById( R.id. f_pat_appointment_edtx);
    f_p_nme_edtx = v.findViewById( R.id. f_p_nme_edtx);
    f_pat_status_edtx = v.findViewById( R.id. f_pat_status_edtx);

    f_p_comment_edtxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
    f_p_comment_edtxt.setRawInputType(InputType.TYPE_CLASS_TEXT);

    f_p_address_edtxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
    f_p_address_edtxt.setRawInputType(InputType.TYPE_CLASS_TEXT);

    if (StoredObjects.tab_type.equalsIgnoreCase("Hospital")) {

      title_txt.setText( "Hospital Lead" );
      f_p_nme_edtx.setHint("Hospital Name *");
    } else if (StoredObjects.tab_type.equalsIgnoreCase("Doctor")) {

      title_txt.setText( "Doctor Lead" );
      f_p_nme_edtx.setHint("Dr Name *");
    } else {

      title_txt.setText( "Patient Lead" );
      f_p_nme_edtx.setHint("Patient Name *");
    }



    f_p_number_edtxt.setEnabled(false);
    f_p_save_btn.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String name_str = f_p_nme_edtx.getText().toString();
        String mobile_str = f_p_number_edtxt.getText().toString();
        String appointment_date_str = f_appointment_edtx.getText().toString();
        String status_str = f_pat_status_edtx.getText().toString();
        String comment_str = f_p_comment_edtxt.getText().toString();
        String address_str = f_p_address_edtxt.getText().toString();
        String user_id_str = data_list.get(0).get("lead_id");

        if (StoredObjects.inputValidation(f_p_nme_edtx, getString(R.string.enter_dr_name), getActivity())) {
          if (StoredObjects.inputValidation(f_appointment_edtx,getString(R.string.address_validation), getActivity())) {

              if (StoredObjects.inputValidation(f_p_address_edtxt,getString(R.string.appointment_validation), getActivity())) {
                if (StoredObjects.inputValidation(f_pat_status_edtx,"Please select Status", getActivity())) {
                  if (InterNetChecker.isNetworkAvailable(getActivity())) {
                    leadEditService(getActivity(), name_str, mobile_str, appointment_date_str, status_str, comment_str, address_str,user_id_str);
                  } else {
                    StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
                  }
                }

              }

          }
        }
      }
    });


    backbtn_img.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
          fm.popBackStack();
        }
      }
    } );

    f_pat_status_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        StatusListPopUp(f_pat_status_edtx,getActivity());
      }
    });

    f_appointment_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(),
            new DatePickerDialog.OnDateSetListener() {
              @Override
              public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                f_appointment_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
              }
            }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();
      }
    });

  }

  private void leadEditService(final Activity activity, String name_str, String mobile_str, String appointment_date_str, String status_str, String comment_str, String address_str,String user_id_str) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    String editmethod="";
    if (StoredObjects.tab_type.equalsIgnoreCase("Hospital")) {
      editmethod=RetrofitInstance.edit_hospital_lead;
    } else if (StoredObjects.tab_type.equalsIgnoreCase("Doctor")) {
      editmethod=RetrofitInstance.edit_doctor_lead;
    } else {
      editmethod=RetrofitInstance.edit_patient_lead;
    }

    api.editLead(editmethod,name_str,mobile_str,appointment_date_str,status_str,comment_str,address_str,StoredObjects.UserId,StoredObjects.UserRoleId, user_id_str).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("response", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Edited successfully!", activity);
              fragmentcallinglay(new Franchisee_Details());
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

  private void StatusListPopUp(final EditText f_pat_status_edtx,Activity activity) {
    final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
    listPopupWindow.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, status_list));
    listPopupWindow.setAnchorView(f_pat_status_edtx);
    listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        f_pat_status_edtx.setText(status_list[position]);
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
