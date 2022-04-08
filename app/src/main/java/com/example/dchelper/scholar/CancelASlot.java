package com.example.dchelper.scholar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class CancelASlot extends AppCompatActivity {
    DatabaseReference db;
    Button cancel;

    Bundle bundle;
    String date;
    String userStartTime;
    String userEndTime;
    String venue;
    String owner;
    String mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_aslot);
        cancel=findViewById(R.id.cancel_booked_slot);

        db=FirebaseDatabase.getInstance().getReference();

        bundle = getIntent().getExtras();
        date = bundle.getString("date");
        userStartTime = bundle.getString("start_time");
        userEndTime = bundle.getString("end_time");
        venue = bundle.getString("venue");
        owner = bundle.getString("owner");
        mode =bundle.getString("Mode");
//        String slotStartTime=bundle.getString("slot_start_time");
//        String slotEndTime=bundle.getString("slot_end_time");
//        String slot_path=bundle.getString("reference");
        Slot slot = new Slot(owner, userStartTime, userEndTime, venue, date);

        TextView b_name=findViewById(R.id.cancel_name);
        b_name.setText("Name:"+slot.getOwner());
        TextView b_venue=findViewById(R.id.cancel_venue);
        b_venue.setText("venue:"+slot.getVenue());
        TextView b_mode=findViewById(R.id.cancel_mode);
        b_mode.setText("mode:" + mode);
        TextView b_date=findViewById(R.id.cancel_date);
        b_date.setText("Date:"+slot.getDate());
        TextView b_time=findViewById(R.id.cancel_timing);
        b_time.setText("Time:"+slot.getStart_time()+"-" + slot.getEnd_time());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelSlot();
                Intent intent=new Intent(CancelASlot.this , ScholarDashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    void cancelSlot(){
        final int[] restrict = {1};
        db.child("slot")
                .child(date)
                .child(venue)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> keys=new ArrayList<>();
                        String path="";
                        String new_slotStart_time = userStartTime;
                        String new_slotEnd_time=userEndTime;
                        if(snapshot.exists()){
                            for(DataSnapshot dt:snapshot.getChildren()){
                                Slot slot=dt.getValue(Slot.class);
                                if(slot.getEnd_time().equals(userStartTime) && slot.getStatus().equals("free")){
                                    keys.add(dt.getKey());
                                    new_slotStart_time=slot.getStart_time();
                                }
                                else if(slot.getStart_time().equals(userEndTime) && slot.getStatus().equals("free")){
                                    keys.add(dt.getKey());
                                    new_slotEnd_time=slot.getEnd_time();
                                }
                                if(slot.getStatus().equals("Booked") && slot.getOwner().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
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
                                            Toast.makeText(CancelASlot.this, "cancelled", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //Add new free slot
                                db.child("slot").child(date).child(venue)
                                        .push().setValue(new Slot("free",new_slotStart_time,new_slotEnd_time,venue,date,"free"));

                                //Remove slot from scholar's upcoming event
                                db.child("scholars")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("UpComingEvent")
                                        .removeValue();
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