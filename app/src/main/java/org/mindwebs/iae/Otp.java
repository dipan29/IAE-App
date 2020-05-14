package org.mindwebs.iae;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Otp extends AppCompatActivity {

    String apiKey = "FQUnIaZv3Ictgt1F";
    EditText otp;
    Button verifyOTP, resendOTP;
    String TAG = "OTPLOG";

    private static final String SHARED_PREFS = "iaeSharedPref";
    RequestQueue QUEUE;
    String URLVerify = "https://api.mindwebs.org/iae/verifyOTP.php";
    String URLResend = "https://api.mindwebs.org/iae/resendOTP.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otp = (EditText) findViewById(R.id.otp);
        verifyOTP = (Button) findViewById(R.id.verifyOTP);
        resendOTP = (Button) findViewById(R.id.resendOTP);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        final String phoneNo = sharedPreferences.getString("phone", "");

        boolean idSet = sharedPreferences.getBoolean("otpSet", false);
        Log.d(TAG, "SKIP VALUE " + idSet);
        if(idSet){
            startActivity(new Intent(Otp.this, MainActivity.class));
        }

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QUEUE = Volley.newRequestQueue(getApplicationContext());
                verify(URLVerify, otp.getText().toString(), phoneNo);
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(),"OTP was resent to your Mobile Number",Toast.LENGTH_LONG);
                toast.show();
                QUEUE = Volley.newRequestQueue(getApplicationContext());
                resend(URLResend, phoneNo);
            }
        });
    }

    public void resend(String url,
                       final String phone){
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "OTP Was Resent");
                Log.d(TAG, "RESPONSE FROM SERVER : " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    Log.d(TAG, "ERROR : " + responseBody);
                }catch (UnsupportedEncodingException errorr){
                    Log.d(TAG, errorr.toString());
                }
            }
        }){
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<String,String>();
                params.put("apiKey",  apiKey);
                params.put("phone", phone);
//                params.put("timestamp", Long.toString(timeStamp));
                return params;
            }
        };
        QUEUE.add(postRequest);
    }

    public void verify(String url,
                         final String otp,
                         final String phone){
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if(response.contains("1")){
                    editor.putBoolean("otpSet", true);
                    Log.d(TAG, "RESPONSE VALIDATED");
                    Toast toast = Toast.makeText(getApplicationContext(),"Your OTP was Verified",Toast.LENGTH_LONG);
                    toast.show();
                    startActivity(new Intent(Otp.this, MainActivity.class));
                } else {
                    editor.putBoolean("otpSet", false);
                    Log.d(TAG, "RESPONSE INVALIDATED");
                    Toast toast = Toast.makeText(getApplicationContext(),"Your OTP was incorrect, Please Retry or Resend OTP",Toast.LENGTH_LONG);
                    toast.show();
                }
                editor.commit();

                Log.d(TAG, "RESPONSE FROM SERVER : " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    Log.d(TAG, "ERROR : " + responseBody);
                }catch (UnsupportedEncodingException errorr){
                    Log.d(TAG, errorr.toString());
                }
            }
        }){
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<String,String>();
                params.put("apiKey",  apiKey);
                params.put("otp", otp);
                params.put("phone", phone);
//                params.put("timestamp", Long.toString(timeStamp));
                return params;
            }
        };
        QUEUE.add(postRequest);
    }
}
