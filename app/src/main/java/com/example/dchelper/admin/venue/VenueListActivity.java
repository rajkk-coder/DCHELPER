package com.example.dchelper.admin.venue;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.example.dchelper.admin.AdminDashboardActivity;
import com.example.dchelper.login.LoginActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class VenueListActivity extends AppCompatActivity {
    VenueAdapter venueAdapter;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, AdminDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_list);
        //FloatingActionButton add_venues
        FloatingActionButton button=findViewById(R.id.add_venue);

        button.setOnClickListener(view -> startActivity(new Intent(VenueListActivity.this, AddVenueActivity.class)));

        //For Scattering faculties
        RecyclerView recyclerView=findViewById(R.id.rv1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("venueList");
        Query query=myRef.orderByKey();
        FirebaseRecyclerOptions<Venue> options =
                new FirebaseRecyclerOptions.Builder<Venue>()
                        .setQuery(query, Venue.class)
                        .build();
        venueAdapter =new VenueAdapter(options);
        venueAdapter.startListening();
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