package com.example.homedy;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.homedy.Account.AccountActivity;
import com.example.homedy.Account.LoginActivity;
import com.example.homedy.Home.HomeFragment;
import com.example.homedy.NewPost.NewPostActivity;
import com.example.homedy.Posts.PostFragment;
import com.example.homedy.Search.SearchActivity;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnHomeFragmentListener, PostFragment.OnPostFragmentListener,
        BottomSheetMain.Listenner {
    private static final int REQUEST_NEW_POST = 40;
    private static final int REQUEST_LOGIN = 47;
    private static final int REQUEST_ACCOUNT = 48;
    private static final String TAG = "main";
    private HomeFragment homeFragment;
    private PostFragment postFragment;
    BottomSheetMain bottomSheetMain;


    FloatingActionButton floatingActionButton;
    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        homeFragment = HomeFragment.newInstance(1);
        floatingActionButton = findViewById(R.id.fab_navigation);
        bottomAppBar = (BottomAppBar) findViewById(R.id.navigation);

        setSupportActionBar(bottomAppBar);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationSearch();
            }
        });

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetMain = new BottomSheetMain();
                bottomSheetMain.show(getSupportFragmentManager(), "Custom Bottom Sheet");
            }
        });


        fragmentTransaction.add(R.id.frame_layout, homeFragment);
        fragmentTransaction.commit();

    }

    private void navigationSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        Transition transition = new Explode();
        getWindow().setSharedElementEnterTransition(transition);
        getWindow().setSharedElementExitTransition(transition);
        ActivityOptions options;

        android.util.Pair<View, String> p2 = android.util.Pair.create((View)bottomAppBar, "navigation_transition");
        android.util.Pair<View, String> p1 = android.util.Pair.create((View)floatingActionButton, "fab_transition");
        View navBar = this.findViewById(android.R.id.navigationBarBackground);
        if (navBar != null) {
            android.util.Pair<View, String> p3 = android.util.Pair.create(navBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
            options = ActivityOptions.makeSceneTransitionAnimation(this, p1, p2, p3);
        }
        else options = ActivityOptions.makeSceneTransitionAnimation(this, p1, p2 );
        startActivity(intent, options.toBundle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    // ham thay the fragment
    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String check = sharedPreferences.getString("email", "des");
        switch (item.getItemId()) {
            case R.id.navigation_new:
                if (!check.equals("des")) {
                    Intent intent = new Intent(this, NewPostActivity.class);
                    startActivityForResult(intent, REQUEST_NEW_POST);
                }
                else Toast.makeText(this,"Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemPressed(String content) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void Profile() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String check = sharedPreferences.getString("email", "des");
        if (!check.equals("des")) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivityForResult(intent, REQUEST_ACCOUNT);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }
        bottomSheetMain.dismiss();
    }

    @Override
    public void Post() {
        postFragment = PostFragment.newInstance(3);
        loadFragment(postFragment);
        bottomSheetMain.dismiss();
    }

    @Override
    public void Signout() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        bottomSheetMain.dismiss();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_NEW_POST:
                    finish();
                    startActivity(getIntent());
                    Log.d(TAG, "onActivityResult: ");
                    break;
                case REQUEST_LOGIN:
                    finish();
                    startActivity(getIntent());
                    break;
            }
        }
    }
}
