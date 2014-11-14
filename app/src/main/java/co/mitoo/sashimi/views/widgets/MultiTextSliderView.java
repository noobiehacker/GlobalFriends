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
    public MultiTextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_multi_text, null);
        ImageView target = (ImageView) v.findViewById(R.id.image);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView description = (TextView) v.findViewById(R.id.description);
        description.setText(getDescription());
        title.setText(getTitle());
        bindEventAndShow(v, target);
        return v;
    }

    public String getTitle(){
        return mTitle;
    }

    public BaseSliderView title(String Title){
        mTitle = Title;
        return this;
    }
}