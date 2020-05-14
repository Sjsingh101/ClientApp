package accurate.gaw.livestreamingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.chip.Chip;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextClock;
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

public class ProfileActivity extends AppCompatActivity {

    Chip homeChip,out;
    private static final String URL = "http://test-demo.co.in/shwe_wala/api/my_profile.php";
    TextView followingTextView;
    TextView followerTextView;
    TextView fanTextView;
    SharedPreferences settings;
    FloatingActionButton camerabtn;
    ImageView homeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.hide();*/

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.statusbar_color));


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        homeBtn = findViewById(R.id.profileHome);
        followingTextView = findViewById(R.id.followingTextView);
        followerTextView = findViewById(R.id.followerTextView);
        fanTextView = findViewById(R.id.fanTextView);
        camerabtn = findViewById(R.id.profilecamerafabbtn);


        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,AfterLoginActivity.class));
                finish();
            }
        });

        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoActivity.startActivity(ProfileActivity.this);
                finish();
            }
        });

        homeChip = findViewById(R.id.homeChip);
        out=findViewById(R.id.out);
        homeChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ProfileActivity.this,AfterLoginActivity.class));
                finish();
            }
        });


out.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        settings = getApplicationContext().getSharedPreferences("ACCESS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("flag", 0);
        editor.clear();
        editor.commit();
        Toast.makeText(getApplicationContext(),"Sign Out",Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(),SelectLoginActivity.class));
        finish();
    }
});
        StringRequest request = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("CODE", response);
                Gson gson = new Gson();
                //Map<String,String> mp = gson.fromJson(response,Map.class);
                List<Map<String, String>> ls = gson.fromJson(response, List.class);
                Map<String, String> mp = ls.get(0);
                String val = mp.get("message");
                followingTextView.setText(mp.get("no_of_following"));
                followerTextView.setText(mp.get("no_of_follower"));
                Log.d("Json", ls + "");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivity.this, "some thingh is wrong", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "my_profile");
                params.put("email", "nitinj99@gmail.com");

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        queue.add(request);

        //Toast.makeText(SingUp.this,emailEditText.getText().toString(), Toast.LENGTH_LONG).show();
    }



}
