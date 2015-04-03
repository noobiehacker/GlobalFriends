package co.mitoo.sashimi.views.widgets;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by david on 15-02-23.
 */
public class MitooImageTarget implements Target {
    private ImageView imageView;
    private Callback callBack;

    public MitooImageTarget(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
        imageView.setImageBitmap(bitmap);
        if(callBack!=null)
            callBack.onSuccess();
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        imageView.setImageDrawable(errorDrawable);
        if(callBack!=null)
            callBack.onError();
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        imageView.setImageDrawable(placeHolderDrawable);
    }

    @Override
    public boolean equals(Object o) {
        return imageView.equals(o);
    }

    @Override
    public int hashCode() {
        return imageView.hashCode();
    }
    
    public void setCallBack(Callback cb){
        this.callBack=cb;
        
    }
}