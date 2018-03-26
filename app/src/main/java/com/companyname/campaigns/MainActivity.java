package com.companyname.campaigns;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,

        Categories.OnFragmentInteractionListener,
        Orders.OnFragmentInteractionListener,
        Home.OnFragmentInteractionListener,
        Favorites.OnFragmentInteractionListener,
        ContentList.OnFragmentInteractionListener,
        NewsList.OnFragmentInteractionListener,
        AddressList.OnFragmentInteractionListener{

    static userPro userInf = null;
    // header
    TextView headerNameSurname, headerMail;
    ImageButton headerCompanyInfoBtn;
    private static final int EXTERNAL_WRİTE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // user login control
        if (userInf == null) {
            finish();
        }
        userInf.setUserName(cap1stChar(userInf.getUserName()));
        userInf.setUserSurname(cap1stChar(userInf.getUserSurname()));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // header element
        headerNameSurname = navigationView.getHeaderView(0).findViewById(R.id.headerNameSurname);
        headerMail = navigationView.getHeaderView(0).findViewById(R.id.headerMail);
        headerCompanyInfoBtn = navigationView.getHeaderView(0).findViewById(R.id.headerCompanyInfoBtn);
        headerCompanyInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CompanyInfo.class);
                startActivity(i);
            }
        });
        String nameSurname = userInf.getUserName() + " " + userInf.getUserSurname();
        headerNameSurname.setText(nameSurname);
        headerMail.setText(userInf.getUserEmail());
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fgt = null;
        Class fgtClass = Home.class;
        // fragment file open
        try {
            fgt = (Fragment) fgtClass.newInstance();
            FragmentManager mng = getSupportFragmentManager();
            mng.beginTransaction().replace(R.id.flgContent, fgt).commit();
        }catch (Exception ex) {
            Log.e("hata",ex.toString());
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            Intent i = new Intent(MainActivity.this, UserSetting.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fgt = null;
        Class fgtClass = null;

        if (id == R.id.nav_home) {
            fgtClass = Home.class;
        }
       else if (id == R.id.nav_categories) {
            fgtClass = Categories.class;
            getSupportActionBar().setTitle("Kategoriler");
        } else if (id == R.id.nav_orders) {
            fgtClass = Orders.class;
        } else if (id == R.id.nav_favorites) {
            fgtClass = Favorites.class;
        } else if (id == R.id.nav_concent) {
            fgtClass = ContentList.class;
        } else if (id == R.id.nav_news) {
            fgtClass = NewsList.class;
        }else if (id == R.id.nav_address) {
            fgtClass = AddressList.class;
        }else if (id == R.id.nav_share) {


            //Share paykaşımı create Muharrem
            //Runtime izin alma
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},EXTERNAL_WRİTE);

                   Bitmap bitmap;
                    OutputStream output;

                    // Image bitmape çevirme
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.ic_launcher);

                    // SD card yolu
                    File filepath = Environment.getExternalStorageDirectory();

                    // Klasör açma
                    File dir = new File(filepath.getAbsolutePath() + "/Share Image Tutorial/");
                    dir.mkdirs();

                    // Kaydedilecek resmin name
                    File file = new File(dir, "sample_wallpaper.png");

                    try {


                        Intent share = new Intent(Intent.ACTION_SEND);

                        // Intent tipi
                        share.setType("image/jpeg");

                        output = new FileOutputStream(file);


                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                        output.flush();
                        output.close();

                        //Verilen path de resim paylaşımı
                        Uri uri = Uri.fromFile(file);

                        // Intent içine resimi basma
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        share.putExtra(Intent.EXTRA_TEXT,"sea");

                        // Paylaşım seçme ekranı
                        startActivity(Intent.createChooser(share, "Share Image Tutorial"));

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }







        } else if (id == R.id.nav_send) {

        }

        // fragment file open
        try {
            fgt = (Fragment) fgtClass.newInstance();
            FragmentManager mng = getSupportFragmentManager();
            mng.beginTransaction().replace(R.id.flgContent, fgt).commit();
        }catch (Exception ex) {
            Log.e("hata",ex.toString());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static String cap1stChar(String userIdea)
    {
        char[] stringArray = userIdea.toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        return userIdea = new String(stringArray);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    //Android 6dan sonra uygulama içinde runtime izin alma methodu
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.e("Muharrem","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }



}
