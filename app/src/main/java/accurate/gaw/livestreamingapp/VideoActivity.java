package accurate.gaw.livestreamingapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class VideoActivity extends BaseCameraActivity {

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, VideoActivity.class);
        activity.startActivity(intent);
    }

    Button mag1,mag2,mag3,mag4,mag5,aud;
    ImageButton img1,img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        onCreateActivity();
        videoWidth = 720;
        videoHeight = 1280;
        cameraWidth = 1280;
        cameraHeight = 720;
        mag1=findViewById(R.id.mag1);
        mag2=findViewById(R.id.mag2);
        mag3=findViewById(R.id.mag3);
        mag4=findViewById(R.id.mag4);
        mag5=findViewById(R.id.mag5);

        img1=findViewById(R.id.btn_back);
        img2=findViewById(R.id.btn_cross);

        aud =findViewById(R.id.audio);
        aud.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       startActivity(new Intent(getApplicationContext(),AudioActivity.class));

                                   }
                               }
        );


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VideoActivity.this,AfterLoginActivity.class));
                finish();
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VideoActivity.this,AfterLoginActivity.class));
                finish();
            }
        });
        mag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    clearChoice(v);
                    selectchoice(v);
            }
        });

        mag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChoice(v);
                selectchoice(v);
            }
        });

        mag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChoice(v);
                selectchoice(v);
            }
        });

        mag4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChoice(v);
                selectchoice(v);
            }
        });

        mag5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChoice(v);
                selectchoice(v);
            }
        });


    }

    private void clearChoice(View v) {





    }
    private void selectchoice(View v) {


    }


}
