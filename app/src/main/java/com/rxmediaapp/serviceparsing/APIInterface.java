package com.rxmediaapp.serviceparsing;

import com.rxmediaapp.storedobjects.StoredObjects;

import org.json.JSONArray;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

  @POST("app/index.php?")
  Call<ResponseBody> getweekdays(@Query("method") String method);

  @POST("app/index.php?")
  Call<ResponseBody> getDrSpecialization(@Query("method") String method);

  @POST("app/index.php?")
  Call<ResponseBody> getBloodgroup(@Query("method") String method);

  @POST("app/index.php?")
  Call<ResponseBody> getTimeSlots(@Query("method") String method);

  @POST("app/index.php?")
  Call<ResponseBody> getToDays(@Query("method") String method,
                               @Query("from_days") String from_days_id);

  @POST("app/index.php?")
  Call<ResponseBody> getToTime(@Query("method") String method,
                               @Query("from_time") String from_time_id);

  @POST("app/index.php?")
  Call<ResponseBody> Dashboards(@Query("method") String method,
                                @Query("logged_in_user_id") String logged_in_user_id,
                                @Query("logged_in_role_id") String logged_in_role_id,
                                @Query("from_date") String from_date,
                                @Query("to_date") String to_date);


  @POST("app/index.php?")
  Call<ResponseBody> SendEmail(@Query("method") String method,
                               @Query("logged_in_user_id") String logged_in_user_id,
                               @Query("logged_in_role_id") String logged_in_role_id,
                               @Query("prescription_id") String prescription_id);

  @POST("app/index.php?")
  Call<ResponseBody> hospitalSignup(@Query("method") String method,
                                    @Query("name") String hosp_name,
                                    @Query("email") String email,
                                    @Query("hospital_registration_number") String hospital_registration_number,
                                    @Query("no_of_beds") String no_of_beds,
                                    @Query("phone") String phone,
                                    @Query("address") String address,
                                    @Query("from_time") String from_time,
                                    @Query("to_time") String to_time,
                                    @Query("password") String password);


  @POST("app/index.php?")
  Call<ResponseBody> drSignUp(@Query("method") String method,
                              @Query("name") String name,
                              @Query("email") String email,
                              @Query("specialization_id") String specialization_id,
                              @Query("state_board") String state_board,
                              @Query("doctor_registration_number") String doctor_registration_number,
                              @Query("year_of_registration") String year_of_registration,
                              @Query("phone") String phone,
                              @Query("address") String address,
                              @Query("from_days") String from_days,
                              @Query("to_days") String to_days,
                              @Query("custom") String custom,
                              @Query("from_time") String from_time,
                              @Query("to_time") String to_time,
                              @Query("password") String password);


  //name,email,date_of_birth,gender,phone,blood_grup,aadhar_number,password
  @POST("app/index.php?")
  Call<ResponseBody> patientSignUp(@Query("method") String method,
                                   @Query("name") String name,
                                   @Query("email") String email,
                                   @Query("date_of_birth") String date_of_birth,
                                   @Query("gender") String gender,
                                   @Query("phone") String phone,
                                   @Query("blood_group") String blood_grup,
                                   @Query("aadhar_number") String aadhar_number,
                                   @Query("password") String password);


  //user_name,password,gcm_regid,phone_type=Android
  @POST("app/index.php?")
  Call<ResponseBody> login(@Query("method") String method,
                           @Query("user_name") String user_name,
                           @Query("password") String password,
                           @Query("gcm_regid") String gcm_regid,
                           @Query("phone_type") String phone_type);

  @POST("app/index.php?")
  Call<ResponseBody> forgotpassword(@Query("method") String method,
                           @Query("user_name") String user_name);

  @POST("app/index.php?")
  Call<ResponseBody> getDrProfile(@Query("method") String method,
                                  @Query("doctor_id") String doctor_id,
                                  @Query("logged_in_user_id") String logged_in_user_id,
                                  @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> addDrAssistant(@Query("method") String method,
                                    @Query("name") String name,

                                    @Query("phone") String mobile_number,
                                    @Query("password") String password,
                                    //@Query("from_time") String from_time,
                                    //@Query("to_time") String to_time,
                                    @Query("logged_in_user_id") String logged_in_user_id,
                                    @Query("logged_in_role_id") String logged_in_role_id,
                                    @Query("email") String email  );

/*  @POST("app/index.php?")
  Call<ResponseBody> getPhsyicalSuggestions(@Query("method") String method,
                                            @Query("logged_in_user_id") String logged_in_user_id,
                                            @Query("logged_in_role_id") String logged_in_role_id);*/


  //method=add-physical-suggestion&logged_in_role_id=7&logged_in_user_id=35&suggestion=BP
  @POST("app/index.php?")
  Call<ResponseBody> addPhsyicalSuggestions(@Query("method") String method,
                                            @Query("logged_in_role_id") String logged_in_role_id,
                                            @Query("logged_in_user_id") String logged_in_user_id,
                                            @Query("suggestion") String suggestion);


  @POST("app/index.php?")
  Call<ResponseBody> editPhsyicalSuggestions(@Query("method") String method,
                                             @Query("suggestion") String suggestion,
                                             @Query("logged_in_user_id") String logged_in_user_id,
                                             @Query("suggestion_id") String suggestion_id,
                                             @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> delPhsyicalSuggestions(@Query("method") String method,
                                            @Query("suggestion_id") String suggestion_id,
                                            @Query("logged_in_user_id") String logged_in_user_id,
                                            @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> getPrintFormats(@Query("method") String method,
                                     @Query("logged_in_user_id") String logged_in_user_id,
                                     @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> delDrAssistants(@Query("method") String method,
                                     @Query("assistant_id") String assistant_id,
                                     @Query("logged_in_user_id") String logged_in_user_id,
                                     @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> assistantProfile(@Query("method") String method,
                                      @Query("assistant_id") String assistant_id,
                                      @Query("logged_in_user_id") String logged_in_user_id,
                                      @Query("logged_in_role_id") String logged_in_role_id);



  @POST("app/index.php?")
  Call<ResponseBody> gethospitalProfile(@Query("method") String method,
                                        @Query("hospital_id") String hospital_id,
                                        @Query("logged_in_user_id") String logged_in_user_id,
                                        @Query("logged_in_role_id") String logged_in_role_id);



  @POST("app/index.php?")
  Call<ResponseBody> gethospitalAssistants(@Query("method") String method,
                                           @Query("logged_in_user_id") String logged_in_user_id,
                                           @Query("logged_in_role_id") String logged_in_role_id,
                                           @Query("page") String page,
                                           @Query("records_per_page") String records_per_page);

  @POST("app/index.php?")
  Call<ResponseBody> addhospitalAssistant(@Query("method") String method,
                                          @Query("name") String name ,
                                          @Query("phone") String mobile_number,
                                          //@Query("from_time") String from_time,
                                         // @Query("to_time") String to_time,
                                          @Query("doctors") String doctors  ,
                                          @Query("logged_in_user_id") String logged_in_user_id,
                                          @Query("logged_in_role_id") String logged_in_role_id,
                                          @Query("email") String email,
                                          @Query("password") String password);


  @POST("app/index.php?")
  Call<ResponseBody> delhospitalAssistants(@Query("method") String method,
                                           @Query("assistant_id") String assistant_id,
                                           @Query("logged_in_user_id") String logged_in_user_id,
                                           @Query("logged_in_role_id") String logged_in_role_id);



  @POST("app/index.php?")
  Call<ResponseBody> drEditAssistant(@Query("method") String method,
                                     @Query("name") String name,
                                     @Query("phone") String mobile_number,
                                     @Query("email") String email,
                                    // @Query("from_time") String from_time,
                                    // @Query("to_time") String to_time,
                                     @Query("assistant_id") String assistant_id,
                                     @Query("logged_in_user_id") String logged_in_user_id,
                                     @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> getPatientProfile(@Query("method") String method,
                                       @Query("patient_id") String patient_id,
                                       @Query("logged_in_user_id") String logged_in_user_id,
                                       @Query("logged_in_role_id") String logged_in_role_id);



  @POST("app/index.php?")
  Call<ResponseBody> getDrnames(@Query("method") String method,
                                @Query("logged_in_user_id") String logged_in_user_id,
                                @Query("logged_in_role_id") String logged_in_role_id);


  @POST("app/index.php?")
  Call<ResponseBody> EdithospitalAssistant(@Query("method") String method,
                                           @Query("logged_in_user_id") String logged_in_user_id,
                                           @Query("logged_in_role_id") String logged_in_role_id,
                                           @Query("name") String assistant_name,
                                           @Query("phone") String phone,
                                          // @Query("from_time") String from_time,
                                          // @Query("to_time") String to_time,
                                           @Query("doctors") String assign_doc,
                                           @Query("assistant_id") String assistant_id,
                                           @Query("email") String email);

  @POST("app/index.php?")
  Call<ResponseBody> hospitalAddDr(@Query("method") String method,
                                   @Query("name") String name,
                                   @Query("email") String email,
                                   @Query("specialization_id") String specialization_id  ,
                                   @Query("state_board") String state_board ,
                                   @Query("doctor_registration_number") String doctor_registration_number ,
                                   @Query("year_of_registration") String year_of_registration ,
                                   @Query("phone") String phone ,
                                   //@Query("password") String password ,
                                   @Query("from_days") String from_days   ,
                                   @Query("to_days") String to_days  ,
                                   @Query("from_time") String from_time  ,
                                   @Query("to_time") String to_time  ,
                                   @Query("from_time1") String from_time1,
                                   @Query("to_time1") String to_time1,
                                   @Query("from_time2") String from_time2,
                                   @Query("to_time2") String to_time2,
                                   @Query("from_time3") String from_time3,
                                   @Query("to_time3") String to_time3,
                                   @Query("logged_in_user_id") String logged_in_user_id   ,
                                   @Query("logged_in_role_id") String logged_in_role_id   ,
                                   @Query("assistant_id") String assistant_id,
                                    @Query("password") String password,
                                    @Query("address") String address,
                                    @Query("custom") String custom
          ,
                                   @Query("qualification") String qualification,
                                   @Query("extra_qualification") String extra_qualification,
                                   @Query("other_qualification") String other_qualification,
                                   @Query("extra_time_slots") String extra_time_slots);


  @POST("app/index.php?")
  Call<ResponseBody> gethospitals(@Query("method") String method,
                                  @Query("logged_in_user_id") String logged_in_user_id,
                                  @Query("logged_in_role_id") String logged_in_role_id,
                                  @Query("page") String page,
                                  @Query("records_per_page") String records_per_page);

  @POST("app/index.php?")
  Call<ResponseBody> addhospital(@Query("method") String method,
                                 @Query("name") String name,
                                 @Query("email") String email,
                                 @Query("hospital_registration_number") String hospital_reg_num,
                                 @Query("no_of_beds") String no_of_beds,
                                 @Query("phone") String phone,
                                 @Query("address") String address,
                                 @Query("from_time") String from_time,
                                 @Query("to_time") String to_time,
                                 @Query("password") String password,
                                 @Query("logged_in_user_id") String logged_in_user_id,
                                 @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> edithospital(@Query("method") String method,
                                 @Query("hosp_name") String name,
                                 @Query("email") String email,
                                 @Query("hospital_registration_number") String hospital_reg_num,
                                 @Query("no_of_beds") String no_of_beds,
                                 @Query("phone") String phone,
                                 @Query("address") String address,
                                 @Query("from_time") String from_time,
                                 @Query("to_time") String to_time,
                                 @Query("logged_in_user_id") String logged_in_user_id,
                                 @Query("logged_in_role_id") String logged_in_role_id,
                                  @Query("hospital_id") String user_id);


  @POST("app/index.php?")
  Call<ResponseBody> delhospital(@Query("method") String method,
                                 @Query("hospital_id") String hospital_id,
                                 @Query("logged_in_user_id") String logged_in_user_id,
                                 @Query("logged_in_role_id") String logged_in_role_id);


  @POST("app/index.php?")
  Call<ResponseBody> tl_editDoctor(@Query("method") String method,
                               @Query("logged_in_user_id") String logged_in_user_id,
                               @Query("logged_in_role_id") String logged_in_role_id,
                               @Query("name") String name,
                               @Query("email") String email ,
                               @Query("doctor_registration_number") String doctor_registration_number,
                               @Query("specialization_id") String specialization_id,
                               @Query("state_board") String state_board,
                               @Query("year_of_registration") String year_of_registration,
                               @Query("phone") String phone,
                               @Query("from_days") String from_days,
                               @Query("to_days") String to_days,
                               @Query("custom") String custom,
                               @Query("from_time") String from_time,
                               @Query("to_time") String to_time,
                                   @Query("from_time1") String from_time1,
                                   @Query("to_time1") String to_time1,
                                   @Query("from_time2") String from_time2,
                                   @Query("to_time2") String to_time2,
                                   @Query("from_time3") String from_time3,
                                   @Query("to_time3") String to_time3,
                                   @Query("doctor_id") String user_id,
                                   @Query("address") String address,
                                  @Query("qualification") String qualification,
                                   @Query("extra_qualification") String extra_qualification,
                                   @Query("other_qualification") String other_qualification,
                                   @Query("extra_time_slots") String extra_time_slots);



    @POST("app/index.php?")
  Call<ResponseBody> tl_addDoctor(@Query("method") String method,
                               @Query("logged_in_user_id") String logged_in_user_id,
                               @Query("logged_in_role_id") String logged_in_role_id,
                               @Query("name") String name,
                               @Query("email") String email ,
                               @Query("specialization_id") String specialization_id,
                               @Query("state_board") String state_board,
                               @Query("doctor_registration_number") String doctor_registration_number,
                               @Query("year_of_registration") String year_of_registration,
                               @Query("phone") String phone,
                               @Query("from_days") String from_days,
                               @Query("to_days") String to_days,
                               @Query("custom") String custom,
                               @Query("from_time") String from_time,
                               @Query("to_time") String to_time,
                                  @Query("from_time1") String from_time1,
                                  @Query("to_time1") String to_time1,
                                  @Query("from_time2") String from_time2,
                                  @Query("to_time2") String to_time2,
                                  @Query("from_time3") String from_time3,
                                  @Query("to_time3") String to_time3,
                                  @Query("password") String password,
                                  @Query("address") String address,
                                  @Query("qualification") String qualification,
                                  @Query("extra_qualification") String extra_qualification,
                                  @Query("other_qualification") String other_qualification,
                                  @Query("extra_time_slots") String extra_time_slots);

  @POST("app/index.php?")
  Call<ResponseBody> marketingExecutives(@Query("method") String method,
                                         @Query("logged_in_user_id") String logged_in_user_id,
                                         @Query("logged_in_role_id") String logged_in_role_id,
                                         @Query("page") String page,
                                         @Query("records_per_page") String records_per_page);

  @POST("app/index.php?")
  Call<ResponseBody> addMarketingExecutives(@Query("method") String method,
                                            @Query("reference_from") String reference_from,
                                            @Query("name") String name,
                                            @Query("email") String email,
                                            @Query("aadhar_number") String aadhar_number,
                                            @Query("phone") String mobile_number,
                                            @Query("password") String password,
                                            @Query("logged_in_user_id") String logged_in_user_id,
                                            @Query("logged_in_role_id") String logged_in_role_id);


  @POST("app/index.php?")
  Call<ResponseBody> addMarketingExecutives_img(@Query("method") String method,
                                            @Query("reference_from") String reference_from,
                                            @Query("name") String name,
                                            @Query("email") String email,
                                            @Query("aadhar_number") String aadhar_number,
                                            @Query("phone") String mobile_number,
                                            @Query("password") String password,
                                            @Query("logged_in_user_id") String logged_in_user_id,
                                            @Query("logged_in_role_id") String logged_in_role_id,
                                                @Query("image") String image);

  @POST("app/index.php?")
  Call<ResponseBody> delMarketingExecutives(@Query("method") String method,
                                            @Query("logged_in_user_id") String logged_in_user_id,
                                            @Query("logged_in_role_id") String logged_in_role_id,
                                            @Query("user_id") String user_id);


  @POST("app/index.php?")
  Call<ResponseBody> getFranchiseeList(@Query("method") String method,
                                       @Query("logged_in_user_id") String logged_in_user_id,
                                       @Query("logged_in_role_id") String logged_in_role_id,
                                       @Query("page") String page,
                                       @Query("records_per_page") String records_per_page);

  @POST("app/index.php?")
  Call<ResponseBody> delFranchiseeList(@Query("method") String method,
                                       @Query("logged_in_user_id") String logged_in_user_id,
                                       @Query("logged_in_role_id") String logged_in_role_id,
                                       @Query("user_id") String user_id);

  @POST("app/index.php?")
  Call<ResponseBody> addFranchisee(@Query("method") String method,
                                   @Query("reference_from") String reference_from,
                                   @Query("name") String name,
                                   @Query("email") String email,
                                   @Query("aadhar_number") String aadhar_number,
                                   @Query("phone") String mobile_number,
                                   @Query("password") String password,
                                   @Query("logged_in_user_id") String logged_in_user_id,
                                   @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> addFranchisee_img(@Query("method") String method,
                                   @Query("reference_from") String reference_from,
                                   @Query("name") String name,
                                   @Query("email") String email,
                                   @Query("aadhar_number") String aadhar_number,
                                   @Query("phone") String mobile_number,
                                   @Query("password") String password,
                                   @Query("logged_in_user_id") String logged_in_user_id,
                                   @Query("logged_in_role_id") String logged_in_role_id,
                                       @Query("image") String image);



  @POST("app/index.php?")
  Call<ResponseBody> editMarketExecutive(@Query("method") String method,
                                         @Query("reference_from") String reference_from,
                                         @Query("name") String name,
                                         @Query("email") String email,
                                         @Query("aadhar_number") String aadhar_number,
                                         @Query("phone") String mobile_number,
                                         @Query("logged_in_user_id") String logged_in_user_id,
                                         @Query("logged_in_role_id") String logged_in_role_id,
                                         @Query("user_id") String user_id);


  @POST("app/index.php?")
  Call<ResponseBody> editMarketExecutive_img(@Query("method") String method,
                                         @Query("reference_from") String reference_from,
                                         @Query("name") String name,
                                         @Query("email") String email,
                                         @Query("aadhar_number") String aadhar_number,
                                         @Query("phone") String mobile_number,
                                         @Query("logged_in_user_id") String logged_in_user_id,
                                         @Query("logged_in_role_id") String logged_in_role_id,
                                         @Query("user_id") String user_id,
                                             @Query("image") String image);
  @POST("app/index.php?")
  Call<ResponseBody> editFranchisee(@Query("method") String method,
                                    @Query("reference_from") String reference_from,
                                    @Query("name") String name,
                                    @Query("email") String email,
                                    @Query("aadhar_number") String aadhar_number,
                                    @Query("phone") String mobile_number,
                                    @Query("logged_in_user_id") String logged_in_user_id,
                                    @Query("logged_in_role_id") String logged_in_role_id,
                                    @Query("user_id") String user_id);

  @POST("app/index.php?")
  Call<ResponseBody> editFranchisee_img(@Query("method") String method,
                                    @Query("reference_from") String reference_from,
                                    @Query("name") String name,
                                    @Query("email") String email,
                                    @Query("aadhar_number") String aadhar_number,
                                    @Query("phone") String mobile_number,
                                    @Query("logged_in_user_id") String logged_in_user_id,
                                    @Query("logged_in_role_id") String logged_in_role_id,
                                    @Query("user_id") String user_id,
                                        @Query("image") String image);

  @POST("app/index.php?")
  Call<ResponseBody> getTLPatients(@Query("method") String method,
                                   @Query("logged_in_user_id") String logged_in_user_id,
                                   @Query("logged_in_role_id") String logged_in_role_id,
                                   @Query("page") String page,
                                   @Query("records_per_page") String records_per_page);


  @POST("app/index.php?")
  Call<ResponseBody> getDoctorPatients(@Query("method") String method,
                                   @Query("logged_in_user_id") String logged_in_user_id,
                                   @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> delPatient(@Query("method") String method,
                                @Query("logged_in_user_id") String logged_in_user_id,
                                @Query("logged_in_role_id") String logged_in_role_id,
                                @Query("patient_id") String patient_id);


  @POST("app/index.php?")
  Call<ResponseBody> addPatient(@Query("method") String method,
                                @Query("name") String name,
                                @Query("email") String email,
                                @Query("date_of_birth") String date_of_birth,
                                @Query("gender") String gender,
                                @Query("aadhar_number") String aadhar_number,
                                @Query("blood_group") String blood_grup,
                                @Query("phone") String phone,
                                @Query("password") String password,
                                @Query("logged_in_user_id") String logged_in_user_id,
                                @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> editPatient(@Query("method") String method,
                                 @Query("name") String name,
                                 @Query("email") String email,
                                 @Query("date_of_birth") String date_of_birth,
                                 @Query("gender") String gender,
                                 @Query("aadhar_number") String aadhar_number,
                                 @Query("blood_group") String blood_grup,
                                 @Query("phone") String phone,
                                 @Query("logged_in_user_id") String logged_in_user_id,
                                 @Query("logged_in_role_id") String logged_in_role_id,
                                 @Query("patient_id") String patient_id);

  @POST("app/index.php?")
  Call<ResponseBody> GetLeads(@Query("method") String method,
                                   @Query("logged_in_user_id") String logged_in_user_id,
                                   @Query("logged_in_role_id") String logged_in_role_id,
                                   @Query("page") String page,
                                   @Query("records_per_page") String records_per_page,
                                    @Query("from_date") String from_date,
                                    @Query("to_date") String to_date,
                                    @Query("search_text") String search_text);


  @POST("app/index.php?")
  Call<ResponseBody> getmembers(@Query("method") String method,
                                @Query("logged_in_user_id") String logged_in_user_id,
                                @Query("logged_in_role_id") String logged_in_role_id,
                                @Query("page") String page,
                                @Query("records_per_page") String records_per_page);



  @POST("app/index.php?")
  Call<ResponseBody> addmember(@Query("method") String method,
                               @Query("relation") String relation,
                               @Query("name") String name,
                               @Query("email") String email,
                               @Query("phone") String mobile_number,
                               @Query("date_of_birth") String date_of_birth,
                               @Query("blood_group") String blood_grup,
                               @Query("gender") String gender,
                               @Query("aadhar_number") String aadhar_number,
                               @Query("password") String password,
                               @Query("logged_in_user_id") String logged_in_user_id,
                               @Query("logged_in_role_id") String logged_in_role_id);


  @POST("app/index.php?")
  Call<ResponseBody> delMember(@Query("method") String method,
                               @Query("patient_id") String patient_id,
                               @Query("logged_in_user_id") String logged_in_user_id,
                               @Query("logged_in_role_id") String logged_in_role_id);



  @POST("app/index.php?")
  Call<ResponseBody> EditMember(@Query("method") String method,
                                @Query("patient_id") String patient_id,
                                @Query("logged_in_user_id") String logged_in_user_id,
                                @Query("logged_in_role_id") String logged_in_role_id,
                                @Query("relation") String relation,
                                @Query("name") String name,
                                @Query("email") String email,
                                @Query("phone") String mobile_number,
                                @Query("date_of_birth") String date_of_birth,
                                @Query("blood_group") String blood_grup,
                                @Query("gender") String gender,
                                @Query("aadhar_number") String aadhar_number);

  @POST("app/index.php?")
  Call<ResponseBody> addLead(@Query("method") String method,
                                    @Query("name") String name,
                                    @Query("phone") String phone,
                                    @Query("appointment_date") String appointment_date,
                                    @Query("comments") String comments,
                                    @Query("address") String address,
                                    @Query("logged_in_user_id") String logged_in_user_id,
                                    @Query("logged_in_role_id") String logged_in_role_id);


  @POST("app/index.php?")
  Call<ResponseBody> changepassword(@Query("method") String method,
                                    @Query("new_password") String new_password,
                                    @Query("logged_in_user_id") String logged_in_user_id,
                                    @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> TL_GetProfile(@Query("method") String method,
                                   @Query("logged_in_user_id") String logged_in_user_id,
                                   @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> TL_EditProfile(@Query("method") String method,
                                    @Query("name") String name,
                                    // @Query("image") String image,
                                    @Query("email") String email,
                                    @Query("aadhar_number") String aadhar_number,
                                    @Query("phone") String phone,
                                    @Query("logged_in_user_id") String logged_in_user_id,
                                    @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> TL_EditProfile_img(@Query("method") String method,
                                    @Query("name") String name,
                                    @Query("email") String email,
                                    @Query("aadhar_number") String aadhar_number,
                                    @Query("phone") String phone,
                                    @Query("logged_in_user_id") String logged_in_user_id,
                                    @Query("logged_in_role_id") String logged_in_role_id,
                                        @Query("image") String image);

  @POST("app/index.php?")
  Call<ResponseBody> deleteLead(@Query("method") String method,
                                @Query("logged_in_user_id") String logged_in_user_id,
                                @Query("logged_in_role_id") String logged_in_role_id,
                                @Query("lead_id") String user_id);

  @POST("app/index.php?")
  Call<ResponseBody> editLead(@Query("method") String method,
                              @Query("name") String name,
                              @Query("phone") String phone,
                              @Query("appointment_date") String appointment_date,
                              @Query("status") String status,
                              @Query("comments") String comments,
                              @Query("address") String address,
                              @Query("logged_in_user_id") String logged_in_user_id,
                              @Query("logged_in_role_id") String logged_in_role_id,
                              @Query("lead_id") String user_id);

  @POST("app/index.php?")
  Call<ResponseBody> getDrAssistants(@Query("method") String method,
                                     @Query("logged_in_user_id") String logged_in_user_id,
                                     @Query("logged_in_role_id") String logged_in_role_id,
                                     @Query("page") String page,
                                     @Query("records_per_page") String records_per_page);


  @POST("app/index.php?")
  Call<ResponseBody> getPhsyicalSuggestions(@Query("method") String method,
                                            @Query("logged_in_user_id") String logged_in_user_id,
                                            @Query("logged_in_role_id") String logged_in_role_id,
                                            @Query("page") String page,
                                            @Query("records_per_page") String records_per_page);



  @POST("app/index.php?")
  Call<ResponseBody> editPrintFormats(@Query("method") String method,
                                      @Query("logo") String logo,
                                      @Query("header_name") String header_name,
                                      @Query("signature") String signature,
                                      @Query("logged_in_user_id") String logged_in_user_id,
                                      @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> editDrProfile(@Query("method") String method,
                                   @Query("name") String name,
                                   @Query("email") String email,
                                   @Query("mobile_number") String mobile_number,
                                   @Query("image") String image_str,
                                   @Query("specialization_id") String specialization_id,
                                   @Query("state_board") String state_board,
                                   @Query("doctor_registration_number") String doctor_registration_number,
                                   @Query("year_of_registration") String year_of_registration,
                                   @Query("address") String address,
                                   @Query("available_timings") String available_timings,
                                   @Query("doctor_id") String doctor_id,
                                   @Query("logged_in_user_id") String logged_in_user_id,
                                   @Query("logged_in_role_id") String logged_in_role_id,
                                   @Query("qualification") String qualification,
                                   @Query("extra_qualification") String extra_qualification,
                                   @Query("other_qualification") String other_qualification);

  @POST("app/index.php?")
  Call<ResponseBody> edithospitalProfile(@Query("method") String method,
                                         @Query("hospital_id") String hospital_id,
                                         @Query("logged_in_user_id") String logged_in_user_id,
                                         @Query("logged_in_role_id") String logged_in_role_id,
                                         @Query("hospital_name") String hospital_name,
                                         @Query("image") String image,
                                         @Query("hospital_reg_num") String hospital_reg_num,
                                         @Query("no_of_beds") String no_of_beds,
                                         @Query("phone") String phone,
                                         @Query("from_time") String from_time,
                                         @Query("to_time") String to_time,
                                         @Query("address") String address,
  @Query("email") String email);

  @POST("app/index.php?")
  Call<ResponseBody> editAssistantProfile(@Query("method") String method,
                                          @Query("name") String name,
                                          @Query("email") String email,
                                          @Query("mobile_number") String mobile_number,
                                         // @Query("from_time") String from_time,
                                         // @Query("to_time") String to_time,
                                          @Query("assistant_id") String assistant_id,
                                          @Query("logged_in_user_id") String logged_in_user_id,
                                          @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> editAssistantProfile_img(@Query("method") String method,
                                          @Query("name") String name,
                                          @Query("image") String image,
                                          @Query("email") String email,
                                          @Query("mobile_number") String mobile_number,
                                          //@Query("from_time") String from_time,
                                         // @Query("to_time") String to_time,
                                          @Query("assistant_id") String assistant_id,
                                          @Query("logged_in_user_id") String logged_in_user_id,
                                          @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> editPatientProfile(@Query("method") String method,
                                        @Query("name") String name,
                                        //@Query("image") String image,
                                        @Query("email") String email,
                                        @Query("phone") String phone,
                                        @Query("aadhar_number") String aadhar_number,
                                        @Query("date_of_birth") String date_of_birth,
                                        @Query("gender") String gender,
                                        @Query("blood_group") String blood_grup,
                                        @Query("patient_id") String patient_id,
                                        @Query("logged_in_user_id") String logged_in_user_id,
                                        @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> editPatientProfile_img(@Query("method") String method,
                                        @Query("name") String name,
                                        @Query("image") String image,
                                        @Query("email") String email,
                                        @Query("phone") String phone,
                                        @Query("aadhar_number") String aadhar_number,
                                        @Query("date_of_birth") String date_of_birth,
                                        @Query("gender") String gender,
                                        @Query("blood_group") String blood_grup,
                                        @Query("patient_id") String patient_id,
                                        @Query("logged_in_user_id") String logged_in_user_id,
                                        @Query("logged_in_role_id") String logged_in_role_id);



  @POST("app/index.php?")
  Call<ResponseBody> Docpatientdetails(@Query("method") String method,
                                       @Query("logged_in_user_id") String logged_in_user_id,
                                       @Query("logged_in_role_id") String logged_in_role_id);



  @POST("app/index.php?")
  Call<ResponseBody> Docpatientlistmain(@Query("method") String method,
                                        @Query("logged_in_user_id") String logged_in_user_id,
                                        @Query("logged_in_role_id") String logged_in_role_id);


  @POST("app/index.php?")
  Call<ResponseBody> PatientPrescription(@Query("method") String method,
                                         @Query("logged_in_user_id") String logged_in_user_id,
                                         @Query("logged_in_role_id") String logged_in_role_id,
                                         @Query("page") String page,
                                         @Query("records_per_page") String records_per_page);

  @POST("app/index.php?")
  Call<ResponseBody> franchiseeDoctors(@Query("method") String method,
                                       @Query("logged_in_user_id") String logged_in_user_id,
                                       @Query("logged_in_role_id") String logged_in_role_id,
                                       @Query("page") String page,
                                       @Query("records_per_page") String records_per_page,
                                       @Query("specialization_id") String specialization_id);


  @POST("app/index.php?")
  Call<ResponseBody> franchiseeDoctors(@Query("method") String method,
                                       @Query("logged_in_user_id") String logged_in_user_id,
                                       @Query("logged_in_role_id") String logged_in_role_id,
                                       @Query("page") String page,
                                       @Query("records_per_page") String records_per_page);

  @POST("app/index.php?")
  Call<ResponseBody> franchiseeHospitals(@Query("method") String method,
                                         @Query("logged_in_user_id") String logged_in_user_id,
                                         @Query("logged_in_role_id") String logged_in_role_id,
                                         @Query("page") String page,
                                         @Query("records_per_page") String records_per_page);

  @POST("app/index.php?")
  Call<ResponseBody> franchiseePatients(@Query("method") String method,
                                        @Query("logged_in_user_id") String logged_in_user_id,
                                        @Query("logged_in_role_id") String logged_in_role_id,
                                        @Query("page") String page,
                                        @Query("records_per_page") String records_per_page);

  @POST("app/index.php?")
  Call<ResponseBody> Docpatientlist(@Query("method") String method,
                                    @Query("hospital_id") String hospital_id,
                                    @Query("logged_in_user_id") String logged_in_user_id,
                                    @Query("logged_in_role_id") String logged_in_role_id,
                                    @Query("from_date") String from_date,
                                    @Query("to_date") String to_date,
  @Query("search_text") String search_text);

  @POST("app/index.php?")
  Call<ResponseBody> DocpatientlistHistory(@Query("method") String method,
                                           @Query("hospital_id") String hospital_id,
                                           @Query("from_date") String from_date,
                                           @Query("to_date") String to_date,
                                           @Query("search_text") String search_text,
                                           @Query("logged_in_user_id") String logged_in_user_id,
                                           @Query("logged_in_role_id") String logged_in_role_id,

                                           @Query("page") String page,
                                           @Query("records_per_page") String records_per_page);



  @POST("app/index.php?")
  Call<ResponseBody> AddAppointment(@Query("method") String method,
                                    @Query("logged_in_user_id") String logged_in_user_id,
                                    @Query("logged_in_role_id") String logged_in_role_id);




    @POST("app/index.php?")
    Call<ResponseBody> Assistantpatientlist(@Query("method") String method,
                                      @Query("logged_in_user_id") String logged_in_user_id,
                                      @Query("logged_in_role_id") String logged_in_role_id,
                                      @Query("from_date") String from_date,
                                      @Query("to_date") String to_date,
                                    //  @Query("status") String status,
                                      @Query("search_text") String search_text,
                                      @Query("doctor_id") String doctor_id,
                                            @Query("page") String page,
                                      @Query("records_per_page") String records_per_page);

  @POST("app/index.php?")
  Call<ResponseBody> doctorslist(@Query("method") String method,
                                 @Query("logged_in_user_id") String logged_in_user_id,
                                 @Query("logged_in_role_id") String logged_in_role_id,
                                 @Query("specialization_id") String specialization_id,
                                 @Query("page") String page,
                                 @Query("records_per_page") String records_per_page
                                 ,@Query("search_text") String search_text);




  @POST("app/index.php?")
  Call<ResponseBody> Patientlist(@Query("method") String method,
                                 @Query("doctor_id") String doctor_id,
                                 @Query("from_date") String from_date,
                                 @Query("to_date") String to_date,
                                 @Query("logged_in_user_id") String logged_in_user_id,
                                 @Query("logged_in_role_id") String logged_in_role_id,
                                 @Query("page") String page,
                                 @Query("records_per_page") String records_per_page);



  @POST("app/index.php?")
  Call<ResponseBody> PersonalPrescriptions(@Query("method") String method,
                                           @Query("logged_in_user_id") String logged_in_user_id,
                                           @Query("logged_in_role_id") String logged_in_role_id,
                                           @Query("page") String page,
                                           @Query("records_per_page") String records_per_page);



  @POST("app/index.php?")
  Call<ResponseBody> DoctorsPrescriptions(@Query("method") String method,
                                          @Query("logged_in_user_id") String logged_in_user_id,
                                          @Query("logged_in_role_id") String logged_in_role_id,
                                          @Query("page") String page,
                                          @Query("records_per_page") String records_per_page);




  @POST("app/index.php?")
  Call<ResponseBody> TotalPrescriptions(@Query("method") String method,
                                        @Query("doctor_id") String doctor_id,
                                        @Query("from_date") String from_date,
                                        @Query("to_date") String to_date, @Query("search_text") String search_text,
                                        @Query("logged_in_user_id") String logged_in_user_id,
                                        @Query("logged_in_role_id") String logged_in_role_id,
                                        @Query("page") String page,
                                        @Query("records_per_page") String records_per_page);


  @POST("app/index.php?")
  Call<ResponseBody> AddAppointmentTwo(@Query("method") String method,
                                       @Query("patient_id") String patient_id,
                                       @Query("name") String name,
                                       @Query("email") String email,
                                       @Query("date_of_birth") String date_of_birth,
                                       @Query("gender") String gender,
                                       @Query("blood_group") String blood_grup,
                                       @Query("phone") String mobile_number,
                                       @Query("aadhar_number") String aadhar_number,
                                       @Query("problem") String problem,
                                       @Query("doctor_id") String doctor_id,
                                       @Query("physical_examinations") String physical_examinations,
                                       @Query("logged_in_user_id") String logged_in_user_id,
                                       @Query("logged_in_role_id") String logged_in_role_id);


  @POST("app/index.php?")
  Call<ResponseBody> SearchPatient(@Query("method") String method,
                                   @Query("doctor_id") String doctor_id,
                                   @Query("mobile") String mobile);

  @POST("app/index.php?")
  Call<ResponseBody> AddAppointmentone(@Query("method") String method,
                                       @Query("patient_id") String patient_id,
                                       @Query("problem") String problem,
                                       @Query("doctor_id") String doctor_id,
                                       @Query("physical_examinations") String physical_examinations,
                                       @Query("logged_in_user_id") String logged_in_user_id,
                                       @Query("logged_in_role_id") String logged_in_role_id,
                                       @Query("phone") String mobile_number);

  @POST("app/index.php?")
  Call<ResponseBody> DoctorPrescription(@Query("method") String method,
                                        @Query("hospital_id") String hospital_id,
                                        @Query("logged_in_user_id") String logged_in_user_id,
                                        @Query("logged_in_role_id") String logged_in_role_id,
                                        @Query("from_date") String from_date,
                                        @Query("to_date") String to_date,
                                        @Query("page") String page,
                                        @Query("records_per_page") String records_per_page);



    @POST("app/index.php?")
    Call<ResponseBody> DocAddAppointment(@Query("method") String method,
                                         @Query("patient_id") String patient_id,
                                         @Query("name") String name,
                                         @Query("email") String email,
                                         @Query("date_of_birth") String date_of_birth,
                                         @Query("aadhar_number") String aadhar_number,
                                         @Query("phone") String mobile_number,
                                         @Query("gender") String gender,
                                         @Query("blood_group") String blood_grup,
                                         @Query("doctor_id") String doctor_id,
                                         @Query("hospital_id") String hospital_id,
                                         @Query("problem") String problem,
                                         @Query("logged_in_user_id") String logged_in_user_id,
                                         @Query("logged_in_role_id") String logged_in_role_id);


  @POST("app/index.php?")
  Call<ResponseBody> DocAddAppointment(@Query("method") String method,
                                       @Query("patient_id") String patient_id,
                                       @Query("hospital_id") String hospital_id,
                                       @Query("problem") String problem,
                                       @Query("doctor_id") String doctor_id,
                                       @Query("logged_in_user_id") String logged_in_user_id,
                                       @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> PrescriptionList(@Query("method") String method,
                                      @Query("from_date") String from_date,
                                      @Query("to_date") String to_date,
                                      @Query("type") String type,
                                      @Query("search_text") String search_text,
                                      @Query("logged_in_user_id") String logged_in_user_id,
                                      @Query("logged_in_role_id") String logged_in_role_id,
                                      @Query("page") String page,
                                      @Query("records_per_page") String records_per_page,
   @Query("patient_id") String patient_id);


  @POST("app/index.php?")
  Call<ResponseBody> hospitalEditDr(@Query("method") String method,
                                    @Query("name") String name,
                                    @Query("email") String email,
                                    @Query("specialization_id") String specialization_id  ,
                                    @Query("state_board") String state_board ,
                                    @Query("doctor_registration_number") String doctor_registration_number ,
                                    @Query("year_of_registration") String year_of_registration ,
                                    //@Query("phone") String phone ,
                                    @Query("from_days") String from_days   ,
                                    @Query("to_days") String to_days  ,
                                    @Query("from_time") String from_time,
                                    @Query("to_time") String to_time,
                                    @Query("from_time1") String from_time1,
                                    @Query("to_time1") String to_time1,
                                    @Query("from_time2") String from_time2,
                                    @Query("to_time2") String to_time2,
                                    @Query("from_time3") String from_time3,
                                    @Query("to_time3") String to_time3,
                                    // @Query("assistant_id") String assistant_id   ,
                                    @Query("logged_in_user_id") String logged_in_user_id   ,
                                    @Query("logged_in_role_id") String logged_in_role_id   ,
                                    @Query("doctor_id") String existing_doctor_id,
                                    @Query("assistant_id") String assistant_id ,
                                      @Query("custom") String custom,
                                      @Query("address") String address
          ,
                                    @Query("qualification") String qualification,
                                    @Query("extra_qualification") String extra_qualification,
                                    @Query("other_qualification") String other_qualification,
  @Query("extra_time_slots") String extra_time_slots);


  @POST("app/index.php?")
  Call<ResponseBody> Dashboard(@Query("method") String method,
                               @Query("logged_in_user_id") String logged_in_user_id,
                               @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> DoctorTestSugested(@Query("method") String method,
                                        @Query("logged_in_user_id") String logged_in_user_id,
                                        @Query("logged_in_role_id") String logged_in_role_id,
                                        @Query("hospital_id") String hospital_id,
                                        @Query("from_date") String from_date,
                                        @Query("to_date") String to_date,
                                        @Query("page") String page,
                                        @Query("records_per_page") String records_per_page,
                                        @Query("search_text") String search_text);

  @POST("app/index.php?")
  Call<ResponseBody> TestSugestions(@Query("method") String method,
                                    @Query("logged_in_user_id") String logged_in_user_id,
                                    @Query("logged_in_role_id") String logged_in_role_id,
                                    @Query("from_date") String from_date,
                                    @Query("to_date") String to_date,
                                    @Query("page") String page,
                                    @Query("records_per_page") String records_per_page,
                                    @Query("search_text") String search_text,
                                    @Query("type") String type);


/*  @POST("app/index.php?")
  Call<ResponseBody> DoctorTotalPrscriptionslist(@Query("method") String method,
                                                 @Query("logged_in_user_id") String logged_in_user_id,
                                                 @Query("logged_in_role_id") String logged_in_role_id,
                                                 @Query("hospital_id") String hospital_id,
                                                 @Query("from_date") String from_date,
                                                 @Query("to_date") String to_date,
                                                 @Query("page") String page,
                                                 @Query("records_per_page") String records_per_page,
                                                 @Query("search_text") String search_text);




  @POST("app/index.php?")
  Call<ResponseBody> DoctorTotalPrscriptionslist(@Query("method") String method,
                                                 @Query("patient_id") String patient_id,
                                                 @Query("logged_in_user_id") String logged_in_user_id,
                                                 @Query("logged_in_role_id") String logged_in_role_id,
                                                 @Query("hospital_id") String hospital_id,
                                                 @Query("from_date") String from_date,
                                                 @Query("to_date") String to_date,
                                                 @Query("type") String type,
                                                 @Query("search_text") String search_text);*/


  @POST("app/index.php?")
  Call<ResponseBody> DoctorTotalPrscriptionslist(@Query("method") String method,
                                                 @Query("logged_in_user_id") String logged_in_user_id,
                                                 @Query("logged_in_role_id") String logged_in_role_id,
                                                 @Query("hospital_id") String hospital_id,
                                                 @Query("from_date") String from_date,
                                                 @Query("to_date") String to_date,
                                                 @Query("type") String type,
                                                 @Query("page") String page,
                                                 @Query("records_per_page") String records_per_page,
                                                 @Query("search_text") String search_text);


  @POST("app/index.php?")
  Call<ResponseBody> DoctorTotalPrscriptionslist(@Query("method") String method,
                                                 @Query("patient_id") String patient_id,
                                                 @Query("logged_in_user_id") String logged_in_user_id,
                                                 @Query("logged_in_role_id") String logged_in_role_id,
                                                 @Query("hospital_id") String hospital_id,
                                                 @Query("from_date") String from_date,
                                                 @Query("to_date") String to_date,
                                                 @Query("type") String type,
                                                 @Query("search_text") String search_text);

  @POST("app/index.php?")
  Call<ResponseBody> TotalPrescriptions(@Query("method") String method,
                                        @Query("doctor_id") String doctor_id,
                                        @Query("from_date") String from_date,
                                        @Query("to_date") String to_date,
                                        @Query("search_text") String search_text,
                                        @Query("logged_in_user_id") String logged_in_user_id,
                                        @Query("logged_in_role_id") String logged_in_role_id,
                                        @Query("patient_id") String patient_id);


  @POST("app/index.php?")
  Call<ResponseBody> OtherDoctorPrescriptions(@Query("method") String method,
                                              @Query("logged_in_user_id") String logged_in_user_id,
                                              @Query("logged_in_role_id") String logged_in_role_id,
                                              @Query("from_date") String from_date,
                                              @Query("to_date") String to_date,
                                              @Query("page") String page,
                                              @Query("records_per_page") String records_per_page,
                                              @Query("search_text") String search_text);

  @POST("app/index.php?")
  Call<ResponseBody> hospitalDrList(@Query("method") String method,
                                    @Query("logged_in_user_id") String logged_in_user_id,
                                    @Query("logged_in_role_id") String logged_in_role_id,
                                    @Query("page") String page,
                                    @Query("records_per_page") String records_per_page);

  @POST("app/index.php?")
  Call<ResponseBody> searchBrand(@Query("method") String method,
                                 @Query("term") String term);

  @POST("app/index.php?")
  Call<ResponseBody> alternateBrand(@Query("method") String method,
                                    @Query("brand_id") String brand_id);

  @POST("app/index.php?")
  Call<ResponseBody> HospitalTestSuggested(@Query("method") String method,
                                           @Query("doctor_id") String doctor_id,
                                           @Query("from_date") String from_date,
                                           @Query("to_date") String to_date,
                                           @Query("search_text") String search_text,
                                           @Query("logged_in_user_id") String logged_in_user_id,
                                           @Query("logged_in_role_id") String logged_in_role_id,
                                           @Query("page") String page,
                                           @Query("records_per_page") String records_per_page);

  @POST("app/index.php?")
  Call<ResponseBody> UpdatePrescription(@Query("method") String method,
                                        @Query("prescription_id") String prescription_id,
                                        @Query("problem") String problem,
                                        @Query("physical_examinations") String physical_examinations,
                                        @Query("assessment_plan") String assessment_plan,
                                        @Query("patient_advice") String patient_advice,
                                        @Query("doctors_note") String doctors_note,
                                        @Query("medication") String medication,
                                        @Query("tests_suggested") String tests_suggested,
                                        @Query("selected_brands") String selected_brands,
                                        @Query("selected_molecules") String selected_molecules);

  @POST("app/index.php?")
  Call<ResponseBody> prescriptiondetails(@Query("method") String method,
                                         @Query("logged_in_user_id") String logged_in_user_id,
                                         @Query("logged_in_role_id") String logged_in_role_id,
                                         @Query("prescription_id") String prescription_id);


  @POST("app/index.php?")
  Call<ResponseBody> SearchDoctor(@Query("method") String method,
                                  @Query("mobile") String mobile,
                                  @Query("logged_in_user_id") String logged_in_user_id,
                                  @Query("logged_in_role_id") String logged_in_role_id );

  @POST("app/index.php?")
  Call<ResponseBody> Patientlistmain(@Query("method") String method,
                                     @Query("logged_in_user_id") String logged_in_user_id,
                                     @Query("logged_in_role_id") String logged_in_role_id,
                                     @Query("from_date") String from_date,
                                     @Query("to_date") String to_date,
                                     @Query("page") String page,
                                     @Query("records_per_page") String records_per_page,
                                     @Query("search_text") String search_text,
                                     @Query("status") String status,
                                     @Query("doctor_id") String doctor_id);



  @POST("app/index.php?")
  Call<ResponseBody> TestSugestion(@Query("method") String method,
                                   @Query("logged_in_user_id") String logged_in_user_id,
                                   @Query("logged_in_role_id") String logged_in_role_id,
                                   @Query("page") String page,
                                   @Query("records_per_page") String records_per_page,
                                   @Query("search_text") String search_text,
                                   @Query("type") String type,
                                   @Query("report_from_date") String report_from_date,
                                   @Query("report_to_date") String report_to_date
                                   //@Query("patient_id") String patient_id
  );

  @POST("app/index.php?")
  Call<ResponseBody> Prscriptionslist(@Query("method") String method,
                                      @Query("logged_in_user_id") String logged_in_user_id,
                                      @Query("logged_in_role_id") String logged_in_role_id);
  @POST("app/index.php?")
  Call<ResponseBody> AddDiagnosis(@Query("method") String method,
                                  @Query("ref_doctor_name") String ref_doctor_name ,
                                  @Query("report_date") String report_date,
                                  @Query("diagnostic_name") String diagnostic_name,
                                  @Query("test_name") String test_name,
                                  @Query("report_image") String scanned_copy,
                                  @Query("report_details") String remarks,
                                  @Query("logged_in_user_id") String logged_in_user_id,
                                  @Query("logged_in_role_id") String logged_in_role_id,
                                  @Query("patient_id") String patient_id);



  @POST("app/index.php?")
  Call<ResponseBody> EditDiagnosis(@Query("method") String method,
                                   @Query("ref_doctor_name") String ref_doctor_name ,
                                   @Query("report_date") String report_date,
                                   @Query("diagnostic_name") String diagnostic_name,
                                   @Query("test_name") String test_name,
                                   @Query("report_image") String scanned_copy,
                                   @Query("report_details") String remarks,
                                   @Query("logged_in_user_id") String logged_in_user_id,
                                   @Query("logged_in_role_id") String logged_in_role_id,
                                   @Query("id") String id,
                                   @Query("patient_id") String patient_id);


  @POST("app/index.php?")
  Call<ResponseBody> AvailableDoctorList(@Query("method") String method,
                                     @Query("logged_in_user_id") String logged_in_user_id,
                                     @Query("logged_in_role_id") String logged_in_role_id);

  @POST("app/index.php?")
  Call<ResponseBody> BlockDoctorList(@Query("method") String method,
                                     @Query("logged_in_user_id") String logged_in_user_id,
                                     @Query("logged_in_role_id") String logged_in_role_id,
                                     @Query("page") String page,
                                     @Query("records_per_page") String records_per_page,
                                     @Query("specialization_id") String specialization_id );

  @POST("app/index.php?")
  Call<ResponseBody> BlockDoctor(@Query("method") String method,
                                 @Query("logged_in_user_id") String logged_in_user_id,
                                 @Query("logged_in_role_id") String logged_in_role_id,
                                 @Query("doctor_id") String doctor_id);


  @POST("app/index.php?")
  Call<ResponseBody> UnBlockDoctor(@Query("method") String method,
                                   @Query("logged_in_user_id") String logged_in_user_id,
                                   @Query("logged_in_role_id") String logged_in_role_id,
                                   @Query("id") String id);

  @POST("app/index.php?")
  Call<ResponseBody> patientaddedprescriptions(@Query("method") String method,
                                               @Query("logged_in_user_id") String logged_in_user_id,
                                               @Query("logged_in_role_id") String logged_in_role_id,
                                               @Query("prescription_id") String prescription_id);

  @POST("app/index.php?")
  Call<ResponseBody> addPrescriptions(@Query("method") String method,
                                      @Query("patient_id") String patient_id,
                                      @Query("appointment_date_time") String appointment_date_time,
                                      @Query("doctor_name") String doctor_name,
                                      @Query("problem") String problem,
                                      @Query("scanned_copy") String scanned_copy,
                                      @Query("medication") JSONArray medication,
                                      @Query("selected_brands") String selected_brands,
                                      @Query("selected_molecules") String selected_molecules,
                                      @Query("tests_suggested") JSONArray tests_suggested);




  @POST("app/index.php?")
  Call<ResponseBody> DocSearchPatient(@Query("method") String method,
                                       @Query("hospital_id") String hospital_id,
                                       @Query("mobile") String mobile,
                                       @Query("logged_in_user_id") String logged_in_user_id,
                                       @Query("logged_in_role_id") String logged_in_role_id);




}
