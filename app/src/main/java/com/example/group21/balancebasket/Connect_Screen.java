package com.example.group21.balancebasket;
//Todo: update connection boolean repeating
//Todo: Remove if fully copied to fragment
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Connect_Screen extends AppCompatActivity {

    private static Bluetooth mChatService;
    private Button motionButton;
    private Button joystickButton;
    private Button followButton;
    private TextView connectionText;
    private TextView chooseText;
    private ProgressBar progressBar;


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
        setContentView(R.layout.activity_connect__screen);

        startService(new Intent(this, Bluetooth.class));

        motionButton = (Button) findViewById(R.id.Motion_Button);
        joystickButton = (Button) findViewById(R.id.Joystick_Button);
        followButton = (Button) findViewById(R.id.Follow_Button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        connectionText = (TextView) findViewById(R.id.Connection_text);
        chooseText = (TextView) findViewById(R.id.choose_Text);

        motionButton.setEnabled(false);
        joystickButton.setEnabled(false);
        followButton.setEnabled(false);
        bindService(new Intent(this, Bluetooth.class), blueConnection, Context.BIND_AUTO_CREATE);
        checkConnection();

    }

    private void checkConnection() {
        if(Bluetooth.connection){
            // TODO: Check with loop
            motionButton.setEnabled(true);
            joystickButton.setEnabled(true);
            followButton.setEnabled(true);
            connectionText.setText("Connection Established!");
            chooseText.setText("Please choose a method of control:");
            //TODO: change icon to check
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    public void Start_Joystick_Activity(View view) {
//        Intent intent = new Intent(this, Joystick.class);
//        startActivity(intent);
    }
    public void Start_Accelerometer_Activity(View view) {
//        Intent intent = new Intent(this, Accelerometer.class);
//        startActivity(intent);
    }
//    public void Start_Follow_Activity(View view) {
//        Intent intent = new Intent(this, Follow.class);
//        startActivity(intent);
//    }
//    public void Start_List_Activity(View view) {
//        Intent intent = new Intent(this, List.class);
//        startActivity(intent);
//    }
}
