package com.rxmediaapp.customadapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customfonts.CustomBoldTextView;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.customfonts.CustomMediumTextView;
import com.rxmediaapp.customfonts.CustomRegularTextView;

import com.rxmediaapp.fragments.assistant.Add_Appointment;
import com.rxmediaapp.fragments.assistant.Add_Appointment_One;
import com.rxmediaapp.fragments.assistant.Add_Appointment_Two;
import com.rxmediaapp.fragments.assistant.Ass_AppointmentList;
import com.rxmediaapp.fragments.assistant.Ass_PatientList;
import com.rxmediaapp.fragments.assistant.AssistantProfile;
import com.rxmediaapp.fragments.dashboards.Asst_Dashboard;
import com.rxmediaapp.fragments.dashboards.Doc_Dashboard;
import com.rxmediaapp.fragments.dashboards.Franchisee_Dashboard;
import com.rxmediaapp.fragments.dashboards.H_Dashboard;
import com.rxmediaapp.fragments.dashboards.Marketing_Dashboard;
import com.rxmediaapp.fragments.dashboards.P_Dashboard;
import com.rxmediaapp.fragments.dashboards.SubFranchisee_Dashboard;
import com.rxmediaapp.fragments.dashboards.TL_Dashboard;
import com.rxmediaapp.fragments.doctor.DocTotalPrescriptionsList;
import com.rxmediaapp.fragments.doctor.Doc_Appointmentslist;
import com.rxmediaapp.fragments.doctor.Doc_Assistant;
import com.rxmediaapp.fragments.doctor.Doc_EditAssistant;
import com.rxmediaapp.fragments.doctor.Doc_PatPrescriptionslist;
import com.rxmediaapp.fragments.doctor.Doc_PatientPrescrDetails;
import com.rxmediaapp.fragments.doctor.Doc_Patient_Details;
import com.rxmediaapp.fragments.doctor.Doc_Patient_List;
import com.rxmediaapp.fragments.doctor.Doc_Patientlistmain;
import com.rxmediaapp.fragments.doctor.Doc_PhysicalSuggestions;
import com.rxmediaapp.fragments.doctor.Doc_TestSuggested;
import com.rxmediaapp.fragments.doctor.Doctor_Details;
import com.rxmediaapp.fragments.doctor.Doc_AddMedication;
import com.rxmediaapp.fragments.hospital.H_AddAssistant;
import com.rxmediaapp.fragments.hospital.H_AddDoctor;
import com.rxmediaapp.fragments.hospital.H_Assistant;
import com.rxmediaapp.fragments.hospital.H_DoctorsList;
import com.rxmediaapp.fragments.hospital.H_EditAssistant;
import com.rxmediaapp.fragments.hospital.H_EditDoctor;
import com.rxmediaapp.fragments.hospital.H_PatientPrescrDetails;
import com.rxmediaapp.fragments.hospital.H_Test_Suggested;
import com.rxmediaapp.fragments.hospital.H_TotalPatientList;
import com.rxmediaapp.fragments.patient.Block_Doctor;
import com.rxmediaapp.fragments.patient.Edit_Diagnosis;
import com.rxmediaapp.fragments.patient.Edit_TestSuggestions;
import com.rxmediaapp.fragments.patient.P_AddMedication;
import com.rxmediaapp.fragments.patient.P_AddTestSuggestions;
import com.rxmediaapp.fragments.patient.P_Add_Prescription;
import com.rxmediaapp.fragments.patient.P_Edit_Member;
import com.rxmediaapp.fragments.patient.P_Prescriptionslist;
import com.rxmediaapp.fragments.patient.P_Sub_Member;
import com.rxmediaapp.fragments.patient.P_Test_Sugestions;
import com.rxmediaapp.fragments.patient.TestImageUpload;
import com.rxmediaapp.fragments.teamleader.Edit_FranchiseeList;
import com.rxmediaapp.fragments.teamleader.Edit_Marketing_Executive;
import com.rxmediaapp.fragments.teamleader.F_Doctor_Lead;
import com.rxmediaapp.fragments.teamleader.F_Edit_Lead;
import com.rxmediaapp.fragments.teamleader.F_Hospital_Lead;
import com.rxmediaapp.fragments.teamleader.F_Patient_Lead;
import com.rxmediaapp.fragments.teamleader.Franchisee_Details;
import com.rxmediaapp.fragments.teamleader.Hospital_DoctorLeads;
import com.rxmediaapp.fragments.teamleader.Franchisee_SubFranchises;
import com.rxmediaapp.fragments.teamleader.Franchisee_Patients;
import com.rxmediaapp.fragments.teamleader.FranchiseeList_Main;
import com.rxmediaapp.fragments.teamleader.Marketing_Exicutive;
import com.rxmediaapp.fragments.teamleader.TL_AddDoctor;
import com.rxmediaapp.fragments.teamleader.TL_Doctors;
import com.rxmediaapp.fragments.teamleader.TL_Edit_Doctor;
import com.rxmediaapp.fragments.teamleader.TL_Edit_Hospital;
import com.rxmediaapp.fragments.teamleader.TL_Edit_Patient;
import com.rxmediaapp.fragments.teamleader.TL_Franchisee_List;
import com.rxmediaapp.fragments.teamleader.TL_Hospitals;
import com.rxmediaapp.fragments.teamleader.TL_Patients;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.CameraUtils;
import com.rxmediaapp.storedobjects.StoredObjects;
import com.rxmediaapp.fragments.patient.Diagnosis_Reports_Details;
import com.rxmediaapp.fragments.doctor.Doc_Profile;
import com.rxmediaapp.fragments.hospital.H_OtherDoc_Prescriptions;
import com.rxmediaapp.fragments.hospital.H_PatentList;
import com.rxmediaapp.fragments.hospital.H_PatientList_Main;

