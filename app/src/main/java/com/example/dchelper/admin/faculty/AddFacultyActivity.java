package com.example.dchelper.admin.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dchelper.MainActivity;
import com.example.dchelper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFacultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);
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
                    Toast.makeText(AddFacultyActivity.this, "Faculty name missing", Toast.LENGTH_SHORT).show();
                else if(fid.length()==0)
                    Toast.makeText(AddFacultyActivity.this, "Faculty id missing", Toast.LENGTH_SHORT).show();
                else if(f_name.length()==0 && fid.length()==0)
                    Toast.makeText(AddFacultyActivity.this, "Please enter details", Toast.LENGTH_SHORT).show();
                else{
                    myRef.push().setValue(new Faculty(f_name,fid)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddFacultyActivity.this, "Faculty added successfully", Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                },1000);
                                startActivity(new Intent(AddFacultyActivity.this, FacultyListActivity.class));
                                finish();
                            }
                        }
                    });}
            }
        });
    }
}