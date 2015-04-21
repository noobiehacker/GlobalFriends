package co.mitoo.sashimi.models.appObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.mitoo.sashimi.models.jsonPojo.recieve.standings.SteakStandings;
import co.mitoo.sashimi.utils.MitooConstants;

/**
 * Created by david on 15-04-20.
 */
public class MitooStandings implements Comparable<MitooStandings>{

    private static int[] series;
    private static String[] cols;
    private static Map<String, Map<String, String>> data;
    private static MitooStandings mitooStandingsHead;

    private int id;
    private Map<String, String> dataMap;
    private boolean head = false;

    public MitooStandings(int id){
        setId(id);
        String hash="";
        if(id== MitooConstants.standingHead){
            setHead(true);
            hash = "head";
        }else{
            hash = Integer.toString(getId());
        }
        setDataMap(MitooStandings.data.get(hash));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }

    public boolean isHead() {
        return head;
    }

    public void setHead(boolean head) {
        this.head = head;
    }

    public static void setUpClassData(SteakStandings steakStandings){
        if(steakStandings!=null){
            MitooStandings.series=steakStandings.getSeries();
            MitooStandings.cols=steakStandings.getCols();
            MitooStandings.data=steakStandings.getData();
        }

    }

    @Override
    public int compareTo(MitooStandings another) {

        int position = getPositionInArray(getId());
        int anotherPosition = another.getPositionInArray(another.getId());
        return position-anotherPosition;

    }

    private int getPositionInArray(int id){

        int result = MitooConstants.invalidConstant;
        loop:
        for(int i = 0 ; i< series.length ; i++){
            if(series[i] == id){
                result= i;
                break loop;
            }
        }
        return result;
    }

    public List<String> getRowData(){
        List<String> result = new ArrayList<String>();
        for(String item : MitooStandings.cols){
            Object data = getDataFromMap(item);
            if(data!=null)
                result.add((String)data);
        }
        return result;
    }

    public String getDataFromMap(String key){
        String result = "";
        Object data = getDataMap().get(key);
        if(data!=null)
            result =(String)data;
        return result;
    }

    public static MitooStandings getHead() {
        if(MitooStandings.mitooStandingsHead == null){
            mitooStandingsHead = new MitooStandings(MitooConstants.standingHead);
        }
        return MitooStandings.mitooStandingsHead;
    }


}
