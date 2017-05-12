package com.majavrella.bloodfactory.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
    private static Bitmap bitmapImgOnCloud = null;

    private JSONObject userObj = null;
    private static String selfRefKey = null;
    private DatabaseReference mUserDatabase;
    private SharedPreferences.Editor editor;
    private StorageReference storageRef;

    @Bind(R.id.update_whole_app) ImageView updateApp;
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

        editor = sharPrefs.edit();
        // Create a storage reference from our app
        storageRef = FirebaseStorage.getInstance().getReference();

        mUserState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(getActivity());
                if(position>0){
                    setCities(mUserCity, parent.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideKeyboard(getActivity());
            }
        });

        updateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage("Current changes will be reflected in whole app and application will be restarted. click ok to start processing...")
                        .setTitle("Update whole app")
                        .setIcon(R.drawable.right_icon)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent self = new Intent(getActivity(), UserActivity.class);
                                self.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                self.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(self);
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
        });
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

    @Override
    public void onStart() {
        progressDialog.setMessage(RegisterConstants.waitProgress);
        progressDialog.show();
        getImage();
        setData();
        super.onStart();
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        super.onResume();
        progressDialog.dismiss();
    }

    private void getImage() {
        if(userProfileManager.getImageBitmap()!=null){
            mDummyPicContainer.setVisibility(View.GONE);
            mProfilePic.setImageBitmap(userProfileManager.getImageBitmap());
            mProfilePic.setVisibility(View.VISIBLE);
        } else {
            StorageReference islandRef = storageRef.child(userProfileManager.getUserId()+"profile.jpg");
            final long ONE_MEGABYTE = 1024 * 1024;
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    bitmapImgOnCloud = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    mDummyPicContainer.setVisibility(View.GONE);
                    mProfilePic.setImageBitmap(bitmapImgOnCloud);
                    mProfilePic.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    mDummyPicContainer.setVisibility(View.VISIBLE);
                    mProfilePic.setVisibility(View.GONE);
                }
            });
        }
    }

    private void setData() {
        try {
            mUsername.setText(userProfileManager.getName().equals(RegisterConstants.defaultVarType)? "Name(null)" :userProfileManager.getName());
            mUserGender.setText(userProfileManager.getGender().equals(RegisterConstants.defaultVarType)? "Gender(null)" :userProfileManager.getGender());
            mUserEmail.setText(userProfileManager.getEmailId().equals(RegisterConstants.defaultVarType)? "Email(null)" :userProfileManager.getEmailId());
            mUserBlood.setText(userProfileManager.getBloodGroup().equals(RegisterConstants.defaultVarType)? "Blood group(null)" :userProfileManager.getBloodGroup());
            mUserDob.setText(userProfileManager.getDob().equals(RegisterConstants.defaultVarType)? "DOB(null)" :userProfileManager.getDob());
            mUserAddress.setText(userProfileManager.getAddress().equals(RegisterConstants.defaultVarType)? "No address" :userProfileManager.getAddress());
            mUserLocation.setText(userProfileManager.getCity().equals(RegisterConstants.defaultVarType)? "City(Empty), State(Empty)" :userProfileManager.getCity()+", "+userProfileManager.getState());
            selfRefKey = userProfileManager.getUsersSelfRefKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String getTitle() {
        return Constants.kEditProfileFragment;
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
                        mUserLocation.setText(city+", "+state);
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
                        mUserAddress.setText(address);
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
                        mUserDob.setText(dob);
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
                        mUserBlood.setText(bloodGroup);
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
                        mUserEmail.setText(email);
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
                        mUserGender.setText(gender);
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
                        mUsername.setText(name);
                        Intent self = new Intent(getActivity(), UserActivity.class);
                        self.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        self.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(self);
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
                        userProfileManager.setUserData(json);
                        editor.putString(RegisterConstants.userData, json.toString());
                        editor.apply();
                        Toast.makeText(mActivity, "Updated succeessfully", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
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
        setImageOnCloudStorage();
    }

    private void setImageOnCloudStorage() {
        // Create a reference to "profile.jpg"
        StorageReference profileRef = storageRef.child(userProfileManager.getUserId()+"profile.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapImg.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = profileRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d(TAG, "onSuccess: "+downloadUrl);
                Toast.makeText(mActivity, "success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private View.OnClickListener mAddImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(getActivity());
            selectImage();
        }
    };

    private void playWithVisibilty(View v) {
        hideKeyboard(mActivity);
        if(v.getVisibility()==View.VISIBLE){
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }
}
