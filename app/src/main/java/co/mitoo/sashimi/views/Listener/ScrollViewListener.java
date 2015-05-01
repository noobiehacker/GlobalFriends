package co.mitoo.sashimi.views.Listener;

import co.mitoo.sashimi.views.widgets.ObservableScrollView;

/**
 * Created by david on 15-04-30.
 */
public interface ScrollViewListener {

    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

}