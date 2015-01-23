package co.mitoo.sashimi.views.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.mitoo.sashimi.R;

/**
 * Created by david on 15-01-12.
 */
public class UserProfileDetailsAdapter extends ArrayAdapter<String> {

    private List<String> leftStringList;
    
    public UserProfileDetailsAdapter(Context context, int resourceId, List<String> objects) {
        super(context, resourceId, objects);
        leftStringList = buildStringList(R.id.settings_list_details, context.getResources());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = View.inflate(getContext(), R.layout.list_view_item_text_text, null);
            TextView rightText = (TextView)convertView.findViewById(R.id.rightText);
            TextView leftText = (TextView)convertView.findViewById(R.id.leftText);
            rightText.setText(getItem(position));
            leftText.setText(leftStringList.get(position));

        }
        return convertView;

    }
    
    public List<String> buildStringList(int arrayID , Resources resources){

        List<String> returnList = new ArrayList<String>();
        String[] arrayOfString = resources.getStringArray(arrayID);
        for(String item : arrayOfString){
            returnList.add(item);
        }
        return returnList;

    }

}
