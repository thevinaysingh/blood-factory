package com.majavrella.bloodfactory.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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

public class GuidanceFragment extends UserFragment {

    private static View mGuidanceView;

    public static GuidanceFragment newInstance() {
        return new GuidanceFragment();
    }

    public GuidanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mGuidanceView = inflater.inflate(R.layout.fragment_guidance, container, false);
        ButterKnife.bind(this, mGuidanceView);

        setStatusBarColor(Constants.colorStatusBarDark);
        return mGuidanceView;
    }

    @Override
    protected String getTitle() {
        return Constants.kGuidanceFragment;
    }
}
