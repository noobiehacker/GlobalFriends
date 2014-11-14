package co.mitoo.sashimi.views.fragments;

import android.app.Fragment;
import android.view.View;
import android.widget.EditText;

import co.mitoo.sashimi.utils.listener.FragmentChangeListener;

/**
 * Created by david on 14-11-13.
 */
public abstract class MitooFragment extends Fragment implements View.OnClickListener{

    protected FragmentChangeListener viewlistner;

    protected String getTextFromTextField(int textFieldId){
        EditText textField = (EditText) getActivity().findViewById(textFieldId);
        return textField.getText().toString();
    }

    public void setViewlistner(FragmentChangeListener viewlistner) {
        this.viewlistner = viewlistner;
    }
}
