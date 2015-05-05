package co.mitoo.sashimi.models.jsonPojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by david on 15-05-04.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCheck implements Serializable {

    private int id;
    private String name;
    private String confirmed_at;
    private boolean has_email;
    private boolean has_phone;
    private String picture;
    private String picture_large;
    private String picture_mediim;
    private String picture_small;
    private String picture_thumb;

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

    public String getConfirmed_at() {
        return confirmed_at;
    }

    public void setConfirmed_at(String confirmed_at) {
        this.confirmed_at = confirmed_at;
    }

    public boolean isHas_email() {
        return has_email;
    }

    public void setHas_email(boolean has_email) {
        this.has_email = has_email;
    }

    public boolean isHas_phone() {
        return has_phone;
    }

    public void setHas_phone(boolean has_phone) {
        this.has_phone = has_phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture_large() {
        return picture_large;
    }

    public void setPicture_large(String picture_large) {
        this.picture_large = picture_large;
    }

    public String getPicture_mediim() {
        return picture_mediim;
    }

    public void setPicture_mediim(String picture_mediim) {
        this.picture_mediim = picture_mediim;
    }

    public String getPicture_small() {
        return picture_small;
    }

    public void setPicture_small(String picture_small) {
        this.picture_small = picture_small;
    }

    public String getPicture_thumb() {
        return picture_thumb;
    }

    public void setPicture_thumb(String picture_thumb) {
        this.picture_thumb = picture_thumb;
    }
}
