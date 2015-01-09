package co.mitoo.sashimi.models;

import android.content.res.Resources;
import android.location.Location;

import com.squareup.otto.Subscribe;

import java.util.List;

import co.mitoo.sashimi.models.jsonPojo.League;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.LocationRequestEvent;
import co.mitoo.sashimi.utils.events.LocationResponseEvent;

/**
 * Created by david on 14-12-08.
 */
public class CompetitionModel extends MitooModel implements ICompetitionModel{

    public CompetitionModel(Resources resources){
        super(resources);
    }

    private Location location;

    public void requestLocation() {
        if(location==null)
            locationRequest();
        else{
            BusProvider.post(new LocationResponseEvent(location));
        }
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    private void locationRequest(){
        BusProvider.post(new LocationRequestEvent());
    }

    @Subscribe
    public void locationResponse(LocationResponseEvent event){
        setLocation(event.getLocation());
    }

    @Override
    public List<League> getLeagueData() {
        return null;
    }

    @Override
    public void updateLeagueData(List<League> data) {

    }

    public void removeReferences(){
        super.removeReferences();
    }
}
