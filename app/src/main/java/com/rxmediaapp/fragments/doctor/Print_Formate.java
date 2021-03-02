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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rxmediaapp.R;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.fragments.dashboards.Doc_Dashboard;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
import com.rxmediaapp.serviceparsing.JsonParsing;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.CameraUtils;
import com.rxmediaapp.storedobjects.StoredObjects;
import com.rxmediaapp.Sidemenu.SideMenu;

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


public class Print_Formate extends Fragment {

  ImageView backbtn_img, logo_img, signature_img;
  TextView title_txt;
  LinearLayout addphy_lay;
  CustomEditText enter_header_edtxt;
  CustomButton logo_browse_btn, signature_browse_btn;
  public static ArrayList<HashMap<String, String>> data_list = new ArrayList<>();
  Button submit_btn;
  String image_type = "";
  String file_name_logo = "";
  String file_name_sign = "";

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.print_formate, null, false);
    StoredObjects.page_type = "print_formate";

    SideMenu.updatemenu(StoredObjects.page_type);
    try {
      StoredObjects.listcount= 8;
      SideMenu.adapter.notifyDataSetChanged();
    }catch (Exception e){

    }
    initilization(v);
    if (InterNetChecker.isNetworkAvailable(getActivity())) {
      getPrintFormatService(getActivity());
    } else {
      StoredObjects.ToastMethod(getString(R.string.nointernet), getActivity());
    }
    return v;
  }


  private void initilization(View v) {

    backbtn_img = v.findViewById(R.id.backbtn_img);
    title_txt = v.findViewById(R.id.title_txt);
    submit_btn = v.findViewById(R.id.pf_submit_btn);
    logo_img = v.findViewById(R.id.logo_img);
    signature_img = v.findViewById(R.id.signature_img);
    enter_header_edtxt = v.findViewById(R.id.enter_header_edtxt);
    logo_browse_btn = v.findViewById(R.id.logo_browse_btn);
    signature_browse_btn = v.findViewById(R.id.signature_browse_btn);

    title_txt.setText("Print Format");


    backbtn_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        fragmentcallinglay(new Doc_Dashboard());
      }
    });

    submit_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String enter_header_str = enter_header_edtxt.getText().toString().trim();

        if(StoredObjects.inputValidation(enter_header_edtxt,"Please enter Header Name",getActivity())){
          if(!file_name_logo.equalsIgnoreCase("")){
            if(!file_name_sign.equalsIgnoreCase("")){
              if (InterNetChecker.isNetworkAvailable(getActivity())) {
                editPrintFormatService(getActivity(),enter_header_str);
              } else {
                StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.nointernet), getActivity());
              }
            }else{
              StoredObjects.ToastMethod("Please upload Signature",getActivity());
            }
          }else{
            StoredObjects.ToastMethod("Please upload Logo",getActivity());
          }

        }

      }
    });

    logo_browse_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (CameraUtils.checkAndRequestPermissions(getActivity()) == true) {
          Imagepickingpopup(getActivity(), "logo");
        }

      }
    });

    signature_browse_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (CameraUtils.checkAndRequestPermissions(getActivity()) == true) {
          Imagepickingpopup(getActivity(), "signature");
        }

      }
    });

  }

  private void editPrintFormatService(final Activity activity, String enter_header_str) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.editPrintFormats(RetrofitInstance.edit_print_format, file_name_logo,enter_header_str,file_name_sign,StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.body() != null) {
          try {
            String responseReceived = response.body().string();
            JSONObject jsonObject = new JSONObject(responseReceived);
            StoredObjects.LogMethod("response", "response::" + responseReceived);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              StoredObjects.ToastMethod("Updated Successfully", activity);
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

  private void getPrintFormatService(final Activity activity) {
    CustomProgressbar.Progressbarshow(activity);
    APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
    api.getPrintFormats(RetrofitInstance.print_format, StoredObjects.UserId, StoredObjects.UserRoleId).enqueue(new Callback<ResponseBody>() {
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
              enter_header_edtxt.setText(data_list.get(0).get("header_name"));
              file_name_logo = "";
              file_name_sign = "";
              file_name_logo = data_list.get(0).get("logo");
              file_name_sign = data_list.get(0).get("signature");
              try {
                Glide.with(activity)
                        .load(Uri.parse(RetrofitInstance.BASEURL + data_list.get(0).get("logo"))) // add your image url
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.no_image)
                                .fitCenter()
                                .centerCrop())
                        .into(logo_img);
                Glide.with(activity)
                        .load(Uri.parse(RetrofitInstance.BASEURL + data_list.get(0).get("signature"))) // add your image url
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.no_image)
                                .fitCenter()
                                .centerCrop())
                        .into(signature_img);
              } catch (Exception e) {

              }
            }else{
              file_name_logo = "";
              file_name_sign = "";
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
      //  postFile(realPath, RetrofitInstance.BASEURL + "app/index.php", file.getName(),image_type);
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

  public void postFile(String encodedImage, String postUrl, String fileName, String image_type) {


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
      StoredObjects.LogMethod("response_image",""+responseReceived);
      if (response.code() == 200) {
        JSONObject jsonObject = new JSONObject(responseReceived);
        if(image_type.equalsIgnoreCase("logo")){
          file_name_logo = jsonObject.getString("file_name");
          logo_img.setImageBitmap(myImg);
        }else{
          file_name_sign = jsonObject.getString("file_name");
          signature_img.setImageBitmap(myImg);
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
            // patient_image.setImageBitmap(myImg);

            if(image_type.equalsIgnoreCase("logo")){
              file_name_logo = jsonObject.getString("file_name");
              logo_img.setImageBitmap(myImg);
            }else{
              file_name_sign = jsonObject.getString("file_name");
              signature_img.setImageBitmap(myImg);
            }
            CustomProgressbar.Progressbarcancel(getActivity());

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


