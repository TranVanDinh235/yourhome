package com.example.homedy.Account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.homedy.ExternalType;
import com.example.homedy.IPaddress;
import com.example.homedy.R;
import com.example.homedy.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginFragment";
    private static final int REQUEST_SIGNUP = 0;
    private static final int RC_SIGN_IN = 20;
    private static final int REQUEST_ACCOUNT = 39;

    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private LoginButton loginFaceBookButton;
    private User nativelyUser;
    private User googleUser;
    private User facebookUser;
    private String ip = IPaddress.getIp();
    private String URL_LOGIN = ip + "user/";


    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singInNatively();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loginFaceBookButton = (LoginButton) findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();
        loginFaceBookButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loadFacebookUserProfile(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "login fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.printStackTrace();
                    }
                });


        ImageButton signInGoogle = (ImageButton) findViewById(R.id.btn_signin_gg);
        signInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        final ImageButton signInFaceBook = (ImageButton) findViewById(R.id.btn_signin_fb);
        signInFaceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == signInFaceBook){
                    loginFaceBookButton.performClick();
                }
            }
        });
    }


    public void signup(){
        Intent intent = new Intent(this , SignupActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }

    public void getProfile(User user, String url){
        Log.d(TAG, "getProfile: ");
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Đang đăng nhập....");


        RequestQueue queue = Volley.newRequestQueue(this);
        progressDialog.show();

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("external_type", user.getExternal_type());
        postParam.put("external_id", user.getExternal_id());
        postParam.put("email", user.getEmail());
        postParam.put("password", user.getPassword());
        postParam.put("urlavatar", user.getUrl_avatar());
        postParam.put("nameuser", user.getName_user());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response != null){
                            Log.d(TAG, "onResponse: success");
                            try {
                                    SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("email", response.getString("email"));
                                    editor.putString("password", response.getString("password"));
                                    editor.putString("name_user", response.getString("name_user"));
                                    editor.putString("phone", response.getString("phone"));
                                    editor.putString("avatar", response.getString("avatar"));
                                    editor.putString("address", response.getString("address"));
                                    editor.putString("typeuser", response.getString("external_type"));
                                    if(editor.commit()) getProfileSuccess();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else getProfileFailed();
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
        queue.add(jsonObjReq);
    }

    public void getProfileSuccess() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivityForResult(intent, REQUEST_ACCOUNT);
    }

    public void getProfileFailed(){
        Toast.makeText(this , "Dang nhap that bai", Toast.LENGTH_SHORT).show();
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

    private void singInNatively(){
        if(!validate()) {
            return;
        }
        _loginButton.setEnabled(false);

        nativelyUser = new User();
        nativelyUser.setExternal_type(ExternalType.nativelyUser);
        nativelyUser.setEmail(_emailText.getText().toString());
        nativelyUser.setPassword(_passwordText.getText().toString());
        String url = URL_LOGIN + "signin";
        getProfile(nativelyUser, url);
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOutGoogle() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "sign out", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInFaceBook() {

    }

    private void loadFacebookUserProfile(AccessToken newAccessToken){
        facebookUser = new User();
        GraphRequest request = GraphRequest.newMeRequest(
                newAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        facebookUser.setExternal_type(ExternalType.facebookUser);
                        facebookUser.setExternal_id(object.optString("id"));
                        facebookUser.setName_user(object.optString("name"));
                        facebookUser.setUrl_avatar("https://graph.facebook.com/"+ object.optString("id") + "/picture?type=large");
                        if(object.optString("email").equals(""))
                            facebookUser.setEmail(object.optString("id")+"@facebook.com");
                        else
                            facebookUser.setEmail(object.optString("email"));

                        String url = URL_LOGIN + "fbsignin";
                        getProfile(facebookUser, url);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields","id,name");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInGoogleResult(task);
        }
        else callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void handleSignInGoogleResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                googleUser = new User();
                googleUser.setExternal_type(ExternalType.googelUser);
                googleUser.setExternal_id(acct.getId());
                googleUser.setName_user(acct.getDisplayName());
                googleUser.setEmail(acct.getEmail());
                if(acct.getPhotoUrl() != null){
                    googleUser.setUrl_avatar(acct.getPhotoUrl().getPath());
                }
                String url = URL_LOGIN + "gosignin";
                getProfile(googleUser, url);
            }
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this , "Dang nhap that bai", Toast.LENGTH_SHORT).show();
        }
    }
}
