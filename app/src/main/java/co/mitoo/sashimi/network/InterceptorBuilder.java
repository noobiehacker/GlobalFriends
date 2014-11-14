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
                for(String key : headerMappings.keySet()){
                    request.addHeader(key, headerMappings.get(key));
                };
            }
        };
        return requestInterceptor;
    }

    private void initalizeHeaderMapping(){
        headerMappings = new HashMap<String,String>();
        headerMappings.put("Content-Type" , "application/json");
    }

    public InterceptorBuilder addHeaderMapping(String key, String value)
    {
        if(headerMappings==null)
            initalizeHeaderMapping();
        headerMappings.put(key,value);
        return this;
    }

    public InterceptorBuilder removeHeaderMapping(String key)
    {
        headerMappings.remove(key);
        return this;
    }
}
