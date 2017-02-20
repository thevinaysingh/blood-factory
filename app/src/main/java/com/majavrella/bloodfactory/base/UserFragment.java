package com.majavrella.bloodfactory.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.majavrella.bloodfactory.user.UserActivity;

public abstract class UserFragment extends Fragment {
	public UserActivity mActivity;

	protected FirebaseAuth mFirebaseAuth;
	protected ProgressDialog progress;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity		=	(UserActivity) this.getActivity();
        mFirebaseAuth = FirebaseAuth.getInstance();
        progress=new ProgressDialog(mActivity);
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().setTitle(getTitle());
	}

	protected abstract String getTitle();
}
