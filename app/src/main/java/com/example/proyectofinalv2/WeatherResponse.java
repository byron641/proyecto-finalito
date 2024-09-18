package com.example.proyectofinalv2;

import java.util.List;

public class WeatherResponse {
    private List<Weather> weather;
    private Main main;

    public List<Weather> getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }

    public class Weather {
        private String description;
        private String icon; //

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon; //
        }
    }

    public class Main {
        private float temp;

        public float getTemp() {
            return temp;
        }
    }
}