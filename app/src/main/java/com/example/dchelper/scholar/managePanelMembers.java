package com.example.dchelper.scholar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dchelper.R;
import com.example.dchelper.admin.faculty.AddFacultyActivity;
import com.example.dchelper.admin.faculty.Faculty;
import com.example.dchelper.admin.faculty.FacultyAdapter;
import com.example.dchelper.admin.faculty.FacultyListActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class managePanelMembers extends AppCompatActivity {
    PanelMemberAdapter panelMemberAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_panel_members);
        FloatingActionButton button=(FloatingActionButton) findViewById(R.id.add_panel_member);
        button.setOnClickListener(view -> startActivity(new Intent(managePanelMembers.this, addPanelMembers.class)));
        recyclerView=findViewById(R.id.rv_panel_member);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayPanelMember();
    }

    private void displayPanelMember() {
       DatabaseReference myRef=FirebaseDatabase.getInstance().getReference().child("PanelMember");
        Query query=myRef.orderByKey();
        FirebaseRecyclerOptions<PanelMember> options =
                new FirebaseRecyclerOptions.Builder<PanelMember>()
                        .setQuery(query, PanelMember.class)
                        .build();
        panelMemberAdapter=new PanelMemberAdapter(options);
        recyclerView.setAdapter(panelMemberAdapter);
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        panelMemberAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        panelMemberAdapter.stopListening();
    }

}