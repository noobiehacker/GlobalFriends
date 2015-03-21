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
}
