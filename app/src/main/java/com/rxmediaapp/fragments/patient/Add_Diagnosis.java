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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
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
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.fragments.hospital.H_Assistant;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.InterNetChecker;
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
import java.util.Calendar;
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


public class Add_Diagnosis extends Fragment {

    ImageView backbtn_img,diagnose_image;
    TextView title_txt;
    EditText diagnose_date_edtx,diagnose_docname_edtx,diagnose_dignosename_edtx,diagnose_remarks_edtx,diagnose_testname_edtx;
    Button save_button,diagnose_image_btn;
    String scanned_copy ="";
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    EditText patient_edtx;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.add_diagnosis,null,false );
        StoredObjects.page_type="add_diagnosis";

        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        return v;
    }

    private void initilization(View v) {

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        save_button = v.findViewById( R.id. save_button);
        diagnose_date_edtx = v.findViewById( R.id. diagnose_date_edtx);

        diagnose_image = v.findViewById( R.id. diagnose_image);
        diagnose_docname_edtx= v.findViewById( R.id. diagnose_docname_edtx);
        diagnose_dignosename_edtx = v.findViewById( R.id. diagnose_dignosename_edtx);
        diagnose_remarks_edtx = v.findViewById( R.id. diagnose_remarks_edtx);
        diagnose_image_btn = v.findViewById( R.id. diagnose_image_btn);
        diagnose_testname_edtx = v.findViewById( R.id. diagnose_testname_edtx);
        patient_edtx=v.findViewById(R.id.patient_edtx);



        title_txt.setText( "Add Diagnosis" );

        diagnose_remarks_edtx.setImeOptions(EditorInfo.IME_ACTION_DONE);
        diagnose_remarks_edtx.setRawInputType(InputType.TYPE_CLASS_TEXT);

        diagnose_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CameraUtils.checkAndRequestPermissions(getActivity())) {
                    Imagepickingpopup(getActivity(), "add diagnosis");
                }
            }
        });
        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        } );



        patient_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(P_Diagnosis_Reports.patname_list.size()>0){
                    PatientListPopUp((CustomEditText) patient_edtx,getActivity());
                }else{
                    StoredObjects.ToastMethod("No Data found",getActivity());
                }
            }
        });


        diagnose_date_edtx.setOnClickListener(new View.OnClickListener() {
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
                                diagnose_date_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date_str = diagnose_date_edtx.getText().toString().trim();
                String nme_str = diagnose_docname_edtx.getText().toString().trim();
                String dia_name_str = diagnose_dignosename_edtx.getText().toString().trim();
                String test_str=diagnose_testname_edtx.getText().toString().trim();
                String remarks_str=diagnose_remarks_edtx.getText().toString().trim();

                if(StoredObjects.inputValidation(diagnose_date_edtx,getString(R.string.reportdate_validation),getActivity())) {
                    if (StoredObjects.inputValidation(diagnose_docname_edtx, "Please enter Ref Doctor Name", getActivity())) {
                        if(StoredObjects.inputValidation(diagnose_dignosename_edtx, getString(R.string.dianame_validation), getActivity())) {
                                if (StoredObjects.inputValidation(diagnose_testname_edtx, getString(R.string.testname_validation), getActivity())) {
                                    if (StoredObjects.inputValidation(diagnose_remarks_edtx, getString(R.string.remarks_validation), getActivity())) {
                                        if(patient_id.equalsIgnoreCase("")){
                                            StoredObjects.ToastMethod("Please select Patient",getActivity());
                                        }else{
                                            if(scanned_copy.equalsIgnoreCase("")){
                                                StoredObjects.ToastMethod("Please upload Scanned Copy",getActivity());
                                            }else{
                                                AddDiagnosisService(getActivity(),date_str,nme_str,dia_name_str,test_str,remarks_str);
                                            }
                                        }






                                    }
                                }
                        }


                    }
                }

            }
        });


    }
    String patient_id="";
    private void PatientListPopUp(final EditText f_pat_status_edtx,Activity activity) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
        listPopupWindow.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, P_Diagnosis_Reports.patname_list));
        listPopupWindow.setAnchorView(f_pat_status_edtx);
        listPopupWindow.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                f_pat_status_edtx.setText(P_Diagnosis_Reports.patname_list.get(position));

                patient_id=P_Diagnosis_Reports.patientslist.get(position).get("user_id");
                listPopupWindow.dismiss();

            }
        });

        listPopupWindow.show();
    }
    private void AddDiagnosisService(final FragmentActivity activity, String date_str, String nme_str, String dia_name_str, String test_str, String remarks_str) {
        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.AddDiagnosis(RetrofitInstance.add_test_suggestions,nme_str,date_str,dia_name_str,test_str,scanned_copy,remarks_str, StoredObjects.UserId,StoredObjects.UserRoleId,patient_id).enqueue(new Callback<ResponseBody>() {
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
                            fragmentcallinglay(new P_Diagnosis_Reports());

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

                captureImage();

                dialog.dismiss();
            }
        });

        pickglry_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
       // diagnose_image.setImageBitmap(myImg);
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
                //postFile(realPath, RetrofitInstance.BASEURL + "app/index.php", file.getName());
                new ImageuploadTask().execute(realPath, file.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.nointernet), getActivity());
        }


    }


    public void fragmentcallinglay(Fragment fragment) {

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).addToBackStack( "" ).commit ();

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
                JSONObject jsonObject = new JSONObject(responseReceived);
                scanned_copy = jsonObject.getString("file_name");

                try {
                    Glide.with(getActivity())
                            .load(Uri.parse(RetrofitInstance.IMAGE_URL + scanned_copy))
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.no_image)
                                    .fitCenter()
                                    .centerCrop())
                            .into(diagnose_image);
                } catch (Exception e) {
                    e.printStackTrace();

                }
               // diagnose_image.setImageBitmap(myImg);
                CustomProgressbar.Progressbarcancel(getActivity());
            } else {
                CustomProgressbar.Progressbarcancel(getActivity());
            }
            StoredObjects.LogMethod("val", "val::" + responseReceived);
        } catch (IOException| JSONException e) {

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
                        scanned_copy = jsonObject.getString("file_name");
                        // patient_image.setImageBitmap(myImg);
                        try {
                            Glide.with(getActivity())
                                    .load(Uri.parse(RetrofitInstance.IMAGE_URL + scanned_copy))
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.no_image)
                                            .fitCenter()
                                            .centerCrop())
                                    .into(diagnose_image);


                        } catch (Exception e) {
                            e.printStackTrace();

                        }

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


