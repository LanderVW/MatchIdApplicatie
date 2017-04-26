package com.matchid.matchidapplicatie;

import android.content.Context;
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
import android.widget.ListView;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectsFragment extends Fragment {
    static final String ipadress = LoginActivity.ipadress;
    boolean ok = false;
    private View view;
    private ListView lv;
    private List<String> strArr;
    private List<String> descriptionList, locationList, aAnalysisList, projectIDList;
    private List<Boolean> activeList;
    private ArrayAdapter<String> adapter;
    private static final String TAG = "ProjectsFragment";
    private static String url;


    private OnFragmentInteractionListener mListener;

    /**
     * Required empty public constructor
     */
    public ProjectsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment ProjectFragment.
     */
    public static ProjectsFragment newInstance() {
        ProjectsFragment fragment = new ProjectsFragment();
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
        getActivity().setTitle("Projects");
        setHasOptionsMenu(true);
        Log.d("ProjectFragment", "onCreate");

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

        Log.d(TAG, "OncreateView");
        view = inflater.inflate(R.layout.fragment_projects, container, false);
        lv = (ListView) view.findViewById(R.id.lvproject);
        strArr = new ArrayList<>();
        projectIDList = new ArrayList<>();
        descriptionList = new ArrayList<>();
        activeList = new ArrayList<>();
        locationList = new ArrayList<>();
        aAnalysisList = new ArrayList<>();

        url = "http://" + ipadress + ":8080/MatchIDEnterpriseApp-war/rest/project/";

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, strArr);
        lv.setAdapter(adapter);

        new XMLTask().execute(url);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "naar nieuwe activity", Toast.LENGTH_SHORT).show();
                Fragment fragment = null;

                Class fragmentClass = ProjectInformationFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();

                } catch (java.lang.InstantiationException e) {
                    Log.d(TAG, "instantiationException");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Log.d(TAG, "illegalAccesException");
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.d(TAG, "onverwachte fout");
                    e.printStackTrace();
                }
                Bundle bundle = new Bundle();
                bundle.putString("projectID",projectIDList.get(position));
                bundle.putString("title",strArr.get(position));
                bundle.putString("description", descriptionList.get(position));
                bundle.putString("location",locationList.get(position));
                bundle.putBoolean("active",activeList.get(position));
                bundle.putString("numberAnalysis",aAnalysisList.get(position));
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }

        });

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


                Log.d(TAG, "current element: " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    strArr.add(getValue("title", eElement));

                    projectIDList.add(getValue("projectID", eElement));

                    descriptionList.add(getValue("description",eElement));


                    activeList.add(Boolean.parseBoolean(getValue("active",eElement)));

                    locationList.add(getValue("location", eElement));

                    aAnalysisList.add(getValue("numberAnalysis",eElement));

                }
            }

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.d(TAG, "hij doet het niet in het parsen van XML naar de app lijst");
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
     * called to do final cleanup of the fragment's state.
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    /**
     * bepaald wat er gebeurd als op de back knop wordt gedrukt
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
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

                Log.d("ProjectFragment", "malformedURL");
                e.printStackTrace();
                return null;
            } catch (IOException e) {

                Log.d("ProjectFragment", "ioexception");
                e.printStackTrace();

                return null;
            } finally {
                if (connection != null) {

                    Log.d("ProjectFragment", "disconnect");
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        Log.d("ProjectFragment", "reader close");
                        reader.close();
                    }
                } catch (IOException e) {

                    Log.d("ProjectFragment", "ioexception 2");
                    e.printStackTrace();

                    return null;
                }
            }


        }

        /**
         * er wordt gecontroleerd op de parameter
         * die string bevat een xml pagina die eerst wordt gesplitst in bruikbaar en
         * onbruikbaar deel
         * bruikbaar deel wordt gebruikt om via de tags informatie op te halen via een
         * nodelist
         *
         *
         * @param line
         */
        @Override
        protected void onPostExecute(String line) {


            if (line == null) {
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
                //xml doc naar iets dat we kunnen weergeven op de app
                doc.getDocumentElement().normalize();  Log.d("ProjectFragment", "root element: " + doc.getDocumentElement().getNodeName());

                NodeList nList = doc.getElementsByTagName("project");

                updateListview(nList);
            }
        }

    }
}