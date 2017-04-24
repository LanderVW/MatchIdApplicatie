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

public class ComponentInformationFragment extends Fragment {
    //voor upload
    ProgressDialog prgDialog;

    TextView tv_component_description,tv_projectNaam;



    static final String ipadress = LoginActivity.ipadress;
    static int id = LoginActivity.id;


    private View view;
    private ArrayAdapter<String> adapter;


    private OnFragmentInteractionListener mListener;

    public ComponentInformationFragment() {
        // Required empty public constructor
    }

    public static ComponentInformationFragment newInstance() {
        ComponentInformationFragment fragment = new ComponentInformationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            getActivity().setTitle(bundle.getString("componentNaam"));
        }else getActivity().setTitle("ProjectInfo");
        setHasOptionsMenu(true);
        Log.d("cif", "onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("cif", "OncreateView");

        view = inflater.inflate(R.layout.fragment_info_component, container, false);

        tv_component_description = (TextView) view.findViewById(R.id.tv_component_description);
        tv_projectNaam = (TextView) view.findViewById(R.id.tv_projectNaam);
        String description ="";




        Bundle bundle = this.getArguments();
        if (bundle != null) {
            tv_projectNaam.setText(bundle.getString("title"));
            description = bundle.getString("description");

        }

        tv_component_description.setText(description);

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
            Log.d("cif", "error in onAttach" + e.toString());
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




