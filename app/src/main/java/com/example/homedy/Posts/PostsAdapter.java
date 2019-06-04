package com.example.homedy.Posts;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homedy.APost.APostActivity;
import com.example.homedy.Post;
import com.example.homedy.IPaddress;
import com.example.homedy.MainActivity;
import com.example.homedy.R;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> implements BottomSheetFragment.PostListener {
    private final String POSITION = "key_position";
    private ArrayList<Post> posts;
    String ip = IPaddress.getIp();
    Context context;
    BottomSheetFragment bottomSheetDialog;

    public PostsAdapter(){}

    public PostsAdapter(Context context){
        this.context = context;
        posts = Post.getHomeItemsPost();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Post post = posts.get(i);
        TextView textViewName = viewHolder._namePerson;
        TextView textViewDes = viewHolder._description;
        TextView textViewGia = viewHolder._gia;
        TextView textViewAddress = viewHolder._address;
        TextView textViewTitle = viewHolder._title;

        LinearLayout linearLayout = viewHolder.linearLayout;

//        if(post.url_image.equals("default")){
//            imageView.setImageResource(R.drawable.image1);
//        }
        for(int index = 0; index < post.getUrl_image().size(); index ++){
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setPadding(0, 0, 8,0);
            Glide.with(viewHolder.itemView).load(ip + post.getUrl_image().get(index)).into(imageView);
            linearLayout.addView(imageView);
        }


        String name = post.getName() + " - " + post.getTime();
        textViewName.setText(name);

        String gia = post.getArea() + " m2 - " + post.getRent() + " đ/tháng";
        textViewGia.setText(gia);

        String title = post.getTitle();
        if(title.length() > 30) title = title.substring(0 , 27) + "...";
        textViewTitle.setText(title);

        String description = post.getDescription();
        if(description.length() > 32) description = description.substring(0,30) + "...";
        textViewDes.setText(description);

        textViewAddress.setText(post.getAddress());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void deletePost(int pos) {
        removeAt(pos);
        bottomSheetDialog.dismiss();
    }

    @Override
    public void updatePost(int pos) {
        bottomSheetDialog.dismiss();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public ImageView _avatar;
        public TextView _namePerson;
        public TextView _title;
        public TextView _description;
        public TextView _gia;
        public TextView _address;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout);
            _namePerson = (TextView) itemView.findViewById(R.id.txt_home_person);
            _title = (TextView) itemView.findViewById(R.id.txt_home_title);
            _description =(TextView) itemView.findViewById(R.id.txt_home_des);
            _gia = (TextView) itemView.findViewById(R.id.txt_home);
            _address = (TextView) itemView.findViewById(R.id.txt_address);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), APostActivity.class);
            intent.putExtra(POSITION, getLayoutPosition());
            v.getContext().startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            showBottomSheet();
            return true;
        }

        public void showBottomSheet(){
            Log.d(TAG, "showBottomSheet: ");
            bottomSheetDialog = BottomSheetFragment.newInstance(getLayoutPosition());
            bottomSheetDialog.show(((MainActivity)context).getSupportFragmentManager(), "Custom Bottom Sheet");
        }
    }

    private void removeAt(int position) {
        posts.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, posts.size());
    }
}
