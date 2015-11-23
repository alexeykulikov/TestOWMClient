package loc.alex.owmsampleapp.rest;

import loc.alex.owmsampleapp.entity.Forecast;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

public interface WeatherService {

    @GET("/data/2.5/forecast")
    Call<Forecast> getWeatherById(@Header("x-api-key") String appid,
                                  @Query("id") int cityId,
                                  @Query("units") String tempUnits);

    @GET("/data/2.5/forecast")
    Call<Forecast> getWeatherByName(@Header("x-api-key") String appid,
                                    @Query("q") String cityName,
                                    @Query("units") String tempUnits);

    @GET("/data/2.5/forecast")
    Call<Forecast> getWeatherByCoords(@Header("x-api-key") String appid,
                                      @Query("lat") double latitude,
                                      @Query("lon") double longitude,
                                      @Query("units") String tempUnits);
}
