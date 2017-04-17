package com.matchid.matchidapplicatie;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lander on 15/04/2017.
 */

public class XMLParser extends AsyncTask {

    private static HttpURLConnection urlConnection =null;
    // constructor
    public XMLParser() {
    }

    public InputStream loadXML() {

        try {
            Log.d("start", "loadXml start");
            URL url = new URL("http://"+LoginActivity.ipadress+":8080/MatchIDEnterpriseApp-war/rest/entities.users/username/user");
            urlConnection = (HttpURLConnection) url.openConnection();


            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Log.d("print", "test");
            Log.d("print", in.toString());
            return in;


        } catch (MalformedURLException e) {
            Log.d("error", "urlfout");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Log.d("error", "iofout");
            e.printStackTrace();
            return null;
        }catch (Exception e){
            Log.d("error", "andere fout");
            e.printStackTrace();
            return null;
        } finally{
            Log.d("error", "disconnect");
            urlConnection.disconnect();
        }
    }




    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }


}