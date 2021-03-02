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

public class P_AddTestSuggestions extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    CustomButton search_button;
    RecyclerView testsug_rv;
    LinearLayout addtest_lay;
    public static HashMapRecycleviewadapter test_adapter;
    public  static ArrayList<HashMap<String, String>> testsuggestionlist = new ArrayList<>();

    public  static int testpos =0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.p_addtestsuggestions,null,false );
        StoredObjects.page_type="add_testsuggestion";

        SideMenu.updatemenu(StoredObjects.page_type);

        initilization(v);

        return v;
    }

    public static void medicationlist(){
        HashMap<String,String> hashMap1=new HashMap<>();
        hashMap1.put("report_date","");
        hashMap1.put("report_image","");
        hashMap1.put("diagnostic_name","");
        hashMap1.put("test_name","");
        hashMap1.put("test_remarks","");
        testsuggestionlist.add(hashMap1);
    }
    private void initilization(View dialog) {

        backbtn_img = dialog.findViewById( R.id. backbtn_img);
        title_txt= dialog.findViewById( R.id. title_txt);

        title_txt.setText( "Diagnosis Suggestion" );

        search_button = (CustomButton) dialog.findViewById(R.id.search_button);
        testsug_rv = (RecyclerView) dialog.findViewById(R.id.testsug_rv);
        addtest_lay = (LinearLayout) dialog.findViewById(R.id.addtest_lay);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        testsug_rv.setLayoutManager(linearLayoutManager);
        test_adapter=new HashMapRecycleviewadapter(getActivity(),testsuggestionlist, "diagnose_listitem", testsug_rv, R.layout.diagnose_listitem);
        testsug_rv.setAdapter( test_adapter);

        if(testsuggestionlist.size()==0){
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
        addtest_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int val=0;
                for(int k=0;k<testsuggestionlist.size();k++){
                    if(testsuggestionlist.get(k).get("report_date").equalsIgnoreCase("")||
                            testsuggestionlist.get(k).get("report_image").equalsIgnoreCase("")
                            ||testsuggestionlist.get(k).get("diagnostic_name").equalsIgnoreCase("")||
                            testsuggestionlist.get(k).get("test_name").equalsIgnoreCase("")||
                            testsuggestionlist.get(k).get("test_remarks").equalsIgnoreCase("")){
                        val=-1;
                    }

                }
                if(val==0){
                    medicationlist();
                    test_adapter.notifyDataSetChanged();
                }else{
                    StoredObjects.ToastMethod("Please save Test Details",getActivity());
                }


            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int val=0;
                for(int k=0;k<testsuggestionlist.size();k++){
                    if(testsuggestionlist.get(k).get("report_date").equalsIgnoreCase("")||
                            testsuggestionlist.get(k).get("report_image").equalsIgnoreCase("")
                            ||testsuggestionlist.get(k).get("diagnostic_name").equalsIgnoreCase("")||
                            testsuggestionlist.get(k).get("test_name").equalsIgnoreCase("")||
                            testsuggestionlist.get(k).get("test_remarks").equalsIgnoreCase("")){
                        val=-1;
                    }

                }
                if(val== 0){

                    String physuggestionsval="";
                    JSONArray PhysicalsugArray = new JSONArray();
                    JSONObject jsonObject1 = null;
                    for (int i= 0;i< testsuggestionlist.size();i++) {
                        try {
                            jsonObject1 = new JSONObject();
                            jsonObject1.put("report_date",  testsuggestionlist.get(i).get("report_date"));
                            jsonObject1.put("report_image",  testsuggestionlist.get(i).get("report_image"));
                            jsonObject1.put("diagnostic_name",  testsuggestionlist.get(i).get("diagnostic_name"));
                            jsonObject1.put("test_name",  testsuggestionlist.get(i).get("test_name"));
                            jsonObject1.put("test_remarks",  testsuggestionlist.get(i).get("test_remarks"));

                            PhysicalsugArray.put(jsonObject1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    physuggestionsval=PhysicalsugArray.toString();

                    P_Add_Prescription.setUpdate_data("ref_test",physuggestionsval,"","");

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    if (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack();
                    }

                }else{
                    StoredObjects.ToastMethod("Please fill Test Details",getActivity());
                }


            }
        });



    }




}




