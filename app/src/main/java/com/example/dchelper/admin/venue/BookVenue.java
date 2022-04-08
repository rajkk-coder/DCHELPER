package com.example.dchelper.admin.venue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.dchelper.scholar.Slot;
import com.example.dchelper.scholar.chooseVenue;
import com.example.dchelper.scholar.select_time;
import com.google.android.gms.common.internal.Objects;
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

    ArrayList<Slot> slot_details=new ArrayList<Slot>();
    ArrayList<String> path=new ArrayList<String>();
    int size=0;

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

                DatabaseReference myRef= FirebaseDatabase.getInstance().getReference()
                        .child("slot")
                        .child(date)
                        .child(venue);

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            if(start_time=="08:00" && end_time=="18:00"){
                                myRef.push().setValue(new Slot("CSED Department",start_time,end_time,venue,date,"Booked"));
                            }
                            else if(start_time=="08:00"){
                                myRef.push().setValue(new Slot("CSED Department",start_time,end_time,venue,date,"Booked"));
                                myRef.push().setValue(new Slot("free",end_time,"18:00",venue,date,"free"));
                            }
                            else if(end_time=="18:00"){
                                myRef.push().setValue(new Slot("free","08:00",start_time,venue,date,"free"));
                                myRef.push().setValue(new Slot("CSED Department",start_time,end_time,venue,date,"Booked"));
                            }
                            else if(start_time!="08:00" && end_time!="18:00"){
                                myRef.push().setValue(new Slot("free","08:00",start_time,venue,date,"free"));
                                myRef.push().setValue(new Slot("CSED Department",start_time,end_time,venue,date,"Booked"));
                                myRef.push().setValue(new Slot("free",end_time,"18:00",venue,date,"free"));
                            }
                        }
                        else{
                            for(DataSnapshot item:snapshot.getChildren()){
                                slot_details.add(item.getValue(Slot.class));
                                path.add(item.getKey());
                                size++;
                            }
                            getslot();

                        }
                    }

                    private void getslot() {
                        int temp=0;
                        for(int i=0;i<size;i++){
                            if(cmp(slot_details.get(i).getStart_time(), start_time) && cmp(end_time,slot_details.get(i).getEnd_time()) && (slot_details.get(i).getStatus().equals("free"))){
                                if(slot_details.get(i).getStart_time()==start_time && end_time== slot_details.get(i).getEnd_time()){
                                    myRef.child(path.get(i)).removeValue();
                                    myRef.push().setValue(new Slot("CSED Department",start_time,end_time,venue,date,"Booked"));
                                    temp=1;
                                    break;
                                }
                                else if(slot_details.get(i).getStart_time()==start_time ){
                                    myRef.child(path.get(i)).removeValue();
                                    myRef.push().setValue(new Slot("CSED Department",start_time,end_time,venue,date,"Booked"));
                                    myRef.push().setValue(new Slot("free",end_time,slot_details.get(i).getEnd_time(),venue,date,"free"));
                                    temp=1;
                                    break;
                                }
                                else if(slot_details.get(i).getEnd_time()==end_time){
                                    myRef.child(path.get(i)).removeValue();
                                    myRef.push().setValue(new Slot("free",slot_details.get(i).getStart_time(),start_time,venue,date,"free"));
                                    myRef.push().setValue(new Slot("CSED Department",start_time,end_time,venue,date,"Booked"));
                                    temp=1;
                                    break;
                                }
                                else{
                                    myRef.child(path.get(i)).removeValue();
                                    myRef.push().setValue(new Slot("free",slot_details.get(i).getStart_time(),start_time,venue,date,"free"));
                                    myRef.push().setValue(new Slot("CSED Department",start_time,end_time,venue,date,"Booked"));
                                    myRef.push().setValue(new Slot("free",end_time,slot_details.get(i).getEnd_time(),venue,date,"free"));
                                    temp=1;
                                    break;
                                }
                            }
                        }
                        if(temp==0){
                            Toast.makeText(BookVenue.this, "Cant override Bookings", Toast.LENGTH_SHORT).show();
                        }
                    }

                    private boolean cmp(String a, String b) {
                        String[] token1=a.split(":");
                        String[] token2=b.split(":");
                        int x=Integer.parseInt(token1[0])*60+Integer.parseInt(token1[1]);
                        int y=Integer.parseInt(token2[0])*60+Integer.parseInt(token2[1]);
                        if(x<=y) return true;
                        else return false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent intent=new Intent(BookVenue.this, AdminDashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}