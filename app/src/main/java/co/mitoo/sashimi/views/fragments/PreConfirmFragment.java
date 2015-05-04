package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;

/**
 * Created by david on 15-05-04.
 */
public class PreConfirmFragment extends MitooFragment {

    private TextView topText;
    private TextView bottomText;

    public static PreConfirmFragment newInstance() {
        return new PreConfirmFragment();
    }


    @Override
    public void onClick(View v) {
        if (getDataHelper().isClickable(v.getId())) {
            switch (v.getId()) {
                case R.id.preConfirmButton:
                    resendButtonAction();
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_pre_confirm,
                container, false);
        initializeFields();
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeOnClickListeners(View view) {
        super.initializeOnClickListeners(view);
        view.findViewById(R.id.preConfirmButton).setOnClickListener(this);

    }

    @Override
    public void onResume() {

        super.onResume();

    }

    @Override
    public void initializeViews(View view) {

        super.initializeViews(view);
        this.topText = (TextView) view.findViewById(R.id.preConfirmText1);
        this.topText.setText(createTopText());
        this.bottomText = (TextView) view.findViewById(R.id.preConfirmText2);
        this.bottomText.setText(createBottomText());

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error) {

        super.onError(error);
    }

    @Override
    protected void initializeFields() {
        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_pre_confirm));
    }

    private String getIdentifierType(){

        Bundle bundle = getArguments();
        String key = getString(R.string.bundle_key_identifier_type);
        String identifier = bundle.getString(key);
        return identifier;

    }

    private String createTopText(){

        String identifier = getIdentifierType();
        return getString(R.string.pre_confirm_page_text1) + identifier;
    }

    private String createBottomText(){

        String result = getString(R.string.pre_confirm_page_text2)+ getIdentifierType()+ getString(R.string.pre_confirm_page_text3);
        return result;

    }

    protected void handleHttpErrors(int statusCode) {

        /*
        if(statusCode == 404 ){
            String errorMessage = getDataHelper().getResetPageBadEmailMessage(getEmail());
            displayTextWithToast(errorMessage);
        }else{
            super.handleHttpErrors(statusCode);
        }*/

    }

    private void resendButtonAction(){

    }
}