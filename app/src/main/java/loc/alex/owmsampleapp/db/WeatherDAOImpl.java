package loc.alex.owmsampleapp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import loc.alex.owmsampleapp.entity.Weather;

public class WeatherDAOImpl implements WeatherDAO {
    DBHelper helper;

    public WeatherDAOImpl(DBHelper helper) {
        this.helper = helper;
    }

    @Override
    public List<Weather> getAvg(int cityId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(WeatherTable.TABLE_NAME,
                new String[]{"avg(" + WeatherTable.TEMP + ") as temp",
                "date(" + WeatherTable.DT + ") as date"},
                WeatherTable.CT_ID + " = ?",
                new String[] {String.valueOf(cityId)},
                "date(" + WeatherTable.DT + ")", null,
                "date desc", "5");

        int tempIndex = cursor.getColumnIndex("temp");
        int dateIndex = cursor.getColumnIndex("date");
        List<Weather> weatherList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Weather weather = new Weather();
            weather.setTemperature(cursor.getFloat(tempIndex));
            weather.setDateTime(cursor.getString(dateIndex));
            weatherList.add(weather);
        }
        cursor.close();
        db.close();
        return weatherList;
    }

    @Override
    public List<Weather> getByDate(int cityId, String date) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(WeatherTable.TABLE_NAME,
                new String[]{WeatherTable.TEMP, WeatherTable.DT},
                WeatherTable.CT_ID + " = ? and date(" + WeatherTable.DT + ") = date(?)",
                new String[] {String.valueOf(cityId), date},
                null, null,
                WeatherTable.DT);

        int tempIndex = cursor.getColumnIndex(WeatherTable.TEMP);
        int dateIndex = cursor.getColumnIndex(WeatherTable.DT);
        List<Weather> weatherList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Weather weather = new Weather();
            weather.setTemperature(cursor.getFloat(tempIndex));
            weather.setDateTime(cursor.getString(dateIndex));
            weatherList.add(weather);
        }
        cursor.close();
        db.close();
        return weatherList;
    }

    @Override
    public int insertOrUpdate(int cityId, List<Weather> weatherList) {
        SQLiteDatabase db = helper.getWritableDatabase();
        for (Weather weather : weatherList) {
            String dt = weather.getDateTime();
            String where = WeatherTable.CT_ID + " = ? and " + WeatherTable.DT + " = ?";
            String[] whereArgs = new String[]{String.valueOf(cityId), dt};
            Cursor cursor = db.query(WeatherTable.TABLE_NAME,
                    new String[]{WeatherTable.TEMP, WeatherTable.DT},
                    where, whereArgs,
                    null, null,
                    WeatherTable.DT);
            if (cursor.getCount() > 0) {
                ContentValues values = new ContentValues(1);
                values.put(WeatherTable.TEMP, weather.getTemperature());
                db.update(WeatherTable.TABLE_NAME, values, where, whereArgs);
            }
            else {
                ContentValues values = new ContentValues(3);
                values.put(WeatherTable.TEMP, weather.getTemperature());
                values.put(WeatherTable.DT, weather.getDateTime());
                values.put(WeatherTable.CT_ID, cityId);
                db.insert(WeatherTable.TABLE_NAME, null, values);
            }
            cursor.close();
        }
        db.close();
        return 0;
    }
}
