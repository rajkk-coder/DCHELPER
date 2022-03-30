package com.example.dchelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dchelper.admin.faculty.Faculty;
import com.example.dchelper.admin.faculty.FacultyAdapter;
import com.example.dchelper.admin.faculty.FacultyListActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MainActivity extends AppCompatActivity {
    FacultyAdapter facultyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView=findViewById(R.id.demo_on_main);
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("facultyList");
        Query query=myRef.orderByKey();
        FirebaseRecyclerOptions<Faculty> options =
                new FirebaseRecyclerOptions.Builder<Faculty>()
                        .setQuery(query, Faculty.class)
                        .build();
        facultyAdapter=new FacultyAdapter(options);
        recyclerView.setAdapter(facultyAdapter);
    }

}