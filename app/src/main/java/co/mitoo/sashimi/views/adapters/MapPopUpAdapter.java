package co.mitoo.sashimi.views.adapters;

/**
 * Created by david on 15-02-03.
 */
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import co.mitoo.sashimi.R;

public class MapPopUpAdapter implements InfoWindowAdapter {
    private View popup=null;
    private LayoutInflater inflater=null;

    public MapPopUpAdapter(LayoutInflater inflater) {
        this.inflater=inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup=inflater.inflate(R.layout.view_map_pop_up, null);
        }

        TextView tv=(TextView)popup.findViewById(R.id.map_title);
        tv.setText(marker.getTitle());
       // tv=(TextView)popup.findViewById(R.id.map_icon);
        //tv.setText(marker.getSnippet());

        return(popup);
    }
}