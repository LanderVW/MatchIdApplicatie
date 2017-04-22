package com.matchid.matchidapplicatie;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.UnknownHostException;

import static android.graphics.Typeface.BOLD;


public class LoginActivity extends Activity implements QuitDialog.Communicator{
    Button btn_signin, btn_cancel;
    EditText etUsername, etPassword;
    TextView matchid_logo, error_message;
    private ProgressBar spinner;
    public static final String KEY_PRIVATE = "USERNAME";
    static final String ipadress = "192.168.1.7";
    static int id =0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/calibri.ttf");
        matchid_logo = (TextView)findViewById(R.id.matchid_logo);
        matchid_logo.setTypeface(face,BOLD);


        etUsername = (EditText)findViewById(R.id.username);
        etPassword = (EditText)findViewById(R.id.password);
        btn_signin = (Button)findViewById(R.id.btn_signin);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);

        error_message=(TextView)findViewById(R.id.error_message);
        error_message.setVisibility(TextView.INVISIBLE);
        spinner = (ProgressBar)findViewById(R.id.progressLogin);
        spinner.setVisibility(View.GONE);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    spinner.setVisibility(View.VISIBLE);
                    login();
                }catch(Exception e){

                }
            }
        });



        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v);
            }
        });
    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    @Override
    public void onBackPressed() {
        showDialog(this.findViewById(R.id.flContent));
    }

    public void showDialog(View v){
        FragmentManager manager = getFragmentManager();
        QuitDialog dialog = new QuitDialog();

        dialog.show(manager,"dialog");

    }

    public void safeInfo(){
        SharedPreferences userinfo = getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        Editor editor = userinfo.edit();
        editor.putString(KEY_PRIVATE, etUsername.getText().toString());
        editor.commit();
    }

    public String getUsername(){
        SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        return sp.getString("name", "fout");
    }
    public void login() throws UnknownHostException{
        RequestQueue mRequestQueue;
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();

        //ip adres aanpassen naar local ip adres   (command prompt : ipconfig    ->   ipv4adres
        String url ="http://"+ipadress+":8080/MatchIDEnterpriseApp-war/LoginServlet?username="+ etUsername.getText()+
                "&password="+ etPassword.getText()+"&android=true";
        //Toast.makeText(LoginActivity.this, "test", Toast.LENGTH_SHORT).show();


        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        if(!response.equalsIgnoreCase("nowp")){
                            id = Integer.parseInt(response);
                            Log.d("tag" , response);
                            Intent goHome = new Intent(getApplicationContext(), MainActivity.class);
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Welcome " + etUsername.getText().toString() , Toast.LENGTH_SHORT).show();
                            //goHome.putExtra("username", etUsername.getText().toString());
                            //goHome.putExtra("ipadres", ipadress);
                            //safeInfo();

                            startActivity(goHome);
                            spinner.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        if(error.toString().contains("TimeoutError")||error.toString().contains("NoConnectionError")){
                            error_message.setText("Er is iets foutgelopen.\nCheck je connectie en probeer opnieuw.");

                        }else error_message.setText(error.toString());

                        error_message.setVisibility(TextView.VISIBLE);
                        spinner.setVisibility(View.GONE);
                    }
                });

        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }



    @Override
    public void onDialogMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}

