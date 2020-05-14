package accurate.gaw.livestreamingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingUp extends AppCompatActivity {

    Button signup;
    SharedPreferences settings;
    private static final String URL = "http://test-demo.co.in/shwe_wala/api/register.php";
    EditText emailEditText,passwordEditText,confirmEditText;
    TextView btnBackToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_sing_up);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmEditText = findViewById(R.id.confirmPassword);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SingUp.this, ManualLoginActivity.class));
                finish();
            }
        });


        signup = findViewById(R.id.signupbtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest request = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("CODE", response);
                        Gson gson = new Gson();
                        //Map<String,String> mp = gson.fromJson(response,Map.class);
                        List<Map<String, String>> ls = gson.fromJson(response, List.class);
                        Map<String, String> mp = ls.get(0);
                        String val = mp.get("message");
                        /* tv.setText(val);*/
                        if(mp.get("message").equals("Registered Successfully")){
                            /*SharedPreferences.Editor editor = settings.edit();
                            settings = getSharedPreferences("ACCESS", Context.MODE_PRIVATE);
                            editor.putInt("flag", 1);
                            editor.putString("email",emailEditText.getText().toString());
                            editor.commit();*/
                            startActivity(new Intent(SingUp.this, ManualLoginActivity.class));
                            finish();
                        }
                        else if(val.equals("Email Already Exists")){
                            showDialoag("Email Already Exists");
                        }
                        Log.d("Json", ls + "");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SingUp.this, "some thingh is wrong", Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("action", "register");
                        params.put("email", emailEditText.getText().toString());
                        params.put("password", passwordEditText.getText().toString());
                        params.put("confirm_password", confirmEditText.getText().toString());

                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(SingUp.this);
                queue.add(request);

                Toast.makeText(SingUp.this,emailEditText.getText().toString(), Toast.LENGTH_LONG).show();
            }

        });

               // startActivity(new Intent(SingUp.this,ManualLoginActivity.class));

    }

    public void showDialoag(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage(message);
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //alertDialog.dismiss();
                                finish();
                            }
                        });


        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}
