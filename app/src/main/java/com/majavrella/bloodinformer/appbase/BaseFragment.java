package com.majavrella.bloodinformer.appbase;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.majavrella.bloodinformer.R;
import com.majavrella.bloodinformer.base.Constants;

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

	public void setCities(Spinner spinner, String state){
		if(state.equals("--Select state--")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.list_of_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Madhya pradesh")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.mp_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Andaman and Nicobar")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.andman_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Andra Pradesh")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.ap_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Arunachal Pradesh")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.arunanchal_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Assam")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.assam_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Bihar")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.bihar_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Chandigarh")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.chandigarh_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Chhattisgarh")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.cg_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Dadar and Nagar Haveli")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.dadar_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Daman and Diu")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.daman_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Delhi")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.delhi_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Gujarat")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.gujrat_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Goa")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.goa_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Haryana")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.haryana_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Himachal Pradesh")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.himanchal_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Jammu and Kashmir")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.jk_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Jharkhand")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.jharkhand_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Karnataka")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.karnataka_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Kerala")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.kerala_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Lakshadeep")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.lakshadeep_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Maharashtra")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.mh_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Manipur")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.manipur_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Meghalaya")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.meghalaya_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Mizoram")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.mijoram_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Nagaland")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.nagaland_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Orissa")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.odisa_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Pondicherry")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.pondicherry_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Punjab")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.punjab_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Rajasthan")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.raj_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Sikkim")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.sikkim_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Tamil Nadu")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.tp_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Tripura")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.tripura_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Uttaranchal")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.uttaranchal_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("Uttar Pradesh")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.up_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else if(state.equals("West Bengal")){
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.wb_cities, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
		} else {
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					getContext(), R.array.dummy_select, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
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

}
