package com.rxmediaapp.fragments.teamleader;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.rxmediaapp.R;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;
import com.rxmediaapp.Sidemenu.SideMenu;

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

public class F_Hospital_Lead extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    EditText f_appointment_edtx,f_hsptl_nme_edtx,f_mbile_edtx,f_coment_edtx,f_address_edtx;
    Button f_save_btn;



    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
    int pagecount=1,totalpages=0;
    String recordsperpage="10";
    ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.f_hospital_lead_one,null,false );
        StoredObjects.page_type="f_hospital_lead_one";

        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);


        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        f_save_btn = v.findViewById( R.id. f_save_btn);
        f_appointment_edtx = v.findViewById( R.id. f_appointment_edtx);
        f_hsptl_nme_edtx = v.findViewById( R.id. f_hsptl_nme_edtx);
        f_mbile_edtx= v.findViewById( R.id. f_mbile_edtx);
        f_coment_edtx = v.findViewById( R.id. f_coment_edtx);
        f_address_edtx = v.findViewById( R.id. f_address_edtx);

        f_coment_edtx.setImeOptions(EditorInfo.IME_ACTION_DONE);
        f_coment_edtx.setRawInputType(InputType.TYPE_CLASS_TEXT);

        f_address_edtx.setImeOptions(EditorInfo.IME_ACTION_DONE);
        f_address_edtx.setRawInputType(InputType.TYPE_CLASS_TEXT);

        title_txt.setText( "Hospital Lead" );

        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        } );

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


        f_save_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String hosp_nme_str = f_hsptl_nme_edtx.getText().toString().trim();
                String hosp_mobile_str = f_mbile_edtx.getText().toString().trim();
                String hosp_appdate_str = f_appointment_edtx.getText().toString().trim();
                String hosp_comnt_str = f_coment_edtx.getText().toString().trim();
                String hosp_addre_str = f_address_edtx.getText().toString().trim();

                if (StoredObjects.inputValidation(f_hsptl_nme_edtx, getString(R.string.enter_dr_name), getActivity())) {
                    if (StoredObjects.inputValidation(f_mbile_edtx, getString(R.string.enter_mobile_validation), getActivity())) {
                        if (StoredObjects.isValidMobile(hosp_mobile_str)) {
                            if (StoredObjects.inputValidation(f_appointment_edtx, getString(R.string.appointment_validation), getActivity())) {
                                      if (StoredObjects.inputValidation(f_address_edtx, getString(R.string.address_validation), getActivity())) {

                                        addHospitalLeadService(getActivity(),hosp_nme_str,hosp_mobile_str,hosp_appdate_str,hosp_comnt_str,hosp_addre_str);

                                    }

                            }
                        } else {
                            StoredObjects.ToastMethod(getString(R.string.enter_valid_mobile), getActivity());
                        }


                    }
                }
            }
        });


    }

    private void addHospitalLeadService(final FragmentActivity activity, String hosp_nme_str, String hosp_mobile_str, String hosp_appdate_str, String hosp_comnt_str, String hosp_addre_str) {
        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.addLead(RetrofitInstance.add_hospital_lead,hosp_nme_str,hosp_mobile_str,hosp_appdate_str,hosp_comnt_str,hosp_addre_str,StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseRecieved = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseRecieved);
                        StoredObjects.LogMethod("response", "response::" + responseRecieved);
                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("200")) {
                            StoredObjects.ToastMethod("Added Successfully", activity);
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



    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).commit ();

    }

}
