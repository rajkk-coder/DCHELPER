package com.example.dchelper.scholar.homePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dchelper.R;
import com.example.dchelper.scholar.booking.Slot;
import com.example.dchelper.scholar.panelMembers.PanelMember;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    FirebaseUser user;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_aslot);
        cancel=findViewById(R.id.cancel_booked_slot);

        db=FirebaseDatabase.getInstance().getReference();
        user=FirebaseAuth.getInstance().getCurrentUser();
        alertDialog=new AlertDialog.Builder(CancelASlot.this).create();
        alertDialog.setTitle("Deleting");
        alertDialog.setMessage("Please wait....");
        bundle = getIntent().getExtras();
        date = bundle.getString("date");
        userStartTime = bundle.getString("start_time");
        userEndTime = bundle.getString("end_time");
        venue = bundle.getString("venue");
        owner = bundle.getString("owner");
        mode =bundle.getString("Mode");

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
                alertDialog.show();
                db.child("scholars").child(user.getUid())
                        .child("UpComingEvent")
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    db.child("slot").child(slot.getDate()).child(slot.getVenue())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for(DataSnapshot data:snapshot.getChildren()){
                                                        Slot slot1=data.getValue(Slot.class);
                                                        if(slot1.getStart_time().equals(slot.getStart_time()) && slot1.getEnd_time().equals(slot.getEnd_time())){
                                                            data.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        alertDialog.hide();
                                                                    }
                                                                }
                                                            });
                                                            break;
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(CancelASlot.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                                    alertDialog.dismiss();
                                                    finish();
                                                }
                                            });
                                }
                            }
                        });
                db.child("scholars").child(user.getUid()).child("PanelMember").child("DC").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data:snapshot.getChildren()){
                            PanelMember panelMember=data.getValue(PanelMember.class);
                            db.child("FNA").child(panelMember.getFacultyName()).child(slot.getDate()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot data:snapshot.getChildren()){
                                        Slot slot1=data.getValue(Slot.class);
                                        if(slot1.getOwner().equals(user.getDisplayName()))
                                            data.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    alertDialog.hide();
                                                    finish();
                                                }
                                            });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    alertDialog.hide();
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        alertDialog.hide();
                        finish();
                    }
                });

            }

        });
    }
}