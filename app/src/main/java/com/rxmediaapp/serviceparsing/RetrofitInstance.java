package com.rxmediaapp.serviceparsing;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

  private static Retrofit retrofit;

  public static final String BASEURL = "http://o3sa.co.in/demos/Rx/";
  public static final String IMAGEUPLOADURL = BASEURL + "app/index.php?";

  public static final String dashboard = "dashboard-counts";

  public static final String IMAGE_URL = BASEURL + "uploads/";
  public static final String device_type = "Android";
  public static final String week_days = "week-days";
  public static final String dr_specializtion = "doctor-specializations";
  public static final String time_slots = "time-slots";
  public static final String to_days = "to-days";
  public static final String to_time = "to-time";
  public static final String hospital_sign_up = "hospital-sign-up";
  public static final String doctor_sign_up = "doctor-sign-up";
  public static final String patient_sign_up = "patient-sign-up";
  public static final String login = "login";
  public static final String doctor_profile = "doctor-profile";
  public static final String doctor_assistants = "doctor-assistants";
  public static final String doctor_add_assistant = "doctor-add-assistant";
  public static final String doctor_delete_assistant = "doctor-delete-assistant";
  public static final String doctor_edit_assistant = "doctor-edit-assistatnt";
  public static final String physical_suggestions = "physical-suggestions";
  public static final String add_physical_suggestion = "add-physical-suggestion";
  public static final String del_physical_suggestion = "delete-physical-suggestion";
  public static final String edit_physical_suggestion = "edit-physical-suggestion";
  public static final String print_format = "print-format";
  public static final String assistant_profile = "assistant-profile";
  public static final String edit_assistant_profile = "edit-assistant-profile";
  public static final String edit_print_format = "edit-print-format";

  //RetrofitInstance
  public static final String hospital_profile = "hospital-profile";
  public static final String edit_hospital_profile = "edit-hospital-profile";
  public static final String edit_doctor_profile = "edit-doctor-profile";
  public static final String hospital_assistants = "hospital-assistants";
  public static final String hospital_add_assistant = "hospital-add-assistant";
  public static final String hospital_delete_assistant = "hospital-delete-assistant";
  public static final String hospital_edit_assistant = "hospital-edit-assistant";
  public static final String hospital_add_doctor = "hospital-add-doctor";
  public static final String hospitals = "hospitals";
  public static final String add_hospitals = "add-hospital";
  public static final String edit_hospitals = "edit-hospital";
  public static final String delete_hospital = "delete-hospital";
  public static final String add_doctor = "add-doctor";
  public static final String edit_doctor = "edit-doctor";

  public static final String marekting_executives = "marketing-executives";
  public static final String add_marekting_executives = "add-marketing-executive";
  public static final String edit_marketing_executives = "edit-marketing-executive";
  public static final String delete_marekting_executives = "delete-marketing-executive";
  public static final String franchisee_list = "franchisee-list";
  public static final String edit_franchisee = "edit-franchisee";
  public static final String delete_franchise = "delete-franchisee";
  public static final String add_franchisee = "add-franchisee";
  public static final String patients = "patients";
  public static final String delete_patient = "delete-patient";
  public static final String add_patient = "add-patient";
  public static final String edit_patient = "edit-patient";
  public static final String hospital_leads = "hospital-leads";
  public static final String doctor_leads = "doctor-leads";
  public static final String patient_leads = "patient-leads";
  public static final String members = "members";
  public static final String add_member = "add-member";
  public static final String patient_delete_member = "delete-member";
  public static final String patient_edit_member = "edit-member";
  public static final String add_patient_lead = "add-patient-lead";
  public static final String add_hospital_lead = "add-hospital-lead";
  public static final String add_doctor_lead = "add-doctor-lead";
  public static final String change_password = "change-password";
  public static final String sub_franchisee = "sub-franchisee";
  public static final String add_subfranchisee = "add-sub-franchisee";
  public static final String edit_subfranchisee = "edit-sub-franchisee";
  public static final String delete_subfranchisee = "delete-sub-franchisee";

  public static final String get_profile = "get-profile";
  public static final String edit_profile = "edit-profile";
  public static final String delete_patient_lead = "delete-patient-lead";
  public static final String delete_doctor_lead = "delete-doctor-lead";
  public static final String delete_hospital_lead = "delete-hospital-lead";
  public static final String edit_doctor_lead = "edit-doctor-lead";
  public static final String edit_patient_lead = "edit-patient-lead";
  public static final String edit_hospital_lead = "edit-hospital-lead";

  public static final String patient_profile = "patient-profile";
  public static final String edit_patient_profile = "edit-patient-profile";
  public static final String upload_file = "upload-file";

  public static final String doctor_patient_list = "doctors-patient-list";
  public static final String doctor_clinics = "doctor-clinics";
  public static final String send_prescription_email = "send-prescription-email";

  public static final String add_appointment = "doctors-assigned-to-assistant";
  public static final String add_appointment_two = "assistant-add-appointment";
  public static final String search_patient = "search-patient";
  public static final String doctor_prescription = "doctor-prescriptions";
  public static final String search_doctor = "search-doctor";
  public static final String patient_list_main = "hospital-patient-list";

  public static final String assistant_prescription = "assistant-prescriptins";

  public static final String doctors = "doctors";
  public static final String assistant_patient_list = "assistant-patient-list";

  public static final String personal_prescriptions = "personal-prescriptions";
  public static final String doctors_prescriptions = "doctors-prescriptions";

  public static final String total_prescriptions = "total-prescription";
  public static final String doc_add_appointment = "doctor-add-appointment";
  public static final String prescriptions_list = "prescriptions-list";
  public static final String doctor_test_suggested = "tests-suggested";

  public static final String hospital_edit_doctor = "hospital-edit-doctor";

  public static final String test_suggestions = "test-suggestions";
  public static final String other_doctor_prescriptions = "other-doctor-prescriptions";

  public static final String hospital_doctors_list = "hospital-doctors-list";

  public static final String search_brand = "search-brand";
  public static final String alternate_brand = "brand-alternatives";
  public static final String hospital_test_suggested = "total-tests-suggested";

  public static final String update_prescription = "update-prescription";
  public static final String doctor_view_prescription = "doctor-view-prescription";
  public static final String add_test_suggestions = "add-test-suggestion";

  public static final String edit_test_suggestions = "edit-test-suggestion";

  public static final String block_doctorlist = "block-list";
  public static final String block_doctor = "block-doctor";
  public static final String unblock_doctor = "unblock-doctor";

  public static final String add_prescription = "add-prescription";
  public static final String doc_search_patient = "doctor-search-patient";

  public static final String available_doctors = "available-doctors";
  public static final String forgot_password = "forgot-password";
  public static final String blood_groups = "blood-groups";


  public static final String doctors_patients = "doctors-patients";
  public static final String patient_details = "patient-details";

  public static final String hospital_patients = "hospital-patients";
  public static final String assistant_patients = "assistant-patients";






 /* public static Retrofit getRetrofitInstance() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
          .baseUrl(BASEURL)
          .addConverterFactory(GsonConverterFactory.create())
          .build();
    }
    return retrofit;
  }*/


  public static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
    @Override
    public Response intercept(Chain chain) throws IOException {
      Request newRequest  = chain.request().newBuilder()
              .method(chain.request().method(),chain.request().body())
              .build();
      return chain.proceed(newRequest);
    }
  }).connectTimeout(60, TimeUnit.SECONDS)
          .readTimeout(60,TimeUnit.SECONDS).build();


  public static Retrofit getRetrofitInstance() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
              .client(client)
              .baseUrl(BASEURL)
              .addConverterFactory(GsonConverterFactory.create())

              .build();
    }
    return retrofit;
  }

}

