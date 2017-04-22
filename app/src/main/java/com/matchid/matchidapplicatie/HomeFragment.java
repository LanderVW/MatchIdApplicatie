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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

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
    ImageView img;
    GPSTracker gps;
    private static final int CAMERA_REQUEST = 123;
    private static final int GALLERY_REQUEST = 124;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        /*
        init de knoppen in de home
         */
        btn_add_picture = (Button) view.findViewById(R.id.btn_camera);//initialitie knop (zet de naam zelf bovenaan de klasse zodat je er overal aankan)
        btn_logout =(Button) view.findViewById(R.id.btn_logout);
        btn_projects = (Button) view.findViewById(R.id.btn_projects);
        btn_analyse = (Button) view.findViewById(R.id.btn_analyse);
        img = (ImageView) view.findViewById(R.id.img);

        btn_analyse.setOnClickListener(getLocation);
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                Log.d("HomeFragment","Logout");
                Intent logout = new Intent(getActivity(),LoginActivity.class);
                startActivity(logout);
                return true;
            case R.id.action_user_info:
                Log.d("HomeFragment","Action user info");
                return true;

            default:
                break;
        }

        return false;
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
            PopupMenu popupMenu = new PopupMenu(getActivity(),btn_add_picture);
            popupMenu.getMenuInflater().inflate(R.menu.add_picture,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //luisteren naar wat je aanklikt in het menu
                    int id = item.getItemId();

                    if (id == R.id.option_from_camera) {
                        //Intent = zorgt dat je naar een andere view kan
                        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //opstarten intent
                        startActivityForResult(camera, CAMERA_REQUEST);//getal doet er niet toe maar moet uniek zijn

                    } else if (id == R.id.option_from_gallery) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(pickPhoto , GALLERY_REQUEST);//one can be replaced with any action code
                    }

                    return true;
                }
            });

            popupMenu.show();


        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);//kweet nie waarom

        switch(requestCode){
            case CAMERA_REQUEST:
                if(resultCode==RESULT_OK){
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                    //img is de imageview voor in de layout de foto te tonen
                    img.setImageBitmap(selectedImage);
                }
                //popup venstertje (short of long = tijd)
                Toast.makeText(getActivity(),"picture added from camera",Toast.LENGTH_SHORT).show();

                break;
            case GALLERY_REQUEST:
                if(resultCode ==RESULT_OK){
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                    img.setImageBitmap(selectedImage);
                }
                Toast.makeText(getActivity(),"picture added from gallery",Toast.LENGTH_SHORT).show();

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



    View.OnClickListener getLocation = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // create class object
            Log.d("home fragment", " get location");
            gps = new GPSTracker(getActivity());

            // check if GPS enabled
            if (gps.canGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                // \n is for new line
                Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }


            //vanaf hier van Axel voor verbinding met mysql
            //ip niet vergeten te veranderen!
            String url = "http://"+LoginActivity.ipadress+":8080/MatchIDEnterpriseApp-war/rest/project";

            Log.d("HomeFragment", "start!");

            XMLParser xml = new XMLParser();
            xml.execute(url);
        }
    };
}

