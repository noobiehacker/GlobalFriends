package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.otto.Subscribe;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.MitooActivitiesErrorEvent;

/**
 * Created by david on 15-03-17.
 */
public class ConfirmAccountFragment extends MitooFragment {

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable()){
            switch (v.getId()) {
                case R.id.confirmJoinButton:
                    confirmButtonAction();
                    break;
            }
        }
    }

    public static ConfirmAccountFragment newInstance() {
        ConfirmAccountFragment fragment = new ConfirmAccountFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_confirm_account,
                container, false);
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    protected void initializeViews(View view){

        super.initializeViews(view);

    }

    @Override
    protected void initializeFields(){

        super.initializeFields();
        setFragmentTitle(getString(R.string.tool_bar_confirm_your_account));

    }

    @Subscribe
    public void onError(MitooActivitiesErrorEvent error){

        super.onError(error);
    }

    @Override
    protected void initializeOnClickListeners(View view) {

        view.findViewById(R.id.confirmJoinButton).setOnClickListener(this);

    }

    private void confirmButtonAction(){
        fireFragmentChangeAction(R.id.fragment_confirm_set_password, MitooEnum.FragmentTransition.PUSH
                , MitooEnum.FragmentAnimation.HORIZONTAL);
    }


}
