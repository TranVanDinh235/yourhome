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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.homedy.NewPost.DialogPost2Fragment;
import com.example.homedy.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int GALLERY_REQUEST_CODE = 98;

    private ArrayList<Bitmap> bitmaps = Image.getBitmaps();
    private ArrayList<String> paths = Image.getPaths();
    RecyclerView rvHomeItem;
    RecyclerViewImageAdapter recyclerViewImageAdapter = new RecyclerViewImageAdapter();


    private static final String TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
//        Toolbar toolbar = findViewById(R.id.toolbar_image);
//
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setTitle("Đăng bài");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);


        rvHomeItem = (RecyclerView) findViewById(R.id.rv_image);
        rvHomeItem.setAdapter(recyclerViewImageAdapter);
        rvHomeItem.setLayoutManager(new LinearLayoutManager(this));

        addImage();
        Button button = findViewById(R.id.btn_image_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });
    }

    public void addImage(){
        DialogPost2Fragment dialogPost2Fragment = DialogPost2Fragment.newInstance(1);
        dialogPost2Fragment.show(getSupportFragmentManager(), "");
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ID_IMAGE_CAPTURE:
//                    Bitmap bp = (Bitmap) data.getExtras().get("data");
//                    bitmaps.add(bp);
//                    Uri tempUri = getImageUri(getApplicationContext(), bp);
//                    String imagePath = getRealPathFromURI(tempUri);
//                    paths.add(imagePath);
//                    recyclerViewImageAdapter.notifyDataSetChanged();
                    File imgFile = new File(paths.get(paths.size()-1));
                    if(imgFile.exists()){
                        Bitmap myBitmap = null;
                        try {
                            myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(imgFile));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        bitmaps.add(myBitmap);
                        recyclerViewImageAdapter.notifyDataSetChanged();

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
                        recyclerViewImageAdapter.notifyDataSetChanged();
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
                        recyclerViewImageAdapter.notifyDataSetChanged();
                    }
                    break;

            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Action canced!!", Toast.LENGTH_LONG).show();
        } else Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public interface Update{
        void updateData();
    }

}
