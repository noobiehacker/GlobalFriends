package co.mitoo.sashimi.views.widgets;

import android.content.Context;
import android.util.TypedValue;
import android.widget.TextView;

import co.mitoo.sashimi.R;
import it.neokree.materialtabs.MaterialTab;

/**
 * Created by david on 15-03-08.
 */
public class MitooMaterialsTab extends MaterialTab {

    public MitooMaterialsTab(Context ctx, boolean hasIcon) {
        super(ctx, hasIcon);
        setFontSize(ctx.getResources().getDimensionPixelSize(R.dimen.competition_page_tab_text_size));
    }

    protected TextView getTextView(){
        return (TextView) super.getView().findViewById(R.id.text);
    }

    private void setFontSize(float fontSize){

        getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
    }

    public String getName(){
        return this.getTextView().getText().toString();
    }
}
