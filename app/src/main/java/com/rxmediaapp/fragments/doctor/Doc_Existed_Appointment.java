package com.rxmediaapp.fragments.doctor;

import android.app.Activity;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customadapter.HashmapViewHolder;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.fragments.assistant.Add_Appointment;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Doc_Existed_Appointment extends Fragment {

    ImageView backbtn_img;
    TextView title_txt,appointment_name_txt,appointment_gender_txt,appointment_bloodgroup_txt,appointment_age_txt,appointment_consultdate_txt;
    LinearLayout addphy_ex_lay;
    EditText appointment_dueto_edtxt;
    Button addapp_btn;

    String patient_id="";


    EditText select_patient_edtx,pat_mbile_edtx;

    public  ArrayList<String> patlist = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.add_appointment_one,null,false );
        StoredObjects.page_type="add_appointment_one";
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        AssignData();
        return v;
    }

    private void AssignData() {
        pat_mbile_edtx.setVisibility(View.GONE);
        appointment_name_txt.setText(Doc_Sel_Patient.p_data_list.get(0).get("name"));
        appointment_gender_txt.setText(Doc_Sel_Patient.p_data_list.get(0).get("gender"));
        appointment_bloodgroup_txt.setText(Doc_Sel_Patient.p_data_list.get(0).get("blood_group"));
        appointment_age_txt.setText(Doc_Sel_Patient.p_data_list.get(0).get("age"));
        appointment_consultdate_txt.setText(Doc_Sel_Patient.p_data_list.get(0).get("last_consulted_date"));

        patient_id=Doc_Sel_Patient.p_data_list.get(0).get("user_id");

        select_patient_edtx.setText(Doc_Sel_Patient.p_data_list.get(0).get("name"));

        if(Doc_Sel_Patient.p_data_list.size()==1){
            select_patient_edtx.setVisibility(View.GONE);
        }else{
            select_patient_edtx.setVisibility(View.VISIBLE);
            patlist.clear();
            for(int k=0;k<Doc_Sel_Patient.p_data_list.size();k++){
                if(Doc_Sel_Patient.p_data_list.get(k).get("relation").equalsIgnoreCase("")){
                    patlist.add(Doc_Sel_Patient.p_data_list.get(k).get("name"));
                }else{
                    patlist.add(Doc_Sel_Patient.p_data_list.get(k).get("name")+" ("+Doc_Sel_Patient.p_data_list.get(k).get("relation")+")");
                }

            }
        }
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        addphy_ex_lay = v.findViewById( R.id. addphy_ex_lay);
        addapp_btn = v.findViewById( R.id. addapp_btn);

        appointment_name_txt = v.findViewById( R.id. appointment_name_txt);
        appointment_gender_txt= v.findViewById( R.id. appointment_gender_txt);
        appointment_bloodgroup_txt = v.findViewById( R.id. appointment_bloodgroup_txt);
        appointment_age_txt = v.findViewById( R.id. appointment_age_txt);
        appointment_consultdate_txt = v.findViewById( R.id. appointment_consultdate_txt);
        appointment_dueto_edtxt = v.findViewById( R.id. appointment_dueto_edtxt);
        select_patient_edtx = v.findViewById( R.id. select_patient_edtx);
        pat_mbile_edtx = v.findViewById( R.id. pat_mbile_edtx);

        select_patient_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PatListPopup(select_patient_edtx,getActivity());
            }
        });


        title_txt.setText( "Add Appointment" );

        appointment_dueto_edtxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        appointment_dueto_edtxt.setRawInputType(InputType.TYPE_CLASS_TEXT);


        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        } );
        addphy_ex_lay.setVisibility(View.GONE);


        addapp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String consult_str = appointment_dueto_edtxt.getText().toString().trim();

                if (StoredObjects.inputValidation(appointment_dueto_edtxt, getActivity().getResources().getString(R.string.enter_dueto), getActivity())) {

                    if (InterNetChecker.isNetworkAvailable(getActivity())) {
                        AddAppointmentService(getActivity(),consult_str);
                    } else {
                        StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                    }


                }


            }
        });




    }


    private void PatListPopup(final EditText prfilenme,Activity activity){
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
        listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.drpdwn_lay,patlist));
        listPopupWindow.setAnchorView(prfilenme);
        listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prfilenme.setText(patlist.get(position));


                appointment_name_txt.setText(Doc_Sel_Patient.p_data_list.get(position).get("name"));
                appointment_gender_txt.setText(Doc_Sel_Patient.p_data_list.get(position).get("gender"));
                appointment_bloodgroup_txt.setText(Doc_Sel_Patient.p_data_list.get(position).get("blood_group"));
                appointment_age_txt.setText(Doc_Sel_Patient.p_data_list.get(position).get("age"));
                appointment_consultdate_txt.setText(Doc_Sel_Patient.p_data_list.get(position).get("last_consulted_date"));

                patient_id=Doc_Sel_Patient.p_data_list.get(position).get("user_id");
                pat_mbile_edtx.setText(Doc_Sel_Patient.p_data_list.get(position).get("phone"));

                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
    }
    private void AddAppointmentService(final FragmentActivity activity, String consult_str) {
        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.DocAddAppointment(RetrofitInstance.doc_add_appointment,patient_id,Doc_Sel_Patient.Hospital_ID,consult_str, StoredObjects.UserId,StoredObjects.UserId,StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).commit ();

    }


}



