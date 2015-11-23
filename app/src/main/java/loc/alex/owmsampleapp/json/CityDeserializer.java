package loc.alex.owmsampleapp.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import loc.alex.owmsampleapp.entity.City;

public class CityDeserializer implements JsonDeserializer<City> {
    @Override
    public City deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        City city = new City();
        JsonObject jsonObject = json.getAsJsonObject();
        city.setId(jsonObject.get("id").getAsInt());
        city.setName(jsonObject.get("name").getAsString());
        city.setCountry(jsonObject.get("country").getAsString());

        JsonObject coord = jsonObject.get("coord").getAsJsonObject();
        city.setLatitude(coord.get("lat").getAsFloat());
        city.setLongitude(coord.get("lon").getAsFloat());

        return city;
    }
}
