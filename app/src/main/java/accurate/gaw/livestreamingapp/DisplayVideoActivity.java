package accurate.gaw.livestreamingapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class DisplayVideoActivity extends AppCompatActivity {

    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_video);

        videoView = findViewById(R.id.vid);

        Intent intent = getIntent();
        videoView.setVideoURI(Uri.parse(intent.getStringExtra("uri")));

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                mp.start();
                mp.setLooping(true);


    /*            holder.videoView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            if(holder.videoView.isPlaying()){
                                holder.videoView.pause();
                            }
                            else{
                               // holder.videoView.resume();
                                holder.videoView.start();}
                        }
                        return false;
                    }
                });*/
            }
        });


    }
}
