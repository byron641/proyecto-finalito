package com.example.proyectofinalv2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;
    private Button btnlocalizar;
    private Button btnGuardarId;
    private ImageView imageViewWeather;
    private FusedLocationProviderClient fusedLocationClient;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "add213f2bd92e831f3f46b5c8504c3e8"; // API aún funciona
    private static final int LOCATION_REQUEST_CODE = 101;
    private static final String DEFAULT_CITY_ID = "3589626"; // ID de la ciudad

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.textViewResult);
        btnlocalizar = findViewById(R.id.BtnLocalizar);
        btnGuardarId = findViewById(R.id.BtnGuardarId);
        imageViewWeather = findViewById(R.id.imageViewWeather);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnlocalizar.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            } else {
                getLocation();
            }
        });

        btnGuardarId.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            } else {
                saveLocation();
            }
        });

        // Consulta inicial con ID de ciudad por defecto
        fetchWeatherDataByCityId(DEFAULT_CITY_ID);
    }

    private void getLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            fetchWeatherDataByCoordinates(latitude, longitude);
                        } else {
                            textViewResult.setText("No se pudo obtener la ubicación.");
                        }
                    }
                });
    }

    private void saveLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            saveLocationData(latitude, longitude);
                        } else {
                            textViewResult.setText("No se pudo obtener la ubicación para guardar.");
                        }
                    }
                });
    }

    private void fetchWeatherDataByCoordinates(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        Call<WeatherResponse> call = weatherApi.getWeatherByCoordinates(latitude, longitude, API_KEY, "es");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Código: " + response.code());
                    return;
                }

                WeatherResponse weatherResponse = response.body();
                if (weatherResponse != null) {
                    String content = "";
                    double temperatura = weatherResponse.getMain().getTemp() - 273.15;
                    DecimalFormat df = new DecimalFormat("#.##");
                    content += "La Temperatura actualmente es de " + df.format(temperatura) + " Grados" + "\n";
                    content += "El clima de hoy es " + weatherResponse.getWeather().get(0).getDescription() + "\n";

                    textViewResult.setText(content);

                    // Obtén el código del ícono y construye la URL
                    String iconCode = weatherResponse.getWeather().get(0).getIcon();
                    String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + ".png";

                    // Usa Picasso para cargar la imagen desde la URL
                    Picasso.get().load(iconUrl).into(imageViewWeather);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void fetchWeatherDataByCityId(String cityId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        Call<WeatherResponse> call = weatherApi.getWeather(cityId, API_KEY, "es");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Código: " + response.code());
                    return;
                }

                WeatherResponse weatherResponse = response.body();
                if (weatherResponse != null) {
                    String content = "";
                    double temperatura = weatherResponse.getMain().getTemp() - 273.15;
                    DecimalFormat df = new DecimalFormat("#.##");
                    content += "La Temperatura actualmente es de " + df.format(temperatura) + " Grados" + "\n";
                    content += "El clima de hoy es " + weatherResponse.getWeather().get(0).getDescription() + "\n";

                    textViewResult.setText(content);

                    // Obtén el código del ícono y construye la URL
                    String iconCode = weatherResponse.getWeather().get(0).getIcon();
                    String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + ".png";

                    // Usa Picasso para cargar la imagen desde la URL
                    Picasso.get().load(iconUrl).into(imageViewWeather);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void saveLocationData(double latitude, double longitude) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        boolean isInserted = dbHelper.addLocation(latitude, longitude);
        if (isInserted) {
            textViewResult.setText("Ubicación guardada correctamente.");
        } else {
            textViewResult.setText("Error al guardar la ubicación.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                textViewResult.setText("Permiso de ubicación denegado.");
            }
        }
    }
}