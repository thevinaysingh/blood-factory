package com.majavrella.bloodfactory.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.api.APIConstant;
import com.majavrella.bloodfactory.api.APIManager;
import com.majavrella.bloodfactory.api.APIResponse;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.base.UserProfileManager;
import com.majavrella.bloodfactory.base.Utility;
import com.majavrella.bloodfactory.register.RegisterConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileFragment extends UserFragment {

    private static View mEditProfileView;
    private static String name, gender, email, bloodGroup, dob, state, city;
    String TAG = "-------";
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static Bitmap bitmapImg = null;
    private JSONObject userObj = null;
    protected SharedPreferences mSharedpreferences;
    private static String selfRefKey = null;
    private DatabaseReference mUserDatabase;
    private SharedPreferences.Editor editor;

    @Bind(R.id.add_image) LinearLayout mAddImage;
    @Bind(R.id.dummy_pic_container) LinearLayout mDummyPicContainer;
    @Bind(R.id.profile_pic) ImageView mProfilePic;

    @Bind(R.id.user_name) TextView mUsername;
    @Bind(R.id.user_name_edit) ImageView mUsernameEdit;
    @Bind(R.id.user_name_edit_box) LinearLayout mUsernameEditBox;
    @Bind(R.id.user_name_edit_text) EditText mUsernameEditText;
    @Bind(R.id.change_name) Button mChangeName;

    @Bind(R.id.user_gender) TextView mUserGender;
    @Bind(R.id.user_gender_edit) ImageView mUserGenderEdit;
    @Bind(R.id.user_gender_edit_box) LinearLayout mUserGenderEditBox;
    @Bind(R.id.genderStatus) RadioGroup mGender;
    @Bind(R.id.change_gender) Button mChangeGender;

    @Bind(R.id.user_email) TextView mUserEmail;
    @Bind(R.id.user_email_edit) ImageView mUserEmailEdit;
    @Bind(R.id.email_edit_box) LinearLayout mEmailEditBox;
    @Bind(R.id.user_email_edit_text) EditText mUserEmailEditText;
    @Bind(R.id.change_email) Button mChangeEmail;

    @Bind(R.id.user_blood) TextView mUserBlood;
    @Bind(R.id.user_blood_edit) ImageView mUserBloodEdit;
    @Bind(R.id.user_blood_edit_box) LinearLayout mUserBloodEditBox;
    @Bind(R.id.user_blood_edit_spinner) Spinner mUserBloodEditSpinner;
    @Bind(R.id.change_blood_group) Button mChangeBloodGroup;

    @Bind(R.id.user_dob) TextView mUserDob;
    @Bind(R.id.user_dob_edit) ImageView mUserDobEdit;
    @Bind(R.id.user_dob_edit_box) LinearLayout mUserDobEditBox;
    @Bind(R.id.user_dob_edit_text) EditText mUserDobEditText;
    @Bind(R.id.change_dob) Button mChangeDob;

    @Bind(R.id.user_address) TextView mUserAddress;
    @Bind(R.id.user_address_edit) ImageView mUserAddressEdit;
    @Bind(R.id.user_address_edit_box) LinearLayout mUserAddressEditBox;
    @Bind(R.id.user_address_edit_text) EditText mUserAddressEditText;
    @Bind(R.id.change_address) Button mChangeAddress;

    @Bind(R.id.user_location) TextView mUserLocation;
    @Bind(R.id.user_location_edit) ImageView mUserLocationEdit;
    @Bind(R.id.user_location_edit_box) LinearLayout mUserLocationEditBox;
    @Bind(R.id.user_city) Spinner mUserCity;
    @Bind(R.id.user_state) Spinner mUserState;
    @Bind(R.id.change_location) Button mChangeLocation;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mEditProfileView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, mEditProfileView);

        progressDialog.setMessage(RegisterConstants.waitProgress);
        progressDialog.show();
        mSharedpreferences = getActivity().getSharedPreferences(RegisterConstants.userPrefs, Context.MODE_PRIVATE);
        editor = mSharedpreferences.edit();
        mAddImage.setOnClickListener(mAddImageListener);

        mUsernameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playWithVisibilty(mUsernameEditBox);
            }
        });
        mChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                if(isNetworkAvailable()&&isNameValid(getStringDataFromEditText(mUsernameEditText))){
                    changeUsername(capitalizeFirstLetter(getStringDataFromEditText(mUsernameEditText)));
                } else {
                    showSnackbar(mEditProfileView, RegisterConstants.editBoxError);
                }
            }
        });

        mUserGenderEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playWithVisibilty(mUserGenderEditBox);
            }
        });
        mChangeGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                if(isNetworkAvailable()&&mGender.getCheckedRadioButtonId()>=0){
                    setGender(getStringDataFromRadioButton((RadioButton) mEditProfileView.findViewById(mGender.getCheckedRadioButtonId())));
                } else {
                    showSnackbar(mEditProfileView, RegisterConstants.editBoxError);
                }
            }
        });

        mUserEmailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playWithVisibilty(mEmailEditBox);}
        });
        mChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                if(isNetworkAvailable()&&isEmailValid(getStringDataFromEditText(mUserEmailEditText))){
                    changeEmail(getStringDataFromEditText(mUserEmailEditText));
                } else {
                    showSnackbar(mEditProfileView, RegisterConstants.editBoxError);
                }
            }
        });


        mUserBloodEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playWithVisibilty(mUserBloodEditBox);
            }
        });
        mChangeBloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                if(isNetworkAvailable()&& !getStringDataFromSpinner(mUserBloodEditSpinner).equals("--Select blood group--")){
                    changeBloodGroup(getStringDataFromSpinner(mUserBloodEditSpinner));
                } else {
                    showSnackbar(mEditProfileView, RegisterConstants.editBoxError);
                }
            }
        });

        mUserDobEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playWithVisibilty(mUserDobEditBox);
            }
        });
        mChangeDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                if(isNetworkAvailable()&& isDateValid(getStringDataFromEditText(mUserDobEditText))){
                    changeDob(getStringDataFromEditText(mUserDobEditText));
                } else {
                    showSnackbar(mEditProfileView, RegisterConstants.editBoxError);
                }
            }
        });


        mUserAddressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playWithVisibilty(mUserAddressEditBox);
            }
        });
        mChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                if(isNetworkAvailable()&& !getStringDataFromEditText(mUserAddressEditText).equals("")){
                    changeAddress(getStringDataFromEditText(mUserAddressEditText));
                } else {
                    showSnackbar(mEditProfileView, RegisterConstants.editBoxError);
                }
            }
        });

        mUserLocationEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                playWithVisibilty(mUserLocationEditBox);
            }
        });
        mChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                if(isNetworkAvailable()&& !getStringDataFromSpinner(mUserCity).equals("--Select city--")
                        &&!getStringDataFromSpinner(mUserState).equals("--Select state--")){
                    changeLocation(getStringDataFromSpinner(mUserCity), getStringDataFromSpinner(mUserState) );
                } else {
                    showSnackbar(mEditProfileView, RegisterConstants.editBoxError);
                }
            }
        });

        setStatusBarColor(Constants.colorStatusBarDark);
        return mEditProfileView;
    }

    private void changeLocation(final String city, final String state) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("You are trying to change/set city and state, If yes click ok to start processing...")
                .setTitle("Change location")
                .setIcon(R.drawable.edit_icon_new)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playWithVisibilty(mUserLocationEditBox);
                        changelocDatabase("city", city, "state", state);
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

    private void changelocDatabase(String city, String city1, String state, String state1) {
        progress.setMessage(RegisterConstants.waitProgress);
        progress.show();
        try {
            mUserDatabase = getRootReference().child(RegisterConstants.user_Data_db);
            mUserDatabase.child(selfRefKey).child(city).setValue(city1);
            mUserDatabase.child(selfRefKey).child(state).setValue(state1);
            updateDataOnLocal();
        } catch(Exception e) {
            e.printStackTrace();
            showDialogError(RegisterConstants.serverErrorTitle, RegisterConstants.serverErrorText);
            progress.dismiss();
        }
    }

    private void changeAddress(final String address) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("You are trying to change/set Address, If yes click ok to start processing...")
                .setTitle("Change address")
                .setIcon(R.drawable.edit_icon_new)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playWithVisibilty(mUserAddressEditBox);
                        changeDatabase("address", address);
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

    private void changeDob(final String dob) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("You are trying to change/set DOB, If yes click ok to start processing...")
                .setTitle("Change DOB")
                .setIcon(R.drawable.edit_icon_new)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playWithVisibilty(mUserDobEditBox);
                        changeDatabase("dob", dob);
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

    private void changeBloodGroup(final String bloodGroup) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("You are trying to change/set blood group, If yes click ok to start processing...")
                .setTitle("Change blood group")
                .setIcon(R.drawable.edit_icon_new)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playWithVisibilty(mUserBloodEditBox);
                        changeDatabase("bloodGroup", bloodGroup);
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

    private void changeEmail(final String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("You are trying to change/set email, If yes click ok to start processing...")
                .setTitle("Change email")
                .setIcon(R.drawable.edit_icon_new)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playWithVisibilty(mEmailEditBox);
                        changeDatabase("emailId", email);
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

    private void setGender(final String gender) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("You are trying to change/set identity, If yes click ok to start processing...")
                .setTitle("Change Identity")
                .setIcon(R.drawable.edit_icon_new)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playWithVisibilty(mUserGenderEditBox);
                        changeDatabase("gender", gender);
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

    private void changeUsername(final String name) {
       AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("You are trying to change/set name field, If yes click ok to start processing...")
                .setTitle("Change name")
                .setIcon(R.drawable.edit_icon_new)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playWithVisibilty(mUsernameEditBox);
                        changeDatabase("name", name);
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

    private void changeDatabase(String key, String value) {
        progress.setMessage(RegisterConstants.waitProgress);
        progress.show();
        try {
            mUserDatabase = getRootReference().child(RegisterConstants.user_Data_db);
            mUserDatabase.child(selfRefKey).child(key).setValue(value);
            updateDataOnLocal();
        } catch(Exception e) {
            e.printStackTrace();
            showDialogError(RegisterConstants.serverErrorTitle, RegisterConstants.serverErrorText);
            progress.dismiss();
        }
    }

    private void updateDataOnLocal() {
        final String url = Constants.kBaseUrl+Constants.kUsers+selfRefKey+Constants.jsonTail;
        APIManager.getInstance().callApiListener(url, getContext(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        setUsersData(json);
                        break;
                    case API_FAIL:
                        showDialogError(RegisterConstants.serverErrorTitle, RegisterConstants.serverErrorText);
                        progress.dismiss();
                        break;
                    case API_NETWORK_FAIL:
                        showDialogError(RegisterConstants.networkErrorTitle, RegisterConstants.networkErrorText);
                        progress.dismiss();
                        break;
                    default : {
                    }
                }
            }
        });
    }

    private void setUsersData(JSONObject json) {
        Log.d(TAG, "setUsersData: "+json);
        try {
            mUsername.setText(json.getString("name").equals(RegisterConstants.defaultVarType)? "name not set" :json.getString("name"));
            mUserGender.setText(json.getString("gender").equals(RegisterConstants.defaultVarType)? "Gender not set" :json.getString("gender"));
            mUserEmail.setText(json.getString("emailId").equals(RegisterConstants.defaultVarType)? "Email not set" :json.getString("emailId"));
            mUserBlood.setText(json.getString("bloodGroup").equals(RegisterConstants.defaultVarType)? "Gender not set" :json.getString("bloodGroup"));
            mUserDob.setText(json.getString("dob").equals(RegisterConstants.defaultVarType)? "DOB not set" :json.getString("dob"));
            mUserAddress.setText(json.getString("address").equals(RegisterConstants.defaultVarType)? "No address" :json.getString("address"));
            mUserLocation.setText(json.getString("city").equals(RegisterConstants.defaultVarType)? "City(Empty), State(Empty)" :json.getString("city")+", "+json.getString("state"));
            editor.putString(RegisterConstants.userData, json.toString());
            editor.commit();
            progress.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
            progress.dismiss();
        }
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        setDataFromDataBase();
        super.onResume();
    }

    private void setDataFromDataBase() {
        final String userData = mSharedpreferences.getString(RegisterConstants.userData, "DEFAULT_VALUE");
        try {
            userObj = new JSONObject(userData);
            mUsername.setText(userObj.getString("name").equals(RegisterConstants.defaultVarType)? "name not set" :userObj.getString("name"));
            mUserGender.setText(userObj.getString("gender").equals(RegisterConstants.defaultVarType)? "Gender not set" :userObj.getString("gender"));
            mUserEmail.setText(userObj.getString("emailId").equals(RegisterConstants.defaultVarType)? "Email not set" :userObj.getString("emailId"));
            mUserBlood.setText(userObj.getString("bloodGroup").equals(RegisterConstants.defaultVarType)? "Gender not set" :userObj.getString("bloodGroup"));
            mUserDob.setText(userObj.getString("dob").equals(RegisterConstants.defaultVarType)? "DOB not set" :userObj.getString("dob"));
            mUserAddress.setText(userObj.getString("address").equals(RegisterConstants.defaultVarType)? "No address" :userObj.getString("address"));
            mUserLocation.setText(userObj.getString("city").equals(RegisterConstants.defaultVarType)? "City(Empty), State(Empty)" :userObj.getString("city")+", "+userObj.getString("state"));
            selfRefKey = userObj.getString("selfRefKey");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Add Profile Image ");
        builder.setIcon(R.drawable.user_img);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(mActivity);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        bitmapImg=null;
        bitmapImg = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmapImg.compress(Bitmap.CompressFormat.PNG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".png");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDummyPicContainer.setVisibility(View.GONE);
        mProfilePic.setImageBitmap(bitmapImg);
        mProfilePic.setVisibility(View.VISIBLE);
    }

    private void onSelectFromGalleryResult(Intent data) {
        bitmapImg=null;
        if (data != null) {
            try {
                bitmapImg = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mDummyPicContainer.setVisibility(View.GONE);
        Log.d("Edit profile", "onSelectFromGalleryResult: "+ bitmapImg);
        mProfilePic.setImageBitmap(bitmapImg);
        mProfilePic.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener mAddImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(getActivity());
            selectImage();
        }
    };

    @Override
    protected String getTitle() {
        return Constants.kEditProfileFragment;
    }

    private void playWithVisibilty(View v) {
        hideKeyboard(mActivity);
        if(v.getVisibility()==View.VISIBLE){
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }
}
