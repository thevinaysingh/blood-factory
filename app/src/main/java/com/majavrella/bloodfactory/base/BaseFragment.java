package com.majavrella.bloodfactory.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.majavrella.bloodfactory.activities.MainActivity;

public class BaseFragment extends Fragment {
	protected MainActivity mActivity;
	protected FirebaseAuth mFirebaseAuth;
	protected FragmentManager mFragmentManager;
    protected FragmentTransaction mFragmentTransaction;
    protected ProgressDialog progress;




    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity		=	(MainActivity) this.getActivity();
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFirebaseAuth = FirebaseAuth.getInstance();
        progress=new ProgressDialog(mActivity);
	}

	public boolean onBackPressed(){
		return false;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data){
		
	}
}
