package com.rxmediaapp.fragments.assistant;

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

import com.rxmediaapp.fragments.patient.P_Sub_Member;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;

import org.json.JSONArray;
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

public class Add_Appointment_Two extends Fragment {

    CustomEditText apnmnt_nme_edtx,apnmnt_email_edtx,apnmnt_mbile_edtx,apnmnt_adhar_edtx,
            apnmnt_dob_edtx,apnmnt_gender_edtx,apnmnt_bldgrup_edtx,apnmnt_cnsltdctr_edtx,apnmnt_password_edtx,apnmnt_confirm_pass_edtx;

    ImageView backbtn_img;
    TextView title_txt;
    LinearLayout addphy_ex_lay;
    CustomButton apnmnt_adapntmnt_btn;


    String[] genderlist = {"Male","Female"};

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;


    public  static ArrayList<HashMap<String, String>> physuggestionslist = new ArrayList<>();
    String patient_id="0";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.assis_add_apntmnt_one,null,false );
        StoredObjects.page_type="add_apntmnt";

        SideMenu.updatemenu(StoredObjects.page_type);

        initilization(v);
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById(R.id.backbtn_img);
        title_txt= v.findViewById( R.id.title_txt);
        title_txt.setText( "Add Patient" );

        apnmnt_nme_edtx = v.findViewById(R.id.apnmnt_nme_edtx);
        apnmnt_email_edtx= v.findViewById( R.id.apnmnt_email_edtx);
        apnmnt_mbile_edtx = v.findViewById(R.id.apnmnt_mbile_edtx);
        apnmnt_adhar_edtx= v.findViewById( R.id.apnmnt_adhar_edtx);
        apnmnt_dob_edtx = v.findViewById(R.id.apnmnt_dob_edtx);
        apnmnt_gender_edtx= v.findViewById( R.id.apnmnt_gender_edtx);
        apnmnt_bldgrup_edtx = v.findViewById(R.id.apnmnt_bldgrup_edtx);
        apnmnt_cnsltdctr_edtx= v.findViewById( R.id.apnmnt_cnsltdctr_edtx);
        addphy_ex_lay= v.findViewById( R.id.addphy_ex_lay);
      // apnmnt_confirm_pass_edtx= v.findViewById( R.id.apnmnt_confirm_pass_edtx);
        apnmnt_adapntmnt_btn = v.findViewById(R.id.apnmnt_adapntmnt_btn);
        apnmnt_cnsltdctr_edtx.setImeOptions(EditorInfo.IME_ACTION_DONE);
        apnmnt_cnsltdctr_edtx.setRawInputType(InputType.TYPE_CLASS_TEXT);

        apnmnt_mbile_edtx.setText(StoredObjects.p_mobilenum);

        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }

            }
        } );


        apnmnt_dob_edtx.setOnClickListener(new View.OnClickListener() {
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

                                apnmnt_dob_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        apnmnt_gender_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GenderListPopup (apnmnt_gender_edtx,getActivity());
            }
        });

        addphy_ex_lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                physicalexampopup (getActivity());
            }
        } );


        apnmnt_bldgrup_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BloodgroupPopup(apnmnt_bldgrup_edtx,getActivity());
            }
        });
        apnmnt_adapntmnt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                String  nme_str = apnmnt_nme_edtx.getText().toString().trim();
                String  email_str = apnmnt_email_edtx.getText().toString().trim();
                String  mobile_str = apnmnt_mbile_edtx.getText().toString().trim();
                String  aadhar_str = apnmnt_adhar_edtx.getText().toString().trim();
                String  dob_str = apnmnt_dob_edtx.getText().toString().trim();
                String gender = apnmnt_gender_edtx.getText().toString().trim();
                String  blood_str = apnmnt_bldgrup_edtx.getText().toString().trim();
                String consult_str = apnmnt_cnsltdctr_edtx.getText().toString().trim();

                String physuggestionsval="";
                JSONArray PhysicalsugArray = new JSONArray();
                JSONObject jsonObject1 = null;
                for (int i= 0;i<physuggestionslist.size();i++) {

                    if(physuggestionslist.get(i).get("suggestion_id").equalsIgnoreCase("")&&
                            physuggestionslist.get(i).get("suggestion_value").equalsIgnoreCase("")){

                    }else{
                        try {
                            jsonObject1 = new JSONObject();
                            jsonObject1.put("suggestion_id", physuggestionslist.get(i).get("suggestion_id"));
                            jsonObject1.put("suggestion_value",  physuggestionslist.get(i).get("suggestion_value"));

                            PhysicalsugArray.put(jsonObject1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

                physuggestionsval=PhysicalsugArray.toString();




                if (StoredObjects.inputValidation(apnmnt_nme_edtx,getActivity().getResources().getString(R.string.enter_dr_name), getActivity())) {
                        if (StoredObjects.Emailvalidation( email_str, getString(R.string.enter_valid_email), getActivity())) {
                            if (StoredObjects.inputValidation(apnmnt_mbile_edtx, getString(R.string.enter_mobile_validation), getActivity())) {
                                if (StoredObjects.isValidMobile( mobile_str)) {
                                            if (StoredObjects.inputValidation(apnmnt_gender_edtx, getString(R.string.gender_validate), getActivity())) {
                                                if (StoredObjects.inputValidation(apnmnt_bldgrup_edtx, getString(R.string.blood_validation), getActivity())) {
                                                            if (StoredObjects.inputValidation(apnmnt_cnsltdctr_edtx, getString(R.string.enter_dueto), getActivity())) {

                                                                if(physuggestionsval.equalsIgnoreCase("[]")){

                                                                    StoredObjects.ToastMethod("Please select Physical Suggestions",getActivity());
                                                                }else{

                                                                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                                                                        AddAppointmentService(getActivity(), nme_str, email_str, mobile_str, aadhar_str, dob_str, gender, blood_str,consult_str,physuggestionsval);
                                                                    } else {
                                                                        StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                                                                    }

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

            
        });

        physuggestionslist.clear();
        addphysuggestions();

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


    private void AddAppointmentService(final FragmentActivity activity, String  nme_str, String  email_str, String  mobile_str, String  aadhar_str, String  dob_str, String  blood_str, String gender, String consult_str,String physical) {
        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.AddAppointmentTwo(RetrofitInstance.add_appointment_two,patient_id,nme_str,email_str,dob_str,
                gender,blood_id,mobile_str,aadhar_str,consult_str,StoredObjects.Pat_DocID,physical,StoredObjects.UserId,
                StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
                            fragmentcallinglay(new Add_Appointment());
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

    public static void addphysuggestions() {
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("suggestion_id","");
        hashMap.put("suggestion_value","");
        hashMap.put("suggestion_name","");
        hashMap.put("remove","0");
        physuggestionslist.add(hashMap);
    }
    public static HashMapRecycleviewadapter adapter;
    private void physicalexampopup(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_physical_examination );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);

        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
        CustomButton search_button =(CustomButton) dialog.findViewById(R.id.search_button);
        ImageView canclimg = (ImageView)dialog.findViewById(R.id.canclimg);
        LinearLayout addphy_lay = (LinearLayout)dialog.findViewById(R.id.addphy_lay);

        RecyclerView add_physical_recycle = (RecyclerView)dialog.findViewById(R.id.add_physical_recycle);

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(activity);
        add_physical_recycle.setLayoutManager(linearLayoutManager);

        adapter=new HashMapRecycleviewadapter(activity, physuggestionslist,"add_physical",add_physical_recycle,R.layout.add_physical_examination_listitems);
        add_physical_recycle.setAdapter(adapter);


        addphy_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int val=0;
                for(int k=0;k<physuggestionslist.size();k++){
                    if(physuggestionslist.get(k).get("suggestion_id").equalsIgnoreCase("")||
                            physuggestionslist.get(k).get("suggestion_value").equalsIgnoreCase("")){

                        val=-1;
                    }
                }
                if(val==0){
                    addphysuggestions();
                    adapter.notifyDataSetChanged();
                }else{
                    StoredObjects.ToastMethod("Please save Physical Examinations Details",getActivity());
                }

            }
        });
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


        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int val=0;
                for(int k=0;k<physuggestionslist.size();k++){
                    if(physuggestionslist.get(k).get("suggestion_id").equalsIgnoreCase("")||
                            physuggestionslist.get(k).get("suggestion_value").equalsIgnoreCase("")){

                        val=-1;
                    }
                }
                if(val==0){
                    dialog.dismiss();
                }else{
                    StoredObjects.ToastMethod("Please fill Physical Examinations Details",getActivity());
                }

            }
        });

        dialog.show();
    }
    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }


}

