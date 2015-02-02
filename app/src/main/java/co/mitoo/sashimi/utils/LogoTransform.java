package co.mitoo.sashimi.utils;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by david on 15-02-02.
 */
public class LogoTransform implements Transformation {

    int maxWidth;
    int maxHeight;

    public LogoTransform(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int targetWidth, targetHeight;
        double aspectRatio;
        targetHeight = maxHeight;
        aspectRatio = (double) source.getWidth() / (double) source.getHeight();
        targetWidth = (int) (targetHeight * aspectRatio);
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