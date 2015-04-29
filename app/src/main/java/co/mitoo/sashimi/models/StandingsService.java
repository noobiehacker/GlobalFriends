package co.mitoo.sashimi.models;


import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.mitoo.sashimi.models.appObject.StandingsRow;
import co.mitoo.sashimi.models.jsonPojo.recieve.standings.StandingsJSON;
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

        Observable<StandingsJSON> observable = getSteakApiService().getCompetitionStandings(event.getCompetitionSeasonID());
        observable.subscribe(new Subscriber<StandingsJSON>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(StandingsJSON objectRecieve) {

                StandingsJSON steakStanding = (StandingsJSON) objectRecieve;
                BusProvider.post(new StandingsLoadedEvent(standingsTransform(steakStanding)));
            }
        });

    }

    private List<StandingsRow> standingsTransform(StandingsJSON json){

        List<StandingsRow> result = new ArrayList<StandingsRow>();

        for(int id : json.getSeries()){

            result.add(new StandingsRow(id, json.getData().get(id)));
        }
        return result;

    }
}