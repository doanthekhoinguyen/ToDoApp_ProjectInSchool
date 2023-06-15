package tdtu.project.finaltodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText mloginemail,mloginpassword;
    private AppCompatButton mlogin,mgotoregister,mgotoforgotpassword;

    private FirebaseAuth firebaseAuth;


    //variables
    Animation botAnim;
    ImageView imglogo;
    TextView tvlogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        botAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        mloginemail = findViewById(R.id.loginemail);
        mloginpassword = findViewById(R.id.loginpassword);
        mlogin = findViewById(R.id.btn_login);
        mgotoregister = findViewById(R.id.gotoregister);
        mgotoforgotpassword = findViewById(R.id.gotoforgotpassword);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        imglogo = findViewById(R.id.img_logoImage);
        tvlogo = findViewById(R.id.tv_logoName);

        imglogo.setAnimation(botAnim);
        tvlogo.setAnimation(botAnim);

        if(firebaseUser != null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,TaskActivity.class));
        }

        //when click this btn we will go to forgot password screen
        mgotoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterAcivity.class));
            }
        });
        //when click this btn we will go to register screen
        mgotoforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgotPassword.class));
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mloginemail.getText().toString().trim();
                String password = mloginpassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Email and password can not empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //login the user to firebase

                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {
                                checkmailverification();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Account does not exist",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


    }

    private void checkmailverification()
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser.isEmailVerified() == true)
        {
            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,TaskActivity.class));
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Verify your email first", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}