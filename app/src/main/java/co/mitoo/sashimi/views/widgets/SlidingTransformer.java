package co.mitoo.sashimi.views.widgets;

import android.view.View;

import com.daimajia.slider.library.Animations.BaseAnimationInterface;
import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by david on 14-12-02.
 */
public class SlidingTransformer extends BaseTransformer {

    private BaseAnimationInterface mCustomAnimationInterface;
    private HashMap<View,ArrayList<Float>> h = new HashMap<View, ArrayList<Float>>();
    @Override
    protected void onTransform(View view, float position) {
        ViewHelper.setTranslationX(view,position < 0 ? 0f : -view.getWidth() * position);
    }

    /**
     * Called each {@link #transformPage(View, float)} before {{@link #onTransform(View, float)} is called.
     *
     * @param view
     * @param position
     */
    @Override
    protected void onPreTransform(View view, float position) {
        final float width = view.getWidth();

        ViewHelper.setRotationX(view,0);
        ViewHelper.setRotationY(view,0);
        ViewHelper.setRotation(view,0);
        ViewHelper.setScaleX(view,1);
        ViewHelper.setScaleX(view,1);
        ViewHelper.setScaleY(view,1);
        ViewHelper.setPivotX(view,0);
        ViewHelper.setPivotY(view,0);
        ViewHelper.setTranslationY(view,0);
        ViewHelper.setTranslationX(view, isPagingEnabled() ? 0f : -width * position);

        if (hideOffscreenPages()) {
            ViewHelper.setAlpha(view,position <= -1f || position >= 1f ? 1f : 1f);
        } else {
            ViewHelper.setAlpha(view,1f);
        }

        if(mCustomAnimationInterface != null){
            if(h.containsKey(view) == false || h.get(view).size() == 1){
                if(position > -1 && position < 1){
                    if(h.get(view) == null){
                        h.put(view,new ArrayList<Float>());
                    }
                    h.get(view).add(position);
                    if(h.get(view).size() == 2){
                        float zero = h.get(view).get(0);
                        float cha = h.get(view).get(1) - h.get(view).get(0);
                        if(zero > 0){
                            if(cha > -1 && cha < 0){
                                //in
                                mCustomAnimationInterface.onPrepareNextItemShowInScreen(view);
                            }else{
                                //out
                                mCustomAnimationInterface.onPrepareCurrentItemLeaveScreen(view);
                            }
                        }else{
                            if(cha > -1 && cha < 0){
                                //out
                                mCustomAnimationInterface.onPrepareCurrentItemLeaveScreen(view);
                            }else{
                                //in
                                mCustomAnimationInterface.onPrepareNextItemShowInScreen(view);
                            }
                        }
                    }
                }
            }
        }
    }
    boolean isApp,isDis;
    /**
     * Called each {@link #transformPage(View, float)} call after {@link #onTransform(View, float)} is finished.
     *
     * @param view
     * @param position
     */
    @Override
    protected void onPostTransform(View view, float position) {
        if(mCustomAnimationInterface != null){
            if(position == -1 || position == 1){
                mCustomAnimationInterface.onCurrentItemDisappear(view);
                isApp = true;
            }else if(position == 0){
                mCustomAnimationInterface.onNextItemAppear(view);
                isDis = true;
            }
            if(isApp && isDis){
                h.clear();
                isApp = false;
                isDis = false;
            }
        }
    }


    @Override
    public void setCustomAnimationInterface(BaseAnimationInterface animationInterface){
        mCustomAnimationInterface = animationInterface;
    }
}
