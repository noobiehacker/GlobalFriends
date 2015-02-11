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
    private int mIconId;
    public MultiTextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_multi_text, null);
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setText(getTitle());
        ImageView background = (ImageView) v.findViewById(R.id.image);
        ImageView icon = (ImageView) v.findViewById(R.id.icon);
        if(getIconId() == R.drawable.home_2_assets){
            icon.getLayoutParams().height = getContext().getResources().getDimensionPixelSize(R.dimen.slide_icon_big_height);
            icon.getLayoutParams().width = getContext().getResources().getDimensionPixelSize(R.dimen.slide_icon_big_width);
        }
        icon.setBackgroundResource(getIconId());

        bindEventAndShow(v, background);
        return v;
    }

    public String getTitle(){
        return mTitle;
    }

    public int getIconId(){
        return mIconId;
    }

    public MultiTextSliderView title(String Title){
        mTitle = Title;
        return this;
    }

    public MultiTextSliderView icon(int id){
        mIconId = id;
        return this;
    }
}