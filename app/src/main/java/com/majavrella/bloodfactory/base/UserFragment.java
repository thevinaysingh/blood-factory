package com.majavrella.bloodfactory.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

	public void hideIt(LinearLayout ll){
		ll.setVisibility(View.GONE);
	}

	public void setErrorMsg(LinearLayout linearLayout, TextView tv, String msg){
		linearLayout.setVisibility(View.VISIBLE);
		tv.setText(msg);
	}

	protected abstract String getTitle();
}
