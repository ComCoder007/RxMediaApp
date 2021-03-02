package com.rxmediaapp.fragments.assistant;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.drm.DrmStore;
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
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.fragments.dashboards.Asst_Dashboard;
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
import static androidx.constraintlayout.widget.Constraints.TAG;

public class AssistantProfile extends Fragment {

  ImageView backbtn_img, patient_image,edit_image;
  TextView title_txt,prfh_name_txt;
  CustomEditText prf_nme_edtx, prf_email_edtx, prf_mbile_edtx, p_adhar_edtx,as_avabletme_edtx,as_tmetwo_edtx;
  CustomButton prf_save_btn;
  ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
  String image_type = "", file_name_str = "",fromtimeslot_id="",totimeslot_id="",first_time="yes";
  LinearLayout timeslots_lay;
  TextView p_adhar_txt;

 /* ArrayList<String> From_timeSlot_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> from_times_list = new ArrayList<>();

  ArrayList<String> to_timeSlot_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> to_times_list = new ArrayList<>();
*/

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.commonprofile, null, false);
    StoredObjects.page_type = "ass_profile";

    SideMenu.updatemenu(StoredObjects.page_type);
    try {
      StoredObjects.listcount= 4;
      SideMenu.adapter.notifyDataSetChanged();
    }catch (Exception e){

    }
    initilization(v);
    if (InterNetChecker.isNetworkAvailable(getContext())) {
      assitantProfileService(getActivity());
    } else {
      StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
    }

    return v;
  }


  private void initilization(View v) {

    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);
    prf_nme_edtx = v.findViewById(R.id.prf_nme_edtx);
    prf_email_edtx = v.findViewById(R.id.prf_email_edtx);
    prf_mbile_edtx = v.findViewById(R.id.prf_mbile_edtx);
    p_adhar_edtx = v.findViewById(R.id.p_adhar_edtx);
    patient_image = v.findViewById(R.id.patient_image);
    prf_save_btn = v.findViewById(R.id.prf_save_btn);
    timeslots_lay= v.findViewById(R.id.timeslots_lay);
    as_tmetwo_edtx= v.findViewById(R.id.as_tmetwo_edtx);
    as_avabletme_edtx= v.findViewById(R.id.as_avabletme_edtx);
    prfh_name_txt=v.findViewById(R.id.prfh_name_txt);
    edit_image=v.findViewById(R.id.edit_image);
    p_adhar_txt=v.findViewById(R.id.p_adhar_txt);

    timeslots_lay.setVisibility(View.GONE);
    p_adhar_edtx.setVisibility(View.GONE);
    p_adhar_txt.setVisibility(View.GONE);

    title_txt.setText("Profile");

   /* as_avabletme_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        as_tmetwo_edtx.setText("");
        totimeslot_id="";
        timeListPopup(as_avabletme_edtx,getActivity());
      }
    });

    as_tmetwo_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        toTimeListPopup(as_tmetwo_edtx,getActivity());
      }
    });*/



    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        fragmentcallinglay(new Asst_Dashboard());
      }
    });

    patient_image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (CameraUtils.checkAndRequestPermissions(getActivity()) == true) {
          Imagepickingpopup(getActivity(), "assistant profile");
        }

      }
    });

    edit_image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (CameraUtils.checkAndRequestPermissions(getActivity()) == true) {
          Imagepickingpopup(getActivity(), "assistant profile");
        }

      }
    });


    prf_mbile_edtx.setEnabled(false);
    prf_save_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String name_str = prf_nme_edtx.getText().toString().trim();
        String prf_mbile_str = prf_mbile_edtx.getText().toString().trim();
        String prf_email_str = prf_email_edtx.getText().toString().trim();

        if (StoredObjects.inputValidation(prf_nme_edtx, getString(R.string.enter_dr_name), getActivity())) {
          if (StoredObjects.Emailvalidation(prf_email_str,getString(R.string.enter_valid_email), getActivity())) {
                        try {
                          SideMenu.header_name.setText(name_str);
                          prfh_name_txt.setText(name_str);
                        }catch (Exception e){

                        }
                        editAssistantProfileService(getActivity(), name_str, prf_mbile_str, prf_email_str);
            }

        }

      }
    });


  }

 /* private void timeListPopup(final CustomEditText prfilenme, final Activity activity) {
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

  private void toTimeListPopup(final CustomEditText prfilenme,Activity activity) {
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



            if (InterNetChecker.isNetworkAvailable(getActivity())) {
              getToTimeService(getActivity(), totimeslot_id);
            } else {
              StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
            }

          } catch (IOException | JSONException e) {
            e.printStackTrace();
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

    if(first_time.equalsIgnoreCase("Yes")){
      first_time="No";
    }else {
      CustomProgressbar.Progressbarshow(activity);
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

        CustomProgressbar.Progressbarcancel(activity);
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        CustomProgressbar.Progressbarcancel(activity);

      }
    });
  }
 */ private void editAssistantProfileService(final Activity activity, String name_str, String prf_mbile_str, String prf_email_str) {

    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);

    if(file_name_str.equalsIgnoreCase("")){
      api.editAssistantProfile(RetrofitInstance.edit_assistant_profile, name_str, prf_email_str, prf_mbile_str, StoredObjects.Logged_AssistantId, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
          if(response.body() != null) {
            try {
              String responseReceived = response.body().string();
              JSONObject jsonObject = new JSONObject(responseReceived);
              StoredObjects.LogMethod("response", "response::" + responseReceived);
              String status = jsonObject.getString("status");
              if (status.equalsIgnoreCase("200")) {
                StoredObjects.ToastMethod("Updated successfully", activity);
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
      api.editAssistantProfile_img(RetrofitInstance.edit_assistant_profile, name_str, file_name_str, prf_email_str, prf_mbile_str, StoredObjects.Logged_AssistantId, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
          if(response.body() != null) {
            try {
              String responseReceived = response.body().string();
              JSONObject jsonObject = new JSONObject(responseReceived);
              StoredObjects.LogMethod("response", "response::" + responseReceived);
              String status = jsonObject.getString("status");
              if (status.equalsIgnoreCase("200")) {
                StoredObjects.ToastMethod("Saved successfully", activity);
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

  private void assitantProfileService(final Activity activity) {

    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.assistantProfile(RetrofitInstance.assistant_profile, StoredObjects.Logged_AssistantId, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
              prf_nme_edtx.setText(data_list.get(0).get("name"));
              prf_email_edtx.setText(data_list.get(0).get("email"));
              prf_mbile_edtx.setText(data_list.get(0).get("phone"));
              fromtimeslot_id = data_list.get(0).get("from_time");
              totimeslot_id = data_list.get(0).get("to_time");
              prfh_name_txt.setText(data_list.get(0).get("name"));
              as_avabletme_edtx.setText(StoredObjects.time12hrsformat(fromtimeslot_id));
              as_tmetwo_edtx.setText(StoredObjects.time12hrsformat(totimeslot_id));


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
          /*if (InterNetChecker.isNetworkAvailable(getActivity())) {
            getTimeSlots(getActivity());
          } else {
            StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
          }*/

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
    File file = new File(realPath);
    if (InterNetChecker.isNetworkAvailable(getActivity())) {
      try {
        //postRequest(getActivity(),realPath,RetrofitInstance.IMAGEUPLOADURL ,file.getName());

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
      StoredObjects.LogMethod("response_image",""+responseReceived);
      JSONObject jsonObject = new JSONObject(responseReceived);
      if (response.code() == 200) {
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

            String name_str = prf_nme_edtx.getText().toString().trim();
            String prf_mbile_str = prf_mbile_edtx.getText().toString().trim();
            String prf_email_str = prf_email_edtx.getText().toString().trim();

            editAssistantProfileService(getActivity(), name_str, prf_mbile_str, prf_email_str);

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


