package com.rxmediaapp.fragments.teamleader;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

public class FranchiseeList_Main extends Fragment {

    ImageView backbtn_img;
    TextView title_txt;
    EditText search_franchase_edtxt;

    public static RecyclerView marketing_exicutiveone_recyler;
    public static TextView nodatavailable_txt;

    public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
    int pagecount=1,totalpages=0;
    String recordsperpage="10";
    public static HashMapRecycleviewadapter adapter;
    ArrayList<HashMap<String, String>> filter_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.marketing_exicutive_one,null,false );
        StoredObjects.page_type="marketing_exicutive_one";
        pagecount=1;
        data_list.clear();
        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        serviceCalling();
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        marketing_exicutiveone_recyler = v.findViewById( R.id.marketing_exicutiveone_recyler);
        search_franchase_edtxt=v.findViewById(R.id.search_franchase_edtxt);

        nodatavailable_txt=v.findViewById(R.id.nodatavailable_txt);
        title_txt.setText( "Franchises List" );


        search_franchase_edtxt.setVisibility(View.GONE);

        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        } );




        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        marketing_exicutiveone_recyler.setLayoutManager(linearLayoutManager);

        marketing_exicutiveone_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                    pagecount=pagecount+1;
                    if(pagecount<=totalpages){
                        serviceCalling();
                    }

                }
            }
        });


        search_franchase_edtxt.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = search_franchase_edtxt.getText().length();

                filter_list.clear();

                for (int i = 0; i < data_list.size(); i++) {
                    if (textlength <= data_list.get(i).get("name").length()) {

                        if ((data_list.get(i).get("name").toLowerCase().trim().contains(  search_franchase_edtxt.getText().toString().toLowerCase().trim()))) {

                            filter_list.add(data_list.get(i));
                        }
                    }
                }


                adapter = new HashMapRecycleviewadapter(getActivity(),filter_list,"marketing_exicutiveone",marketing_exicutiveone_recyler,R.layout.marketing_exicutiveone_listitem);
                marketing_exicutiveone_recyler.setAdapter(adapter);
                if(filter_list.size()==0){
                    nodatavailable_txt.setVisibility(View.VISIBLE);
                    marketing_exicutiveone_recyler.setVisibility(View.GONE);

                }else{
                    nodatavailable_txt.setVisibility(View.GONE);
                    marketing_exicutiveone_recyler.setVisibility(View.VISIBLE);

                }



            }
        });




    }

    private void serviceCalling() {

        if (InterNetChecker.isNetworkAvailable(getContext())) {
            getFranchiseeListService(getActivity(),RetrofitInstance.franchisee_list);
        } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
        }

    }

    private void getFranchiseeListService(final FragmentActivity activity, String method) {
        if(pagecount==1){
            CustomProgressbar.Progressbarshow(activity);
        }
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.getFranchiseeList(method, StoredObjects.M_UserId, StoredObjects.M_RoleUserId,pagecount+"",recordsperpage).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseReceived = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseReceived);
                        StoredObjects.LogMethod("response", "response::" + responseReceived);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("200")) {
                            String results = jsonObject.getString("results");
                            String total_pages = jsonObject.getString("total_pages");
                            totalpages = Integer.parseInt(total_pages);

                            if (pagecount == 1) {
                                data_list.clear();
                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                updatelay(data_list);
                                adapter = new HashMapRecycleviewadapter(getActivity(), data_list, "marketing_exicutiveone", marketing_exicutiveone_recyler, R.layout.marketing_exicutiveone_listitem);
                                marketing_exicutiveone_recyler.setAdapter(adapter);
                            } else {

                                dummy_list = JsonParsing.GetJsonData(results);
                                data_list.addAll(dummy_list);
                                adapter.notifyDataSetChanged();
                                marketing_exicutiveone_recyler.invalidate();
                            }

                        } else {

                            if (pagecount == 1) {
                                data_list.clear();
                                dummy_list.clear();
                                updatelay(data_list);
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
                if(pagecount==1){

                    CustomProgressbar.Progressbarcancel(activity);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(pagecount==1){
                    CustomProgressbar.Progressbarcancel(activity);
                }

            }
        });


    }

    private void updatelay(ArrayList<HashMap<String, String>> data_list) {

        if(data_list.size()==0){
            nodatavailable_txt.setVisibility(View.VISIBLE);

            marketing_exicutiveone_recyler.setVisibility(View.GONE);
        }else{
            nodatavailable_txt.setVisibility(View.GONE);
            marketing_exicutiveone_recyler.setVisibility(View.VISIBLE);

        }
    }
    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

    }




}

