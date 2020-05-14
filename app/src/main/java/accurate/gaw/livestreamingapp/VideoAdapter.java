package accurate.gaw.livestreamingapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{
    private Video[] listdata;
    Intent intent;

    // RecyclerView recyclerView;
    public VideoAdapter(Video[] listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.videotile, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Video myListData = listdata[position];
        if (listdata[position].getVideoUrl()!=null) {
            holder.videoView.setVideoURI(Uri.parse(listdata[position].getVideoUrl()));
            holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                    mp.start();
                    mp.setVolume(0, 0);
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

            holder.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    intent = new Intent(v.getContext(), DisplayVideoActivity.class);
                    intent.putExtra("uri", myListData.getVideoUrl());
                    v.getContext().startActivity(intent);
                }
            });


        }
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public VideoView videoView;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.videoView =(VideoView)itemView.findViewById(R.id.vtile);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.Vlinearlayout);
        }
    }
}