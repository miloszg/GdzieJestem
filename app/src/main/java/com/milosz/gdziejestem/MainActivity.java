package com.milosz.gdziejestem;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocation(location);
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location ostatniaZnanaLokacja = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(ostatniaZnanaLokacja!=null){
                updateLocation(ostatniaZnanaLokacja);
                updateLocationInfo(ostatniaZnanaLokacja);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            startLokacji();
        }
    }

    public void startLokacji(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
    public void updateLocation(Location location){
        Log.i("Location",location.toString());
    }
    public void updateLocationInfo(Location location){
        TextView szerokoscText=findViewById(R.id.szerokosc_geo);
        TextView dlugoscText=findViewById(R.id.dlugosc_geo);
        TextView wysokoscText=findViewById(R.id.wysokosc);
        TextView adresText=findViewById(R.id.adres);

        szerokoscText.setText("Szerokość geograficzna: "+Double.toString(location.getLatitude()));
        dlugoscText.setText("Szerokość geograficzna: "+Double.toString(location.getLongitude()));
        wysokoscText.setText("Szerokość geograficzna: "+Double.toString(location.getAltitude()));
        adresText.setText("Adres:");

        String adres="Nie udało się odnaleźć adresu";
        Geocoder geocoder=new Geocoder(this,Locale.getDefault());
        try {
            List<Address> lista_adresow=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            if(lista_adresow!=null && lista_adresow.size()>0){
                adres="Adres:\n";
                if(lista_adresow.get(0).getThoroughfare()!=null){
                    adres+=lista_adresow.get(0).getThoroughfare()+"\n";
                }
                if(lista_adresow.get(0).getLocality()!=null){
                    adres+=lista_adresow.get(0).getLocality()+" ";
                }
                if(lista_adresow.get(0).getPostalCode()!=null){
                    adres+=lista_adresow.get(0).getPostalCode()+" ";
                }
                if(lista_adresow.get(0).getAdminArea()!=null){
                    adres+=lista_adresow.get(0).getAdminArea()+" ";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            adresText.setText(adres);
    }
}
