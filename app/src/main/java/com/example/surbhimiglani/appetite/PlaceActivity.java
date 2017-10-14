package com.example.surbhimiglani.appetite;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.security.AccessController.getContext;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class PlaceActivity extends AppCompatActivity implements
            GoogleApiClient.OnConnectionFailedListener,
            GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, LocationListener {

        ImageView imageView12;
        Button imageView;
        double distanceBetween;
    TextView textView5;
        private static final String LOG_TAG = "MainActivity";
        private static final int GOOGLE_API_CLIENT_ID = 0;
        private AutoCompleteTextView mAutocompleteTextView;
        private TextView mAttTextView, blankTexView;
        private GoogleApiClient mGoogleApiClient, client;
        private PlaceArrayAdapter mPlaceArrayAdapter;
        private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
                new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
        SharedPreferences sf3;
        private static String preference = "pref3";
        private static String saveIt = "Savekey3";
        private GoogleMap mMap;
        private LocationRequest locationRequest;
        private Marker currentLocation, marker;
        public Location lastLocation;
        private static final int REQUEST_PERMISION_CODE = 99;
        Geocoder geocoder;
        LatLng latLng, latLng2;
        LoadMap lm;
        android.os.Handler handler;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_place);

            geocoder = new Geocoder(this);
             imageView=(Button) findViewById(R.id.imageView);
            textView5=(TextView) findViewById(R.id.textView5);
            client = new GoogleApiClient.Builder(PlaceActivity.this)
                    .addApi(Places.GEO_DATA_API)
                    .build();
            // .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
            handler=new Handler();
            mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.placeOfBirthautoCompleteTextView);
            mAutocompleteTextView.setThreshold(1);
            mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
            mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                    BOUNDS_MOUNTAIN_VIEW, null);

            if(isNetworkAvailable()) {
                mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
            }
            else
            {
                client.disconnect();
                Toast.makeText(PlaceActivity.this, "Internet not Connected", Toast.LENGTH_SHORT).show();
            }

            sf3 = getSharedPreferences(preference, Context.MODE_PRIVATE);
            if(SharedPreferncesHelper.getPlace(getApplicationContext())!=null){
                mAutocompleteTextView.setText(SharedPreferncesHelper.getPlace(getApplicationContext()));
            }
            else {
                if (sf3.contains(saveIt)) {
                    mAutocompleteTextView.setText(sf3.getString(saveIt, ""));
                }
            }


            lm=new LoadMap();

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }
            else
            {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (mGoogleApiClient == null) {
                        buildGoogleApiClient();
                    }
                    mMap.setMyLocationEnabled(true);
                }
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String store = mAutocompleteTextView.getText().toString();
                    SharedPreferences.Editor editor = sf3.edit();
                    editor.putString(saveIt, store);
                    editor.apply();
                   SharedPreferncesHelper.putPlace(getApplicationContext(), mAutocompleteTextView.getText().toString());
                   /*  Intent q = new Intent(getApplicationContext(), yourZodiacActivity.class);
                    startActivity(q); */
                    finish();
                }
            });

            mAutocompleteTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(isNetworkAvailable()){
                        if(!client.isConnected()) {
                            client.connect();
                        }
                    }
                    else
                    {
                        if(client.isConnected()){
                            client.disconnect();
                        }
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if(isNetworkAvailable()){
                        if(!client.isConnected()){
                            client.connect();
                        }
                        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
                    }
                    else
                    {
                        if(client.isConnected()){
                            client.disconnect();
                        }
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(isNetworkAvailable()){
                        if(!client.isConnected()) {
                            client.connect();
                        }
                    }
                    else
                    {
                        if(client.isConnected()){
                            client.disconnect();
                        }
                    }
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.grey));
            }

        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case REQUEST_PERMISION_CODE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            if (mGoogleApiClient == null) {
                                buildGoogleApiClient();
                            }
                            mMap.setMyLocationEnabled(true);
                        }
                    } else {
                        Toast.makeText(this, "Permisision denied", Toast.LENGTH_LONG).show();
                    }
            }
            return;
        }

        private AdapterView.OnItemClickListener mAutocompleteClickListener
                = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
                final String placeId;
                placeId = String.valueOf(item.placeId);
                Log.i(LOG_TAG, "Selected: " + item.description);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(client, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
                Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);

                String address = mAutocompleteTextView.getText().toString();
                List<Address> addressList = null;
                MarkerOptions mo = new MarkerOptions();
                if (!address.equals("")) {
                    try {
                        addressList = geocoder.getFromLocationName(address, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (addressList != null) {
                    for (int i = 0; i < addressList.size(); i++) {
                        Address myAddress = addressList.get(i);
                        LatLng latLng3 = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                        mo.position(latLng3);
                        mo.title("Your entered location");
                        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                        mMap.addMarker(mo);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng3, 10));



                            // Getting URL to the Google Directions AP
                                    String urlRoute = getDirectionsUrl(latLng, latLng3);
                            DownloadTask downloadTask = new DownloadTask();

                            // Start downloading json data from Google Directions API
                            downloadTask.execute(urlRoute);
                      distanceBetween=  CalculationByDistance(latLng,latLng3);
                        textView5.setText("Distance = "+String.valueOf(distanceBetween)+" km");
                    }


                }



            }
        };

        private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
                = new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    Log.e(LOG_TAG, "Place query did not complete. Error: " +
                            places.getStatus().toString());
                    return;
                }

                // Selecting the first object buffer.
                CharSequence attributions = places.getAttributions();
                if (attributions != null) {
                    mAttTextView.setText(Html.fromHtml(attributions.toString()));
                }
            }
        };

        @Override
        public void onConnected(Bundle bundle) {
            mPlaceArrayAdapter.setGoogleApiClient(client);
            Log.i(LOG_TAG, "Google Places API connected.");

            locationRequest = new LocationRequest();

            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            }

        }

        public boolean checkLocationPermission() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISION_CODE);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISION_CODE);
                }
                return false;
            } else {
                return true;
            }
        }


        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                    + connectionResult.getErrorCode());

            Toast.makeText(this,
                    "Google Places API connection failed with error code:" +
                            connectionResult.getErrorCode(),
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onConnectionSuspended(int i) {
            mPlaceArrayAdapter.setGoogleApiClient(null);
            Log.e(LOG_TAG, "Google Places API connection suspended.");
        }

        @Override
        public void onBackPressed() {
            //  super.onBackPressed();
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                if (mAutocompleteTextView.getText().length() == 0) {
                    mMap.setMyLocationEnabled(true);
                }
                else {
                    savedPosition();
                }
            }

        }

        protected synchronized void buildGoogleApiClient() {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();


        }

        @Override
        public void onLocationChanged(Location location) {

            lastLocation = location;
            if (currentLocation != null) {
                currentLocation.remove();
            }

            lm=new LoadMap();
            lm.execute();

            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        }

        Runnable updateMarker = new Runnable() {
            @Override
            public void run() {
                marker.remove();
                marker = mMap.addMarker(new MarkerOptions().position(latLng2));

                handler.postDelayed(this, 2000);

            }
        };

        public void savedPosition() {
            mMap.clear();
            String address = mAutocompleteTextView.getText().toString();
            List<Address> addressList = null;
            MarkerOptions mo = new MarkerOptions();
            if(!address.equals("")) {
                try {
                    addressList = geocoder.getFromLocationName(address, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (addressList != null) {
                for(int i = 0; i < addressList.size(); i++) {
                    Address myAddress = addressList.get(i);
                    latLng2 = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                    mo.position(latLng2);
                    mo.title("Your entered location");
                    mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    marker=mMap.addMarker(mo);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 1));
                    handler.postDelayed(updateMarker, 2000);
                }
            }
        }

        @Override
        protected void onDestroy() {
            handler.removeCallbacks(updateMarker);

            super.onDestroy();
        }

        @Override
        protected void onStart() {
            super.onStart();
            client.connect();
        }

        @Override
        protected void onStop() {
            super.onStop();
            client.disconnect();
        }

        class LoadMap extends AsyncTask<Void, Void, LatLng> {


            @Override
            protected LatLng doInBackground(Void... objects) {

                latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                return latLng;
            }

            @Override
            protected void onPostExecute(LatLng location) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(location);
                markerOptions.title("Current Location");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                currentLocation = mMap.addMarker(markerOptions);

                if(mAutocompleteTextView.getText().length()==0){
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 1));
                }

            }
        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;


            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    }


