package accurate.gaw.livestreamingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class GetStart extends AppCompatActivity {

    Button btn;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        settings = getSharedPreferences("ACCESS", Context.MODE_PRIVATE);
        /*if(settings.getInt("flag",0)==1){
            startActivity(new Intent(GetStart.this,AfterLoginActivity.class));
        }*/


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.getStart_statusbar_color));

        setContentView(R.layout.activity_get_start);

        btn = findViewById(R.id.startBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GetStart.this,SplashScreenAcitvity.class));
                finish();
            }
        });

    }
}
