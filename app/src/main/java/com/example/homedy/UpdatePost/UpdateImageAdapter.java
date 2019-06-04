package com.example.homedy.UpdatePost;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homedy.Post;
import com.example.homedy.IPaddress;
import com.example.homedy.R;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class UpdateImageAdapter extends RecyclerView.Adapter<UpdateImageAdapter.ViewHolder>{
    private Post post;
    private ArrayList<Post> posts = Post.getHomeItemsPost();
    private String ip = IPaddress.getIp();

    public UpdateImageAdapter(int pos){
        Log.d(TAG, "UpdateImageAdapter: " + posts.size());
        post = posts.get(pos);
    }

    @NonNull
    @Override
    public UpdateImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_image, viewGroup, false);
        UpdateImageAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateImageAdapter.ViewHolder viewHolder, int i) {
        ImageView imageView = viewHolder._imageView;
        TextView textView = viewHolder._textView;
        Glide.with(viewHolder.itemView).load(ip + post.getUrl_image().get(i)).into(imageView);
    }

    @Override
    public int getItemCount() {
        return post.getUrl_image().size();
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
        post.getUrl_image().remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, post.getUrl_image().size());
    }
}
