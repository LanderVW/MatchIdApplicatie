package com.matchid.matchidapplicatie;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by lander on 15/04/2017.
 */

public class XMLParser extends AsyncTask<String , String , InputStream> {

    @Override
    protected InputStream doInBackground(String... urls) {
        HttpURLConnection connection =null;
        BufferedReader reader = null;

        try{
            URL url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            //ProjectsFragment.setXmlString(stream);

            Log.d("stream xmlparser", stream.toString());//dit geeft geen mooie xml pagina terug
//            reader = new BufferedReader(new InputStreamReader(stream));
//            StringBuffer buffer = new StringBuffer();
//
//            String line = "" ;
//            while((line = reader.readLine())!= null){
//                buffer.append(line);
//            }
//            Log.d("string buffer",buffer.toString());
//            return buffer.toString();
            return stream;

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
    protected void onPostExecute(InputStream line) {
        super.onPostExecute(line);
        //deze onPost wordt uitgevoerd als er iets terug gegeven is
        Log.d("xml?" , line.toString());
        //ProjectsFragment.setXmlString(line);

    }

    public Document loadXMLFromString(String xml) throws Exception
    {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Log.d("XMLParser","voor document");
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        Log.d("XMLParser","na document");
        return doc; //builder.parse(new ByteArrayInputStream(xml.getBytes()));
    }


    private String getXML(String xmlString){
        return xmlString;
    }
}
