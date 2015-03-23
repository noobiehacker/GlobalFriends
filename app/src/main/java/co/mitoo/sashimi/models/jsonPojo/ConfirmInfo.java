package co.mitoo.sashimi.models.jsonPojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

import co.mitoo.sashimi.models.jsonPojo.recieve.UserInfoRecieve;

/**
 * Created by david on 15-03-20.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmInfo implements Serializable {

    private UserInfoRecieve user;
    private League league;
    private Competition[] competition_seasons;
    private String identifier_used;

    public UserInfoRecieve getUser() {
        return user;
    }

    public void setUser(UserInfoRecieve user) {
        this.user = user;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Competition[] getCompetition_seasons() {
        return competition_seasons;
    }

    public void setCompetition_seasons(Competition[] competition_seasons) {
        this.competition_seasons = competition_seasons;
    }

    public String getIdentifier_used() {
        return identifier_used;
    }

    public void setIdentifier_used(String identifier_used) {
        this.identifier_used = identifier_used;
    }
}
