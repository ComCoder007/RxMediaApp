package com.rxmediaapp.fragments.doctor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.fragments.dashboards.Doc_Dashboard;
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

public class Doc_Sel_Patient extends Fragment {

    CustomEditText sel_clinic_edtx,sel_mobile_edtx;
    CustomButton sel_search_btn;
    ImageView backbtn_img;
    TextView title_txt;

    ArrayList<String> hospitalname_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> hospitals_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> p_data_list = new ArrayList<>();

    public static String Hospital_ID="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.d_searchpat_popup,null,false );
        StoredObjects.page_type="sel_patient";
        p_data_list.clear();
        SideMenu.updatemenu(StoredObjects.page_type);
        try {
            StoredObjects.listcount= 3;
            SideMenu.adapter.notifyDataSetChanged();
        }catch (Exception e){

        }
        initilization(v);
        serviceCalling();
        return v;
    }

    private void initilization(View v) {
        backbtn_img = v.findViewById(R.id.backbtn_img);
        title_txt= v.findViewById( R.id.title_txt);
        title_txt.setText( "Add Appointment" );


        sel_clinic_edtx = v.findViewById(R.id.sel_clinic_edtx);
        sel_mobile_edtx= v.findViewById( R.id.sel_mobile_edtx);
        sel_search_btn = v.findViewById(R.id.sel_search_btn);

        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentcallinglay1(new Doc_Dashboard());

            }
        } );



        sel_clinic_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Hospital_ID="";

                if(hospitalname_list.size()>0){
                    ListPopup((CustomEditText) sel_clinic_edtx,getActivity());
                }else{
                    StoredObjects.ToastMethod("No Hospitals found",getActivity());
                }
            }
        });



        sel_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  mobile_str = sel_mobile_edtx.getText().toString().trim();

                if(Hospital_ID.equalsIgnoreCase("")){
                    StoredObjects.ToastMethod("Please select Hospital",getActivity());
                }else{
                    if (StoredObjects.inputValidation(sel_mobile_edtx, getString(R.string.enter_mobile_validation), getActivity())) {
                        if (StoredObjects.isValidMobile(mobile_str)) {

                            if (InterNetChecker.isNetworkAvailable(getActivity())) {
                                AddAppointmentService(getActivity(), mobile_str);
                            } else {
                                StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                            }


                        } else {
                            StoredObjects.ToastMethod(getString(R.string.enter_valid_mobile), getActivity());
                        }
                    }

                }



            }


        });

    }

    private void serviceCalling() {

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            PatientlistmainService(getActivity());
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }

    private void PatientlistmainService(final Activity activity) {
        CustomProgressbar.Progressbarshow(activity);
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.Docpatientlistmain(RetrofitInstance.doctor_clinics, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseReceived = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseReceived);
                        StoredObjects.LogMethod("response", "response::" + responseReceived);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("200")) {
                            String results = jsonObject.getString("results");
                            hospitals_list = JsonParsing.GetJsonData(results);
                            hospitalname_list.clear();
                            for (int k = 0; k < hospitals_list.size(); k++) {
                                hospitalname_list.add(hospitals_list.get(k).get("clinic_name"));
                            }


                        } else {
                            hospitals_list.clear();
                            hospitalname_list.clear();
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


    private void ListPopup(final CustomEditText prfilenme,Activity activity){
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
        listPopupWindow.setAdapter(new ArrayAdapter<>(activity,R.layout.drpdwn_lay,hospitalname_list));
        listPopupWindow.setAnchorView(prfilenme);
        listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Hospital_ID=hospitals_list.get(position).get("hospital_id");
                prfilenme.setText(hospitalname_list.get(position));
                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
    }


    private void AddAppointmentService(final FragmentActivity activity, final String  mobile) {
        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.DocSearchPatient(RetrofitInstance.doc_search_patient,Hospital_ID,mobile,StoredObjects.UserId,StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseRecieved = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseRecieved);
                        StoredObjects.LogMethod("response", "response::" + responseRecieved);
                        String status = jsonObject.getString("status");


                        if (status.equalsIgnoreCase("200")) {
                            p_data_list.clear();
                            String results = jsonObject.getString("results");
                            p_data_list = JsonParsing.GetJsonData(results);
                            fragmentcallinglay(new Doc_Existed_Appointment());
                        } else {
                            StoredObjects.p_mobilenum=mobile;
                            p_data_list.clear();
                            fragmentcallinglay(new Doc_Add_Appointment());
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
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
    }

    public void fragmentcallinglay1(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }
}


