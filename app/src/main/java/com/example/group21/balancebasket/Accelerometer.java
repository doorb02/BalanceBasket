package com.example.group21.balancebasket;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Accelerometer extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        ActionBar Hamburger = getActionBar();
        assert Hamburger != null;
        Hamburger.setDisplayShowHomeEnabled(false);
        Hamburger.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.header, null);
        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Drawer should open", Toast.LENGTH_SHORT).show();


            }
        });




    }

    public void Open_Drawer(View view) {
            Intent drawer;
            drawer = new Intent(this, Drawer.class);
            startActivity(drawer);
    }
}
