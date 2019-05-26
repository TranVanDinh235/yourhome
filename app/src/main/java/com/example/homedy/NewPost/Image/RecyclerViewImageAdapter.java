package com.example.homedy.NewPost.Image;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homedy.R;

import java.util.ArrayList;

public class RecyclerViewImageAdapter extends RecyclerView.Adapter<RecyclerViewImageAdapter.ViewHolder> {
    private ArrayList<Bitmap> bitmaps = Image.getBitmaps();

    @NonNull
    @Override
    public RecyclerViewImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_image, viewGroup, false);

        RecyclerViewImageAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewImageAdapter.ViewHolder viewHolder, int i) {
        Bitmap bitmap = bitmaps.get(i);
        ImageView imageView = viewHolder._imageView;
        TextView textView = viewHolder._textView;
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView _imageView;
        public TextView _textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _imageView = (ImageView) itemView.findViewById(R.id.imv_frg_image);
            _textView = (TextView) itemView.findViewById(R.id.txt_frg_image);
        }
    }
}
