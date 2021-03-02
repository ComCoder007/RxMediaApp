package com.rxmediaapp.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.rxmediaapp.R;
import com.rxmediaapp.Sidemenu.SideMenu;
import com.rxmediaapp.database.Database;
import com.rxmediaapp.storedobjects.CameraUtils;
import com.rxmediaapp.storedobjects.StoredObjects;

public class Splash extends Activity {
    private static final int TIME = 2 * 1000;
    Database database;
    Context mContext;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        mContext = Splash.this;
        database = new Database(getApplicationContext());
        database.getAllDevice();

        // setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        StoredObjects.LogMethod("oncreate","oncreate::::"+"onresume::::");

        if(CameraUtils.checkAndRequestPermissions(Splash.this)==true){
            GenerateSplashScreenMethod();
        }

        //GenerateSplashScreenMethod();
        super.onResume();
    }

    public void GenerateSplashScreenMethod(){

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {


                if(StoredObjects.UserId.equalsIgnoreCase("0")||StoredObjects.UserId.equalsIgnoreCase("")){
                    startActivity(new Intent(Splash.this, SignIn.class));
                    Splash.this.finish();
                }else{
                    startActivity(new Intent(Splash.this, SideMenu.class));
                    Splash.this.finish();

                }
            }
        }, TIME);
    }


}

