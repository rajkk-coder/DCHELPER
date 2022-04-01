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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class book_or_block extends AppCompatActivity {
    Button block;
    Button book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_or_block);
        block=findViewById(R.id.button4);
        book=findViewById(R.id.button3);
        Bundle bundle = getIntent().getExtras();

        String date = bundle.getString("date");
        String userStartTime = bundle.getString("stime");
        String userEndTime = bundle.getString("etime");
        String venue = bundle.getString("venue");
        String owner = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String slotStartTime=bundle.getString("slot_start_time");
        String slotEndTime=bundle.getString("slot_end_time");
        String slot_path=bundle.getString("reference");
        Slot slot = new Slot(owner, userStartTime, userEndTime, venue, date);
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
                FirebaseDatabase.getInstance().getReference()
                        .child("slot")
                        .child(date)
                        .child(venue).child(slot_path)
                        .removeValue();

                DatabaseReference myRef=FirebaseDatabase.getInstance().getReference()
                        .child("slot")
                        .child(date)
                        .child(venue);

                if(slotStartTime.equals(userStartTime)){
                    if(slotEndTime.equals(userEndTime)){
                        myRef.push().setValue(new Slot(owner,slotStartTime,slotEndTime,venue,date,"Blocked"));
                        FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getUid())
                                .child("UpComingEvent")
                                .push().setValue(new Slot(owner,slotStartTime,slotEndTime,venue,date,"Blocked"));
                    }
                    else{
                        //2 slot case
                        myRef.push().setValue(new Slot(owner,slotStartTime,userEndTime,venue,date,"Blocked"));
                        myRef.push().setValue(new Slot("free",userEndTime,slotEndTime,venue,date,"free"));
                    }
                }
                else if(slotEndTime.equals(userEndTime)){
                    //2 wala case

                    myRef.push().setValue(new Slot("free",slotStartTime,userStartTime,venue,date,"free"));
                    myRef.push().setValue(new Slot(owner,userStartTime,slotEndTime,venue,date,"Blocked"));
                }
                else{
                    //3 wala case

                    myRef.push().setValue(new Slot("free",slotStartTime,userStartTime,venue,date,"free"));

                    myRef.push().setValue(new Slot(owner,userStartTime,userEndTime,venue,date,"Blocked"));

                    myRef.push().setValue(new Slot("free",userEndTime,slotEndTime,venue,date,"free"));

                }
                Intent intent=new Intent(book_or_block.this , ScholarDashboardActivity.class);
                Toast.makeText(book_or_block.this, "block", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("slot")
                      .child(date).child(venue).child(slot_path).removeValue();

                Toast.makeText(book_or_block.this, slot_path, Toast.LENGTH_SHORT).show();

                DatabaseReference myRef=FirebaseDatabase.getInstance().getReference()
                        .child("slot")
                        .child(date)
                        .child(venue);


                if(slotStartTime.equals(userStartTime)){
                    if(slotEndTime.equals(userEndTime)){
                        myRef.push().setValue(new Slot(owner,slotStartTime,slotEndTime,venue,date,"Booked"));
                    }
                    else{
                        //2 slot case
                        myRef.push().setValue(new Slot(owner,slotStartTime,userEndTime,venue,date,"Booked"));
                        myRef.push().setValue(new Slot("free",userEndTime,slotEndTime,venue,date,"free"));
                    }
                }
                else if(slotEndTime.equals(userEndTime)){
                    //2 wala case

                    myRef.push().setValue(new Slot("free",slotStartTime,userStartTime,venue,date,"free"));
                    myRef.push().setValue(new Slot(owner,userStartTime,slotEndTime,venue,date,"Booked"));
                }
                else{
                    //3 wala case

                    myRef.push().setValue(new Slot("free",slotStartTime,userStartTime,venue,date,"free"));

                    myRef.push().setValue(new Slot(owner,userStartTime,userEndTime,venue,date,"Booked"));

                    myRef.push().setValue(new Slot("free",userEndTime,slotEndTime,venue,date,"free"));

                }
                Intent intent=new Intent(book_or_block.this , ScholarDashboardActivity.class);
                Toast.makeText(book_or_block.this, "book", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
    }