package com.example.vale.geocoder;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    //calve proyecto CiceProject cice-project141709
    //clave API Google Maps AIzaSyAYSLs6SOaLDKTb28RBcsZCCAaIuCoVqvA

    private GoogleApiAvailability googleApiAvailability;
    private GoogleApiClient googleApiClient;
    private GoogleMap googleMap;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleApiAvailability = GoogleApiAvailability.getInstance();
        int estadoAPI = googleApiAvailability.isGooglePlayServicesAvailable(this);

        switch (estadoAPI)
        {
            case ConnectionResult.SUCCESS:


                Log.d(getClass().getCanonicalName(), "CONEXIÓN A LA API DE GOOLE CHACHI");

                //setUpMapIfNeeded();

                googleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();

                googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

                break;
            case ConnectionResult.SERVICE_MISSING:
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
            case ConnectionResult.SERVICE_DISABLED:

                Log.d(getClass().getCanonicalName(), "CONEXIÓN A LA API DE GOOgLE chunga " + estadoAPI);

                Dialog dialog = googleApiAvailability.getErrorDialog(this, estadoAPI, 5);
                dialog.show();

                break;


        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(getClass().getCanonicalName(), "ERROR conectando al API de Google");
    }

    @Override
    public void onConnected(Bundle bundle) {
        try
        {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location == null)
            {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            }
            else
            {
                handleNewLocation(location);
                traduceDir(location);
            }

        }
        catch (SecurityException se)
        {
            Log.e(getClass().getCanonicalName(), "SE PETO LA COSA" , se);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d(getClass().getCanonicalName(), "Conexion suspendida");
    }


    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }



    private void handleNewLocation(Location location) {
        Log.d(getClass().getCanonicalName(), location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("Toy AKI!");
        googleMap.addMarker(options);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }


    /**
     * Este método debería ir a parte, en otro hilo (IntentService o AsyncTask) porque tarda varios segundos en ejecutarse
     * y retarda la representación de la Actividad más de lo debido
     *
     * ejemplo usando IntentService https://developer.android.com/training/location/display-address.html
     *
     *
     * @param location
     */
    private void traduceDir (Location location)
    {
        Geocoder geocoder = null;
        List<Address> lista_direcciones = null;
        Address direccion = null;

            if (Geocoder.isPresent())
            {
                Log.d(getClass().getCanonicalName(), "GEOCODER presente :)");
                geocoder = new Geocoder(this);

                try
                {
                    lista_direcciones = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (lista_direcciones.size()>=1)
                    {
                        direccion = lista_direcciones.get(0);
                        Log.d(getClass().getCanonicalName(), "Dirección obtenida = " + direccion.toString());
                    }

                } catch (IOException io)
                    {
                        Log.e(getClass().getCanonicalName(), "Errorazo obteniedo la dirección desde location (Geocoder)", io);
                    }


            } else
                {
                    Log.d(getClass().getCanonicalName(), "GEOCODER NO presente :(");

                }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(getClass().getCanonicalName(), "LOCALIZACIÓN CAMBIADA ");
        handleNewLocation(location);
        traduceDir (location);
    }
}
