package com.rxmediaapp.fragments.doctor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import retrofit2.http.POST;
import retrofit2.http.Query;

public class Doc_Add_Appointment extends Fragment {

    CustomEditText doc_nme_edtx,doc_email_edtx,doc_mbile_edtx,doc_adhar_edtx,
            doc_dob_edtx,doc_gender_edtx,doc_bldgrup_edtx,doc_cnsltdctr_edtx;

    ImageView backbtn_img;
    TextView title_txt;
    CustomButton doc_adapntmnt_btn;

    String[] genderlist = {"Male","Female"};

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    String patient_id="0";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.doc_add_apntmnt,null,false );
        StoredObjects.page_type="doc_add_apntmnt";

        SideMenu.updatemenu(StoredObjects.page_type);

        initilization(v);
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById(R.id.backbtn_img);
        title_txt= v.findViewById( R.id.title_txt);
        title_txt.setText( "Add Appointment" );

        doc_nme_edtx = v.findViewById(R.id.doc_nme_edtx);
        doc_email_edtx= v.findViewById( R.id.doc_email_edtx);
        doc_mbile_edtx = v.findViewById(R.id.doc_mbile_edtx);
        doc_adhar_edtx= v.findViewById( R.id.doc_adhar_edtx);
        doc_dob_edtx = v.findViewById(R.id.doc_dob_edtx);
        doc_gender_edtx= v.findViewById( R.id.doc_gender_edtx);
        doc_bldgrup_edtx = v.findViewById(R.id.doc_bldgrup_edtx);
        doc_cnsltdctr_edtx= v.findViewById( R.id.doc_cnsltdctr_edtx);

        doc_adapntmnt_btn = v.findViewById(R.id.doc_adapntmnt_btn);

        doc_cnsltdctr_edtx.setImeOptions(EditorInfo.IME_ACTION_DONE);
        doc_cnsltdctr_edtx.setRawInputType(InputType.TYPE_CLASS_TEXT);

        doc_mbile_edtx.setText(StoredObjects.p_mobilenum);


        doc_bldgrup_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BloodgroupPopup(doc_bldgrup_edtx,getActivity());
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


        doc_dob_edtx.setOnClickListener(new View.OnClickListener() {
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

                                doc_dob_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        doc_gender_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GenderListPopup (doc_gender_edtx,getActivity());
            }
        });



        doc_adapntmnt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  nme_str = doc_nme_edtx.getText().toString().trim();
                String  email_str = doc_email_edtx.getText().toString().trim();
                String  mobile_str = doc_mbile_edtx.getText().toString().trim();
                String  aadhar_str = doc_adhar_edtx.getText().toString().trim();
                String  dob_str = doc_dob_edtx.getText().toString().trim();
                String gender = doc_gender_edtx.getText().toString().trim();
                String  blood_str = doc_bldgrup_edtx.getText().toString().trim();

                String consult_str = doc_cnsltdctr_edtx.getText().toString().trim();



                if (StoredObjects.inputValidation(doc_nme_edtx, getString(R.string.enter_dr_name), getActivity())) {
                    if (StoredObjects.Emailvalidation( email_str, getString(R.string.enter_valid_email), getActivity())) {
                        if (StoredObjects.inputValidation(doc_dob_edtx, getString(R.string.dob_validation), getActivity())) {

                            if (StoredObjects.inputValidation(doc_adhar_edtx, getString(R.string.aadhar_validation), getActivity())) {
                                if (StoredObjects.inputValidation(doc_mbile_edtx, getString(R.string.enter_mobile_validation), getActivity())) {
                                    if (StoredObjects.isValidMobile( mobile_str)) {
                                        if (StoredObjects.inputValidation(doc_gender_edtx, getString(R.string.gender_validate), getActivity())) {
                                            if (StoredObjects.inputValidation(doc_bldgrup_edtx, getString(R.string.blood_validation), getActivity())) {

                                                if (StoredObjects.inputValidation(doc_cnsltdctr_edtx, getString(R.string.enter_dueto), getActivity())) {

                                                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                                                        AddAppointmentService(getActivity(), nme_str, email_str,mobile_str, aadhar_str,dob_str , gender, blood_str,consult_str);
                                                    } else {
                                                        StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                                                    }


                                                }

                                            }
                                        }

                                    } else {
                                        StoredObjects.ToastMethod(getString(R.string.enter_valid_mobile), getActivity());
                                    }
                                }
                            }



                        }
                    }
                }


            }


        });

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            getBloodGroup(getActivity());

        } else {
            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.nointernet), getActivity());
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

    private void GenderListPopup(final CustomEditText prfilenme,Activity activity){
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
        listPopupWindow.setAdapter(new ArrayAdapter<>(activity,R.layout.drpdwn_lay,genderlist));
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


    private void AddAppointmentService(final FragmentActivity activity, String  nme_str, String  email_str, String  mobile_str, String  aadhar_str, String  dob_str, String  gender, String blood_str, String consult_str) {
        CustomProgressbar.Progressbarshow(activity);



        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.DocAddAppointment(RetrofitInstance.doc_add_appointment,patient_id,nme_str,email_str,dob_str,aadhar_str,
                mobile_str,gender,blood_id,StoredObjects.UserId,Doc_Sel_Patient.Hospital_ID,
                consult_str,StoredObjects.UserId,StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseRecieved = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseRecieved);
                        StoredObjects.LogMethod("response", "response::" + responseRecieved);
                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("200")) {
                            StoredObjects.ToastMethod("Added Appointment Successfully", activity);
                            fragmentcallinglay(new Doc_Sel_Patient());
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

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }


}


