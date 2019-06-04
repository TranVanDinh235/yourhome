package com.example.homedy;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BottomSheetMain extends BottomSheetDialogFragment {

    TextView post;
    TextView profile;
    TextView signout;
    SharedPreferences sharedPreferences;
    Listenner mlistener;

    public BottomSheetMain() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottomsheet_main, container, false);
        post = view.findViewById(R.id.fragment_bottom_sheet_post);
        profile = view.findViewById(R.id.fragment_bottom_sheet_profile);
        signout = view.findViewById(R.id.fragment_bottom_sheet_sign_out);

        sharedPreferences = ((MainActivity)getContext()).getSharedPreferences("user", Context.MODE_PRIVATE);

        profile.setTextColor(getResources().getColor(R.color.bottomsheettext));
        profile.setClickable(true);

        String check = sharedPreferences.getString("email", "des");
        if(!check.equals("des")){
            post.setTextColor(getResources().getColor(R.color.bottomsheettext));
            signout.setTextColor(getResources().getColor(R.color.bottomsheettext));
            post.setClickable(true);
            signout.setClickable(true);
            profile.setText("Trang cá nhân");
        }
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.Post();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.Profile();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.Signout();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomSheetMain.Listenner) {
            mlistener = (Listenner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Listener");
        }
    }

    interface Listenner{
        void Profile();
        void Post();
        void Signout();
    }
}
