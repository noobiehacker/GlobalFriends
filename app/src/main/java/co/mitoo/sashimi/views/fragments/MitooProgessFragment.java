package co.mitoo.sashimi.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;


import co.mitoo.sashimi.R;

/**
 * Created by david on 14-11-13.
 */
public abstract class MitooProgessFragment{//} extends ProgressFragment {
    protected View contentView;
/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(contentView);
        setEmptyText(R.string.empty);
        obtainData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //remove model's callback
    }

    private void obtainData() {
        // Show indeterminate progress
        setContentShown(false);
        int loadingTime = 0;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if(MitooProgessFragment.this.isVisible())
                    setContentShown(true);
            }
        }, loadingTime);
    }*/
}
