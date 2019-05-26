package com.example.homedy.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homedy.APost.APostActivity;
import com.example.homedy.R;

import java.util.ArrayList;

public class RecyclerViewHomeAdapter extends RecyclerView.Adapter<RecyclerViewHomeAdapter.ViewHolder> {
    private final String POSITION = "key_position";
    ArrayList<HomeItem> homeItems = HomeItem.getHomeItems();
    private Context context;
    @NonNull
    @Override
    public RecyclerViewHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHomeAdapter.ViewHolder viewHolder, int i) {
        HomeItem homeItem = homeItems.get(i);
        TextView textViewName = viewHolder._namePerson;
        TextView textViewDes = viewHolder._description;
        TextView textViewGia = viewHolder._gia;
        TextView textViewAddress = viewHolder._address;
        TextView textViewTitle = viewHolder._title;
        LinearLayout linearLayout = viewHolder.linearLayout;

//        if(homeItem.url_image.equals("default")){
//            imageView.setImageResource(R.drawable.image1);
//        }
        for(int index = 0; index < homeItem.url_image.size(); index ++){
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setPadding(0, 0, 8,0);
            Glide.with(viewHolder.itemView).load(homeItem.url_image.get(index)).into(imageView);
            linearLayout.addView(imageView);
        }



        String name = homeItem.name + " - " + homeItem.time;
        textViewName.setText(name);

        String gia = homeItem.area + " m2 - " + homeItem.gia + " đ/tháng";
        textViewGia.setText(gia);

        String title = homeItem.title;
        if(title.length() > 30) title = title.substring(0 , 27) + "...";
        textViewTitle.setText(title);

        String description = homeItem.description;
        if(description.length() > 32) description = description.substring(0,30) + "...";
        textViewDes.setText(description);

        textViewAddress.setText(homeItem.address);
    }

    @Override
    public int getItemCount() {
        return homeItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView _avatar;
        public TextView _namePerson;
        public TextView _title;
        public TextView _description;
        public TextView _gia;
        public TextView _address;
        public LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
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
    }
}
