package co.mitoo.sashimi.utils;
import co.mitoo.sashimi.models.LeagueModel;
import co.mitoo.sashimi.models.MitooModel;
import co.mitoo.sashimi.models.SessionModel;
import co.mitoo.sashimi.views.activities.MitooActivity;

/**
 * Created by david on 15-01-21.
 */
public class ModelRetriever {

    private MitooActivity activity;

    public ModelRetriever(MitooActivity activity) {
        this.activity = activity;
    }

    public MitooActivity getMitooActivity() {
        return activity;
    }

    public void setActivity(MitooActivity activity) {
        this.activity = activity;
    }

    public LeagueModel getLeagueModel() {

        LeagueModel leagueModel = null;
        MitooModel model = getMitooActivity().getModel(LeagueModel.class);
        if (model != null) {
            leagueModel = (LeagueModel) model;
        }
        return leagueModel;
    }

    public SessionModel getUserModel() {

        SessionModel sessionModel = null;
        MitooModel model = getMitooActivity().getModel(SessionModel.class);
        if (model != null) {
            sessionModel = (SessionModel) model;
        }
        return sessionModel;
    }
}
