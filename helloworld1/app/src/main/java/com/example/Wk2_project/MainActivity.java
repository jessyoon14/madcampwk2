package com.example.Wk2_project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.Wk2_project.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
//COMMENT

//Color theme : https://www.colorcombos.com/color-scheme-124.html
public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int MY_PERMISSION_CAMERA = 1111;

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    final DBHelper dbHelper = new DBHelper(MainActivity.this, "MemoBook30.db", null, 1);
    String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CONTACTS

    };

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);


        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                            permissionsDenied += "\n" + per;

                        }

                    }
                    // Show permissionsDenied
                    //updateViews();
                }

                SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
                ViewPager viewPager = findViewById(R.id.view_pager);
                viewPager.setAdapter(sectionsPagerAdapter);


                TabLayout tabs = findViewById(R.id.tabs);
                tabs.setupWithViewPager(viewPager);
                FloatingActionButton fab = findViewById(R.id.fab);
                return;
            }
        }
    }

//    private  boolean checkPermissions() {
//        int result;
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        for (String p:permissions) {
//            result = ContextCompat.checkSelfPermission(this,p);
//            if (result != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(p);
//            }
//        }
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
//            return false;
//        }else{
//            ViewPager viewPager = findViewById(R.id.view_pager);
//            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
//            viewPager.setAdapter(sectionsPagerAdapter);
//            TabLayout tabs = findViewById(R.id.tabs);
//            tabs.setupWithViewPager(viewPager);
//        }
//        return true;
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
//        switch (requestCode) {
//            case MULTIPLE_PERMISSIONS:{
//                if (grantResults.length > 0) {
//                    String permissionsDenied = "";
//                    int cnt = 0;
//                    for (String per : permissionsList) {
//                        if(grantResults[0] == PackageManager.PERMISSION_DENIED){
//                            permissionsDenied += "\n" + per;
//                        }else{
//                            cnt ++;
//                        }
//                    }
//
//                    if(cnt == 4){
//                        ViewPager viewPager = findViewById(R.id.view_pager);
//                        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
//                        viewPager.setAdapter(sectionsPagerAdapter);
//                        TabLayout tabs = findViewById(R.id.tabs);
//                        tabs.setupWithViewPager(viewPager);
////                        return;
//                    }
//                    // Show permissionsDenied
//                    break;
////                       updateViews();
//                }
//                return;
//            }
//        }
//    }

//    Fragment1 frag1;
//    Fragment2 frag2;
//    Fragment3 frag3;
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
            PreActivity.user_email="";

            Intent i = new Intent(MainActivity.this,PreActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//
//        ViewPager viewPager = findViewById(R.id.view_pager);
//        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
//        viewPager.setAdapter(sectionsPagerAdapter);
//        TabLayout tabs = findViewById(R.id.tabs);
//        tabs.setupWithViewPager(viewPager);

        checkPermissions();

    }
}