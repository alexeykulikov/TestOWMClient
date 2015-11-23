package loc.alex.owmsampleapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    private static final String CREATION_REQUEST =
            "CREATE TABLE " + WeatherTable.TABLE_NAME + " (" +
                    WeatherTable.ID + " INTEGER PRIMARY KEY," +
                    WeatherTable.DT + " TEXT," +
                    WeatherTable.TEMP + " REAL," +
                    WeatherTable.CT_ID + " INTEGER)";
    private static final String DROP_REQUEST =
            "DROP TABLE IF EXISTS " + WeatherTable.TABLE_NAME;

    private static final String DATABASE_NAME = "owm.db";

    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATION_REQUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_REQUEST);
        onCreate(db);
    }
}
