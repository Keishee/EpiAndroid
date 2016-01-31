package com.example.joseph.app;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.joseph.app.helper.ActiveUser;
import com.example.joseph.app.helper.ApiIntra;

public class FrontPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener,
        PlanningFragment.OnFragmentInteractionListener, ModuleFragment.OnFragmentInteractionListener, GradeFragment.OnFragmentInteractionListener,
        TrombiFragment.OnFragmentInteractionListener {

    private final String TAG = "FrontPageActivity";
    private String login;
    private Fragment currentFragment = null;
    private ActiveUser user = null;

    public ActiveUser getUser() {
        return user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        login = getIntent().getStringExtra("login");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        user = new ActiveUser();
        user.setLogin(login);
        ApiIntra.setActivity(this);
        loadFragment(1);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (currentFragment != null && currentFragment instanceof ModuleFragment) {
            if (!((ModuleFragment) currentFragment).backPressed()) {
                super.onBackPressed();
            }
        }
        else {
            super.onBackPressed();
        }
    }

    public String getLogin() {
        return login;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.front_page, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            loadFragment(1);
        } else if (id == R.id.nav_planning) {
            loadFragment(2);
        } else if (id == R.id.nav_module) {
            loadFragment(3);
        } else if (id == R.id.nav_project) {
            loadFragment(4);
        } else if (id == R.id.nav_grade) {
            loadFragment(5);
        } else if (id == R.id.nav_trombi) {
            loadFragment(6);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    //Handle fragment change
    public void loadFragment(int i) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        HomeFragment homeFragment = new HomeFragment();
//        fragmentTransaction.add(R.id.relativeLayout, homeFragment);
//        fragmentTransaction.commit();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (currentFragment != null) {
            fragmentTransaction.remove(currentFragment);
            fragmentTransaction.commit();
            fragmentTransaction = fragmentManager.beginTransaction();
        }
        switch (i) {
            case 1:
                currentFragment = new HomeFragment();
                break;
            case 2:
                currentFragment = new PlanningFragment();
                break;
            case 3:
                currentFragment = new ModuleFragment();
                break;
            case 4:
                currentFragment = new ProjectFragment();
                break;
            case 5:
                currentFragment = new GradeFragment();
                break;
            case 6:
                currentFragment = new TrombiFragment();
                break;
        }
        fragmentTransaction.add(R.id.relativeLayout, currentFragment);
        fragmentTransaction.commit();
    }
}
