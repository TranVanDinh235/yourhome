package com.example.homedy.NewPost;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.homedy.NewPost.Image.Image;
import com.example.homedy.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DialogPost2Fragment extends DialogFragment {
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int GALLERY_REQUEST_CODE = 98;
    private static final int REQUEST_ID_AVATAR_IMAGE_CAPTURE = 101;
    private static final int AVATAR_GALLERY_REQUEST_CODE = 102;
    private static final String KEY_TAB = "key_tab";
    ArrayList<String> paths = Image.getPaths();
    String pictureImagePath = "";


    @InjectView(R.id.btn_dialog_camera) Button _cammeraDialogButton;
    @InjectView(R.id.btn_dialog_fileimage) Button _imageFileDialogButton;

    public static DialogPost2Fragment newInstance(int buttonId) {
        Bundle args = new Bundle();
        args.putInt(KEY_TAB, buttonId);
        DialogPost2Fragment fragment = new DialogPost2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_2, null);
        ButterKnife.inject(this, view);

        _cammeraDialogButton.setText("Camera");
        _cammeraDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
                dismiss();
            }
        });

        _imageFileDialogButton.setText("Thư viện");
        _imageFileDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
                dismiss();
            }
        });
        builder.setView(view);
        return builder.create();
    }


//    private void captureImage(int requestCode) {
//        askPermission();
//        // Tao mot intent khong tuong minh de yeu cau he thong mo camera chuan bi chup hinh
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        getActivity().startActivityForResult(intent, requestCode);
//    }

    private void captureImage() {
        askPermission();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        //Uri outputFileUri = Uri.fromFile(file);
        Uri outputFileUri = FileProvider.getUriForFile(getContext(),  "com.example.homedy.fileprovider" , file);
        paths.add(pictureImagePath);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        getActivity().startActivityForResult(cameraIntent, REQUEST_ID_IMAGE_CAPTURE);
    }

    private void pickFromGallery(){
        askPermission();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY_REQUEST_CODE);
    }

    private void askPermission(){
        if(Build.VERSION.SDK_INT > 23){
            int readPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int cameraPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            if(readPermission != PackageManager.PERMISSION_GRANTED || writePermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_ID_READ_WRITE_PERMISSION);
            }

        }

    }
    // khi yeu cau nguoi dung duoc tra ve (chap nhan hoac khong chap nhan)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_ID_READ_WRITE_PERMISSION:
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this.getActivity(), "Permisthion granted", Toast.LENGTH_LONG).show();
                    this.captureImage();
                } else {
                    Toast.makeText(this.getActivity(), "Permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }
}