package co.mitoo.sashimi.views.widgets;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import co.mitoo.sashimi.R;

/**
 * Created by david on 15-05-13.
 */
public class HeaderListView extends ListView {

    private View headerView;
    private TextView headerTextView;
    public HeaderListView(Context context) {
        super(context);
    }

    public HeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeaderView(View headerView) {

        if (headerView != null) {
            this.headerView = headerView;
            this.headerTextView = (TextView) this.headerView.findViewById(R.id.header_view);
        }
    }

    public View getHeaderView() {
        return headerView;
    }

    public void setHeaderVisibility(int visibility) {
        switch (visibility) {
            case View.GONE:
            case View.INVISIBLE:
                hideHeaderView();
                break;
            case View.VISIBLE:
                showHeaderView();
                break;
        }
    }

    private void showHeaderView() {

        setSubViewsVisibility(View.VISIBLE);

    }

    private void hideHeaderView() {

        setSubViewsVisibility(View.GONE);

    }

    private void setSubViewsVisibility(int viewsVisibility){

        if (this.headerTextView != null) {
            this.headerTextView.setVisibility(viewsVisibility);
        }
        if (this.headerView != null) {
            this.headerView.setVisibility(viewsVisibility);
        }

    }
}
