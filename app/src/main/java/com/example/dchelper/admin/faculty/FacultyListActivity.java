package com.example.dchelper.admin.faculty;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FacultyListActivity extends AppCompatActivity {
    FacultyAdapter facultyAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_list);
        FloatingActionButton button=(FloatingActionButton) findViewById(R.id.add_faculty);

        button.setOnClickListener(view -> startActivity(new Intent(FacultyListActivity.this, AddFacultyActivity.class)));

        //RecyclerView to display data obtained from the Firebase real-time database
        recyclerView=findViewById(R.id.rv_faculty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayFaculty();


    }

    private void displayFaculty() {

        //Data retrieval from Firebase real-time database
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("facultyList");
        Query query=myRef.orderByKey();
        FirebaseRecyclerOptions<Faculty> options =
                new FirebaseRecyclerOptions.Builder<Faculty>()
                        .setQuery(query, Faculty.class)
                        .build();

        facultyAdapter=new FacultyAdapter(options);
        facultyAdapter.startListening();
        recyclerView.setAdapter(facultyAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        facultyAdapter.stopListening();
    }


}