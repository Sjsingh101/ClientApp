package accurate.gaw.livestreamingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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


public class ManualLoginActivity extends AppCompatActivity {
    private static final String URL = "http://test-demo.co.in/shwe_wala/api/login.php";

    Button loginBtn;
    EditText emailEditText;
    SharedPreferences settings;
    EditText passwordEditText;
    TextView forgotPasswrod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        settings = getSharedPreferences("ACCESS", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        loginBtn = findViewById(R.id.loginBtn);

        forgotPasswrod = findViewById(R.id.textViewForgotPassword);

        forgotPasswrod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManualLoginActivity.this,ForgotPasswordActivity.class));

            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManualLoginActivity.this, emailEditText.getText().toString()+"   "+
                        passwordEditText.getText().toString() , Toast.LENGTH_SHORT).show();
                StringRequest request = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("CODE", response);
                        Gson gson = new Gson();
                        //Map<String,String> mp = gson.fromJson(response,Map.class);
                        List<Map<String, String>> ls = gson.fromJson(response, List.class);
                        Map<String, String> mp = ls.get(0);
                        //Toast.makeText(ManualLoginActivity.this,"welcome", Toast.LENGTH_LONG).show();
                        String val = mp.get("message");
                        if(val.equals("success")){
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putInt("flag", 1);
                            editor.putString("email",emailEditText.getText().toString());
                            editor.commit();
                            startActivity(new Intent(ManualLoginActivity.this,AfterLoginActivity.class));
                            finish();
                        }
                        Log.d("Json", val + "");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ManualLoginActivity.this, "some thingh is wrong", Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("action", "login");
                        params.put("email", emailEditText.getText().toString());
                        params.put("password", passwordEditText.getText().toString());
                        //params.put("confirm_password", "admin123");

                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(ManualLoginActivity.this);
                queue.add(request);

            }

        });


    }
}
