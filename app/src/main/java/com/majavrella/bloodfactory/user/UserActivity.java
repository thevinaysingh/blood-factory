package com.majavrella.bloodfactory.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.majavrella.bloodfactory.activities.MainActivity;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.main_toolbar)
    Toolbar toolbar;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        setupDrawerAndToggle();
        navigationView.setNavigationItemSelectedListener(this);
        add(UserHomeFragment.newInstance());
    }

    private void setupDrawerAndToggle() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setDrawerIndicatorEnabled(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    protected ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    @Override
    protected DrawerLayout getDrawer() {
        return drawerLayout;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            //mFirebaseAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(this, " Successfully logout!!!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_member) {
            add(AddMemberFragment.newInstance());
        } else if (id == R.id.donate_blood) {
            add(DonateFragment.newInstance());
        } else if (id == R.id.search_blood) {
            add(RecieveFragment.newInstance());
        } else if (id == R.id.post_blood_request) {
            BloodRequestFragment fragment = new BloodRequestFragment();
            add(BloodRequestFragment.newInstance());
        } else if (id == R.id.people_in_need) {
            PeopleNeedFragment fragment = new PeopleNeedFragment();
            add(PeopleNeedFragment.newInstance());
        }  else if (id == R.id.change_password) {
            ChangePasswordFragment fragment = new ChangePasswordFragment();
            add(ChangePasswordFragment.newInstance());
        } else if (id == R.id.extra_settings) {
            ExtraSettingsFragment fragment = new ExtraSettingsFragment();
            add(ExtraSettingsFragment.newInstance());
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}

