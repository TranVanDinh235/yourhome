package com.example.homedy.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.homedy.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomWindowInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context context;

    public CustomWindowInfoAdapter(Context context) {
        this.mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
        this.context = context;
    }

    private void rendowWindowText(Marker marker, View view){
        String title = marker.getTitle();
        String snippet = marker.getSnippet();

        TextView tvTitle = view.findViewById(R.id.window_info_title);
        TextView tvSnippet = view.findViewById(R.id.window_snippet);


        if(!title.equals("")){
            tvTitle.setText(title);
        }

        if(!title.equals("")){
            tvSnippet.setText(snippet);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }
}
