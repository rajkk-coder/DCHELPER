package com.example.dchelper.admin.faculty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dchelper.MainActivity;
import com.example.dchelper.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FacultyListActivity extends AppCompatActivity {
    FacultyAdapter facultyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_list);

        FloatingActionButton button=(FloatingActionButton) findViewById(R.id.add_faculty);
        button.setOnClickListener(new View.OnClickListener() {
                        @Override
            public void onClick(View view) {
                startActivity(new Intent(FacultyListActivity.this, MainActivity.class));
            }
        });
        //FloatingActionButton add_faculty;
        RecyclerView recyclerView=findViewById(R.id.rv_faculty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //For Scattering faculties
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("facultyList");
        Query query=myRef.orderByKey();

        FirebaseRecyclerOptions<Faculty> options =
                new FirebaseRecyclerOptions.Builder<Faculty>()
                        .setQuery(query, Faculty.class)
                        .build();
        if(options==null) {
            Toast.makeText(FacultyListActivity.this,"Maar jae",Toast.LENGTH_LONG).show();
        }
        facultyAdapter=new FacultyAdapter(options);
        recyclerView.setAdapter(facultyAdapter);


    }
    @Override
    protected void onStart() {
        super.onStart();
        facultyAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        facultyAdapter.stopListening();
    }

}