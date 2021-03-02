package com.rxmediaapp.fragments.patient;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;

import java.util.ArrayList;
import java.util.HashMap;

public class Diagnosis_Reports_Details  extends Fragment {

    ImageView backbtn_img,diagnose_report_img;
    TextView title_txt,diagnose_date_txt,diagnose_name_txt,diagnose_docname_txt,diagnose_remarks_txt,
            diagnose_enrole_txt,diagnose_patientname_txt,diagnose_reflabname_txt,diagnose_testname_txt;
    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.diagnosis_reports_details,null,false );
        StoredObjects.page_type="diagnosis_reports_details";

        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        assignData();
        return v;
    }

    private void assignData() {
        try {
            try {
                diagnose_date_txt.setText(StoredObjects.convertMonthformat(data_list.get(0).get("report_date")));
            }catch (Exception e){
                diagnose_date_txt.setText(data_list.get(0).get("report_date"));
            }

            diagnose_name_txt.setText(data_list.get(0).get("diagnostic_name"));
            diagnose_docname_txt.setText(data_list.get(0).get("ref_doctor_name"));
            diagnose_remarks_txt.setText(data_list.get(0).get("report_details"));
            diagnose_enrole_txt .setText(data_list.get(0).get("id"));
            diagnose_patientname_txt .setText(data_list.get(0).get("name"));
            diagnose_reflabname_txt.setText(data_list.get(0).get("referred_lab"));
            diagnose_testname_txt .setText(data_list.get(0).get("test_name"));


            try {
                Glide.with(getActivity())
                        .load(Uri.parse(RetrofitInstance.IMAGE_URL + data_list.get(0).get("report_image")))
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.no_image)
                                .fitCenter()
                                .centerCrop())
                        .into(diagnose_report_img);
            } catch (Exception e) {
                e.printStackTrace();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);

        diagnose_report_img = v.findViewById( R.id. diagnose_report_img);
        diagnose_date_txt= v.findViewById( R.id. diagnose_date_txt);
        diagnose_name_txt = v.findViewById( R.id. diagnose_name_txt);
        diagnose_docname_txt= v.findViewById( R.id. diagnose_docname_txt);
        diagnose_remarks_txt = v.findViewById( R.id. diagnose_remarks_txt);
        diagnose_enrole_txt = v.findViewById( R.id. diagnose_enrole_txt);
        diagnose_patientname_txt = v.findViewById( R.id. diagnose_patientname_txt);
        diagnose_reflabname_txt = v.findViewById( R.id. diagnose_reflabname_txt);
        diagnose_testname_txt = v.findViewById( R.id. diagnose_testname_txt);


        title_txt.setText( "Diagnosis Reports Details" );


        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        } );


    }
    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

    }




}


