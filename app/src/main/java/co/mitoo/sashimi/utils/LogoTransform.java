package co.mitoo.sashimi.utils;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by david on 15-02-02.
 */
public class LogoTransform implements Transformation {

    int maxWidth;
    int maxHeight;
    double idealRatio = 2.1;
    public LogoTransform( int maxHeight) {
        this.maxHeight = maxHeight;
        this.maxWidth = (int)(this.maxHeight * idealRatio);
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int targetWidth, targetHeight;
        double aspectRatio;
        //If aspect ratio is tall and thin, use MaxHeight allowed and scale width according to ratio
        //If aspect ratio is short and fat, use MaxWidth allowed and scale width according to ratio
        targetHeight = maxHeight;
        aspectRatio = (double) source.getWidth() / (double) source.getHeight();
        if(aspectRatio>=idealRatio){
            targetWidth = maxWidth;
            targetHeight = (int) (targetWidth/ aspectRatio);

        }
        else{
            targetHeight =maxHeight;
            targetWidth = (int) (targetHeight * aspectRatio);

        }
        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return maxWidth + "x" + maxHeight;
    }

};