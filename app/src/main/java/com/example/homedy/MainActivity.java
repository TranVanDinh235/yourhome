package com.example.homedy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.homedy.Account.AccountFragment;
import com.example.homedy.Account.LoginActivity;
import com.example.homedy.Account.LoginFragment;
import com.example.homedy.Home.HomeFragment;
import com.example.homedy.NewPost.NewPostActivity;
import com.example.homedy.Post.PostFragment;
import com.example.homedy.Search.SearchActivity;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnHomeFragmentListener, LoginFragment.OnLoginFragmentListener, AccountFragment.OnAccountFragmentListener, PostFragment.OnPostFragmentListener,
        BottomSheetMain.Listenner, BottomSheetAccount.Listener {
    private static final int REQUEST_NEW_POST = 40;
    private static final int REQUEST_LOGIN = 47;
    private AccountFragment accountFragment = AccountFragment.newInstance(null, null);
    private HomeFragment homeFragment = HomeFragment.newInstance(1);
    private LoginFragment loginFragment = LoginFragment.newInstance(false);
    private PostFragment postFragment = PostFragment.newInstance(3);
    BottomSheetMain bottomSheetMain;


    FloatingActionButton floatingActionButton;
    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        floatingActionButton = findViewById(R.id.fab_navigation);
        bottomAppBar = findViewById(R.id.navigation);

        setSupportActionBar(bottomAppBar);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationSearch();
                floatingActionButton.hide();
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
        Pair<View, String> p1 = Pair.create((View)bottomAppBar, "navigationbar");
        Pair<View, String> p2 = Pair.create((View)floatingActionButton, "floatactionbutton");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
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
    public void onLoginFragmentListenre(Uri uri) {
    }

    @Override
    public void onAccountFragmentListener(Uri uri) {

    }

    @Override
    public void Profile() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String check = sharedPreferences.getString("email", "des");
        if (!check.equals("des")) {
            loadFragment(accountFragment);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }
        bottomSheetMain.dismiss();
    }

    @Override
    public void Post() {
        Log.d("tag", "Post: ");
        loadFragment(postFragment);
        bottomSheetMain.dismiss();
    }

    @Override
    public void Signout() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }


    @Override
    public void edit() {
        bottomSheetMain.dismiss();
    }

    @Override
    public void changepass() {
        bottomSheetMain.dismiss();
    }

    @Override
    public void signout() {
        bottomSheetMain.dismiss();
    }
}
