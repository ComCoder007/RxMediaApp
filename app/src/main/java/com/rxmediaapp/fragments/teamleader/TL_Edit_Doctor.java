package com.rxmediaapp.fragments.teamleader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.CameraUtils;
import com.rxmediaapp.storedobjects.StoredObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TL_Edit_Doctor extends Fragment {

    ImageView backbtn_img,h_add_dr_image;
    TextView title_txt;
    CustomEditText h_doc_nme_edtx,h_email_edtx,h_regnum_edtx,h_spclztn_edtx,h_statbard_edtx,h_yrrgstn_edtx,
            h_mbile_edtx,h_custm_edtx,h_avabletme_edtx,h_tmetwo_edtx,assistant_edtxt,dp_passwd_edtx,tl_docadres_edtx;
    Button hd_submit_btn;
    CustomEditText h_avabletme_edtx2,h_avabletme_edtx3,h_avabletme_edtx4,h_tmetwo_edtx2,h_tmetwo_edtx3,h_tmetwo_edtx4
            ,h_frmday_edtx,h_today_edtx;
    String speacialisation_id="";
    String image_type = "";
    String from_day="",to_day="",user_id="";
    String fromtimeslot_id_one="",totimeslot_id_one="",fromtimeslot_id_two="",totimeslot_id_two="",
            fromtimeslot_id_three="",totimeslot_id_three="",fromtimeslot_id_four="",totimeslot_id_four="";

    // String[] dayslist = {"Sun","Mon","Tues","Wed","Thur","Fri","Sat"};

    public  static  ArrayList<HashMap<String, String>> editarraylist = new ArrayList<>();

    ArrayList<String> specialisation_names_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> specialization_list = new ArrayList<>();

    ArrayList<String> to_daynames_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> to_days_list = new ArrayList<>();



    String first_time="yes";
    EditText d_qualification_edtx,d_otherqualification_edtx,d_extraqualification_edtx;

    TextView assistant_txt;





    ArrayList<HashMap<String, String>> extratimeslot_list = new ArrayList<>();

    ArrayList<HashMap<String, String>> s_extratimeslot_list = new ArrayList<>();

    RecyclerView appointment_slot_rv;
    Button h_addoc_add_day_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.tl_editdoctor,null,false );
        StoredObjects.page_type="tl_edit_doctor";

        SideMenu.updatemenu(StoredObjects.page_type);

        initilization(v);

        servicecalling();
        return v;
    }

    private void servicecalling() {
        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            get_weekdays(getActivity());

        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
        }
    }


    private void initilization(View v) {

        backbtn_img = v.findViewById(R.id.backbtn_img);
        title_txt = v.findViewById(R.id.title_txt);

        h_doc_nme_edtx = v.findViewById(R.id.h_doc_nme_edtx);
        h_email_edtx = v.findViewById(R.id.h_email_edtx);
        h_regnum_edtx = v.findViewById(R.id.h_regnum_edtx);
        h_spclztn_edtx = v.findViewById(R.id.h_spclztn_edtx);
        h_statbard_edtx = v.findViewById(R.id.h_statbard_edtx);
        h_yrrgstn_edtx = v.findViewById(R.id.h_yrrgstn_edtx);
        h_mbile_edtx = v.findViewById(R.id.h_mbile_edtx);
        h_frmday_edtx = v.findViewById(R.id.h_frmday_edtx);
        h_today_edtx = v.findViewById(R.id.h_today_edtx);
        h_custm_edtx = v.findViewById(R.id.h_custm_edtx);
        h_avabletme_edtx = v.findViewById(R.id.h_avabletme_edtx);
        h_avabletme_edtx2 = v.findViewById(R.id.h_avabletme_edtx2);
        h_avabletme_edtx3 = v.findViewById(R.id.h_avabletme_edtx3);
        h_avabletme_edtx4 = v.findViewById(R.id.h_avabletme_edtx4);
        h_tmetwo_edtx = v.findViewById(R.id.h_tmetwo_edtx);
        h_tmetwo_edtx2 = v.findViewById(R.id.h_tmetwo_edtx2);
        h_tmetwo_edtx3 = v.findViewById(R.id.h_tmetwo_edtx3);
        h_tmetwo_edtx4 = v.findViewById(R.id.h_tmetwo_edtx4);
        assistant_edtxt = v.findViewById(R.id.assistant_edtxt);
        hd_submit_btn = v.findViewById(R.id.hd_submit_btn);
        h_add_dr_image = v.findViewById(R.id.h_add_dr_image);
        dp_passwd_edtx=v.findViewById(R.id.dp_passwd_edtx);
        assistant_txt=v.findViewById(R.id.assistant_txt);

        h_addoc_add_day_btn=v.findViewById(R.id.h_addoc_add_day_btn);
        appointment_slot_rv=v.findViewById(R.id.appointment_slot_rv);

        tl_docadres_edtx=v.findViewById(R.id.tl_docadres_edtx);

        tl_docadres_edtx.setImeOptions(EditorInfo.IME_ACTION_DONE);
        tl_docadres_edtx.setRawInputType(InputType.TYPE_CLASS_TEXT);

        assistant_edtxt.setVisibility(View.GONE);
        h_add_dr_image.setVisibility(View.GONE);
        dp_passwd_edtx.setVisibility(View.GONE);
        assistant_txt.setVisibility(View.GONE);
        title_txt.setText("Edit Doctor");

        d_qualification_edtx = v.findViewById(R.id.d_qualification_edtx);
        d_otherqualification_edtx = v.findViewById(R.id.d_otherqualification_edtx);
        d_extraqualification_edtx = v.findViewById(R.id.d_extraqualification_edtx);




        try {
            fromtimeslot_id_one=editarraylist.get(0).get("from_time");
            totimeslot_id_one=editarraylist.get(0).get("to_time");

            fromtimeslot_id_two=editarraylist.get(0).get("from_time1");
            totimeslot_id_two=editarraylist.get(0).get("to_time1");

            fromtimeslot_id_three=editarraylist.get(0).get("from_time2");
            totimeslot_id_three=editarraylist.get(0).get("to_time2");

            fromtimeslot_id_four=editarraylist.get(0).get("from_time3");
            totimeslot_id_four=editarraylist.get(0).get("to_time3");

            user_id=editarraylist.get(0).get("doctor_id");
            speacialisation_id=editarraylist.get(0).get("specialization_id");
            h_spclztn_edtx.setText(editarraylist.get(0).get("specialization"));
            h_frmday_edtx.setText(editarraylist.get(0).get("from_days"));
            h_today_edtx.setText(editarraylist.get(0).get("to_days"));
            h_custm_edtx.setText(editarraylist.get(0).get("custom_timings"));
            from_day=editarraylist.get(0).get("from_days");
            to_day=editarraylist.get(0).get("to_days");

            h_regnum_edtx.setText(editarraylist.get(0).get("doctor_registration_number"));
            h_email_edtx.setText(editarraylist.get(0).get("email"));
            h_mbile_edtx.setText(editarraylist.get(0).get("phone"));
            tl_docadres_edtx.setText(editarraylist.get(0).get("address"));
            h_yrrgstn_edtx.setText(editarraylist.get(0).get("year_of_registration"));
            h_statbard_edtx.setText(editarraylist.get(0).get("state_board"));
            h_doc_nme_edtx.setText(editarraylist.get(0).get("name"));
            d_qualification_edtx.setText(editarraylist.get(0).get("qualification"));
            d_otherqualification_edtx.setText(editarraylist.get(0).get("other_qualification"));
            d_extraqualification_edtx.setText(editarraylist.get(0).get("extra_qualification"));


            h_avabletme_edtx2.setText(StoredObjects.time12hrsformat(fromtimeslot_id_two));
            h_tmetwo_edtx2.setText(StoredObjects.time12hrsformat(totimeslot_id_two));

            h_avabletme_edtx.setText(StoredObjects.time12hrsformat(fromtimeslot_id_one));
            h_tmetwo_edtx.setText(StoredObjects.time12hrsformat(totimeslot_id_one));


            h_avabletme_edtx3.setText(StoredObjects.time12hrsformat(fromtimeslot_id_three));
            h_tmetwo_edtx3.setText(StoredObjects.time12hrsformat(totimeslot_id_three));


            h_avabletme_edtx4.setText(StoredObjects.time12hrsformat(fromtimeslot_id_four));
            h_tmetwo_edtx4.setText(StoredObjects.time12hrsformat(totimeslot_id_four));


            if(editarraylist.get(0).get("extra_time_slots_count").equalsIgnoreCase("0")){
                s_extratimeslot_list.clear();
                extratimeslot_list.clear();
            }else{
                s_extratimeslot_list.clear();
                s_extratimeslot_list=JsonParsing.GetJsonData(editarraylist.get(0).get("extra_time_slots"));
                extratimeslot_list.clear();

                for(int k=0;k<s_extratimeslot_list.size();k++){
                    HashMap<String, String> hashMap = new HashMap<>();


                    hashMap.put("id", s_extratimeslot_list.get(k).get("id"));

                    hashMap.put("from_time",  s_extratimeslot_list.get(k).get("from_time"));
                    hashMap.put("to_time",  s_extratimeslot_list.get(k).get("to_time"));
                    hashMap.put("from_time1",  s_extratimeslot_list.get(k).get("from_time1"));
                    hashMap.put("to_time1",  s_extratimeslot_list.get(k).get("to_time1"));
                    hashMap.put("from_time2",  s_extratimeslot_list.get(k).get("from_time2"));
                    hashMap.put("to_time2",  s_extratimeslot_list.get(k).get("to_time2"));
                    hashMap.put("from_time3",  s_extratimeslot_list.get(k).get("from_time3"));
                    hashMap.put("to_time3",  s_extratimeslot_list.get(k).get("to_time3"));

                    int val1=-1,val2=-1;
                    int f_val1=-1,t_val1=-1;
                    int f_val2=-1,t_val2=-1;
                    int f_val3=-1,t_val3=-1;
                    int f_val4=-1,t_val4=-1;

                    for (int m = 0; m < TL_AddDoctor.dummy_times_list.size(); m++) {

                        if(s_extratimeslot_list.get(k).get("from_time").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                            f_val1=m;
                        }
                        if(s_extratimeslot_list.get(k).get("to_time").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                            t_val1=m;
                        }
                        if(s_extratimeslot_list.get(k).get("from_time1").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                            f_val2=m;
                        }
                        if(s_extratimeslot_list.get(k).get("to_time1").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                            t_val2=m;
                        }

                        if(s_extratimeslot_list.get(k).get("from_time2").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                            f_val3=m;
                        }

                        if(s_extratimeslot_list.get(k).get("to_time2").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                            t_val3=m;
                        }

                        if(s_extratimeslot_list.get(k).get("from_time3").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                            f_val4=m;
                        }
                        if(s_extratimeslot_list.get(k).get("to_time3").equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(m).get("time_slot"))){
                            t_val4=m;
                        }


                    }
                    if(f_val1==-1){
                        hashMap.put("from_time_pos", "0");
                    }else{
                        hashMap.put("from_time_pos", f_val1+"");
                    }

                    if(t_val1==-1){
                        hashMap.put("to_time_pos", "0");
                    }else{
                        hashMap.put("to_time_pos", t_val1+"");
                    }


                    if(f_val2==-1){
                        hashMap.put("from_time1_pos", "0");
                    }else{
                        hashMap.put("from_time1_pos", f_val2+"");
                    }

                    if(t_val2==-1){
                        hashMap.put("to_time1_pos", "0");
                    }else{
                        hashMap.put("to_time1_pos", t_val2+"");
                    }

                    if(f_val3==-1){
                        hashMap.put("from_time2_pos", "0");
                    }else{
                        hashMap.put("from_time2_pos", f_val3+"");
                    }

                    if(t_val3==-1){
                        hashMap.put("to_time2_pos", "0");
                    }else{
                        hashMap.put("to_time2_pos", t_val3+"");
                    }

                    if(f_val4==-1){
                        hashMap.put("from_time3_pos", "0");
                    }else{
                        hashMap.put("from_time3_pos", f_val4+"");
                    }

                    if(t_val4==-1){
                        hashMap.put("to_time3_pos", "0");
                    }else{
                        hashMap.put("to_time3_pos", t_val4+"");
                    }


                    for (int i= 0; i < TL_AddDoctor.days_list.size(); i++) {
                        if(TL_AddDoctor.days_list.get(i).get("id").equalsIgnoreCase(s_extratimeslot_list.get(k).get("from_days"))){
                            val1=i;
                        }
                        if(TL_AddDoctor.days_list.get(i).get("id").equalsIgnoreCase(s_extratimeslot_list.get(k).get("to_days"))){
                            val2=i;
                        }


                    }
                    if(val1==-1){
                        hashMap.put("from_days_pos", "0");
                        hashMap.put("from_days", "");
                    }else{
                        hashMap.put("from_days_pos", TL_AddDoctor.days_list.get(val1).get("id"));
                        hashMap.put("from_days", TL_AddDoctor.days_list.get(val1).get("day_name"));
                    }

                    if(val2==-1){
                        hashMap.put("to_days_pos", "0");
                        hashMap.put("to_days", "");
                    }else{
                        hashMap.put("to_days_pos", TL_AddDoctor.days_list.get(val2).get("id"));
                        hashMap.put("to_days", TL_AddDoctor.days_list.get(val2).get("day_name"));
                    }



                    extratimeslot_list.add(hashMap);
                }

                adapter = new HashMapRecycleviewadapter(getActivity(), extratimeslot_list, "doc_timeslots", appointment_slot_rv, R.layout.appointment_slot_listitem);
                appointment_slot_rv.setAdapter(adapter);
            }




        }catch (Exception e){

        }

        backbtn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        });

        h_frmday_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                h_today_edtx.setText("");
                AppointmentListPopup(h_frmday_edtx,getActivity());
            }
        });

        h_today_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ToDayListPopup(h_today_edtx,getActivity());
            }
        });

        h_avabletme_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                h_tmetwo_edtx.setText("");
                totimeslot_id_one="";
                h_tmetwo_edtx2.setText("");
                totimeslot_id_two="";
                h_tmetwo_edtx3.setText("");
                totimeslot_id_three="";
                h_tmetwo_edtx4.setText("");
                totimeslot_id_four="";

                h_avabletme_edtx2.setText("");
                fromtimeslot_id_two="";
                h_avabletme_edtx3.setText("");
                fromtimeslot_id_three="";
                h_avabletme_edtx4.setText("");
                fromtimeslot_id_four="";

                FromtimeListPopup(h_avabletme_edtx,getActivity(),"0");
            }
        });

        h_tmetwo_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toTimeListPopup(h_tmetwo_edtx,getActivity(),"0");
            }
        });


        h_avabletme_edtx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                h_tmetwo_edtx2.setText("");
                totimeslot_id_two="";

                h_tmetwo_edtx3.setText("");
                totimeslot_id_three="";
                h_tmetwo_edtx4.setText("");
                totimeslot_id_four="";

                h_avabletme_edtx3.setText("");
                fromtimeslot_id_three="";
                h_avabletme_edtx4.setText("");
                fromtimeslot_id_four="";
                FromtimeListPopup(h_avabletme_edtx2,getActivity(),"1");
            }
        });

        h_tmetwo_edtx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toTimeListPopup(h_tmetwo_edtx2,getActivity(),"1");
            }
        });


        h_avabletme_edtx3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                h_tmetwo_edtx3.setText("");
                totimeslot_id_three="";
                h_tmetwo_edtx4.setText("");
                totimeslot_id_four="";

                h_avabletme_edtx4.setText("");
                fromtimeslot_id_four="";
                FromtimeListPopup(h_avabletme_edtx3,getActivity(),"2");
            }
        });

        h_tmetwo_edtx3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toTimeListPopup(h_tmetwo_edtx3,getActivity(),"2");
            }
        });

        h_avabletme_edtx4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                h_tmetwo_edtx4.setText("");
                totimeslot_id_four="";
                FromtimeListPopup(h_avabletme_edtx4,getActivity(),"3");
            }
        });

        h_tmetwo_edtx4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toTimeListPopup(h_tmetwo_edtx4,getActivity(),"3");
            }
        });

        h_spclztn_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(specialisation_names_list.size()>0){
                    SpeclizatnListPopup((CustomEditText) h_spclztn_edtx,getActivity());
                }else{
                    StoredObjects.ToastMethod("No Data found",getActivity());
                }
            }
        });

        h_add_dr_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CameraUtils.checkAndRequestPermissions(getActivity()) == true) {
                    Imagepickingpopup(getActivity(), "TL add doctors");
                }
            }
        });

        h_mbile_edtx.setEnabled(false);
        hd_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String d_signup_dr_name_str = h_doc_nme_edtx.getText().toString().trim();
                String d_signup_dr_email_str = h_email_edtx.getText().toString().trim();
                String d_signup_dr_regNo_str = h_regnum_edtx.getText().toString().trim();
                String d_signup_dr_spclztn_str = h_spclztn_edtx.getText().toString().trim();
                String d_signup_dr_statboard_str = h_statbard_edtx.getText().toString().trim();
                String d_signup_dr_yrrgstn_str = h_yrrgstn_edtx.getText().toString().trim();
                String d_signup_dr_mobile_str = h_mbile_edtx.getText().toString().trim();
                String d_signup_dr_frmday_str = h_frmday_edtx.getText().toString().trim();
                String d_signup_dr_today_str = h_today_edtx.getText().toString().trim();
                String d_signup_dr_custom_str = h_custm_edtx.getText().toString().trim();
                String d_signup_dr_avabletme_str = h_avabletme_edtx.getText().toString().trim();
                String address = tl_docadres_edtx.getText().toString().trim();

                String qualification = d_qualification_edtx.getText().toString().trim();
                String o_qualification = d_otherqualification_edtx.getText().toString().trim();
                String e_qualification = d_extraqualification_edtx.getText().toString().trim();

                String extratimeslots="";
                JSONArray TimeSlotsArray = new JSONArray();
                JSONObject jsonObject1 = null;
                for (int i= 0;i< extratimeslot_list.size();i++) {
                    try {
                        jsonObject1 = new JSONObject();
                        jsonObject1.put("id",  extratimeslot_list.get(i).get("id"));
                        jsonObject1.put("from_days",  extratimeslot_list.get(i).get("from_days_pos"));
                        jsonObject1.put("to_days",  extratimeslot_list.get(i).get("to_days_pos"));
                        jsonObject1.put("from_time",  extratimeslot_list.get(i).get("from_time"));
                        jsonObject1.put("to_time",  extratimeslot_list.get(i).get("to_time"));
                        jsonObject1.put("from_time1",  extratimeslot_list.get(i).get("from_time1"));
                        jsonObject1.put("to_time1",  extratimeslot_list.get(i).get("to_time1"));

                        jsonObject1.put("from_time2",  extratimeslot_list.get(i).get("from_time2"));
                        jsonObject1.put("to_time2",  extratimeslot_list.get(i).get("to_time2"));

                        jsonObject1.put("from_time3",  extratimeslot_list.get(i).get("from_time3"));
                        jsonObject1.put("to_time3",  extratimeslot_list.get(i).get("to_time3"));
                        TimeSlotsArray.put(jsonObject1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                extratimeslots=TimeSlotsArray.toString();

                if (StoredObjects.inputValidation(h_doc_nme_edtx, getString(R.string.enter_dr_name), getActivity())) {
                    if (StoredObjects.Emailvalidation(d_signup_dr_email_str,getString(R.string.enter_valid_email), getActivity())) {

                        if (StoredObjects.inputValidation(h_regnum_edtx, getString(R.string.reg_validation), getActivity())) {
                            if (StoredObjects.inputValidation(h_spclztn_edtx, getString(R.string.specizlization_validation), getActivity())) {
                                if (StoredObjects.inputValidation(h_yrrgstn_edtx, getString(R.string.year_reg_validation), getActivity())) {
                                    if (StoredObjects.inputValidation(d_qualification_edtx, "Please enter Qualification", getActivity())) {
                                        if (StoredObjects.inputValidation(h_frmday_edtx, getString(R.string.from_day_validate), getActivity())) {
                                            if (StoredObjects.inputValidation(h_today_edtx, getString(R.string.to_day_validate), getActivity())) {
                                                if (StoredObjects.inputValidation(h_avabletme_edtx, getString(R.string.from_time_validate), getActivity())) {
                                                    if (StoredObjects.inputValidation(h_tmetwo_edtx, getString(R.string.to_time_validate), getActivity())) {


                                                        addDoctorService(getActivity(), d_signup_dr_name_str, d_signup_dr_email_str, speacialisation_id, d_signup_dr_statboard_str,
                                                                d_signup_dr_regNo_str, d_signup_dr_yrrgstn_str, d_signup_dr_mobile_str, d_signup_dr_frmday_str, d_signup_dr_today_str,
                                                                d_signup_dr_custom_str, fromtimeslot_id_one, totimeslot_id_one, address,qualification,e_qualification,o_qualification,extratimeslots);


                                                    }
                                                }
                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }


                }


            }
        });





        final LinearLayoutManager linearLayoutManagerone = new LinearLayoutManager(getActivity());
        appointment_slot_rv.setLayoutManager(linearLayoutManagerone);

        h_addoc_add_day_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(extratimeslot_list.size()==0){
                    String d_signup_dr_frmday_str = h_frmday_edtx.getText().toString().trim();
                    String d_signup_dr_today_str = h_today_edtx.getText().toString().trim();
                    String d_signup_dr_avabletme_str = h_avabletme_edtx.getText().toString().trim();
                    String d_signup_dr_tmetwo_str = h_tmetwo_edtx.getText().toString().trim();
                    if(d_signup_dr_frmday_str.length()>0&&d_signup_dr_today_str.length()>0
                            &&d_signup_dr_avabletme_str.length()>0&&d_signup_dr_tmetwo_str.length()>0){
                        addtimeslots();
                        adapter = new HashMapRecycleviewadapter(getActivity(), extratimeslot_list, "doc_timeslots", appointment_slot_rv, R.layout.appointment_slot_listitem);
                        appointment_slot_rv.setAdapter(adapter);
                    }else{
                        StoredObjects.ToastMethod("Please select available days and available timings",getActivity());
                    }

                }else{
                    int count=0;
                    for(int k=0;k<extratimeslot_list.size();k++){
                        if((!extratimeslot_list.get(k).get("from_days").equalsIgnoreCase(""))
                                &&(!extratimeslot_list.get(k).get("to_days").equalsIgnoreCase(""))
                                &&(!extratimeslot_list.get(k).get("from_time").equalsIgnoreCase(""))
                                &&(!extratimeslot_list.get(k).get("to_time").equalsIgnoreCase(""))){

                            count=count+1;
                        }
                    }
                    if(count==extratimeslot_list.size()){
                        addtimeslots();
                        adapter.notifyDataSetChanged();
                    }else{
                        StoredObjects.ToastMethod("Please select available days and available timings",getActivity());

                    }

                }
            }
        });



    }
    public static HashMapRecycleviewadapter adapter;

    private void addtimeslots() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id","0");
        hashMap.put("from_days", "");
        hashMap.put("to_days", "");
        hashMap.put("from_time", "");
        hashMap.put("to_time", "");
        hashMap.put("from_time1", "");
        hashMap.put("to_time1", "");
        hashMap.put("from_time2", "");
        hashMap.put("to_time2", "");
        hashMap.put("from_time3", "");
        hashMap.put("to_time3", "");

        hashMap.put("from_time_pos", "0");
        hashMap.put("to_time_pos", "0");
        hashMap.put("from_time1_pos", "0");
        hashMap.put("to_time1_pos", "0");
        hashMap.put("from_time2_pos", "0");
        hashMap.put("to_time2_pos", "0");
        hashMap.put("from_time3_pos", "0");
        hashMap.put("to_time3_pos", "0");
        hashMap.put("from_days_pos", "0");
        hashMap.put("to_days_pos", "0");
        extratimeslot_list.add(hashMap);
    }


    int pos_one=0,pos_two=0,pos_three=0,pos_four=0;
    int t_pos_one=0,t_pos_two=0,t_pos_three=0;


    ArrayList<String> fromtimeSlots_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> fromtimes_list = new ArrayList<>();


    public int getposition(String selval){
        int value=0;
        for (int k = 0; k < TL_AddDoctor.dummy_times_list.size(); k++) {
            if(selval.equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(k).get("time_slot"))){
                value=k;
            }

        }

        return value;
    }

    private void FromtimeListPopup(final CustomEditText prfilenme, final Activity activity,String type) {



        if(type.equalsIgnoreCase("0")){
            fromtimes_list.clear();
            fromtimeSlots_list.clear();


            for (int k = 0; k < TL_AddDoctor.dummy_times_list.size(); k++) {
                fromtimes_list.add(TL_AddDoctor.dummy_times_list.get(k));
            }

            for (int k = 0; k < fromtimes_list.size(); k++) {
                fromtimeSlots_list.add(StoredObjects.time12hrsformat(fromtimes_list.get(k).get("time_slot")));
            }


            final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
            listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, fromtimeSlots_list));
            listPopupWindowone.setAnchorView(prfilenme);
            listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
            listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    prfilenme.setText(fromtimeSlots_list.get(position));
                    fromtimeslot_id_one=fromtimes_list.get(position).get("time_slot");
                    pos_one=getposition(fromtimes_list.get(position).get("time_slot"));
                    StoredObjects.LogMethod("sel_pos","sel_pos::"+pos_one);
                    listPopupWindowone.dismiss();

                }
            });

            listPopupWindowone.show();
        }else if(type.equalsIgnoreCase("1")){
            fromtimes_list.clear();
            fromtimeSlots_list.clear();


            for (int k = t_pos_one; k < TL_AddDoctor.dummy_times_list.size(); k++) {
                fromtimes_list.add(TL_AddDoctor.dummy_times_list.get(k));
            }


            for (int k = 0; k < fromtimes_list.size(); k++) {
                fromtimeSlots_list.add(StoredObjects.time12hrsformat(fromtimes_list.get(k).get("time_slot")));
            }

            final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
            listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, fromtimeSlots_list));
            listPopupWindowone.setAnchorView(prfilenme);
            listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
            listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    prfilenme.setText(fromtimeSlots_list.get(position));
                    fromtimeslot_id_two=fromtimes_list.get(position).get("time_slot");
                    pos_two=getposition(fromtimes_list.get(position).get("time_slot"));

                    StoredObjects.LogMethod("sel_pos","sel_pos::"+pos_two+":::"+t_pos_one);
                    listPopupWindowone.dismiss();

                }
            });

            listPopupWindowone.show();
        }else if(type.equalsIgnoreCase("2")){
            fromtimes_list.clear();
            fromtimeSlots_list.clear();


            for (int k = t_pos_two; k < TL_AddDoctor.dummy_times_list.size(); k++) {
                fromtimes_list.add(TL_AddDoctor.dummy_times_list.get(k));
            }


            for (int k = 0; k < fromtimes_list.size(); k++) {
                fromtimeSlots_list.add(StoredObjects.time12hrsformat(fromtimes_list.get(k).get("time_slot")));
            }


            final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
            listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, fromtimeSlots_list));
            listPopupWindowone.setAnchorView(prfilenme);
            listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
            listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    prfilenme.setText(fromtimeSlots_list.get(position));
                    fromtimeslot_id_three=fromtimes_list.get(position).get("time_slot");
                    pos_three=getposition(fromtimes_list.get(position).get("time_slot"));;
                    StoredObjects.LogMethod("sel_pos","sel_pos::"+pos_three+":::"+t_pos_two);
                    listPopupWindowone.dismiss();

                }
            });

            listPopupWindowone.show();
        }else if(type.equalsIgnoreCase("3")){
            fromtimes_list.clear();
            fromtimeSlots_list.clear();


            for (int k = t_pos_three; k < TL_AddDoctor.dummy_times_list.size(); k++) {
                fromtimes_list.add(TL_AddDoctor.dummy_times_list.get(k));
            }


            for (int k = 0; k < fromtimes_list.size(); k++) {
                fromtimeSlots_list.add(StoredObjects.time12hrsformat(fromtimes_list.get(k).get("time_slot")));
            }


            final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
            listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, fromtimeSlots_list));
            listPopupWindowone.setAnchorView(prfilenme);
            listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
            listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    prfilenme.setText(fromtimeSlots_list.get(position));
                    fromtimeslot_id_four=fromtimes_list.get(position).get("time_slot");


                    pos_four=getposition(fromtimes_list.get(position).get("time_slot"));
                    StoredObjects.LogMethod("sel_pos","sel_pos::"+pos_four+":::"+t_pos_three);
                    listPopupWindowone.dismiss();

                }
            });

            listPopupWindowone.show();
        }

    }

    ArrayList<String> totimeSlots_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> totimes_list = new ArrayList<>();

    private void toTimeListPopup(final CustomEditText prfilenme,Activity activity,String type) {


        if(type.equalsIgnoreCase("0")){
            totimes_list.clear();
            totimeSlots_list.clear();
            for (int k = pos_one; k < TL_AddDoctor.dummy_times_list.size(); k++) {
                totimes_list.add(TL_AddDoctor.dummy_times_list.get(k));
            }


            for (int k = 0; k < totimes_list.size(); k++) {
                totimeSlots_list.add(StoredObjects.time12hrsformat(totimes_list.get(k).get("time_slot")));
            }


            final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
            listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, totimeSlots_list));
            listPopupWindowone.setAnchorView(prfilenme);
            listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
            listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    prfilenme.setText(totimeSlots_list.get(position));
                    totimeslot_id_one=totimes_list.get(position).get("time_slot");
                    t_pos_one=getposition(totimes_list.get(position).get("time_slot"));
                    StoredObjects.LogMethod("sel_pos","sel_pos::"+pos_one+":::"+t_pos_one);
                    listPopupWindowone.dismiss();

                }
            });

            listPopupWindowone.show();
        }else  if(type.equalsIgnoreCase("1")){
            totimes_list.clear();
            totimeSlots_list.clear();
            for (int k = pos_two; k < TL_AddDoctor.dummy_times_list.size(); k++) {
                totimes_list.add(TL_AddDoctor.dummy_times_list.get(k));
            }


            for (int k = 0; k < totimes_list.size(); k++) {
                totimeSlots_list.add(StoredObjects.time12hrsformat(totimes_list.get(k).get("time_slot")));
            }
            final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
            listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, totimeSlots_list));
            listPopupWindowone.setAnchorView(prfilenme);
            listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
            listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    prfilenme.setText(totimeSlots_list.get(position));
                    totimeslot_id_two=totimes_list.get(position).get("time_slot");
                    t_pos_two=getposition(totimes_list.get(position).get("time_slot"));
                    StoredObjects.LogMethod("sel_pos","sel_pos::"+pos_two+":::"+t_pos_two);
                    listPopupWindowone.dismiss();

                }
            });

            listPopupWindowone.show();
        }else  if(type.equalsIgnoreCase("2")){
            totimes_list.clear();
            totimeSlots_list.clear();
            for (int k = pos_three; k < TL_AddDoctor.dummy_times_list.size(); k++) {
                totimes_list.add(TL_AddDoctor.dummy_times_list.get(k));
            }


            for (int k = 0; k < totimes_list.size(); k++) {
                totimeSlots_list.add(StoredObjects.time12hrsformat(totimes_list.get(k).get("time_slot")));
            }



            final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
            listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, totimeSlots_list));
            listPopupWindowone.setAnchorView(prfilenme);
            listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
            listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    prfilenme.setText(totimeSlots_list.get(position));
                    totimeslot_id_three=totimes_list.get(position).get("time_slot");
                    t_pos_three=getposition(totimes_list.get(position).get("time_slot"));

                    StoredObjects.LogMethod("sel_pos","sel_pos::"+pos_three+":::"+t_pos_three);
                    listPopupWindowone.dismiss();

                }
            });

            listPopupWindowone.show();
        }else  if(type.equalsIgnoreCase("3")){
            totimes_list.clear();
            totimeSlots_list.clear();
            for (int k = pos_four; k < TL_AddDoctor.dummy_times_list.size(); k++) {
                totimes_list.add(TL_AddDoctor.dummy_times_list.get(k));
            }


            for (int k = 0; k < totimes_list.size(); k++) {
                totimeSlots_list.add(StoredObjects.time12hrsformat(totimes_list.get(k).get("time_slot")));
            }

            final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
            listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, totimeSlots_list));
            listPopupWindowone.setAnchorView(prfilenme);
            listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
            listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    prfilenme.setText(totimeSlots_list.get(position));
                    totimeslot_id_four=totimes_list.get(position).get("time_slot");
                    StoredObjects.LogMethod("sel_pos","sel_pos::"+pos_four);
                    listPopupWindowone.dismiss();

                }
            });

            listPopupWindowone.show();
        }

    }


    private void SpeclizatnListPopup(final CustomEditText prfilenme,Activity activity) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
        listPopupWindow.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, specialisation_names_list));
        listPopupWindow.setAnchorView(prfilenme);
        listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                speacialisation_id = specialization_list.get(position).get("specialization_id");
                prfilenme.setText(specialisation_names_list.get(position));
                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
    }

    String from_id="",to_id="";
    private void AppointmentListPopup(final CustomEditText prfilenme,Activity activity) {
        final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
        listPopupWindowone.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.drpdwn_lay, TL_AddDoctor.daynames_list));
        listPopupWindowone.setAnchorView(prfilenme);
        listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prfilenme.setText(TL_AddDoctor.daynames_list.get(position));
                from_id = TL_AddDoctor.days_list.get(position).get("id");
                getToDaysService(getActivity(), from_id);
                listPopupWindowone.dismiss();

            }
        });

        listPopupWindowone.show();
    }

    private void ToDayListPopup(final CustomEditText prfilenme,Activity activity) {
        final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
        listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, to_daynames_list));
        listPopupWindowone.setAnchorView(prfilenme);
        listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prfilenme.setText(to_daynames_list.get(position));
                to_id = to_days_list.get(position).get("id");
                listPopupWindowone.dismiss();

            }
        });
        listPopupWindowone.show();
    }






    public void get_weekdays(final Activity activity) {

        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.getweekdays(RetrofitInstance.week_days).enqueue(new Callback<ResponseBody>() {
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
                            TL_AddDoctor.days_list.clear();
                            TL_AddDoctor.days_list = JsonParsing.GetJsonData(results);
                            TL_AddDoctor.daynames_list.clear();

                            for (int k = 0; k <  TL_AddDoctor.days_list.size(); k++) {
                                TL_AddDoctor.daynames_list.add( TL_AddDoctor.days_list.get(k).get("day_name"));

                            }

                        } else {
                            StoredObjects.ToastMethod("No Data found", activity);
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    //CustomProgressbar.Progressbarcancel(activity);


                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                        getTimeSlots(getActivity());
                    } else {
                        StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomProgressbar.Progressbarcancel(activity);


            }
        });
    }

    private void getToDaysService(final Activity activity, String days_id) {

        if(first_time.equalsIgnoreCase("Yes")){

        }else {
            CustomProgressbar.Progressbarshow(activity);
        }

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.getToDays(RetrofitInstance.to_days, days_id).enqueue(new Callback<ResponseBody>() {
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
                            to_days_list = JsonParsing.GetJsonData(results);
                            to_daynames_list.clear();
                            for (int k = 0; k < to_days_list.size(); k++) {
                                to_daynames_list.add(to_days_list.get(k).get("day_name"));

                            }

                        } else {

                            StoredObjects.ToastMethod("No Data found", activity);
                        }



                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                     CustomProgressbar.Progressbarcancel(activity);

                    if(first_time.equalsIgnoreCase("Yes")){

                        for(int k=0;k<to_days_list.size();k++){
                            if(to_days_list.get(k).get("day_name").equalsIgnoreCase(to_day)){
                                to_id = to_days_list.get(k).get("id");
                            }
                        }
                        //getToTimeService(getActivity(), fromtimeslot_id_one);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomProgressbar.Progressbarcancel(activity);

            }
        });
    }

    private void getTimeSlots(final Activity activity) {

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.getTimeSlots(RetrofitInstance.time_slots).enqueue(new Callback<ResponseBody>() {
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
                            TL_AddDoctor.dummy_times_list.clear();
                            TL_AddDoctor.dummy_times_list = JsonParsing.GetJsonData(results);
                            TL_AddDoctor. dummy_timeSlot_list.clear();
                            for (int k = 0; k < TL_AddDoctor.dummy_times_list.size(); k++) {

                                if(fromtimeslot_id_one.equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(k).get("time_slot"))){
                                    pos_one=k;
                                }
                                if(fromtimeslot_id_two.equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(k).get("time_slot"))){
                                    pos_two=k;
                                }
                                if(fromtimeslot_id_three.equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(k).get("time_slot"))){
                                    pos_three=k;
                                }
                                if(fromtimeslot_id_four.equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(k).get("time_slot"))){
                                    pos_four=k;
                                }

                                if(totimeslot_id_one.equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(k).get("time_slot"))){
                                    t_pos_one=k;
                                }

                                if(totimeslot_id_two.equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(k).get("time_slot"))){
                                    t_pos_two=k;
                                }

                                if(totimeslot_id_three.equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(k).get("time_slot"))){
                                    t_pos_three=k;
                                }


                                TL_AddDoctor.dummy_timeSlot_list.add(StoredObjects.time12hrsformat(TL_AddDoctor.dummy_times_list.get(k).get("time_slot")));
                            }

                            fromtimeSlots_list.clear();
                            fromtimes_list.clear();


                            fromtimes_list.addAll(TL_AddDoctor.dummy_times_list);
                            fromtimeSlots_list.addAll(TL_AddDoctor.dummy_timeSlot_list);



                        } else {
                            StoredObjects.ToastMethod("No Data found", activity);
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                        getDrSpecializationService(getActivity());
                    } else {
                        StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomProgressbar.Progressbarcancel(activity);

            }
        });
    }



    private void getDrSpecializationService(final Activity activity) {
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.getDrSpecialization(RetrofitInstance.dr_specializtion).enqueue(new Callback<ResponseBody>() {
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
                            specialization_list = JsonParsing.GetJsonData(results);
                            specialisation_names_list.clear();
                            for (int k = 0; k < specialization_list.size(); k++) {
                                specialisation_names_list.add(specialization_list.get(k).get("name"));
                            }

                        } else {
                            StoredObjects.ToastMethod("No Data found", activity);
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                    for(int k=0;k<TL_AddDoctor.days_list.size();k++){
                        if(TL_AddDoctor.days_list.get(k).get("day_name").equalsIgnoreCase(from_day)){
                            from_id = TL_AddDoctor.days_list.get(k).get("id");
                        }
                    }
                    getToDaysService(getActivity(), from_id);
                   //CustomProgressbar.Progressbarcancel(activity);

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                CustomProgressbar.Progressbarcancel(activity);


            }
        });
    }

    private void addDoctorService(final FragmentActivity activity, String d_signup_dr_name_str, String d_signup_dr_email_str, String d_signup_dr_spclztn_str, String d_signup_dr_statboard_str, String d_signup_dr_regNo_str , String d_signup_dr_yrrgstn_str, String d_signup_dr_mobile_str, String d_signup_dr_frmday_str, String d_signup_dr_today_str, String d_signup_dr_custom_str,
                                  String d_signup_dr_avabletme_str, String d_signup_dr_tmetwo_str,String address,String qualify,String e_qualify,String o_qualify,String extratimeslots) {
        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.tl_editDoctor(RetrofitInstance.edit_doctor,StoredObjects.UserId,StoredObjects.UserRoleId,
                d_signup_dr_name_str,d_signup_dr_email_str,d_signup_dr_regNo_str,d_signup_dr_spclztn_str,d_signup_dr_statboard_str
                ,d_signup_dr_yrrgstn_str,d_signup_dr_mobile_str,from_id,to_id,d_signup_dr_custom_str,
                fromtimeslot_id_one,totimeslot_id_one,fromtimeslot_id_two,totimeslot_id_two,
                fromtimeslot_id_three,totimeslot_id_three,fromtimeslot_id_four,totimeslot_id_four,user_id,address,qualify,e_qualify,o_qualify,extratimeslots).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseRecieved = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseRecieved);
                        StoredObjects.LogMethod("response", "response::" + responseRecieved);
                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("200")) {
                            StoredObjects.ToastMethod("Edited Successfully", activity);
                            fragmentcallinglay(new TL_Doctors());
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
                postFile(realPath, RetrofitInstance.BASEURL + "app/index.php", file.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.nointernet), getActivity());
        }


    }

    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    public void postFile(String encodedImage, String postUrl, String fileName) {


        CustomProgressbar.Progressbarshow(getActivity());
        okhttp3.Response response = null;

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
                CustomProgressbar.Progressbarcancel(getActivity());
            } else {
                CustomProgressbar.Progressbarcancel(getActivity());
            }
            StoredObjects.LogMethod("val", "val::" + responseReceived);
        } catch (IOException e) {

        }


    }
}


