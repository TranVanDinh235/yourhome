package com.example.homedy.Account;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.homedy.ExternalType;
import com.example.homedy.IPaddress;
import com.example.homedy.R;

public class BottomSheetAccount extends BottomSheetDialogFragment {
    private static String POS = "pos";
    TextView edit;
    TextView pass;
    TextView signout;

    int pos;
    String ip = IPaddress.getIp();
    String url = ip + "post/delete";
    Listener mListener;

    public BottomSheetAccount() {
        // Required empty public constructor
    }

    public static BottomSheetAccount newInstance(int pos) {
        BottomSheetAccount bottomSheetAccount = new BottomSheetAccount();
        Bundle args = new Bundle();
        args.putInt(POS, pos);
        bottomSheetAccount.setArguments(args);
        return bottomSheetAccount;
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
        View view = inflater.inflate(R.layout.fragment_bottomsheet_account, container, false);
        edit = view.findViewById(R.id.fragment_bottom_sheet_edit_account);
        pass = view.findViewById(R.id.fragment_bottom_sheet_changepass);
        signout = view.findViewById(R.id.fragment_bs_account_sign_out);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        String typeUser = sharedPreferences.getString("external_type", "default");

        edit.setClickable(true);
        if(typeUser != ExternalType.nativelyUser){
            pass.setTextColor(getResources().getColor(R.color.bottomsheettext));
        }
        else pass.setClickable(true);
        signout.setClickable(true);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.edit();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.signout();
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changepass();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomSheetAccount.Listener) {
            mListener = (BottomSheetAccount.Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Listener");
        }
    }

    interface Listener{
        void edit();
        void changepass();
        void signout();
    }
}
