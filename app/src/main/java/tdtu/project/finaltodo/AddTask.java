package tdtu.project.finaltodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddTask extends AppCompatActivity {

    private TextInputEditText maddtitle,madddescription;
    private FloatingActionButton msavetask;
    private TextView setDuedate;
    String dueDate = "";
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setDuedate = findViewById(R.id.addduedate);
        msavetask = findViewById(R.id.savetaskfab);
        maddtitle = findViewById(R.id.addtitle);
        madddescription = findViewById(R.id.adddescription);

        Toolbar toolbar = findViewById(R.id.toolbaraddtask);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        setDuedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int MONTH = calendar.get(Calendar.MONTH);
                int YEAR = calendar.get(Calendar.YEAR);
                int DAY = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        setDuedate.setText(dayOfMonth + "/" + month + "/" + year);
                        dueDate = dayOfMonth + "/" + month +"/"+year;
                    }
                } , YEAR, MONTH ,DAY);

                datePickerDialog.show();
            }
        });


        msavetask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = maddtitle.getText().toString();
                String description = madddescription.getText().toString();
                if (title.isEmpty() || description.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Both title and description can not empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    DocumentReference documentReference = firebaseFirestore.collection("tasks").document(firebaseUser.getUid()).collection("myTasks").document();
                    Map<String,Object> task = new HashMap<>();

                    task.put("title",title);
                    task.put("description",description);
                    task.put("due",dueDate);

                    documentReference.set(task).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Task add Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddTask.this,TaskActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Task add Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}