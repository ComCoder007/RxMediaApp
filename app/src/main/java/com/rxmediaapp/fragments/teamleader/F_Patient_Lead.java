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
import com.rxmediaapp.fragments.patient.P_Sub_Member;
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
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class F_Patient_Lead extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    EditText f_appointment_edtx,f_p_address_edtxt,f_p_comment_edtxt,f_p_number_edtxt,f_p_nme_edtx;
    Button f_p_save_btn;


    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.f_patient_lead_one,null,false );
        StoredObjects.page_type="f_patient_lead_one";
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);

        return v;
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

        f_p_comment_edtxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        f_p_comment_edtxt.setRawInputType(InputType.TYPE_CLASS_TEXT);

        f_p_address_edtxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        f_p_address_edtxt.setRawInputType(InputType.TYPE_CLASS_TEXT);
        title_txt.setText( "Patient Lead" );

        f_p_save_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pati_nme_str = f_p_nme_edtx.getText().toString().trim();
                String pati_mobile_str = f_p_number_edtxt.getText().toString().trim();
                String pati_appdate_str = f_appointment_edtx.getText().toString().trim();
                String pati_comnt_str = f_p_comment_edtxt.getText().toString().trim();
                String pati_addre_str = f_p_address_edtxt.getText().toString().trim();

                        if (StoredObjects.inputValidation(f_p_nme_edtx, getString(R.string.enter_dr_name), getActivity())) {
                            if (StoredObjects.inputValidation(f_p_number_edtxt, getString(R.string.enter_mobile_validation), getActivity())) {
                                if (StoredObjects.isValidMobile(pati_mobile_str)) {
                                    if (StoredObjects.inputValidation(f_appointment_edtx, getString(R.string.dob_validation), getActivity())) {

                                            if (StoredObjects.inputValidation(f_p_address_edtxt, getString(R.string.address_validation), getActivity())) {

                                                addPatientLeadService(getActivity(),pati_nme_str,pati_mobile_str,pati_appdate_str,pati_comnt_str,pati_addre_str);

                                            }

                                    }
                                } else {
                                    StoredObjects.ToastMethod(getString(R.string.enter_valid_mobile), getActivity());
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
                                calendar.set(year,month,day);
                                f_appointment_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();


            }
        });

    }

    private void addPatientLeadService(final FragmentActivity activity, String pati_nme_str, String pati_mobile_str, String pati_appdate_str, String pati_comnt_str, String pati_addre_str) {
        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.addLead(RetrofitInstance.add_patient_lead,pati_nme_str,pati_mobile_str,pati_appdate_str,pati_comnt_str,pati_addre_str,StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
