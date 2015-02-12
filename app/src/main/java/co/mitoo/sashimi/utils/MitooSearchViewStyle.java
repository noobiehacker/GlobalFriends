package co.mitoo.sashimi.utils;

import android.content.Context;
import android.text.SpannableStringBuilder;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import java.lang.reflect.Method;

import co.mitoo.sashimi.R;


/**
 * Created by david on 15-02-11.
 */
public class MitooSearchViewStyle extends SearchViewStyle{

    private Context context;
    public static MitooSearchViewStyle on(final Menu menu, final int id) {
        return new MitooSearchViewStyle(menu, id);
    }

    public static MitooSearchViewStyle on(final SearchView searchView) {
        return new MitooSearchViewStyle(searchView);
    }

    private MitooSearchViewStyle(final Menu menu, final int id) {
        this((SearchView) menu.findItem(id).getActionView());
    }

    private MitooSearchViewStyle(final SearchView searchView) {
        super(searchView);
    }

    public MitooSearchViewStyle setSearchPlateColor(final int color) {
        final View view = getView(SEARCH_PLATE);
        if (view != null) {
            view.setBackgroundColor(color);
        }
        return this;
    }

    public MitooSearchViewStyle setAutoCompleteHintColor(final int color) {
        final AutoCompleteTextView editText = getView(SEARCH_SRC_TEXT);
        if (editText != null) {
            editText.setHintTextColor(color);
        }
        return this;
    }

    public MitooSearchViewStyle setAutoCompleteTextColor(final int color) {
        final AutoCompleteTextView editText = getView(SEARCH_SRC_TEXT);
        if (editText != null) {
            editText.setTextColor(color);
        }
        return this;
    }

    public MitooSearchViewStyle removeAutoCompletePadding() {
        final AutoCompleteTextView editText = getView(SEARCH_SRC_TEXT);
        if (editText != null) {
            editText.setPadding(0,0,0,0);
        }
        return this;
    }

    public MitooSearchViewStyle removeSearchPlatePadding() {
        final View view = getView(SEARCH_PLATE);
        if (view != null) {
            view.setPadding(0,0,0,0);
        }
        return this;
    }
    
    public MitooSearchViewStyle hideCloseButton(){

        View view = super.getView(SearchViewStyle.SEARCH_CLOSE_BTN);
        view.setVisibility(View.GONE);
        view.setClickable(false);
        return this;
    }

    public MitooSearchViewStyle showCloseButton(){

        View view = super.getView(SearchViewStyle.SEARCH_CLOSE_BTN);
        view.setVisibility(View.VISIBLE);
        view.setClickable(true);
        return this;
    }

    public MitooSearchViewStyle setUpRemaining(){
        getSearchView().requestFocusFromTouch();
        getSearchView().setIconified(false);
        getSearchView().setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        removeAutoCompletePadding();
        removeSearchPlatePadding();
        hideCloseButton();
        return this;
    }

    public MitooSearchViewStyle setSearchHintDrawable(final String hint) {
        try {
            // android.widget.SearchView$SearchAutoComplete extends AutoCompleteTextView
            final AutoCompleteTextView editText = getView(SEARCH_SRC_TEXT);
            if (editText == null) {
                return this;
            }
            // http://nlopez.io/how-to-style-the-actionbar-searchview-programmatically/
            final Class<?> clazz = Class.forName("android.widget.SearchView$SearchAutoComplete");
            // Add the icon as an ImageSpan
            final Method textSizeMethod = clazz.getMethod("getTextSize");
            final Float rawTextSize = (Float) textSizeMethod.invoke(editText);
            final int textSize = (int) (rawTextSize * 1.25);
            // Create hint text
            final SpannableStringBuilder stopHint = new SpannableStringBuilder();
            if (hint != null ) {
                stopHint.append(hint);
            }
            // Set the new hint text
            final Method setHintMethod = clazz.getMethod("setHint", CharSequence.class);
            setHintMethod.invoke(editText, stopHint);
        } catch (final Exception ignored) {
        }
        return this;
    }

    private Context getContext() {
        if(context==null)
            return getSearchView().getContext();
        return context;
    }
}
