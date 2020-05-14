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

public class Register extends AppCompatActivity {

    String apiKey = "FQUnIaZv3Ictgt1F";
    EditText name, email, phone;
    Button register;
    String TAG = "REGLOG";

    private static final String SHARED_PREFS = "iaeSharedPref";
    RequestQueue QUEUE;
    String URLHTTP = "https://api.mindwebs.org/iae/newuser.php";

    private int activity_register;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        register = (Button) findViewById(R.id.register);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//        SharedPreferences.Editor Editor = sharedPreferences.edit();

        boolean idSet = sharedPreferences.getBoolean("idSet", false);
        boolean otpSet = sharedPreferences.getBoolean("otpSet", false);
        Log.d(TAG, "SKIP VALUE " + idSet);
        if(otpSet){
            startActivity(new Intent(Register.this, MainActivity.class));
        }
        if(idSet){
            startActivity(new Intent(Register.this, Otp.class));
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QUEUE = Volley.newRequestQueue(getApplicationContext());
                httpPost(URLHTTP, name.getText().toString(), email.getText().toString(), phone.getText().toString());
                Intent i = new Intent(Register.this, Otp.class);
//                startActivity(new Intent(Register.this, Otp.class));
                Bundle bundle = new Bundle();
                bundle.putString("phoneNo", phone.getText().toString());
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });

    }

    public void httpPost(String url,
                         final String name,
                         final String email,
                         final String phone){
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name);
                editor.putString("phone", phone);
                editor.putString("email", email);
                if(response.contains("1")) {
                    editor.putBoolean("idSet", true);
                    Toast toast = Toast.makeText(getApplicationContext(),"Please enter OTP sent to your mobile number",Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"There was some error sending the OTP, Please try again",Toast.LENGTH_LONG);
                    toast.show();
                }
                editor.commit();
//                    Log.d(TAG, "SKIP VALUE NOW - " + sharedPreferences.getBoolean("registered", false));
                Log.d(TAG, "RESPONSE FROM SERVER : "+ response);
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
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
//                params.put("timestamp", Long.toString(timeStamp));
                return params;
            }
        };
        QUEUE.add(postRequest);
    }

}