import com.rxmediaapp.fragments.hospital.H_Profile;
import com.rxmediaapp.fragments.hospital.H_TotalPrescriptions;
import com.rxmediaapp.fragments.patient.Members_Details;
import com.rxmediaapp.fragments.patient.Patient_Added_Prescriptions;
import com.rxmediaapp.fragments.teamleader.Franchisee_Doctors;
import com.rxmediaapp.fragments.teamleader.TL_Franchises_Details;
import com.rxmediaapp.fragments.teamleader.Franchisee_Hospitals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class HashmapViewHolder extends RecyclerView.ViewHolder {

  private Activity activity;
  HashMapRecycleviewadapter adapter;

  //p_tst_sugstn
  ImageView p_tst_delete_img;

  //doc_prfle
  LinearLayout d_prf_dctrnme_lay, d_prf_apntmnt_lay;

  ImageView d_prf_up_img, d_prf_down_img;
  TextView d_prf_dctrnme_txt,phonenum_txt;
  EditText d_sgnup_frmday_edtx,d_sgnup_today_edtx,d_sgnup_custm_edtx,d_sgnup_avabletme_edtx,d_sgnup_tmetwo_edtx;

  CustomEditText h_avabletme_edtx2,h_avabletme_edtx3,h_avabletme_edtx4,h_tmetwo_edtx2,h_tmetwo_edtx3,h_tmetwo_edtx4
          ,h_frmday_edtx,h_today_edtx,h_custm_edtx;
  CustomEditText d_avabletme_edtx2,d_avabletme_edtx3,d_avabletme_edtx4,d_tmetwo_edtx2,d_tmetwo_edtx3,d_tmetwo_edtx4
          ,d_frmday_edtx,d_today_edtx,d_tmetwo_edtx,d_avabletme_edtx;



  RecyclerView appointment_slot_rv;
  Button h_addoc_add_day_btn;
  //h_patentlst
  LinearLayout h_patnttmngs_lay;

  int pos=0;


  CustomBoldTextView patientlist_name_txt,patientlist_dr_name_txt;;
  CustomRegularTextView patientlist_apptime_txt;
  ImageView patientlist_arrow_img;
  LinearLayout h_patientlist_listlay;

  //ad_apntmnt
  LinearLayout ad_patntlst_lay, ad_patntdtls_lay, apnt_addphy_ex_lay;
  ImageView ad_patntlst_up_img, ad_patntlst_down_img;
  CustomButton adap_submit_btn;

  //add_appointment
  LinearLayout add_appointment_lay, appointment_innerlay;
  ImageView ad_doc_up_img, ad_doc_down_img;
  CustomButton pat_search_btn;

  EditText search_edtxt;
  CustomRegularTextView docdetails_txt;

  ImageView yellow_circle, red_circle;

  RecyclerView main_innerlist;
  ImageView expand_img, collapse_img;
  TextView h_maintitle_txt;
  LinearLayout mainlist_lay, docdetails_lay, h_docmain_lay, h_ttlpres_lay, p_hstlpresr_lay, h_docpres_lay, h_patmain_lay, numofass_lay,
      p_mainlist_lay, dpre_ttlpres_lay, patownpre_lay, patotherdoc_lay, p_diareports_lay, p_submember_lay;

  View lead_view;

  TextView doctime_txt,docattender_txt,enrollid_txt,doc_problem_txt,p_enroll_txt;
  LinearLayout enrolledlay;

  //f_hospital_four
  LinearLayout add_hospital_lead, f_mainlist_lay;
  TextView f_maintitle_txt, patlisttitle_txt;
  ImageView f_expand_img, f_collapse_img;
  LinearLayout addlistitem_lay;

  //ass_patientlist
  RecyclerView ass_main_innerlist;
  ImageView ass_expand_img, ass_collapse_img;
  TextView ass_maintitle_txt;
  LinearLayout ass_mainlist_lay, as_patdetails_lay, mar_franchise_lay, franshicelist_lay, tl_franshice_lay, tl_hsptl_lay;

  TextView fradetls_name_txt, fr_title_txt,tabcount_txt;

  LinearLayout frdetails_list_lay;
  TextView as_pat_aptid,as_pat_name,as_pat_mobile,as_pat_apttime;

  Button ref_save_btn;


  //h_assistant
  TextView h_assistant_name_txt,ad_patntnme_txt, h_assistant_timings_txt, h_assistant_docassign_txt;
  ImageView h_assistant_image, h_assistant_edit_img, h_assistant_del_img;
  LinearLayout h_assistant_list_lay;

  //h_otherdoc_prescrdetails
  TextView prescrip_count_txt, prescrip_name_txt, prescrip_days_txt, prescrip_quantity_txt, prescrip_times_txt,
      prescrip_intake_txt, diagnose_sug_count_txt, diagnose_sug_test_txt,pp_note_txt;
  LinearLayout prescrip_details_listlay, prescrip_note_lay, diagnose_sug_listlay;
  LinearLayout testsuggested_lay,helplist_lay;
  //h_patents_lst

  //h_patient_details
  TextView doc_enroleid_txt, doc_appointment_txt,status_txt, h_prescr_docname_txt, h_prescr_problem_txt, h_prescr_appdatetime_txt;
  ImageView doc_arrow_img, h_prescr_arrow_img;
  LinearLayout doc_hstlpresr_lay;

  //new

  //Franchisee_Details
  TextView f_hospital_txt, number_txt, f_address_txt, f_app_date_txt;
  ImageView f_arrow_img, f_edit_img, f_del_img,h_docimg;

  //Franchisee_Doctors
  TextView docname_txt, doc_number_txt, doc_address_txt;
  ImageView doctor_img;
  LinearLayout doctor_two_listlay;

  //Franchisee_Hospitals
  TextView hospitalname_txt, hospital_number_txt, hospital_address_txt;
  ImageView hospitals_image;
  LinearLayout hospital_one_listlay;

  //Franchisee_Patients
  TextView f_patient_name, f_patient_number, f_patient_address;
  LinearLayout f_patientlist_listlay;
  ImageView pat_img,subfra_img;

  //Franchisee_SubFranchises
  TextView f_patientlist_name, f_patientlist_number, f_patientlist_address;
  LinearLayout f_patient_listlay;

  //FranchiseeList_Main
  TextView franshicelist_name_txt, franshicelist_number_txt, franshicelist_address_txt;
  ImageView franshicelist_image, franshicelist_edit_img, franshicelist_del_img;

  //marketing_exicutive
  TextView mar_franchisename_txt, mar_franchisenumber_txt, mar_franchise_address_txt;
  ImageView mar_franchise_img, mar_franchise_edit_img, mar_franchise_del_img;

  //TL_Doctors
  TextView hospital_txt, doc_contact_txt, tl_doc_address_txt;
  ImageView hospital_image, tl_doc_del_img, tl_doc_edit_img;
  LinearLayout doctor_one_listlay;

  //TL_Franchisee_List
  TextView franchasename_txt, franchasenumber_txt, franchaseaddress_txt;
  ImageView franchase_image, franchasedel_img, franchaseedit_img;

  //TL_Hospitals
  TextView tl_hospital_txt, tl_contact_txt, tl_address_txt;
  ImageView tl_hospital_image, tl_del_img, tl_edit_img;

  //TL_Patients
  TextView patient_name, patient_number, patient_address;
  ImageView patient_edit_img, patient_del_img,tl_pat_image;

  TextView ad_patgender_txt,ad_patname_txt,ad_patblood_txt,ad_patage_txt,ad_patdate_txt;
//Hashmap

  EditText consultdoc_edt;

  ImageView p_img;

  EditText search_brand_edt,molecule_name_edt,no_days_edt,dose_edt,medication_remarks;
  Button suggestedbrands_btn;
  TextView sel_brands_txt;
  CheckBox morning_cb,afternoon_cb,evening_cb;
  Button p_save_btn;
  RadioButton beforefood_rb,afterfood_rb;
  ImageView med_del_btn;
  CustomMediumTextView descr_txt;
  CustomRegularTextView test_reff_dr,test_sug_date;
  //Doc_PhysicalSuggestions
  TextView pat_weight_txt;

  LinearLayout b_lay;
  TextView b_brand_txt;
  CheckBox b_checkbox ;

  //Doc_TestSuggested
  TextView doc_enrole_txt, doc_testname_txt, doc_reffer_txt, doc_date_txt;
  LinearLayout doc_testsuggested_listlay;

  //DocTotalPrescriptionsList
  TextView prescrip_enrole_txt, prescrip_datetime_txt,prescrip_pname_txt;

  //Block_Doctor
  TextView blockeddoc_name_txt, blockeddoc_desig_txt, blockeddoc_address_txt, blocked_txt;
  ImageView blocked_doc_image;
  LinearLayout blockdoc_list_lay;

  //Members_Details
  TextView patownpre_docname_txt, patownpre_date_txt, patownpre_problem_txt, patotherdoc_name_txt, patotherdoc_problem_txt, patotherdoc_date_txt;

  //P_Diagnosis_Reports
  TextView p_diareports_date_txt, p_diareports_dname_txt, p_diareports_refdoc_txt;
  ImageView p_diareports_edit_img, p_diareports_del_img;

  //P_Sub_Member
  TextView submem_relation_txt, submem_name_txt, submem_date_txt;
  ImageView submember_image, submem_edit_img, submem_del_img;

  //P_Test_Sugestions
  TextView p_tst_date_txt, p_tst_diastc_txt, p_tst_rfdctr_txt, p_tst_ultrasund_txt, p_tst_tft_txt, p_tst_cbp_txt;

  //Patient_Added_Prescriptions
  TextView p_prescrip_count_txt, p_prescrip_name_txt, p_prescrip_days_txt, p_prescrip_quantity_txt, p_prescrip_times_txt,
      p_prescrip_intake_txt, p_diagnose_sug_count_txt, p_diagnose_sug_test_txt;
  LinearLayout p_prescrip_details_listlay, p_prescrip_note_lay, p_diagnose_sug_listlay;

  //assign_docpopup
  CheckBox assign_doc_checkbox;
  LinearLayout assigndoc_listlay;
  TextView assigtndoc_txt;

  CustomRegularTextView doc_patientList_name;

  TextView h_prescSub_enrollID,h_prescSub_consulted_dr,h_pres_apt_date_time;


  EditText physug_droplay,physug_droplay_;
  EditText phy_value_edt;
  ImageView p_del_img;
  Button unblock_doc_btn,t_save_btn;
  ImageView attach_img;

  //diagnose_listitem
  CustomEditText diagnose_listitem_report_date_edtx,diagnose_list_item_name_edtxt,diagnose_listitem_test_nme_edtxt,diagnose_listitem_remark_edtx;
  CustomButton browse_btn;

  EditText reflabname_edtx,reftestname_edtx;
  ImageView ref_del_img;

  DatePickerDialog datePickerDialog;
  int year;
  int month;
  int dayOfMonth;
  Calendar calendar;

  Button  physug_save_btn;

  //Dashboard
  LinearLayout dashboard_lay,dashboard_bg_lay;
  ImageView dashboard_img;
  CustomBoldTextView dashboard_title_txt,count_txt;
  //doc_timeslots
  CustomEditText h_avabletme_edtx,h_tmetwo_edtx;
  ImageView del_icon;

  CustomRegularTextView patient_name_txt,doc_name_txt,clinic_name_txt;

  HashmapViewHolder(View convertView, String type, final Activity activity) {

    super(convertView);
    this.activity = activity;

    if(type.equalsIgnoreCase("ass_dashboard")) {

      dashboard_lay = convertView.findViewById(R.id.dashboard_lay);
      dashboard_bg_lay = convertView.findViewById(R.id.dashboard_bg_lay);
      dashboard_img = convertView.findViewById(R.id.dashboard_img);
      dashboard_title_txt = convertView.findViewById(R.id.dashboard_title_txt);
      count_txt = convertView.findViewById(R.id.count_txt);

    } else if(type.equalsIgnoreCase("prescriptionslist")) {

      doc_enroleid_txt = convertView.findViewById(R.id.doc_enroleid_txt);
      patient_name_txt = convertView.findViewById(R.id.patient_name_txt);
      doc_problem_txt = convertView.findViewById(R.id.doc_problem_txt);
      clinic_name_txt = convertView.findViewById(R.id.clinic_name_txt);
      doc_name_txt = convertView.findViewById(R.id.doc_name_txt);
      doc_appointment_txt = convertView.findViewById(R.id.doc_appointment_txt);
      doc_hstlpresr_lay = convertView.findViewById(R.id.doc_hstlpresr_lay);
    }else if(type.equalsIgnoreCase("doc_dashboard")) {

      dashboard_lay = convertView.findViewById(R.id.dashboard_lay);
      dashboard_bg_lay = convertView.findViewById(R.id.dashboard_bg_lay);
      dashboard_img = convertView.findViewById(R.id.dashboard_img);
      dashboard_title_txt = convertView.findViewById(R.id.dashboard_title_txt);
      count_txt = convertView.findViewById(R.id.count_txt);

    } else if(type.equalsIgnoreCase("h_dashboard")) {

      dashboard_lay = convertView.findViewById(R.id.dashboard_lay);
      dashboard_bg_lay = convertView.findViewById(R.id.dashboard_bg_lay);
      dashboard_img = convertView.findViewById(R.id.dashboard_img);
      dashboard_title_txt = convertView.findViewById(R.id.dashboard_title_txt);
      count_txt = convertView.findViewById(R.id.count_txt);

    } else if(type.equalsIgnoreCase("p_dashboard")) {

      dashboard_lay = convertView.findViewById(R.id.dashboard_lay);
      dashboard_bg_lay = convertView.findViewById(R.id.dashboard_bg_lay);
      dashboard_img = convertView.findViewById(R.id.dashboard_img);
      dashboard_title_txt = convertView.findViewById(R.id.dashboard_title_txt);
      count_txt = convertView.findViewById(R.id.count_txt);

    } else if(type.equalsIgnoreCase("tl_dashboard")) {

      dashboard_lay = convertView.findViewById(R.id.dashboard_lay);
      dashboard_bg_lay = convertView.findViewById(R.id.dashboard_bg_lay);
      dashboard_img = convertView.findViewById(R.id.dashboard_img);
      dashboard_title_txt = convertView.findViewById(R.id.dashboard_title_txt);
      count_txt = convertView.findViewById(R.id.count_txt);

    } else if(type.equalsIgnoreCase("franchisee_dashboard")) {

      dashboard_lay = convertView.findViewById(R.id.dashboard_lay);
      dashboard_bg_lay = convertView.findViewById(R.id.dashboard_bg_lay);
      dashboard_img = convertView.findViewById(R.id.dashboard_img);
      dashboard_title_txt = convertView.findViewById(R.id.dashboard_title_txt);
      count_txt = convertView.findViewById(R.id.count_txt);

    }else if(type.equalsIgnoreCase("me_dashboard")) {

      dashboard_lay = convertView.findViewById(R.id.dashboard_lay);
      dashboard_bg_lay = convertView.findViewById(R.id.dashboard_bg_lay);
      dashboard_img = convertView.findViewById(R.id.dashboard_img);
      dashboard_title_txt = convertView.findViewById(R.id.dashboard_title_txt);
      count_txt = convertView.findViewById(R.id.count_txt);

    } else if(type.equalsIgnoreCase("subfranchisee_dashboard")) {

      dashboard_lay = convertView.findViewById(R.id.dashboard_lay);
      dashboard_bg_lay = convertView.findViewById(R.id.dashboard_bg_lay);
      dashboard_img = convertView.findViewById(R.id.dashboard_img);
      dashboard_title_txt = convertView.findViewById(R.id.dashboard_title_txt);
      count_txt = convertView.findViewById(R.id.count_txt);

    }
     if (type.equalsIgnoreCase("add_physical")) {
      physug_droplay = convertView.findViewById(R.id.physug_droplay);
      phy_value_edt = convertView.findViewById(R.id.phy_value_edt);
      p_del_img = convertView.findViewById(R.id.p_del_img);
       physug_save_btn=convertView.findViewById(R.id.physug_save_btn);

    }
     else if(type.equalsIgnoreCase("doc_timeslots"))
     {
       h_frmday_edtx = convertView.findViewById(R.id.h_frmday_edtx);
       h_today_edtx = convertView.findViewById(R.id.h_today_edtx);
       h_avabletme_edtx = convertView.findViewById(R.id.h_avabletme_edtx);
       h_tmetwo_edtx = convertView.findViewById(R.id.h_tmetwo_edtx);
       h_avabletme_edtx2 = convertView.findViewById(R.id.h_avabletme_edtx2);
       h_tmetwo_edtx2 = convertView.findViewById(R.id.h_tmetwo_edtx2);
       h_avabletme_edtx3 = convertView.findViewById(R.id.h_avabletme_edtx3);
       h_tmetwo_edtx3 = convertView.findViewById(R.id.h_tmetwo_edtx3);
       h_avabletme_edtx4 = convertView.findViewById(R.id.h_avabletme_edtx4);
       h_tmetwo_edtx4 = convertView.findViewById(R.id.h_tmetwo_edtx4);
       del_icon = convertView.findViewById(R.id.del_icon);
     }
    if (type.equalsIgnoreCase("pat_add_physical")) {
      physug_droplay = convertView.findViewById(R.id.physug_droplay);
      phy_value_edt = convertView.findViewById(R.id.phy_value_edt);
      p_del_img = convertView.findViewById(R.id.p_del_img);
      physug_droplay_=convertView.findViewById(R.id.physug_droplay_);
      physug_save_btn=convertView.findViewById(R.id.physug_save_btn);

    }
     else if(type.equalsIgnoreCase("diagnose_listitem"))
     {
       diagnose_listitem_report_date_edtx = convertView.findViewById(R.id.diagnose_listitem_report_date_edtx);
       diagnose_list_item_name_edtxt = convertView.findViewById(R.id.diagnose_list_item_name_edtxt);
       diagnose_listitem_test_nme_edtxt = convertView.findViewById(R.id.diagnose_listitem_test_nme_edtxt);
       diagnose_listitem_remark_edtx = convertView.findViewById(R.id.diagnose_listitem_remark_edtx);
       browse_btn = convertView.findViewById(R.id.browse_btn);
       med_del_btn=convertView.findViewById(R.id.med_del_btn);
       t_save_btn=convertView.findViewById(R.id.t_save_btn);
       attach_img=convertView.findViewById(R.id.attach_img);
     }
     else if (type.equalsIgnoreCase("ass_patientlist")) {
      ass_main_innerlist = convertView.findViewById(R.id.ass_main_innerlist);
      ass_expand_img = convertView.findViewById(R.id.ass_expand_img);
      ass_collapse_img = convertView.findViewById(R.id.ass_collapse_img);
      ass_maintitle_txt = convertView.findViewById(R.id.ass_maintitle_txt);
      ass_mainlist_lay = convertView.findViewById(R.id.ass_mainlist_lay);

    } else if (type.equalsIgnoreCase("assign_doctor")) {
      assign_doc_checkbox = convertView.findViewById(R.id.assign_doc_checkbox);
      assigndoc_listlay = convertView.findViewById(R.id.assigndoc_listlay);
      assigtndoc_txt = convertView.findViewById(R.id.assigtndoc_txt);

    }else if (type.equalsIgnoreCase("f_hospital_four")) {
      add_hospital_lead = convertView.findViewById(R.id.add_hospital_lead);
      f_expand_img = convertView.findViewById(R.id.f_expand_img);
      f_collapse_img = convertView.findViewById(R.id.f_collapse_img);
      f_maintitle_txt = convertView.findViewById(R.id.f_maintitle_txt);
      f_mainlist_lay = convertView.findViewById(R.id.f_mainlist_lay);

    }else if (type.equalsIgnoreCase("doc_testsuggested")) {
      doc_testsuggested_listlay = convertView.findViewById(R.id.doc_testsuggested_listlay);
      doc_enrole_txt = convertView.findViewById(R.id.doc_enrole_txt);
      doc_testname_txt = convertView.findViewById(R.id.doc_testname_txt);
      doc_reffer_txt = convertView.findViewById(R.id.doc_reffer_txt);
      doc_date_txt = convertView.findViewById(R.id.doc_date_txt);
    } else if (type.equalsIgnoreCase("h_assistant")) {
      h_assistant_name_txt = convertView.findViewById(R.id.h_assistant_name_txt);
      h_assistant_timings_txt = convertView.findViewById(R.id.h_assistant_timings_txt);
      h_assistant_docassign_txt = convertView.findViewById(R.id.h_assistant_docassign_txt);
      h_assistant_image = convertView.findViewById(R.id.h_assistant_image);
      h_assistant_edit_img = convertView.findViewById(R.id.h_assistant_edit_img);
      h_assistant_del_img = convertView.findViewById(R.id.h_assistant_del_img);
      h_assistant_list_lay = convertView.findViewById(R.id.h_assistant_list_lay);

    } else if (type.equalsIgnoreCase("h_presclist")) {

      main_innerlist = convertView.findViewById(R.id.main_innerlist);
      expand_img = convertView.findViewById(R.id.expand_img);
      collapse_img = convertView.findViewById(R.id.collapse_img);
      h_maintitle_txt = convertView.findViewById(R.id.h_maintitle_txt);
      mainlist_lay = convertView.findViewById(R.id.mainlist_lay);
      h_prescSub_enrollID = convertView.findViewById(R.id.h_prescSub_enrollID);
      h_prescSub_consulted_dr = convertView.findViewById(R.id.h_prescSub_consulted_dr);



    }else if (type.equalsIgnoreCase("h_presinnerlist")) {

       h_prescSub_enrollID = convertView.findViewById(R.id.h_prescSub_enrollID);
       h_prescSub_consulted_dr = convertView.findViewById(R.id.h_prescSub_consulted_dr);
       h_pres_apt_date_time= convertView.findViewById(R.id.h_pres_apt_date_time);
       h_ttlpres_lay=convertView.findViewById(R.id.h_ttlpres_lay);


     } else if (type.equalsIgnoreCase("h_patientlist_main")) {
      main_innerlist = convertView.findViewById(R.id.main_innerlist);
      expand_img = convertView.findViewById(R.id.expand_img);
      collapse_img = convertView.findViewById(R.id.collapse_img);
      h_maintitle_txt = convertView.findViewById(R.id.h_maintitle_txt);
      mainlist_lay = convertView.findViewById(R.id.mainlist_lay);

    } else if (type.equalsIgnoreCase("p_diagnose_recyler")) {
      p_diagnose_sug_count_txt = convertView.findViewById(R.id.p_diagnose_sug_count_txt);
      p_diagnose_sug_test_txt = convertView.findViewById(R.id.p_diagnose_sug_test_txt);
      p_diagnose_sug_listlay = convertView.findViewById(R.id.p_diagnose_sug_listlay);

    } else if (type.equalsIgnoreCase("p_tst_sugstn")) {
      p_tst_delete_img = convertView.findViewById(R.id.p_tst_delete_img);
      p_tst_date_txt = convertView.findViewById(R.id.p_tst_date_txt);
      p_tst_diastc_txt = convertView.findViewById(R.id.p_tst_diastc_txt);
      p_tst_rfdctr_txt = convertView.findViewById(R.id.p_tst_rfdctr_txt);
      p_tst_ultrasund_txt = convertView.findViewById(R.id.p_tst_ultrasund_txt);
      p_tst_tft_txt = convertView.findViewById(R.id.p_tst_tft_txt);
      p_tst_cbp_txt = convertView.findViewById(R.id.p_tst_cbp_txt);
      testsuggested_lay= convertView.findViewById(R.id.testsuggested_lay);
    }  else if (type.equalsIgnoreCase("h_patentlst")) {

      patientlist_name_txt = convertView.findViewById(R.id.patientlist_name_txt);
      patientlist_dr_name_txt = convertView.findViewById(R.id.patientlist_dr_name_txt);
      patientlist_apptime_txt = convertView.findViewById(R.id.patientlist_apptime_txt);
      patientlist_arrow_img = convertView.findViewById(R.id.patientlist_arrow_img);
      h_patientlist_listlay = convertView.findViewById(R.id.h_patientlist_listlay);
      h_patnttmngs_lay = convertView.findViewById(R.id.h_patnttmngs_lay);

    } else if (type.equalsIgnoreCase("diagnose_recyler")) {
      diagnose_sug_count_txt = convertView.findViewById(R.id.diagnose_sug_count_txt);
      diagnose_sug_test_txt = convertView.findViewById(R.id.diagnose_sug_test_txt);
      diagnose_sug_listlay = convertView.findViewById(R.id.diagnose_sug_listlay);

    } else if (type.equalsIgnoreCase("prescrption_details")) {
      prescrip_count_txt = convertView.findViewById(R.id.prescrip_count_txt);
      prescrip_name_txt = convertView.findViewById(R.id.prescrip_name_txt);
      prescrip_days_txt = convertView.findViewById(R.id.prescrip_days_txt);
      prescrip_quantity_txt = convertView.findViewById(R.id.prescrip_quantity_txt);
      prescrip_times_txt = convertView.findViewById(R.id.prescrip_times_txt);
      prescrip_intake_txt = convertView.findViewById(R.id.prescrip_intake_txt);
      prescrip_details_listlay = convertView.findViewById(R.id.prescrip_details_listlay);
      prescrip_note_lay = convertView.findViewById(R.id.prescrip_note_lay);
       pp_note_txt=convertView.findViewById(R.id.pp_note_txt);

    }  else if (type.equalsIgnoreCase("doc_prfle")) {

      d_prf_dctrnme_lay = convertView.findViewById(R.id.d_prf_dctrnme_lay);
      d_prf_apntmnt_lay = convertView.findViewById(R.id.d_prf_apntmnt_lay);
      d_prf_up_img = convertView.findViewById(R.id.d_prf_up_img);
      d_prf_down_img = convertView.findViewById(R.id.d_prf_down_img);

      d_prf_dctrnme_txt= convertView.findViewById(R.id.d_prf_dctrnme_txt);


      d_frmday_edtx = convertView.findViewById(R.id.d_frmday_edtx);
      d_today_edtx = convertView.findViewById(R.id.d_today_edtx);
      h_custm_edtx = convertView.findViewById(R.id.h_custm_edtx);
      d_avabletme_edtx = convertView.findViewById(R.id.d_avabletme_edtx);
      d_avabletme_edtx2 = convertView.findViewById(R.id.d_avabletme_edtx2);
      d_avabletme_edtx3 = convertView.findViewById(R.id.d_avabletme_edtx3);
      d_avabletme_edtx4 = convertView.findViewById(R.id.d_avabletme_edtx4);
      d_tmetwo_edtx = convertView.findViewById(R.id.d_tmetwo_edtx);
      d_tmetwo_edtx2 = convertView.findViewById(R.id.d_tmetwo_edtx2);
      d_tmetwo_edtx3 = convertView.findViewById(R.id.d_tmetwo_edtx3);
      d_tmetwo_edtx4 = convertView.findViewById(R.id.d_tmetwo_edtx4);

      h_addoc_add_day_btn=convertView.findViewById(R.id.h_addoc_add_day_btn);
      appointment_slot_rv=convertView.findViewById(R.id.appointment_slot_rv);


    } else if (type.equalsIgnoreCase("addapointment")) {

      ad_patntnme_txt= convertView.findViewById(R.id.ad_patntnme_txt);
      ad_doc_up_img = convertView.findViewById(R.id.ad_doc_up_img);
      ad_doc_down_img = convertView.findViewById(R.id.ad_doc_down_img);
      add_appointment_lay = convertView.findViewById(R.id.add_appointment_lay);
      appointment_innerlay = convertView.findViewById(R.id.appointment_innerlay);
      pat_search_btn = convertView.findViewById(R.id.pat_search_btn);
      search_edtxt = convertView.findViewById(R.id.search_edtxt);


    } else if (type.equalsIgnoreCase("doc_details")) {
      h_docmain_lay = convertView.findViewById(R.id.h_docmain_lay);
      docdetails_txt = convertView.findViewById(R.id.docdetails_txt);
    } else if (type.equalsIgnoreCase("medication")) {

      yellow_circle = convertView.findViewById(R.id.yellow_circle);
      red_circle = convertView.findViewById(R.id.red_circle);
    } else if (type.equalsIgnoreCase("h_docsinnerlist")) {

      docdetails_lay = convertView.findViewById(R.id.docdetails_lay);
      docname_txt= convertView.findViewById(R.id.docname_txt);
      doctime_txt= convertView.findViewById(R.id.doctime_txt);
      docattender_txt= convertView.findViewById(R.id.docattender_txt);
      f_edit_img= convertView.findViewById(R.id.f_edit_img);
      f_del_img= convertView.findViewById(R.id.f_del_img);
       h_docimg=convertView.findViewById(R.id.h_docimg);

    } else if (type.equalsIgnoreCase("p_prescription")) {
      doc_hstlpresr_lay = convertView.findViewById(R.id.doc_hstlpresr_lay);
      doc_enroleid_txt = convertView.findViewById(R.id.doc_enroleid_txt);
      doc_problem_txt = convertView.findViewById(R.id.doc_problem_txt);
      doc_appointment_txt = convertView.findViewById(R.id.doc_appointment_txt);
      doc_arrow_img = convertView.findViewById(R.id.doc_arrow_img);
    } else if (type.equalsIgnoreCase("p_doc_prescription")) {

      h_docpres_lay = convertView.findViewById(R.id.h_docpres_lay);
      h_prescr_docname_txt = convertView.findViewById(R.id.h_prescr_docname_txt);
      h_prescr_problem_txt = convertView.findViewById(R.id.h_prescr_problem_txt);
      h_prescr_appdatetime_txt = convertView.findViewById(R.id.h_prescr_appdatetime_txt);
      h_prescr_arrow_img = convertView.findViewById(R.id.h_prescr_arrow_img);

    } else if (type.equalsIgnoreCase("h_patientinnerlist")) {

      h_patmain_lay = convertView.findViewById(R.id.h_patmain_lay);
      docname_txt = convertView.findViewById(R.id.docname_txt);
      patient_name = convertView.findViewById(R.id.patient_name);
      doc_appointment_txt = convertView.findViewById(R.id.doc_appointment_txt);

      p_enroll_txt = convertView.findViewById(R.id.p_enroll_txt);
      doc_problem_txt = convertView.findViewById(R.id.doc_problem_txt);
      status_txt=convertView.findViewById(R.id.status_txt);

    } else if (type.equalsIgnoreCase("d_assistant")) {

      numofass_lay = convertView.findViewById(R.id.numofass_lay);
      h_assistant_name_txt = convertView.findViewById(R.id.h_assistant_name_txt);
      h_assistant_timings_txt = convertView.findViewById(R.id.h_assistant_timings_txt);
      h_assistant_docassign_txt = convertView.findViewById(R.id.h_assistant_docassign_txt);
      h_assistant_image = convertView.findViewById(R.id.h_assistant_image);
      h_assistant_edit_img = convertView.findViewById(R.id.h_assistant_edit_img);
      h_assistant_del_img = convertView.findViewById(R.id.h_assistant_del_img);
      h_assistant_list_lay = convertView.findViewById(R.id.h_assistant_list_lay);

    } else if (type.equalsIgnoreCase("d_doctorslist")) {
      doc_patientList_name = convertView.findViewById(R.id.doc_patientList_name);
      p_mainlist_lay = convertView.findViewById(R.id.p_mainlist_lay);
    } else if (type.equalsIgnoreCase("d_patnt_que")) {

      h_patnttmngs_lay = convertView.findViewById(R.id.h_patnttmngs_lay);
      patientlist_name_txt = convertView.findViewById(R.id.patientlist_name_txt);
      patientlist_apptime_txt = convertView.findViewById(R.id.patientlist_apptime_txt);
      patientlist_arrow_img = convertView.findViewById(R.id.patientlist_arrow_img);

    } else if (type.equalsIgnoreCase("d_patent_chcked")) {

      h_patnttmngs_lay = convertView.findViewById(R.id.h_patnttmngs_lay);
      patientlist_name_txt = convertView.findViewById(R.id.patientlist_name_txt);
      patientlist_apptime_txt = convertView.findViewById(R.id.patientlist_apptime_txt);
      patientlist_arrow_img = convertView.findViewById(R.id.patientlist_arrow_img);

    } else if (type.equalsIgnoreCase("doc_patient_details")) {

      addlistitem_lay = convertView.findViewById(R.id.addlistitem_lay);
      patlisttitle_txt = convertView.findViewById(R.id.patlisttitle_txt);
      p_img = convertView.findViewById(R.id.p_img);

    } else if (type.equalsIgnoreCase("patientlist_history")) {
      d_prf_dctrnme_lay = convertView.findViewById(R.id.d_prf_dctrnme_lay);
      pat_weight_txt = convertView.findViewById(R.id.pat_weight_txt);
      d_prf_up_img = convertView.findViewById(R.id.d_prf_up_img);
      d_prf_down_img = convertView.findViewById(R.id.d_prf_down_img);
    } else if (type.equalsIgnoreCase("d_ttlpreslist")) {


      prescrip_pname_txt = convertView.findViewById(R.id.prescrip_pname_txt);
      dpre_ttlpres_lay = convertView.findViewById(R.id.dpre_ttlpres_lay);
      prescrip_enrole_txt = convertView.findViewById(R.id.prescrip_enrole_txt);
      prescrip_datetime_txt = convertView.findViewById(R.id.prescrip_datetime_txt);
    } else if (type.equalsIgnoreCase("patpresr_list")) {

      patownpre_lay = convertView.findViewById(R.id.patownpre_lay);
      patownpre_docname_txt = convertView.findViewById(R.id.patownpre_docname_txt);
      patownpre_date_txt = convertView.findViewById(R.id.patownpre_date_txt);
      patownpre_problem_txt = convertView.findViewById(R.id.patownpre_problem_txt);

    } else if (type.equalsIgnoreCase("blockdoctor")) {
       blockeddoc_name_txt = convertView.findViewById(R.id.blockeddoc_name_txt);
       blockeddoc_desig_txt = convertView.findViewById(R.id.blockeddoc_desig_txt);
       blockeddoc_address_txt = convertView.findViewById(R.id.blockeddoc_address_txt);
       blocked_txt = convertView.findViewById(R.id.blocked_txt);
       blocked_doc_image = convertView.findViewById(R.id.blocked_doc_image);
       blockdoc_list_lay = convertView.findViewById(R.id.blockdoc_list_lay);
       unblock_doc_btn= convertView.findViewById(R.id.unblock_doc_btn);
       phonenum_txt=convertView.findViewById(R.id.phonenum_txt);

     } else if (type.equalsIgnoreCase("patprescr_doc_list")) {

      patotherdoc_lay = convertView.findViewById(R.id.patotherdoc_lay);
      patotherdoc_name_txt = convertView.findViewById(R.id.patotherdoc_name_txt);
      patotherdoc_problem_txt = convertView.findViewById(R.id.patotherdoc_problem_txt);
      patotherdoc_date_txt = convertView.findViewById(R.id.patotherdoc_date_txt);
       enrollid_txt=convertView.findViewById(R.id.enrollid_txt);
      enrolledlay=convertView.findViewById(R.id.enrolledlay);

    } else if (type.equalsIgnoreCase("diagnose_report")) {

      p_diareports_lay = convertView.findViewById(R.id.p_diareports_lay);
      p_diareports_date_txt = convertView.findViewById(R.id.p_diareports_date_txt);
      p_diareports_dname_txt = convertView.findViewById(R.id.p_diareports_dname_txt);
      p_diareports_refdoc_txt = convertView.findViewById(R.id.p_diareports_refdoc_txt);
      p_diareports_edit_img = convertView.findViewById(R.id.p_diareports_edit_img);
      p_diareports_del_img = convertView.findViewById(R.id.p_diareports_del_img);

    } else if (type.equalsIgnoreCase("p_submember")) {

      p_submember_lay = convertView.findViewById(R.id.p_submember_lay);
      submem_relation_txt = convertView.findViewById(R.id.submem_relation_txt);
      submem_name_txt = convertView.findViewById(R.id.submem_name_txt);
      submem_date_txt = convertView.findViewById(R.id.submem_date_txt);
      submember_image = convertView.findViewById(R.id.submember_image);
      submem_edit_img = convertView.findViewById(R.id.submem_edit_img);
      submem_del_img = convertView.findViewById(R.id.submem_del_img);
    } else if (type.equalsIgnoreCase("subuserdoc_prescr")) {

      patotherdoc_lay = convertView.findViewById(R.id.patotherdoc_lay);
      patotherdoc_name_txt = convertView.findViewById(R.id.patotherdoc_name_txt);
      patotherdoc_problem_txt = convertView.findViewById(R.id.patotherdoc_problem_txt);
      patotherdoc_date_txt = convertView.findViewById(R.id.patotherdoc_date_txt);

    } else if (type.equalsIgnoreCase("subuser_prescr")) {

      patownpre_lay = convertView.findViewById(R.id.patownpre_lay);
      patownpre_docname_txt = convertView.findViewById(R.id.patownpre_docname_txt);
      patownpre_date_txt = convertView.findViewById(R.id.patownpre_date_txt);
      patownpre_problem_txt = convertView.findViewById(R.id.patownpre_problem_txt);


    } else if (type.equalsIgnoreCase("ass_inner_patientlist")) {

      as_patdetails_lay = convertView.findViewById(R.id.as_patdetails_lay);
       as_pat_aptid = convertView.findViewById(R.id.as_pat_aptid);
       as_pat_name = convertView.findViewById(R.id.as_pat_name);
       as_pat_mobile = convertView.findViewById(R.id.as_pat_mobile);
       as_pat_apttime = convertView.findViewById(R.id.as_pat_apttime);
    } else if (type.equalsIgnoreCase("marketing_exicutive")) {

      mar_franchise_lay = convertView.findViewById(R.id.mar_franchise_lay);
      mar_franchisename_txt = convertView.findViewById(R.id.mar_franchisename_txt);
      mar_franchisenumber_txt = convertView.findViewById(R.id.mar_franchisenumber_txt);
      mar_franchise_address_txt = convertView.findViewById(R.id.mar_franchise_email_txt);
      mar_franchise_img = convertView.findViewById(R.id.mar_franchise_img);
      mar_franchise_edit_img = convertView.findViewById(R.id.mar_franchise_edit_img);
      mar_franchise_del_img = convertView.findViewById(R.id.mar_franchise_del_img);


    } else if (type.equalsIgnoreCase("marketing_exicutiveone")) {

      franshicelist_lay = convertView.findViewById(R.id.franshicelist_lay);
      franshicelist_image = convertView.findViewById(R.id.franshicelist_image);
      franshicelist_name_txt = convertView.findViewById(R.id.franshicelist_name_txt);
      franshicelist_number_txt = convertView.findViewById(R.id.franshicelist_number_txt);
      franshicelist_address_txt = convertView.findViewById(R.id.franshicelist_address_txt);
      franshicelist_edit_img = convertView.findViewById(R.id.franshicelist_edit_img);
      franshicelist_del_img = convertView.findViewById(R.id.franshicelist_del_img);

    } else if (type.equalsIgnoreCase("tl_franchises_one")) {

      tl_franshice_lay = convertView.findViewById(R.id.tl_franshice_lay);
      franchase_image = convertView.findViewById(R.id.franchase_image);
      franchasename_txt = convertView.findViewById(R.id.franchasename_txt);
      franchasenumber_txt = convertView.findViewById(R.id.franchasenumber_txt);
      franchaseaddress_txt = convertView.findViewById(R.id.franchaseaddress_txt);
      franchasedel_img = convertView.findViewById(R.id.franchasedel_img);
      franchaseedit_img = convertView.findViewById(R.id.franchaseedit_img);
    } else if (type.equalsIgnoreCase("tl_franchise")) {

      fradetls_name_txt = convertView.findViewById(R.id.fradetls_name_txt);
      tabcount_txt=convertView.findViewById(R.id.tabcount_txt);
    } else if (type.equalsIgnoreCase("tl_franchise_one")) {

      fr_title_txt = convertView.findViewById(R.id.fr_title_txt);

      frdetails_list_lay = convertView.findViewById(R.id.frdetails_list_lay);
    }else if (type.equalsIgnoreCase("f_hospital_five")) {
      docdetails_lay = convertView.findViewById(R.id.docdetails_lay);
      f_hospital_txt = convertView.findViewById(R.id.f_hospital_txt);
      number_txt = convertView.findViewById(R.id.number_txt);
      f_address_txt = convertView.findViewById(R.id.f_address_txt);
      f_app_date_txt = convertView.findViewById(R.id.f_app_date_txt);
      f_del_img = convertView.findViewById(R.id.f_del_img);
      f_arrow_img = convertView.findViewById(R.id.f_arrow_img);
      f_edit_img = convertView.findViewById(R.id.f_edit_img);
      lead_view=convertView.findViewById(R.id.lead_view);

    } else if (type.equalsIgnoreCase("tl_hospitals")) {
      tl_hsptl_lay = convertView.findViewById(R.id.tl_hsptl_lay);
      tl_hospital_image = convertView.findViewById(R.id.tl_hospital_image);
      tl_hospital_txt = convertView.findViewById(R.id.tl_hospital_txt);
      tl_contact_txt = convertView.findViewById(R.id.tl_contact_txt);
      tl_address_txt = convertView.findViewById(R.id.tl_address_txt);
      tl_del_img = convertView.findViewById(R.id.tl_del_img);
      tl_edit_img = convertView.findViewById(R.id.tl_edit_img);



    } else if (type.equalsIgnoreCase("f_patient_one")) {
      docdetails_lay = convertView.findViewById(R.id.docdetails_lay);
      patient_name = convertView.findViewById(R.id.patient_name);
      tl_hospital_txt = convertView.findViewById(R.id.tl_hospital_txt);
      patient_number = convertView.findViewById(R.id.patient_number);
      patient_address = convertView.findViewById(R.id.patient_address);
      patient_edit_img = convertView.findViewById(R.id.patient_edit_img);
      patient_del_img = convertView.findViewById(R.id.patient_del_img);
      tl_pat_image=convertView.findViewById(R.id.tl_pat_image);
    }

    else if (type.equalsIgnoreCase("d_patient_one")) {
      docdetails_lay = convertView.findViewById(R.id.docdetails_lay);
      patient_name = convertView.findViewById(R.id.patient_name);
      tl_hospital_txt = convertView.findViewById(R.id.tl_hospital_txt);
      patient_number = convertView.findViewById(R.id.patient_number);
      patient_address = convertView.findViewById(R.id.patient_address);
      tl_pat_image=convertView.findViewById(R.id.tl_pat_image);
    }

    else if (type.equalsIgnoreCase("tl_doctors")) {
      doctor_one_listlay = convertView.findViewById(R.id.doctor_one_listlay);
      hospital_txt = convertView.findViewById(R.id.hospital_txt);
      doc_contact_txt = convertView.findViewById(R.id.doc_contact_txt);
      tl_doc_address_txt = convertView.findViewById(R.id.tl_doc_address_txt);
      hospital_image = convertView.findViewById(R.id.hospital_image);
      tl_doc_del_img = convertView.findViewById(R.id.tl_doc_del_img);
      tl_doc_edit_img = convertView.findViewById(R.id.tl_doc_edit_img);

    } else if (type.equalsIgnoreCase("tl_hospitals_one")) {
      hospitalname_txt = convertView.findViewById(R.id.hospitalname_txt);
      hospital_number_txt = convertView.findViewById(R.id.hospital_number_txt);
      hospital_address_txt = convertView.findViewById(R.id.hospital_address_txt);
      hospitals_image = convertView.findViewById(R.id.hospitals_image);
      hospital_one_listlay = convertView.findViewById(R.id.hospital_one_listlay);

    } else if (type.equalsIgnoreCase("f_patient")) {
      f_patient_address = convertView.findViewById(R.id.f_patient_address);
      f_patient_number = convertView.findViewById(R.id.f_patient_number);
      f_patient_name = convertView.findViewById(R.id.f_patient_name);
      f_patientlist_listlay = convertView.findViewById(R.id.f_patientlist_listlay);
      pat_img=convertView.findViewById(R.id.pat_img);

    } else if (type.equalsIgnoreCase("f_patient_three")) {
      f_patient_listlay = convertView.findViewById(R.id.f_patient_listlay);
      f_patientlist_address = convertView.findViewById(R.id.f_patientlist_address);
      f_patientlist_number = convertView.findViewById(R.id.f_patientlist_number);
      f_patientlist_name = convertView.findViewById(R.id.f_patientlist_name);
      subfra_img=convertView.findViewById(R.id.subfra_img);

    }else if (type.equalsIgnoreCase("tl_doctor")) {
      docname_txt = convertView.findViewById(R.id.docname_txt);
      doc_number_txt = convertView.findViewById(R.id.doc_number_txt);
      doc_address_txt = convertView.findViewById(R.id.doc_address_txt);
      doctor_img = convertView.findViewById(R.id.doctor_img);
      doctor_two_listlay = convertView.findViewById(R.id.doctor_two_listlay);

    }
    else if (type.equalsIgnoreCase("othdoc_prescrip")) {
      doc_enroleid_txt = convertView.findViewById(R.id.doc_enroleid_txt);
      patient_name = convertView.findViewById(R.id.patient_name);
      docname_txt = convertView.findViewById(R.id.docname_txt);
      doc_appointment_txt = convertView.findViewById(R.id.doc_appointment_txt);
      doc_hstlpresr_lay = convertView.findViewById(R.id.doc_hstlpresr_lay);

    }else if (type.equalsIgnoreCase("testsuggested"))     {
      descr_txt = convertView.findViewById(R.id.descr_txt);
      test_reff_dr = convertView.findViewById(R.id.test_reff_dr);
      test_sug_date = convertView.findViewById(R.id.test_sug_date);
      helplist_lay= convertView.findViewById(R.id.helplist_lay);
    }
    else if (type.equalsIgnoreCase("brand_suggestions"))     {

      b_lay = convertView.findViewById(R.id.b_lay);
      b_brand_txt = convertView.findViewById(R.id.b_brand_txt);
      b_checkbox = convertView.findViewById(R.id.b_checkbox);
    }

    else if (type.equalsIgnoreCase("medication_view"))
    {



      search_brand_edt = convertView.findViewById(R.id.search_brand_edt);
      molecule_name_edt = convertView.findViewById(R.id.molecule_name_edt);
      no_days_edt = convertView.findViewById(R.id.no_days_edt);
      dose_edt = convertView.findViewById(R.id.dose_edt);
      medication_remarks = convertView.findViewById(R.id.medication_remarks);

      suggestedbrands_btn = convertView.findViewById(R.id.suggestedbrands_btn);
      sel_brands_txt = convertView.findViewById(R.id.sel_brands_txt);
      morning_cb = convertView.findViewById(R.id.morning_cb);
      afternoon_cb = convertView.findViewById(R.id.afternoon_cb);
      evening_cb = convertView.findViewById(R.id.evening_cb);
      beforefood_rb = convertView.findViewById(R.id.beforefood_rb);
      afterfood_rb = convertView.findViewById(R.id.afterfood_rb);

      med_del_btn = convertView.findViewById(R.id.med_del_btn);
      p_save_btn=convertView.findViewById(R.id.p_save_btn);

    }

    else if (type.equalsIgnoreCase("physical_examination")) {
      fradetls_name_txt = convertView.findViewById(R.id.fradetls_name_txt);
      tabcount_txt = convertView.findViewById(R.id.tabcount_txt);

    }
    else if (type.equalsIgnoreCase("diagnose_suggest")) {

      reflabname_edtx = convertView.findViewById(R.id.reflabname_edtx);
      reftestname_edtx = convertView.findViewById(R.id.reftestname_edtx);
      ref_del_img=convertView.findViewById(R.id.ref_del_img);
      ref_save_btn=convertView.findViewById(R.id.ref_save_btn);

    }



  }

  private void PhyPopupupdate(final EditText h_test_list_status_select,Activity activity,final ArrayList<HashMap<String,String>>
          data_list,final int pos,final String type) {
    final ListPopupWindow listPopupWindow=new ListPopupWindow(activity);
    listPopupWindow.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay,  StoredObjects.physuggestionsnames_list));
    listPopupWindow.setAnchorView(h_test_list_status_select);
    listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        h_test_list_status_select.setText( StoredObjects.physuggestionsnames_list.get(position));

        updatedata(data_list,data_list.get(pos),pos,Doc_Patient_Details.data_list_one.get(position).get("suggestion")
                ,Doc_Patient_Details.data_list_one.get(position).get("suggestion_id"),type);
        listPopupWindow.dismiss();

      }
    });

    listPopupWindow.show();
  }
  private void PhyPopup(final EditText h_test_list_status_select,Activity activity,final ArrayList<HashMap<String,String>>
                            data_list,final int pos,final String type) {
    final ListPopupWindow listPopupWindow=new ListPopupWindow(activity);
    listPopupWindow.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay,  StoredObjects.physuggestionsnames_list));
    listPopupWindow.setAnchorView(h_test_list_status_select);
    listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        h_test_list_status_select.setText( StoredObjects.physuggestionsnames_list.get(position));

        updatedata(data_list,data_list.get(pos),pos,StoredObjects.physuggestions_list.get(position).get("suggestion")
        ,StoredObjects.physuggestions_list.get(position).get("suggestion_id"),type);
        listPopupWindow.dismiss();

      }
    });

    listPopupWindow.show();
  }
  void assign_data(final ArrayList<HashMap<String, String>> datalist, final int position, String formtype) {
  if (formtype.equalsIgnoreCase("prescriptionslist")) {

      doc_enroleid_txt.setText(datalist.get(position).get("enroll_id"));
      patient_name_txt.setText(datalist.get(position).get("patient_name"));
      doc_problem_txt.setText(datalist.get(position).get("problem"));
      clinic_name_txt.setText(datalist.get(position).get("clinic_name"));
      doc_name_txt.setText(datalist.get(position).get("doctor_name"));
      doc_appointment_txt.setText(datalist.get(position).get("appointment_date_time"));

      doc_hstlpresr_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          //fragmentcalling(new );

        }
      });

    }

     if (formtype.equalsIgnoreCase("ass_dashboard")) {

      dashboard_title_txt.setText(datalist.get(position).get("name"));
      count_txt.setText(datalist.get(position).get("count"));


      dashboard_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (position == 0) {
            fragmentcalling(new Ass_PatientList());
          } else if (position == 1) {
            fragmentcalling(new Ass_AppointmentList());
          } else {
            fragmentcalling(new Asst_Dashboard());
          }
        }
      });

    }
    else if (formtype.equalsIgnoreCase("doc_dashboard")) {

      dashboard_title_txt.setText(datalist.get(position).get("name"));
      count_txt.setText(datalist.get(position).get("count"));

      if (position == 5) {

        dashboard_bg_lay.setBackgroundColor(activity.getResources().getColor(R.color.yellow));
      } else if (position == 6) {

        dashboard_bg_lay.setBackgroundColor(activity.getResources().getColor(R.color.yellow));
      }

      dashboard_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (position == 0) {
            d_fragmentcalling(new Doc_Assistant());
          } else if (position == 1) {

            if(SideMenu.hospitals_list.size()==0||SideMenu.hospitals_list.size()==1){
              if(SideMenu.hospitals_list.size()==1){
                StoredObjects.Doc_Hospital_Id=SideMenu.hospitals_list.get(0).get("hospital_id");
              }
              d_fragmentcalling(new Doc_Patient_List());

            }else{
              StoredObjects.tab_type="patients";
              d_fragmentcalling(new Doc_Patientlistmain());

            }

          } else if (position == 2) {
            //fragmentcalling(new Doc_Patientlistmain());

            if(SideMenu.hospitals_list.size()==0||SideMenu.hospitals_list.size()==1){
              if(SideMenu.hospitals_list.size()==1){
                StoredObjects.Doc_Hospital_Id=SideMenu.hospitals_list.get(0).get("hospital_id");
              }
              d_fragmentcalling(new Doc_Appointmentslist());

            }else{
              StoredObjects.tab_type="appointments";
              d_fragmentcalling(new Doc_Patientlistmain());

            }
          } else if (position == 3) {

            if(SideMenu.hospitals_list.size()==0||SideMenu.hospitals_list.size()==1){
              if(SideMenu.hospitals_list.size()==1){
                StoredObjects.Doc_Hospital_Id=SideMenu.hospitals_list.get(0).get("hospital_id");
              }
              d_fragmentcalling(new DocTotalPrescriptionsList());

            }else{
              StoredObjects.tab_type="prescr";
              d_fragmentcalling(new Doc_Patientlistmain());

            }


          } else if (position == 4) {
            if(SideMenu.hospitals_list.size()==0||SideMenu.hospitals_list.size()==1){
              if(SideMenu.hospitals_list.size()==1){
                StoredObjects.Doc_Hospital_Id=SideMenu.hospitals_list.get(0).get("hospital_id");
              }
              d_fragmentcalling(new DocTotalPrescriptionsList());

            }else{
              StoredObjects.tab_type="prescr";
              d_fragmentcalling(new Doc_Patientlistmain());

            }
          } else if (position == 5) {
            if(SideMenu.hospitals_list.size()==0||SideMenu.hospitals_list.size()==1){
              if(SideMenu.hospitals_list.size()==1){
                StoredObjects.Doc_Hospital_Id=SideMenu.hospitals_list.get(0).get("hospital_id");
              }
              d_fragmentcalling(new Doc_TestSuggested());

            }else{
              StoredObjects.tab_type="testsugsted";
              d_fragmentcalling(new Doc_Patientlistmain());

            }
          } else if (position == 6) {
            if(SideMenu.hospitals_list.size()==0||SideMenu.hospitals_list.size()==1){
              if(SideMenu.hospitals_list.size()==1){
                StoredObjects.Doc_Hospital_Id=SideMenu.hospitals_list.get(0).get("hospital_id");
              }
              d_fragmentcalling(new Doc_TestSuggested());

            }else{
              StoredObjects.tab_type="testsugsted";
              d_fragmentcalling(new Doc_Patientlistmain());

            }
          }
        }
      });

    }
    else if (formtype.equalsIgnoreCase("h_dashboard")) {

      dashboard_title_txt.setText(datalist.get(position).get("name"));
      count_txt.setText(datalist.get(position).get("count"));

      if (position == 6) {

        dashboard_bg_lay.setBackgroundColor(activity.getResources().getColor(R.color.yellow));
      }else if (position == 7) {

        dashboard_bg_lay.setBackgroundColor(activity.getResources().getColor(R.color.yellow));
      }

      dashboard_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (position == 0) {
            d_fragmentcalling(new H_DoctorsList());
          } else if (position == 1) {
            d_fragmentcalling(new H_Assistant());
          } else if (position == 2) {
            d_fragmentcalling(new H_TotalPatientList());
          } else if (position == 3) {
            d_fragmentcalling(new H_PatientList_Main());
          } else if (position == 4) {
            StoredObjects.redirect_type="menu";
            d_fragmentcalling(new H_TotalPrescriptions());
          } else if (position == 5) {
            StoredObjects.redirect_type="menu";
            d_fragmentcalling(new H_TotalPrescriptions());
          } else if (position == 6) {
            StoredObjects.redirect_type="menu";
            d_fragmentcalling(new H_Test_Suggested());
          } else if (position == 7) {
            StoredObjects.redirect_type="menu";
            d_fragmentcalling(new H_Test_Suggested());
          }
        }
      });

    }
    else if (formtype.equalsIgnoreCase("p_dashboard")) {
      dashboard_title_txt.setText(datalist.get(position).get("name"));
      count_txt.setText(datalist.get(position).get("count"));

      if (position == 2) {

        dashboard_bg_lay.setBackgroundColor(activity.getResources().getColor(R.color.yellow));
      }

      dashboard_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (position == 0) {
            d_fragmentcalling(new P_Sub_Member());
          } else if (position == 1) {
            d_fragmentcalling(new P_Prescriptionslist());
          } else if (position == 2) {
            d_fragmentcalling(new P_Test_Sugestions());
          }
        }
      });

    }
    else if (formtype.equalsIgnoreCase("tl_dashboard")) {

      dashboard_title_txt.setText(datalist.get(position).get("name"));
      count_txt.setText(datalist.get(position).get("count"));

      if (position == 2) {
        dashboard_bg_lay.setBackgroundColor(activity.getResources().getColor(R.color.yellow));
      }

      dashboard_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (position == 0) {
            d_fragmentcalling(new Marketing_Exicutive());
          } else if (position == 1) {
            d_fragmentcalling(new TL_Franchisee_List());
          } else if (position == 2) {
            d_fragmentcalling(new TL_Hospitals());
          } else if (position == 3) {
            d_fragmentcalling(new TL_Doctors());
          } else if (position == 4) {
            d_fragmentcalling(new TL_Patients());
          } else if (position == 5) {
            StoredObjects.redirect_type="dashboard";
            StoredObjects.tab_type="Hosiptal";
            d_fragmentcalling(new Franchisee_Details());
          } else if (position == 6) {
            StoredObjects.redirect_type="dashboard";
            StoredObjects.tab_type="Doctor";
            d_fragmentcalling(new Franchisee_Details());
          } else if (position == 7) {
            StoredObjects.redirect_type="dashboard";
            StoredObjects.tab_type="Patient";
            d_fragmentcalling(new Franchisee_Details());
          }
        }
      });

    }
    else if (formtype.equalsIgnoreCase("franchisee_dashboard")) {

      dashboard_title_txt.setText(datalist.get(position).get("name"));
      count_txt.setText(datalist.get(position).get("count"));

      if (position == 1) {
        dashboard_bg_lay.setBackgroundColor(activity.getResources().getColor(R.color.yellow));
      }

      dashboard_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (position == 0) {
            d_fragmentcalling(new TL_Franchisee_List());
          }  else if (position == 1) {
            d_fragmentcalling(new TL_Hospitals());
          } else if (position == 2) {
            d_fragmentcalling(new TL_Doctors());
          } else if (position == 3) {
            d_fragmentcalling(new TL_Patients());
          } else if (position == 4) {
            StoredObjects.redirect_type="dashboard";
            StoredObjects.tab_type="Hosiptal";
            d_fragmentcalling(new Franchisee_Details());
          } else if (position == 5) {
            StoredObjects.redirect_type="dashboard";
            StoredObjects.tab_type="Doctor";
            d_fragmentcalling(new Franchisee_Details());
          }else if (position == 6) {
            StoredObjects.redirect_type="dashboard";
            StoredObjects.tab_type="Patient";
            d_fragmentcalling(new Franchisee_Details());
          }
        }
      });

    }
    else if (formtype.equalsIgnoreCase("me_dashboard")) {

      dashboard_title_txt.setText(datalist.get(position).get("name"));
      count_txt.setText(datalist.get(position).get("count"));

      if (position == 1) {
        dashboard_bg_lay.setBackgroundColor(activity.getResources().getColor(R.color.yellow));
      }

      dashboard_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (position == 0) {
            d_fragmentcalling(new TL_Franchisee_List());
          }  else if (position == 1) {
            d_fragmentcalling(new TL_Hospitals());
          } else if (position == 2) {
            d_fragmentcalling(new TL_Doctors());
          } else if (position == 3) {
            d_fragmentcalling(new TL_Patients());
          } else if (position == 4) {
            StoredObjects.redirect_type="dashboard";
            StoredObjects.tab_type="Hosiptal";
            d_fragmentcalling(new Franchisee_Details());
          } else if (position == 5) {
            StoredObjects.redirect_type="dashboard";
            StoredObjects.tab_type="Doctor";
            d_fragmentcalling(new Franchisee_Details());
          }else if (position == 6) {
            StoredObjects.redirect_type="dashboard";
            StoredObjects.tab_type="Patient";
            d_fragmentcalling(new Franchisee_Details());
          }
        }
      });

    }
    else if (formtype.equalsIgnoreCase("subfranchisee_dashboard")) {

      dashboard_title_txt.setText(datalist.get(position).get("name"));
      count_txt.setText(datalist.get(position).get("count"));

      if (position == 0) {
        dashboard_bg_lay.setBackgroundColor(activity.getResources().getColor(R.color.yellow));
      }

      dashboard_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (position == 0) {
            d_fragmentcalling(new TL_Hospitals());
          } else if (position == 1) {
            d_fragmentcalling(new TL_Doctors());
          } else if (position == 2) {
            d_fragmentcalling(new TL_Patients());
          } else if (position == 3) {
            StoredObjects.redirect_type="dashboard";
            StoredObjects.tab_type="Hosiptal";
            d_fragmentcalling(new Franchisee_Details());
          } else if (position == 4) {
            StoredObjects.redirect_type="dashboard";
            StoredObjects.tab_type="Doctor";
            d_fragmentcalling(new Franchisee_Details());
          }else if (position == 5) {
            StoredObjects.redirect_type="dashboard";
            StoredObjects.tab_type="Patient";
            d_fragmentcalling(new Franchisee_Details());
          }
        }
      });

    }
    if (formtype.equalsIgnoreCase("doc_timeslots")) {


      h_avabletme_edtx3.setText(StoredObjects.time12hrsformat(datalist.get(position).get("from_time2")));
      h_tmetwo_edtx3.setText(StoredObjects.time12hrsformat(datalist.get(position).get("to_time2")));

      h_avabletme_edtx4.setText(StoredObjects.time12hrsformat(datalist.get(position).get("from_time3")));
      h_tmetwo_edtx4.setText(StoredObjects.time12hrsformat(datalist.get(position).get("to_time3")));

      h_frmday_edtx.setText(datalist.get(position).get("from_days"));
      h_today_edtx.setText(datalist.get(position).get("to_days"));

      h_avabletme_edtx.setText(StoredObjects.time12hrsformat(datalist.get(position).get("from_time")));
      h_tmetwo_edtx.setText(StoredObjects.time12hrsformat(datalist.get(position).get("to_time")));

      h_avabletme_edtx2.setText(StoredObjects.time12hrsformat(datalist.get(position).get("from_time1")));
      h_tmetwo_edtx2.setText(StoredObjects.time12hrsformat(datalist.get(position).get("to_time1")));

      del_icon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          updatedata(datalist,datalist.get(position),position,"remove","doc_timeslots_remove");

        }
      });

      h_today_edtx.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          ToDayListPopup(h_today_edtx,activity,datalist,position);
        }
      });

      h_frmday_edtx.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          FromdaysListPopup(h_frmday_edtx,activity,datalist,position);
        }
      });

      h_avabletme_edtx.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


          FromtimeListPopup(h_avabletme_edtx,activity,"0",datalist,position);
        }
      });

      h_tmetwo_edtx.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          toTimeListPopup(h_tmetwo_edtx,activity,"0",datalist,position);
        }
      });


      h_avabletme_edtx2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          FromtimeListPopup(h_avabletme_edtx2,activity,"1",datalist,position);
        }
      });

      h_tmetwo_edtx2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          toTimeListPopup(h_tmetwo_edtx2,activity,"1",datalist,position);
        }
      });


      h_avabletme_edtx3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          FromtimeListPopup(h_avabletme_edtx3,activity,"2",datalist,position);
        }
      });

      h_tmetwo_edtx3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          toTimeListPopup(h_tmetwo_edtx3,activity,"2",datalist,position);
        }
      });

      h_avabletme_edtx4.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          FromtimeListPopup(h_avabletme_edtx4,activity,"3",datalist,position);
        }
      });

      h_tmetwo_edtx4.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          toTimeListPopup(h_tmetwo_edtx4,activity,"3",datalist,position);
        }
      });



    }
    if (formtype.equalsIgnoreCase("f_patient_three")) {

      f_patientlist_name.setText(datalist.get(position).get("name"));
      f_patientlist_number.setText(datalist.get(position).get("phone"));
      f_patientlist_address.setText(datalist.get(position).get("email"));
      try {
        Glide.with(activity)
                .load(Uri.parse(RetrofitInstance.IMAGE_URL + datalist.get(position).get("image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(subfra_img);
      } catch (Exception e) {
        e.printStackTrace();

      }


    }else if (formtype.equalsIgnoreCase("p_diagnose_recyler")) {

      p_diagnose_sug_count_txt.setText(datalist.get(position).get("id"));
      p_diagnose_sug_test_txt.setText(datalist.get(position).get("test_name"));

    }
    else if (formtype.equalsIgnoreCase("medication_view"))
    {
  /*    EditText search_brand_edt,molecule_name_edt,no_days_edt,dose_edt,medication_remarks;
      Button suggestedbrands_btn;
      TextView sel_brands_txt;
      ImageView med_del_btn;*/

      search_brand_edt.setOnEditorActionListener(
              new EditText.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                  //Identifier of the action. This will be either the identifier you supplied,
                  //or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                  if (actionId == EditorInfo.IME_ACTION_DONE
                  ) {


                    String answer_txt=search_brand_edt.getText().toString().trim();

                    if(answer_txt.length()>0){


                      brandService(activity,answer_txt);

                    }
                    search_brand_edt.clearFocus();

                    StoredObjects.hide_keyboard(activity);
                    return true;
                  }
                  // Return true if you have consumed the action, else false.
                  return false;
                }
              });

      suggestedbrands_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          brand_list.clear();
          for(int k=0;k<d_brand_list.size();k++){
            HashMap<String, String> dumpData_update = new HashMap<String, String>();
            dumpData_update.put("BrandId", d_brand_list.get(k).get("BrandId"));
            dumpData_update.put("Brand", StoredObjects.stripHtml(d_brand_list.get(k).get("Brand")).replace("<br/>","\n"));
            dumpData_update.put("MoleculeId", d_brand_list.get(k).get("MoleculeId"));
            dumpData_update.put("Molecule", StoredObjects.stripHtml(d_brand_list.get(k).get("Molecule")).replace("<br/>","\n"));
            dumpData_update.put("is_viewed", "No");
            brand_list.add(dumpData_update);
          }
          if(brand_list.size()>0){

            assigndoctorpopup (activity,datalist,position);

          }else{
            StoredObjects.ToastMethod("No Brands found",activity);
          }
        }
      });
      if(datalist.get(position).get("intake").equalsIgnoreCase("After Food,Before Food")){
        beforefood_rb.setChecked(true);
        afterfood_rb.setChecked(true);
      }else if(datalist.get(position).get("intake").equalsIgnoreCase("After Food")){
        beforefood_rb.setChecked(false);
        afterfood_rb.setChecked(true);
      }else if(datalist.get(position).get("intake").equalsIgnoreCase("Before Food")){
        beforefood_rb.setChecked(true);
        afterfood_rb.setChecked(false);
      }else{
        beforefood_rb.setChecked(false);
        afterfood_rb.setChecked(false);
      }

      afterfood_rb.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          String updatevalue="";

          try {
            String dose=dose_edt.getText().toString().trim();
            String noofdays=no_days_edt.getText().toString().trim();
            String remarks=medication_remarks.getText().toString().trim();

            if(datalist.get(position).get("intake").equalsIgnoreCase("After Food,Before Food")){
               updatevalue="Before Food";
            }else if(datalist.get(position).get("intake").equalsIgnoreCase("After Food")){
              updatevalue="";
            }else if(datalist.get(position).get("intake").equalsIgnoreCase("Before Food")){
              updatevalue="After Food,Before Food";
            }else{
              updatevalue="After Food";
            }
            prescr_updatedata(datalist, datalist.get(position), position, dose,noofdays,remarks,updatevalue,"","medication_intake");

          }catch (Exception e){

          }
        }
      });
      beforefood_rb.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


          String updatevalue="";
          try {
            String dose=dose_edt.getText().toString().trim();
            String noofdays=no_days_edt.getText().toString().trim();
            String remarks=medication_remarks.getText().toString().trim();

            if(datalist.get(position).get("intake").equalsIgnoreCase("After Food,Before Food")){
              updatevalue="After Food";
            }else if(datalist.get(position).get("intake").equalsIgnoreCase("After Food")){
              updatevalue="After Food,Before Food";
            }else if(datalist.get(position).get("intake").equalsIgnoreCase("Before Food")){
              updatevalue="";
            }else{
              updatevalue="Before Food";
            }
            prescr_updatedata(datalist, datalist.get(position), position, dose,noofdays,remarks,updatevalue,"","medication_intake");

          }catch (Exception e){

          }
          //updatedata(datalist,datalist.get(position),position,updateval,"medication_intake");
        }
      });


      if(datalist.get(position).get("no_of_times").equalsIgnoreCase("")){
        morning_cb.setChecked(false);
        afternoon_cb.setChecked(false);
        evening_cb.setChecked(false);
      }else if(datalist.get(position).get("no_of_times").equalsIgnoreCase("Morning")){
        morning_cb.setChecked(true);
        afternoon_cb.setChecked(false);
        evening_cb.setChecked(false);
      }else if(datalist.get(position).get("no_of_times").equalsIgnoreCase("Afternoon")){
        morning_cb.setChecked(false);
        afternoon_cb.setChecked(true);
        evening_cb.setChecked(false);
      }else if(datalist.get(position).get("no_of_times").equalsIgnoreCase("Evening")){
        morning_cb.setChecked(false);
        afternoon_cb.setChecked(false);
        evening_cb.setChecked(true);
      }else if(datalist.get(position).get("no_of_times").equalsIgnoreCase("Morning,Afternoon")||
              datalist.get(position).get("no_of_times").equalsIgnoreCase("Afternoon,Morning")){
        morning_cb.setChecked(true);
        afternoon_cb.setChecked(true);
        evening_cb.setChecked(false);
      }else if(datalist.get(position).get("no_of_times").equalsIgnoreCase("Morning,Evening")||
              datalist.get(position).get("no_of_times").equalsIgnoreCase("Evening,Morning")){
        morning_cb.setChecked(true);
        afternoon_cb.setChecked(false);
        evening_cb.setChecked(true);
      }else if(datalist.get(position).get("no_of_times").equalsIgnoreCase("Afternoon,Evening")||
              datalist.get(position).get("no_of_times").equalsIgnoreCase("Evening,Afternoon")){
        morning_cb.setChecked(false);
        afternoon_cb.setChecked(true);
        evening_cb.setChecked(true);
      }else{
        morning_cb.setChecked(true);
        afternoon_cb.setChecked(true);
        evening_cb.setChecked(true);
      }

      morning_cb.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          try {
            String dose=dose_edt.getText().toString().trim();
            String noofdays=no_days_edt.getText().toString().trim();
            String remarks=medication_remarks.getText().toString().trim();
            prescr_updatedata(datalist, datalist.get(position), position, dose,noofdays,remarks, getupdtedvalue(morning_cb,afternoon_cb,evening_cb),"","medication_view_no_of_times");

          }catch (Exception e){

          }
         // updatedata(datalist,datalist.get(position),position,getupdtedvalue(morning_cb,afternoon_cb,evening_cb),"medication_view_no_of_times");

        }
      });
      evening_cb.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          try {
            String dose=dose_edt.getText().toString().trim();
            String noofdays=no_days_edt.getText().toString().trim();
            String remarks=medication_remarks.getText().toString().trim();
            prescr_updatedata(datalist, datalist.get(position), position, dose,noofdays,remarks, getupdtedvalue(morning_cb,afternoon_cb,evening_cb),"","medication_view_no_of_times");

          }catch (Exception e){

          }        }
      });
      afternoon_cb.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          try {
            String dose=dose_edt.getText().toString().trim();
            String noofdays=no_days_edt.getText().toString().trim();
            String remarks=medication_remarks.getText().toString().trim();

            prescr_updatedata(datalist, datalist.get(position), position, dose,noofdays,remarks, getupdtedvalue(morning_cb,afternoon_cb,evening_cb),"","medication_view_no_of_times");

          }catch (Exception e){

          }
        }
      });

      molecule_name_edt.setEnabled(false);
      sel_brands_txt.setText(datalist.get(position).get("brand_name"));
      molecule_name_edt.setText(datalist.get(position).get("molecule"));
      no_days_edt.setText(datalist.get(position).get("no_of_days"));
      dose_edt.setText(datalist.get(position).get("dose"));
      medication_remarks.setText(datalist.get(position).get("remarks"));


      p_save_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

         try {
           String dose=dose_edt.getText().toString().trim();
           String noofdays=no_days_edt.getText().toString().trim();
           String remarks=medication_remarks.getText().toString().trim();

           dose_edt.clearFocus();
           no_days_edt.clearFocus();
           medication_remarks.clearFocus();
           StoredObjects.hide_keyboard(activity);

           prescr_updatedata(datalist, datalist.get(position), position, dose,noofdays,remarks,getupdtedvalue(morning_cb,afternoon_cb,evening_cb),datalist.get(position).get("intake"), "prescr_updateval");
           StoredObjects.LogMethod("Val:::","value::"+dose+noofdays+remarks);
         }catch (Exception e){

         }
          StoredObjects.ToastMethod("Medication details data has been Saved",activity);

        }
      });

      med_del_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          DeleteConfirmationpopup(activity,datalist,position,"medication_view_remove");
        }
      });
      medication_remarks.setImeOptions(EditorInfo.IME_ACTION_DONE);
      medication_remarks.setRawInputType(InputType.TYPE_CLASS_TEXT);


    }
    else if(formtype.equalsIgnoreCase("diagnose_listitem"))
    {
      diagnose_listitem_report_date_edtx.setText(datalist.get(position).get("report_date"));
      diagnose_list_item_name_edtxt.setText(datalist.get(position).get("diagnostic_name"));
      diagnose_listitem_test_nme_edtxt.setText(datalist.get(position).get("test_name"));
      diagnose_listitem_remark_edtx.setText(datalist.get(position).get("test_remarks"));
      diagnose_listitem_remark_edtx.setImeOptions(EditorInfo.IME_ACTION_DONE);
      diagnose_listitem_remark_edtx.setRawInputType(InputType.TYPE_CLASS_TEXT);

      if(!datalist.get(position).get("report_image").equalsIgnoreCase("")){
        try {
          Glide.with(activity)
                  .load(Uri.parse(RetrofitInstance.IMAGE_URL + datalist.get(position).get("report_image")))
                  .apply(new RequestOptions()
                          .placeholder(R.drawable.no_image)
                          .fitCenter()
                          .centerCrop())
                  .into(attach_img);
        } catch (Exception e) {
          e.printStackTrace();

        }
      }



      t_save_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          try {
            String dianame=diagnose_list_item_name_edtxt.getText().toString().trim();
            String testname=diagnose_listitem_test_nme_edtxt.getText().toString().trim();
            String remarks=diagnose_listitem_remark_edtx.getText().toString().trim();

            diagnose_list_item_name_edtxt.clearFocus();
            diagnose_listitem_test_nme_edtxt.clearFocus();
            diagnose_listitem_remark_edtx.clearFocus();
            StoredObjects.hide_keyboard(activity);

            prescr_updatedata(datalist, datalist.get(position), position, dianame,testname,remarks, "","","testsuggtion");
            StoredObjects.LogMethod("Val:::","value::"+dianame+testname+remarks);
          }catch (Exception e){

          }
          StoredObjects.ToastMethod("Test details data has been Saved",activity);
        }
      });



      diagnose_listitem_report_date_edtx.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          calendar = Calendar.getInstance();
          year = calendar.get(Calendar.YEAR);
          month = calendar.get(Calendar.MONTH);
          dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
          datePickerDialog = new DatePickerDialog(activity,
                  new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                      diagnose_listitem_report_date_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
                      try {
                        String dianame=diagnose_list_item_name_edtxt.getText().toString().trim();
                        String testname=diagnose_listitem_test_nme_edtxt.getText().toString().trim();
                        String remarks=diagnose_listitem_remark_edtx.getText().toString().trim();

                        prescr_updatedata(datalist, datalist.get(position), position, dianame,testname,remarks, StoredObjects.GetSelectedDate(day,month,year),"","testsugg_date");
                        StoredObjects.LogMethod("Val:::","value::"+dianame+testname+remarks);
                      }catch (Exception e){

                      }
                      //updatedata(datalist, datalist.get(position), position, StoredObjects.GetSelectedDate(day,month,year), "testsugg_date");
                    }
                  }, year, month, dayOfMonth);
          datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
          datePickerDialog.show();
        }
      });
      browse_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (CameraUtils.checkAndRequestPermissions(activity) == true) {
            try {
              String dianame=diagnose_list_item_name_edtxt.getText().toString().trim();
              String testname=diagnose_listitem_test_nme_edtxt.getText().toString().trim();
              String remarks=diagnose_listitem_remark_edtx.getText().toString().trim();
              prescr_updatedata(datalist, datalist.get(position), position, dianame,testname,remarks, "","","testsuggtion");
            }catch (Exception e){

            }
            P_AddTestSuggestions.testpos=position;

            fragmentcalling(new TestImageUpload());
            //Imagepickingpopup(activity, datalist,position);
          }
        }
      });

      med_del_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          DeleteConfirmationpopup(activity,datalist,position,"testsugg_remove");
        }
      });
    }
    else if (formtype.equalsIgnoreCase("blockdoctor")) {

      blockeddoc_name_txt.setText(datalist.get(position).get("name"));
      blockeddoc_desig_txt .setText(datalist.get(position).get("specialization"));
      blockeddoc_address_txt.setText(datalist.get(position).get("address"));
      phonenum_txt.setText("Mobile : "+datalist.get(position).get("phone"));

      //doctor_id=

      try {
        Glide.with(activity)
                .load(Uri.parse(RetrofitInstance.BASEURL + datalist.get(position).get("image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(blocked_doc_image);
      } catch (Exception e) {
        e.printStackTrace();

      }

      unblock_doc_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          UnblockConfirmationpopup(activity,datalist,position,"");

        }
      });

    }

    if (formtype.equalsIgnoreCase("diagnose_suggest")) {

      reflabname_edtx.setText(datalist.get(position).get("referredLab"));
      reftestname_edtx.setText(datalist.get(position).get("suggestedTest"));

      ref_save_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          try {
            String testname=reftestname_edtx.getText().toString();
            String labname=reflabname_edtx.getText().toString();
            reftestname_edtx.clearFocus();
            reflabname_edtx.clearFocus();
            prescr_updatedata(datalist, datalist.get(position), position, testname,labname,"","","", "diagnose_suggest");

          }catch (Exception e){

          }

        }
      });
      ref_del_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          DeleteConfirmationpopup(activity,datalist,position,"diagnose_suggest");
        }
      });
      /*reftestname_edtx.setOnEditorActionListener(
              new EditText.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                  //Identifier of the action. This will be either the identifier you supplied,
                  //or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                  if (actionId == EditorInfo.IME_ACTION_DONE
                  ) {


                    String answer_txt=reftestname_edtx.getText().toString();

                    if(answer_txt.length()>0){


                      updatedata(datalist, datalist.get(position), position, answer_txt, "diagnose_suggest_test");

                    }else{

                      updatedata(datalist, datalist.get(position), position, "", "diagnose_suggest_test");

                    }
                    reftestname_edtx.clearFocus();

                    StoredObjects.hide_keyboard(activity);
                    return true;
                  }
                  // Return true if you have consumed the action, else false.
                  return false;
                }
              });

      reflabname_edtx.setOnEditorActionListener(
              new EditText.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                  //Identifier of the action. This will be either the identifier you supplied,
                  //or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                  if (actionId == EditorInfo.IME_ACTION_DONE
                  ) {


                    String answer_txt=reflabname_edtx.getText().toString();

                    if(answer_txt.length()>0){


                      updatedata(datalist, datalist.get(position), position, answer_txt, "diagnose_suggest_lab");

                    }else{

                      updatedata(datalist, datalist.get(position), position, "", "diagnose_suggest_lab");

                    }
                    reflabname_edtx.clearFocus();

                    StoredObjects.hide_keyboard(activity);
                    return true;
                  }
                  // Return true if you have consumed the action, else false.
                  return false;
                }
              });*/

    }
    if (formtype.equalsIgnoreCase("pat_add_physical")) {

      physug_droplay.setText(datalist.get(position).get("suggestion_name"));
      physug_droplay_.setText(datalist.get(position).get("suggestion_name"));
      phy_value_edt.setText(datalist.get(position).get("suggestion_value"));
      physug_droplay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          if(StoredObjects.physuggestionsnames_list.size()>0){
            PhyPopupupdate(physug_droplay,activity,datalist,position,"pat_add_physical");
          }else{
            StoredObjects.ToastMethod("No Physical Suggestions added",activity);
          }

        }
      });

      p_del_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          DeleteConfirmationpopup(activity,datalist,position,"p_del_add_phy");

        }
      });

      if(datalist.get(position).get("remove").equalsIgnoreCase("add")){

        p_del_img.setVisibility(View.VISIBLE);
        physug_droplay.setVisibility(View.VISIBLE);
        physug_droplay_.setVisibility(View.GONE);
      }else{

        p_del_img.setVisibility(View.GONE);
        physug_droplay.setVisibility(View.GONE);
        physug_droplay_.setVisibility(View.VISIBLE);
      }
      phy_value_edt.setImeOptions(EditorInfo.IME_ACTION_DONE);
      phy_value_edt.setRawInputType(InputType.TYPE_CLASS_TEXT);

      physug_save_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          try {
            String answer_txt=phy_value_edt.getText().toString();
            phy_value_edt.clearFocus();

            StoredObjects.hide_keyboard(activity);
            updatedata(datalist, datalist.get(position), position, answer_txt, "pat_add_physical");
          }catch (Exception e){

          }

        }
      });

      /*phy_value_edt.setOnEditorActionListener(
              new EditText.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                  //Identifier of the action. This will be either the identifier you supplied,
                  //or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                  if (actionId == EditorInfo.IME_ACTION_DONE
                  ) {


                    String answer_txt=phy_value_edt.getText().toString();

                    if(answer_txt.length()>0){


                      updatedata(datalist, datalist.get(position), position, answer_txt, "pat_add_physical");

                    }else{

                      updatedata(datalist, datalist.get(position), position, "", "pat_add_physical");

                    }
                    phy_value_edt.clearFocus();

                    StoredObjects.hide_keyboard(activity);
                    return true;
                  }
                  // Return true if you have consumed the action, else false.
                  return false;
                }
              });*/

    }
    if (formtype.equalsIgnoreCase("add_physical")) {

      physug_droplay.setText(datalist.get(position).get("suggestion_name"));
      phy_value_edt.setText(datalist.get(position).get("suggestion_value"));
      physug_droplay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          if(StoredObjects.physuggestionsnames_list.size()>0){
            PhyPopup(physug_droplay,activity,datalist,position,"add_physical");
          }else{
            StoredObjects.ToastMethod("No Physical Suggestions added by selected Doctor",activity);
          }

        }
      });

      p_del_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          DeleteConfirmationpopup(activity,datalist,position,"del_add_phy");
        }
      });
      phy_value_edt.setImeOptions(EditorInfo.IME_ACTION_DONE);
      phy_value_edt.setRawInputType(InputType.TYPE_CLASS_TEXT);


      physug_save_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          try {
            String answer_txt=phy_value_edt.getText().toString();
            phy_value_edt.clearFocus();

            StoredObjects.hide_keyboard(activity);
            updatedata(datalist, datalist.get(position), position, answer_txt, "add_physical");
          }catch (Exception e){

          }

        }
      });
      /*phy_value_edt.setOnEditorActionListener(
              new EditText.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                  //Identifier of the action. This will be either the identifier you supplied,
                  //or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                  if (actionId == EditorInfo.IME_ACTION_DONE
                  ) {


                    String answer_txt=phy_value_edt.getText().toString();

                    if(answer_txt.length()>0){


                      updatedata(datalist, datalist.get(position), position, answer_txt, "add_physical");

                    }else{

                      updatedata(datalist, datalist.get(position), position, "", "add_physical");

                    }
                    phy_value_edt.clearFocus();

                    StoredObjects.hide_keyboard(activity);
                    return true;
                  }
                  // Return true if you have consumed the action, else false.
                  return false;
                }
              });*/

    }

    else if (formtype.equalsIgnoreCase("assign_doctor")) {
      assigtndoc_txt.setText(datalist.get(position).get("name"));

      if(datalist.get(position).get("is_viewed").equalsIgnoreCase("Yes")){
        assign_doc_checkbox.setChecked(true);
      }else{
        assign_doc_checkbox.setChecked(false);
      }


      assigndoc_listlay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if(datalist.get(position).get("is_viewed").equalsIgnoreCase("Yes")){
             updatedata(datalist,datalist.get(position),position,"No","assign_doctor");
          }else{
            updatedata(datalist,datalist.get(position),position,"Yes","assign_doctor");
          }

        }
      });

    }
    else if (formtype.equalsIgnoreCase("brand_suggestions")) {

      b_brand_txt.setText(datalist.get(position).get("Brand"));

      if(datalist.get(position).get("is_viewed").equalsIgnoreCase("Yes")){
        b_checkbox.setChecked(true);
      }else{
        b_checkbox.setChecked(false);
      }


      b_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if(datalist.get(position).get("is_viewed").equalsIgnoreCase("Yes")){
            updatedata(datalist,datalist.get(position),position,"No","brand_suggestions");
          }else{
            updatedata(datalist,datalist.get(position),position,"Yes","brand_suggestions");
          }

        }
      });

    }
    else if (formtype.equalsIgnoreCase("testsuggested"))
    {
      descr_txt.setText(datalist.get(position).get("test_name"));
      test_reff_dr.setText(datalist.get(position).get("name")+" ("+datalist.get(position).get("specialization")+")");


      test_sug_date.setText(StoredObjects.convertfullTimeformat(datalist.get(position).get("created_at")));
      //test_sug_date.setText(datalist.get(position).get("report_date"));

      helplist_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            H_ViewTestSuggestPopup(activity,datalist,position);

        }
      });

    }else if (formtype.equalsIgnoreCase("othdoc_prescrip")) {

      doc_enroleid_txt.setText("Enrollee ID : "+datalist.get(position).get("enroll_id"));
      patient_name.setText("Patient Name : "+datalist.get(position).get("patient_name"));
      docname_txt.setText("Doctor Name : "+datalist.get(position).get("doctor_name"));
    //  doc_appointment_txt.setText("Appointment Date : "+datalist.get(position).get("appointment_date_time"));
      try {
        doc_appointment_txt.setText("Appointment Date : "+StoredObjects.convertfullDateTimeformat(datalist.get(position).get("appointment_date_time")));

      }catch (Exception e){
        doc_appointment_txt.setText("Appointment Date : "+datalist.get(position).get("appointment_date_time"));

      }

      doc_hstlpresr_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          // fragmentcalling(new ());

        }
      });
    }
    if (formtype.equalsIgnoreCase("tl_doctor")) {

      docname_txt.setText(datalist.get(position).get("name"));
      doc_number_txt.setText(datalist.get(position).get("phone"));
      doc_address_txt.setText(datalist.get(position).get("email"));


      try {
        Glide.with(activity)
                .load(Uri.parse(RetrofitInstance.IMAGE_URL + datalist.get(position).get("image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(doctor_img);
      } catch (Exception e) {
        e.printStackTrace();

      }

    }
    if (formtype.equalsIgnoreCase("f_patient")) {

      f_patient_name.setText(datalist.get(position).get("name"));
      f_patient_number.setText(datalist.get(position).get("phone"));
      f_patient_address.setText(datalist.get(position).get("email"));


      try {
        Glide.with(activity)
                .load(Uri.parse(RetrofitInstance.IMAGE_URL + datalist.get(position).get("image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(pat_img);
      } catch (Exception e) {
        e.printStackTrace();

      }


    }
    if (formtype.equalsIgnoreCase("tl_hospitals_one")) {

      hospitalname_txt.setText(datalist.get(position).get("name"));
      hospital_number_txt.setText(datalist.get(position).get("phone"));
      hospital_address_txt.setText(datalist.get(position).get("email"));
      try {
        Glide.with(activity)
                .load(Uri.parse(RetrofitInstance.IMAGE_URL + datalist.get(position).get("image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(hospitals_image);
      } catch (Exception e) {
        e.printStackTrace();

      }


    }
    if (formtype.equalsIgnoreCase("f_hospital_four")) {

      f_maintitle_txt.setText(datalist.get(position).get("name"));

      f_mainlist_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          StoredObjects.tab_type = datalist.get(position).get("name");
          StoredObjects.redirect_type="inner";
          fragmentcalling(new Franchisee_Details());
        }
      });
    }else if (formtype.equalsIgnoreCase("prescrption_details")) {

      prescrip_count_txt.setText((position+1)+")");
      prescrip_name_txt.setText(datalist.get(position).get("molecule_name"));
      prescrip_days_txt.setText(datalist.get(position).get("no_of_days"));
      prescrip_quantity_txt.setText(datalist.get(position).get("dosage"));
      prescrip_times_txt.setText(datalist.get(position).get("no_of_times"));
      prescrip_intake_txt.setText(datalist.get(position).get("intake"));
      pp_note_txt.setText(datalist.get(position).get("notes"));
      if(datalist.get(position).get("notes").equalsIgnoreCase("")){
        prescrip_note_lay.setVisibility(View.GONE);
      }else{
        prescrip_note_lay.setVisibility(View.VISIBLE);
      }
    }else if (formtype.equalsIgnoreCase("diagnose_recyler")) {

      diagnose_sug_count_txt.setText((position+1)+") ");
      diagnose_sug_test_txt.setText(datalist.get(position).get("test_name"));

    }
    else if (formtype.equalsIgnoreCase("physical_examination")) {

      fradetls_name_txt.setText(datalist.get(position).get("suggestion"));
      tabcount_txt.setText(datalist.get(position).get("description"));

    }else if (formtype.equalsIgnoreCase("doc_testsuggested")) {
      doc_enrole_txt.setText(datalist.get(position).get("enroll_id"));
      doc_testname_txt.setText(datalist.get(position).get("test_name"));
      doc_reffer_txt.setText(datalist.get(position).get("referred_lab"));

      try {
        doc_date_txt.setText(StoredObjects.convertfullTimeformat(datalist.get(position).get("created_at")));

      }catch (Exception e){
        doc_date_txt.setText(datalist.get(position).get("created_at"));

      }

      doc_testsuggested_listlay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          TotalTestSuggestPopup(activity,datalist,position);

        }
      });


    }else if (formtype.equalsIgnoreCase("f_hospital_five")) {

      f_del_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
         DeleteConfirmationpopup(activity,datalist,position,"f_hospital_five");
        }
      });

      f_edit_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          F_Edit_Lead.data_list.clear();
          F_Edit_Lead.data_list.add(datalist.get(position));
          fragmentcalling(new F_Edit_Lead());
        }
      });

      if(datalist.get(position).get("status").equalsIgnoreCase("Interested")){
        lead_view.setBackgroundColor(activity.getResources().getColor(R.color.theme_color));
      }else   if(datalist.get(position).get("status").equalsIgnoreCase("Next Appointment")){
        lead_view.setBackgroundColor(activity.getResources().getColor(R.color.yellow));
      }else   if(datalist.get(position).get("status").equalsIgnoreCase("Not Interested")){
        lead_view.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
      }else{
        lead_view.setBackgroundColor(activity.getResources().getColor(R.color.red));
      }
      f_hospital_txt.setText(datalist.get(position).get("name"));
      number_txt.setText(datalist.get(position).get("phone"));
      f_address_txt.setText(datalist.get(position).get("address"));
      f_app_date_txt.setText(StoredObjects.convertDateformat(datalist.get(position).get("appointment_date")));


      docdetails_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (StoredObjects.tab_type.equalsIgnoreCase("Hospital")) {

            HospitalLeadpopup(activity,datalist,position);
          } else if (StoredObjects.tab_type.equalsIgnoreCase("Doctor")) {

            DoctorLeadpopup(activity,datalist,position);
          } else {

            PatientLeadpopup(activity,datalist,position);
          }
        }
      });

    }
  else if (formtype.equalsIgnoreCase("tl_doctors")) {

      hospital_txt.setText(datalist.get(position).get("name"));
      doc_contact_txt.setText(datalist.get(position).get("phone"));
      tl_doc_address_txt.setText(datalist.get(position).get("address"));

      try {
        Glide.with(activity)
                .load(Uri.parse(RetrofitInstance.IMAGE_URL + datalist.get(position).get("image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(hospital_image);
      } catch (Exception e) {
        e.printStackTrace();

      }
      doctor_one_listlay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          viewDoctorPopup(activity,datalist,position);
        }
      });

      tl_doc_edit_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          TL_Edit_Doctor.editarraylist.clear();
          TL_Edit_Doctor.editarraylist.add(datalist.get(position));
          fragmentcalling(new TL_Edit_Doctor());
        }
      });



    } else if (formtype.equalsIgnoreCase("tl_franchise_one")) {

      fr_title_txt.setText(datalist.get(position).get("name"));

      frdetails_list_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (position == 0) {
            fragmentcalling(new Franchisee_Hospitals());
          } else if (position == 1) {
            fragmentcalling(new Franchisee_Doctors());
          } else if (position == 2) {
            fragmentcalling(new Franchisee_Patients());
          } else {
            fragmentcalling(new Franchisee_SubFranchises());
          }
        }
      });
    } else if (formtype.equalsIgnoreCase("tl_franchise")) {

      fradetls_name_txt.setText(datalist.get(position).get("name"));
      tabcount_txt.setText(datalist.get(position).get("tabcount"));
    }else if (formtype.equalsIgnoreCase("h_assistant")) {

      h_assistant_name_txt.setText(datalist.get(position).get("name"));
      h_assistant_timings_txt.setText("Mobile : "+ datalist.get(position).get("phone"));

     // h_assistant_timings_txt.setText(StoredObjects.time12hrsformat( datalist.get(position).get("from_time"))+" - "+StoredObjects.time12hrsformat(datalist.get(position).get("to_time")));

      h_assistant_docassign_txt.setText("No of Doctors Assigned : "+ datalist.get(position).get("assigned_doctors_count"));

      h_assistant_del_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          DeleteConfirmationpopup(activity,datalist,position,"h_assistant");
        }
      });

      h_assistant_edit_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          H_EditAssistant.editarraylist.clear();
          H_EditAssistant.editarraylist.add(datalist.get(position));
          fragmentcalling(new H_EditAssistant());
        }
      });

    } else if (formtype.equalsIgnoreCase("tl_hospitals")) {

      tl_hospital_txt.setText(datalist.get(position).get("name"));
      tl_contact_txt.setText(datalist.get(position).get("phone"));
      tl_address_txt.setText(datalist.get(position).get("address"));

      tl_del_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


          DeleteConfirmationpopup(activity,datalist,position,"tl_hospitals");
        }
      });

      tl_edit_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          TL_Edit_Hospital.editarraylist.clear();
          TL_Edit_Hospital.editarraylist.add(datalist.get(position));
          fragmentcalling(new TL_Edit_Hospital());
        }
      });

     try {
        Glide.with(activity)
                .load(Uri.parse(RetrofitInstance.IMAGE_URL + datalist.get(position).get("image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(tl_hospital_image);
      } catch (Exception e) {
        e.printStackTrace();

      }

      tl_hsptl_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          viewHospitalPopup(activity,datalist,position);
        }
      });

    } else if (formtype.equalsIgnoreCase("tl_franchises_one")) {

      franchasename_txt.setText(datalist.get(position).get("name"));
      franchasenumber_txt.setText(datalist.get(position).get("phone"));
      franchaseaddress_txt.setText(datalist.get(position).get("email"));
      tl_franshice_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          StoredObjects.F_UserId="";
          StoredObjects.F_RoleUserId="";

          StoredObjects.F_UserId=datalist.get(position).get("user_id");
          StoredObjects.F_RoleUserId=datalist.get(position).get("role_id");
          StoredObjects.franshise_list.clear();
          StoredObjects.franshise_list.add(datalist.get(position));

          TL_Franchises_Details.gridtabs_list.clear();
          fragmentcalling(new TL_Franchises_Details());
        }
      });



      try {
        Glide.with(activity)
                .load(Uri.parse(RetrofitInstance.IMAGE_URL + datalist.get(position).get("image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(franchase_image);
      } catch (Exception e) {
        e.printStackTrace();

      }

      franchaseedit_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Edit_FranchiseeList.data_list.clear();
          Edit_FranchiseeList.data_list.add(datalist.get(position));
          fragmentcalling(new Edit_FranchiseeList());
        }
      });

      franchasedel_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          DeleteConfirmationpopup(activity,datalist,position,"tl_franchises_one");
        }
      });

    } else if (formtype.equalsIgnoreCase("marketing_exicutiveone")) {


      franshicelist_name_txt.setText(datalist.get(position).get("name"));
      franshicelist_number_txt.setText(datalist.get(position).get("phone"));
      franshicelist_address_txt.setText(datalist.get(position).get("email"));


      try {
        Glide.with(activity)
                .load(Uri.parse(RetrofitInstance.IMAGE_URL + datalist.get(position).get("image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(franshicelist_image);
      } catch (Exception e) {
        e.printStackTrace();

      }
      franshicelist_edit_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Edit_FranchiseeList.data_list.clear();
          Edit_FranchiseeList.data_list.add(datalist.get(position));
          fragmentcalling(new Edit_FranchiseeList());
        }
      });

      franshicelist_del_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          DeleteConfirmationpopup(activity,datalist,position,"marketing_exicutiveone");
        }
      });

      franshicelist_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          StoredObjects.F_UserId="";
          StoredObjects.F_RoleUserId="";

          StoredObjects.F_UserId=datalist.get(position).get("user_id");
          StoredObjects.F_RoleUserId=datalist.get(position).get("role_id");
          StoredObjects.franshise_list.clear();
          StoredObjects.franshise_list.add(datalist.get(position));
          TL_Franchises_Details.gridtabs_list.clear();
          fragmentcalling(new TL_Franchises_Details());
        }
      });

    }

    else if (formtype.equalsIgnoreCase("marketing_exicutive")) {
      mar_franchisename_txt.setText(datalist.get(position).get("name"));
      mar_franchisenumber_txt.setText(datalist.get(position).get("phone"));
      mar_franchise_address_txt.setText(datalist.get(position).get("email"));
      try {
        Glide.with(activity)
                .load(Uri.parse(RetrofitInstance.IMAGE_URL + datalist.get(position).get("image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(mar_franchise_img);
      } catch (Exception e) {
        e.printStackTrace();

      }

      mar_franchise_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          StoredObjects.M_UserId="";
          StoredObjects.M_RoleUserId="";

          StoredObjects.M_UserId=datalist.get(position).get("user_id");
          StoredObjects.M_RoleUserId=datalist.get(position).get("role_id");
          fragmentcalling(new FranchiseeList_Main());
        }
      });

      mar_franchise_edit_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Edit_Marketing_Executive.data_list.clear();
          Edit_Marketing_Executive.data_list.add(datalist.get(position));
          fragmentcalling(new Edit_Marketing_Executive());
        }
      });

      mar_franchise_del_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          DeleteConfirmationpopup(activity,datalist,position,"marketing_exicutive");
        }
      });

    }  else if (formtype.equalsIgnoreCase("subuserdoc_prescr")) {

       patotherdoc_name_txt.setText(datalist.get(position).get("doctor_name"));
      try {
        patotherdoc_date_txt.setText(StoredObjects.convertfullDateTimeformat(datalist.get(position).get("appointment_date_time")));

      }catch (Exception e){
        patotherdoc_date_txt.setText(datalist.get(position).get("appointment_date_time"));

      }
      patotherdoc_problem_txt.setText(datalist.get(position).get("problem"));


      patotherdoc_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


          StoredObjects.Prescription_Id=datalist.get(position).get("prescription_id");
          fragmentcalling(new H_PatientPrescrDetails());
        }
      });

    } else if (formtype.equalsIgnoreCase("ass_patientlist")) {
      ass_maintitle_txt .setText(datalist.get(position).get(""));
      if (datalist.get(position).get("is_viewed").equals("Yes")) {
        ass_main_innerlist.setVisibility(View.VISIBLE);
        ass_expand_img.setVisibility(View.GONE);
        ass_collapse_img.setVisibility(View.VISIBLE);
      } else {
        ass_main_innerlist.setVisibility(View.GONE);
        ass_expand_img.setVisibility(View.VISIBLE);
        ass_collapse_img.setVisibility(View.GONE);
      }

      StoredObjects.subhashmaplist(3);
      final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
      ass_main_innerlist.setLayoutManager(linearLayoutManager);

      ass_main_innerlist.setAdapter(new HashMapRecycleviewadapter(activity, StoredObjects.subdummy_list, "ass_inner_patientlist", ass_main_innerlist, R.layout.ass_patientsub_listitem));

      ass_mainlist_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (datalist.get(position).get("is_viewed").equals("Yes")) {
            updatedata(datalist, datalist.get(position), position, "No", "ass_patientlist");
          } else {
            updatedata(datalist, datalist.get(position), position, "Yes", "ass_patientlist");
          }
        }
      });
    } else if (formtype.equalsIgnoreCase("ass_inner_patientlist")) {


      as_pat_aptid.setText("Appointment ID : "+datalist.get(position).get("enroll_id"));
      as_pat_name.setText(datalist.get(position).get("patient_name"));
      as_pat_mobile.setText("Problem : "+datalist.get(position).get("problem"));
      try {
        as_pat_apttime.setText("Appointment Date : "+StoredObjects.convertfullDateTimeformat(datalist.get(position).get("appointment_date_time")));

      }catch (Exception e){
        as_pat_apttime.setText("Appointment Date : "+datalist.get(position).get("appointment_date_time"));

      }



    } else if (formtype.equalsIgnoreCase("subuser_prescr")) {

      patownpre_docname_txt.setText(datalist.get(position).get("doctor_name"));
      try {
        patownpre_date_txt.setText(StoredObjects.convertfullDateTimeformat(datalist.get(position).get("appointment_date_time")));

      }catch (Exception e){
        patownpre_date_txt.setText(datalist.get(position).get("appointment_date_time"));

      }


      patownpre_problem_txt.setText(datalist.get(position).get("problem"));


      patownpre_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          StoredObjects.Prescription_Id=datalist.get(position).get("prescription_id");
          fragmentcalling(new Patient_Added_Prescriptions());
        }
      });

    } else if (formtype.equalsIgnoreCase("p_submember")) {

      p_submember_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          Members_Details.data_list.clear();
          Members_Details.data_list.add(datalist.get(position));
          fragmentcalling(new Members_Details());
        }
      });

      submem_relation_txt.setText("Relation : "+datalist.get(position).get("relation"));
      submem_name_txt.setText(datalist.get(position).get("name"));

      String[] currentdate=datalist.get(position).get("created_at").split(" ");
      try {
        submem_date_txt.setText(StoredObjects.convertDateformat(currentdate[0]));
      }catch (Exception e){

      }



      submem_del_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          DeleteConfirmationpopup(activity,datalist,position,"p_submember");

        }
      });


      submem_edit_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          P_Edit_Member.editarraylist.clear();
          P_Edit_Member.editarraylist.add(datalist.get(position));
          fragmentcalling(new P_Edit_Member());
        }
      });

    }else if (formtype.equalsIgnoreCase("diagnose_report")) {
      try {
        p_diareports_date_txt.setText(" "+StoredObjects.convertDateformat(datalist.get(position).get("report_date")));

      }catch (Exception e){
        p_diareports_date_txt.setText(" "+StoredObjects.convertDateformat(datalist.get(position).get("report_date")));

      }
     // p_diareports_date_txt.setText(" " +datalist.get(position).get("created_at"));
      p_diareports_dname_txt.setText(" " +datalist.get(position).get("diagnostic_name"));
      p_diareports_refdoc_txt.setText(" " +datalist.get(position).get("ref_doctor_name"));

      p_diareports_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Diagnosis_Reports_Details.data_list.clear();
          Diagnosis_Reports_Details.data_list.add(datalist.get(position));
          fragmentcalling(new Diagnosis_Reports_Details());
        }
      });

      p_diareports_edit_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Edit_Diagnosis.data_list.clear();
          Edit_Diagnosis.data_list.add(datalist.get(position));
          fragmentcalling(new Edit_Diagnosis());
        }
      });

    } else if (formtype.equalsIgnoreCase("patprescr_doc_list")) {

      patotherdoc_name_txt.setText(datalist.get(position).get("doctor_name"));
      patotherdoc_problem_txt.setText(datalist.get(position).get("problem"));
      enrollid_txt.setText(datalist.get(position).get("enroll_id"));
      enrolledlay.setVisibility(View.VISIBLE);
      try {
        patotherdoc_date_txt.setText(StoredObjects.convertfullDateTimeformat(datalist.get(position).get("appointment_date_time")));

      }catch (Exception e){
        patotherdoc_date_txt.setText(datalist.get(position).get("appointment_date_time"));

      }

      patotherdoc_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          StoredObjects.Prescription_Id=datalist.get(position).get("prescription_id");
          fragmentcalling(new H_PatientPrescrDetails());
        }
      });

    } else if (formtype.equalsIgnoreCase("patpresr_list")) {

      patownpre_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          fragmentcalling(new Patient_Added_Prescriptions());
        }
      });

    } else if (formtype.equalsIgnoreCase("d_ttlpreslist")) {
      prescrip_enrole_txt.setText("Enrollee ID : "+datalist.get(position).get("enroll_id"));
      prescrip_pname_txt.setText("Patient Name : "+datalist.get(position).get("patient_name"));
      try {
        prescrip_datetime_txt.setText("Appointment Date : "+StoredObjects.convertfullDateTimeformat(datalist.get(position).get("appointment_date_time")));

      }catch (Exception e){
        prescrip_datetime_txt.setText("Appointment Date : "+datalist.get(position).get("appointment_date_time"));

      }

      // prescrip_datetime_txt.setText("Appointment Date & Time : "+datalist.get(position).get("appointment_date_time"));

      dpre_ttlpres_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          StoredObjects.Prescription_Id=datalist.get(position).get("prescription_id");
          fragmentcalling(new H_PatientPrescrDetails());
        }
      });

    } else if (formtype.equalsIgnoreCase("doc_patient_details")) {
      patlisttitle_txt.setText(datalist.get(position).get("name"));

      addlistitem_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (position == 0) {
            physicalexampopup(activity);
          } else if (position == 1) {

            assessmentpopup(activity);
          }/* else if (position == 2) {
            fragmentcalling(new Doc_AddMedication());
           // medicationpopup(activity);
          }*/ else {
            Diagnosepopup(activity);
          }
        }
      });

    } else if (formtype.equalsIgnoreCase("patientlist_history")) {

      pat_weight_txt.setText(datalist.get(position).get("suggestion"));


      d_prf_up_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

         physicalexampopupOrg(activity,datalist,position);
        }
      });

      d_prf_down_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          DeleteConfirmationpopup(activity,datalist,position,"patientlist_history");
        }
      });

    }
    else if (formtype.equalsIgnoreCase("d_patnt_que")) {

      patientlist_name_txt.setText(datalist.get(position).get("patient_name"));
      //patientlist_apptime_txt.setText(datalist.get(position).get("appointment_date_time"));

      try {
        patientlist_apptime_txt.setText(StoredObjects.convertfullDateTimeformat(datalist.get(position).get("appointment_date_time")));

      }catch (Exception e){
        patientlist_apptime_txt.setText(datalist.get(position).get("appointment_date_time"));

      }

      h_patnttmngs_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          StoredObjects.Prescription_Id=datalist.get(position).get("prescription_id");
          StoredObjects.menuitems_list.clear();
          Doc_Patient_Details.update_data.clear();
          HashMap<String,String> hashMap=new HashMap<>();
          hashMap.put("problem","");
          hashMap.put("physical_examinations","");
          hashMap.put("assessment_plan","");
          hashMap.put("patient_advice","");
          hashMap.put("doctors_note","");
          hashMap.put("medication","");
          hashMap.put("tests_suggested","");
          hashMap.put("selected_brands","");
          hashMap.put("selected_molecules","");
          Doc_Patient_Details.update_data.add(hashMap);
          Doc_AddMedication.medications_list.clear();
          Doc_Patient_Details. physuggestionslist.clear();
          Doc_Patient_Details.testadd_list.clear();
          StoredObjects.first_time="yes";

          fragmentcalling(new Doc_Patient_Details());
        }
      });

    } else if (formtype.equalsIgnoreCase("d_patent_chcked")) {

      patientlist_name_txt.setText(datalist.get(position).get("patient_name"));
     // patientlist_apptime_txt.setText(datalist.get(position).get("appointment_date_time"));

      try {
        patientlist_apptime_txt.setText(StoredObjects.convertfullDateTimeformat(datalist.get(position).get("appointment_date_time")));

      }catch (Exception e){
        patientlist_apptime_txt.setText(datalist.get(position).get("appointment_date_time"));

      }
      h_patnttmngs_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          if(datalist.get(position).get("status").equalsIgnoreCase("In Que")){
            StoredObjects.Prescription_Id=datalist.get(position).get("prescription_id");
            StoredObjects.menuitems_list.clear();
            Doc_Patient_Details.update_data.clear();
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("problem","");
            hashMap.put("physical_examinations","");
            hashMap.put("assessment_plan","");
            hashMap.put("patient_advice","");
            hashMap.put("doctors_note","");
            hashMap.put("medication","");
            hashMap.put("tests_suggested","");
            hashMap.put("selected_brands","");
            hashMap.put("selected_molecules","");
            Doc_Patient_Details.update_data.add(hashMap);
            Doc_AddMedication.medications_list.clear();
            Doc_Patient_Details. physuggestionslist.clear();
            Doc_Patient_Details.testadd_list.clear();
            StoredObjects.first_time="yes";
            fragmentcalling(new Doc_Patient_Details());
          }else{
            StoredObjects.Prescription_Id=datalist.get(position).get("prescription_id");
            fragmentcalling(new H_PatientPrescrDetails());
          }

        }
      });

    } else if (formtype.equalsIgnoreCase("h_docsinnerlist")) {


      docname_txt.setText(datalist.get(position).get("name"));
      doctime_txt.setText(StoredObjects.time12hrsformat( datalist.get(position).get("from_time"))+" - "+StoredObjects.time12hrsformat(datalist.get(position).get("to_time")));
      docattender_txt.setText("Attender : "+datalist.get(position).get("assistant_name"));

      try {
        Glide.with(activity)
                .load(Uri.parse(RetrofitInstance.BASEURL + datalist.get(position).get("image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(h_docimg);
      } catch (Exception e) {
        e.printStackTrace();

      }
      docdetails_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Doctor_Details.details_list.clear();
          Doctor_Details.details_list.add(datalist.get(position));
          StoredObjects.H_DOC_ID=datalist.get(position).get("user_id");
          fragmentcalling(new Doctor_Details());
        }
      });


     /* docdetails_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          ViewDoctorpopup(activity,datalist,position);
        }
      });*/
      f_del_img.setVisibility(View.GONE);
      f_edit_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          H_EditDoctor.data_list.clear();
          H_EditDoctor.data_list.add(datalist.get(position));

          fragmentcalling(new H_EditDoctor());
        }
      });

    } else if (formtype.equalsIgnoreCase("d_doctorslist")) {
      doc_patientList_name.setText(datalist.get(position).get("clinic_name"));

      if (position % 2 == 0) {
        p_mainlist_lay.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.simple_button_bg));

      } else {
        p_mainlist_lay.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.blue_button_bg));
      }
      p_mainlist_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          StoredObjects.Doc_Hospital_Id=datalist.get(position).get("hospital_id");
          if (StoredObjects.tab_type.equalsIgnoreCase("patients")) {
            fragmentcalling(new Doc_Patient_List());
          } else if (StoredObjects.tab_type.equalsIgnoreCase("prescr")) {
            fragmentcalling(new DocTotalPrescriptionsList());
          } else if (StoredObjects.tab_type.equalsIgnoreCase("testsugsted")) {
            fragmentcalling(new Doc_TestSuggested());
          }else if (StoredObjects.tab_type.equalsIgnoreCase("appointments")) {
            fragmentcalling(new Doc_Appointmentslist());
          }



        }
      });
    } /*else if (formtype.equalsIgnoreCase("f_hospital_four")) {
      if (datalist.get(position).get("is_viewed").equals("Yes")) {
        add_hospital_lead.setVisibility(View.VISIBLE);
        f_expand_img.setVisibility(View.GONE);
        f_collapse_img.setVisibility(View.VISIBLE);
      } else {
        add_hospital_lead.setVisibility(View.GONE);
        f_expand_img.setVisibility(View.VISIBLE);
        f_collapse_img.setVisibility(View.GONE);
      }

      f_mainlist_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (datalist.get(position).get("is_viewed").equals("Yes")) {
            updatedata(datalist, datalist.get(position), position, "No", "f_hospital_four");
          } else {
            updatedata(datalist, datalist.get(position), position, "Yes", "f_hospital_four");
          }
        }
      });
    }*/  else if (formtype.equalsIgnoreCase("h_presclist")) {
      h_maintitle_txt.setText(datalist.get(position).get("name"));
      h_prescSub_enrollID.setText("Enrollee ID : "+datalist.get(position).get("enroll_id"));
      h_prescSub_consulted_dr.setText(datalist.get(position).get("name"));

    } else if (formtype.equalsIgnoreCase("h_presinnerlist")) {


      h_prescSub_enrollID.setText("Enrollee ID : "+datalist.get(position).get("enroll_id"));
      h_prescSub_consulted_dr.setText(datalist.get(position).get("name"));

      try {
        h_pres_apt_date_time.setText("Appointment Date : "+StoredObjects.convertfullDateTimeformat(datalist.get(position).get("appointment_date_time")));

      }catch (Exception e){
        h_pres_apt_date_time.setText("Appointment Date : "+datalist.get(position).get("appointment_date_time"));

      }

      h_ttlpres_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          StoredObjects.tab_type = "presr";
          StoredObjects.Prescription_Id=datalist.get(position).get("prescription_id");
          fragmentcalling(new H_PatientPrescrDetails());
        }
      });
    } else if (formtype.equalsIgnoreCase("d_assistant")) {
      //numofass_lay.setVisibility(View.INVISIBLE);

      h_assistant_docassign_txt.setText(datalist.get(position).get("phone"));
      h_assistant_name_txt.setText(datalist.get(position).get("name"));
      h_assistant_timings_txt.setText(StoredObjects.time12hrsformat( datalist.get(position).get("from_time"))+" - "+StoredObjects.time12hrsformat(datalist.get(position).get("to_time")));

      h_assistant_edit_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Doc_EditAssistant.data_list.clear();
          Doc_EditAssistant.data_list.add(datalist.get(position));
          fragmentcalling(new Doc_EditAssistant());
        }
      });

      h_assistant_del_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


          DeleteConfirmationpopup(activity,datalist,position,"d_assistant");

        }
      });

    }else if (formtype.equalsIgnoreCase("h_patientlist_main")) {
      if (datalist.get(position).get("is_viewed").equals("Yes")) {
        main_innerlist.setVisibility(View.VISIBLE);
        expand_img.setVisibility(View.GONE);
        collapse_img.setVisibility(View.VISIBLE);
      } else {
        main_innerlist.setVisibility(View.GONE);
        expand_img.setVisibility(View.VISIBLE);
        collapse_img.setVisibility(View.GONE);
      }

      StoredObjects.subhashmaplist(3);
      final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
      main_innerlist.setLayoutManager(linearLayoutManager);

      main_innerlist.setAdapter(new HashMapRecycleviewadapter(activity, StoredObjects.subdummy_list, "h_patientinnerlist", main_innerlist, R.layout.h_patsub_listitem));

      mainlist_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (datalist.get(position).get("is_viewed").equals("Yes")) {
            updatedata(datalist, datalist.get(position), position, "No", "h_patientlist_main");
          } else {
            updatedata(datalist, datalist.get(position), position, "Yes", "h_patientlist_main");
          }
        }
      });
    } else if (formtype.equalsIgnoreCase("h_patientinnerlist")) {

      docname_txt.setText(datalist.get(position).get("doctor_name"));
      doc_problem_txt.setText("Problem : "+datalist.get(position).get("problem"));
      p_enroll_txt.setText("Enrollee ID : "+datalist.get(position).get("enroll_id"));
      patient_name.setText("Patient Name : "+datalist.get(position).get("patient_name"));


      status_txt.setText(datalist.get(position).get("status"));
     // doc_appointment_txt.setText("Appointment Time : "+datalist.get(position).get("appointment_date_time"));

      try {
        doc_appointment_txt.setText("Appointment Date : "+StoredObjects.convertfullDateTimeformat(datalist.get(position).get("appointment_date_time")));

      }catch (Exception e){
        doc_appointment_txt.setText("Appointment Date : "+datalist.get(position).get("appointment_date_time"));

      }

     /* h_patmain_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          StoredObjects.Prescription_Id=datalist.get(position).get("prescription_id");
          fragmentcalling(new H_PatientPrescrDetails());
        }
      });*/

    }  else if (formtype.equalsIgnoreCase("doc_details")) {
      docdetails_txt.setText(datalist.get(position).get("name"));

      h_docmain_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          if (position == 0) {
            fragmentcalling(new H_PatentList());
          } else if (position == 1) {
            StoredObjects.redirect_type="doctor";
            fragmentcalling(new H_TotalPrescriptions());
          } else {
            StoredObjects.redirect_type="doctor";
            fragmentcalling(new H_Test_Suggested());
          }
        }
      });

    } else if (formtype.equalsIgnoreCase("medication")) {
      if (position == 1) {
        red_circle.setVisibility(View.VISIBLE);
        yellow_circle.setVisibility(View.VISIBLE);
      } else {
        red_circle.setVisibility(View.GONE);
        yellow_circle.setVisibility(View.GONE);
      }
    } else if (formtype.equalsIgnoreCase("addapointment")) {

      ad_patntnme_txt.setText(datalist.get(position).get("name")+" ("+
              datalist.get(position).get("specialization")+")");

      if (datalist.get(position).get("is_viewed").equalsIgnoreCase("Yes")) {
        appointment_innerlay.setVisibility(View.VISIBLE);
        ad_doc_up_img.setVisibility(View.GONE);
        ad_doc_down_img.setVisibility(View.VISIBLE);
      } else {
        appointment_innerlay.setVisibility(View.GONE);
        ad_doc_up_img.setVisibility(View.VISIBLE);
        ad_doc_down_img.setVisibility(View.GONE);

      }

      add_appointment_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          if (datalist.get(position).get("is_viewed").equalsIgnoreCase("No")) {
            updatedata(datalist, datalist.get(position), position, "Yes", "addapointment");

          } else {
            updatedata(datalist, datalist.get(position), position, "No", "addapointment");

          }

        }
      });



      pat_search_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {



          String  mobile_str = search_edtxt.getText().toString().trim();


          if (mobile_str.equals("")){
            StoredObjects.ToastMethod("Please enter Mobile Number",activity);
          }else {

            String doctor_id=datalist.get(position).get("doctor_id");
            StoredObjects.Pat_DocID=doctor_id;
            if (InterNetChecker.isNetworkAvailable(activity)) {

              StoredObjects.physuggestions_list.clear();
              try {
                StoredObjects.physuggestions_list=JsonParsing.GetJsonData(datalist.get(position).get("physical_suggestions"));
              }catch (JSONException e){

              }
              StoredObjects.physuggestionsnames_list.clear();
              for(int k=0;k<StoredObjects.physuggestions_list.size();k++){
                StoredObjects.physuggestionsnames_list.add(StoredObjects.physuggestions_list.get(k).get("suggestion"));
              }

              SearchPatientService(activity,mobile_str,doctor_id);

            }else {
              StoredObjects.ToastMethod("Please Check Internet",activity);
            }
          }

        }
      });
    } else if (formtype.equalsIgnoreCase("p_tst_sugstn")) {


      try {
        p_tst_date_txt.setText("Date : "+StoredObjects.convertfullDateTimeformat(datalist.get(position).get("created_at")));

      }catch (Exception e){
        p_tst_date_txt.setText("Date : "+datalist.get(position).get("created_at"));

      }
     // p_tst_date_txt.setText("Date : "+datalist.get(position).get("created_at"));
      p_tst_diastc_txt.setText("Diagnostic Name : "+datalist.get(position).get("referred_lab"));
      p_tst_rfdctr_txt.setText("Ref Doctor Name : "+datalist.get(position).get("ref_doctor_name"));
      p_tst_ultrasund_txt.setText(datalist.get(position).get("test_name"));
      p_tst_tft_txt.setVisibility(View.GONE);
      p_tst_cbp_txt.setVisibility(View.GONE);

     /* testsuggested_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          ..
          Diagnosereportpopup(activity,datalist,position);

        }
      });*/

      testsuggested_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Diagnosis_Reports_Details.data_list.clear();
          Diagnosis_Reports_Details.data_list.add(datalist.get(position));
          fragmentcalling(new Diagnosis_Reports_Details());
        }
      });

      p_tst_delete_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Edit_TestSuggestions.data_list.clear();
          Edit_TestSuggestions.data_list.add(datalist.get(position));
          fragmentcalling(new Edit_TestSuggestions());
        }
      });
    } else if (formtype.equalsIgnoreCase("h_patentlst")) {

      patientlist_name_txt.setText(datalist.get(position).get("patient_name"));
      patientlist_dr_name_txt.setVisibility(View.GONE);
      patientlist_dr_name_txt.setText(datalist.get(position).get("doctor_name"));
      //patientlist_apptime_txt.setText(datalist.get(position).get("appointment_date_time"));
      try {
        patientlist_apptime_txt.setText("Appointment Date : "+StoredObjects.convertfullDateTimeformat(datalist.get(position).get("appointment_date_time")));

      }catch (Exception e){
        patientlist_apptime_txt.setText("Appointment Date : "+datalist.get(position).get("appointment_date_time"));

      }
      h_patnttmngs_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          StoredObjects.Prescription_Id=datalist.get(position).get("prescription_id");
          fragmentcalling(new H_PatientPrescrDetails());
          //fragmentcalling(new H_Patient_Details());

        }
      });
    } else if (formtype.equalsIgnoreCase("p_prescription")) {
      doc_enroleid_txt.setText(datalist.get(position).get("enroll_id"));
      doc_problem_txt.setText(datalist.get(position).get("problem"));

      try {
        doc_appointment_txt.setText(StoredObjects.convertfullDateTimeformat(datalist.get(position).get("appointment_date_time")));

      }catch (Exception e){
        doc_appointment_txt.setText(datalist.get(position).get("appointment_date_time"));

      }
      doc_hstlpresr_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


          StoredObjects.Prescription_Id=datalist.get(position).get("prescription_id");
          fragmentcalling(new H_PatientPrescrDetails());

        }
      });

    }
    else if (formtype.equalsIgnoreCase("p_doc_prescription")) {
      h_prescr_docname_txt.setText(datalist.get(position).get("patient_name"));
      h_prescr_problem_txt.setText(datalist.get(position).get("problem"));
     // h_prescr_appdatetime_txt.setText(datalist.get(position).get("appointment_date_time"));

      try {
        h_prescr_appdatetime_txt.setText(StoredObjects.convertfullDateTimeformat(datalist.get(position).get("appointment_date_time")));

      }catch (Exception e){
        h_prescr_appdatetime_txt.setText(datalist.get(position).get("appointment_date_time"));

      }
      h_docpres_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          StoredObjects.Prescription_Id=datalist.get(position).get("prescription_id");
          fragmentcalling(new H_PatientPrescrDetails());

        }
      });

    }

    else if (formtype.equalsIgnoreCase("doc_prfle")) {

      //[{"id":10,"hospital_id":19,"from_days":"2","to_days":"4","custom_timings":"","from_time":"01:30:00","to_time":"03:30:00","clinic_name":"hosp_name"}]

      if(datalist.get(position).get("hospital_id").equalsIgnoreCase("0")){
        d_prf_dctrnme_txt.setText(datalist.get(position).get("clinic_name")+" (Own Clinic)");
      }else{
        d_prf_dctrnme_txt.setText(datalist.get(position).get("clinic_name"));
      }

      d_frmday_edtx.setText(datalist.get(position).get("from_days"));
      d_today_edtx.setText(datalist.get(position).get("to_days"));
      /*if(datalist.get(position).get("to_days").equalsIgnoreCase("0")){
        d_today_edtx.setText("");
      }else{
        for(int k=0;k<TL_AddDoctor.days_list.size();k++){

          if(datalist.get(position).get("to_days").equalsIgnoreCase(TL_AddDoctor.days_list.get(k).get("id") )){

            d_today_edtx.setText(TL_AddDoctor.days_list.get(k).get("day_name"));
          }
        }
      }

      for(int k=0;k<TL_AddDoctor.days_list.size();k++){

        if((datalist.get(position).get("from_days").equalsIgnoreCase(TL_AddDoctor.days_list.get(k).get("day_name")))||
                (datalist.get(position).get("from_days").equalsIgnoreCase(TL_AddDoctor.days_list.get(k).get("id")))  ){
          d_frmday_edtx.setText(TL_AddDoctor.days_list.get(k).get("day_name"));
        }
      }*/



      h_custm_edtx.setOnEditorActionListener(
              new EditText.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                  //Identifier of the action. This will be either the identifier you supplied,
                  //or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                  if (actionId == EditorInfo.IME_ACTION_DONE
                  ) {


                    String answer_txt=h_custm_edtx.getText().toString();

                    if(answer_txt.length()>0){

                      d_timeslot_updatedata(datalist,datalist.get(position),position,answer_txt,"","custom");

                     // profile_updatedata(datalist, datalist.get(position), position, answer_txt, "custom");

                    }else{
                      d_timeslot_updatedata(datalist,datalist.get(position),position,"","","custom");
                    //  profile_updatedata(datalist, datalist.get(position), position, "", "custom");

                    }
                    h_custm_edtx.clearFocus();

                    StoredObjects.hide_keyboard(activity);
                    return true;
                  }
                  // Return true if you have consumed the action, else false.
                  return false;
                }
              });
      //d_sgnup_today_edtx.setText(datalist.get(position).get("to_days"));
      h_custm_edtx.setText(datalist.get(position).get("custom_timings"));



      if (datalist.get(position).get("is_viewed").equalsIgnoreCase("Yes")) {
        d_prf_apntmnt_lay.setVisibility(View.VISIBLE);
        d_prf_up_img.setVisibility(View.GONE);
        d_prf_down_img.setVisibility(View.VISIBLE);
      } else {
        d_prf_apntmnt_lay.setVisibility(View.GONE);
        d_prf_up_img.setVisibility(View.VISIBLE);
        d_prf_down_img.setVisibility(View.GONE);

      }

      d_prf_dctrnme_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          if (datalist.get(position).get("is_viewed").equalsIgnoreCase("No")) {
            updatedata(datalist, datalist.get(position), position, "Yes", "doc_prfle");


          } else {
            updatedata(datalist, datalist.get(position), position, "No", "doc_prfle");

          }

        }
      });

      d_frmday_edtx.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          D_FromdaysListPopup(d_frmday_edtx,activity,datalist,position);
        }
      });

      d_today_edtx.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          D_ToDayListPopup(d_today_edtx,activity,datalist,position);
        }
      });



      d_avabletme_edtx3.setText(StoredObjects.time12hrsformat(datalist.get(position).get("from_time2")));
      d_tmetwo_edtx3.setText(StoredObjects.time12hrsformat(datalist.get(position).get("to_time2")));

      d_avabletme_edtx4.setText(StoredObjects.time12hrsformat(datalist.get(position).get("from_time3")));
      d_tmetwo_edtx4.setText(StoredObjects.time12hrsformat(datalist.get(position).get("to_time3")));

      d_avabletme_edtx.setText(StoredObjects.time12hrsformat(datalist.get(position).get("from_time")));
      d_tmetwo_edtx.setText(StoredObjects.time12hrsformat(datalist.get(position).get("to_time")));

      d_avabletme_edtx2.setText(StoredObjects.time12hrsformat(datalist.get(position).get("from_time1")));
      d_tmetwo_edtx2.setText(StoredObjects.time12hrsformat(datalist.get(position).get("to_time1")));



      d_avabletme_edtx.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


          D_FromtimeListPopup(d_avabletme_edtx,activity,"0",datalist,position);
        }
      });

      d_tmetwo_edtx.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          D_toTimeListPopup(d_tmetwo_edtx,activity,"0",datalist,position);
        }
      });


      d_avabletme_edtx2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          D_FromtimeListPopup(d_avabletme_edtx2,activity,"1",datalist,position);
        }
      });

      d_tmetwo_edtx2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          D_toTimeListPopup(d_tmetwo_edtx2,activity,"1",datalist,position);
        }
      });


      d_avabletme_edtx3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          D_FromtimeListPopup(d_avabletme_edtx3,activity,"2",datalist,position);
        }
      });

      d_tmetwo_edtx3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          D_toTimeListPopup(d_tmetwo_edtx3,activity,"2",datalist,position);
        }
      });

      d_avabletme_edtx4.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          D_FromtimeListPopup(d_avabletme_edtx4,activity,"3",datalist,position);
        }
      });

      d_tmetwo_edtx4.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          D_toTimeListPopup(d_tmetwo_edtx4,activity,"3",datalist,position);
        }
      });


    }


    else if (formtype.equalsIgnoreCase("d_patient_one")) {
      patient_name.setText(datalist.get(position).get("name"));
      patient_number.setText(datalist.get(position).get("phone"));
      patient_address.setText(datalist.get(position).get("email"));


      try {
        Glide.with(activity)
                .load(Uri.parse(RetrofitInstance.IMAGE_URL + datalist.get(position).get("image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(tl_pat_image);
      } catch (Exception e) {
        e.printStackTrace();

      }
      docdetails_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          StoredObjects.Pat_DocID=datalist.get(position).get("patient_id");
          fragmentcalling(new Doc_PatientPrescrDetails());
        }
      });

    }

    else if (formtype.equalsIgnoreCase("f_patient_one")) {
      patient_name.setText(datalist.get(position).get("name"));
      patient_number.setText(datalist.get(position).get("phone"));
      patient_address.setText(datalist.get(position).get("email"));


      try {
        Glide.with(activity)
                .load(Uri.parse(RetrofitInstance.BASEURL + datalist.get(position).get("image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(tl_pat_image);
      } catch (Exception e) {
        e.printStackTrace();

      }
      patient_edit_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          TL_Edit_Patient.data_list.clear();
          TL_Edit_Patient.data_list.add(datalist.get(position));
          fragmentcalling(new TL_Edit_Patient());
        }
      });

      patient_del_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          DeleteConfirmationpopup(activity,datalist,position,"f_patient_one");
        }
      });

      docdetails_lay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          viewPatientPopup(activity,datalist,position);
        }
      });
    }

  }

  private void AddAppointmentService(final Activity activity, String consult_str, String physuggestions, String patient_id,final ArrayList<HashMap<String, String>> datalist,final int position) {
    CustomProgressbar.Progressbarshow(activity);

    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.AddAppointmentone(RetrofitInstance.add_appointment_two,patient_id,consult_str, StoredObjects.Pat_DocID,physuggestions,StoredObjects.UserId,StoredObjects.UserRoleId,"").enqueue(new Callback<ResponseBody>() {
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

              updatedata(datalist, datalist.get(position), position, "Yes", "ad_apntmnt_clear");
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
  private void D_FromdaysListPopup(final EditText prfilenme, final Activity activity,final ArrayList<HashMap<String, String>> list,final int pos) {
    final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
    listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, TL_AddDoctor.daynames_list));
    listPopupWindowone.setAnchorView(prfilenme);
    listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(TL_AddDoctor.daynames_list.get(position));

        d_timeslot_updatedata(list,list.get(pos),pos,TL_AddDoctor.daynames_list.get(position),TL_AddDoctor.days_list.get(position).get("id"),"d_from");
        listPopupWindowone.dismiss();

      }
    });

    listPopupWindowone.show();
  }

  ArrayList<String> todaysname_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> todays_list = new ArrayList<>();


  private void D_ToDayListPopup(final EditText prfilenme,Activity activity,final ArrayList<HashMap<String, String>> list,final int pos) {
    todaysname_list.clear();
    todays_list.clear();



    for (int k = 0; k < TL_AddDoctor.days_list.size(); k++) {
      if(list.get(pos).get("from_days").equalsIgnoreCase(TL_AddDoctor.days_list.get(k).get("day_name"))){
        pos_four=k;
      }

    }
    for (int k = pos_four; k < TL_AddDoctor.days_list.size(); k++) {
      todays_list.add(TL_AddDoctor.days_list.get(k));
    }


    for (int k = 0; k < todays_list.size(); k++) {
      todaysname_list.add(todays_list.get(k).get("day_name"));
    }
    final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
    listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, todaysname_list));
    listPopupWindowone.setAnchorView(prfilenme);
    listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(todaysname_list.get(position));
        d_timeslot_updatedata(list,list.get(pos),pos,todaysname_list.get(position),todays_list.get(position).get("id"),"d_to");

        listPopupWindowone.dismiss();

      }
    });
    listPopupWindowone.show();
  }
 /* private void FromdaysPopup(final EditText prfilenme,final Activity activity,final ArrayList<HashMap<String, String>> list, final int pos) {
    final ListPopupWindow dropdownpopup = new ListPopupWindow(activity);
    dropdownpopup.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, TL_AddDoctor.daynames_list));
    dropdownpopup.setAnchorView(prfilenme);
    dropdownpopup.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    dropdownpopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(TL_AddDoctor.daynames_list.get(position));
        profile_updatedata(list, list.get(pos), pos,  TL_AddDoctor.days_list.get(position).get("id"), "from_day");
        //getToDaysService(activity, Doc_Profile.days_list.get(position).get("id"));

        TL_AddDoctor.to_days_list.clear();
        for(int k=position;k<TL_AddDoctor.days_list.size();k++){
          TL_AddDoctor.to_days_list.add(Doc_Profile.days_list.get(k));
        }

        Doc_Profile.to_daynames_list.clear();
        for (int k = 0; k < Doc_Profile.to_days_list.size(); k++) {
          Doc_Profile.to_daynames_list.add(Doc_Profile.to_days_list.get(k).get("day_name"));

        }
        dropdownpopup.dismiss();

      }
    });

    dropdownpopup.show();
  }

  private void ToDayListPopup(final EditText prfilenme,final Activity activity,final ArrayList<HashMap<String, String>> list, final int pos) {
    final ListPopupWindow dropdownpopup = new ListPopupWindow(activity);
    dropdownpopup.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, Doc_Profile.to_daynames_list));
    dropdownpopup.setAnchorView(prfilenme);
    dropdownpopup.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    dropdownpopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(Doc_Profile.to_daynames_list.get(position));
        profile_updatedata(list, list.get(pos), pos, Doc_Profile.to_days_list.get(position).get("id"), "to_day");

        dropdownpopup.dismiss();

      }
    });
    dropdownpopup.show();
  }*/





 /* private void timeListPopup(final EditText prfilenme,final Activity activity,final ArrayList<HashMap<String, String>> list, final int pos) {
    final ListPopupWindow dropdownpopup = new ListPopupWindow(activity);
    dropdownpopup.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, Doc_Profile.From_timeSlot_list));
    dropdownpopup.setAnchorView(prfilenme);
    dropdownpopup.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
    dropdownpopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(Doc_Profile.From_timeSlot_list.get(position));
        profile_updatedata(list, list.get(pos), pos, Doc_Profile.from_times_list.get(position).get("time_slot"), "from_time");
       // getToTimeService(activity, Doc_Profile.from_times_list.get(position).get("time_slot"));

        Doc_Profile.to_times_list.clear();
        for(int k=position;k<Doc_Profile.from_times_list.size();k++){
          Doc_Profile.to_times_list.add(Doc_Profile.from_times_list.get(k));
        }


        Doc_Profile.to_timeSlot_list.clear();
        for (int k = 0; k < Doc_Profile.to_times_list.size(); k++) {
          Doc_Profile.to_timeSlot_list.add(StoredObjects.time12hrsformat(Doc_Profile.to_times_list.get(k).get("time_slot")));
        }
        dropdownpopup.dismiss();

      }
    });

    dropdownpopup.show();
  }

  private void toTimeListPopup(final EditText prfilenme,Activity activity,final ArrayList<HashMap<String, String>> list,final int pos) {
    final ListPopupWindow dropdownpopup = new ListPopupWindow(activity);
    dropdownpopup.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, Doc_Profile.to_timeSlot_list));
    dropdownpopup.setAnchorView(prfilenme);
    dropdownpopup.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
    dropdownpopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(Doc_Profile.to_timeSlot_list.get(position));

        profile_updatedata(list, list.get(pos), pos, Doc_Profile.to_times_list.get(position).get("time_slot"), "to_time");

        dropdownpopup.dismiss();

      }
    });

    dropdownpopup.show();
  }
*/
  private void physicalexampopupOrg(final Activity activity, final ArrayList<HashMap<String, String>> datalist, final int position) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.add_physical_examination_popup);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomButton search_button = (CustomButton) dialog.findViewById(R.id.search_button);
    final CustomEditText physcial_examination_edtx = (CustomEditText) dialog.findViewById(R.id.physcial_examination_edtx);
    final TextView info_txt = dialog.findViewById(R.id.info_txt);

    info_txt.setText("Edit Physical Suggestion");
    final ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);


    physcial_examination_edtx.setText(datalist.get(position).get("suggestion"));
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
        String physcial_examination_str = physcial_examination_edtx.getText().toString().trim();
        if (StoredObjects.inputValidation(physcial_examination_edtx, "Please enter suggestion",activity))
          if (InterNetChecker.isNetworkAvailable(activity)) {
            String suggest_id =  datalist.get(position).get("suggestion_id");
            updatedata(datalist,datalist.get(position),position,physcial_examination_str,"physical_suggestion");
            editPhsyicalSuggestions(activity, physcial_examination_str,suggest_id);
          } else {
            StoredObjects.ToastMethod(activity.getResources().getString(R.string.nointernet), activity);
          }
        dialog.dismiss();
      }
    });

    dialog.show();
  }

  private void editPhsyicalSuggestions(final Activity activity, String physcial_examination_str, String suggest_id) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.editPhsyicalSuggestions(RetrofitInstance.edit_physical_suggestion, physcial_examination_str,StoredObjects.UserId,suggest_id,StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("response", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Edited Successfully",activity);
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

  private void deleteAssistantService(final Activity activity, final ArrayList<HashMap<String, String>> datalist, final int position, String assistant_id_str) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.delDrAssistants(RetrofitInstance.doctor_delete_assistant,assistant_id_str ,StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("del_id", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Deleted successfully",activity);
              datalist.remove(position);
             // Doc_Assistant.h_assistant_recyler.removeViewAt(position);
              Doc_Assistant.adapter.notifyDataSetChanged();
              Doc_Assistant.updatelay(datalist);
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


  private void delPhysicalSuggestions(final Activity activity, String del_id, final int position, final ArrayList<HashMap<String, String>> datalist) {

    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.delPhsyicalSuggestions(RetrofitInstance.del_physical_suggestion,del_id ,StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("del_id", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Deleted successfully",activity);
              datalist.remove(position);
             // Doc_PhysicalSuggestions.patientlist_history_recyler.removeViewAt(position);
              Doc_PhysicalSuggestions.adapter.notifyDataSetChanged();
              Doc_PhysicalSuggestions.updatelay(datalist);
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

  public void fragmentcalling(Fragment fragment) {
    FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("").commit();
  }
  public void d_fragmentcalling(Fragment fragment) {
    FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
  }
  //update data
  public void updatedata(final ArrayList<HashMap<String, String>> list, HashMap<String, String> HashMapdata, int postion, String val, String val1,String type) {

    int i = list.indexOf(HashMapdata);

    if (i == -1) {
      throw new IndexOutOfBoundsException();
    }

    if (type.equalsIgnoreCase("add_physical")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();

      dumpData_update.put("suggestion_id", val1);
      dumpData_update.put("suggestion_value", list.get(postion).get("suggestion_value"));
      dumpData_update.put("suggestion_name", val);
      dumpData_update.put("remove", list.get(postion).get("remove"));

      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      if(StoredObjects.page_type.equalsIgnoreCase("add_appointment_one")){
        Add_Appointment_One.adapter.notifyDataSetChanged();
      }else  if(StoredObjects.page_type.equalsIgnoreCase("add_apntmnt")){
        Add_Appointment_Two.adapter.notifyDataSetChanged();
      }


    } if (type.equalsIgnoreCase("pat_add_physical")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();

      dumpData_update.put("suggestion_id", val1);
      dumpData_update.put("suggestion_value", list.get(postion).get("suggestion_value"));
      dumpData_update.put("examination_id", list.get(postion).get("examination_id"));
      dumpData_update.put("suggestion_name", val);
      dumpData_update.put("remove", list.get(postion).get("remove"));


      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      Doc_Patient_Details.phy_adapter.notifyDataSetChanged();

    }



  }

  //update data
  public void prescr_updatedata(final ArrayList<HashMap<String, String>> list, HashMap<String, String> HashMapdata, int postion, String dose, String noofdays,String remarks,String val4,String val5,String type) {

    int i = list.indexOf(HashMapdata);

    if (i == -1) {
      throw new IndexOutOfBoundsException();
    }


    if (type.equalsIgnoreCase("diagnose_suggest")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();

      dumpData_update.put("referredLab",noofdays );
      dumpData_update.put("suggestedTest",dose);

      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      Doc_Patient_Details.test_adapter.notifyDataSetChanged();

    }
   if (type.equalsIgnoreCase("prescr_updateval")) {

     HashMap<String, String> dumpData_update = new HashMap<String, String>();
     dumpData_update.put("brand_id", list.get(postion).get("brand_id"));
     dumpData_update.put("brand_name", list.get(postion).get("brand_name"));
     dumpData_update.put("molecule_id", list.get(postion).get("molecule_id"));
     dumpData_update.put("molecule", list.get(postion).get("molecule"));
     dumpData_update.put("no_of_times", val4);
     dumpData_update.put("intake", val5);
     dumpData_update.put("no_of_days", noofdays);
     dumpData_update.put("dose", dose);
     dumpData_update.put("remarks", remarks);


     list.remove(HashMapdata);
     list.add(postion, dumpData_update);
     if(  StoredObjects.page_type.equalsIgnoreCase("doc_add_medication")){
       Doc_AddMedication.medi_adapter.notifyDataSetChanged();
     }else{
       P_AddMedication.medi_adapter.notifyDataSetChanged();
     }


   } if (type.equalsIgnoreCase("medication_view_no_of_times")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();
      dumpData_update.put("brand_id", list.get(postion).get("brand_id"));
      dumpData_update.put("brand_name", list.get(postion).get("brand_name"));
      dumpData_update.put("molecule_id", list.get(postion).get("molecule_id"));
      dumpData_update.put("molecule", list.get(postion).get("molecule"));
      dumpData_update.put("no_of_times", val4);
      dumpData_update.put("intake", list.get(postion).get("intake"));
      dumpData_update.put("no_of_days", noofdays);
      dumpData_update.put("dose", dose);
      dumpData_update.put("remarks", remarks);


      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      if(  StoredObjects.page_type.equalsIgnoreCase("doc_add_medication")){
        Doc_AddMedication.medi_adapter.notifyDataSetChanged();
      }else{
        P_AddMedication.medi_adapter.notifyDataSetChanged();
      }


    }if (type.equalsIgnoreCase("medication_intake")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();
      dumpData_update.put("brand_id", list.get(postion).get("brand_id"));
      dumpData_update.put("brand_name", list.get(postion).get("brand_name"));
      dumpData_update.put("molecule_id", list.get(postion).get("molecule_id"));
      dumpData_update.put("molecule", list.get(postion).get("molecule"));
      dumpData_update.put("no_of_times", list.get(postion).get("no_of_times"));
      dumpData_update.put("intake", val4);
      dumpData_update.put("no_of_days", noofdays);
      dumpData_update.put("dose", dose);
      dumpData_update.put("remarks", remarks);


      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      if(  StoredObjects.page_type.equalsIgnoreCase("doc_add_medication")){
        Doc_AddMedication.medi_adapter.notifyDataSetChanged();
      }else{
        P_AddMedication.medi_adapter.notifyDataSetChanged();
      }


    }
    if (type.equalsIgnoreCase("testsuggtion")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();
      dumpData_update.put("report_date", list.get(postion).get("report_date"));
      dumpData_update.put("report_image", list.get(postion).get("report_image"));
      dumpData_update.put("diagnostic_name",dose);
      dumpData_update.put("test_name", noofdays);
      dumpData_update.put("test_remarks", remarks);
      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      P_AddTestSuggestions.test_adapter.notifyDataSetChanged();


    }
    if (type.equalsIgnoreCase("testsugg_date")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();
      dumpData_update.put("report_date", val4);
      dumpData_update.put("report_image", list.get(postion).get("report_image"));
      dumpData_update.put("diagnostic_name",dose);
      dumpData_update.put("test_name", noofdays);
      dumpData_update.put("test_remarks", remarks);
      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      P_AddTestSuggestions.test_adapter.notifyDataSetChanged();


    }



  }

  public static void test_updatedata(final ArrayList<HashMap<String, String>> list, HashMap<String, String> HashMapdata,
                              int postion, String val,String type) {

    int i = list.indexOf(HashMapdata);

    if (i == -1) {
      throw new IndexOutOfBoundsException();
    }
    if (type.equalsIgnoreCase("testsugg_file")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();
      dumpData_update.put("report_date", list.get(postion).get("report_date"));
      dumpData_update.put("report_image", val);
      dumpData_update.put("diagnostic_name", list.get(postion).get("diagnostic_name"));
      dumpData_update.put("test_name", list.get(postion).get("test_name"));
      dumpData_update.put("test_remarks", list.get(postion).get("test_remarks"));
      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      P_AddTestSuggestions.test_adapter.notifyDataSetChanged();


    }




  }

  public void d_timeslot_updatedata(final ArrayList<HashMap<String, String>> list, HashMap<String, String> HashMapdata, int postion, String val, String val1,String type) {

    int i = list.indexOf(HashMapdata);

    if (i == -1) {
      throw new IndexOutOfBoundsException();
    }


    HashMap<String, String> dumpData_update = new HashMap<String, String>();
    dumpData_update.put("id", list.get(postion).get("id"));
    dumpData_update.put("hospital_id", list.get(postion).get("hospital_id"));
    dumpData_update.put("from_days", list.get(postion).get("from_days"));
    dumpData_update.put("to_days", list.get(postion).get("to_days"));
    dumpData_update.put("from_time", list.get(postion).get("from_time"));
    dumpData_update.put("to_time", list.get(postion).get("to_time"));
    dumpData_update.put("from_time1", list.get(postion).get("from_time1"));
    dumpData_update.put("to_time1", list.get(postion).get("to_time1"));
    dumpData_update.put("from_time2", list.get(postion).get("from_time2"));
    dumpData_update.put("to_time2", list.get(postion).get("to_time2"));
    dumpData_update.put("from_time3", list.get(postion).get("from_time3"));
    dumpData_update.put("to_time3", list.get(postion).get("to_time3"));

    dumpData_update.put("clinic_name", list.get(postion).get("clinic_name"));
    dumpData_update.put("custom_timings", list.get(postion).get("custom_timings"));
    dumpData_update.put("is_viewed", list.get(postion).get("is_viewed"));


    dumpData_update.put("from_time_pos", list.get(postion).get("from_time_pos"));
    dumpData_update.put("to_time_pos", list.get(postion).get("to_time_pos"));
    dumpData_update.put("from_time1_pos", list.get(postion).get("from_time1_pos"));
    dumpData_update.put("to_time1_pos", list.get(postion).get("to_time1_pos"));
    dumpData_update.put("from_time2_pos", list.get(postion).get("from_time2_pos"));
    dumpData_update.put("to_time2_pos", list.get(postion).get("to_time2_pos"));
    dumpData_update.put("from_time3_pos", list.get(postion).get("from_time3_pos"));
    dumpData_update.put("to_time3_pos", list.get(postion).get("to_time3_pos"));

    dumpData_update.put("from_days_pos", list.get(postion).get("from_days_pos"));
    dumpData_update.put("to_days_pos", list.get(postion).get("to_days_pos"));




    if(type.equalsIgnoreCase("f_1")){
      dumpData_update.put("from_time",val);
      dumpData_update.put("from_time_pos", val1);

      dumpData_update.put("to_time", "");
      dumpData_update.put("from_time1", "");
      dumpData_update.put("to_time1","");
      dumpData_update.put("from_time2", "");
      dumpData_update.put("to_time2","");
      dumpData_update.put("from_time3", "");
      dumpData_update.put("to_time3","");
    }else if(type.equalsIgnoreCase("f_2")){
      dumpData_update.put("from_time1",val);
      dumpData_update.put("from_time1_pos", val1);

      dumpData_update.put("to_time1","");
      dumpData_update.put("from_time2", "");
      dumpData_update.put("to_time2","");
      dumpData_update.put("from_time3", "");
      dumpData_update.put("to_time3","");
    }else if(type.equalsIgnoreCase("f_3")){
      dumpData_update.put("from_time2",val);
      dumpData_update.put("from_time2_pos", val1);

      dumpData_update.put("to_time2","");
      dumpData_update.put("from_time3", "");
      dumpData_update.put("to_time3","");
    }else if(type.equalsIgnoreCase("f_4")){
      dumpData_update.put("from_time3",val);
      dumpData_update.put("from_time3_pos", val1);

      dumpData_update.put("to_time3","");
    }

    else  if(type.equalsIgnoreCase("t_1")){
      dumpData_update.put("to_time",val);
      dumpData_update.put("to_time_pos", val1);

      dumpData_update.put("from_time1", "");
      dumpData_update.put("to_time1","");
      dumpData_update.put("from_time2", "");
      dumpData_update.put("to_time2","");
      dumpData_update.put("from_time3", "");
      dumpData_update.put("to_time3","");
    }else  if(type.equalsIgnoreCase("t_2")){
      dumpData_update.put("to_time1",val);
      dumpData_update.put("to_time1_pos", val1);

      dumpData_update.put("from_time2", "");
      dumpData_update.put("to_time2","");
      dumpData_update.put("from_time3", "");
      dumpData_update.put("to_time3","");
    }else  if(type.equalsIgnoreCase("t_3")){
      dumpData_update.put("to_time2",val);
      dumpData_update.put("to_time2_pos", val1);

      dumpData_update.put("from_time3", "");
      dumpData_update.put("to_time3","");
    }else  if(type.equalsIgnoreCase("t_4")){
      dumpData_update.put("to_time3",val);
      dumpData_update.put("to_time3_pos", val1);
    }

    else  if(type.equalsIgnoreCase("d_from")){
      dumpData_update.put("from_days",val);

      dumpData_update.put("from_days_pos", val1);
      dumpData_update.put("to_days","");
      dumpData_update.put("to_days_pos", "0");
    }else  if(type.equalsIgnoreCase("d_to")){
      dumpData_update.put("to_days",val);
      dumpData_update.put("to_days_pos", val1);
    }else  if(type.equalsIgnoreCase("custom")){
      dumpData_update.put("custom_timings",val);
    }


    list.remove(HashMapdata);
    list.add(postion, dumpData_update);
    Doc_Profile.adapter.notifyDataSetChanged();




  }

  public void timeslot_updatedata(final ArrayList<HashMap<String, String>> list, HashMap<String, String> HashMapdata, int postion, String val, String val1,String type) {

    int i = list.indexOf(HashMapdata);

    if (i == -1) {
      throw new IndexOutOfBoundsException();
    }


    HashMap<String, String> dumpData_update = new HashMap<String, String>();

    dumpData_update.put("id", list.get(postion).get("id"));
    dumpData_update.put("from_days", list.get(postion).get("from_days"));
    dumpData_update.put("to_days", list.get(postion).get("to_days"));
    dumpData_update.put("from_time", list.get(postion).get("from_time"));
    dumpData_update.put("to_time", list.get(postion).get("to_time"));
    dumpData_update.put("from_time1", list.get(postion).get("from_time1"));
    dumpData_update.put("to_time1", list.get(postion).get("to_time1"));
    dumpData_update.put("from_time2", list.get(postion).get("from_time2"));
    dumpData_update.put("to_time2", list.get(postion).get("to_time2"));
    dumpData_update.put("from_time3", list.get(postion).get("from_time3"));
    dumpData_update.put("to_time3", list.get(postion).get("to_time3"));

    dumpData_update.put("from_time_pos", list.get(postion).get("from_time_pos"));
    dumpData_update.put("to_time_pos", list.get(postion).get("to_time_pos"));
    dumpData_update.put("from_time1_pos", list.get(postion).get("from_time1_pos"));
    dumpData_update.put("to_time1_pos", list.get(postion).get("to_time1_pos"));
    dumpData_update.put("from_time2_pos", list.get(postion).get("from_time2_pos"));
    dumpData_update.put("to_time2_pos", list.get(postion).get("to_time2_pos"));
    dumpData_update.put("from_time3_pos", list.get(postion).get("from_time3_pos"));
    dumpData_update.put("to_time3_pos", list.get(postion).get("to_time3_pos"));

    dumpData_update.put("from_days_pos", list.get(postion).get("from_days_pos"));
    dumpData_update.put("to_days_pos", list.get(postion).get("to_days_pos"));


    if(type.equalsIgnoreCase("f_1")){
      dumpData_update.put("from_time",val);
      dumpData_update.put("from_time_pos", val1);

      dumpData_update.put("to_time", "");
      dumpData_update.put("from_time1", "");
      dumpData_update.put("to_time1","");
      dumpData_update.put("from_time2", "");
      dumpData_update.put("to_time2","");
      dumpData_update.put("from_time3", "");
      dumpData_update.put("to_time3","");
    }else if(type.equalsIgnoreCase("f_2")){
      dumpData_update.put("from_time1",val);
      dumpData_update.put("from_time1_pos", val1);

      dumpData_update.put("to_time1","");
      dumpData_update.put("from_time2", "");
      dumpData_update.put("to_time2","");
      dumpData_update.put("from_time3", "");
      dumpData_update.put("to_time3","");
    }else if(type.equalsIgnoreCase("f_3")){
      dumpData_update.put("from_time2",val);
      dumpData_update.put("from_time2_pos", val1);

      dumpData_update.put("to_time2","");
      dumpData_update.put("from_time3", "");
      dumpData_update.put("to_time3","");
    }else if(type.equalsIgnoreCase("f_4")){
      dumpData_update.put("from_time3",val);
      dumpData_update.put("from_time3_pos", val1);

      dumpData_update.put("to_time3","");
    }

    else  if(type.equalsIgnoreCase("t_1")){
      dumpData_update.put("to_time",val);
      dumpData_update.put("to_time_pos", val1);

      dumpData_update.put("from_time1", "");
      dumpData_update.put("to_time1","");
      dumpData_update.put("from_time2", "");
      dumpData_update.put("to_time2","");
      dumpData_update.put("from_time3", "");
      dumpData_update.put("to_time3","");
    }else  if(type.equalsIgnoreCase("t_2")){
      dumpData_update.put("to_time1",val);
      dumpData_update.put("to_time1_pos", val1);

      dumpData_update.put("from_time2", "");
      dumpData_update.put("to_time2","");
      dumpData_update.put("from_time3", "");
      dumpData_update.put("to_time3","");
    }else  if(type.equalsIgnoreCase("t_3")){
      dumpData_update.put("to_time2",val);
      dumpData_update.put("to_time2_pos", val1);

      dumpData_update.put("from_time3", "");
      dumpData_update.put("to_time3","");
    }else  if(type.equalsIgnoreCase("t_4")){
      dumpData_update.put("to_time3",val);
      dumpData_update.put("to_time3_pos", val1);
    }

    else  if(type.equalsIgnoreCase("d_from")){
      dumpData_update.put("from_days",val);

      dumpData_update.put("from_days_pos", val1);
      dumpData_update.put("to_days","");
      dumpData_update.put("to_days_pos", "0");
    }else  if(type.equalsIgnoreCase("d_to")){
      dumpData_update.put("to_days",val);
      dumpData_update.put("to_days_pos", val1);
    }


    list.remove(HashMapdata);
    list.add(postion, dumpData_update);
    if(StoredObjects.page_type.equalsIgnoreCase("tl_add_doctor")){
      TL_AddDoctor.adapter.notifyDataSetChanged();
    }else  if(StoredObjects.page_type.equalsIgnoreCase("tl_edit_doctor")){
      TL_Edit_Doctor.adapter.notifyDataSetChanged();
    }else  if(StoredObjects.page_type.equalsIgnoreCase("h_add_doctor")){
      H_AddDoctor.adapter.notifyDataSetChanged();
    }else  if(StoredObjects.page_type.equalsIgnoreCase("h_edit_doctor")){
      H_EditDoctor.adapter.notifyDataSetChanged();
    }




  }

  //update data
  public void updatedata(final ArrayList<HashMap<String, String>> list, HashMap<String, String> HashMapdata, int postion, String val, String type) {

    int i = list.indexOf(HashMapdata);

    if (i == -1) {
      throw new IndexOutOfBoundsException();
    }

    if (type.equalsIgnoreCase("doc_timeslots_remove")) {

      list.remove(postion);
      if(StoredObjects.page_type.equalsIgnoreCase("tl_add_doctor")){
        TL_AddDoctor.adapter.notifyDataSetChanged();
      }else  if(StoredObjects.page_type.equalsIgnoreCase("tl_edit_doctor")){
        TL_Edit_Doctor.adapter.notifyDataSetChanged();
      }else  if(StoredObjects.page_type.equalsIgnoreCase("h_add_doctor")){
        H_AddDoctor.adapter.notifyDataSetChanged();
      }else  if(StoredObjects.page_type.equalsIgnoreCase("h_edit_doctor")){
        H_EditDoctor.adapter.notifyDataSetChanged();
      }


    }



    if (type.equalsIgnoreCase("testsugg_remove")) {
      list.remove(postion);
      P_AddTestSuggestions.test_adapter.notifyDataSetChanged();

    }

    if (type.equalsIgnoreCase("brand_suggestions")) {




      for(int k=0;k<list.size();k++){
        HashMap<String, String> dumpData_update = new HashMap<String, String>();
        dumpData_update.put("BrandId", list.get(k).get("BrandId"));
        dumpData_update.put("Brand", list.get(k).get("Brand"));
        dumpData_update.put("MoleculeId", list.get(k).get("MoleculeId"));
        dumpData_update.put("Molecule",list.get(k).get("Molecule"));

        if(k==postion){
          dumpData_update.put("is_viewed", "Yes");
        }else{
          dumpData_update.put("is_viewed", "No");
        }


        list.remove(k);
        list.add(k, dumpData_update);
      }

      if(StoredObjects.page_type.equalsIgnoreCase("doc_patient_details")){
        Doc_Patient_Details.brand_adapter.notifyDataSetChanged();
      }else{
        P_Add_Prescription.brand_adapter.notifyDataSetChanged();
      }

    }







    if (type.equalsIgnoreCase("add_physical_remove")) {
      list.remove(postion);
      if(StoredObjects.page_type.equalsIgnoreCase("add_appointment_one")){
        Add_Appointment_One.adapter.notifyDataSetChanged();
      }else  if(StoredObjects.page_type.equalsIgnoreCase("add_apntmnt")){
        Add_Appointment_Two.adapter.notifyDataSetChanged();
      }

    }

    if (type.equalsIgnoreCase("add_physical")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();

      dumpData_update.put("suggestion_id", list.get(postion).get("suggestion_id"));
      dumpData_update.put("suggestion_value", val);
      dumpData_update.put("suggestion_name", list.get(postion).get("suggestion_name"));
      dumpData_update.put("remove", list.get(postion).get("remove"));

      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      if(StoredObjects.page_type.equalsIgnoreCase("add_appointment_one")){
        Add_Appointment_One.adapter.notifyDataSetChanged();
      }else  if(StoredObjects.page_type.equalsIgnoreCase("add_apntmnt")){
        Add_Appointment_Two.adapter.notifyDataSetChanged();
      }

    }
    if (type.equalsIgnoreCase("pat_add_physical")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();

      dumpData_update.put("suggestion_id", list.get(postion).get("suggestion_id"));
      dumpData_update.put("suggestion_value", val);
      dumpData_update.put("suggestion_name", list.get(postion).get("suggestion_name"));
      dumpData_update.put("remove", list.get(postion).get("remove"));
      dumpData_update.put("examination_id", list.get(postion).get("examination_id"));



      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      Doc_Patient_Details.phy_adapter.notifyDataSetChanged();

    }
    if (type.equalsIgnoreCase("pat_add_physical_remove")) {
      list.remove(postion);
      Doc_Patient_Details.phy_adapter.notifyDataSetChanged();

    }if (type.equalsIgnoreCase("medication_view_remove")) {
      list.remove(postion);
      if(  StoredObjects.page_type.equalsIgnoreCase("doc_add_medication")){
        Doc_AddMedication.medi_adapter.notifyDataSetChanged();
      }else{
        P_AddMedication.medi_adapter.notifyDataSetChanged();
      }

    }


    if (type.equalsIgnoreCase("diagnose_suggest")) {
      list.remove(postion);
      Doc_Patient_Details.test_adapter.notifyDataSetChanged();

    }

    if (type.equalsIgnoreCase("diagnose_suggest_lab")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();

      dumpData_update.put("referredLab", val);
      dumpData_update.put("suggestedTest", list.get(postion).get("suggestedTest"));

      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      Doc_Patient_Details.test_adapter.notifyDataSetChanged();

    }

    if (type.equalsIgnoreCase("diagnose_suggest_test")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();

      dumpData_update.put("referredLab", list.get(postion).get("referredLab"));
      dumpData_update.put("suggestedTest",val);

      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      Doc_Patient_Details.test_adapter.notifyDataSetChanged();

    }
    if (type.equalsIgnoreCase("physical_suggestion")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();

      dumpData_update.put("suggestion_id", list.get(postion).get("suggestion_id"));
    dumpData_update.put("suggestion", val);
    dumpData_update.put("doctor_id", list.get(postion).get("doctor_id"));
    dumpData_update.put("created_at", list.get(postion).get("created_at"));

      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      Doc_PhysicalSuggestions.adapter.notifyDataSetChanged();

    }
    if (type.equalsIgnoreCase("assign_doctor")) {


      HashMap<String, String> dumpData_update = new HashMap<String, String>();
      dumpData_update.put("is_viewed", val);
      dumpData_update.put("user_id", list.get(postion).get("user_id"));
      dumpData_update.put("doctor_id", list.get(postion).get("doctor_id"));
      dumpData_update.put("name", list.get(postion).get("name"));

      list.remove(HashMapdata);
      list.add(postion, dumpData_update);

      if(StoredObjects.page_type.equalsIgnoreCase("add_doctor")){
        H_AddAssistant.adapter.notifyDataSetChanged();
      }else{
        H_EditAssistant.adapter.notifyDataSetChanged();
      }


    }

    if (type.equalsIgnoreCase("addapointment")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();
      dumpData_update.put("is_viewed", val);
      dumpData_update.put("doctor_id", list.get(postion).get("doctor_id"));
      dumpData_update.put("name", list.get(postion).get("name"));
      dumpData_update.put("specialization", list.get(postion).get("specialization"));
      dumpData_update.put("physical_suggestions_count", list.get(postion).get("physical_suggestions_count"));
      dumpData_update.put("physical_suggestions", list.get(postion).get("physical_suggestions"));
      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      Add_Appointment.adapter.notifyDataSetChanged();

    }

    if (type.equalsIgnoreCase("f_hospital_four")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();
      dumpData_update.put("is_viewed", val);
      dumpData_update.put("name", list.get(postion).get("name"));

      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      Hospital_DoctorLeads.adapter.notifyDataSetChanged();

    }
    if (type.equalsIgnoreCase("doc_prfle")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();
      dumpData_update.put("is_viewed", val);
      dumpData_update.put("id", list.get(postion).get("id"));
      dumpData_update.put("clinic_name", list.get(postion).get("clinic_name"));
      dumpData_update.put("custom_timings", list.get(postion).get("custom_timings"));

      dumpData_update.put("hospital_id", list.get(postion).get("hospital_id"));

      dumpData_update.put("from_days", list.get(postion).get("from_days"));
      dumpData_update.put("to_days", list.get(postion).get("to_days"));

      dumpData_update.put("from_time", list.get(postion).get("from_time"));
      dumpData_update.put("to_time", list.get(postion).get("to_time"));

      dumpData_update.put("from_time1", list.get(postion).get("from_time1"));
      dumpData_update.put("to_time1", list.get(postion).get("to_time1"));

      dumpData_update.put("from_time2", list.get(postion).get("from_time2"));
      dumpData_update.put("to_time2", list.get(postion).get("to_time2"));

      dumpData_update.put("from_time3", list.get(postion).get("from_time3"));
      dumpData_update.put("to_time3", list.get(postion).get("to_time3"));

      dumpData_update.put("from_time_pos", list.get(postion).get("from_time_pos"));
      dumpData_update.put("to_time_pos", list.get(postion).get("to_time_pos"));
      dumpData_update.put("from_time1_pos", list.get(postion).get("from_time1_pos"));

      dumpData_update.put("to_time1_pos", list.get(postion).get("to_time1_pos"));
      dumpData_update.put("from_time2_pos", list.get(postion).get("from_time2_pos"));
      dumpData_update.put("to_time2_pos", list.get(postion).get("to_time2_pos"));


      dumpData_update.put("from_time3_pos", list.get(postion).get("from_time3_pos"));
      dumpData_update.put("to_time3_pos", list.get(postion).get("to_time3_pos"));


      dumpData_update.put("from_days_pos", list.get(postion).get("from_days_pos"));
      dumpData_update.put("to_days_pos", list.get(postion).get("to_days_pos"));




      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      Doc_Profile.adapter.notifyDataSetChanged();

    }



    if (type.equalsIgnoreCase("h_presclist")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();
      dumpData_update.put("is_viewed", val);
      dumpData_update.put("name", list.get(postion).get("name"));

      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      H_TotalPrescriptions.adapter.notifyDataSetChanged();

    }


    if (type.equalsIgnoreCase("h_patientlist_main")) {

      HashMap<String, String> dumpData_update = new HashMap<String, String>();
      dumpData_update.put("is_viewed", val);
      dumpData_update.put("name", list.get(postion).get("name"));

      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      H_PatientList_Main.adapter.notifyDataSetChanged();

    }
  }
  public void profile_updatedata(final ArrayList<HashMap<String, String>> list, HashMap<String, String> HashMapdata, int postion, String val, String type) {

    int i = list.indexOf(HashMapdata);

    if (i == -1) {
      throw new IndexOutOfBoundsException();
    }



      HashMap<String, String> dumpData_update = new HashMap<String, String>();
      dumpData_update.put("is_viewed", "Yes");
      dumpData_update.put("clinic_name", list.get(postion).get("clinic_name"));

      dumpData_update.put("hospital_id", list.get(postion).get("hospital_id"));
      if (type.equalsIgnoreCase("from_day")) {
       dumpData_update.put("from_days",val);
       dumpData_update.put("to_days", "0");
        dumpData_update.put("from_time", list.get(postion).get("from_time"));
        dumpData_update.put("to_time", list.get(postion).get("to_time"));
      }else if(type.equalsIgnoreCase("to_day")){
        dumpData_update.put("from_days", list.get(postion).get("from_days"));
        dumpData_update.put("to_days", val);

        dumpData_update.put("from_time", list.get(postion).get("from_time"));
        dumpData_update.put("to_time", list.get(postion).get("to_time"));

      }else if(type.equalsIgnoreCase("from_time")){
        dumpData_update.put("from_time", val);
        dumpData_update.put("to_time", "0");

        dumpData_update.put("from_days", list.get(postion).get("from_days"));
        dumpData_update.put("to_days", list.get(postion).get("to_days"));
      }else if(type.equalsIgnoreCase("to_time")){
        dumpData_update.put("from_time", list.get(postion).get("from_time"));
        dumpData_update.put("to_time", val);

        dumpData_update.put("from_days", list.get(postion).get("from_days"));
        dumpData_update.put("to_days", list.get(postion).get("to_days"));
      }else{
        dumpData_update.put("from_days", list.get(postion).get("from_days"));
        dumpData_update.put("to_days", list.get(postion).get("to_days"));
        dumpData_update.put("from_time", list.get(postion).get("from_time"));
        dumpData_update.put("to_time", list.get(postion).get("to_time"));
      }


    if(type.equalsIgnoreCase("custom")){
      dumpData_update.put("custom_timings", val);
    }else{
      dumpData_update.put("custom_timings", list.get(postion).get("custom_timings"));
    }



      dumpData_update.put("id", list.get(postion).get("id"));


      list.remove(HashMapdata);
      list.add(postion, dumpData_update);
      Doc_Profile.adapter.notifyDataSetChanged();



  }

  public void brand_updatedata(final ArrayList<HashMap<String, String>> list, HashMap<String, String> HashMapdata,
                               int postion, String val, String val1,String val2,String val3,   String type) {

    int i = list.indexOf(HashMapdata);

    if (i == -1) {
      throw new IndexOutOfBoundsException();
    }

    HashMap<String, String> dumpData_update = new HashMap<String, String>();
    dumpData_update.put("brand_id",val2);
    dumpData_update.put("brand_name", val);
    dumpData_update.put("molecule_id", val3);
    dumpData_update.put("molecule", val1);
    dumpData_update.put("no_of_times", list.get(postion).get("no_of_times"));
    dumpData_update.put("intake", list.get(postion).get("intake"));
    dumpData_update.put("no_of_days", list.get(postion).get("no_of_days"));
    dumpData_update.put("dose", list.get(postion).get("dose"));
    dumpData_update.put("remarks", list.get(postion).get("remarks"));


    list.remove(HashMapdata);
    list.add(postion, dumpData_update);
    if(  StoredObjects.page_type.equalsIgnoreCase("doc_add_medication")){
      Doc_AddMedication.medi_adapter.notifyDataSetChanged();
    }else{
      P_AddMedication.medi_adapter.notifyDataSetChanged();
    }




  }

  /*private void assessmentpopup(final Activity activity) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.assessment_and_plan);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomButton search_button = (CustomButton) dialog.findViewById(R.id.search_button);
    ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);


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

        dialog.dismiss();
      }
    });

    dialog.show();
  }
*/


  /*private void physicalexampopup(final Activity activity,final ArrayList<HashMap<String,String>> arrayList,final  int pos) {
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

    Add_Apntmnt_Three.p_adapter=new HashMapRecycleviewadapter(activity, Add_Apntmnt_Three.physuggestionslist,"add_physical",add_physical_recycle,R.layout.add_physical_examination_listitems);
    add_physical_recycle.setAdapter(Add_Apntmnt_Three.p_adapter);


    addphy_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Add_Apntmnt_Three.addphysuggestions();
        Add_Apntmnt_Three.p_adapter.notifyDataSetChanged();
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
        for(int k=0;k<Add_Apntmnt_Three.physuggestionslist.size();k++){
          if(Add_Apntmnt_Three.physuggestionslist.get(k).get("suggestion_id").equalsIgnoreCase("")&&
                  Add_Apntmnt_Three.physuggestionslist.get(k).get("suggestion_value").equalsIgnoreCase("")){

          }else{
            val=val+1;
          }

        }
        if(val==Add_Apntmnt_Three.physuggestionslist.size()){

          String physuggestionsval="";
          JSONArray PhysicalsugArray = new JSONArray();
          JSONObject jsonObject1 = null;
          for (int i= 0;i<Add_Apntmnt_Three.physuggestionslist.size();i++) {
            try {
              jsonObject1 = new JSONObject();
              jsonObject1.put("suggestion_id", Add_Apntmnt_Three.physuggestionslist.get(i).get("suggestion_id"));
              jsonObject1.put("suggestion_value",  Add_Apntmnt_Three.physuggestionslist.get(i).get("suggestion_value"));

              PhysicalsugArray.put(jsonObject1);

            } catch (JSONException e) {
              e.printStackTrace();
            }
          }

          physuggestionsval=PhysicalsugArray.toString();

          updatedata(arrayList,arrayList.get(pos),pos,physuggestionsval,"ad_phy_suggestions");


          dialog.dismiss();
        }else{
          StoredObjects.ToastMethod("Please fill Physical Examinations Details",activity);
        }

      }
    });

    dialog.show();
  }
*/
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

    Doc_Patient_Details.phy_adapter=new HashMapRecycleviewadapter(activity, Doc_Patient_Details.physuggestionslist,"pat_add_physical",add_physical_recycle,R.layout.add_physical_examination_listitems);
    add_physical_recycle.setAdapter( Doc_Patient_Details.phy_adapter);


    addphy_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        int val=0;
        for(int k=0;k<Doc_Patient_Details.physuggestionslist.size();k++){
          if(Doc_Patient_Details.physuggestionslist.get(k).get("suggestion_id").equalsIgnoreCase("")||
                  Doc_Patient_Details.physuggestionslist.get(k).get("suggestion_value").equalsIgnoreCase("")){

            val=-1;
          }

        }
        if(val==0){
          Doc_Patient_Details.addphysuggestions(Doc_Patient_Details.data_list_one,"add");
          Doc_Patient_Details.phy_adapter.notifyDataSetChanged();
        }else{
          StoredObjects.ToastMethod("Please save Physical Examinations Details",activity);
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
        for(int k=0;k<Doc_Patient_Details.physuggestionslist.size();k++){
          if(Doc_Patient_Details.physuggestionslist.get(k).get("suggestion_id").equalsIgnoreCase("")||
                  Doc_Patient_Details.physuggestionslist.get(k).get("suggestion_value").equalsIgnoreCase("")){

            val=-1;
          }

        }
        if(val==0){

          String physuggestionsval="";
          JSONArray PhysicalsugArray = new JSONArray();
          JSONObject jsonObject1 = null;
          for (int i= 0;i<Doc_Patient_Details.physuggestionslist.size();i++) {
            try {
              jsonObject1 = new JSONObject();
              jsonObject1.put("suggestion_id", Doc_Patient_Details.physuggestionslist.get(i).get("suggestion_id"));
              jsonObject1.put("suggestion_value",  Doc_Patient_Details.physuggestionslist.get(i).get("suggestion_value"));
              jsonObject1.put("examination_id",  Doc_Patient_Details.physuggestionslist.get(i).get("examination_id"));
              PhysicalsugArray.put(jsonObject1);

            } catch (JSONException e) {
              e.printStackTrace();
            }
          }

          physuggestionsval=PhysicalsugArray.toString();

          Doc_Patient_Details.setUpdate_data("physical",physuggestionsval,"","");


          dialog.dismiss();
        }else{
          StoredObjects.ToastMethod("Please fill Physical Examinations Details",activity);
        }


      }
    });

    dialog.show();
  }

 /* private void medicationpopup(final Activity activity) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.doc_medication);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomButton search_button = (CustomButton) dialog.findViewById(R.id.search_button);
    ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);

    // CustomEditText tablets_dropdn=dialog.findViewById(R.id.tablets_dropdn);


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

        dialog.dismiss();
      }
    });

    dialog.show();
  }*/

 /* private void medicationone_popup(final Activity activity) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.doc_medication_one);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);
    CustomButton search_button = (CustomButton) dialog.findViewById(R.id.search_button);
    RecyclerView medication_recyclerview = (RecyclerView) dialog.findViewById(R.id.medication_recyclerview);

    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
    medication_recyclerview.setLayoutManager(linearLayoutManager);

    medication_recyclerview.setAdapter(new HashMapRecycleviewadapter(activity, Doc_Patient_Details.medications_list, "medication", medication_recyclerview, R.layout.doc_medication_one_listitem));


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

        dialog.dismiss();
      }
    });

    dialog.show();
  }
*/
  private void Diagnosepopup(final Activity activity) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.diagnose_suggestion);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomButton search_button = (CustomButton) dialog.findViewById(R.id.search_button);
    ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);

    LinearLayout addtest_lay = (LinearLayout) dialog.findViewById(R.id.addtest_lay);
    RecyclerView diagnose_suggest_recyclerview = (RecyclerView) dialog.findViewById(R.id.diagnose_suggest_recyclerview);

    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
    diagnose_suggest_recyclerview.setLayoutManager(linearLayoutManager);

    Doc_Patient_Details.test_adapter=new HashMapRecycleviewadapter(activity, Doc_Patient_Details.testadd_list, "diagnose_suggest", diagnose_suggest_recyclerview, R.layout.diagnosis_suggestion_item);
    diagnose_suggest_recyclerview.setAdapter(Doc_Patient_Details.test_adapter);


    addtest_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        int val=0;
        for(int k=0;k<Doc_Patient_Details.testadd_list.size();k++){
          if(Doc_Patient_Details.testadd_list.get(k).get("referredLab").equalsIgnoreCase("")||
                  Doc_Patient_Details.testadd_list.get(k).get("suggestedTest").equalsIgnoreCase("")){

            val=-1;
          }

        }
        if(val==0){
          Doc_Patient_Details.testlist();
          Doc_Patient_Details.test_adapter.notifyDataSetChanged();
        }else{
          StoredObjects.ToastMethod("Please save Test Details",activity);
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
            for(int k=0;k<Doc_Patient_Details.testadd_list.size();k++){
              if(Doc_Patient_Details.testadd_list.get(k).get("referredLab").equalsIgnoreCase("")||
                      Doc_Patient_Details.testadd_list.get(k).get("suggestedTest").equalsIgnoreCase("")){

                val=-1;
              }

            }
             if(val==0){

              String physuggestionsval="";
              JSONArray PhysicalsugArray = new JSONArray();
              JSONObject jsonObject1 = null;
              for (int i= 0;i<Doc_Patient_Details.testadd_list.size();i++) {
                try {
                  jsonObject1 = new JSONObject();
                  jsonObject1.put("referredLab", Doc_Patient_Details.testadd_list.get(i).get("referredLab"));
                  jsonObject1.put("suggestedTest",  Doc_Patient_Details.testadd_list.get(i).get("suggestedTest"));

                  PhysicalsugArray.put(jsonObject1);

                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }

              physuggestionsval=PhysicalsugArray.toString();



              Doc_Patient_Details.setUpdate_data("ref_test",physuggestionsval,"","");
              dialog.dismiss();
            }else{
              StoredObjects.ToastMethod("Please fill Test Details",activity);
            }



      }
    });

    dialog.show();
  }
  private void deleteHospitalAssistantService(final Activity activity, final ArrayList<HashMap<String, String>> datalist, final int position, String assistant_id_str) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.delhospitalAssistants(RetrofitInstance.hospital_delete_assistant,assistant_id_str ,StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("del_id", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Deleted successfully", activity);
              datalist.remove(position);
              H_Assistant.adapter.notifyDataSetChanged();
              H_Assistant.updatelay(datalist);
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
  private void deleteHospitalService(final Activity activity, final ArrayList<HashMap<String, String>> datalist, final int position, String hospital_id) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.delhospital(RetrofitInstance.delete_hospital,hospital_id ,StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("del_id", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Delete Hospital successfully", activity);
              datalist.remove(position);
              //TL_Hospitals.tl_hospital_recyler.removeViewAt(position);
              TL_Hospitals.adapter.notifyDataSetChanged();
              TL_Hospitals.updatelay(datalist);
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

  private void delFranchiseeListService(final Activity activity, String user_id_str, final ArrayList<HashMap<String, String>> datalist, final int position) {

    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    String method="";
    if (StoredObjects.UserType.equalsIgnoreCase("Franchisee")) {
      method=RetrofitInstance.delete_subfranchisee;
    } else {
      method=RetrofitInstance.delete_franchise;
    }
    api.delFranchiseeList(method, StoredObjects.UserId, StoredObjects.UserRoleId, user_id_str).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("del_id", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Deleted successfully", activity);
              datalist.remove(position);
             // TL_Franchisee_List.tl_franchises_recyler.removeViewAt(position);
              TL_Franchisee_List.adapter.notifyDataSetChanged();
              TL_Franchisee_List.updatelay(datalist);

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

  private void deleteMarExecService(final Activity activity, String user_id_str, final ArrayList<HashMap<String, String>> datalist, final int position) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.delMarketingExecutives(RetrofitInstance.delete_marekting_executives, StoredObjects.UserId, StoredObjects.UserRoleId, user_id_str).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("del_id", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Deleted successfully", activity);
              datalist.remove(position);
             // Marketing_Exicutive.marketing_exicutive_recyler.removeViewAt(position);
              Marketing_Exicutive.adapter.notifyDataSetChanged();
              Marketing_Exicutive.updatelay(datalist);
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

  private void deletePatientService(final Activity activity, String patient_id_str, final ArrayList<HashMap<String ,String >> datalist, final int position) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.delPatient(RetrofitInstance.delete_patient, StoredObjects.UserId, StoredObjects.UserRoleId,patient_id_str ).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("del_id", "response:" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Deleted successfully", activity);
              datalist.remove(position);
             // TL_Patients.f_patient_recyler.removeViewAt(position);
              TL_Patients.adapter.notifyDataSetChanged();
              TL_Patients.updatelay(datalist);
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

  private void deleteMemberService(final Activity activity, final ArrayList<HashMap<String, String>> members_list, final int position, String patient_id_str ) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.delMember(RetrofitInstance.patient_delete_member,patient_id_str,StoredObjects.UserId,StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("del_id", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Deleted successfully", activity);
              members_list.remove(position);
             // P_Sub_Member.submember_recyler.removeViewAt(position);
              P_Sub_Member.adapter.notifyDataSetChanged();

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
  private void leadDeleteService(final Activity activity, String delete_lead_method, final ArrayList<HashMap<String, String>> datalist, final int position, String user_id_str) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.deleteLead(delete_lead_method, StoredObjects.UserId, StoredObjects.UserRoleId, user_id_str).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("del_id", "response:" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Deleted successfully", activity);
              datalist.remove(position);
              //Franchisee_Details.f_patient_recyler.removeViewAt(position);
              Franchisee_Details.adapter.notifyDataSetChanged();
              Franchisee_Details.updatelay(datalist);
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
  public static ArrayList<HashMap<String, String>> p_data_list = new ArrayList<>();
  private void SearchPatientService(final Activity activity, final String mobile_str, String doctor_id) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.SearchPatient(RetrofitInstance.search_patient,doctor_id, mobile_str).enqueue(new Callback<ResponseBody>() {
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
              p_data_list.clear();
              p_data_list = JsonParsing.GetJsonData(results);
              fragmentcalling(new Add_Appointment_One());

            } else {
              p_data_list.clear();
              StoredObjects.p_mobilenum=mobile_str;
              AddPatientpopup(activity);
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












  private void AddPatientpopup(final Activity activity) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.logooutpopup );
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
    Button ok_btn = (Button)dialog.findViewById(R.id.ok_btn);
    Button cancel_btn = (Button)dialog.findViewById(R.id.cancel_btn);
    ImageView canclimg = (ImageView)dialog.findViewById(R.id.canclimg);
    TextView logout_txt = (TextView)dialog.findViewById( R.id.logout_txt );
    TextView exit_txt = (TextView)dialog.findViewById( R.id.exit_txt );

    logout_txt.setText("Patient not found\nDo you want to Add Patient?");


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



    ok_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        fragmentcalling(new Add_Appointment_Two());
        dialog.dismiss();
      }
    });


    cancel_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        dialog.dismiss();
      }
    });

    dialog.show();
  }

  private void assessmentpopup(final Activity activity) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.assessment_and_plan);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomButton search_button = (CustomButton) dialog.findViewById(R.id.search_button);
    final EditText assess_plan_edtxt = (EditText) dialog.findViewById(R.id.assess_plan_edtxt);
    final EditText assessment_plan_pat_adv = (EditText) dialog.findViewById(R.id.assessment_plan_pat_adv);
    final EditText assessment_plan_dr_adv = (EditText) dialog.findViewById(R.id.assessment_plan_dr_adv);
    ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);


    assess_plan_edtxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
    assess_plan_edtxt.setRawInputType(InputType.TYPE_CLASS_TEXT);

    assessment_plan_pat_adv.setImeOptions(EditorInfo.IME_ACTION_DONE);
    assessment_plan_pat_adv.setRawInputType(InputType.TYPE_CLASS_TEXT);

    assessment_plan_dr_adv.setImeOptions(EditorInfo.IME_ACTION_DONE);
    assessment_plan_dr_adv.setRawInputType(InputType.TYPE_CLASS_TEXT);

    try {

      assess_plan_edtxt.setText(Doc_Patient_Details.update_data.get(0).get("assessment_plan"));
      assessment_plan_pat_adv.setText(Doc_Patient_Details.update_data.get(0).get("patient_advice"));
      assessment_plan_dr_adv.setText(Doc_Patient_Details.update_data.get(0).get("doctors_note"));

    }catch (Exception e){

    }
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

        String assessemnet=assess_plan_edtxt.getText().toString().trim();
        String patientadvi=assessment_plan_pat_adv.getText().toString().trim();
        String doctoradvise=assessment_plan_dr_adv.getText().toString().trim();

        if(StoredObjects.inputValidation(assess_plan_edtxt,"Please enter Assessment & Plan",activity)){
          dialog.dismiss();
          Doc_Patient_Details.setUpdate_data("assessment_plan",assessemnet,patientadvi,doctoradvise);

        }


      }
    });

    dialog.show();
  }



  ArrayList<HashMap<String, String>> brand_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> d_brand_list = new ArrayList<>();


  /*private void medicationpopup(final Activity activity) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.doc_medication);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomButton search_button = (CustomButton) dialog.findViewById(R.id.search_button);
    ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);
    RecyclerView doc_medication_rv = (RecyclerView) dialog.findViewById(R.id.doc_medication_rv);
    LinearLayout addmedical_lay = (LinearLayout) dialog.findViewById(R.id.addmedical_lay);

    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
    doc_medication_rv.setLayoutManager(linearLayoutManager);
    Doc_Patient_Details.medi_adapter=new HashMapRecycleviewadapter(activity, Doc_Patient_Details.medications_list, "medication_view", doc_medication_rv, R.layout.doc_medication_listitem);
    doc_medication_rv.setAdapter( Doc_Patient_Details.medi_adapter);

    addmedical_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Doc_Patient_Details.medicationlist();
        Doc_Patient_Details.medi_adapter.notifyDataSetChanged();
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
        for(int k=0;k<Doc_Patient_Details.medications_list.size();k++){
          if(Doc_Patient_Details.medications_list.get(k).get("brand_id").equalsIgnoreCase("")&&
                  Doc_Patient_Details.medications_list.get(k).get("brand_name").equalsIgnoreCase("")
          &&Doc_Patient_Details.medications_list.get(k).get("molecule_id").equalsIgnoreCase("")&&
                  Doc_Patient_Details.medications_list.get(k).get("molecule").equalsIgnoreCase("")&&
                  Doc_Patient_Details.medications_list.get(k).get("no_of_times").equalsIgnoreCase("")&&
                  Doc_Patient_Details.medications_list.get(k).get("intake").equalsIgnoreCase("")&&
                  Doc_Patient_Details.medications_list.get(k).get("no_of_days").equalsIgnoreCase("")&&
                  Doc_Patient_Details.medications_list.get(k).get("dose").equalsIgnoreCase("")){

          }else{
            val=val+1;
          }

        }
        if(val==Doc_Patient_Details.medications_list.size()){

          String physuggestionsval="";
          String brandIds="";
          String moleculeids="";
          JSONArray PhysicalsugArray = new JSONArray();
          JSONObject jsonObject1 = null;
          for (int i= 0;i<Doc_Patient_Details.medications_list.size();i++) {
            try {
              jsonObject1 = new JSONObject();
              jsonObject1.put("brand_id", Doc_Patient_Details.medications_list.get(i).get("brand_id"));
              jsonObject1.put("brand_name", Doc_Patient_Details.medications_list.get(i).get("brand_name"));
              jsonObject1.put("molecule_id", Doc_Patient_Details.medications_list.get(i).get("molecule_id"));
              jsonObject1.put("molecule", Doc_Patient_Details.medications_list.get(i).get("molecule"));
              jsonObject1.put("no_of_times", Doc_Patient_Details.medications_list.get(i).get("no_of_times"));
              jsonObject1.put("intake", Doc_Patient_Details.medications_list.get(i).get("intake"));
              jsonObject1.put("no_of_days", Doc_Patient_Details.medications_list.get(i).get("no_of_days"));
              jsonObject1.put("dose", Doc_Patient_Details.medications_list.get(i).get("dose"));
              jsonObject1.put("remarks", Doc_Patient_Details.medications_list.get(i).get("remarks"));

              if(i==Doc_Patient_Details.medications_list.size()-1){
                brandIds=brandIds+Doc_Patient_Details.medications_list.get(i).get("brand_id");
              }else{
                brandIds=brandIds+Doc_Patient_Details.medications_list.get(i).get("brand_id")+",";
              }

              if(i==Doc_Patient_Details.medications_list.size()-1){
                moleculeids=moleculeids+Doc_Patient_Details.medications_list.get(i).get("molecule_id")+":"+Doc_Patient_Details.medications_list.get(i).get("molecule");
              }else{
                moleculeids=moleculeids+Doc_Patient_Details.medications_list.get(i).get("molecule_id")+":"+Doc_Patient_Details.medications_list.get(i).get("molecule")+",";
              }
              PhysicalsugArray.put(jsonObject1);
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }

          physuggestionsval=PhysicalsugArray.toString();

          Doc_Patient_Details.setUpdate_data("medication",physuggestionsval,brandIds,moleculeids);


          dialog.dismiss();
        }else{
          StoredObjects.ToastMethod("Please fill Physical Examinations Details",activity);
        }

        dialog.dismiss();
      }
    });

    dialog.show();
  }
*/
  private void alternateBrandService(final Activity activity) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.alternateBrand(RetrofitInstance.search_brand,"8573").enqueue(new Callback<ResponseBody>() {
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
              d_brand_list.clear();
              d_brand_list = JsonParsing.GetJsonData(results);
              brand_list.clear();
              for (int k = 0; k < d_brand_list.size(); k++) {
                HashMap<String, String> dumpData_update = new HashMap<String, String>();
                dumpData_update.put("BrandId", d_brand_list.get(k).get("BrandId"));
                dumpData_update.put("Brand", StoredObjects.stripHtml(d_brand_list.get(k).get("Brand")) + "");
                dumpData_update.put("MoleculeId", d_brand_list.get(k).get("MoleculeId"));
                dumpData_update.put("Molecule", StoredObjects.stripHtml(d_brand_list.get(k).get("Molecule")) + "");
                dumpData_update.put("is_viewed", "No");
                brand_list.add(dumpData_update);
              }


            } else {
              brand_list.clear();
              d_brand_list.clear();

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
  private void brandService(final Activity activity,final String searchtext) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.searchBrand(RetrofitInstance.search_brand,searchtext).enqueue(new Callback<ResponseBody>() {
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
              d_brand_list.clear();
              d_brand_list = JsonParsing.GetJsonData(results);


            } else {
              brand_list.clear();
              d_brand_list.clear();
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

  private void UnblockConfirmationpopup(final Activity activity,final ArrayList<HashMap<String, String>> datalist,final int position, final String type) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.logooutpopup );
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
    Button ok_btn = (Button)dialog.findViewById(R.id.ok_btn);
    Button cancel_btn = (Button)dialog.findViewById(R.id.cancel_btn);
    ImageView canclimg = (ImageView)dialog.findViewById(R.id.canclimg);
    TextView logout_txt = (TextView)dialog.findViewById( R.id.logout_txt );
    TextView exit_txt = (TextView)dialog.findViewById( R.id.exit_txt );

    logout_txt.setText("Do you want to UnBlock the selected Doctor ?");


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



    ok_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String id=datalist.get(position).get("id");
        UnBlockService(activity,datalist,position,id);


        dialog.dismiss();
      }
    });


    cancel_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        dialog.dismiss();
      }
    });

    dialog.show();
  }

  private void DeleteConfirmationpopup(final Activity activity,final ArrayList<HashMap<String, String>> datalist,final int position, final String type) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.logooutpopup );
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout)dialog.findViewById(R.id.cancel_lay);
    Button ok_btn = (Button)dialog.findViewById(R.id.ok_btn);
    Button cancel_btn = (Button)dialog.findViewById(R.id.cancel_btn);
    ImageView canclimg = (ImageView)dialog.findViewById(R.id.canclimg);
    TextView logout_txt = (TextView)dialog.findViewById( R.id.logout_txt );
    TextView exit_txt = (TextView)dialog.findViewById( R.id.exit_txt );

    logout_txt.setText("Do you want to Delete this Record ?");


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



    ok_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if(type.equalsIgnoreCase("d_assistant")){
          String assistant_id_str = datalist.get(position).get("assistant_id");
          deleteAssistantService(activity,datalist,position,assistant_id_str);

        }else  if(type.equalsIgnoreCase("h_assistant")){
          String assistant_id_str = datalist.get(position).get("assistant_id");
          deleteHospitalAssistantService(activity,datalist,position,assistant_id_str);

        }else if(type.equalsIgnoreCase("tl_hospitals")){
          String hospital_id_str = datalist.get(position).get("hospital_id");
          deleteHospitalService(activity,datalist,position,hospital_id_str);



        }else if(type.equalsIgnoreCase("tl_franchises_one")){
          String user_id_str = datalist.get(position).get("user_id");
          delFranchiseeListService(activity, user_id_str, datalist, position);

        }else if(type.equalsIgnoreCase("marketing_exicutiveone")){
          String user_id_str = datalist.get(position).get("user_id");
          delFranchiseeListService(activity, user_id_str, datalist, position);

        }else if(type.equalsIgnoreCase("marketing_exicutive")){
          String user_id_str = datalist.get(position).get("user_id");
          deleteMarExecService(activity, user_id_str, datalist, position);

        }else if(type.equalsIgnoreCase("p_submember")){
          String patient_id_str=datalist.get(position).get("patient_id");
          deleteMemberService(activity,datalist,position,patient_id_str);



        }else if(type.equalsIgnoreCase("f_patient_one")){
          String patient_id_str = datalist.get(position).get("patient_id");
          deletePatientService(activity,patient_id_str,datalist,position);
        }
        else if(type.equalsIgnoreCase("f_hospital_five")){
          String user_id_str = datalist.get(position).get("lead_id");
          if (StoredObjects.tab_type.equalsIgnoreCase("Hospital")) {

            leadDeleteService(activity, RetrofitInstance.delete_hospital_lead, datalist, position, user_id_str);
          } else if (StoredObjects.tab_type.equalsIgnoreCase("Doctor")) {

            leadDeleteService(activity, RetrofitInstance.delete_doctor_lead, datalist, position, user_id_str);
          } else {

            leadDeleteService(activity, RetrofitInstance.delete_patient_lead, datalist, position, user_id_str);
          }
        }else if(type.equalsIgnoreCase("patientlist_history")){
          String del_id= datalist.get(position).get("suggestion_id");
          delPhysicalSuggestions(activity,del_id,position,datalist);
        }else if(type.equalsIgnoreCase("del_add_phy")){
          updatedata(datalist,datalist.get(position),position,"1","add_physical_remove");
        }else if(type.equalsIgnoreCase("p_del_add_phy")){
          updatedata(datalist,datalist.get(position),position,"1","pat_add_physical_remove");
        }else if(type.equalsIgnoreCase("testsugg_remove")){
          updatedata(datalist,datalist.get(position),position,"1","testsugg_remove");
        }

        else if(type.equalsIgnoreCase("medication_view_remove")){
          updatedata(datalist,datalist.get(position),position,"1","medication_view_remove");

        }  else if(type.equalsIgnoreCase("diagnose_suggest")){
          updatedata(datalist,datalist.get(position),position,"1","diagnose_suggest");

        }




        dialog.dismiss();
      }
    });


    cancel_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        dialog.dismiss();
      }
    });

    dialog.show();
  }

  private void UnBlockService(final Activity activity, final ArrayList<HashMap<String, String>> datalist, final int position,String id) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.UnBlockDoctor(RetrofitInstance.unblock_doctor ,StoredObjects.UserId, StoredObjects.UserRoleId,id).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("del_id", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Unblocked successfully", activity);
              datalist.remove(position);
              //Block_Doctor.blockdoctor_recycle.removeViewAt(position);
              Block_Doctor.adapter.notifyDataSetChanged();
              Block_Doctor.updatelay(datalist);
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

  private void assigndoctorpopup(final Activity activity,final ArrayList<HashMap<String, String>> datalist,final int position) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.brand_suggestion_popup);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout b_cancel_lay = (LinearLayout) dialog.findViewById(R.id.b_cancel_lay);
    CustomButton b_save_button = (CustomButton) dialog.findViewById(R.id.b_save_button);
   RecyclerView brand_rv = (RecyclerView) dialog.findViewById(R.id.brand_rv);

    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
    brand_rv.setLayoutManager(linearLayoutManager);
    if(StoredObjects.page_type.equalsIgnoreCase("doc_patient_details")){

      Doc_Patient_Details.brand_adapter = new HashMapRecycleviewadapter(activity, brand_list, "brand_suggestions", brand_rv, R.layout.brand_suggestion_listitem);
      brand_rv.setAdapter( Doc_Patient_Details.brand_adapter);

    }else{
      P_Add_Prescription.brand_adapter = new HashMapRecycleviewadapter(activity, brand_list, "brand_suggestions", brand_rv, R.layout.brand_suggestion_listitem);
      brand_rv.setAdapter( P_Add_Prescription.brand_adapter);
    }



    b_cancel_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        dialog.dismiss();
      }
    });


    b_save_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String molecule_id="";
        String brand_id="";
        String molecule="";
        String brand="";
        for(int k=0;k<brand_list.size();k++){
          if(brand_list.get(k).get("is_viewed").equalsIgnoreCase("Yes")){
            brand_id=brand_list.get(k).get("BrandId");
            molecule_id=brand_list.get(k).get("MoleculeId");
            molecule=brand_list.get(k).get("Molecule");
            brand=brand_list.get(k).get("Brand");

          }
        }
        if(brand_id.equalsIgnoreCase("")){

          StoredObjects.ToastMethod("Please select Brand",activity);
        }else{
          dialog.dismiss();

        }
        brand_updatedata(datalist,datalist.get(position),position,brand,molecule,brand_id,molecule_id,"medication_brand");

      }
    });

    dialog.show();
  }

  public  String getupdtedvalue(RadioButton cb1,RadioButton cb2){
    String value="";

    if(cb1.isChecked()&&cb2.isChecked()){
      value="After Food,Before Food";
    }else if(cb1.isChecked()&&(!cb2.isChecked())){
      value="After Food";
    }else if((!cb1.isChecked())&&cb2.isChecked()){
      value="Before Food";
    }else if((!cb1.isChecked())&&(!cb2.isChecked())){
      value="";
    }else{
      value="";
    }

    return value;
  }
  public  String getupdtedvalue(CheckBox cb1,CheckBox cb2,CheckBox cb3){
    String value="";

    if(cb1.isChecked()&&cb2.isChecked()&&cb3.isChecked()){
      value="Morning,Afternoon,Evening";
    }else if(cb1.isChecked()&&cb2.isChecked()&&(!cb3.isChecked())){
      value="Morning,Afternoon";
    }else if(cb1.isChecked()&&(!cb2.isChecked())&&cb3.isChecked()){
      value="Morning,Evening";
    }else if((!cb1.isChecked())&&cb2.isChecked()&&cb3.isChecked()){
      value="Afternoon,Evening";
    }else if(cb1.isChecked()&&(!cb2.isChecked())&&(!cb3.isChecked())){
      value="Morning";
    }else if((!cb1.isChecked())&&cb2.isChecked()&&(!cb3.isChecked())){
      value="Afternoon";
    }else if((!cb1.isChecked())&&(!cb2.isChecked())&&cb3.isChecked()){
      value="Evening";
    }else{
      value="";
    }

    return value;
  }
  private void Imagepickingpopup(final Activity activity, ArrayList<HashMap<String, String>> datalist, int position) {

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

        captureImage();

        dialog.dismiss();
      }
    });

    pickglry_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, 2);


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

    Uri fileUri = CameraUtils.getOutputMediaFileUri(activity, file);

    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

    // start the image capture Intent
    activity.startActivityForResult(intent, CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
  }

  private Uri picUri;

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //user is returning from capturing an image using the camera
    onActivityResult(requestCode, resultCode, data);
    if (requestCode == CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        // Refreshing the gallery
        CameraUtils.refreshGallery(activity, CameraUtils.imageStoragePath);

        try {
          f_new = createNewFile("CROP_");
          try {
            f_new.createNewFile();
          } catch (IOException ex) {
            Log.e("io", ex.getMessage());
          }


          //Photo_SHowDialog(SignUp.this(),f_new,imageStoragePath,myBitmap);
          StoredObjects.LogMethod("", "imagepathexpection:--" + CameraUtils.imageStoragePath);
          imageupload(activity, CameraUtils.imageStoragePath);
        } catch (Exception e) {
          e.printStackTrace();
          StoredObjects.LogMethod("", "imagepathexpection:--" + e);

        }
        // successfully captured the image
        // display it in image view
        // Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

      } else if (resultCode == RESULT_CANCELED) {
        // user cancelled Image capture
        Toast.makeText(activity,
                "User cancelled image capture", Toast.LENGTH_SHORT)
                .show();
      } else {
        // failed to capture image
        Toast.makeText(activity,
                "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                .show();
      }
    } else if (requestCode == 2) {

      StoredObjects.LogMethod("resultcode", "result code" + resultCode);
      if (resultCode == RESULT_OK) {
        Uri selectedImage = data.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = activity.getContentResolver().query(selectedImage, filePath, null, null, null);
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
            imageupload(activity, picturePath);
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
        Toast.makeText(activity,
                "User cancelled image picking", Toast.LENGTH_SHORT)
                .show();
      } else {
        // failed to capture image
        Toast.makeText(activity,
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
        Log.d(activity.getClass().getName(), newDirectory.getAbsolutePath() + " directory created");
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

      File file = new File(activity.getFilesDir(), "UploadImages"
              + new Random().nextInt() + ".png");

      FileOutputStream out;
      int currentAPIVersion = Build.VERSION.SDK_INT;
      if (currentAPIVersion > Build.VERSION_CODES.M) {
        out = activity.openFileOutput(file.getName(),
                Context.MODE_PRIVATE);
      } else {
        out = activity.openFileOutput(file.getName(),
                Context.MODE_WORLD_READABLE);
      }

      mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
      out.flush();
      out.close();
      //get absolute path
      new Compressor(activity)
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
    if (InterNetChecker.isNetworkAvailable(activity)) {

      File file = new File(realPath);

      try {
        //postFile(realPath, RetrofitInstance.BASEURL + "app/index.php", file.getName());
          new ImageuploadTask().execute(realPath, file.getName());
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {

      StoredObjects.ToastMethod(activity.getResources().getString(R.string.nointernet),activity);
    }


  }
    public class ImageuploadTask extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomProgressbar.Progressbarshow(activity);
        }

        @Override
        protected String doInBackground(String... params) {

            String res = null;
            try {


                StrictMode.ThreadPolicy policy = new StrictMode.
                        ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("method", RetrofitInstance.upload_file)
                        .addFormDataPart("uploaded_file", params[1],
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        new File(params[0])))
                        .build();
                Request request = new Request.Builder()
                        .url(RetrofitInstance.IMAGEUPLOADURL)
                        .method("POST", body)
                        .build();

                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                Log.e("TAG", "Response : " + res);
                return res;

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e("TAG", "Error: " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
            }


            return res;

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            CustomProgressbar.Progressbarcancel(activity);

            if (response != null) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        String file_name_str = jsonObject.getString("file_name");
                        updatedata(P_AddTestSuggestions.testsuggestionlist,P_AddTestSuggestions.testsuggestionlist.get(pos),
                                pos,file_name_str,"testsugg_file");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }
  public void postFile(String encodedImage, String postUrl, String fileName) {



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
        JSONObject jsonObject = new JSONObject(responseReceived);
        String file_name_scanned_copy = jsonObject.getString("file_name");
        CustomProgressbar.Progressbarcancel(activity);
        updatedata(P_AddTestSuggestions.testsuggestionlist,P_AddTestSuggestions.testsuggestionlist.get(pos),
                pos,file_name_scanned_copy,"testsugg_file");
      } else {
        CustomProgressbar.Progressbarcancel(activity);
      }
      StoredObjects.LogMethod("val", "val::" + responseReceived);
    } catch (IOException | JSONException e) {

    }


  }

  private void DoctorLeadpopup(final Activity activity, ArrayList<HashMap<String, String>> datalist, int position) {

    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.doctorlead_popup);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);


    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomRegularTextView doctor_name_txt = (CustomRegularTextView) dialog.findViewById(R.id.doctor_name_txt);
    CustomRegularTextView mobilenum_txt = (CustomRegularTextView) dialog.findViewById(R.id.mobilenum_txt);
    CustomRegularTextView appointment_date_txt = (CustomRegularTextView) dialog.findViewById(R.id.appointment_date_txt);
    CustomRegularTextView status_txt = (CustomRegularTextView) dialog.findViewById(R.id.status_txt);
    CustomRegularTextView comments_txt = (CustomRegularTextView) dialog.findViewById(R.id.comments_txt);
    CustomRegularTextView address_txt = (CustomRegularTextView) dialog.findViewById(R.id.address_txt);
    CustomButton close_btn = (CustomButton) dialog.findViewById(R.id.close_btn);

    doctor_name_txt.setText(datalist.get(position).get("name"));
    mobilenum_txt.setText(datalist.get(position).get("phone"));
    appointment_date_txt.setText(StoredObjects.convertDateformat(datalist.get(position).get("appointment_date")));
    status_txt.setText(datalist.get(position).get("status"));
    comments_txt.setText(datalist.get(position).get("comments"));
    address_txt.setText(datalist.get(position).get("address"));

    close_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog.dismiss();
      }
    });
    cancel_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });


    dialog.show();

  }

  private void HospitalLeadpopup(final Activity activity, ArrayList<HashMap<String, String>> datalist, int position) {




    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.hospitallead_popup);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomRegularTextView hospital_name_txt = (CustomRegularTextView) dialog.findViewById(R.id.hospital_name_txt);
    CustomRegularTextView mobilenum_txt = (CustomRegularTextView) dialog.findViewById(R.id.mobilenum_txt);
    CustomRegularTextView appointment_date_txt = (CustomRegularTextView) dialog.findViewById(R.id.appointment_date_txt);
    CustomRegularTextView status_txt = (CustomRegularTextView) dialog.findViewById(R.id.status_txt);
    CustomRegularTextView comments_txt = (CustomRegularTextView) dialog.findViewById(R.id.comments_txt);
    CustomRegularTextView address_txt = (CustomRegularTextView) dialog.findViewById(R.id.address_txt);
    CustomButton close_btn = (CustomButton) dialog.findViewById(R.id.close_btn);

    hospital_name_txt.setText(datalist.get(position).get("name"));
    mobilenum_txt.setText(datalist.get(position).get("phone"));
    appointment_date_txt.setText(StoredObjects.convertDateformat(datalist.get(position).get("appointment_date")));
    status_txt.setText(datalist.get(position).get("status"));
    comments_txt.setText(datalist.get(position).get("comments"));
    address_txt.setText(datalist.get(position).get("address"));



    close_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog.dismiss();
      }
    });
    cancel_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });


    dialog.show();

  }

  private void PatientLeadpopup(final Activity activity, ArrayList<HashMap<String, String>> datalist, int position) {



    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.patientlead_popup);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomRegularTextView patient_name_txt = (CustomRegularTextView) dialog.findViewById(R.id.patient_name_txt);
    CustomRegularTextView mobilenum_txt = (CustomRegularTextView) dialog.findViewById(R.id.mobilenum_txt);
    CustomRegularTextView appointment_date_txt = (CustomRegularTextView) dialog.findViewById(R.id.appointment_date_txt);
    CustomRegularTextView status_txt = (CustomRegularTextView) dialog.findViewById(R.id.status_txt);
    CustomRegularTextView comments_txt = (CustomRegularTextView) dialog.findViewById(R.id.comments_txt);
    CustomRegularTextView address_txt = (CustomRegularTextView) dialog.findViewById(R.id.address_txt);
    CustomButton close_btn = (CustomButton) dialog.findViewById(R.id.close_btn);


    patient_name_txt.setText(datalist.get(position).get("name"));
    mobilenum_txt.setText(datalist.get(position).get("phone"));
    appointment_date_txt.setText(StoredObjects.convertDateformat(datalist.get(position).get("appointment_date")));
    status_txt.setText(datalist.get(position).get("status"));
    comments_txt.setText(datalist.get(position).get("comments"));
    address_txt.setText(datalist.get(position).get("address"));

    close_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog.dismiss();
      }
    });
    cancel_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });


    dialog.show();

  }

  /*private void ViewDoctorpopup(final Activity activity, ArrayList<HashMap<String, String>> datalist, int position) {

    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.view_doctor_popup);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);



    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);

    CustomRegularTextView dr_name_txt = (CustomRegularTextView) dialog.findViewById(R.id.dr_name_txt);
    CustomRegularTextView specialization_txt = (CustomRegularTextView) dialog.findViewById(R.id.specialization_txt);
    CustomRegularTextView email_txt = (CustomRegularTextView) dialog.findViewById(R.id.email_txt);
    CustomRegularTextView mobilenum_txt = (CustomRegularTextView) dialog.findViewById(R.id.mobilenum_txt);
    CustomRegularTextView statebord_txt = (CustomRegularTextView) dialog.findViewById(R.id.statebord_txt);
    CustomRegularTextView doc_regnum_txt = (CustomRegularTextView) dialog.findViewById(R.id.doc_regnum_txt);
    CustomRegularTextView yearofreg_txt = (CustomRegularTextView) dialog.findViewById(R.id.yearofreg_txt);
    CustomRegularTextView qualification_txt = (CustomRegularTextView) dialog.findViewById(R.id.qualification_txt);
    CustomRegularTextView oth_qualification_txt = (CustomRegularTextView) dialog.findViewById(R.id.oth_qualification_txt);
    CustomRegularTextView extra_qualification_txt = (CustomRegularTextView) dialog.findViewById(R.id.extra_qualification_txt);
    CustomRegularTextView address_txt = (CustomRegularTextView) dialog.findViewById(R.id.address_txt);
    CustomRegularTextView frmdays_txt = (CustomRegularTextView) dialog.findViewById(R.id.frmdays_txt);
    CustomRegularTextView todays_txt = (CustomRegularTextView) dialog.findViewById(R.id.todays_txt);
    CustomRegularTextView slotone_txt = (CustomRegularTextView) dialog.findViewById(R.id.slotone_txt);
    CustomRegularTextView slottwo_txt = (CustomRegularTextView) dialog.findViewById(R.id.slottwo_txt);
    CustomRegularTextView slotthree_txt = (CustomRegularTextView) dialog.findViewById(R.id.slotthree_txt);
    CustomRegularTextView slotfour_txt = (CustomRegularTextView) dialog.findViewById(R.id.slotfour_txt);
    CustomRegularTextView custom_txt = (CustomRegularTextView) dialog.findViewById(R.id.custom_txt);
    CustomRegularTextView ass_assistant_txt = (CustomRegularTextView) dialog.findViewById(R.id.ass_assistant_txt);
    CustomButton close_btn = (CustomButton) dialog.findViewById(R.id.close_btn);



    dr_name_txt.setText(datalist.get(position).get("name"));
    specialization_txt.setText(datalist.get(position).get("specialization"));
    email_txt.setText(datalist.get(position).get("email"));
    mobilenum_txt.setText(datalist.get(position).get("phone"));
    statebord_txt.setText(datalist.get(position).get("state_board"));
    doc_regnum_txt.setText(datalist.get(position).get("doctor_registration_number"));
    yearofreg_txt.setText(datalist.get(position).get("year_of_registration"));
    qualification_txt.setText(datalist.get(position).get("qualification"));
    oth_qualification_txt.setText(datalist.get(position).get("other_qualification"));
    extra_qualification_txt.setText(datalist.get(position).get("extra_qualification"));
    address_txt.setText(datalist.get(position).get("address"));
    frmdays_txt.setText(datalist.get(position).get("from_days"));
    todays_txt.setText(datalist.get(position).get("to_days"));
    slotone_txt.setText(datalist.get(position).get("from_time"));
    slottwo_txt.setText(datalist.get(position).get("to_time"));
    slotthree_txt.setText(datalist.get(position).get(""));
    slotfour_txt.setText(datalist.get(position).get(""));
    custom_txt.setText(datalist.get(position).get("custom_timings"));
    ass_assistant_txt.setText(datalist.get(position).get("assistant_name"));

    cancel_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });


    dialog.show();

  }
*/

    private void H_ViewTestSuggestPopup(final Activity activity, ArrayList<HashMap<String, String>> datalist, int position) {

        final Dialog dialog = new Dialog(activity);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.h_totaltestsugg_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);



        LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);

        CustomRegularTextView enroleid_txt = (CustomRegularTextView) dialog.findViewById(R.id.enroleid_txt);
        CustomRegularTextView name_txt = (CustomRegularTextView) dialog.findViewById(R.id.name_txt);
        CustomRegularTextView lab_name_txt = (CustomRegularTextView) dialog.findViewById(R.id.lab_name_txt);
        CustomRegularTextView testname_txt = (CustomRegularTextView) dialog.findViewById(R.id.testname_txt);
        CustomRegularTextView docname_txt = (CustomRegularTextView) dialog.findViewById(R.id.docname_txt);
        CustomRegularTextView remarks_txt = (CustomRegularTextView) dialog.findViewById(R.id.remarks_txt);

        ImageView scanned_img=dialog.findViewById(R.id.scanned_img);
        CustomButton close_btn = (CustomButton) dialog.findViewById(R.id.close_btn);

        enroleid_txt.setText(datalist.get(position).get("enroll_id"));
        name_txt.setText(datalist.get(position).get("patient_name"));
        docname_txt.setText(datalist.get(position).get("name")+" ("+datalist.get(position).get("specialization")+")");
        lab_name_txt.setText(datalist.get(position).get("referred_lab"));
            testname_txt.setText(datalist.get(position).get("test_name"));
        remarks_txt.setText(datalist.get(position).get("report_details"));

      try {
        Glide.with(activity)
                .load(Uri.parse(RetrofitInstance.IMAGE_URL + datalist.get(position).get("report_image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(subfra_img);
      } catch (Exception e) {
        e.printStackTrace();

      }

        if(datalist.get(position).get("report_image").equalsIgnoreCase("")){
            scanned_img.setVisibility(View.GONE);
        }else{
            scanned_img.setVisibility(View.VISIBLE);
        }




        cancel_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    private void ViewTestSuggestPopup(final Activity activity, ArrayList<HashMap<String, String>> datalist, int position) {

    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.view_testsuggested_popup);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);



    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);

    CustomRegularTextView enroleid_txt = (CustomRegularTextView) dialog.findViewById(R.id.enroleid_txt);
    CustomRegularTextView name_txt = (CustomRegularTextView) dialog.findViewById(R.id.name_txt);
    CustomRegularTextView clinic_name_txt = (CustomRegularTextView) dialog.findViewById(R.id.clinic_name_txt);
    CustomRegularTextView lab_name_txt = (CustomRegularTextView) dialog.findViewById(R.id.lab_name_txt);
    CustomRegularTextView diagnose_name_txt = (CustomRegularTextView) dialog.findViewById(R.id.diagnose_name_txt);
    CustomRegularTextView report_date_txt = (CustomRegularTextView) dialog.findViewById(R.id.report_date_txt);
    CustomRegularTextView test_name_txt = (CustomRegularTextView) dialog.findViewById(R.id.test_name_txt);
    CustomRegularTextView scannedcopy_txt = (CustomRegularTextView) dialog.findViewById(R.id.scannedcopy_txt);
    CustomRegularTextView remarks_txt = (CustomRegularTextView) dialog.findViewById(R.id.remarks_txt);
    CustomButton close_btn = (CustomButton) dialog.findViewById(R.id.close_btn);


    enroleid_txt.setText(datalist.get(position).get("enroll_id"));
    name_txt.setText(datalist.get(position).get("patient_name"));
    clinic_name_txt.setText(datalist.get(position).get("name"));
    lab_name_txt.setText(datalist.get(position).get("referred_lab"));
    diagnose_name_txt.setText(datalist.get(position).get("diagnostic_name"));
    report_date_txt.setText(datalist.get(position).get("report_date"));
    test_name_txt.setText(datalist.get(position).get("test_name"));

    remarks_txt.setText(datalist.get(position).get("report_details"));


    cancel_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
    close_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });

    dialog.show();

  }



  private void TotalTestSuggestPopup(final Activity activity, ArrayList<HashMap<String, String>> datalist, int position) {

    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.total_testsuggested_popup);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);



    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);

    CustomRegularTextView enroleid_txt = (CustomRegularTextView) dialog.findViewById(R.id.enroleid_txt);
    CustomRegularTextView name_txt = (CustomRegularTextView) dialog.findViewById(R.id.name_txt);
    CustomRegularTextView lab_name_txt = (CustomRegularTextView) dialog.findViewById(R.id.lab_name_txt);
    CustomRegularTextView testname_txt = (CustomRegularTextView) dialog.findViewById(R.id.testname_txt);
    CustomRegularTextView diagno_name_txt = (CustomRegularTextView) dialog.findViewById(R.id.diagno_name_txt);
    CustomRegularTextView repdate_txt = (CustomRegularTextView) dialog.findViewById(R.id.repdate_txt);
    CustomRegularTextView remarks_txt = (CustomRegularTextView) dialog.findViewById(R.id.remarks_txt);
    CustomRegularTextView hsptl_name_txt = (CustomRegularTextView) dialog.findViewById(R.id.hsptl_name_txt);

    ImageView scanned_img=dialog.findViewById(R.id.scanned_img);


    CustomButton close_btn = (CustomButton) dialog.findViewById(R.id.close_btn);

    enroleid_txt.setText(datalist.get(position).get("enroll_id"));
    name_txt.setText(datalist.get(position).get("patient_name"));
    lab_name_txt.setText(datalist.get(position).get("referred_lab"));
    testname_txt.setText(datalist.get(position).get("test_name"));
    hsptl_name_txt.setText(datalist.get(position).get("clinic_name"));

    hsptl_name_txt.setText(datalist.get(position).get("clinic_name"));
    diagno_name_txt.setText(datalist.get(position).get("diagnostic_name"));
    remarks_txt.setText(datalist.get(position).get("report_details"));

    try {
      repdate_txt.setText(StoredObjects.convertfullTimeformat(datalist.get(position).get("created_at")));

    }catch (Exception e){
      repdate_txt.setText(datalist.get(position).get("created_at"));

    }
    try {
      Glide.with(activity)
              .load(Uri.parse(RetrofitInstance.IMAGE_URL + datalist.get(position).get("report_image")))
              .apply(new RequestOptions()
                      .placeholder(R.drawable.no_image)
                      .fitCenter()
                      .centerCrop())
              .into(subfra_img);
    } catch (Exception e) {
      e.printStackTrace();

    }

    if(datalist.get(position).get("report_image").equalsIgnoreCase("")){
      scanned_img.setVisibility(View.GONE);
    }else{
      scanned_img.setVisibility(View.VISIBLE);
    }

    cancel_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });

    close_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });


    dialog.show();

  }



  private void Diagnosereportpopup(final Activity activity, ArrayList<HashMap<String, String>> datalist, int position) {

    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.diagnosis_report_popup);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);



    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomRegularTextView enroleid_txt = (CustomRegularTextView) dialog.findViewById(R.id.enroleid_txt);
    CustomRegularTextView name_txt = (CustomRegularTextView) dialog.findViewById(R.id.name_txt);
    CustomRegularTextView ref_doctor_name_txt = (CustomRegularTextView) dialog.findViewById(R.id.ref_doctor_name_txt);
    CustomRegularTextView lab_name_txt = (CustomRegularTextView) dialog.findViewById(R.id.lab_name_txt);
    CustomRegularTextView diagnosis_txt = (CustomRegularTextView) dialog.findViewById(R.id.diagnosis_txt);
    CustomRegularTextView report_date_txt = (CustomRegularTextView) dialog.findViewById(R.id.report_date_txt);
    CustomRegularTextView testname_txt = (CustomRegularTextView) dialog.findViewById(R.id.testname_txt);
    CustomRegularTextView remarks_txt = (CustomRegularTextView) dialog.findViewById(R.id.remarks_txt);
    CustomButton close_btn = (CustomButton) dialog.findViewById(R.id.close_btn);



    enroleid_txt.setText(datalist.get(position).get("report_date"));
    name_txt.setText(datalist.get(position).get("name"));
    ref_doctor_name_txt.setText(datalist.get(position).get("ref_doctor_name"));
    lab_name_txt.setText(datalist.get(position).get("referred_lab"));
    report_date_txt.setText(datalist.get(position).get("diagnostic_name"));

    testname_txt.setText(datalist.get(position).get("test_name"));
    remarks_txt.setText(datalist.get(position).get("report_details"));

    cancel_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });

    close_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
    dialog.show();

  }

  private void viewHospitalPopup(final Activity activity, ArrayList<HashMap<String, String>> datalist, int position) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.view_hospital_popup);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomButton close_button = (CustomButton) dialog.findViewById(R.id.close_button);
    ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);
    CustomRegularTextView hosp_name, hosp_reg_no, hosp_mob_no, hosp_address, hosp_from_time, hosp_to_time, hospno_beds, hosp_email;
    hosp_mob_no = dialog.findViewById(R.id.hosp_mobile_num_popup);
    hosp_reg_no = dialog.findViewById(R.id.hosp_reg_num_popup);
    hosp_address = dialog.findViewById(R.id.hosp_address_popup);
    hosp_from_time = dialog.findViewById(R.id.hosp_from_time_popup);
    hosp_to_time = dialog.findViewById(R.id.hosp_to_time_popup);
    hospno_beds = dialog.findViewById(R.id.hosp_no_beds_popup);
    hosp_email = dialog.findViewById(R.id.hosp_email_popup);
    hosp_name = dialog.findViewById(R.id.hosp_name_popup);

    // CustomEditText tablets_dropdn=dialog.findViewById(R.id.tablets_dropdn);

    hospno_beds.setText(datalist.get(position).get("no_of_beds"));
    hosp_reg_no.setText(datalist.get(position).get("hospital_registration_number"));
    hosp_name.setText(datalist.get(position).get("name"));
    hosp_mob_no.setText(datalist.get(position).get("phone"));
    hosp_email.setText(datalist.get(position).get("email"));
    hosp_address.setText(datalist.get(position).get("address"));
    hosp_from_time.setText(StoredObjects.time12hrsformat(datalist.get(position).get("from_time")));
    hosp_to_time.setText(StoredObjects.time12hrsformat(datalist.get(position).get("to_time")));


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


    close_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        dialog.dismiss();
      }
    });

    dialog.show();
  }

