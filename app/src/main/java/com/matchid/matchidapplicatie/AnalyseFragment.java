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
import android.view.MenuItem;
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
    String componentId = "4"; //moet nog verander wroden!

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
    private EditText etInputfile;

    private View view;


    private OnFragmentInteractionListener mListener;

    public AnalyseFragment() {
        // Required empty public constructor
    }

    public static AnalyseFragment newInstance() {
        AnalyseFragment fragment = new AnalyseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Analyse");
        setHasOptionsMenu(true);
        Log.d(TAG, "onPicture upload Fragment");

    }

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
                if(etStepsize.getText().toString().equals("") | etStepsize.getText().toString().equals("") | etInputfile.getText().toString().equals("")){
                    Toast.makeText(
                            getActivity(),
                            "Please select subset and stepsize and inputfile name!", Toast.LENGTH_LONG)
                            .show();
                }else {
                    startAnalyse(Integer.parseInt(etSubset.getText().toString()), Integer.parseInt(etStepsize.getText().toString()));
                }

            }
        });

        etStepsize = (EditText) view.findViewById(R.id.etStepsize);
        etSubset = (EditText) view.findViewById(R.id.etSubset);
        etInputfile = (EditText) view.findViewById(R.id.etInputfile);

        prgDialog = new ProgressDialog(getActivity());
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_user_info) {
            Log.d("tag", "userinfo option");
            Toast.makeText(getActivity(), "account", Toast.LENGTH_SHORT).show();
            return false;

        }else if(id ==R.id.logout){
            Log.d("tag", "logout option");
            Intent logout = new Intent(getActivity(), LoginActivity.class);
            startActivity(logout);
            return false;
        }
        return false;
    }

    public void takePicture(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //opstarten intent
        startActivityForResult(camera, CAMERA_REQUEST);//getal doet er niet toe maar moet uniek zijn

    }


    public void loadImagefromGallery() {

        Log.d(TAG, "in loadimagefrom gallery");
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

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
                    params.put("undeformed" , 1);
                    params.put("filename", fileName);
                }else{
                    fileName2 = fileNameSegments[fileNameSegments.length - 1];
                    params.put("undeformed" , 0);
                    params.put("filename", fileName2);
                }

                // Put file name in Async Http Post Param which will used in Java web app

            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

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

    public void triggerImageUpload() {
        makeHTTPCall();
    }

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
                        // Hide Progress Dialog
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


    public void startAnalyse(int sub, int set) {
            Log.d("tag", etSubset.getText().toString() + "  " + etStepsize.getText().toString());
//            fileName = "Tensile_Hole_Unloaded.tif";
//            fileName2 = "Tensile_Hole_2177N.tif";
            Log.d("tag" , fileName);
            Log.d("tag" , fileName2);
            String url = "http://" + ipadress + ":8080/MatchIDEnterpriseApp-war/rest/analyse/subset/" + etSubset.getText().toString() + "/stepsize/" + etStepsize.getText().toString() + "/pic1/"+  fileName + "/pic2/" + fileName2;
                    //+ "/inputfile/" + etInputfile.getText().toString();
            new XMLTask().execute(url);
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
            Log.d("AnalyseFragment", "error in onAttach" + e.toString());
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

            if(line.equals("ok")){
                Toast.makeText(getActivity(),
                        "Analysis is done",
                        Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(),
                        "Something went wrong with analysis",
                        Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, line);
            //line is een string


        }
    }
}




