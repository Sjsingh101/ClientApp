package accurate.gaw.livestreamingapp;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class AfterLoginActivity extends AppCompatActivity {

    FloatingActionButton fab ;
    ImageView profileImg;
    String val;

    List<Video> urllist;
    Video[] myListData;
    RecyclerView recyclerView;
    SharedPreferences settings;
    private static final String URL1 = "http://test-demo.co.in/shwe_wala/api/video_list.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        settings = getSharedPreferences("ACCESS", Context.MODE_PRIVATE);
        recyclerView = findViewById(R.id.recyclerViewg);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));


        urllist = new LinkedList<Video>();


        /*StringRequest request = new StringRequest(Request.Method.POST,URL1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("CODE", response);
                    Gson gson = new Gson();
                    //Map<String,String> mp = gson.fromJson(response,Map.class);
                    List<Map<String, String>> ls = gson.fromJson(response, List.class);
                    Log.d("CODE1", ls.toString());

                    for(Map<String, String> mp:ls){
                      urllist.add(new Video(mp.get("video_url")));
                        //Log.d("CODEW", mp.get("video_url"));

                    }


                    Map<String, String> mp = ls.get(0);

                    val = mp.get("video_url");
                    *//* tv.setText(val);*//*

                    myListData = urllist.toArray(new Video[0]);


                    recyclerView.setAdapter(new VideoAdapter(myListData));
                   *//* Log.d("Json", ls + "");
                    Log.d("Json1", mp + "");
                    Log.d("Json2", val + "");*//*
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "some thingh is wrong", Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "view_video");
                    params.put("email", settings.getString("email","a3"));
                    Log.d("Email",settings.getString("email","a3"));

                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);*/


        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.statusbar_color));


       fab=findViewById(R.id.camerafab);


       fab=findViewById(R.id.camerafab);

       profileImg = findViewById(R.id.profileImg);


       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(AfterLoginActivity.this,VideoActivity.class));
               finish();
               //VideoActivity.startActivity(AfterLoginActivity.this);
           }
       });

       profileImg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(AfterLoginActivity.this,ProfileActivity.class));
           }
       });
    }




    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();


        urllist = new LinkedList<Video>();


        StringRequest request = new StringRequest(Request.Method.POST,URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CODE", response);
                Gson gson = new Gson();
                //Map<String,String> mp = gson.fromJson(response,Map.class);
                List<Map<String, String>> ls = gson.fromJson(response, List.class);
                Log.d("CODE1", ls.toString());

                for(Map<String, String> mp:ls){
                    urllist.add(new Video(mp.get("video_url")));
                    //Log.d("CODEW", mp.get("video_url"));

                }


                Map<String, String> mp = ls.get(0);

                val = mp.get("video_url");
                /* tv.setText(val);*/

                myListData = urllist.toArray(new Video[0]);


                recyclerView.setAdapter(new VideoAdapter(myListData));
                   /* Log.d("Json", ls + "");
                    Log.d("Json1", mp + "");
                    Log.d("Json2", val + "");*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "some thingh is wrong", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "view_video");
                params.put("email", settings.getString("email","a3"));
                Log.d("Email",settings.getString("email","a4"));

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);



    }
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 88888;

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        // request camera permission if it has not been grunted.
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {

            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AfterLoginActivity.this, "camera permission has been grunted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AfterLoginActivity.this, "[WARN] camera permission is not grunted.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {

        finish();
    }



}
