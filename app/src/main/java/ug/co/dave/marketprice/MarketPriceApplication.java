package ug.co.dave.marketprice;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by dave on 12/20/2014.
 */
public class MarketPriceApplication extends Application {

    private INetworkService networkService;

    private Gson gson;

    public static final String BASE_URL = "http://10.0.2.2/MarketPrices/api/";

    public static final String TAG = MarketPriceApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        //initialize the json element..
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();


        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(60*1000, TimeUnit.MILLISECONDS);

        //instatitating the RestAdapter...
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(client))
                .build();
        networkService = restAdapter.create(INetworkService.class);

    }

    public Gson getGsonApplication(){
        return this.gson;
    }

    public INetworkService getNetworkService(){
        return this.networkService;
    }
}
