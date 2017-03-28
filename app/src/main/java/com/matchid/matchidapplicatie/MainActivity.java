package com.matchid.matchidapplicatie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static com.matchid.matchidapplicatie.R.id.btn_camera;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button btn_add_picture, btn_gallery, btn_results;
    ImageView img;
    private static final int CAMERA_REQUEST = 123;
    private static final int GALLERY_REQUEST = 124;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btn_add_picture = (Button) findViewById(btn_camera);//initialitie knop (zet de naam zelf bovenaan de klasse zodat je er overal aankan)
        btn_gallery =(Button) findViewById(R.id.btn_gallery);
        btn_results = (Button) findViewById(R.id.btn_results);

        btn_add_picture.setOnClickListener(openCamera);
        img = (ImageView) findViewById(R.id.img);

    }

    View.OnClickListener openCamera = new View.OnClickListener(){
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
    };

    @Override
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
    }

    @Override
    public void onBackPressed() {
        //dit is als op de ingebouwde terugknop wordt gedrukt
        //als je er in zit, terug naar hoofdmenu
        //indien niet uit de app
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //bij opstarten van mainActivity
        Toast.makeText(this, "create options", Toast.LENGTH_SHORT).show();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
            return true;
        }else if(id ==R.id.logout){
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
