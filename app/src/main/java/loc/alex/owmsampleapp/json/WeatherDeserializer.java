package loc.alex.owmsampleapp.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import loc.alex.owmsampleapp.entity.Weather;

public class WeatherDeserializer implements JsonDeserializer<Weather> {
    @Override
    public Weather deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Weather weather = new Weather();
        JsonObject jsonObject = json.getAsJsonObject();
        String dateTime = jsonObject.get("dt_txt").getAsString();
        weather.setDateTime(dateTime);
        JsonObject main = jsonObject.getAsJsonObject("main");
        weather.setTemperature(main.get("temp").getAsFloat());
        return weather;
    }
}
