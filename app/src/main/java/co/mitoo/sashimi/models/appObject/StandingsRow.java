package co.mitoo.sashimi.models.appObject;
import java.util.List;

/**
 * Created by david on 15-04-20.
 */
public class StandingsRow {

    private int id;
    private List<String> score;

    public StandingsRow(int id, List<String> score) {
        this.id = id;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public List<String> getScore() {
        return score;
    }
}
