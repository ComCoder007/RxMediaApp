package com.rxmediaapp.fragments.assistant;

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
import com.rxmediaapp.customfonts.CustomEditText;
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
import retrofit2.http.Query;


public class Add_Appointment_One extends Fragment {

    ImageView backbtn_img;
    TextView title_txt,appointment_name_txt,appointment_gender_txt,appointment_bloodgroup_txt,appointment_age_txt,appointment_consultdate_txt;
    LinearLayout addphy_ex_lay;
    EditText appointment_dueto_edtxt;
    Button addapp_btn;

    EditText select_patient_edtx,pat_mbile_edtx;

    public  ArrayList<String> patlist = new ArrayList<>();
    public  static ArrayList<HashMap<String, String>> physuggestionslist = new ArrayList<>();
    String patient_id="";


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

        if(HashmapViewHolder.p_data_list.size()==1){
            select_patient_edtx.setVisibility(View.GONE);
            appointment_name_txt.setText(HashmapViewHolder.p_data_list.get(0).get("name"));
            appointment_gender_txt.setText(HashmapViewHolder.p_data_list.get(0).get("gender"));
            appointment_bloodgroup_txt.setText(HashmapViewHolder.p_data_list.get(0).get("blood_group"));
            appointment_age_txt.setText(HashmapViewHolder.p_data_list.get(0).get("age"));
            appointment_consultdate_txt.setText(HashmapViewHolder.p_data_list.get(0).get("last_consulted_date"));
            pat_mbile_edtx.setText(HashmapViewHolder.p_data_list.get(0).get("phone"));
            patient_id=HashmapViewHolder.p_data_list.get(0).get("user_id");

        }else{

            select_patient_edtx.setVisibility(View.VISIBLE);
            select_patient_edtx.setText(HashmapViewHolder.p_data_list.get(0).get("name"));
            appointment_name_txt.setText(HashmapViewHolder.p_data_list.get(0).get("name"));
            appointment_gender_txt.setText(HashmapViewHolder.p_data_list.get(0).get("gender"));
            appointment_bloodgroup_txt.setText(HashmapViewHolder.p_data_list.get(0).get("blood_group"));
            appointment_age_txt.setText(HashmapViewHolder.p_data_list.get(0).get("age"));
            appointment_consultdate_txt.setText(HashmapViewHolder.p_data_list.get(0).get("last_consulted_date"));

            patient_id=HashmapViewHolder.p_data_list.get(0).get("user_id");
            pat_mbile_edtx.setText(HashmapViewHolder.p_data_list.get(0).get("phone"));
            patlist.clear();
            for(int k=0;k<HashmapViewHolder.p_data_list.size();k++){
               // patlist.add(HashmapViewHolder.p_data_list.get(k).get("name"));

                if(HashmapViewHolder.p_data_list.get(k).get("relation").equalsIgnoreCase("")){
                    patlist.add(HashmapViewHolder.p_data_list.get(k).get("name"));
                }else{
                    patlist.add(HashmapViewHolder.p_data_list.get(k).get("name")+" ("+HashmapViewHolder.p_data_list.get(k).get("relation")+")");
                }
            }
        }

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


                appointment_name_txt.setText(HashmapViewHolder.p_data_list.get(position).get("name"));
                appointment_gender_txt.setText(HashmapViewHolder.p_data_list.get(position).get("gender"));
                appointment_bloodgroup_txt.setText(HashmapViewHolder.p_data_list.get(position).get("blood_group"));
                appointment_age_txt.setText(HashmapViewHolder.p_data_list.get(position).get("age"));
                appointment_consultdate_txt.setText(HashmapViewHolder.p_data_list.get(position).get("last_consulted_date"));

                patient_id=HashmapViewHolder.p_data_list.get(position).get("user_id");
                pat_mbile_edtx.setText(HashmapViewHolder.p_data_list.get(position).get("phone"));

                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
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

        title_txt.setText( "Add Appointment" );

        appointment_dueto_edtxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        appointment_dueto_edtxt.setRawInputType(InputType.TYPE_CLASS_TEXT);

