package com.example.homedy.Search;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.homedy.Post;
import com.example.homedy.IPaddress;
import com.example.homedy.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static final String EXTRA_CONTACT = "EXTRA_CONTACT";
    private String ip = IPaddress.getIp();
    private String url = ip + "post/search";
    private GoogleMap mMap;
    private Marker marker;
    private ArrayList<Post> homeItemsSearches = Post.getHomeItemsSearches();
    private ArrayList<Post> homeItemsMap = Post.getPosts();
    EditText _minRent;
    EditText _maxRent;
    EditText _minArea;
    EditText _maxArea;
    EditText _keyWord;
    int minRent;
    int maxRent;
    int minArea;
    int maxArea;
    private static final int REQUEST_LOCATION_PERMISSION = 69;
    FloatingActionButton floatingActionButton;
    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyA2d8RYsPfPLKspnYnQciZDFBJGbFyNDUc");
        }

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frg_map2);
        supportMapFragment.getMapAsync(this);

        _minRent = findViewById(R.id.edt_gia_nho);
        _maxRent = findViewById(R.id.edt_gia_lon);
        _minArea = findViewById(R.id.edt_dientich_nho);
        _maxArea = findViewById(R.id.edt_dientich_lon);
        _keyWord = findViewById(R.id.input_name);

        Button btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        _minRent.setText("1000000");
        _maxRent.setText("10000000");
        _minArea.setText("10");
        _maxArea.setText("100");

        floatingActionButton = findViewById(R.id.fab_navigation);
        bottomAppBar = findViewById(R.id.navigation);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        postponeEnterTransition();

        final View decor = getWindow().getDecorView();
        decor.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                decor.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });
    }

    public void checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    public void load() {
        checkPermission();

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    public void search(){
        String keyWord = _keyWord.getText().toString();
        int minRent = Integer.parseInt(_minRent.getText().toString());
        int maxRent = Integer.parseInt(_maxRent.getText().toString());
        int minArea = Integer.parseInt(_minArea.getText().toString());
        int maxArea = Integer.parseInt(_maxArea.getText().toString());
        if(!keyWord.matches("")){
            final ProgressDialog progressDialog1 = new ProgressDialog(this,R.style.Theme_AppCompat_DayNight_Dialog);
            progressDialog1.setIndeterminate(true);
            progressDialog1.setMessage("Đang tìm kiếm....");

            Map<String, String> postParam= new HashMap<String, String>();
            Log.d(TAG, "search: ");
            postParam.put("keyword", keyWord);

            RequestQueue queue = Volley.newRequestQueue(this);
            progressDialog1.show();
            final JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject(postParam);
            jsonArray.put(jsonObject);

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray ,new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if(response != null) {
                        if(homeItemsSearches.size() != 0) homeItemsSearches.clear();
                        if(homeItemsSearches.size() == 0) {
                            for (int i = 0; i < response.length(); i++) {
                                Post post = new Post();
                                try {
                                    JSONObject jsonObject = response.getJSONObject(response.length() - 1 - i);
                                    post.setId(jsonObject.getString("_id"));
                                    post.setTitle(jsonObject.getString("title"));
                                    post.setDescription(jsonObject.getString("description"));
                                    post.setName(jsonObject.getString("provider"));
                                    int gia = jsonObject.getInt("gia");
                                    int dientich = jsonObject.getInt("dientich");
                                    post.setRent(gia);
                                    post.setArea(dientich);
                                    JSONArray jsonArray = jsonObject.getJSONArray("images");
                                    if (jsonArray.length() != 0)
                                        for(int j = 0; j < jsonArray.length(); j ++){
                                            post.getUrl_image().add(ip + jsonArray.getString(j));
                                        }
                                    else post.getUrl_image().add("default");
                                    post.setTime(jsonObject.getString("time"));
                                    post.setAddress(jsonObject.getString("address"));
                                    post.setPosttype(jsonObject.getString("posttype"));
                                    post.setPhone(jsonObject.getString("phonepost"));
                                    post.setTyperoom(jsonObject.getString("typeroom"));
                                    post.setLat(jsonObject.getDouble("lat"));
                                    post.setLng(jsonObject.getDouble("lng"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                homeItemsSearches.add(post);
                            }
                        }
                        progressDialog1.dismiss();
                        result();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: ");
                    progressDialog1.dismiss();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            jsonArrayRequest.setTag(TAG);
            queue.add(jsonArrayRequest);
        }
    }

    public void result(){
        Log.d(TAG, "result: ");
        for(int i = 0; i < homeItemsSearches.size(); i ++){
            int gia = homeItemsSearches.get(i).getRent();
            int dientich = homeItemsSearches.get(i).getArea();
            if(gia < minRent && gia > maxRent && dientich < minArea && dientich > maxArea){
                homeItemsSearches.remove(i);
                i--;
            }

        }
        Intent intent = new Intent(this, ResultSearchActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                try {
//                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                    String city = addresses.get(0).getLocality();
//                    String state = addresses.get(0).getAdminArea();
//                    String country = addresses.get(0).getCountryName();
//                    String postalCode = addresses.get(0).getPostalCode();
//                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
//                    Log.d("tag", address);
//                    marker = createMarker(latLng);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
        mMap.setInfoWindowAdapter(new CustomWindowInfoAdapter(this));
        for(int i = 0; i < homeItemsMap.size(); i ++) {
            if(homeItemsMap.get(i).getLng() != null)
                createMarker(homeItemsMap.get(i));
        }
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fixedLocation, 15));
        checkPermission();
        mMap.setMyLocationEnabled(true);
    }

    private Marker createMarker (Post post){
        if (mMap == null) {
            return null;
        }
        //if(marker != null) marker.remove();
        LatLng latLng = new LatLng(post.getLat(), post.getLng());
        String title = post.getTitle();
        String snippet ="Phone Number :" + post.getPhone() + "\n" + "Rent: "
                + post.getRent() + "đ - " + "Area: " + post.getArea() + " m2\n" + "Address: " +
                post.getAddress();

        MarkerOptions mOptions = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(title)
                .snippet(snippet);

        marker = mMap.addMarker(mOptions);
        return marker;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    load();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
