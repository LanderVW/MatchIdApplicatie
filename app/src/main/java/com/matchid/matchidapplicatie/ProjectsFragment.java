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
import java.util.HashMap;
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
    //static final String URL = "http://"+ipadress+":8080/MatchIDEnterpriseApp-war/rest/entities.users";
    static final String URL = "http://api.androidhive.info/pizza/?format=xml";
    // XML node keys
    static final String KEY_ITEM = "item"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_NAME = "username";
    static final String KEY_COST = "cost";
    static final String KEY_DESC = "description";
    ArrayList<HashMap<String, String>> menuItems;
    private static String xmlString;
    static InputStream is;

    private static TextView tv;
    private View view;
    private ListView lv;
    private List<String> strArr;
    private ArrayAdapter<String> adapter;
    XMLParser xmlParser;


    private OnFragmentInteractionListener mListener;

    public ProjectsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProjectsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectsFragment newInstance() {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("tag","onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("tag","OncreateView");
        view = inflater.inflate(R.layout.fragment_projects, container, false);
        tv = (TextView) view.findViewById(R.id.tv);
        lv = (ListView) view.findViewById(R.id.lvproject);
        strArr = new ArrayList<String>();

        //haal alle projecten op (nog niet naar id gekekeken)
        String url = "http://"+LoginActivity.ipadress+":8080/MatchIDEnterpriseApp-war/rest/project";
        Log.d("tag", "start!");
        // haal het op, er is nu nog niks mee gebeurd!

        try {
            xmlParser = new XMLParser();
            xmlParser.execute(url);
            Log.d("ProjectFragment","xmlParser aangemaakt");
//        new XMLTask().execute(url);     //dit mag er nu niet meer in
        }catch (Exception e){
            e.printStackTrace();
        }

        /**
         * dit stuk code vormt de verkregen xml STRING om naar een xml document
         */
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);//waarom is dit eiglijk feitelijk
        DocumentBuilder builder;
        StringReader sr;

        try{


            builder = factory.newDocumentBuilder();Log.d("ProjectFragment", "start bouw xml");
//           // sr = new StringReader(xmlString);Log.d("ProjectFragment", "Stringreader");
//            InputSource inps = new InputSource(xmlString);


            Document doc = builder.parse(is);//xmlParser.loadXMLFromString(xmlString);
            Log.d("log","doc");
            //builder.parse(inps);



//            builder = factory.newDocumentBuilder();Log.d("log","test");
//            Document doc = builder.parse( new InputSource( new StringReader( xmlString ) ) );

//        hier heb je nu een document die een xml bestand bevat


//            InputStream is = getActivity().getAssets().open("user.xml");
//
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("project");
            adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1,strArr);
            lv.setAdapter(adapter);


            for (int i=0; i<nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element2 = (Element) node;
                    strArr.add(getValue("title", element2));
                    Log.d("in if","strArr");
//                    tv.setText("\nName : " + getValue("name", element2)+"\n");
//                    tv.setText(tv.getText()+"Surname : " + getValue("surname", element2)+"\n");
//                    tv.setText(tv.getText()+"-----------------------");
                }
            }
            adapter.notifyDataSetChanged();
//

        } catch (Exception e) {
            Log.d("tag","hij doet het niet");
            e.printStackTrace();}


        return view;
    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public static void setXmlString(InputStream xml) {
        Log.d("ProjectsFragment", "setXmlString");
        is = xml;
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

    public static void settext(String string){
        tv.setText(string);
    }
    public class XMLTask extends AsyncTask<String , String , String> {

        private TextView tv;
        private View view;
        private ListView lv;
        private List<String> strArr;
        private ArrayAdapter<String> adapter;

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection =null;
            BufferedReader reader = null;
            Log.d("try","net voor try");
            Toast.makeText(getActivity(), "doInBackground", Toast.LENGTH_SHORT).show();
            try{
                java.net.URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                Log.d("stream", stream.toString());
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
            Document document = null;
            super.onPostExecute(line);
            //deze onPost wordt uitgevoerd als er iets terug gegeven is
            Log.d("onpostexecute" , line);
            //line is een string
            //maak van string een XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            Document doc = null;

            try{
                builder = factory.newDocumentBuilder();
                doc = builder.parse( new InputSource( new StringReader( line ) ) );

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("tag" , "iets fout tijdens string naar xml");
            }

            //xml doc naar iets dat we kunnen weergeven op de app, hier is iets fout
            try {

            Element element=doc.getDocumentElement();
            element.normalize();
            if(doc == null){
                Log.d("tag" , "leeg");
            }
            NodeList nList = doc.getElementsByTagName("project");
            adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1,strArr);
            lv.setAdapter(adapter);

            for (int i=0; i<nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element2 = (Element) node;
                    strArr.add(getValue("title", element2));
//                    tv.setText("\nName : " + getValue("name", element2)+"\n");
//                    tv.setText(tv.getText()+"Surname : " + getValue("surname", element2)+"\n");
//                    tv.setText(tv.getText()+"-----------------------");
                }
            }
            adapter.notifyDataSetChanged();



        } catch (Exception e) {
            Log.d("tag","hij doet het niet");
            e.printStackTrace();}


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
    }public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
