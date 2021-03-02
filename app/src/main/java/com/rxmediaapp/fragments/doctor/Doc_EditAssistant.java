package com.rxmediaapp.fragments.doctor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

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

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customadapter.HashMapRecycleviewadapter;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.customfonts.CustomEditText;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Doc_EditAssistant extends Fragment {

  ImageView backbtn_img, doc_editImg;
  TextView title_txt;
  CustomButton docas_sbmt_btn;
  CustomEditText docas_nme_edtx, docas_avabletme_edtx, docas_tmetwo_edtx,docas_email_edtx, docas_mbile_edtx, docas_pswd_edtx, docas_cnfmpswd_edtx;

  public static int doctor_edit_assistant_position;
  public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();

 /* ArrayList<String> timeSlot_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> times_list = new ArrayList<>();

  ArrayList<String> to_timeSlot_list = new ArrayList<>();
  ArrayList<HashMap<String, String>> to_times_list = new ArrayList<>();*/
  String image_type = "";

  //String fromtimeslot_id="",totimeslot_id="";
  String first_time="yes";
  public Doc_EditAssistant() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.doc_edit_assistant, container, false);
    StoredObjects.page_type = "d_edit_assistant";
    SideMenu.updatemenu(StoredObjects.page_type);

    initilization(view);
    assignData();
    return view;
  }

  private void assignData() {
    try {
      docas_nme_edtx.setText(data_list.get(0).get("name"));
      docas_mbile_edtx.setText(data_list.get(0).get("phone"));

      docas_email_edtx.setText(data_list.get(0).get("email"));

   /*   fromtimeslot_id=data_list.get(0).get("from_time");
      totimeslot_id=data_list.get(0).get("to_time");
      docas_avabletme_edtx.setText(StoredObjects.time12hrsformat(fromtimeslot_id));
      docas_tmetwo_edtx.setText(StoredObjects.time12hrsformat(totimeslot_id));*/
      //getTimeSlots(getActivity());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private void initilization(View v) {

    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);
    title_txt.setText("Edit Assistant");

    docas_nme_edtx = v.findViewById(R.id.docas_nme_edtx);
    docas_avabletme_edtx = v.findViewById(R.id.docas_avabletme_edtx);
    docas_tmetwo_edtx = v.findViewById(R.id.docas_tmetwo_edtx);
    docas_mbile_edtx = v.findViewById(R.id.docas_mbile_edtx);
    docas_pswd_edtx = v.findViewById(R.id.docas_pswd_edtx);
    docas_cnfmpswd_edtx = v.findViewById(R.id.docas_cnfmpswd_edtx);
    docas_sbmt_btn = v.findViewById(R.id.docas_sbmt_btn);
    doc_editImg = v.findViewById(R.id.doc_editImg);
    docas_email_edtx=v.findViewById(R.id.docas_email_edtx);

    doc_editImg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (CameraUtils.checkAndRequestPermissions(getActivity())) {
          Imagepickingpopup(getActivity(), "doctor_edit_assistant");
        }
      }
    });
    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
          fm.popBackStack();
        }
      }
    });

    /*docas_tmetwo_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        toTimeListPopup(docas_tmetwo_edtx,getActivity());
      }
    });
    docas_avabletme_edtx.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        docas_tmetwo_edtx.setText("");
        totimeslot_id="";
        timeListPopup(docas_avabletme_edtx,getActivity());
      }
    });*/
    docas_mbile_edtx.setEnabled(false);

    docas_sbmt_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String docas_nme_str = docas_nme_edtx.getText().toString().trim();
        String docas_mbile_str = docas_mbile_edtx.getText().toString().trim();
        String email = docas_email_edtx.getText().toString().trim();
        String assistant_id_str = data_list.get(0).get("assistant_id");


        if (StoredObjects.inputValidation(docas_nme_edtx, getString(R.string.enter_dr_name), getActivity())) {
                if (StoredObjects.Emailvalidation(email, getString(R.string.enter_valid_email), getActivity())) {
                  if (StoredObjects.inputValidation(docas_mbile_edtx, getString(R.string.enter_mobile_validation), getActivity())) {
                    editAssistantService(getActivity(), docas_nme_str, docas_mbile_str, email, assistant_id_str);

                  }
                }

        }

      }
    });
  }
/*

  private void timeListPopup(final CustomEditText prfilenme, final Activity activity) {
    final ListPopupWindow listPopupWindowone = new ListPopupWindow(activity);
    listPopupWindowone.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, timeSlot_list));
    listPopupWindowone.setAnchorView(prfilenme);
    listPopupWindowone.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);
    listPopupWindowone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prfilenme.setText(timeSlot_list.get(position));
        fromtimeslot_id=times_list.get(position).get("time_slot");
        getToTimeService(activity, fromtimeslot_id);
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
    CustomProgressbar.Progressbarshow(activity);
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
              times_list = JsonParsing.GetJsonData(results);
              timeSlot_list.clear();
              for (int k = 0; k < times_list.size(); k++) {
                timeSlot_list.add(StoredObjects.time12hrsformat(times_list.get(k).get("time_slot")));
              }
            } else {
              StoredObjects.ToastMethod("No Data found", activity);
            }

          } catch (IOException | JSONException e) {
            e.printStackTrace();
          }

          getToTimeService(getActivity(), fromtimeslot_id);
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
      first_time="no";

    }else{
      CustomProgressbar.Progressbarshow(activity);
    }

    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getToTime(RetrofitInstance.to_time, time_selected).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
        CustomProgressbar.Progressbarcancel(activity);

      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        CustomProgressbar.Progressbarcancel(activity);

      }
    });
  }
*/

  private void editAssistantService(final Activity activity, String name_str, String mobile_str,String email, String assistant_id_str) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);

    api.drEditAssistant(RetrofitInstance.doctor_edit_assistant, name_str, mobile_str,email,  assistant_id_str, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {

            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);

            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Edited successfully", activity);

              fragmentcallinglay(new Doc_Assistant());
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
        postFile(realPath, RetrofitInstance.BASEURL + "app/index.php", file.getName());
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


    CustomProgressbar.Progressbarshow(getActivity());
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
        CustomProgressbar.Progressbarcancel(getActivity());
      } else {
        CustomProgressbar.Progressbarcancel(getActivity());
      }
      StoredObjects.LogMethod("val", "val::" + responseReceived);
    } catch (IOException e) {

    }


  }

}
