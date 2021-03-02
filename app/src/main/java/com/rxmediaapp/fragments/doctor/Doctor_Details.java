package com.rxmediaapp.fragments.doctor;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rxmediaapp.R;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;
import com.rxmediaapp.Sidemenu.SideMenu;

import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customfonts.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Doctor_Details extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    EditText doc_attender_edtx;
    public static TextView nodatavailable_txt;
    public static RecyclerView doc_details_recyler;
    public static HashMapRecycleviewadapter adapter;

    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();

    public static ArrayList<HashMap<String, String>> details_list = new ArrayList<>();


    String[] tabslist = {"Patients List","Prescriptions List","Test Suggested List"};


    ImageView ddocname_img;
    TextView ddocname_txt,ddocspc_txt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.doctor_details,null,false );
        StoredObjects.page_type="doctor_details";
        SideMenu.updatemenu(StoredObjects.page_type);

        initilization(v);

        //serviceCalling();
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        doc_details_recyler = v.findViewById( R.id. doc_details_recyler);
        doc_attender_edtx = v.findViewById( R.id. doc_attender_edtx);
        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        ddocname_img=v.findViewById(R.id.ddocname_img);
        ddocname_txt=v.findViewById(R.id.ddocname_txt);
        ddocspc_txt=v.findViewById(R.id.ddocspc_txt);

        title_txt.setText( "Doctor Details" );

        try {
            ddocname_txt.setText( Doctor_Details.details_list.get(0).get("name"));
            ddocspc_txt.setText( Doctor_Details.details_list.get(0).get("specialization"));
            doc_attender_edtx.setText( Doctor_Details.details_list.get(0).get("assistant_name"));
            try {
                Glide.with(getActivity())
                        .load(Uri.parse(RetrofitInstance.IMAGE_URL + Doctor_Details.details_list.get(0).get("image")))
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.no_image)
                                .fitCenter()
                                .centerCrop())
                        .into(ddocname_img);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }catch (Exception e){

        }



        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        });

        doc_attender_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //AsignDctrsLstPopup ((CustomEditText) doc_attender_edtx);
            }
        });


        StoredObjects.getrray(tabslist);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        doc_details_recyler.setLayoutManager(linearLayoutManager);

        adapter = new HashMapRecycleviewadapter(getActivity(), StoredObjects.menuitems_list, "doc_details", doc_details_recyler, R.layout.doc_details_listitem);
        doc_details_recyler.setAdapter(adapter);

    }

   public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

    }

   /* private void AsignDctrsLstPopup(final CustomEditText prfilenme){
        listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.drpdwn_lay,doctrslist));
        listPopupWindow.setAnchorView(prfilenme);
        listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //prfilenme.setText(doctrslist[position]);
                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
    }*/

    private void updatelay(ArrayList<HashMap<String, String>> data_list) {

        if(data_list.size()==0){
            nodatavailable_txt.setVisibility(View.VISIBLE);
            doc_details_recyler.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            doc_details_recyler.setVisibility(View.VISIBLE);
        }
    }




}


