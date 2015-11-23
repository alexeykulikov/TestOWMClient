package loc.alex.owmsampleapp.ui;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import loc.alex.owmsampleapp.rest.ApiFactory;
import loc.alex.owmsampleapp.R;
import loc.alex.owmsampleapp.rest.WeatherService;
import loc.alex.owmsampleapp.db.DBHelper;
import loc.alex.owmsampleapp.db.WeatherDAO;
import loc.alex.owmsampleapp.db.WeatherDAOImpl;
import loc.alex.owmsampleapp.db.WeatherTable;
import loc.alex.owmsampleapp.entity.Forecast;
import loc.alex.owmsampleapp.entity.Weather;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, Callback<Forecast>,
        SettingsFragment.OnFragmentInteractionListener {
    private static final String CITY_ID = "city_id";
    private static final String APP_ID = "app_id";
    private static final String DETALIZATION = "detalization";
    private static final String D_DATE = "d_date";

    @Bind(R.id.swipe_container)SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.lvWeather)ListView lvWeather;
    WeatherService service;
    WeatherDAO dao;
    private List<Map<String, Object>> itemList = new ArrayList<>();
    private BaseAdapter adapter;
    private boolean detalization = false;
    private String dDate;
    private ActionBar actionBar;
    private Integer cityId;
    private String appId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        service = ApiFactory.getWeatherService();
        dao = new WeatherDAOImpl(new DBHelper(this));

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(this);


        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // формируем столбцы сопоставления
        String[] from = new String[] {WeatherTable.DT, WeatherTable.TEMP };
        int[] to = new int[] { R.id.tvDate, R.id.tvTemp };

        // создааем адаптер и настраиваем список
        adapter = new SimpleAdapter(this, itemList, R.layout.item, from, to);
        lvWeather.setAdapter(adapter);
    }

    @OnItemClick(R.id.lvWeather)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!detalization) {
            TextView textView = (TextView) view.findViewById(R.id.tvDate);
            dDate = textView.getText().toString();
            detalization = true;
            updateListView();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                actionBar.setDisplayHomeAsUpEnabled(false);
                dDate = null;
                detalization = false;
                updateListView();
                return true;
            case R.id.action_settings:
                SettingsFragment dialog = SettingsFragment.newInstance(cityId, appId);
                dialog.show(getFragmentManager(), "SettingsFragment");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        Call<Forecast> call = service.getWeatherById(appId, cityId, "metric");
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<Forecast> response, Retrofit retrofit) {
        if (response.isSuccess()) {
            Forecast forecast = response.body();
            final int cityId = forecast.getCityId();
            final List<Weather> weatherList = forecast.getWeatherList();
            AsyncTask<Void, Void, Void> save2DB = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    dao.insertOrUpdate(cityId, weatherList);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    updateListView();
                }
            };
            save2DB.execute();
        } else {
            Toast.makeText(this, response.message(), Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onFailure(Throwable t) {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
        t.printStackTrace();
        Log.e("NetError", t.getMessage());
    }

    private void updateListView() {
        AsyncTask<Void, Void, List<Weather>> loadFromBD = new AsyncTask<Void, Void, List<Weather>>() {
            @Override
            protected List<Weather> doInBackground(Void... params) {
                List<Weather>  weatherList = detalization ? dao.getByDate(cityId, dDate) : dao.getAvg(cityId);
                return weatherList;
            }

            @Override
            protected void onPostExecute(List<Weather> weatherList) {
                itemList.clear();
                for (Weather weather :
                        weatherList) {
                    Map<String, Object> item = new HashMap<>();
                    String dt = weather.getDateTime();
                    item.put(WeatherTable.DT, dt);
                    item.put(WeatherTable.TEMP, String.format("%.0f°C", weather.getTemperature()));
                    itemList.add(item);
                }
                adapter.notifyDataSetChanged();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        };
        loadFromBD.execute();
    }

    private String genNewTitle() {
        return String.format("%s: %s", getString(R.string.app_name),
                SettingsFragment.cityNames[Arrays.asList(SettingsFragment.cityIds).indexOf(cityId)]);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        cityId = preferences.getInt(CITY_ID, 2643743);
        appId = preferences.getString(APP_ID, "2de143494c0b295cca9337e1e96b00e0");
        detalization = preferences.getBoolean(DETALIZATION, false);
        dDate = preferences.getString(D_DATE, null);
        if (detalization) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(genNewTitle());
        updateListView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(CITY_ID, cityId);
        editor.putString(APP_ID, appId);
        editor.putBoolean(DETALIZATION, detalization);
        editor.putString(D_DATE, dDate);
        editor.apply();
    }

    @Override
    public void onFragmentInteraction(Integer cityId, String appId) {
        this.cityId = cityId;
        this.appId = appId;
        setTitle(genNewTitle());
        updateListView();
    }

}
