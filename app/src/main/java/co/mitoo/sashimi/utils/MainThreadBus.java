package co.mitoo.sashimi.utils;

import android.os.Looper;
import android.os.Handler;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by david on 14-12-16.
 */
public class MainThreadBus extends Bus {

    public MainThreadBus(ThreadEnforcer enforcer){
        super(enforcer);
    }
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override public void post(final Object event) {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MainThreadBus.super.post(event);
                }
            });
        }
    }
}