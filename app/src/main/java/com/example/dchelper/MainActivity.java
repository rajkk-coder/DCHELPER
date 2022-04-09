package com.example.dchelper;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.admin.faculty.Faculty;
import com.example.dchelper.scholar.booking.Slot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btn;
    ArrayList<Faculty>faculties;
    ArrayList<Slot>slots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.demo_on_main);
        btn=findViewById(R.id.demo_btn);
        slots=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot data:snapshot.getChildren()){
                            faculties.add(data.getValue(Faculty.class));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        FirebaseDatabase.getInstance().getReference()
                .child("slot")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       long count= snapshot.getChildrenCount();
                       if(count!=0){
                           ArrayList<Slot>temp=new ArrayList<>();
                           for (DataSnapshot data:snapshot.getChildren()){
                               temp.add(data.getValue(Slot.class));
                           }



                           //slots=findFreeSlot(temp, faculties);
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private ArrayList<Slot> findFreeSlot(ArrayList<Slot> temp, ArrayList<Slot> timings) {
        ArrayList<Slot> allSlots = new ArrayList<>();
        int i=0,j=0;
        while(i<temp.size() && j<timings.size()) {
            String s1= temp.get(i).getStart_time();
            String s2= timings.get(j).getStart_time();
            String[] token1=s1.split(":");
            String[] token2=s2.split(":");
            int ss1 = Integer.parseInt(token1[0])*60+Integer.parseInt(token1[1]);
            int ss2 = Integer.parseInt(token2[0])*60+Integer.parseInt(token2[1]);
            if(ss1 <= ss2) {
                allSlots.add(temp.get(i));
                i++;
            }
            else {
                allSlots.add(timings.get(j));
                j++;
            }
        }
        while(i<temp.size()) {
            allSlots.add(temp.get(i));
            i++;
        }
        while(j<timings.size()) {
            allSlots.add(timings.get(j));
            j++;
        }

        int index = 0;
        ArrayList<Slot> mergeSlots = allSlots;

        for (i=1; i<mergeSlots.size(); i++)
        {
            String s1= mergeSlots.get(index).getEnd_time();
            String s2= mergeSlots.get(i).getStart_time();
            String s3= mergeSlots.get(i).getEnd_time();
            String[] token1=s1.split(":");
            String[] token2=s2.split(":");
            String[] token3=s3.split(":");
            int ss1 = Integer.parseInt(token1[0])*60+Integer.parseInt(token1[1]);
            int ss2 = Integer.parseInt(token2[0])*60+Integer.parseInt(token2[1]);
            int ss3 = Integer.parseInt(token3[0])*60+Integer.parseInt(token3[1]);
            int end = 0;
            if (ss1 >= ss2 )
            {
                end = Math.max(ss1, ss3);
                int hr = end/60,minute= end%60;
                mergeSlots.get(index).setEnd_time(Integer.toString(hr) + ":" + Integer.toString(minute) );
            }
            else {
                index++;
                mergeSlots.add(index,mergeSlots.get(i));
            }
        }
        int startTime = 8*60, endTime= 18*60;
        for(i = 0; i<=index; i++) {
            String s1= mergeSlots.get(i).getStart_time();
            String s2= mergeSlots.get(i).getEnd_time();
            String[] token1=s1.split(":");
            String[] token2=s2.split(":");
            int ss1 = Integer.parseInt(token1[0])*60+Integer.parseInt(token1[1]);
            int ss2 = Integer.parseInt(token2[0])*60+Integer.parseInt(token2[1]);
            String slotStartTime = Integer.toString(startTime/60) + ":" + Integer.toString(startTime%60);
            String slotEndTime = Integer.toString(ss1/60) + ":" + Integer.toString(ss1%60);
            Slot freeSlot = new Slot("Koi nhi h",slotStartTime,slotEndTime,"mila nhi","aaj ka","Available");
            allSlots.add(freeSlot);
            startTime = ss2;
        }

        String slotStartTime = Integer.toString(startTime/60) + ":" + Integer.toString(startTime%60);
        String slotEndTime = Integer.toString(endTime/60) + ":" + Integer.toString(endTime%60);
        Slot freeSlot = new Slot("Koi nhi h",slotStartTime,slotEndTime,"mila nhi","aaj ka","Available");
        allSlots.add(freeSlot);

        Collections.sort(allSlots);

        return allSlots;
    }

}

