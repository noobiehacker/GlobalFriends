package co.mitoo.sashimi.utils;

/**
 * Created by david on 15-01-22.
 */
public interface IsPersistable {

    public void readData();

    public void saveData() ;

    public void deleteData();

    public String getPreferenceKey();
    
}
