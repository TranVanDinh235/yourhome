package com.example.homedy.Search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.homedy.R;

public class ResultSearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        TextView textView = (TextView) findViewById(R.id.txt_result);
        textView.setText("Kết quả: ");


        RecyclerView rvSearchItem = (RecyclerView) findViewById(R.id.rv_search);
        SearchAdapter searchAdapter = new SearchAdapter();
        rvSearchItem.setAdapter(searchAdapter);
        rvSearchItem.setLayoutManager(new LinearLayoutManager(this));
    }
}
