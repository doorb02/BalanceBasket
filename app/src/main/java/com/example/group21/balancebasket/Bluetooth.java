package com.example.group21.balancebasket;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

public class Bluetooth extends IOIOActivity {
    private Button button_;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        button_ = (Button) findViewById(R.id.button);
    }

    class Looper extends BaseIOIOLooper {
        /** The on-board LED. */
        private DigitalOutput led_;
        private Uart uart_;
        private OutputStream out_;
        private InputStream in_;

        @Override
        protected void setup() throws ConnectionLostException {
            showVersions(ioio_, "IOIO connected!");
            led_.write(true);
            led_ = ioio_.openDigitalOutput(0, true);
            enableUi(true);
            uart_=ioio_.openUart(5,6,9600, Uart.Parity.NONE, Uart.StopBits.ONE);
            out_=uart_.getOutputStream();
            in_=uart_.getInputStream();
        }

        @Override
        public void loop() throws ConnectionLostException, InterruptedException {
            try{
                out_.write(40);
            }
            catch(IOException e){
                e.printStackTrace();
            }

            Thread.sleep(100);
        }

        @Override
        public void disconnected() {
            enableUi(false);
            toast("IOIO disconnected");
            try {
                led_.write(false);
            } catch (ConnectionLostException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void incompatible() {
            showVersions(ioio_, "Incompatible firmware version!");
        }
    }

    @Override
    protected IOIOLooper createIOIOLooper() {
        return new Looper();
    }

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
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private int numConnected_ = 0;

    private void enableUi(final boolean enable) {
       runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (enable) {
                    if (numConnected_++ == 0) {
                        button_.setEnabled(true);
                    }
                } else {
                    if (--numConnected_ == 0) {
                        button_.setEnabled(false);
                    }
                }
            }
        });
    }
}
