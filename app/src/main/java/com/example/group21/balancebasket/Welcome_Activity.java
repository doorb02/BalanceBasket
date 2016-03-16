package com.example.group21.balancebasket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Welcome_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_);
    }
    public void Start_Connect_Activity(View view) {
        Intent intent = new Intent(this, Connect_Screen.class);
        startActivity(intent);
    }
}
