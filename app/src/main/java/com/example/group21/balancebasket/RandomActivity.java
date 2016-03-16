package com.example.group21.balancebasket;
//Todo: try to replace with drawer and fragments

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class RandomActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent accelerometer;
            accelerometer = new Intent(this, Accelerometer.class);
            startActivity(accelerometer);
            //set the fragment initially
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {//set the fragment initially


        } else if (id == R.id.nav_slideshow) {

            Intent settings;
            settings = new Intent(this, Settings.class);
            startActivity(settings);


        } else if (id == R.id.nav_manage) {
            Intent accelerometer;
            accelerometer = new Intent(this, Accelerometer.class);
            startActivity(accelerometer);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        return super.onOptionsItemSelected(item);

    }
}