package accurate.gaw.livestreamingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
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

import accurate.gaw.livestreamingapp.retrofitpack.RetrofitService;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AudioActivity extends AppCompatActivity {


    ImageButton imageButton;
    ImageButton backBtn;
    RecyclerView recyclerView;
    SharedPreferences settings;
    List<MyListData> urllist;
    private static final String URL1 = "http://test-demo.co.in/shwe_wala/api/audio_list.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.statusbar_color));

        setContentView(R.layout.activity_audio);

        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, 1);
        settings = getSharedPreferences("ACCESS", Context.MODE_PRIVATE);

        backBtn = findViewById(R.id.backbtn);
        imageButton = findViewById(R.id.selectAudio);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent_upload = new Intent();
                intent_upload.setType("audio/*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_upload,1);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        urllist = new LinkedList<MyListData>();


        StringRequest request = new StringRequest(Request.Method.POST,URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CODE", response);
                Gson gson = new Gson();
                //Map<String,String> mp = gson.fromJson(response,Map.class);
                List<Map<String, String>> ls = gson.fromJson(response, List.class);
                Log.d("CODE1", ls.toString());

                for(Map<String, String> mp:ls){

                    urllist.add(new MyListData("Sound Name",mp.get("audio_url"),
                            "Duration - 00:00", android.R.drawable.ic_dialog_email));
                    //Log.d("CODEW", mp.get("video_url"));

                }


                MyListAdapter adapter = new MyListAdapter(urllist);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(AudioActivity.this));
                recyclerView.setAdapter(adapter);

                Map<String, String> mp = ls.get(0);

                String val = mp.get("video_url");
                /* tv.setText(val);*/


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "some thingh is wronguuu", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "view_audio");
                params.put("email", settings.getString("email","a4"));
                Log.d("Email",settings.getString("email","a4"));

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);


    }




    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                //the selected audio.
                Uri uri = data.getData();
                String filePath = getPath(uri);

                Log.d("filepath",filePath);

                RetrofitService retrofitService = new RetrofitService();
                retrofitService.uploadAudio(filePath,AudioActivity.this);

         /*       MediaMetadataRetriever metaRetriver;
                metaRetriver = new MediaMetadataRetriever();
                metaRetriver.setDataSource(uri.toString());
                String title = metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);*/
                //Toast.makeText(getApplicationContext(),uri.toString(),Toast.LENGTH_LONG).show();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        cursor.close();

        return path;
    }
}
