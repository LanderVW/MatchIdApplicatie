package com.matchid.matchidapplicatie;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by vulst on 18/04/2017.
 */

public class PictureViewFragment extends Fragment {


    static final String ipadress = LoginActivity.ipadress;
    static int id = LoginActivity.id;

    private static final String TAG = "PictureViewFragment";

    String nameUitDbVanResult = "naam ng komen";
    String nameUitDbVanResultPath = "Tensile_Hole_2177N.tif_u";
    String url = "http://"+ ipadress + ":8080/MatchIDEnterpriseApp-war/rest/getImage/imagepath/"+nameUitDbVanResultPath;
    private ImageView ivFoto;
    private TextView tvName;
    private ProgressDialog prgDialog;

    private View view;

    private OnFragmentInteractionListener mListener;

    /**
     * Required empty public constructor
     */
    public PictureViewFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment PictureViewFragment.
     */
    public static PictureViewFragment newInstance() {
        PictureViewFragment fragment = new PictureViewFragment();
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
        Log.d(TAG, "onPictureViewFragment");

    }
    /**
     * zorgt voor alles wat het uitzicht bepaald
     * hier worden de parameters geinitialliseerd
     * de onclicklisteners worden hier aangemaakt dit zijn de methodes die zorgen dat
     * items, button, .. kunnen worden geselecteerd en dat er een actie wordt
     * ondernomen
     *
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "OncreateViewPictureViewFragment");
        prgDialog = new ProgressDialog(getActivity());
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        view = inflater.inflate(R.layout.picture, container, false);
        tvName = (TextView) view.findViewById(R.id.tvName);
        ivFoto = (ImageView) view.findViewById(R.id.fotoweertegeven);
        prgDialog.setMessage("Downloading image");
        prgDialog.show();
        loadImageFromURL(url);

        //view image
        return view;
    }

    /**
     * laadt een foto in
     * @param url
     */
    private void loadImageFromURL(String url) {
        new XMLTask().execute(url);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            Log.d(TAG, "error"+ e.toString());
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

    public class XMLTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                java.net.URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
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
            Log.d(TAG, line);
            //line is een string
            if(line.equals("0")){
                tvName.setText( "The image is not found");
                prgDialog.hide();
            }else {
                byte[] imageByteArray = Base64.decode(line, Base64.DEFAULT);
                try {
                    Bitmap bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                    ivFoto.setImageBitmap(bmp);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
                prgDialog.hide();
            }
        }
    }
}
