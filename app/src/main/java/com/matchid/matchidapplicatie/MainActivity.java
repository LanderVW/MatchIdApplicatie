package com.matchid.matchidapplicatie;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import com.matchid.matchidapplicatie.entities.SessionManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener,
            ProjectsFragment.OnFragmentInteractionListener {


    public static Context contextOfApplication;
    private static final String TAG = "MainActivity";
    public ContentResolver getContextOfApplication(){
        return getContentResolver();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextOfApplication = getApplicationContext();
        // Create global configuration and initialize ImageLoader with this config
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

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
            Log.d("MainActivity" , "in de imageview ding");
        }
    };


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

        int id =item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        //Log.d(TAG , id + "id van user: " + R.id.action_user_info);
        if (id == R.id.action_user_info) {

            Log.d(TAG, "userinfo option");
            Toast.makeText(this, "Show user info", Toast.LENGTH_SHORT).show();
            fragmentClass = UserFragment.class;
            try{
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (InstantiationException e) {
                Log.d("tag" , e.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e ){
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return false;

        }else {
            if (id == R.id.logout) {
                //Log.d(TAG, "logout option");
                Toast.makeText(this, "Goodbye!", Toast.LENGTH_SHORT).show();
                Intent logout = new Intent(MainActivity.this, LoginActivity.class);
                SessionManager session = new SessionManager(getApplicationContext());
                session.logoutUser();
                startActivity(logout);
                finish();
                return false;
            }
        }



        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        if(id==R.id.nav_home){
            fragmentClass = HomeFragment.class;
        }else if(id == R.id.nav_projects){
            fragmentClass = ProjectsFragment.class;
            //fragmentClass = PictureViewFragment.class;
            //fragmentClass = AnalyseFragment.class;
        }else if(id == R.id.nav_slideshow){
            fragmentClass = AnalyseFragment.class;
        }

        try{
            Log.d("MainActivity", "newinstance");
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e ){
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
