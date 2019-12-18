package com.ethan.imapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ethan.common.Common;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Common();
    }
}
