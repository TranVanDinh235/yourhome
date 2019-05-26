package com.example.homedy.Account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.homedy.IPaddress;
import com.example.homedy.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class LoginFragment extends Fragment {
    private static final String IS_LOGIN = "is_login";
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "LoginFragment";
    private static final int REQUEST_SIGNUP = 0;
    private final int RC_SIGN_IN = 20;



    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;


    private String mParam1;
    private Boolean isLogin;

    private OnLoginFragmentListener mListener;
    private String ip = IPaddress.getIp();
    private String URL_LOGIN = ip + "user/signin";

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(boolean isLogin) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_LOGIN, isLogin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isLogin = getArguments().getBoolean(IS_LOGIN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this,view);


        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SignupActivity.class );
                getActivity().startActivityForResult(intent, REQUEST_SIGNUP);

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

//        SignInButton signInButton =  view.findViewById(R.id.btn_signin_gg);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);
        ImageButton signInButton = (ImageButton) view.findViewById(R.id.btn_signin_gg);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onLoginFragmentListenre(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentListener) {
            mListener = (OnLoginFragmentListener) context;
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

    public interface OnLoginFragmentListener {
        // TODO: Update argument type and nme
        void onLoginFragmentListenre(Uri uri);
    }

    public void login(){
        Log.d(TAG,"login");

        if(!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authencating....");

        final String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        progressDialog.show();

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("email", email);
        postParam.put("password", password);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL_LOGIN, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response != null){
                            try {
                                if(!response.isNull("email")){
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("email", response.getString("email"));
                                    editor.putString("password", response.getString("password"));
                                    editor.putString("name_user", response.getString("name_person"));
                                    editor.putString("phone", response.getString("phone"));
                                    editor.putString("avatar", response.getString("avatar"));
                                    editor.putString("facebook", response.getString("facebook"));
                                    editor.putString("address", response.getString("address"));
                                    editor.apply();
                                    onLoginSuccess();
                                }
                                else onLoginFailed();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else onLoginFailed();
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
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

        jsonObjReq.setTag(TAG);
        // Adding request to request queue
        queue.add(jsonObjReq);
    }

    public void onLoginSuccess() {
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout, new AccountFragment());
//        fragmentTransaction.commit();
    }

    public void onLoginFailed(){
        Toast.makeText(getActivity(), "Dang nhap that bai", Toast.LENGTH_SHORT).show();
    }

    public boolean validate(){
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 16) {
            _passwordText.setError("between 6 and 16 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Toast.makeText(getActivity(), "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getActivity(), "Dang nhap that bai", Toast.LENGTH_SHORT).show();
        }
    }
}
