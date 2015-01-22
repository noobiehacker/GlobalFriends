package co.mitoo.sashimi.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Sport;

/**
 * Created by david on 14-11-25.
 */
public class SportAdapter extends ArrayAdapter<Sport> {
    public SportAdapter(Context context, int resourceId, List<Sport> objects) {
        super(context, resourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.list_view_item_text, null);
        }
        TextView sportsText = (TextView) convertView.findViewById(R.id.itemText);
        sportsText.setText(getItem(position).getName());
        return convertView;

    }

}
