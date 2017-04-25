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

    /**
     * Required empty public constructor
     */
    public ComponentInformationFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static ComponentInformationFragment newInstance() {
        ComponentInformationFragment fragment = new ComponentInformationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * bij opstart van fragment
     * hier wordt alles gedeclareerd dat niets met de views te maken hebben
     *
     * @param savedInstanceState
     */
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
    /**
     * zorgt voor alles wat het uitzicht bepaald
     * hier worden de parameters geinitialliseerd
     *de onclicklisteners worden hier aangemaakt dit zijn de methodes die zorgen dat
     * items, button, .. kunnen worden geselecteerd en dat er een actie wordt
     * ondernomen
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
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

    /**
     * called to do final cleanup of the fragment's state.
     */
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (prgDialog != null) {
            prgDialog.dismiss();
        }
    }

    /**
     * methode om te kunnen intrageren met de fragment
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    /**
     *called once the fragment is associated with its activity.
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            Log.d("cif", "error in onAttach" + e.toString());
        }
    }
    /**
     *called immediately prior to the fragment no longer being associated with its activity.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    /**
     * This interface must be implemented by activities that contain this fragment
     * to allow an interaction in this fragment to be communicated to the activity
     * and potentially other fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}




