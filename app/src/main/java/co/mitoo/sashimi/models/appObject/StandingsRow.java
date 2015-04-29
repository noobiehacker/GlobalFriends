package co.mitoo.sashimi.models.appObject;
import java.util.Map;

/**
 * Created by david on 15-04-20.
 */
public class StandingsRow {

    private int id;
    private Map<String, String> dataMap;

    public StandingsRow(int id, Map<String, String> dataMap) {
        this.id = id;
        this.dataMap = dataMap;
    }
/*
    public List<String> getRowData(){
      /*  List<String> result = new ArrayList<String>();
        for(String item : StandingsRow.cols){
            Object data = getDataFromMap(item);
            if(data!=null)
                result.add((String)data);
        }
        return result;
    }
*/
    public String getDataFromMap(String key){
        String result = "";
        Object data = this.dataMap.get(key);
        if(data!=null)
            result =(String)data;
        return result;
    }

    public int getId() {
        return id;
    }
}
