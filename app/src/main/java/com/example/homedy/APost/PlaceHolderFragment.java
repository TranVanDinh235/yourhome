package com.example.homedy.APost;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.homedy.Home.HomeItem;
import com.example.homedy.IPaddress;
import com.example.homedy.R;

import java.util.ArrayList;

public class PlaceHolderFragment extends Fragment {
    private String ip = IPaddress.getIp();
    private static int numImage;
    private static int pos;
    ArrayList<HomeItem> homeItems = HomeItem.getHomeItems();
    private static final String KEY_IMAGE = "key_image";

    public PlaceHolderFragment(){}

    public static PlaceHolderFragment newInstance(int image, int position, int count){
        numImage = count;
        pos = position;
        PlaceHolderFragment fragment = new PlaceHolderFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vp_apost,container,false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.img_a_post);
//
//        switch (getArguments().getInt(KEY_IMAGE)) {
//            case 1:
//                imageView.setImageResource(R.drawable.image1);
//                break;
//            case 2:
//                imageView.setImageResource(R.drawable.image2);
//                break;
//            case 3:
//                imageView.setImageResource(R.drawable.image3);
//                break;
//            case 4:
//                imageView.setImageResource(R.drawable.image4);
//                break;
//            case 5:
//                imageView.setImageResource(R.drawable.image5);
//                break;
//                default:
//                    imageView.setImageResource(R.drawable.image1);
//                    break;
//        }
        HomeItem homeItem = homeItems.get(pos);
        for(int i = 0; i < numImage; i ++){
            if(getArguments().getInt(KEY_IMAGE) == i){
                Glide.with(this).load(homeItem.getUrl_image().get(i)).into(imageView);
            }
        }
        return rootView;
    }
}

