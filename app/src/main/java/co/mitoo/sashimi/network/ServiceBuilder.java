package co.mitoo.sashimi.network;

import com.squareup.okhttp.OkHttpClient;
import co.mitoo.sashimi.utils.MitooConstants;
import co.mitoo.sashimi.utils.StaticString;
import retrofit.MockRestAdapter;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

public class ServiceBuilder {

    private String end_point;
    private InterceptorBuilder interceptorBuilder;
    private MockRestAdapter mockRestAdapter;
    private RestAdapter restAdapter;
    private SteakApi steakApiService;
    private static ServiceBuilder singleTonInstance;
    
    private ServiceBuilder(){
        initializeFields();
    }

    public static ServiceBuilder getSingleTonInstance(){
        if(ServiceBuilder.singleTonInstance==null){
            ServiceBuilder.singleTonInstance= new ServiceBuilder();
        }
        return ServiceBuilder.singleTonInstance;
    }
    
    public ServiceBuilder setEndPoint(String end_point) {
        this.end_point = end_point;
        return this;
    }

    public ServiceBuilder setXAuthToken(String token) {
        getInterceptorBuilder().addXAuthToken(token);
        return this;
    }

    public ServiceBuilder resetXAuthToken() {
        getInterceptorBuilder().resetXAuthToken();
        return this;
    }
    
    public <T> T create(Class<T> service) {
        checkSettingsForErrors(service);
        return this.buildAdapter().create(service);
    }

    public <T> T create(Class<T> service, T mockClass) {
        setMockRestAdapter(MockRestAdapter.from(this.buildAdapter()));
        return getMockRestAdapter().create(service, mockClass);
    }

    private RestAdapter buildAdapter() {

        restAdapter= new RestAdapter.Builder()
                .setRequestInterceptor(getInterceptorBuilder().Builder())
                .setClient(getHttpClient())
                .setConverter(new JacksonConverter())
                .setEndpoint(this.end_point)
                .setErrorHandler(new SteakErrorHandler())
                .build();
        return restAdapter;
    }

    private Client getHttpClient() {
        OkHttpClient httpClient = new OkHttpClient();
        return new OkClient(httpClient);
    }

    private <T> void checkSettingsForErrors(Class<T> service) {
        if (end_point == null) {
            throw new IllegalArgumentException("Endpoint may not be null.");
        } else if (service == null)
            throw new IllegalArgumentException("Service may not be null.");
    }

    public InterceptorBuilder getInterceptorBuilder() {
        if (interceptorBuilder == null)
            interceptorBuilder = new InterceptorBuilder();
        return interceptorBuilder;
    }

    public MockRestAdapter getMockRestAdapter() {
        return mockRestAdapter;
    }

    private void setMockRestAdapter(MockRestAdapter mockRestAdapter) {
        this.mockRestAdapter = mockRestAdapter;
    }

    public SteakApi getSteakApiService() {
        if(steakApiService==null)
            steakApiService = this.create(SteakApi.class);
        return steakApiService;
    }
    
    public void setSteakApiService(SteakApi steakApiService) {
        this.steakApiService = steakApiService;
    }

    public void rebuildService(){
        
        setSteakApiService(this.create(SteakApi.class));

    }
    
    private void initializeFields() {

        switch (MitooConstants.getEndPoint()) {
            case PRODUCTION:
                this.setEndPoint(StaticString.steakProductionEndPoint);
                break;
            case STAGING:
                this.setEndPoint(StaticString.steakStagingEndPoint);
                break;
            case APIARY:
                this.setEndPoint(StaticString.steakApiaryEndPoint);
                break;
            case LOCALHOST:
                this.setEndPoint(StaticString.steakLocalEndPoint);
                break;
        }

    }
}


