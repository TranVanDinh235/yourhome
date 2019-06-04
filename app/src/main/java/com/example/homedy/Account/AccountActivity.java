package com.example.homedy.Account;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.homedy.BottomSheetMain;
import com.example.homedy.IPaddress;
import com.example.homedy.MainActivity;
import com.example.homedy.NewPost.Image.Image;
import com.example.homedy.NewPost.Image.ImageActivity;
import com.example.homedy.NewPost.Image.ReadPathUtil;
import com.example.homedy.NewPost.NewPostActivity;
import com.example.homedy.NewPost.PickImageDialog;
import com.example.homedy.R;
import com.facebook.login.widget.LoginButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity implements BottomSheetAccount.Listener {

    private static final int REQUEST_HOME = 33;
    private String ip = IPaddress.getIp();
    private String url;
    private EditText edtName;
    private EditText edtPhone;
    private EditText edtFacebook;
    private EditText edtAddress;
    private ImageView avatar;
    FloatingActionButton floatingActionButton;
    BottomAppBar bottomAppBar;
    BottomSheetAccount bottomSheetAccount;
    SharedPreferences sharedPreferences;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int GALLERY_REQUEST_CODE = 98;
    ArrayList<String> paths = Image.getPaths();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        avatar = (ImageView) findViewById(R.id.img_avatar_account);
        CircleImageView subAvatar = (CircleImageView) findViewById(R.id.img_small_avatar);
        TextView tvNamePerson = (TextView) findViewById(R.id.txt_name_person);
        TextView email = (TextView) findViewById(R.id.tv_email_account);
        floatingActionButton = findViewById(R.id.fab_navigation);
        bottomAppBar = (BottomAppBar) findViewById(R.id.navigation);

        setSupportActionBar(bottomAppBar);

        paths.clear();
        edtName = (EditText) findViewById(R.id.edt_name_person);
        edtPhone = (EditText) findViewById(R.id.edt_phone_account);
        edtFacebook = (EditText) findViewById(R.id.edt_facebook);
        edtAddress = (EditText) findViewById(R.id.edt_address);


        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetAccount = new BottomSheetAccount();
                bottomSheetAccount.show(getSupportFragmentManager(), "Custom Bottom Sheet");
            }
        });

        floatingActionButton.hide();
        if (sharedPreferences != null) {
            tvNamePerson.setText(sharedPreferences.getString("name_user", "Homedy"));
            email.setText(sharedPreferences.getString("email", "homedy@gmail.com"));
            edtName.setText(sharedPreferences.getString("name_user", "Homedy"));
            edtName.setFocusable(false);
            edtPhone.setText(sharedPreferences.getString("phone", null));
            edtPhone.setFocusable(false);
            edtFacebook.setText(sharedPreferences.getString("facebook", null));
            edtFacebook.setFocusable(false);
            edtAddress.setText(sharedPreferences.getString("address", null));
            edtAddress.setFocusable(false);
            String urlAvatar = sharedPreferences.getString("avatar", null);
            if (urlAvatar != null) {
                url = ip + urlAvatar;
                Glide.with(this).load(url).into(avatar);
                Glide.with(this).load(url).into(subAvatar);
            }
            avatar.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    PickImageDialog pickImageDialog = PickImageDialog.newInstance(1);
                    pickImageDialog.show(getSupportFragmentManager(), "");
                    return true;
                }
            });
            avatar.setEnabled(false);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }


    public void update(){
        floatingActionButton.hide();
    }


    @Override
    public void edit() {
        edtName.setFocusableInTouchMode(true);
        edtAddress.setFocusableInTouchMode(true);
        edtPhone.setFocusableInTouchMode(true);
        edtFacebook.setFocusableInTouchMode(true);
        avatar.setEnabled(true);
        floatingActionButton.show();
        bottomSheetAccount.dismiss();
    }

    @Override
    public void changepass() {

    }

    @Override
    public void signout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        bottomSheetAccount.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivityForResult(intent, REQUEST_HOME);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ID_IMAGE_CAPTURE:
                    File imgFile = new File(paths.get(paths.size() - 1));
                    if (imgFile.exists()) {
                        Bitmap myBitmap = null;
                        try {
                            myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(imgFile));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        avatar.setImageBitmap(myBitmap);
                    }

                    break;

                case GALLERY_REQUEST_CODE:
                    if (data.getData() != null) {
                        //If uploaded with Android Gallery (max 1 image)
                        Uri imageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            avatar.setImageBitmap(bitmap);
                            paths.add(ReadPathUtil.getPath(AccountActivity.this, imageUri));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //If uploaded with the new Android Photos gallery
                        ClipData clipData = data.getClipData();
                        ClipData.Item item = clipData.getItemAt(0);
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), item.getUri());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        avatar.setImageBitmap(bitmap);
                        paths.add(ReadPathUtil.getPath(AccountActivity.this, item.getUri()));
                    }
                    break;

            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Action canced!!", Toast.LENGTH_LONG).show();
        } else Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
    }
}
