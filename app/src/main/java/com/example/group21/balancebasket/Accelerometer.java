package com.example.group21.balancebasket;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Accelerometer extends AppCompatActivity {

private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent accelerometer;
            accelerometer = new Intent(this,Accelerometer.class);
            startActivity(accelerometer);
            //set the fragment initially
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {//set the fragment initially
            Intent random;
            random = new Intent(this,RandomActivity.class);
            startActivity(random);

        } else if (id == R.id.nav_slideshow) {

            Intent settings;
            settings = new Intent(this, Settings.class);
            startActivity(settings);


        } else if (id == R.id.nav_manage) {
            Intent accelerometer;
            accelerometer = new Intent(this,Accelerometer.class);
            startActivity(accelerometer);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        return super.onOptionsItemSelected(item);

    }

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //ActionBar Hamburger = getActionBar();
        //assert Hamburger != null;
        //Hamburger.setDisplayShowHomeEnabled(false);
        //Hamburger.setDisplayShowTitleEnabled(false);
        //LayoutInflater mInflater = LayoutInflater.from(this);

//        View mCustomView = mInflater.inflate(R.layout.header, null);
  //      TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
    //    mTitleTextView.setText("Accelerometer");
      //  Button btn = (Button)findViewById(R.id.button);
        //btn.setOnClickListener(new View.OnClickListener() {


          //  @Override
            //public void onClick(View v) {
              //  Toast.makeText(getApplicationContext(), "Drawer should open", Toast.LENGTH_SHORT).show();


     //       }
 //       });



    public void Open_Drawer(View view) {

        Intent drawer;
            drawer = new Intent(this, Drawer.class);
            startActivity(drawer);
    }
}
