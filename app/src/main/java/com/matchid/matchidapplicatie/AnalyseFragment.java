package com.matchid.matchidapplicatie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;

/**
 * Created by vulst on 19/04/2017.
 */

public class AnalyseFragment extends Fragment {
    //voor upload
    ProgressDialog prgDialog;
    String encodedString;
    RequestParams params = new RequestParams();
    String imgPath, fileName , fileName2;
    Bitmap bitmap;
    private static final int CAMERA_REQUEST = 123;
    private static String TAG = "AnalyseFragment";
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


    private OnFragmentInteractionListener mListener;

    /**
     * Required empty public constructor
     */
    public AnalyseFragment(){
    }

    /**
     * bij opstart van fragment wordt nieuw fragment aangemaakt
     * dit is een vervanger van de constructor
     *
     * @return Fragment
     */
    public static AnalyseFragment newInstance() {
        AnalyseFragment fragment = new AnalyseFragment();
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
        getActivity().setTitle("Analyse");
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreate");

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
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "Oncreate upload PictureViewFragment");
        view = inflater.inflate(R.layout.fragment_analyse, container, false);
        btn_select_picture1 = (Button) view.findViewById(R.id.PicPicture1);
        btn_select_picture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getal = 1;
                loadImagefromGallery();

            }
        });

        btn_select_picture2 = (Button) view.findViewById(R.id.PicPicture2);
        btn_select_picture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getal = 2;
                loadImagefromGallery();
            }
        });

        btn_upload_picture1 = (Button) view.findViewById(R.id.uploadPicture1);
        btn_upload_picture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });




        btn_select_picture1 = (Button) view.findViewById(R.id.PicPicture1);
        btn_select_picture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        btn_upload_picture2 = (Button) view.findViewById(R.id.uploadPicture2);
        btn_upload_picture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });


        btn_analyse = (Button) view.findViewById(R.id.btn_StartAnalyse);
        btn_analyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag" , "text: " + etStepsize.getText().toString());
                if(etStepsize.getText().toString().equals("") | etStepsize.getText().toString().equals("")){
                    Toast.makeText(
                            getActivity(),
                            "Please select subset and stepsize!", Toast.LENGTH_LONG)
                            .show();
                }else {
                    startAnalyse(Integer.parseInt(etSubset.getText().toString()), Integer.parseInt(etStepsize.getText().toString()));
                }

            }
        });

        etStepsize = (EditText) view.findViewById(R.id.etStepsize);
        etSubset = (EditText) view.findViewById(R.id.etSubset);

        prgDialog = new ProgressDialog(getActivity());
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        return view;
    }

    /**
     * de camera wordt opgestart
     */
    public void takePicture(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST);

    }

    /**
     * galery wordt geopend
     * indien meerdere gallerijen aanwezig op de smartphone dan wordt de gebruiker
     * een keuze voorgesteld
     */
    public void loadImagefromGallery() {

        Log.d(TAG, "loadimageFromGallery");
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    /**
     *wordt aangeroepen  na het selecteren van een foto
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            int RESULT_OK = -1;

            Log.d(TAG, String.valueOf(resultCode));
            if (requestCode == RESULT_LOAD_IMG && RESULT_OK == resultCode
                    && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Log.d("tag" ,"gwn test");
                // Get the cursor troubles for getting application context
                Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView;
                if (getal == 1) {
                    imgView = (ImageView) view.findViewById(R.id.ivPicure1);
                } else {
                    imgView = (ImageView) view.findViewById(R.id.ivPicure2);
                }
                // Set the Image in ImageView
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                if(getal == 1){
                    fileName = fileNameSegments[fileNameSegments.length - 1];
                }else{
                    fileName2 = fileNameSegments[fileNameSegments.length - 1];
                }

                // Put file name in Async Http Post Param which will used in Java web app
                params.put("filename", fileName);

            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }


    /**
     * een foto wordt upgeload naar de database
     */
    public void uploadImage() {
        // When Image is selected from Gallery

        Log.d(TAG, "in upload Image");
        if (imgPath != null && !imgPath.isEmpty()) {
            prgDialog.setMessage("Converting Image to Binary Data");
            prgDialog.show();
            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        } else {
            Toast.makeText(
                    getActivity(),
                    "You must select image from gallery before you try to upload",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * een image wordt geconverteerd naar een string
     */
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {
            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);
                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    /**
     * zorgt dat request wordt verzonden
     *
     */
    public void triggerImageUpload() {
        makeHTTPCall();
    }

    /**
     * stuur een request naar de server en vangt statues codes op
     * status code 200: de foto is upgeload
     * status code 404 en 500: er is iets fout gelopen en je moet het nogmaals proberen
     */
    public void makeHTTPCall() {
        prgDialog.setMessage("Uploading image");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.

        String url = "http://" + ipadress + ":8080/MatchIDEnterpriseApp-war/uploadimg.jsp";
        Log.d("AnalyseFragment", url);
        client.post(url,
                params, new AsyncHttpResponseHandler() {

                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("AnalyseFragment", "on success" + statusCode);
                        prgDialog.hide();
                        Toast.makeText(getActivity(), "Picture uploaded!",
                                Toast.LENGTH_LONG).show();
                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // Hide Progress Dialog
                        Log.d("AnalyseFragment", "onfailure");
                        prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getActivity(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getActivity(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getActivity(),
                                    "Error Occured n Most Common Error: n1. Device not connected to Internetn2. Web App is not deployed in App servern3. App server is not runningn HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                });
    }

    /**
     * de analyse op de matchid software wordt gestart met de meegegeven parameters
     *
     * @param sub
     * @param set
     */
    public void startAnalyse(int sub, int set) {
            Log.d("tag", etSubset.getText().toString() + "  " + etStepsize.getText().toString());
            fileName = "Tensile_Hole_Unloaded.tif";
            fileName2 = "Tensile_Hole_2177N.tif";
            String url = "http://" + ipadress + ":8080/MatchIDEnterpriseApp-war/rest/analyse/subset/" + etSubset.getText().toString() + "/stepsize/" + etStepsize.getText().toString() + "/pic1/"+  fileName + "/pic2/" + fileName2;
            new XMLTask().execute(url);
    }

    /**
     * called to do final cleanup of the fragment's state.
     *
     */
    @Override
    public void onDestroy() {
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
            Log.d("AnalyseFragment", "error in onAttach" + e.toString());
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
        void onFragmentInteraction(Uri uri);
    }

    /**
     * @author lander
     */
    public class XMLTask extends AsyncTask<String, String, String> {
        /**
         * in de achtergrond wordt asyncroon de http link aangemaakt
         * en de response wordt teruggegeven in string formaat
         * @param urls
         * @return string
         */
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

        /**
         * er wordt gecontroleerd op de parameter
         * als 'ok' is de analyse het geslaagd
         * anders niet geslaagd
         *
         * @param line
         */
        @Override
        protected void onPostExecute(String line) {
            super.onPostExecute(line);

            if(line.equals("ok")){
                Toast.makeText(getActivity(),
                        "Analysis is done",
                        Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(),
                        "Something went wrong with analysis",
                        Toast.LENGTH_SHORT).show();
            }


        }
    }
}




