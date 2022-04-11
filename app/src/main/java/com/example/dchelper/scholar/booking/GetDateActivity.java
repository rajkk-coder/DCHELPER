package com.example.dchelper.scholar.booking;

import static java.lang.Math.max;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.example.dchelper.scholar.adapters.SlotAdapter;
import com.example.dchelper.scholar.panelMembers.PanelMember;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class GetDateActivity extends AppCompatActivity {

    ArrayList<SlotAdapter>slotAdapter;
    String venue;
    String NoOfDates;
    String mode;
    List<Date> dates = new ArrayList<>();
    List<String> req_for_date=new ArrayList<>();
    DatabaseReference db;
    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<Slot>slots=new ArrayList<>();
    AlertDialog alertDialog;
    ProgressBar progressBar;

    List<Button> list;
    LinearLayout layout;
    FrameLayout layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slots);

        //Retrieve from bundle
        Bundle bundle=getIntent().getExtras();
        NoOfDates = bundle.getString("noOfDates");
        String start_date = bundle.getString("sdate");
        String end_date = bundle.getString("edate");
        venue=bundle.getString("venue");
        mode=bundle.getString("mode");
        slotAdapter=new ArrayList<>(Integer.parseInt(NoOfDates));
        db=FirebaseDatabase.getInstance().getReference();
        alertDialog=new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Loading");
        alertDialog.setMessage("Please wait..");
        progressBar=findViewById(R.id.loading);
        progressBar.setVisibility(View.GONE);
        layout = (LinearLayout) findViewById(R.id.scroll_dates);
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
        layout2=(FrameLayout) findViewById(R.id.xyz);
        list = new ArrayList<>();
        for(int i = 0; i<Integer.parseInt(NoOfDates)+1 ; i++) {
            //create the button
            Button btn = new Button(this);
            btn.setText(req_for_date.get(i));
            btn.setId(i+1);
            layout.addView(btn);
            list.add(btn);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                disperse();
            }
        },2500);
    }

    private void disperse() {
        int i=0;
        for(Button btn: list){

            RecyclerView temp = new RecyclerView(this);
            temp.setLayoutManager(new LinearLayoutManager(this));
            temp.setAdapter(slotAdapter.get(i));

            btn.setOnClickListener(view -> {

                layout2.removeViewAt(0);
                layout2.addView(temp);
//                    purana[0] =temp;
            });
            i++;
        }
    }

    private void retrieveData() {
        alertDialog.show();
        for(int i = 0;i<Integer.parseInt(NoOfDates)+1;i++){
            int l=i;
            db.child("slot")
                    .child(req_for_date.get(i))
                    .child(venue)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<Slot> slots1=new ArrayList<>();
                            for(DataSnapshot data:snapshot.getChildren()){
                                slots1.add(data.getValue(Slot.class));
                            }
                            ArrayList<Slot>fna=new ArrayList<>();
                            db.child("scholars").child(user.getUid()).child("PanelMember").child(mode).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dt:snapshot.getChildren()){
                                        PanelMember panelMember=dt.getValue(PanelMember.class);
                                        db.child("FNA").child(panelMember.getFacultyName()).child(req_for_date.get(l)).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot data:snapshot.getChildren()){
                                                    fna.add(data.getValue(Slot.class));
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertDialog.hide();
                                    ArrayList<Slot>slt=findFreeSlot(slots1,fna,l);
                                }
                            },1000);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<Integer.parseInt(NoOfDates)+1;i++)
                    slotAdapter.get(i).notifyDataSetChanged();
            }
        },2500);
