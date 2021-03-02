package com.rxmediaapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.rxmediaapp.R;
import com.rxmediaapp.customfonts.CustomButton;

public class SignupOptions extends Activity {

    CustomButton sgnup_patent_btn,sgnup_hsptl_btn,sgnup_indvduldctr_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_options);

        initialisation();
    }

    public void initialisation() {

        sgnup_patent_btn =  findViewById(R.id.sgnup_patent_btn);
        sgnup_hsptl_btn =  findViewById(R.id.sgnup_hsptl_btn);
        sgnup_indvduldctr_btn =  findViewById(R.id.sgnup_indvduldctr_btn);

        sgnup_patent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               startActivity(new Intent(SignupOptions.this, PatientSignup.class));
            }
        });

        sgnup_hsptl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  startActivity(new Intent(SignupOptions.this, HosptalSignup.class));
            }
        });

        sgnup_indvduldctr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  startActivity(new Intent(SignupOptions.this, DoctorSignup.class));
            }
        });


    }
}
