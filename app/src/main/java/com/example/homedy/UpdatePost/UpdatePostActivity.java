package com.example.homedy.UpdatePost;

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

import com.example.homedy.Post;
import com.example.homedy.IPaddress;
import com.example.homedy.NewPost.Image.Image;
import com.example.homedy.NewPost.Image.ImageActivity;
import com.example.homedy.NewPost.Message;
import com.example.homedy.NewPost.NewPostActivity;
import com.example.homedy.NewPost.TypeRoomDialog;
import com.example.homedy.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import retrofit2.http.Part;
import retrofit2.http.Query;

public class UpdatePostActivity extends AppCompatActivity implements TypeRoomDialog.DialogPost3Listener{

    private static final int REQUEST_UPDATE_IMAGE = 65;
    private static final String POS = "pos";

    private SharedPreferences sharedPreferences;
    private ArrayList<Bitmap> bitmaps = Image.getBitmaps();
    private ArrayList<String> paths = Image.getPaths();
    private ArrayList<Post> posts = Post.getHomeItemsPost();
    private Post post;
    private String email;
    private int position;
    private static final String TAG = "image path ";

    String ip = IPaddress.getIp();
    private String UPLOAD_URL = ip + "post/updatepost/";

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
    AutocompleteSupportFragment addressAuto;

    FloatingActionButton floatingActionButton;
    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        final Intent intent = this.getIntent();
        final Bundle bundle = intent.getBundleExtra("data");
        position = bundle.getInt(POS, 0);
        Log.d(TAG, "onCreate: " + position);
        post = posts.get(position);
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email","trandinhcn@gmail.com");

        bitmaps.clear();
        paths.clear();

        floatingActionButton = findViewById(R.id.fab_navigation);
        bottomAppBar = (BottomAppBar) findViewById(R.id.navigation);

        setSupportActionBar(bottomAppBar);

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        posttype = (RadioGroup) findViewById(R.id.radioGroup_newpost_type);
        grafted = (RadioButton) findViewById(R.id.radioButton_newpost_oGhep);
        rented = (RadioButton) findViewById(R.id.radioButton_newpost_forRent);
        title = (EditText) findViewById(R.id.edt_newpost_title);
        rent = (EditText) findViewById(R.id.edt_newpost_rent);
        area = (EditText) findViewById(R.id.edt_newpost_area);
        phone = (EditText) findViewById(R.id.edt_newpost_phone);
        description = (EditText) findViewById(R.id.edt_newpost_description);
        typeroom = (EditText) findViewById(R.id.edt_newpost_typeRoom);

        typeroom.setFocusable(false);
        typeroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TypeRoomDialog typeRoomDialog = new TypeRoomDialog();
                typeRoomDialog.show(getSupportFragmentManager(), "");
            }
        });

        addressAuto = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        addressAuto.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        addressAuto.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                String location = place.getName();
                Geocoder gc = new Geocoder(getApplicationContext());
                try {
                    List<Address> addresses= gc.getFromLocationName(location, 5);
                    Address address = addresses.get(0);
                    post.setAddress(place.getName());
                    post.setLat(address.getLatitude());
                    post.setLng(address.getLongitude());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        setData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPost()) uploadImage();
            }
        });
    }

    public void setData(){
        if(post.getPosttype().equals("Cho thuê"))
            rented.setChecked(true);
        else grafted.setChecked(true);
        addressAuto.setText(post.getAddress());
        title.setText(post.getTitle());
        typeroom.setText(post.getTyperoom());
        rent.setText(String.valueOf(post.getRent()));
        area.setText(String.valueOf(post.getArea()));
        phone.setText(post.getPhone());
        description.setText(post.getDescription());
    }

    public boolean checkPost() {
        if (posttype.getCheckedRadioButtonId() == -1) {
            Toast.makeText(UpdatePostActivity.this, "Bạn chưa chọn loại tin!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(rented.isChecked()){
            post.setPosttype("Cho thuê");
        } else post.setPosttype("Ở ghép");


        if (title.getText().toString().matches("")) {
            Toast.makeText(UpdatePostActivity.this, "Bạn chưa có tiêu đề của tin", Toast.LENGTH_SHORT).show();
            return false;
        } else post.setTitle(title.getText().toString());

        if (typeroom.getText().toString().matches("")) {
            Toast.makeText(UpdatePostActivity.this, "Bạn chưa chọn kiểu phòng", Toast.LENGTH_SHORT).show();
            return false;
        }else post.setTyperoom(typeroom.getText().toString());

        if (rent.getText().toString().matches("")) {
            Toast.makeText(UpdatePostActivity.this, "Bạn chưa nhập giá cho thuê ", Toast.LENGTH_SHORT).show();
            return false;
        } else post.setRent(Integer.parseInt(rent.getText().toString()));

        if (area.getText().toString().matches("")) {
            Toast.makeText(UpdatePostActivity.this, "Bạn chưa nhập diện tích cho thuê", Toast.LENGTH_SHORT).show();
            return false;
        } else post.setArea(Integer.parseInt(area.getText().toString()));

        if(post.getAddress().equals("")){
            Toast.makeText(UpdatePostActivity.this, "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.getText().toString().matches("")) {
            Toast.makeText(UpdatePostActivity.this, "Bạn chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return false;
        } else post.setPhone(phone.getText().toString());

        if (description.getText().toString().matches("")) {
            Toast.makeText(UpdatePostActivity.this, "Bạn chưa nhập mô tả", Toast.LENGTH_SHORT).show();
            return false;
        } else post.setDescription((description.getText().toString()));
        return true;
    }

    @Override
    public void setDataFromFragment(String data) {
        typeroom.setText(data);
    }

    public interface FileUploadService {
        @POST("/post")
        Call<Message> uploadImage(@Body RequestBody requestBody, @Query("image") ArrayList<String> image);
    }

    public void uploadImage(){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(UpdatePostActivity.this);
        progressDialog.setMessage("Đang tải lên...");
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UPLOAD_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("email", email );
        builder.addFormDataPart("posttype", post.getPosttype());
        builder.addFormDataPart("typeroom", post.getTyperoom() );
        builder.addFormDataPart("gia", String.valueOf(post.getRent()));
        builder.addFormDataPart("dientich", String.valueOf(post.getArea()));
        builder.addFormDataPart("lat", String.valueOf(post.getLat()));
        builder.addFormDataPart("lng", String.valueOf(post.getLng()));
        builder.addFormDataPart("address", post.getAddress());
        builder.addFormDataPart("phonepost", post.getPhone());
        builder.addFormDataPart("title", post.getTitle());
        builder.addFormDataPart("description", post.getDescription());
        for(int i = 0; i < post.getUrl_image().size(); i ++){
            builder.addFormDataPart(i + "images", post.getUrl_image().get(i));
        }

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
        UpdatePostActivity.FileUploadService service = retrofit.create(UpdatePostActivity.FileUploadService.class);
        Call<Message> call = service.uploadImage(requestBody, post.getUrl_image());
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                progressDialog.dismiss();
                Message message = response.body();
                if (message.getMessage().equals("Success")) {
                    Toast.makeText(UpdatePostActivity.this, "success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdatePostActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent1 = new Intent(UpdatePostActivity.this, ImageActivity.class );
        Bundle bundle1 = new Bundle();
        bundle1.putInt(POS, position);
        intent1.putExtra("images", bundle1);
        startActivityForResult(intent1, REQUEST_UPDATE_IMAGE);
        return super.onOptionsItemSelected(item);
    }
}
