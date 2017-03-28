package com.matchid.matchidapplicatie;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.net.UnknownHostException;

import static android.graphics.Typeface.BOLD;


public class LoginActivity extends Activity {
    Button btn_signin, btn_cancel;
    EditText etUsername, etPassword;
    TextView matchid_logo, error_message;


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

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    login();
                }catch(Exception e){

                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        String url ="http://10.108.16.180:8080/MatchIDEnterpriseApp-war/LoginServlet?username="+ etUsername.getText()+
                "&password="+ etPassword.getText()+"&android=true";

        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        if(response.equalsIgnoreCase("ok")){
                            Intent goHome = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(goHome);
                        }else{
                            error_message.setText(response);
                            error_message.setVisibility(TextView.VISIBLE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        error_message.setText(error.toString());
                        error_message.setVisibility(TextView.VISIBLE);
                    }
                });

        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }
}