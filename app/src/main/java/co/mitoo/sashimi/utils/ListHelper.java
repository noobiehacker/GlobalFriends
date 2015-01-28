package co.mitoo.sashimi.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.models.jsonPojo.Sport;

/**
 * Created by david on 15-01-27.
 */
public class ListHelper {
    
    private Context context;

    public ListHelper(Context context) {
        this.context = context;
    }

    public <T> void addToListList(List<T> container ,List<T> additionList){
        for(T item : additionList){
            container.add(item);
        }
    }

    public <T> void clearList(List<T> result){
        if(result!=null){
            Iterator<T> iterator = result.iterator();
            while(iterator.hasNext()){
                iterator.next();
                iterator.remove();
            }
        }
    }

    public List<IsSearchable> getSports() {

        ArrayList<IsSearchable> returnList = new ArrayList<IsSearchable>();
        String[] sportsArray = getContext().getResources().getStringArray(R.array.sports_array);
        for (String item : sportsArray) {
            returnList.add(new Sport(item));
        }
        return returnList;

    }

    public List<IsSearchable> getSports(String prefix) {

        ArrayList<IsSearchable> returnList = new ArrayList<IsSearchable>();
        String[] sportsArray = getContext().getResources().getStringArray(R.array.sports_array);
        for (String item : sportsArray) {
            if (item.toLowerCase().startsWith(prefix))
                returnList.add(new Sport(item));
        }
        return returnList;

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
