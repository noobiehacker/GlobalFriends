package co.mitoo.sashimi.models;


import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import co.mitoo.sashimi.models.appObject.StandingsRow;
import co.mitoo.sashimi.models.jsonPojo.recieve.standings.StandingsJSON;
import co.mitoo.sashimi.network.Services.MitooService;
import co.mitoo.sashimi.services.BaseService;
import co.mitoo.sashimi.utils.BusProvider;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.events.LoadStandingsEvent;
import co.mitoo.sashimi.utils.events.StandingsLoadedEvent;
import co.mitoo.sashimi.views.activities.MitooActivity;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by david on 15-04-20.
 */
public class StandingsService extends MitooService {

    public StandingsService(MitooActivity activity) {
        super(activity);
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

        String headKey = "head";
        List<StandingsRow> result = new ArrayList<StandingsRow>();

        //Add head

        //TODO: REMOVE LATER


        String[] dataNew = new String[4];
        for(int i = 0 ; i< 4 ; i++){
            dataNew[i] = json.getCols()[i];
        }
        json.setCols(dataNew);
        List<String> headData = getDataFromMap(json.getCols(), json.getData().get(headKey));
        result.add(new StandingsRow(MitooConstants.standingHead,headData));

        //Add the rest of the columns

        //TODO:REMOVE REFACTOR
        for(int i = 0 ; i < 3 ; i++){
            for(int id : json.getSeries()) {

                List<String> mapData = getDataFromMap(json.getCols(), json.getData().get(Integer.toString(id)));
                result.add(new StandingsRow(id, mapData));

            }
        }

        return result;

    }

    private List<String> getDataFromMap(String[] key, Map<String, String> map ){

        List<String> result = new ArrayList<String>();
        for(String item : key){
            result.add(map.get(item));
        }
        return result;
    }
}