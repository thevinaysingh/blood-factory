package com.majavrella.bloodinformer.user;

import android.content.DialogInterface;
import android.os.Bundle;
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
import com.majavrella.bloodinformer.R;
import com.majavrella.bloodinformer.api.APIConstant;
import com.majavrella.bloodinformer.api.APIManager;
import com.majavrella.bloodinformer.api.APIResponse;
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

/**
 * Created by Administrator on 4/27/2017.
 */

public class PostedRequests extends UserFragment {

    private static View mPostBloodRequests;
    private static JSONArray mRequestsArray;
    @Bind(R.id.list_container) LinearLayout mListContainer;

    public static PostedRequests newInstance() {
        return new PostedRequests();
    }

    public PostedRequests() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPostBloodRequests = inflater.inflate(R.layout.fragment_requests_list, container, false);
        ButterKnife.bind(this, mPostBloodRequests);
        fetchRequests();
        return mPostBloodRequests;
    }

    private void fetchRequests() {
        if(isNetworkAvailable()){
            progress.setMessage(RegisterConstants.waitProgress);
            progress.show();
            final String url = Constants.kBaseUrl+Constants.kPatients;
            APIManager.getInstance().callApiListener(url, getActivity(), new APIResponse() {
                @Override
                public void resultWithJSON(APIConstant.ApiLoginResponse code, JSONObject json) {
                    switch (code) {
                        case API_SUCCESS:
                            JSONArray jsonArray = searchResultInJson(json);
                            try {
                                createListOfRequests(jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case API_FAIL:
                            needInternet();
                            progress.dismiss();
                            showNetworkError(mPostBloodRequests, RegisterConstants.networkErrorText);
                            break;
                        case API_NETWORK_FAIL:
                            needInternet();
                            progress.dismiss();
                            showNetworkError(mPostBloodRequests, RegisterConstants.networkErrorText);
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
                    fetchRequests();
                } else {
                    showNetworkError(mPostBloodRequests, "No network connection");
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
                if(json.getJSONObject(key).getString("userId").equals(userProfileManager.getUserId())){
                    requiredJson.put(json.getJSONObject(key));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return requiredJson;
    }

    private void createListOfRequests(final JSONArray mRequestsArray) throws JSONException {
        mListContainer.removeAllViews();
        if(mRequestsArray.length()>0){
            for(int i = 0; i<mRequestsArray.length(); i++){
                Patient patient = null;
                JSONObject json_data = null;
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.request_item, null);
                ImageView memberImg = (ImageView) view.findViewById(R.id.member_image);
                ImageView statusImg = (ImageView) view.findViewById(R.id.status_img);
                TextView statusText = (TextView)view.findViewById(R.id.status_text);
                TextView name = (TextView)view.findViewById(R.id.name);
                TextView bloodGroup = (TextView)view.findViewById(R.id.blood_group);
                TextView lastDate = (TextView)view.findViewById(R.id.last_date);
                TextView city = (TextView)view.findViewById(R.id.city);
                TextView state = (TextView)view.findViewById(R.id.state);

                LinearLayout donarContainer = (LinearLayout)view.findViewById(R.id.donar_container);
                try {
                    json_data = mRequestsArray.getJSONObject(i);
                    patient = setPatient(json_data, new Patient());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                name.setText(patient.getName());
                bloodGroup.setText(patient.getBloodGroup());
                city.setText(patient.getCity());
                state.setText(patient.getState());
                lastDate.setText(patient.getDate());
                if(patient.getGender().equals("Male")){
                    memberImg.setImageResource(R.drawable.male);
                } else {
                    memberImg.setImageResource(R.drawable.female);
                }

                // Check date
                Date currentDate = null;
                Date lastDateOfNeed = null;
                SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    currentDate = mdformat.parse(getCurrentDate());
                    lastDateOfNeed = mdformat.parse(patient.getDate());
                    if(lastDateOfNeed.after(currentDate)||lastDateOfNeed.equals(currentDate)){
                        statusImg.setImageResource(R.drawable.progress);
                        statusText.setText("In progress");
                    } else {
                        statusText.setText("Date over");
                        statusImg.setImageResource(R.drawable.cross);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                final JSONObject finalJson_data = new JSONObject(json_data.toString());
                donarContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseActivityFromOption(finalJson_data);
                    }
                });

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
                                +"Last date: "+finalJson_data.getString("date")+"\n"
                                +"Purpose: "+finalJson_data.getString("purpose")+"\n"
                                +"Mobile: "+finalJson_data.getString("mobile")+"\n"
                                +"City: "+finalJson_data.getString("city")+"\n"
                                +"State: "+finalJson_data.getString("state")+"\n"
                                +"Status: "+finalJson_data.getString("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showDialogForBloodGroup("Request info", message);
                } else if (items[item].equals("Edit")) {
                    add(EditRequestFragment.newInstance(finalJson_data));
                } else if (items[item].equals("Delete")) {
                    if(isNetworkAvailable()){
                        progress.setMessage(RegisterConstants.waitProgress);
                        progress.show();
                        progress.setCancelable(false);
                        try {
                            DatabaseReference mDonarsDatabase = getRootReference().child(RegisterConstants.patients_db);
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
                        showNetworkError(mPostBloodRequests, RegisterConstants.networkErrorText);
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
        //checkDate();
        super.onResume();
    }

    @Override
    protected String getTitle() {
        return "Requests";
    }
}
