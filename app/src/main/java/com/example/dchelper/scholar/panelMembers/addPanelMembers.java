package com.example.dchelper.scholar.panelMembers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.example.dchelper.admin.faculty.Faculty;
import com.example.dchelper.admin.faculty.FacultyListActivity;
import com.example.dchelper.scholar.adapters.PanelAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class addPanelMembers extends AppCompatActivity {
    RecyclerView recyclerView;
    public ArrayList<Faculty>faculties;
    DatabaseReference db=FirebaseDatabase.getInstance().getReference();
    PanelAdapter panelAdapter;
    private String mode;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, managePanelMembers.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        super.onBackPressed();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_panel_members);
        recyclerView=findViewById(R.id.rv_add_panel_member);
        recyclerView.setHasFixedSize(true);

        Bundle bundle=getIntent().getExtras();
        mode=bundle.getString("mode");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        faculties = new ArrayList<>();

        panelAdapter=new PanelAdapter(this,faculties,mode);
        recyclerView.setAdapter(panelAdapter);
        retrieveData();

    }

    private void retrieveData(){

        db.child("facultyList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot data:snapshot.getChildren()){
                        Faculty faculty= data.getValue(Faculty.class);
                        faculties.add(faculty);
                    }
                    panelAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}