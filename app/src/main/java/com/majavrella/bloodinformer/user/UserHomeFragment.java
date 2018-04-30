package com.majavrella.bloodinformer.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majavrella.bloodinformer.R;
import com.majavrella.bloodinformer.api.APIConstant;
import com.majavrella.bloodinformer.api.APIManager;
import com.majavrella.bloodinformer.api.APIResponse;
import com.majavrella.bloodinformer.base.BackButtonSupportFragment;
import com.majavrella.bloodinformer.base.Constants;
import com.majavrella.bloodinformer.base.UserFragment;
import com.majavrella.bloodinformer.modal.Patient;
import com.majavrella.bloodinformer.register.RegisterConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserHomeFragment extends UserFragment implements BackButtonSupportFragment {

    protected static View userHomeFragment;
    protected boolean consumingBackPress = true;
    protected final String TAG = "UserHomeFragment";
    private Toast toast;
    private JSONArray jsonArray= null;
    protected SharedPreferences mSharedpreferences;
    @Bind(R.id.donate_blood) LinearLayout mDonateButton;
    @Bind(R.id.find_blood) LinearLayout mFindButton;

    @Bind(R.id.request_info_container) LinearLayout mRequestInfoContainer;
    @Bind(R.id.request_container) LinearLayout mRequestContainer;
    @Bind(R.id.front_page) LinearLayout mfront_page;
    @Bind(R.id.user_info_container) LinearLayout mUser_info_container;
    @Bind(R.id.user_info) LinearLayout mUser_info;
    @Bind(R.id.back) TextView mback;

    public static UserHomeFragment newInstance() {
        return new UserHomeFragment();
    }

    public UserHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userHomeFragment = inflater.inflate(R.layout.fragment_user_home, container, false);
        ButterKnife.bind(this, userHomeFragment);
        mSharedpreferences = getActivity().getSharedPreferences(RegisterConstants.userPrefs, Context.MODE_PRIVATE);
        setStatusBarColor(Constants.colorStatusBar);
        mDonateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    add(DonateFragment.newInstance());
                } else {
                    Toast.makeText(mActivity, RegisterConstants.networkErrorTitle, Toast.LENGTH_SHORT).show();
                }

            }
        });
        mFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    add(RecieveFragment.newInstance());
                } else {
                    Toast.makeText(mActivity, RegisterConstants.networkErrorTitle, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestContainer.setVisibility(View.VISIBLE);
                mUser_info_container.setVisibility(View.GONE);
            }
        });

        return userHomeFragment;
    }

    @Override
    public void onStart() {
        fetchRequests();
        super.onStart();
    }

    private void fetchRequests() {
        if(isNetworkAvailable()){
            final String url = Constants.kBaseUrl+Constants.kPatients;
            APIManager.getInstance().callApiListener(url, getActivity(), new APIResponse() {
                @Override
                public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                    switch (code) {
                        case API_SUCCESS:
                            jsonArray = searchResultInJson(json);
                            try {
                                createListOfRequests(jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case API_FAIL:
                            showNetworkError(userHomeFragment, RegisterConstants.networkErrorText);
                            break;
                        case API_NETWORK_FAIL:
                            showNetworkError(userHomeFragment, RegisterConstants.networkErrorText);
                            break;
                    }
                }
            });
        } else {
            showNetworkError(userHomeFragment, RegisterConstants.networkErrorText);
        }
    }

    private void createListOfRequests(final JSONArray mRequestsArray) throws JSONException {
        mRequestContainer.removeAllViews();
        if(mRequestsArray.length()>0){
            mRequestInfoContainer.setVisibility(View.VISIBLE);
            mfront_page.setVisibility(View.GONE);
            for(int i = 0; i<mRequestsArray.length(); i++){
                int counter = 0;
                Patient patient = null;
                JSONObject json_data = null;
                String[] helpingUsersArray = null;
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.request_response, null);
                ImageView memberImg = (ImageView) view.findViewById(R.id.member_image);
                TextView name = (TextView)view.findViewById(R.id.name);
                TextView bloodGroup = (TextView)view.findViewById(R.id.blood_group);
                TextView city = (TextView)view.findViewById(R.id.city);
                TextView helpingHandsCounter = (TextView)view.findViewById(R.id.helpingHandsCounter);
                LinearLayout requestContainer = (LinearLayout)view.findViewById(R.id.req_container);
                TextView state = (TextView)view.findViewById(R.id.state);
                try {
                    json_data = mRequestsArray.getJSONObject(i);
                    patient = setPatient(json_data, new Patient());
                    name.setText(patient.getName());
                    bloodGroup.setText(patient.getBloodGroup());
                    city.setText(patient.getCity());
                    state.setText(patient.getState());
                    if(patient.getGender().equals("Male")){
                        memberImg.setImageResource(R.drawable.male);
                    } else {
                        memberImg.setImageResource(R.drawable.female);
                    }
                    if(patient.getHelpingUsers().contains("/")){
                        helpingUsersArray = patient.getHelpingUsers().split("/");
                        for (String s : helpingUsersArray) {
                            counter++;
                        }
                        helpingHandsCounter.setText(String.valueOf(counter));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String[] finalHelpingUsersArray = helpingUsersArray;
                requestContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRequestContainer.setVisibility(View.GONE);
                        mUser_info_container.setVisibility(View.VISIBLE);
                        final String url = Constants.kBaseUrl+Constants.kUserList;
                        APIManager.getInstance().callApiListener(url, getActivity(), new APIResponse() {
                            @Override
                            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                                switch (code) {
                                    case API_SUCCESS:
                                        setDataFromApi(json, finalHelpingUsersArray);
                                        break;
                                    case API_FAIL:
                                        showNetworkError(userHomeFragment, RegisterConstants.networkErrorText);
                                        break;
                                    case API_NETWORK_FAIL:
                                        showNetworkError(userHomeFragment, RegisterConstants.networkErrorText);
                                        break;
                                }
                            }
                        });

                    }
                });
                mRequestContainer.addView(view, i);
            }
        } else {
            mRequestInfoContainer.setVisibility(View.GONE);
            mfront_page.setVisibility(View.VISIBLE);
        }
    }

    private void setDataFromApi(final JSONObject json, final String[] finalHelpingUsersArray) {
        final String[] mobilesArray = new String[finalHelpingUsersArray.length];
        for(int i=0; i<finalHelpingUsersArray.length; i++){
            mobilesArray[i] = getUserMobileNo(json, finalHelpingUsersArray[i]);
        }
        final String url = Constants.kBaseUrl+Constants.kUsersData;
        APIManager.getInstance().callApiListener(url, getActivity(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        createHelpingHandsList(json, finalHelpingUsersArray, mobilesArray);
                        break;
                    case API_FAIL:
                        showNetworkError(userHomeFragment, RegisterConstants.networkErrorText);
                        break;
                    case API_NETWORK_FAIL:
                        showNetworkError(userHomeFragment, RegisterConstants.networkErrorText);
                        break;
                }
            }
        });

    }

    private void createHelpingHandsList(JSONObject json, String[] finalHelpingUsersArray, String[] mobilesArray) {
        mUser_info.removeAllViews();
        if(finalHelpingUsersArray.length>0){
            for(int i=0; i<finalHelpingUsersArray.length; i++){
                final String user_id = finalHelpingUsersArray[i];
                JSONObject userJson = null;
                String message = null;
                String mobileNo = null;
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.helper_item, null);
                TextView bloodGroup = (TextView)view.findViewById(R.id.blood_group);
                TextView phone = (TextView)view.findViewById(R.id.phone);
                TextView statusText = (TextView)view.findViewById(R.id.status_text);
                TextView name = (TextView)view.findViewById(R.id.name);
                TextView more = (TextView)view.findViewById(R.id.more);
                ImageView memberImg = (ImageView) view.findViewById(R.id.member_image);
                ImageView statusImg = (ImageView) view.findViewById(R.id.donar_availability_status);
                ImageView call = (ImageView) view.findViewById(R.id.call);
                ImageView sms = (ImageView) view.findViewById(R.id.sms);
                ImageView share = (ImageView) view.findViewById(R.id.share);
                ImageView about = (ImageView) view.findViewById(R.id.about);
                final LinearLayout moreContainer = (LinearLayout)view.findViewById(R.id.more_container);
                try {
                    userJson = new JSONObject(getUser(json, user_id));
                    phone.setText(mobilesArray[i]);
                    if(!userJson.getString("name").equals(RegisterConstants.defaultVarType)){
                        name.setText(userJson.getString("name"));
                    }

                    if(!userJson.getString("gender").equals(RegisterConstants.defaultVarType)){
                        if(userJson.getString("gender").equals("Male")){
                            memberImg.setImageResource(R.drawable.male);
                        }  else {
                            memberImg.setImageResource(R.drawable.female);
                        }
                    }
                    if(!userJson.getString("bloodGroup").equals(RegisterConstants.defaultVarType)){
                        bloodGroup.setText(userJson.getString("bloodGroup"));
                    }
                    statusText.setText("Available");
                    statusImg.setImageResource(R.drawable.right);

                    mobileNo = mobilesArray[i];
                    message = "Name: "+userJson.getString("name")+"\n"
                            +"Gender: "+userJson.getString("gender")+"\n"
                            +"Age Group: "+userJson.getString("ageGroup")+"\n"
                            +"blood group: "+userJson.getString("bloodGroup")+"\n"
                            +"Address: "+userJson.getString("address")+"\n"
                            +"City: "+userJson.getString("city")+"\n"
                            +"State: "+userJson.getString("state")+"\n"
                            +"Status: "+"Available";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "createHelpingHandsList: "+ message);

                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(moreContainer.getVisibility()==View.VISIBLE){
                            moreContainer.setVisibility(View.GONE);
                        } else {
                            moreContainer.setVisibility(View.VISIBLE);
                        }
                    }
                });
                final String finalMobileNo = "+91"+mobileNo;
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setMessage("If you want to call directly,\n Click ok and start processing...")
                                .setTitle("Contact by phone")
                                .setIcon(R.drawable.phone)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent callIntent = new Intent(Intent.ACTION_VIEW);
                                        callIntent.setData(Uri.parse("tel:"+finalMobileNo));
                                        startActivity(callIntent);
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });


                final String finalMessage1 = message;
                sms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setMessage("If you want to sms this information to others,\n Click ok and start processing...")
                                .setTitle("SMS")
                                .setIcon(R.drawable.sms)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                        sendIntent.setData(Uri.parse("sms:"));
                                        sendIntent.putExtra("sms_body", finalMessage1);
                                        startActivity(sendIntent);                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setMessage("If you want to share this information to others,\nClick ok and start processing...")
                                .setTitle("Suggest to others")
                                .setIcon(R.drawable.share)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent shareIntent = new Intent();
                                        shareIntent.setAction(Intent.ACTION_SEND);
                                        shareIntent.putExtra(Intent.EXTRA_TEXT, finalMessage1);
                                        shareIntent.setType("text/plain");
                                        startActivity(shareIntent);
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

                final String finalMessage = message;
                about.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogForBloodGroup("Donar info", finalMessage);
                    }
                });

                mUser_info.addView(view);
            }
        }

    }

    private JSONArray searchResultInJson(JSONObject json) {
        JSONArray requiredJson = new JSONArray();
        Iterator iterator = json.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                if(json.getJSONObject(key).getString("userId").equals(userProfileManager.getUserId())
                        && json.getJSONObject(key).getString("helpingUsers").contains("/")){
                    // Check date
                    Date currentDate = null;
                    Date lastDateOfNeed = null;
                    SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        currentDate = mdformat.parse(getCurrentDate());
                        lastDateOfNeed = mdformat.parse(json.getJSONObject(key).getString("date"));
                        if(lastDateOfNeed.after(currentDate)||lastDateOfNeed.equals(currentDate)){
                            requiredJson.put(json.getJSONObject(key));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return requiredJson;
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        super.onResume();
    }

    @Override
    protected String getTitle() {
        return Constants.kHomeFragment;
    }

    @Override
    public boolean onBackPressed() {
        if (consumingBackPress) {
            toast = Toast.makeText(getActivity(), "Press back again to exit", Toast.LENGTH_LONG);
            toast.show();
            consumingBackPress = false;
            return true;
        }
        toast.cancel();
        mActivity.finish();
        return true;
    }
}

