package com.example.dchelper.scholar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class GetDateActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SlotAdapter slotAdapter;
    //ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slots);
        LinearLayout layout = (LinearLayout) findViewById(R.id.scroll_dates);

        //Fetching data from the Firebase
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("slot").child("12-03-2022")
                .child("Aryabhat");
        Query query = myRef.orderByKey();
        FirebaseRecyclerOptions<Slot> options =
                new FirebaseRecyclerOptions.Builder<Slot>()
                        .setQuery(query, Slot.class)
                        .build();


        recyclerView = findViewById(R.id.slot_display);
        recyclerView.setLayoutManager(new LinearLayoutManager(GetDateActivity.this));



        slotAdapter = new SlotAdapter(options);
        recyclerView.setAdapter(slotAdapter);

        for(int i = 0; i<15 ; i++) {

            //create the button
            Button btn = new Button(this);

            //set all your button attributes, like text color,background color etc. here
            btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            btn.setText(""+i);

            //Set onClickListener
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(GetDateActivity.this, btn.getText(), Toast.LENGTH_SHORT).show();
                    // define this method in your activity
//                    onBtnClick(i);

                    //Sending data to the RecyclerView to display
                }
            });

            //add the button to your linear layout
            layout.addView(btn);
        }


//        progressbar=findViewById(R.id.progress_bar);
//        progressbar.setProgress(50);
//        progressbar.setMax(100);

    }
    @Override
    protected void onStop() {
        super.onStop();
        slotAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        slotAdapter.startListening();
    }
}