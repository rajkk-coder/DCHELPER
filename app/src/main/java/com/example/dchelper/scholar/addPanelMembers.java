package com.example.dchelper.scholar;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.example.dchelper.admin.faculty.Faculty;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_panel_members);
        recyclerView=findViewById(R.id.rv_add_panel_member);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        faculties = new ArrayList<>();

        panelAdapter=new PanelAdapter(this,faculties);
        recyclerView.setAdapter(panelAdapter);
        retrieveData();
        Toast.makeText(addPanelMembers.this, "hell "+faculties.size(), Toast.LENGTH_SHORT).show();
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