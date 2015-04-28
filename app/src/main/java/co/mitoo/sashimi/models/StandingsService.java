package co.mitoo.sashimi.models;


import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.mitoo.sashimi.models.appObject.MitooStandings;
import co.mitoo.sashimi.models.jsonPojo.recieve.standings.SteakStandings;
import co.mitoo.sashimi.services.BaseService;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.events.LoadStandingsEvent;
import co.mitoo.sashimi.utils.events.StandingsLoadedEvent;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by david on 15-04-20.
 */
public class StandingsService extends BaseService {


    public StandingsService() {
    }

    @Subscribe
    public void onLoadStandings(LoadStandingsEvent event) {

        Observable<SteakStandings> observable = getSteakApiService().getCompetitionStandings(event.getCompetitionSeasonID());
        observable.subscribe(new Subscriber<SteakStandings>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SteakStandings objectRecieve) {

                SteakStandings steakStanding = (SteakStandings) objectRecieve;
                BusProvider.post(new StandingsLoadedEvent(standingsTransform(steakStanding)));
            }
        });

    }

    private List<MitooStandings> standingsTransform(SteakStandings steakStanding){

        MitooStandings.setUpClassData(steakStanding);
        List<MitooStandings> result = new ArrayList<MitooStandings>();
        int[] series = steakStanding.getSeries();
        for(int id : series){
            result.add(new MitooStandings(id));
        }
        Collections.sort(result);
        return result;

    }
}