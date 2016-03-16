package com.example.group21.balancebasket;
//Todo: test connection boolean
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOService;

public class Bluetooth extends IOIOService {
    // Debugging
    private static final String TAG = "Bluetooth";
    private static final boolean D = Accelerometer.D;

    private int mState;
    private byte[] coordinates = new byte[1];

    private final IBinder blueBinder =  new BlueBinder();

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0; // we're doing nothing
    public static final int STATE_CONNECTING = 1; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 2; // now connected to a remote device
    public static final int STATE_DISCONNECTED = 3; //
    public static boolean connection = false;

    /*
        Default constructor
    */

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (intent != null && intent.getAction() != null
                && intent.getAction().equals("stop")) {
            // User clicked the notification. Need to stop the service.
            nm.cancel(0);
            stopSelf();
        } else {
            // Service starting. Create a notification.
//            Notification notification = new Notification(
//                    R.drawable.icon, "IOIO service running",
//                    System.currentTimeMillis());
//            notification
//                    .setLatestEventInfo(this, "IOIO Service", "Click to stop",
//                            PendingIntent.getService(this, 0, new Intent(
//                                    "stop", null, this, this.getClass()), 0));
//            notification.flags |= Notification.FLAG_ONGOING_EVENT;
//            nm.notify(0, notification);
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        IBinder blueBinder = new BlueBinder();
        return blueBinder;
    }

    public class BlueBinder extends Binder
    {
        Bluetooth getBluetooth() {
            return Bluetooth.this;
        }
    }

    /**
     * This is the thread on which all the IOIO activity happens. It will be run
     * every time the application is resumed and aborted when it is paused. The
     * method setup() will be called right after a connection with the IOIO has
     * been established (which might happen several times!). Then, loop() will
     * be called repetitively until the IOIO gets disconnected.
     */
    class Looper extends BaseIOIOLooper {
        /** The on-board LED. */
        private DigitalOutput led_;
        Uart uart;
        OutputStream out;
        InputStream in;




        /**
         * Called every time a connection with IOIO has been established.
         * Typically used to open pins.
         * @see ioio.lib.util.IOIOLooper#setup(IOIO)
         */
        @Override
        protected void setup() throws ConnectionLostException {
            uart = ioio_.openUart(6, 7, 9600, Uart.Parity.NONE, Uart.StopBits.ONE);
            in = uart.getInputStream();
            out = uart.getOutputStream();

            mState = STATE_CONNECTED;
            showVersions(ioio_, "IOIO connected!");
            led_ = ioio_.openDigitalOutput(0, true);
            connection = true;
            }

        /**
         * Called repetitively while the IOIO is connected.
         *
         * @throws ConnectionLostException
         *             When IOIO connection is lost.
         *
         * @see ioio.lib.util.IOIOLooper#loop()
         */
        @Override
        public void loop() throws ConnectionLostException, InterruptedException {
            try {
              out.write(coordinates);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.sleep(100);

        }

        @Override
        public void disconnected() {
//            mState = Bluetooth.STATE_DISCONNECTED;
            toast("IOIO disconnected");
            connection = false;
        }

        @Override
        public void incompatible() {
            showVersions(ioio_, "Incompatible firmware version!");
        }
    }

    /*
        get and show version info
    */
    private void showVersions(IOIO ioio, String title) {
        toast(String.format("%s\n" +
                        "IOIOLib: %s\n" +
                        "Application firmware: %s\n" +
                        "Bootloader firmware: %s\n" +
                        "Hardware: %s",
                title,
                ioio.getImplVersion(IOIO.VersionType.IOIOLIB_VER),
                ioio.getImplVersion(IOIO.VersionType.APP_FIRMWARE_VER),
                ioio.getImplVersion(IOIO.VersionType.BOOTLOADER_VER),
                ioio.getImplVersion(IOIO.VersionType.HARDWARE_VER)));

    }

    private void toast(final String message) {
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getState() {

        return mState;
    }

    /*
        Create IOIO thread
        @see ioio.lib.util.AbstractIOIOActivity#createIOIOThread
    */
    @Override
    protected IOIOLooper createIOIOLooper() {
        return new Looper();
    }

    /**
     * Write
     *
     * @param out The bytes to write
     */
    public void write(byte[] out) {
        coordinates = out;
    }

    public void write(String string) {
        write(string.getBytes());
    }
}
