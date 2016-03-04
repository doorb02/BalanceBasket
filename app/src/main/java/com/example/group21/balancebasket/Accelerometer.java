package com.example.group21.balancebasket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Accelerometer extends AppCompatActivity {
    public TextView Xas;
    public TextView Yas;
    public TextView Zas;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        Xas = (TextView) findViewById(R.id.x_as);
        Yas = (TextView) findViewById(R.id.y_as);
        Zas = (TextView) findViewById(R.id.z_as);


    }
}


