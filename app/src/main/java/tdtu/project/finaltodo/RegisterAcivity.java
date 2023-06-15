package tdtu.project.finaltodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterAcivity extends AppCompatActivity {

    private TextInputEditText mregisteremail,mregisterpassword;
    private AppCompatButton mregister,mgotologin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acivity);

        getSupportActionBar().hide();

        mregisteremail = findViewById(R.id.registeremail);
        mregisterpassword = findViewById(R.id.registerpassword);
        mregister = findViewById(R.id.btn_register);
        mgotologin = findViewById(R.id.gotologin);

        firebaseAuth = FirebaseAuth.getInstance();

        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterAcivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mregisteremail.getText().toString().trim();
                String password = mregisterpassword.getText().toString().trim();
                //validation
                if(mail.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Email and password can not empty", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 6)
                {
                    Toast.makeText(getApplicationContext(), "Password need more than 6 characters", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //register to firebase
                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Register Successfull",Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Failed to Register",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }
    //send email verification
    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Verification email is sent,verify and login again",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(RegisterAcivity.this,MainActivity.class));
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Failed to send verification email",Toast.LENGTH_SHORT).show();
        }
    }
}