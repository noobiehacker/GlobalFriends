package co.mitoo.sashimi.network;

import java.util.HashMap;
import java.util.Map;

import co.mitoo.sashimi.BuildConfig;
import retrofit.RequestInterceptor;

/**
 * Created by david on 14-11-12.
 */
public class InterceptorBuilder {

    private Map<String,String> headerMappings;
    private String token;

    public InterceptorBuilder(){
    }

    public RequestInterceptor Builder(){

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                Map<String, String> map = getHeaderMappings();
                for(String key : map.keySet()){
                    request.addHeader(key, map.get(key));
                };
            }
        };
        return requestInterceptor;
    }

    public Map<String, String> getHeaderMappings() {
        if(headerMappings==null)
            initializeHeaderMapping();
        return headerMappings;
    }

    public void addXAuthToken(String token){
        addHeaderMapping( "X-AUTH-TOKEN" , token);
    }

    public void resetXAuthToken(){
        removeHeaderMapping( "X-AUTH-TOKEN" );
    }
    
    private void initializeHeaderMapping(){
        headerMappings = new HashMap<String,String>();
        headerMappings.put("Content-Type" , "application/json");
        headerMappings.put("X-APP-VERSION" , BuildConfig.VERSION_NAME);
        headerMappings.put("X-PLATFORM" , "Android");
    }

    private InterceptorBuilder addHeaderMapping(String key, String value){

        if(headerMappings==null)
            initializeHeaderMapping();
        headerMappings.put(key,value);
        return this;
    }

    private InterceptorBuilder removeHeaderMapping(String key){

        headerMappings.remove(key);
        return this;
    }

    public String getToken(){
        return getHeaderMappings().get("X-AUTH-TOKEN");
    }
}
