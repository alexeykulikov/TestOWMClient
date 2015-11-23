package loc.alex.owmsampleapp.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import loc.alex.owmsampleapp.entity.Forecast;
import loc.alex.owmsampleapp.entity.Weather;

public class ForecastDeserializer implements JsonDeserializer<Forecast> {
    @Override
    public Forecast deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Forecast forecast = new Forecast();
        JsonObject rootObject = json.getAsJsonObject();
        JsonObject city = rootObject.get("city").getAsJsonObject();
        forecast.setCityId(city.get("id").getAsInt());

        int cnt = rootObject.get("cnt").getAsInt();
        List<Weather> weatherList = new ArrayList<>(cnt);
        JsonArray list = rootObject.getAsJsonArray("list");
        for (JsonElement weather : list) {
            weatherList.add((Weather) context.deserialize(weather, Weather.class));
        }
        forecast.setWeatherList(weatherList);
        return forecast;
    }
}
