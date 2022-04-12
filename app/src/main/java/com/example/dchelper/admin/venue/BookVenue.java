package com.example.dchelper.admin.venue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dchelper.R;
import com.example.dchelper.admin.AdminDashboardActivity;
import com.example.dchelper.scholar.booking.Slot;
import com.example.dchelper.scholar.booking.book_or_block;
import com.example.dchelper.scholar.homePage.ScholarDashboardActivity;
import com.example.dchelper.scholar.panelMembers.PanelMember;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class BookVenue extends AppCompatActivity {

    EditText admin_date;
    EditText admin_start_time;
    EditText admin_end_time;
    TextView admin_venue;
    ImageView img1;
    ImageView img2;
    ImageView img3;

    String date="";
    String start_time="";
    String end_time="";
    String venue="";
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_venue);

        Bundle bundle = getIntent().getExtras();
        venue=bundle.getString("venue");

        admin_date=findViewById(R.id.date_calender);
        admin_start_time=findViewById(R.id.admin_start_time);
        admin_end_time=findViewById(R.id.admin_end_time);
        admin_venue=findViewById(R.id.venue_name);
        img1=findViewById(R.id.imageButton3);
        img2=findViewById(R.id.imageButton4);
        img3=findViewById(R.id.imageButton5);
        alertDialog=new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Processing");
        alertDialog.setMessage("Please wait..");
        admin_venue.setText(venue);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calender=Calendar.getInstance();
                int year= calender.get(Calendar.YEAR);
                int month=calender.get(Calendar.MONTH);
                int day=calender.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(BookVenue.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        if(day<10) date="0"+day;
                        else date=""+day;
                        if(month<10) date+="-0"+month;
                        else date += "-"+month;
                        date+="-"+year;
                        admin_date.setText(date);
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int hrs=calendar.get(Calendar.HOUR);
                int mn=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(BookVenue.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        if(hour<10) start_time="0" + hour;
                        else start_time=""+hour;
                        if(minute<10) start_time+=":0"+minute;
                        else start_time+=":"+minute;
                        admin_start_time.setText(start_time);
                    }
                },hrs,mn,true);
                timePickerDialog.show();
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int hrs=calendar.get(Calendar.HOUR);
                int mn=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(BookVenue.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        if(hour<10) end_time="0" + hour;
                        else end_time=""+hour;
                        if(minute<10) end_time+=":0"+minute;
                        else end_time+=":"+minute;
                        admin_end_time.setText(end_time);
                    }
                },hrs,mn,true);
                timePickerDialog.show();
            }
        });

        Button btn=findViewById(R.id.admin_book);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                alertDialog.show();
                Slot slot=new Slot("CSED Department",admin_start_time.getText().toString(),admin_end_time.getText().toString(),venue,admin_date.getText().toString(),"Booked");
                if(admin_date!=null && admin_start_time!=null && admin_end_time!=null){
                    boolean[] check={false};
                    FirebaseDatabase.getInstance().getReference()
                            .child("slot").child(slot.getDate()).child(slot.getVenue()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data:snapshot.getChildren()){
                                Slot slot1=data.getValue(Slot.class);
                                assert slot1 != null;
                                String s1= slot1.getStart_time();
                                String s2= slot1.getEnd_time();
                                String[] token1=s1.split(":");
                                String[] token2=s2.split(":");
                                int ss1 = Integer.parseInt(token1[0])*60+Integer.parseInt(token1[1]);
                                int ss2 = Integer.parseInt(token2[0])*60+Integer.parseInt(token2[1]);

                                String v1= slot.getStart_time();
                                String v2= slot.getEnd_time();
                                String[] token3=v1.split(":");
                                String[] token4=v2.split(":");
                                int vv1 = Integer.parseInt(token3[0])*60+Integer.parseInt(token3[1]);
                                int vv2 = Integer.parseInt(token4[0])*60+Integer.parseInt(token4[1]);
                                if(ss1 < vv2 && ss2 > vv1 ){
                                    check[0] =true;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(BookVenue.this, "Something went wrong. Please try again!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.hide();
                            if(!check[0]){
                                //add values to slot table
                                FirebaseDatabase.getInstance().getReference()
                                        .child("slot").child(slot.getDate()).child(slot.getVenue()).push()
                                        .setValue(slot).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(BookVenue.this, "Successfully booked", Toast.LENGTH_SHORT).show();

                                        }
                                        else {
                                            Toast.makeText(BookVenue.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                                        }
                                        Intent intent=new Intent(BookVenue.this,AdminDashboardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                });
                                }
                            else {
                                Toast.makeText(BookVenue.this, "Someone has booked/blocked it!!!", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(BookVenue.this,AdminDashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    },1000);
                }else {
                    Toast.makeText(BookVenue.this, "Please fill the options", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}