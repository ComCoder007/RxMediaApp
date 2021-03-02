package com.rxmediaapp.fragments.hospital;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.fragments.dashboards.H_Dashboard;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class H_Profile extends Fragment {

  ImageView backbtn_img, hos_profile_img,edit_image;
  TextView title_txt, hospital_name;
  LinearLayout info_lay, h_working_address_lay;
   EditText h_prfle_avabletme_edtx, h_prfle_tmetwo_edtx, hosp_regnum_edtxt, nobeds_edtxt, mobinum_edtxt, address_edtxt,hosp_name_edtxt,hosp_email_edtxt;
  CustomButton h_prfle_sbmt_btn;


  ArrayList<String> From_timeSlot_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> from_times_list = new ArrayList<>();

  ArrayList<String> to_timeSlot_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> to_times_list = new ArrayList<>();

  String  regnum = "", noofbeds = "", phone = "", frmtime = "", totime = "", address = "";
  String image_type = "",file_name_str = "";
  public static ArrayList<HashMap<String, String>> profile_list = new ArrayList<>();

  String fromtimeslot_id="",totimeslot_id="",first_time="yes",user_id="";
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.h_profile, null, false);
    StoredObjects.page_type = "h_prfile";
    SideMenu.updatemenu(StoredObjects.page_type);
    try {
      StoredObjects.listcount= 7;
      SideMenu.adapter.notifyDataSetChanged();
    }catch (Exception e){

    }
    initilization(v);

    if (InterNetChecker.isNetworkAvailable(getContext())) {
      getProfileService(getActivity());

    } else {
      StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
    }
    return v;
  }


  private void initilization(final View v) {

    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);
    title_txt.setText("Profile");

    hos_profile_img = v.findViewById(R.id.hos_profile_img);
    hospital_name = v.findViewById(R.id.hospital_name);
    h_prfle_avabletme_edtx = v.findViewById(R.id.h_prfle_avabletme_edtx);
    h_prfle_tmetwo_edtx = v.findViewById(R.id.h_prfle_tmetwo_edtx);
    h_prfle_sbmt_btn = v.findViewById(R.id.h_prfle_sbmt_btn);


    info_lay = v.findViewById(R.id.info_lay);
    h_working_address_lay = v.findViewById(R.id.h_working_address_lay);
    hosp_regnum_edtxt = v.findViewById(R.id.hosp_regnum_edtxt);
    nobeds_edtxt = v.findViewById(R.id.nobeds_edtxt);
    mobinum_edtxt = v.findViewById(R.id.mobinum_edtxt);
    address_edtxt = v.findViewById(R.id.address_edtxt);
    hosp_name_edtxt = v.findViewById(R.id.hosp_name_edtxt);
    hosp_email_edtxt = v.findViewById(R.id.hosp_email_edtxt);
    edit_image = v.findViewById(R.id.edit_image);


    address_edtxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
    address_edtxt.setRawInputType(InputType.TYPE_CLASS_TEXT);


    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        fragmentcallinglay(new H_Dashboard());
      }
    });


    hos_profile_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (CameraUtils.checkAndRequestPermissions(getActivity()) == true) {
          Imagepickingpopup(getActivity(), "hospital profile");
        }
      }
    });

    edit_image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (CameraUtils.checkAndRequestPermissions(getActivity()) == true) {
          Imagepickingpopup(getActivity(), "hospital profile");
        }
      }
    });

    mobinum_edtxt.setEnabled(false);
    h_prfle_sbmt_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        regnum = hosp_regnum_edtxt.getText().toString().trim();
        noofbeds = nobeds_edtxt.getText().toString().trim();
        phone = mobinum_edtxt.getText().toString().trim();
        frmtime = h_prfle_avabletme_edtx.getText().toString().trim();
        totime = h_prfle_tmetwo_edtx.getText().toString().trim();
        address = address_edtxt.getText().toString().trim();
        String email=hosp_email_edtxt.getText().toString().trim();
        String name=hosp_name_edtxt.getText().toString().trim();

        if(StoredObjects.inputValidation(hosp_name_edtxt,getString(R.string.enter_dr_name),getActivity())) {
          if(StoredObjects.Emailvalidation(email,getString(R.string.enter_valid_email),getActivity())){
            if(StoredObjects.inputValidation(hosp_regnum_edtxt,getString(R.string.year_reg_validation),getActivity())) {
              if(StoredObjects.inputValidation(nobeds_edtxt,getString(R.string.no_beds_validate),getActivity())) {
                if (StoredObjects.inputValidation(address_edtxt, getString(R.string.address_validation), getActivity())) {
                  if (StoredObjects.inputValidation(h_prfle_avabletme_edtx, getString(R.string.from_time_validate), getActivity())) {
                    if (StoredObjects.inputValidation(h_prfle_tmetwo_edtx, getString(R.string.to_time_validate), getActivity())) {


                      try {
                        SideMenu.header_name.setText(name);
                      }catch (Exception e){

                      }
                      if (InterNetChecker.isNetworkAvailable(getContext())) {
                        editProfileService(getActivity(),name,email);
                      } else {
                        StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
                      }


                    }
                  }
                }

              }

            }


          }


        }
        }

    });


    h_prfle_tmetwo_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        toTimeListPopup(h_prfle_tmetwo_edtx,getActivity());
      }
    });
    h_prfle_avabletme_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        h_prfle_tmetwo_edtx.setText("");
        totimeslot_id="";
        timeListPopup(h_prfle_avabletme_edtx,getActivity());
      }
    });


  }

  private void timeListPopup(final EditText prfilenme, final Activity activity) {
    final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
    listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, From_timeSlot_list));
    listPopupWindowone.setAnchorView(prfilenme);
    listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
    listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(From_timeSlot_list.get(position));
        String time_selected = From_timeSlot_list.get(position);
        fromtimeslot_id=from_times_list.get(position).get("time_slot");
        getToTimeService(activity, time_selected);
        listPopupWindowone.dismiss();

      }
    });

    listPopupWindowone.show();
  }

  private void toTimeListPopup(final EditText prfilenme,Activity activity) {
    final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
    listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, to_timeSlot_list));
    listPopupWindowone.setAnchorView(prfilenme);
    listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
    listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(to_timeSlot_list.get(position));
        totimeslot_id=to_times_list.get(position).get("time_slot");
        listPopupWindowone.dismiss();

      }
    });

    listPopupWindowone.show();
  }

  private void getTimeSlots(final Activity activity) {
    //  CustomProgressbar.Progressbarshow(activity);

    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getTimeSlots(RetrofitInstance.time_slots).enqueue(new Callback<ResponseBody>() {
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
              from_times_list = JsonParsing.GetJsonData(results);
              From_timeSlot_list.clear();
              for (int k = 0; k < from_times_list.size(); k++) {
                From_timeSlot_list.add(StoredObjects.time12hrsformat(from_times_list.get(k).get("time_slot")));
              }

            } else {
              StoredObjects.ToastMethod("No Data found", activity);
            }


          } catch (IOException | JSONException e) {
            e.printStackTrace();
          }
          CustomProgressbar.Progressbarcancel(activity);

          if(first_time.equalsIgnoreCase("yes")){

            getToTimeService(getActivity(), fromtimeslot_id);
          }
        }

      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        CustomProgressbar.Progressbarcancel(activity);

      }
    });
  }


  private void getToTimeService(final Activity activity, String time_selected) {
    if(first_time.equalsIgnoreCase("yes")){

    }else{
      CustomProgressbar.Progressbarshow(getActivity());
    }
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getToTime(RetrofitInstance.to_time, time_selected).enqueue(new Callback<ResponseBody>() {
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
              to_times_list = JsonParsing.GetJsonData(results);
              to_timeSlot_list.clear();
              for (int k = 0; k < to_times_list.size(); k++) {
                to_timeSlot_list.add(StoredObjects.time12hrsformat(to_times_list.get(k).get("time_slot")));
              }

            } else {
              StoredObjects.ToastMethod("No Data found", activity);
            }


          } catch (IOException | JSONException e) {
            e.printStackTrace();
          }
        }

        first_time="no";
        CustomProgressbar.Progressbarcancel(activity);
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        CustomProgressbar.Progressbarcancel(activity);

      }
    });
  }


  private void getProfileService(final Activity activity) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.gethospitalProfile(RetrofitInstance.hospital_profile, StoredObjects.Logged_HospitalId, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {

              String results = jsonObject.getString("results");
              profile_list = JsonParsing.GetJsonData(results);
              hosp_regnum_edtxt.setText(profile_list.get(0).get("hospital_registration_number"));
              nobeds_edtxt.setText(profile_list.get(0).get("no_of_beds"));
              mobinum_edtxt.setText(profile_list.get(0).get("phone"));
              address_edtxt.setText(profile_list.get(0).get("address"));
              hospital_name.setText(profile_list.get(0).get("name"));

              hosp_name_edtxt.setText(profile_list.get(0).get("name"));
              hosp_email_edtxt.setText(profile_list.get(0).get("email"));

              fromtimeslot_id = profile_list.get(0).get("from_time");
              totimeslot_id = profile_list.get(0).get("to_time");


              h_prfle_avabletme_edtx.setText(StoredObjects.time12hrsformat(fromtimeslot_id));
              h_prfle_tmetwo_edtx.setText(StoredObjects.time12hrsformat(totimeslot_id));
              file_name_str = profile_list.get(0).get("image");
              try {
                Glide.with(activity)
                        .load(Uri.parse(RetrofitInstance.BASEURL + profile_list.get(0).get("image")))
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.no_image)
                                .fitCenter()
                                .centerCrop())
                        .into(hos_profile_img);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          } catch (IOException | JSONException e) {
            e.printStackTrace();
          }

          if (first_time.equalsIgnoreCase("yes")) {
            if (InterNetChecker.isNetworkAvailable(getActivity())) {
              getTimeSlots(getActivity());
            } else {
              StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
            }
          } else {
            CustomProgressbar.Progressbarcancel(activity);
          }

        }
        //CustomProgressbar.Progressbarcancel(activity);
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        CustomProgressbar.Progressbarcancel(activity);

      }
    });

  }

  private void editProfileService(final Activity activity,String name,String email) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.edithospitalProfile(RetrofitInstance.edit_hospital_profile, StoredObjects.Logged_HospitalId, StoredObjects.UserId, StoredObjects.UserRoleId, name,
            ""+file_name_str, regnum, noofbeds, phone, fromtimeslot_id, totimeslot_id, address,email).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Updated Profile Successfully!", getActivity());
           /* if (InterNetChecker.isNetworkAvailable(getContext())) {
              getProfileService(getActivity());
            } else {
              StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
            }*/

            } else {
              StoredObjects.ToastMethod("Error", getActivity());
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

       /* if (InterNetChecker.isNetworkAvailable(getActivity())) {
            new ImageUploadingTask().execute(realPath);
        }else{
            if(img_type.equalsIgnoreCase("takephoto")){
                database.ImagesDataInsert(StoredObjects.User_Id, CameraUtils.imageStoragePath);
            }
            uploadimages(CameraUtils.imageStoragePath);
        }*/

  }


  public void fragmentcallinglay(Fragment fragment) {

    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
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

              try {
                Glide.with(getActivity())
                        .load(Uri.parse(RetrofitInstance.IMAGE_URL + file_name_str))
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.no_image)
                                .fitCenter()
                                .centerCrop())
                        .into(hos_profile_img);
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
                      .into(hos_profile_img);
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
            regnum = hosp_regnum_edtxt.getText().toString().trim();
            noofbeds = nobeds_edtxt.getText().toString().trim();
            phone = mobinum_edtxt.getText().toString().trim();
            frmtime = h_prfle_avabletme_edtx.getText().toString().trim();
            totime = h_prfle_tmetwo_edtx.getText().toString().trim();
            address = address_edtxt.getText().toString().trim();
            String email=hosp_email_edtxt.getText().toString().trim();
            String name=hosp_name_edtxt.getText().toString().trim();

            editProfileService(getActivity(),name,email);
          }else{

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
