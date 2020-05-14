package accurate.gaw.livestreamingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

public class SelectLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button btn;

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    SharedPreferences settings;


    TextView siupText;
    ImageButton imageButton;
    //facebook
    CallbackManager callbackManager;
    //google
    ImageButton signInButton;
    GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_select_login);

    }


    @Override
    protected void onResume(){
        super.onResume();
        settings = getSharedPreferences("ACCESS",Context.MODE_PRIVATE);
        btn = findViewById(R.id.loginBtn);
        siupText = findViewById(R.id.signuptext);
        imageButton= findViewById(R.id.fbbtn);
        signInButton = findViewById(R.id.gmail);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signin();
            }
        });


        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                SharedPreferences.Editor editor = settings.edit();
                settings = getSharedPreferences("ACCESS", Context.MODE_PRIVATE);
                editor.putInt("flag", 1);
                editor.putString("email",loginResult.getAccessToken().getUserId());
                editor.commit();
                Toast.makeText(getApplicationContext(),loginResult.getAccessToken().getUserId(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(SelectLoginActivity.this,AfterLoginActivity.class);
                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        siupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectLoginActivity.this,SingUp.class));
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectLoginActivity.this, ManualLoginActivity.class));
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(SelectLoginActivity.this, Arrays.asList("user_photos", "email", "user_birthday", "public_profile"));
            }
        });

    }


    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(this, REQ_CODE+"", Toast.LENGTH_SHORT).show();
        Log.d("gmail reqCode",requestCode+"");

        if(requestCode==REQ_CODE){
            GoogleSignInResult googleSignInResult =Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleresult(googleSignInResult);
        }
        else{callbackManager.onActivityResult(requestCode, resultCode, data);}

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }
     private void Signin(){

       Intent intent= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
       startActivityForResult(intent,REQ_CODE);




     }
     private void Signout(){

     }
     private void handleresult(GoogleSignInResult googleSignInResult){
        Log.d("gmail Login ",googleSignInResult.isSuccess()+"");
         Toast.makeText(this, googleSignInResult.isSuccess()+"", Toast.LENGTH_SHORT).show();
        if(!googleSignInResult.isSuccess()){

            GoogleSignInAccount googleSignInAccount=googleSignInResult.getSignInAccount();
            //Toast.makeText(getApplicationContext(),googleSignInAccount.getDisplayName(),Toast.LENGTH_LONG).show();
           /* SharedPreferences.Editor editor = settings.edit();
            editor.putInt("flag", 1);
            editor.putString("email",googleSignInAccount.getEmail());
            editor.commit();*/
            startActivity(new Intent(SelectLoginActivity.this,AfterLoginActivity.class));
        }

     }
}
