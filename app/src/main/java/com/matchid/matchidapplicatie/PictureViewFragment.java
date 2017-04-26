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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by vulst on 18/04/2017.
 */

public class PictureViewFragment extends Fragment {

    static final String ipadress = LoginActivity.ipadress;
    static int id = LoginActivity.id;

    private static final String TAG = "PictureViewFragment";

    String nameUitDbVanResult = "naam ng komen";
    String nameUitDbVanResultPath = "Tensile_Hole_2177N.tif_u";
    String url = "http://" + ipadress + ":8080/MatchIDEnterpriseApp-war/rest/getImage/imagepath/" + nameUitDbVanResultPath;

    //die url moet je nog aanpassen om de query op te roepen die alle results teruggeeft
    String urlGetResults = "";
    private ImageView ivFoto;
    private TextView tvName;
    private ProgressDialog prgDialog;
    private ListView lv_results;

    private Button btn_select_new_result;
    private TextView tv_projectnaam, tv_componentnaam, tv_resultnaam;

    private List<String> resultnaamList;

    private ArrayAdapter<String> adapter;


    String componentNaam, componentDescription, resultnaam, projectNaam;
    int projectID, componentID;

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
        Bundle bundle = this.getArguments();
        projectNaam ="";
        if (bundle != null) {
            componentNaam = bundle.getString("componentNaam");
            projectNaam = bundle.getString("title");
            getActivity().setTitle(componentNaam + ": resultatenlijst");
        } else getActivity().setTitle("ProjectInfo");
        Log.d(TAG, "onPictureViewFragment");

    }

    /**
     * zorgt voor alles wat het uitzicht bepaald
     * hier worden de parameters geinitialliseerd
     * de onclicklisteners worden hier aangemaakt dit zijn de methodes die zorgen dat
     * items, button, .. kunnen worden geselecteerd en dat er een actie wordt
     * ondernomen
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


        componentDescription = "";
        resultnaam = "";
        projectID = componentID = -1;
        resultnaamList = new ArrayList<>();

        //get info vanaf componentview
        //haal info op van de meegekregen bundle
        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            componentDescription = bundle.getString("description");
            projectID = bundle.getInt("projectID");
            componentID = bundle.getInt("componentID");

        }
        lv_results = (ListView) view.findViewById(R.id.lv_results);
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, resultnaamList);
        lv_results.setAdapter(adapter);
        loadImageFromURL(url);
        //get data from url
        //deze xmltask laadt de namen van de result in in de lijst 'resultnaamList'
        urlGetResults = "http://"+ ipadress + ":8080/MatchIDEnterpriseApp-war/rest/entities.image/" + componentID;
        new XMLTask2().execute(urlGetResults);
        Log.d("picture","test");


        btn_select_new_result = (Button) view.findViewById(R.id.btn_select_new_result);
        tv_projectnaam = (TextView) view.findViewById(R.id.tv_result_projectnaam);
        tv_componentnaam = (TextView) view.findViewById(R.id.tv_result_componentnaam);
        tv_resultnaam = (TextView) view.findViewById(R.id.tv_result_naam);

        lv_results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //aangezien de url momenteel nog niet aanpasbaar is mag dit direct worden gedaan
                //als je de img kan ophalen van de databank zal het nog moeten veranderen
                //(laat et mij weten)
                //je mag dit commentaar wegdoen indien niet meer nodig
                //


                lv_results.setVisibility(view.GONE);
                btn_select_new_result.setVisibility(View.VISIBLE);
                tv_componentnaam.setVisibility(View.VISIBLE);
                tv_projectnaam.setVisibility(View.VISIBLE);
                tv_resultnaam.setVisibility(View.VISIBLE);


                tv_componentnaam.setText(bundle.getString("componentNaam"));
                tv_projectnaam.setText(projectNaam);
                tv_resultnaam.setText(resultnaamList.get(position));

            }


        });

        btn_select_new_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv_results.setVisibility(View.VISIBLE);
                btn_select_new_result.setVisibility(View.GONE);
                tv_componentnaam.setVisibility(View.GONE);
                tv_projectnaam.setVisibility(View.GONE);
                tv_resultnaam.setVisibility(View.GONE);
            }
        });


        //view image
        return view;
    }

    /**
     * de lijsten met parameters worden aangevuld en de listview wordt upgedatete naa
     * de huidige staat
     *
     * @param nd
     */
    public void updateListview(NodeList nd) {
        try {

            for (int i = 0; i < nd.getLength(); i++) {
                Node node = nd.item(i);


                Log.d("pif", "current element: " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    resultnaamList.add(getValue("name", eElement));
                }
            }

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.d("pif", "hij doet het niet in het parsen van XML naar de app lijst");
            e.printStackTrace();
        }
    }

    /**
     * op basis van een xml tag wordt de waarde terug gegeven in de vorm van een String
     *
     * @param tag
     * @param element
     * @return String
     */
    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }


    /**
     * laadt een foto in
     *
     * @param url
     */
    private void loadImageFromURL(String url) {
        new XMLTask().execute(url);
    }

    /**
     * methode om te kunnen intrageren met de fragment
     *
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * called once the fragment is associated with its activity.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            Log.d(TAG, "error" + e.toString());
        }
    }

    /**
     * called immediately prior to the fragment no longer being associated with its activity.
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
         *
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
         * als '0' dan is de foto niet gevonden
         * anders zal een bitmap worden aangemaakt en wordt die bitmap weergegeven
         * op het scherm
         *
         * @param line
         */
        @Override
        protected void onPostExecute(String line) {
            super.onPostExecute(line);
            prgDialog.show();
            Log.d(TAG, line);
            //line is een string
            if (line.equals("0")) {
                tvName.setText("The image is not found");
                prgDialog.hide();
            } else {
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

    /**
     * @author lander
     */
    public class XMLTask2 extends AsyncTask<String, String, String> {
        /**
         * in de achtergrond wordt asyncroon de http link aangemaakt
         * en de response wordt teruggegeven in string formaat
         *
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
         * als '0' dan is de foto niet gevonden
         * anders zal een bitmap worden aangemaakt en wordt die bitmap weergegeven
         * op het scherm
         *
         * @param line
         */
        @Override
        protected void onPostExecute(String line) {
            if (line == null) {
                new XMLTask2().execute(urlGetResults);
            } else {
                super.onPostExecute(line);
                //deze onPost wordt uitgevoerd als er iets terug gegeven is

                Log.d(TAG, line);
                //line is een string
                String[] parts = line.split(("\\?>"));
                String part1 = parts[0];
                String part2 = parts[1];

                Log.d(TAG, part1);
                Log.d(TAG, part2);

                //maak van string een XML file


                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                Document doc = null;
                try {
                    builder = factory.newDocumentBuilder();
                    doc = builder.parse(new InputSource(new StringReader(part2)));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                doc.getDocumentElement().normalize();

                //xml doc naar iets dat we kunnen weergeven op de app
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("image");

                updateListview(nList);
            }
        }
    }
}
