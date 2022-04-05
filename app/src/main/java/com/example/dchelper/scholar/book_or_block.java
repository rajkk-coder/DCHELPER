package com.example.dchelper.scholar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dchelper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class book_or_block extends AppCompatActivity {
    Button block;
    Button book;
    FirebaseUser user;
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_or_block);
        block=findViewById(R.id.button4);
        book=findViewById(R.id.button3);

        user=FirebaseAuth.getInstance().getCurrentUser();
        db=FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();

        String date = bundle.getString("date");
        String userStartTime = bundle.getString("stime");
        String userEndTime = bundle.getString("etime");
        String venue = bundle.getString("venue");
        String owner = user.getDisplayName();
        String slotStartTime=bundle.getString("slot_start_time");
        String slotEndTime=bundle.getString("slot_end_time");
        String slot_path=bundle.getString("reference");
        Slot slot = new Slot(owner,userStartTime, userEndTime, venue, date,"book");
        Toast.makeText(this, "not null", Toast.LENGTH_SHORT).show();

        TextView b_name=findViewById(R.id.book_name);
        b_name.setText("Name:"+slot.getOwner());
        TextView b_venue=findViewById(R.id.book_venue);
        b_venue.setText("venue:"+slot.getVenue());
        TextView b_mode=findViewById(R.id.book_mode);
        b_mode.setText("mode:DC");
        TextView b_date=findViewById(R.id.book_date);
        b_date.setText("Date:"+slot.getDate());
        TextView b_time=findViewById(R.id.book_timing);
        b_time.setText("Time:"+slot.getStart_time()+"-" + slot.getEnd_time());


        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.child("slot")
                        .child(date)
                        .child(venue).child(slot_path)
                        .removeValue();

                DatabaseReference myRef=db
                        .child("slot")
                        .child(date)
                        .child(venue);

                if(slotStartTime.equals(userStartTime)){
                    if(slotEndTime.equals(userEndTime)){
                        myRef.push().setValue(new Slot(owner,slotStartTime,slotEndTime,venue,date,"Blocked"));
                        db.child("scholars").child(user.getUid())
                                .child("UpComingEvent")
                                .push().setValue(new Slot(owner,slotStartTime,slotEndTime,venue,date,"Blocked"));
                    }
                    else{
                        //2 slot case
                        myRef.push().setValue(new Slot(owner,slotStartTime,userEndTime,venue,date,"Blocked"));
                        myRef.push().setValue(new Slot("free",userEndTime,slotEndTime,venue,date,"free"));
                        db.child("scholars").child(user.getUid()).child("UpcomingEvent")
                                .push().setValue(new Slot(owner,slotStartTime,userEndTime,venue,date,"Blocked"));
                    }
                }
                else if(slotEndTime.equals(userEndTime)){
                    //2 wala case

                    myRef.push().setValue(new Slot("free",slotStartTime,userStartTime,venue,date,"free"));
                    myRef.push().setValue(new Slot(owner,userStartTime,slotEndTime,venue,date,"Blocked"));
                    db.child("scholars").child(user.getUid())
                            .child("UpComingEvent")
                            .push().setValue(new Slot(owner,userStartTime,slotEndTime,venue,date,"Blocked"));
                }
                else{
                    //3 wala case

                    myRef.push().setValue(new Slot("free",slotStartTime,userStartTime,venue,date,"free"));

                    myRef.push().setValue(new Slot(owner,userStartTime,userEndTime,venue,date,"Blocked"));

                    myRef.push().setValue(new Slot("free",userEndTime,slotEndTime,venue,date,"free"));

                    db.child("scholars").child(user.getUid())
                            .child("UpComingEvent")
                            .push().setValue(new Slot(owner,userStartTime,userEndTime,venue,date,"Blocked"));

                }
                Intent intent=new Intent(book_or_block.this , ScholarDashboardActivity.class);
                Toast.makeText(book_or_block.this, "Successflly blocked !!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.child("slot")
                        .child(date).child(venue).child(slot_path).removeValue();

                DatabaseReference myRef=db
                        .child("slot")
                        .child(date)
                        .child(venue);


                if(slotStartTime.equals(userStartTime)){
                    if(slotEndTime.equals(userEndTime)){
                        myRef.push().setValue(new Slot(owner,slotStartTime,slotEndTime,venue,date,"Booked"));
                        db.child("scholars").child(user.getUid())
                                .child("UpComingEvent")
                                .push().setValue(new Slot(owner,slotStartTime,slotEndTime,venue,date,"Booked"));
                    }
                    else{
                        //2 slot case
                        myRef.push().setValue(new Slot(owner,slotStartTime,userEndTime,venue,date,"Booked"));
                        myRef.push().setValue(new Slot("free",userEndTime,slotEndTime,venue,date,"free"));
                        db.child("scholars").child(user.getUid())
                                .child("UpComingEvent")
                                .push().setValue(new Slot(owner,slotStartTime,userEndTime,venue,date,"Booked"));

                    }
                }
                else if(slotEndTime.equals(userEndTime)){
                    //2 wala case

                    myRef.push().setValue(new Slot("free",slotStartTime,userStartTime,venue,date,"free"));
                    myRef.push().setValue(new Slot(owner,userStartTime,slotEndTime,venue,date,"Booked"));
                    db.child("scholars").child(user.getUid())
                            .child("UpComingEvent")
                            .push().setValue(new Slot(owner,userStartTime,slotEndTime,venue,date,"Booked"));
                }
                else{
                    //3 wala case

                    myRef.push().setValue(new Slot("free",slotStartTime,userStartTime,venue,date,"free"));

                    myRef.push().setValue(new Slot(owner,userStartTime,userEndTime,venue,date,"Booked"));

                    myRef.push().setValue(new Slot("free",userEndTime,slotEndTime,venue,date,"free"));

                    db.child("scholars").child(user.getUid())
                            .child("UpComingEvent")
                            .push().setValue(new Slot(owner,userStartTime,userEndTime,venue,date,"Booked"));
                }
                Intent intent=new Intent(book_or_block.this , ScholarDashboardActivity.class);
                Toast.makeText(book_or_block.this, "successfully booked !!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}