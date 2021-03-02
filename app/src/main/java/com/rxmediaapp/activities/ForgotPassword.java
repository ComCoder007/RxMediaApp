package com.rxmediaapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.customfonts.CustomButton;
import com.rxmediaapp.customfonts.CustomEditText;
import com.rxmediaapp.serviceparsing.APIInterface;
import com.rxmediaapp.serviceparsing.CustomProgressbar;
import com.rxmediaapp.serviceparsing.RetrofitInstance;
import com.rxmediaapp.storedobjects.StoredObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends Activity {

    ImageView forgot_back_img;
    CustomEditText forgot_mbile_edtx;
    CustomButton forgot_reset_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        initialisation();
    }

    public void initialisation() {

        forgot_back_img =  findViewById(R.id.forgot_back_img);
        forgot_mbile_edtx =  findViewById(R.id.forgot_mbile_edtx);
        forgot_reset_btn =  findViewById(R.id.forgot_reset_btn);

        forgot_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ForgotPassword.this.finish();
            }
        });

        forgot_reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  startActivity(new Intent(ForgotPassword.this, SideMenu.class));

                String username = forgot_mbile_edtx.getText().toString().trim();
                if (StoredObjects.inputValidation(forgot_mbile_edtx, "Please enter Mobile Number", ForgotPassword.this)) {
                    loginService(ForgotPassword.this,username);
                }
            }
        });


    }

    private void loginService(final Activity activity, String username) {
        CustomProgressbar.Progressbarshow(activity);

        APIInterface api = RetrofitInstance.getRetrofitInstance().create(APIInterface.class);
        api.forgotpassword(RetrofitInstance.forgot_password,username).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body() != null) {
                    try {
                        String responseReceived = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseReceived);
                        String status = jsonObject.getString("status");
                        if(status.equalsIgnoreCase("200"))
                        {
                            forgot_mbile_edtx.setText("");
                            StoredObjects.ToastMethod("Password request has been accepted",activity);
                            ForgotPassword.this.finish();
                            startActivity(new Intent(ForgotPassword.this, SignIn.class));
                        }else{
                            forgot_mbile_edtx.setText("");
                            String error = jsonObject.getString("error");
                            StoredObjects.ToastMethod(error,activity);
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
