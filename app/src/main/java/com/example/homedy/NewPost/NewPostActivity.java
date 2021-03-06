package com.example.homedy.NewPost;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.homedy.BottomSheetMain;
import com.example.homedy.Post;
import com.example.homedy.IPaddress;
import com.example.homedy.NewPost.Image.Image;
import com.example.homedy.NewPost.Image.ImageActivity;
import com.example.homedy.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;


public class NewPostActivity extends AppCompatActivity implements TypeRoomDialog.DialogPost3Listener {
    private static final int REQUEST_IMAGE = 66;

    private SharedPreferences sharedPreferences;
    private ArrayList<Post> posts = Post.getHomeItemsPost();
    private ArrayList<Bitmap> bitmaps = Image.getBitmaps();
    private ArrayList<String> paths = Image.getPaths();
    private Post newPost = new Post();
    private String email;
    private static final String TAG = "image path ";

    String ip = IPaddress.getIp();
    private String UPLOAD_URL = ip;


    private RadioGroup posttype;
    private RadioButton rented;
    private RadioButton grafted;
    private EditText title;
    private EditText rent;
    private EditText area;
    private EditText phone;
    private EditText description;
    private EditText typeroom;
    private Button addImage;
    private Button btnpost;
    AutocompleteSupportFragment autocompleteFragment;
    private String POS = "pos";

    FloatingActionButton floatingActionButton;
    BottomAppBar bottomAppBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("name", "trandinhcn@gmail.com");
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyA2d8RYsPfPLKspnYnQciZDFBJGbFyNDUc");
        }

        bitmaps.clear();
        paths.clear();

        floatingActionButton = findViewById(R.id.fab_navigation);
        bottomAppBar = (BottomAppBar) findViewById(R.id.navigation);

        setSupportActionBar(bottomAppBar);

        posttype = (RadioGroup) findViewById(R.id.radioGroup_newpost_type);
        grafted = (RadioButton) findViewById(R.id.radioButton_newpost_oGhep);
        rented = (RadioButton) findViewById(R.id.radioButton_newpost_forRent);
        title = (EditText) findViewById(R.id.edt_newpost_title);
        rent = (EditText) findViewById(R.id.edt_newpost_rent);
        area = (EditText) findViewById(R.id.edt_newpost_area);
        phone = (EditText) findViewById(R.id.edt_newpost_phone);
        description = (EditText) findViewById(R.id.edt_newpost_description);
        typeroom = (EditText) findViewById(R.id.edt_newpost_typeRoom);
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        typeroom.setFocusable(false);
        typeroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TypeRoomDialog typeRoomDialog = new TypeRoomDialog();
                typeRoomDialog.show(getSupportFragmentManager(), "");
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPost()) UploadImage();
            }
        });

        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setHint("Địa chỉ ");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                String location = place.getName();
                Geocoder gc = new Geocoder(getApplicationContext());
                try {
                    List<Address> addresses= gc.getFromLocationName(location, 5);
                    Address address = addresses.get(0);
                    newPost.setAddress(place.getName());
                    newPost.setLat(address.getLatitude());
                    newPost.setLng(address.getLongitude());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    public boolean checkPost() {
        if (posttype.getCheckedRadioButtonId() == -1) {
            Toast.makeText(NewPostActivity.this, "Bạn chưa chọn loại tin!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(rented.isChecked()){
            newPost.setPosttype("Cho thuê");
        } else newPost.setPosttype("Ở ghép");


        if (title.getText().toString().matches("")) {
            Toast.makeText(NewPostActivity.this, "Bạn chưa có tiêu đề của tin", Toast.LENGTH_SHORT).show();
            return false;
        } else newPost.setTitle(title.getText().toString());

        if (typeroom.getText().toString().matches("")) {
            Toast.makeText(NewPostActivity.this, "Bạn chưa chọn kiểu phòng", Toast.LENGTH_SHORT).show();
            return false;
        }else newPost.setTyperoom(typeroom.getText().toString());

        if (rent.getText().toString().matches("")) {
            Toast.makeText(NewPostActivity.this, "Bạn chưa nhập giá cho thuê ", Toast.LENGTH_SHORT).show();
            return false;
        } else newPost.setRent(Integer.parseInt(rent.getText().toString()));

        if (area.getText().toString().matches("")) {
            Toast.makeText(NewPostActivity.this, "Bạn chưa nhập diện tích cho thuê", Toast.LENGTH_SHORT).show();
            return false;
        } else newPost.setArea(Integer.parseInt(area.getText().toString()));

        if(newPost.getAddress().equals("")){
            Toast.makeText(NewPostActivity.this, "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.getText().toString().matches("")) {
            Toast.makeText(NewPostActivity.this, "Bạn chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return false;
        } else newPost.setPhone(phone.getText().toString());

        if (description.getText().toString().matches("")) {
            Toast.makeText(NewPostActivity.this, "Bạn chưa nhập mô tả", Toast.LENGTH_SHORT).show();
            return false;
        } else newPost.setDescription((description.getText().toString()));
        return true;
    }

    @Override
    public void setDataFromFragment(String data) {
        typeroom.setText(data);
    }

    public interface FileUploadService {
        @POST("post/newpost")
        Call<Message> uploadImage(@Body RequestBody requestBody);
    }

    public void UploadImage(){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(NewPostActivity.this);
        progressDialog.setMessage("Dang tai len");
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UPLOAD_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("email", email );
        builder.addFormDataPart("posttype", newPost.getPosttype());
        builder.addFormDataPart("title", newPost.getTitle());
        builder.addFormDataPart("description", newPost.getDescription());
        builder.addFormDataPart("typeroom", newPost.getTyperoom() );
        builder.addFormDataPart("gia", String.valueOf(newPost.getRent()));
        builder.addFormDataPart("dientich", String.valueOf(newPost.getArea()));
        builder.addFormDataPart("lat", String.valueOf(newPost.getLat()));
        builder.addFormDataPart("lng", String.valueOf(newPost.getLng()));
        builder.addFormDataPart("address", newPost.getAddress());
        builder.addFormDataPart("phonepost", newPost.getPhone());
        try {
            for (String s : paths) {
                File file = new File(s);
                RequestBody requestBody = RequestBody.create(
                        MediaType.parse("image/*"),
                        file);
                builder.addFormDataPart("upload", file.getName(), requestBody);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        MultipartBody requestBody = builder.build();
        FileUploadService service = retrofit.create(FileUploadService.class);
        Call<Message> call = service.uploadImage(requestBody);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    Toast.makeText(NewPostActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, t.toString());
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(NewPostActivity.this, ImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(POS, -1);
        intent.putExtra("images", bundle);
        startActivityForResult(intent, REQUEST_IMAGE);
        return super.onOptionsItemSelected(item);
    }
}

