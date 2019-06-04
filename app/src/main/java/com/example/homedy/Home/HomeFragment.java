package com.example.homedy.Home;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.homedy.IPaddress;
import com.example.homedy.Post;
import com.example.homedy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;
import static com.android.volley.VolleyLog.e;

public class HomeFragment extends Fragment {
    private static final String KEY_TAB = "key_tab";
    private String ip = IPaddress.getIp();
    private String url = ip + "post/getall";
    ArrayList<Post> posts = Post.getPosts();

    private HomeAdapter homeAdapter;
    private OnHomeFragmentListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(int tab) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_TAB, tab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = container.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Dang tai....");

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        progressDialog.show();

        posts.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null) {
                    Log.d(TAG, "onResponse: response");
                    if(posts.size() == 0) {
                        for (int i = 0; i < response.length(); i++) {
                            Post post = new Post();
                            try {
                                JSONObject jsonObject = response.getJSONObject(response.length() - 1 - i);
                                post.setId(jsonObject.getString("_id"));
                                post.setTitle(jsonObject.getString("title"));
                                post.setDescription(jsonObject.getString("description"));
                                post.setName(jsonObject.getString("provider"));
                                post.setRent(jsonObject.getInt("gia"));
                                post.setArea(jsonObject.getInt("dientich"));
                                JSONArray jsonArray = jsonObject.getJSONArray("images");
                                if (jsonArray.length() != 0)
                                    for(int j = 0; j < jsonArray.length(); j ++){
                                        post.getUrl_image().add(ip + jsonArray.getString(j));
                                    }
                                else post.getUrl_image().add("default");
                                post.setTime(jsonObject.getString("time"));
                                post.setAddress(jsonObject.getString("address"));
                                post.setPosttype(jsonObject.getString("posttype"));
                                post.setPhone(jsonObject.getString("phonepost"));
                                post.setTyperoom(jsonObject.getString("typeroom"));
                                post.setLat(jsonObject.getDouble("lat"));
                                post.setLng(jsonObject.getDouble("lng"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            posts.add(post);
                        }
                    }
                    Log.d(TAG, String.valueOf(posts.size()));
                }
                Log.d(TAG, "onResponse: ");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                progressDialog.dismiss();
            }
        });

        jsonArrayRequest.setTag(TAG);
        queue.add(jsonArrayRequest);

        // tri hoan main thread
        if(posts.size() == 0) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            load();
                            progressDialog.dismiss();
                        }
                    }, 1500);
        }
        else progressDialog.dismiss();
        RecyclerView rvHomeItem = (RecyclerView) view.findViewById(R.id.rv_home);
        homeAdapter = new HomeAdapter();
        rvHomeItem.setAdapter(homeAdapter);
        rvHomeItem.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    public void load(){
        homeAdapter.notifyDataSetChanged();
    }


    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onItemPressed(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeFragmentListener) {
            mListener = (OnHomeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnHomeFragmentListener {
        void onItemPressed(String content);
    }
}
