package com.matchid.matchidapplicatie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
public class ProjectsFragment extends Fragment{
    static final String ipadress = LoginActivity.ipadress;
    // All static variables

    private View view;
    private ListView lv;
    private List<String> strArr;
    private ArrayAdapter<String> adapter;

    private OnFragmentInteractionListener mListener;

    public ProjectsFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @return fragment
     */
    public static ProjectsFragment newInstance() {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("tag","onCreate");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("tag","OncreateView");
        view = inflater.inflate(R.layout.fragment_projects, container, false);

        lv = (ListView) view.findViewById(R.id.lvproject);
        strArr = new ArrayList<String>();

        //haal alle projecten op (nog niet naar id gekekeken)
        String url = "http://"+LoginActivity.ipadress+":8080/MatchIDEnterpriseApp-war/rest/project/";
        Log.d("tag", "start!");
        new XMLTask().execute(url);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_user_info:
                Log.d("Project Fragment","action user info");
                return false;
            case R.id.logout:
                Log.d("Project Fragment", "logout option");
                Intent logout = new Intent(getActivity(), LoginActivity.class);
                startActivity(logout);

                getActivity().finish();
                return true;

            default:
                break;
        }

        return false;
    }

    /**
     *
     * @param nd
     */
    public void updateListview(NodeList nd){
        try {

            adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1,strArr);
            lv.setAdapter(adapter);
            for (int i = 0; i < nd.getLength(); i++) {
                Node node = nd.item(i);

                Log.d("tag", "current element: " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    strArr.add(getValue("title",eElement));

                    Log.d("tag", "title : " + eElement.getElementsByTagName("title").item(0).getTextContent());
                }
            }

            adapter.notifyDataSetChanged();
        }catch(Exception e) {
            Log.d("tag","hij doet het niet in het parsen van XML naar de app lijst");
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
     *
     * @param uri
     */
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     *
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

    /**
     *
     * @author lander
     */
    public class XMLTask extends AsyncTask<String , String , String> {

        private TextView tv;
        private View view;
        private ListView lv;
        private List<String> strArr;
        private ArrayAdapter<String> adapter;

        /**
         *
         * @param urls
         * @return
         */
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection =null;
            BufferedReader reader = null;

            try{
                java.net.URL url = new URL(urls[0]);
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
                Log.d("ProjectFragment", "malformedURL");
                e.printStackTrace();
            } catch (IOException e) {

                Log.d("ProjectFragment", "ioexception");
                e.printStackTrace();
            }finally {
                if(connection != null){

                    Log.d("ProjectFragment", "disconnect");
                    connection.disconnect();}
                try {
                    if(reader != null){
                        Log.d("ProjectFragment", "reader close");
                        reader.close();}
                } catch (IOException e) {

                    Log.d("ProjectFragment", "ioexception 2");
                    e.printStackTrace();
                }
            }

            return null;
        }

        /**
         *
         * @param line
         */
        @Override
        protected void onPostExecute(String line) {
            super.onPostExecute(line);
            //deze onPost wordt uitgevoerd als er iets terug gegeven is
            Log.d("tag" , line);
            //line is een string
            String[] parts = line.split(("\\?>"));
            String part1 = parts[0];
            String part2 = parts[1];

            Log.d("tag" , part1);
            Log.d("tag" , part2);

            //maak van string een XML file

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            Document doc = null;
            try
            {
                builder = factory.newDocumentBuilder();
                doc = builder.parse( new InputSource( new StringReader( part2 ) ) );

            } catch (Exception e) {
                e.printStackTrace();
            }


            //xml doc naar iets dat we kunnen weergeven op de app
            doc.getDocumentElement().normalize();
            Log.d("tag" , "root element: "+ doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("project");

            updateListview(nList);
        }
    }
}