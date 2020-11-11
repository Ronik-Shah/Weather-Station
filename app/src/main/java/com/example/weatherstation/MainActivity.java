package com.example.weatherstation;

import android.content.Context;


import android.content.DialogInterface;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherstation.models.RecyclerViewAdapter;
import com.example.weatherstation.models.WeatherData;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    static private final long ANIM_DURATION = 1500;
    EditText city;
    private ArrayList<WeatherData> info;
    private String weatherType = "";
    private String weatherIcon = "";

    public class PageDownloader extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            StringBuffer api = new StringBuffer();
            HttpURLConnection connection;
            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                int data = isr.read();
                while (data >= 0) {
                    api.append((char) data);
                    data = isr.read();
                }
                return api.toString();
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.main_layout), "Please write a correct city.", Snackbar.LENGTH_SHORT).show();
                return "MALFORMED URL!";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            info.clear();
            weatherType = "";
            weatherIcon = "";
            try {
                JSONObject weather = new JSONObject(s);
                JSONArray weatherArray = new JSONArray(new JSONObject(s).getString("weather"));
                for (int i = 0; i < weatherArray.length(); i++) {
                    weatherType = weatherArray.getJSONObject(i).getString("main");
                    weatherIcon = weatherArray.getJSONObject(i).getString("icon");
                }

                JSONObject main = new JSONObject(weather.getString("main"));
                Object tempMin = main.get("temp_min");
                Object tempMax = main.get("temp_max");
                Object humidity = main.get("humidity");
                info.add(new WeatherData("Minimum Temperature",String.format("%.2f", (Float.parseFloat(tempMin.toString()) - 273.15)) + "\u00B0C", R.drawable.minimum_temperature));
                info.add(new WeatherData("Maximum Temperature",String.format("%.2f", (Float.parseFloat(tempMax.toString()) - 273.15)) + "\u00B0C", R.drawable.maximum_temperature));
                info.add(new WeatherData("Humidity", humidity.toString() + "%", R.drawable.humidity));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            manageViews();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = new ArrayList<>();
        city = findViewById(R.id.city_edit_text);
        city.setOnClickListener(this);
    }

    public void showWeather(){
        city.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    PageDownloader api = new PageDownloader();
                    try {
                        String encoded = URLEncoder.encode(city.getText().toString(),"UTF-8");
                        api.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encoded +"&appid=b53960bd1e5f78033e3e8fed2f11bbc0").get();
                    }catch (Exception e){
                        Snackbar.make(findViewById(R.id.main_layout),"Sorry an error occurred", Snackbar.LENGTH_SHORT).show();
                    }
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(city.getWindowToken(),0);
                }
                return false;
            }
        });
    }

    protected void manageViews(){
        RecyclerView information = findViewById(R.id.recycler_view);
        RecyclerViewAdapter la = new RecyclerViewAdapter(info);
        information.setLayoutManager(new LinearLayoutManager(this));
        information.setAdapter(la);
        setWeatherMainInfo();
    }

    protected void setWeatherMainInfo(){
        ImageView weatherImage = findViewById(R.id.weather_image);
        int imageResource = 0;
        switch (weatherIcon){
            case "01d":
                imageResource = R.drawable.day;
                break;
            case "01n":
                imageResource = R.drawable.night;
                break;
            case "02d":
                imageResource = R.drawable.sunny_cloudy;
                break;
            case "02n":
                imageResource = R.drawable.cloudy_night;
                break;
            case "03d":
            case "03n":
                imageResource = R.drawable.cloudy;
                break;
            case "04d":
            case "04n":
                imageResource = R.drawable.double_clouds;
                break;
            case "09d":
            case "09n":
            case "10d":
            case "10n":
                imageResource = R.drawable.rain;
                break;
            case "11d":
            case "11n":
                imageResource = R.drawable.thunder_strom;
                break;
            case "13d":
            case "13n":
                imageResource = R.drawable.snow;
                break;
            case "50d":
            case "50n":
                imageResource = R.drawable.mist;
                break;
        }
        weatherImage.setAlpha(0f);
        weatherImage.setImageResource(imageResource);
        weatherImage.animate().alpha(1).setDuration(ANIM_DURATION);
        TextView textView = findViewById(R.id.weather_text);
        textView.setAlpha(0f);
        textView.setText(weatherType);
        TextView placeHolderText = findViewById(R.id.place_holder_text);
        placeHolderText.animate().alpha(0).setDuration(ANIM_DURATION);
        textView.animate().alpha(1).setDuration(ANIM_DURATION);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.city_edit_text){
            showWeather();
        }
    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        dialog.setTitle(R.string.exit_title);
        dialog.setMessage(R.string.exit_content);
        dialog.setIcon(R.drawable.baseline_warning_black_48dp);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }
}
