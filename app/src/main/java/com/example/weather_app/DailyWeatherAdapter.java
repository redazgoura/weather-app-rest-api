package com.example.weather_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.koushikdutta.ion.Ion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DailyWeatherAdapter extends ArrayAdapter<Weather> {

    private Context context;
    private List<Weather> weatherList;
    public DailyWeatherAdapter(@NonNull Context context,  @NonNull List<Weather> weatherList) {
        super(context, 0, weatherList);

        this.context = context;
        this.weatherList = weatherList;

    }

    //convert item_forcast xml to a view
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //convert item_forcast to a recycler view
        convertView = LayoutInflater.from(context).inflate(R.layout.item_forcast, parent, false);

        TextView txtTime = convertView.findViewById(R.id.idTxtTime);
        TextView txtTemp = convertView.findViewById(R.id.idTxtTemperature);
        //TextView txtWind = convertView.findViewById(R.id.idTxtWind);
        ImageView iconWeath = convertView.findViewById(R.id.idImageCondition);

        //to get the current weather from the weatherList we use position
        Weather weather = weatherList.get(position);

        //load temperature
        txtTemp.setText(weather.getTempr()+"°C");

        ///load image
        Ion.with(context)
                .load("http://openweathermap.org/img/wn/"+weather.getIcon()+".png")
                .intoImageView(iconWeath);

        //date
        Date d = new Date(weather.getDate() * 1000);//convert to millisecs
        DateFormat dateFormat = new SimpleDateFormat("EEE,MM,yy", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone(weather.getTimeZone()));
        txtTime.setText(dateFormat.format(d));

        return convertView;
    }


}
