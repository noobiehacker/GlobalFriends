package co.mitoo.sashimi.models.jsonPojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by david on 15-03-10.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team implements Serializable {

    private int id;
    private String name;
    private String sport;
    private String color_1;
    private String color_2;
    private String logo;
    private String logo_large;
    private String logo_medium;
    private String logo_small;
    private String logo_thumb;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo_large() {
        return logo_large;
    }

    public void setLogo_large(String logo_large) {
        this.logo_large = logo_large;
    }

    public String getLogo_medium() {
        return logo_medium;
    }

    public void setLogo_medium(String logo_medium) {
        this.logo_medium = logo_medium;
    }

    public String getLogo_small() {
        return logo_small;
    }

    public void setLogo_small(String logo_small) {
        this.logo_small = logo_small;
    }

    public String getLogo_thumb() {
        return logo_thumb;
    }

    public void setLogo_thumb(String logo_thumb) {
        this.logo_thumb = logo_thumb;
    }
}
