package com.example.dchelper.scholar.panelMembers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.example.dchelper.scholar.adapters.PanelMemberAdapter;
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
    ToggleButton toggleButton;
    DatabaseReference db;
    FirebaseUser user;
    private String mode;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_panel_members);
        FloatingActionButton button=(FloatingActionButton) findViewById(R.id.add_panel_member);
        toggleButton=findViewById(R.id.toggleButton);

        recyclerView=findViewById(R.id.rv_panel_member);
        mode="DC";
        alertDialog=new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Loading");
        alertDialog.setMessage("Please wait...");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db=FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        displayPanelMember("DC");
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
                onToggleClick();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(managePanelMembers.this,addPanelMembers.class);
                Bundle bundle=new Bundle();
                bundle.putString("mode",mode);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    public void  onToggleClick(){
        if(toggleButton.isChecked()){
            mode="CE";
            toggleButton.setText("Comprehensive Exam");
            displayPanelMember(mode);
        }
        else {
            toggleButton.setText("DC Meeting");
            displayPanelMember("DC");
            mode="DC";
        }
    }

    private void displayPanelMember(String mode) {
       DatabaseReference myRef=db.child("scholars").child(user.getUid()).child("PanelMember").child(mode);
        Query query=myRef.orderByChild("FacultyName");
        FirebaseRecyclerOptions<PanelMember> options =
                new FirebaseRecyclerOptions.Builder<PanelMember>()
                        .setQuery(query, PanelMember.class)
                        .build();
        alertDialog.hide();
        panelMemberAdapter=new PanelMemberAdapter(options);
        panelMemberAdapter.startListening();
        recyclerView.setAdapter(panelMemberAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //panelMemberAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        panelMemberAdapter.stopListening();
    }
}