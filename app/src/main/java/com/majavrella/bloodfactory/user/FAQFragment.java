package com.majavrella.bloodfactory.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majavrella.bloodfactory.R;
import com.majavrella.bloodfactory.base.Constants;
import com.majavrella.bloodfactory.base.FaqQandA;
import com.majavrella.bloodfactory.base.UserFragment;
import com.majavrella.bloodfactory.register.RegisterConstants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FAQFragment extends UserFragment {

    private static View mEditProfileView;
    String quest;
    @Bind(R.id.linear_card_view) LinearLayout faqContainer;
    /*@Bind(R.id.ask_quest_button)
    Button askButton;
    @Bind(R.id.ask_your_quest)
    EditText askQuest;*/

    public static FAQFragment newInstance() {
        return new FAQFragment();
    }

    public FAQFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mEditProfileView = inflater.inflate(R.layout.fragment_faq, container, false);
        ButterKnife.bind(this, mEditProfileView);
        
        createQandA(FaqQandA.faqQuestions, FaqQandA.faqAnswers);
        setStatusBarColor(Constants.colorStatusBarDark);
        /*askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    quest = null;
                } else {
                    Toast.makeText(mActivity, RegisterConstants.networkErrorTitle, Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        return mEditProfileView;
    }

    @Override
    public void onResume() {
        hideKeyboard(getActivity());
        super.onResume();
    }

    private void createQandA(String[] faqQuestions, String[] faqAnswers) {
        for(int i=0; i<faqQuestions.length; i++){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.q_a_block, null);
            LinearLayout questBlock = (LinearLayout) view.findViewById(R.id.question_block);
            final ImageView nextArrow = (ImageView) view.findViewById(R.id.next_image);
            final ImageView downArrow = (ImageView) view.findViewById(R.id.down_image);
            TextView quest = (TextView) view.findViewById(R.id.question);
            final TextView answer = (TextView) view.findViewById(R.id.answer);
            quest.setText(faqQuestions[i]);
            answer.setText(faqAnswers[i]);
            faqContainer.addView(view, i);
            questBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(answer.getVisibility()==View.VISIBLE){
                        nextArrow.setVisibility(View.VISIBLE);
                        downArrow.setVisibility(View.GONE);
                        answer.setVisibility(View.GONE);
                    } else {
                        nextArrow.setVisibility(View.GONE);
                        downArrow.setVisibility(View.VISIBLE);
                        answer.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    protected String getTitle() {
        return Constants.kFAQFragment;
    }
}
