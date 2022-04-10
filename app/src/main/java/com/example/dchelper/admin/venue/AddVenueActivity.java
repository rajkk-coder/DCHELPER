package com.example.dchelper.admin.venue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dchelper.MainActivity;
import com.example.dchelper.R;
import com.example.dchelper.admin.AdminDashboardActivity;
import com.example.dchelper.admin.faculty.Faculty;
import com.example.dchelper.admin.faculty.FacultyListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddVenueActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, VenueListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        super.onBackPressed();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue);
        Button button=findViewById(R.id.button);
        EditText name=findViewById(R.id.v_name);
        EditText id=findViewById(R.id.v_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("venueList");
                String f_name =name.getText().toString();
                String fid=id.getText().toString();
                AlertDialog alertDialog=new AlertDialog.Builder(AddVenueActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Please wait...");
                if(f_name.length()==0)
                    Toast.makeText(AddVenueActivity.this, "Venue name missing", Toast.LENGTH_SHORT).show();
                else if(fid.length()==0)
                    Toast.makeText(AddVenueActivity.this, "Venue id missing", Toast.LENGTH_SHORT).show();
                else if(f_name.length()==0 && fid.length()==0)
                    Toast.makeText(AddVenueActivity.this, "Please enter details", Toast.LENGTH_SHORT).show();
                else{
                    alertDialog.show();
                    myRef.push().setValue(new Faculty(f_name,fid)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddVenueActivity.this, "Venue added successfully", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                finish();
                            }

                        }
                    });
                }
            }
        });
    }
}