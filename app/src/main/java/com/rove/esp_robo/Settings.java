package com.rove.esp_robo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class Settings extends AppCompatActivity {
    private String ipaddress;
    private EditText ip,port;
    private TextView ipview;
    private MaterialButton save;
    private TextInputLayout iplayout,portlayout;
    private Context context;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ip = findViewById(R.id.ip_address);
        port = findViewById(R.id.port);
        save = findViewById(R.id.ipsavebtn);
        iplayout = findViewById(R.id.iplayout);
        portlayout = findViewById(R.id.portlayout);
        ipview = findViewById(R.id.curentip);
        context = this;
        pref = getSharedPreferences(MainActivity.IP_KEY,context.MODE_PRIVATE);
        ipview.setText(pref.getString(MainActivity.IP_KEY,MainActivity.IP_DEFLT));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(ip.getText().toString().equals("")){
                  iplayout.setError("Enter valid IP");
              }
              else if (port.getText().toString().equals("")){
                  portlayout.setError("Enter valid PortNo");
              }
              else {
                  ipaddress = ip.getText().toString()+":"+port.getText().toString();
                  pref.edit().putString(MainActivity.IP_KEY,ipaddress).commit();
                  Toast.makeText(context,"Saved Sucessfully..!",Toast.LENGTH_SHORT);
                  ipview.setText(pref.getString(MainActivity.IP_KEY,MainActivity.IP_DEFLT));
              }
            }
        });
    }
}
