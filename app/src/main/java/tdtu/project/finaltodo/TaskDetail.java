package tdtu.project.finaltodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaskDetail extends AppCompatActivity {

    private TextView mtitiledetail,mdescriptiondetail;
    private TextView mdatedetail;
    FloatingActionButton mgotoedit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        mtitiledetail = findViewById(R.id.titledetail);
        mdescriptiondetail = findViewById(R.id.descriptiondetail);
        mdatedetail = findViewById(R.id.detailduedate);



        mgotoedit = findViewById(R.id.gotoedittask);

        Toolbar toolbar = findViewById(R.id.toolbartaskdetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();

        mgotoedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),edittaskactivity.class);

                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("description",data.getStringExtra("description"));
                intent.putExtra("due",data.getStringExtra("due"));
                intent.putExtra("taskId",data.getStringExtra("taskId"));

                v.getContext().startActivity(intent);

            }
        });

        mtitiledetail.setText(data.getStringExtra("title"));
        mdescriptiondetail.setText(data.getStringExtra("description"));
        mdatedetail.setText(data.getStringExtra("due"));


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