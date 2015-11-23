package loc.alex.owmsampleapp.db;

import java.util.List;

import loc.alex.owmsampleapp.entity.Weather;

public interface WeatherDAO {
    List<Weather> getAvg(int cityId);
    List<Weather> getByDate(int cityId, String date);
    int insertOrUpdate(int cityId, List<Weather> weatherList);

}
