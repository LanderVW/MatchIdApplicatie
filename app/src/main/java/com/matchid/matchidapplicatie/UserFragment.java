package com.matchid.matchidapplicatie;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by vulst on 18/04/2017.
 */

public class UserFragment extends Fragment {


    static final String ipadress = LoginActivity.ipadress;
    static int id = LoginActivity.id;
    // XML node keys
    static final String KEY_ITEM = "item"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_NAME = "username";
    static final String KEY_COST = "cost";
    static final String KEY_DESC = "description";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<HashMap<String, String>> menuItems;
    String url;
    int tel=0;
    private TextView tvUsername;
    private TextView tvRole;
    private TextView tvLicense;
    private TextView tvCompany;
    private TextView tvStreet;
    private TextView tvHousenumber;
    private TextView tvPostalCode;
    private TextView tvCountry;
    private TextView tvTown;
    private View view;
    private ArrayAdapter<String> adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("tag", "onCreateUserFragment");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("tag", "OncreateViewUserFragment");
        view = inflater.inflate(R.layout.currentuser, container, false);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvRole = (TextView) view.findViewById(R.id.tvRole);

        tvRole = (TextView) view.findViewById(R.id.tvRole);
        tvLicense = (TextView) view.findViewById(R.id.tvLicense);
        tvCompany = (TextView) view.findViewById(R.id.tvCompany);
        tvStreet = (TextView) view.findViewById(R.id.tvStreet);
        tvHousenumber = (TextView) view.findViewById(R.id.tvHousenumber);
        tvPostalCode = (TextView) view.findViewById(R.id.tvPostalCode);
        tvCountry = (TextView) view.findViewById(R.id.tvCountry);
        tvTown = (TextView) view.findViewById(R.id.tvTown);
        //haal current user op
        //de id staat hier boven maar om te testen gebruik ik gwn 3
        url = "http://" + ipadress + ":8080/MatchIDEnterpriseApp-war/rest/entities.users/id/3";
        Log.d("tag", "start van user ophalen!");
        // haal het op, er is nu nog niks mee gebeurd!
        new XMLTask().execute(url);
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
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            Log.d("tag", e.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public class XMLTask extends AsyncTask<String, String, String> {

        private TextView tv;
        private View view;
        private ListView lv;
        private List<String> strArr;
        private ArrayAdapter<String> adapter;

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
            if (line == null && tel<10) {new XMLTask().execute(url); tel++;}
            else {
                super.onPostExecute(line);
                //deze onPost wordt uitgevoerd als er iets terug gegeven is
                Log.d("tag", line);
                //line is een string
                String[] parts = line.split(("\\?>"));
                String part2 = parts[1];
                Log.d("tag", part2);

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


                //xml doc naar iets dat we kunnen weergeven op de app
                doc.getDocumentElement().normalize();
                Log.d("tag", "root element: " + doc.getDocumentElement().getNodeName());
                try {
                    NodeList nList = doc.getElementsByTagName("users");

                    for (int i = 0; i < nList.getLength(); i++) {
                        Node n = nList.item(i);

                        Log.d("tag", "current element: " + n.getNodeName());

                        if (n.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) n;
                            //Log.d("tag", "username: " + eElement.getElementsByTagName("username").item(0).getTextContent());
                            tvUsername.setText(eElement.getElementsByTagName("username").item(0).getTextContent());
                            tvRole.setText(eElement.getElementsByTagName("roles").item(0).getTextContent());

                            NodeList nList2 = doc.getElementsByTagName("company");
                            for (int i2 = 0; i2 < nList2.getLength(); i2++) {
                                Node n2 = nList2.item(i2);

                                Log.d("tag", "current element: " + n2.getNodeName());

                                if (n2.getNodeType() == Node.ELEMENT_NODE) {
                                    Log.d("tag", "ind if");
                                    Element eElement2 = (Element) n2;
                                    //Log.d("tag", "username: " + eElement.getElementsByTagName("username").item(0).getTextContent());
                                    Log.d("tag", eElement2.getElementsByTagName("name").item(0).getTextContent());
                                    tvCompany.setText(eElement2.getElementsByTagName("name").item(0).getTextContent());
                                    tvCountry.setText(eElement2.getElementsByTagName("country").item(0).getTextContent());
                                    tvStreet.setText(eElement2.getElementsByTagName("street").item(0).getTextContent());
                                    tvHousenumber.setText(eElement2.getElementsByTagName("streetnumber").item(0).getTextContent());
                                    tvPostalCode.setText(eElement2.getElementsByTagName("postalCode").item(0).getTextContent());
                                    tvCountry.setText(eElement2.getElementsByTagName("country").item(0).getTextContent());
                                    tvTown.setText(eElement2.getElementsByTagName("town").item(0).getTextContent());
                                    tvLicense.setText(eElement2.getElementsByTagName("endOfLicence").item(0).getTextContent());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d("tag", "hij doet het niet in het parsen van XML naar de app lijst");
                    e.printStackTrace();
                }
            }

        }
    }


}
