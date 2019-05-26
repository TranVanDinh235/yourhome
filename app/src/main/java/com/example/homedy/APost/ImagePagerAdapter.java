package com.example.homedy.APost;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ImagePagerAdapter extends FragmentPagerAdapter {
    int position;
    int count;

    public ImagePagerAdapter(FragmentManager fm, int position, int count){
        super(fm);
        this.position = position;
        this.count = count;
    }


    @Override
    public Fragment getItem(int i) {
        return PlaceHolderFragment.newInstance(i, position, count);
    }

    @Override
    public int getCount() {
        return count;
    }

}