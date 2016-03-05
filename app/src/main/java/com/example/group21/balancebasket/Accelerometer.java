package com.example.group21.balancebasket;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class Accelerometer extends AppCompatActivity {
    private static Bluetooth mChatService;
    private static Activity activity;
    public TextView Xas;
    public TextView Yas;
    private Button mButton;
    public TextView mPitchView;
    public TextView mRollView;
    public TextView mCoefficient;
    private TableRow mTableRow;

    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private int counter = 0;
    private static boolean buttonState;

    private static SensorFusion mSensorFusion = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        Xas = (TextView) findViewById(R.id.x_as);
        Yas = (TextView) findViewById(R.id.y_as);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_accelerometer, container, false);

        mPitchView = (TextView) v.findViewById(R.id.textViewX);
        mRollView = (TextView) v.findViewById(R.id.textViewY);
        mButton = (Button) v.findViewById(R.id.activate_button);

        mHandler.postDelayed(new Runnable() { // Hide the menu icon and tablerow if there is no build in gyroscope in the device
            @Override
            public void run() {
                if (SensorFusion.IMUOutputSelection == -1)
                    mHandler.postDelayed(this, 100); // Run this again if it hasn't initialized the sensors yet
                else if (SensorFusion.IMUOutputSelection != 2) // Check if a gyro is supported
                    mTableRow.setVisibility(View.GONE); // If not then hide the tablerow
            }
        }, 100); // Wait 100ms before running the code

        Accelerometer.buttonState = false;

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 50); // Update IMU data every 50ms
                if (Accelerometer.mSensorFusion == null)
                    return;
                mPitchView.setText(Accelerometer.mSensorFusion.pitch);
                mRollView.setText(Accelerometer.mSensorFusion.roll);
                mCoefficient.setText(Accelerometer.mSensorFusion.coefficient);

                counter++;
                if (counter > 2) { // Only send data every 150ms time
                    counter = 0;
                    if (Accelerometer.mChatService == null)
                        return;
                    if (Accelerometer.mChatService.getState() == Bluetooth.STATE_CONNECTED) { //&& Accelerometer.currentTabSelected == ViewPagerAdapter.IMU_FRAGMENT) {
                        buttonState = mButton.isPressed();
//                            Accelerometer.buttonState = buttonState;

//                            if (Accelerometer.joystickReleased || getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) // Check if joystick is released or we are not in landscape mode
//                                CustomViewPager.setPagingEnabled(!buttonState); // Set the ViewPager according to the button
//                            else
//                                CustomViewPager.setPagingEnabled(false);
//
//                            if (Accelerometer.joystickReleased) {
//                                if (buttonState) {
//                                    lockRotation();
//                                    Accelerometer.mChatService.write(Accelerometer.sendIMUValues + Accelerometer.mSensorFusion.pitch + ',' + Accelerometer.mSensorFusion.roll + ";");
//                                    mButton.setText(R.string.sendingData);
//                                } else {
//                                    unlockRotation();
//                                    Accelerometer.mChatService.write(Accelerometer.sendStop);
//                                    mButton.setText(R.string.notSendingData);
//                                }
//                            }
//                        } else {
//                            mButton.setText(R.string.activate_Button);
//                            if (Accelerometer.currentTabSelected == ViewPagerAdapter.IMU_FRAGMENT && Accelerometer.joystickReleased)
//                                CustomViewPager.setPagingEnabled(true);
//                        }
//                    }
                    }
                }
                ;
                mHandler.postDelayed(mRunnable, 50); // Update IMU data every 50ms
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            private void lockRotation() {
                if (getResources().getBoolean(R.bool.isTablet)) { // Check if the layout can rotate
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); // Lock screen orientation so it doesn't rotate
                    else { // Lock rotation manually - source: http://blogs.captechconsulting.com/blog/eric-miles/programmatically-locking-android-screen-orientation
                        int rotation = getRotation();
                        int lock;

                        if (rotation == Surface.ROTATION_90) // Landscape
                            lock = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                        else if (rotation == Surface.ROTATION_180) // Reverse Portrait
                            lock = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                        else if (rotation == Surface.ROTATION_270) // Reverse Landscape
                            lock = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                        else
                            lock = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                        setRequestedOrientation(lock);
                    }
                }
            }

            public int getRotation() {
                return activity.getWindowManager().getDefaultDisplay().getRotation();
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            private void unlockRotation() {
                if (getResources().getBoolean(R.bool.isTablet)) { // Check if the layout can rotate
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER); // Unlock screen orientation
                    else
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR); // Unlock screen orientation
                }
            }

        };
  }
}
