package com.example.homedy.Search;

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
import com.example.homedy.Home.HomeItem;
import com.example.homedy.R;

import java.util.ArrayList;

public class RecycleViewSearchAdapter extends RecyclerView.Adapter<RecycleViewSearchAdapter.ViewHolder> {
    private final String POSITION = "key_position";
    ArrayList<HomeItem> homeItemsSearch = HomeItem.getHomeItemsSearch();
    ArrayList<HomeItem> homeItems = HomeItem.getHomeItems();
    ArrayList<Integer> arr = new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public RecycleViewSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        convert();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_item, viewGroup, false);

        RecycleViewSearchAdapter.ViewHolder viewHolder = new RecycleViewSearchAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewSearchAdapter.ViewHolder viewHolder, int i) {
        HomeItem homeItem = homeItemsSearch.get(i);
        TextView textViewName = viewHolder._namePerson;
        TextView textViewDes = viewHolder._description;
        TextView textViewGia = viewHolder._gia;
        TextView textViewAddress = viewHolder._address;
        TextView textViewTitle = viewHolder._title;
        LinearLayout linearLayout = viewHolder.linearLayout;

//        if(homeItem.url_image.equals("default")){
//            imageView.setImageResource(R.drawable.image1);
//        }
        for(int index = 0; index < homeItem.getUrl_image().size(); index ++){
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setPadding(0, 0, 8,0);
            Glide.with(viewHolder.itemView).load(homeItem.getUrl_image().get(index)).into(imageView);
            linearLayout.addView(imageView);
        }


        String name = homeItem.getName() + " - " + homeItem.getTime();
        textViewName.setText(name);

        String gia = homeItem.getArea() + " m2 - " + homeItem.getGia() + " đ/tháng";
        textViewGia.setText(gia);

        String title = homeItem.getTitle();
        if(title.length() > 30) title = title.substring(0 , 27) + "...";
        textViewTitle.setText(title);

        String description = homeItem.getDescription();
        if(description.length() > 32) description = description.substring(0,30) + "...";
        textViewDes.setText(description);

        textViewAddress.setText(homeItem.getAddress());
    }

    @Override
    public int getItemCount() {
        return homeItemsSearch.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            intent.putExtra(POSITION, arr.get(getLayoutPosition()));
            v.getContext().startActivity(intent);
        }
    }

    public void convert(){
        for(int i = 0; i < homeItemsSearch.size(); i ++){
            for(int j = 0; j < homeItems.size(); j ++){
                if(homeItemsSearch.get(i).getId().equals(homeItems.get(j).getId())){
                    arr.add(j);
                }
            }
        }
    }
}
