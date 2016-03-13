package com.example.group21.balancebasket;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

public class Accelerometer extends AppCompatActivity {
    private static final String TAG = "Accelerometer";
    public static final boolean D = BuildConfig.DEBUG; // This is automatically set when building

    public static Activity activity;
    public static Context context;

    private static boolean joystickReleased = true;
    private Button mButton;
    public TextView mPitchView;
    public TextView mRollView;
    public TableRow mTableRow;

    public final static String sendStop = "CS;";
    public final static String sendIMUValues = "CM,";

    private static Bluetooth mChatService;

    private Handler mHandler;
    private Runnable mRunnable;
    private int counter = 0;
    private static boolean buttonState;

    private static SensorFusion mSensorFusion = null;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_DEVICE_NAME = 3;
    public static final int MESSAGE_DISCONNECTED = 4;
    public static final int MESSAGE_RETRY = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Setup connection with Bluetooth service
    private ServiceConnection blueConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Bluetooth.BlueBinder b = (Bluetooth.BlueBinder) binder;
            mChatService = b.getBluetooth();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mChatService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        context = getApplicationContext();
        setContentView(R.layout.activity_accelerometer);

        // Start Bluetooth service
        startService(new Intent(this, Bluetooth.class));

        // get sensorManager and initialize sensor listeners
        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorFusion = new SensorFusion(getApplicationContext(), mSensorManager);

        mPitchView = (TextView) findViewById(R.id.textView1);
        mRollView = (TextView) findViewById(R.id.textView2);
        mButton = (Button) findViewById(R.id.activate_button);
        mHandler = new Handler();
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

        mHandler.postDelayed(new Runnable() { // Hide the menu icon and tablerow if there is no build in gyroscope in the device
            @Override
            public void run() {
                if (SensorFusion.IMUOutputSelection == -1)
                    mHandler.postDelayed(this, 100); // Run this again if it hasn't initialized the sensors yet
                else if (SensorFusion.IMUOutputSelection != 2) // Check if a gyro is supported
                    mTableRow.setVisibility(View.GONE); // If not then hide the tablerow
            }
        }, 100); // Wait 100ms before running the code
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
        unbindService(blueConnection);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        bindService(new Intent(this, Bluetooth.class), blueConnection, Context.BIND_AUTO_CREATE);
        mSensorFusion.initListeners();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 50); // Update IMU data every 50ms
                if (Accelerometer.mSensorFusion == null)
                    return;
                mPitchView.setText(Accelerometer.mSensorFusion.pitch);
                mRollView.setText(Accelerometer.mSensorFusion.roll);

                counter++;
                if (counter > 2) {
                    counter = 0;
                    if (Accelerometer.mChatService == null)
                        return;
                    if (Accelerometer.mChatService.getState() == Bluetooth.STATE_CONNECTED) {
                        buttonState = true;//mButton.isPressed(); TODO - recreate button state

                        if (Accelerometer.joystickReleased) {
                            if (buttonState) {
                                lockRotation();
                                Accelerometer.mChatService.write(Accelerometer.sendIMUValues + Accelerometer.mSensorFusion.pitch + ',' + Accelerometer.mSensorFusion.roll + ";");
                            } else {
                                unlockRotation();
                                Accelerometer.mChatService.write(Accelerometer.sendStop);
                            }
                        }
                    } else {
                        mButton.setText(R.string.activate_Button);
                    }
                }

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
                return this.getRotation();
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
        });
    }
}