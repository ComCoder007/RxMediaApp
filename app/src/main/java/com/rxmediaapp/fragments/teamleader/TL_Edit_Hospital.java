package com.rxmediaapp.fragments.teamleader;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TL_Edit_Hospital extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    EditText tl_avabletme_edtx,hsp_passwd_edtx,tl_tmetwo_edtx,tl_adres_edtx, tl_hsptlnme_edtx,tl_email_edtx,tl_hsptlrgstno_edtx,tl_beds_edtx,tl_mbile_edtx;
    Button tl_save_btn;
    ArrayList<String> From_timeSlot_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> from_times_list = new ArrayList<>();

    ArrayList<String> to_timeSlot_list = new ArrayList<>();
    ArrayList<HashMap<String, String>> to_times_list = new ArrayList<>();

    public static ArrayList<HashMap<String, String>> editarraylist = new ArrayList<>();
    String fromtimeslot_id="",totimeslot_id="",first_time="yes",user_id="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.tl_edit_hospital,null,false );
        StoredObjects.page_type="tl_add_hospital";

        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);

        return v;
    }

    private void serviceCalling() {
        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            getTimeSlots(getActivity());
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
        }
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        tl_save_btn = v.findViewById( R.id. tl_save_btn);
        tl_avabletme_edtx= v.findViewById( R.id. tl_avabletme_edtx);
        tl_tmetwo_edtx = v.findViewById( R.id. tl_tmetwo_edtx);
        tl_adres_edtx= v.findViewById( R.id. tl_adres_edtx);
        tl_hsptlnme_edtx = v.findViewById( R.id. tl_hsptlnme_edtx);
        tl_email_edtx= v.findViewById( R.id. tl_email_edtx);
        tl_hsptlrgstno_edtx = v.findViewById( R.id. tl_hsptlrgstno_edtx);
        tl_beds_edtx= v.findViewById( R.id. tl_beds_edtx);
        tl_mbile_edtx = v.findViewById( R.id. tl_mbile_edtx);
        hsp_passwd_edtx=v.findViewById(R.id.hsp_passwd_edtx);

        hsp_passwd_edtx.setVisibility(View.GONE);

        tl_adres_edtx.setImeOptions(EditorInfo.IME_ACTION_DONE);
        tl_adres_edtx.setRawInputType(InputType.TYPE_CLASS_TEXT);

        title_txt.setText( "Edit Hospital" );

        try {
            fromtimeslot_id=editarraylist.get(0).get("from_time");
            totimeslot_id=editarraylist.get(0).get("to_time");
            user_id=editarraylist.get(0).get("hospital_id");



            tl_hsptlnme_edtx.setText(editarraylist.get(0).get("name"));
            tl_email_edtx.setText(editarraylist.get(0).get("email"));
            tl_mbile_edtx.setText(editarraylist.get(0).get("phone"));
            tl_adres_edtx.setText(editarraylist.get(0).get("address"));
            tl_beds_edtx.setText(editarraylist.get(0).get("no_of_beds"));
            tl_hsptlrgstno_edtx.setText(editarraylist.get(0).get("hospital_registration_number"));

            tl_avabletme_edtx.setText(StoredObjects.time12hrsformat(fromtimeslot_id));
            tl_tmetwo_edtx.setText(StoredObjects.time12hrsformat(totimeslot_id));

        }catch (Exception e){

        }
        serviceCalling();
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        } );

        tl_avabletme_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tl_tmetwo_edtx.setText("");
                totimeslot_id="";
                timeListPopup((CustomEditText) tl_avabletme_edtx,getActivity());
            }
        });

        tl_tmetwo_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toTimeListPopup((CustomEditText) tl_tmetwo_edtx,getActivity());
            }
        });
        tl_mbile_edtx.setEnabled(false);

        tl_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hospital_nme_str = tl_hsptlnme_edtx.getText().toString().trim();
                String hospital_email_str = tl_email_edtx.getText().toString().trim();
                String hospital_reg_str = tl_hsptlrgstno_edtx.getText().toString().trim();
                String hospital_beds_str = tl_beds_edtx.getText().toString().trim();
                String hospital_mobile_str = tl_mbile_edtx.getText().toString().trim();
                String hospital_address_str = tl_adres_edtx.getText().toString().trim();




                if(StoredObjects.inputValidation(tl_hsptlnme_edtx,getString(R.string.enter_dr_name),getActivity())) {
                    if(StoredObjects.Emailvalidation(hospital_email_str,getString(R.string.enter_valid_email),getActivity())){
                        if(StoredObjects.inputValidation(tl_hsptlrgstno_edtx,getString(R.string.year_reg_validation),getActivity())) {
                            if(StoredObjects.inputValidation(tl_beds_edtx,getString(R.string.no_beds_validate),getActivity())) {
                                        if (StoredObjects.inputValidation(tl_adres_edtx, getString(R.string.address_validation), getActivity())) {
                                            if (StoredObjects.inputValidation(tl_avabletme_edtx, getString(R.string.from_time_validate), getActivity())) {
                                                if (StoredObjects.inputValidation(tl_tmetwo_edtx, getString(R.string.to_time_validate), getActivity())) {


                                                    addHospitalService(getActivity(), hospital_nme_str, hospital_email_str, hospital_reg_str, hospital_beds_str, hospital_mobile_str, hospital_address_str, fromtimeslot_id, totimeslot_id);


                                                }
                                            }
                                        }

                            }
                        }
                    }


                }

            }
        });


    }

    private void addHospitalService(final FragmentActivity activity, String hospital_nme_str, String hospital_email_str, String hospital_reg_str, String hospital_beds_str, String hospital_mobile_str, String hospital_address_str, String hospital_avabletme_str, String hospital_tmetwo_str) {
        CustomProgressbar.Progressbarshow(activity);
        StoredObjects.LogMethod("response", "response::" + RetrofitInstance.edit_hospitals+"&"+hospital_nme_str+"&"+hospital_email_str+"&"+
                hospital_reg_str+"&"+hospital_beds_str+"&"+hospital_mobile_str+"&"+hospital_address_str+"&"+hospital_avabletme_str+"&"+
                hospital_tmetwo_str+"&"+StoredObjects.UserId+"&"+StoredObjects.UserRoleId+"&"+user_id);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.edithospital(RetrofitInstance.edit_hospitals,hospital_nme_str,hospital_email_str,hospital_reg_str,hospital_beds_str,hospital_mobile_str,hospital_address_str,hospital_avabletme_str,hospital_tmetwo_str,StoredObjects.UserId, StoredObjects.UserRoleId,user_id).enqueue(new Callback<ResponseBody>() {
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
                            fragmentcallinglay(new TL_Hospitals());
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


    private void timeListPopup(final CustomEditText prfilenme, final Activity activity) {
        final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
        listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, From_timeSlot_list));
        listPopupWindowone.setAnchorView(prfilenme);
        listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
        listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prfilenme.setText(From_timeSlot_list.get(position));
                String time_selected = From_timeSlot_list.get(position);
                fromtimeslot_id=from_times_list.get(position).get("time_slot");
                getToTimeService(activity, time_selected);
                listPopupWindowone.dismiss();

            }
        });

        listPopupWindowone.show();
    }

    private void toTimeListPopup(final CustomEditText prfilenme,Activity activity) {
        final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
        listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, to_timeSlot_list));
        listPopupWindowone.setAnchorView(prfilenme);
        listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
        listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prfilenme.setText(to_timeSlot_list.get(position));
                totimeslot_id=to_times_list.get(position).get("time_slot");
                listPopupWindowone.dismiss();

            }
        });

        listPopupWindowone.show();
    }





    private void getTimeSlots(final Activity activity) {
        CustomProgressbar.Progressbarshow(activity);

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
                            from_times_list = JsonParsing.GetJsonData(results);
                            From_timeSlot_list.clear();

                            for (int k = 0; k < from_times_list.size(); k++) {

                                From_timeSlot_list.add(StoredObjects.time12hrsformat(from_times_list.get(k).get("time_slot")));
                            }

                        } else {
                            StoredObjects.ToastMethod("No Data found", activity);
                        }



                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    if(first_time.equalsIgnoreCase("yes")){

                        getToTimeService(getActivity(), fromtimeslot_id);
                    }
                }

               // CustomProgressbar.Progressbarcancel(activity);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CustomProgressbar.Progressbarcancel(activity);

            }
        });
    }

    private void getToTimeService(final Activity activity, String time_selected) {

        if(first_time.equalsIgnoreCase("yes")){



        }else{
            CustomProgressbar.Progressbarshow(getActivity());
        }
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.getToTime(RetrofitInstance.to_time, time_selected).enqueue(new Callback<ResponseBody>() {
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
                            to_times_list = JsonParsing.GetJsonData(results);
                            to_timeSlot_list.clear();
                            for (int k = 0; k < to_times_list.size(); k++) {

                                to_timeSlot_list.add(StoredObjects.time12hrsformat(to_times_list.get(k).get("time_slot")));
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




    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

    }


}

