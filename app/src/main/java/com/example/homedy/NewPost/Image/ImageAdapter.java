package com.example.homedy.NewPost.Image;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homedy.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private ArrayList<Bitmap> bitmaps = Image.getBitmaps();
    private ArrayList<String> path = Image.getPaths();

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_image, viewGroup, false);

        ImageAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder viewHolder, int i) {
        Bitmap bitmap = bitmaps.get(i);
        ImageView imageView = viewHolder._imageView;
        TextView textView = viewHolder._textView;
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView _imageView;
        public TextView _textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            _imageView = (ImageView) itemView.findViewById(R.id.imv_frg_image);
            _textView = (TextView) itemView.findViewById(R.id.txt_frg_image);

            _textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == _textView.getId()){
                removeAt(getAdapterPosition());
            }
        }
    }
    private void removeAt(int position) {
        bitmaps.remove(position);
        path.remove(position);
        Log.d("remove", bitmaps.size() + " " + path.size());
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, bitmaps.size());
    }
}
