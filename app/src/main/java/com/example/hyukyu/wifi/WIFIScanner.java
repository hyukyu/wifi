package com.example.hyukyu.wifi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WIFIScanner extends Activity implements OnClickListener{

    private static final String TAG = "WIFIScanner";

    WifiManager wifimanager;

    TextView textStatus;
    Button btnScanStart;
    Button btnScanStop;

    private int scanCount = 0;
    private float timeCount = 0;
    String text = "";
    String result = "";
    private int start = 1;
    private int lowest = -100;
    private String best;

    private List<ScanResult> mScanResult;


    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            final String action = intent.getAction();
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

        ArrayList<String> tmp = new ArrayList<String>();

        int sum = 0;

        // Edit text
        textStatus.setText("Scan count is \t" + ++scanCount + " times \n");
        textStatus.append("Found: " + tmp.size() + "\n");
        textStatus.append("============================================\n");

        // Read
        for(int i=0;i<mScanResult.size();i++){
            ScanResult result = mScanResult.get(i);

            if(start==1 && result.level < 0 && result.level > lowest){
                Log.d("Best", result.SSID + " or " + result.BSSID + " : " + result.level);
                best = result.BSSID;
                lowest = result.level;
            }
            else if(result.BSSID.equals(best)){
                sum = result.level;
            }
        }

        start = 0;

        Log.d("Best: ", best + " level: " + sum);
        textStatus.append("Best: " + best + " level: " + sum + "\n");

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

        //use this?
        WifiInfo wifiInfo = wifimanager.getConnectionInfo();
        Log.d("level", "" + wifiInfo.getRssi());
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
                Log.d("bad", "1");
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 87);
            }
            if(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != getPackageManager().PERMISSION_GRANTED)
            {
                Log.d("bad", "2");
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            }
        }
    }
}
