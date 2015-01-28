package co.mitoo.sashimi.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.utils.IsSearchable;

/**
 * Created by david on 14-11-25.
 */
public class SearchableAdapter extends ArrayAdapter<IsSearchable> {
    public SearchableAdapter(Context context, int resourceId, List<IsSearchable> objects) {
        super(context, resourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.list_view_item_text, null);
        }
        TextView itemText = (TextView) convertView.findViewById(R.id.dynamicText);
        String text = getItem(position).getName();
        itemText.setText(getItem(position).getName());
        return convertView;

    }

}
