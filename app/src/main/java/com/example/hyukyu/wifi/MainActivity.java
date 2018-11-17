package com.example.hyukyu.wifi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("check", "hello");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("check", "hello2");

        Intent intent = new Intent(this, WIFIScanner.class);

        Log.d("check", "hello3");
        startActivity(intent);
    }
}
