package com.example.homedy.NewPost.Image;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Image {
    public static ArrayList<Bitmap> bitmaps = new ArrayList<>();
    public static ArrayList<String> paths = new ArrayList<>();

    public static ArrayList<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public static ArrayList<String> getPaths() {
        return paths;
    }
}
