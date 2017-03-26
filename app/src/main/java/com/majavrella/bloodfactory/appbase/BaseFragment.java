package com.majavrella.bloodfactory.appbase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.register.RegisterConstants;

public abstract class BaseFragment extends Fragment {
    public MainActivity mActivity;
    private AddFragmentHandler fragmentHandler;
    protected ProgressDialog progress;
	protected FirebaseAuth mFirebaseAuth;
	public CoordinatorLayout coordinatorLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentHandler = new AddFragmentHandler(getActivity().getSupportFragmentManager());
		mActivity		=	(MainActivity) this.getActivity();
        mFirebaseAuth = FirebaseAuth.getInstance();
        progress=new ProgressDialog(mActivity);
		coordinatorLayout = new CoordinatorLayout(mActivity);
    }

	@Override
	public void onResume() {
		super.onResume();
		getActivity().setTitle(getTitle());
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

	protected void add(BaseFragment fragment) {
		fragmentHandler.add(fragment);
	}

	public boolean isNetworkAvailable() {
		final ConnectivityManager connectivityManager = ((ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE));
		return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
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

	public void setStatusBarColor(String color){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
	}

	public void showSnackbar(String text) {
		final Snackbar snackbar = Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG)
				.setAction("OK", new View.OnClickListener() {
					@Override
					public void onClick(View view) {  }
				});
		snackbar.show();
	}
}
