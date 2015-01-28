package co.mitoo.sashimi.utils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by david on 15-01-27.
 */
public class ListHelper {

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
}
