package com.rxmediaapp.Sidemenu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rxmediaapp.R;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.database.Database;
import com.rxmediaapp.fragments.assistant.Add_Appointment;
import com.rxmediaapp.fragments.assistant.Ass_AppointmentList;
import com.rxmediaapp.fragments.assistant.Ass_PatientList;
import com.rxmediaapp.fragments.assistant.AssistantProfile;
import com.rxmediaapp.fragments.dashboards.Asst_Dashboard;
import com.rxmediaapp.fragments.dashboards.Doc_Dashboard;
import com.rxmediaapp.fragments.dashboards.Franchisee_Dashboard;
import com.rxmediaapp.fragments.dashboards.H_Dashboard;
import com.rxmediaapp.fragments.dashboards.Marketing_Dashboard;
import com.rxmediaapp.fragments.dashboards.P_Dashboard;
import com.rxmediaapp.fragments.dashboards.SubFranchisee_Dashboard;
import com.rxmediaapp.fragments.dashboards.TL_Dashboard;
import com.rxmediaapp.fragments.doctor.DocTotalPrescriptionsList;
import com.rxmediaapp.fragments.doctor.Doc_Add_Appointment;
import com.rxmediaapp.fragments.doctor.Doc_Appointmentslist;
import com.rxmediaapp.fragments.doctor.Doc_Assistant;
import com.rxmediaapp.fragments.doctor.Doc_Patient_List;
import com.rxmediaapp.fragments.doctor.Doc_Patientlistmain;
import com.rxmediaapp.fragments.doctor.Doc_PhysicalSuggestions;
import com.rxmediaapp.fragments.doctor.Doc_Sel_Patient;
import com.rxmediaapp.fragments.doctor.Doc_TestSuggested;
import com.rxmediaapp.fragments.doctor.Doctor_Details;
import com.rxmediaapp.fragments.doctor.OtherDoctorPrescriptions;
import com.rxmediaapp.fragments.doctor.Print_Formate;
import com.rxmediaapp.fragments.hospital.H_TotalPatientList;

import com.rxmediaapp.fragments.patient.P_Prescriptionslist;
import com.rxmediaapp.fragments.teamleader.Change_Password;
import com.rxmediaapp.fragments.teamleader.Common_Profile;

import com.rxmediaapp.fragments.teamleader.Hospital_DoctorLeads;
import com.rxmediaapp.fragments.teamleader.TL_Patients;
import com.rxmediaapp.fragments.teamleader.Marketing_Exicutive;
import com.rxmediaapp.fragments.teamleader.TL_Doctors;
import com.rxmediaapp.fragments.teamleader.TL_Franchisee_List;
import com.rxmediaapp.fragments.teamleader.TL_Hospitals;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;
import com.rxmediaapp.activities.SignIn;
import com.rxmediaapp.fragments.patient.Add_Patient_Profile;
import com.rxmediaapp.fragments.patient.Block_Doctor;
import com.rxmediaapp.fragments.hospital.H_Assistant;
import com.rxmediaapp.fragments.hospital.H_Test_Suggested;

