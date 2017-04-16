package com.majavrella.bloodfactory.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.majavrella.bloodfactory.api.APIConstant;
import com.majavrella.bloodfactory.api.APIManager;
import com.majavrella.bloodfactory.api.APIResponse;
import com.majavrella.bloodfactory.appbase.MainActivity;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.BaseActivity;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserProfileManager;
import com.majavrella.bloodfactory.register.RegisterConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class UserActivity extends BaseActivity {

    protected SharedPreferences mSharedpreferences;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.main_toolbar)
    Toolbar toolbar;

    @Bind(R.id.main_navigation)
    ListView drawerList;

    @Bind(R.id.nav_view)
    LinearLayout navigationView;

    @Bind(R.id.mUsername)
    TextView mUsername;

    @Bind(R.id.mUserMob)
    TextView mUserMob;

    @Bind(R.id.imageView)
    ImageView mImageView;

    static private FirebaseAuth mFirebaseAuth;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        mSharedpreferences = getSharedPreferences(RegisterConstants.userPrefs, Context.MODE_PRIVATE);
        mFirebaseAuth = FirebaseAuth.getInstance();
        setupNavigationItems();
        setupDrawerAndToggle();
        add(UserHomeFragment.newInstance());
        setUsernameAndMob();
    }

    private void setUsernameAndMob() {
        mUsername.setText(UserProfileManager.getInstance().getName());
        mUserMob.setText(UserProfileManager.getInstance().getMobile());
        JSONObject userListObj;
        JSONObject userObj = null;
        final String usersListData = mSharedpreferences.getString(RegisterConstants.usersListData, "DEFAULT_VALUE");
        final String userData = mSharedpreferences.getString(RegisterConstants.userData, "DEFAULT_VALUE");
        try {
            userListObj = new JSONObject(usersListData);
            userObj = new JSONObject(userData);
            mUsername.setText(userObj.getString("name"));
            mUserMob.setText(userListObj.getString("mobile"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                Constants.kBloodRequestFragment, Constants.kPeopleInNeedFragment, Constants.kExtraSettingsFragment};
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
        switch(id){
            case R.id.notification:
                Toast.makeText(this, "Notification in WIP", Toast.LENGTH_SHORT).show();
                break;
            case R.id.guidance:
                add(GuidanceFragment.newInstance());
                break;
            case R.id.about_us:
                add(AboutUs.newInstance());
                break;
            case R.id.faqs:
                add(FAQFragment.newInstance());
                break;
            case R.id.logout:
                mFirebaseAuth.signOut();
                deleteSharedPreferences();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(this, " Successfully logout!!!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSharedPreferences() {
        File f = new File("/data/data/"+getPackageName()+"/shared_prefs/"+RegisterConstants.userPrefs+".xml");
        if(f.exists()){
            f.delete();
        }
    }
}

