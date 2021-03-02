package com.rxmediaapp.fragments.patient;

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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.HttpPostClass;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.CameraUtils;
import com.rxmediaapp.storedobjects.StoredObjects;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Query;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class P_Add_Prescription extends Fragment {

    ImageView backbtn_img,attach_img;
    TextView title_txt;
    EditText prescrip_date_edtx,p_ap_search_edtxt,p_add_presc_patient_edtx,p_presc_prob_edtx;
    LinearLayout remark_lay,diasuggest_lay,medication_lay;
    Button save_button,browse_btn;

    String image_type="";
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    String patient_id="";
    String file_name_scanned_copy = "";
    public static ArrayList<HashMap<String, String>> update_data = new ArrayList<>();
    public static HashMapRecycleviewadapter brand_adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.p_add_prescription,null,false );
        StoredObjects.page_type="p_add_prescription";

        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);

        return v;
    }

    public static void setUpdate_data(String type,String val,String val1,String val2) {

        HashMap<String, String> dumpData_update = new HashMap<String, String>();
        if(type.equalsIgnoreCase("ref_test")){
            dumpData_update.put("medication", update_data.get(0).get("medication"));
            dumpData_update.put("tests_suggested", val);
            dumpData_update.put("selected_brands", update_data.get(0).get("selected_brands"));
            dumpData_update.put("selected_molecules", update_data.get(0).get("selected_molecules"));

        }else if(type.equalsIgnoreCase("medication")){
            dumpData_update.put("tests_suggested", update_data.get(0).get("tests_suggested"));
            dumpData_update.put("medication", val);
            dumpData_update.put("selected_brands", val1);
            dumpData_update.put("selected_molecules", val2);

        }
        update_data.remove(0);
        update_data.add(0, dumpData_update);



    }

    String first_time="yes";

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        save_button = v.findViewById( R.id. save_button);
        prescrip_date_edtx = v.findViewById( R.id. prescrip_date_edtx);
        browse_btn = v.findViewById( R.id. browse_btn);
        medication_lay= v.findViewById( R.id. medication_lay);
        attach_img = v.findViewById( R.id. attach_img);
        p_ap_search_edtxt= v.findViewById( R.id. p_ap_search_edtxt);
        remark_lay = v.findViewById( R.id. remark_lay);
        diasuggest_lay = v.findViewById( R.id. diasuggest_lay);
        p_add_presc_patient_edtx = v.findViewById( R.id. p_add_presc_patient_edtx);
        p_presc_prob_edtx = v.findViewById( R.id. p_presc_prob_edtx);
        p_presc_prob_edtx.setImeOptions(EditorInfo.IME_ACTION_DONE);
        p_presc_prob_edtx.setRawInputType(InputType.TYPE_CLASS_TEXT);

        title_txt.setText( "Add Prescription" );


        if(!file_name_scanned_copy.equalsIgnoreCase("")){
            try {
                Glide.with(getActivity())
                        .load(Uri.parse(RetrofitInstance.IMAGE_URL + file_name_scanned_copy))
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.no_image)
                                .fitCenter()
                                .centerCrop())
                        .into(attach_img);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        save_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String appointment_date_str = prescrip_date_edtx.getText().toString().trim();
                String problem_str = p_presc_prob_edtx.getText().toString().trim();
                String doctor_name_str = p_ap_search_edtxt.getText().toString().trim();

                String medication=update_data.get(0).get("medication");
                String test_suggested=update_data.get(0).get("tests_suggested");

                String selected_brands=update_data.get(0).get("selected_brands");
                String selected_molecules=update_data.get(0).get("selected_molecules");

                StoredObjects.LogMethod("values::","values::"+medication);
                if(patient_id.equalsIgnoreCase("")){
                    StoredObjects.ToastMethod("Please select Patient",getActivity());
                }else{
                    if(StoredObjects.inputValidation(prescrip_date_edtx,"Please select Appointment Date",getActivity())){
                            if(StoredObjects.inputValidation(p_ap_search_edtxt,"Please enter Doctor Name",getActivity())){
                                if(StoredObjects.inputValidation(p_presc_prob_edtx,"Please enter Problem",getActivity())){
                                if(file_name_scanned_copy.equalsIgnoreCase("")){
                                    StoredObjects.ToastMethod("Please upload Scanned Copy",getActivity());
                                }else{
                                    if(medication.equalsIgnoreCase("")){
                                        StoredObjects.ToastMethod("Please add Medication Details",getActivity());
                                    }else{
                                        if(test_suggested.equalsIgnoreCase("")){
                                            StoredObjects.ToastMethod("Please add Test Details",getActivity());
                                        }else{
                                           /* if (InterNetChecker.isNetworkAvailable(getActivity())) {
                                                new SubmitSurveyTask().execute(patient_id,appointment_date_str,doctor_name_str,problem_str,file_name_scanned_copy,medication,selected_brands,selected_molecules,test_suggested);
                                            }else{

                                            }*/

                                            try {
                                                new UpdatePresciptionsTask().execute(patient_id,appointment_date_str,doctor_name_str,problem_str,
                                                        file_name_scanned_copy,medication,selected_brands,selected_molecules,test_suggested);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                          /* addprescriptionService(getActivity(),patient_id,appointment_date_str,doctor_name_str,problem_str,
                                                   file_name_scanned_copy,medication,selected_brands,selected_molecules,test_suggested);
*/
                                        }
                                    }
                                }



                            }
                        }
                    }
                }



            }
        } );

        p_add_presc_patient_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpeclizatnListPopup(p_add_presc_patient_edtx,getActivity());
            }
        });

        browse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CameraUtils.checkAndRequestPermissions(getActivity()) == true) {
                    Imagepickingpopup(getActivity(), "p_add_prescription");
                }
            }
        });
        remark_lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remarkpopup(getActivity());
            }
        } );


        diasuggest_lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcallinglay_(new P_AddTestSuggestions());
               // Diagnosepopup(getActivity());
            }
        } );
        medication_lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentcallinglay_(new P_AddMedication());
                //medicationpopup(getActivity());
            }
        } );
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        } );



        prescrip_date_edtx.setOnClickListener(new View.OnClickListener() {
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
                                prescrip_date_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
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

                String posturl=RetrofitInstance.IMAGEUPLOADURL+"method=add-prescription&patient_id="+patient_id
                        +"&appointment_date_time="+params[1]+
                        "&doctor_name="+params[2]+"&problem="+params[3]+"&scanned_copy="+file_name_scanned_copy
                        +"&medication="+params[5]+"selected_brands="+params[6]+"selected_molecules="+params[7]+"&tests_suggested="+params[8];

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

                        P_AddTestSuggestions.testsuggestionlist.clear();
                        update_data.clear();
                        P_AddMedication.medications_list.clear();
                        fragmentcallinglay(new P_Prescriptionslist());

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }
  /*  private void addprescriptionService(final Activity activity,
                                        String patient_id, String appointment_date_str, String doctor_name_str,
                                        String problem_str, String file_name_scanned_copy
    ,String medication,String selbrands,String selmolecules,String test) {

        CustomProgressbar.Progressbarshow(activity);

        *//*StoredObjects.LogMethod("values::","values::"+patient_id+"::"+doctor_name_str+"::"+
                problem_str+"::"+file_name_scanned_copy+"::"+ P_AddMedication.m_jsonArray+"::"+selbrands+"::"+selmolecules+"::"+P_AddTestSuggestions.t_jsonArray);
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.addPrescriptions(RetrofitInstance.add_prescription,patient_id,appointment_date_str,doctor_name_str,problem_str,file_name_scanned_copy,
                P_AddMedication.m_jsonArray,selbrands,selmolecules,P_AddTestSuggestions.t_jsonArray).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String responseReceived = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseReceived);
                    StoredObjects.LogMethod("response", "response::" + responseReceived);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                       StoredObjects.ToastMethod("Added Successfully",activity);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                CustomProgressbar.Progressbarcancel(activity);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomProgressbar.Progressbarcancel(activity);
            }
        });
*//*
        Response response = null;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try {
            String posturl="http://o3sa.co.in/demos/Rx/app/index.php?method=add-prescription&patient_id="+patient_id+"&appointment_date_time="+appointment_date_str+
                    "&doctor_name="+doctor_name_str+"&problem="+problem_str+"&scanned_copy="+file_name_scanned_copy+"&medication="+medication+"selected_brands="+selbrands+"selected_molecules="+selmolecules+"&tests_suggested="+test;

            StoredObjects.LogMethod("values::","values::"+posturl);

            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(posturl)
                    .method("POST", body)
                    .addHeader("Cookie", "PHPSESSID=so33jinnfq7e8rgdvmrr1ucvd0")
                    .build();
             response = client.newCall(request).execute();
            String responseReceived = response.body().string();
            if (response.code() == 200) {
                StoredObjects.ToastMethod("Added Successfully",activity);
                P_AddTestSuggestions.testsuggestionlist.clear();
                update_data.clear();
                P_AddMedication.medications_list.clear();
                fragmentcallinglay(new P_Prescriptionslist());
                CustomProgressbar.Progressbarcancel(getActivity());
            } else {
                CustomProgressbar.Progressbarcancel(getActivity());
            }
            StoredObjects.LogMethod("val", "val::" + responseReceived);
        } catch (IOException  e) {

        }


    }
*/
    private void SpeclizatnListPopup(final EditText prfilenme,Activity activity) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
        listPopupWindow.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, P_Prescriptionslist.patname_list));
        listPopupWindow.setAnchorView(prfilenme);
        listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                patient_id = P_Prescriptionslist.patientslist.get(position).get("patient_id");
                prfilenme.setText(P_Prescriptionslist.patname_list.get(position));
                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
    }

    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).commit ();

    }

    public void fragmentcallinglay_(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack("").commit ();

    }

   /* private void getAssistantService(final Activity activity,final int pagecount,String recordsperpage) {
        CustomProgressbar.Progressbarshow(activity);
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.getmembers(RetrofitInstance.members, StoredObjects.UserId, StoredObjects.UserRoleId,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String responseReceived = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseReceived);
                    StoredObjects.LogMethod("response", "response::" + responseReceived);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        String results = jsonObject.getString("results");
                        dummypatientslist.clear();
                        patientslist.clear();
                        patname_list.clear();
                        dummypatientslist = JsonParsing.GetJsonData(results);
                        HashMap<String,String> hashMap=new HashMap<>();
                        hashMap.put("name",SideMenu.data_list.get(0).get("name") +"(Self)");
                        hashMap.put("patient_id",StoredObjects.Logged_PatientId);
                        patientslist.add(hashMap);

                        for(int k=0;k<dummypatientslist.size();k++){
                            HashMap<String,String> hashMap1=new HashMap<>();
                            hashMap1.put("name",patientslist.get(k).get("name"));
                            hashMap1.put("patient_id",patientslist.get(k).get("patient_id"));
                            patientslist.add(hashMap1);

                        }
                        for(int k=0;k<patientslist.size();k++){
                            patname_list.add(patientslist.get(k).get("name"));

                        }


                    } else {
                        dummypatientslist.clear();
                        patientslist.clear();
                        patname_list.clear();

                        StoredObjects.ToastMethod("No Data found", activity);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                if (pagecount == 1) {
                    CustomProgressbar.Progressbarcancel(activity);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (pagecount == 1) {
                    CustomProgressbar.Progressbarcancel(activity);
                }

            }
        });

    }
*/
    private void remarkpopup(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.remarks_popup );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);

        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
        CustomButton search_button =(CustomButton) dialog.findViewById(R.id.search_button);
        ImageView canclimg = (ImageView)dialog.findViewById(R.id.canclimg);




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

                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void Diagnosepopup(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.diagnose_suggestion );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);

        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
        CustomButton search_button =(CustomButton) dialog.findViewById(R.id.search_button);
        ImageView canclimg = (ImageView)dialog.findViewById(R.id.canclimg);
        RecyclerView diagnose_suggest_recyclerview = (RecyclerView)dialog.findViewById(R.id.diagnose_suggest_recyclerview);

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(activity);
        diagnose_suggest_recyclerview.setLayoutManager(linearLayoutManager);

        diagnose_suggest_recyclerview.setAdapter(new HashMapRecycleviewadapter(activity,StoredObjects.dummy_list,"diagnose_suggest",diagnose_suggest_recyclerview,R.layout.diagnose_suggestion_listitems));





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

                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void medicationpopup(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.doc_medication );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);

        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
        CustomButton search_button =(CustomButton) dialog.findViewById(R.id.search_button);
        ImageView canclimg = (ImageView)dialog.findViewById(R.id.canclimg);

        // CustomEditText tablets_dropdn=dialog.findViewById(R.id.tablets_dropdn);



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

                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void Imagepickingpopup(final Activity activity, final String type) {

        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.photo_selpopup);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout p_cancel_lay=(LinearLayout) dialog.findViewById(R.id.p_cancel_lay);

    p_cancel_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog.dismiss();
      }
    });LinearLayout takepic_lay = (LinearLayout) dialog.findViewById(R.id.takepic_lay);
        LinearLayout pickglry_lay = (LinearLayout) dialog.findViewById(R.id.pickglry_lay);
        LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);


        cancel_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        takepic_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_type = type;
                captureImage();

                dialog.dismiss();
            }
        });

        pickglry_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_type = type;

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);


                dialog.dismiss();

            }

        });

        dialog.show();
    }

    private Uri filePath;
    File fileOrDirectory;

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(CameraUtils.MEDIA_TYPE_IMAGE);
        if (file != null) {
            CameraUtils.imageStoragePath = file.getAbsolutePath();
            fileOrDirectory = file;
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getActivity(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private Uri picUri;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //user is returning from capturing an image using the camera
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getActivity(), CameraUtils.imageStoragePath);

                try {
                    f_new = createNewFile("CROP_");
                    try {
                        f_new.createNewFile();
                    } catch (IOException ex) {
                        Log.e("io", ex.getMessage());
                    }


                    //Photo_SHowDialog(SignUp.this(),f_new,imageStoragePath,myBitmap);
                    imageupload(getActivity(), CameraUtils.imageStoragePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    StoredObjects.LogMethod("", "imagepathexpection:--" + e);

                }
                // successfully captured the image
                // display it in image view
                // Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getActivity(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
            }
        } else if (requestCode == 2) {

            StoredObjects.LogMethod("resultcode", "result code" + resultCode);
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();


                try {
                    Bitmap myBitmap = null;
                    picUri = data.getData();

                    myBitmap = (BitmapFactory.decodeFile(picturePath));

                    try {


                        f_new = createNewFile("CROP_");
                        try {
                            f_new.createNewFile();
                        } catch (IOException ex) {
                            Log.e("io", ex.getMessage());
                        }
                        StoredObjects.LogMethod("path", "path:::" + picturePath + "--" + myBitmap);
                        CameraUtils.imageStoragePath = picturePath;
                        imageupload(getActivity(), picturePath);
                        //new ImageUploadTaskNew().execute(docFilePath.toString());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        StoredObjects.LogMethod("", "Exception:--" + e1);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    StoredObjects.LogMethod("", "Exception:--" + e);
                }
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getActivity(),
                    "User cancelled image picking", Toast.LENGTH_SHORT)
                    .show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity(),
                    "Sorry! Failed to pick the image", Toast.LENGTH_SHORT)
                    .show();
            }

        }


    }

    private Uri mCropImagedUri;
    File f_new;

    private File createNewFile(String prefix) {
        if (prefix == null || "".equalsIgnoreCase(prefix)) {
            prefix = "IMG_";
        }
        File newDirectory = new File(Environment.getExternalStorageDirectory() + "/mypics/");
        if (!newDirectory.exists()) {
            if (newDirectory.mkdir()) {
                Log.d(getActivity().getClass().getName(), newDirectory.getAbsolutePath() + " directory created");
            }
        }
        File file = new File(newDirectory, (prefix + System.currentTimeMillis() + ".jpg"));
        if (file.exists()) {
            //this wont be executed
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public static String fileName = "";
    private Bitmap myImg = null;
    private File compressedImage;

    public void imageupload(final Context context, final String path) {

        String fileNameSegments[] = path.split("/");
        fileName = fileNameSegments[fileNameSegments.length - 1];

        myImg = Bitmap.createBitmap(CameraUtils.getResizedBitmap(CameraUtils.getUnRotatedImage(path, BitmapFactory.decodeFile(path)), 500));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        myImg.compress(Bitmap.CompressFormat.PNG, 100, stream);

        bitmapToUriConverter(myImg);

    }

    public void bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();

            File file = new File(getActivity().getFilesDir(), "UploadImages"
                + new Random().nextInt() + ".png");

            FileOutputStream out;
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion > Build.VERSION_CODES.M) {
                out = getActivity().openFileOutput(file.getName(),
                    Context.MODE_PRIVATE);
            } else {
                out = getActivity().openFileOutput(file.getName(),
                    Context.MODE_WORLD_READABLE);
            }

            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            new Compressor(getActivity())
                .compressToFileAsFlowable(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) {
                        compressedImage = file;
                        setCompressedImage();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });


        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
    }

    private void setCompressedImage() {

        Log.i("Compressor", "Compressed image save in " + compressedImage.getAbsolutePath());
        String realPath = compressedImage.getAbsolutePath();
        if (InterNetChecker.isNetworkAvailable(getActivity())) {

            File file = new File(realPath);

            try {
               // postFile(realPath, RetrofitInstance.BASEURL + "app/index.php", file.getName());
                new ImageuploadTask().execute(realPath, file.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.nointernet), getActivity());
        }


    }
    public void postFile(String encodedImage, String postUrl, String fileName) {



        Response response = null;

        OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
        StrictMode.ThreadPolicy policy = new StrictMode.
            ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("method", RetrofitInstance.upload_file)
            .addFormDataPart("uploaded_file", fileName,
                RequestBody.create(MediaType.parse("application/octet-stream"),
                    new File(encodedImage)))
            .build();
        Request request = new Request.Builder()
            .url(postUrl)
            .method("POST", body)
            .addHeader("Cookie", "PHPSESSID=pp4db1qhog5fku530huapduqm5")
            .build();

        try {
            response = client.newCall(request).execute();
            String responseReceived = response.body().string();
            if (response.code() == 200) {
                JSONObject jsonObject = new JSONObject(responseReceived);
                file_name_scanned_copy = jsonObject.getString("file_name");


                try {
                    Glide.with(getActivity())
                            .load(Uri.parse(RetrofitInstance.IMAGE_URL + file_name_scanned_copy))
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.no_image)
                                    .fitCenter()
                                    .centerCrop())
                            .into(attach_img);
                } catch (Exception e) {
                    e.printStackTrace();

                }

                CustomProgressbar.Progressbarcancel(getActivity());
            } else {
                CustomProgressbar.Progressbarcancel(getActivity());
            }
            StoredObjects.LogMethod("val", "val::" + responseReceived);
        } catch (IOException | JSONException e) {

        }


    }



    public class ImageuploadTask extends AsyncTask<String, Integer, String> {


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
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("method", RetrofitInstance.upload_file)
                        .addFormDataPart("uploaded_file", params[1],
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        new File(params[0])))
                        .build();
                Request request = new Request.Builder()
                        .url(RetrofitInstance.IMAGEUPLOADURL)
                        .method("POST", body)
                        .build();

                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Response response = client.newCall(request).execute();
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
                        file_name_scanned_copy = jsonObject.getString("file_name");

                        // patient_image.setImageBitmap(myImg);
                        try {
                            Glide.with(getActivity())
                                    .load(Uri.parse(RetrofitInstance.IMAGE_URL + file_name_scanned_copy))
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.no_image)
                                            .fitCenter()
                                            .centerCrop())
                                    .into(attach_img);


                        } catch (Exception e) {
                            e.printStackTrace();

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


}