        select_patient_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(patlist.size()>0){
                    PatListPopup((CustomEditText) select_patient_edtx,getActivity());
                }else{
                    StoredObjects.ToastMethod("No Patient found",getActivity());
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
        } );

        addphy_ex_lay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                physicalexampopup (getActivity());
            }
        } );

        addapp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String physuggestionsval="";
                JSONArray PhysicalsugArray = new JSONArray();
                JSONObject jsonObject1 = null;
                for (int i= 0;i<physuggestionslist.size();i++) {
                    if(physuggestionslist.get(i).get("suggestion_id").equalsIgnoreCase("")&&
                            physuggestionslist.get(i).get("suggestion_value").equalsIgnoreCase("")){

                    }else{
                        try {
                            jsonObject1 = new JSONObject();
                            jsonObject1.put("suggestion_id", physuggestionslist.get(i).get("suggestion_id"));
                            jsonObject1.put("suggestion_value",  physuggestionslist.get(i).get("suggestion_value"));

                            PhysicalsugArray.put(jsonObject1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

                physuggestionsval=PhysicalsugArray.toString();

                String phone=pat_mbile_edtx.getText().toString().trim();
                if(StoredObjects.inputValidation(pat_mbile_edtx,"Please enter Mobile Number",getActivity())){
                    if(physuggestionsval.equalsIgnoreCase("[]")){

                        StoredObjects.ToastMethod("Please select Physical Suggestions",getActivity());
                    }else{
                        String consult_str = appointment_dueto_edtxt.getText().toString().trim();

                        if (StoredObjects.inputValidation(appointment_dueto_edtxt, getActivity().getResources().getString(R.string.enter_dueto), getActivity())) {

                            if (InterNetChecker.isNetworkAvailable(getActivity())) {
                                AddAppointmentService(getActivity(),consult_str,physuggestionsval,phone);
                            } else {
                                StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
                            }

                        }
                    }
                }



            }
        });

        physuggestionslist.clear();
        addphysuggestions();




    }



    private void AddAppointmentService(final FragmentActivity activity, String consult_str,String physuggestions,String phone) {
        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.AddAppointmentone(RetrofitInstance.add_appointment_two,patient_id,consult_str, StoredObjects.Pat_DocID,physuggestions,StoredObjects.UserId,StoredObjects.UserRoleId,phone).enqueue(new Callback<ResponseBody>() {
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
                            fragmentcallinglay(new Add_Appointment());
                            physuggestionslist.clear();
                            addphysuggestions();
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
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

    }

    public static void addphysuggestions() {
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("suggestion_id","");
        hashMap.put("suggestion_value","");
        hashMap.put("suggestion_name","");
        hashMap.put("remove","0");
        physuggestionslist.add(hashMap);
    }
    public static HashMapRecycleviewadapter adapter;
    private void physicalexampopup(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_physical_examination );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.getWindow().setElevation(20);

        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
        CustomButton search_button =(CustomButton) dialog.findViewById(R.id.search_button);
        ImageView canclimg = (ImageView)dialog.findViewById(R.id.canclimg);
        LinearLayout addphy_lay = (LinearLayout)dialog.findViewById(R.id.addphy_lay);

        RecyclerView add_physical_recycle = (RecyclerView)dialog.findViewById(R.id.add_physical_recycle);

        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(activity);
        add_physical_recycle.setLayoutManager(linearLayoutManager);

        adapter=new HashMapRecycleviewadapter(activity, physuggestionslist,"add_physical",add_physical_recycle,R.layout.add_physical_examination_listitems);
        add_physical_recycle.setAdapter(adapter);


        addphy_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int val=0;
                for(int k=0;k<physuggestionslist.size();k++){
                    if(physuggestionslist.get(k).get("suggestion_id").equalsIgnoreCase("")||
                            physuggestionslist.get(k).get("suggestion_value").equalsIgnoreCase("")){

                        val=-1;
                    }

                }
                if(val==0){
                    addphysuggestions();
                    adapter.notifyDataSetChanged();
                }else{
                    StoredObjects.ToastMethod("Please save Physical Examinations Details",getActivity());
                }


            }
        });
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

                int val=0;
                for(int k=0;k<physuggestionslist.size();k++){
                    if(physuggestionslist.get(k).get("suggestion_id").equalsIgnoreCase("")||
                            physuggestionslist.get(k).get("suggestion_value").equalsIgnoreCase("")){

                        val=-1;
                    }

                }
                if(val==0){
                    dialog.dismiss();
                }else{
                   StoredObjects.ToastMethod("Please fill Physical Examinations Details",getActivity());
                }

            }
        });

        dialog.show();
    }



}


