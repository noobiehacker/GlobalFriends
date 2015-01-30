package co.mitoo.sashimi.views.fragments;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import java.util.ArrayList;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.views.widgets.MultiTextSliderView;

/**
 * Created by david on 14-11-05.
 */
public class LandingFragment extends MitooFragment implements BaseSliderView.OnSliderClickListener
{

    private SliderLayout slider;
    private PagerIndicator indicator;
    private Handler myHandler;
    private Runnable changeBackgroundCallBack;
    private boolean stacked = false;
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
        initializeViewElements(view);
        initializeFields();
        myHandler.postDelayed(changeBackgroundCallBack, 1000);
        return view;
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
        //Override just to satisfy library interface
    }

    @Override
    public void onResume(){
        changeBackgroundToMatchSlider();
        super.onResume();
    }

    @Override
    public void onPause(){
        myHandler.removeCallbacks(changeBackgroundCallBack);
        updateSliderPosition();
        super.onPause();
    }

    @Override
    public void onStop() {
        myHandler.removeCallbacks(changeBackgroundCallBack);
        super.onStop();

    }

    private void searchButtonAction(){
        fireFragmentChangeAction(R.id.fragment_search);
    }

    private void loginButtonAction(){
        fireFragmentChangeAction(R.id.fragment_login);
    }

    @Override
    protected void initializeFields(){
        this.myHandler = new Handler();
    }

    private void initializeViewElements(View view){
        //Work around for the animation to display a gray background during load
        indicator = (PagerIndicator)view.findViewById(R.id.custom_indicator);
        initializeCallBacks();
        initializeSlider(view);
        initializeOnClickListeners(view);
        setUpToolBar(view);
    }

    private void initializeCallBacks(){
        changeBackgroundCallBack = new Runnable() {
            @Override
            public void run() {
                changeBackground(R.color.gray_light_two);
            }
        };
    }

    @Override
    protected void initializeOnClickListeners(View view){
        view.findViewById(R.id.signupButton).setOnClickListener(this);
        view.findViewById(R.id.searchButton).setOnClickListener(this);
        view.findViewById(R.id.logo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signupButton:
                loginButtonAction();
                break;
            case R.id.searchButton:
                searchButtonAction();
                break;
        }
    }

    //Inner class for holding fields
    private class sliderContent{

        public sliderContent(int imageBackgroundId, int imageIconId, int textTitle , int descriptionOne , int descriptionTwo){
            this.imageBackgroundId = imageBackgroundId;
            this.imageIconId = imageIconId;
            this.title = textTitle;
            this.descriptionOne = descriptionOne;
            this.descriptionTwo = descriptionTwo;
        }
        public int imageBackgroundId;
        public int imageIconId;
        public int title;
        public int descriptionOne;
        public int descriptionTwo;

    }


    private List<sliderContent> createSliderContents() {
        List<sliderContent> contents = new ArrayList<sliderContent>();
        contents.add(new sliderContent(R.drawable.bg1, R.drawable.home_1_assets, R.string.landing_page_slider_text1 , R.string.landing_page_slider_text2, R.string.landing_page_slider_text3));
        contents.add(new sliderContent(R.drawable.bg2, R.drawable.home_2_assets, R.string.landing_page_slider_text4 , R.string.landing_page_slider_text5, R.string.landing_page_slider_text6));
        contents.add(new sliderContent(R.drawable.bg3, R.drawable.home_3_assets, R.string.landing_page_slider_text7 , R.string.landing_page_slider_text8, R.string.landing_page_slider_text9));
        return contents;
    }


    private void initializeSlider(View view) {

        slider = (SliderLayout)view.findViewById(R.id.slider);
        List<sliderContent> contents = createSliderContents();
        addContentsToSlider(slider, contents);
        slider.setPresetTransformer(SliderLayout.Transformer.Stack);
        slider.setCustomIndicator(indicator);
        slider.stopAutoCycle();

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

}
