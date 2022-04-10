package com.example.dchelper.scholar.booking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dchelper.R;
import com.example.dchelper.scholar.homePage.ScholarDashboardActivity;
import com.example.dchelper.scholar.panelMembers.PanelMember;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class book_or_block extends AppCompatActivity {
    Button block;
    Button book;
    FirebaseUser user;
    DatabaseReference db;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_or_block);
        //initialising buttons
        block=findViewById(R.id.button4);
        book=findViewById(R.id.button3);

        //creating database connection
        user=FirebaseAuth.getInstance().getCurrentUser();
        db=FirebaseDatabase.getInstance().getReference();

        //unpacking bundle
        Bundle bundle = getIntent().getExtras();
        date = bundle.getString("date");
        String userStartTime = bundle.getString("stime");
        String userEndTime = bundle.getString("etime");
        String venue = bundle.getString("venue");
        String owner = user.getDisplayName();
        String mode=bundle.getString("mode");
        Slot slot = new Slot(owner,userStartTime, userEndTime, venue, date,"fna");

        //setting text according to bundle
        TextView b_name=findViewById(R.id.book_name);
        b_name.setText("Name:"+slot.getOwner());
        TextView b_venue=findViewById(R.id.book_venue);
        b_venue.setText("venue:"+slot.getVenue());
        TextView b_mode=findViewById(R.id.book_mode);
        if(mode.equals("CE")){
            b_mode.setText("mode: Comprehensive Exam");
        }
        else{
            b_mode.setText("mode: DC Meeting");
        }

        TextView b_date=findViewById(R.id.book_date);
        b_date.setText("Date:"+slot.getDate());
        TextView b_time=findViewById(R.id.book_timing);
        b_time.setText("Time:"+slot.getStart_time()+"-" + slot.getEnd_time());

        //Event listener for block button
        book.setOnClickListener(view -> {
            final boolean[] check = {false};
            db.child("slot").child(date).child(venue).orderByChild("start_time").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data:snapshot.getChildren()){
                        Slot slot1=data.getValue(Slot.class);
                        assert slot1 != null;
                        if(slot.getStart_time().compareTo(slot1.getEnd_time()) < 0 && slot.getEnd_time().compareTo(slot1.getStart_time()) > 0){
                            check[0] =true;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(book_or_block.this, "Something went wrong..Please try again!!", Toast.LENGTH_SHORT).show();
                }
            });
            if(!check[0]){
                //add values to slot table
                db.child("slot").child(date).child(venue).push()
                        .setValue(new Slot(owner,userStartTime,userEndTime,venue,date,"Booked",mode));
                //add values to upcoming event table
                db.child("scholars").child(user.getUid()).child("UpComingEvent").push()
                        .setValue(new Slot(owner,userStartTime,userEndTime,venue,date,"Booked",mode));

                //FNA Table update
                String dat=date;
                db.child("scholars").child(user.getUid()).child("PanelMember").child(mode).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot date:snapshot.getChildren()){
                            PanelMember panelMember=date.getValue(PanelMember.class);
                            assert panelMember != null;
                            db.child("FNA").child(panelMember.getFacultyName()).child(dat)
                                    .push().setValue(slot);
                        }
                        Toast.makeText(book_or_block.this, "Booked successfully !", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(book_or_block.this,ScholarDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(book_or_block.this, "Something went wrong !!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        //onclick listener for book button
        block.setOnClickListener(new View.OnClickListener() {
            final SimpleDateFormat sdf
                    = new SimpleDateFormat(
                    "dd-MM-yyyy HH:mm:ss");
            final String currentDateAndTime = sdf.format(new Date());
            @Override
            public void onClick(View view) {
                final boolean[] check = {false};
                db.child("slot").child(date).child(venue).orderByChild("start_time").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data:snapshot.getChildren()){
                            Slot slot1=data.getValue(Slot.class);
                            assert slot1 != null;
                            if(slot.getStart_time().compareTo(slot1.getEnd_time()) < 0 && slot.getEnd_time().compareTo(slot1.getStart_time()) > 0){
                                check[0] =true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(book_or_block.this, "Something went wrong..Please try again!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                if(!check[0]){
                    //add values to slot table
                    db.child("slot").child(date).child(venue).push()
                            .setValue(new Slot(owner,userStartTime,userEndTime,venue,date,"Blocked",mode));
                    //add values to upcoming event table
                    db.child("scholars").child(user.getUid()).child("UpComingEvent").push()
                            .setValue(new Slot(owner,userStartTime,userEndTime,venue,date,"Blocked",mode,currentDateAndTime));

                    //FNA Table update
                    String dat=date;
                    db.child("scholars").child(user.getUid()).child("PanelMember").child(mode).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot date:snapshot.getChildren()){
                                PanelMember panelMember=date.getValue(PanelMember.class);
                                assert panelMember != null;
                                db.child("FNA").child(panelMember.getFacultyName()).child(dat)
                                        .push().setValue(slot).addOnCompleteListener(task -> {
                                            if(task.isSuccessful()){
                                                Intent intent=new Intent(book_or_block.this,ScholarDashboardActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(book_or_block.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
    }
}