//
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveData();
    }

    private ArrayList<Slot> retrieveFacultyNotAvailability(String date) {
        ArrayList<Slot>fna=new ArrayList<>();
        db.child("scholars").child(user.getUid()).child("PanelMember").child(mode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dt:snapshot.getChildren()){
                    PanelMember panelMember=dt.getValue(PanelMember.class);
                    db.child("FNA").child(panelMember.getFacultyName()).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data:snapshot.getChildren())
                                fna.add(data.getValue(Slot.class));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return fna;
    }

    private ArrayList<Slot> findFreeSlot(ArrayList<Slot> temp, ArrayList<Slot> timings, int m) {
        ArrayList<Slot> allSlots = new ArrayList<>();
        Collections.sort(timings);
        int index = 0,n=timings.size();
        for (int i=1; i<n; i++)
        {
            String s1= timings.get(i).getStart_time();
            String s2= timings.get(i).getEnd_time();
            String s3= timings.get(index).getEnd_time();
            String[] token1=s1.split(":");
            String[] token2=s2.split(":");
            String[] token3=s3.split(":");
            int ss1 = Integer.parseInt(token1[0])*60+Integer.parseInt(token1[1]);
            int ss2 = Integer.parseInt(token2[0])*60+Integer.parseInt(token2[1]);
            int ss3 = Integer.parseInt(token3[0])*60+Integer.parseInt(token3[1]);
            if (ss3 >=  ss1)
            {
                int val=max(ss3, ss2);
                 timings.get(index).setEnd_time(Integer.toString(val / 60) + ":" + Integer.toString(val % 60));
            }
            else {
                index++;
                timings.add(index,timings.get(i));
            }
        }
        int i=0,j=0;
        if(timings.size()==0) index=-1;
        while(i<temp.size() && j<=index) {
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
        while(j<=index) {
            allSlots.add(timings.get(j));
            j++;
        }

        int startTime = 8*60, endTime= 18*60;
        int sz=allSlots.size();
        for (int it=0; it<sz; it++)
        {
            String s2= allSlots.get(it).getStart_time();
            String s3= allSlots.get(it).getEnd_time();
            String[] token2=s2.split(":");
            String[] token3=s3.split(":");
            int ss2 = Integer.parseInt(token2[0])*60+Integer.parseInt(token2[1]);
            int ss3 = Integer.parseInt(token3[0])*60+Integer.parseInt(token3[1]);
            if (startTime < ss2 ){

                String slotStartTime =Integer.toString(startTime / 60);
                if(startTime/60 < 10) {
                    slotStartTime = '0'+slotStartTime;
                }
                String slotEndTime =Integer.toString(ss2 / 60);
                if(ss2/60 < 10) {
                    slotEndTime = '0'+slotEndTime;
                }
                String using =Integer.toString(startTime % 60);
                if(startTime%60 < 10) {
                    using = '0'+using;
                }
                String using1 =Integer.toString(ss2 % 60);
                if(ss2%60 < 10) {
                    using1 = '0'+using1;
                }
                slotStartTime = slotStartTime + ":" + using;
                slotEndTime = slotEndTime + ":" + using1;
                Slot freeSlot = new Slot("Available", slotStartTime, slotEndTime, venue, req_for_date.get(m), "free");
                allSlots.add(freeSlot);
            }
            startTime=max(startTime,ss3);
        }
        if(startTime<endTime) {

            String slotStartTime =Integer.toString(startTime / 60);
            if(startTime/60 < 10) {
                slotStartTime = '0'+slotStartTime;
            }
            String slotEndTime =Integer.toString(endTime / 60);
            if(endTime/60 < 10) {
                slotEndTime = '0'+slotEndTime;
            }
            String using =Integer.toString(startTime % 60);
            if(startTime%60 < 10) {
                using = '0'+using;
            }
            String using1 =Integer.toString(endTime % 60);
            if(endTime%60 < 10) {
                using1 = '0'+using1;
            }
            slotStartTime = slotStartTime + ":" + using;
            slotEndTime = slotEndTime + ":" + using1;

            Slot freeSlot = new Slot("Available", slotStartTime, slotEndTime, venue, req_for_date.get(m), "free");
            allSlots.add(freeSlot);
        }
        Collections.sort(allSlots);
        slots=allSlots;
        slotAdapter.add(new SlotAdapter(GetDateActivity.this,allSlots,mode));
       return allSlots;
    }

}