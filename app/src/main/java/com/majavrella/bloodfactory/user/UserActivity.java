package com.majavrella.bloodfactory.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.majavrella.bloodfactory.appbase.MainActivity;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.BaseActivity;
import com.majavrella.bloodfactory.base.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class UserActivity extends BaseActivity {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.main_toolbar)
    Toolbar toolbar;

    @Bind(R.id.main_navigation)
    ListView drawerList;

    @Bind(R.id.nav_view)
    LinearLayout navigationView;

    @Bind(R.id.edit_profile) LinearLayout mEditProfile;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        setupNavigationItems();
        setupDrawerAndToggle();
        //navigationView.setNavigationItemSelectedListener(this);
        add(UserHomeFragment.newInstance());
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(EditProfileFragment.newInstance());
                drawerLayout.closeDrawer(navigationView);
            }
        });
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

    private void setupNavigationItems() {
        String[] navigationItems = {Constants.kAddMemberFragment, Constants.kDonateBloodFragment, Constants.kSearchBloodFragment,
                Constants.kBloodRequestFragment, Constants.kPeopleInNeedFragment, Constants.kChangePasswordFragment, Constants.kExtraSettingsFragment};
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, navigationItems);
        drawerList.setAdapter(mAdapter);
    }

    @OnItemClick(R.id.main_navigation)
    public void onItemClick(int index) {
        switch (index) {
            case 0:
                add(AddMemberFragment.newInstance());
                break;
            case 1:
                add(DonateFragment.newInstance());
                break;
            case 2:
                add(RecieveFragment.newInstance());
                break;
            case 3:
                add(BloodRequestFragment.newInstance());
                break;
            case 4:
                add(PeopleNeedFragment.newInstance());
                break;
            case 5:
                add(ChangePasswordFragment.newInstance());
                break;
            case 6:
                add(ExtraSettingsFragment.newInstance());
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(navigationView);
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
    protected ListView getDrawerList() {
        return drawerList;
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
/*
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
    }*/
}

