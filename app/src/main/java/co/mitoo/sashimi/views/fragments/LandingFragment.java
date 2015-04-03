package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.view.*;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.FragmentChangeEventBuilder;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.MitooEnum;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.views.widgets.MultiTextSliderView;

/**
 * Created by david on 14-11-05.
 */
public class LandingFragment extends MitooFragment implements BaseSliderView.OnSliderClickListener
{

    private SliderLayout slider;
    private PagerIndicator indicator;
    private int sliderPosition = 0;

    public static LandingFragment newInstance() {
        LandingFragment fragment = new LandingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_landing,
                container, false);
        initializeViews(view);
        initializeFields();
        return view;
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
        //Override just to satisfy library interface
    }

    @Override
    public void onResume(){
        super.onResume();
        changeBackgroundToMatchSlider();
    }

    @Override
    public void onPause(){
        super.onPause();
        updateSliderPosition();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void searchButtonAction(){

        FragmentChangeEvent event = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_search)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setAnimation(MitooEnum.FragmentAnimation.VERTICAL)
                .build();
        postFragmentChangeEvent(event);

    }

    private void loginButtonAction(){

        FragmentChangeEvent event = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_login)
                .build();
        postFragmentChangeEvent(event);

    }

    private void termsAction() {

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundle_key_prompt), String.valueOf(MitooConstants.termsSpinnerNumber));
        routeToAboutMitoo(bundle);

    }

    private void privacyAction(){

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.bundle_key_prompt), String.valueOf(MitooConstants.privacySpinnerNumber));
        routeToAboutMitoo(bundle);
    }

    @Override
    protected void initializeFields(){
        super.initializeFields();
        setPopActionRequiresDelay(true);
    }

    @Override
    protected void initializeViews(View view){
        //Work around for the animation to display a gray background during load
        setIndicator((PagerIndicator)view.findViewById(R.id.custom_indicator));
        initializeCallBacks();
        initializeSlider(view);
        initializeOnClickListeners(view);
        setUpToolBar(view);
    }

    private void initializeCallBacks(){
        setRunnable(new Runnable() {
            @Override
            public void run() {
                changeBackground(R.color.gray_light_five);
            }
        });
    }

    @Override
    protected void initializeOnClickListeners(View view){

        view.findViewById(R.id.signupButton).setOnClickListener(this);
        view.findViewById(R.id.searchButton).setOnClickListener(this);
        view.findViewById(R.id.termsTextView).setOnClickListener(this);
        view.findViewById(R.id.privacyTextView).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(getDataHelper().isClickable()){
            switch (v.getId()) {
                case R.id.signupButton:
                    loginButtonAction();
                    break;
                case R.id.searchButton:
                    searchButtonAction();
                    break;
                case R.id.termsTextView:
                    termsAction();
                    break;
                case R.id.privacyTextView:
                    privacyAction();
                    break;
            }
        }

    }

    //Inner class for holding fields
    private class sliderContent{

        public sliderContent(int imageBackgroundId, int imageIconId, int textTitle ){

            this.imageBackgroundId = imageBackgroundId;
            this.imageIconId = imageIconId;
            this.title = textTitle;

        }
        public int imageBackgroundId;
        public int imageIconId;
        public int title;

    }


    private List<sliderContent> createSliderContents() {
        List<sliderContent> contents = new ArrayList<sliderContent>();
        contents.add(new sliderContent(R.drawable.bg1, R.drawable.home_1_assets, R.string.landing_page_slider_text1)); 
        contents.add(new sliderContent(R.drawable.bg2, R.drawable.home_2_assets, R.string.landing_page_slider_text2));
        contents.add(new sliderContent(R.drawable.bg3, R.drawable.home_3_assets, R.string.landing_page_slider_text3));
        return contents;
    }


    private void initializeSlider(View view) {

        setSlider((SliderLayout)view.findViewById(R.id.slider));
        List<sliderContent> contents = createSliderContents();
        addContentsToSlider(slider, contents);
        getSlider().setPresetTransformer(SliderLayout.Transformer.Stack);
        getSlider().setCustomIndicator(indicator);
        getSlider().stopAutoCycle();

    }

    private void updateSliderPosition(){

        sliderPosition = slider.getCurrentPosition();

    }

    private void addContentsToSlider(SliderLayout slider , List<sliderContent> contents){
        for(sliderContent content : contents){
            MultiTextSliderView textSliderView = new MultiTextSliderView(getActivity());
            textSliderView
                    .title(getResources().getString(content.title))
                    .icon(content.imageIconId)
                    .image(content.imageBackgroundId)
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            slider.addSlider(textSliderView);
        }
    }

    private void changeBackgroundToMatchSlider(){

        switch(sliderPosition){
            case 0:
                changeBackground(R.drawable.bg1);
                break;
            case 1:
                changeBackground(R.drawable.bg2);
                break;
            case 2:
                changeBackground(R.drawable.bg3);
                break;
        }
    }

    private void changeBackground(int resource){
        getActivity().findViewById(R.id.fragment_landing).setBackgroundResource(resource);
    }

    public PagerIndicator getIndicator() {
        return indicator;
    }

    public void setIndicator(PagerIndicator indicator) {
        this.indicator = indicator;
    }

    public SliderLayout getSlider() {
        return slider;
    }

    public void setSlider(SliderLayout slider) {
        this.slider = slider;
    }

    private void routeToAboutMitoo(Bundle bundle){

        FragmentChangeEvent fragmentChangeEvent = FragmentChangeEventBuilder.getSingletonInstance()
                .setFragmentID(R.id.fragment_about_mitoo)
                .setTransition(MitooEnum.FragmentTransition.PUSH)
                .setAnimation(MitooEnum.FragmentAnimation.HORIZONTAL)
                .setBundle(bundle)
                .build();
        postFragmentChangeEvent(fragmentChangeEvent);

    }
}
