package com.example.homedy.Post;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.homedy.Home.HomeItem;
import com.example.homedy.IPaddress;
import com.example.homedy.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;


public class BottomSheetFragment extends BottomSheetDialogFragment {

    private static String POS = "pos";
    TextView edit;
    TextView delete;
    TextView share;
    TextView ok;
    TextView cancel;
    int pos;
    String ip = IPaddress.getIp();
    String url = ip + "post/delete";
    ArrayList<HomeItem> homeItems = HomeItem.getHomeItemsPost();
    PostListener postListener;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    public static BottomSheetFragment newInstance(int pos) {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        Bundle args = new Bundle();
        args.putInt(POS, pos);
        bottomSheetFragment.setArguments(args);
        return bottomSheetFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            pos = getArguments().getInt("pos", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottomsheet, container, false);
        edit = view.findViewById(R.id.fragment_bottom_sheet_edit);
        delete = view.findViewById(R.id.fragment_bottom_sheet_delete);
        share = view.findViewById(R.id.fragment_bottom_sheet_share);
        ok = view.findViewById(R.id.fragment_bottom_sheet_confirm_ok);
        cancel = view.findViewById(R.id.fragment_bottom_sheet_confirm_no);
        edit.setClickable(true);
        delete.setClickable(true);
        share.setClickable(true);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    public void delete(){
        delete.setTextColor(this.getResources().getColor(R.color.replyorange));
        ok.setText("Ok");
        ok.setClickable(true);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.Theme_AppCompat_DayNight_Dialog);
                progressDialog.setIndeterminate(true);
                HomeItem homeItem = homeItems.get(pos);
                String email = homeItem.getName();

                RequestQueue queue = Volley.newRequestQueue(getActivity());
                progressDialog.show();

                Map<String, String> postParam= new HashMap<String, String>();
                postParam.put("email", email);

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, new JSONObject(postParam),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                postListener.deletePost(pos);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                            }
                        }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                };

                jsonObjReq.setTag(TAG);
                // Adding request to request queue
                queue.add(jsonObjReq);
                afterdelete();
            }
        });
        cancel.setText("Cancel");
        cancel.setClickable(true);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterdelete();
            }
        });
    }

    public void afterdelete(){
        delete.setTextColor(this.getResources().getColor(R.color.replyorange));
        ok.setText("");
        ok.setClickable(false);
        cancel.setText("");
        cancel.setClickable(false);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof BottomSheetFragment.PostListener) {
//            postListener = (PostListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement Listener");
//        }
//    }
    interface PostListener{
        void deletePost(int pos);
    }
}
