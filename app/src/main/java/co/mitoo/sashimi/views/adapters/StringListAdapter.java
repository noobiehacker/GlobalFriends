package co.mitoo.sashimi.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import co.mitoo.sashimi.R;

/**
 * Created by david on 15-01-12.
 */
public class StringListAdapter extends ArrayAdapter<String> {
    
    public StringListAdapter(Context context, int resourceId, List<String> objects) {
        super(context, resourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        this.getItem(position);
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.list_view_item_text, null);
            TextView textView = (TextView) convertView.findViewById(R.id.dynamicText);
            textView.setText(getItem(position));
        }
        return convertView;

    }

}
