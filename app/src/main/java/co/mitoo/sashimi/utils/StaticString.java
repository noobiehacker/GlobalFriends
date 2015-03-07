package co.mitoo.sashimi.utils;

/**
 * Created by david on 14-12-02.
 */

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by david on 10/27/2014.
 */
public class StaticString {

    private static StaticString singleton_staticString;
    public static String steakLocalEndPoint = getString("localEndPoint");
    public static String steakApiaryEndPoint = getString("apiaryEndPoint");
    public static String steakStagingEndPoint = getString("stagingEndPoint");
    public static String steakProductionEndPoint = getString("productionEndPoint");
    public static String testPassword = getString("password");
    public static String errorMessage = getString("errorMessage");
    public static String apiConstantRegister = getString("apiConstantRegister");
    
    private Properties properties;

    public static String getString(String key){
        String result;
        try{
            result = StaticString.getInstance().getProperties().getProperty(key);
        }
        catch(Exception e){
            result =  "No Key Found";
        }
        return result;
    }

    private static StaticString getInstance() {
        if(StaticString.singleton_staticString==null)
            StaticString.singleton_staticString = new StaticString();
        return StaticString.singleton_staticString;
    }

    private Properties getProperties() {
        if(properties==null)
            initializeProperties();
        return properties;
    }

    private void initializeProperties(){
        try{
            properties = new Properties();
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("String.properties");
            properties.load(in);
        }
        catch(Exception e)
        {

        }
    }
}
