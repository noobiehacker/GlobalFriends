package co.mitoo.sashimi.views.adapters;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.appObject.Notification;
import co.mitoo.sashimi.views.fragments.MitooFragment;

/**
 * Created by david on 15-03-12.
 */

public class NotificationListAdapter extends ArrayAdapter<Notification> {

    private MitooFragment fragment;

    public NotificationListAdapter(Context context, int resourceId, List<Notification> objects, MitooFragment fragment){
        super(context, resourceId, objects);
        setFragment(fragment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(getContext(), R.layout.list_view_item_notification ,null);
        Notification notification = this.getItem(position);
        TextView notificationTextView = (TextView) convertView.findViewById(R.id.list_item_text_view);
        String notificationText =getFragment().getDataHelper().getNotificationText(notification.getNotificationType());
        notificationTextView.setText(notificationText);
        return convertView;
    }


    public MitooFragment getFragment() {
        return fragment;
    }

    public void setFragment(MitooFragment fragment) {
        this.fragment = fragment;
    }

}
