package com.example.group21.balancebasket;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FollowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FollowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowFragment extends Fragment {
    private ToggleButton fButton;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private int counter = 0;
    private boolean toggleButtonState;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FollowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowFragment newInstance(String param1, String param2) {
        FollowFragment fragment = new FollowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        context = getContext().getApplicationContext();
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_follow, container, false);
        View view = inflater.inflate(R.layout.fragment_follow, container, false);

        fButton = (ToggleButton)view.findViewById(R.id.follow_button);

//        fButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            }
//        });

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() { // Hide the menu icon and tablerow if there is no build in gyroscope in the device
            @Override
            public void run() {
//                if (SensorFusion.IMUOutputSelection == -1)
//                    mHandler.postDelayed(this, 100); // Run this again if it hasn't initialized the sensors yet
//                else if (SensorFusion.IMUOutputSelection != 2) // Check if a gyro is supported
//                    mTableRow.setVisibility(View.GONE); // If not then hide the tablerow
            }
        }, 100); // Wait 100ms before running the code

        //BasketDrawer.buttonState = false;
        //toggleButtonState = false;

        return view;
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
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 50); // Update IMU data every 50ms
//                if (BasketDrawer.mSensorFusion == null)
//                    return;

                counter++;
                if (counter > 2) { // Only send data every 150ms time
                    counter = 0;
                    if (BasketDrawer.bluetoothService == null)
                        return;
                    if (BasketDrawer.bluetoothService.getState() == Bluetooth.STATE_CONNECTED){
                        toggleButtonState = fButton.isChecked();
//                        BasketDrawer.buttonState = buttonState;

//                        if (BasketDrawer.joystickReleased || getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) // Check if joystick is released or we are not in landscape mode
//                            ViewPager.setPagingEnabled(!buttonState); // Set the ViewPager according to the button
//                        else
//                            CustomViewPager.setPagingEnabled(false);

                        if (BasketDrawer.joystickReleased) {
                            if (toggleButtonState) {
//                                lockRotation();
                                BasketDrawer.bluetoothService.write(BasketDrawer.sendFollow + ";");
                                fButton.setText(R.string.followModeOn);
                            } else {
//                                unlockRotation();
                                BasketDrawer.bluetoothService.write(BasketDrawer.sendStop);
                                fButton.setText(R.string.followModeOff);
                            }
                        }
                    } else {
                        fButton.setText(R.string.connectFirst);
//                        if (BasketDrawer.currentTabSelected == ViewPagerAdapter.IMU_FRAGMENT && BasketDrawer.joystickReleased)
//                            CustomViewPager.setPagingEnabled(true);
                    }
                }
            }
        };
        mHandler.postDelayed(mRunnable, 50); // Update IMU data every 50ms
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
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
        getActivity().unbindService(BasketDrawer.blueConnection);
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
}
