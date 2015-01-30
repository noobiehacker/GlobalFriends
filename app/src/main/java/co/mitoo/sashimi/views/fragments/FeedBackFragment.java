package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.MitooEnum;

/**
 * Created by david on 15-01-13.
 */
public class FeedBackFragment extends MitooFragment {

    private MitooEnum.feedBackOption feedBack = null;
    private String[] feedBackString;

    public static FeedBackFragment newInstance() {
        FeedBackFragment fragment = new FeedBackFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  getActivity().getLayoutInflater().inflate(R.layout.fragment_feed_back,
                container, false);
        initializeFields();
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.feedback_contact_mitoo_container:
                contactMitooAction();
                break;
        }
    }

    @Override
    protected void initializeFields(){
        super.initializeFields();
        setFragmentTitle();

    }

    @Override
    protected void initializeViews(View view){
        super.initializeViews(view);
        RelativeLayout container = (RelativeLayout) view.findViewById(R.id.feedback_partial_container);
        container.addView(getLayoutPartial());
        setUpContactMitooView(view);
    }
    
    @Override
    protected void initializeOnClickListeners(View view){
        
        view.findViewById(R.id.feedback_contact_mitoo_container).setOnClickListener(this);
        super.initializeOnClickListeners(view);
    }

    public String[] getFeedBackString() {
        if(feedBackString==null){
            setFeedBackString(getResources().getStringArray(R.array.prompt_feed_back_array));
        }
        return feedBackString;
    }

    public void setFeedBackString(String[] feedBackString) {
        this.feedBackString = feedBackString;
    }

    public void setFragmentTitle(){
        switch(getFeedBack()){
            case HAPPY:
                setFragmentTitle(getFeedBackString()[0]);
                break;
            case CONFUSED:
                setFragmentTitle(getFeedBackString()[1]);
                break;
            case UNHAPPY:
                setFragmentTitle(getFeedBackString()[2]);
                break;
        }
    }
    
    private View getLayoutPartial(){
        int partialId = R.layout.partial_happy;
        View returnView = null;
        switch(getFeedBack()){
            case HAPPY:
                partialId= R.layout.partial_happy;
                returnView = getActivity().getLayoutInflater().inflate(partialId, null);
                break;
            case CONFUSED:
                partialId= R.layout.partial_confused;
                returnView = getActivity().getLayoutInflater().inflate(partialId, null);
                setUpFAQView(returnView);
                break;
            case UNHAPPY:
                partialId= R.layout.partial_unhappy;
                returnView = getActivity().getLayoutInflater().inflate(partialId, null);
                break;
        }
        return returnView;
    }

    public MitooEnum.feedBackOption getFeedBack() {
        
        if (this.feedBack == null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                String value = (String) arguments.get(getString(R.string.bundle_key_prompt));
                switch(Integer.parseInt(value)){
                    case 0:
                        setFeedback(MitooEnum.feedBackOption.HAPPY);
                        break;
                    case 1:
                        setFeedback(MitooEnum.feedBackOption.CONFUSED);
                        break;
                    case 2:
                        setFeedback(MitooEnum.feedBackOption.UNHAPPY);
                        break;
                }
            }
        }
        return feedBack;

    }

    public void setFeedback(MitooEnum.feedBackOption feedback) {
        this.feedBack = feedback;
    }
    
    public void setUpContactMitooView(View view){
        
        RelativeLayout container = (RelativeLayout) view.findViewById(R.id.feedback_contact_mitoo_container);
        TextView contactMitooTextView = (TextView) container.findViewById(R.id.dynamicText);
        contactMitooTextView.setText(getString(R.string.feedback_page_text1));
        
    }

    public void setUpFAQView(View view){

        RelativeLayout container = (RelativeLayout) view.findViewById(R.id.faq_container);
        TextView contactMitooTextView = (TextView) container.findViewById(R.id.dynamicText);
        contactMitooTextView.setText(getString(R.string.confused_page_text2));

    }
    
    private void contactMitooAction(){

        getMitooActivity().contactMitoo();

    }
}