import com.rxmediaapp.fragments.doctor.Doc_Profile;
import com.rxmediaapp.fragments.hospital.H_DoctorsList;
import com.rxmediaapp.fragments.hospital.H_PatientList_Main;
import com.rxmediaapp.fragments.hospital.H_Profile;
import com.rxmediaapp.fragments.hospital.H_TotalPrescriptions;
import com.rxmediaapp.fragments.patient.P_Diagnosis_Reports;
import com.rxmediaapp.fragments.patient.P_Sub_Member;
import com.rxmediaapp.fragments.patient.P_Test_Sugestions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SideMenu extends AppCompatActivity {

    //actionbarlayout
    public static ImageView custm_menu_img, custm_backbtn_img, custm_search_img;
    public static TextView custm_title_txt;
    public static LinearLayout actionbar_lay, bottom_layout, custom_new_actionbar_lay,top_lay;
    ImageView sidemnu_cancel_img;
    public static TextView header_name;
    public static ImageView header_circular_img;

    public static DrawerLayout mDrawerLayout;
    public static ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    String[] navMenuTitles;
    private TypedArray navMenuIconsleft;
    private ArrayList<NavDrawerItem> navDrawerItems;
    public static NavDrawerListAdapter adapter;
    public static int drawablecount = 0;
    public static LinearLayout list_slidermenu_lay;

    public static LinearLayout btm_log_catch_lay, btm_map_lay, btm_notifications_lay, btm_profile_lay;
    public static ImageView btm_log_catch_img, btm_map_img, btm_notifications_img, btm_profile_img;

    public static LinearLayout btm_notifications_lay_bg;
    Database database;

    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> hospitals_list = new ArrayList<>();
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidemenu);
        database=new Database(SideMenu.this);
        database.getAllDevice();
        initialization();
    }

    public void initialization() {

        list_slidermenu_lay = (LinearLayout) findViewById(R.id.list_slidermenu_lay);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        if( StoredObjects.UserType.equalsIgnoreCase("Doctors")){

            navMenuTitles = getResources().getStringArray(R.array.doctor_list);
        }else if(StoredObjects.UserType.equalsIgnoreCase("Patients")){
            navMenuTitles = getResources().getStringArray(R.array.patient_list);
        }else if(StoredObjects.UserType.equalsIgnoreCase("Hospitals")){
            navMenuTitles = getResources().getStringArray(R.array.hospital_list);
        }else if(StoredObjects.UserType.equalsIgnoreCase("Assistant")){
            navMenuTitles = getResources().getStringArray(R.array.assistant_list);
        }else if(StoredObjects.UserType.equalsIgnoreCase("Team Leader")){
            navMenuTitles = getResources().getStringArray(R.array.teamleader_list);
        }else if(StoredObjects.UserType.equalsIgnoreCase("Marketing Executive")){
            navMenuTitles = getResources().getStringArray(R.array.marketexc_list);
        }else if(StoredObjects.UserType.equalsIgnoreCase("Franchisee")){
            navMenuTitles = getResources().getStringArray(R.array.mainfranchise_list);
        }else if(StoredObjects.UserType.equalsIgnoreCase("Sub Franchisee")){
            navMenuTitles = getResources().getStringArray(R.array.subfranchise_list);
        }




        for (int i = 0; i < navMenuTitles.length; i++) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[i]));

        }


        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        adapter = new NavDrawerListAdapter(SideMenu.this, navDrawerItems);
        mDrawerList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        top_lay  = (LinearLayout) findViewById(R.id.top_lay);
         custm_menu_img = (ImageView)findViewById(R.id.custm_menu_img);
         custm_backbtn_img = (ImageView)findViewById(R.id.custm_backbtn_img);
         custm_title_txt = (TextView) findViewById(R.id.custm_title_txt);
         custm_search_img = (ImageView)findViewById( R.id.custm_search_img );
        actionbar_lay = (LinearLayout) findViewById(R.id.actionbar_lay);
        bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);

        sidemnu_cancel_img = (ImageView) findViewById(R.id.sidemnu_cancel_img);
        header_name = findViewById(R.id.header_name);
        header_circular_img =  findViewById(R.id.header_circular_img);


        btm_log_catch_lay = (LinearLayout) findViewById(R.id.btm_log_catch_lay);
        btm_map_lay = (LinearLayout) findViewById(R.id.btm_map_lay);
        btm_notifications_lay = (LinearLayout) findViewById(R.id.btm_notifications_lay);
        btm_profile_lay = (LinearLayout) findViewById(R.id.btm_profile_lay);

        btm_log_catch_img = (ImageView) findViewById(R.id.btm_log_catch_img);
        btm_map_img = (ImageView) findViewById(R.id.btm_map_img);
        btm_notifications_img = (ImageView) findViewById(R.id.btm_notifications_img);
        btm_profile_img = (ImageView) findViewById(R.id.btm_profile_img);

        btm_notifications_lay_bg = findViewById(R.id.btm_notifications_lay_bg);

        sidemnu_cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawerLayout.closeDrawer(list_slidermenu_lay);
            }
        });


        custm_menu_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                drawablecount++;
                if (drawablecount == 1) {
                    mDrawerLayout.openDrawer(list_slidermenu_lay);
                } else {
                    drawablecount = 0;
                    mDrawerLayout.closeDrawer(list_slidermenu_lay);
                }
            }

        });


        custm_backbtn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backbuttonclickevents();
            }
        });

        if (InterNetChecker.isNetworkAvailable(SideMenu.this)) {
            getProfileService(SideMenu.this);
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), SideMenu.this);
        }

        buttonchangemethod(SideMenu.this, btm_log_catch_lay, btm_log_catch_img, "0");


        buttonchangelaymethod(SideMenu.this, btm_log_catch_lay, btm_log_catch_img, "0");
        buttonchangelaymethod(SideMenu.this, btm_map_lay, btm_map_img, "1");
        buttonchangelaymethod(SideMenu.this, btm_notifications_lay, btm_notifications_img, "2");
        buttonchangelaymethod(SideMenu.this, btm_profile_lay, btm_profile_img, "3");
    }

    private void getProfileService(final Activity activity) {
        CustomProgressbar.Progressbarshow(activity);
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);

        if( StoredObjects.UserType.equalsIgnoreCase("Doctors")){

            api.getDrProfile(RetrofitInstance.doctor_profile, StoredObjects.Logged_DoctorId, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.body() != null) {
                        try {
                            String responseReceived = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseReceived);
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                String results = jsonObject.getString("results");
                                data_list = JsonParsing.GetJsonData(results);
                                hospitals_list.clear();
                                hospitals_list = JsonParsing.GetJsonData(data_list.get(0).get("attached_hospital"));
                                header_name.setText(data_list.get(0).get("name"));

                                try {
                                    Glide.with(activity)
                                            .load(Uri.parse(RetrofitInstance.BASEURL + data_list.get(0).get("image")))
                                            .apply(new RequestOptions()
                                                    .placeholder(R.drawable.no_image)
                                                    .fitCenter()
                                                    .centerCrop())
                                            .into(header_circular_img);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                callmethod();
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
        }else if(StoredObjects.UserType.equalsIgnoreCase("Patients")){

            api.getPatientProfile(RetrofitInstance.patient_profile, StoredObjects.Logged_PatientId, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
                                data_list = JsonParsing.GetJsonData(results);
                                header_name.setText(data_list.get(0).get("name"));

                                try {
                                    Glide.with(activity)
                                            .load(Uri.parse(RetrofitInstance.BASEURL + data_list.get(0).get("image")))
                                            .apply(new RequestOptions()
                                                    .placeholder(R.drawable.no_image)
                                                    .fitCenter()
                                                    .centerCrop())
                                            .into(header_circular_img);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                callmethod();
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
        }else if(StoredObjects.UserType.equalsIgnoreCase("Hospitals")){

            api.gethospitalProfile(RetrofitInstance.hospital_profile, StoredObjects.Logged_HospitalId, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
                                data_list = JsonParsing.GetJsonData(results);
                                header_name.setText(data_list.get(0).get("name"));

                                try {
                                    Glide.with(activity)
                                            .load(Uri.parse(RetrofitInstance.BASEURL + data_list.get(0).get("image")))
                                            .apply(new RequestOptions()
                                                    .placeholder(R.drawable.no_image)
                                                    .fitCenter()
                                                    .centerCrop())
                                            .into(header_circular_img);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                callmethod();
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

        }else if(StoredObjects.UserType.equalsIgnoreCase("Assistant")){
            api.assistantProfile(RetrofitInstance.assistant_profile, StoredObjects.Logged_AssistantId, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
                                data_list = JsonParsing.GetJsonData(results);
                                header_name.setText(data_list.get(0).get("name"));

                                try {
                                    Glide.with(activity)
                                            .load(Uri.parse(RetrofitInstance.BASEURL + data_list.get(0).get("image")))
                                            .apply(new RequestOptions()
                                                    .placeholder(R.drawable.no_image)
                                                    .fitCenter()
                                                    .centerCrop())
                                            .into(header_circular_img);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                callmethod();
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
        }else if(StoredObjects.UserType.equalsIgnoreCase("Team Leader")
        ||StoredObjects.UserType.equalsIgnoreCase("Marketing Executive")
        ||StoredObjects.UserType.equalsIgnoreCase("Franchisee")
        ||StoredObjects.UserType.equalsIgnoreCase("Sub Franchisee")){
            api.TL_GetProfile(RetrofitInstance.get_profile, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
                                data_list = JsonParsing.GetJsonData(results);
                                header_name.setText(data_list.get(0).get("name"));

                                try {
                                    Glide.with(activity)
                                            .load(Uri.parse(RetrofitInstance.BASEURL + data_list.get(0).get("image")))
                                            .apply(new RequestOptions()
                                                    .placeholder(R.drawable.no_image)
                                                    .fitCenter()
                                                    .centerCrop())
                                            .into(header_circular_img);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                callmethod();
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

    private void callmethod() {
        if( StoredObjects.UserType.equalsIgnoreCase("Doctors")){

            custm_title_txt.setText("Home");
            fragmentcallinglay(new Doc_Dashboard());

        }else if(StoredObjects.UserType.equalsIgnoreCase("Patients")){

            custm_title_txt.setText("Home");
            fragmentcallinglay(new P_Dashboard());

        }else if(StoredObjects.UserType.equalsIgnoreCase("Hospitals")){

            custm_title_txt.setText("Home");
            fragmentcallinglay(new H_Dashboard());

        }else if(StoredObjects.UserType.equalsIgnoreCase("Assistant")){

            custm_title_txt.setText("Home");
            fragmentcallinglay(new Asst_Dashboard());
        }else if(StoredObjects.UserType.equalsIgnoreCase("Team Leader")){

            custm_title_txt.setText("Home");
            fragmentcallinglay(new TL_Dashboard());
        }else if(StoredObjects.UserType.equalsIgnoreCase("Marketing Executive")){

            custm_title_txt.setText("Home");
            fragmentcallinglay(new Marketing_Dashboard());
        }else if(StoredObjects.UserType.equalsIgnoreCase("Franchisee")){

            custm_title_txt.setText("Home");
            fragmentcallinglay(new Franchisee_Dashboard());
        }else if(StoredObjects.UserType.equalsIgnoreCase("Sub Franchisee")){

            custm_title_txt.setText("Home");
            fragmentcallinglay(new SubFranchisee_Dashboard());
        }

    }

    public void buttonchangelaymethod(final Activity activity, final LinearLayout layout1, final ImageView image,  final String type) {

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonchangemethod(activity, layout1, image,  type);
/*
                if (type.equalsIgnoreCase("0")) {
                    fragmentcallinglay(new Home());
                } else if (type.equalsIgnoreCase("1")) {
                    fragmentcallinglay(new AddPatient());
                } else if (type.equalsIgnoreCase("2")) {
                    fragmentcallinglay(new Print_Formate());
                } else if (type.equalsIgnoreCase("3")) {
                fragmentcallinglay(new Diagnosis_Reports_Details());
                }*/
            }
        });
    }

    public static void buttonchangemethod(Activity activity, LinearLayout layout1, ImageView image,   String type) {

        btm_log_catch_lay.setBackgroundColor(activity.getResources().getColor(R.color.white));
        btm_map_lay.setBackgroundColor(activity.getResources().getColor(R.color.white));
        btm_notifications_lay.setBackgroundColor(activity.getResources().getColor(R.color.white));
        btm_profile_lay.setBackgroundColor(activity.getResources().getColor(R.color.white));

        btm_log_catch_img.setColorFilter(ContextCompat.getColor(activity, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        btm_map_img.setColorFilter(ContextCompat.getColor(activity, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        btm_notifications_img.setColorFilter(ContextCompat.getColor(activity, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        btm_profile_img.setColorFilter(ContextCompat.getColor(activity, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);

        image.setColorFilter(ContextCompat.getColor(activity, R.color.theme_color), android.graphics.PorterDuff.Mode.SRC_IN);

    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
            displayView(position);
            adapter.notifyDataSetChanged();
        }
    }

    public void displayView(int position) {


        Fragment fragment = null;
        StoredObjects.listcount = position;
        if( StoredObjects.UserType.equalsIgnoreCase("Doctors")){
            switch (position) {
                case 0:
                    fragment  = new Doc_Dashboard();
                    break;
                case 1:
                    fragment  = new Doc_Assistant();
                    //Diagnosepopup(SideMenu.this);
                    break;
                case 2:
                    if(hospitals_list.size()==0||hospitals_list.size()==1){
                        if(hospitals_list.size()==1){
                            StoredObjects.Doc_Hospital_Id=hospitals_list.get(0).get("hospital_id");
                        }

                        fragment  = new Doc_Appointmentslist();
                    }else{
                        StoredObjects.tab_type="appointments";
                        fragment  = new Doc_Patientlistmain();
                    }

                    // medicationpopup (SideMenu.this);
                    break;

                case 3:
                    fragment  = new Doc_Sel_Patient();
                    //Diagnosepopup(SideMenu.this);
                    break;

                case 4:
                    if(hospitals_list.size()==0||hospitals_list.size()==1){
                        if(hospitals_list.size()==1){
                            StoredObjects.Doc_Hospital_Id=hospitals_list.get(0).get("hospital_id");
                        }

                        fragment  = new Doc_Patient_List();
                    }else{
                        StoredObjects.tab_type="patients";
                        fragment  = new Doc_Patientlistmain();
                    }

                   // medicationpopup (SideMenu.this);
                    break;
                case 5:


                    if(hospitals_list.size()==0||hospitals_list.size()==1){
                        if(hospitals_list.size()==1){
                            StoredObjects.Doc_Hospital_Id=hospitals_list.get(0).get("hospital_id");
                        }
                        fragment  = new DocTotalPrescriptionsList();
                    }else{
                        StoredObjects.tab_type="prescr";
                        fragment  = new Doc_Patientlistmain();
                    }

                    break;

                case 6:
                    if(hospitals_list.size()==0||hospitals_list.size()==1){
                        if(hospitals_list.size()==1){
                            StoredObjects.Doc_Hospital_Id=hospitals_list.get(0).get("hospital_id");
                        }
                        fragment  = new Doc_TestSuggested();
                    }else{
                        StoredObjects.tab_type="testsugsted";
                        fragment  = new Doc_Patientlistmain();
                    }

                    break;

                case 7:
                    fragment  = new Doc_PhysicalSuggestions();
                    break;
                case 8:
                    fragment  = new Print_Formate();
                    break;

                case 9:
                    fragment  = new Doc_Profile();
                    break;
                case 10:
                    fragment  = new Change_Password();
                    break;

                case 11:
                    Logoutpopup(SideMenu.this,"1");
                    break;

            }
        }else if(StoredObjects.UserType.equalsIgnoreCase("Patients")){
            switch (position) {

                case 0:
                    fragment  = new P_Dashboard();
                    break;
                case 1:
                    fragment  = new P_Sub_Member();
                    break;
                case 2:
                    fragment  = new P_Prescriptionslist();
                    break;
                case 3:
                    fragment  = new P_Diagnosis_Reports();
                    break;
                case 4:
                    fragment  = new P_Test_Sugestions();
                    break;

                case 5:
                    fragment  = new Block_Doctor();
                    break;
                case 6:
                    fragment  = new Add_Patient_Profile();
                    break;
                case 7:
                    fragment  = new Change_Password();
                    break;
                case 8:

                    Logoutpopup(SideMenu.this,"1");
                    break;

            }
        }else if(StoredObjects.UserType.equalsIgnoreCase("Hospitals")){
            switch (position) {
                case 0:
                    fragment  = new H_Dashboard();
                    break;
                case 1:
                    fragment  = new H_DoctorsList();
                    break;
                case 2:
                    fragment  = new H_Assistant();
                    break;
                case 3:
                    fragment  = new H_PatientList_Main();

                    break;
                case 4:
                    fragment  = new H_TotalPatientList();
                    break;

                case 5:
                    StoredObjects.redirect_type="menu";
                    fragment  = new H_TotalPrescriptions();
                    break;

                case 6:
                    StoredObjects.redirect_type="menu";
                    fragment  = new H_Test_Suggested();
                    break;
                case 7:
                    fragment  = new H_Profile();
                    break;
                case 8:
                    fragment  = new Change_Password();
                    break;
                case 9:

                    Logoutpopup(SideMenu.this,"1");
                    break;

            }
        }else if(StoredObjects.UserType.equalsIgnoreCase("Assistant")){
            switch (position) {

                case 0:
                    fragment  = new Asst_Dashboard();
                    break;
                case 1:
                    fragment  = new Add_Appointment();
                    break;
                case 2:
                    fragment  = new Ass_AppointmentList();
                    break;
                case 3:
                    fragment  = new Ass_PatientList();
                    break;
                case 4:
                    fragment  = new AssistantProfile();
                    break;
                case 5:
                    fragment  = new Change_Password();
                    break;
                case 6:

                    Logoutpopup(SideMenu.this,"1");
                    break;

            }
       }
        else if(StoredObjects.UserType.equalsIgnoreCase("Team Leader")){
            switch (position) {

                case 0:
                    fragment  = new TL_Dashboard();
                    break;
                case 1:
                    fragment  = new Marketing_Exicutive();
                    break;
                case 2:
                    fragment  = new TL_Franchisee_List();
                    break;
                case 3:
                    fragment  = new TL_Hospitals();
                    break;
                case 4:
                    fragment  = new TL_Doctors();
                    break;
                case 5:
                    fragment  = new TL_Patients();
                    break;
                case 6:
                    fragment  = new Hospital_DoctorLeads();
                    break;
                case 7:
                    fragment  = new Common_Profile();
                    break;
                case 8:
                    fragment  = new Change_Password();
                    break;
                case 9:

                    Logoutpopup(SideMenu.this,"1");
                    break;

            }

        }else if(StoredObjects.UserType.equalsIgnoreCase("Marketing Executive")){
            switch (position) {

                case 0:
                    fragment  = new Marketing_Dashboard();
                    break;
                case 1:
                    fragment  = new TL_Franchisee_List();
                    break;
                case 2:
                    fragment  = new TL_Hospitals();
                    break;
                case 3:
                    fragment  = new TL_Doctors();
                    break;
                case 4:
                    fragment  = new TL_Patients();
                    break;
                case 5:
                    fragment  = new Hospital_DoctorLeads();
                    break;
                case 6:
                    fragment  = new Common_Profile();
                    break;
                case 7:
                    fragment  = new Change_Password();
                    break;
                case 8:

                    Logoutpopup(SideMenu.this,"1");
                    break;

            }

        }else if(StoredObjects.UserType.equalsIgnoreCase("Franchisee")){
            switch (position) {
                case 0:
                    fragment  = new Franchisee_Dashboard();
                    break;
                case 1:
                    fragment  = new TL_Franchisee_List();
                    break;
                case 2:
                    fragment  = new TL_Hospitals();
                    break;
                case 3:
                    fragment  = new TL_Doctors();
                    break;
                case 4:
                    fragment  = new TL_Patients();
                    break;
                case 5:
                    fragment  = new Hospital_DoctorLeads();
                    break;
                case 6:
                    fragment  = new Common_Profile();
                    break;
                case 7:
                    fragment  = new Change_Password();
                    break;
                case 8:

                    Logoutpopup(SideMenu.this,"1");
                    break;

            }



        }else if(StoredObjects.UserType.equalsIgnoreCase("Sub Franchisee")){
            switch (position) {
                case 0:
                    fragment  = new SubFranchisee_Dashboard();
                    break;
                case 1:
                    fragment  = new TL_Hospitals();
                    break;
                case 2:
                    fragment  = new TL_Doctors();
                    break;
                case 3:
                    fragment  = new TL_Patients();
                    break;
                case 4:
                    fragment  = new Hospital_DoctorLeads();
                    break;
                case 5:
                    fragment  = new Common_Profile();
                    break;
                case 6:
                    fragment  = new Change_Password();
                    break;
                case 7:

                    Logoutpopup(SideMenu.this,"1");
                    break;

            }

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerLayout.closeDrawer(list_slidermenu_lay);

        } else {
            // error in creating fragment
            Log.e("Sidemenu", "Error in creating fragment");
        }
    }

    boolean doubleBackToExitPressedOnce = false;
    public void backbuttonclickevents() {

        if(!StoredObjects.page_type.equalsIgnoreCase("home")){

            if( StoredObjects.UserType.equalsIgnoreCase("Doctors")){
                if( StoredObjects.page_type.equalsIgnoreCase("patients_main")
                  ||StoredObjects.page_type.equalsIgnoreCase("phy_suggestion")
                        ||StoredObjects.page_type.equalsIgnoreCase("print_formate")
                        ||StoredObjects.page_type.equalsIgnoreCase("doc_prfle")
                        ||StoredObjects.page_type.equalsIgnoreCase("change_password")
                ||StoredObjects.page_type.equalsIgnoreCase("otherdoctorprescriptions")
                ||StoredObjects.page_type.equalsIgnoreCase("sel_patient")
            ||StoredObjects.page_type.equalsIgnoreCase("d_assistant")){//

                    fragmentcallinglay(new Doc_Dashboard());

                }else{
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    }
                }
            }else if( StoredObjects.UserType.equalsIgnoreCase("Patients")){

                if( StoredObjects.page_type.equalsIgnoreCase("p_preslist")
                        ||StoredObjects.page_type.equalsIgnoreCase("p_diagnosis_reports")
                        ||StoredObjects.page_type.equalsIgnoreCase("p_test_sugest")
                        ||StoredObjects.page_type.equalsIgnoreCase("block_doctor")
                ||StoredObjects.page_type.equalsIgnoreCase("add_patient_profile")||
                        StoredObjects.page_type.equalsIgnoreCase("submembers")

                        ||StoredObjects.page_type.equalsIgnoreCase("change_password")){


                    fragmentcallinglay(new P_Dashboard());

                }else{

                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    }
                }
            }else if( StoredObjects.UserType.equalsIgnoreCase("Hospitals")){
                if( StoredObjects.page_type.equalsIgnoreCase("h_assistant")
                        ||StoredObjects.page_type.equalsIgnoreCase("h_patientmain")
                        ||StoredObjects.page_type.equalsIgnoreCase("total_patients")
                        ||StoredObjects.page_type.equalsIgnoreCase("h_prfile")
                        ||StoredObjects.page_type.equalsIgnoreCase("change_password")
                        ||StoredObjects.page_type.equalsIgnoreCase("h_doctors")){


                    fragmentcallinglay(new H_Dashboard());

                }else  if(StoredObjects.page_type.equalsIgnoreCase("total_prescr")
                        ||StoredObjects.page_type.equalsIgnoreCase("h_test_suggested")){

                    if(StoredObjects.redirect_type.equalsIgnoreCase("doctor")){
                        fragmentcallinglay(new Doctor_Details());
                    }else{
                        fragmentcallinglay(new H_DoctorsList());
                    }


                }else  if(StoredObjects.page_type.equalsIgnoreCase("total_prescr")){

                    if(StoredObjects.redirect_type.equalsIgnoreCase("doctor")){
                        fragmentcallinglay(new Doctor_Details());
                    }else{
                        fragmentcallinglay(new H_DoctorsList());
                    }




                }else {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    }
                }
            }else if( StoredObjects.UserType.equalsIgnoreCase("Assistant")){
                if( StoredObjects.page_type.equalsIgnoreCase("ass_patientlist")
            || StoredObjects.page_type.equalsIgnoreCase("add_apmnt")

                        ||StoredObjects.page_type.equalsIgnoreCase("ass_profile")
                        ||StoredObjects.page_type.equalsIgnoreCase("change_password")
                        ||StoredObjects.page_type.equalsIgnoreCase("ass_appointmentlist")){


                    fragmentcallinglay(new Asst_Dashboard());

                }else{
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    }
                }
            }else if( StoredObjects.UserType.equalsIgnoreCase("Team Leader")){
                if( StoredObjects.page_type.equalsIgnoreCase("tl_franchises")
                        ||StoredObjects.page_type.equalsIgnoreCase("tl_hospitals")
                        ||StoredObjects.page_type.equalsIgnoreCase("tl_doctors")
                        ||StoredObjects.page_type.equalsIgnoreCase("tl_patients")
                        ||StoredObjects.page_type.equalsIgnoreCase("hospital_leads")
                        ||StoredObjects.page_type.equalsIgnoreCase("profile")
                        ||StoredObjects.page_type.equalsIgnoreCase("marketing")
                        ||StoredObjects.page_type.equalsIgnoreCase("change_password")
                ||StoredObjects.page_type.equalsIgnoreCase("tl_franchises")){


                    fragmentcallinglay(new TL_Dashboard());

                }else{
                    if(StoredObjects.page_type.equalsIgnoreCase("f_hospital_five")){

                        if(StoredObjects.redirect_type.equalsIgnoreCase("inner")){
                            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                                getSupportFragmentManager().popBackStack();
                            }
                        }else{
                            fragmentcallinglay(new TL_Dashboard());
                        }
                    }else{
                        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStack();
                        }
                    }
                }
            }else if( StoredObjects.UserType.equalsIgnoreCase("Marketing Executive")){
                if( StoredObjects.page_type.equalsIgnoreCase("tl_franchises")
                        ||StoredObjects.page_type.equalsIgnoreCase("tl_hospitals")
                        ||StoredObjects.page_type.equalsIgnoreCase("tl_doctors")
                        ||StoredObjects.page_type.equalsIgnoreCase("tl_patients")
                        ||StoredObjects.page_type.equalsIgnoreCase("hospital_leads")
                        ||StoredObjects.page_type.equalsIgnoreCase("profile")
                        ||StoredObjects.page_type.equalsIgnoreCase("change_password")
                        ||StoredObjects.page_type.equalsIgnoreCase("tl_franchises")){


                    fragmentcallinglay(new Marketing_Dashboard());

                }else{
                    if(StoredObjects.page_type.equalsIgnoreCase("f_hospital_five")){

                        if(StoredObjects.redirect_type.equalsIgnoreCase("inner")){
                            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                                getSupportFragmentManager().popBackStack();
                            }
                        }else{
                            fragmentcallinglay(new Marketing_Dashboard());
                        }
                    }else{
                        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStack();
                        }
                    }
                }
            }else if( StoredObjects.UserType.equalsIgnoreCase("Franchisee")){

                if( StoredObjects.page_type.equalsIgnoreCase("tl_franchises")
                        ||StoredObjects.page_type.equalsIgnoreCase("tl_hospitals")
                        ||StoredObjects.page_type.equalsIgnoreCase("tl_doctors")
                        ||StoredObjects.page_type.equalsIgnoreCase("tl_patients")
                        ||StoredObjects.page_type.equalsIgnoreCase("hospital_leads")
                        ||StoredObjects.page_type.equalsIgnoreCase("profile")
                        ||StoredObjects.page_type.equalsIgnoreCase("change_password")
                        ||StoredObjects.page_type.equalsIgnoreCase("tl_franchises")){


                    fragmentcallinglay(new Franchisee_Dashboard());

                }else{
                    if(StoredObjects.page_type.equalsIgnoreCase("f_hospital_five")){

                        if(StoredObjects.redirect_type.equalsIgnoreCase("inner")){
                            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                                getSupportFragmentManager().popBackStack();
                            }
                        }else{
                            fragmentcallinglay(new Franchisee_Dashboard());
                        }
                    }else{
                        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStack();
                        }
                    }
                }
            }else if( StoredObjects.UserType.equalsIgnoreCase("Sub Franchisee")){
                if( StoredObjects.page_type.equalsIgnoreCase("tl_franchises")
                        ||StoredObjects.page_type.equalsIgnoreCase("tl_hospitals")
                        ||StoredObjects.page_type.equalsIgnoreCase("tl_doctors")
                        ||StoredObjects.page_type.equalsIgnoreCase("tl_patients")
                        ||StoredObjects.page_type.equalsIgnoreCase("hospital_leads")
                        ||StoredObjects.page_type.equalsIgnoreCase("profile")
                        ||StoredObjects.page_type.equalsIgnoreCase("change_password")){


                    fragmentcallinglay(new SubFranchisee_Dashboard());

                }else{
                    if(StoredObjects.page_type.equalsIgnoreCase("f_hospital_five")){

                        if(StoredObjects.redirect_type.equalsIgnoreCase("inner")){
                            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                                getSupportFragmentManager().popBackStack();
                            }
                        }else{
                            fragmentcallinglay(new SubFranchisee_Dashboard());
                        }
                    }else{
                        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStack();
                        }
                    }

                }
            }



            else {
                super.onBackPressed();
            }
        }else{
            checkbackclick();
        }

    }

    public void checkbackclick () {

            if (doubleBackToExitPressedOnce) {
                // super.onBackPressed();
                Logoutpopup(SideMenu.this,"0");
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 3000);
        }



        public void minimizeApp () {
            finish();
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }

        public void onBackPressed () {
             backbuttonclickevents();
        }

        public void fragmentcallinglay (Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        }

        public static void updatemenu (String pagetype){

            if (pagetype.equalsIgnoreCase("home")) {
                top_lay.setVisibility( View.VISIBLE );
                try {
                    StoredObjects.listcount=0;
                    adapter.notifyDataSetChanged();


                }catch (Exception e){

                }
            } else {
                top_lay.setVisibility( View.GONE );
            }

        }

        private void Logoutpopup(final Activity activity, final String type) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logooutpopup );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);

        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
        Button ok_btn = (Button)dialog.findViewById(R.id.ok_btn);
        Button cancel_btn = (Button)dialog.findViewById(R.id.cancel_btn);
        ImageView canclimg = (ImageView)dialog.findViewById(R.id.canclimg);
        TextView logout_txt = (TextView)dialog.findViewById( R.id.logout_txt );
        TextView exit_txt = (TextView)dialog.findViewById( R.id.exit_txt );

        if (type.equals( "1" )){
            logout_txt.setVisibility( View.VISIBLE );
        }
        else {
            exit_txt.setVisibility( View.VISIBLE );
            logout_txt.setVisibility( View.GONE );
        }

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



            ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type.equals( "1" )) {
                        database.UpdateUserdata( "user_id","0" );
                        activity.finish();
                        Intent intent = new Intent( activity, SignIn.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        activity.startActivity( intent );
                    } else {
                        minimizeApp();
                    }
                }
            });


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }



}
