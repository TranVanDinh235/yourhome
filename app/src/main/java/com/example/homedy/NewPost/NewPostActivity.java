package com.example.homedy.NewPost;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.example.homedy.IPaddress;
import com.example.homedy.NewPost.Image.Image;
import com.example.homedy.NewPost.Image.ImageActivity;
import com.example.homedy.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.File;
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


public class NewPostActivity extends AppCompatActivity implements DialogPost3Fragment.DialogPost3Listener {

    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int GALLERY_REQUEST_CODE = 98;
    private static final int REQUEST_ID_AVATAR_IMAGE_CAPTURE = 101;
    private static final int AVATAR_GALLERY_REQUEST_CODE = 102;
    private static final int REQUEST_IMAGE = 66;

    SharedPreferences sharedPreferences;
    ArrayList<String> images = Image.getPaths();
    Post post = Post.getPost();
    String email;
    private static final String TAG = "image path ";

    String ip = IPaddress.getIp();
    private String UPLOAD_URL = ip + "post/newpost";

    @InjectView(R.id.radioGroup_newpost_type)
    RadioGroup _posttypeRadioGroup;
    @InjectView(R.id.radioButton_newpost_forRent)
    RadioButton _postRentRadioButton;
    @InjectView(R.id.radioButton_newpost_oGhep)
    RadioButton _postOghepRadioButton;
    @InjectView(R.id.btn_newpost_addImage)
    Button _addImageButton;
    @InjectView(R.id.edt_newpost_title)
    EditText _titleEditText;
    @InjectView(R.id.btn_newpost_typeRoom)
    Button _typeRoomButton;
    @InjectView(R.id.edt_newpost_rent)
    EditText _rentEditText;
    @InjectView(R.id.edt_newpost_area)
    EditText _areaEditText;
    @InjectView(R.id.edt_newpost_phone)
    EditText _phoneEditText;
    @InjectView(R.id.edt_newpost_description)
    EditText _descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("name", "trandinhcn@gmail.com");
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyA2d8RYsPfPLKspnYnQciZDFBJGbFyNDUc");
        }
        ButterKnife.inject(this);


        _addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPostActivity.this, ImageActivity.class );
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        _typeRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPost3Fragment dialogPost3Fragment = new DialogPost3Fragment();
                dialogPost3Fragment.show(getSupportFragmentManager(), "");
            }
        });

        Button button = findViewById(R.id.btn_newpost);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPost();
                UploadImage();
            }
        });

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }


    private void CallAutocompleteFragment(int AUTOCOMPLETE_REQUEST_CODE) {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).setCountry("VN").build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    public boolean checkPost() {

        if (_posttypeRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(NewPostActivity.this, "Bạn chưa chọn loại tin!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(_postRentRadioButton.isChecked()){
            post.setPosttype("Cho thuê");
        } else post.setPosttype("Ở ghép");


        if (_titleEditText.getText().toString().matches("")) {
            Toast.makeText(NewPostActivity.this, "Bạn chưa có tiêu đề của tin", Toast.LENGTH_SHORT).show();
            return false;
        } else post.setTitle(_titleEditText.getText().toString());

        if (_typeRoomButton.getText().toString().matches("")) {
            Toast.makeText(NewPostActivity.this, "Bạn chưa chọn kiểu phòng", Toast.LENGTH_SHORT).show();
            return false;
        }else post.setTyperoom(_typeRoomButton.getText().toString());

        if (_rentEditText.getText().toString().matches("")) {
            Toast.makeText(NewPostActivity.this, "Bạn chưa nhập giá cho thuê ", Toast.LENGTH_SHORT).show();
            return false;
        } else post.setGia(Integer.parseInt(_rentEditText.getText().toString()));

        if (_areaEditText.getText().toString().matches("")) {
            Toast.makeText(NewPostActivity.this, "Bạn chưa nhập diện tích cho thuê", Toast.LENGTH_SHORT).show();
            return false;
        } else post.setDientich(Integer.parseInt(_areaEditText.getText().toString()));

        if (_phoneEditText.getText().toString().matches("")) {
            Toast.makeText(NewPostActivity.this, "Bạn chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return false;
        } else post.setPhone(_phoneEditText.getText().toString());

        if (_descriptionEditText.getText().toString().matches("")) {
            Toast.makeText(NewPostActivity.this, "Bạn chưa nhập mô tả", Toast.LENGTH_SHORT).show();
            return false;
        } else post.setDescription((_descriptionEditText.getText().toString()));
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newpost_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_newpost_send:
                if (checkPost()) {
                } else {
                    Log.d(TAG, "onOptionsItemSelected: false");
                }
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                super.onOptionsItemSelected(item);
                break;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Log.d(TAG, "onSupportNavigateUp: back activity");
        return true;
    }

    // khi activity chup hinh hoan thanh thi ham nay se duoc goi
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 61) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng location = place.getLatLng();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void setDataFromFragment(String data) {
        _typeRoomButton.setText(data);
    }

    public interface FileUploadService {
        @POST("/post")
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
        builder.addFormDataPart("posttype", post.getPosttype());
        builder.addFormDataPart("typeroom", post.getTyperoom() );
        builder.addFormDataPart("gia", String.valueOf(post.getGia()));
        builder.addFormDataPart("dientich", String.valueOf(post.getDientich()));
        builder.addFormDataPart("address", post.getAddress());
        builder.addFormDataPart("phonepost", post.getPhone());
        try {
            for (String s : images) {
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
                Message message = response.body();
                if (message.getMessage().equals("Success")) {
                    Toast.makeText(NewPostActivity.this, "success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewPostActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, t.toString());
            }
        });
    }

}

