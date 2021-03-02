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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rxmediaapp.R;
import com.rxmediaapp.customadapter.HashmapViewHolder;
import com.rxmediaapp.fragments.patient.P_AddMedication;
import com.rxmediaapp.fragments.patient.P_AddTestSuggestions;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class Doc_Patient_Details extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    Button preprescr_btn,updateprscr_btn;
     TextView nodatavailable_txt;
     EditText problem_txt;
    RecyclerView doc_patient_recyler;
    HashMapRecycleviewadapter adapter;
    public static   ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> data_list_one = new ArrayList<>();
    String[] tabslist = {"Add Physical Examination","Assessment & Plan","Diagnose Suggestion"};

    public static ArrayList<HashMap<String, String>> update_data = new ArrayList<>();
    public  static ArrayList<HashMap<String, String>> physuggestionslist = new ArrayList<>();
    public  static ArrayList<HashMap<String, String>> testadd_list = new ArrayList<>();


    LinearLayout medication_lay;

    ImageView pr_img;
    TextView descr_txt,enrollid_txt,mobilenum_txt,age_txt,gender_txt;

    EditText nextvisit_txt;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.doc_patient_details,null,false );
        StoredObjects.page_type="doc_patient_details";
        SideMenu.updatemenu(StoredObjects.page_type);
        //reset();
        initilization(v);
        if(StoredObjects.first_time.equalsIgnoreCase("yes")){
            data_list.clear();
            testadd_list.clear();
            testlist();
            serviceCalling();
        }else{
            if(data_list.size()>0){
                try {
                    if(data_list.get(0).get("physical_examinations_count").equalsIgnoreCase("0")){
                        data_list_one.clear();
                    }else{
                        data_list_one = JsonParsing.GetJsonData(data_list.get(0).get("physical_examinations"));
                    }
                    descr_txt.setText(data_list.get(0).get("name"));
                    enrollid_txt.setText(data_list.get(0).get("enroll_id"));
                    mobilenum_txt.setText(data_list.get(0).get("phone"));
                    age_txt.setText(data_list.get(0).get("age"));
                    gender_txt.setText(data_list.get(0).get("gender"));
                    problem_txt.setText(data_list.get(0).get("problem"));

                    try {
                        Glide.with(getActivity())
                                .load(Uri.parse(RetrofitInstance.IMAGE_URL + data_list.get(0).get("image")))
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.no_image)
                                        .fitCenter()
                                        .centerCrop())
                                .into(pr_img);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }catch (Exception e){

                }
            }
        }

        return v;
    }
    public void reset(){
        Doc_AddMedication.medications_list.clear();
        update_data.clear();
        physuggestionslist.clear();
        testadd_list.clear();
    }
    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        doc_patient_recyler = v.findViewById( R.id. doc_patient_recyler);
        preprescr_btn=v.findViewById(R.id.preprescr_btn);
        updateprscr_btn=v.findViewById(R.id.updateprscr_btn);
        title_txt.setText( "Patient Details" );
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);

        descr_txt=v.findViewById(R.id.descr_txt);
        pr_img=v.findViewById(R.id.pr_img);
        enrollid_txt=v.findViewById(R.id.enrollid_txt);
        mobilenum_txt=v.findViewById(R.id.mobilenum_txt);
        age_txt=v.findViewById(R.id.age_txt);
        gender_txt=v.findViewById(R.id.gender_txt);
        problem_txt=v.findViewById(R.id.problem_txt);
        medication_lay=v.findViewById(R.id.medication_lay);
        nextvisit_txt=v.findViewById(R.id.nextvisit_txt);

        problem_txt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        problem_txt.setRawInputType(InputType.TYPE_CLASS_TEXT);

        medication_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcallinglay_(new Doc_AddMedication());
            }
        });


        preprescr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                fragmentcallinglay(new Doc_PatPrescriptionslist());
            }
        });

        updateprscr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String problem=problem_txt.getText().toString().trim();
                String nextvisit=nextvisit_txt.getText().toString().trim();
                if(StoredObjects.inputValidation(problem_txt,"Please enter Problem",getActivity())){

                    if(!update_data.get(0).get("physical_examinations").equalsIgnoreCase("")){
                        if(!update_data.get(0).get("assessment_plan").equalsIgnoreCase("")){
                            if(!update_data.get(0).get("medication").equalsIgnoreCase("")){

                                if(StoredObjects.inputValidation(nextvisit_txt,"Please enter Next Visit Suggestion",getActivity())){
                                    try {
                                        new UpdatePresciptionsTask().execute(problem,nextvisit);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                //UpdatePrescriptionService(getActivity(),problem);



                            }else{
                                StoredObjects.ToastMethod("Please select Medication Details",getActivity());
                            }
                        }else{
                            StoredObjects.ToastMethod("Please enter Assessment and Plan",getActivity());
                        }
                    }else{
                        StoredObjects.ToastMethod("Please enter Physical Suggestions",getActivity());
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
        });



        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        doc_patient_recyler.setLayoutManager(linearLayoutManager);

        StoredObjects.getrray(tabslist);
        doc_patient_recyler.setAdapter(new HashMapRecycleviewadapter(getActivity(),StoredObjects.menuitems_list,"doc_patient_details",doc_patient_recyler,R.layout.doc_patientdetails_listitem));





    }

    public static void testlist(){
        HashMap<String,String> hashMap1=new HashMap<>();
        hashMap1.put("referredLab","");
        hashMap1.put("suggestedTest","");
        testadd_list.add(hashMap1);
    }



    private void serviceCalling() {

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            PrescriptiondetailsService(getActivity());
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
        }
    }

    private void PrescriptiondetailsService(final Activity activity) {

        CustomProgressbar.Progressbarshow(activity);
        StoredObjects.first_time="no";
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.prescriptiondetails(RetrofitInstance.doctor_view_prescription,StoredObjects.UserId,StoredObjects.UserRoleId,StoredObjects.Prescription_Id).enqueue(new Callback<ResponseBody>() {
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


                            if (data_list.get(0).get("physical_examinations_count").equalsIgnoreCase("0")) {
                                data_list_one.clear();
                            } else {
                                data_list_one = JsonParsing.GetJsonData(data_list.get(0).get("physical_examinations"));
                            }
                            descr_txt.setText(data_list.get(0).get("name"));
                            enrollid_txt.setText(data_list.get(0).get("enroll_id"));
                            mobilenum_txt.setText(data_list.get(0).get("phone"));
                            age_txt.setText(data_list.get(0).get("age"));
                            gender_txt.setText(data_list.get(0).get("gender"));
                            problem_txt.setText(data_list.get(0).get("problem"));

                            try {
                                Glide.with(getActivity())
                                        .load(Uri.parse(RetrofitInstance.IMAGE_URL + data_list.get(0).get("image")))
                                        .apply(new RequestOptions()
                                                .placeholder(R.drawable.no_image)
                                                .fitCenter()
                                                .centerCrop())
                                        .into(pr_img);
                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        } else {

                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    if (data_list_one.size() > 0) {
                        physuggestionslist.clear();
                        addphysuggestions(data_list_one, "edit");
                        CustomProgressbar.Progressbarcancel(activity);
                    } else {
                        if (InterNetChecker.isNetworkAvailable(getActivity())) {
                            getPhsyicalSuggestions(getActivity(), 1, "50");
                        } else {
                            StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                        }

                    }


                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                CustomProgressbar.Progressbarcancel(activity);


            }
        });

    }

    public static void setUpdate_data(String type,String val,String val1,String val2) {

        HashMap<String, String> dumpData_update = new HashMap<String, String>();
        if(type.equalsIgnoreCase("physical")){
            dumpData_update.put("problem", update_data.get(0).get("problem"));
            dumpData_update.put("physical_examinations", val);
            dumpData_update.put("assessment_plan", update_data.get(0).get("assessment_plan"));
            dumpData_update.put("patient_advice", update_data.get(0).get("patient_advice"));
            dumpData_update.put("doctors_note", update_data.get(0).get("doctors_note"));
            dumpData_update.put("medication", update_data.get(0).get("medication"));
            dumpData_update.put("tests_suggested", update_data.get(0).get("tests_suggested"));
            dumpData_update.put("selected_brands", update_data.get(0).get("selected_brands"));
            dumpData_update.put("selected_molecules", update_data.get(0).get("selected_molecules"));

        }else if(type.equalsIgnoreCase("assessment_plan")){
            dumpData_update.put("problem", update_data.get(0).get("problem"));
            dumpData_update.put("physical_examinations", update_data.get(0).get("physical_examinations"));
            dumpData_update.put("assessment_plan",val);
            dumpData_update.put("patient_advice", val1);
            dumpData_update.put("doctors_note", val2);
            dumpData_update.put("medication", update_data.get(0).get("medication"));
            dumpData_update.put("tests_suggested", update_data.get(0).get("tests_suggested"));
            dumpData_update.put("selected_brands", update_data.get(0).get("selected_brands"));
            dumpData_update.put("selected_molecules", update_data.get(0).get("selected_molecules"));

        }else if(type.equalsIgnoreCase("ref_test")){
            dumpData_update.put("problem", update_data.get(0).get("problem"));
            dumpData_update.put("physical_examinations", update_data.get(0).get("physical_examinations"));
            dumpData_update.put("assessment_plan", update_data.get(0).get("assessment_plan"));
            dumpData_update.put("patient_advice", update_data.get(0).get("patient_advice"));
            dumpData_update.put("doctors_note", update_data.get(0).get("doctors_note"));
            dumpData_update.put("medication", update_data.get(0).get("medication"));
            dumpData_update.put("tests_suggested", val);
            dumpData_update.put("selected_brands", update_data.get(0).get("selected_brands"));
            dumpData_update.put("selected_molecules", update_data.get(0).get("selected_molecules"));

        }else if(type.equalsIgnoreCase("medication")){
            dumpData_update.put("problem", update_data.get(0).get("problem"));
            dumpData_update.put("physical_examinations", update_data.get(0).get("physical_examinations"));
            dumpData_update.put("assessment_plan", update_data.get(0).get("assessment_plan"));
            dumpData_update.put("patient_advice", update_data.get(0).get("patient_advice"));
            dumpData_update.put("doctors_note", update_data.get(0).get("doctors_note"));
            dumpData_update.put("medication", val);
            dumpData_update.put("tests_suggested", update_data.get(0).get("tests_suggested"));
            dumpData_update.put("selected_brands", val1);
            dumpData_update.put("selected_molecules", val2);

        }
        update_data.remove(0);
        update_data.add(0, dumpData_update);

    }
    public static HashMapRecycleviewadapter phy_adapter;
    public static HashMapRecycleviewadapter test_adapter;

    public static HashMapRecycleviewadapter brand_adapter;
    public static void addphysuggestions(ArrayList<HashMap<String, String>> data_list_one, String type) {

        if(type.equalsIgnoreCase("add")){
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("suggestion_id","");
            hashMap.put("suggestion_value","");
            hashMap.put("suggestion_name","");
            hashMap.put("examination_id","0");
            hashMap.put("remove","add");
            physuggestionslist.add(hashMap);
        }else{
            for(int k=0;k<data_list_one.size();k++){
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("suggestion_id",data_list_one.get(k).get("suggestion_id"));
                hashMap.put("suggestion_value",data_list_one.get(k).get("description"));
                hashMap.put("suggestion_name",data_list_one.get(k).get("suggestion"));
                hashMap.put("examination_id",data_list_one.get(k).get("examination_id"));
                hashMap.put("remove","edit");
                physuggestionslist.add(hashMap);
            }


        }


    }



    private void getPhsyicalSuggestions(final Activity activity, final int pagecount, String recordsperpage) {


        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.getPhsyicalSuggestions(RetrofitInstance.physical_suggestions, StoredObjects.UserId, StoredObjects.UserRoleId,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
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
                            data_list_one = JsonParsing.GetJsonData(results);
                            physuggestionslist.clear();
                            addphysuggestions(data_list_one, "add");

                            StoredObjects.physuggestionsnames_list.clear();
                            for (int k = 0; k < data_list_one.size(); k++) {
                                StoredObjects.physuggestionsnames_list.add(data_list_one.get(k).get("suggestion"));
                            }


                        } else {
                            data_list_one.clear();

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
    public class UpdatePresciptionsTask extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomProgressbar.Progressbarshow(getActivity());
        }

        @Override
        protected String doInBackground(String... params) {

            String res = null;
            try {


                StrictMode.ThreadPolicy policy = new StrictMode.
                        ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String posturl=RetrofitInstance.IMAGEUPLOADURL+"method=update-prescription&prescription_id="+StoredObjects.Prescription_Id
                        +"&problem="+params[0]+
                        "&physical_examinations="+update_data.get(0).get("physical_examinations")+"&assessment_plan="+update_data.get(0).get("assessment_plan")
                        +"&patient_advice="+update_data.get(0).get("patient_advice")+"&doctors_note="+update_data.get(0).get("doctors_note")
                        +"&medication="+update_data.get(0).get("medication")+"&tests_suggested="+update_data.get(0).get("tests_suggested")
                        +"selected_brands="+update_data.get(0).get("selected_brands")+"selected_molecules="+update_data.get(0).get("selected_molecules")
                        +"&logged_in_role_id="+StoredObjects.UserRoleId+"&logged_in_user_id="+StoredObjects.UserId+"&next_visit_suggestion="+params[1];

                StoredObjects.LogMethod("values::","values::"+posturl);

                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url(posturl)
                        .method("POST", body)
                        .build();


                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                Log.e("TAG", "Response : " + res);
                return res;

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e("TAG", "Error: " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
            }


            return res;

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            CustomProgressbar.Progressbarcancel(getActivity());

            if (response != null) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        StoredObjects.ToastMethod("Prescription details updated Successfully",getActivity());

                        update_data.clear();
                        Doc_AddMedication.medications_list.clear();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        if (fm.getBackStackEntryCount() > 0) {
                            fm.popBackStack();
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }

        }
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

