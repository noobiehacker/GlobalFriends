package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.MitooEnum;

/**
 * Created by david on 15-01-23.
 */
public class AboutMitooFragment extends MitooFragment {

    private MitooEnum.aboutMitooOption aboutMitooOptions;
    private String[] optionsString;

    public static AboutMitooFragment newInstance() {
        AboutMitooFragment fragment = new AboutMitooFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  getActivity().getLayoutInflater().inflate(R.layout.fragment_about_mitoo,
                container, false);
        initializeFields();
        initializeViews(view);
        initializeOnClickListeners(view);
        return view;
    }


    @Override
    public void onClick(View v) {


    }

    @Override
    protected void initializeFields(){
        super.initializeFields();
        setFragmentTitle();

    }

    @Override
    protected void initializeViews(View view){
        super.initializeViews(view);
        setUpWebView(view);
    }
    
    private void setUpWebView(View view){
        
        WebView webView = (WebView) view.findViewById(R.id.terms_web_view);
        webView.loadUrl(getString(R.string.mitoo_privacy_url));
    
    }

    public String[] getOptionsString() {
        if(optionsString ==null){
            setOptionsString(getResources().getStringArray(R.array.prompt_about_mitoo_array));
        }
        return optionsString;
    }

    public void setOptionsString(String[] optionsString) {
        this.optionsString = optionsString;
    }

    public void setFragmentTitle(){
        switch(getAboutMitooOptions()){
            case TERMS:
                setFragmentTitle(getOptionsString()[0]);
                break;
            case PRIVACYPOLICY:
                setFragmentTitle(getOptionsString()[1]);
                break;
            default:
                setFragmentTitle(getOptionsString()[0]);
                break;
        }
    }

    public MitooEnum.aboutMitooOption getAboutMitooOptions() {
        if(this.aboutMitooOptions == null){
            Bundle arguments = getArguments();
            if (arguments != null) {
                String value = (String) arguments.get(getString(R.string.bundle_key_prompt));
                switch(Integer.parseInt(value)){
                    case 0:
                        setAboutMitooOptions(MitooEnum.aboutMitooOption.TERMS);
                        break;
                    case 1:
                        setAboutMitooOptions(MitooEnum.aboutMitooOption.PRIVACYPOLICY);
                        break;
                    default:
                        setAboutMitooOptions(MitooEnum.aboutMitooOption.TERMS);
                }
            }
        }
        return aboutMitooOptions;
    }

    public void setAboutMitooOptions(MitooEnum.aboutMitooOption aboutMitooOptions) {
        this.aboutMitooOptions = aboutMitooOptions;
    }

}