//// next popup

  private void viewPatientPopup(final Activity activity, ArrayList<HashMap<String, String>> datalist, int position) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.view_patient_popup);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomButton close_button = (CustomButton) dialog.findViewById(R.id.close_button);
    ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);
    CustomRegularTextView patient_name, pateitn_dob, patient_aadahar_no, patient_mob_no, patient_email, patient_gender, patient_bg_popup;
    patient_name = dialog.findViewById(R.id.patient_name_popup);
    pateitn_dob = dialog.findViewById(R.id.patient_dob_popup);
    patient_aadahar_no = dialog.findViewById(R.id.patient_aadhaar_popup);
    patient_mob_no = dialog.findViewById(R.id.patient_mobile_popup);
    patient_email = dialog.findViewById(R.id.patient_email_popup);
    patient_gender = dialog.findViewById(R.id.patient_gender_popup);
    patient_bg_popup = dialog.findViewById(R.id.patient_bg_popup);

    // CustomEditText tablets_dropdn=dialog.findViewById(R.id.tablets_dropdn);

    patient_name.setText(datalist.get(position).get("name"));
    pateitn_dob.setText(datalist.get(position).get("date_of_birth"));
    patient_aadahar_no.setText(datalist.get(position).get("aadhar_number"));
    patient_mob_no.setText(datalist.get(position).get("phone"));
    patient_email.setText(datalist.get(position).get("email"));
    patient_gender.setText(datalist.get(position).get("gender"));
    //patient_bg_popup.setText(datalist.get(position).get("blood_group"));

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


    close_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        dialog.dismiss();
      }
    });

    dialog.show();
  }

