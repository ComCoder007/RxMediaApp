package com.rxmediaapp.fragments.doctor;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.fragments.hospital.H_PatPrescriptionslist;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Doc_PatientPrescrDetails extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;

    LinearLayout medication_lay;

    ImageView pr_img;
    TextView descr_txt,email_txt,mobilenum_txt,age_txt,gender_txt

            ,aadharnum_txt,bloodgroup_txt,lastcndate_txt;

      ArrayList<HashMap<String, String>> data_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.d_patprescr_details,null,false );
        StoredObjects.page_type="d_patient_details";
        SideMenu.updatemenu(StoredObjects.page_type);

        initilization(v);

        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        title_txt.setText( "Patient Details" );
        aadharnum_txt=v.findViewById(R.id.aadharnum_txt);
        bloodgroup_txt=v.findViewById(R.id.bloodgroup_txt);
        lastcndate_txt=v.findViewById(R.id.lastcndate_txt);



        descr_txt=v.findViewById(R.id.descr_txt);
        pr_img=v.findViewById(R.id.pr_img);
        email_txt=v.findViewById(R.id.email_txt);
        mobilenum_txt=v.findViewById(R.id.mobilenum_txt);
        age_txt=v.findViewById(R.id.age_txt);
        gender_txt=v.findViewById(R.id.gender_txt);

        medication_lay=v.findViewById(R.id.medication_lay);

        backbtn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcallinglay(new Doc_Patient_List());
            }
        });
        if( StoredObjects.UserType.equalsIgnoreCase("Assistant")){
            medication_lay.setVisibility(View.GONE);
        }else{
            medication_lay.setVisibility(View.VISIBLE);
        }

        medication_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StoredObjects.UserType.equalsIgnoreCase("Doctors")){
                    fragmentcallinglay_(new Doc_PatPrescriptionslist());
                }else if(StoredObjects.UserType.equalsIgnoreCase("Hospitals")){
                    fragmentcallinglay_(new H_PatPrescriptionslist());
                }else{
                    fragmentcallinglay_(new H_PatPrescriptionslist());

                }

            }
        });

        if (InterNetChecker.isNetworkAvailable(getContext())) {
            getPatientProfileService(getActivity());
        }



    }

    private void getPatientProfileService(final Activity activity) {
        CustomProgressbar.Progressbarshow(activity);
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.getPatientProfile(RetrofitInstance.patient_details, StoredObjects.Pat_DocID, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
                            descr_txt.setText(data_list.get(0).get("name"));
                            email_txt.setText(data_list.get(0).get("email"));
                            mobilenum_txt.setText(data_list.get(0).get("phone"));
                            aadharnum_txt.setText(data_list.get(0).get("aadhar_number"));
                            bloodgroup_txt.setText(data_list.get(0).get("blood_group"));
                            lastcndate_txt.setText(data_list.get(0).get("last_consulted_date"));
                            age_txt.setText(data_list.get(0).get("age"));
                            gender_txt.setText(data_list.get(0).get("gender"));
                            StoredObjects.Pat_DocID=data_list.get(0).get("patient_id");
                            try {
                                Glide.with(activity)
                                        .load(Uri.parse(RetrofitInstance.BASEURL + data_list.get(0).get("image")))
                                        .apply(new RequestOptions()
                                                .placeholder(R.drawable.no_image)
                                                .fitCenter()
                                                .centerCrop())
                                        .into(pr_img);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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

    public void fragmentcallinglay_(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack("").commit ();

    }




}


