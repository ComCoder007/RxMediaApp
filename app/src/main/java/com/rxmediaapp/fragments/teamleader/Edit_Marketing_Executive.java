package com.rxmediaapp.fragments.teamleader;

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
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.rxmediaapp.customfonts.CustomRegularTextView;
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

public class Edit_Marketing_Executive extends Fragment {

  ImageView backbtn_img;
  TextView title_txt;
  EditText me_ref_frm_edtxt, me_nme_edtx, me_email_edtx, me_mbile_edtx, me_adhar_edtx;
  CustomButton me_sgnup_btn;
  String user_id="";
  public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();

  ImageView marketing_exec_image;
  String image_type = "",filename="";
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.edit_marketing_executive, null, false);
    StoredObjects.page_type = "edit_marketing_exicutive";

    SideMenu.updatemenu(StoredObjects.page_type);
    initilization(v);
    assignData();
    return v;
  }

  private void assignData() {
    try {
      user_id =data_list.get(0).get("user_id");
      me_ref_frm_edtxt.setText(data_list.get(0).get("reference_from"));
      me_nme_edtx.setText(data_list.get(0).get("name"));
      me_email_edtx.setText(data_list.get(0).get("email"));
      me_mbile_edtx.setText(data_list.get(0).get("phone"));
      me_adhar_edtx.setText(data_list.get(0).get("aadhar_number"));

      try {
        Glide.with(getActivity())
                .load(Uri.parse(RetrofitInstance.IMAGE_URL + data_list.get(0).get("image")))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter()
                        .centerCrop())
                .into(marketing_exec_image);
      } catch (Exception e) {
        e.printStackTrace();

      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private void initilization(View v) {

    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);

    me_ref_frm_edtxt = v.findViewById(R.id.me_ref_frm_edtxt);
    me_nme_edtx = v.findViewById(R.id.me_nme_edtx);
    me_email_edtx = v.findViewById(R.id.me_email_edtx);
    me_mbile_edtx = v.findViewById(R.id.me_mbile_edtx);
    me_adhar_edtx = v.findViewById(R.id.me_adhar_edtx);
    me_sgnup_btn = v.findViewById(R.id.me_sgnup_btn);
    marketing_exec_image = v.findViewById(R.id.marketing_exec_image);

    title_txt.setText("Edit Marketing Executive");

    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
          fm.popBackStack();
        }
      }
    });

    me_mbile_edtx.setEnabled(false);
    marketing_exec_image.setVisibility(View.GONE);
    me_sgnup_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String reference_from_str = me_ref_frm_edtxt.getText().toString().trim();
        String name_str = me_nme_edtx.getText().toString().trim();
        String email_str = me_email_edtx.getText().toString().trim();
        String mobile_str = me_mbile_edtx.getText().toString().trim();
        String aadhar_str = me_adhar_edtx.getText().toString().trim();


        if (StoredObjects.inputValidation(me_nme_edtx, getString(R.string.enter_dr_name), getActivity())) {
          if (StoredObjects.Emailvalidation(email_str, getString(R.string.enter_valid_email), getActivity())) {
            if (InterNetChecker.isNetworkAvailable(getContext())) {
              editMarketingExecutiveService(getActivity(), reference_from_str, name_str, email_str, mobile_str, aadhar_str, user_id);
            } else {
              StoredObjects.ToastMethod(getString(R.string.nointernet), getContext());
            }

          }
        }

      }

    });

    marketing_exec_image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (CameraUtils.checkAndRequestPermissions(getActivity()) == true) {
          Imagepickingpopup(getActivity(), "edit");
        }
      }
    });

  }

  private void editMarketingExecutiveService(final Activity activity, String reference_from_str, String name_str, String email_str, String mobile_str, String aadhar_str,String user_id_str) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.editMarketExecutive(RetrofitInstance.edit_marketing_executives,reference_from_str,name_str,email_str,aadhar_str,mobile_str,StoredObjects.UserId,StoredObjects.UserRoleId,user_id_str).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("response", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Edited successfully!", activity);
              fragmentcallinglay(new Marketing_Exicutive());
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
    /*if(filename.equalsIgnoreCase("")){


    }else{
      api.editMarketExecutive_img(RetrofitInstance.edit_marketing_executives,reference_from_str,name_str,email_str,aadhar_str,mobile_str,StoredObjects.UserId,StoredObjects.UserRoleId,user_id_str,filename).enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("response", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Edited successfully!",activity);
              fragmentcallinglay(new Marketing_Exicutive());
            }else{
              String error = jsonObject.getString("error");
              StoredObjects.ToastMethod(error, activity);

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

    }*/


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
      CustomProgressbar.Progressbarshow(getActivity());
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
      StoredObjects.LogMethod("response_image",""+responseReceived);
      if (response.code() == 200) {
        JSONObject jsonObject = new JSONObject(responseReceived);
        filename = jsonObject.getString("file_name");
        CustomProgressbar.Progressbarcancel(getActivity());
        marketing_exec_image.setImageBitmap(myImg);
      } else {
        CustomProgressbar.Progressbarcancel(getActivity());
      }
      StoredObjects.LogMethod("val", "val::" + responseReceived);
    } catch (IOException | JSONException e) {

    }


  }
}
