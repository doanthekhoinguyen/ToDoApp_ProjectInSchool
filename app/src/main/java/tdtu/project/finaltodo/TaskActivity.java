package tdtu.project.finaltodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.Query;

public class TaskActivity extends AppCompatActivity {

    FloatingActionButton maddTaskfab;
    private FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<firebasemodel,TaskViewHolder> taskAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        getSupportActionBar().setTitle("TO DO APP");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));

        firebaseAuth = FirebaseAuth.getInstance();
        maddTaskfab = findViewById(R.id.addnotefab);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        maddTaskfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskActivity.this,AddTask.class));
            }
        });

        Query query = firebaseFirestore.collection("tasks").document(firebaseUser.getUid()).collection("myTasks").orderBy("due",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<firebasemodel> allusertasks = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        taskAdapter = new FirestoreRecyclerAdapter<firebasemodel, TaskViewHolder>(allusertasks) {
            @Override
            protected void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int position, @NonNull firebasemodel model) {

                ImageView popup = taskViewHolder.itemView.findViewById(R.id.menupopbutton);

                taskViewHolder.tasktitle.setText(model.getTitle());
                taskViewHolder.taskdescription.setText(model.getDescription());
                taskViewHolder.taskdate.setText(model.getDue());
                String docId = taskAdapter.getSnapshots().getSnapshot(position).getId();

                taskViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //open the task detail
                        Intent intent = new Intent(v.getContext(),TaskDetail.class);

                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("description",model.getDescription());
                        intent.putExtra("due",model.getDue());
                        intent.putExtra("taskId",docId);

                        v.getContext().startActivity(intent);

                    }
                });

                popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent intent = new Intent(v.getContext(),edittaskactivity.class);

                                intent.putExtra("title",model.getTitle());
                                intent.putExtra("description",model.getDescription());
                                intent.putExtra("due",model.getDue());
                                intent.putExtra("taskId",docId);

                                v.getContext().startActivity(intent);
                                return false;
                            }
                        });
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
                                builder.setTitle("Delete");
                                builder.setMessage("Are you sure to delete ?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        DocumentReference documentReference = firebaseFirestore.collection("tasks").document(firebaseUser.getUid()).collection("myTasks").document(docId);
                                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(v.getContext(), "This Task is deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(v.getContext(), "Failed to deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.create().show();
                                return false;
                            }
                        });

                        popupMenu.show();

                    }
                });
            }

            @NonNull
            @Override
            public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,parent,false);
                return new TaskViewHolder(view);
            }
        };

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(taskAdapter);


    }

    public class TaskViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tasktitle;
        private TextView taskdescription;
        private  TextView taskdate;
        LinearLayout mtask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            tasktitle = itemView.findViewById(R.id.tasktitle);
            taskdescription = itemView.findViewById(R.id.taskdescription);
            taskdate = itemView.findViewById(R.id.taskdate);
            mtask = itemView.findViewById(R.id.task);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(TaskActivity.this,MainActivity.class));
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        taskAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(taskAdapter != null)
        {
            taskAdapter.stopListening();
        }
    }
}