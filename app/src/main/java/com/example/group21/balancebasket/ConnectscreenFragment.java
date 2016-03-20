package com.example.group21.balancebasket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConnectscreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConnectscreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConnectscreenFragment extends Fragment {

    private Button motionButton;
    private Button joystickButton;
    private Button followButton;
    private TextView connectionText;
    private TextView chooseText;
    private ProgressBar progressBar;

    private OnFragmentInteractionListener mListener;

    public ConnectscreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConnectscreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConnectscreenFragment newInstance(String param1, String param2) {
        ConnectscreenFragment fragment = new ConnectscreenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_connect__screen, container, false);

//        startService(new Intent(this, Bluetooth.class));

        motionButton = (Button) view.findViewById(R.id.Motion_Button);
        joystickButton = (Button) view.findViewById(R.id.Joystick_Button);
        followButton = (Button) view.findViewById(R.id.Follow_Button);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        connectionText = (TextView) view.findViewById(R.id.Connection_text);
        chooseText = (TextView) view.findViewById(R.id.choose_Text);

        motionButton.setEnabled(false);
        joystickButton.setEnabled(false);
        followButton.setEnabled(false);
//        bindService(new Intent(this, Bluetooth.class), blueConnection, Context.BIND_AUTO_CREATE);
//        checkConnection();
        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

//    public void Start_Joystick_Activity(View view) {
//        Intent intent = new Intent(this, JoystickFragment.class);
//        startActivity(intent);
//    }
//    public void Start_Accelerometer_Activity(View view) {
//        Intent intent = new Intent(this, Accelerometer.class);
//        startActivity(intent);
//    }
//    public void Start_Follow_Activity(View view) {
//        Intent intent = new Intent(this, Follow.class);
//        startActivity(intent);
//    }
//    public void Start_List_Activity(View view) {
//        Intent intent = new Intent(this, List.class);
//        startActivity(intent);
//    }


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
