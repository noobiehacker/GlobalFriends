package co.mitoo.sashimi.utils.listener;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by david on 15-01-12.
 */
public class ListViewOnClickLIstener implements AdapterView.OnItemClickListener {

    int topListId;

    public ListViewOnClickLIstener(int topListId) {
        this.topListId = topListId;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        if (parent.getId() == this.topListId) {
            topListClickAction(position);
        }
    }

    private void topListClickAction(int position) {


    }

}
