package com.rxmediaapp.fragments.teamleader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.rxmediaapp.fragments.assistant.Add_Appointment;
import com.rxmediaapp.fragments.dashboards.Asst_Dashboard;
import com.rxmediaapp.fragments.dashboards.Doc_Dashboard;
import com.rxmediaapp.fragments.dashboards.Franchisee_Dashboard;
import com.rxmediaapp.fragments.dashboards.H_Dashboard;
import com.rxmediaapp.fragments.dashboards.Marketing_Dashboard;
import com.rxmediaapp.fragments.dashboards.P_Dashboard;
import com.rxmediaapp.fragments.dashboards.SubFranchisee_Dashboard;
import com.rxmediaapp.fragments.dashboards.TL_Dashboard;
import com.rxmediaapp.fragments.doctor.Doc_Assistant;
import com.rxmediaapp.fragments.hospital.H_DoctorsList;
import com.rxmediaapp.fragments.patient.P_Sub_Member;
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

public class Change_Password extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    EditText new_passwd_edtxt,confirm_passwd_edtxt;
    Button chg_submit_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.change_password,null,false );
        StoredObjects.page_type="change_password";

        SideMenu.updatemenu(StoredObjects.page_type);
        try {
            if (StoredObjects.UserType.equalsIgnoreCase("Doctors")) {
                StoredObjects.listcount= 10;
                SideMenu.adapter.notifyDataSetChanged();
            }else if(StoredObjects.UserType.equalsIgnoreCase("Patients")){
                StoredObjects.listcount= 7;
                SideMenu.adapter.notifyDataSetChanged();
            }else if(StoredObjects.UserType.equalsIgnoreCase("Hospitals")){
                StoredObjects.listcount= 8;
                SideMenu.adapter.notifyDataSetChanged();
            }else if(StoredObjects.UserType.equalsIgnoreCase("Assistant")){
                StoredObjects.listcount= 5;
                SideMenu.adapter.notifyDataSetChanged();
            }else if(StoredObjects.UserType.equalsIgnoreCase("Team Leader")){
                StoredObjects.listcount= 8;
                SideMenu.adapter.notifyDataSetChanged();
            }else if(StoredObjects.UserType.equalsIgnoreCase("Marketing Executive")){
                StoredObjects.listcount= 7;
                SideMenu.adapter.notifyDataSetChanged();
            }else if(StoredObjects.UserType.equalsIgnoreCase("Franchisee")){
                StoredObjects.listcount= 7;
                SideMenu.adapter.notifyDataSetChanged();
            }else if(StoredObjects.UserType.equalsIgnoreCase("Sub Franchisee")){
                StoredObjects.listcount= 6;
                SideMenu.adapter.notifyDataSetChanged();
            }else{

            }
        }catch (Exception e){

        }

        initilization(v);
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById(R.id.backbtn_img);
        title_txt = v.findViewById(R.id.title_txt);
        new_passwd_edtxt = v.findViewById(R.id.new_passwd_edtxt);
        confirm_passwd_edtxt = v.findViewById(R.id.confirm_passwd_edtxt);
        chg_submit_btn = v.findViewById(R.id.chg_submit_btn);
        title_txt.setText("Change Password");


        backbtn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( StoredObjects.UserType.equalsIgnoreCase("Doctors")){
                    fragmentcallinglay(new Doc_Dashboard());
                }else  if( StoredObjects.UserType.equalsIgnoreCase("Patients")){
                    fragmentcallinglay(new P_Dashboard());
                }else  if( StoredObjects.UserType.equalsIgnoreCase("Hospitals")){
                    fragmentcallinglay(new H_Dashboard());
                }else  if( StoredObjects.UserType.equalsIgnoreCase("Assistant")){
                    fragmentcallinglay(new Asst_Dashboard());
                }else  if( StoredObjects.UserType.equalsIgnoreCase("Marketing Executive")){
                    fragmentcallinglay(new Marketing_Dashboard());
                }else  if( StoredObjects.UserType.equalsIgnoreCase("Franchisee")){
                    fragmentcallinglay(new Franchisee_Dashboard());
                }else  if( StoredObjects.UserType.equalsIgnoreCase("Sub Franchisee")){
                    fragmentcallinglay(new SubFranchisee_Dashboard());
                }else if( StoredObjects.UserType.equalsIgnoreCase("Team Leader")){
                    fragmentcallinglay(new TL_Dashboard());
                }

            }
        });

        chg_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String new_pswd_str = new_passwd_edtxt.getText().toString().trim();
                String cnfmpswd_str = confirm_passwd_edtxt.getText().toString().trim();

                if(StoredObjects.inputValidation(new_passwd_edtxt,getString(R.string.enter_newpass_validation),getActivity())) {
                    if(StoredObjects.inputValidation(confirm_passwd_edtxt,getString(R.string.enter_confirm_pass_validation),getActivity())) {
                        if (new_pswd_str.equals(cnfmpswd_str)) {
                            ChangePasswordService(getActivity(), new_pswd_str);
                        }else {
                            StoredObjects.ToastMethod(getString(R.string.confirm_pass_validation), getActivity());
                        }
                    }
                }
            }
        });

    }

    private void ChangePasswordService(final FragmentActivity activity,String new_pswd_str) {
        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.changepassword(RetrofitInstance.change_password,new_pswd_str,StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseRecieved = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseRecieved);
                        StoredObjects.LogMethod("response", "response::" + responseRecieved);
                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("200")) {
                            StoredObjects.ToastMethod("Password Changed Successfully", activity);
                            new_passwd_edtxt.setText("");
                            confirm_passwd_edtxt.setText("");
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
        fragmentManager.beginTransaction ().replace (R.id.frame_container , fragment).commit ();

    }

}
