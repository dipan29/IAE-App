package org.mindwebs.iae;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    Button btn, btn2;
    TextView textView;
    private static final String SHARED_PREFS = "iaeSharedPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String name = sharedPreferences.getString("name", "");

        if(name != ""){
            String message = "Welcome " + name + ",";
            textView.setText(message);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(),"Please login or register using the menu.",Toast.LENGTH_SHORT);
                toast.show();
//                Toast.makeText(this, "Please login or register using the menu.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, Tutor.class));
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "You will be redirected to the Test Portal, Please Login", Toast.LENGTH_LONG).show();

                String url = "http://lms.iaegroup.in";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }


}
