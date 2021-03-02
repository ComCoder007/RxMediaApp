package com.rxmediaapp.serviceparsing;

import com.rxmediaapp.storedobjects.StoredObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kiran on 22-08-2018.
 */

public class JsonParsing {


   public static ArrayList<HashMap<String, String>> GetJsonData(String results) throws JSONException {
       ArrayList<HashMap<String, String>> d_child = new ArrayList<HashMap<String, String>>();
       d_child.clear();
       try {
           StoredObjects.LogMethod("status_res", "status_res:--"+results);
           JSONArray cast = new JSONArray(results);
           for(int i = 0;i<cast.length();i++){
               JSONObject jsonObject1 = cast.getJSONObject(i);
               JSONArray myStringArray = jsonObject1.names();
               HashMap<String, String> dash_hash = new HashMap<String, String>();
               for(int j = 0;j<myStringArray.length();j++){
                   if(jsonObject1.getString(myStringArray.getString(j)).equalsIgnoreCase("0000-00-00 00:00:00")||jsonObject1.getString(myStringArray.getString(j)).equalsIgnoreCase("")||jsonObject1.getString(myStringArray.getString(j)).equalsIgnoreCase("null")||jsonObject1.getString(myStringArray.getString(j)).equalsIgnoreCase(null)){
                       dash_hash.put(myStringArray.getString(j), "");
                   }else{
                       dash_hash.put(myStringArray.getString(j), jsonObject1.getString(myStringArray.getString(j)));
                   }

                   StoredObjects.LogMethod("tag", "values >>>" +myStringArray.getString(j)+"---"+jsonObject1.getString(myStringArray.getString(j)));
               }

               dash_hash.put("is_viewed","No");
               dash_hash.put("update_value","");
               dash_hash.put("doc_consult","");
               d_child.add(dash_hash);
           }


       } catch (JSONException e) {
           e.printStackTrace();
       }
       return d_child;
   }

}
