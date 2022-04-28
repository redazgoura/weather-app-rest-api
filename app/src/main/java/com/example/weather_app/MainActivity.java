package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private  static final String key = "3538cac7041f68f66da19b27feb13d40";
    Button btnSearch;
    EditText edtCityName;
    ImageView iconWeather,imageBackground;
    TextView txtTemper,txtCity,txtCondition;
    ListView idLV;


    public void clicked(View view){

        Log.i("TAG", "clicked");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.idBtnSearch);
        edtCityName = findViewById(R.id.idEdtCity);
        iconWeather = findViewById(R.id.idIconWeather);
        txtTemper= findViewById(R.id.idTemperature);
        txtCity = findViewById(R.id.idCityName);
        txtCondition = findViewById(R.id.idCondition);
        imageBackground = findViewById(R.id.idImageBackground);
        idLV = findViewById(R.id.idListView);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String city = edtCityName.getText().toString();
                if(city.isEmpty()){

                    Toast.makeText(MainActivity.this, "Please enter city name !", Toast.LENGTH_SHORT).show();
                }else {
                    //TODO
                    loadWeatherByCityName(city);
                }
            }
        });




    }

    private void loadWeatherByCityName(String city) {
        String apiUrlCity = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+key+"&units=metric";
        Ion.with(this)
                .load(apiUrlCity)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e,JsonObject result) {

                       // Log.d("TAG", result.toString());


                        // do stuff with the result or error
                       if(e != null){
                           e.printStackTrace();
                           Toast.makeText(MainActivity.this, "SERVER ERROR", Toast.LENGTH_SHORT).show();
                       }else {

                           ////convert json response to java
                           JsonObject main = result.get("main").getAsJsonObject();

                           ////temperature
                           double temp = main.get("temp").getAsDouble();
                           txtTemper.setText(temp + "°C");

                           ///city name
                           JsonObject sys = result.get("sys").getAsJsonObject();
                           String cityNameLoc = result.get("name").getAsString();
                           String countr = sys.get("country").getAsString();
                           txtCity.setText(cityNameLoc +", "+countr);


                           //condition wind speed
                           JsonObject wind = result.get("wind").getAsJsonObject();
                           String windSpeed = wind.get("speed").getAsString();
                           txtCondition.setText(windSpeed +" Km/h");

                           /// weather icon
                           JsonArray weather = result.get("weather").getAsJsonArray();
                           String weatherIcon = weather.get(0).getAsJsonObject().get("icon").getAsString();
                           loadIcon(weatherIcon);

                           ///Daily forcast
                           JsonObject coord = result.get("coord").getAsJsonObject();
                           double lat = coord.get("lat").getAsDouble();
                           double lon = coord.get("lon").getAsDouble();
                           loadDailyForCast(lat, lon);

                           ///load backgroundimage
                          /*// int sunrise = sys.get("sunrise").getAsInt();
                           int sunset = sys.get("sunset").getAsInt();
                           Timestamp ts = new Timestamp(sunset);
                           Date d = new Date(ts.getTime());
                           SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                           String dayOrNight = sdf.format(d);
                          // weatherBackgroundImage(dayOrNight);
                           Log.d("TAG",  sdf.format(d));
*/



                       }
                    }
                });
    }


    private void loadDailyForCast(double lat, double lon) {

        String apiUrl = "http://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=hourly,minutely,current,&appid="+key+"&units=metric";
        Ion.with(this)
                .load(apiUrl)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Log.i("TAG", result.toString());

                        List<Weather> weatherList = new ArrayList<>();
                        String timeZone = result.get("timezone").getAsString();
                        JsonArray daily = result.get("daily").getAsJsonArray();

                        for(int i = 1; i < daily.size(); i++){
                            Long dateTime = daily.get(i).getAsJsonObject().get("dt").getAsLong();
                            double temprt = daily.get(i).getAsJsonObject().get("temp").getAsJsonObject().get("day").getAsDouble();
                           // double wind = daily.get(i).getAsJsonObject().get("wind_speed").getAsDouble();
                            String icon = daily.get(i).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").getAsString();
                            weatherList.add(new Weather(dateTime, timeZone, temprt, icon));

                        }

                        //attach adapter to  recycler view
                        DailyWeatherAdapter dailyWeatherAdapter = new DailyWeatherAdapter(MainActivity.this, weatherList);
                        idLV.setAdapter(dailyWeatherAdapter);



                    }
                });
    }


    private void loadIcon(String weatherIcon) {
        // but for brevity, use the ImageView specific builder...
        Ion.with(this)
                .load("http://openweathermap.org/img/wn/"+weatherIcon+".png")
                .intoImageView(iconWeather);
    }


 /*   public  void weatherBackgroundImage(String dayOrNight){

                           if(dayOrNight.equals("AM")){

                               //day
                               Picasso.get().load("https://images.unsplash.com/photo-1597316342034-39cb9003f5bf?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1888&q=80").into(imageBackground);
                           }else{

                               //night
                               Picasso.get().load("https://images.unsplash.com/photo-1584888914138-160eaadbb076?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=327&q=80").into(imageBackground);

                           }
    }*/


}

//https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&exclude=hourly,minutely,current,&appid=key