//next popup

  private void viewDoctorPopup(final Activity activity, ArrayList<HashMap<String, String>> datalist, int position) {
    final Dialog dialog = new Dialog(activity);
    dialog.getWindow();
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.view_doctor_popup);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      dialog.getWindow().setElevation(20);

    }
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    LinearLayout cancel_lay = (LinearLayout) dialog.findViewById(R.id.cancel_lay);
    CustomButton close_button = (CustomButton) dialog.findViewById(R.id.close_button);
    ImageView canclimg = (ImageView) dialog.findViewById(R.id.canclimg);
    CustomRegularTextView doctor_name, doc_email_popup, doc_to_time_popup, doc_from_time_popup, doc_to_day_popup, doc_from_day_popup, doc_mobile_num_popup, doc_specialisation_popup, doc_state_board_popup, doc_reg_num_popup, doc_year_reg_popup, doc_address_popup;
    doctor_name = dialog.findViewById(R.id.doc_name_popup);
    doc_email_popup = dialog.findViewById(R.id.doc_email_popup);
    doc_specialisation_popup = dialog.findViewById(R.id.doc_specialisation_popup);
    doc_state_board_popup = dialog.findViewById(R.id.doc_state_board_popup);
    doc_reg_num_popup = dialog.findViewById(R.id.doc_reg_num_popup);
    doc_year_reg_popup = dialog.findViewById(R.id.doc_year_reg_popup);
    doc_address_popup = dialog.findViewById(R.id.doc_address_popup);
    doc_mobile_num_popup = dialog.findViewById(R.id.doc_mobile_num_popup);
    doc_from_day_popup = dialog.findViewById(R.id.doc_from_day_popup);
    doc_to_day_popup = dialog.findViewById(R.id.doc_to_day_popup);
    doc_from_time_popup = dialog.findViewById(R.id.doc_from_time_popup);
    doc_to_time_popup = dialog.findViewById(R.id.doc_to_time_popup);
    CustomRegularTextView  doc_custom_time_popup = dialog.findViewById(R.id.doc_custom_time_popup);


    // CustomEditText tablets_dropdn=dialog.findViewById(R.id.tablets_dropdn);
    doctor_name.setText(datalist.get(position).get("name"));
    doc_email_popup.setText(datalist.get(position).get("email"));
    doc_specialisation_popup.setText(datalist.get(position).get("specialization"));
    doc_state_board_popup.setText(datalist.get(position).get("state_board"));
    doc_reg_num_popup.setText(datalist.get(position).get("doctor_registration_number"));
    doc_year_reg_popup.setText(datalist.get(position).get("year_of_registration"));
    doc_address_popup.setText(datalist.get(position).get("address"));
    doc_mobile_num_popup.setText(datalist.get(position).get("phone"));
    doc_from_day_popup.setText(datalist.get(position).get("from_days"));
    doc_to_day_popup.setText(datalist.get(position).get("to_days"));
    doc_from_time_popup.setText(StoredObjects.time12hrsformat(datalist.get(position).get("from_time")));
    doc_to_time_popup.setText(StoredObjects.time12hrsformat(datalist.get(position).get("to_time")));
    doc_custom_time_popup.setText(datalist.get(position).get("custom_timings"));



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


    close_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        dialog.dismiss();
      }
    });

    dialog.show();
  }

  private void D_FromtimeListPopup(final CustomEditText prfilenme, final Activity activity,String type,final ArrayList<HashMap<String, String>> list,final int pos) {

    fromtimes_list.clear();
    fromtimeSlots_list.clear();



    if(type.equalsIgnoreCase("0")){
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
          //fromtimeslot_id_one=fromtimes_list.get(position).get("time_slot");

          d_timeslot_updatedata(list,list.get(pos),pos,fromtimes_list.get(position).get("time_slot"),getposition(fromtimes_list.get(position).get("time_slot"))+"","f_1");
          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }else if(type.equalsIgnoreCase("1")){

      t_pos_one=Integer.parseInt(list.get(pos).get("to_time_pos"));
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
          // fromtimeslot_id_two=fromtimes_list.get(position).get("time_slot");
          d_timeslot_updatedata(list,list.get(pos),pos,fromtimes_list.get(position).get("time_slot"),getposition(fromtimes_list.get(position).get("time_slot"))+"","f_2");
          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }else if(type.equalsIgnoreCase("2")){
      t_pos_two=Integer.parseInt(list.get(pos).get("to_time1_pos"));
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
          d_timeslot_updatedata(list,list.get(pos),pos,fromtimes_list.get(position).get("time_slot"),getposition(fromtimes_list.get(position).get("time_slot"))+"","f_3");
          //fromtimeslot_id_three=fromtimes_list.get(position).get("time_slot");
          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }else if(type.equalsIgnoreCase("3")){
      t_pos_three=Integer.parseInt(list.get(pos).get("to_time2_pos"));
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
          d_timeslot_updatedata(list,list.get(pos),pos,fromtimes_list.get(position).get("time_slot"),getposition(fromtimes_list.get(position).get("time_slot"))+"","f_4");
          //fromtimeslot_id_four=fromtimes_list.get(position).get("time_slot");

          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }

  }

  ArrayList<String> totimeSlots_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> totimes_list = new ArrayList<>();

  private void D_toTimeListPopup(final CustomEditText prfilenme,Activity activity,String type,final ArrayList<HashMap<String, String>> list,final int pos) {


    totimes_list.clear();
    totimeSlots_list.clear();

    if(type.equalsIgnoreCase("0")){
      pos_one=Integer.parseInt(list.get(pos).get("from_time_pos"));
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
          // totimeslot_id_one=totimes_list.get(position).get("time_slot");
          d_timeslot_updatedata(list,list.get(pos),pos,totimes_list.get(position).get("time_slot"),getposition(totimes_list.get(position).get("time_slot"))+"","t_1");

          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }else  if(type.equalsIgnoreCase("1")){
      pos_two=Integer.parseInt(list.get(pos).get("from_time1_pos"));
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
          d_timeslot_updatedata(list,list.get(pos),pos,totimes_list.get(position).get("time_slot"),getposition(totimes_list.get(position).get("time_slot"))+"","t_2");
          //totimeslot_id_two=totimes_list.get(position).get("time_slot");
          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }else  if(type.equalsIgnoreCase("2")){

      pos_three=Integer.parseInt(list.get(pos).get("from_time2_pos"));
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
          d_timeslot_updatedata(list,list.get(pos),pos,totimes_list.get(position).get("time_slot"),getposition(totimes_list.get(position).get("time_slot"))+"","t_3");
          // totimeslot_id_three=totimes_list.get(position).get("time_slot");
          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }else  if(type.equalsIgnoreCase("3")){
      pos_four=Integer.parseInt(list.get(pos).get("from_time3_pos"));
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
          d_timeslot_updatedata(list,list.get(pos),pos,totimes_list.get(position).get("time_slot"),getposition(totimes_list.get(position).get("time_slot"))+"","t_4");
          //totimeslot_id_four=totimes_list.get(position).get("time_slot");

          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }

  }


  int pos_one=0,pos_two=0,pos_three=0,pos_four=0;
  int t_pos_one=0,t_pos_two=0,t_pos_three=0;


  ArrayList<String> fromtimeSlots_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> fromtimes_list = new ArrayList<>();


  private void FromtimeListPopup(final CustomEditText prfilenme, final Activity activity,String type,final ArrayList<HashMap<String, String>> list,final int pos) {

    fromtimes_list.clear();
    fromtimeSlots_list.clear();



    if(type.equalsIgnoreCase("0")){
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
          //fromtimeslot_id_one=fromtimes_list.get(position).get("time_slot");

          timeslot_updatedata(list,list.get(pos),pos,fromtimes_list.get(position).get("time_slot"),getposition(fromtimes_list.get(position).get("time_slot"))+"","f_1");
          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }else if(type.equalsIgnoreCase("1")){

      t_pos_one=Integer.parseInt(list.get(pos).get("to_time_pos"));
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
         // fromtimeslot_id_two=fromtimes_list.get(position).get("time_slot");
          timeslot_updatedata(list,list.get(pos),pos,fromtimes_list.get(position).get("time_slot"),getposition(fromtimes_list.get(position).get("time_slot"))+"","f_2");
          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }else if(type.equalsIgnoreCase("2")){
      t_pos_two=Integer.parseInt(list.get(pos).get("to_time1_pos"));
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
          timeslot_updatedata(list,list.get(pos),pos,fromtimes_list.get(position).get("time_slot"),getposition(fromtimes_list.get(position).get("time_slot"))+"","f_3");
          //fromtimeslot_id_three=fromtimes_list.get(position).get("time_slot");
          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }else if(type.equalsIgnoreCase("3")){
      t_pos_three=Integer.parseInt(list.get(pos).get("to_time2_pos"));
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
          timeslot_updatedata(list,list.get(pos),pos,fromtimes_list.get(position).get("time_slot"),getposition(fromtimes_list.get(position).get("time_slot"))+"","f_4");
          //fromtimeslot_id_four=fromtimes_list.get(position).get("time_slot");

          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }

  }


  private void toTimeListPopup(final CustomEditText prfilenme,Activity activity,String type,final ArrayList<HashMap<String, String>> list,final int pos) {


    totimes_list.clear();
    totimeSlots_list.clear();

    if(type.equalsIgnoreCase("0")){
      pos_one=Integer.parseInt(list.get(pos).get("from_time_pos"));
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
         // totimeslot_id_one=totimes_list.get(position).get("time_slot");
          timeslot_updatedata(list,list.get(pos),pos,totimes_list.get(position).get("time_slot"),getposition(totimes_list.get(position).get("time_slot"))+"","t_1");

          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }else  if(type.equalsIgnoreCase("1")){
      pos_two=Integer.parseInt(list.get(pos).get("from_time1_pos"));
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
          timeslot_updatedata(list,list.get(pos),pos,totimes_list.get(position).get("time_slot"),getposition(totimes_list.get(position).get("time_slot"))+"","t_2");
          //totimeslot_id_two=totimes_list.get(position).get("time_slot");
          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }else  if(type.equalsIgnoreCase("2")){

      pos_three=Integer.parseInt(list.get(pos).get("from_time2_pos"));
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
          timeslot_updatedata(list,list.get(pos),pos,totimes_list.get(position).get("time_slot"),getposition(totimes_list.get(position).get("time_slot"))+"","t_3");
         // totimeslot_id_three=totimes_list.get(position).get("time_slot");
          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }else  if(type.equalsIgnoreCase("3")){
      pos_four=Integer.parseInt(list.get(pos).get("from_time3_pos"));
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
          timeslot_updatedata(list,list.get(pos),pos,totimes_list.get(position).get("time_slot"),getposition(totimes_list.get(position).get("time_slot"))+"","t_4");
          //totimeslot_id_four=totimes_list.get(position).get("time_slot");

          listPopupWindowone.dismiss();

        }
      });

      listPopupWindowone.show();
    }

  }

  public int getposition(String selval){
    int value=0;
    for (int k = 0; k < TL_AddDoctor.dummy_times_list.size(); k++) {
      if(selval.equalsIgnoreCase(TL_AddDoctor.dummy_times_list.get(k).get("time_slot"))){
        value=k;
      }

    }

    return value;
  }
  private void FromdaysListPopup(final CustomEditText prfilenme, final Activity activity,final ArrayList<HashMap<String, String>> list,final int pos) {
    final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
    listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, TL_AddDoctor.daynames_list));
    listPopupWindowone.setAnchorView(prfilenme);
    listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(TL_AddDoctor.daynames_list.get(position));

        timeslot_updatedata(list,list.get(pos),pos,TL_AddDoctor.daynames_list.get(position),TL_AddDoctor.days_list.get(position).get("id"),"d_from");
        listPopupWindowone.dismiss();

      }
    });

    listPopupWindowone.show();
  }


  private void ToDayListPopup(final CustomEditText prfilenme,Activity activity,final ArrayList<HashMap<String, String>> list,final int pos) {
    todaysname_list.clear();
    todays_list.clear();



    for (int k = 0; k < TL_AddDoctor.days_list.size(); k++) {
      if(list.get(pos).get("from_days").equalsIgnoreCase(TL_AddDoctor.days_list.get(k).get("day_name"))){
        pos_four=k;
      }

    }
    for (int k = pos_four; k < TL_AddDoctor.days_list.size(); k++) {
      todays_list.add(TL_AddDoctor.days_list.get(k));
    }


    for (int k = 0; k < todays_list.size(); k++) {
      todaysname_list.add(todays_list.get(k).get("day_name"));
    }
    final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
    listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, todaysname_list));
    listPopupWindowone.setAnchorView(prfilenme);
    listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(todaysname_list.get(position));
        timeslot_updatedata(list,list.get(pos),pos,todaysname_list.get(position),todays_list.get(position).get("id"),"d_to");

        listPopupWindowone.dismiss();

      }
    });
    listPopupWindowone.show();
  }

}
