package com.matchid.matchidapplicatie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{


    View view;
    Button btn_add_picture, btn_analyse, btn_logout, btn_projects;

    TextView tv_placename;
    private static final int CAMERA_REQUEST = 123;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * deze methode is een beetje de vervanger van een deftige constructor want een fragment
     * moet alleen een default constructor hebben
     *
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("tag", "onCreate: ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("tag", "onCreateView: ");
        getActivity().setTitle("Home");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        tv_placename = (TextView) view.findViewById(R.id.placename);
        /*
        init de knoppen in de home
         */
        btn_add_picture = (Button) view.findViewById(R.id.btn_camera);//initialitie knop (zet de naam zelf bovenaan de klasse zodat je er overal aankan)
        btn_logout =(Button) view.findViewById(R.id.btn_logout);
        btn_projects = (Button) view.findViewById(R.id.btn_projects);
        btn_analyse = (Button) view.findViewById(R.id.btn_analyse);

        btn_analyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass = AnalyseFragment.class;
                try{
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (java.lang.InstantiationException e) {
                    Log.d("HomeFragment", "instantiationException");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Log.d("HomeFragment", "illegalAccesException");
                    e.printStackTrace();
                } catch(Exception e){
                    Log.d("HomeFragment", "onverwachte fout");
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();
            }
        });
        btn_add_picture.setOnClickListener(getPicture);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(getActivity(),LoginActivity.class);
                startActivity(logout);
            }
        });
        btn_projects.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass = ProjectsFragment.class;
                try{
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (java.lang.InstantiationException e) {
                    Log.d("HomeFragment", "instantiationException");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Log.d("HomeFragment", "illegalAccesException");
                    e.printStackTrace();
                } catch(Exception e){
                    Log.d("HomeFragment", "onverwachte fout");
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();

            }
        });


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



    View.OnClickListener getPicture = new View.OnClickListener(){
        public void onClick(View v){
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, CAMERA_REQUEST);

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case CAMERA_REQUEST:
                if(resultCode==RESULT_OK){
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                }

                break;
            default:break;
        }
    }




    @Override
    public void onClick(View v) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




}

