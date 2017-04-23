package com.matchid.matchidapplicatie;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author lander
 */

public class ProjectInformationFragment extends Fragment {
    //voor upload
    ProgressDialog prgDialog;

    TextView tv_description, tv_location, tv_numberAnalysis;
    android.support.v7.widget.AppCompatCheckBox cb_active;



    static final String ipadress = LoginActivity.ipadress;
    static int id = LoginActivity.id;


    private View view;
    private ArrayAdapter<String> adapter;


    private OnFragmentInteractionListener mListener;

    public ProjectInformationFragment() {
        // Required empty public constructor
    }

    public static ProjectInformationFragment newInstance() {
        ProjectInformationFragment fragment = new ProjectInformationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Project Info");
        setHasOptionsMenu(true);
        Log.d("pif", "onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("pif", "OncreateView");

        view = inflater.inflate(R.layout.fragment_project_info, container, false);

        tv_description = (TextView) view.findViewById(R.id.tv_description);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        tv_numberAnalysis = (TextView) view.findViewById(R.id.tv_number_analysis);
        cb_active = (android.support.v7.widget.AppCompatCheckBox) view.findViewById(R.id.appCompatCheckBox);
        String description ="";
        String location = "";
        String numberAnalysis = "";
        Boolean active = false;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            description = bundle.getString("description", "default");
            numberAnalysis = bundle.getString("numberAnalysis","default");
            location = bundle.getString("location", "default");
            active = bundle.getBoolean("active",false);

        }

        tv_description.setText(description);
        tv_location.setText(location);
        tv_numberAnalysis.setText(numberAnalysis);
        cb_active.setChecked(active);



        return view;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (prgDialog != null) {
            prgDialog.dismiss();
        }
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
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            Log.d("pif", "error in onAttach" + e.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}




