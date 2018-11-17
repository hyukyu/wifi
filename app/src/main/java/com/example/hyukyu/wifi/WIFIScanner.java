package com.example.hyukyu.wifi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

public class WIFIScanner extends Activity implements OnClickListener{

    private static final String TAG = "WIFIScanner";

    WifiManager wifimanager;

    TextView textStatus;
    Button btnScanStart;
    Button btnScanStop;

    private int scanCount = 0;
    String text = "";
    String result = "";

    private List<ScanResult> mScanResult;

    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            final String action = intent.getAction();
            Log.d("check ME ", action);
            if(action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
                getWIFIScanResult();
                wifimanager.startScan();
            } else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
                sendBroadcast(new Intent("wifi.ON_NETWORK_STATE_CHANGED"));
            }
        }

    };

    public void getWIFIScanResult(){
        mScanResult = wifimanager.getScanResults();

        textStatus.setText("Scan count is \t" + ++scanCount + " times \n");
        textStatus.append("Found: " + mScanResult.size() + "\n");
        textStatus.append("============================================\n");
        for(int i=0;i<mScanResult.size();i++){
            ScanResult result = mScanResult.get(i);
            textStatus.append((i+1) + ". SSID : " + result.SSID + "\t\t RSSI: " + result.level + " dBm\n");
        }
        textStatus.append("============================================\n");
    }

    public void initWIFIScan(){
        scanCount = 0;
        text = "";
        final IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
        wifimanager.startScan();
        Log.d(TAG, "initWIFIScan()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //  Setup UI
        textStatus = findViewById(R.id.textStatus);
        btnScanStart = findViewById(R.id.btnScanStart);
        btnScanStop = findViewById(R.id.btnScanStop);

        // setup OnClickListener
        btnScanStart.setOnClickListener(this);
        btnScanStop.setOnClickListener(this);

        // Setup WIFI
        wifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.d(TAG, "Setup WifiManager getSystemService");

        // turn-on wifi WIFIEnabled
        if(!wifimanager.isWifiEnabled())
            wifimanager.setWifiEnabled(true);
    }

    public void printToast(String messageToast){
        Toast.makeText(this, messageToast, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.btnScanStart){
            Log.d(TAG, "OnClick() btnScanStart()");
            printToast("WIFI SCAN!!");
            initWIFIScan();
        }
        if(v.getId() == R.id.btnScanStop){
            Log.d(TAG, "OnClick() btnScanStop()");
            printToast("WIFI STOP!!");
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != getPackageManager().PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 87);
            }
        }
    }
}
