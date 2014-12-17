package co.mitoo.sashimi.network;

import java.util.HashMap;
import java.util.Map;

import retrofit.RequestInterceptor;

/**
 * Created by david on 14-11-12.
 */
public class InterceptorBuilder {

    private Map<String,String> headerMappings;

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
            initalizeHeaderMapping();
        return headerMappings;
    }

    public void addXAuthToken(String token){
        addHeaderMapping( "X-AUTH-TOKEN" , token);
    }

    private void initalizeHeaderMapping(){
        headerMappings = new HashMap<String,String>();
        headerMappings.put("Content-Type" , "application/json");
    }

    private InterceptorBuilder addHeaderMapping(String key, String value)
    {
        if(headerMappings==null)
            initalizeHeaderMapping();
        headerMappings.put(key,value);
        return this;
    }

    private InterceptorBuilder removeHeaderMapping(String key)
    {
        headerMappings.remove(key);
        return this;
    }
}
