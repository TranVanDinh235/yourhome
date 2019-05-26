package com.example.homedy.Account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homedy.IPaddress;
import com.example.homedy.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnAccountFragmentListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnAccountFragmentListener mListener;
    private String ip = IPaddress.getIp();
    private String url;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account1, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        ImageView avatar = (ImageView) view.findViewById(R.id.img_avatar_account);
        CircleImageView subAvatar = (CircleImageView) view.findViewById(R.id.img_small_avatar);
        TextView tvNamePerson = (TextView) view.findViewById(R.id.txt_name_person);
        TextView email = (TextView) view.findViewById(R.id.tv_email_account);

        EditText edtName = (EditText) view.findViewById(R.id.edt_name_person);
        EditText edtPhone = (EditText) view.findViewById(R.id.edt_phone_account);
        EditText edtFacebook = (EditText) view.findViewById(R.id.edt_facebook);
        EditText edtAddress = (EditText) view.findViewById(R.id.edt_address);

        if(sharedPreferences != null){
            tvNamePerson.setText(sharedPreferences.getString("name_user", "Homedy"));
            email.setText(sharedPreferences.getString("email", "homedy@gmail.com"));
            edtName.setText(sharedPreferences.getString("name_user", "Homedy"));
            edtName.setFocusable(false);
            edtPhone.setText(sharedPreferences.getString("phone", null));
            edtPhone.setFocusable(false);
            edtFacebook.setText(sharedPreferences.getString("facebook", null));
            edtFacebook.setFocusable(false);
            edtAddress.setText(sharedPreferences.getString("address", null));
            edtAddress.setFocusable(false);
            String urlAvatar = sharedPreferences.getString("avatar", null);
            if(urlAvatar != null) {
                url = ip + urlAvatar;
                Glide.with(this).load(url).into(avatar);
                Glide.with(this).load(url).into(subAvatar);
            }
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAccountFragmentListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAccountFragmentListener) {
            mListener = (OnAccountFragmentListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAccountFragmentListener {
        // TODO: Update argument type and name
        void onAccountFragmentListener(Uri uri);
    }

//    public void signout(){
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout, new LoginFragment());
//        fragmentTransaction.commit();
//    }

    public void update(){



    }
}
