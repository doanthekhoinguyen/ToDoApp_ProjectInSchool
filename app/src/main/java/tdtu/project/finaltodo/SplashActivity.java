package tdtu.project.finaltodo;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.ActionBar;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    //variables
    Animation topAnim,botAnim;
    ImageView imageView;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        //Hooks
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        imageView.setAnimation(topAnim);
        textView.setAnimation(botAnim);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        }, 3000);
    }
}