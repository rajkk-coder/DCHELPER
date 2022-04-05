package com.example.dchelper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.admin.faculty.Faculty;
import com.example.dchelper.admin.faculty.FacultyAdapter;
import com.example.dchelper.scholar.Slot;
import com.example.dchelper.scholar.SlotAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FacultyAdapter facultyAdapter;
    RecyclerView recyclerView;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.demo_on_main);
        btn=findViewById(R.id.demo_btn);
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

    @Override
    public void onClick(View view) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("slot").child("14-03-2022")
                        .child("Chanakya");
                Query query = myRef.orderByKey();
                FirebaseRecyclerOptions<Slot> options =
                        new FirebaseRecyclerOptions.Builder<Slot>()
                                .setQuery(query, Slot.class)
                                .build();
               // slotAdapter = new SlotAdapter(options);
                //recyclerView.setAdapter(slotAdapter);
            }
        });
    }


}
