package com.example.dchelper.admin.venue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dchelper.MainActivity;
import com.example.dchelper.R;
import com.example.dchelper.admin.faculty.Faculty;
import com.example.dchelper.admin.faculty.FacultyAdapter;
import com.example.dchelper.admin.faculty.FacultyListActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class VenueListActivity extends AppCompatActivity {
    VenueAdapter venueAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_list);

        //FloatingActionButton add_venues
        FloatingActionButton button=findViewById(R.id.add_venue);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VenueListActivity.this, AddVenueActivity.class));
            }
        });


        RecyclerView recyclerView=findViewById(R.id.rv1);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //For Scattering faculties
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("venueList");
        Query query=myRef.orderByKey();

        FirebaseRecyclerOptions<Venue> options =
                new FirebaseRecyclerOptions.Builder<Venue>()
                        .setQuery(query, Venue.class)
                        .build();
        if(options==null) {
            Toast.makeText(VenueListActivity.this,"Maar jae",Toast.LENGTH_LONG).show();
        }
        venueAdapter =new VenueAdapter(options);
        recyclerView.setAdapter(venueAdapter);


    }
    @Override
    protected void onStart() {
        super.onStart();
        venueAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        venueAdapter.stopListening();
    }
}