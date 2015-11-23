package loc.alex.owmsampleapp.entity;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("dt_txt")
    private String dateTime;
    @SerializedName("temp")
    private Float temperature;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }


}
