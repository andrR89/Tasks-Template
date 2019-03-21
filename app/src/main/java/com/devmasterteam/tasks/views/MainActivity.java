package com.devmasterteam.tasks.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.devmasterteam.tasks.R;
import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.infra.SecurityPreferences;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.manager.PriorityManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private PriorityManager priorityManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.priorityManager = new PriorityManager(this);
        this.initialLoad();

        // Incia a fragment padrão
        this.startDefaultFragment();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        int id = item.getItemId();

        try {
            if (id == R.id.nav_all_tasks) {
                fragment = TaskListFragment.newInstance(TaskConstants.TASK_FILTER.VALUES.NO_FILTER.ordinal());
            } else if (id == R.id.nav_next_seven_days) {
                fragment = TaskListFragment.newInstance(TaskConstants.TASK_FILTER.VALUES.NEXT_7_DAYS.ordinal());
            } else if (id == R.id.nav_overdue) {
                fragment = TaskListFragment.newInstance(TaskConstants.TASK_FILTER.VALUES.OVERDUE.ordinal());
            } else if (id == R.id.nav_logout) {
                this.handleLogout();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insere fragment substituindo qualquer existente
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_content, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleLogout() {
        SecurityPreferences preferences = new SecurityPreferences(this);
        preferences.removeStoredString(TaskConstants.HEADER.PERSON_KEY);
        preferences.removeStoredString(TaskConstants.HEADER.TOKEN_KEY);
        preferences.removeStoredString(TaskConstants.USER.NAME);
        preferences.removeStoredString(TaskConstants.USER.EMAIL);
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void initialLoad() {
        this.priorityManager.getList(this.priorityListener());
    }

    private OperationListener priorityListener(){
        return new OperationListener<Boolean>(){

            @Override
            public void onSuccess(Boolean result){
            }

            @Override
            public void onError(int errorCode, String errorMessage){
            }
        };
    }

    /**
     * Incia a fragment padrão
     */
    private void startDefaultFragment() {

        Fragment fragment = null;
        try {
            fragment = TaskListFragment.newInstance(TaskConstants.TASK_FILTER.VALUES.NO_FILTER.ordinal());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insere fragment substituindo qualquer existente
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_content, fragment).commit();
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
    }
}
