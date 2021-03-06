package com.example.homedy.Posts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.homedy.Account.BottomSheetAccount;
import com.example.homedy.Post;
import com.example.homedy.IPaddress;
import com.example.homedy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

import static com.android.volley.VolleyLog.TAG;
public class PostFragment extends Fragment{
    private static final int REQUEST_NEW_POST = 40;
    private static final String KEY_TAG = "key_tag";
    private int keyTag;
    private OnPostFragmentListener mListener;
    private PostsAdapter postsAdapter;
    String ip = IPaddress.getIp();
    String url = ip + "post/getpost";
    ArrayList<Post> posts = Post.getHomeItemsPost();


    public PostFragment() {
    }

    public static PostFragment newInstance(int tag) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_TAG, tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            keyTag = getArguments().getInt(KEY_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_post, container, false);
        ButterKnife.inject(this,view);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Dang tai....");

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("email", sharedPreferences.getString("email", "trandinhcn@gmail.com"));
        posts.clear();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        progressDialog.show();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject(postParam);
        jsonArray.put(jsonObject);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray ,new Response.Listener<JSONArray>() {
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
                                        post.getUrl_image().add(jsonArray.getString(j));
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
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

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
                    }, 1000);
        }
        else progressDialog.dismiss();

        RecyclerView rvPostItem = (RecyclerView) view.findViewById(R.id.rv_post);
        postsAdapter = new PostsAdapter(this.getContext());
        rvPostItem.setAdapter(postsAdapter);
        rvPostItem.setLayoutManager(new LinearLayoutManager(this.getContext()));


        return view;
    }


    public void load(){
        postsAdapter.notifyDataSetChanged();
    }


    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onItemPressed(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPostFragmentListener) {
            mListener = (OnPostFragmentListener) context;
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


    public interface OnPostFragmentListener {
        // TODO: Update argument type and name
        void onItemPressed(String content);
    }
}
