package com.example.homedy.APost;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.homedy.Post;
import com.example.homedy.IPaddress;
import com.example.homedy.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.libraries.places.api.Places;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class APostActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPoiClickListener {

    private static final String TAG = "tag";
    private final String POSITION = "key_position";
    private ArrayList<Post> posts = Post.getPosts();
    private ImagePagerAdapter imagePagerAdapter;
    private ViewPager viewPager;
    private TextView tvTitle;
    private TextView tvTypePost;
    private TextView tvTypeRoom;
    private TextView tvPhone;
    private TextView tvGia;
    private TextView tvDienTich;
    private TextView tvAddress;
    private TextView tvDes;
    private TextView tvNamePerson;
    private TextView tvPhonePerson;
    private TextView tvEmail;
    private String ip = IPaddress.getIp();
    private String URL_LOGIN = ip + "user/getuser";
    private GoogleMap mMap;
    private Marker marker;
    Double lat;
    Double lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_post);
        Intent intent = getIntent();
        int position = intent.getIntExtra(POSITION, 0);
        final Post post = posts.get(position);

        tvTitle = findViewById(R.id.txt_title_apost);
        tvTypePost = findViewById(R.id.txt_posttype_apost);
        tvTypeRoom = findViewById(R.id.txt_typeroom_apost);
        tvPhone = findViewById(R.id.txt_phone_apost);
        tvGia = findViewById(R.id.txt_gia_apost);
        tvDienTich = findViewById(R.id.txt_dientich_apost);
        tvAddress = findViewById(R.id.txt_address_apost);
        tvDes = findViewById(R.id.txt_descriptiảon_apost);
        tvNamePerson = findViewById(R.id.txt_provider_apost);
        tvPhonePerson = findViewById(R.id.txt_phoneperson_apost);
        tvEmail = findViewById(R.id.txt_email_apost);


        tvTypePost.setText(post.getPosttype());



        tvTypeRoom.setText(post.getTyperoom());

        tvTitle.setText(post.getTitle());
        tvPhone.setText(post.getPhone());
        tvGia.setText(String.valueOf(post.getRent()) + "đ");
        tvDienTich.setText(String.valueOf(post.getArea()) + " m2");
        tvAddress.setText(post.getAddress());
        tvDes.setText(post.getDescription());
        tvEmail.setText(post.getName());
        lat = post.getLat();
        lng = post.getLng();

        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("email", post.getName());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL_LOGIN, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            load(response.getString("name_person") + " - " + post.getTime(),response.getString("phone"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonObjReq.setTag(TAG);
        queue.add(jsonObjReq);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyA2d8RYsPfPLKspnYnQciZDFBJGbFyNDUc");
        }
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frg_map);
        supportMapFragment.getMapAsync(this);

        imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), position, post.getUrl_image().size());
        viewPager = (ViewPager) findViewById(R.id.vp_image);
        viewPager.setAdapter(imagePagerAdapter);
    }

    void load(String str1, String str2){
        tvNamePerson.setText(str1);
        tvPhonePerson.setText(str2);
    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {
        createMarker(pointOfInterest.latLng);
        Toast.makeText(getApplicationContext(), "Clicked: " +
                        pointOfInterest.name + "\nPlace ID:" + pointOfInterest.placeId +
                        "\nLatitude:" + pointOfInterest.latLng.latitude +
                        " Longitude:" + pointOfInterest.latLng.longitude,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(APostActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                    Log.d("tag", address);
                    marker = createMarker(latLng);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mMap.setOnPoiClickListener(this);
        LatLng fixedLocation = new LatLng(lat, lng);
        createMarker(fixedLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fixedLocation, 15));
    }

    private Marker createMarker (LatLng latLng){
        if (mMap == null) {
            return null;
        }
        if(marker != null) marker.remove();
        MarkerOptions mOptions = new MarkerOptions().position(latLng);
        mOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        marker = mMap.addMarker(mOptions);
        return marker;
    }
}
