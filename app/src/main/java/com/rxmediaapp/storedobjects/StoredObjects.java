package com.rxmediaapp.storedobjects;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Android-9 on 03-08-2019.
 */

public class StoredObjects {

    public static String UserId = "0";
    public static String UserRoleId = "0";
    public static String Logged_DoctorId = "0";
    public static String Logged_HospitalId = "0";
    public static String Logged_PatientId = "0";
    public static String Logged_AssistantId = "0";
    public static String UserType = "0";
    public static int listcount = 0;
    public static String page_type = "";
    public static String p_mobilenum = "";
    public static String Prescription_Id = "";

    public static String Doc_Hospital_Id = "";
    public static String tab_type = "";
    public static String first_time = "";

    public static String redirect_type = "";
    public static String H_DOC_ID = "";

    public static String M_UserId = "0";
    public static String M_RoleUserId = "0";


    public static String F_UserId = "0";
    public static String F_RoleUserId = "0";


    public static String Pat_DocID = "";
    public static String CurrentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    public static String PreviousDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()-24*60*60*1000));
    public static String LastDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()-90*24*60*60*1000));




    public static boolean daysDifference(String fromdate, String todate){
        boolean ret =false;
        if(fromdate.equalsIgnoreCase("")&&todate.equalsIgnoreCase("")){
            ret =true;
        }else{
            SimpleDateFormat dfDate  = new SimpleDateFormat("dd/MM/yyyy");
            Date startdate = null;
            Date enddate = null;
            try {
                startdate = dfDate.parse(fromdate);
                enddate = dfDate.parse(todate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int diffInDays = (int) ((enddate.getTime() - startdate.getTime())/ (1000 * 60 * 60 * 24));
            if(diffInDays>=0){
                ret =true;
            }else{
                ret=false;
            }
        }

        return ret;
    }
    //Log
    public static void LogMethod(String keyval, String val) {
        Log.i(keyval, val);
    }

    //Displaying toast
    public static void ToastMethod(String message, Context context) {
        Toast.makeText(context, message, 0).show();
    }

    public static boolean inputValidation(EditText edittext, String message, Activity activity) {
        if (edittext.getText().toString().trim().equals("") || edittext.getText().toString().trim().equals(null)) {
            Toast.makeText(activity, message,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static boolean isValidMobile(String phone) {

        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 9 && phone.length() <= 13;
        }
        return false;
    }

    public static void hide_dialogkeyboard(Activity activity, EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }catch (Exception e){

        }
    }
    public static void hide_keyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService( Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Check Email Validation
    public static boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean Emailvalidation(String email,String message,Activity activity) {

        Boolean value=false;
        if(email.length()==0){
            value=true;
        }else{
            String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);
            value=matcher.matches();



        }

        if(value==false){
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        }
        return value;

    }



    public static ArrayList<HashMap<String, String>> dummy_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> franshise_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> menuitems_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> subdummy_list = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> physuggestions_list = new ArrayList<>();
    public static ArrayList<String> physuggestionsnames_list = new ArrayList<>();

    public static String stripHtml(String html) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //Spanned htmlAsSpanned = Html.fromHtml(html);
            return String.valueOf(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
        } else {
            return String.valueOf(Html.fromHtml(html));
        }
    }

    public static void hashmaplist(int val) {
        dummy_list.clear();

        for (int k = 0; k < val; k++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name", "Alex Gosh");
            hashMap.put("is_viewed", "No");
            dummy_list.add(hashMap);
        }
    }

    public static void getrray(String[] array) {
        menuitems_list.clear();

        for (int k = 0; k < array.length; k++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name", array[k]);

            menuitems_list.add(hashMap);
        }
    }

    public static void subhashmaplist(int val) {
        subdummy_list.clear();

        for (int k = 0; k < val; k++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name", "Alex Gosh");
            hashMap.put("is_viewed", "No");
            subdummy_list.add(hashMap);
        }
    }

    public static void AlertForCall(final Activity activity, final String customer_care_number) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(customer_care_number)
                .setCancelable(false)
                .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (ContextCompat.checkSelfPermission(activity,
                                Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(activity,
                                        Manifest.permission.CALL_PHONE)
                                        != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 1);

                        } else {

                            StoredObjects.LogMethod("response", "response<><><><>><:---"+customer_care_number);

                            activity.startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+customer_care_number))); }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }

                });

       //Creating dialog box
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void hide_dialogkeyboardtextview(Activity activity, AutoCompleteTextView editText) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }catch (Exception e){

        }
    }

    public  static String GetSelectedDate(int day,int month,int year){
        month=month+1;
        String selectddate="";
        if(month<10&&day>=10){
            selectddate = day + "/" + "0"+month + "/" + year;
        }else if(month>=10&&day<10){
            selectddate ="0"+ day  + "/" +month + "/" + year;
        }else if(month<10&&day<10){
            selectddate ="0"+ day + "/" +"0"+ month + "/" + year;
        }else{
            selectddate = day + "/" + month + "/" + year;
        }
        return selectddate;
    }



    public static String time12hrsformat(String value){
        String finalvalue="";
        if(value.equalsIgnoreCase("")){

        }else{
            try {
                SimpleDateFormat date12Format = new SimpleDateFormat("HH:mm:ss");

                SimpleDateFormat date24Format = new SimpleDateFormat("hh:mm a");
                finalvalue=date24Format.format(date12Format.parse(value));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return finalvalue;

    }

    public static String convertMonthformat(String inputDate) {//0000-00-00 00:00:00
        String finalvalue="";
        if(inputDate.equalsIgnoreCase("")){

        }else{
            DateFormat theDateFormat = new SimpleDateFormat("yyyy-MM-dd");//10-11-2016
            Date date = null;// = new Date();;

            try {
                date = theDateFormat.parse(inputDate);
            } catch (ParseException parseException) {
                // Date is invalid. Do what you want.
            } catch (Exception exception) {
                // Generic catch. Do what you want.
            }
            //12-02-2019 3:20 AM
            theDateFormat = new SimpleDateFormat("dd MMM yyyy");//11/08/2016
            finalvalue= theDateFormat.format(date);
        }

        return finalvalue;
    }

    public static String convertfullTimeformat(String inputDate) {//0000-00-00 00:00:00
        String finalvalue="";
        if(inputDate.equalsIgnoreCase("")){

        }else{
            DateFormat theDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//10-11-2016
            Date date = null;// = new Date();;

            try {
                date = theDateFormat.parse(inputDate);
            } catch (ParseException parseException) {
                // Date is invalid. Do what you want.
            } catch (Exception exception) {
                // Generic catch. Do what you want.
            }
            //12-02-2019 3:20 AM
            theDateFormat = new SimpleDateFormat("dd/MM/yyyy");//11/08/2016
            finalvalue= theDateFormat.format(date);
        }

        return finalvalue;
    }
    public static String convertDateformat(String inputDate) {//0000-00-00 00:00:00
        String finalvalue="";
        if(inputDate.equalsIgnoreCase("")){

        }else{
            DateFormat theDateFormat = new SimpleDateFormat("yyyy-MM-dd");//10-11-2016
            Date date = null;// = new Date();;

            try {
                date = theDateFormat.parse(inputDate);
            } catch (ParseException parseException) {
                // Date is invalid. Do what you want.
            } catch (Exception exception) {
                // Generic catch. Do what you want.
            }
            //12-02-2019 3:20 AM
            theDateFormat = new SimpleDateFormat("dd/MM/yyyy");//11/08/2016
            finalvalue= theDateFormat.format(date);
        }

        return finalvalue;
    }

    public static String convertfullDateformat(String inputDate) {//0000-00-00 00:00:00
        String finalvalue="";
        if(inputDate.equalsIgnoreCase("")){

        }else{
            DateFormat theDateFormat = new SimpleDateFormat("dd-MM-yyyy");//10-11-2016
            Date date = null;// = new Date();;

            try {
                date = theDateFormat.parse(inputDate);
            } catch (ParseException parseException) {
                // Date is invalid. Do what you want.
            } catch (Exception exception) {
                // Generic catch. Do what you want.
            }
            //12-02-2019 3:20 AM
            theDateFormat = new SimpleDateFormat("dd/MM/yyyy");//11/08/2016
            finalvalue= theDateFormat.format(date);
        }

        return finalvalue;
    }

    public static String convertfullDateTimeformat(String inputDate) {//0000-00-00 00:00:00
        String finalvalue="";
        if(inputDate.equalsIgnoreCase("")){

        }else{
            DateFormat theDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//10-11-2016
            Date date = null;// = new Date();;

            try {
                date = theDateFormat.parse(inputDate);
            } catch (ParseException parseException) {
                // Date is invalid. Do what you want.
            } catch (Exception exception) {
                // Generic catch. Do what you want.
            }
            //12-02-2019 3:20 AM
            theDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");//11/08/2016
            finalvalue= theDateFormat.format(date);
        }

        return finalvalue;
    }
    //"2020-06-13 19:53:48",
}