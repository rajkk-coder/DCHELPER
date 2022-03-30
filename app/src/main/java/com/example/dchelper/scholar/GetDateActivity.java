package com.example.dchelper.scholar;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class GetDateActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SlotAdapter slotAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slots);
        recyclerView=findViewById(R.id.slot_display);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Fetching data from the Firebase
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("slot").child("12-03-2022")
                .child("Chanakya");
        Query query=myRef.orderByKey();
        FirebaseRecyclerOptions<Slot> options=
                new FirebaseRecyclerOptions.Builder<Slot>()
                        .setQuery(query, Slot.class)
                        .build();
        //Sending data to the RecyclerView to display
        slotAdapter=new SlotAdapter(options);
        recyclerView.setAdapter(slotAdapter);




//        progressbar=findViewById(R.id.progress_bar);
//        progressbar.setProgress(50);
//        progressbar.setMax(100);

    }
    @Override
    protected void onStop() {
        super.onStop();
        slotAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        slotAdapter.startListening();
    }
}