package com.example.group21.balancebasket;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JoystickFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JoystickFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoystickFragment extends Fragment implements JoystickView.OnJoystickChangeListener {
    DecimalFormat d = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
    JoystickView mJoystick;
    TextView mText1;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    private double xValue, yValue;

    public static boolean joystickReleased;

    Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the frag ment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public JoystickFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JoystickFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JoystickFragment newInstance(String param1, String param2) {
        JoystickFragment fragment = new JoystickFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_joystick, container, false);
        View v = inflater.inflate(R.layout.fragment_joystick, container, false);

        mJoystick = (JoystickView) v.findViewById(R.id.joystick);
        mJoystick.setOnJoystickChangeListener(this);

        mText1 = (TextView) v.findViewById(R.id.textView1);
        mText1.setText(R.string.defaultJoystickValue);

        BasketDrawer.joystickReleased = true;

        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().bindService(new Intent(getActivity(), Bluetooth.class), BasketDrawer.blueConnection, Context.BIND_AUTO_CREATE);

        mJoystick.invalidate();
        Joystick.joystickReleased = true;

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 150); // Send data every 150ms
                if (BasketDrawer.bluetoothService == null)
                    return;
                if (BasketDrawer.bluetoothService.getState() == Bluetooth.STATE_CONNECTED) {
                    if (!getResources().getBoolean(R.bool.isTablet) || !BasketDrawer.buttonState) { // Don't send stop if the button in the IMU fragment is pressed
                        if (joystickReleased || (xValue == 0 && yValue == 0))
                            BasketDrawer.bluetoothService.write(BasketDrawer.sendStop);
                        else {
                            String message = BasketDrawer.sendJoystickValues + d.format(xValue) + ',' + d.format(yValue) + ";";
                            BasketDrawer.bluetoothService.write(message);
                        }
                    }
                }
            }
        };
        mHandler.postDelayed(mRunnable, 150); // Send data every 150ms
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setOnTouchListener(double xValue, double yValue) {

    }

    @Override
    public void setOnMovedListener(double xValue, double yValue) {

    }

    @Override
    public void setOnReleaseListener(double xValue, double yValue) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPause() {
        super.onPause();
        mJoystick.invalidate();
        BasketDrawer.joystickReleased = true;
        mHandler.removeCallbacks(mRunnable);
        getActivity().unbindService(BasketDrawer.blueConnection);
    }

    @Override
    public void onStop() {
        super.onStop();
        mJoystick.invalidate();
        BasketDrawer.joystickReleased = true;
    }

}
