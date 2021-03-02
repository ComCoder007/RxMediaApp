package com.rxmediaapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rxmediaapp.storedobjects.StoredObjects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kiran on 23-10-2018.
 */

public class Database extends SQLiteOpenHelper {


    Context context;


    public Database(Context applicationcontext) {
        super(applicationcontext, "rxmediaapp.db", null, 2);
        Log.d("database","Created");
        context = applicationcontext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        query = "CREATE TABLE userdata ( idno INTEGER PRIMARY KEY,Userroleid Text,Userid Text,UserType Text,DoctorId Text,PatientId Text,HospitalId Text,AssistantId Text)";
        db.execSQL(query);
        query = "insert into userdata(Userroleid,Userid,UserType,DoctorId,PatientId,HospitalId,AssistantId)values('0','0','0','0','0','0','0')";
        Log.i("query", "query"+query);
        db.execSQL(query);

        Log.d("userdata","userdata Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;
        query = "DROP TABLE IF EXISTS userdata";
        db.execSQL(query);
        onCreate(db);

    }

    public ArrayList<HashMap<String, String>> getAllDevice() {

        StoredObjects.LogMethod("hello","hello");
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM userdata";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);



        if(cursor!=null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    StoredObjects.LogMethod("hello","hello"+"<>"+cursor.getColumnNames()+"<>"+database.getVersion());
                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("idno", cursor.getString(cursor.getColumnIndex("idno")));
                    map.put("Userroleid", cursor.getString(cursor.getColumnIndex("Userroleid")));
                    map.put("Userid", cursor.getString(cursor.getColumnIndex("Userid")));
                    map.put("UserType", cursor.getString(cursor.getColumnIndex("UserType")));
                    map.put("DoctorId", cursor.getString(cursor.getColumnIndex("DoctorId")));
                    map.put("PatientId", cursor.getString(cursor.getColumnIndex("PatientId")));
                    map.put("HospitalId", cursor.getString(cursor.getColumnIndex("HospitalId")));
                    map.put("AssistantId", cursor.getString(cursor.getColumnIndex("AssistantId")));
                    wordList.add(map);

                    StoredObjects.UserRoleId = cursor.getString(1);
                    StoredObjects.UserId=cursor.getString(2);
                    StoredObjects.UserType=cursor.getString(3);

                    StoredObjects.Logged_DoctorId=cursor.getString(4);
                    StoredObjects.Logged_PatientId=cursor.getString(5);
                    StoredObjects.Logged_HospitalId=cursor.getString(6);
                    StoredObjects.Logged_AssistantId=cursor.getString(7);



                } while (cursor.moveToNext());

            }
        }

        return wordList;
    }
    public void insertID(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Userid", userId);
        database.insert("userdata", null, values);
        database.close();

    }

    public void deleteLastDataTable() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("userdata", null, null);
    }

    public void UpdateUserdata(String type,String value) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
         if(type.equalsIgnoreCase("user_id")){
            values.put("Userid", value);
        }else if(type.equalsIgnoreCase("user_type")){
            values.put("UserType", value);
        }else if(type.equalsIgnoreCase("role_id")){
            values.put("Userroleid", value);
        }else if(type.equalsIgnoreCase("doctor_id")){
            values.put("DoctorId", value);
        }else if(type.equalsIgnoreCase("assistant_id")){
            values.put("AssistantId", value);
        }else if(type.equalsIgnoreCase("patient_id")){
            values.put("PatientId", value);
        }else if(type.equalsIgnoreCase("hospital_id")){
            values.put("HospitalId", value);
        }

        database.update("userdata", values,null, null);//insert("userdata", null, values);
        database.close();

    }






}

