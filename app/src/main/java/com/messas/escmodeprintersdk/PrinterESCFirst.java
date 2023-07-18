package com.messas.escmodeprintersdk;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class PrinterESCFirst extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText quantityProductPage,quantityProductPage_speed;
    SeekBar seekBar;
    TextView progressbarsechk;
    TextView connectedornot;

    String geeet;

    /////bitmap data
    Uri imageuri;
    int flag = 0;
    BluetoothSocket m5ocket;
    BluetoothManager mBluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice device;
    ImageView imageposit;

    Button printimageA;
    Bitmap bitmapdataMe;
    TextView printtimer;
    Spinner papertype;
    String valueSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_e_s_c_first);
        ///for the papertype
        papertype=findViewById(R.id.papertype);
        papertype.setOnItemSelectedListener(this);
        quantityProductPage_speed=findViewById(R.id.quantityProductPage_speed);

        String[] textSizes = getResources().getStringArray(R.array.papersize);
        ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.selectitem, textSizes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        papertype.setAdapter(adapter);

        //
        ScrollView scrollView = findViewById(R.id.scrollView);

// Perform smooth scrolling to a specific position within the ScrollView
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                // Replace the X and Y coordinates with the desired scroll position
                scrollView.smoothScrollTo(0, 500);
            }
        });
        //

        printtimer=findViewById(R.id.printtimer);
        quantityProductPage=findViewById(R.id.quantityProductPage);
        progressbarsechk=findViewById(R.id.progressbarsechk);
        connectedornot=findViewById(R.id.connectedornot);
        seekBar=findViewById(R.id.seekBar);
        ImageView closedialouge=findViewById(R.id.closedialouge);
        closedialouge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressbarsechk=findViewById(R.id.progressbarsechk);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                progressbarsechk.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //   Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
            }
        });
        //check connected or not
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean value11 = isBluetoothConnected(PrinterESCFirst.this);

     /*
        if(value11){
            connectedornot.setText("Connected");
            connectedornot.setTextColor(Color.GREEN);
        }
        else{
            connectedornot.setText("Not Connected");
            connectedornot.setTextColor(Color.RED);
        }
      */

        RelativeLayout relagoo=findViewById(R.id.relagoo);
        relagoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Findlocation.class));
            }
        });

        //getdata
        try {
            geeet=getIntent().getStringExtra("geeet");
            if (TextUtils.isEmpty(geeet)|| geeet.equals(null))
            {
                geeet="FB:7F:9B:F2:20:B7";
            }
            else
            {
                geeet=geeet;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(geeet);
    }
    public void decrement(View view) {
        int value = Integer.parseInt(quantityProductPage.getText().toString());
        if (value==1) {
            Toast.makeText(this, "It is the lowest value.Print Copy value is not decrement now.", Toast.LENGTH_SHORT).show();
        }
        else{
            value=value-1;
            quantityProductPage.setText(""+value);
        }
    }

    public void increment(View view) {
        int value = Integer.parseInt(quantityProductPage.getText().toString());
        if (value==99) {
            Toast.makeText(this, "It is the highest value. Print Copy value is not increment now.", Toast.LENGTH_SHORT).show();
        }
        else{
            value=value+1;
            quantityProductPage.setText(""+value);
        }
    }
    public static boolean isBluetoothConnected(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return false; // Bluetooth is not available or not enabled
        }

        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);//Toast.makeText(context, ""+bluetoothManager.getConnectedDevices(BluetoothProfile.GATT), Toast.LENGTH_SHORT).show();

        if (bluetoothManager != null) {
            for (BluetoothDevice device : bluetoothManager.getConnectedDevices(BluetoothProfile.GATT)) {

                if (device.getType() == BluetoothDevice.DEVICE_TYPE_LE || device.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC) {
                    return true; // At least one Bluetooth device is connected
                }
            }
        }

        return false; // No Bluetooth devices connected
    }
    public static boolean isSocketConnected(Socket socket) {
        if (socket == null) {
            return false;
        }

        return socket.isConnected() && !socket.isClosed();
    }

    public static boolean isSocketConnected(String host, int port) {
        try {
            InetAddress address = InetAddress.getByName(host);
            Socket socket = new Socket(address, port);
            boolean isConnected = isSocketConnected(socket);
            socket.close();
            return isConnected;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //print section
    Uri bitmapUri;
    Bitmap mainimageBitmap;

    int PICK=12;
    boolean request=false;
    CountDownTimer countDownTimer,countDownTimer1;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void decrement_speed(View view) {
        int value = Integer.parseInt(quantityProductPage_speed.getText().toString());
        if (value==1) {
            Toast.makeText(this, "It is the lowest value.Print speed value is not decrement now.", Toast.LENGTH_SHORT).show();
        }
        else{
            value=value-1;
            quantityProductPage_speed.setText(""+value);
        }
    }

    public void increment_speed(View view) {
        int value = Integer.parseInt(quantityProductPage_speed.getText().toString());
        if (value==6) {
            Toast.makeText(this, "It is the highest value. Print Speed value is not increment now.", Toast.LENGTH_SHORT).show();
        }
        else{
            value=value+1;
            quantityProductPage_speed.setText(""+value);
        }
    }
}