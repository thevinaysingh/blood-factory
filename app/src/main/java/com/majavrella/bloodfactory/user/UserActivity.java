package com.majavrella.bloodfactory.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class UserActivity extends BaseActivity {

    protected SharedPreferences mSharedpreferences;
    protected SharedPreferences.Editor editor;
    protected UserProfileManager userProfileManager;
    private ProgressDialog progressDialog;
    private StorageReference storageRef;

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

    @Bind(R.id.edit_profile_direct)
    TextView mEditProfile;

    static private FirebaseAuth mFirebaseAuth;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(RegisterConstants.waitProgress);
        progressDialog.show();

        userProfileManager = UserProfileManager.getInstance();
        mSharedpreferences = getSharedPreferences(RegisterConstants.userPrefs, Context.MODE_PRIVATE);
        editor = mSharedpreferences.edit();
        // Create a storage reference from our app
        storageRef = FirebaseStorage.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        setupNavigationItems();
        setupDrawerAndToggle();
        add(UserHomeFragment.newInstance());
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(navigationView);
                add(EditProfileFragment.newInstance());
            }
        });
    }

    private void setDataFromLocal() {
        JSONObject userListObject = null ;
        JSONObject userObject = null;
        String usersListData = mSharedpreferences.getString(RegisterConstants.usersListData, "DEFAULT_VALUE");
        String userData = mSharedpreferences.getString(RegisterConstants.userData, "DEFAULT_VALUE");
        try {
            userListObject = new JSONObject(usersListData);
            userObject = new JSONObject(userData);
            userProfileManager.setUserListData(userListObject);
            userProfileManager.setUserData(userObject);
            mUserMob.setText(userProfileManager.getMobile());
            mUsername.setText(userProfileManager.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        setData();
        getImage();
        super.onStart();
    }

    private void getImage() {
        StorageReference islandRef = storageRef.child(userProfileManager.getUserId()+"profile.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                // Data for "images/island.jpg" is returns, use this as needed
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private void setData() {
        if(isNetworkAvailable()){
            setDataOnLocalFromCloud();
        } else {
            setDataFromLocal();
        }
    }

    @Override
    protected void onResume() {
        delay();
        super.onResume();

    }

    private void delay() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try{
                    progressDialog.dismiss();
                }catch (Exception e){
                    progressDialog.dismiss();
                }
            }
        }, 2500);
    }

    private void setDataOnLocalFromCloud() {
        final String url = Constants.kBaseUrl+Constants.kUserList;
        APIManager.getInstance().callApiListener(url, this, new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        try {
                            JSONObject jsonObject = new JSONObject(extractUsersData(json));
                            userProfileManager.setUserListData(jsonObject);
                            mUserMob.setText(userProfileManager.getMobile());
                            editor.putString(RegisterConstants.usersListData, jsonObject.toString());
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case API_FAIL:
                        Toast.makeText(UserActivity.this, "Got server error", Toast.LENGTH_SHORT).show();
                        break;
                    case API_NETWORK_FAIL:
                        Toast.makeText(UserActivity.this, "Got network error", Toast.LENGTH_SHORT).show();
                        break;
                    default : {
                    }
                }
            }
        });

        final String userDataUrl = Constants.kBaseUrl+Constants.kUsersData;
        APIManager.getInstance().callApiListener(userDataUrl, this, new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        try {
                            JSONObject jsonObject = new JSONObject(extractUsersData(json));
                            userProfileManager.setUserData(jsonObject);
                            mUsername.setText(userProfileManager.getName());
                            editor.putString(RegisterConstants.userData, jsonObject.toString());
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case API_FAIL:
                        showDialogError(RegisterConstants.serverErrorTitle, RegisterConstants.serverErrorText);
                        break;
                    case API_NETWORK_FAIL:
                        showDialogError(RegisterConstants.networkErrorTitle, RegisterConstants.networkErrorText);
                        break;
                    default : {
                    }
                }
            }
        });
    }

    private String extractUsersData(JSONObject json) {
        String jsonString = null;
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        Iterator iterator = json.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                if(json.getJSONObject(key).get(Constants.kUserId).toString().equals(user.getUid())){
                    jsonString = json.getJSONObject(key).toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonString;
    }

    private void showDialogError(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setTitle(title)
                .setIcon(R.drawable.error)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(UserActivity.this, "Something went worng", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
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
            case R.id.guidance:
                add(GuidanceFragment.newInstance());
                break;
            case R.id.about_us:
                add(AboutUs.newInstance());
                break;
            case R.id.refresh:
                Intent self = new Intent(this, UserActivity.class);
                self.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                self.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(self);
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

