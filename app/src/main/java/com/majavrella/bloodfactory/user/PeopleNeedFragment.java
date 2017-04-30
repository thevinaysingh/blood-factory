package com.majavrella.bloodfactory.user;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.SigninFragment;
import com.majavrella.bloodfactory.api.APIConstant;
import com.majavrella.bloodfactory.api.APIManager;
import com.majavrella.bloodfactory.api.APIResponse;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.register.RegisterConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleNeedFragment extends UserFragment {

    private static View mPeopleInNeed;
    private static JSONArray mPeopleInNeedArray;
    @Bind(R.id.list_container) LinearLayout mListContainer;

    public static PeopleNeedFragment newInstance() {
        return new PeopleNeedFragment();
    }

    public PeopleNeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mPeopleInNeed = inflater.inflate(R.layout.fragment_people_need, container, false);
        progressDialog.setMessage(RegisterConstants.waitProgress);
        progressDialog.show();
        mPeopleInNeedArray = new JSONArray();
        ButterKnife.bind(this, mPeopleInNeed);
        if(isNetworkAvailable()){
            setListOfNeedy();
        }
        return mPeopleInNeed;
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        super.onResume();
    }

    private void createListOfRequest(JSONArray mPeopleInNeedArray) {
        if(mPeopleInNeedArray.length()>0){
            for(int i = 0; i<mPeopleInNeedArray.length(); i++){
                String message = null;
                String mobileNo = null;
                String patient = null;
                String ref_key= null;
                String helpingHands = null;
                JSONObject json_data=null;
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_needy, null);
                TextView bloodGroup = (TextView)view.findViewById(R.id.blood_group);
                LinearLayout donarContainer = (LinearLayout)view.findViewById(R.id.donar_container);
                TextView lastDate = (TextView)view.findViewById(R.id.status);
                TextView name = (TextView)view.findViewById(R.id.name);
                TextView gender = (TextView)view.findViewById(R.id.gender);
                TextView city = (TextView)view.findViewById(R.id.city);
                TextView state = (TextView)view.findViewById(R.id.state);
                TextView phone = (TextView)view.findViewById(R.id.phone_no);
                TextView reply = (TextView)view.findViewById(R.id.more);
                ImageView replyOnCall = (ImageView) view.findViewById(R.id.call);
                final ImageView smsReply = (ImageView) view.findViewById(R.id.sms);
                ImageView shareToOthers = (ImageView) view.findViewById(R.id.share);
                ImageView singleTapReply = (ImageView) view.findViewById(R.id.about);
                try {
                    json_data = mPeopleInNeedArray.getJSONObject(i);
                    bloodGroup.setText(json_data.getString("bloodGroup"));
                    lastDate.setText(json_data.getString("date"));
                    name.setText(json_data.getString("name"));
                    patient = capitalizeFirstLetter(json_data.getString("name"));
                    gender.setText(json_data.getString("gender"));
                    city.setText(json_data.getString("city"));
                    state.setText(json_data.getString("state"));
                    phone.setText(json_data.getString("mobile"));
                    ref_key = json_data.getString("selfRefKey");
                    helpingHands = json_data.getString("helpingUsers")+userProfileManager.getUserId()+"/";
                    mobileNo = json_data.getString("mobile");
                    message = "Name: "+json_data.getString("name")+"\n"
                            +"Gender: "+json_data.getString("gender")+"\n"
                            +"Age Group: "+json_data.getString("ageGroup")+"\n"
                            +"blood group: "+json_data.getString("bloodGroup")+"\n"
                            +"Mobile: "+json_data.getString("mobile")+"\n"
                            +"City: "+json_data.getString("city")+"\n"
                            +"State: "+json_data.getString("state")+"\n"
                            +"Last date: "+json_data.getString("date")+"\n"
                            +"Purpose: "+json_data.getString("purpose");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final LinearLayout replyContainer = (LinearLayout) view.findViewById(R.id.more_container);
                mListContainer.addView(view, i);
                reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(replyContainer.getVisibility()==View.VISIBLE){
                            replyContainer.setVisibility(View.GONE);
                        } else {
                            replyContainer.setVisibility(View.VISIBLE);
                        }
                    }
                });


                final String finalMobileNo = "+91"+mobileNo;
                replyOnCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setMessage(R.string.call_msg)
                                .setTitle("Reply on CALL")
                                .setIcon(R.drawable.phone)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        callReply(finalMobileNo);
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
                smsReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setMessage(R.string.sms_msg)
                                .setTitle("SMS reply")
                                .setIcon(R.drawable.sms)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        replyOnSms(finalMessage1);
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

                shareToOthers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setMessage(R.string.share_msg)
                                .setTitle("Suggest to others")
                                .setIcon(R.drawable.share)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        shareOthers(finalMessage1);
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
                final String finalRef_key = ref_key;
                final String finalHelpingHands = helpingHands;
                singleTapReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setMessage(R.string.one_tap_msg)
                                .setTitle("Reply through app")
                                .setIcon(R.drawable.one_tap)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progress.setMessage(RegisterConstants.waitProgress);
                                        progress.show();
                                        try {
                                            DatabaseReference mDonarsDatabase = getRootReference().child(RegisterConstants.patients_db);
                                            mDonarsDatabase.child(finalRef_key).child("helpingUsers").setValue(finalHelpingHands);
                                            Toast.makeText(mActivity, "Thanks for help", Toast.LENGTH_SHORT).show();
                                            progress.dismiss();
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            progress.dismiss();
                                        }
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

                donarContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogForBloodGroup(" Requested person info", finalMessage);
                    }
                });
            }
        } else {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_error, null);
            mListContainer.addView(view);
        }
        progressDialog.dismiss();
    }

    private void sendInstantReply(String finalRef_key) {

    }

    private void shareOthers(String finalMessage1) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, finalMessage1);
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    private void replyOnSms(String finalMessage1) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("sms_body", finalMessage1);
        startActivity(sendIntent);
    }

    private void callReply(String finalMobileNo) {
        Intent callIntent = new Intent(Intent.ACTION_VIEW);
        callIntent.setData(Uri.parse("tel:"+finalMobileNo));
        startActivity(callIntent);
    }


    private void setListOfNeedy() {
        final String url = Constants.kBaseUrl+Constants.kPatients;
        APIManager.getInstance().callApiListener(url, getActivity(), new APIResponse() {
            @Override
            public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                switch (code) {
                    case API_SUCCESS:
                        setDataInJson(json);
                        break;
                    case API_FAIL:
                        showDialogError(RegisterConstants.serverErrorTitle, RegisterConstants.serverErrorText);
                        break;
                    case API_NETWORK_FAIL:
                        showDialogError(RegisterConstants.networkErrorTitle, RegisterConstants.networkErrorText);
                        break;
                    default : {
                        break;
                    }
                }
            }
        });
    }

    private void setDataInJson(JSONObject json) {
        Iterator iterator = json.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                mPeopleInNeedArray.put(json.getJSONObject(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        createListOfRequest(mPeopleInNeedArray);
    }

    @Override
    protected String getTitle() {
        return "People in Need";
    }
}
