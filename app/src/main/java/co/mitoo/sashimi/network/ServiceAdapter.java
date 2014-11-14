package co.mitoo.sashimi.network;



import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

public class ServiceAdapter {

    public ServiceAdapter(){

    }

    public static class Builder{

        private String end_point;
        private RestAdapter adapter;

        public Builder(){

        }
        public Builder setEndPoint(String end_point){
            this.end_point=end_point;
            return this;
        }

        public <T> T create(Class<T> service) {
            if ( end_point == null ) {
                throw new IllegalArgumentException("Endpoint may not be null.");
            }
            else if ( service == null )
                throw new IllegalArgumentException("Service may not be null.");
            return  adapter.create(service);
        }

        public RestAdapter build() {

            adapter =  new RestAdapter.Builder()
                        .setRequestInterceptor(new InterceptorBuilder().Builder())
                        .setClient(getHttpClient())
                        .setConverter(new JacksonConverter())
                        .setEndpoint(this.end_point)
                        .build();
            return adapter;
        }

        private Client getHttpClient() {
            OkHttpClient httpClient = new OkHttpClient();
            return new OkClient(httpClient);
        }
    }
}


