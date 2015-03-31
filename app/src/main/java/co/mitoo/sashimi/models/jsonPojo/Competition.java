package co.mitoo.sashimi.models.jsonPojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by david on 14-11-21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Competition  implements Serializable {
    private int id;
    private int fixed_competition_id;
    private boolean current_season;
    private String name;
    private String sport;
    private String level;
    private String days;
    private String team_price;
    private String player_price;
    private String season_name;
    private String start_date;
    private String end_date;
    private String reg_open_date;
    private String reg_close_date;
    private League league;
    private location location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFixed_competition_id() {
        return fixed_competition_id;
    }

    public void setFixed_competition_id(int fixed_competition_id) {
        this.fixed_competition_id = fixed_competition_id;
    }

    public boolean isCurrent_season() {
        return current_season;
    }

    public void setCurrent_season(boolean current_season) {

        this.current_season = current_season;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTeam_price() {
        return team_price;
    }

    public void setTeam_price(String team_price) {
        this.team_price = team_price;
    }

    public String getPlayer_price() {
        return player_price;
    }

    public void setPlayer_price(String player_price) {
        this.player_price = player_price;
    }

    public String getSeason_name() {
        return season_name;
    }

    public void setSeason_name(String season_name) {
        this.season_name = season_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getReg_open_date() {
        return reg_open_date;
    }

    public void setReg_open_date(String reg_open_date) {
        this.reg_open_date = reg_open_date;
    }

    public String getReg_close_date() {
        return reg_close_date;
    }

    public void setReg_close_date(String reg_close_date) {
        this.reg_close_date = reg_close_date;
    }

    public location getLocation() {
        return location;
    }

    public void setLocation(location location) {
        this.location = location;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }
}

