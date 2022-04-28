package com.example.weather_app;

public class Weather {

    private long date;
    private String timeZone;
    private double tempr;
    private String icon;



    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public double getTempr() {
        return tempr;
    }

    public void setTempr(double tempr) {
        this.tempr = tempr;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    ///constructor for temperature and icon
    public Weather(Long dateTime, double tempr, String icon) {
        this.tempr = tempr;
        this.icon = icon;
    }

    public Weather(long date, String timeZone, double tempr, String icon) {
        this.date = date;
        this.timeZone = timeZone;
        this.tempr = tempr;
        this.icon = icon;
    }
}
