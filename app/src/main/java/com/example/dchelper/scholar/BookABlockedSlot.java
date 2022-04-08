package com.example.dchelper.scholar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dchelper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookABlockedSlot extends AppCompatActivity {
    Button cancel;
    Button book;

    String date;
    String userStartTime;
    String userEndTime;
    String venue;
    String owner;
    String mode;
    String path;
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ablocked_slot);

        cancel=findViewById(R.id.book_a_block_cancel);
        book=findViewById(R.id.book_a_block_book);
        db=FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();
        date = bundle.getString("date");
        userStartTime = bundle.getString("start_time");
        userEndTime = bundle.getString("end_time");
        venue = bundle.getString("venue");
        owner = bundle.getString("owner");
        mode = bundle.getString("Mode");
        path=bundle.getString("path");
        TextView b_name=findViewById(R.id.book_a_blocked_name);
        b_name.setText("Name:"+owner);
        TextView b_venue=findViewById(R.id.book_a_blocked_venue);
        b_venue.setText("venue:"+venue);
        TextView b_mode=findViewById(R.id.book_a_blocked_mode);
        b_mode.setText("mode:"+mode);
        TextView b_date=findViewById(R.id.book_a_blocked_date);
        b_date.setText("Date:"+date);
        TextView b_time=findViewById(R.id.book_a_blocked_timing);
        b_time.setText("Time:"+userStartTime+"-" + userEndTime);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BookABlockedSlot.this, "cancelled", Toast.LENGTH_SHORT).show();
                cancelSlot();
                Intent intent=new Intent(BookABlockedSlot.this,FirstFragment.class);
                startActivity(intent);
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int[] restrict = {1};
                Map<String,Object>obj=new HashMap<>();
                obj.put("status","Booked");
                db.child("scholars").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UpComingEvent")
                .child(path).updateChildren(obj);

                db.child("slot").child(date).child(venue).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(restrict[0] ==1){
                            for(DataSnapshot dt:snapshot.getChildren()){
                                Slot slot = dt.getValue(Slot.class);
                                if(slot.getOwner().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                                    Map<String, Object>mp=new HashMap<>();
                                    mp.put("status","Booked");
                                    db.child("slot").child(date).child(venue).child(dt.getKey())
                                            .updateChildren(mp).addOnCompleteListener(task -> {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(BookABlockedSlot.this, "Slot booked successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                            restrict[0] +=1;
                        }
                        Intent intent=new Intent(BookABlockedSlot.this,FirstFragment.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }


    void cancelSlot(){
        final int[] restrict = {1};
        db.child("slot")
                .child(date)
                .child(venue).orderByChild("start_time")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> keys=new ArrayList<>();
                        String path="";
                        String new_slotStart_time = userStartTime;
                        String new_slotEnd_time=userEndTime;
                        if(snapshot.exists()){
                            for(DataSnapshot dt:snapshot.getChildren()){
                                Slot slot=dt.getValue(Slot.class);
                                if(slot.getEnd_time().equals(userStartTime) && slot.getOwner().equals("free")){
                                    keys.add(dt.getKey());
                                    new_slotStart_time=slot.getStart_time();
                                }
                                else if(slot.getStart_time().equals(userEndTime) && slot.getOwner().equals("free")){
                                    keys.add(dt.getKey());
                                    new_slotEnd_time=slot.getEnd_time();
                                }
                                if(slot.getStatus().equals("Blocked") && slot.getOwner().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                    path=dt.getKey();
                                }
                            }
                            for (int i=0;i<keys.size();i++)
                                db.child("slot").child(date).child(venue).child(keys.get(i)).removeValue();

                            //should be done only once
                            if(restrict[0] ==1){
                                //Remove slot from the corresponding date and venue
                                db.child("slot").child(date).child(venue).child(path)
                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                            Toast.makeText(BookABlockedSlot.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //Add new free slot
                                db.child("slot").child(date).child(venue)
                                    .push().setValue(new Slot("free",new_slotStart_time,new_slotEnd_time,venue,date,"free"));

                                //Remove slot from scholar's upcoming event
                                db.child("scholars")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("UpComingEvent")
                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(BookABlockedSlot.this, "Slot cancelled !!", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(BookABlockedSlot.this,FirstFragment.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                                restrict[0] +=1;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}