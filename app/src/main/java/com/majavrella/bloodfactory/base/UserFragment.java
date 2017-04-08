package com.majavrella.bloodfactory.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.register.RegisterConstants;
import com.majavrella.bloodfactory.user.UserActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public abstract class UserFragment extends Fragment {
	public UserActivity mActivity;
    private AddFragmentHandler fragmentHandler;
	protected FirebaseAuth mFirebaseAuth;
    protected DatabaseReference mDatabase;
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

    public String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public DatabaseReference getRootReference(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        return mDatabase;
    }

	public String getCurrentUserId() {
		FirebaseUser user = mFirebaseAuth.getCurrentUser();
		return user.getUid().toString();
	}

	protected String extractRefKey(JSONObject json) {
		String ref_key = null;
		FirebaseUser user = mFirebaseAuth.getCurrentUser();
		Iterator iterator = json.keys();
		while (iterator.hasNext()){
			String key = (String) iterator.next();
			try {
				if(json.getJSONObject(key).get(Constants.kUserId).toString().equals(user.getUid().toString())){
					ref_key = json.getJSONObject(key).get(Constants.kRefKey).toString();
					return ref_key;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return ref_key;
	}

	public void hideKeyboard(Context ctx) {
		InputMethodManager inputManager = (InputMethodManager) ctx
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// check if no view has focus:
		View v = ((Activity) ctx).getCurrentFocus();
		if (v == null)
			return;
		inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

    public void showSnackbar(View view, String text) {
        final Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {  }
                });
        snackbar.show();
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

	public boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

	public void setStatusBarColor(String color){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = mActivity.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.parseColor(color));
		}
	}

	public void showDialogForBloodGroup(String title, String msg) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setIcon(R.drawable.blood_drop);
		builder.setMessage(msg);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		final AlertDialog dialog = builder.create();
		dialog.show(); //show() should be called before dialog.getButton().

		final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
		LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
		positiveButtonLL.gravity = Gravity.CENTER;
		positiveButton.setLayoutParams(positiveButtonLL);
	}

	public void showDialogError(String title, String msg) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setIcon(R.drawable.error);
		builder.setMessage(msg);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		final AlertDialog dialog = builder.create();
		dialog.show(); //show() should be called before dialog.getButton().

		final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
		LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
		positiveButtonLL.gravity = Gravity.CENTER;
		positiveButton.setLayoutParams(positiveButtonLL);
	}
}
