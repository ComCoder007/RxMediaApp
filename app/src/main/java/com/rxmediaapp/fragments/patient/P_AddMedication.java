package com.rxmediaapp.fragments.patient;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.fragments.doctor.Doc_Patient_Details;
import com.rxmediaapp.storedobjects.StoredObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class P_AddMedication extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    CustomButton search_button;
    RecyclerView doc_medication_rv;
    LinearLayout addmedical_lay;
    public static HashMapRecycleviewadapter medi_adapter;
    public  static ArrayList<HashMap<String, String>> medications_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.add_medication,null,false );
        StoredObjects.page_type="pat_add_medication";

        SideMenu.updatemenu(StoredObjects.page_type);

        initilization(v);

        return v;
    }

    public static void medicationlist(){
        HashMap<String,String> hashMap1=new HashMap<>();
        hashMap1.put("brand_id","");
        hashMap1.put("brand_name","");
        hashMap1.put("molecule_id","");
        hashMap1.put("molecule","");
        hashMap1.put("no_of_times","");
        hashMap1.put("intake","");
        hashMap1.put("no_of_days","");
        hashMap1.put("dose","");
        hashMap1.put("remarks","");
        medications_list.add(hashMap1);
    }
    private void initilization(View dialog) {

        backbtn_img = dialog.findViewById( R.id. backbtn_img);
        title_txt= dialog.findViewById( R.id. title_txt);

        title_txt.setText( "Medication" );

        search_button = (CustomButton) dialog.findViewById(R.id.search_button);
        doc_medication_rv = (RecyclerView) dialog.findViewById(R.id.doc_medication_rv);
        addmedical_lay = (LinearLayout) dialog.findViewById(R.id.addmedical_lay);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        doc_medication_rv.setLayoutManager(linearLayoutManager);
        medi_adapter=new HashMapRecycleviewadapter(getActivity(),medications_list, "medication_view", doc_medication_rv, R.layout.doc_medication_listitem);
        doc_medication_rv.setAdapter( medi_adapter);

        if(medications_list.size()==0){
            medicationlist();
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
        addmedical_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int val=0;
                for(int k=0;k<medications_list.size();k++){
                    if(medications_list.get(k).get("brand_id").equalsIgnoreCase("")||
                            medications_list.get(k).get("brand_name").equalsIgnoreCase("")
                            ||medications_list.get(k).get("molecule_id").equalsIgnoreCase("")||
                            medications_list.get(k).get("molecule").equalsIgnoreCase("")||
                            medications_list.get(k).get("no_of_times").equalsIgnoreCase("")||
                            medications_list.get(k).get("intake").equalsIgnoreCase("")||
                            medications_list.get(k).get("no_of_days").equalsIgnoreCase("")||
                            medications_list.get(k).get("dose").equalsIgnoreCase("")){

                        val=-1;
                    }

                }

                if(val==0){
                    medicationlist();
                    medi_adapter.notifyDataSetChanged();
                }else{
                    StoredObjects.ToastMethod("Please save Medication Details",getActivity());
                }
            }
        });


        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int val=0;
                for(int k=0;k<medications_list.size();k++){
                    if(medications_list.get(k).get("brand_id").equalsIgnoreCase("")||
                            medications_list.get(k).get("brand_name").equalsIgnoreCase("")
                            ||medications_list.get(k).get("molecule_id").equalsIgnoreCase("")||
                            medications_list.get(k).get("molecule").equalsIgnoreCase("")||
                            medications_list.get(k).get("no_of_times").equalsIgnoreCase("")||
                            medications_list.get(k).get("intake").equalsIgnoreCase("")||
                            medications_list.get(k).get("no_of_days").equalsIgnoreCase("")||
                            medications_list.get(k).get("dose").equalsIgnoreCase("")){
                        val=-1;
                    }

                }
                if(val==0){

                    String physuggestionsval="";
                    String brandIds="";
                    String moleculeids="";
                    JSONArray PhysicalsugArray = new JSONArray();
                    JSONObject jsonObject1 = null;
                    for (int i= 0;i< medications_list.size();i++) {
                        try {
                            jsonObject1 = new JSONObject();
                            jsonObject1.put("brand_id",  medications_list.get(i).get("brand_id"));
                            jsonObject1.put("brand_name",  medications_list.get(i).get("brand_name"));
                            jsonObject1.put("molecule_id",  medications_list.get(i).get("molecule_id"));
                            jsonObject1.put("molecule",  medications_list.get(i).get("molecule"));
                            jsonObject1.put("no_of_times",  medications_list.get(i).get("no_of_times"));
                            jsonObject1.put("intake",  medications_list.get(i).get("intake"));
                            jsonObject1.put("no_of_days",  medications_list.get(i).get("no_of_days"));
                            jsonObject1.put("dose",  medications_list.get(i).get("dose"));
                            jsonObject1.put("remarks",  medications_list.get(i).get("remarks"));

                            if(i== medications_list.size()-1){
                                brandIds=brandIds+ medications_list.get(i).get("brand_id");
                            }else{
                                brandIds=brandIds+ medications_list.get(i).get("brand_id")+",";
                            }

                            if(i== medications_list.size()-1){
                                moleculeids=moleculeids+ medications_list.get(i).get("molecule_id")+":"+ medications_list.get(i).get("molecule");
                            }else{
                                moleculeids=moleculeids+ medications_list.get(i).get("molecule_id")+":"+ medications_list.get(i).get("molecule")+",";
                            }

                            PhysicalsugArray.put(jsonObject1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    physuggestionsval=PhysicalsugArray.toString();





                    P_Add_Prescription.setUpdate_data("medication",physuggestionsval,brandIds,moleculeids);

                    StoredObjects.LogMethod("values::","values::"+physuggestionsval+brandIds+moleculeids);

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    if (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack();
                    }

                }else{
                    StoredObjects.ToastMethod("Please fill Medication Details",getActivity());
                }


            }
        });



    }




}



