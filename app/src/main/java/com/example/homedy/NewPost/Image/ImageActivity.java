package com.example.homedy.NewPost.Image;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.homedy.Post;
import com.example.homedy.NewPost.PickImageDialog;
import com.example.homedy.R;
import com.example.homedy.UpdatePost.UpdateImageAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int GALLERY_REQUEST_CODE = 98;
    private static final String POS = "pos";

    private ArrayList<Bitmap> bitmaps = Image.getBitmaps();
    private ArrayList<String> paths = Image.getPaths();
    private ArrayList<Post> posts = Post.getHomeItemsPost();
    private Post post;
    private int position;
    RecyclerView rvImage;
    RecyclerView rvImageUpdate;
    ImageAdapter imageAdapter;
    UpdateImageAdapter updateImageAdapter;
    FloatingActionButton floatingActionButton;
    BottomAppBar bottomAppBar;


    private static final String TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        final Intent intent = this.getIntent();
        final Bundle bundle = intent.getBundleExtra("images");
        position = bundle.getInt(POS, 0);
        if(position != -1) {
            post = posts.get(position);
            rvImageUpdate = (RecyclerView) findViewById(R.id.rv_image_update);
            updateImageAdapter = new UpdateImageAdapter(position);
            rvImageUpdate.setAdapter(updateImageAdapter);
            rvImageUpdate.setLayoutManager(new LinearLayoutManager(this));
        }

        floatingActionButton = findViewById(R.id.fab_navigation);
        bottomAppBar = (BottomAppBar) findViewById(R.id.navigation);

        setSupportActionBar(bottomAppBar);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvImage = (RecyclerView) findViewById(R.id.rv_image);
        imageAdapter = new ImageAdapter();
        rvImage.setAdapter(imageAdapter);
        rvImage.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addImage(){
        PickImageDialog pickImageDialog = PickImageDialog.newInstance(1);
        pickImageDialog.show(getSupportFragmentManager(), "");
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ID_IMAGE_CAPTURE:
                    File imgFile = new File(paths.get(paths.size()-1));
                    if(imgFile.exists()){
                        Bitmap myBitmap = null;
                        try {
                            myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(imgFile));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        bitmaps.add(myBitmap);
                        imageAdapter.notifyDataSetChanged();

                    }

                    break;

                case GALLERY_REQUEST_CODE:
                    if (data.getData() != null) {
                        //If uploaded with Android Gallery (max 1 image)
                        Uri imageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            bitmaps.add(bitmap);
                            paths.add(ReadPathUtil.getPath(ImageActivity.this, imageUri));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imageAdapter.notifyDataSetChanged();
                    } else {
                        //If uploaded with the new Android Photos gallery
                        ClipData clipData = data.getClipData();
                        for(int index = 0; index < clipData.getItemCount(); index ++){
                            ClipData.Item item = clipData.getItemAt(index);
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), item.getUri());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            bitmaps.add(bitmap);
                            paths.add(ReadPathUtil.getPath(ImageActivity.this, item.getUri()));
                        }
                        imageAdapter.notifyDataSetChanged();
                    }
                    break;

            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Action canced!!", Toast.LENGTH_LONG).show();
        } else Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();

    }
}
