package com.example.dchelper.scholar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dchelper.R;
import com.example.dchelper.admin.venue.Venue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class chooseVenue extends AppCompatActivity {

    ArrayList<String> venueList= new ArrayList<>();
    int size =0;

    String NoOfDates;
    String start_date;
    String end_date;
    String mode;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_venue);
        layout=(LinearLayout) findViewById(R.id.choose_venues);
        Bundle bundle=getIntent().getExtras();
        NoOfDates = bundle.getString("noOfDates");
        start_date = bundle.getString("sdate");
        end_date = bundle.getString("edate");
        mode=bundle.getString("mode");
        retrieveDatavenue();

    }

    public void retrieveDatavenue(){
        FirebaseDatabase.getInstance().getReference("venueList")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            venueList.add(item.getValue(Venue.class).getName());
                            size++;
                        }
                        getbutton();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                    private void getbutton() {
                        List<Button> list = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            //create the button
                            Button btn = new Button(chooseVenue.this);
                            btn.setText(venueList.get(i));
                            btn.setId(i + 1);
                            layout.addView(btn);
                            list.add(btn);
                        }
                        for (Button btn : list) {
                            btn.setOnClickListener(view -> {
                                Intent intent = new Intent(chooseVenue.this, GetDateActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("venue", "" + btn.getText());
                                bundle.putString("noOfDates", NoOfDates);
                                bundle.putString("sdate", start_date);
                                bundle.putString("edate", end_date);
                                bundle.putString("mode", mode);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            });
                        }
                    }
                });
    }
}