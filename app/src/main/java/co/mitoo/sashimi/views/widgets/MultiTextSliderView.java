package co.mitoo.sashimi.views.widgets;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import co.mitoo.sashimi.R;

/**
 * This is a slider with a description multiple TextViews.
 */
public class MultiTextSliderView extends BaseSliderView {

    private String mTitle;
    private String mDescriptionOne;
    private String mDescriptionTwo;
    private int mIconId;
    public MultiTextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_multi_text, null);
        ImageView background = (ImageView) v.findViewById(R.id.image);
        ImageView icon = (ImageView) v.findViewById(R.id.icon);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView descriptionOne = (TextView) v.findViewById(R.id.descriptionOne);
        TextView descriptionTwo = (TextView) v.findViewById(R.id.descriptionTwo);
        descriptionOne.setText(getDescriptionOne());
        descriptionTwo.setText(getDescriptionTwo());
        icon.setBackgroundResource(getIconId());
        title.setText(getTitle());
        bindEventAndShow(v, background);
        return v;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getDescriptionOne(){
        return mDescriptionOne;
    }

    public String getDescriptionTwo(){
        return mDescriptionTwo;
    }

    public int getIconId(){
        return mIconId;
    }

    public MultiTextSliderView title(String Title){
        mTitle = Title;
        return this;
    }
    public MultiTextSliderView descriptionOne(String description){
        mDescriptionOne = description;
        return this;
    }

    public MultiTextSliderView descriptionTwo(String description){
        mDescriptionTwo = description;
        return this;
    }

    public MultiTextSliderView icon(int id){
        mIconId = id;
        return this;
    }
}