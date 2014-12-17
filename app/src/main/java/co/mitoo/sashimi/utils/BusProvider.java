package co.mitoo.sashimi.utils;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by david on 14-12-01.
 */
public final class BusProvider {
    private static final MainThreadBus BUS = new MainThreadBus(ThreadEnforcer.ANY);

    private static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }

    public static void register(Object obj){
        getInstance().register(obj);
    }

    public static void unregister(Object obj){
        getInstance().unregister(obj);
    }

    public static void post(Object obj){
        getInstance().post(obj);
    }
}