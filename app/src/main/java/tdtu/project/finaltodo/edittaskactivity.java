package tdtu.project.finaltodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class edittaskactivity extends AppCompatActivity {

    Intent data;
    private EditText medittitletask,meditdescriptiontask;
    private TextView meditduedate;
    private FloatingActionButton msaveedittask;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittaskactivity);
        medittitletask = findViewById(R.id.edittitle);
        meditdescriptiontask = findViewById(R.id.editdescription);
        meditduedate = findViewById(R.id.editduedate);
        msaveedittask = findViewById(R.id.saveedittaskfab);

        data = getIntent();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbaredittask);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        msaveedittask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newtitle = medittitletask.getText().toString();
                String newdescription = meditdescriptiontask.getText().toString();
                String newdate = meditduedate.getText().toString();

                if(newtitle.isEmpty() || newdescription.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Can not empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    DocumentReference documentReference = firebaseFirestore.collection("tasks").document(firebaseUser.getUid())
                            .collection("myTasks").document(data.getStringExtra("taskId"));
                    Map<String,Object> task = new HashMap<>();
                    task.put("title",newtitle);
                    task.put("description",newdescription);
                    task.put("due",newdate);
                    documentReference.set(task).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Task is updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(edittaskactivity.this,TaskActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        String tasktitle = data.getStringExtra("title");
        String taskDescription = data.getStringExtra("description");
        String taskduedate = data.getStringExtra("due");
        medittitletask.setText(tasktitle);
        meditdescriptiontask.setText(taskDescription);
        meditduedate.setText(taskduedate);

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