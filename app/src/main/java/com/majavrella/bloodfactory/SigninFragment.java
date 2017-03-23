package com.majavrella.bloodfactory;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.majavrella.bloodfactory.appbase.BaseFragment;
import com.majavrella.bloodfactory.appbase.MainActivity;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.register.RegisterConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SigninFragment extends BaseFragment {

    private static View mSigninFragment;
    private String mobile, password;
    @Bind(R.id.user_mob) EditText mUserMobile;
    @Bind(R.id.user_password) EditText mUserPassword;
    @Bind(R.id.show_password) CheckBox mShowPassword;
    @Bind(R.id.signin) Button mSignin;
    @Bind(R.id.textRegister) TextView mTextRegister;
    @Bind(R.id.lostPassword) TextView mLostPassword;
    @Bind(R.id.back) FrameLayout mBack;

    public static SigninFragment newInstance() {
        return new SigninFragment();
    }

    public SigninFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSigninFragment = inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this, mSigninFragment);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mActivity);
                add(FirstFragment.newInstance()); }
        });
        mSignin.setOnClickListener(mSigninListener);
        mTextRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mActivity);
                add(RegisterFragment.newInstance());}
        });

        mShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mUserPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        mLostPassword.setOnClickListener(mLostPasswordListener);

        setStatusBarColor(Constants.colorLogin);
        return mSigninFragment;
    }

    private boolean dataValidation() {
        boolean validation = true;
        if(!isPhoneValid(mobile)){
            validation = false;
            mUserMobile.setError(Constants.mobErrorText);
        }
        if(password.length()<6){
            validation = false;
            mUserPassword.setError("Enter 6 characters!");
        }
        return validation;
    }

    @Override
    public void onResume() {
        hideKeyboard(mActivity);
        super.onResume();
    }

    View.OnClickListener mLostPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Lost password cliked!!", Toast.LENGTH_SHORT).show();
            if(isNetworkAvailable()){
                getUserList(Constants.kUserList);
                new JsonTask().execute(Constants.kBaseUrl+Constants.kUserList);
            } else {
                showDialogError(RegisterConstants.networkErrorTitle,RegisterConstants.networkErrorText);
            }
        }
    };

    private class JsonTask extends AsyncTask<String, String, JSONObject> {

        protected void onPreExecute() {
            super.onPreExecute();
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        protected JSONObject doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                    //JSONObject jObject = new JSONObject(line);

                   /* if (jObject.has("name")) {

                        String temp = jObject.getString("name");
                        Log.e("name",temp);

                    }
                     */  //here u ll get whole response...... :-)
                    Log.d("Response: ", "> " + line);
                }

                /*JsonParser parser = new JsonParser();
                JsonElement tradeElement = parser.parse(buffer.toString());
                JsonArray trade = tradeElement.getAsJsonArray();
                Log.d("Response: ", "> " + trade);*/


                JSONObject jsonObject = new JSONObject(buffer.toString());
                return jsonObject;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            progress.dismiss();
            ArrayList<HashMap<String, String>> contactList;
            JSONObject jsonArray = new JSONObject();
            JSONArray jsonArr= new JSONArray();
            Iterator iterator = result.keys();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                try {
                    jsonArr.put(result.getJSONObject(key));
                    jsonArray = result.getJSONObject(key);

                    Log.d("User list ---", String.valueOf(jsonArray));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //jsonArray.put(key);
            }

            try {
                Object object = jsonArr.get(0);
                Log.d("Object -- ", String.valueOf(object));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray1 = result.names();
            Log.d("no of users", String.valueOf(result.length()));
            Log.d("no user", String.valueOf(jsonArray.length()));
            Log.d("jsonArr -- ", String.valueOf(jsonArr));


            //JSONObject jsonObject= new JSONObject(result);
            //JsonArray jsonArray = new JsonArray();

        }
    }

    private void getUserList(String kUserList) {

    }

    private void startUserActivity() {
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(mActivity, " Successfully login!!!", Toast.LENGTH_SHORT).show();
    }

    private void setDataInStringFormat() {
        mobile = getStringDataFromEditText(mUserMobile);
        password = getStringDataFromEditText(mUserPassword);
    }

    private void resetData() {
        mobile = password = null;
    }

    View.OnClickListener mSigninListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isNetworkAvailable()){
                resetData(); setDataInStringFormat(); boolean isAllFieldsValid = dataValidation();
                if (isAllFieldsValid){
                    progress.setMessage(RegisterConstants.validationProgress); progress.show();
                    final  String user_id = mobile+RegisterConstants.userIdDummyTail;
                    mFirebaseAuth.signInWithEmailAndPassword(user_id, password).addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progress.dismiss();
                                    startUserActivity();
                                } else {
                                    progress.dismiss();
                                    showDialogError(RegisterConstants.loginErrorTitle,RegisterConstants.loginErrorText);
                                }
                            }
                        });
                }
            } else {
                showDialogError(RegisterConstants.networkErrorTitle, RegisterConstants.networkErrorText);
            }

        }
    };

    @Override
    protected String getTitle() {
        return Constants.kLoginFragment;
    }
}
