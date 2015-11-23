package loc.alex.owmsampleapp.entity;

import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("country")
    private String country;
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lon")
    private double longitude;
//    @SerializedName("coord")
//    private Coords coords;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
//    public Coords getCoords() {
//        return coords;
//    }
//
//    public void setCoords(Coords coords) {
//        this.coords = coords;
//    }
//
//    public static class Coords {
//        @SerializedName("lat")
//        private double latitude = 0.0;
//        @SerializedName("lon")
//        private double longitude = 0.0;
//
//        public double getLatitude() {
//            return latitude;
//        }
//
//        public void setLatitude(double latitude) {
//            this.latitude = latitude;
//        }
//
//        public double getLongitude() {
//            return longitude;
//        }
//
//        public void setLongitude(double longitude) {
//            this.longitude = longitude;
//        }
//    }
}
