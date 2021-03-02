package com.rxmediaapp.fragments.patient;

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
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.activities.PatientSignup;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.fragments.dashboards.P_Dashboard;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.CameraUtils;
import com.rxmediaapp.storedobjects.StoredObjects;

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
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class Add_Patient_Profile extends Fragment {

  ImageView backbtn_img, patient_image,edit_image;
  TextView title_txt, patient_name_txt;
  CustomEditText p_dob_edtx, p_nme_edtx, p_email_edtx, p_mbile_edtx, p_adhar_edtx, p_bldgrup_edtx, p_sgnup_gender_edtx;
  Button p_save_btn;
  public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
  DatePickerDialog datePickerDialog;
  int year;
  int month;
  int dayOfMonth;
  Calendar calendar;
  String[] genderlist = {"Male", "Female"};
  String image_type = "", file_name_str = "";

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.add_patient_profile, null, false);
    StoredObjects.page_type = "add_patient_profile";

    SideMenu.updatemenu(StoredObjects.page_type);
    try {
      StoredObjects.listcount= 6;
      SideMenu.adapter.notifyDataSetChanged();
    }catch (Exception e){

    }
    initilization(v);
    if (InterNetChecker.isNetworkAvailable(getContext())) {
      getPatientProfileService(getActivity());
    } else {

    }
    return v;
  }
  String blood_id="";
  private void getPatientProfileService(final Activity activity) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getPatientProfile(RetrofitInstance.patient_profile, StoredObjects.Logged_PatientId, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
              data_list = JsonParsing.GetJsonData(results);

              patient_name_txt.setText(data_list.get(0).get("name"));
              p_nme_edtx.setText(data_list.get(0).get("name"));
              p_email_edtx.setText(data_list.get(0).get("email"));
              p_mbile_edtx.setText(data_list.get(0).get("phone"));
              p_adhar_edtx.setText(data_list.get(0).get("aadhar_number"));
             // p_bldgrup_edtx.setText(data_list.get(0).get("blood_group"));
              p_dob_edtx.setText(StoredObjects.convertDateformat(data_list.get(0).get("date_of_birth")));
              p_sgnup_gender_edtx.setText(data_list.get(0).get("gender"));

              try {
                Glide.with(activity)
                        .load(Uri.parse(RetrofitInstance.BASEURL + data_list.get(0).get("image")))
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.no_image)
                                .fitCenter()
                                .centerCrop())
                        .into(patient_image);

              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          } catch (IOException | JSONException e) {
            e.printStackTrace();
          }

        }
       // CustomProgressbar.Progressbarcancel(activity);

        if (InterNetChecker.isNetworkAvailable(getActivity())) {
          getBloodGroup(getActivity());

        } else {
          StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.nointernet), getActivity());
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        CustomProgressbar.Progressbarcancel(activity);

      }
    });


  }

  private void BloodgroupPopup(final CustomEditText prfilenme,Activity activity) {
    final ListPopupWindow dropdownpopup = new ListPopupWindow(activity);
    dropdownpopup.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, bloodgroupnames));
    dropdownpopup.setAnchorView(prfilenme);
    dropdownpopup.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    dropdownpopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        blood_id=bloodgroup_list.get(position).get("id");
        prfilenme.setText(bloodgroupnames.get(position));
        dropdownpopup.dismiss();

      }
    });

    dropdownpopup.show();
  }
  ArrayList<String> bloodgroupnames = new ArrayList<>();
  ArrayList<HashMap<String, String>> bloodgroup_list = new ArrayList<>();
  private void getBloodGroup(final Activity activity) {


    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getBloodgroup(RetrofitInstance.blood_groups).enqueue(new Callback<ResponseBody>() {
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
              bloodgroup_list = JsonParsing.GetJsonData(results);
              bloodgroupnames.clear();
              for(int k = 0; k < bloodgroup_list.size(); k++) {
                if(data_list.get(0).get("blood_group").equalsIgnoreCase(bloodgroup_list.get(k).get("id"))){
                  blood_id=bloodgroup_list.get(k).get("id");
                  p_bldgrup_edtx.setText(bloodgroup_list.get(k).get("name"));
                }

                bloodgroupnames.add(bloodgroup_list.get(k).get("name"));
              }

            } else {
              StoredObjects.ToastMethod("No Data found", activity);
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
  private void initilization(View v) {

    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);

    patient_image = v.findViewById(R.id.patient_image);
    patient_name_txt = v.findViewById(R.id.patient_name_txt);
    p_nme_edtx = v.findViewById(R.id.p_nme_edtx);
    p_email_edtx = v.findViewById(R.id.p_email_edtx);
    p_mbile_edtx = v.findViewById(R.id.p_mbile_edtx);
    p_adhar_edtx = v.findViewById(R.id.p_adhar_edtx);
    p_bldgrup_edtx = v.findViewById(R.id.p_bldgrup_edtx);
    p_sgnup_gender_edtx = v.findViewById(R.id.p_sgnup_gender_edtx);
    edit_image = v.findViewById(R.id.edit_image);

    p_save_btn = v.findViewById(R.id.p_save_btn);
    p_dob_edtx = v.findViewById(R.id.p_dob_edtx);
    title_txt.setText("Profile");

    p_bldgrup_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        BloodgroupPopup(p_bldgrup_edtx,getActivity());
      }
    });

    patient_image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (CameraUtils.checkAndRequestPermissions(getActivity())) {
          Imagepickingpopup(getActivity(), "patient profile");
        }
      }
    });

    edit_image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (CameraUtils.checkAndRequestPermissions(getActivity())) {
          Imagepickingpopup(getActivity(), "patient profile");
        }
      }
    });

    p_mbile_edtx.setEnabled(false);
    p_save_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String name_str = p_nme_edtx.getText().toString().trim();
        String mobile_str = p_mbile_edtx.getText().toString().trim();
        String email_str = p_email_edtx.getText().toString().trim();
        String aadhar_str = p_adhar_edtx.getText().toString().trim();
        String dob_str = p_dob_edtx.getText().toString().trim();
        String gender_str = p_sgnup_gender_edtx.getText().toString().trim();
        String blood_grp_str = p_bldgrup_edtx.getText().toString().trim();

        if(StoredObjects.inputValidation(p_nme_edtx,getString(R.string.dr_name),getActivity()))
        {
          if(StoredObjects.Emailvalidation(email_str,getString(R.string.enter_valid_email),getActivity()))
          {
            if (StoredObjects.inputValidation(p_dob_edtx, getString(R.string.dob_validation), getActivity())) {
              if(StoredObjects.inputValidation(p_sgnup_gender_edtx,getString(R.string.gender_validate),getActivity()))
              {
                if(StoredObjects.inputValidation(p_bldgrup_edtx,getString(R.string.blood_validation),getActivity()))
                {

                  try {
                    patient_name_txt.setText(name_str);
                    SideMenu.header_name.setText(name_str);
                  }catch (Exception e){

                  }
                  editPatientProfileService(getActivity(), name_str, email_str, mobile_str, aadhar_str, dob_str, gender_str, blood_grp_str);

                }
              }

            }

          }
        }


      }
    });


    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        fragmentcallinglay(new P_Dashboard());
      }
    });

    p_dob_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(),
            new DatePickerDialog.OnDateSetListener() {
              @Override
              public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                p_dob_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
              }
            }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
      }
    });

    p_sgnup_gender_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        GenderListPopup(p_sgnup_gender_edtx, getActivity());
      }
    });


  }

  private void GenderListPopup(final CustomEditText prfilenme, Activity activity) {
    final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
    listPopupWindow.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, genderlist));
    listPopupWindow.setAnchorView(prfilenme);
    listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(genderlist[position]);
        listPopupWindow.dismiss();

      }
    });

    listPopupWindow.show();
  }

  private void editPatientProfileService(final Activity activity, String name_str, String email_str, String mobile_str, String aadhar_str, String dob_str, String gender_str, String blood_grp_str) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);

    if(file_name_str.equalsIgnoreCase("")){
      api.editPatientProfile(RetrofitInstance.edit_patient_profile, name_str, email_str, mobile_str, aadhar_str, dob_str, gender_str, blood_id, StoredObjects.Logged_PatientId, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
          if(response.body() != null) {
            try {
              String responseReceived = response.body().string();
              JSONObject jsonObject = new JSONObject(responseReceived);
              StoredObjects.LogMethod("response", "response::" + responseReceived);
              String status = jsonObject.getString("status");
              if (status.equalsIgnoreCase("200")) {
                StoredObjects.ToastMethod("Saved Successfully!", activity);
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
    }else{
      api.editPatientProfile_img(RetrofitInstance.edit_patient_profile, name_str,file_name_str, email_str, mobile_str, aadhar_str, dob_str, gender_str, blood_grp_str, StoredObjects.Logged_PatientId, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
          if(response.body() != null) {
            try {
              String responseReceived = response.body().string();
              JSONObject jsonObject = new JSONObject(responseReceived);
              StoredObjects.LogMethod("response", "response::" + responseReceived);
              String status = jsonObject.getString("status");
              if (status.equalsIgnoreCase("200")) {
                StoredObjects.ToastMethod("Updated Successfully!", activity);
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



  }


  private void Imagepickingpopup(final Activity activity, final String type) {

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
        image_type = type;
        captureImage();

        dialog.dismiss();
      }
    });

    pickglry_lay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        image_type = type;

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);


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

    Uri fileUri = CameraUtils.getOutputMediaFileUri(getActivity(), file);

    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

    // start the image capture Intent
    startActivityForResult(intent, CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
  }

  private Uri picUri;

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //user is returning from capturing an image using the camera
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == CameraUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        // Refreshing the gallery
        CameraUtils.refreshGallery(getActivity(), CameraUtils.imageStoragePath);

        try {
          f_new = createNewFile("CROP_");
          try {
            f_new.createNewFile();
          } catch (IOException ex) {
            Log.e("io", ex.getMessage());
          }
          //Photo_SHowDialog(SignUp.this(),f_new,imageStoragePath,myBitmap);
          imageupload(getActivity(), CameraUtils.imageStoragePath);
        } catch (Exception e) {
          e.printStackTrace();
          StoredObjects.LogMethod("", "imagepathexpection:--" + e);

        }
        // successfully captured the image
        // display it in image view
        // Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

      } else if (resultCode == RESULT_CANCELED) {
        // user cancelled Image capture
        Toast.makeText(getActivity(),
            "User cancelled image capture", Toast.LENGTH_SHORT)
            .show();
      } else {
        // failed to capture image
        Toast.makeText(getActivity(),
            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
            .show();
      }
    } else if (requestCode == 2) {

      StoredObjects.LogMethod("resultcode", "result code" + resultCode);
      if (resultCode == RESULT_OK) {
        Uri selectedImage = data.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
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
            imageupload(getActivity(), picturePath);
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
        Toast.makeText(getActivity(),
            "User cancelled image picking", Toast.LENGTH_SHORT)
            .show();
      } else {
        // failed to capture image
        Toast.makeText(getActivity(),
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
        Log.d(getActivity().getClass().getName(), newDirectory.getAbsolutePath() + " directory created");
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

      File file = new File(getActivity().getFilesDir(), "UploadImages"
          + new Random().nextInt() + ".png");

      FileOutputStream out;
      int currentAPIVersion = Build.VERSION.SDK_INT;
      if (currentAPIVersion > Build.VERSION_CODES.M) {
        out = getActivity().openFileOutput(file.getName(),
            Context.MODE_PRIVATE);
      } else {
        out = getActivity().openFileOutput(file.getName(),
            Context.MODE_WORLD_READABLE);
      }

      mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
      out.flush();
      out.close();
      //get absolute path
      new Compressor(getActivity())
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
    if (InterNetChecker.isNetworkAvailable(getActivity())) {

      File file = new File(realPath);

      try {

       // postFile(realPath, RetrofitInstance.BASEURL + "app/index.php", file.getName());
        new ImageuploadTask().execute(realPath, file.getName());
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {

      StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.nointernet), getActivity());
    }

  }


  public void fragmentcallinglay(Fragment fragment) {

    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace(R.id.frame_container, fragment).commit();

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
        file_name_str = jsonObject.getString("file_name");
       // patient_image.setImageBitmap(myImg);

        try {
          Glide.with(getActivity())
                  .load(Uri.parse(RetrofitInstance.IMAGE_URL + file_name_str))
                  .apply(new RequestOptions()
                          .placeholder(R.drawable.no_image)
                          .fitCenter()
                          .centerCrop())
                  .into(patient_image);
        } catch (Exception e) {
          e.printStackTrace();

        }
        CustomProgressbar.Progressbarcancel(getActivity());
      } else {
        CustomProgressbar.Progressbarcancel(getActivity());
      }
      StoredObjects.LogMethod("val", "val::" + responseReceived);
    } catch (IOException | JSONException e) {

    }
  }

  public class ImageuploadTask extends AsyncTask<String, Integer, String> {


    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      CustomProgressbar.Progressbarshow(getActivity());
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

      CustomProgressbar.Progressbarcancel(getActivity());

      if (response != null) {
        try {

          JSONObject jsonObject = new JSONObject(response);

          String status = jsonObject.getString("status");
          if (status.equalsIgnoreCase("200")) {

            file_name_str = jsonObject.getString("file_name");
            // patient_image.setImageBitmap(myImg);
            try {
              Glide.with(getActivity())
                      .load(Uri.parse(RetrofitInstance.IMAGE_URL + file_name_str))
                      .apply(new RequestOptions()
                              .placeholder(R.drawable.no_image)
                              .fitCenter()
                              .centerCrop())
                      .into(patient_image);
              Glide.with(getActivity())
                      .load(Uri.parse(RetrofitInstance.IMAGE_URL + file_name_str))
                      .apply(new RequestOptions()
                              .placeholder(R.drawable.no_image)
                              .fitCenter()
                              .centerCrop())
                      .into(SideMenu.header_circular_img);

            } catch (Exception e) {
              e.printStackTrace();

            }

            String name_str = p_nme_edtx.getText().toString().trim();
            String mobile_str = p_mbile_edtx.getText().toString().trim();
            String email_str = p_email_edtx.getText().toString().trim();
            String aadhar_str = p_adhar_edtx.getText().toString().trim();
            String dob_str = p_dob_edtx.getText().toString().trim();
            String gender_str = p_sgnup_gender_edtx.getText().toString().trim();
            String blood_grp_str = p_bldgrup_edtx.getText().toString().trim();


            editPatientProfileService(getActivity(), name_str, email_str, mobile_str, aadhar_str, dob_str, gender_str, blood_grp_str);


          }


        } catch (JSONException e) {
          e.printStackTrace();
        }
      } else {
        Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
      }

    }
  }

}

