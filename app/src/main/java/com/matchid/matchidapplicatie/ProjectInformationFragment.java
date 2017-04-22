package com.matchid.matchidapplicatie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;

/**
 * @author lander
 */

public class ProjectInformationFragment extends Fragment {
    //voor upload
    ProgressDialog prgDialog;
    String encodedString;
    RequestParams params = new RequestParams();
    String imgPath, fileName;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;


    static final String ipadress = LoginActivity.ipadress;
    static int id = LoginActivity.id;

    private Button btn_select_picture1;
    private Button btn_select_picture2;

    private Button btn_upload_picture1;
    private Button btn_upload_picture2;
    private int getal;

    private Button btn_analyse;

    private EditText etSubset;
    private EditText etStepsize;

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
        Log.d("pif", "onPicture upload Fragment");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("pif", "Oncreate upload PictureViewFragment");

        view = inflater.inflate(R.layout.fragment_project_info, container, false);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here

        inflater.inflate(R.menu.main, menu);

        //return true;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                Log.d("pif","Logout");
                Intent logout = new Intent(getActivity(),LoginActivity.class);
                startActivity(logout);
                return true;
            case R.id.action_user_info:
                Log.d("pif","Action user info");
                return true;

            default:
                break;
        }

        return false;
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




