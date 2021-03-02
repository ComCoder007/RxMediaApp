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

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.fragments.patient.P_Sub_Member;
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
import java.text.SimpleDateFormat;
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
import retrofit2.http.Query;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class P_Edit_Member extends Fragment {

    ImageView backbtn_img,p_edit_mem_img;
    TextView title_txt;
    EditText p_admem_dob_edtx,psub_gender_edtx,p_admem_relation_edtx,p_admem_nme_edtx,p_admem_email_edtx,p_admem_mbile_edtx,p_admem_adhar_edtx,p_admem_bldgrup_edtx;
    Button p_admem_signup_btn;
    public static int position;

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    String  monthStr;
    String dayStr;
    String image_type = "";
    Calendar calendar;
    String[] genderlist = {"Male", "Female"};

    public static ArrayList<HashMap<String, String>> editarraylist=  new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.p_edit_member,null,false );
        StoredObjects.page_type="p_edit_member";

        SideMenu.updatemenu(StoredObjects.page_type);
        initilization(v);
        assignData();
        return v;
    }
    private void GenderListPopup(final EditText prfilenme, Activity activity) {
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
    private void assignData() {
        try {
            p_admem_adhar_edtx.setVisibility(View.VISIBLE);
            p_admem_relation_edtx.setText(editarraylist.get(0).get("relation"));
            p_admem_nme_edtx.setText(editarraylist.get(0).get("name"));
            p_admem_email_edtx.setText(editarraylist.get(0).get("email"));
            p_admem_mbile_edtx.setText(editarraylist.get(0).get("phone"));
            p_admem_adhar_edtx.setText(editarraylist.get(0).get("aadhar_number"));
            p_admem_dob_edtx.setText(StoredObjects.convertDateformat(editarraylist.get(0).get("date_of_birth")));
           // p_admem_bldgrup_edtx.setText(editarraylist.get(0).get("blood_group"));
            psub_gender_edtx.setText(editarraylist.get(0).get("gender"));


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (InterNetChecker.isNetworkAvailable(getActivity())) {
            getBloodGroup(getActivity());

        } else {
            StoredObjects.ToastMethod(getActivity().getResources().getString(R.string.nointernet), getActivity());
        }


    }
    private void BloodgroupPopup(final EditText prfilenme,Activity activity) {
        final ListPopupWindow dropdownpopup = new ListPopupWindow(activity);
        dropdownpopup.setAdapter(new ArrayAdapter<>(activity, R.layout.drpdwn_lay, bloodgroupnames));
        dropdownpopup.setAnchorView(prfilenme);
        dropdownpopup.setHeight(LinearLayout.MarginLayoutParams.WRAP_CONTENT);

        dropdownpopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prfilenme.setText(bloodgroupnames.get(position));
                blood_id=bloodgroup_list.get(position).get("id");
                dropdownpopup.dismiss();

            }
        });

        dropdownpopup.show();
    }

    String blood_id="";
    ArrayList<String> bloodgroupnames = new ArrayList<>();
    ArrayList<HashMap<String, String>> bloodgroup_list = new ArrayList<>();
    private void getBloodGroup(final Activity activity) {
        CustomProgressbar.Progressbarshow(activity);
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
                            for (int k = 0; k < bloodgroup_list.size(); k++) {
                                if(editarraylist.get(0).get("blood_group").equalsIgnoreCase(bloodgroup_list.get(k).get("id"))){
                                    blood_id=bloodgroup_list.get(k).get("id");
                                    p_admem_bldgrup_edtx.setText(bloodgroup_list.get(k).get("name"));
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

        backbtn_img = v.findViewById( R.id. backbtn_img);
        title_txt= v.findViewById( R.id. title_txt);
        p_admem_signup_btn = v.findViewById( R.id. p_admem_signup_btn);
        p_admem_dob_edtx = v.findViewById( R.id. p_admem_dob_edtx);
        p_admem_relation_edtx = v.findViewById( R.id. p_admem_relation_edtx);
        p_admem_nme_edtx= v.findViewById( R.id. p_admem_nme_edtx);
        p_admem_email_edtx = v.findViewById( R.id. p_admem_email_edtx);
        p_admem_mbile_edtx = v.findViewById( R.id. p_admem_mbile_edtx);
        p_admem_adhar_edtx = v.findViewById( R.id. p_admem_adhar_edtx);
        p_admem_bldgrup_edtx = v.findViewById( R.id. p_admem_bldgrup_edtx);
        p_edit_mem_img = v.findViewById( R.id. p_edit_mem_img);

        psub_gender_edtx=v.findViewById(R.id.psub_gender_edtx);
        psub_gender_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GenderListPopup(psub_gender_edtx, getActivity());
            }
        });


        p_admem_bldgrup_edtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BloodgroupPopup(p_admem_bldgrup_edtx,getActivity());
            }
        });
        title_txt.setText( "Edit Member" );

        backbtn_img.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        } );
        p_edit_mem_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CameraUtils.checkAndRequestPermissions(getActivity()) == true) {
                    Imagepickingpopup(getActivity(), "P_edit_member");
                }
            }
        });
        p_admem_dob_edtx.setOnClickListener(new View.OnClickListener() {
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
                                p_admem_dob_edtx.setText(StoredObjects.GetSelectedDate(day,month,year));
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        p_admem_mbile_edtx.setEnabled(false);

        p_admem_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pati_relation_str = p_admem_relation_edtx.getText().toString().trim();
                String pati_nme_str = p_admem_nme_edtx.getText().toString().trim();
                String pati_email_str = p_admem_email_edtx.getText().toString().trim();
                String pati_mobile_str = p_admem_mbile_edtx.getText().toString().trim();
                String pati_aadhar_str = p_admem_adhar_edtx.getText().toString().trim();
                String pati_dob_str = p_admem_dob_edtx.getText().toString().trim();
                String pati_blood_str = p_admem_bldgrup_edtx.getText().toString().trim();
                String patient_id_str = editarraylist.get(0).get("patient_id");
                String gender = psub_gender_edtx.getText().toString().trim();

                if (StoredObjects.inputValidation(p_admem_relation_edtx, getString(R.string.enter_relation), getActivity())) {
                    if (StoredObjects.inputValidation(p_admem_nme_edtx, getString(R.string.enter_dr_name), getActivity())) {
                        if (StoredObjects.Emailvalidation(pati_email_str, getString(R.string.enter_valid_email), getActivity())) {
                            //if (StoredObjects.inputValidation(p_admem_mbile_edtx, getString(R.string.enter_mobile_validation), getActivity())) {
                               // if (StoredObjects.isValidMobile(pati_mobile_str)) {
                                    if (StoredObjects.inputValidation(p_admem_dob_edtx, getString(R.string.dob_validation), getActivity())) {
                                        //if (StoredObjects.inputValidation(p_admem_adhar_edtx, getString(R.string.aadhar_validation), getActivity())) {
                                            if (StoredObjects.inputValidation(p_admem_bldgrup_edtx, getString(R.string.blood_validation), getActivity())) {

                                                if (StoredObjects.inputValidation(psub_gender_edtx, getString(R.string.gender_validate), getActivity())) {
                                                    editMemberService(getActivity(), patient_id_str, pati_relation_str, pati_nme_str, pati_email_str, pati_mobile_str, pati_aadhar_str, pati_dob_str, pati_blood_str, gender);

                                                }


                                            }
                                        //}
                                    }



                                //}


                            //}
                        }
                    }

                }

            }
        });

    }


    private void editMemberService(final Activity activity,String patient_id_str, String pati_relation_str, String pati_nme_str, String pati_email_str, String pati_mobile_str,
                                   String pati_aadhar_str, String pati_dob_str, String pati_blood_str,String gender) {
        CustomProgressbar.Progressbarshow(activity);
        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.EditMember(RetrofitInstance.patient_edit_member,patient_id_str,StoredObjects.UserId, StoredObjects.UserRoleId,pati_relation_str,pati_nme_str,pati_email_str,pati_mobile_str,pati_dob_str,blood_id,gender,pati_aadhar_str).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {

                        String responseReceived = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseReceived);
                        StoredObjects.LogMethod("response", "response::" + responseReceived);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("200")) {
                            StoredObjects.ToastMethod("Edited successfully", activity);
                        } else {
                            String error = jsonObject.getString("error");
                            StoredObjects.ToastMethod(error, activity);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
                CustomProgressbar.Progressbarcancel(activity);

                fragmentcallinglay(new P_Sub_Member());
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

        FragmentManager fragmentManager = getActivity ().getSupportFragmentManager ();
        fragmentManager.beginTransaction ()/*.setCustomAnimations(R.anim.falldown, R.anim.falldown)*/.replace (R.id.frame_container , fragment).commit ();

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
