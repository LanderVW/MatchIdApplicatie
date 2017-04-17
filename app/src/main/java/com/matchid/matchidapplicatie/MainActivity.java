package com.matchid.matchidapplicatie;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener, ProjectsFragment.OnFragmentInteractionListener {

    Button btn_add_picture, btn_gallery, btn_results, btn_analyse;
    ImageView img;
    TextView username_nav_header, companyname_nav_header;
    GPSTracker gps;
    private static final int CAMERA_REQUEST = 123;
    private static final int GALLERY_REQUEST = 124;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = HomeFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

        /*
        init de drawer
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        /*
        init hoe die eruit ziet
         */
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

    }
    /*de onclicklistener voor de location
    * er wordt momenteel gewoon een toast getoond op het scherm
     */
    View.OnClickListener getLocation = new View.OnClickListener(){
        @Override
        public void onClick(View arg0) {
            // create class object
            gps = new GPSTracker(MainActivity.this);
            Log.d("tag","probeer http te maken");
            // check if GPS enabled
            if(gps.canGetLocation()){

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                // \n is for new line
                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }




        }
    };
/*
*de onclicklistener voor de camera te gebruiken
* er verschijnt een dialoogvenster die aangeeft of je een nieuwe
* foto wil nemen of een van de gallery wil gebruiken
 */
    /*View.OnClickListener openCamera = new View.OnClickListener(){
        public void onClick(View v){
            PopupMenu popupMenu = new PopupMenu(MainActivity.this,btn_add_picture);
            popupMenu.getMenuInflater().inflate(R.menu.add_picture,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //luisteren naar wat je aanklikt in het menu
                    int id = item.getItemId();

                    if (id == R.id.option_from_camera) {
                        //Intent = zorgt dat je naar een andere view kan
                        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //opstarten intent
                        startActivityForResult(camera, CAMERA_REQUEST);//getal doet er niet toe maar moet uniek zijn

                    } else if (id == R.id.option_from_gallery) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(pickPhoto , GALLERY_REQUEST);//one can be replaced with any action code
                    }

                    return true;
                }
            });

            popupMenu.show();


        }
    };*/

    /*
     *na het onvangen van de foto wordt die opgeslagen
     */
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);//kweet nie waarom

        switch(requestCode){
            case CAMERA_REQUEST:
                if(resultCode==RESULT_OK){
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                    //img is de imageview voor in de layout de foto te tonen
                    img.setImageBitmap(selectedImage);
                }
                //popup venstertje (short of long = tijd)
                Toast.makeText(this,"picture added from camera",Toast.LENGTH_SHORT).show();

            break;
            case GALLERY_REQUEST:
                if(resultCode ==RESULT_OK){
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                    img.setImageBitmap(selectedImage);
                }
                Toast.makeText(this,"picture added from gallery",Toast.LENGTH_SHORT).show();

                break;
            default:break;
        }
    }*/

    /*
     * methode die aangeeft wat gebeurt als op de terug
     * knop wordt gedrukt
     */
    @Override
    public void onBackPressed() {
        //dit is als op de ingebouwde terugknop wordt gedrukt
        //als je er in zit, terug naar hoofdmenu
        //indien niet uit de app
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //bij opstarten van mainActivity
        //Toast.makeText(this, "create options", Toast.LENGTH_SHORT).show();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_user_info) {
            Toast.makeText(this, "account", Toast.LENGTH_SHORT).show();

            return true;
        }else if(id ==R.id.logout){
            Intent logout = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logout);

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        if(id==R.id.nav_home){
            Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
            fragmentClass = HomeFragment.class;
        }else if(id ==R.id.nav_projects){
            Toast.makeText(this, "projects", Toast.LENGTH_SHORT).show();
            fragmentClass = ProjectsFragment.class;
        }
        try{
            Log.d("newinstance", "newinstance");
            fragment = (Fragment) fragmentClass.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
