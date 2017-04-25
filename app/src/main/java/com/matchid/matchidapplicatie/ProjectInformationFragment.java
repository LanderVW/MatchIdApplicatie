package com.matchid.matchidapplicatie;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.content.ContentValues.TAG;

/**
 * @author lander
 */

public class ProjectInformationFragment extends Fragment {
    //voor upload
    ProgressDialog prgDialog;

    List<String> naamList, componentDescriptionList;
    String projectNaam, projectID = "";
    ListView lv_components;
    TextView tv_description, tv_location, tv_numberAnalysis;
    android.support.v7.widget.AppCompatCheckBox cb_active;
    private static String url;
    Button btn_location;
    GPSTracker gps;



    static final String ipadress = LoginActivity.ipadress;
    static int id = LoginActivity.id;


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
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            getActivity().setTitle(bundle.getString("title"));
            projectID = bundle.getString("projectID");
        }
        setHasOptionsMenu(true);
        Log.d("pif", "onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("pif", "OncreateView");

        view = inflater.inflate(R.layout.fragment_project_info, container, false);
        tv_description = (TextView) view.findViewById(R.id.tv_description);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        btn_location = (Button) view.findViewById(R.id.btn_set_location);
        btn_location.setOnClickListener(getLocation);
        tv_numberAnalysis = (TextView) view.findViewById(R.id.tv_number_analysis);
        cb_active = (android.support.v7.widget.AppCompatCheckBox) view.findViewById(R.id.appCompatCheckBox);

        projectNaam = "";
        String description ="";
        String location = "";
        String numberAnalysis = "";
        Boolean active = false;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            projectNaam = bundle.getString("title");
            description = bundle.getString("description");
            numberAnalysis = bundle.getString("numberAnalysis");
            location = bundle.getString("location");
            active = bundle.getBoolean("active");

        }

        tv_description.setText(description);
        tv_location.setText(location);
        tv_numberAnalysis.setText(numberAnalysis);
        cb_active.setChecked(active);

        //nu kijken we voor de listview

        url = "http://" + ipadress + ":8080/MatchIDEnterpriseApp-war/rest/components/"+projectID;
        naamList = new ArrayList<>();
        componentDescriptionList = new ArrayList<>();
        lv_components = (ListView) view.findViewById(R.id.lv_components);
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, naamList);
        lv_components.setAdapter(adapter);

        Log.d("pif", "start!");
        new XMLTask().execute(url);
        Log.d("pif", "na start");

        lv_components.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = null;
                Class fragmentClass = ComponentInformationFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();

                } catch (java.lang.InstantiationException e) {
                    Log.d("pif", "instantiationException");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Log.d("pif", "illegalAccesException");
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.d("pif", "onverwachte fout");
                    e.printStackTrace();
                }
                Bundle bundle = new Bundle();
                bundle.putString("title",projectNaam);
                bundle.putString("componentNaam",naamList.get(position));
                bundle.putString("description", componentDescriptionList.get(position));
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }



        });



        return view;
    }


    /**
     * @param nd
     */
    public void updateListview(NodeList nd) {
        try {

            for (int i = 0; i < nd.getLength(); i++) {
                Node node = nd.item(i);


                Log.d("pif", "current element: " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    //ier moeten we nog zorgen dat het ook leeg kan zijn
                    Element eElement = (Element) node;
                    naamList.add(getValue("componentNaam", eElement));
                    componentDescriptionList.add(getValue("description",eElement));
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
        Node nodetest= element.getElementsByTagName(tag).item(0);
        if(nodetest == null){
            return "dit invoerveld is leeg";
        }
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();

            Node node = nodeList.item(0);
            return node.getNodeValue();
    }


    public void getAdress(double lat, double lon){
        try {
            Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(lat, lon, 1);
            if (addresses.isEmpty()) {
                //placeName.setText("Waiting for Location");
            }
            else {
                if (addresses.size() > 0) {
                    tv_location.setText(addresses.get(0).getLocality());
                }
            }
        }
        catch(Exception e){
            Toast.makeText(getActivity(), "No Location Name Found", Toast.LENGTH_SHORT).show();
        }
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

                getAdress(latitude,longitude);

            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }

        }
    };

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


    /**
     * @author lander
     */
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

                Log.d("pif", "malformedURL");
                e.printStackTrace();
                return null;
            } catch (IOException e) {

                Log.d("pif", "ioexception");
                e.printStackTrace();

                return null;
            } finally {
                if (connection != null) {

                    Log.d("pif", "disconnect");
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        Log.d("pif", "reader close");
                        reader.close();
                    }
                } catch (IOException e) {

                    Log.d("pif", "ioexception 2");
                    e.printStackTrace();

                    return null;
                }
            }


        }

        /**
         * @param line
         */
        @Override
        protected void onPostExecute(String line) {


            if (line == null) {
                Log.d("pif", "nog keer");
                new XMLTask().execute(url);
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


                //xml doc naar iets dat we kunnen weergeven op de app
                doc.getDocumentElement().normalize();
                Log.d("pif", "root element: " + doc.getDocumentElement().getNodeName());

                //xml doc naar iets dat we kunnen weergeven op de app
                doc.getDocumentElement().normalize();
                Log.d("pif", "root element: " + doc.getDocumentElement().getNodeName());

                NodeList nList = doc.getElementsByTagName("components");

                updateListview(nList);
                int i = 0;
            }
        }

    }
}




