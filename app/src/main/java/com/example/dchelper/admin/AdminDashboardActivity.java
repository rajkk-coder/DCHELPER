package com.example.dchelper.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dchelper.R;
import com.example.dchelper.admin.faculty.FacultyListActivity;
import com.example.dchelper.admin.venue.VenueListActivity;

public class AdminDashboardActivity extends AppCompatActivity {
    androidx.constraintlayout.widget.ConstraintLayout btn1, btn2;
    private long backPressedTime;
    private Toast backToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        btn1=findViewById(R.id.manage_faculty);
        btn2=findViewById(R.id.manage_venue);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboardActivity.this, FacultyListActivity.class));
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboardActivity.this, VenueListActivity.class));
            }
        });
    }
    @Override
    public void onBackPressed(){
        if(backPressedTime+2000>System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            finish();
        }else {
            backToast=Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime=System.currentTimeMillis();
    }
}