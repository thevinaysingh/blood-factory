package com.majavrella.bloodfactory.user;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.UserFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DonarList extends UserFragment {

    private static View mDonarList;
    private static JSONArray mDonarsListArray;
    private ProgressDialog progressDialog;
    @Bind(R.id.list_container) LinearLayout mListContainer;

    public static DonarList newInstance(JSONArray jsonArray) {
        mDonarsListArray = jsonArray;
        return new DonarList();
    }


    public DonarList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDonarList = inflater.inflate(R.layout.fragment_donar_list, container, false);
        ButterKnife.bind(this, mDonarList);

        progressDialog = new ProgressDialog(getActivity(), android.R.style.Theme_Black);
        progressDialog.setMessage("Displaying result...");
        progressDialog.show();
        createListOfDonars(mDonarsListArray);
        return mDonarList;
    }

    private void createListOfDonars(final JSONArray mDonarsListArray) {
        if(mDonarsListArray.length()>0){
            for(int i = 0; i<mDonarsListArray.length(); i++){
                String message = null;
                String mobileNo = null;
                String donarName = null;
                JSONObject json_data=null;
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item, null);
                TextView bloodGroup = (TextView)view.findViewById(R.id.blood_group);
                LinearLayout donarContainer = (LinearLayout)view.findViewById(R.id.donar_container);
                TextView status = (TextView)view.findViewById(R.id.status);
                TextView name = (TextView)view.findViewById(R.id.name);
                TextView gender = (TextView)view.findViewById(R.id.gender);
                TextView city = (TextView)view.findViewById(R.id.city);
                TextView state = (TextView)view.findViewById(R.id.state);
                TextView phone = (TextView)view.findViewById(R.id.phone_no);
                TextView more = (TextView)view.findViewById(R.id.more);
                ImageView call = (ImageView) view.findViewById(R.id.call);
                ImageView sms = (ImageView) view.findViewById(R.id.sms);
                ImageView share = (ImageView) view.findViewById(R.id.share);
                ImageView about = (ImageView) view.findViewById(R.id.about);
                try {
                    json_data = mDonarsListArray.getJSONObject(i);
                    bloodGroup.setText(json_data.getString("bloodGroup"));
                    status.setText(json_data.getString("availability"));
                    if (status.getText().equals("Available")){
                        donarContainer.setBackgroundColor(Color.parseColor("#B41C2B"));
                    }
                    name.setText(json_data.getString("name"));
                    donarName = capitalizeFirstLetter(json_data.getString("name"));
                    gender.setText(json_data.getString("gender"));
                    city.setText(json_data.getString("city"));
                    state.setText(json_data.getString("state"));
                    phone.setText(json_data.getString("mobile"));
                    mobileNo = json_data.getString("mobile").toString();
                    message = "Name: "+json_data.getString("name")+"\n"
                            +"Gender: "+json_data.getString("gender")+"\n"
                            +"Age Group: "+json_data.getString("ageGroup")+"\n"
                            +"blood group: "+json_data.getString("bloodGroup")+"\n"
                            +"Mobile: "+json_data.getString("mobile")+"\n"
                            +"Address: "+json_data.getString("address")+"\n"
                            +"City: "+json_data.getString("city")+"\n"
                            +"State: "+json_data.getString("state")+"\n"
                            +"Status: "+json_data.getString("availability");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final LinearLayout moreContainer = (LinearLayout) view.findViewById(R.id.more_container);
                mListContainer.addView(view, i);
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
            }
        } else {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_error, null);
            mListContainer.addView(view);
        }
        progressDialog.dismiss();
    }

    @Override
    protected String getTitle() {
        return "List of Donars";
    }
}
