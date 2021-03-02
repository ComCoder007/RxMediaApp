package com.rxmediaapp.serviceparsing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;


import com.rxmediaapp.storedobjects.StoredObjects;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class HttpPostClass {

	static String logMess = "";
	static long startTime;
	static long stopTime;
	static long elapsedTime;

	public static String PostMethod(String url, List<NameValuePair> pairs)throws URISyntaxException,SocketTimeoutException, UnsupportedEncodingException, ConnectTimeoutException {
		String response = null;
		HttpClient httpclient = new DefaultHttpClient();//DefaultHttpClient
		HttpPost httppost = new HttpPost(url);
		HttpParams httpParameters = httppost.getParams();

		startTime = System.currentTimeMillis();
		Log.d("checking", "namevaluepairs_makeHttpRequest ---<1>" + startTime);

		try {
			StrictMode.ThreadPolicy policy = new StrictMode.
					ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity( pairs, HTTP.UTF_8 );
			httppost.setEntity( ent );
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 8 * 1000);
			HttpConnectionParams.setSoTimeout (httpParameters, 120* 1000);

			httppost.setParams(httpParameters);

			response = httpclient.execute(httppost, responseHandler);

			StoredObjects.LogMethod("HttpTAG:1", "namevaluepairs:--"+url);
			StoredObjects.LogMethod("HttpTAG:1", "namevaluepairs:--"+pairs.toString());
			System.out.println("Httpresponse:-"+"namevaluepairs_makeHttpRequest_0--- "+response.toString());

			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime - startTime;
			logMess = "   elapsed Time :  " + elapsedTime + " ms";
			Log.d("checking", "namevaluepairs_makeHttpRequest_1--- " + logMess);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.d("checking", "namevaluepairs_makeHttpRequest_2--- " + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("checking", "namevaluepairs_makeHttpRequest_3--- " + e);
		}
		return response;

	}
	public static String Http_PostMethod(String url, List<NameValuePair> pairs)throws URISyntaxException,SocketTimeoutException, UnsupportedEncodingException, ConnectTimeoutException {
		String response = null;
		HttpClient httpclient = new CustomHttpClient();//DefaultHttpClient
		HttpPost httppost = new HttpPost(url);
		HttpParams httpParameters = httppost.getParams();

		startTime = System.currentTimeMillis();
		Log.d("checking", "namevaluepairs_makeHttpRequest ---<1>" + startTime);

		try {
			StrictMode.ThreadPolicy policy = new StrictMode.
					ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity( pairs, HTTP.UTF_8 );
			httppost.setEntity( ent );
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 8 * 1000);
			HttpConnectionParams.setSoTimeout (httpParameters, 360* 1000);

			httppost.setParams(httpParameters);

			response = httpclient.execute(httppost, responseHandler);

			StoredObjects.LogMethod("HttpTAG:1", "namevaluepairs:--"+url);
			StoredObjects.LogMethod("HttpTAG:1", "namevaluepairs:--"+pairs.toString());
			System.out.println("Httpresponse:-"+"namevaluepairs_makeHttpRequest_0--- "+response.toString());

			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime - startTime;
			logMess = "   elapsed Time :  " + elapsedTime + " ms";
			Log.d("checking", "namevaluepairs_makeHttpRequest_1--- " + logMess);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.d("checking", "namevaluepairs_makeHttpRequest_2--- " + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("checking", "namevaluepairs_makeHttpRequest_3--- " + e);
		}
		return response;

	}
	public static String GET_java12(String url, String header, List<NameValuePair> pairs) throws NullPointerException {

		InputStream inputStream = null;
		String result = "";

		try {
			StoredObjects.LogMethod("result", "strResult::-"+pairs+"<Header>"+header+"<link>"+url+"<>"+ URLEncoder.encode(url, "UTF-8"));
			url = URLEncoder.encode(url, "UTF-8");
			// create HttpClient
			HttpClient httpclient = new CustomHttpClient();//DefaultHttpClient

			/*HttpGet httpget = new HttpGet(url+"?"+ new UrlEncodedFormEntity(pairs, "utf-8"));
			httpget.addHeader("Authorization" , header);*/
			//HttpResponse httpResponse = httpclient.execute(httpget);

			// make GET request to the given URL
			HttpGet httpget = new HttpGet(url);
			httpget.addHeader("Authorization" , header);
			HttpResponse httpResponse = httpclient.execute(httpget);

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if(inputStream != null){
				result = convertInputStreamToString(inputStream);

			}
			else{
				result = "Did not work!";
			}

			StoredObjects.LogMethod("Data","result>>>" + result );
		} catch (Exception e) {
			//  Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}
	public static String GET_imageBitmap(String url) throws NullPointerException {

		InputStream inputStream = null;
		String result = null;

		try {
			// create HttpClient
			HttpClient httpclient = new CustomHttpClient();//DefaultHttpClient
			HttpGet httpget = new HttpGet(url);
			HttpResponse httpResponse = httpclient.execute(httpget);
			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();
			// convert inputstream to string
			if(inputStream != null){
				// decoding stream data back into image Bitmap that android understands
				final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

				return BitMapToString(bitmap);
			}
			else{
				result = null;
			}

			StoredObjects.LogMethod("Data","result>>>" + result );
		} catch (Exception e) {
			//  Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}
	public static Bitmap StringToBitMap(String encodedString){
		try{
			byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
			Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
			return bitmap;
		}catch(Exception e){
			e.getMessage();
			return null;
		}
	}
	public static String BitMapToString(Bitmap bitmap){
		ByteArrayOutputStream ByteStream=new  ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG,40, ByteStream);
		byte [] b=ByteStream.toByteArray();
		String temp= Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}
	public static String GET_java(String url, String header, List<NameValuePair> pairs) throws NullPointerException {
		StoredObjects.LogMethod("result", "strResult::-"+pairs+"<Header>"+header+"<link>"+url);
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new CustomHttpClient();//DefaultHttpClient

			/*HttpGet httpget = new HttpGet(url+"?"+ new UrlEncodedFormEntity(pairs, "utf-8"));
			httpget.addHeader("Authorization" , header);*/
			//HttpResponse httpResponse = httpclient.execute(httpget);

			// make GET request to the given URL
			HttpGet httpget = new HttpGet(url);
			httpget.addHeader("Authorization" , header);
			HttpResponse httpResponse = httpclient.execute(httpget);

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if(inputStream != null){
				result = convertInputStreamToString(inputStream);

			}
			else{
				result = "Did not work!";
			}

			StoredObjects.LogMethod("Data","result>>>" + result );
		} catch (Exception e) {
			//  Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}


	public static String PutMethod_java123(String url, String header, List<NameValuePair> pairs)throws URISyntaxException,SocketTimeoutException, UnsupportedEncodingException, ConnectTimeoutException {
		InputStream inputStream = null;
		String result = "";

		try {
			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// 2. make POST request to the given URL

			HttpPut httpPut = new
                    HttpPut("http://000.000.0.000:0000/xxxxxx/webresources/net.xxxxx.users/5");
			String json = "";
			JSONObject jsonObject = new JSONObject();
			json = jsonObject.toString();
			StringEntity se = new StringEntity(json);
			// 6. set httpPost Entity
			httpPut.setEntity(se);
			// 7. Set some headers to inform server about the type of the content
			httpPut.addHeader("Accept", "application/json");
			httpPut.addHeader("Content-type", "application/json");
			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPut);


			//Try to add this
			inputStream = httpResponse.getEntity().getContent();

			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			//Log.d("InputStream", e.getLocalizedMessage());
		}
		return result;

	}

	public static String PutMethod_java(String url, String header, List<NameValuePair> pairs)throws URISyntaxException,SocketTimeoutException, UnsupportedEncodingException, ConnectTimeoutException {
		String response = null;
		HttpClient httpclient = new CustomHttpClient();//DefaultHttpClient
		HttpPut httpPut = new
                HttpPut(url);
		//HttpPost httppost = new HttpPost(url);
		httpPut.addHeader("Authorization" , header);
		try {
			StoredObjects.LogMethod("result", "strResult::-"+pairs+"<Header>"+header+"<link>"+url);

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity( pairs, HTTP.UTF_8 );
			httpPut.addHeader("Accept", "application/json");
			httpPut.addHeader("Content-type", "application/json");
			httpPut.setEntity( ent );
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			HttpResponse httpResponse=	httpclient.execute(httpPut);
			HttpEntity httpEntity=httpResponse.getEntity();
			response = readResponselist_(httpResponse);

			//StoredObject.LogMethod("","http_response:--"+response);
			System.out.println("namevaluepairs:-"+pairs.toString());
			System.out.println("Httpresponse:-"+response.toString());

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return response;

	}

	public static String DeleteMethod_java(String url, String header, List<NameValuePair> pairs) throws NullPointerException {
		StoredObjects.LogMethod("result", "strResult::-"+pairs+"<Header>"+header+"<link>"+url);
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new CustomHttpClient();//DefaultHttpClient
			// make GET request to the given URL
			HttpDelete httpget = new HttpDelete(url);
			httpget.addHeader("Authorization" , header);
			HttpResponse httpResponse = httpclient.execute(httpget);

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if(inputStream != null){
				result = convertInputStreamToString(inputStream);

			}
			else{
				result = "Did not work!";
			}

			StoredObjects.LogMethod("Data","result>>>" + result );
		} catch (Exception e) {
			//  Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}







	public static String PostMethod_java(String url, String header, List<NameValuePair> pairs)throws URISyntaxException,SocketTimeoutException, UnsupportedEncodingException, ConnectTimeoutException {
		String response = null;
		HttpClient httpclient = new CustomHttpClient();//DefaultHttpClient
		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("Authorization" , header);
		httppost.addHeader("Content-Type" , "application/json");
		try {
			StoredObjects.LogMethod("result", "strResult::-"+pairs+"<Header>"+header+"<link>"+url);

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity( pairs, HTTP.UTF_8 );
			httppost.setEntity( ent );
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			HttpResponse httpResponse=	httpclient.execute(httppost);

			HttpEntity httpEntity=httpResponse.getEntity();
			response = readResponselist_(httpResponse);

			System.out.println("namevaluepairs:-"+pairs.toString());
			System.out.println("Httpresponse:-"+response.toString());

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return response;

	}

	public static String PostMethod_JsonObject(String url_s, String header, String jsonstring, String Method)throws URISyntaxException,SocketTimeoutException, UnsupportedEncodingException, ConnectTimeoutException {
		String response = null;
		StoredObjects.LogMethod("result", "strResult::-"+jsonstring+"<Header>"+header+"<link>"+url_s);
		try {
			URL url = new URL(url_s); //Enter URL here
			HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
			httpURLConnection.setDoOutput(true);
			httpURLConnection.addRequestProperty("Authorization",header);
			httpURLConnection.setRequestMethod(Method); //"POST"// here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
			httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
			httpURLConnection.connect();

			DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
			wr.writeBytes(jsonstring);
			wr.flush();
			StringBuilder sb = new StringBuilder();
			int HttpResult = httpURLConnection.getResponseCode();
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				response=sb.toString();
				//System.out.println("" + sb.toString());
			} else {
				response = httpURLConnection.getResponseMessage();
				System.out.println("paymentresponse:--"+httpURLConnection.getResponseMessage());
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("namevaluepairs:-"+jsonstring.toString());
		System.out.println("Httpresponse:-"+response.toString());

		return response;

	}

	public static String readResponselist_(HttpResponse res) {
		InputStream is=null;
		String return_text="";
		try {
			is=res.getEntity().getContent();
			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
			String line="";
			StringBuffer sb=new StringBuffer();
			while ((line=bufferedReader.readLine())!=null)
			{
				sb.append(line);
			}
			return_text=sb.toString();

		} catch (Exception e)
		{

		}
		return return_text;

	}


	public static String PostMethodByUrl(String url) throws SocketTimeoutException, ConnectTimeoutException, UnsupportedEncodingException, URISyntaxException {
		String strResult = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity( nameValuePairs, HTTP.UTF_8 );
			httppost.setEntity( ent );

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			strResult = httpclient.execute(httppost, responseHandler);
			StoredObjects.LogMethod("response","response"+strResult);
			//Toast.makeText(getApplicationContext(), strResult, 0).show();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strResult;
	}


	public static String uploadFile(String sourceFileUri, String uploadurl) {

		StoredObjects.LogMethod("uploadFile", "uploaded_file : " + sourceFileUri );
		String output_result = null;
		int  serverResponseCode =0;
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);
		if (!sourceFile.isFile()) {
			Log.e("uploadFile", "Source File Does not exist");
			return null;
		}
		try{
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			URL url = new URL(uploadurl);
			conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			conn.setRequestProperty("uploaded_file", sourceFileUri);

			StoredObjects.LogMethod("uploadFile", "uploaded_file : " + sourceFileUri );


			dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ sourceFileUri + "\"" + lineEnd);
			dos.writeBytes(lineEnd);


			bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// Responses from the server (code and message)
			serverResponseCode = conn.getResponseCode();
			final String serverResponseMessage = conn.getResponseMessage();
			final String mikel  =  conn.getContentEncoding();
			StoredObjects.LogMethod("", "serverResponseMessage:-"+serverResponseMessage+"<><>>"+mikel);
			output_result = readInputStreamToString(conn);

			fileInputStream.close();
			dos.flush();
			dos.close();



		}catch (MalformedURLException ex) {
			ex.printStackTrace();
			Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
		}
		catch (Exception e) {
			// TODO: handle exception
		}

		return output_result;

	}


	public static String readInputStreamToString(HttpURLConnection connection) {
		String result = null;
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		try{
			is = new BufferedInputStream(connection.getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String inputLine = "";
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
			result = sb.toString();
		}catch (Exception e) {
			StoredObjects.LogMethod("Mikel", "Error reading InputStream");
			result = null;
		}
		finally {
			if (is != null) {
				try {
					is.close();
				}
				catch (IOException e) {
					StoredObjects.LogMethod("Mikel", "Error closing InputStream");
				}
			}
		}
		return result;
	}


	public static String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}



	public static String GET(String url){
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new CustomHttpClient();//DefaultHttpClient

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}
	private static String convertInputStreamToString1(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}


}
