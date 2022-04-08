package com.example.dchelper.scholar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class ThirdFragment extends Fragment {

    public ThirdFragment(){

    }
    history_adapter h_adapter;
    FirebaseUser user;

    @Override
    public void onStart() {
        super.onStart();
        h_adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        h_adapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_third, container, false);
        user= FirebaseAuth.getInstance().getCurrentUser();

        RecyclerView recyclerView=view.findViewById(R.id.rv_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference()
                .child("scholars")
                .child(user.getUid())
                .child("History");

        Query query=myRef.orderByKey();
        FirebaseRecyclerOptions<Slot> options =
                new FirebaseRecyclerOptions.Builder<Slot>()
                        .setQuery(query, Slot.class)
                        .build();
        h_adapter =new history_adapter(options);
        recyclerView.setAdapter(h_adapter);
        return view;
    }
}