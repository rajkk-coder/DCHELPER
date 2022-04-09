package com.example.dchelper;

import androidx.annotation.NonNull;

import com.example.dchelper.scholar.booking.Slot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Demo {
    void cancelSlot(){
        FirebaseDatabase.getInstance().getReference()
                .child("slot")
                .child("date_")
                .child("venue_")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String>keys=new ArrayList<>();
                        //we have start time and end time
                        //New start_time=start_time_of_cancelledSlot
                        //New end_time=end_time_of_cancelledSlot
                        if(snapshot.exists()){
                            for(DataSnapshot dt:snapshot.getChildren()){
                                Slot slot=dt.getValue(Slot.class);
                                if(slot.getEnd_time().equals("start_time_of_cancelledSlot")){
                                    keys.add(dt.getKey());
                                    //new_slotStart_time=slot.getStart_time();
                                }
                                else if(slot.getStart_time().equals("end_time_of_cancelledSlot")){
                                    keys.add(dt.getKey());
                                    //new_slotEnd_time=slot.getEnd_time();
                                }
                            }
                        }
                        for (int i=0;i<keys.size();i++)
                            FirebaseDatabase.getInstance().getReference()
                            .child("slot")
                            .child("date_")
                            .child("venue_")
                            .child(keys.get(i)).removeValue();
                        FirebaseDatabase.getInstance().getReference()
                                .child("slot")
                                .child("date_")
                                .child("venue_")
                                .push().setValue(new Slot("free","new_SlotStart_time","new_SlotEnd_time","venue_","date_","free"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
