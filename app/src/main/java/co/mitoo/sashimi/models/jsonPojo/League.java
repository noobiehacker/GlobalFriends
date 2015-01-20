package co.mitoo.sashimi.models.jsonPojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by david on 14-11-21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class League implements Serializable {
    private boolean claimed;
    private String name;
    private String logo;
    private String cover;
    private String about;
    private String website;
    private String color_1;
    private String color_2;
    private String[] sports;
    private int objectID;
    private _geoLoc _geoloc;

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getColor_1() {
        return color_1;
    }

    public void setColor_1(String color_1) {
        this.color_1 = color_1;
    }

    public String getColor_2() {
        return color_2;
    }

    public void setColor_2(String color_2) {
        this.color_2 = color_2;
    }

    public String[] getSports() {
        return sports;
    }

    public void setSports(String[] sports) {
        this.sports = sports;
    }

    public int getObjectID() {
        return objectID;
    }

    public void setObjectID(int objectID) {
        this.objectID = objectID;
    }

    public _geoLoc get_geoloc() {
        return _geoloc;
    }

    public void set_geoloc(_geoLoc _geoloc) {
        this._geoloc = _geoloc;
    }

}
