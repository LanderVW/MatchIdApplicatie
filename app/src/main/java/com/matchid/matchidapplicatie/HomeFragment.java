package com.matchid.matchidapplicatie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    Button btn_add_picture, btn_gallery, btn_results, btn_analyse;
    ImageView img;
    TextView username_nav_header, companyname_nav_header;
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
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        btn_gallery =(Button) view.findViewById(R.id.btn_gallery);
        btn_results = (Button) view.findViewById(R.id.btn_results);
        btn_analyse = (Button) view.findViewById(R.id.btn_analyse);
        img = (ImageView) view.findViewById(R.id.img);

        btn_analyse.setOnClickListener(getLocation);
        btn_add_picture.setOnClickListener(getPicture);

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
            Log.d("tag", "probeer http te maken!");
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
            String url = "http://192.168.1.7:8080/MatchIDEnterpriseApp-war/rest/project/";

            Log.d("tag", "start!");

            new XMLTask().execute(url);
        }
    };


        public static class XMLTask extends AsyncTask<String , String , String>{

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection =null;
            BufferedReader reader = null;

            try{
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "" ;
                while((line = reader.readLine())!= null){
                    buffer.append(line);
                }

            return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(connection != null){
                    connection.disconnect();}
                try {
                    if(reader != null){
                        reader.close();}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String line) {
            super.onPostExecute(line);
            //deze onPost wordt uitgevoerd als er iets terug gegeven is
            Log.d("tag" , line);
        }
    }
}

