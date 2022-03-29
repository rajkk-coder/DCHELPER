package com.example.dchelper.admin.faculty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.MainActivity;
import com.example.dchelper.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FacultyListActivity extends AppCompatActivity {
    FacultyAdapter facultyAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_list);
        FloatingActionButton button=(FloatingActionButton) findViewById(R.id.add_faculty);
         progressDialog=new ProgressDialog(this,R.style.CustomDialog);
        progressDialog.setMessage("Loading.....Please Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.show();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                }catch (Exception e){
//
//                }
//            }
//        }).start();
        //progressBar.setProgress(1000);
        button.setOnClickListener(view -> startActivity(new Intent(FacultyListActivity.this, AddFacultyActivity.class)));
        //FloatingActionButton add_faculty;
        recyclerView=findViewById(R.id.rv_faculty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayFaculty();



    }

    private void displayFaculty() {
        //For Scattering faculties
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("facultyList");
        Query query=myRef.orderByKey();
        FirebaseRecyclerOptions<Faculty> options =
                new FirebaseRecyclerOptions.Builder<Faculty>()
                        .setQuery(query, Faculty.class)
                        .build();
        facultyAdapter=new FacultyAdapter(options);
        recyclerView.setAdapter(facultyAdapter);
        progressDialog.dismiss();

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