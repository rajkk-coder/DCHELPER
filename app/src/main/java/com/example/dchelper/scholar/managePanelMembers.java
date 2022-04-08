package com.example.dchelper.scholar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class managePanelMembers extends AppCompatActivity {
    PanelMemberAdapter panelMemberAdapter;
    RecyclerView recyclerView;
    DatabaseReference db;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_panel_members);

        FloatingActionButton button=(FloatingActionButton) findViewById(R.id.add_panel_member);

        recyclerView=findViewById(R.id.rv_panel_member);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db=FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        displayPanelMember();

        button.setOnClickListener(view -> startActivity(new Intent(managePanelMembers.this, addPanelMembers.class)));

    }

    private void displayPanelMember() {
       DatabaseReference myRef=db.child("scholars").child(user.getUid()).child("PanelMember").child("DC");
        Query query=myRef.orderByChild("FacultyName");
        FirebaseRecyclerOptions<PanelMember> options =
                new FirebaseRecyclerOptions.Builder<PanelMember>()
                        .setQuery(query, PanelMember.class)
                        .build();
        panelMemberAdapter=new PanelMemberAdapter(options);
        recyclerView.setAdapter(panelMemberAdapter);
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