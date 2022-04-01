package com.example.dchelper.scholar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.example.dchelper.admin.venue.Venue;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class GetDateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<SlotAdapter>slotAdapter;
    String venue="Aryabhat";
    Spinner mySpinner;
    ValueEventListener listener;
    ArrayAdapter<String> adapter;
    String NoOfDates;
    ArrayList<String> spinnerDataList;
    DatabaseReference databaseReference;
    List<Date> dates = new ArrayList<>();
    List<String> req_for_date=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slots);

        //Retrieve from bundle
        Bundle bundle=getIntent().getExtras();
        NoOfDates = bundle.getString("noOfDates");
        String start_date = bundle.getString("sdate");
        String end_date = bundle.getString("edate");
        slotAdapter=new ArrayList<>(Integer.parseInt(NoOfDates));

        Toast.makeText(this, NoOfDates, Toast.LENGTH_SHORT).show();
        //Retrieve from bundle

        //Spinner
        LinearLayout layout = (LinearLayout) findViewById(R.id.scroll_dates);
        databaseReference=FirebaseDatabase.getInstance().getReference("venueList");
        spinnerDataList=new ArrayList<>();
        adapter=new ArrayAdapter<String>(GetDateActivity.this, android.R.layout.simple_spinner_dropdown_item,spinnerDataList);

        mySpinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(GetDateActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));

        mySpinner.setAdapter(adapter);
        retrieveDatavenue();
        Toast.makeText(this, "values in adapter"+adapter.getCount(), Toast.LENGTH_SHORT).show();
        mySpinner.setOnItemSelectedListener(this);
        //Spinner

        try {
            String[] token1 = start_date.split("/");
            String[] token2 = end_date.split("/");
            String new_start_date = token1[1] + "/" + token1[0] + "/" + token1[2];
            String new_end_date = token2[1] + "/" + token2[0] + "/" + token2[2];
            SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
            Date date1;
            Date date2;
            date1 = date.parse(new_start_date);
            date2 = date.parse(new_end_date);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date1);

            while (calendar.getTime().before(date2)) {
                Date result = calendar.getTime();
                dates.add(result);
                calendar.add(Calendar.DATE, 1);
            }
            Date result = calendar.getTime();
            dates.add(result);
        }catch (Exception e){
            Toast.makeText(this, "incorrect", Toast.LENGTH_SHORT).show();
        }

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        for(int i=0;i<Integer.parseInt(NoOfDates)+1;i++){
            req_for_date.add(DATE_FORMAT.format(dates.get(i)));
        }





        //Horizontal Scrollbar
        FrameLayout layout2=(FrameLayout) findViewById(R.id.xyz);

       retrieveData();
        List<Button> list = new ArrayList<>();
        for(int i = 0; i<Integer.parseInt(NoOfDates)+1 ; i++) {
            //create the button
            Button btn = new Button(this);
            btn.setText(req_for_date.get(i));
            btn.setId(i+1);
            layout.addView(btn);
            list.add(btn);
        }
        int i=0;
        for(Button btn: list){

            RecyclerView temp = new RecyclerView(this);
            temp.setLayoutManager(new LinearLayoutManager(this));
            temp.setAdapter(slotAdapter.get(i));

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(GetDateActivity.this, venue, Toast.LENGTH_SHORT).show();
                    layout2.removeViewAt(0);
                    layout2.addView(temp);
//                    purana[0] =temp;
                }
            });
            i++;
        }
        //Horizontal Scrollbar

    }

    private void retrieveData() {
        Query query=null;
        for(int i = 0;i<Integer.parseInt(NoOfDates)+1;i++){
            DatabaseReference myRef=FirebaseDatabase.getInstance().getReference()
                    .child("slot").child(req_for_date.get(i)).child(venue);
            query=myRef.orderByChild("start_time");
            String str=req_for_date.get(i);
            final int[] lauda = {0};
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists() && lauda[0] ==0){
                        FirebaseDatabase.getInstance().getReference()
                        .child("slot")
                        .child(str)
                        .child(venue)
                        .push().setValue(new Slot("free","08:00","18:00",venue,str,"free"));
                        lauda[0]=1;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

            //query.get()
            FirebaseRecyclerOptions<Slot> options =
                    new FirebaseRecyclerOptions.Builder<Slot>()
                            .setQuery(query, Slot.class)
                            .build();
//
//            if (options == null) {
//
//
//                options =
//                        new FirebaseRecyclerOptions.Builder<Slot>()
//                                .setQuery(query, Slot.class)
//                                .build();
//            }
            slotAdapter.add(new SlotAdapter(options));
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        for (int i=0;i<Integer.parseInt(NoOfDates)+1;i++)
            slotAdapter.get(i).stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (int i=0;i<Integer.parseInt(NoOfDates)+1;i++)
            slotAdapter.get(i).startListening();
    }

    public void retrieveDatavenue(){
        listener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item:snapshot.getChildren()){
                    spinnerDataList.add(item.getValue(Venue.class).getName());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        venue=""+adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}