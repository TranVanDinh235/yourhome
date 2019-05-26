package com.example.homedy.Home;

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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.homedy.IPaddress;
import com.example.homedy.R;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.android.volley.VolleyLog.e;

public class HomeFragment extends Fragment {
    private static final String KEY_TAB = "key_tab";
    private String ip = IPaddress.getIp();
    private String url = ip + "post/getall";
    ArrayList<HomeItem> homeItems = HomeItem.getHomeItems();

    private RecyclerViewHomeAdapter recyclerViewHomeAdapter;
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

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null) {
                    Log.d(TAG, "onResponse: response");
                    if(homeItems.size() == 0) {
                        for (int i = 0; i < response.length(); i++) {
                            HomeItem homeItem = new HomeItem();
                            try {
                                JSONObject jsonObject = response.getJSONObject(response.length() - 1 - i);
                                homeItem.id = jsonObject.getString("_id");
                                homeItem.title = jsonObject.getString("title");
                                homeItem.description = jsonObject.getString("description");
                                homeItem.name = jsonObject.getString("provider");
                                homeItem.gia = jsonObject.getInt("gia");
                                homeItem.area = jsonObject.getInt("dientich");
                                JSONArray jsonArray = jsonObject.getJSONArray("images");
                                if (jsonArray.length() != 0)
                                    for(int j = 0; j < jsonArray.length(); j ++){
                                        homeItem.getUrl_image().add(ip + jsonArray.getString(j));
                                    }
                                else homeItem.getUrl_image().add("default");
                                homeItem.time = jsonObject.getString("time");
                                homeItem.address = jsonObject.getString("address");
                                homeItem.posttype = jsonObject.getString("posttype");
                                homeItem.phone = jsonObject.getString("phonepost");
                                homeItem.typeroom = jsonObject.getString("typeroom");
                                homeItem.lat = jsonObject.getDouble("lat");
                                homeItem.lng = jsonObject.getDouble("lng");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            homeItems.add(homeItem);
                        }
                    }
                    Log.d(TAG, String.valueOf(homeItems.size()));
                }
                Log.d(TAG, "onResponse: ");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });

        jsonArrayRequest.setTag(TAG);
        queue.add(jsonArrayRequest);

        // tri hoan main thread
        if(homeItems.size() == 0) {
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
        recyclerViewHomeAdapter = new RecyclerViewHomeAdapter();
        rvHomeItem.setAdapter(recyclerViewHomeAdapter);
        rvHomeItem.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    public void load(){
        recyclerViewHomeAdapter.notifyDataSetChanged();
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
