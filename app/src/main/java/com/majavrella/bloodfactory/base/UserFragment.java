package com.majavrella.bloodfactory.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.majavrella.bloodfactory.user.UserActivity;

public abstract class UserFragment extends Fragment {
	public UserActivity mActivity;
    private AddFragmentHandler fragmentHandler;
	protected FirebaseAuth mFirebaseAuth;
	protected ProgressDialog progress;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        fragmentHandler = new AddFragmentHandler(getActivity().getSupportFragmentManager());
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

	public String getStringDataFromEditText(EditText v){
		return v.getText().toString().trim();
	}

    public String getStringDataFromSpinner(Spinner v){
        return v.getSelectedItem().toString();
    }

    public String getStringDataFromRadioButton(RadioButton v){
        return v.getText().toString();
    }

	public boolean isNameValid(String name){
        return name.matches(Constants.nameRegex);
    }
	public boolean isEmailValid(String email){
        return email.matches(Constants.emailRegex);
    }
	public boolean isPhoneValid(String phone){
        return phone.matches(Constants.mobRegex);
    }
	public boolean isDateValid(String date){
        return date.matches(Constants.dateRegex);
    }

	protected abstract String getTitle();

    protected void add(UserFragment fragment) {
        fragmentHandler.add(fragment);
    }
}
