package com.matchid.matchidapplicatie;

import android.content.Context;
import android.net.Uri;
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

import java.io.InputStream;
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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<HashMap<String, String>> menuItems;

    private TextView tv;
    private View view;
    private ListView lv;
    private List<String> strArr;
    private ArrayAdapter<String> adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProjectsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectsFragment newInstance(String param1, String param2) {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        try {

            InputStream is = getActivity().getAssets().open("user.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("users");
            adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1,strArr);
            lv.setAdapter(adapter);

            for (int i=0; i<nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element2 = (Element) node;
                    strArr.add(getValue("username", element2));
//                    tv.setText("\nName : " + getValue("name", element2)+"\n");
//                    tv.setText(tv.getText()+"Surname : " + getValue("surname", element2)+"\n");
//                    tv.setText(tv.getText()+"-----------------------");
                }
            }
            adapter.notifyDataSetChanged();



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
}
