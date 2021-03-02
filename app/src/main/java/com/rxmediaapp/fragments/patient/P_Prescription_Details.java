package com.rxmediaapp.fragments.patient;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.fragments.hospital.H_OtherDoc_Prescriptions;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;


public class P_Prescription_Details extends Fragment {

    ImageView backbtn_img;
    TextView title_txt,otherdoc_txt,image_upload_txt,datetime_txt,problem_txt,pp_docname_txt;
    LinearLayout prescription_lay;
    ImageView document_img;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.patient_prescription_details,null,false );
        StoredObjects.page_type="prescription_details";

        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        otherdoc_txt = v.findViewById( R.id. otherdoc_txt);
        document_img = v.findViewById( R.id. document_img);
        prescription_lay = v.findViewById( R.id. prescription_lay);
        image_upload_txt= v.findViewById( R.id. image_upload_txt);
        datetime_txt = v.findViewById( R.id. datetime_txt);
        problem_txt = v.findViewById( R.id. problem_txt);
        pp_docname_txt = v.findViewById( R.id. pp_docname_txt);

        title_txt.setText( "Prescription Details" );

        try {
            pp_docname_txt.setText(H_OtherDoc_Prescriptions.data_list.get(0).get("doctor_name"));
            problem_txt.setText(H_OtherDoc_Prescriptions.data_list.get(0).get("problem"));




            try {
                Glide.with(getActivity())
                        .load(Uri.parse(RetrofitInstance.IMAGE_URL + H_OtherDoc_Prescriptions.data_list.get(0).get("scanned_copy")))
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.no_image)
                                .fitCenter()
                                .centerCrop())
                        .into(document_img);
            } catch (Exception e) {
                e.printStackTrace();

            }
            try {
                datetime_txt.setText(StoredObjects.convertfullDateTimeformat(H_OtherDoc_Prescriptions.data_list.get(0).get("appointment_date_time")));

            }catch (Exception e){
                datetime_txt.setText(H_OtherDoc_Prescriptions.data_list.get(0).get("appointment_date_time"));

            }
            try {
                image_upload_txt.setText(" Image uploaded on "+StoredObjects.convertfullDateTimeformat(H_OtherDoc_Prescriptions.data_list.get(0).get("updated_at")));

            }catch (Exception e){
                image_upload_txt.setText(" Image uploaded on "+H_OtherDoc_Prescriptions.data_list.get(0).get("updated_at"));

            }
        }catch (Exception e){

        }

        prescription_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // fragmentcallinglay(new H_Patient_Details());

            }
        });

        document_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //fragmentcallinglay(new Doc_Patient_Details());

            }
        });



        otherdoc_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // fragmentcallinglay(new Members_Details());

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




    }
    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

    }




}


