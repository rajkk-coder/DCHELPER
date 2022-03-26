package com.example.dchelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dchelper.admin.faculty.Faculty;
import com.example.dchelper.admin.faculty.FacultyListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kotlinx.coroutines.Delay;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button);
        EditText name=findViewById(R.id.f_name);
        EditText id=findViewById(R.id.f_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("facultyList");
                String f_name =name.getText().toString();
                String fid=id.getText().toString();
                if(f_name.length()==0)
                    Toast.makeText(MainActivity.this, "Faculty name missing", Toast.LENGTH_SHORT).show();
                else if(fid.length()==0)
                    Toast.makeText(MainActivity.this, "Faculty id missing", Toast.LENGTH_SHORT).show();
                else if(f_name.length()==0 && fid.length()==0)
                    Toast.makeText(MainActivity.this, "Please enter details", Toast.LENGTH_SHORT).show();
                else{
                myRef.push().setValue(new Faculty(f_name,fid)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Faculty added successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, FacultyListActivity.class));}
                    }
                });}
            }
        });
    }
}