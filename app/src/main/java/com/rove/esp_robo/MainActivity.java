package com.rove.esp_robo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static final String PREF_NAME = "com.rove.esp_robo.IP";
    public static final String IP_KEY = "com.rove.esp_robo.IP";
    public static final String IP_DEFLT = "0.0.0.0:80";
    private Button Forward,Backward,Left,Right,Cr_T_Forward,Cr_T_Backward,Cr_B_Forward,Cr_B_Backward;
    private Switch LED;
    private final String TAG = "Key";
    private Socket client;
    private boolean isConnected=false;
    private Timer Tloading;
    private TimerTask loadtask;
    private String ipaddress;
    private String toSend_packet="",Recieved_packet="";
    private MenuItem connectBtn;


    private static final String FORWARD = "F";
    private static final String BACKWARD = "B";
    private static final String LEFT = "L";
    private static final String RIGHT = "R";
    private static final String Cr_T_FORWARD = "A";
    private static final String Cr_T_BACKWARD = "D";
    private static final String Cr_B_FORWARD = "W";
    private static final String Cr_B_BACKWARD = "S";
    private static final String Light_N = "N";
    private static final String Light_F = "E";
    private static final String STOP_ALL = "X";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Forward = findViewById(R.id.forwardbtn);
        Backward = findViewById(R.id.backwardbtn);
        Left = findViewById(R.id.left);
        Right = findViewById(R.id.right);
        LED = findViewById(R.id.ledswitch);
        Cr_B_Forward = findViewById(R.id.crainBFbtn);
        Cr_B_Backward = findViewById(R.id.crainBBbtn);
        Cr_T_Forward = findViewById(R.id.crainTFbtn);
        Cr_T_Backward = findViewById(R.id.crainTBbtn);

        LED.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    toSend_packet = Light_N;
                    Log.d(TAG,"light_ON");
                }
                else {
                    toSend_packet = Light_F;
                    Log.d(TAG,"light_OFF");
                }

            }
        });
        Forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        toSend_packet = FORWARD;
                        Log.d(TAG,"forward");
                        Log.d(TAG,ipaddress);
                        return false;
                    case MotionEvent.ACTION_UP:
                        toSend_packet = STOP_ALL;
                        Log.d(TAG,"stoped");
                        return false;
                    default:return false;
                }
            }
        });
        Backward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        toSend_packet = BACKWARD;
                        Log.d(TAG,"backward");
                        return false;
                    case MotionEvent.ACTION_UP:
                        toSend_packet = STOP_ALL;
                        Log.d(TAG,"stoped");
                        return false;
                    default:return false;
                }
            }
        });
        Left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        toSend_packet = LEFT;
                        Log.d(TAG,"left");
                        return false;
                    case MotionEvent.ACTION_UP:
                        toSend_packet = STOP_ALL;
                        Log.d(TAG,"stoped");
                        return false;
                    default:return false;
                }
            }
        });
        Right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        toSend_packet = RIGHT;
                        Log.d(TAG,"right");
                        return false;
                    case MotionEvent.ACTION_UP:
                        toSend_packet = STOP_ALL;
                        Log.d(TAG,"stoped");
                        return false;
                    default:return false;
                }
            }
        });
        Cr_B_Forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        toSend_packet = Cr_B_FORWARD;
                        Log.d(TAG,"Cr Bforward");
                        return false;
                    case MotionEvent.ACTION_UP:
                        toSend_packet = STOP_ALL;
                        Log.d(TAG,"stoped");
                        return false;
                    default:return false;
                }
            }
        });
        Cr_T_Forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        toSend_packet = Cr_T_FORWARD;
                        Log.d(TAG,"Cr Tforward");
                        return false;
                    case MotionEvent.ACTION_UP:
                        toSend_packet = STOP_ALL;
                        Log.d(TAG,"stoped");
                        return false;
                    default:return false;
                }
            }
        });
        Cr_B_Backward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        toSend_packet = Cr_B_BACKWARD;
                        Log.d(TAG,"Cr Bbackward");
                        return false;
                    case MotionEvent.ACTION_UP:
                        toSend_packet = STOP_ALL;
                        Log.d(TAG,"stoped");
                        return false;
                    default:return false;
                }
            }
        });
        Cr_T_Backward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        toSend_packet = Cr_T_BACKWARD;
                        Log.d(TAG,"Cr Tbackward");
                        return false;
                    case MotionEvent.ACTION_UP:
                        toSend_packet = STOP_ALL;
                        Log.d(TAG,"stoped");
                        return false;
                    default:return false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        MenuItem connectbtn,settings;
        connectbtn = menu.findItem(R.id.connect_btn);
        settings = menu.findItem(R.id.settings_btn);
        connectBtn = connectbtn;
        connectbtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (!isConnected) {
                    isConnected=true;
                    create_connection();
                    menuItem.setIcon(R.drawable.connected_icon);
                    Tloading = new Timer();

                    loadtask = new TimerTask() {
                        @Override
                        public void run() {
                            request_packet();
                            recieve_packet();
                        }

                    };
                    Tloading.schedule(loadtask,500,500);
                }

                else{
                    try {

                        Tloading.cancel();
                        Tloading.purge();
                        Tloading = null;
                        client.close();
                        menuItem.setIcon(R.drawable.disconnected_icon);
                        isConnected=false;
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                return true;
            }
        });
        settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(getApplicationContext(),Settings.class));
                return true;
            }
        });
        return true;
    }

    private void create_connection() {
        final String text = ipaddress;
        final int port;
        final String ipaddress;
        if (text != null) {
            String[] splitted = text.split(":");
            ipaddress = splitted[0];
            port = Integer.parseInt(splitted[1]);

            Thread t = new Thread() {
                public void run() {
                    try {
                        client = new Socket(ipaddress, port);
                        isConnected = true;

                    } catch (IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Could not connect!", Toast.LENGTH_SHORT).show();
                                connectBtn.setIcon(R.drawable.disconnected_icon);
                                isConnected=false;
                            }
                        });
                    }
                }
            };
            t.start();
            while(t.isAlive());
        }
    }
    private void request_packet() {
        OutputStream outstream;

        try {
            outstream = client.getOutputStream();
            PrintWriter out = new PrintWriter(outstream);
            String outputPacket=toSend_packet;
            out.print(outputPacket);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Tloading.cancel();
                        Tloading.purge();
                        Tloading = null;
                        client.close();
                        connectBtn.setIcon(R.drawable.disconnected_icon);
                        isConnected=false;
                        Toast.makeText(getApplicationContext(), "Could not connect!", Toast.LENGTH_SHORT).show();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
    private void recieve_packet() {
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    InputStream instream;
                    instream = client.getInputStream();
                    int a = instream.available();
                    if (a > 0) {
                        InputStreamReader inputStreamReader = new InputStreamReader(instream);
                        BufferedReader reader = new BufferedReader(inputStreamReader);
                        final String Recievedstring = reader.readLine();
                        if (Recievedstring != null) {
                            Recieved_packet = Recievedstring;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                }
                            });



                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
        t.start();
        while(t.isAlive());

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        if(pref.contains(IP_KEY)){
            ipaddress = pref.getString(IP_KEY,IP_DEFLT);
        }
        else{
            pref.edit().putString(IP_KEY,IP_DEFLT).commit();
        }
    }
}
