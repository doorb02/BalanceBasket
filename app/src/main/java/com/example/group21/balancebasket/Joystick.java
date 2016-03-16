package com.example.group21.balancebasket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Joystick extends AppCompatActivity implements JoystickView.OnJoystickChangeListener {

    DecimalFormat d = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
    JoystickView mJoystick;
    TextView mText1;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    private double xValue, yValue;

    public static boolean joystickReleased;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LayoutInflater inflater = this.getLayoutInflater();
//        View v = inflater.inflate(R.layout.activity_joystick, container, false);
//        setContentView(R.layout.activity_joystick);
//        mJoystick = (JoystickView) v.findViewById(R.id.joystick);
//        mJoystick.setOnJoystickChangeListener(this);
//
//        mText1 = (TextView) v.findViewById(R.id.textView1);
//        mText1.setText(R.string.defaultJoystickValue);
//
//        Joystick.joystickReleased = true;
//
//        return v;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.activity_joystick, container, false);
//
//        mJoystick = (JoystickView) v.findViewById(R.id.joystick);
//        mJoystick.setOnJoystickChangeListener(this);
//
//        mText1 = (TextView) v.findViewById(R.id.textView1);
//        mText1.setText(R.string.defaultJoystickValue);
//
//        Joystick.joystickReleased = true;
//
//        return v;
//    }

    private void newData(double xValue, double yValue, boolean joystickReleased) {
        if (xValue == 0 && yValue == 0)
            joystickReleased = true;

        Joystick.joystickReleased = joystickReleased;
        this.joystickReleased = joystickReleased;
        this.xValue = xValue;
        this.yValue = yValue;
        mText1.setText("x: " + d.format(xValue) + " y: " + d.format(yValue));
    }

    @Override
    public void setOnTouchListener(double xValue, double yValue) {
        newData(xValue, yValue, false);
    }

    @Override
    public void setOnMovedListener(double xValue, double yValue) {
        newData(xValue, yValue, false);
    }

    @Override
    public void setOnReleaseListener(double xValue, double yValue) {
        newData(xValue, yValue, true);
    }
    @Override
    public void onStart() {
        super.onStart();
        mJoystick.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        mJoystick.invalidate();
        Joystick.joystickReleased = true;

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 150); // Send data every 150ms
//                if (BalanduinoActivity.bluetoothService == null)
//                    return;
//                if (BalanduinoActivity.bluetoothService.getState() == BluetoothChatService.STATE_CONNECTED && BalanduinoActivity.checkTab(ViewPagerAdapter.JOYSTICK_FRAGMENT)) {
//                    if (!getResources().getBoolean(R.bool.isTablet) || !BalanduinoActivity.buttonState) { // Don't send stop if the button in the IMU fragment is pressed
//                        if (joystickReleased || (xValue == 0 && yValue == 0))
//                            BalanduinoActivity.bluetoothService.write(BalanduinoActivity.sendStop);
//                        else {
//                            String message = BalanduinoActivity.sendJoystickValues + d.format(xValue) + ',' + d.format(yValue) + ";";
//                            BalanduinoActivity.bluetoothService.write(message);
//                        }
//                    }
//                }
            }
        };
        mHandler.postDelayed(mRunnable, 150); // Send data every 150ms
    }

    @Override
    public void onPause() {
        super.onPause();
        mJoystick.invalidate();
        Joystick.joystickReleased = true;
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onStop() {
        super.onStop();
        mJoystick.invalidate();
        Joystick.joystickReleased = true;
    }
}
