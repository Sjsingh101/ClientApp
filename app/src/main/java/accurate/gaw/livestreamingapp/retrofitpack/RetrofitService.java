package accurate.gaw.livestreamingapp.retrofitpack;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import accurate.gaw.livestreamingapp.ServerResponse;
import accurate.gaw.livestreamingapp.dialogpack.DialogBoxCls;
import accurate.gaw.livestreamingapp.retrofitpack.retroconfig.ApiConfig;
import accurate.gaw.livestreamingapp.retrofitpack.retroconfig.AppConfig;
import accurate.gaw.livestreamingapp.retrofitpack.retroconfig.AudioApiConfig;
import accurate.gaw.livestreamingapp.retrofitpack.retroconfig.AudioAppConfig;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RetrofitService {

    private SharedPreferences settings ;
    DialogBoxCls dialog;
    public void uploadVideo(String postPath , Context context) {
        dialog = new DialogBoxCls();
        //Log.d("Post pathhh",postPath);
        settings = context.getSharedPreferences("ACCESS", Context.MODE_PRIVATE);;
        if (postPath == null || postPath.equals("")) {
            Toast.makeText(context, "please select an image ", Toast.LENGTH_LONG).show();
            Log.e("retro","baaas");
            return;
        } else {
            dialog.showpDialog(context);
        }
        // Map is used to multipart the file using okhttp3.RequestBody
        Map<String, RequestBody> map = new HashMap<>();
        File file = new File(postPath);
        //   File file = new File(String.valueOf(getFileStreamPath(postPath.getPath())));
        //Log.e("retro",file.getPath());
        // Parsing any Media type file
        Uri uri = Uri.parse("file://"+postPath);
        //Log.e("retro",uri.toString());


        RequestBody filetype =  RequestBody.create(MediaType.parse("video/mp4"),file);

        // RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        RequestBody emailBody = RequestBody.create(MultipartBody.FORM, settings.getString("email","a4"));
        RequestBody action = RequestBody.create(MultipartBody.FORM, "add_video");


        MultipartBody.Part filepart = MultipartBody.Part.createFormData("video_file",file.getName(),filetype);
   /*     if(RequestBody.create(MediaType.parse(getContentResolver().getType(uri)),file)==null){
            Log.e("null","null");
        }*/


            /*map.put("video_file", requestBody);
            map.put("email",emailBody);
            map.put("action",action);*/

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<ServerResponse> call = getResponse.upload2(action,emailBody,filepart);
        //Call<ServerResponse> call = getResponse.upload(map);

        call.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
               // Log.v("response0",response.body()+"");
                if (response.isSuccessful()){
                    if (response.body() != null){
                        //dialog.showpDialog(context);
                        dialog.hidepDialog();
                        //Log.v("response1",response.message());
                        ServerResponse serverResponse = response.body();
                        //Log.v("response2",serverResponse.getMessage());
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }else {
                    //Log.v("err",response.message());
                    Toast.makeText(getApplicationContext(), "problem uploading image!! 0ne", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                dialog.hidepDialog();
                Log.e("Response gotten is", t.getMessage());
                //Toast.makeText(getApplicationContext(), "problem uploading audio fuuuuuuuulllllll " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        }


        public void uploadAudio(String postPath , Context context) {

            getInfo(postPath);

            dialog = new DialogBoxCls();
            //Log.d("Post pathhh", postPath);
            settings = context.getSharedPreferences("ACCESS", Context.MODE_PRIVATE);
            ;
            if (postPath == null || postPath.equals("")) {
                Toast.makeText(context, "please select an image ", Toast.LENGTH_LONG).show();
                Log.e("retro", "baaas");
                return;
            } else {
                dialog.showpDialog(context);
            }
            // Map is used to multipart the file using okhttp3.RequestBody
            Map<String, RequestBody> map = new HashMap<>();
            File file = new File(postPath);

            Uri uri = Uri.parse("file://" + postPath);
            Log.e("retro", uri.toString());


            RequestBody filetype = RequestBody.create(MediaType.parse("audio/mp3"), file);

            // RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            RequestBody emailBody = RequestBody.create(MultipartBody.FORM, settings.getString("email", "a4"));
            RequestBody action = RequestBody.create(MultipartBody.FORM, "add_audio");


            MultipartBody.Part filepart = MultipartBody.Part.createFormData("audio_file", file.getName(), filetype);



            AudioApiConfig getResponse = AudioAppConfig.getRetrofit().create(AudioApiConfig.class);

            Call<ServerResponse> call = getResponse.upload2(action, emailBody, filepart);

            call.enqueue(new Callback<ServerResponse>() {

                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    Log.v("response0", response.body() + "");
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            dialog.hidepDialog();
                            //Log.v("response1", response.message());
                            ServerResponse serverResponse = response.body();


                        }
                    } else {
                        Log.v("err", response.message());
                        Toast.makeText(getApplicationContext(), "problem uploading image!! 0ne", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    dialog.hidepDialog();
                    Log.e("Response gotten is", t.getMessage());
                    //Toast.makeText(getApplicationContext(), "problem uploading image fuuuuuuuulllllll " + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }


        public void getInfo(String filePath){

            MediaMetadataRetriever metaRetriver;
            byte[] art;

            metaRetriver = new MediaMetadataRetriever(); metaRetriver.setDataSource(filePath);
            try { art = metaRetriver.getEmbeddedPicture();
                Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
                //album_art.setImageBitmap(songImage);
                Log.d("songAlbm",metaRetriver .extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                Log.d("songArtist",metaRetriver .extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                Log.d("songGEnre",metaRetriver .extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
            } catch (Exception e) {
                //album_art.setBackgroundColor(Color.GRAY);
                //album.setText("Unknown Album"); artist.setText("Unknown Artist"); genre.setText("Unknown Genre");
                e.printStackTrace();
            }

        }
}
