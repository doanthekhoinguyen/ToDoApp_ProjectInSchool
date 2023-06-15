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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private TextInputEditText mforgotpassword;
    private AppCompatButton mpasswordrecoverbutton,mgobacktologin;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().hide();

        mforgotpassword = findViewById(R.id.forgotpassword);
        mpasswordrecoverbutton = findViewById(R.id.btn_recover);
        mgobacktologin = findViewById(R.id.btn_gotobacklogin);

        firebaseAuth =FirebaseAuth.getInstance();

        //when click btn will go to login screen
        mgobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this,MainActivity.class);
                startActivity(intent);
            }
        });
        //when click btn will go ro
        mpasswordrecoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mforgotpassword.getText().toString().trim();
                //validation email
                if(mail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Enter your mail first", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //send password recover
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Mail sent,You can recover password using email",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPassword.this,MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Mail is wrong or account not exist",Toast.LENGTH_SHORT).show();
                            }
                            
                        }
                    });
                }
            }
        });
    }
}