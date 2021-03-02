package com.rxmediaapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.rxmediaapp.R;


public class Package_Selection extends Activity {
    ImageView p_back_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_selection);
        initialisation();
    }

    public void initialisation() {

        p_back_img =  findViewById(R.id.p_back_img);


        p_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Package_Selection.this.finish();
            }
        });


    }



}
