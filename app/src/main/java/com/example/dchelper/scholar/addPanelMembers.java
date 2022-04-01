package com.example.dchelper.scholar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dchelper.R;
import com.example.dchelper.admin.faculty.Faculty;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class addPanelMembers extends AppCompatActivity {
    addPanelMemberAdapter addpanelMemberAdapter;
    RecyclerView recyclerView;
    public ArrayList<String> panel=new ArrayList<>();
    Button add_panel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_panel_members);
        recyclerView=findViewById(R.id.rv_add_panel_member);
        add_panel=findViewById(R.id.button2_add);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addPanel();
        add_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(panel.isEmpty())
//                    for(int i=0;i<panel.size();i++)
//                        Toast.makeText(addPanelMembers.this,""+panel.get(i), Toast.LENGTH_SHORT).show();
//                else
                    Toast.makeText(addPanelMembers.this, "working", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPanel() {
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("facultyList");
        Query query=myRef.orderByKey();
        FirebaseRecyclerOptions<Faculty> options =
                new FirebaseRecyclerOptions.Builder<Faculty>()
                        .setQuery(query, Faculty.class)
                        .build();
        addpanelMemberAdapter=new addPanelMemberAdapter(options);
        recyclerView.setAdapter(addpanelMemberAdapter);
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        addpanelMemberAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        addpanelMemberAdapter.stopListening();
    }


}