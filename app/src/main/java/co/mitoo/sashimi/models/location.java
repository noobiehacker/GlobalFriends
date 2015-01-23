package co.mitoo.sashimi.models;

/**
 * Created by david on 15-01-22.
 */
public class location {

    public int id;
    public String title;
    public String street_1;
    public String street_2;
    public String city;
    public String state;
    public String postal_code;
    public String country;
    public double lat;
    public double lng;
    public String created_at;
    public String updated_at;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
