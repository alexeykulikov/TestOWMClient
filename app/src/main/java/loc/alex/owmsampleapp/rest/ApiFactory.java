package loc.alex.owmsampleapp.rest;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import loc.alex.owmsampleapp.BuildConfig;
import loc.alex.owmsampleapp.entity.Forecast;
import loc.alex.owmsampleapp.entity.Weather;
import loc.alex.owmsampleapp.json.ForecastDeserializer;
import loc.alex.owmsampleapp.json.WeatherDeserializer;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class ApiFactory {
    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int TIMEOUT = 60;

    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final Gson GSON;

    static {
        CLIENT.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        CLIENT.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        CLIENT.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);
        GSON = new GsonBuilder()
                .registerTypeAdapter(Forecast.class, new ForecastDeserializer())
//                .registerTypeAdapter(City.class, new CityDeserializer())
                .registerTypeAdapter(Weather.class, new WeatherDeserializer())
//                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
    }

    @NonNull
    public static WeatherService getWeatherService() {
        return getRetrofit().create(WeatherService.class);
    }

    @NonNull
    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .client(CLIENT)
                .build();
    }
}
