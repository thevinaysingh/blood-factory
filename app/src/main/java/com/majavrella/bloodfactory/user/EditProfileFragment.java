package com.majavrella.bloodfactory.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.base.Utility;
import com.majavrella.bloodfactory.register.RegisterConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileFragment extends UserFragment {

    private static View mEditProfileView;
    private static String name, mob, gender, email, bloodGroup, dob, state, city;

    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static Bitmap bitmapImg = null;

    @Bind(R.id.add_image) LinearLayout mAddImage;
    @Bind(R.id.dummy_pic_container) LinearLayout mDummyPicContainer;
    @Bind(R.id.profile_pic) ImageView mProfilePic;

    @Bind(R.id.user_name) TextView mUsername;
    @Bind(R.id.user_name_edit) ImageView mUsernameEdit;
    @Bind(R.id.user_name_edit_text) EditText mUsernameEditText;

    @Bind(R.id.user_mob) TextView mUserMob;
    @Bind(R.id.user_mob_edit) ImageView mUserMobEdit;
    @Bind(R.id.user_mob_edit_text) EditText mUserMobEditText;

    @Bind(R.id.user_gender) TextView mUserGender;
    @Bind(R.id.user_gender_edit) ImageView mUserGenderEdit;
    @Bind(R.id.user_gender_edit_box) LinearLayout mUserGenderEditBox;
    @Bind(R.id.genderStatus) RadioGroup mGender;

    @Bind(R.id.user_email) TextView mUserEmail;
    @Bind(R.id.user_email_edit) ImageView mUserEmailEdit;
    @Bind(R.id.user_email_edit_text) EditText mUserEmailEditText;

    @Bind(R.id.user_blood) TextView mUserBlood;
    @Bind(R.id.user_blood_edit) ImageView mUserBloodEdit;
    @Bind(R.id.user_blood_edit_box) Spinner mUserBloodEditBox;

    @Bind(R.id.user_dob) TextView mUserDob;
    @Bind(R.id.user_dob_edit) ImageView mUserDobEdit;
    @Bind(R.id.user_dob_edit_text) EditText mUserDobEditText;

    @Bind(R.id.user_address) TextView mUserAddress;
    @Bind(R.id.user_address_edit) ImageView mUserAddressEdit;
    @Bind(R.id.user_address_edit_box) LinearLayout mUserAddressEditBox;
    @Bind(R.id.user_city) Spinner mUserCity;
    @Bind(R.id.user_state) Spinner mUserState;

    @Bind(R.id.update_profile) Button mUpdateProfile;

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

        mAddImage.setOnClickListener(mAddImageListener);
        mUsernameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playWithVisibilty(mUsernameEditText);
            }
        });
        mUserMobEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playWithVisibilty(mUserMobEditText);
            }
        });
        mUserGenderEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playWithVisibilty(mUserGenderEditBox);
            }
        });
        mUserEmailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playWithVisibilty(mUserEmailEditText);
            }
        });
        mUserBloodEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playWithVisibilty(mUserBloodEditBox);
            }
        });
        mUserDobEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playWithVisibilty(mUserDobEditText);
            }
        });
        mUserAddressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { playWithVisibilty(mUserAddressEditBox);
            }
        });
        mUpdateProfile.setOnClickListener(mUpdateProfileButtonListener);
        setStatusBarColor(Constants.colorStatusBar);
        return mEditProfileView;
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
        mProfilePic.setImageBitmap(bitmapImg);
        mProfilePic.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener mAddImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectImage();
        }
    };

    View.OnClickListener mUpdateProfileButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isNetworkAvailable()){
                resetData();
                setData();
            } else {
                showSnackbar(mEditProfileView, RegisterConstants.networkErrorText);
            }
        }
    };

    private void setData() {

    }

    private void resetData() {
        name = mob = gender = email = dob = bloodGroup = state = city = null;
    }

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
