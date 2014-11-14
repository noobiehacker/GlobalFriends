package co.mitoo.sashimi.views.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.events.FragmentChangeEvent;
import co.mitoo.sashimi.utils.listener.FragmentChangeListener;
import co.mitoo.sashimi.views.widgets.MultiTextSliderView;

/**
 * Created by david on 14-11-05.
 */
public class LandingFragment extends MitooFragment implements BaseSliderView.OnSliderClickListener{

    private SliderLayout slider;
    private List<sliderContent> sliderContents ;

    public static LandingFragment newInstance(FragmentChangeListener listner) {
        LandingFragment fragment = new LandingFragment();
        fragment.viewlistner=listner;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_landing,
                container, false);
        initializeOnClickListeners(view);
        return view;
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
        //Override just to satisfy library interface
    }
    @Override
    public void onResume(){
        if(slider==null)
            setUpSlider();
        super.onStart();

    }
    @Override
    public void onPause(){
        slider=null;
        super.onPause();
    }

    private void searchButtonAction(){
        FragmentChangeEvent event = new FragmentChangeEvent(this, SignupFragment.class);
        event.setPush(true);
        viewlistner.onFragmentChange(event);
    }

    private void loginButtonAction(){
        FragmentChangeEvent event = new FragmentChangeEvent(this, LoginFragment.class);
        event.setPush(true);
        viewlistner.onFragmentChange(event);
    }

    private void initializeOnClickListeners(View view){
        view.findViewById(R.id.signupButton).setOnClickListener(this);
        view.findViewById(R.id.searchButton).setOnClickListener(this);
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

    private void changeBackground(int id){

        //Fade out and than fade in
        View view = getView().findViewById(R.id.background);
        YoYo.with(Techniques.FadeOutLeft)
                .duration(700)
                .playOn(view);
        Drawable drawable = getResources().getDrawable(id);
        view.setBackgroundResource(id);
        YoYo.with(Techniques.FadeInRight)
                .duration(700)
                .playOn(view);

    }

    //Inner class for holding fields
    private class sliderContent{

        public sliderContent(int imageId, int textTitle , int textDescription){
            this.imageId = imageId;
            this.title = textTitle;
            this.description = textDescription;
        }
        public int imageId;
        public int title;
        public int description;

    }

    private void initializeSlider(){
        List<sliderContent> contents = new ArrayList<sliderContent>();
        contents.add(new sliderContent(R.drawable.ic_search_white, R.string.landing_page_slider_text1 , R.string.landing_page_slider_text2));
        contents.add(new sliderContent(R.drawable.ic_account_circle_white, R.string.landing_page_slider_text3, R.string.landing_page_slider_text4));
        contents.add(new sliderContent(R.drawable.ic_alarm_white, R.string.landing_page_slider_text5 , R.string.landing_page_slider_text6));
        this.sliderContents = contents;
    }

    private List<sliderContent> getSliderContents() {
        if(sliderContents==null){
            initializeSlider();
        }
        return sliderContents;
    }

    private SliderLayout getSlider() {
        if(slider==null)
            slider = (SliderLayout)getView().findViewById(R.id.slider);

        return slider;
    }

    private void setUpSlider(){
        slider = (SliderLayout)getView().findViewById(R.id.slider);
        List<sliderContent> contents = getSliderContents();
        for(sliderContent content : contents){
            MultiTextSliderView textSliderView = new MultiTextSliderView(getActivity());
            textSliderView
                    .title(getResources().getString(content.title))
                    .description(getResources().getString(content.description))
                    .image(content.imageId)
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            slider.addSlider(textSliderView);
        }
        slider.setPresetTransformer(SliderLayout.Transformer.FlipHorizontal);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(5000);
    }

}
