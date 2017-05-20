package com.majavrella.bloodfactory.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.api.APIConstant;
import com.majavrella.bloodfactory.api.APIManager;
import com.majavrella.bloodfactory.api.APIResponse;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.base.Utility;
import com.majavrella.bloodfactory.modal.Donar;
import com.majavrella.bloodfactory.register.RegisterConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 4/27/2017.
 */

public class AddedMembers extends UserFragment {

    private static View mAddedMembers;
    private static JSONArray mMembersArray;
    @Bind(R.id.list_container) LinearLayout mListContainer;

    public static AddedMembers newInstance() {
        return new AddedMembers();
    }

    public AddedMembers() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAddedMembers = inflater.inflate(R.layout.fragment_members_list, container, false);
        ButterKnife.bind(this, mAddedMembers);
        fetchMembers();
        return mAddedMembers;
    }

    private void fetchMembers() {
        if(isNetworkAvailable()){
            progress.setMessage(RegisterConstants.waitProgress);
            progress.show();
            final String url = Constants.kBaseUrl+Constants.kDonars;
            APIManager.getInstance().callApiListener(url, getActivity(), new APIResponse() {
                @Override
                public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                    switch (code) {
                        case API_SUCCESS:
                            JSONArray jsonArray = searchResultInJson(json);
                            try {
                                createListOfMembers(jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case API_FAIL:
                            needInternet();
                            progress.dismiss();
                            showNetworkError(mAddedMembers, RegisterConstants.networkErrorText);
                            break;
                        case API_NETWORK_FAIL:
                            needInternet();
                            progress.dismiss();
                            showNetworkError(mAddedMembers, RegisterConstants.networkErrorText);
                            break;
                    }
                }
            });
        } else {
            needInternet();
        }
    }

    private void needInternet() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.error_layout, null);
        Button tryAgain = (Button) view.findViewById(R.id.try_again);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    fetchMembers();
                } else {
                    showNetworkError(mAddedMembers, "No network connection");
                }
            }
        });
        mListContainer.addView(view);

    }

    private JSONArray searchResultInJson(JSONObject json) {
        JSONArray requiredJson = new JSONArray();
        Iterator iterator = json.keys();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            try {
                if(json.getJSONObject(key).getString("userId").equals(userProfileManager.getUserId())
                        &&json.getJSONObject(key).getString("isUser").equals(RegisterConstants.kFalse)){
                    requiredJson.put(json.getJSONObject(key));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return requiredJson;
    }

    private void createListOfMembers(final JSONArray mMembersArray) throws JSONException {
        mListContainer.removeAllViews();
        if(mMembersArray.length()>0){
            for(int i = 0; i<mMembersArray.length(); i++){
                Donar donar = null;
                JSONObject json_data = null;
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.donar_item, null);
                TextView bloodGroup = (TextView)view.findViewById(R.id.blood_group);
                TextView statusText = (TextView)view.findViewById(R.id.status_text);
                TextView name = (TextView)view.findViewById(R.id.name);
                TextView city = (TextView)view.findViewById(R.id.city);
                TextView state = (TextView)view.findViewById(R.id.state);
                ImageView memberImg = (ImageView) view.findViewById(R.id.member_image);
                ImageView statusImg = (ImageView) view.findViewById(R.id.donar_availability_status);

                LinearLayout donarContainer = (LinearLayout)view.findViewById(R.id.donar_container);
                try {
                    json_data = mMembersArray.getJSONObject(i);
                    donar = setDonar(json_data, new Donar());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final JSONObject finalJson_data = new JSONObject(json_data.toString());
                donarContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseActivityFromOption(finalJson_data);
                    }
                });
                name.setText(donar.getName());
                city.setText(donar.getCity());
                state.setText(donar.getState());
                bloodGroup.setText(donar.getBloodGroup());
                if(donar.getGender().equals("Male")){
                    memberImg.setImageResource(R.drawable.male);
                } else {
                    memberImg.setImageResource(R.drawable.female);
                }
                 if(donar.getAvailability().equals("Available")){
                     statusImg.setImageResource(R.drawable.right);
                     statusText.setText("Available");
                 } else {
                     statusText.setText("Unavailable");
                     statusImg.setImageResource(R.drawable.cross);
                 }
                mListContainer.addView(view, i);
            }
        } else {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_error, null);
            mListContainer.addView(view);
        }
        progress.dismiss();
    }

    private void chooseActivityFromOption(final JSONObject finalJson_data) {
        final CharSequence[] items = { "View", "Edit", "Delete", "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mActivity);
        builder.setTitle("Select an activity");
        builder.setIcon(R.drawable.user_img);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("View")) {
                    String message = null;
                    try {
                        message = "Name: "+finalJson_data.getString("name")+"\n"
                                 +"Gender: "+finalJson_data.getString("gender")+"\n"
                                 +"Age Group: "+finalJson_data.getString("ageGroup")+"\n"
                                 +"blood group: "+finalJson_data.getString("bloodGroup")+"\n"
                                 +"Mobile: "+finalJson_data.getString("mobile")+"\n"
                                 +"Address: "+finalJson_data.getString("address")+"\n"
                                 +"City: "+finalJson_data.getString("city")+"\n"
                                 +"State: "+finalJson_data.getString("state")+"\n"
                                 +"Status: "+finalJson_data.getString("availability");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showDialogForBloodGroup("Donar info", message);
                } else if (items[item].equals("Edit")) {
                    add(EditFragment.newInstance(finalJson_data));
                } else if (items[item].equals("Delete")) {
                    if(isNetworkAvailable()){
                        progress.setMessage(RegisterConstants.waitProgress);
                        progress.show();
                        progress.setCancelable(false);
                        try {
                            DatabaseReference mDonarsDatabase = getRootReference().child(RegisterConstants.donars_db);
                            mDonarsDatabase.child(finalJson_data.getString("selfRefKey")).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    getFragmentManager().popBackStackImmediate();
                                    Toast.makeText(mActivity, "Deleted", Toast.LENGTH_SHORT).show();
                                    progress.dismiss();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress.dismiss();
                            Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        showNetworkError(mAddedMembers, RegisterConstants.networkErrorText);
                    }
                } else{
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected String getTitle() {
        return "Added Members";
    }
}
