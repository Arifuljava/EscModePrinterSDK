package com.messas.escmodeprintersdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Findlocation extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDeviceAdapter deviceAdapter;
    private ListView listView;
    private BroadcastReceiver discoveryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findlocation);

        // Request necessary permissions
        requestPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        // Initialize Bluetooth adapter and device adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceAdapter = new BluetoothDeviceAdapter(this, new ArrayList<>());
        listView = findViewById(R.id.list_view);
        listView.setAdapter(deviceAdapter);

        // Register BroadcastReceiver for discovered devices
        discoveryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    deviceAdapter.add(device);
                    deviceAdapter.notifyDataSetChanged();
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryReceiver, filter);

        // Check if Bluetooth is supported and enabled
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
        } else if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled, prompt the user to enable it
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        } else {
            // Bluetooth is enabled, start device discovery
            bluetoothAdapter.startDiscovery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                // Bluetooth is enabled, start device discovery
                bluetoothAdapter.startDiscovery();
            } else {
                Toast.makeText(this, "Bluetooth is required to discover devices", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister BroadcastReceiver
        unregisterReceiver(discoveryReceiver);
    }

    private static class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {
        private LayoutInflater inflater;

        public BluetoothDeviceAdapter(Context context, List<BluetoothDevice> devices) {
            super(context, 0, devices);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.showalllisted, parent, false);
            }

            BluetoothDevice device = getItem(position);
            TextView deviceNameTextView = view.findViewById(R.id.listedd);


            deviceNameTextView.setText(device.getName()+"\n"+device.getAddress());

            CardView carditem=view.findViewById(R.id.carditem);




            carditem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Clicked on : \n"+device.getName()+"\n"+device.getAddress(), Toast.LENGTH_SHORT).show();
                   /*
                    String BlueMac = "FB:7F:9B:F2:20:B7";


                    Intent intent=new Intent(v.getContext(),CPCLFresh.class);
                    intent.putExtra("geeet",""+device.getAddress());
                    v.getContext().startActivity(intent);
                    */
                    ///

                    ////
                      /*
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    /// Toast.makeText(AssenTaskDounwActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                    if (ActivityCompat.checkSelfPermission(ShowAllPairdDevices.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                        m5ocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                                        m5ocket.connect();
                                        Log.d("Connect","Conncted");









                                    }
                                    else {

                                    }




                                } catch (IOException e) {
                                    Log.e("Error", ""+e.getMessage());
                                    Toast.makeText(ShowAllPairdDevices.this, "Try Again. Bluetooth Connection Problem.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                        thread.start();
                       */
                }
            });
            return view;
        }
    }
}